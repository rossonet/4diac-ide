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
package org.eclipse.fordiac.ide.fbtypeeditor.fbtester.model;

/**
 * The listener interface for receiving ITriggerEvent events. The class that is
 * interested in processing a ITriggerEvent event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addTriggerEventListener<code> method. When the
 * ITriggerEvent event occurs, that object's appropriate method is invoked.
 * 
 * @see ITriggerEventEvent
 */
public interface ITriggerEventListener {

	/**
	 * Send event.
	 * 
	 * @param element the element
	 */
	void sendEvent(TestElement element);
}
