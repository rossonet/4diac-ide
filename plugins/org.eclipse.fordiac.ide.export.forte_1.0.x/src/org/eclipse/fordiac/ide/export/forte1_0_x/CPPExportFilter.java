/*******************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GmbH, TU Wien ACIN, fortiss GmbH,
 * 			2018, TU Wien/ACIN
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Monika Wenger, Ingo Hegny, Martin Jobst
 *     - initial API and implementation and/or initial documentation
 *   Martin Melik Merkumians - adds export for SimpleFB
 *******************************************************************************/
package org.eclipse.fordiac.ide.export.forte1_0_x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.fordiac.ide.export.ICompareEditorOpener;
import org.eclipse.fordiac.ide.export.IExportFilter;
import org.eclipse.fordiac.ide.export.utils.CompareEditorOpenerUtil;
import org.eclipse.fordiac.ide.model.libraryElement.FBType;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElement;
import org.eclipse.fordiac.ide.util.Utils;
import org.eclipse.jface.dialogs.IDialogLabelKeys;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class ExportFilter.
 */
public abstract class CPPExportFilter implements IExportFilter {

	protected Element docel;

	protected String destDir;

	protected PrintWriter pwCPP;

	protected PrintWriter pwH;

	protected String name;

	private Map<String, VarDefinition> vars = new HashMap<>();

	protected int dataInCount;

	protected int dataOutCount;
	protected int internalCount;
	protected int fBCount;

	protected List<String> inputVars = new ArrayList<>();
	protected List<String> outputVars = new ArrayList<>();
	protected List<VarDefinition> internalVars = new ArrayList<>();

	protected List<FBDefinition> funtionBlocks = new ArrayList<>();

	protected Set<String> internal2InterfaceEventConns = new HashSet<>();

	protected List<Element> eventConn = new ArrayList<>();

	protected Set<String> eventConnHash = new HashSet<>();

	protected List<Element> dataConn = new ArrayList<>();

	protected Set<String> dataConnHash = new HashSet<>();

	protected Set<String> interface2InternalDataConns = new HashSet<>();

	protected Set<String> internal2InterfaceDataConns = new HashSet<>();

	protected List<String> forteEmitterErrors = new ArrayList<>();

	protected List<String> forteEmitterWarnings = new ArrayList<>();

	protected List<String> forteEmitterInfos = new ArrayList<>();

	protected LibraryElement libraryType;

	public Map<String, VarDefinition> getVars() {
		return vars;
	}

	/**
	 * The Class VarDefinition.
	 */
	public static class VarDefinition {

		/** The name. */
		private String name;

		/** The type. */
		private String type;

		/** The array size. */
		private int arraySizeValue;

		/** The initial value. */
		private String initialValue;

		/** The number of the var e.g., 0 means the first input */
		private int count;

		/**
		 * defines if it is an input: 0, output: 1, or an internal var: 2 FIXME change
		 * to enum
		 */
		private int inOutInternal;

