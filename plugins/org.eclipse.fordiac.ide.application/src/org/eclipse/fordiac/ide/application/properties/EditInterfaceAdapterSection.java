/*******************************************************************************
 * Copyright (c) 2017 fortiss GmbH
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Monika Wenger - initial implementation
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.properties;

import java.util.ArrayList;

import org.eclipse.fordiac.ide.application.commands.CreateSubAppInterfaceElementCommand;
import org.eclipse.fordiac.ide.model.Palette.AdapterTypePaletteEntry;
import org.eclipse.fordiac.ide.model.libraryElement.AdapterType;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class EditInterfaceAdapterSection extends AbstractEditInterfaceSection {
	public void createControls(final Composite parent, final TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		inputsViewer.setContentProvider(new InterfaceContentProvider(true, InterfaceContentProviderType.ADAPTER));
		outputsViewer.setContentProvider(new InterfaceContentProvider(false, InterfaceContentProviderType.ADAPTER));
	}

	@Override
	protected void setType(Object input) {
		super.setType(input);
		setCellEditors();  //only now the types are correctly set
	}

	@Override
	protected CreateSubAppInterfaceElementCommand newCommand(boolean isInput) {
		AdapterType type = (AdapterType) getType().getFbNetwork().getApplication().getAutomationSystem().getPalette().getTypeEntry(fillTypeCombo()[0]).getType();
		return new CreateSubAppInterfaceElementCommand(type, getType().getInterface(), isInput, -1);
	}

	@Override
	protected String[] fillTypeCombo() {
		ArrayList<String> list = new ArrayList<String>();
		if(null != getType()) {
			for (AdapterTypePaletteEntry adaptertype : getAdapterTypes(getType().getFbNetwork().getApplication().getAutomationSystem().getPalette())){
				list.add(adaptertype.getAdapterType().getName());
			}
		}
		return list.toArray(new String[0]);		
	}
}