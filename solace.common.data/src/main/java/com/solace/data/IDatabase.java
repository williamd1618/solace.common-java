/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, IDatabase.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data;

public interface IDatabase {

	public void open();
	
	public void close();
	
	public String getConnectionString();
	
	public java.sql.Connection getConnection();

	public void flush();
	
	public void beginTransaction(int isolationLevel);
	
	public void beginTransaction();
	
	public void rollbackTransaction();
	
	public void commitTransaction();
}