		public VarDefinition(Element el, int count, int inoutinternal) {
			name = el.getAttribute("Name"); //$NON-NLS-1$
			type = el.getAttribute("Type"); //$NON-NLS-1$
			String arraySize = el.getAttribute("ArraySize"); //$NON-NLS-1$
			int val = 0;

			if (null != arraySize && !"".equals(arraySize)) { //$NON-NLS-1$
				val = Integer.parseInt(arraySize);
			}
			arraySizeValue = val;

			initialValue = el.getAttribute("InitialValue"); //$NON-NLS-1$
			this.count = count;
			this.inOutInternal = inoutinternal;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getArraySizeValue() {
			return arraySizeValue;
		}

		public void setArraySizeValue(int arraySizeValue) {
			this.arraySizeValue = arraySizeValue;
		}

		public String getInitialValue() {
			return initialValue;
		}

		public void setInitialValue(String initialValue) {
			this.initialValue = initialValue;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getInOutInternal() {
			return inOutInternal;
		}

		public void setInOutInternal(int inOutInternal) {
			this.inOutInternal = inOutInternal;
		}

	}

	protected static class FBDefinition {
		private String name;

		private String type;

		private String[] paramName;

		private String[] paramValue;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String[] getParamName() {
			return paramName;
		}

		public void setParamName(String[] paramName) {
			this.paramName = paramName;
		}

		public String[] getParamValue() {
			return paramValue;
		}

		public void setParamValue(String[] paramValue) {
			this.paramValue = paramValue;
		}
	}

	/**
	 * Converts the given Document element to LibraryElement2.dtd format if
	 * necessary. // OOONEIDA Workbench code!
	 * 
	 * @param docel the docel
	 */
	public static void convertToLibraryElement2(final Element docel) {
		// Convert "EVENT" type names to empty strings
		NodeList evts = docel.getElementsByTagName("Event"); //$NON-NLS-1$
		for (int i = 0; i < evts.getLength(); i++) {
			Element el = (Element) evts.item(i);
			if (el.getAttribute("Type").equals("EVENT")) { //$NON-NLS-1$ //$NON-NLS-2$
				el.setAttribute("Type", ""); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		// Convert ECTransitions to Guard/Condition format
		NodeList eihdr = docel.getElementsByTagName("EventInputs"); //$NON-NLS-1$
		if (eihdr.getLength() < 1) {
			return;
		}
		NodeList eis = ((Element) eihdr.item(0)).getElementsByTagName("Event"); //$NON-NLS-1$
		String[] einames = new String[eis.getLength()];
		for (int j = 0; j < einames.length; j++) {
			einames[j] = ((Element) eis.item(j)).getAttribute("Name"); //$NON-NLS-1$
		}
		NodeList nodes = docel.getElementsByTagName("ECTransition"); //$NON-NLS-1$
		for (int i = 0; i < nodes.getLength(); i++) {
			Element tran = (Element) nodes.item(i);
			String cond = tran.getAttribute("Condition").trim(); //$NON-NLS-1$
			if (cond.length() < 1) {
				break;
			}
			tran.removeAttribute("Condition"); //$NON-NLS-1$
			String einame = cond;
			int n = einame.indexOf(' ');
			if (n > 0) {
				einame = einame.substring(0, n);
			}
			n = einame.indexOf('&');
			if (n > 0) {
				einame = einame.substring(0, n);
			}
			for (int j = 0; j < einames.length; j++) {
				if (einame.equals(einames[j])) {
					tran.setAttribute("Event", einame); //$NON-NLS-1$
					break;
				}
			}
			if (tran.getAttribute("Event").length() > 0) { //$NON-NLS-1$
				cond = cond.substring(einame.length()).trim();
				if (cond.startsWith("&")) { //$NON-NLS-1$
					cond = cond.substring(1);
				} else if (cond.startsWith("AND ")) { //$NON-NLS-1$
					cond = cond.substring(4);
				}
			}
			cond = cond.trim();
			if (cond.length() > 0) {
				tran.setAttribute("Guard", cond); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Instantiates a new export filter.
	 */
	public CPPExportFilter() {

	}

	/**
	 * Instantiates a new export filter.
	 * 
	 * @param doc     the doc
	 * @param destDir the dest dir
	 */
	public CPPExportFilter(final Document doc, final String destDir) {
		Document document = doc;
		docel = document.getDocumentElement();
		convertToLibraryElement2(docel);
		this.destDir = destDir;
		name = docel.getAttribute("Name"); //$NON-NLS-1$
	}

	@Override
	public void export(IFile typeFile, String destination, boolean forceOverwrite, LibraryElement type)
			throws org.eclipse.fordiac.ide.export.ExportException {
		this.libraryType = type;
		export(typeFile, destination, forceOverwrite);
	}

	/**
	 * Start export.
	 * 
	 * @param overwrite the overwrite
	 */
	public void startExport(final boolean overwrite) {
		// clear all vectors & hashtables
		vars = new HashMap<>();

		dataInCount = 0;

		dataOutCount = 0;
		internalCount = 0;
		fBCount = 0;

		inputVars = new ArrayList<>();

		outputVars = new ArrayList<>();
		internalVars = new ArrayList<>();
		funtionBlocks = new ArrayList<>();

		internal2InterfaceEventConns = new HashSet<>();

		eventConn = new ArrayList<>();

		eventConnHash = new HashSet<>();

		dataConn = new ArrayList<>();

		dataConnHash = new HashSet<>();

		interface2InternalDataConns = new HashSet<>();

		internal2InterfaceDataConns = new HashSet<>();

		String name = docel.getAttribute("Name"); //$NON-NLS-1$

		if (destDir != null) {
			String fName = destDir + File.separatorChar + name;
			try {
				File f = new File(fName + ".cpp"); //$NON-NLS-1$
				File h = new File(fName + ".h"); //$NON-NLS-1$
				if ((!overwrite) && (f.exists() || h.exists())) {
					ICompareEditorOpener opener = CompareEditorOpenerUtil.getOpener();
					String msg = MessageFormat.format(
							"Overwrite " + name + ".cpp" + " and " + name + ".h. " + ((opener != null)
									? "\nMerge will create a backup of the original File and open an editor to merge the files manually!"
									: ""), //$NON-NLS-1$
							new Object[] { f.getAbsolutePath() });

					MessageDialog msgDiag = new MessageDialog(Display.getDefault().getActiveShell(), "File Exists",
							null, msg, MessageDialog.QUESTION_WITH_CANCEL,
							new String[] { JFaceResources.getString(IDialogLabelKeys.YES_LABEL_KEY), "Merge",
									JFaceResources.getString(IDialogLabelKeys.CANCEL_LABEL_KEY) },
							0);

					int res = msgDiag.open();

					switch (res) {
					case 0:
						pwCPP = new PrintWriter(new FileOutputStream(fName + ".cpp")); //$NON-NLS-1$
						pwH = new PrintWriter(new FileOutputStream(fName + ".h")); //$NON-NLS-1$
						export();
						pwCPP.close();
						pwH.close();
						break;
					case 1:
						performManualMerge(name, fName, f, h, opener);
						break;
					default:
						break;
					}
				} else {
					pwCPP = new PrintWriter(new FileOutputStream(fName + ".cpp")); //$NON-NLS-1$
					pwH = new PrintWriter(new FileOutputStream(fName + ".h")); //$NON-NLS-1$
					export();
					pwCPP.close();
					pwH.close();
				}
			} catch (IOException e) {
				forteEmitterErrors.add(" - " + fName + " " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	private void performManualMerge(String name, String fName, File f, File h, ICompareEditorOpener opener)
			throws IOException {
		// manually merge the files
		if (opener != null) {
			boolean diffs = false;
			File originalCpp = Utils.createBakFile(f);
			File originalH = Utils.createBakFile(h);
			File tempcpp = new File(fName + ".cpp"); //$NON-NLS-1$
			File temph = new File(fName + ".h"); //$NON-NLS-1$
			pwCPP = new PrintWriter(new FileOutputStream(tempcpp));
			pwH = new PrintWriter(new FileOutputStream(temph));
			export();
			pwCPP.close();
			pwH.close();

			opener.setName(name);
			opener.setTitle(name + ".cpp"); //$NON-NLS-1$
			opener.setNewFile(tempcpp);
			opener.setOriginalFile(originalCpp);
			if (opener.hasDifferences()) {
				opener.openCompareEditor();
				diffs = true;
			}

			opener.setTitle(name + ".h"); //$NON-NLS-1$
			opener.setNewFile(temph);
			opener.setOriginalFile(originalH);
			if (opener.hasDifferences()) {
				opener.openCompareEditor();
				diffs = true;
			}

			if (!diffs) {
				// there where no differences inform the user
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), "No Differences",
						"There where no differences between the orignal file and the newly generated one!");
			}
		}
	}

	private void export() {
		exportHeader();

		// determine the type of library entry to emit
		String type = docel.getTagName();
		if (type.equals("ResourceType")) { //$NON-NLS-1$
			// emit the header for a resource type
			exportRES();
		} else if (type.equals("DeviceType")) { //$NON-NLS-1$
			// emit the header for a device type
			exportDevice();
		} else if ((type.equals("FBType")) || (type.equals("AdapterType"))) { //$NON-NLS-1$ //$NON-NLS-2$
			exportFB();
		}

		exportClosingCode();
	}

	private void exportRES() {
		exportResStarter();
		exportResConstructor();
	}

	private void exportDevice() {
		exportDeviceStarter();
		exportDeviceConstructor();
	}

	private void exportFB() {
		exportFBStarter();

		NodeList l1 = docel.getElementsByTagName("InterfaceList"); //$NON-NLS-1$
		org.w3c.dom.Node node;
		Element el;
		if (0 != l1.getLength()) {
			node = l1.item(0);
			if (node instanceof Element) {
				el = (Element) node;
				exportFBInterface();
			}
		}

		// l1 = docel.getElementsByTagName("BasicFB"); //$NON-NLS-1$
		if (0 != docel.getElementsByTagName("BasicFB").getLength()) {
			l1 = docel.getElementsByTagName("BasicFB");
			node = l1.item(0);
			if (node instanceof Element) {
				el = (Element) node;
				exportBasicFB(el.getChildNodes());
			}
		} else if (0 != docel.getElementsByTagName("FBNetwork").getLength()) {
			l1 = docel.getElementsByTagName("FBNetwork");
			node = l1.item(0);
			if (node instanceof Element) {
				el = (Element) node;
				exportFBNetwork(el.getChildNodes());
			}
		} else if (0 != docel.getElementsByTagName("SimpleFB").getLength()) {
			l1 = docel.getElementsByTagName("SimpleFB");
			node = l1.item(0);
			if (node instanceof Element) {
				el = (Element) node;
				exportSimpleFB(el.getChildNodes());
			}
		} else if (libraryType instanceof FBType) {
			exportSIFBExecuteEvent();
		}

		exportFBConstructor();

	}

	private void exportFBInterface() {
		NodeList l1 = docel.getElementsByTagName("InputVars"); //$NON-NLS-1$
		org.w3c.dom.Node node;
		if (0 != l1.getLength()) {
			node = l1.item(0);
			exportVarInputs((Element) node);
		}

		l1 = docel.getElementsByTagName("OutputVars"); //$NON-NLS-1$
		if (0 != l1.getLength()) {
			node = l1.item(0);
			exportVarOutputs((Element) node);
		}

		l1 = docel.getElementsByTagName("EventInputs"); //$NON-NLS-1$
		if (0 != l1.getLength()) {
			node = l1.item(0);
			exportEvents("EventInput", ((Element) node).getChildNodes(), inputVars); //$NON-NLS-1$
		}

		l1 = docel.getElementsByTagName("EventOutputs"); //$NON-NLS-1$
		if (0 != l1.getLength()) {
			node = l1.item(0);
			exportEvents("EventOutput", ((Element) node).getChildNodes(), outputVars); //$NON-NLS-1$
		} else {
			handleNotPresentEOTag();
		}

		l1 = docel.getElementsByTagName("InternalVars"); //$NON-NLS-1$
		if (l1.getLength() != 0) {
			node = l1.item(0);
			exportVarInternals((Element) node);
		}
		exportFBInterfaceSpec();
	}

	private void exportFBNetworkConnections() {
		eventConn.clear();
		eventConnHash.clear();
		dataConn.clear();
		dataConnHash.clear();
		NodeList l1 = docel.getElementsByTagName("EventConnections"); //$NON-NLS-1$
		if (l1.getLength() > 0) {
			org.w3c.dom.Node node = l1.item(0);
			NodeList conns = node.getChildNodes();
			for (int i = 0; i < conns.getLength(); ++i) {
				node = conns.item(i);
				if (node instanceof Element) {
					Element el = (Element) node;
					if (el.getNodeName().equals("Connection")) { //$NON-NLS-1$
						eventConn.add(el);
						if (-1 == (el.getAttribute("Destination")).indexOf('.')) { //$NON-NLS-1$
							internal2InterfaceEventConns.add(el.getAttribute("Source")); //$NON-NLS-1$
						} else {
							eventConnHash.add(el.getAttribute("Source")); //$NON-NLS-1$
						}
					}
				}
			}
			exportEC();
		}

		l1 = docel.getElementsByTagName("DataConnections"); //$NON-NLS-1$
		if (l1.getLength() > 0) {
			Node node = l1.item(0);
			NodeList conns = node.getChildNodes();
			for (int i = 0; i < conns.getLength(); ++i) {
				node = conns.item(i);
				if (node instanceof Element) {
					Element el = (Element) node;
					if (el.getNodeName().equals("Connection")) { //$NON-NLS-1$
						dataConn.add(el);
						if (-1 == (el.getAttribute("Source")).indexOf('.')) { //$NON-NLS-1$
							interface2InternalDataConns.add(el.getAttribute("Source")); //$NON-NLS-1$
						} else if (-1 == (el.getAttribute("Destination")).indexOf('.')) { //$NON-NLS-1$
							internal2InterfaceDataConns.add(el.getAttribute("Source")); //$NON-NLS-1$
						} else {
							dataConnHash.add(el.getAttribute("Source")); //$NON-NLS-1$
						}
					}
				}
			}
			exportDC();
		}
		exportFBNetworkInternalInterface();
	}

	private void exportBasicFB(final NodeList basicFBNodes) {
		Element eccNode = null;
		int len = basicFBNodes.getLength();
		for (int i = 0; i < len; ++i) {
			org.w3c.dom.Node node = basicFBNodes.item(i);
			if (node instanceof Element) {
				Element el = (Element) node;
				if (el.getNodeName().equals("ECC")) { //$NON-NLS-1$
					eccNode = el;
				} else if (el.getNodeName().equals("Algorithm")) { //$NON-NLS-1$
					NodeList nodes = el.getChildNodes();
					int alglen = nodes.getLength();
					for (int ii = 0; ii < alglen; ++ii) {
						org.w3c.dom.Node node2 = nodes.item(ii);
						if (node2 instanceof Element) {
							exportAlgorithm(el.getAttribute("Name"), ((Element) node2) //$NON-NLS-1$
									.getNodeName(), ((Element) node2).getAttribute("Text")); //$NON-NLS-1$
						}
					}
				}
			}
		}

		if (eccNode != null) {
			exportECC(eccNode);
		}
	}

	private void exportSimpleFB(final NodeList simpleFBNodes) {
		int len = simpleFBNodes.getLength();
		for (int i = 0; i < len; ++i) {
			org.w3c.dom.Node node = simpleFBNodes.item(i);
			if (node instanceof Element) {
				Element el = (Element) node;
				if (el.getNodeName().equals("Algorithm")) { //$NON-NLS-1$
					NodeList nodes = el.getChildNodes();
					int alglen = nodes.getLength();
					for (int ii = 0; ii < alglen; ++ii) {
						org.w3c.dom.Node node2 = nodes.item(ii);
						if (node2 instanceof Element) {
							exportAlgorithm(el.getAttribute("Name"), ((Element) node2) //$NON-NLS-1$
									.getNodeName(), ((Element) node2).getAttribute("Text")); //$NON-NLS-1$
						}
					}
				}
			}
		}
	}

	private void exportFBNetwork(final NodeList fbnNodes) {
		int len = fbnNodes.getLength();
		fBCount = 0;
		funtionBlocks.clear();
		for (int i = 0; i < len; i++) {
			org.w3c.dom.Node node = fbnNodes.item(i);
			if (node instanceof Element) {
				Element el = (Element) node;
				if (el.getNodeName().equals("FB")) { //$NON-NLS-1$
					++fBCount;
					FBDefinition newFBDef = new FBDefinition();
					newFBDef.type = el.getAttribute("Type"); //$NON-NLS-1$
					newFBDef.name = el.getAttribute("Name"); //$NON-NLS-1$
					NodeList parameterNodes = el.getChildNodes();
					// FIX gebenh, 24th September 2008 -> dynamicaly create list
					// to avoid a fixed size array where elements can be null
					List<String> paramNames = new ArrayList<>();
					List<String> paramValues = new ArrayList<>();

					for (int j = 0; j < parameterNodes.getLength(); j++) {
						org.w3c.dom.Node nodeParam = parameterNodes.item(j);
						if (nodeParam instanceof Element) {
							Element parameter = (Element) nodeParam;
							paramNames.add(parameter.getAttribute("Name")); //$NON-NLS-1$
							paramValues.add(parameter.getAttribute("Value")); //$NON-NLS-1$
						}
					}
					newFBDef.paramName = paramNames.toArray(new String[paramNames.size()]);
					newFBDef.paramValue = paramValues.toArray(new String[paramNames.size()]);
					funtionBlocks.add(newFBDef);
				}
			}
		}
		exportFBVar();
		exportFBNetworkConnections();
		exportCompFBExecuteEventMethod();
	}

	protected void exportVarInputs(final Element varInputs) {
		exportVarNameArrays("DataInput", varInputs.getChildNodes()); //$NON-NLS-1$
		NodeList l1 = varInputs.getChildNodes();
		int len = l1.getLength();
		dataInCount = 0;
		for (int i = 0; i < len; i++) {
			org.w3c.dom.Node node = l1.item(i);
			if (node instanceof Element) {
				Element el = (Element) node;
				if (el.getNodeName().equals("VarDeclaration")) { //$NON-NLS-1$
					VarDefinition newVarDef = new VarDefinition(el, dataInCount, 0);
					inputVars.add(newVarDef.name);
					vars.put(newVarDef.name, newVarDef);
					exportVariable(newVarDef);
					++dataInCount;
				}
			}
		}
	}

	private void exportVarOutputs(final Element varOutputs) {
		exportVarNameArrays("DataOutput", varOutputs.getChildNodes()); //$NON-NLS-1$
		NodeList l1 = varOutputs.getChildNodes();
		int len = l1.getLength();
		dataOutCount = 0;
		for (int i = 0; i < len; i++) {
			org.w3c.dom.Node node = l1.item(i);
			if (node instanceof Element) {
				Element el = (Element) node;
				if (el.getNodeName().equals("VarDeclaration")) { //$NON-NLS-1$
					VarDefinition newVarDef = new VarDefinition(el, dataOutCount, 1);
					outputVars.add(newVarDef.name);
					vars.put(newVarDef.name, newVarDef);
					exportVariable(newVarDef);
					++dataOutCount;
				}
			}
		}
	}

	private void exportVarInternals(final Element varInternals) {
		exportVarNameArrays("Internals", varInternals.getChildNodes()); //$NON-NLS-1$
		NodeList l1 = varInternals.getChildNodes();
		int len = l1.getLength();
		internalCount = 0;
		for (int i = 0; i < len; i++) {
			org.w3c.dom.Node node = l1.item(i);
			if (node instanceof Element) {
				Element el = (Element) node;
				if (el.getNodeName().equals("VarDeclaration")) { //$NON-NLS-1$
					VarDefinition newVarDef = new VarDefinition(el, internalCount, 2);
					internalVars.add(newVarDef);
					vars.put(newVarDef.name, newVarDef);
					exportVariable(newVarDef);
					++internalCount;
				}
			}
		}
	}

	protected abstract void exportHeader();

	protected abstract void exportFBStarter();

	protected abstract void exportCompFBExecuteEventMethod();

	protected abstract void exportSIFBExecuteEvent();

	protected abstract void exportFBNetworkInternalInterface();

	protected abstract void exportFBConstructor();

	protected abstract void exportFBManagedObjectMethods();

	protected abstract void exportResStarter();

	protected abstract void exportResConstructor();

	protected abstract void exportDeviceStarter();

	protected abstract void exportDeviceConstructor();

	protected abstract void exportClosingCode();

	protected abstract void exportVarNameArrays(String namePrefix, NodeList nodes);

	protected abstract void exportEvents(String namePrefix, NodeList nodes, List<String> varNames);

	protected abstract void exportAlgorithm(String algName, String type, String src);

	protected abstract void exportVariable(VarDefinition newVarDef);

	protected abstract void exportFBVar();

	protected abstract void exportEC();

	protected abstract void exportDC();

	protected abstract void exportECC(Element eccNode);

	protected abstract void exportFBInterfaceSpec();

	protected abstract void handleNotPresentEOTag();

	/**
	 * Trim string.
	 * 
	 * @param value the value
	 * 
	 * @return the string
	 */
	public static String trimSTRING(String value) {
		final String trimValue1 = "\""; //$NON-NLS-1$
		final String trimValue2 = "'"; //$NON-NLS-1$

		if (value.startsWith(trimValue1) || value.startsWith(trimValue2)) {
			value = value.substring(1);
		}
		if (value.endsWith(trimValue1) || value.endsWith(trimValue2)) {
			value = value.substring(0, value.length() - 1);
		}

		return value;
	}

	@Override
	public List<String> getErrors() {
		return forteEmitterErrors;
	}

	@Override
	public List<String> getWarnings() {
		return forteEmitterWarnings;
	}

	@Override
	public List<String> getInfos() {
		return forteEmitterInfos;
	}
}
