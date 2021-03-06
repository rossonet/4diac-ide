/*******************************************************************************
 * Copyright (c) 2008, 2012 - 2017 Profactor GmbH, fortiss GmbH
 * 				 2020 Johannes Kepler University Linz
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
 *   Alois Zoitl - changed base class to correctly reflect the layouting,
 *                 code clean-up
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.ecc.policies;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.fordiac.ide.fbtypeeditor.ecc.commands.ActionMoveCommand;
import org.eclipse.fordiac.ide.fbtypeeditor.ecc.commands.CreateECActionCommand;
import org.eclipse.fordiac.ide.fbtypeeditor.ecc.editparts.ECActionAlgorithmEditPart;
import org.eclipse.fordiac.ide.fbtypeeditor.ecc.editparts.ECActionOutputEventEditPart;
import org.eclipse.fordiac.ide.gef.policies.ModifiedNonResizeableEditPolicy;
import org.eclipse.fordiac.ide.model.libraryElement.ECAction;
import org.eclipse.fordiac.ide.model.libraryElement.ECState;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

public class ECStateLayoutEditPolicy extends LayoutEditPolicy {
	@Override
	public Command getCommand(Request request) {
		Object type = request.getType();
		if (REQ_ALIGN.equals(type) && request instanceof AlignmentRequest) {
			return getAlignCommand((AlignmentRequest) request);
		}
		return super.getCommand(request);
	}

	protected Command getAlignCommand(AlignmentRequest request) {
		AlignmentRequest req = new AlignmentRequest(REQ_ALIGN_CHILDREN);
		req.setEditParts(getHost());
		req.setAlignment(request.getAlignment());
		req.setAlignmentRectangle(request.getAlignmentRectangle());
		return getHost().getParent().getCommand(req);
	}

	@Override
	protected Command getCreateCommand(final CreateRequest request) {
		if (request.getNewObjectType().equals(ECAction.class) && getHost().getModel() instanceof ECState) {
			ECState state = (ECState) getHost().getModel();
			if ((null != state) && (!state.isStartState())) {
				// only create an action when the target is not the initial state
				return new CreateECActionCommand((ECAction) request.getNewObject(), state);
			}
		}
		return null;
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		ModifiedNonResizeableEditPolicy editPolicy = new ModifiedNonResizeableEditPolicy(0, new Insets(0)) {
			@Override
			protected Command getMoveCommand(ChangeBoundsRequest request) {
				return getActionMoveCommand(request);
			}
		};
		editPolicy.setDragAllowed(true);
		return editPolicy;
	}

	@Override
	protected Command getAddCommand(Request generic) {
		// move actions between states
		if (generic instanceof ChangeBoundsRequest) {
			return getActionMoveCommand((ChangeBoundsRequest) generic);
		}
		return null;
	}

	private Command getActionMoveCommand(ChangeBoundsRequest request) {
		if (getHost().getModel() instanceof ECState) {
			ECState targetState = (ECState) getHost().getModel();
			ECAction action = null;
			if (!request.getEditParts().isEmpty()) {
				if (request.getEditParts().get(0) instanceof ECActionAlgorithmEditPart) {
					action = ((ECActionAlgorithmEditPart) request.getEditParts().get(0)).getAction();
				}
				if (request.getEditParts().get(0) instanceof ECActionOutputEventEditPart) {
					action = ((ECActionOutputEventEditPart) request.getEditParts().get(0)).getAction();
				}
			}
			return new ActionMoveCommand(action, targetState, targetState.getECAction().size());
		}
		return null;
	}

	@Override
	protected Command getMoveChildrenCommand(Request request) {
		// we currently can not directly reorder actions of states in the graphics
		// therefore null is fine here
		return null;
	}

}
