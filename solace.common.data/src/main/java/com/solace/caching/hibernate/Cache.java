/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, Cache.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching.hibernate;

import java.util.Map;

import org.hibernate.cache.*;
import com.solace.ArgumentException;
import com.solace.caching.*;
import com.solace.caching.Caches.CacheConfig;
import com.solace.logging.*;

/**
 * HibernateDistributedCache is the adapter to the
 * {@link org.hibernate.cache.Cache} interface. This class is dynamically loaded
 * via the {@link CacheProvider} implementation which derives off of the concept
 * of {@link CacheAccessor}.
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 * 
 */
@SuppressWarnings("deprecation")
public class Cache implements org.hibernate.cache.Cache {

	com.solace.caching.ICache cache;
	String regionName;
	String clearIndexKey;
	
	int timespan = 0;

	public Cache(String regionName,
			com.solace.caching.ICache cache) {

		try {
			if ( cache == null )
				throw new RuntimeException("cache paramter may not be null");
			
			this.cache = cache;
			this.regionName = (regionName != null) ? regionName : "default";
			this.cache.setRegionName(((regionName != null) ? regionName
					: "default"));
			this.clearIndexKey = this.regionName.replaceAll("\\s","") + ":index_key";
			
			if ( cache instanceof com.solace.caching.DistributedCache )
				timespan = ((com.solace.caching.DistributedCache)cache).getTimeout();
			
		} catch (Exception e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.solace.caching.DistributedCache#clear()
	 */
	public void clear() throws org.hibernate.cache.CacheException {
		try {
			this.cache.clear();
		} catch (Exception e) {
			throw new org.hibernate.cache.CacheException(e);
		}
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.solace.caching.Cache#getParameters()
//	 */
//	public Map<String, String> getParameters() {
//		// TODO Auto-generated method stub
//		return this.cache.getParameters();
//	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.solace.caching.Cache#getRegionName()
//	 */
//	@Override
//	public String getRegionName() {
//		// TODO Auto-generated method stub
//		return super.getRegionName();
//	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.solace.caching.Cache#setRegionName(java.lang.String)
//	 */
//	@Override
//	public void setRegionName(String name) {
//		// TODO Auto-generated method stub
//		super.setRegionName(name);
//	}

	static final Logger LOGGER = Logger.getLogger(Cache.class);

//	public DistributedCache(CacheConfig config) throws ArgumentException,
//			com.solace.caching.CacheException {
//		super(config);
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public void destroy() throws org.hibernate.cache.CacheException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object get(Object key) throws org.hibernate.cache.CacheException {
//		String k;
		try {
//			k = keyAsString(key);

			LOGGER.debug("Getting from region: {} key: {}.",
					getRegionName(), key.toString());

			return cache.get(key);
//			return super.get(k);
		} catch (com.solace.caching.CacheException e) {
			throw new org.hibernate.cache.CacheException("Could not get", e);
		}
	}

	@Override
	public long getElementCountInMemory() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public long getElementCountOnDisk() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public long getSizeInMemory() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public int getTimeout() {
		return this.timespan;
	}

	@Override
	public void lock(Object arg0) throws org.hibernate.cache.CacheException {
		// TODO Auto-generated method stub

	}

	@Override
	public long nextTimestamp() {
		return Timestamper.next();
	}

	@Override
	public void put(Object key, Object value) throws org.hibernate.cache.CacheException {

//		String k;
		try {
//			k = keyAsString(key);
//
//			LOGGER.debugFormat("Putting in region: {} key: {}.",
//					getRegionName(), super.keyAsString(key));

			this.cache.set(key, value);
		} catch (com.solace.caching.CacheException e) {
			throw new org.hibernate.cache.CacheException("Could not put", e);
		}

	}

	@Override
	public Object read(Object arg0) throws org.hibernate.cache.CacheException {
		return get(arg0);
	}

	@Override
	public void remove(Object key) throws org.hibernate.cache.CacheException {

//		String k;
		try {
//			k = keyAsString(key);
//
			LOGGER.debug("Removing from region: {} key: {}.",
					getRegionName(), key.toString());

			this.cache.delete(key);
		} catch (com.solace.caching.CacheException e) {
			throw new org.hibernate.cache.CacheException("Could not delete", e);
		}
	}

	@Override
	public Map toMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unlock(Object arg0) throws org.hibernate.cache.CacheException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Object key, Object value) throws org.hibernate.cache.CacheException {
		put(key, value);
	}

	@Override
	public String getRegionName() {
		// TODO Auto-generated method stub
		return this.regionName;
	}

//	@Override
//	protected String fullKeyAsString(Object key) {
//		return String.format("{}", (key == null ? "" : key.toString()));
//		// return String.format("{}@{}", getRegionName(),
//		// (key==null?"":key.toString()));
//	}
}
