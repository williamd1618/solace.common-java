/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, GenericDao.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.jpa;

import java.util.Calendar;
import java.util.List;
import java.lang.reflect.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import com.solace.ExceptionHandler;
import com.solace.data.*;
import com.solace.logging.*;

/**
 * The GenericJpaDao provides the genericized functionality that you would
 * expect to find in a functional DAO implementation, abstracted. No abstract
 * factory is needed as if all implementation adhere to the concept of Entity
 * they can be injected via the Spring
 * 
 * Lazy initialization does NOT work outside of a transaction b/c no
 * EntityManager exists ergo no Session exists
 * 
 * @Repository and @Autowired annotations.
 * @author dwilliams
 * 
 * @param <ID>
 *            the identifying type of the dao
 * @param <E>
 *            the entity to per accessed
 * @see Repistory
 * @see Autowired
 */
@SuppressWarnings("unchecked")
public abstract class GenericDao<ID extends Number, E extends IEntity<ID>>
		implements IGenericDao<ID, E> {

	private static final String S_TOOK_D_MS_TO_EXECUTE = "{} took {} ms to execute";

	private static final String S_TOOK_D_MS_TO_EXECUTE_AND_RETURNED_D_ROWS = "{} took {} ms to execute and returned {} rows";

	private static final String MERGING_S = "merging ... {}";

	private static final String PERSISTING_S = "persisting ... {}";

	static final Logger LOGGER = Logger.getLogger(GenericDao.class);

	protected Class<E> m_persistentClass;

	protected EntityManager m_entityManager;

	public GenericDao() {
		try {
			ParameterizedType genericSuperclass = (ParameterizedType) getClass()
					.getGenericSuperclass();
			this.m_persistentClass = (Class<E>) genericSuperclass
					.getActualTypeArguments()[1];
		} catch (Exception e) {
		}
	}

	public EntityManager getEntityManager() {
		return this.m_entityManager;
	}

	@javax.persistence.PersistenceContext
	public void setEntityManager(EntityManager manager) {
		this.m_entityManager = manager;
	}

	@Override
	public void delete(E entity) {
		m_entityManager.remove(entity);
	}

	@Override
	public List<E> findAll() throws Exception {
		// query assumeds that the CreateDate attribute is always present
		Query q = m_entityManager.createQuery("SELECT e FROM "
				+ m_persistentClass.getName() + " e order by e.createDate");
		return (List<E>) q.getResultList();

	}

	@Override
	public E findById(ID id) throws Exception {
		return m_entityManager.find(m_persistentClass, id);
	}

	@Override
	public void save(E entity)  {
		if (entity.getId() == null) {
			LOGGER.debug(PERSISTING_S, entity.getClass().getName());
			m_entityManager.persist(entity);			
		} else {
			LOGGER.debug(MERGING_S, entity);
			entity = m_entityManager.merge(entity);
		}
	}

	/**
	 * Will take in a string query and execute it
	 * 
	 * @param query
	 *            the query to execute
	 * @return a instance of E
	 * @author dwilliams
	 * @note possibly create a permutation of this method which takes in a set
	 *       of objects and will infer the type to be added to the query
	 */
	@Override
	public E queryObject(String query, Object... args) throws Exception {
		
		E entity = null;
		
		try {
			entity = executeSingle(createQuery(query, args));
		} catch (javax.persistence.NoResultException e) {
			entity = null;
		}
		
		return entity;
	}

	/**
	 * Will take in a string query and execute it
	 * 
	 * @param query								
	 *            the query to execute
	 * @return a list of E
	 * @author dwilliams
	 * @note possibly create a permutation of this method which takes in a set
	 *       of objects and will infer the type to be added to the query
	 */
	@Override
	public List<E> queryObjects(String query, Object... args) throws Exception {
		
		List<E> entities = null;
		
		try {
			entities = executeList(createQuery(query, args));
		} catch (javax.persistence.NoResultException e) {
			entities = null;
		} 
		
		return entities;
	}
	
	/**
	 * Allows a developer to pass in a very simple DML operation
	 * quickly.	
	 */
	@Override
	public int executeUpdate(String query, Object... args) throws Exception {
		return createQuery(query, args).executeUpdate();
	}

	Query createQuery(String query, Object... args) throws Exception {
		Query q = m_entityManager.createQuery(query);

		if (args != null)
			for (int i = 0; i < args.length; i++)
				q.setParameter(i+1, args[i]);

		return q;
	}	

	/**
	 * A simple clocking method
	 * 
	 * @param q
	 *            the query to execute
	 * @return
	 */
	private List<E> executeList(javax.persistence.Query q) throws Exception  {

		long now = System.currentTimeMillis();

		List<E> list = (List<E>) q.getResultList();

		LOGGER.debug(S_TOOK_D_MS_TO_EXECUTE_AND_RETURNED_D_ROWS, q
				.toString(), System.currentTimeMillis() - now, list.size());

		return list;
	}

	private E executeSingle(javax.persistence.Query q) throws Exception {

		long now = System.currentTimeMillis();

		E e = (E) q.getSingleResult();

		LOGGER.debug(S_TOOK_D_MS_TO_EXECUTE, q.toString(), System
				.currentTimeMillis()
				- now);

		return e;
	}
}
