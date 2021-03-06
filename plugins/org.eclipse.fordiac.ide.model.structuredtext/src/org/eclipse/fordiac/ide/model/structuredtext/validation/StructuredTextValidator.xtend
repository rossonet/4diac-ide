/*******************************************************************************
 * Copyright (c) 2020 Johannes Kepler University Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *
 *   Ernst Blecha - initial API and implementation and/or initial documentation
 *******************************************************************************/

/*
 * generated by Xtext 2.20.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.validation

import org.eclipse.fordiac.ide.model.structuredtext.structuredText.PartialAccess
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage
import org.eclipse.xtext.validation.Check
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.LocatedVariable
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.PrimaryVariable
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.AdapterVariable
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.Variable

/**
 * This class contains custom validation rules.
 *
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#validation
 */
class StructuredTextValidator extends AbstractStructuredTextValidator {

	def private int AccessBits(PartialAccess part) {
		part.extractTypeInformation("").BitSize
	}

	def private isIndexInRange(PartialAccess p, int size) {
  		(p.index >= 0 && p.index < size)
	}

	@Check
	def checkPartialAccess(PrimaryVariable v) {
		if (null !== v.part) {
			//BitSize(v) != BitSize(v.^var.type.name) -> BitSize(v) returns the accessed partial size!
			val indexSize = (BitSize(v.^var.type.name)/v.part.AccessBits);
			if (v.part.AccessBits > 0 && BitSize(v.^var.type.name) > v.part.AccessBits)
				if (!isIndexInRange(v.part, indexSize)) {
					error("Incorrect partial access: index not within limits.", StructuredTextPackage.Literals.PRIMARY_VARIABLE__VAR)
				}
			else if(v.part.AccessBits == 0 || BitSize(v.^var.type.name) == 0) {
				error("Incorrect partial access: datatypes other than ANY_BIT.", StructuredTextPackage.Literals.PRIMARY_VARIABLE__VAR)
			} else if (indexSize < 1) {
				error("Incorrect partial access: access dataype must be smaller than storage datatype.", StructuredTextPackage.Literals.PRIMARY_VARIABLE__VAR)
			}
		}
	}
	
	def private dispatch int BitSize(VarDeclaration v) { BitSize(v.extractTypeInformation)	}
	
	def private dispatch int BitSize(PrimaryVariable v) { BitSize(v.extractTypeInformation)	}
	
	def private dispatch int BitSize(LocatedVariable v) { BitSize(v.extractTypeInformation) }
	
	def private dispatch int BitSize(String str) {
		switch (str) {
			case str.equals("LWORD"): 64
			case str.equals("DWORD"): 32
			case str.equals("WORD"):  16
			case str.equals("BYTE"):   8
			case str.equals("BOOL"):   1
			default:                   0
		}
	}

	def private dispatch String extractTypeInformation(PartialAccess part, String DataType) {
		if (null !== part) {
			if (part.bitaccess)        "BOOL"
			else if (part.byteaccess)  "BYTE"
			else if (part.wordaccess)  "WORD"
			else if (part.dwordaccess) "DWORD"
			else                       ""
		} else                         DataType
	}

	def private dispatch String extractTypeInformation(PrimaryVariable variable, String DataType) {
		if (null !== variable && null !== variable.part) variable.part.extractTypeInformation
		else                                             variable.^var.type.name
	}

	def private dispatch String extractTypeInformation(Variable variable, String DataType) {
		if (null !== variable && null !== variable.part) variable.part.extractTypeInformation
		else                                             ""
	}

	def protected dispatch String extractTypeInformation(PrimaryVariable variable) {
		if (null !== variable) variable.extractTypeInformation(variable.^var.extractTypeInformation)
		else                   variable.^var.type.name
	}

	def protected dispatch String extractTypeInformation(VarDeclaration variable) {	variable.type.name }
	
	def protected dispatch String extractTypeInformation(AdapterVariable variable) { variable.^var.type.name }
	
	@Check
	def checkAtLocation(LocatedVariable v) {
		if (null !== v.location) {
			if (v.BitSize == 0 && v.array )
				error("Piecewise located variables are allowed only for variables of type ANY_BIT", StructuredTextPackage.Literals.LOCATED_VARIABLE__LOCATION)
			if (v.BitSize > 0 && v.array && v.arraySize == 0)
				error("Piecewise located variables must have at least an array size of 1", StructuredTextPackage.Literals.LOCATED_VARIABLE__LOCATION)
			if (v.BitSize > 0 && v.array && v.arraySize > v.location.BitSize)
				error("Piecewise located variables cannot access more bits than are available in the destination", StructuredTextPackage.Literals.LOCATED_VARIABLE__LOCATION)
			if (v.BitSize == 0 && v.location.BitSize == 0 && !(v.location.extractTypeInformation(v.location.extractTypeInformation) == v.extractTypeInformation))
				error("General located variables must have matching types", StructuredTextPackage.Literals.LOCATED_VARIABLE__LOCATION)
		}
	}

}
