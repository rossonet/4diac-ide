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
 *   Martin Jobst 
 *       - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.model.structuredtext.converter

import org.eclipse.xtext.conversion.impl.AbstractNullSafeConverter
import org.eclipse.xtext.nodemodel.INode
import org.eclipse.xtext.conversion.ValueConverterException

import static extension java.lang.Boolean.valueOf

class Bool_ValueValueConverter extends AbstractNullSafeConverter<Boolean> {

	override protected internalToString(Boolean value) {
		value.toString.toUpperCase
	}

	override protected internalToValue(String string, INode node) throws ValueConverterException {
		if (string.nullOrEmpty) {
			throw new ValueConverterException("Couldn't convert empty string to a bool value.", node, null)
		}
		string.valueOf
	}

}
