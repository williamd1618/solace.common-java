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
package com.solace.data.basic;

import static org.junit.Assert.*;
import org.springframework.transaction.annotation.*;

import com.solace.logging.*;

import java.util.*;

//import org.springframework.context.*;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.*;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
@Service(value="testService")
//http://en.wikibooks.org/wiki/Java_Persistence/OneToMany#Example_of_a_OneToMany_relationship_and_inverse_ManyToOne_annotations
public class TestService {
	
	static final Logger LOGGER = Logger.getLogger(TestService.class);
	
	/**
	 * validates a delete occurs
	 */
	public void delete() throws Exception
	{
		Person p = new Person("Dan", "Williams", 32);
		
		p.save();
		
		List<Person> people = DaoManager.getPersonDao().findAll();
		
		for(Person o : people)
			o.delete();
	}
	
	public Person create()
	{
		Person p = new Person("Dan","Williams",32);
		
		p.save();
		
		return p;
	}
	
	
	public void update(Person p)
	{
		p.setAge(p.getAge()+1);
		p.save();		
	}
	
	public Person friends()
	{
		Person p = new Person("Person", "One", 30);
		
		p.save();
		
		Person p2 = new Person("Person", "Two", 30);
		
		p2.save();
		
		ArrayList<Person> friends = new ArrayList<Person>();
		
		friends.add(p2);
		
		p.setFriends(friends);
		
		p.save();
		
		return p;
	}
	
	
	
	public void rollbackExample() throws Exception
	{
		saveAndFindAll();
		
		throw new Exception("rollback");
	}
	
	
	/**
	 * validates that the version identifier is inserted and incremented
	 */
	public void versionIncrementCheck()
	{
		Person a = new Person("Daniel", "Williams", 32);
		
		a.save();
		
		for(int i=1;i<10;i++)
		{
			//getting older line by line.  :(
			a.setAge(a.getAge()+1);
			a.save();
						
//			assertEquals(new Long(i),a.getVersion());
		}
	}
	
	public void saveAndFindAll() throws Exception
	{
		for(int i=0; i<100;i++)
			(new Person("Foo","Bar", (new Integer(i)).intValue())).save();

		List<Person> as = DaoManager.getPersonDao().findAll();

		assertEquals(100, DaoManager.getPersonDao().findAll().size());
	}
	
	public void saveAndFindById() throws Exception
	{
		Person a = new Person("Daniel","Williams",32);
		
		a.save();
		
		Person b = DaoManager.getPersonDao().findById(a.getId());
		
		assertEquals(a,b);
	}
}
