/*******************************************************************************
 * Copyright (c) 2016 fortiss GmbH
 * 				 2019 Johannes Kepler University Linz
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Alois Zoitl - initial API and implementation and/or initial documentation
 *   Alois Zoitl - moved getCurrentActiveEditor and openEditor helper functions 
 *   			   to EditorUtils  
 *******************************************************************************/
package org.eclipse.fordiac.ide.ui.editors;

import org.eclipse.fordiac.ide.ui.UIPlugin;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public final class EditorUtils {

	public static final EditorAction CloseEditor = (IEditorPart part) -> PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage().closeEditor(part, false);

	private EditorUtils() {
		throw new AssertionError();
	}

	public static IEditorPart getCurrentActiveEditor() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null && window.getActivePage() != null) {
			return window.getActivePage().getActiveEditor();
		}
		return null;
	}

	public static IEditorPart openEditor(IEditorInput input, String editorId) {
		IEditorPart editor = null;
		IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			editor = activePage.openEditor(input, editorId);
		} catch (PartInitException e) {
			editor = null;
			UIPlugin.getDefault().logError(e.getMessage(), e);
		}
		return editor;
	}

	public static IEditorPart findEditor(final EditorFilter filter) {
		IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();

		for (IEditorReference editorReference : editorReferences) {
			IEditorPart editor = editorReference.getEditor(false);
			if (null != editor && filter.filter(editor)) {
				return editor;
			}
		}
		return null;
	}

	public static void forEachOpenEditorFiltered(final EditorFilter filter, final EditorAction action) {
		IEditorReference[] editorReferences = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences();

		for (IEditorReference editorReference : editorReferences) {
			IEditorPart editor = editorReference.getEditor(false);
			if (null != editor && filter.filter(editor)) {
				action.run(editor);
			}
		}
	}

	public static void closeEditorsFiltered(final EditorFilter filter) {
		forEachOpenEditorFiltered(filter, CloseEditor);
	}
}
