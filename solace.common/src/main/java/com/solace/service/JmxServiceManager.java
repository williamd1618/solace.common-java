/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, JmxServiceManager.java, and the accompanying materials
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
package com.solace.service;

/**
 * @author dwilliams
 *
 */
public class JmxServiceManager<MetaType> extends ServiceManager<MetaType> implements IRmiService {
	
	private String m_uri;

	/**
	 * 
	 */
	public JmxServiceManager() {
		// TODO Auto-generated constructor stub
	}
	
	public void setUri(String uri) {
		m_uri = uri;
	}
	
	public String getUri() {
		return m_uri;
	}
	
	/**
	 * Override the {@link Service} start method to guarantee JMX RMI instrumentation occurs
	 * @see Service#start()
	 */
	@Override
	public void start()
	{
		// setup our JMX connectors
		
		super.start();
	}
	
	/**
	 * Override the {@link Service} stop to guarantee JMX RMI instrumentation shutdown
	 * @see Service#stop()
	 */
	@Override
	public void stop()
	{
		// tear down our JMX connectors
		
		super.stop();
	}
}
