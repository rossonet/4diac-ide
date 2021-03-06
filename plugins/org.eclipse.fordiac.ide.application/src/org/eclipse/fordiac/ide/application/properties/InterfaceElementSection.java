/*******************************************************************************
 * Copyright (c) 2016, 2017 fortiss GmbH
 * 				 2019 Johannes Kepler University Linz	
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Monika Wenger, Alois Zoitl
 *     - initial API and implementation and/or initial documentation
 *   Alois Zoitl - fixed issues in type changes for subapp interface elements  
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.properties;

import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.fordiac.ide.application.Messages;
import org.eclipse.fordiac.ide.application.commands.ChangeSubAppIETypeCommand;
import org.eclipse.fordiac.ide.model.commands.change.ChangeTypeCommand;
import org.eclipse.fordiac.ide.model.commands.delete.DeleteConnectionCommand;
import org.eclipse.fordiac.ide.model.data.DataType;
import org.eclipse.fordiac.ide.model.libraryElement.Connection;
import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement;
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class InterfaceElementSection extends org.eclipse.fordiac.ide.gef.properties.InterfaceElementSection {
	private TreeViewer connectionsTree;
	private Group group;

	@Override
	public void createControls(final Composite parent, final TabbedPropertySheetPage tabbedPropertySheetPage) {
		createSuperControls = false;
		super.createControls(parent, tabbedPropertySheetPage);
		parent.setLayout(new GridLayout(2, true));
		parent.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		createConnectionDisplaySection(parent);
	}

	private void createConnectionDisplaySection(Composite parent) {
		group = getWidgetFactory().createGroup(parent, Messages.InterfaceElementSection_ConnectionGroup);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		connectionsTree = new TreeViewer(group, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.heightHint = 100;
		gridData.widthHint = 80;
		connectionsTree.getTree().setLayoutData(gridData);
		connectionsTree.setContentProvider(new ConnectionContentProvider());
		connectionsTree.setLabelProvider(new AdapterFactoryLabelProvider(getAdapterFactory()));
		connectionsTree.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
		new AdapterFactoryTreeEditor(connectionsTree.getTree(), getAdapterFactory());

		Button delConnection = getWidgetFactory().createButton(group, "", SWT.PUSH); //$NON-NLS-1$
		delConnection.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true));
		delConnection.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_DELETE));
		delConnection.setToolTipText(Messages.InterfaceElementSection_DeleteConnectionToolTip);
		delConnection.addListener(SWT.Selection, event -> {
			Object selection = ((TreeSelection) connectionsTree.getSelection()).getFirstElement();
			if (selection instanceof Connection) {
				executeCommand(new DeleteConnectionCommand((Connection) selection));
				connectionsTree.refresh();
			}
		});
	}

	@Override
	public void refresh() {
		super.refresh();
		CommandStack commandStackBuffer = commandStack;
		commandStack = null;
		if (null != type) {
			if (getType().isIsInput()) {
				group.setText(Messages.InterfaceElementSection_InConnections);
			} else {
				group.setText(Messages.InterfaceElementSection_OutConnections);
			}
			connectionsTree.setInput(getType());
		}
		commandStack = commandStackBuffer;
	}

	@Override
	protected void setInputCode() {
		connectionsTree.setInput(null);
	}

	private static class ConnectionContentProvider implements ITreeContentProvider {
		private IInterfaceElement element;

		@Override
		public Object[] getElements(final Object inputElement) {
			if (inputElement instanceof IInterfaceElement) {
				element = ((IInterfaceElement) inputElement);
				if (element.isIsInput() && null != element.getFBNetworkElement()
						|| (!element.isIsInput() && null == element.getFBNetworkElement())) {
					return element.getInputConnections().toArray();
				} else {
					return element.getOutputConnections().toArray();
				}
			}
			return new Object[] {};
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Connection) {
				Object[] objects = new Object[2];
				if (element.isIsInput()) {
					objects[0] = null != ((Connection) parentElement).getSourceElement()
							? ((Connection) parentElement).getSourceElement()
							: element;
					objects[1] = ((Connection) parentElement).getSource();
				} else {
					objects[0] = null != ((Connection) parentElement).getDestinationElement()
							? ((Connection) parentElement).getDestinationElement()
							: element;
					objects[1] = ((Connection) parentElement).getDestination();
				}
				return objects;
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof Connection) {
				return this.element;
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof Connection) {
				return null != ((Connection) element).getSource() && null != ((Connection) element).getDestination();
			}
			return false;
		}
	}

	@Override
	protected ChangeTypeCommand newChangeTypeCommand(VarDeclaration data, DataType newType) {
		return new ChangeSubAppIETypeCommand(data, newType);
	}
}
