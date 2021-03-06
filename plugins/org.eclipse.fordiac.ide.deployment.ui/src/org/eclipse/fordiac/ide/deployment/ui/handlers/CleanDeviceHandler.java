/*******************************************************************************
 * Copyright (c) 2017 fortiss GmbH
 * 				 2018 Johannes Kepler University 
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Jose Cabral, Alois Zoitl
 *     - initial API and implementation and/or initial documentation
 *   Alois Zoitl - introduce abstract base class for devcie deployment commands  
 *******************************************************************************/
package org.eclipse.fordiac.ide.deployment.ui.handlers;

import org.eclipse.fordiac.ide.deployment.exceptions.DeploymentException;
import org.eclipse.fordiac.ide.deployment.interactors.IDeviceManagementInteractor;
import org.eclipse.fordiac.ide.deployment.ui.Messages;
import org.eclipse.fordiac.ide.model.libraryElement.Resource;

/**
 * The Class SendClearAction.
 */
public class CleanDeviceHandler extends AbstractDeviceDeploymentCommand {

	@Override
	protected void executeCommand(IDeviceManagementInteractor executor) throws DeploymentException {
		for (Resource res : getDevice().getResource()) {
			if (!res.isDeviceTypeResource()) {
				executor.deleteResource(res.getName());
			}
		}
	}

	@Override
	protected String getErrorMessageHeader() {
		return Messages.CleanDeviceHandler_CleanDeviceError;
	}

}
