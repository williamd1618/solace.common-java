/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, LRUCache.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import com.solace.*;
import com.solace.caching.*;
import com.solace.logging.*;

import java.util.*;

/**
 * Is an EvictionCache for in process purposes only. It is meant to maintain a
 * queue depth that is passed in via the LRUCache.MaxDepth property via the
 * {@link Caches.CacheConfig.Type.Properties.Property}.
 * <p>
 * The LRUCache implementation evicts an entity when the queue depth is met.
 * When this boundary is hit the implementation sorts the caches entities by the
 * last accessed properties.
 * <p>
 * 
 * @see #LRUCACHE_MAXDEPTH has a default setting of 1000. When this value is hit
 *      the least recently used element is evicted.
 * 
 * @author dwilliams
 * 
 */
public class LRUCache extends EvictionCache {
	private static final String LRU_CACHE_HIT_FOR_S = "LRUCache hit for [{}].";

	private static final String LRU_CACHE_MISS_FOR_ITEM_S = "LRUCache miss for item [{}].";

	private static final String CACHING_ITEM_ALREADY_EXISTS_FOR_S_REPLACING = "Caching item already exists for [{}], replacing.";

	private static final String CACHING_ITEM_S = "Caching item: [{}].";

	private static final String APPLYING_LRU_EVICTION_TO_S = "Applying LRU eviction to [{}].";

	private static Logger LOGGER = Logger.getLogger(LRUCache.class);

	private int m_maxQueueDepth = 1000;

	/**
	 * the max depth for the cache before the eviction sort works
	 */
	public static final String LRUCACHE_MAXDEPTH = "LRUCache.MaxDepth";

	public LRUCache(Caches.CacheConfig _config) throws CacheException,
			ArgumentException {
		super(_config);

		String tmp;

		if ((tmp = getParameters().get(LRUCACHE_MAXDEPTH)) != null) {
			m_maxQueueDepth = Integer.parseInt(tmp);
			LOGGER.info("LRUCache.MaxDepth set to {}", m_maxQueueDepth);
		}
	}

	@Override
	public boolean set(ICacheable _obj) throws CacheException {
		return set(_obj.getCacheKey(), _obj);
	}

	@Override
	public boolean set(String _key, Object _obj) throws CacheException {
		synchronized (this) {

			if (m_cacheItems.size() >= m_maxQueueDepth) {
				List<CacheItem> list = new ArrayList<CacheItem>(m_cacheItems
						.values());

				// pass in anonymous class for comparison operations
				Collections.sort(list, new Comparator() {
					public int compare(Object _lhs, Object _rhs) {
						int retVal = 0;

						if (!(_lhs instanceof LRUCacheItem)
								|| !(_rhs instanceof LRUCacheItem)) {
							return 1;
						} else {

							LRUCacheItem lhs = (LRUCacheItem) _lhs;
							LRUCacheItem rhs = (LRUCacheItem) _rhs;

							if (lhs.getLastAccessed().compareTo(
									rhs.getLastAccessed()) < 0)
								return -1;
							else
								return 1;
						}
					}
				});

				LRUCacheItem moribund = (LRUCacheItem) list.get(0);

				LOGGER.debug(APPLYING_LRU_EVICTION_TO_S, moribund
						.getKey());

				evict(moribund.getKey());

				LOGGER.debug(CACHING_ITEM_S, _key);
			} else if (m_cacheItems.containsKey(_key)) {
				LOGGER.debug(
						CACHING_ITEM_ALREADY_EXISTS_FOR_S_REPLACING,
						_key);
			} else {
				LOGGER.debug(CACHING_ITEM_S, _key);
			}

			m_cacheItems.put(_key, new LRUCacheItem(_key, _obj));
		}
		
		return true;
	}

	@Override
	public Object get(ICacheable _key) {
		return get(_key.getCacheKey());
	}

	@Override
	public Object get(String _key) {
		Object retVal = null;

		synchronized (this) {
			if (!m_cacheItems.containsKey(_key)) {
				LOGGER.debug(LRU_CACHE_MISS_FOR_ITEM_S, _key);
			} else {
				LRUCacheItem item = (LRUCacheItem) m_cacheItems.get(_key);

				LOGGER.debug(LRU_CACHE_HIT_FOR_S, _key);

				retVal = item.getObject();

				item.setLastAccessed(new Date());
			}
		}

		return retVal;
	}

	/**
	 * The LRUCacheItem is meant to be an internal class to maintain information
	 * specific to the LRUCache implementation.
	 * 
	 * @author dwilliams
	 * 
	 */
	private final class LRUCacheItem extends CacheItem {
		private Date m_lastAccessed = new Date();

		public LRUCacheItem(String key, Object item) {
			super(key, item);

			m_lastAccessed = new Date();
		}

		public Date getLastAccessed() {
			return m_lastAccessed;
		}

		public void setLastAccessed(Date _date) {
			m_lastAccessed = _date;
		}
	}

	/* (non-Javadoc)
	 * @see com.solace.caching.Cache#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(Object key) throws CacheException {
		// TODO Auto-generated method stub
		return delete(key.toString());
	}

	/* (non-Javadoc)
	 * @see com.solace.caching.Cache#get(java.lang.Object)
	 */
	@Override
	public Object get(Object key) throws CacheException {
		// TODO Auto-generated method stub
		return get(key.toString());
	}

	/* (non-Javadoc)
	 * @see com.solace.caching.Cache#set(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean set(Object key, Object obj) throws CacheException {
		// TODO Auto-generated method stub
		return set(key.toString(), obj);
	}
}
