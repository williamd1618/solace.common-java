/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ArgumentException.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package com.solace;

/**
 * The ArgumentException is a more semantically rich exception class
 * around inbound arguments.
 * @author dwilliams
 *
 */
public class ArgumentException extends CommonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7816240291528973820L;

	/**
	 * 
	 */
	public ArgumentException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ArgumentException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ArgumentException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	
	public ArgumentException(String _msg, Object... _data) {
		super(_msg, _data);
	}

	public ArgumentException(String _msg, NameValue... _data) {
		super(_msg, _data);
	}

	
	/**
	 * @param arg0
	 * @param arg1
	 */
	public ArgumentException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	public ArgumentException(String _msg, Throwable _t, Object... _data) {
		this(_msg, _t);
		
		loadData(_data);
	}

	public ArgumentException(String _msg, Throwable _t, NameValue... _data) {
		this(_msg, _t);
		
		loadData((Object[])_data);
	}
}
