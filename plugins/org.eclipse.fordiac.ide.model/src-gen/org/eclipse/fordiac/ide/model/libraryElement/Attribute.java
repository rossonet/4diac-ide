/**
 * *******************************************************************************
 *  * Copyright (c) 2008 - 2017 4DIAC - consortium.
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the Eclipse Public License v1.0
 *  * which accompanies this distribution, and is available at
 *  * http://www.eclipse.org/legal/epl-v10.html
 *  *
 *  * Contributors:
 *  *   Gerhard Ebenhofer, Alois Zoitl, Ingo Hegny, Monika Wenger, Martin Jobst
 *  *     - initial API and implementation and/or initial documentation
 *  *******************************************************************************
 */
package org.eclipse.fordiac.ide.model.libraryElement;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fordiac.ide.model.libraryElement.Attribute#getAttributeDeclaration <em>Attribute Declaration</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getAttribute()
 * @model
 * @generated
 */
public interface Attribute extends IAttribute {
	/**
	 * Returns the value of the '<em><b>Attribute Declaration</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute Declaration</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Declaration</em>' reference.
	 * @see #setAttributeDeclaration(AttributeDeclaration)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getAttribute_AttributeDeclaration()
	 * @model
	 * @generated
	 */
	AttributeDeclaration getAttributeDeclaration();

	/**
	 * Sets the value of the '{@link org.eclipse.fordiac.ide.model.libraryElement.Attribute#getAttributeDeclaration <em>Attribute Declaration</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute Declaration</em>' reference.
	 * @see #getAttributeDeclaration()
	 * @generated
	 */
	void setAttributeDeclaration(AttributeDeclaration value);

} // Attribute
