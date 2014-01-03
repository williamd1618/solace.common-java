/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, Parent.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.derived;

import javax.persistence.*;

import com.solace.data.*;
import com.solace.data.derived.Person;

import java.util.*;

@javax.persistence.Entity
public class Parent extends com.solace.data.IdentityEntity<Long> {

	Person person;

	public Parent() {

	}

	public Parent(final String firstName, final String lastName, final int age,
			final Date bday, final Date effectiveDate) {

		this.effectiveDate = effectiveDate;
		this.person = new Person(firstName, lastName, age, bday);
	}

	Date effectiveDate;

	@Column(name = "effective_date")
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date d) {
		this.effectiveDate = d;
	}

	@OneToOne(cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, optional = false)
	@JoinColumn(name = "person_id", referencedColumnName = "id")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person p) {
		this.person = p;
	}

	List<Person> children;

	@ManyToMany(targetEntity = Person.class, cascade = {
			CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "ParentChild", joinColumns = { @JoinColumn(name = "parent_id")}, 
			inverseJoinColumns = { @JoinColumn(name = "child_id") })
	public List<Person> getChildren() {
		return this.children;
	}

	public void setChildren(List<Person> kids) {
		this.children = kids;
	}

	public void addChild(Person p) {
		if (this.children == null)
			children = new ArrayList<Person>();

		children.add(p);
	}

	@Override
	public void doSave() {
		DaoManager.getParentDao().save(this);
	}

	@Override
	public void doDelete() {
		DaoManager.getParentDao().delete(this);
	}
}
