/*******************************************************************************
 * Copyright (c) 2008, 2009, 2011, 2015, 2017 Profactor GbmH, fortiss GmbH
 * 				 2019 Johannes Kepler University Linz
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl
 *     - initial API and implementation and/or initial documentation
 *   Alois Zoitl - added diagram font preference 
 *******************************************************************************/
package org.eclipse.fordiac.ide.ui.preferences;

/**
 * Constant definitions for plug-in preferences.
 * 
 * @author Gerhard Ebenhofer (gerhard.ebenhofer@profactor.at)
 */
public final class PreferenceConstants {

	/** The Constant P_EVENT_CONNECTOR_COLOR. */
	public static final String P_EVENT_CONNECTOR_COLOR = "EventConnectionConnectorColor"; //$NON-NLS-1$

	/** The Constant P_DATA_CONNECTOR_COLOR. */
	public static final String P_DATA_CONNECTOR_COLOR = "DataConnectionConnectorColor";//$NON-NLS-1$

	/** The Constant P_ADAPTER_CONNECTOR_COLOR. */
	public static final String P_ADAPTER_CONNECTOR_COLOR = "AdapterConnectionConnectorColor";//$NON-NLS-1$

	/** The Constant P_DEFAULT_COMPLIANCE_PROFILE. */
	public static final String P_DEFAULT_COMPLIANCE_PROFILE = "P_DEFAULT_COMPLIANCE_PROFILE";//$NON-NLS-1$

	/** The Constant P_HIDE_EVENT_CON. */
	public static final String P_HIDE_EVENT_CON = "hideEventConnections";//$NON-NLS-1$

	/** The Constant P_HIDE_DATA_CON. */
	public static final String P_HIDE_DATA_CON = "hideDataConnections";//$NON-NLS-1$

	/**
	 * The font to be used on all 4diac IDE graphical editors and diagrams,
	 * currently defaults to the Text_Font
	 */
	public static final String DIAGRAM_FONT = "org.eclipse.fordiac.ide.preferences.diagramFontDefinition"; //$NON-NLS-1$

	private PreferenceConstants() {
		throw new UnsupportedOperationException("PreferenceConstants utility class should not be instantiated!"); //$NON-NLS-1$
	}

}