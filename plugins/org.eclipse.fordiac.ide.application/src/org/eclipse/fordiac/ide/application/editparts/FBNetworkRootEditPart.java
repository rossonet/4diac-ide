/*******************************************************************************
 * Copyright (c) 2008 - 2018 Profactor GmbH, TU Wien ACIN, AIT, fortiss GmbH,
 * 				 2018 - 2020 Johannes Kepler University
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Matthias Plasch, Filip Andren,
 *   Monika Wenger
 *       - initial API and implementation and/or initial documentation
 *   Alois Zoitl - fixed copy/paste handling
 *               - added inplace fb instance creation
 *               - extracted FBNetworkRootEditPart from FBNetworkEditor
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.editparts;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.fordiac.ide.application.editors.NewInstanceDirectEditManager;
import org.eclipse.fordiac.ide.gef.editparts.ZoomScalableFreeformRootEditPart;
import org.eclipse.fordiac.ide.model.Palette.FBTypePaletteEntry;
import org.eclipse.fordiac.ide.model.Palette.Palette;
import org.eclipse.fordiac.ide.model.Palette.SubApplicationTypePaletteEntry;
import org.eclipse.fordiac.ide.model.commands.create.AbstractCreateFBNetworkElementCommand;
import org.eclipse.fordiac.ide.model.commands.create.CreateSubAppInstanceCommand;
import org.eclipse.fordiac.ide.model.commands.create.FBCreateCommand;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.MarqueeDragTracker;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPartSite;

public class FBNetworkRootEditPart extends ZoomScalableFreeformRootEditPart {

	private class FBNetworkMarqueeDragTracker extends AdvancedMarqueeDragTracker {
		// redefined from MarqueeSelectionTool
		static final int DEFAULT_MODE = 0;
		static final int TOGGLE_MODE = 1;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		protected void performMarqueeSelect() {
			// determine which edit parts are affected by the current marquee
			// selection
			Collection marqueeSelectedEditParts = calculateMarqueeSelectedEditParts();

			// calculate nodes/connections that are to be selected/deselected,
			// dependent on the current mode of the tool
			Collection editPartsToSelect = new LinkedHashSet();
			Collection editPartsToDeselect = new HashSet();
			for (Iterator iterator = marqueeSelectedEditParts.iterator(); iterator.hasNext();) {
				EditPart affectedEditPart = (EditPart) iterator.next();
				if ((affectedEditPart.getSelected() == EditPart.SELECTED_NONE)
						|| (getCurrentSelectionMode() != TOGGLE_MODE)) {
					// only add connections and FBs
					if ((affectedEditPart instanceof FBEditPart) || (affectedEditPart instanceof ConnectionEditPart)
							|| (affectedEditPart instanceof SubAppForFBNetworkEditPart)) {
						editPartsToSelect.add(affectedEditPart);
					}
				} else {
					editPartsToDeselect.add(affectedEditPart);
				}
			}

			// include the current viewer selection, if not in DEFAULT mode.
			if (getCurrentSelectionMode() != DEFAULT_MODE) {
				editPartsToSelect.addAll(getCurrentViewer().getSelectedEditParts());
				editPartsToSelect.removeAll(editPartsToDeselect);
			}

			getCurrentViewer().setSelection(new StructuredSelection(editPartsToSelect.toArray()));
		}

	}

	private final FBNetwork fbNetwork;
	private final Palette palette;
	private NewInstanceDirectEditManager manager;

	public FBNetworkRootEditPart(FBNetwork fbNetwork, Palette palette, IWorkbenchPartSite site,
			ActionRegistry actionRegistry) {
		super(site, actionRegistry);
		this.fbNetwork = fbNetwork;
		this.palette = palette;
	}

	@Override
	public DragTracker getDragTracker(Request req) {
		MarqueeDragTracker dragTracker = new FBNetworkMarqueeDragTracker();
		dragTracker.setMarqueeBehavior(MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED_AND_RELATED_CONNECTIONS);
		return dragTracker;
	}

	@Override
	public void performRequest(final Request request) {
		// REQ_DIRECT_EDIT -> first select 0.4 sec pause -> click -> edit
		// REQ_OPEN -> doubleclick

		if (((request.getType() == RequestConstants.REQ_DIRECT_EDIT)
				|| (request.getType() == RequestConstants.REQ_OPEN)) && (request instanceof SelectionRequest)) {
			performDirectEdit((SelectionRequest) request);
		} else {
			super.performRequest(request);
		}
	}

	private NewInstanceDirectEditManager getManager() {
		if (null == manager) {
			manager = new NewInstanceDirectEditManager(this, palette);
		}
		return manager;
	}

	private void performDirectEdit(SelectionRequest request) {
		NewInstanceDirectEditManager directEditManager = getManager();
		directEditManager.updateRefPosition(new Point(request.getLocation().x, request.getLocation().y));
		if (request.getExtendedData().isEmpty()) {
			directEditManager.show();
		} else {
			Object key = request.getExtendedData().keySet().iterator().next();
			if (key instanceof String) {
				directEditManager.show((String) key);
			}
		}
	}

	@Override
	public Command getCommand(Request request) {
		if (request instanceof DirectEditRequest) {
			AbstractCreateFBNetworkElementCommand cmd = getDirectEditCommand((DirectEditRequest) request);
			if (null != cmd) {
				getViewer().getEditDomain().getCommandStack().execute(cmd);
				EditPart part = (EditPart) getViewer().getEditPartRegistry().get(cmd.getElement());
				getViewer().select(part);
			}
			return null;
		}
		return super.getCommand(request);
	}

	private AbstractCreateFBNetworkElementCommand getDirectEditCommand(DirectEditRequest request) {
		Object value = request.getCellEditor().getValue();
		Point refPoint = getInsertPos();
		if (value instanceof FBTypePaletteEntry) {
			return new FBCreateCommand((FBTypePaletteEntry) value, fbNetwork, refPoint.x, refPoint.y);
		}
		if (value instanceof SubApplicationTypePaletteEntry) {
			return new CreateSubAppInstanceCommand((SubApplicationTypePaletteEntry) value, fbNetwork, refPoint.x,
					refPoint.y);
		}
		return null;
	}

	private Point getInsertPos() {
		Point location = getManager().getLocator().getRefPoint();
		FigureCanvas figureCanvas = (FigureCanvas) getViewer().getControl();
		org.eclipse.draw2d.geometry.Point viewLocation = figureCanvas.getViewport().getViewLocation();
		location.x += viewLocation.x;
		location.y += viewLocation.y;
		org.eclipse.draw2d.geometry.Point insertPos = new org.eclipse.draw2d.geometry.Point(location.x, location.y)
				.scale(1.0 / getZoomManager().getZoom());
		return new Point(insertPos.x, insertPos.y);
	}
}