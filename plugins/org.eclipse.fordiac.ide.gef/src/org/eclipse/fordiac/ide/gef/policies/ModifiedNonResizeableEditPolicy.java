/*******************************************************************************
 * Copyright (c) 2008, 2009, 2011, 2017 Profactor GbmH, fortiss GmbH 
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
package org.eclipse.fordiac.ide.gef.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.fordiac.ide.gef.preferences.DiagramPreferences;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;

/**
 * NonResizeableEditPolicy with a rounded EtchedBorder and moveHandles (move
 * cursor) on each side.
 */
public class ModifiedNonResizeableEditPolicy extends NonResizableEditPolicy {

	private int arc = DiagramPreferences.CORNER_DIM;

	private Insets insets = new Insets(2);

	/**
	 * Constructor.
	 * 
	 * @param arc    the arc
	 * @param insets the insets
	 */
	public ModifiedNonResizeableEditPolicy(int arc, Insets insets) {
		super();
		this.arc = arc;
		this.insets = insets;
	}

	/**
	 * Default constructor.
	 */
	public ModifiedNonResizeableEditPolicy() {
		super();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List createSelectionHandles() {
		List list = new ArrayList(1);
		list.add(new ModifiedMoveHandle((GraphicalEditPart) getHost(), insets, arc));
		return list;
	}
}
