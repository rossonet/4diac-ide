/**
 * Copyright (c) 2015 fortiss GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Martin Jobst
 *       - initial API and implementation and/or initial documentation
 */
package org.eclipse.fordiac.ide.model.structuredtext.converter;

import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.conversion.impl.AbstractNullSafeConverter;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class Bool_ValueValueConverter extends AbstractNullSafeConverter<Boolean> {
  @Override
  protected String internalToString(final Boolean value) {
    return value.toString().toUpperCase();
  }
  
  @Override
  protected Boolean internalToValue(final String string, final INode node) throws ValueConverterException {
    Boolean _xblockexpression = null;
    {
      boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(string);
      if (_isNullOrEmpty) {
        throw new ValueConverterException("Couldn\'t convert empty string to a bool value.", node, null);
      }
      _xblockexpression = Boolean.valueOf(string);
    }
    return _xblockexpression;
  }
}
