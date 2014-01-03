/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, BasicJpaTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.basic;

import org.springframework.transaction.annotation.*;

import org.junit.*;

import java.lang.reflect.ParameterizedType;
import java.net.*;

import static org.junit.Assert.*;

import javax.persistence.*;

import java.io.*;

import java.sql.*;
import com.solace.ExceptionHandler;
import com.solace.logging.*;

import java.util.*;
import org.springframework.context.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.*;

import org.hibernate.tool.hbm2ddl.*;

/**
 * http://schuchert.wikispaces.com/JPA+Tutorial+1+-+Getting+Started
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a> t
 */
public class BasicJpaTests extends com.solace.data.BaseTest {
	private static Logger LOGGER = Logger.getLogger(BasicJpaTests.class);

	/**
	 * this class must be on the classpath
	 */
	private TestService svc = null;

	@Before
	public void setUp() throws Exception {
		boolean testExists = false;
		copyFile("/src/test/resources/META-INF/persistence.basic.xml",
				"/src/test/resources/META-INF/persistence.xml");

		if ((testExists = (new File("./target/test-classes")).exists()))
			copyFile("/src/test/resources/META-INF/persistence.basic.xml",
					"/target/test-classes/META-INF/persistence.xml");

		copyFile("/src/test/resources/daofactories.basic.xml",
				"/src/test/resources/daofactories.xml");

		if (testExists)
			copyFile("/src/test/resources/daofactories.basic.xml",
					"/target/test-classes/daofactories.xml");

		copyFile("/src/test/resources/basic.sql",
				"/src/test/resources/input.sql");

		if (testExists)
			copyFile("/src/test/resources/basic.sql",
					"/target/test-classes/input.sql");

		super.setUp();

		// this may need to be here due to some odd behavior in spring
		try {
			String[] configFiles = new String[] { DAO_FACTORY_CONFIG_FILE };

			LOGGER.debug("Loading config files: ");

			for (String s : configFiles)
				LOGGER.debug(s);

			context = new ClassPathXmlApplicationContext(configFiles);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}

		try {
			svc = (TestService) context.getBean("testService");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testFriends() {
		Person p = svc.friends();

		assertEquals(p.getFriends().size(), 1);
	}

	@Test
	public void rollbackTest() throws Exception {
		try {
			svc.rollbackExample();
		} catch (Exception e) {
		}

		assertEquals(DaoManager.getPersonDao().findAll().size(), 0);
	}

	@Test
	public void testSave() throws Exception {
		svc.saveAndFindAll();
	}

	@Test
	public void testDelete() throws Exception {
		svc.delete();
	}

	@Test
	public void testVersion() {

		// Person p = svc.create();
		//		
		// for(int i=1;i<10;i++)
		// {
		// svc.update(p);
		// }
		svc.versionIncrementCheck();
	}

	@Test
	public void testFindById() throws Exception {
		svc.saveAndFindById();
	}
	
	@Test
	public void testQueryList() throws Exception {

		Person p = svc.friends();

		List<Person> people = DaoManager.getPersonDao().queryObjects(
				"from Person p where p.firstName = ?", "Person");
		
		assert(people.size() == 2);
	}

	@Test
	public void testQuerySingle() throws Exception {
		Person p = svc.friends();

		Person person = DaoManager.getPersonDao().queryObject(
				"from Person p where p.firstName = ? and p.lastName = ?", "Person", "One");
		
		assert(person != null);
	}
}
