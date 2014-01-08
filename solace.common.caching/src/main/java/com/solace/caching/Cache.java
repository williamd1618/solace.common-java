/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, Cache.java, and the accompanying materials
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

import java.util.*;

/**
 * The most base class that solidifies all actions to a cache. This
 * implementation will hold data in memory for an undetermined amount of time.
 * <p>
 * 
 * @author dwilliams
 * 
 */
public class Cache implements ICache {
	private static final String DELETING_ITEM_S = "Deleting item: [{}].";

	private static final String DELETING_ITEM_S2 = DELETING_ITEM_S;

	private static final String CACHE_CONFIG_MISS_FOR_ITEM_S = "CacheConfig miss for item: [{}].";

	private static final String CACHE_CONFIG_HIT_FOR_ITEM_S = "CacheConfig hit for item: [{}].";

	private static Logger LOGGER = Logger.getLogger(Cache.class);

	private Caches.CacheConfig m_config = null;

	protected Map<String, CacheItem> m_cacheItems;

	protected Map<String, String> m_params = null;

	private String m_region = "";

	public Cache(Caches.CacheConfig _config) {
		m_cacheItems = new HashMap<String, CacheItem>();

		m_params = new HashMap<String, String>();

		m_config = _config;

		loadParams();
	}

	/**
	 * Simply interates and pushes all
	 * {@link Caches.CacheConfig.Type.Properties} inot a Map
	 */
	private void loadParams() {
		// only iterate if the properties collection has elements
		if (m_config != null && m_config.getType().getProperty() != null
				&& m_config.getType().getProperty().size() > 0) {
			for (Caches.CacheConfig.Type.Property prop : m_config.getType()
					.getProperty()) {
				m_params.put(prop.getName(), prop.getValue());
			}
		}
	}

	@Override
	public Map<String, String> getParameters() {
		return this.m_params;
	}

	@Override
	public boolean set(ICacheable _obj) throws CacheException {
		return set(_obj.getCacheKey(), _obj);
	}

	@Override
	public boolean set(String _key, Object _obj) throws CacheException {
		synchronized (m_cacheItems) {
			if (!m_cacheItems.containsKey(_key)) {

				LOGGER.debug("caching item [{}]", _key);

				m_cacheItems.put(_key, new CacheItem(_key, _obj));
			}
		}
		return true;
	}

	@Override
	public Object get(ICacheable _item) throws CacheException {
		return get(_item.getCacheKey());
	}

	@Override
	public Object get(String _key) throws CacheException {
		Object retVal = null;

		synchronized (m_cacheItems) {
			if (!m_cacheItems.containsKey(_key)) {
				retVal = null;
			} else {
				CacheItem item = m_cacheItems.get(_key);

				if (item != null) {
					LOGGER.debug(CACHE_CONFIG_HIT_FOR_ITEM_S, _key);
					retVal = item.getObject();
				} else {
					LOGGER.debug(CACHE_CONFIG_MISS_FOR_ITEM_S, _key);
				}
			}
		}

		return retVal;
	}

	@Override
	public boolean delete(ICacheable _item) throws CacheException {
		return delete(_item.getCacheKey());
	}

	@Override
	public boolean delete(String _key) throws CacheException {
		boolean retVal = false;

		synchronized (m_cacheItems) {
			retVal = m_cacheItems.remove(_key) != null;
			LOGGER.debug(DELETING_ITEM_S2, _key);
		}

		return retVal;
	}

	@Override
	public void clear() throws CacheException {
		LOGGER.debug("Clearing the cache!");

		synchronized (m_cacheItems) {
			m_cacheItems.clear();
		}
	}

	@Override
	public void setRegionName(String _name) {
		this.m_region = _name;
	}

	@Override
	public String getRegionName() {
		return m_region;
	}

	/**
	 * The base class for all things that are to be cached InProc. Can be used
	 * out of proc but is not completely necessary.
	 * <p>
	 * Can be derived in a fashion to allow for specific functionality.
	 * 
	 * @author dwilliams
	 * 
	 */
	public class CacheItem {

		protected Object m_object;

		public Object getObject() {
			return m_object;
		}

		public void setObject(Object _obj) {
			m_object = _obj;
		}

		protected String m_key;

		public String getKey() {
			return m_key;
		}

		public void setKey(String _key) {
			m_key = _key;
		}

		public CacheItem(String _key, Object _item) {
			m_object = _item;
			m_key = _key;
		}
	}

	@Override
	public void shutdown() throws CacheException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.solace.caching.ICache#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(Object key) throws CacheException {
		// TODO Auto-generated method stub
		return delete(key.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.solace.caching.ICache#get(java.lang.Object)
	 */
	@Override
	public Object get(Object key) throws CacheException {
		// TODO Auto-generated method stub
		return get(key.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.solace.caching.ICache#set(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean set(Object key, Object obj) throws CacheException {
		// TODO Auto-generated method stub
		return set(key.toString(), obj);
	}

	@Override
	public void incr(String key) throws CacheException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void decr(String key) throws CacheException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void incr(String key, long delta) throws CacheException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void decr(String key, long delta) throws CacheException {
		throw new UnsupportedOperationException();	
	}
}
