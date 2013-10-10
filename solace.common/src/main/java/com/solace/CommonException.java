/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, CommonException.java, and the accompanying materials
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

import java.util.*;

/**
 * A base exception class that will take in additional information in relation
 * to the exception so that it can easily be logged.
 * @Todo add ctor with Map<String, Object>
 * @author dwilliams
 * 
 */
public abstract class CommonException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5648551784115921753L;
	
	private String m_data;

	/**
	 * 
	 */
	public CommonException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public CommonException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CommonException(String _msg, Object... _data) {
		this(_msg);
		
		loadData(_data);
	}

	public CommonException(String _msg, NameValue... _data) {
		this(_msg);
		
		loadData((Object[])_data);
	}
	
	public CommonException(String _msg, Map<String, Object> _data) {
		this(_msg);
		
		loadData(_data);
	}

	/**
	 * @param arg0
	 */
	public CommonException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CommonException(Throwable _t, Object... _data) {
		this(_t);
		
		loadData(_data);
	}

	public CommonException(Throwable _t, NameValue... _data) {
		this(_t);
		
		loadData((Object[])_data);
	}
	
	public CommonException(Throwable _t, Map<String, Object> _data) {
		this(_t);
		
		loadData(_data);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CommonException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public CommonException(String _msg, Throwable _t, Object... _data) {
		this(_msg, _t);
		
		loadData(_data);
	}

	public CommonException(String _msg, Throwable _t, NameValue... _data) {
		this(_msg, _t);
		
		loadData((Object[])_data);
	}
	
	public CommonException(String _msg, Throwable _t, Map<String, Object> _data) {
		this(_msg, _t);
		
		loadData(_data);
	}
	
	public String getAdditionalData() {
		return m_data;
	}

	protected void loadData(Object... _args) {
		if (_args != null && _args.length > 0) {
			StringBuffer sb = new StringBuffer();
			if (_args[0] instanceof NameValue) {
				for (NameValue nv : (NameValue[]) _args) {
					sb.append(nv.getName()).append(" = ").append(
							nv.getValue().toString()).append("; ");
				}
			} else {
				for (Object obj : _args) {
					sb.append(obj.toString()).append("; ");
				}
			}
			
			m_data = sb.toString();
		}	
	}
	
	protected void loadData(Map<String,Object> _data)
	{
		m_data = _data.toString();
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("Additional parameters: ").append(getAdditionalData());
		sb.append("\n");
		sb.append(super.toString());
		return sb.toString();
	}
}
