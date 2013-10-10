/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, TestObject.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

public class TestObject implements java.io.Serializable {

	/**
		 * 
		 */
//	private static final long serialVersionUID = 4548679753094099078L;
	int i;

	public TestObject(int i) {
		this.i = i;
	}

	@Override
	public String toString() {
		return (new Integer(i)).toString() + super.toString();
	}
}
