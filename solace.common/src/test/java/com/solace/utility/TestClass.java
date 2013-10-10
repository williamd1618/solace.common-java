/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, TestClass.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.utility;

public class TestClass {
	public String m_foo, m_bar;
	
	public TestClass() {
		
	}
	
	public TestClass(String _foo, String _bar)
	{
		m_foo = _foo;
		m_bar = _bar;
	}
	
	public String getValue() {
		return "foo";
	}
}
