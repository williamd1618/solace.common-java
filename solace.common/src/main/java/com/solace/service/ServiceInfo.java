/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ServiceInfo.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.service;

import java.util.*;

/**
 * Meant to be an class that delivers information in regard to a {@link Service}.
 * Can be anything that is key'd by a String 
 * @author dwilliams
 *
 */
public class ServiceInfo extends HashMap<String, Object> {
	
	public long m_startTime;
	
	/**
	 * Default constructor sets m_startTime to current time in ms
	 */
	public ServiceInfo()
	{
		m_startTime = Calendar.getInstance().getTimeInMillis();
	}
	
	public final long getUptime() 
	{
		long uptime = Calendar.getInstance().getTimeInMillis() - m_startTime;
		
		super.put("UPTIME", uptime);
		
		return uptime;
	}
}
