/*******************************************************************************
 * Copyright (c) 2008, 2009, 2011, 2015 - 2017 Profactor GbmH, fortiss GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.fordiac.ide.ui.UIPlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = UIPlugin.getDefault().getPreferenceStore();
		PreferenceConverter.setDefault(store, PreferenceConstants.P_EVENT_CONNECTOR_COLOR, new RGB(255, 0, 0));
		PreferenceConverter.setDefault(store, PreferenceConstants.P_DATA_CONNECTOR_COLOR, new RGB(0, 0, 255));
		PreferenceConverter.setDefault(store, PreferenceConstants.P_ADAPTER_CONNECTOR_COLOR, new RGB(0, 174, 0));
        store.setDefault(PreferenceConstants.P_DEFAULT_COMPLIANCE_PROFILE, "HOLOBLOC"); //$NON-NLS-1$

	}
}