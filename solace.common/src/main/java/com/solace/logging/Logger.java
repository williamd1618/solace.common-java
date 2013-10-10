/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, Logger.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.logging;

import java.util.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * A standardized logging class that currently is composed of a log4j Logger.
 * Attempts to standardize how logging should be handled and additionally
 * provides some other bells and whistles.
 * <p>
 * {@code Logger logger = Logger.getLogger(Test.class);
 * logger.debug("typical log4j log call."); *}
 * <p>
 * {@code Logger logger = Logger.getLogger(Some.class); Logger.EventLogger event
 * = logger.begin("SomeMethod"); event.info("informative message"); event.end();
 * *}
 * 
 * @author dwilliams
 * 
 */
public class Logger implements org.slf4j.Logger {

	private org.slf4j.Logger m_logger = null;

	static final Object m_syncRoot = new Object();

	private static Map<org.slf4j.Logger, com.solace.logging.Logger> m_loggers = new HashMap<org.slf4j.Logger, com.solace.logging.Logger>();

	/**
	 * Returns a decorated instance of the log4j ILog interface to introduced
	 * new methods such as begin() and end()
	 * 
	 * @see org.apache.log4j.Logger#getLogger(Class)
	 */
	public static Logger getLogger(Class<?> _class) {
		Logger instance = null;

		synchronized (m_loggers) {
			if (!m_loggers.containsKey(LoggerFactory.getLogger(_class))) {
				org.slf4j.Logger logger = LoggerFactory.getLogger(_class);

				m_loggers.put(logger, new Logger(logger));
			}

			instance = m_loggers.get(org.apache.log4j.Logger.getLogger(_class));

		}

		return instance;
	}

	/**
	 * Returns a decorated instance of the log4j ILog interface to introduced
	 * new methods such as begin() and end()
	 * 
	 * @see org.apache.log4j.Logger#getLogger(String)
	 */
	public static Logger getLogger(String _value) {
		Logger instance = null;

		synchronized (m_loggers) {
			if (!m_loggers.containsKey(org.apache.log4j.Logger
					.getLogger(_value))) {
				org.slf4j.Logger logger = LoggerFactory.getLogger(_value);

				m_loggers.put(logger, new Logger(logger));
			}

			instance = m_loggers.get(LoggerFactory.getLogger(_value));
		}

		return instance;
	}

	/**
	 * A private constructor that is initialized by the static getLogger method
	 * 
	 * @param _logger
	 * @see #getLogger(Class)
	 */
	private Logger(org.slf4j.Logger _logger) {
		m_logger = _logger;
	}

	/**
	 * begin method merely proxies to another method
	 * 
	 * @param _method
	 *            method that we are logging
	 * @see #begin(String, String)
	 * @return
	 */
	public EventLogger begin(String _method) {
		return begin(_method, null);
	}

	public void info(String _message) {
		if (m_logger.isInfoEnabled())
			m_logger.info(_message);
	}

	/**
	 * logs a formatted string
	 * 
	 * @param _format
	 *            the stringformat
	 * @param _inputs
	 *            inputs to be formatted
	 * @see java.util.String#format(String, Object...)
	 */
	public void info(String _format, Object... _inputs) {
		if (isInfoEnabled())
			m_logger.info(_format, _inputs);
	}

	public void info(String _format, Throwable _t, Object... _inputs) {
		if (isInfoEnabled())
			m_logger.info(_format, _t, _inputs);
	}

	public void debug(String _message) {
		if (isDebugEnabled())
			m_logger.debug(_message);
	}

	public void debug(String _message, Throwable _t) {
		if (isDebugEnabled())
			m_logger.debug(_message, _t);
	}

	/**
	 * logs a formatted string
	 * 
	 * @param _format
	 *            the stringformat
	 * @param _inputs
	 *            inputs to be formatted
	 * @see java.util.String#format(String, Object...)
	 */
	public void debug(String _format, Object... _inputs) {
		if (isDebugEnabled())
			m_logger.debug(String.format(_format, _inputs));
	}

	public void debug(String _format, Throwable _t, Object... _inputs) {
		if (isDebugEnabled())
			m_logger.debug(_format, _t, _inputs);
	}

	public void error(String _message) {
		if (isErrorEnabled())
			m_logger.error(_message);
	}

	public void error(String _message, Throwable _t) {
		if (isErrorEnabled())
			m_logger.error(_message, _t);
	}

	/**
	 * logs a formatted string
	 * 
	 * @param _format
	 *            the stringformat
	 * @param _inputs
	 *            inputs to be formatted
	 * @see java.util.String#format(String, Object...)
	 */
	public void error(String _format, Object... _inputs) {
		if (isErrorEnabled())
			m_logger.error(_format, _inputs);
	}

	public void error(String _format, Throwable _t, Object... _inputs) {
		if (isErrorEnabled())
			m_logger.error(_format, _t, _inputs);
	}

