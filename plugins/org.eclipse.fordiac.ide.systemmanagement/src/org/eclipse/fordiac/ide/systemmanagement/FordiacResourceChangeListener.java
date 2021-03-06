/*******************************************************************************
 * Copyright (c) 2012 - 2017 TU Wien ACIN, Profactor GmbH, fortiss GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Alois Zoitl, Gerhard Ebenhofer, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.systemmanagement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.fordiac.ide.model.Palette.Palette;
import org.eclipse.fordiac.ide.model.Palette.PaletteEntry;
import org.eclipse.fordiac.ide.model.dataexport.AbstractTypeExporter;
import org.eclipse.fordiac.ide.model.libraryElement.AutomationSystem;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElement;
import org.eclipse.fordiac.ide.model.typelibrary.TypeLibrary;
import org.eclipse.fordiac.ide.model.typelibrary.TypeLibraryTags;
import org.eclipse.fordiac.ide.ui.editors.EditorUtils;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;

public class FordiacResourceChangeListener implements IResourceChangeListener {

	private static final String XML_FILE_EXTENSION = "xml"; //$NON-NLS-1$

	/** The instance. */
	private SystemManager systemManager;

	public FordiacResourceChangeListener(SystemManager systemManager) {
		this.systemManager = systemManager;
	}

	// ! buffer containing systems scheduled for import
	private Set<String> systemImportWatingList = Collections.synchronizedSet(new HashSet<String>());

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {

			IResourceDelta rootDelta = event.getDelta();
			// get the delta, if any, for the documentation directory

			IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
				@Override
				public boolean visit(final IResourceDelta delta) {
					switch (delta.getKind()) {
					case IResourceDelta.CHANGED:
						if (IResourceDelta.OPEN == delta.getFlags()) {
							// project is opend oder closed
							if (0 != delta.getAffectedChildren(IResourceDelta.ADDED).length) {
								loadSystem(delta.getResource().getProject());
							} else if (0 != delta.getAffectedChildren(IResourceDelta.REMOVED).length) {
								handleProjectRemove(delta);
								return false;
							}
							return false;
						}
						break;
					case IResourceDelta.REMOVED:
						if (delta.getFlags() == IResourceDelta.MOVED_TO) {
							// we will handle movement only on the add side
							return false;
						}
						switch (delta.getResource().getType()) {
						case IResource.FILE:
							handleFileDelete(delta);
							break;
						case IResource.PROJECT:
							handleProjectRemove(delta);
							return false;
						default:
							// we don't need to do anything in the other cases
							break;
						}
						break;
					case IResourceDelta.ADDED:
						if (IResourceDelta.MOVED_FROM == (delta.getFlags() & IResourceDelta.MOVED_FROM)) {
							switch (delta.getResource().getType()) {
							case IResource.FILE:
								handleFileMove(delta);
								return false;
							case IResource.PROJECT:
								handleProjectRename(delta);
								break;
							default:
								break;
							}
							return true;

						}

						final String projectName = delta.getResource().getProject().getName();
						AutomationSystem system = systemManager.getSystemForName(projectName);
						if ((null == system) && (!TypeLibraryTags.TOOL_LIBRARY_PROJECT_NAME.equals(projectName))) {
							loadSystem(delta.getResource().getProject());
						}

						if ((null != system) || (projectName.equals(TypeLibraryTags.TOOL_LIBRARY_PROJECT_NAME))) {
							switch (delta.getResource().getType()) {
							case IResource.FILE:
								handleFileCopy(delta);
								break;
							case IResource.FOLDER:
								// if a folder has been moved we need to update
								// the IFile of the children
								return true;
							default:
								break;
							}
							return false;
						} else {
							if (IResource.FILE == delta.getResource().getType()) {
								handledCopiedProjectFiles(delta);
							}
						}
						break;
					}
					return true;
				}

			};
			try {
				rootDelta.accept(visitor);
			} catch (CoreException e) {
				// open error dialog with syncExec or print to plugin log file
			}
		}
	}

	protected void handleFileDelete(IResourceDelta delta) {
		Palette palette = systemManager.getPalette(delta.getResource().getProject());
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getResource().getFullPath());

		PaletteEntry entry = TypeLibrary.getPaletteEntryForFile(file, palette);
		if (null != entry) {
			closeAllFBTypeEditor(entry);
			palette.removePaletteEntry(entry);
		}
	}

	protected void handleFileCopy(IResourceDelta delta) {
		Palette dstPalette = systemManager.getPalette(delta.getResource().getProject());
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getResource().getFullPath());

		if (!TypeLibrary.paletteContainsType(dstPalette, file)) {
			PaletteEntry entry = TypeLibrary.createPaleteEntry(dstPalette, file);
			if (null != entry) {
				updatePaletteEntry(file, entry);
			}
		}
	}

	protected void handledCopiedProjectFiles(IResourceDelta delta) {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getResource().getFullPath());

		if ((null != file) && (null != file.getFileExtension())
				&& (XML_FILE_EXTENSION.equalsIgnoreCase(file.getFileExtension()))) {
			handleSystemFileCopy(file);
		}
	}

	protected void handleSystemFileCopy(final IFile file) {
		Scanner scanner;
		try {
			scanner = new Scanner(file.getContents());
			if (null != scanner.findWithinHorizon("<libraryElement:AutomationSystem", 0)) { //$NON-NLS-1$
				// it is an Automation system
				final IProject project = file.getProject();
				if (!file.getName().equals(file.getProject().getName() + ".xml")) { //$NON-NLS-1$
					WorkspaceJob job = new WorkspaceJob("Renaming system file") {
						@Override
						public IStatus runInWorkspace(IProgressMonitor monitor) {
							// do the actual work in here

							IPath path = file.getProjectRelativePath();
							path = path.removeLastSegments(1);
							path = path.append(project.getName() + SystemManager.SYSTEM_FILE_ENDING);
							try {
								file.move(path, true, null);
								// TODO model refactoring - should we remove the old system first?
								// a basic load should be sufficient to update the system configuration
								systemManager.loadProject(project);
							} catch (Exception e) {
								Activator.getDefault().logError(e.getMessage(), e);
							}
							return Status.OK_STATUS;
						}
					};
					job.setRule(project);
					job.schedule();
				}
			}
		} catch (CoreException e) {
			Activator.getDefault().logError(e.getMessage(), e);
		}
	}

	private void handleFileMove(IResourceDelta delta) {
		IFile src = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getMovedFromPath());

		if (src.getParent().equals(delta.getResource().getParent())) {
			handleFileRename(delta, src);
		}

		final AutomationSystem system = systemManager.getSystemForName(src.getProject().getName());
		if (null != system) {
			WorkspaceJob job = new WorkspaceJob("Save system: " + system.getName() + " after type movement") {
				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) {
					// do the actual work in here
					SystemManager.INSTANCE.saveSystem(system);
					return Status.OK_STATUS;
				}
			};
			job.setRule(src.getProject());
			job.schedule();

		}
	}

	private void handleFileRename(IResourceDelta delta, IFile src) {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(delta.getResource().getFullPath());
		Palette dstPalette = systemManager.getPalette(file.getProject());
		PaletteEntry entry = TypeLibrary.getPaletteEntryForFile(src, dstPalette);
		updatePaletteEntry(file, entry);
	}

	private static void updatePaletteEntry(final IFile newFile, final PaletteEntry entry) {
		if (null != entry) {
			String newTypeName = TypeLibrary.getTypeNameFromFile(newFile);
			entry.setLabel(newTypeName);
			entry.setFile(newFile);

			WorkspaceJob job = new WorkspaceJob("Save Renamed type: " + entry.getLabel()) {
				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) {
					// do the actual work in here
					final LibraryElement type = entry.getType();
					if ((null != type) && // this means we couldn't load the type seems
					// like a problem in the type's XML file
					// TODO report on error
					(!newTypeName.equals(type.getName()))) {
						type.setName(newTypeName);
						AbstractTypeExporter.saveType(entry);
					}
					return Status.OK_STATUS;
				}
			};
			job.setRule(newFile.getProject());
			job.schedule();
		}
	}

	private void loadSystem(final IProject project) {
		final String projectName = project.getName();
		if (!systemImportWatingList.contains(projectName)) {
			systemImportWatingList.add(projectName);
			WorkspaceJob job = new WorkspaceJob("Load system: " + projectName) {
				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) {
					// do the actual work in here
					AutomationSystem system = systemManager.loadProject(project);
					if ((null != system) && (!system.getName().equals(projectName))) {
						// we have been copied set the system name and correctly save it
						renameSystem(system, project);
					}
					systemManager.notifyListeners();
					// loading of the system has finished we can remove it from the list
					systemImportWatingList.remove(projectName);
					return Status.OK_STATUS;
				}
			};
			job.setRule(project);
			job.schedule();
		}
	}

	private void handleProjectRename(final IResourceDelta delta) {
		IPath srcPath = delta.getMovedFromPath();

		if (null != srcPath) {

			IResource src = ResourcesPlugin.getWorkspace().getRoot().getProject(srcPath.lastSegment());

			final AutomationSystem system = systemManager.getSystemForName(src.getName());
			IProject project = delta.getResource().getProject();
			if (null != system) {
				renameSystem(system, project);
			}
		}

	}

	protected static void renameSystem(final AutomationSystem system, final IProject project) {
		IFile oldSystemFile = project.getFile(system.getName() + SystemManager.SYSTEM_FILE_ENDING);
		String newProjectName = project.getName();
		system.setName(newProjectName);
		system.setProject(project); // update to the new project

		WorkspaceJob job = new WorkspaceJob("Save system configuration: " + newProjectName) {
			@Override
			public IStatus runInWorkspace(IProgressMonitor monitor) {
				// do the actual work in here
				try {
					// try to remove the old file
					oldSystemFile.delete(true, null);
				} catch (CoreException e) {
					Activator.getDefault().logError(e.getMessage(), e);
				}
				// save the system with the new name and the new system file
				SystemManager.INSTANCE.saveSystem(system);
				return Status.OK_STATUS;
			}
		};
		job.setRule(project);
		job.schedule();
	}

	protected void handleProjectRemove(IResourceDelta delta) {
		IProject project = delta.getResource().getProject();
		AutomationSystem system = systemManager.getSystemForName(project.getName());
		if (null != system) {
			closeAllEditors(system);
			// remove system from system manager
			systemManager.removeSystem(system);
		}
	}

	private static void closeAllEditors(final AutomationSystem refSystem) {
		// display related stuff needs to run in a display thread
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				EditorUtils.closeEditorsFiltered((IEditorPart editor) -> {
					return (editor instanceof ISystemEditor)
							&& (refSystem.equals(((ISystemEditor) editor).getSystem()));
				});
			}
		});

	}

	private static void closeAllFBTypeEditor(final PaletteEntry entry) {
		// display related stuff needs to run in a display thread
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IFile file = entry.getFile();
				EditorUtils.closeEditorsFiltered((IEditorPart editor) -> {
					IEditorInput input = editor.getEditorInput();
					return (input instanceof FileEditorInput) && (file.equals(((FileEditorInput) input).getFile()));
				});
			}
		});

	}
}
