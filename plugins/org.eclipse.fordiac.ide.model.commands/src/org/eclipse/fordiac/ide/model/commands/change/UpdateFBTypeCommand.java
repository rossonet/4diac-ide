/*******************************************************************************
 * Copyright (c) 2012 - 2017 AIT, fortiss GmbH, Profactor GmbH
 * 				 2018 - 2019 Johannes Kepler University
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Filip Andren, Alois Zoitl, Gerhard Ebenhofer, Monika Wenger
 *   - initial API and implementation and/or initial documentation
 *   Alois Zoitl - reworked update fb type to accept also supapps
 *   Alois Zoitl - fixed issues in maintaining FB parameters
 *******************************************************************************/
package org.eclipse.fordiac.ide.model.commands.change;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fordiac.ide.model.Palette.AdapterTypePaletteEntry;
import org.eclipse.fordiac.ide.model.Palette.FBTypePaletteEntry;
import org.eclipse.fordiac.ide.model.Palette.PaletteEntry;
import org.eclipse.fordiac.ide.model.Palette.SubApplicationTypePaletteEntry;
import org.eclipse.fordiac.ide.model.commands.create.AbstractConnectionCreateCommand;
import org.eclipse.fordiac.ide.model.commands.create.AdapterConnectionCreateCommand;
import org.eclipse.fordiac.ide.model.commands.create.DataConnectionCreateCommand;
import org.eclipse.fordiac.ide.model.commands.create.EventConnectionCreateCommand;
import org.eclipse.fordiac.ide.model.commands.delete.DeleteConnectionCommand;
import org.eclipse.fordiac.ide.model.libraryElement.AdapterDeclaration;
import org.eclipse.fordiac.ide.model.libraryElement.AdapterFB;
import org.eclipse.fordiac.ide.model.libraryElement.Connection;
import org.eclipse.fordiac.ide.model.libraryElement.Event;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetworkElement;
import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementFactory;
import org.eclipse.fordiac.ide.model.libraryElement.Resource;
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

/**
 * UpdateFBTypeCommand triggers an update of the type for an FB instance
 */
public class UpdateFBTypeCommand extends Command {

	// Helper data class for storing connection data of resource connection as the
	// connections are lost during the unmapping process
	private static class ConnData {
		private IInterfaceElement source;
		private IInterfaceElement dest;

		public ConnData(IInterfaceElement source, IInterfaceElement dest) {
			this.source = source;
			this.dest = dest;
		}
	}

	/** The FBNetworkElement which should be updated */
	private FBNetworkElement fbnElement;

	/** The updated version of the FBNetworkElement */
	private FBNetworkElement updatedElement;

	/** If not null this entry should be used for the type of the updated FB */
	private PaletteEntry entry;

	private FBNetwork network;

	private CompoundCommand deleteConnCmds = new CompoundCommand();
	private CompoundCommand connCreateCmds = new CompoundCommand();
	private CompoundCommand resourceConnCreateCmds = new CompoundCommand();

	private MapToCommand mapCmd = null;
	private UnmapCommand unmapCmd = null;

	public UpdateFBTypeCommand(FBNetworkElement fbnElement, PaletteEntry entry) {
		this.fbnElement = fbnElement;
		network = (FBNetwork) fbnElement.eContainer();
		if ((entry instanceof FBTypePaletteEntry) || (entry instanceof SubApplicationTypePaletteEntry)) {
			this.entry = entry;
		} else {
			this.entry = fbnElement.getPaletteEntry();
		}
	}

