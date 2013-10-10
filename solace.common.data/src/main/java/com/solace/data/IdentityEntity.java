/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, IdentityEntity.java, and the accompanying materials
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
 * An entity whose PK is an identity field.  We break out the PK in this fashion
 * to allow for a CompositeEntity, Duple or TupleEntity to be created and still leverage
 * the same generic persistence structure
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 *
 * @param <ID>
 */
@MappedSuperclass
public abstract class IdentityEntity<ID extends Number> extends Entity<ID> {
	
	public IdentityEntity() {
		super();
	}
	
	public IdentityEntity(ID id) {
		super(id);
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Override
	public ID getId() { 
		return super.getId();
	}
	
	public void setId(ID id) {
		super.setId(id);
	}
}
