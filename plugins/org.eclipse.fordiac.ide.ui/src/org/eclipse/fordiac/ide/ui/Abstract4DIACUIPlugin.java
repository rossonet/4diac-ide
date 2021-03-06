/*******************************************************************************
 * Copyright (c) 2014 - 2017 fortiss GmbH, TU Wien ACIN
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Alois Zoitl, Martin Melik Merkumians, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public abstract class Abstract4DIACUIPlugin extends AbstractUIPlugin {

	public String getID() {
		return getBundle().getSymbolicName();
	}

	public void logError(String msg, Exception e) {
		getLog().log(new Status(IStatus.ERROR, getID(), msg, e));
	}

	public void logError(String msg) {
		getLog().log(new Status(IStatus.ERROR, getID(), msg));
	}

	public void logWarning(String msg, Exception e) {
		getLog().log(new Status(IStatus.WARNING, getID(), msg, e));
	}

	public void logWarning(String msg) {
		getLog().log(new Status(IStatus.WARNING, getID(), msg));
	}

	public void logInfo(String msg) {
		getLog().log(new Status(IStatus.INFO, getID(), msg));
	}

	public static void statusLineMessage(IWorkbenchPart part, final String msg) {
		final IStatusLineManager manager = getStatusLineManager(part);
		if (null != manager) {
			final Display display = getDisplay();
			display.asyncExec(() -> manager.setErrorMessage(msg));
		}
	}

	public static void statusLineErrorMessage(IWorkbenchPart part, final String errorMsg) {
		final IStatusLineManager manager = getStatusLineManager(part);

		if (null != manager) {
			final Display display = getDisplay();
			display.asyncExec(() -> manager.setErrorMessage(errorMsg));
		}
	}

	public static void statusLineErrorMessage(final String errorMsg) {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (null != workbenchWindow) {
			statusLineErrorMessage(workbenchWindow.getActivePage().getActivePart(), errorMsg);
		} else {
			final Display display = Display.getDefault();
			display.asyncExec(() -> statusLineErrorMessage(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart(), errorMsg));
		}
	}

	private static Display getDisplay() {
		return PlatformUI.isWorkbenchRunning() ? PlatformUI.getWorkbench().getDisplay() : Display.getDefault();
	}

	private static IStatusLineManager getStatusLineManager(IWorkbenchPart part) {
		IStatusLineManager retval = null;
		IActionBars actionbars = null;

		if (part instanceof IViewPart) {
			actionbars = ((IViewPart) part).getViewSite().getActionBars();
		} else if (part instanceof IEditorPart) {
			actionbars = ((IEditorPart) part).getEditorSite().getActionBars();
		}

		if (null != actionbars) {
			retval = actionbars.getStatusLineManager();
		}
		return retval;
	}
}
