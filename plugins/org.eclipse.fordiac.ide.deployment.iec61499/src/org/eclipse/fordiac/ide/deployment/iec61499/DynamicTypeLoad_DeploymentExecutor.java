/*******************************************************************************
 * Copyright (c) 2017 fortiss GmbH
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.deployment.iec61499;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fordiac.ide.deployment.FBDeploymentData;
import org.eclipse.fordiac.ide.deployment.exceptions.CreateFBInstanceException;
import org.eclipse.fordiac.ide.deployment.exceptions.CreateFBTypeException;
import org.eclipse.fordiac.ide.export.forte_lua.ForteLuaExportFilter;
import org.eclipse.fordiac.ide.model.libraryElement.AdapterType;
import org.eclipse.fordiac.ide.model.libraryElement.BasicFBType;
import org.eclipse.fordiac.ide.model.libraryElement.CompositeFBType;
import org.eclipse.fordiac.ide.model.libraryElement.Device;
import org.eclipse.fordiac.ide.model.libraryElement.FB;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetworkElement;
import org.eclipse.fordiac.ide.model.libraryElement.FBType;
import org.eclipse.fordiac.ide.model.libraryElement.InterfaceList;
import org.eclipse.fordiac.ide.model.libraryElement.Resource;
import org.eclipse.swt.widgets.Display;

public class DynamicTypeLoad_DeploymentExecutor extends DeploymentExecutor {

	private static final String PROFILE_NAME = "DynamicTypeLoad"; //$NON-NLS-1$

	public DynamicTypeLoad_DeploymentExecutor() {
		super();
	}

	@Override
	public String getProfileName() {
		return PROFILE_NAME;
	}

	@Override
	public void createFBInstance(final FBDeploymentData fbData, final Resource res) throws CreateFBInstanceException {
		// check first if FBType exists
		HashMap<String, AdapterType> adapters = getAdapterTypes(fbData.fb.getType().getInterfaceList());
		if (!adapters.isEmpty()) {
			queryAdapterTypes(adapters, res);
		}
		queryFBTypes(fbData.fb, res);
		super.createFBInstance(fbData, res);
	}

	private HashMap<String, AdapterType> getAdapterTypes(InterfaceList interfaceList) {
		HashMap<String, AdapterType> list = new HashMap<String, AdapterType>();
		interfaceList.getPlugs().forEach((e) -> list.put(e.getTypeName(), (AdapterType) EcoreUtil.copy(e.getType())));
		interfaceList.getSockets().forEach((e) -> list.put(e.getTypeName(), (AdapterType) EcoreUtil.copy(e.getType())));
		return list;
	}

	public void createFBType(final FBType fbType, final Resource res) throws CreateFBTypeException {
		if(null != devMgmCommHandler.getTypes()) {
			setAttribute(res.getDevice(), "FBType", devMgmCommHandler.getTypes());
		}
		if ((fbType instanceof BasicFBType || fbType instanceof CompositeFBType)
				&& ( (null != devMgmCommHandler.getTypes() && !devMgmCommHandler.getTypes().contains(fbType.getName())) 
						|| (null == devMgmCommHandler.getTypes() && !isAttribute(res.getDevice(), fbType.getName(), "FBType")))) {
			if(fbType instanceof CompositeFBType) {
				for(FBNetworkElement netelem : ((CompositeFBType) fbType).getFBNetwork().getNetworkElements()) {
					if(!devMgmCommHandler.getTypes().contains(netelem.getTypeName())) {
						HashMap<String, AdapterType> adapters = getAdapterTypes(netelem.getInterface());						
						if(!adapters.isEmpty()) {
							loopAdapterTypes(adapters, res);
						}
						createFBType((FBType) netelem.getType(), res);
					}
				}
			}
			ForteLuaExportFilter luaFilter = new ForteLuaExportFilter();
			String luaSkript = luaFilter.createLUA(fbType);
			String request = MessageFormat.format(Messages.DTL_CreateFBType,
					new Object[] { id++, fbType.getName(), luaSkript });
			try {
				if (this.devMgmCommHandler instanceof EthernetDeviceManagementCommunicationHandler) {
					String result = ((EthernetDeviceManagementCommunicationHandler) devMgmCommHandler)
							.sendREQandRESP("", request);
					if (result.contains("Reason")) {
						throw new CreateFBTypeException("LUA skript for " + fbType.getName() + " FBType not executed");
					} else {
						devMgmCommHandler.getTypes().add(fbType.getName());
					}
				} else {
					sendREQ(res.getName(), request);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isAttribute(Device device, String fbTypeName, String attributeType) {
		if(null != device.getAttribute(attributeType)) {			
			for(String s : device.getAttributeValue(attributeType).split(",")) {
				if(fbTypeName.equals(s.trim())) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void setAttribute(Device device, String string, HashSet<String> hashSet) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				device.setAttribute(string, "STRING", String.join(", ", hashSet), "created during deployment");
			}
		});
	}
	
	public void createAdapterType(final String adapterKey, HashMap<String, AdapterType> adapters, final Resource res) throws CreateFBTypeException {
		if(null != devMgmCommHandler.getAdapterTypes()) {
			setAttribute(res.getDevice(), "AdapterType", devMgmCommHandler.getAdapterTypes());
		}
		if ( (null != devMgmCommHandler.getAdapterTypes() && !devMgmCommHandler.getAdapterTypes().contains(adapterKey)) 
				|| (null == devMgmCommHandler.getAdapterTypes() && !isAttribute(res.getDevice(), adapterKey, "AdapterType")) ) {			
			ForteLuaExportFilter luaFilter = new ForteLuaExportFilter();
			String luaSkript = luaFilter.createLUA(adapters.get(adapterKey));
			String request = MessageFormat.format(Messages.DTL_CreateAdapterType,
					new Object[] { id++, adapterKey, luaSkript });
			try {
				if (this.devMgmCommHandler instanceof EthernetDeviceManagementCommunicationHandler) {
					String result = ((EthernetDeviceManagementCommunicationHandler) devMgmCommHandler).sendREQandRESP("",
							request);
					if (result.contains("Reason")) {
						throw new CreateFBTypeException(
								"LUA skript for " + adapterKey + " AdapterType not executed");
					} else {
						devMgmCommHandler.getAdapterTypes().add(adapterKey);
					}
				} else {
					sendREQ(res.getName(), request);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void queryFBTypes(FB fb, Resource res) {
		if (null != devMgmCommHandler && null == devMgmCommHandler.getTypes()) {
			String request = MessageFormat.format(Messages.DTL_QueryFBTypes, new Object[] { id++ });
			try {
				sendQUERY(res.getName(), request);
			} catch (IOException e) {
				System.out.println(MessageFormat.format(Messages.DTL_QueryFailed, new Object[] { "FB Types" }));
			}
		}
		FBType fbType = fb.getType();
		try {
			createFBType(fbType, res);
		} catch (CreateFBTypeException ce) {
			System.out.println(MessageFormat.format(Messages.DTL_CreateTypeFailed, new Object[] { fbType.getName() }));
		}
	}

	private void queryAdapterTypes(HashMap<String, AdapterType> adapters, Resource res) {
		if (null != devMgmCommHandler && null == devMgmCommHandler.getAdapterTypes()) {
			String request = MessageFormat.format(Messages.DTL_QueryAdapterTypes, new Object[] { id++ });
			try {
				sendQUERY(res.getName(), request);
			} catch (IOException e1) {
				System.out.println(MessageFormat.format(Messages.DTL_QueryFailed, new Object[] { "Adapter Types" }));
			}
		}
		loopAdapterTypes(adapters, res);
	}

	private void loopAdapterTypes(HashMap<String, AdapterType> adapters, Resource res) {
		adapters.keySet().forEach((e) -> {
			try {
				createAdapterType(e, adapters, res);
			} catch (CreateFBTypeException ce) {
				System.out.println(MessageFormat.format(Messages.DTL_CreateTypeFailed, new Object[] { e }));
			}
		});
	}
}