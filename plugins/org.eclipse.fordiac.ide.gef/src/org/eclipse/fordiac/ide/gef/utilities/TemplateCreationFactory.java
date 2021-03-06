/*******************************************************************************
 * Copyright (c) 2008, 2009, 2013 Profactor GmbH, fortiss GmbH 
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
 *******************************************************************************/
package org.eclipse.fordiac.ide.gef.utilities;

import org.eclipse.gef.requests.CreationFactory;

/**
 * The typeTemplate is used to create a new instance of the typeTemplate.
 */
public class TemplateCreationFactory implements CreationFactory {

	/** The type template. */
	private final Object typeTemplate;

	/**
	 * The Constructor with the specified template.
	 * 
	 * @param typeTemplate the type template
	 */
	public TemplateCreationFactory(final Object typeTemplate) {
		this.typeTemplate = typeTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	@Override
	public Object getNewObject() {
		// TODO implement
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	@Override
	public Object getObjectType() {
		return typeTemplate;
	}

}
