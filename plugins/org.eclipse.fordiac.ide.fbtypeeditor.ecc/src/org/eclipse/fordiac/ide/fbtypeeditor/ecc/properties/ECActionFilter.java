/*******************************************************************************
 * Copyright (c) 2015 fortiss GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.ecc.properties;

import org.eclipse.fordiac.ide.fbtypeeditor.ecc.editparts.ECActionAlgorithm;
import org.eclipse.fordiac.ide.fbtypeeditor.ecc.editparts.ECActionAlgorithmEditPart;
import org.eclipse.fordiac.ide.fbtypeeditor.ecc.editparts.ECActionOutputEvent;
import org.eclipse.fordiac.ide.fbtypeeditor.ecc.editparts.ECActionOutputEventEditPart;
import org.eclipse.fordiac.ide.model.libraryElement.ECAction;
import org.eclipse.jface.viewers.IFilter;

public class ECActionFilter implements IFilter {

	@Override
	public boolean select(Object toTest) {
		if (toTest instanceof ECActionAlgorithmEditPart) {
			return true;
		}
		if (toTest instanceof ECActionOutputEventEditPart) {
			return true;
		}
		if (toTest instanceof ECActionAlgorithm || toTest instanceof ECActionOutputEvent) {
			return true;
		}
		if (toTest instanceof ECAction) {
			return true;
		}
		return false;
	}

}
