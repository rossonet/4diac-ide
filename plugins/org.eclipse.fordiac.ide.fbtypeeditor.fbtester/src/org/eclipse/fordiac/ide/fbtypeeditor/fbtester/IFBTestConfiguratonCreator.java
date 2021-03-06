/*******************************************************************************
 * Copyright (c) 2012 Profactor GmbH
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer
 *    - initial implementation
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.fbtester;

import org.eclipse.fordiac.ide.fbtypeeditor.fbtester.model.ISetValueListener;
import org.eclipse.fordiac.ide.fbtypeeditor.fbtester.model.ITriggerEventListener;
import org.eclipse.fordiac.ide.model.libraryElement.FBType;
import org.eclipse.swt.widgets.Composite;

public interface IFBTestConfiguratonCreator extends ISetValueListener, ITriggerEventListener {

	IFBTestConfiguration createConfigurationPage(Composite parent);

	void setType(FBType type);

}
