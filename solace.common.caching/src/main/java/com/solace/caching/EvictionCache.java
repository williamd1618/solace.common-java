/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, EvictionCache.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package com.solace.caching;

import com.solace.*;
import com.solace.caching.Caches.CacheConfig;
import com.solace.logging.*;
import com.solace.utility.*;

/**
 * The EvictionCache is an abstract implementation that all caches with an
 * eviction policy are meant to derive from. It gives the ability to evict an
 * object as well as dynamically invoke a HandleEviction that is set by the
 * configuration. Properties that can be passed in {@link Caches.CacheConfig}
 * <p>
 * Derived implementations are {@link LRUCache} and {@link ExpirationCache}
 * @see EVICTION_HANDLER_CLASS
 * @author dwilliams
 * 
 */
public abstract class EvictionCache extends Cache {

	private static final String EVICTING_KEY_S = "Evicting key: {}";

	static Logger LOGGER = Logger.getLogger(EvictionCache.class);

	/**
	 * meant to be the class that represents the implementation of {@ink IEvictionHandler}
	 */
	public static String EVICTION_HANDLER_CLASS = "EvictionHandler.Class";

	private IEvictionHandler m_evictionHandler;

	/**
	 * @param config
	 */
	public EvictionCache(CacheConfig _config) throws CacheException,
			ArgumentException {
		super(_config);

		String clazz = "";

		if ((clazz = getParameters().get(EVICTION_HANDLER_CLASS)) != null) {
			LOGGER.info("EvictionHandler of: {}", clazz);

			try {
				m_evictionHandler = ReflectionUtil
						.<IEvictionHandler> createInstance(clazz);
			} catch (Exception e) {
				throw new ArgumentException(String.format(
						"Error loading class: {}", clazz), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public String getRegionName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 */
	@Override
	public void setRegionName(String name) {
		// TODO Auto-generated method stub

	}

	public void evict(String _key) throws CacheException {
		LOGGER.debug(EVICTING_KEY_S, _key);
		if (_key != null && !_key.isEmpty()) {
			handleEviction(_key, this.get(_key));
		} else
			throw new CacheException("_key must take a non-null string.");
	}

	public void evict(CacheItem _item) throws CacheException {
		LOGGER.debug(EVICTING_KEY_S, _item.getKey());
		if (_item != null) {
			handleEviction(_item.getKey(), _item.getObject());
		} else {
			throw new CacheException("Evict must take in a not-null CacheItem.");
		}
	}

	/**
	 * Provides a template for eviction operations.
	 * 
	 * @param _key
	 * @param _val
	 * @throws CacheException
	 */
	private void handleEviction(String _key, Object _val) throws CacheException {
		delete(_key);

		if (this.m_evictionHandler != null)
			m_evictionHandler.evict(this, _key, _val);
	}

	/**
	 * The IEvictionHandler interface is meant to be a pluggable/loadable
	 * interface that is responsible for firing to evict entities from the
	 * {@link CacheConfig}.
	 * <p>
	 * The implementation of this interface may end up becoming an
	 * {@link Autowired}/{@link Inject} concern in the system.
	 * 
	 * @author dwilliams
	 * 
	 */
	public interface IEvictionHandler {
		/**
		 * The eviction method that is to be implemented.
		 * 
		 * @param _cache
		 *            the cache to evict from
		 * @param _key
		 *            the key to evict for
		 * @param _o
		 *            an optional object to replace the _key with
		 */
		public void evict(EvictionCache _cache, String _key, Object _o)
				throws CacheException;
	}
}
