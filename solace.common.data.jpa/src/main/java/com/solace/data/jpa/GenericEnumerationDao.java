/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, GenericEnumerationDao.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.jpa;

import com.solace.data.*;

/**
 * GenericEnumerationDao is specific to Jpa persistence leveraging a named query
 * and assumes that the mapped entity's key'ing structure is getValue.
 * <p>
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 * 
 * @param <ID>
 *            the enumerating ID type
 * @param <T>
 *            the entity that is enumerated
 */
public class GenericEnumerationDao<ID extends Number, T extends EnumerationEntity<ID>>
		extends GenericDao<ID, T> {

	/**
	 * Still needs to be built up
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T findByValue(String value) throws Exception {
		T t = null;
		try {
			t = (T) createQuery(
					"SELECT e FROM " + m_persistentClass.getName()
							+ " e where e.value = ?", value).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			t = null;
		} catch ( Exception e ) {
			throw e;
		}
		
		return t;
	}

	/**
	 * Called chain
	 * 
	 * @param value
	 * @return
	 */
	public T findByValue(T value) throws Exception {
		return findByValue(value.getValue());
	}
}
