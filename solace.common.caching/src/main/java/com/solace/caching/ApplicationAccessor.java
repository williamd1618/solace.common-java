/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, ApplicationAccessor.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import java.util.*;

import com.solace.*;
import com.solace.logging.*;

/**
 * Will guarantee that only a single instance of a cache configuration can exist
 * in the application. This class should be used for shared, in-memory caching.
 * 
 * @author dwilliams
 * 
 */
public class ApplicationAccessor extends CacheAccessor {
	static Logger LOGGER = Logger.getLogger(ApplicationAccessor.class);

	private static Map<String, ICache> m_caches = new HashMap<String, ICache>();

	private ApplicationAccessor() {
		super();
	}

	/**
	 * Will look into m_caches and see if an instance exists, if not will
	 * attempt to create one
	 * 
	 * @param _name
	 *            name of the {@link Caches.CacheConfig}
	 * @return an instance of an {@link ICache}
	 * @throws CacheException
	 *             thrown by default
	 * @throws ArgumentException
	 *             will be thrown if the ReflectionUtil cannot create an
	 *             instance
	 * @throws ConfigurationException
	 *             thrown if there is no CacheConfig
	 */
	public static ICache getInstance(String _name) throws CacheException,
			ArgumentException, ConfigurationException {
		ICache instance = null;

		synchronized (m_caches) {
			if ((instance = m_caches.get(_name)) == null) {
				ApplicationAccessor cache = new ApplicationAccessor();

				cache.setCache(loadImplementation(_name));

				cache.setRegionName(_name);
				
				instance = (ICache) cache;

				m_caches.put(_name, instance);
			}
		}

		return instance;
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.solace.caching.ICache#getParameter()
	 */
	@Override
	public Map<String, String> getParameters() {
		return this.m_cache.getParameters();
	}
}
