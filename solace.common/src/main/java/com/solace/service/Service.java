/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, Service.java, and the accompanying materials
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

import com.solace.logging.*;

import java.util.*;

/**
 * The abstract class of all service implementations.  Enforces a template
 * pattern on starting and stopping as well as retrieving ServiceInfo.
 * @author dwilliams
 * @see ServiceInfo
 */
public abstract class Service<MetaType> implements IService<MetaType> {
	
	private static final Logger LOGGER = Logger.getLogger(Service.class);
	
	private ServiceInfo m_info;
	
	private String m_classpath;
	
	private String m_runtimeArgs;
	
	/**
	 * The main class to be invoked
	 */
	private String m_mainClass;
	
	private String m_description;
	
	private String m_name;
	
	
	
	/**
	 * Default constructor creates a new ServiceInfo
	 */
	public Service() {
		m_info = new ServiceInfo();
	}
	
	public final HashMap<String,Object> getInformation()
	{
		return m_info;
	}

	/**
	 * To be invoked by any {@link ServiceManager} starting a {@link Service}
	 */
	public void start() 
	{
		LOGGER.info("Starting {}",this.getClass().getName());
		
		doStart();
	}

	/**
	 * Derived hook-in for a derived class to start a process
	 */
	public abstract void doStart();
	
	/**
	 * To be invoked by any {@link ServiceManager} stopping a {@link Service}
	 */
	public void stop() {
		
		LOGGER.info("Stopping {}",this.getClass().getName());
	
		doStop();
	}
	
	/**
	 * Abstract hook-in for a derived instancing stopping functionality
	 */
	public abstract void doStop();
	
	public void start(MetaType data) { }
	
	public void stop(MetaType data) { }
	
	public void setMain(String mainClass) {
		m_mainClass = mainClass;
	}
	
	public String getMain() {
		return m_mainClass;
	}
	
	public void setClasspath(String classpath) {
		m_classpath = classpath;
	}
	
	public String getClasspath() {
		return m_classpath;
	}
	
	public void setRuntimeArgs(String args) {
		m_runtimeArgs = args;
	}
	
	public String getRuntimeArgs() {
		return m_runtimeArgs;
	}
	
	@Override
	public String getName() {
		return m_name;
	}
	
	@Override
	public void setName(String name) {
		m_name = name;
	}
	
	@Override
	public String getDescription() {
		return m_description;
	}
	
	@Override
	public void setDescription(String description) {
		m_description = description;
	}
}
