/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, ThreadingException.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.threading;

import com.solace.*;

public class ThreadingException extends CommonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8996497782950064064L;

	public ThreadingException() {
		// TODO Auto-generated constructor stub
	}

	public ThreadingException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ThreadingException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ThreadingException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ThreadingException(String _msg, Throwable _t, Object... _data) {
		this(_msg, _t);
		
		loadData(_data);
	}

	public ThreadingException(String _msg, Throwable _t, NameValue... _data) {
		this(_msg, _t);
		
		loadData((Object[])_data);
	}
}
