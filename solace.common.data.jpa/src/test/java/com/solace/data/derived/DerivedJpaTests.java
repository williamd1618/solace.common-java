/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, DerivedJpaTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.derived;

import org.springframework.transaction.annotation.*;

import org.junit.*;

import java.lang.reflect.ParameterizedType;
import java.net.*;

import static org.junit.Assert.*;

import javax.persistence.*;

import java.io.*;

import java.sql.*;
import com.solace.ExceptionHandler;
import com.solace.data.BaseTest;
import com.solace.data.derived.TestService;
import com.solace.logging.*;

import java.util.*;
import org.springframework.context.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.*;

import org.hibernate.tool.hbm2ddl.*;

/**
 * http://schuchert.wikispaces.com/JPA+Tutorial+1+-+Getting+Started
 * 
 * @After is handled in the base class and can be overridden
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 * 
 */
public class DerivedJpaTests extends com.solace.data.BaseTest {
	private static Logger LOGGER = Logger.getLogger(DerivedJpaTests.class);

	private TestService svc = null;
	
//	@Before
//	public void setUp() throws Exception {
//		copyFile("/src/test/resources/META-INF/persistence.basic.xml",
//				"/src/test/resources/META-INF/persistence.xml");
//
//		copyFile("/src/test/resources/daofactories.basic.xml",
//				"/src/test/resources/daofactories.xml");
//
//		copyFile("/src/test/resources/basic.sql",
//				"/src/test/resources/input.sql");
//		
//		super.setUp();
//		
//		try {
//			svc = (TestService) context.getBean("testService");
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage(), e);
//			fail("Exception in loading daofactories.xml");
//		}
//	}

	@Before
	public void setUp() throws Exception {
		boolean testExists = false;
		
		copyFile("/src/test/resources/META-INF/persistence.derived.xml",
				"/src/test/resources/META-INF/persistence.xml");
		
		if ( (testExists = (new File("./target/test-classes")).exists()) )
			copyFile("/src/test/resources/META-INF/persistence.derived.xml",
				"/target/test-classes/META-INF/persistence.xml");


		copyFile("/src/test/resources/daofactories.derived.xml",
				"/src/test/resources/daofactories.xml");
		
		if ( testExists )
			copyFile("/src/test/resources/daofactories.derived.xml",
				"/target/test-classes/daofactories.xml");


		copyFile("/src/test/resources/derived.sql",
				"/src/test/resources/input.sql");
		
		if ( testExists )
			copyFile("/src/test/resources/derived.sql",
				"/target/test-classes/input.sql");


		super.setUp();
		
		// this may need to be here due to some odd behavior in spring
		try {
			String[] configFiles = new String[] { DAO_FACTORY_CONFIG_FILE };
			
			LOGGER.debug("Loading config files: ");
			
			for(String s : configFiles)
				LOGGER.debug(s);

			context = new ClassPathXmlApplicationContext(configFiles);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}


		try {
			svc = (TestService) context.getBean("derivedService");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	@Test
	public void bulkSave() throws Exception {
		svc.builkSave();

		assertEquals(DaoManager.getParentDao().findAll().size(), 100);
		assertEquals(DaoManager.getPersonDao().findAll().size(), 100);

		for (Person p : DaoManager.getPersonDao().findAll())
			assertEquals(p.getId().toString(), p.getFirstName());
	}

	@Test
	public void createChildrenFromParent() throws Exception {
		svc.createChildrenFromParent();

	}

	@Test
	public void createParentFromPerson() {
		svc.createParentFromPerson();

	}

	@Test
	public void checkChildCount() throws Exception {
		svc.createChildrenFromParent();

	}

	@Test
	public void checkParentCount() throws Exception {
		svc.createChildrenFromParent();

	}

	@Test
	public void testLazy() throws Exception {
		svc.testLazy();
	}
}
