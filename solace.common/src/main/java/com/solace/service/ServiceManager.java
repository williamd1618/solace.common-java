/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ServiceManager.java, and the accompanying materials
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

import java.util.*;

import com.solace.ExceptionHandler;
import com.solace.logging.*;

/**
 * ServiceManager is a {@link Service} that manages other Services.
 * 
 * @author dwilliams
 * 
 */
public abstract class ServiceManager<MetaType> extends Service<MetaType> {

	private static final Logger LOGGER = Logger.getLogger(ServiceManager.class);

	protected Map<String, Service<MetaType>> m_services = null;

	protected Map<String, Process> m_processes = new HashMap<String, Process>();

	/**
	 * The system key for the java classpath of the current running process
	 */
	public static final String CLASSPATH = "java.class.path";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.solace.service.Service#doStart()
	 */
	@Override
	public void doStart() {

		Runtime rt = Runtime.getRuntime();

		String cp = System.getProperty(CLASSPATH);

		for (Service svc : m_services.values()) {
			LOGGER.info("ServiceManager is starting service {{}}", svc
					.getName());

			String classpath = cp + svc.getClasspath();

			Process p = null;

			try {
				p = rt.exec(String.format("java {} -cp {} {}", svc
						.getRuntimeArgs(), classpath, svc.getMain()));

				m_processes.put(svc.getName(), p);
			} catch (java.io.IOException e) {
				ExceptionHandler.getInstance(this.getClass()).handle(this,
						String.format("Could not start {}", svc.getName()), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.solace.service.Service#doStop()
	 */
	@Override
	public void doStop() {

		for (String key : m_processes.keySet()) {
			LOGGER.info("Shutting down {}", key);

			Process p = m_processes.get(key);

			p.destroy();

			LOGGER.info("{} shutdown with a return code of %i", key, p
					.exitValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.solace.service.IService#getDescription()
	 */
	@Override
	public String getDescription() {

		String description = "";

		try {
			description = java.net.InetAddress.getLocalHost().getHostName()
					+ "-ServiceManager";
		} catch (Exception e) {
			ExceptionHandler.getInstance(this.getClass()).handle(this,
					"Could not determine hostname", e);
		}

		return description;
	}
}
