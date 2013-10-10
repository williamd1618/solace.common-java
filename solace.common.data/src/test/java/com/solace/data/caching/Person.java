/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, Person.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.caching;

import javax.persistence.*;

import java.util.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.annotation.*;

import com.solace.data.IdentityEntity;
import com.solace.data.jpa.*;

@javax.persistence.Entity
@org.hibernate.annotations.Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE,include="non-lazy")
public class Person extends IdentityEntity<Long> implements java.io.Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1153425221081786048L;

	public Person() {
	}

	public Person(final String firstName, final String lastName, final int age,
			final Date bday) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.bday = bday;
	}

	String firstName;

	@Column(name = "first_name")
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(final String name) {
		this.firstName = name;
	}

	String lastName;

	@Column(name = "last_name")
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(final String name) {
		this.lastName = name;
	}

	Date bday;

	@Column(name = "birth_date")
	public Date getBirthDate() {
		return bday;
	}

	public void setBirthDate(Date bday) {
		this.bday = bday;
	}

	int age;

	@Column(name = "age")
	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	List<Parent> parents;

	@ManyToMany(cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, mappedBy = "children", targetEntity = Parent.class)
	public List<Parent> getParents() {
		return this.parents;
	}

	public void setParents(List<Parent> parents) {
		this.parents = parents;
	}

	public void addParent(Parent p) {
		if (parents == null)
			parents = new ArrayList<Parent>();

		parents.add(p);
	}

	List<Person> friends;

	@OneToMany(targetEntity = Person.class)
	@JoinColumn(name = "friend_id")
	public List<Person> getFriends() {
		return this.friends;
	}

	public void setFriends(List<Person> friends) {
		this.friends = friends;
	}

	@Override
	public void doSave() {
		DaoManager.getPersonDao().save(this);
	}

	@Override
	public void doDelete() {
		DaoManager.getPersonDao().delete(this);
	}
}
