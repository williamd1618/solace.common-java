/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, ApplicationAccessorTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import com.solace.caching.*;
import com.solace.logging.*;

import org.junit.*;

public class ApplicationAccessorTests {
	
	static final Logger LOGGER = Logger.getLogger(ApplicationAccessorTests.class);

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
	
	private Thread m_hitThread;
	public void showHit(Thread t)
	{
		m_hitThread = t;
	}
	
	private Thread createThread()
	{
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ICache cache = ApplicationAccessor.getInstance("foo");

					if ( cache.get("foo") == null )
					{
						if ( cache.set("foo", "bar") )
							LOGGER.debug("foo set");
						else
							LOGGER.debug("foo NOT set");
					}															
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					Assert.fail(e.getMessage());
				}
			}
		});

		return t;
	}

	@Test
	public void testThreadAccesses() throws Exception {
		setupCacheConfig();
	
		Thread t1 = createThread();
		
		t1.start();
		
		t1.join();
		
		Thread t2 = createThread();
		
		t2.start();
		
		t2.join();
	}
}
