/********************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Gerhard Ebenhofer, Alois Zoitl, Ingo Hegny, Monika Wenger
 *    - initial API and implementation and/or initial documentation
 ********************************************************************************/
package org.eclipse.fordiac.ide.model.libraryElement;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Output
 * Primitive</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.fordiac.ide.model.libraryElement.OutputPrimitive#getTestResult
 * <em>Test Result</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getOutputPrimitive()
 * @model
 * @generated
 */
public interface OutputPrimitive extends Primitive {
	/**
	 * Returns the value of the '<em><b>Test Result</b></em>' attribute. The default
	 * value is <code>"0"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Test Result</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Test Result</em>' attribute.
	 * @see #isSetTestResult()
	 * @see #unsetTestResult()
	 * @see #setTestResult(int)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getOutputPrimitive_TestResult()
	 * @model default="0" unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" volatile="true"
	 * @generated
	 */
	int getTestResult();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.fordiac.ide.model.libraryElement.OutputPrimitive#getTestResult
	 * <em>Test Result</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Test Result</em>' attribute.
	 * @see #isSetTestResult()
	 * @see #unsetTestResult()
	 * @see #getTestResult()
	 * @generated
	 */
	void setTestResult(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.fordiac.ide.model.libraryElement.OutputPrimitive#getTestResult
	 * <em>Test Result</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetTestResult()
	 * @see #getTestResult()
	 * @see #setTestResult(int)
	 * @generated
	 */
	void unsetTestResult();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.fordiac.ide.model.libraryElement.OutputPrimitive#getTestResult
	 * <em>Test Result</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Test Result</em>' attribute is set.
	 * @see #unsetTestResult()
	 * @see #getTestResult()
	 * @see #setTestResult(int)
	 * @generated
	 */
	boolean isSetTestResult();

	public static final int NOT_TESTED = 0;
	public static final int TEST_OK = 1;
	public static final int TEST_FAIL = -1;

} // OutputPrimitive
