/********************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Gerhard Ebenhofer, Alois Zoitl, Ingo Hegny, Monika Wenger
 *    - initial API and implementation and/or initial documentation
 ********************************************************************************/
package org.eclipse.fordiac.ide.model.libraryElement;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Link</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fordiac.ide.model.libraryElement.Link#getSegment <em>Segment</em>}</li>
 *   <li>{@link org.eclipse.fordiac.ide.model.libraryElement.Link#getDevice <em>Device</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getLink()
 * @model
 * @generated
 */
public interface Link extends ConfigurableObject {
	/**
	 * Returns the value of the '<em><b>Segment</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fordiac.ide.model.libraryElement.Segment#getOutConnections <em>Out Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Segment</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Segment</em>' reference.
	 * @see #setSegment(Segment)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getLink_Segment()
	 * @see org.eclipse.fordiac.ide.model.libraryElement.Segment#getOutConnections
	 * @model opposite="outConnections"
	 * @generated
	 */
	Segment getSegment();

	/**
	 * Sets the value of the '{@link org.eclipse.fordiac.ide.model.libraryElement.Link#getSegment <em>Segment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Segment</em>' reference.
	 * @see #getSegment()
	 * @generated
	 */
	void setSegment(Segment value);

	/**
	 * Returns the value of the '<em><b>Device</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fordiac.ide.model.libraryElement.Device#getInConnections <em>In Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Device</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Device</em>' reference.
	 * @see #setDevice(Device)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getLink_Device()
	 * @see org.eclipse.fordiac.ide.model.libraryElement.Device#getInConnections
	 * @model opposite="inConnections"
	 * @generated
	 */
	Device getDevice();

	/**
	 * Sets the value of the '{@link org.eclipse.fordiac.ide.model.libraryElement.Link#getDevice <em>Device</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Device</em>' reference.
	 * @see #getDevice()
	 * @generated
	 */
	void setDevice(Device value);

} // Link