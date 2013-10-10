/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, ThreadLocalAcessorTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import com.solace.caching.*;
import com.solace.caching.Caches.CacheConfig;
import com.solace.logging.*;

import org.junit.*;

public class ThreadLocalAcessorTests {
	
	public void setupCacheConfig() throws Exception {
		Caches.CacheConfig config = new Caches.CacheConfig();

		config.setName("foo");

		Caches.CacheConfig.Type type = new Caches.CacheConfig.Type();
		type.setValue("com.solace.caching.LRUCache");

		config.setType(type);

		Caches.CacheConfig.Type.Property p = new Caches.CacheConfig.Type.Property();

		p.setName(LRUCache.LRUCACHE_MAXDEPTH);
		p.setValue("3");

		config.getType().getProperty().add(p);

		CacheAccessor.addConfiguration(config);
	}

	@Test
	public void testThreadLocal() throws Exception {
		setupCacheConfig();
		for (int i = 0; i < 3; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ICache cache = ThreadLocalAccessor.getInstance("foo");

						cache.set("foo", "bar");

						Assert.assertEquals("bar", cache.get("foo"));
						Assert.assertEquals("bar", cache.get("foo"));
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
						Assert.fail(e.getMessage());
					}
				}
			});
			t.start();
		}
	}

	@Test	
	public void testEvictionAlgorithm() throws Exception {
		setupCacheConfig();
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ICache cache = ThreadLocalAccessor.getInstance("foo");

						for (int j = 0; j < 10; j++) {
							cache.set(Integer.toString(j), new Integer(j));

							Assert.assertEquals(new Integer(j), cache
									.get(Integer.toString(j)));
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
						Assert.fail(e.getMessage());
					}
				}
			});
			t.start();
		}
	}

	@Test
	public void testEvictionHandler() throws Exception {
		Caches.CacheConfig config = new Caches.CacheConfig();

		config.setName("foo");

		Caches.CacheConfig.Type type = new Caches.CacheConfig.Type();
		type.setValue("com.solace.caching.LRUCache");

		config.setType(type);

		Caches.CacheConfig.Type.Property p = new Caches.CacheConfig.Type.Property();

		p.setName(LRUCache.LRUCACHE_MAXDEPTH);
		p.setValue("3");

		config.getType().getProperty().add(p);
		
		p = new Caches.CacheConfig.Type.Property();
		p.setName(LRUCache.EVICTION_HANDLER_CLASS);
		p.setValue("com.solace.caching.MyEvictionHandler");
		
		config.getType().getProperty().add(p);

		CacheAccessor.addConfiguration(config);

		for (int i = 0; i < 2; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ICache cache = ThreadLocalAccessor.getInstance("foo");

						for (int j = 0; j < 5; j++) {
							cache.set(Integer.toString(j), new Integer(j));

							Assert.assertEquals(new Integer(j), cache
									.get(Integer.toString(j)));
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
						Assert.fail(e.getMessage());
					}
				}
			});
			t.start();
		}
	}
}
