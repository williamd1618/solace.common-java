/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, CacheAccessor.java, and the accompanying materials
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
import com.solace.logging.*;
import com.solace.utility.ReflectionUtil;

import java.util.*;
import java.util.concurrent.*;

import javax.xml.bind.*;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;

/**
 * An abstract decorator class for {@link ICache}. Meant to manage how caches
 * are loaded and maintained in the system.
 * <p>
 * {@code ICache cache =
 * ThreadLocalAccessor.getInstance("cache definition name");
 * cache.set("foo","bar"); cache.get("bar"); * }
 * 
 * @author dwilliams
 * @see ThreadLocalAccessor
 * @see CACHE_CONFIG_FILE
 * @see CACHE_CONFIG_BEAN_KEY
 */
public abstract class CacheAccessor implements ICache {
	private static Logger LOGGER = Logger.getLogger(CacheAccessor.class);

	protected static Map<String, Caches.CacheConfig> m_configs = new ConcurrentHashMap<String, Caches.CacheConfig>();

	/**
	 * The key that should be in {@link CACHE_CONFIG_FILE}
	 */
	public static final String CACHE_CONFIG_BEAN_KEY = "caches";

	/**
	 * The spring configuration file that we look to load
	 * {@link CACHE_CONFIG_BEAN_KEY} from
	 */
	public static final String CACHE_CONFIG_FILE = "caches.xml";