	public void warn(String _message) {
		if (isWarnEnabled())
			m_logger.warn(_message);
	}

	public void warn(String _message, Throwable _t) {
		if (isWarnEnabled())
			m_logger.warn(_message, _t);
	}

	/**
	 * logs a formatted string
	 * 
	 * @param _format
	 *            the stringformat
	 * @param _inputs
	 *            inputs to be formatted
	 * @see java.util.String#format(String, Object...)
	 */
	public void warn(String _format, Object... _inputs) {
		if (isWarnEnabled())
			m_logger.warn(_format, _inputs);
	}

	/**
	 * 
	 * @param _format
	 *            the string format
	 * @param _t
	 *            a throwable
	 * @param _inputs
	 *            string inputs
	 */
	public void warn(String _format, Throwable _t, Object... _inputs) {
		if (isWarnEnabled())
			m_logger.warn(_format, _t, _inputs);
	}

	public boolean isDebugEnabled() {
		return m_logger.isDebugEnabled();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Logger)) {
			return false;
		} else {
			Logger l = (Logger) obj;

			return l.getLogger().equals(this.getLogger());
		}
	}

	/**
	 * begin merely factories an EventLogger to be returned and used for logging
	 * at method scope. No exception should be thrown but at the same time I
	 * don't want the application developer to have to handle exceptions
	 * 
	 * @param _method
	 * @param _user
	 * @return
	 */
	public EventLogger begin(String _method, String _user) {
		EventLogger logger = null;

		try {
			if (_method == null || (_method.equals("")))
				throw new IllegalArgumentException(
						"_method must not be null or empty.");

			logger = new EventLogger(this, _method, _user);

			logger.begin();
		} catch (Exception e) {
			this.error(e.getMessage(), e);
		}

		return logger;
	}

	public final org.slf4j.Logger getLogger() {
		return this.m_logger;
	}

	/**
	 * The EventLogger class' sole purpose is to be able to audit the entry and
	 * exit calls of a method or event in the system that can be audited and
	 * used to debug issues in the system.
	 * <p>
	 * The class extends the {@link Logger} class to allow for standard logging
	 * information
	 * <p>
	 * 
	 * <pre>
	 * {@code
	 * Logger logger = Loggerr.getLogger(Test.class);
	 * EventLogger event = logger.begin("someMethod")
	 * event.getLogger().debug("we just did something"); // actual method in the log4j framework
	 * event.getLogger().info("an info message");
	 * event.end();
	 * }
	 * </pre>
	 * 
	 * @author dwilliams
	 * 
	 */
	public class EventLogger {
		private Logger m_logger = null;
		private long m_start;
		private String m_event;
		private String m_user;

		/**
		 * EventLogger ctor
		 * 
		 * @param _logger
		 *            an instance of the common Logger
		 * @param _event
		 *            a string identified event
		 * @param _user
		 *            the user that invoked the event
		 * @throws Exception
		 */
		public EventLogger(Logger _logger, String _event, String _user)
				throws Exception {
			if (_logger == null)
				throw new Exception("_logger cannot be null");

			m_logger = _logger;
			m_start = System.nanoTime();
			m_event = _event;
			m_user = _user;
		}

		/**
		 * Simply exposes the composed Logger
		 * 
		 * @return
		 */
		public Logger getLogger() {
			return m_logger;
		}

		/**
		 * Simply writes out an entry log call
		 */
		public void begin() {
			m_logger.debug("[ENTER] method: {}, user: {}", m_event, m_user);
		}

		/**
		 * Writes out an exit log call
		 */
		public void end() {
			m_logger.debug("[EXIT] duration: {} ns, method: {}, user: {}",
					System.nanoTime() - m_start, m_event, m_user);
		}
	}

	@Override
	public void debug(String arg0, Object arg1) {
		m_logger.debug(arg0, arg1);
	}

	@Override
	public void debug(Marker arg0, String arg1) {
		m_logger.debug(arg0, arg1);

	}

	@Override
	public void debug(String arg0, Object arg1, Object arg2) {
		m_logger.debug(arg0, arg1, arg2);

	}

	@Override
	public void debug(Marker arg0, String arg1, Object arg2) {
		m_logger.debug(arg0, arg1, arg2);

	}

	@Override
	public void debug(Marker arg0, String arg1, Object... arg2) {
		m_logger.debug(arg0, arg1, arg2);

	}

	@Override
	public void debug(Marker arg0, String arg1, Throwable arg2) {
		m_logger.debug(arg0, arg1, arg2);

	}

	@Override
	public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
		m_logger.debug(arg0, arg1, arg2, arg3);

	}

	@Override
	public void error(String arg0, Object arg1) {
		m_logger.error(arg0, arg1);
	}

	@Override
	public void error(Marker arg0, String arg1) {
		m_logger.error(arg0, arg1);
	}

	@Override
	public void error(String arg0, Object arg1, Object arg2) {
		m_logger.error(arg0, arg1, arg2);
	}

	@Override
	public void error(Marker arg0, String arg1, Object arg2) {
		m_logger.error(arg0, arg1, arg2);
	}

	@Override
	public void error(Marker arg0, String arg1, Object... arg2) {
		m_logger.error(arg0, arg1, arg2);
	}

	@Override
	public void error(Marker arg0, String arg1, Throwable arg2) {
		m_logger.error(arg0, arg1, arg2);
	}

	@Override
	public void error(Marker arg0, String arg1, Object arg2, Object arg3) {
		m_logger.error(arg0, arg1, arg2, arg3);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void info(String arg0, Object arg1) {
		m_logger.info(arg0, arg1);
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		m_logger.info(arg0, arg1);
	}

	@Override
	public void info(Marker arg0, String arg1) {
		m_logger.info(arg0, arg1);
	}

	@Override
	public void info(String arg0, Object arg1, Object arg2) {
		m_logger.info(arg0, arg1, arg2);

	}

	@Override
	public void info(Marker arg0, String arg1, Object arg2) {
		m_logger.info(arg0, arg1, arg2);
	}

	@Override
	public void info(Marker arg0, String arg1, Object... arg2) {
		m_logger.info(arg0, arg1, arg2);

	}

	@Override
	public void info(Marker arg0, String arg1, Throwable arg2) {
		m_logger.info(arg0, arg1, arg2);

	}

	@Override
	public void info(Marker arg0, String arg1, Object arg2, Object arg3) {
		m_logger.info(arg0, arg1, arg2);
	}

	@Override
	public boolean isDebugEnabled(Marker arg0) {
		return m_logger.isDebugEnabled(arg0);
	}

	@Override
	public boolean isErrorEnabled() {
		return m_logger.isErrorEnabled();
	}

	@Override
	public boolean isErrorEnabled(Marker arg0) {
		return m_logger.isErrorEnabled(arg0);
	}

	@Override
	public boolean isInfoEnabled() {
		return m_logger.isInfoEnabled();
	}

	@Override
	public boolean isInfoEnabled(Marker arg0) {
		return m_logger.isInfoEnabled(arg0);
	}

	@Override
	public boolean isTraceEnabled() {
		return m_logger.isTraceEnabled();
	}

	@Override
	public boolean isTraceEnabled(Marker arg0) {
		return m_logger.isTraceEnabled(arg0);
	}

	@Override
	public boolean isWarnEnabled() {
		return m_logger.isWarnEnabled();
	}

	@Override
	public boolean isWarnEnabled(Marker arg0) {
		return m_logger.isWarnEnabled(arg0);
	}

	@Override
	public void trace(String arg0) {
		m_logger.trace(arg0);
	}

	@Override
	public void trace(String arg0, Object arg1) {
		m_logger.trace(arg0, arg1);

	}

	@Override
	public void trace(String arg0, Object... arg1) {
		m_logger.trace(arg0, arg1);

	}

	@Override
	public void trace(String arg0, Throwable arg1) {
		m_logger.trace(arg0, arg1);

	}

	@Override
	public void trace(Marker arg0, String arg1) {
		m_logger.trace(arg0, arg1);

	}

	@Override
	public void trace(String arg0, Object arg1, Object arg2) {
		m_logger.trace(arg0, arg1, arg2);

	}

	@Override
	public void trace(Marker arg0, String arg1, Object arg2) {
		m_logger.trace(arg0, arg1, arg2);

	}

	@Override
	public void trace(Marker arg0, String arg1, Object... arg2) {
		m_logger.trace(arg0, arg1, arg2);

	}

	@Override
	public void trace(Marker arg0, String arg1, Throwable arg2) {
		m_logger.trace(arg0, arg1, arg2);

	}

	@Override
	public void trace(Marker arg0, String arg1, Object arg2, Object arg3) {
		m_logger.trace(arg0, arg1, arg2);

	}

	@Override
	public void warn(String arg0, Object arg1) {
		m_logger.warn(arg0, arg1);

	}

	@Override
	public void warn(Marker arg0, String arg1) {
		m_logger.warn(arg0, arg1);

	}

	@Override
	public void warn(String arg0, Object arg1, Object arg2) {
		m_logger.warn(arg0, arg1, arg2);

	}

	@Override
	public void warn(Marker arg0, String arg1, Object arg2) {
		m_logger.warn(arg0, arg1, arg2);

	}

	@Override
	public void warn(Marker arg0, String arg1, Object... arg2) {
		m_logger.warn(arg0, arg1, arg2);

	}

	@Override
	public void warn(Marker arg0, String arg1, Throwable arg2) {
		m_logger.warn(arg0, arg1, arg2);

	}

	@Override
	public void warn(Marker arg0, String arg1, Object arg2, Object arg3) {
		m_logger.warn(arg0, arg1, arg2, arg3);

	}
}
