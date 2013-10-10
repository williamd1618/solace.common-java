/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, EnumerationEntity.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data;

import javax.persistence.*;

/**
 * The default enumeration entity class (e.g. Company) to guarantee a getValue
 * is exposed appropriately
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 * 
 * @param <ID>
 */
@MappedSuperclass
public abstract class EnumerationEntity<ID extends Number> extends IdentityEntity<ID> implements
		IEnumerationEntity<ID> {

	public EnumerationEntity() {
		super();
	}

	public EnumerationEntity(ID id) {
		super(id);
	}

	protected String m_value = "";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Override
	public ID getId() { 
		return super.getId();
	}
	
	public void setId(ID id) {
		super.setId(id);
	}


	@Override
	@Transient
	public String getValue() {
		return m_value;
	}

	@Override
	public void setValue(String value) {
		this.m_value = value;
	}
}
