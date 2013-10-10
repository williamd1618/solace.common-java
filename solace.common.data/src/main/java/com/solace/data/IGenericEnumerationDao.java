/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, IGenericEnumerationDao.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data;

/**
 * IGenericEnumerationDao is the base functionality for how enumeration entities
 * are accesses
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 *
 * @param <ID> the number type
 * @param <T> the EnumerationEntity type
 */
public interface IGenericEnumerationDao<ID extends Number, T extends EnumerationEntity<ID>>
		extends IGenericDao<ID, T> {

	public T findByValue(String value) throws Exception ;

	public T findByValue(T value) throws Exception ;
}
