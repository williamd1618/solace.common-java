/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, TestJmxService.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.service;

import com.solace.service.JmxService;

public class TestJmxService extends JmxService {

	private static Object m_synch = new Object();

	private boolean m_running = true;

	public TestJmxService() {
		super();
	}

	@Override
	public void doStart() {
		System.out.println("in doStart.");
	}

	@Override
	public void doStop() {
		m_running = false;
	}

	public boolean isRunning() {
		return m_running;
	}
}
