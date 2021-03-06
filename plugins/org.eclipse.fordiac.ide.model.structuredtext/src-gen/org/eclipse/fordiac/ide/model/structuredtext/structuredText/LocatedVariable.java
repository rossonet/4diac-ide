/**
 * generated by Xtext 2.20.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.structuredText;

import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Located
 * Variable</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.LocatedVariable#getLocation
 * <em>Location</em>}</li>
 * <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.LocatedVariable#isArray
 * <em>Array</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage#getLocatedVariable()
 * @model
 * @generated
 */
public interface LocatedVariable extends VarDeclaration {
	/**
	 * Returns the value of the '<em><b>Location</b></em>' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Location</em>' containment reference.
	 * @see #setLocation(Variable)
	 * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage#getLocatedVariable_Location()
	 * @model containment="true"
	 * @generated
	 */
	Variable getLocation();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.LocatedVariable#getLocation
	 * <em>Location</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Location</em>' containment reference.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(Variable value);

	/**
	 * Returns the value of the '<em><b>Array</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Array</em>' attribute.
	 * @see #setArray(boolean)
	 * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage#getLocatedVariable_Array()
	 * @model
	 * @generated
	 */
	boolean isArray();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.LocatedVariable#isArray
	 * <em>Array</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Array</em>' attribute.
	 * @see #isArray()
	 * @generated
	 */
	void setArray(boolean value);

} // LocatedVariable
