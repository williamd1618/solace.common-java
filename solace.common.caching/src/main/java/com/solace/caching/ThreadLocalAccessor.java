/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, ThreadLocalAccessor.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import java.util.*;

import com.solace.ArgumentException;
import com.solace.ConfigurationException;

/**
 * Meant to serve as a single, thread specific access point for getting a
 * {@link CacheAccessor} associated with a {@link CacheConfig}. {@code {@code
 * ICache cache = ThreadLocalAccessor.getInstance("Some.Cache.Name");
 * 
 * cache.put("foo","bar"); * }
 * 
 * @author dwilliams
 * 
 */
public class ThreadLocalAccessor extends CacheAccessor {

	static String m_key = "ThreadLocalAccessor";

	static ThreadLocalAccessor m_instance = null;

	/*
	 * A static, anonymous inner class for maintaining the cache implementation
	 * per thread
	 */
	static ThreadLocal<HashMap<String, ThreadLocalAccessor>> m_tlCaches = new ThreadLocal<HashMap<String, ThreadLocalAccessor>>() {
		protected HashMap<String, ThreadLocalAccessor> initialValue() {
			return new HashMap<String, ThreadLocalAccessor>();
		}
	};

	/**
	 * static accessor to enforce a singleton and make sure that all
	 * ThreadLocalAccessors are stored for the thread.
	 * 
	 * @param _name
	 *            the nam eof the cache to load
	 * @return a ThreadLocalAccessor
	 * @throws Exception
	 *             if there is an issue in constructing the ThreadLocalAccessor
	 * @see com.solace.caching.Caches
	 * @see com.solace.caching.Caches.CacheConfig
	 */
	public synchronized static ICache getInstance(String _name)
			throws ConfigurationException, ArgumentException, CacheException {

		String key = m_key + _name;

		HashMap<String, ThreadLocalAccessor> caches = m_tlCaches.get();

		if (null == (m_instance = caches.get(key))) {
			m_instance = new ThreadLocalAccessor();

			m_instance.setCache(loadImplementation(_name));

			caches.put(key, m_instance);
		}

		return m_instance;
	}

	private ThreadLocalAccessor() {
		super();
	}

	@Override
	public String getRegionName() throws CacheException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void  setRegionName(String name) throws CacheException {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
