/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, DistributedCacheTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

import com.solace.caching.ApplicationAccessor;
import com.solace.caching.CacheAccessor;
import com.solace.caching.MemcachedCache;
import com.solace.caching.ICache;
import com.solace.logging.*;

public class DistributedCacheTests {
	static Logger LOGGER = Logger.getLogger(DistributedCacheTests.class);

	Process process = null;
	
	


	@Before
	public void setup() throws Exception {

		Caches.CacheConfig config = new Caches.CacheConfig();

		config.setName("foo");

		Caches.CacheConfig.Type type = new Caches.CacheConfig.Type();
		type.setValue("com.solace.caching.DistributedCache");

		config.setType(type);

		Caches.CacheConfig.Type.Property p = new Caches.CacheConfig.Type.Property();

		p.setName(MemcachedCache.SERVER_COUNT);
		p.setValue("1");

		config.getType().getProperty().add(p);

		p = new Caches.CacheConfig.Type.Property();
		p.setName(String.format(MemcachedCache.SERVER_HOST, 0));
		p.setValue("localhost");

		config.getType().getProperty().add(p);

		p = new Caches.CacheConfig.Type.Property();
		p.setName(MemcachedCache.CACHE_TIMESPAN);
		p.setValue("3");

		config.getType().getProperty().add(p);

		p = new Caches.CacheConfig.Type.Property();
		p.setName(MemcachedCache.SOCKET_POOL_MINSIZE);
		p.setValue("5");

		config.getType().getProperty().add(p);

		p = new Caches.CacheConfig.Type.Property();
		p.setName(MemcachedCache.SOCKET_POOL_MAXSIZE);
		p.setValue("100");

		config.getType().getProperty().add(p);

		CacheAccessor.addConfiguration(config);

		LOGGER.debug("Starting memcached server");

//		process = Runtime.getRuntime().exec("src/test/resources/memcached.exe");

//		assertNotNull(process);

		Thread.sleep(2000);
	}

	@After
	public void tearDown() throws Exception {
//		if (process != null) {
//			process.destroy();
//			int exit = process.exitValue();
//			LOGGER.debugFormat("Memcached exit value {}", exit);
//		}

		ApplicationAccessor.getInstance("foo").shutdown();

		CacheAccessor.clearConfigurations();

		LOGGER.debug("Memcached has shutdown");
	}

	@Test
	public void testBasicGets() throws Exception {
		try {
			Caches.CacheConfig config = new Caches.CacheConfig();

			config.setName("foo");

			Caches.CacheConfig.Type type = new Caches.CacheConfig.Type();
			type.setValue("com.solace.caching.DistributedCache");

			config.setType(type);

			Caches.CacheConfig.Type.Property p = new Caches.CacheConfig.Type.Property();

			p.setName(MemcachedCache.SERVER_COUNT);
			p.setValue("1");

			config.getType().getProperty().add(p);

			p = new Caches.CacheConfig.Type.Property();
			p.setName(String.format(MemcachedCache.SERVER_HOST, 0));
			p.setValue("localhost");

			config.getType().getProperty().add(p);

			p = new Caches.CacheConfig.Type.Property();
			p.setName(MemcachedCache.CACHE_TIMESPAN);
			p.setValue("10");

			config.getType().getProperty().add(p);

			CacheAccessor.addConfiguration(config);

			ICache cache = ApplicationAccessor.getInstance("foo");

			if (cache.set("testBasicGets", "foo")) {
				Object obj = cache.get("testBasicGets");
				assertNotNull(obj);
			}

			if (cache.set("testBasicGets", "foo"))
				assertEquals("foo", cache.get("testBasicGets"));

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testMultipleThreadsAndApplicationAccessor() throws Exception {

		List<Thread> threads = new ArrayList<Thread>();

		try {
			for (int i = 0; i < 100; i++) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							ICache cache = ApplicationAccessor
									.getInstance("foo");

							for (int j = 0; j < 10000; j++) {

								Integer val = (Integer) cache.get(Integer
										.toString(j));

								boolean set = true;
								if (val == null) {
									LOGGER
											.debug(
													"did not find {} in the cache ... adding",
													j);
									set = cache.set(Integer.toString(j),
											new Integer(j));
								} else
									LOGGER.debug("Found {}", val);

								if (set)
									Assert.assertEquals(new Integer(j), cache
											.get(Integer.toString(j)));
							}
						} catch (Exception e) {
							LOGGER.error(e.getMessage(), e);
							fail(e.getMessage());
						}
					}
				});
				threads.add(t);
				t.start();
			}

			// wait for the threads to complete so that junit doesn't quit
			for (Thread t : threads)
				t.join();

		} catch (Exception e2) {
			LOGGER.error(e2.getMessage(), e2);
			fail(e2.getMessage());
		}
	}

	@Test
	public void testSet() {
		try {
			ICache cache = ApplicationAccessor.getInstance("foo");

			boolean set = cache.set("foo", "bar");

			if (!set)
				fail("did not set.");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	@Test
	public void testSetGet() {
		try {
			ICache cache = ApplicationAccessor.getInstance("foo");

			testSet();

			Object o = cache.get("foo");

			assertNotNull(o);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	@Test
	public void delete() {
		try {
			ICache cache = ApplicationAccessor.getInstance("foo");

			boolean ret = cache.set("delete", "foo");

			if (ret)
				assertNotNull(cache.get("delete"));

			boolean b = cache.delete("delete");

			assertTrue(b);

			assertNull(cache.get("delete"));

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	@Test
	public void settingObjects() {
		
		try {
			ICache cache = ApplicationAccessor.getInstance("foo");
			
			for(int i=0; i<1000; i++)
			{
				cache.set((new Integer(i)).toString(), new TestObject(i));
			}
			
			for(int i=0; i<1000; i++)
			{
				TestObject obj = (TestObject)cache.get((new Integer(i)).toString());				
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	@Test
	public void cacheKeyExceeds250() throws Exception {

		try {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 251; i++)
				sb.append("1");

			ICache cache = ApplicationAccessor.getInstance("foo");

			if (cache.set(sb.toString(), "foo")) {
				assertNotNull(cache.get(sb.toString()));
				assertEquals("foo", cache.get(sb.toString()));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail();
		}
	}
}