	@Override
	public boolean canExecute() {
		return (null != entry) && (null != fbnElement) && (null != network);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		Resource resource = null;
		List<ConnData> resourceConns = null; // buffer for the resource specific connections from the mapped fb that
												// needs to be recreated
		if (fbnElement.isMapped()) {
			if (network.equals(fbnElement.getResource().getFBNetwork())) {
				// this is the resource fb we need to use the opposite for a correct update
				fbnElement = fbnElement.getOpposite();
				network = fbnElement.getFbNetwork();
			}
			resource = fbnElement.getResource();
			resourceConns = getResourceCons();
			unmapCmd = new UnmapCommand(fbnElement);
			unmapCmd.execute();
		}

		// Create new FB
		copyFB();

		// Find connections which should be reconnected
		handleApplicationConnections();

		// Change name
		updatedElement.setName(fbnElement.getName());

		// Map FB
		if (resource != null) {
			mapCmd = new MapToCommand(updatedElement, resource);
			if (mapCmd.canExecute()) {
				mapCmd.execute();
				recreateResourceConns(resourceConns);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		if (unmapCmd != null) {
			unmapCmd.redo();
		}

		deleteConnCmds.redo();
		replaceFBs(fbnElement, updatedElement);
		connCreateCmds.redo();

		if (mapCmd != null) {
			mapCmd.redo();
			// after redoing the mapping for the new FB recreate the resource cons
			resourceConnCreateCmds.redo();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (mapCmd != null) {
			// before removing the copied FB remove the newly created resource connections
			resourceConnCreateCmds.undo();
			mapCmd.undo();
		}

		connCreateCmds.undo();
		replaceFBs(updatedElement, fbnElement);
		deleteConnCmds.undo();

		if (unmapCmd != null) {
			unmapCmd.undo();
		}
	}

	protected void setEntry(PaletteEntry entry) {
		this.entry = entry;
	}

	protected PaletteEntry getEntry() {
		return entry;
	}

	private void handleApplicationConnections() {
		for (Connection conn : getAllConnections(fbnElement)) {
			doReconnect(conn, findUpdatedInterfaceElement(updatedElement, fbnElement, conn.getSource()),
					findUpdatedInterfaceElement(updatedElement, fbnElement, conn.getDestination()));
		}
	}

	private static List<Connection> getAllConnections(FBNetworkElement element) {
		List<Connection> retVal = new ArrayList<>();
		for (IInterfaceElement ifEle : element.getInterface().getAllInterfaceElements()) {
			if (ifEle.isIsInput()) {
				retVal.addAll(ifEle.getInputConnections());
			} else {
				retVal.addAll(ifEle.getOutputConnections());
			}
		}
		return retVal;
	}

	private static IInterfaceElement findUpdatedInterfaceElement(FBNetworkElement copiedElement,
			FBNetworkElement orgElement, IInterfaceElement orig) {
		if (orig == null) {
			// this happens when a connection has already been reconnected,
			// i.e., a connection from an output to an input of the same FB
			return null;
		}
		IInterfaceElement retval = orig;
		if (orig.getFBNetworkElement() == orgElement) {
			// origView is an interface of the original FB => find same interface on copied
			// FB
			retval = copiedElement.getInterfaceElement(orig.getName());
		}
		return retval;
	}

	private void doReconnect(Connection oldConn, IInterfaceElement source, IInterfaceElement dest) {
		// the connection may be already in our list if source and dest are on our FB
		if (!isInDeleteConnList(oldConn)) {
			FBNetwork fbn = oldConn.getFBNetwork();
			// we have to delete the old connection in all cases
			DeleteConnectionCommand cmd = new DeleteConnectionCommand(oldConn);
			cmd.execute();
			deleteConnCmds.add(cmd);

			if ((source != null) && (dest != null)) {
				// if source or dest is null it means that an interface element is not available
				// any more
				AbstractConnectionCreateCommand dccc = createConnCreateCMD(source, fbn);
				if (null != dccc) {
					dccc.setSource(source);
					dccc.setDestination(dest);
					if (dccc.canExecute()) {
						dccc.setArrangementConstraints(oldConn.getDx1(), oldConn.getDx2(), oldConn.getDy());
						dccc.execute();
						connCreateCmds.add(dccc);
					}
				}
			}
		}
	}

	private static AbstractConnectionCreateCommand createConnCreateCMD(IInterfaceElement refIE, FBNetwork fbn) {
		AbstractConnectionCreateCommand retVal = null;
		if (refIE instanceof Event) {
			retVal = new EventConnectionCreateCommand(fbn);
		} else if (refIE instanceof AdapterDeclaration) {
			retVal = new AdapterConnectionCreateCommand(fbn);
		} else if (refIE instanceof VarDeclaration) {
			retVal = new DataConnectionCreateCommand(fbn);
		}
		return retVal;
	}

	private boolean isInDeleteConnList(Connection conn) {
		for (Object cmd : deleteConnCmds.getCommands()) {
			if (((DeleteConnectionCommand) cmd).getConnectionView().equals(conn)) {
				return true;
			}
		}
		return false;
	}

	private void replaceFBs(FBNetworkElement oldElement, FBNetworkElement newElement) {
		network.getNetworkElements().remove(oldElement);
		network.getNetworkElements().add(newElement);
	}

	private void copyFB() {
		updatedElement = createCopiedFBEntry(fbnElement);
		updatedElement.setInterface(EcoreUtil.copy(updatedElement.getType().getInterfaceList()));
		updatedElement.setName(fbnElement.getName());
		updatedElement.setX(fbnElement.getX());
		updatedElement.setY(fbnElement.getY());

		createValues();

		replaceFBs(fbnElement, updatedElement);
	}

	protected FBNetworkElement createCopiedFBEntry(FBNetworkElement srcElement) {
		FBNetworkElement copy;
		if (entry instanceof SubApplicationTypePaletteEntry) {
			copy = LibraryElementFactory.eINSTANCE.createSubApp();
		} else if (entry instanceof AdapterTypePaletteEntry) {
			copy = LibraryElementFactory.eINSTANCE.createAdapterFB();
			((AdapterFB) copy).setAdapterDecl(((AdapterFB) srcElement).getAdapterDecl());
		} else {
			copy = LibraryElementFactory.eINSTANCE.createFB();
		}
		copy.setPaletteEntry(entry);
		return copy;
	}

	private void createValues() {
		for (VarDeclaration var : updatedElement.getInterface().getInputVars()) {
			var.setValue(LibraryElementFactory.eINSTANCE.createValue());
			checkSourceParam(var);
		}
	}

	private void checkSourceParam(VarDeclaration var) {
		VarDeclaration srcVar = fbnElement.getInterface().getVariable(var.getName());
		if ((null != srcVar) && (null != srcVar.getValue()) && (null != srcVar.getValue().getValue())) {
			var.getValue().setValue(srcVar.getValue().getValue());
		}
	}

	private List<ConnData> getResourceCons() {
		List<ConnData> retVal = new ArrayList<>();
		FBNetworkElement resElement = fbnElement.getOpposite();
		for (Connection conn : getAllConnections(resElement)) {
			IInterfaceElement source = conn.getSource();
			IInterfaceElement dest = conn.getDestination();
			if (!source.getFBNetworkElement().isMapped() || !dest.getFBNetworkElement().isMapped()) {
				// one of both ends is a resourceFB therefore the connection needs to be
				// restored
				retVal.add(new ConnData(conn.getSource(), conn.getDestination()));
			} else if (((source.getFBNetworkElement() == resElement)
					&& (dest.getFBNetworkElement().getOpposite().getFbNetwork() != fbnElement.getFbNetwork()))
					|| ((dest.getFBNetworkElement() == resElement) && (source.getFBNetworkElement().getOpposite()
							.getFbNetwork() != fbnElement.getFbNetwork()))) {
				// one of both ends is a FB coming from a different fb network and therefore
				// this is also a resource specific connection
				retVal.add(new ConnData(conn.getSource(), conn.getDestination()));
			}

		}
		return retVal;
	}

	private void recreateResourceConns(List<ConnData> resourceConns) {
		FBNetworkElement orgMappedElement = unmapCmd.getMappedFBNetworkElement();
		FBNetworkElement copiedMappedElement = updatedElement.getOpposite();
		for (ConnData connData : resourceConns) {
			IInterfaceElement source = findUpdatedInterfaceElement(copiedMappedElement, orgMappedElement,
					connData.source);
			IInterfaceElement dest = findUpdatedInterfaceElement(copiedMappedElement, orgMappedElement, connData.dest);
			if ((source != null) && (dest != null)) {
				// if source or dest is null it means that an interface element is not available
				// any more
				AbstractConnectionCreateCommand dccc = createConnCreateCMD(source, copiedMappedElement.getFbNetwork());
				if (null != dccc) {
					dccc.setSource(source);
					dccc.setDestination(dest);
					if (dccc.canExecute()) {
						dccc.execute();
						resourceConnCreateCmds.add(dccc);
					}
				}
			}
		}
	}

}
