/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, DistributedCache.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import java.io.*;
import java.security.*;
import java.util.*;

import redis.clients.jedis.*;

import com.solace.*;
import com.solace.support.ObjectMapperUtils;

/**
 * Is a pluggable implementation that wraps the Memcached implementation.
 * <p>
 * Keys cannot be larger than 250 and are hashed with the sha-1 algorithm and
 * then converted to hex to make sure they are are alphanumeric and don't step
 * into E-ASCII.
 * <p>
 * The MemCachedClient instance is NOT static and exposes configuration
 * parameters in {@link Caches.CacheConfig.Type.Properties}. Because it is not
 * static depending on the {@link CacheAccessor} that is used the SockIOPool may
 * be large. The current DistributedCache implementation should only be used
 * with the ApplicationAccessor which will create a single socket pool for the
 * application and remove the need to synchronize across resources within the
 * memcached client implementation.
 * 
 * @see {@link RedisCache#SERVER_COUNT}
 * @see {@link RedisCache#SERVER_HOST}
 * @see {@link RedisCache#SERVER_PORT}
 * @see {@link RedisCache#SERVER_WEIGHT}
 * @see {@link RedisCache#DEFAULT_SERVER_WEIGHT}
 * @see {@link RedisCache#SOCKET_POOL_MINSIZE}
 * @see {@link RedisCache#SOCKET_POOL_MAXSIZE}
 * @see {@link RedisCache#CONNECTION_TIMEOUT}
 * @see {@link RedisCache#CACHE_TIMESPAN}
 * @see {@link RedisCache#SOCKET_POOL_INIT_CONNS}
 * @see {@link RedisCache#SOCKET_POOL_MAINT_THREAD_SLEEP}
 * @see {@link RedisCache#SOCKET_POOL_SOCKET_CONNECT_TO}
 * @see {@link RedisCache#SOCKET_POOL_SOCKET_TO}
 * @see {@link RedisCache#COMPRESS}
 * @see {@link RedisCache#COMPRESSION_THRESHOLD}
 * 
 * 
 * @author dwilliams
 * 
 */
public class RedisCache extends Cache {

	private static final String COULD_NOT_SET = "Could not set";

	private static final String NOT_FOUND = "not found";

	private static final String FOUND = "found";

	private static final String KEY_S_S = "Key: {}: {}";

	private static final String GETTING_S = "Getting {}.";

	private static final String DELETING_S = "Deleting {}.";

	private static final String COULD_NOT_DELETE = "Could not delete";

	private static final String S_WAS_NOT_SUCCESSFULLY_DELETED = "[%s] was NOT successfully deleted.";

	private static final String S_WAS_SUCCESSFULLY_DELETED = "[%d] was successfully deleted.";

	private static final String S_WAS_NOT_SUCCESSFULLY_SET = "[%d] was NOT successfully set.";

	private static final String S_WAS_SUCCESSFULLY_SET = "[%d] was successfully set.";

	private static final String STORING_S_S_WITH_EXPIRY_S = "Storing {} = {} with expiry {}.";

	private static final String STORING_S_S = "Storing {} = {}.";

	private static final String S_MUST_BE_AN_INTEGER_1 = "{} must be an integer > 1";

	static com.solace.logging.Logger LOGGER = com.solace.logging.Logger
			.getLogger(RedisCache.class);

