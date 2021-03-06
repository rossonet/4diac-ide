/*******************************************************************************
 * Copyright (c) 2013 - 2017 Profactor GmbH, fortiss GmbH
 * 				 2019 Johannes Kepler University
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl
 *     - initial API and implementation and/or initial documentation
 *   Alois Zoitl - copying the FB type to fix issues in monitoring
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.network.viewer;

import java.util.EventObject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fordiac.ide.application.editparts.FBEditPart;
import org.eclipse.fordiac.ide.application.viewer.composite.CompositeInstanceViewerInput;
import org.eclipse.fordiac.ide.gef.DiagramEditor;
import org.eclipse.fordiac.ide.gef.FordiacContextMenuProvider;
import org.eclipse.fordiac.ide.gef.tools.AdvancedPanningSelectionTool;
import org.eclipse.fordiac.ide.model.helpers.FBNetworkHelper;
import org.eclipse.fordiac.ide.model.libraryElement.AutomationSystem;
import org.eclipse.fordiac.ide.model.libraryElement.CompositeFBType;
import org.eclipse.fordiac.ide.model.libraryElement.FB;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.INamedElement;
import org.eclipse.fordiac.ide.model.libraryElement.InterfaceList;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementFactory;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.ui.IEditorInput;

public class CompositeInstanceViewer extends DiagramEditor {

	private FB fb;
	private CompositeFBType cfbt;
	private FBEditPart fbEditPart;

	@Override
	protected EditPartFactory getEditPartFactory() {
		return new CompositeViewerEditPartFactory(this, fb, fbEditPart, getZoomManger());
	}

	@Override
	protected ContextMenuProvider getContextMenuProvider(ScrollingGraphicalViewer viewer, ZoomManager zoomManager) {
		return new FordiacContextMenuProvider(getGraphicalViewer(), zoomManager, getActionRegistry());
	}

	@Override
	protected TransferDropTargetListener createTransferDropTargetListener() {
		//
		return null;
	}

	@Override
	public AutomationSystem getSystem() {
		return null;
	}

	@Override
	public String getFileName() {
		return null;
	}

	@Override
	protected void setModel(IEditorInput input) {

		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(new AdvancedPanningSelectionTool());
		getEditDomain().setActiveTool(getEditDomain().getDefaultTool());

		if (input instanceof CompositeInstanceViewerInput) {
			CompositeInstanceViewerInput untypedInput = (CompositeInstanceViewerInput) input;
			Object content = untypedInput.getContent();
			if ((content instanceof FB) && (((FB) content).getType() instanceof CompositeFBType)) {
				fb = (FB) content;
				String name = getNameHierarchy();
				setPartName(name);
				// we need to copy the type so that we have an instance specific network
				cfbt = createFBType((CompositeFBType) fb.getType(), fb.getInterface());
				cfbt.setName(name); // we set the name of the type so internal function blocks can generate their
									// hierarchy name from it
				fbEditPart = untypedInput.getFbEditPart();
				((CompositeInstanceViewerInput) input).setName(name); // the tooltip will show the whole name when
																		// hovering
			}
		}
	}

	private static CompositeFBType createFBType(final CompositeFBType type, final InterfaceList interfaceList) {
		CompositeFBType typeCopy = LibraryElementFactory.eINSTANCE.createCompositeFBType();
		typeCopy.setInterfaceList(EcoreUtil.copy(interfaceList));
		typeCopy.setFBNetwork(FBNetworkHelper.copyFBNetWork(type.getFBNetwork(), typeCopy.getInterfaceList()));
		return typeCopy;
	}

	@Override
	public FBNetwork getModel() {
		return cfbt.getFBNetwork();
	}

	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		if (cfbt.getFBNetwork() != null) {
			viewer.setContents(getModel());
		}
	}

	@Override
	public void commandStackChanged(EventObject event) {
		// nothing to do as its a viewer!
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// nothing to do as its a viewer!
	}

	private String getNameHierarchy() {
		// TODO mabye a nice helper function to be put into the fb model
		StringBuilder retVal = new StringBuilder(fb.getName());
		EObject cont = fb.eContainer().eContainer();
		if (cont instanceof INamedElement) {
			retVal.insert(0, ((INamedElement) cont).getName() + "."); //$NON-NLS-1$
		}
		return retVal.toString();
	}

}
