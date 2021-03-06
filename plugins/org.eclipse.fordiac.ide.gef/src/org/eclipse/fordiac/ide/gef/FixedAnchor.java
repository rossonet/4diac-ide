/*******************************************************************************
 * Copyright (c) 2008, 2009, 2013, 2015 Profactor GbmH, fortiss GmbH
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
package org.eclipse.fordiac.ide.gef;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;

/**
 * The Class FixedAnchor.
 * 
 * @author gebenh
 */
public class FixedAnchor extends ChopboxAnchor {

	/** The is input. */
	private boolean isInput = false;

	private GraphicalEditPart editPart;

	/**
	 * Instantiates a new fixed anchor.
	 * 
	 * @param owner   the owner
	 * @param isInput the is input
	 */
	public FixedAnchor(final IFigure owner, final boolean isInput) {
		super(owner);
		this.isInput = isInput;
	}

	/**
	 * Instantiates a new fixed anchor.
	 * 
	 * @param owner    the owner
	 * @param isInput  the is input
	 * @param editPart the editPart this anchor is related to
	 */
	public FixedAnchor(final IFigure owner, final boolean isInput, GraphicalEditPart editPart) {
		super(owner);
		this.isInput = isInput;
		this.editPart = editPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.ConnectionAnchor#getLocation(org.eclipse.draw2d.geometry
	 * .Point)
	 */
	@Override
	public Point getLocation(final Point reference) {
		Point location;
		if (isInput) {
			location = getBox().getLeft();
		} else {
			location = getBox().getRight();
			location.x -= 1; // the right seems to be exactly the pixel after the box. With this -1 we get
								// the border of the box.
		}

		getOwner().translateToAbsolute(location);

		return location;
	}

	@Override
	public Point getReferencePoint() {
		return getLocation(null);
	}

	/**
	 * 
	 * @return the editpart this anchor is related to
	 */
	public GraphicalEditPart getEditPart() {
		return editPart;
	}

	public boolean isInput() {
		return isInput;
	}

}
