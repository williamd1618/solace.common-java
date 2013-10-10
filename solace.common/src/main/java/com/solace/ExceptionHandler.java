/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ExceptionHandler.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace;

import com.solace.logging.*;
import com.solace.utility.*;

import java.io.*;
import java.util.*;

//import javax.xml.bind.*;

import java.net.*;

import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.*;

/**
 * Attempts to standardize exception handling via registration/broker paradigm.
 * <p>
 * Registration is done via a Map of {@link Class} and {@link IExceptionhanlder}
 * <p>
 * More needs to be documented. This is a rather complex mapping algorithm.
 * <p>
 * {@code static final Logger LOGGER = Logger.getLogger(Foo.class);}
 * 
 * @author dwilliams
 * 
 * @see EXCEPTION_CONFIG_FILE
 * @see EXCEPTION_CONFIG_BEAN_KEY
 * 
 */
public class ExceptionHandler implements IExceptionHandler {

	static final Logger LOGGER = Logger.getLogger(ExceptionHandler.class);

	protected Logger m_logger;

	/**
	 * will maintain configurations
	 */
	private static Map<Class<?>, ExceptionHandlers.Target> m_configs = new HashMap<Class<?>, ExceptionHandlers.Target>();

	/**
	 * will maintained constructed instances associated with Loggers
	 */
	private static Map<Class<?>, IExceptionHandler> m_registry = new HashMap<Class<?>, IExceptionHandler>();

	/**
	 * Sets the registry for all of the {@link com.ge.nbc.cbnc.logging.Logger}
	 * {@link IExceptionHandlers}. Should only be used for mock tests
	 * 
	 * @param registry
	 */
	public static void setRegistry(Map<Class<?>, IExceptionHandler> registry) {
		m_registry = registry;
	}

	private static Object m_syncRoot = new Object();

	private Map<Class<? extends Exception>, IExceptionHandler> m_handlers = null;

	private static Map<Class<? extends Exception>, IExceptionHandler> m_defaults = new HashMap<Class<? extends Exception>, IExceptionHandler>();

	/**
	 * Should only be used for testing. Added to circumvent limitations with
	 * Mock testing.
	 * 
	 * @param defaults
	 */
	public static void setDefaults(
			Map<Class<? extends Exception>, IExceptionHandler> defaults) {
		m_defaults = defaults;
	}

	/**
	 * The configuration file (JAXB or Spring)
	 */
	public static final String EXCEPTION_CONFIG_FILE = "exceptionHandlers.xml";

	/**
	 * The bean id that is expected to be found in the configuration file
	 */
	public static final String EXCEPTION_CONFIG_BEAN_KEY = "exceptionHandlers";

	private static boolean m_loaded = false;

