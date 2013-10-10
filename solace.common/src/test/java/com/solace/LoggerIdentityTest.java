/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, LoggerIdentityTest.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace;

import com.solace.logging.*;

import org.junit.*;

public class LoggerIdentityTest {
	
	@Test
	public void identical()
	{
		Logger l1 = Logger.getLogger(LoggerIdentityTest.class);
		
		Logger l2 = Logger.getLogger("com.solace.LoggerIdentityTest");
		
		Assert.assertEquals(l1, l2);
	}

}
