/**
 * ******************************************************************************
 * Copyright (c) 2012, 2013, 2015 - 2017 Profactor GmbH, fortiss GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 *
 *
 * Contributors:
 *    Gerhard Ebenhofer, Alois Zoitl, Gerd Kainz
 *      - initial API and implementation and/or initial documentation
 * ******************************************************************************
 *
 */
package org.eclipse.fordiac.ide.deployment.monitoringbase.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.fordiac.ide.deployment.monitoringbase.MonitoringBaseFactory;
import org.eclipse.fordiac.ide.deployment.monitoringbase.MonitoringBasePackage;
import org.eclipse.fordiac.ide.deployment.monitoringbase.PortElement;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * @generated
 */
public class MonitoringBaseFactoryImpl extends EFactoryImpl implements MonitoringBaseFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public static MonitoringBaseFactory init() {
		try {
			MonitoringBaseFactory theMonitoringBaseFactory = (MonitoringBaseFactory)EPackage.Registry.INSTANCE.getEFactory(MonitoringBasePackage.eNS_URI);
			if (theMonitoringBaseFactory != null) {
				return theMonitoringBaseFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MonitoringBaseFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public MonitoringBaseFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case MonitoringBasePackage.PORT_ELEMENT: return createPortElement();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortElement createPortElement() {
		PortElementImpl portElement = new PortElementImpl();
		return portElement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MonitoringBasePackage getMonitoringBasePackage() {
		return (MonitoringBasePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MonitoringBasePackage getPackage() {
		return MonitoringBasePackage.eINSTANCE;
	}

} // MonitoringBaseFactoryImpl
