/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ClassPathTest.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.utility;

import org.junit.*;

import com.solace.logging.*;

public class ClassPathTest {

	static final Logger LOGGER = Logger.getLogger(ClassPathTest.class);
	@Test
	public void test()
	{
		LOGGER.debug("foo");				
	}
}
