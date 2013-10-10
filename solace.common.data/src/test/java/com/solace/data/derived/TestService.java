/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, TestService.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.derived;

import java.util.*;

import static org.junit.Assert.*;

import org.springframework.stereotype.*;

import com.solace.logging.*;

import org.springframework.transaction.annotation.*;

@Service(value = "derivedService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
public class TestService {

	static final Logger LOGGER = Logger.getLogger(TestService.class);

	public void builkSave() {
		for (int i = 0; i < 100; i++) {
			String s = Integer.toString(i);
			(new Parent(s, s, 31, new java.util.Date(1, 21,
					1978), Calendar.getInstance().getTime())).save();
		}
	}

	/**
	 * Creates a Parent and relates a child. If the owning entity, parent, does
	 * not ahve cascadetype set you must save child first and then add to the
	 * parent collection. o.w. you an create both inline and save.
	 */
	public void createChildrenFromParent() throws Exception {
		for (int i = 0; i < 100; i++) {
			String s = Integer.toString(i);
			// parent owns the relationship
			Parent p = new Parent(s,s, 31, new java.util.Date(
					1, 31, 1978), Calendar.getInstance().getTime());

			Person kid = new Person(s,s, 11, new java.util.Date(1, 1,
					1990));

			// if CascadeType MERGE and PERSIST are not set on the owning entity
			// then you must save first
//			kid.save();
			p.addChild(kid);

			p.save();
		}
		
		for(Parent p : DaoManager.getParentDao().findAll())
		{
			assertEquals(p.getPerson().getFirstName(), p.getChildren().get(0).getFirstName());			
		}	
	}

	public void createParentFromPerson() {
		for (int i = 0; i < 10; i++) {
			String s = Integer.toString(i);
			Person p = new Person(s,s, 32, new java.util.Date(
					1, 21, 1978));

			p.addParent(new Parent(s,s, 60, new java.util.Date(1,
					1, 1950), new java.util.Date(1, 1, 1960)));

			p.save();
			
			assertEquals(p.getParents().size(),1);
		}

//		Unfortunately this doesn't work because of the inverse relationship on Parent owning the M:M
//		for(Parent parent : DaoManager.getParentDao().findAll())
//		{
//			for(Person person : parent.getChildren())
//				assertEquals(parent.getPerson().getFirstName(), person.getFirstName());
//		}
	}

	public void checkParents() throws Exception {
		this.createChildrenFromParent();

		List<Person> persons = DaoManager.getPersonDao().findAll();

		for (Person p : persons)
			assertEquals(p.getParents().size(), 1);
	}
	
	public void testLazy() throws Exception
	{
		this.builkSave();
		
		for(Parent parent : DaoManager.getParentDao().findAll())
			LOGGER.debug(parent.getPerson().getFirstName());
	}
}
