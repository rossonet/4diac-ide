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

import java.io.Reader
import org.eclipse.xtext.conversion.ValueConverterException

import static extension java.lang.Integer.parseUnsignedInt
import static extension java.lang.Integer.toUnsignedString

class D_BYTE_CHAR_STRValueConverter extends StringValueConverter {

	override getQuote() {
		'"'
	}

	override parseHexLiteral(Reader reader) {
		val cbuf = newCharArrayOfSize(4)
		if (reader.read(cbuf) != 4) {
			throw new ValueConverterException("Couldn't convert value due to invalid escape sequence", null, null)
		}
		try {
			String.valueOf(cbuf).parseUnsignedInt(16) as char
		} catch (NumberFormatException e) {
			throw new ValueConverterException("Couldn't convert value due to invalid escape sequence", null, null)
		}
	}

	override toHexLiteral(char c) {
		c.toUnsignedString(16)
	}

}
