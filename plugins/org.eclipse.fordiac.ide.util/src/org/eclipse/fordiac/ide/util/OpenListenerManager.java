/*******************************************************************************
 * Copyright (c) 2008, 2009, 2011, 2014, 2017 Profactor GbmH, fortiss GmbH
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
 *   Alois Zoitl - moved openEditor helper function to EditorUtils  
 *******************************************************************************/
package org.eclipse.fordiac.ide.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.fordiac.ide.model.libraryElement.I4DIACElement;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;

/**
 * The Class OpenListenerManager.
 */
public enum OpenListenerManager {
	INSTANCE;

	private List<IOpenListener> openListeners = null;

	List<IOpenListener> getOpenListeners() {
		if (null == openListeners) {
			loadOpenListeners();
		}
		return openListeners;
	}

	/**
	 * Gets the open listener.
	 * 
	 * @param libElement    the lib element
	 * @param elementToOpen the element to open
	 * 
	 * @return the open listener
	 */
	public List<IOpenListener> getOpenListener(I4DIACElement elementToOpen) {
		ArrayList<IOpenListener> listeners = new ArrayList<>();
		for (IOpenListener openListener : getOpenListeners()) {
			if (listenerSupportsElement(openListener, elementToOpen)) {
				openListener.selectionChanged(null, new StructuredSelection(elementToOpen));
				listeners.add(openListener);
			}
		}
		return listeners;
	}

	/**
	 * Sets the default open listener.
	 * 
	 * @param libElement the lib element
	 * @param id         the id
	 */
	public static void setDefaultOpenListener(final Class<? extends I4DIACElement> libElement, final String id) {
		IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
		ps.setValue(libElement.getName(), id);
	}

	/**
	 * Gets the default open listener.
	 * 
	 * @param libElement    the lib element
	 * @param elementToOpen the element to open
	 * 
	 * @return the default open listener
	 */
	public IOpenListener getDefaultOpenListener(final I4DIACElement elementToOpen) {
		IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
		for (IOpenListener openListener : getOpenListeners()) {
			if (listenerSupportsElement(openListener, elementToOpen)) {
				String value = ps.getString(openListener.getHandledClass().getName());
				openListener.selectionChanged(null, new StructuredSelection(elementToOpen));
				if ("".equals(value)) { //$NON-NLS-1$
					return openListener;
				} else if (value.equals(openListener.getOpenListenerID())) {
					return openListener;
				}
			}
		}
		return null;
	}

	/**
	 * Opens the Editor for the specified application
	 * 
	 * @param app the application which should be opened
	 * @return the Editor if opening worked otherwise null
	 */
	public static IEditorPart openEditor(I4DIACElement element) {
		IOpenListener openListener = OpenListenerManager.INSTANCE.getDefaultOpenListener(element);
		if (openListener != null) {
			openListener.run(null);
			return openListener.getOpenedEditor();
		}
		return null;
	}

	private void loadOpenListeners() {
		openListeners = new ArrayList<>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elems = registry
				.getConfigurationElementsFor(org.eclipse.fordiac.ide.util.Activator.PLUGIN_ID, "openListener"); //$NON-NLS-1$
		for (IConfigurationElement element : elems) {
			try {
				Object object = element.createExecutableExtension("class"); //$NON-NLS-1$
				if (object instanceof IOpenListener) {
					openListeners.add((IOpenListener) object);
				}
			} catch (CoreException corex) {
				Activator.getDefault().logError(corex.getMessage(), corex);
			}
		}

	}

	private static boolean listenerSupportsElement(IOpenListener listener, I4DIACElement elementtoOpen) {
		return (null != listener.getHandledClass()) && listener.getHandledClass().isInstance(elementtoOpen);
	}

}
