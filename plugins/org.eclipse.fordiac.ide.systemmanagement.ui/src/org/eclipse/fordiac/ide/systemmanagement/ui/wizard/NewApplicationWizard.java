/*******************************************************************************
 * Copyright (c) 2009 - 2017 Profactor GmbH, TU Wien ACIN, fortiss GmbH
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
 *   Alois Zoitl - reworked selection check code to handle more cases correctly  
 *******************************************************************************/
package org.eclipse.fordiac.ide.systemmanagement.ui.wizard;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.fordiac.ide.model.libraryElement.Application;
import org.eclipse.fordiac.ide.model.libraryElement.AutomationSystem;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.SystemConfiguration;
import org.eclipse.fordiac.ide.systemmanagement.ui.Activator;
import org.eclipse.fordiac.ide.systemmanagement.ui.Messages;
import org.eclipse.fordiac.ide.systemmanagement.ui.commands.NewAppCommand;
import org.eclipse.fordiac.ide.typemanagement.navigator.TypeLibRootElement;
import org.eclipse.fordiac.ide.util.OpenListenerManager;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

/**
 * The Class NewApplicationWizard.
 * 
 * @author Gerhard Ebenhofer (gerhard.ebenhofer@profactor.at)
 */
public class NewApplicationWizard extends Wizard implements INewWizard {

	/** The Constant NEW_IEC61499_APP_DESC. */
	private static final String NEW_IEC61499_APP_DESC = Messages.NewApplicationWizard_Description;

	/** The Constant NEW_IEC61499_APPLICATION. */
	private static final String NEW_IEC61499_APPLICATION = Messages.NewApplicationWizard_Title;

	/** The page. */
	private NewApplicationPage page;

	private AutomationSystem system;

	/**
	 * Instantiates a new new application wizard.
	 */
	public NewApplicationWizard() {
		setWindowTitle(Messages.NewApplicationWizard_Title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		performApplicationCreation(page.getSelectedSystem(), page.getFileName(), page.getOpenApplication(), getShell());
		return true;
	}

	/**
	 * This method performs the creation process and opens the editor if necessary
	 * 
	 * This method has been extracted as static method so that other wizards like
	 * the system creation wizard can utilize this code as well.
	 * 
	 * @param system          the system where the application should be created
	 * @param appName         the application name for the new application
	 * @param openApplication boolean flag indicating if the editor for this
	 *                        application should be opened after creation
	 * @param shell           the wizard's shell invoking this method
	 */
	public static void performApplicationCreation(AutomationSystem system, String appName, boolean openApplication,
			Shell shell) {
		NewAppCommand cmd = new NewAppCommand(system, appName, Messages.NewApplicationWizard_Comment);

		// TODO check how to get the command stack here getCommandStack().execute(cmd);

		IWorkbench workbench = PlatformUI.getWorkbench();
		IOperationHistory operationHistory = workbench.getOperationSupport().getOperationHistory();
		IUndoContext undoContext = workbench.getOperationSupport().getUndoContext();
		cmd.addContext(undoContext);

		try {
			operationHistory.execute(cmd, null, WorkspaceUndoUtil.getUIInfoAdapter(shell));
			Application app = cmd.getApplication();
			if (openApplication && (null != app)) {
				OpenListenerManager.openEditor(app);
			}

		} catch (ExecutionException e) {
			Activator.getDefault().logError(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	@Override
	public void addPages() {
		page = new NewApplicationPage(NEW_IEC61499_APPLICATION);
		page.setTitle(NEW_IEC61499_APPLICATION);
		page.setDescription(NEW_IEC61499_APP_DESC);
		addPage(page);
		page.setSystem(system);
	}

	@Override
	public void init(final IWorkbench workbench, final IStructuredSelection selection) {
		system = null;
		if (null != selection) {
			Object selObj = selection.getFirstElement();
			if (selObj instanceof EditPart) {
				selObj = ((EditPart) selObj).getModel();
			}

			system = getSystemFromSelectedObject(selObj);
		}
	}

	private static AutomationSystem getSystemFromSelectedObject(Object selObj) {
		if (selObj instanceof FBNetwork) {
			return ((FBNetwork) selObj).getAutomationSystem();
		}
		if (selObj instanceof AutomationSystem) {
			return (AutomationSystem) selObj;
		}
		if (selObj instanceof Application) {
			return ((Application) selObj).getAutomationSystem();
		}
		if (selObj instanceof SystemConfiguration) {
			return ((SystemConfiguration) selObj).getAutomationSystem();
		}
		if (selObj instanceof TypeLibRootElement) {
			return ((TypeLibRootElement) selObj).getSystem();
		}
		return null;
	}

}
