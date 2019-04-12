/*******************************************************************************
 * Copyright (c) 2013, 2017 fortiss GmbH
 * 				 2019 Johannes Kepler University
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alois Zoitl, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *   Alois Zoitl - Extracted this class from the FBInsertAction  
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.actions;

import org.eclipse.swt.graphics.Point;
import org.eclipse.fordiac.ide.model.Palette.PaletteEntry;
import org.eclipse.fordiac.ide.model.commands.create.AbstractCreateFBNetworkElementCommand;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.ui.IWorkbenchPart;

public class FBNetworkElementInsertAction extends WorkbenchPartAction {

	private AbstractCreateFBNetworkElementCommand createCmd;

	protected FBNetworkElementInsertAction(IWorkbenchPart part, AbstractCreateFBNetworkElementCommand createCmd, PaletteEntry paletteEntry) {
		super(part);
		this.createCmd = createCmd;
		
		setId(paletteEntry.getFile().getFullPath().toString());
		setText(paletteEntry.getLabel());
	}

	@Override
	protected boolean calculateEnabled() {
		return createCmd.canExecute();
	}

	@Override
	public void run() {
		execute(createCmd);
	}
	
	public void updateCreatePosition(Point pt) {
		createCmd.updateCreatePosition(pt.x, pt.y);		
	}

}