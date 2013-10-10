/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, DaoManager.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.caching;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

/**
 * My test dao factory that wires in test dao interfaces
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 * 
 */
@Component
public class DaoManager {

	private static IPersonDao personDao;
	
	private static IParentDao parentDao;
	
	public static IPersonDao getPersonDao() {
		return personDao;
	}
	
	public static IParentDao getParentDao() {
		return parentDao;
	}

	/**
	 * Should only be called by spring to inject the aDao impl
	 * @param dao an implementation of IADao
	 */
	@Autowired(required = true)
	public void setPersonDao(IPersonDao dao) {
		DaoManager.personDao = dao;
	}
	
	/**
	 * Should only be called by spring to inject the aDao impl
	 * @param dao an implementation of IADao
	 */
	@Autowired(required = true)
	public void setParentDao(IParentDao dao) {
		DaoManager.parentDao = dao;
	}
}
