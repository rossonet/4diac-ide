/*******************************************************************************
 * Copyright (c) 2008, 2011, 2013 Profactor GmbH, fortiss GmbH
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
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.ecc.policies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.fordiac.ide.fbtypeeditor.ecc.commands.CreateTransitionCommand;
import org.eclipse.fordiac.ide.fbtypeeditor.ecc.commands.ReconnectTransitionCommand;
import org.eclipse.fordiac.ide.model.libraryElement.ECState;
import org.eclipse.fordiac.ide.model.libraryElement.ECTransition;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * The Class TransitionNodeEditPolicy.
 */
public class TransitionNodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(final CreateConnectionRequest request) {
		if (request.getStartCommand() instanceof CreateTransitionCommand) {
			CreateTransitionCommand command = (CreateTransitionCommand) request.getStartCommand();
			if (getHost().getModel() instanceof ECState) {

				Point destination = request.getLocation().getCopy();
				getHostFigure().translateToRelative(destination);

				command.setDestinationLocation(destination);
				command.setDestination((ECState) getHost().getModel());
				return command;
			}
		}
		return null;

	}

	@Override
	protected Command getConnectionCreateCommand(final CreateConnectionRequest request) {

		CreateTransitionCommand cmd = new CreateTransitionCommand();
		if (getHost().getModel() instanceof ECState) {
			Point source = request.getLocation().getCopy();
			getHostFigure().translateToRelative(source);

			cmd.setSource((ECState) getHost().getModel());
			cmd.setSourceLocation(source);
			cmd.setViewer(getHost().getViewer());
		}
		request.setStartCommand(cmd);
		return cmd;
	}

	@Override
	protected Command getReconnectSourceCommand(final ReconnectRequest request) {
		ECTransition transition = (ECTransition) request.getConnectionEditPart().getModel();
		// check if the source has changed
		return (transition.getSource().equals(request.getTarget().getModel())) ? null
				: new ReconnectTransitionCommand(request);
	}

	@Override
	protected Command getReconnectTargetCommand(final ReconnectRequest request) {
		ECTransition transition = (ECTransition) request.getConnectionEditPart().getModel();
		// check if the source has changed
		return (transition.getDestination().equals(request.getTarget().getModel())) ? null
				: new ReconnectTransitionCommand(request);
	}

}