	/**
	 * Static loading space for configuration options from the configuration
	 * file
	 */
	static {
		synchronized (m_configs) {
			try {
				String[] configFiles = new String[] { CACHE_CONFIG_FILE };
				BeanFactory factory = new ClassPathXmlApplicationContext(
						configFiles);

				Caches c = (Caches) factory.getBean(CACHE_CONFIG_BEAN_KEY);

				if (c != null) {
					if (c.getCache() != null && c.getCache().size() > 0) {
						for (Caches.CacheConfig config : c.getCache()) {
							addConfiguration(config);
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			// try {
			// String file = ClassUtils
			// .getParentDirectory(CacheAccessor.class)
			// .getAbsolutePath()
			// + File.separator + File.separator + "caches.xml";
			//
			// File configFile = new File(file);
			//
			// // if we have a configFile that exists
			// // let's use it.
			// if (configFile.exists()) {
			// // load the jaxb context
			// JAXBContext jc = JAXBContext
			// .newInstance(com.solace.caching.Caches.class);
			//
			// Unmarshaller u = jc.createUnmarshaller();
			//
			// Caches c = (Caches) u.unmarshal(new FileInputStream(
			// configFile));
			//
			// // iterate across the configurations
			// if (c != null) {
			// if (c.getCache() != null && c.getCache().size() > 0) {
			// for (Caches.CacheConfig config : c.getCache()) {
			// addConfiguration(config);
			// }
			// }
			// }
			// } else {
			// LOGGER.info("caches.xml did not exist ....");
			// }
			// } catch (Exception e) {
			// LOGGER
			// .errorFormat(
			// "An unhandled exception in static load of CacheAccessor: {}",
			// e, e.getMessage());
			// }
		}

	}

	/**
	 * Will add a configuration to the static map of configs.
	 * 
	 * @param _config
	 *            configuration to be added
	 */
	public static void addConfiguration(Caches.CacheConfig _config) {
		synchronized (m_configs) {
			if (m_configs.containsKey(_config.getName()))
				LOGGER
						.info(
								"A previous CacheConfig exists with Name: [{}] ... replacing",
								_config.getName());
			else
				LOGGER.info("Adding a CacheConfig with Name: [{}]",
						_config.getName());

			m_configs.put(_config.getName(), _config);
		}
	}

	public static void clearConfigurations() {
		synchronized (m_configs) {
			m_configs.clear();
		}
	}

	/**
	 * Will dynamically load the cache implementation into memory using the
	 * loosely coupled {@link Caches.CaceConfig} class.
	 * 
	 * @param _name
	 *            the name of the {@link Caches.CachConfig} to load
	 * @return
	 * @throws ConfigurationException
	 *             thrown if the config name is not found
	 * @throws ArgumentException
	 *             thrown if the ReflectionUtil cannot initialize the
	 *             configuration {@link Caches.CacheConfig.Type}
	 * @throws CacheException
	 */
	protected static ICache loadImplementation(String _name)
			throws ConfigurationException, ArgumentException, CacheException {

		ICache cache = null;

		synchronized (m_configs) {

			Caches.CacheConfig config = null;

			if ((config = m_configs.get(_name)) == null) {
				throw new ConfigurationException(String.format(
						"CacheConfig with the name {} does not exist", _name));
			}

			try {
				cache = ReflectionUtil.<ICache> createInstance(config.getType()
						.getValue(), config);
			} catch (Exception e) {
				throw new ArgumentException(String.format("Trouble loading {}",
						config.getType().getValue()), e);
			}

			cache.setRegionName(config.getName());
		}

		return cache;
	}

	public CacheAccessor() {
	}

	protected ICache m_cache;

	public void setCache(ICache _cache) {
		m_cache = _cache;
	}

	public ICache getCache() {
		return m_cache;
	}

	@Override
	public boolean set(ICacheable _item) throws CacheException {
		return set(_item.getCacheKey(), _item);
	}

	@Override
	public boolean set(String _key, Object _item) throws CacheException {
		return m_cache.set(_key, _item);
	}

	@Override
	public Object get(ICacheable _item) throws CacheException {
		return m_cache.get(_item);
	}

	@Override
	public Object get(String _key) throws CacheException {
		return m_cache.get(_key);
	}

	@Override
	public boolean delete(ICacheable _item) throws CacheException {
		return m_cache.delete(_item);
	}

	@Override
	public boolean delete(String _key) throws CacheException {
		return m_cache.delete(_key);
	}

	@Override
	public void clear() throws CacheException {
		m_cache.clear();
	}

	@Override
	public String getRegionName() throws CacheException {
		// TODO Auto-generated method stub
		return m_cache.getRegionName();
	}

	@Override
	public void setRegionName(String name) throws CacheException {
		// TODO Auto-generated method stub
		m_cache.setRegionName(name);
	}

	/* (non-Javadoc)
	 * @see com.solace.caching.ICache#delete(java.lang.Object)
	 */
	@Override
	public boolean delete(Object key) throws CacheException {
		// TODO Auto-generated method stub
		return m_cache.delete(key);
	}

	/* (non-Javadoc)
	 * @see com.solace.caching.ICache#get(java.lang.Object)
	 */
	@Override
	public Object get(Object key) throws CacheException {
		// TODO Auto-generated method stub
		return m_cache.get(key);
	}

	/* (non-Javadoc)
	 * @see com.solace.caching.ICache#set(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean set(Object key, Object obj) throws CacheException {
		// TODO Auto-generated method stub
		return m_cache.set(key, obj);
	}
	
	

	@Override
	public void incr(String key) throws CacheException {
		incr(key);
	}

	@Override
	public void incr(String key, long delta) throws CacheException {
		m_cache.incr(key, delta);
	}

	@Override
	public void decr(String key) throws CacheException {
		decr(key);
	}

	@Override
	public void decr(String key, long delta) throws CacheException {
		m_cache.decr(key, delta);
	}

	/* (non-Javadoc)
	 * @see com.solace.caching.ICache#shutdown()
	 */
	@Override
	public void shutdown() throws com.solace.caching.CacheException {
		m_cache.shutdown();		
	}

	/* (non-Javadoc)
	 * @see com.solace.caching.ICache#getParameters()
	 */
	@Override
	public Map<String, String> getParameters() {
		return m_cache.getParameters();
	}

}
