/*******************************************************************************
 * Copyright (c) 2013 - 2017 fortiss GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Alois Zoitl, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.actions;

import org.eclipse.fordiac.ide.fbtypeeditor.Messages;
import org.eclipse.fordiac.ide.model.commands.create.CreateInterfaceElementCommand;
import org.eclipse.fordiac.ide.model.libraryElement.FBType;
import org.eclipse.fordiac.ide.model.typelibrary.EventTypeLibrary;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.ui.IWorkbenchPart;

public class CreateOutputEventAction extends WorkbenchPartAction {
	public static final String ID = "CreateOutputEventAction"; //$NON-NLS-1$
	private FBType fbType;

	public CreateOutputEventAction(IWorkbenchPart part, FBType fbType) {
		super(part);
		setId(ID);
		setText(Messages.CreateOutputEventAction_CreateOutputEvent);

		this.fbType = fbType;
	}

	@Override
	protected boolean calculateEnabled() {
		return (null != fbType);
	}

	@Override
	public void run() {
		CreateInterfaceElementCommand cmd = new CreateInterfaceElementCommand(
				EventTypeLibrary.getInstance().getType(null), fbType.getInterfaceList(), false, -1);
		execute(cmd);
	}

}