	/**
	 * loads the configuration file via JAXB Should I put this in a static
	 * initialization block to guarantee that it is loaded prior to the
	 * getInstance() call? That way guaranteeing
	 * 
	 * @throws ConfigurationException
	 *             thrown when loaded configuration is not correct
	 */
	private static void loadConfiguration() {
		synchronized (m_configs) {

			try {
				String[] configFiles = new String[] { EXCEPTION_CONFIG_FILE };
				BeanFactory factory = new ClassPathXmlApplicationContext(
						configFiles);

				ExceptionHandlers handlers = (ExceptionHandlers) factory
						.getBean(EXCEPTION_CONFIG_BEAN_KEY);

				if (handlers != null) {
					if (handlers.getTarget() != null
							&& handlers.getTarget().size() > 0) {
						for (ExceptionHandlers.Target t : handlers.getTarget()) {
							addToConfiguration(t);
						}
					}

					if (handlers.getDefault() != null
							&& handlers.getDefault().size() > 0)
						for (ExceptionHandlers.Target.Handler h : handlers
								.getDefault()) {
							addToDefaults(h);
						}
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}

			// try {
			// String file = ClassUtils.getParentDirectory(
			// ExceptionHandler.class).getAbsolutePath()
			// + File.separator + "exceptionHandlers.xml";
			//
			// File configFile = new File(file);
			//
			// // if we have a configFile that exists
			// // let's use it.
			// if (configFile.exists()) {
			// // load the jaxb context
			// JAXBContext jc = JAXBContext
			// .newInstance(com.solace.ExceptionHandlers.class);
			//
			// Unmarshaller u = jc.createUnmarshaller();
			//
			// ExceptionHandlers handlers = (ExceptionHandlers) u
			// .unmarshal(new FileInputStream(configFile));
			//
			// // iterate across the configurations
			// if (handlers != null) {
			// if (handlers.getTarget() != null
			// && handlers.getTarget().size() > 0) {
			// for (ExceptionHandlers.Target t : handlers
			// .getTarget()) {
			// addHandlerTarget(t);
			// }
			// }
			// }
			// } else {
			// LOGGER.info("exceptionHandlers.xml did not exist ....");
			// }
			// } catch (Exception e) {
			// throw new ConfigurationException(String.format(
			// "Error loading ExceptionHandler configuration: {}", e
			// .getMessage(), e));
			// }
		}
	}

	
	private ExceptionHandler(Class<?> clazz,
			Map<Class<? extends Exception>, IExceptionHandler> _handlers) {
		m_logger = Logger.getLogger(clazz);
		m_handlers = _handlers;
	}

	
	public static IExceptionHandler getInstance(Class<?> clazz) {

		IExceptionHandler instance = null;

		synchronized (m_configs) {
			
			// this way we load the config file once
			if (m_configs.isEmpty() && !m_loaded) {
				loadConfiguration();
				m_loaded = true;
			}

			// if we don't have a static instance already created
			if (!m_registry.containsKey(clazz)) {
				ExceptionHandlers.Target t = null;

				// check to make sure we have a config
				if ((t = m_configs.get(clazz)) != null) {

					instance = new ExceptionHandler(clazz,
							createTargetConfiguration(clazz, t));

					m_registry.put(clazz, instance);
				} else {
					instance = new ExceptionHandler(clazz, null);

					m_registry.put(clazz, instance);
				}
			} else {
				instance = m_registry.get(clazz);
			}
		}
		return instance;
	}

	/**
	 * Will load a configuration for an exception handler into the system.
	 * Publicly exposed if developer wants to add or change values at runtime
	 * 
	 * @param handler
	 *            the configuration
	 */
	public static void addToConfiguration(final ExceptionHandlers.Target target) throws Exception {
		synchronized (m_configs) {
			Logger logger = Logger.getLogger(target.getClazz());

			m_configs.put(Class.forName(target.getClazz()), target);
		}
	}

	public static void addToDefaults(final ExceptionHandlers.Target.Handler h) {
		synchronized (m_defaults) {
			try {
				Exception exception = ReflectionUtil
						.<Exception> createInstance(h.getException());
				IExceptionHandler handler = ReflectionUtil
						.<IExceptionHandler> createInstance(h.getClazz());

				m_defaults.put(exception.getClass(), handler);
			} catch (Exception e) {
				ExceptionHandler
						.getInstance(ExceptionHandler.class)
						.handle(
								null,
								String
										.format(
												"Could not load handler exception: {}, class: {}.",
												h.getException(), h.getClazz()),
								e);
			}
		}
	}

	/**
	 * Creates the target configuration
	 * 
	 * @param logger
	 *            an instance of a Logger
	 * @param t
	 *            an instance of a Target configuration
	 * @return the class/IExceptionHandler map
	 */
	private static Map<Class<? extends Exception>, IExceptionHandler> createTargetConfiguration(
			final Class<?> clazz, final ExceptionHandlers.Target t) {
		// if we do let's build up an instance and add it to the
		// registry
		Map<Class<? extends Exception>, IExceptionHandler> handlers = new HashMap<Class<? extends Exception>, IExceptionHandler>();

		// create each exception/handler combo and add it to the
		// previously
		// created map
		for (ExceptionHandlers.Target.Handler h : t.getHandler()) {

			Exception exception = null;
			IExceptionHandler handler = null;

			try {
				exception = ReflectionUtil.<Exception> createInstance(h
						.getException());
				handler = ReflectionUtil.<IExceptionHandler> createInstance(h
						.getClazz());

				handlers.put(exception.getClass(), handler);
			} catch (Exception e) {
				ExceptionHandler.getInstance(ExceptionHandler.class).handle(
						null,
						String.format("{} has an incorrect configuration", t
								.getClazz()), e);
			}
		}

		return handlers;
	}

	/**
	 * 
	 * 
	 * Invocation point
	 */
	@Override
	public void handle(Object sender, String msg, Exception e, Object... data) {

		// no additional checks needed here b/c we guarantee that the
		// singleton accessor makes sure an IExceptionHandler and Logger exists
		m_logger.error(buildOutputMessage(msg, data), e);

		if (e != null)
			handleException(sender, msg, e, data);
	}

	/**
	 * builds a standardized output string based upon whether or not additional
	 * information has been passed at the handle invocation.  If so, it will
	 * format appropriately.  The calling method will pass this as the message
	 * to the standard error logger.  This logger will then call toString against
	 * the exception hierarchy.
	 * 
	 * @param msg
	 *            a general message to output
	 * @param data
	 *            any additional data to be passed or logged
	 * @return the string to be logged
	 */
	private String buildOutputMessage(String msg, Object... data) {
		StringBuffer sb = new StringBuffer();

		sb.append(msg).append("\n");
		
		if ( data != null && data.length > 0 )
		{
			sb.append("Additional information passed at handle invoation:\n");
			for(Object o : data)
				sb.append(o.getClass().getName()).append(" : ").append(o.toString()).append("\n");
		}

		return sb.toString();
	}

	/**
	 * internal invocation point
	 * <ol>
	 * <li>fire first on exact matches of Targeted, class focused, exceptions</li>
	 * <li>fire on sub-typed targeted matches</li>
	 * <li>fire on exact matches on defaults</li>
	 * <li>fire on sub-typed matches on defaults</li>
	 * </ol>
	 * 
	 * @param sender
	 *            the instance that sent it. Can later be cast to a typed
	 *            instance for method invocations (e.g. rollback)
	 * @param msg
	 *            message to be output if needed
	 * @param e
	 *            the actual exception that was thrown
	 * @param data
	 *            additional data
	 */
	private void handleException(Object sender, String msg, Exception e,
			Object... data) {
		IExceptionHandler handler = null;

		// we may have a null handler stack if we just want to get log output
		if (m_handlers != null) {
			// mapped by class, not instance
			if ((handler = m_handlers.get(e.getClass())) != null) {
				m_logger
						.info(
								"Invoking exact targeted ExceptionHandler: [{}] against [{}]",
								handler.getClass(), e.getClass());
				handler.handle(sender, msg, e, data);
			}

			// now let's fire all subtyped targeted handlers
			for (Class<? extends Exception> c : m_handlers.keySet()) {
				if (!classesAreEqual(c, e.getClass()) && c.isInstance(e)) {
					handler = m_handlers.get(c);
					m_logger
							.info(
									"Invoking sub-typed targeted ExceptionHandler: [{}] for [{}] against [{}]",
									handler.getClass(), c, e.getClass());
					handler.handle(sender, msg, e, data);
				}
			}
		}

		// load synchronized default IExcepwtionHandler base
		// for exception chaining
		if (m_defaults != null) {
			// if we have an exact match we fire first on defaults
			if ((handler = m_defaults.get(e)) != null) {
				m_logger
						.info(
								"Invoking exact default ExceptionHandler: [{}] against [{}]",
								handler.getClass(), e.getClass());
				handler.handle(sender, msg, e, data);
			}

			// for each default
			for (Class<? extends Exception> c : m_defaults.keySet()) {
				// is the exception an instance of a default
				if (!classesAreEqual(c, e.getClass()) && c.isInstance(e)) {
					handler = m_defaults.get(c);
					// if so invoke the default hander
					m_logger
							.info(
									"Invoking subtyped default ExceptionHandler: [{}] for [{}] against [{}]",
									handler.getClass(), c, e.getClass());
					handler.handle(sender, msg, e, data);
				}
			}
		}
	}

	private static boolean classesAreEqual(Class<?> c1, Class<?> c2) {
		if (c1.getName().equals(c2.getName()))
			return true;
		else
			return false;
	}
}
