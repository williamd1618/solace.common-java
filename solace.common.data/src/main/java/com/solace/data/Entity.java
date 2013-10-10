/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, Entity.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data;

import com.solace.*;
import com.solace.data.*;
import com.solace.logging.*;
import com.solace.utility.*;

import javax.persistence.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.sql.Date;
import java.util.Calendar;

/**
 * The Entity class is the base class for JPA entities as well. It takes a long
 * as a {@see javax.persistence.Version}.
 * 
 * @author dwilliams
 * 
 * @param <ID>
 *            the type of the identifier
 *            http://en.wikibooks.org/wiki/Java_Persistence/Inheritance
 *            http://en.wikibooks.org/wiki/Java_Persistence/OneToMany
 */
@MappedSuperclass
public abstract class Entity<ID> implements IEntity<ID> {

	static Logger LOGGER = Logger.getLogger("com.solace.data.Entity");

	protected ID id;

	public Entity() {
	}

	public Entity(ID id) {
		this();
		this.id = id;
	}

	@Override
	@Transient
	public ID getId() {
		return this.id;
	}

	@Override
	public void setId(ID id) {
		this.id = id;
	}

	protected long m_version = 0L; // this defaults to 0 for a specific reason.

	// Don't change it!!!
	@Version
	@Column(name = "version")
	@Override
	public long getVersion() {
		return m_version;
	}

	@Override
	public void setVersion(long version) {
		m_version = version;
	}

	protected java.util.Date lastUpdateDate;

	/**
	 * lastModifiedDate is exactly that. Will have column def of
	 * last_modified_date
	 */
	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update_date")
	@Override
	public java.util.Date getLastModifiedDate() {		
		return lastUpdateDate;
	}

	@Override
	public void setLastModifiedDate(java.util.Date date) {
		this.lastUpdateDate = date;
	}

	protected java.util.Date createDate;

	/**
	 * column definition create_date
	 */
	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	@Override
	public java.util.Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(java.util.Date date) {
		this.createDate = date;
	}

	@Override
	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass()))
			return false;
		else {
			Entity<ID> e = (Entity<ID>) o;

			if (e.getId().equals(this.getId()))
				return true;
			else
				return false;
		}
	}

	@Override
	public int hashCode() {
		int result;
		result = 29 * this.getClass().hashCode() + ((getId()!=null)?getId().hashCode():0) + super.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("[{}#{}]", this.getClass(), getId());
	}

	public void onLoad() {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("OnLoad() called for: {}", this);

		doOnLoad();
	}

	protected void doOnLoad() {
	}

	@Override
	public final void save() {
		LOGGER.debug("Save() called for: {}", this);

		LOGGER.info("Invoking doSave on {}", this.getClass().getName());

		doSave();

		LOGGER.debug("Save returned as: {}", this);
	}

	protected void doSave() {
	}

	@Override
	public final void delete() {
		LOGGER.debug("Delete() called for: {}", this);

		LOGGER.info("Invoking doDelete on {}", this.getClass().getName());
	
		doDelete();
	}

	protected void doDelete() {
	}

	@PrePersist
	private void onInsert() {
		java.sql.Date d = new java.sql.Date(Calendar.getInstance().getTime()
				.getTime());

		this.createDate = d;

		this.lastUpdateDate = d;
	}

	@PreUpdate
	private void onUpdate() {
		java.sql.Date d = new java.sql.Date(Calendar.getInstance().getTime()
				.getTime());

		this.lastUpdateDate = d;
	}
}
