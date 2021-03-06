/*******************************************************************************
 * Copyright (c) 2008, 2009, 2014, 2016 Profactor GmbH, AIT, fortiss GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Filip Andren, Alois Zoitl
 *   - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.commands;

import org.eclipse.fordiac.ide.application.Messages;
import org.eclipse.fordiac.ide.model.commands.create.AbstractConnectionCreateCommand;
import org.eclipse.fordiac.ide.model.commands.create.DataConnectionCreateCommand;
import org.eclipse.fordiac.ide.model.commands.create.LinkConstraints;
import org.eclipse.fordiac.ide.model.libraryElement.Connection;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement;
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * A command for reconnecting data connections.
 */
public class ReconnectDataConnectionCommand extends AbstractReconnectConnectionCommand {

	/**
	 * A command for reconnecting data connection.
	 *
	 * @param request the request
	 */
	public ReconnectDataConnectionCommand(final ReconnectRequest request, FBNetwork parent) {
		super(Messages.ReconnectDataConnectionCommand_LABEL, request, parent);
	}

	@Override
	protected boolean checkSourceAndTarget(IInterfaceElement sourceIE, IInterfaceElement targetIE) {
		if ((sourceIE instanceof VarDeclaration) && (targetIE instanceof VarDeclaration)) {
			return LinkConstraints.canExistDataConnection((VarDeclaration) sourceIE, (VarDeclaration) targetIE,
					getParent(), (Connection) getRequest().getConnectionEditPart().getModel());
		}
		return false;
	}

	@Override
	protected AbstractConnectionCreateCommand createConnectionCreateCommand(FBNetwork parent) {
		return new DataConnectionCreateCommand(parent);
	}
}