	// avoid recurring construction
	private static ThreadLocal<MessageDigest> MD5 = new ThreadLocal<MessageDigest>() {
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error("No MD5 algorithm found");
				throw new IllegalStateException("No MD5 algorithm found");
			}
		}
	};

	private int m_minPoolSize = 5;
	private int m_maxPoolSize = 10;
	private int m_timeout = 3;
	private boolean m_hasTimespan = false;
	private int m_timespan = 0;

	public int getTimeout() {
		return m_timespan;
	}

	/**
	 * how many servers in config
	 */
	public static final String SERVER_COUNT = "Server.Count";

	/**
	 * host of server
	 */
	public static final String SERVER_HOST = "Server[%d].Host";

	/**
	 * port of server
	 */
	public static final String SERVER_PORT = "Server[%d].Port";

	/**
	 * weight in relation to other servers, default is 5
	 */
	public static final String SERVER_WEIGHT = "Server[%d].Weight";

	/**
	 * default weight
	 */
	public static final Integer DEFAULT_SERVER_WEIGHT = new Integer(5);

	/**
	 * min size of socket pool
	 */
	public static final String SOCKET_POOL_MINSIZE = "SocketPool.MinSize";

	/**
	 * max size of socket pool
	 */
	public static final String SOCKET_POOL_MAXSIZE = "SocketPool.MaxSize";

	/**
	 * the number of initial connections to factory
	 */
	public static final String SOCKET_POOL_INIT_CONNS = "SocketPool.InitialConnections";

	/**
	 * in seconds (defaults to 3s)
	 */
	public static final String SOCKET_POOL_SOCKET_CONNECT_TO = "SocketPool.SocketConnectTimeout";

	/**
	 * In milliseconds the amount of time for the client to block on reads
	 * (defaults to 3s)
	 */
	public static final String SOCKET_POOL_SOCKET_TO = "SocketPool.SocketTimeout";

	/**
	 * connection timeout in minutes
	 */
	public static final String CONNECTION_TIMEOUT = "ConnectionTimeout";

	/**
	 * how long should the entity exist in seconds.
	 */
	public static final String CACHE_TIMESPAN = "CacheTimespan";

	/**
	 * In bytes at what point the cache client should start compression the data
	 * streams (defaults to 4kb).
	 */
	public static final String COMPRESSION_THRESHOLD = "CompressionThreshold";

	/**
	 * true or false to enable compressions (default is true)
	 */
	public static final String COMPRESS = "Compress";

	/**
	 * The maintenace thread in ms. By default it is 5 seconds
	 */
	public static final String SOCKET_POOL_MAINT_THREAD_SLEEP = "SocketPool.MaintenanceThreadSleep";

	int initialConnections = 10;
	long maxIdleTime = 1000 * 60 * 30; // 30 minutes
	long maxBusyTime = 1000 * 60 * 5; // 5 minutes
	long maintThreadSleep = 1000 * 5; // 5 seconds
	int socketTimeOut = 1000 * 3; // 3 seconds to block on reads
	int socketConnectTO = 1000 * 3; // 3 seconds to block on initial
	boolean nagleAlg = false; // turn off Nagle's algorithm on all
	boolean aliveCheck = false; // disable health check of socket on
	int compressionThreshold = 1024 * 4;
	boolean compress = true;

	ShardedJedis jedis = null;

	/**
	 * Constructor fired by CacheConfig.loadImplementation
	 * 
	 * @param _config
	 * @throws CacheException
	 */
	public RedisCache(Caches.CacheConfig _config) throws ArgumentException,
			CacheException {
		super(_config);

		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();

		String tmp = null;

		int serverCount = 0;

		if ((tmp = getParameters().get(SERVER_COUNT)) != null)
			serverCount = Integer.parseInt(tmp);
		else
			throw new ArgumentException(
					"Server inforemation must be defined for DistributedCache");

		String[] servers = new String[serverCount];
		int[] weights = new int[serverCount];

		for (int i = 0; i < serverCount; i++) {
			servers[i] = loadServer(i);
			weights[i] = loadWeight(i);
		}

		// if ((tmp = getParameters().get(SOCKET_POOL_MINSIZE)) != null
		// && tmp.trim() != "") {
		// this.m_minPoolSize = Integer.parseInt(tmp);
		//
		// if (m_minPoolSize < 1)
		// throw new ArgumentException(String.format(
		// S_MUST_BE_AN_INTEGER_1, SOCKET_POOL_MAXSIZE));
		// }

		// if ((tmp = getParameters().get(SOCKET_POOL_MAXSIZE)) != null
		// && tmp.trim() != "") {
		// this.m_maxPoolSize = Integer.parseInt(tmp);
		//
		// if (m_maxPoolSize < 1)
		// throw new ArgumentException(String.format(
		// S_MUST_BE_AN_INTEGER_1, SOCKET_POOL_MAXSIZE));
		// }

		if ((tmp = getParameters().get(CONNECTION_TIMEOUT)) != null
				&& tmp.trim() != "") {
			this.m_timeout = Integer.parseInt(tmp);

			if (m_timeout < 1)
				throw new ArgumentException(String.format(
						S_MUST_BE_AN_INTEGER_1, CONNECTION_TIMEOUT));
		}

		// if ((tmp = getParameters().get(SOCKET_POOL_INIT_CONNS)) != null
		// && tmp.trim() != "") {
		// initialConnections = Integer.parseInt(tmp);
		// }

		// if ((tmp = getParameters().get(SOCKET_POOL_SOCKET_CONNECT_TO)) !=
		// null
		// && tmp.trim() != "") {
		// socketConnectTO = Integer.parseInt(tmp);
		// }

		// if ((tmp = getParameters().get(SOCKET_POOL_SOCKET_TO)) != null
		// && tmp.trim() != "") {
		// socketTimeOut = Integer.parseInt(tmp);
		// }
		//
		// if ((tmp = getParameters().get(SOCKET_POOL_MAINT_THREAD_SLEEP)) !=
		// null
		// && tmp.trim() != "") {
		// maintThreadSleep = Integer.parseInt(tmp);
		// }

		// if ((tmp = getParameters().get(COMPRESS)) != null && tmp.trim() !=
		// "") {
		// compress = Boolean.parseBoolean(tmp);
		//
		// if (compress) {
		// if ((tmp = getParameters().get(COMPRESSION_THRESHOLD)) != null
		// && tmp.trim() != "") {
		// compressionThreshold = Integer.parseInt(tmp);
		// }
		// }
		// }

		// if (m_minPoolSize > m_maxPoolSize)
		// throw new ArgumentException(
		// "SocketPool.MinSize cannot be greater than SocketPool.MaxSize.");

		StringBuffer sb = new StringBuffer();
		for (String str : servers)
			sb.append(str).append(";");

		LOGGER.info(
				"Initializing a Redis client with servers: {}\n, min socket pool: {}\n, max socket pool: {}\n, timeout: {} mins\n, cache timespan: {} mins.",
				sb.toString(), m_minPoolSize, m_maxPoolSize, m_timeout,
				this.m_timespan);

		for (String s : servers)
			shards.add(new JedisShardInfo(s));

		jedis = new ShardedJedis(shards);

		// try {
		// jedis.set
		// m_client = builder.build();
		// m_client.setConnectTimeout(socketConnectTO);
		// } catch (Exception e) {
		// throw new CacheException(e.getMessage(), e);
		// }

		if ((tmp = getParameters().get(CACHE_TIMESPAN)) != null
				&& tmp.trim() != "") {
			this.m_timespan = Integer.parseInt(tmp);

			if (m_timespan <= 0) {
				throw new ArgumentException(String.format(
						"{} must be a positive integer", CACHE_TIMESPAN));
			} else {
				m_hasTimespan = true;
			}
		}
	}

	/**
	 * Force a finalize for cleanup
	 */
	@Override
	protected void finalize() throws Throwable {
		try {
			shutdown();
		} finally {
			super.finalize();
		}
	}

	@Override
	public void shutdown() throws CacheException {
		try {
			jedis.disconnect();
		} catch (Exception e) {
			throw new CacheException(e.getMessage(), e);
		}
	}

	/**
	 * Loads the server configuration
	 * 
	 * @param i
	 * @return
	 * @throws ArgumentException
	 *             thrown if an argument is impropertly formed or does not exist
	 */
	private String loadServer(int i) throws ArgumentException {
		String tmp = null;

		StringBuffer sb = new StringBuffer("redis://");

		int port;

		if ((tmp = getParameters().get(String.format(SERVER_HOST, i))) == null
				|| tmp == "")
			throw new ArgumentException(String.format(SERVER_HOST
					+ "property is required for RedisCache", i));

		sb.append(tmp);

		if ((tmp = getParameters().get(String.format(SERVER_PORT, i))) == null
				|| tmp == "") {
			port = Protocol.DEFAULT_PORT;
		} else {
			port = Integer.parseInt(tmp);
		}

		sb.append(":").append(port);

		return sb.toString();
	}

	private int loadWeight(int i) {
		String tmp = null;

		// building up our server weights if provided
		// defaults to 5
		if ((tmp = getParameters().get(String.format(SERVER_WEIGHT, i))) != null
				&& tmp != "")
			return Integer.parseInt(tmp);
		else
			return DEFAULT_SERVER_WEIGHT.intValue();
	}

	/**
	 * Will add a number of seconds to the current datetime to signal the
	 * memcached server when to expire content
	 * 
	 * @return
	 */
	private Date getExpiry() {
		Calendar c = Calendar.getInstance();

		c.add(Calendar.SECOND, m_timespan);

		return c.getTime();
	}

	/**
	 * @see RedisCache#set(String, Object)
	 */
	@Override
	public boolean set(ICacheable _item) throws CacheException {
		return set(_item.getCacheKey(), _item);
	}

	/**
	 * @see RedisCache#set(String, Object)
	 */
	public boolean set(Object key, Object value) throws CacheException {
		return set(keyAsString(key), value);
	}

	/**
	 * Will attempt to set the _item into the memcached instanced that it hashes
	 * to Will throw a CacheExcedption of the set operation throws up an
	 * exception, typically a SocketTimeoutException
	 * 
	 * @see com.solace.caching.Cache#set(java.lang.String, java.lang.Object)
	 */
	@Override
	public boolean set(String _key, Object _item) throws CacheException {
		boolean set = false;
		try {
			String val = ObjectMapperUtils.getObjectMapper()
					.writeValueAsString(_item);

			if (!m_hasTimespan) {
				LOGGER.debug(STORING_S_S, _key, val);

				set = jedis.set(keyAsString(_key), val) == _key;
			} else {
				Date d = getExpiry();

				LOGGER.debug(STORING_S_S_WITH_EXPIRY_S, _key, _item.toString(),
						d);

				// assume this has to be a differene against now
				// otherwise why would it be an int.
				set = jedis.setex(keyAsString(_key),
						(int) (d.getTime() - Calendar.getInstance()
								.getTimeInMillis()), val) == _key;
			}
		} catch (Exception e) {
			throw new CacheException(COULD_NOT_SET, e, _key, _item);
		}

		if (set)
			LOGGER.debug(S_WAS_SUCCESSFULLY_SET, _key);
		else
			LOGGER.debug(S_WAS_NOT_SUCCESSFULLY_SET, _key);

		return set;
	}

	/**
	 * @see RedisCache#get(String)
	 */
	@Override
	public Object get(ICacheable _item) throws CacheException {
		return get(_item.getCacheKey());
	}

	/**
	 * @see RedisCache#get(String)
	 */
	public Object get(Object o) throws CacheException {
		return get(keyAsString(o));
	}

	/**
	 * gets an object defined by _key
	 * 
	 * @param _key
	 *            the defining token of the item to be returned
	 * @throws CacheException
	 *             if the caching client throws an exception we will wrap it and
	 *             throw it up
	 * 
	 */
	@Override
	public Object get(String _key) throws CacheException {
		LOGGER.debug(GETTING_S, _key);

		Object obj = null;

		try {
			obj = jedis.get(_key);
		} catch (Exception e) {
			throw new CacheException("Could not get", e, _key);
		}

		LOGGER.debug(KEY_S_S, _key, (obj != null) ? FOUND : NOT_FOUND);

		return obj;
	}

	/**
	 * @see RedisCache#delete(String)
	 */
	@Override
	public boolean delete(ICacheable _item) throws CacheException {
		return delete(_item.getCacheKey());
	}

	/**
	 * @see RedisCache#delete(String)
	 */
	public boolean delete(Object key) throws CacheException {
		return delete(keyAsString(key));
	}

	/**
	 * Will delete an instance from the cache
	 * 
	 * @param _key
	 *            identifies the entity
	 * @throws CacheException
	 *             if the client throws up an exception
	 */
	@Override
	public boolean delete(String _key) throws CacheException {
		LOGGER.debug(DELETING_S, _key);

		boolean retVal = true;
		try {
			retVal = jedis.del(_key) != null;
		} catch (Exception e) {
			throw new CacheException(COULD_NOT_DELETE, e, _key);
		}

		if (retVal)
			LOGGER.debug(S_WAS_SUCCESSFULLY_DELETED, _key);
		else
			LOGGER.debug(S_WAS_NOT_SUCCESSFULLY_DELETED, _key);

		return retVal;
	}

	@Override
	public void clear() throws CacheException {		
		throw new CacheException(new UnsupportedOperationException("clear not supported by Redis."));
	}

	protected String keyAsString(Object key) throws CacheException {
		String fullKey = fullKeyAsString(key);

		if (fullKey.length() >= 250)
			return computeHash(fullKey);
		else
			return fullKey.replace(' ', '-');
	}

	protected String fullKeyAsString(Object key) {
		return String.format("{}", (key == null ? "" : key.toString()));
	}

	/**
	 * Want to convert this to Hex so that we don't end up with anyone odd
	 * characters that could cause issues in memcached
	 * 
	 * @param data
	 *            bytes to be converted
	 * @return
	 */
	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	/**
	 * Hash the key using the SHA-1 hash algorithm
	 * 
	 * @param _key
	 *            key value
	 * @return hashed key
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	private static String computeHash(String _key) throws CacheException {
		MessageDigest md5 = MD5.get();
		md5.reset();
		md5.update(_key.getBytes());
		byte[] data = md5.digest();

		return convertToHex(data);
	}

}
