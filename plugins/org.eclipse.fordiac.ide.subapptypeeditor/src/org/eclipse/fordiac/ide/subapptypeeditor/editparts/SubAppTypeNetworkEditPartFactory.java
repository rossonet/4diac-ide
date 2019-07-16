/*******************************************************************************
 * Copyright (c) 2014, 2016, 2017 fortiss GmbH
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alois Zoitl
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.subapptypeeditor.editparts;

import org.eclipse.fordiac.ide.application.editparts.SubAppForFBNetworkEditPart;
import org.eclipse.fordiac.ide.fbtypeeditor.network.editparts.CompositeNetworkEditPartFactory;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.SubApp;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.parts.GraphicalEditor;

public class SubAppTypeNetworkEditPartFactory extends CompositeNetworkEditPartFactory {

	public SubAppTypeNetworkEditPartFactory(GraphicalEditor editor, ZoomManager zoomManager) {
		super(editor, zoomManager);
	}

	@Override
	protected EditPart getPartForElement(EditPart context, Object modelElement) {
		if (modelElement instanceof FBNetwork) {
			return new TypedSubAppNetworkEditPart();
		}

		if (modelElement instanceof SubApp) {
			return new SubAppForFBNetworkEditPart(getZoomManager());
		}
		return super.getPartForElement(context, modelElement);
	}

}
