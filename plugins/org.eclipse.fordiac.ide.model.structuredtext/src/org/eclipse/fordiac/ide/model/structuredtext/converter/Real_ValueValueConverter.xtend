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

import static extension java.lang.Double.valueOf

class Real_ValueValueConverter extends AbstractNullSafeConverter<Double> {

	override protected internalToString(Double value) {
		value.toString
	}

	override protected internalToValue(String string, INode node) throws ValueConverterException {
		if (string.nullOrEmpty) {
			throw new ValueConverterException("Couldn't convert empty string to a double value.", node, null)
		}
		val value = string.replace("_", "")
		if (value.nullOrEmpty) {
			throw new ValueConverterException("Couldn't convert input '" + string + "' to a double value.", node, null)
		}
		try {
			value.valueOf
		} catch (NumberFormatException e) {
			throw new ValueConverterException("Couldn't convert '" + string + "' to a double value.", node, e);
		}
	}

}
