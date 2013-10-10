/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, CachingJpaTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.caching;

import org.springframework.transaction.annotation.*;

import org.junit.*;

import java.lang.reflect.ParameterizedType;
import java.net.*;

import static org.junit.Assert.*;

import javax.persistence.*;

import java.io.*;

import java.sql.*;
import com.solace.ExceptionHandler;
import com.solace.caching.ICache;
import com.solace.caching.ThreadLocalAccessor;
import com.solace.data.caching.TestService;
import com.solace.logging.*;

import java.util.*;
import org.springframework.context.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.*;

import org.codehaus.plexus.util.cli.StreamPumper;
import org.hibernate.tool.hbm2ddl.*;

/**
 * http://schuchert.wikispaces.com/JPA+Tutorial+1+-+Getting+Started
 * 
 * @After is handled in the base class and can be overridden
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 * 
 */
public class CachingJpaTests extends com.solace.data.BaseTest {
	private static Logger LOGGER = Logger.getLogger(CachingJpaTests.class);

	private TestService svc = null;

	Process p = null;

////	MemcachedThread memcached = null;
//
//	public class MemcachedThread extends Thread {
//		Logger LOGGER = Logger.getLogger(MemcachedThread.class);
//
//		boolean running = true;
//
//		public void signalStop() {
//			running = false;
//		}
//
//		@Override
//		public void run() {
//			try {
//				Process p = Runtime.getRuntime().exec(
//						"memcached.exe -vv");
//
//				BufferedReader in = new BufferedReader(new InputStreamReader(p
//						.getInputStream()));
//
//				String line = null;
//
//				while (running && (line = in.readLine()) != null) {
//					LOGGER.debug(line);
//				}
//
//				p.destroy();
//
//				p.waitFor();
//			} catch (Exception e) {
//				LOGGER.error(e.getMessage(), e);
//			}
//		}
//	}

	@Before
	public void setUp() throws Exception {
		boolean testExists = false;

		copyFile("/src/test/resources/META-INF/persistence.caching.xml",
				"/src/test/resources/META-INF/persistence.xml");

		if ((testExists = (new File("./target/test-classes")).exists()))
			copyFile("/src/test/resources/META-INF/persistence.caching.xml",
					"/target/test-classes/META-INF/persistence.xml");

		copyFile("/src/test/resources/daofactories.caching.xml",
				"/src/test/resources/daofactories.xml");

		if (testExists)
			copyFile("/src/test/resources/daofactories.caching.xml",
					"/target/test-classes/daofactories.xml");

		copyFile("/src/test/resources/caching.sql",
				"/src/test/resources/input.sql");

		if (testExists)
			copyFile("/src/test/resources/caching.sql",
					"/target/test-classes/input.sql");

//		memcached = new MemcachedThread();
//
//		memcached.start();
		

//		p = Runtime.getRuntime().exec("src/test/resources/memcached.exe -vv &");

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
			svc = (TestService) context.getBean("cachedService");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	@Test
	public void cacheTimeTest() throws Exception {
		svc.createParent();
	}


	@Test
	public void volumeTest() throws Exception {
		svc.loadVolume();
		LOGGER.debug("loading into cache");
		svc.findVolume();
		svc.findVolume();
	}

	// @After
	// @Override
	// public void tearDown() throws Exception {
	// p.destroy();
	//		
	// Thread.sleep(1000);
	//
	// LOGGER.debug("Shutting down memcached server.");
	//
	// super.tearDown();
	// }

	@Test
	public void simpleTest() throws Exception {
		svc.simpleTest();
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();

//		 p.destroy();
//		
//		 p.waitFor();
	}
}
