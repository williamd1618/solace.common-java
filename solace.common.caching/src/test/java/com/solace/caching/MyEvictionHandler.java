/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, MyEvictionHandler.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import com.solace.caching.CacheException;
import com.solace.caching.EvictionCache;

public class MyEvictionHandler implements EvictionCache.IEvictionHandler
{
	public MyEvictionHandler() {}

	@Override
	public void evict(EvictionCache cache, String key, Object o) throws CacheException {
		cache.set("foo", "bar");
	}	
}
