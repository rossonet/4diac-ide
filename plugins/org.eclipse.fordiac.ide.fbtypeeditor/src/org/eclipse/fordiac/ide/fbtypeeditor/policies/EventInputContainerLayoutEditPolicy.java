/*******************************************************************************
 * Copyright (c) 2011 - 2017 Profactor GmbH, fortiss GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.policies;

import java.util.List;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.fordiac.ide.fbtypeeditor.editparts.InterfaceEditPart;
import org.eclipse.fordiac.ide.gef.preferences.DiagramPreferences;
import org.eclipse.fordiac.ide.model.commands.change.ChangeInterfaceOrderCommand;
import org.eclipse.fordiac.ide.model.commands.create.CreateInterfaceElementCommand;
import org.eclipse.fordiac.ide.model.data.DataType;
import org.eclipse.fordiac.ide.model.data.EventType;
import org.eclipse.fordiac.ide.model.libraryElement.FBType;
import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

public class EventInputContainerLayoutEditPolicy extends AbstractInterfaceContainerLayoutEditPolicy {

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {

		return new AbstractInterfaceSelectionEditPolicy(DiagramPreferences.CORNER_DIM_HALF, new Insets(1)) {

			@Override
			protected List<? extends IInterfaceElement> getInterfaceElementList() {
				return getFBType().getInterfaceList().getEventInputs();
			}

			@Override
			protected Command getIECreateCommand(DataType refElement, int ref) {
				return new CreateInterfaceElementCommand(refElement, getFBType().getInterfaceList(), true, ref);
			}
		};
	}

	@Override
	protected Command createMoveChildCommand(final EditPart child, final EditPart after) {
		if (child instanceof InterfaceEditPart) {
			InterfaceEditPart childEP = (InterfaceEditPart) child;
			InterfaceEditPart afterEP = null;
			if (after != null) {
				afterEP = (InterfaceEditPart) after;
			}
			if (childEP.isEvent() && childEP.isInput()
					&& (afterEP == null || (afterEP.isEvent() && afterEP.isInput()))) {
				int newIndex = -1;
				if (after == null) {
					newIndex = getHost().getChildren().size();
				} else {
					newIndex = getHost().getChildren().indexOf(after);
				}
				return new ChangeInterfaceOrderCommand((IInterfaceElement) childEP.getModel(),
						((IInterfaceElement) childEP.getModel()).isIsInput(), newIndex);
			}
		}
		return null;
	}

	@Override
	protected Command getCreateCommand(final CreateRequest request) {
		Object childClass = request.getNewObjectType();
		FBType type = getFBType();
		int index = -1;
		EditPart ref = getInsertionReference(request);
		if (ref != null) {
			index = type.getInterfaceList().getEventInputs().indexOf(ref.getModel());
		}
		if (childClass instanceof EventType && type != null) {
			return new CreateInterfaceElementCommand((DataType) childClass, type.getInterfaceList(), true, index);
		}
		return null;
	}
}
