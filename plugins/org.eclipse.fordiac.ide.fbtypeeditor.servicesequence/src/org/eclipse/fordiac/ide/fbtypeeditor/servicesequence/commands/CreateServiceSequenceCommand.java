/*******************************************************************************
 * Copyright (c) 2014 fortiss GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.servicesequence.commands;

import org.eclipse.fordiac.ide.model.libraryElement.FBType;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementFactory;
import org.eclipse.fordiac.ide.model.libraryElement.ServiceInterface;
import org.eclipse.fordiac.ide.model.libraryElement.ServiceSequence;
import org.eclipse.gef.commands.Command;

public class CreateServiceSequenceCommand extends Command {

	private FBType fbType;
	private String name;
	private String leftInterfaceName;
	private String rightInterfaceName;
	private ServiceInterface leftInterface;
	private ServiceInterface rightInterface;
	private ServiceSequence sq;

	public CreateServiceSequenceCommand(FBType fbType) {
		this.fbType = fbType;
		name = "Service Sequence"; //$NON-NLS-1$
		leftInterfaceName = "left interface"; //$NON-NLS-1$
		rightInterfaceName = "right interface"; //$NON-NLS-1$
	}

	@Override
	public boolean canExecute() {
		return fbType != null;
	}

	@Override
	public void execute() {
		if (null == fbType.getService()) {
			fbType.setService(LibraryElementFactory.eINSTANCE.createService());
		}
		sq = LibraryElementFactory.eINSTANCE.createServiceSequence();
		sq.setName(name);
		if (fbType.getService().getLeftInterface() == null) {
			leftInterface = LibraryElementFactory.eINSTANCE.createServiceInterface();
			leftInterface.setName(leftInterfaceName);
			fbType.getService().setLeftInterface(leftInterface);
		}
		if (fbType.getService().getRightInterface() == null) {
			rightInterface = LibraryElementFactory.eINSTANCE.createServiceInterface();
			rightInterface.setName(rightInterfaceName);
			fbType.getService().setRightInterface(rightInterface);
		}
		fbType.getService().getServiceSequence().add(sq);

	}

	@Override
	public void undo() {
		if (leftInterface != null) {
			fbType.getService().setLeftInterface(null);
		}
		if (rightInterface != null) {
			fbType.getService().setRightInterface(null);
		}
		fbType.getService().getServiceSequence().remove(sq);

	}

	@Override
	public void redo() {
		if (leftInterface != null) {
			fbType.getService().setLeftInterface(leftInterface);
		}
		if (rightInterface != null) {
			fbType.getService().setRightInterface(rightInterface);
		}
		fbType.getService().getServiceSequence().add(sq);
	}
}
