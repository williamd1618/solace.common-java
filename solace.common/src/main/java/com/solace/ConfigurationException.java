/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ConfigurationException.java, and the accompanying materials
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
 * Typed exception to represent exception specific to configuration issues
 * @author dwilliams
 *
 */
public class ConfigurationException extends CommonException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4034092144620434159L;

	/**
	 * 
	 */
	public ConfigurationException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ConfigurationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ConfigurationException(String _msg, Throwable _t, Object... _data) {
		this(_msg, _t);
		
		loadData(_data);
	}

	public ConfigurationException(String _msg, Throwable _t, NameValue... _data) {
		this(_msg, _t);
		
		loadData((Object[])_data);
	}
}
