package com.solace.data.cassandra;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;


/**
 * A generic client for cassandra cluster connectivity.  
 * 
 * @author <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>
 *
 */
class Client {

	public static final Map<String[], Client> instances = new ConcurrentHashMap<String[], Client>();
	
	private Cluster cluster;
	
	private Session session;

	protected Client(String... addresses) {		
		cluster = Cluster.builder().addContactPoints(addresses).build();
		session = cluster.connect();
	}
	
	public Session getSession() {
		if (session == null)
			session = cluster.connect();
		
		return session;
	}

	public static Client getInstance(String... addresses) {
		Client client = null;
		
		if ((client = instances.get(addresses)) == null) {
			client = new Client(addresses);
			instances.put(addresses, client);
		}
		
		return client;
	}

}
