/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, CacheProvider.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching.hibernate;

import java.util.*;

import org.hibernate.cache.*;
import org.hibernate.cache.Cache;
import org.hibernate.cache.CacheException;
import org.hibernate.*;

import com.solace.ArgumentException;
import com.solace.ConfigurationException;
import com.solace.caching.*;
import com.solace.logging.*;

/**
 * The HibernateCacheAccessor implementation is meant to be the gateway
 * interface that allows Hibernate to plug into our caching framework.
 * <p>
 * This will allow us to grow and adapter for any cache implementation and plug
 * it into Hibernate (e.g. Memcached).
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 * 
 * @see com.solace.caching.Cache
 * @see com.solace.caching.CacheAccessor
 * @see #HIBERNATE_CACHE
 * 
 */
@SuppressWarnings("deprecation")
public class CacheProvider extends CacheAccessor implements
		org.hibernate.cache.CacheProvider {

	static final Logger LOGGER = Logger.getLogger(CacheProvider.class);

	private Map<String, org.hibernate.cache.Cache> m_caches = null;

	ICache m_cache = null;

	/**
	 * The key that the hibernate cache is key'd off of
	 */
	public static final String HIBERNATE_CACHE = "Hibernate";

	public CacheProvider() {
		super();
		m_caches = new HashMap<String, org.hibernate.cache.Cache>();
	}

	// throws ConfigurationException, ArgumentException, CacheException {
	@SuppressWarnings("deprecation")
	@Override
	public Cache buildCache(String arg0, Properties arg1) throws CacheException {

		return new com.solace.caching.hibernate.Cache(arg0,
				this.m_cache);
		// ICache cache = null;
		//
		// try {
		// cache = super.loadImplementation(HIBERNATE_CACHE);
		//
		// // if
		// // (!cache.getClass().isInstance(org.hibernate.cache.Cache.class))
		// if (!(cache instanceof org.hibernate.cache.Cache))
		// throw new ConfigurationException(
		// String
		// .format(
		// "The {} cache registration must implement org.hibernate.cache.Cache",
		// HIBERNATE_CACHE));
		//
		// cache.setRegionName(arg0);
		//
		// m_caches.put(arg0, (org.hibernate.cache.Cache) cache);
		// } catch (Exception e) {
		// throw new CacheException("Could not buildCache", e);
		// }

//		return (org.hibernate.cache.Cache) m_cache;
	}

	@Override
	public boolean isMinimalPutsEnabledByDefault() {
		return true;
	}

	@Override
	public long nextTimestamp() {
		return Timestamper.next();
	}

	@Override
	public void start(Properties arg0) throws CacheException {
		try {
			m_cache = super.loadImplementation(HIBERNATE_CACHE);
			
		} catch (Exception e) {
			throw new CacheException("Could not buildCache", e);
		}
	}

	@Override
	public void stop() {
		try {
			shutdown();
		} catch ( Exception e ) {
			throw new RuntimeException(e);
		}		
	}

	@Override
	public void shutdown() throws com.solace.caching.CacheException {
		try {
			m_cache.shutdown();
		} catch ( Exception e ) {
			throw new com.solace.caching.CacheException(e);
		}
	}
}
