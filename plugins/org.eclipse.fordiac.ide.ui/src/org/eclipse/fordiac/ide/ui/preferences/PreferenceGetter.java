/*******************************************************************************
 * Copyright (c) 2008, 2009 Profactor GbmH, fortiss GmbH,
 * 				 2018 Johannes Kepler University
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.ui.preferences;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.fordiac.ide.ui.UIPlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * This class implements some static methods for returning different preference
 * settings.
 * 
 * @author Gerhard Ebenhofer (gerhard.ebenhofer@profactor.at)
 * @version $Id: PreferenceGetter.java 21624 2007-06-22 11:18:13Z gebenh $
 */

public final class PreferenceGetter {

	/** The used colors. */
	private static final Map<RGB, Color> usedColors = new HashMap<>();

	/**
	 * Returns the color for the specified preference.
	 * 
	 * @param pref The preference.
	 * 
	 * @return the color
	 */
	public static Color getColor(final String pref) {
		RGB rgb = PreferenceConverter.getColor(UIPlugin.getDefault()
		.getPreferenceStore(), pref);

		if (!usedColors.containsKey(rgb)) {
			usedColors.put(rgb, new Color(null, rgb));
		}

		return usedColors.get(rgb);
	}
	
	/**
	 * Returns the color for the specified preference.
	 * 
	 * @param pref The preference.
	 * @param store the store
	 * 
	 * @return the color
	 */
	public static Color getColor(IPreferenceStore store, final String pref) {
		RGB rgb = PreferenceConverter.getColor(store, pref);
		
		if (!usedColors.containsKey(rgb)) {
			usedColors.put(rgb, new Color(null, rgb));
		}
		
		return usedColors.get(rgb);
	}

	
	private PreferenceGetter() {
		throw new UnsupportedOperationException("PreferenceGetter utility class should not be instantiated!"); //$NON-NLS-1$
	}
}