/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, ICache.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import java.util.*;

/**
 * ICache represents the definition that all cache implementations should meet.  
 * <p>
 * It will also serve as the decorator {@linkplain http://en.wikipedia.org/wiki/Decorator_pattern} 
 * for {@link CacheAccessor}
 * @author dwilliams
 *
 */
public interface ICache {

	public boolean set(ICacheable _obj) throws CacheException;
	public boolean set(String _key, Object _obj) throws CacheException;
	public boolean set(Object _key, Object _obj) throws CacheException;
	
	public Object get(ICacheable _key) throws CacheException;
	public Object get(String _key) throws CacheException;
	public Object get(Object _key) throws CacheException;
	
	public boolean delete(ICacheable _key) throws CacheException;
	public boolean delete(String _key) throws CacheException;
	public boolean delete(Object _key) throws CacheException;
	
	public void incr(String key) throws CacheException;
	public void incr(String key, long delta) throws CacheException;
	public void decr(String key) throws CacheException;
	public void decr(String key, long delta) throws CacheException;
	
	public String getRegionName() throws CacheException;
	public void setRegionName(String _name) throws CacheException;
	
	public Map<String, String> getParameters();
	
	public void clear() throws CacheException;
	
	public void shutdown() throws CacheException;
}
