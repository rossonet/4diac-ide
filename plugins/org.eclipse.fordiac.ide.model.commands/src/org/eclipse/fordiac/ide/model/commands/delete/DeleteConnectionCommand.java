/*******************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GmbH, fortiss GmbH, AIT
 * 				 2019 Johannes Keppler University Linz
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Filip Pr�stl Andr�n, Monika Wenger
 *   	- initial API and implementation and/or initial documentation
 *   Alois Zoitl - removed editor check from canUndo 
 *******************************************************************************/
package org.eclipse.fordiac.ide.model.commands.delete;

import org.eclipse.fordiac.ide.model.commands.Messages;
import org.eclipse.fordiac.ide.model.libraryElement.Connection;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetworkElement;
import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement;
import org.eclipse.gef.commands.Command;

public class DeleteConnectionCommand extends Command {
	private IInterfaceElement source;
	private IInterfaceElement destination;
	private Connection connection;
	private final FBNetwork connectionParent;
	private boolean performMappingCheck;
	private DeleteConnectionCommand deleteMapped = null;

	public DeleteConnectionCommand(final Connection connection) {
		super(Messages.DeleteConnectionCommand_DeleteConnection);
		this.connection = connection;
		if (this.connection.eContainer() instanceof FBNetwork) {
			connectionParent = (FBNetwork) this.connection.eContainer();
		} else {
			connectionParent = null;
		}
		performMappingCheck = true;
	}

	public Connection getConnectionView() {
		return connection;
	}

	@Override
	public void execute() {
		source = connection.getSource();
		destination = connection.getDestination();
		if (performMappingCheck) {
			deleteMapped = checkAndDeleteMirroredConnection();
			if (null != deleteMapped) {
				deleteMapped.execute();
			}
		}
		redo();
	}

	@Override
	public void redo() {
		connection.setSource(null);
		connection.setDestination(null);
		if (null != deleteMapped) {
			deleteMapped.redo();
		}
		if (connectionParent != null) {
			connectionParent.removeConnection(connection);
		}
	}

	@Override
	public void undo() {
		connection.setSource(source);
		connection.setDestination(destination);
		if (connectionParent != null) {
			connectionParent.addConnection(connection);
		}
		if (null != deleteMapped) {
			deleteMapped.undo();
		}
	}

	private DeleteConnectionCommand checkAndDeleteMirroredConnection() {
		if (null != source && null != source.getFBNetworkElement() && null != destination
				&& null != destination.getFBNetworkElement()) {
			FBNetworkElement opSource = source.getFBNetworkElement().getOpposite();
			FBNetworkElement opDestination = destination.getFBNetworkElement().getOpposite();
			if (null != opSource && null != opDestination && opSource.getFbNetwork() == opDestination.getFbNetwork()) {
				Connection con = findConnection(opSource.getInterfaceElement(source.getName()),
						opDestination.getInterfaceElement(destination.getName()));
				if (null != con) {
					DeleteConnectionCommand cmd = new DeleteConnectionCommand(con);
					cmd.setPerformMappingCheck(false); // as this is the command for the mirrored connection we don't
														// want again to check
					return (cmd.canExecute()) ? cmd : null;
				}
			}
		}
		return null;
	}

	private static Connection findConnection(IInterfaceElement source, IInterfaceElement destination) {
		for (Connection con : source.getOutputConnections()) {
			if (con.getDestination() == destination) {
				return con;
			}
		}
		return null;
	}

	private void setPerformMappingCheck(boolean performMappingCheck) {
		this.performMappingCheck = performMappingCheck;
	}
}
