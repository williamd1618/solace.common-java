/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, CacheException.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import java.util.Map;

import com.solace.*;

public class CacheException extends CommonException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8660097969305820001L;

	public CacheException() {
		// TODO Auto-generated constructor stub
	}

	public CacheException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CacheException(String msg, Map<String, Object> data) {
		super(msg, data);
		// TODO Auto-generated constructor stub
	}

	public CacheException(String msg, NameValue... data) {
		super(msg, data);
		// TODO Auto-generated constructor stub
	}

	public CacheException(String msg, Object... data) {
		super(msg, data);
		// TODO Auto-generated constructor stub
	}

	public CacheException(String msg, Throwable t, Map<String, Object> data) {
		super(msg, t, data);
		// TODO Auto-generated constructor stub
	}

	public CacheException(Throwable t, Map<String, Object> data) {
		super(t, data);
		// TODO Auto-generated constructor stub
	}

	public CacheException(Throwable t, NameValue... data) {
		super(t, data);
		// TODO Auto-generated constructor stub
	}

	public CacheException(Throwable t, Object... data) {
		super(t, data);
		// TODO Auto-generated constructor stub
	}

	public CacheException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CacheException(String _msg, Throwable _t, Object... _data) {
		this(_msg, _t);
		
		loadData(_data);
	}

	public CacheException(String _msg, Throwable _t, NameValue... _data) {
		this(_msg, _t);
		
		loadData((Object[])_data);
	}
}
