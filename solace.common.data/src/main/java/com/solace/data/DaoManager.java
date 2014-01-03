package com.solace.data;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.solace.logging.Logger;
import com.solace.data.IGenericDao;

public class DaoManager implements IDaoManager {

	private static final Logger LOGGER = Logger.getLogger(DaoManager.class);

	private Map<Class<?>, IGenericDao<?, ?>> daos;

	public DaoManager() {
	}

	public DaoManager(final Map<Class<?>, IGenericDao<?, ?>> daos) {
		this.daos = daos;

		LOGGER.debug("In constructor");

		for (Entry<Class<?>, IGenericDao<?, ?>> e : daos.entrySet()) {
			LOGGER.info("Registered a {} to {}", e.getKey().getName(), e
					.getValue().getClass().getName());
		}
	}
	public void setDaos(Map<Class<?>, IGenericDao<?, ?>> daos) {

		this.daos = daos;

		for (Entry<Class<?>, IGenericDao<?, ?>> e : daos.entrySet()) {
			LOGGER.info("Registered a {} to {}", e.getKey().getName(), e
					.getValue().getClass().getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobileaccord.messaging.data.IDaoManager#get(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <ID, T> T get(Class<?> _class) {
		LOGGER.info("Attempting to get a dao for {}.", _class.getName());
		Class<T> key = resolveInterface(_class);
		LOGGER.info("Getting dao for resolved key of {}", key.getName());
		return (T) daos.get(this.<ID, T> resolveInterface(_class));
	}

	private <ID, T> Class<T> resolveInterface(Class<?> _class) {

		if (_class.isInterface())
			return (Class<T>) _class;
		else
			return (Class<T>) Iterables.getLast(Lists.asList(null,
					_class.getInterfaces()));
	}
}
