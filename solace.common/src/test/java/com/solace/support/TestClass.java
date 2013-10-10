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
package com.solace.support;

import java.util.List;

public class TestClass {

	public TestClass() {

	}
	
	public TestClass(String value) {
		this.value = value;
	}

	String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	List<ChildClass> children;

	public List<ChildClass> getChildren() {
		return children;
	}

	public void setChildren(List<ChildClass> kids) {
		this.children = kids;
	}

}
