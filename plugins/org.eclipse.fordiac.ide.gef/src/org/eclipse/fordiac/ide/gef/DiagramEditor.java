/*******************************************************************************
 * Copyright (c) 2008 - 2016 Profactor GbmH, TU Wien ACIN, fortiss GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Waldemar Eisenmenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.gef;

import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.fordiac.ide.gef.dnd.ParameterDropTargetListener;
import org.eclipse.fordiac.ide.gef.editparts.ZoomScalableFreeformRootEditPart;
import org.eclipse.fordiac.ide.gef.handles.AdvancedGraphicalViewerKeyHandler;
import org.eclipse.fordiac.ide.gef.print.PrintPreviewAction;
import org.eclipse.fordiac.ide.gef.ruler.FordiacRulerComposite;
import org.eclipse.fordiac.ide.gef.tools.AdvancedPanningSelectionTool;
import org.eclipse.fordiac.ide.model.libraryElement.AutomationSystem;
import org.eclipse.fordiac.ide.systemmanagement.SystemManager;
import org.eclipse.fordiac.ide.ui.editors.I4diacModelEditor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * A base editor for various graphical editors.
 *
 * @author Gerhard Ebenhofer (gerhard.ebenhofer@profactor.at)
 */
public abstract class DiagramEditor extends GraphicalEditor
		implements ITabbedPropertySheetPageContributor, I4diacModelEditor {

	/** The PROPERTY_CONTRIBUTOR_ID. */
	public static final String PROPERTY_CONTRIBUTOR_ID = "org.eclipse.fordiac.ide.application.editors.DiagramEditor"; //$NON-NLS-1$

	/** The shared key handler. */
	private KeyHandler sharedKeyHandler;

	/** The outline page. */
	private DiagramOutlinePage outlinePage;

	/**
	 * Instantiates a new diagram editor.
	 */
	public DiagramEditor() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#commandStackChanged(java.util
	 * .EventObject)
	 */
	@Override
	public void commandStackChanged(final EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	/**
	 * refresh all child editparts when editor gets focus.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setFocus() {
		super.setFocus();
		for (Iterator iter = getGraphicalViewer().getRootEditPart().getChildren().iterator(); iter.hasNext();) {
			EditPart ep = (EditPart) iter.next();
			ep.refresh();
		}
	}

	@Override
	protected void createGraphicalViewer(final Composite parent) {
		RulerComposite rulerComp = new FordiacRulerComposite(parent, SWT.NONE);

		GraphicalViewer viewer = new AdvancedScrollingGraphicalViewer();
		viewer.createControl(rulerComp);
		setGraphicalViewer(viewer);
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();

		rulerComp.setGraphicalViewer((ScrollingGraphicalViewer) getGraphicalViewer());
	}

	/**
	 * Create the root edit part used in this diagram editor. Editors which need
	 * special behavior should override this function
	 *
	 * @return the new root edit part
	 */
	protected ScalableFreeformRootEditPart createRootEditPart() {
		return new ZoomScalableFreeformRootEditPart(getSite(), getActionRegistry());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		AdvancedScrollingGraphicalViewer viewer = (AdvancedScrollingGraphicalViewer) getGraphicalViewer();

		ScalableFreeformRootEditPart root = createRootEditPart();

		ContextMenuProvider cmp = getContextMenuProvider(viewer, root.getZoomManager());
		if (null != cmp) {
			viewer.setContextMenu(cmp);
			getSite().registerContextMenu("org.eclipse.fordiac.ide.gef.contextmenu", //$NON-NLS-1$
					cmp, viewer);
		}

		viewer.setRootEditPart(root);
		viewer.setEditPartFactory(getEditPartFactory());

		AdvancedGraphicalViewerKeyHandler keyHandler = new AdvancedGraphicalViewerKeyHandler(viewer);
		keyHandler.setParent(getCommonKeyHandler());
		viewer.setKeyHandler(keyHandler);

		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
	}

	public ZoomManager getZoomManger() {
		return ((ScalableFreeformRootEditPart) (getGraphicalViewer().getRootEditPart())).getZoomManager();
	}

	/**
	 * Gets the edits the part factory.
	 *
	 * @return the edits the part factory
	 */
	protected abstract EditPartFactory getEditPartFactory();

	/**
	 * Gets the context menu provider.
	 *
	 * @param viewer the viewer
	 * @param root   the root
	 *
	 * @return the context menu provider
	 */
	protected abstract ContextMenuProvider getContextMenuProvider(ScrollingGraphicalViewer viewer,
			ZoomManager zoomManager);

	/**
	 * Creates the transfer drop target listener.
	 *
	 * @return the transfer drop target listener
	 */
	protected abstract TransferDropTargetListener createTransferDropTargetListener();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(getModel());
		// listen for dropped parts
		TransferDropTargetListener listener = createTransferDropTargetListener();
		if (listener != null) {
			viewer.addDropTargetListener(createTransferDropTargetListener());
		}
		// enable drag from palette
		getGraphicalViewer().addDropTargetListener(new TemplateTransferDropTargetListener(getGraphicalViewer()));
		viewer.addDropTargetListener(new ParameterDropTargetListener(getGraphicalViewer()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setModel(input);
		super.init(site, input);
	}

	/**
	 * Sets the model.
	 *
	 * @param input the new model
	 */
	protected void setModel(final IEditorInput input) {
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setDefaultTool(createDefaultTool());
		getEditDomain().setActiveTool(getEditDomain().getDefaultTool());
		// use one "System - Wide" command stack to avoid incositensies due to
		// undo redo
		getEditDomain().setCommandStack(SystemManager.INSTANCE.getCommandStack(getSystem()));
	}

	@SuppressWarnings("static-method")
	protected AdvancedPanningSelectionTool createDefaultTool() {
		return new AdvancedPanningSelectionTool();
	}

	/**
	 * Gets the system.
	 *
	 * @return the system
	 */
	public abstract AutomationSystem getSystem();

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public abstract String getFileName();

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public abstract void doSave(final IProgressMonitor monitor);
// TODO model refactoring - consider if a generic save would be possible here
//	{
//		// TODO __gebenh error handling if save fails!
//
//		SystemManager.getInstance().saveDiagram(getDiagramModel(), getSystem(),
//				getFileName());
//
//		getCommandStack().markSaveLocation();
//		firePropertyChange(IEditorPart.PROP_DIRTY);
//	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// not implemented
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * Gets the common key handler.
	 *
	 * @return the common key handler
	 */
	protected KeyHandler getCommonKeyHandler() {
		if (sharedKeyHandler == null) {
			sharedKeyHandler = new KeyHandler();
			sharedKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
					getActionRegistry().getAction(ActionFactory.DELETE.getId()));
			sharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
					getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
//			sharedKeyHandler.put(/* CTRL + '=' */
//					KeyStroke.getPressed('+', 0x3d, SWT.CTRL),
//					getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));

		}
		return sharedKeyHandler;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getAdapter(
	 * java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class type) {
		if (type == ZoomManager.class) {
			return getGraphicalViewer().getProperty(ZoomManager.class.toString());
		}
		if (type == IContentOutlinePage.class) {
			outlinePage = new DiagramOutlinePage(getGraphicalViewer());
			return outlinePage;
		}
		if (type == IPropertySheetPage.class) {
			return new TabbedPropertySheetPage(this);
		}
		return super.getAdapter(type);
	}

	/**
	 * Gets the editor.
	 *
	 * @return the editor
	 */
	protected FigureCanvas getEditor() {
		return (FigureCanvas) getGraphicalViewer().getControl();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void createActions() {
		ActionRegistry registry = getActionRegistry();
		IAction action;

		action = new DirectEditAction((IWorkbenchPart) this);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.LEFT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.RIGHT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.TOP);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.BOTTOM);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.CENTER);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.MIDDLE);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		super.createActions();

		// remove the default print action and register our own one
		registry.removeAction(registry.getAction(ActionFactory.PRINT.getId()));
		action = new PrintPreviewAction(getGraphicalViewer());
		registry.registerAction(action);
		getEditorSite().getActionBars().setGlobalActionHandler(ActionFactory.PRINT.getId(), action);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#getSelectionActions()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected List getSelectionActions() {
		return super.getSelectionActions();
	}

	/**
	 * Gets the sel actions.
	 *
	 * @return the sel actions
	 */
	@SuppressWarnings("rawtypes")
	public List getSelActions() {
		return getSelectionActions();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		outlinePage = null;
		super.dispose();

	}

	/**
	 * Returns the GraphicalViewer of this Editor.
	 *
	 * @return the GraphicalViewer
	 */
	public GraphicalViewer getViewer() {
		return getGraphicalViewer();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor
	 * #getContributorId()
	 */
	@Override
	public String getContributorId() {
		return PROPERTY_CONTRIBUTOR_ID;
	}

}
