/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, IEntity.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data;

public interface IEntity<ID> extends IPersistable {

	public ID getId();
	public void setId(ID id);

	public long getVersion();
	public void setVersion(long version);
	
	public java.util.Date getCreateDate();
	public void setCreateDate(java.util.Date d);
	
	public java.util.Date getLastModifiedDate();
	public void setLastModifiedDate(java.util.Date d);
}
