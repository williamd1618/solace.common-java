/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ReflectionTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.utility;

import org.junit.*;
import static org.junit.Assert.*;

import com.solace.utility.*;

public class ReflectionTests {
	
	/**
	 * Tests default construction
	 * @throws Exception
	 */
	@Test
	public void defaultCtorTest() throws Exception {
		try
		{		
			TestClass obj = ReflectionUtil.<TestClass>createInstance(
					"com.solace.utility.TestClass");
			
			assertEquals(TestClass.class, obj.getClass());
		}
		catch ( Exception e)
		{
			fail(e.getMessage());
		}
	}
	
	/**
	 * test constructed params
	 * @throws Exception
	 */
	@Test
	public void ctorWithParams() throws Exception {
		try
		{
			TestClass obj = ReflectionUtil.<TestClass>createInstance(
					"com.solace.utility.TestClass", "foo", "bar");
			
			assertEquals("foo", obj.m_foo);
			assertEquals("bar", obj.m_bar);
		}
		catch ( Exception e )
		{
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testInvoke() throws Exception
	{
		TestClass c = new TestClass();
		
		assertEquals("foo",ReflectionUtil.<String>invoke(c, "getValue"));
	}	
}
