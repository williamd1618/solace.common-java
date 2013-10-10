/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, IGenericDao.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data;

import java.util.*;

public interface IGenericDao<ID, E extends IEntity<ID>> {

    public E findById(ID id) throws Exception ;

    public void save(E entity);
    
    public void delete(E entity);
    
    public List<E> findAll() throws Exception ; 
    
    public List<E> queryObjects(String query, Object... args) throws Exception ;
    
    public E queryObject(String query, Object... args) throws Exception ;
    
    public int executeUpdate(String query, Object... args) throws Exception ;
}
