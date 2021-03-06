/*******************************************************************************
 * Copyright (c) 2008, 2009 Profactor GmbH 
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
package org.eclipse.fordiac.ide.gef.router;

import org.eclipse.gef.EditPolicy;

public interface BendpointPolicyRouter {
	/**
	 * 
	 * @return a bendpoint editpolicy suitable for the router returned in
	 *         getConnectionRouter
	 */
	EditPolicy getBendpointPolicy(final Object modelObject);
}
