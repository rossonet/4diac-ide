/*******************************************************************************
 * Copyright (c) 2008, 2009, 2012, 2016 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 *               2019 Johannes Kepler University Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Martijn Rooker, Gerhard Ebenhofer, Thomas Strasser, Alois Zoitl
 *     - initial API and implementation and/or initial documentation
 *   Bianca Wiesmayr
 *     - add access to path setting and configured runtime path
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbrtlauncher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.fordiac.ide.fbrtlauncher.preferences.PreferenceConstants;
import org.eclipse.fordiac.ide.runtime.IRuntimeLauncher;
import org.eclipse.fordiac.ide.runtime.LaunchParameter;
import org.eclipse.fordiac.ide.runtime.LaunchRuntimeException;
import org.eclipse.fordiac.ide.runtime.LaunchRuntimeUtils;
import org.eclipse.jface.util.IPropertyChangeListener;

/**
 * The Class FBRTLauncher.
 */
public class FBRTLauncher implements IRuntimeLauncher {

	private final List<LaunchParameter> params = new ArrayList<>();

	/**
	 * Instantiates a new fBRT launcher.
	 */
	public FBRTLauncher() {
		// define the initial parameters for the runtime
		setParam(Messages.FBRTLauncher_LABEL_PortParam, "61505"); //$NON-NLS-1$
		LaunchParameter param = setParam(Messages.FBRTLauncher_LABEL_DeviceTypeParam, "RMT_FRAME"); //$NON-NLS-1$
		param.setFixedValues(true);
		param.setValues(new String[] { "RMT_FRAME", "RMT_DEV" }); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String getName() {
		return "FBRT"; //$NON-NLS-1$
	}

	@Override
	public void launch() throws LaunchRuntimeException {
		checkPlatform();
		String javaRte = getJavaRte();
		String runtimePath = getRuntimePath();

		String deviceType = params.get(1).getValue();
		String fbrtPath = "fb.rt."; //$NON-NLS-1$
		if (deviceType.equalsIgnoreCase("RMT_FRAME")) { //$NON-NLS-1$
			fbrtPath += "hmi."; //$NON-NLS-1$
		}
		String arguments = "-noverify -classpath ./lib" //$NON-NLS-1$
				+ File.pathSeparatorChar + "./" //$NON-NLS-1$
				+ new File(runtimePath).getName() + File.pathSeparatorChar + " " //$NON-NLS-1$
				+ fbrtPath + deviceType + " -n " //$NON-NLS-1$
				+ deviceType + " -s " //$NON-NLS-1$
				+ Integer.toString(getPortNumber()) + " -p " //$NON-NLS-1$
				+ Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_LIB);
		LaunchRuntimeUtils.startRuntime("FBRT " + deviceType, javaRte, //$NON-NLS-1$
				new File(runtimePath).getParentFile().getAbsolutePath(), arguments);

	}

	@Override
	public String getRuntimePath() {
		return Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_PATH);
	}

	private String getJavaRte() throws LaunchRuntimeException {
		String javaRte = System.getProperty("java.home"); //$NON-NLS-1$
		if (javaRte.isEmpty()) {
			throw new LaunchRuntimeException(Messages.FBRTLauncher_ERROR_MissingJavaVM);
		}
		javaRte = javaRte + File.separatorChar + "bin" + File.separatorChar + "java"; //$NON-NLS-1$ //$NON-NLS-2$
		if (isWin32Platform()) {
			javaRte += ".exe"; //$NON-NLS-1$
		}
		return javaRte;
	}

	private int getPortNumber() throws LaunchRuntimeException {
		int port = Integer.parseInt(params.get(0).getValue());
		if ((port < 1024) || (port > 65535)) {
			throw new LaunchRuntimeException(Messages.FBRTLauncher_ERROR_WrongPort);
		}
		return port;
	}

	private void checkPlatform() throws LaunchRuntimeException {
		boolean isLinux = Platform.getOS().equalsIgnoreCase(Platform.OS_LINUX);
		boolean isMacOS = Platform.getOS().equalsIgnoreCase(Platform.OS_MACOSX);
		if (!(isWin32Platform() || isLinux || isMacOS)) {
			throw new LaunchRuntimeException(Messages.FBRTLauncher_ERROR_MissingPlatform);
		}
	}

	private boolean isWin32Platform() {
		return Platform.getOS().equalsIgnoreCase(Platform.OS_WIN32);
	}

	@Override
	public int getNumParameters() {
		return params.size();
	}

	@Override
	public List<LaunchParameter> getParams() {
		return params;
	}

	@Override
	public final LaunchParameter setParam(final String name, final String value) {
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i).getName().equals(name)) {
				params.get(i).setValue(value);
				return params.get(i);
			}
		}
		LaunchParameter param = new LaunchParameter();
		param.setName(name);
		param.setValue(value);
		params.add(param);
		return param;
	}

	@Override
	public String getPathPreferenceSettingPageID() {
		return "org.eclipse.fordiac.ide.fbrtlauncher.preferences.FBRTPreferencePage"; //$NON-NLS-1$
	}

	@Override
	public void addPathPreferenceChangeListener(IPropertyChangeListener listener) {
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(listener);
	}
}
