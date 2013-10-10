/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, JmxServiceTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.service;

import org.junit.*;

import com.solace.logging.Logger;

public class JmxServiceTests {
	
	class JmxRunnable extends Thread
	{
		TestJmxService svr = null;
		public JmxRunnable()
		{
			svr = new TestJmxService();
		}
		
		@Override
		public void run() {
			svr.start();	
		}

//		@Override
//		public void doStop() {
//			svr.stop();
//		}
	}

//	@Test
	public void waitTest() throws Exception {
		Thread t = new Thread(new JmxRunnable());					

		t.start();
		
		Thread.sleep(10000);

		t.stop();
	}
}
