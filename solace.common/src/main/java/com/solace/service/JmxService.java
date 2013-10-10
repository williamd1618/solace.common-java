/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, JmxService.java, and the accompanying materials
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.lang.management.ManagementFactory;
import javax.management.*;
import java.util.*;

import com.solace.*;
import com.solace.logging.*;
import com.solace.utility.ReflectionUtil;

/**
 * The JMX base extension to a service. Will expose the basic methods to start
 * and stop a service plus any derived public methods.
 * 
 * @author dwilliams
 * 
 */
public abstract class JmxService<MetaType> extends Service<MetaType> implements
		DynamicMBean {
	private static final Logger LOGGER;
	private static final IExceptionHandler HANDLER;

	private static MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

	private ObjectName name = null;

	private Boolean m_registered = Boolean.FALSE;

	static {
		LOGGER = Logger.getLogger(JmxService.class);
		HANDLER = ExceptionHandler.getInstance(JmxService.class);
	}

	/**
	 * The following is all of the MBean dynamic instrumentation
	 */
	protected ArrayList<MBeanAttributeInfo> m_attributes = new ArrayList<MBeanAttributeInfo>();
	protected ArrayList<MBeanConstructorInfo> m_constructors = new ArrayList<MBeanConstructorInfo>();;
	protected ArrayList<MBeanNotificationInfo> m_notifications = new ArrayList<MBeanNotificationInfo>();
	protected ArrayList<MBeanOperationInfo> m_operations = new ArrayList<MBeanOperationInfo>();
	protected MBeanInfo m_beanInfo = null;

	private final String m_className = this.getClass().getName();

	/**
	 * Default ctor
	 */
	public JmxService() {
		setupJmx();

		try {
			m_beanInfo = new MBeanInfo(m_className, getDescription(),
					m_attributes.toArray(new MBeanAttributeInfo[0]),
					m_constructors.toArray(new MBeanConstructorInfo[0]),
					m_operations.toArray(new MBeanOperationInfo[0]),
					m_notifications.toArray(new MBeanNotificationInfo[0]));
		} catch (Exception e) {
			HANDLER.handle(this,
					"MBeanInfo could not be constructed.", e, m_attributes,
					m_constructors, m_operations, m_notifications);
		}
	}

	/**
	 * Will bring up the JMX services. In order to add additional MBeanInfo
	 * constructs override the doSetupJmx method.
	 * 
	 * @see doSetupJmx(void)
	 */
	@Override
	public void start() {
		synchronized (m_registered) {
			if (!m_registered.booleanValue()) {
				try {
					// should we setup the MBeanInfo constructs here?
					name = new ObjectName(this.getClass().getPackage()
							.getName()
							+ ":type=" + this.getClass().getSimpleName());

					mbs.registerMBean(this, name);

					// only when mbean is registered to we fire to the super
					// level
					super.start();

				} catch (Exception e) {
					HANDLER.handle(this,
							"Could not bring up JMX interface.", e);
				}

				m_registered = Boolean.TRUE;
			}
		}
	}

	/**
	 * setup a JMX interfaces and provide a override point
	 * 
	 * @see #m_constructors
	 * @see #m_attributes
	 * @see #m_notifications
	 * @see #m_operations
	 */
	protected final void setupJmx() {
//		LOGGER.info("Methods");
//		for(Method m : this.getClass().getMethods())
//			LOGGER.infoFormat(m.getName(), m.getParameterTypes());
//		LOGGER.info("Fields");
//		for(Field f : this.getClass().getFields())
//			LOGGER.infoFormat(f.getName());

		m_attributes.add(new MBeanAttributeInfo("Information",
				"java.util.HashMap", "Properties", true, false, false));

		m_attributes.add(new MBeanAttributeInfo("Name", "java.lang.String",
				"Name of the Service", true, false, false));

		m_attributes.add(new MBeanAttributeInfo("Description",
				"java.lang.String", "Description of the Service", true, false,
				false));

		m_attributes.add(new MBeanAttributeInfo("Classpath",
				"java.lang.String", "Classpath of the Service", true, false,
				false));

		m_attributes.add(new MBeanAttributeInfo("RuntimeArgs",
				"java.lang.String",
				"Arguments passed to java.exe of the Service", true, false,
				false));

		m_attributes.add(new MBeanAttributeInfo("Main", "java.lang.String",
				"Main class invoked by java.exe of the Service", true, false,
				false));

		Constructor[] constructors = this.getClass().getConstructors();
		for (Constructor c : constructors)
			m_constructors.add(new MBeanConstructorInfo(String.format(
					"Constructs a {} object", this.getClass().getName()), c));

		MBeanParameterInfo[] params = null;
		m_operations.add(new MBeanOperationInfo("start",
				"Calls start on the Service", params, "void",
				MBeanOperationInfo.ACTION));

		m_operations.add(new MBeanOperationInfo("stop",
				"Calls stop on the Service", params, "void",
				MBeanOperationInfo.ACTION));

		doSetupJmx();
	}

	/**
	 * A flex point for additional jmx setup operations
	 * 
	 * @see #m_constructors
	 * @see #m_attributes
	 * @see #m_notifications
	 * @see #m_operations
	 */
	public void doSetupJmx() {
	}

	@Override
	public void stop() {
		
		try {
			super.stop();
			
			mbs.unregisterMBean(name);

		} catch (Exception e) {
			HANDLER.handle(this,
					"Could not unregister JMX interface", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.solace.service.Service#doStart()
	 */
	@Override
	public abstract void doStart();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.solace.service.Service#doStop()
	 */
	@Override
	public abstract void doStop();

	/**
	 * Allows the value of the specified attribute of the Dynamic MBean to be
	 * obtained.
	 */
	@Override
	public Object getAttribute(String attributeName)
			throws AttributeNotFoundException, MBeanException,
			ReflectionException {

		Object retVal = null;

		if (ReflectionUtil.hasMethod(this, "get" + attributeName)) {
			try {
				retVal = ReflectionUtil.invoke(this, "get" + attributeName);
			} catch (Exception e) {
				throw new ReflectionException(e);
			}
		} else
			throw new AttributeNotFoundException(String.format(
					"Cannot find get{} attribute in {}.", attributeName,
					m_className));

		return retVal;
	}

	/**
	 * Sets the value of the specified attribute of the Dynamic MBean.
	 */
	public void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException,
			MBeanException, ReflectionException {

		if (ReflectionUtil.hasMethod(this, "set" + attribute.getName(),
				attribute.getValue())) {
			try {
				ReflectionUtil.<java.lang.Void> invoke(this, "set"
						+ attribute.getName(), attribute.getValue());
			} catch (Exception e) {
				throw new ReflectionException(e);
			}
		} else
			throw new AttributeNotFoundException(String.format(
					"Cannot find set{} attribute in {}.", attribute.getName(),
					m_className));
	}

	/**
	 * Enables the to get the values of several attributes of the Dynamic MBean.
	 */
	public AttributeList getAttributes(String[] attributeNames) {

		// Check attributeNames is not null to avoid NullPointerException
		// later on
		//
		if (attributeNames == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"attributedNames cannot be null"),
					"Could not getAttributes.");
		}

		AttributeList resultList = new AttributeList();

		// If attributeNames is empty, return an empty result list
		//
		if (attributeNames.length == 0)
			return resultList;

		// Build the result attribute list
		String val = "";
		for (int i = 0; i < attributeNames.length; i++) {
			try {
				val = (String) attributeNames[i];
				Object value = getAttribute(val);
				resultList.add(new Attribute(attributeNames[i], value));
			} catch (Exception e) {
				HANDLER.handle(this,
						"Error getting AttributeList", e,
						new NameValue("Attribute", val));
			}
		}
		return resultList;
	}

	/**
	 * Sets the values of several attributes of the Dynamic MBean, and returns
	 * the list of attributes that have been set.
	 */
	public AttributeList setAttributes(AttributeList attributes) {

		// Check attributes is not null to avoid NullPointerException later on
		//
		if (attributes == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"AttributeList attributes cannot be null"), String.format(
					"Cannot invoke a setter of {}", m_className));
		}
		AttributeList resultList = new AttributeList();

		// If attributeNames is empty, nothing more to do
		//
		if (attributes.isEmpty())
			return resultList;

		// For each attribute, try to set it and add to the result list if
		// successfull
		//
		for (Iterator i = attributes.iterator(); i.hasNext();) {
			Attribute attr = (Attribute) i.next();
			try {
				setAttribute(attr);
				String name = attr.getName();
				Object value = getAttribute(name);
				resultList.add(new Attribute(name, value));
			} catch (Exception e) {
				HANDLER.handle(this,
						"Error setting Attribute", e,
						new NameValue("Attribute", attr));
			}
		}
		return resultList;
	}

	/**
	 * Allows an operation to be invoked on the Dynamic MBean.
	 */
	public Object invoke(String operationName, Object params[],
			String signature[]) throws MBeanException, ReflectionException {

		// Check operationName is not null to avoid NullPointerException
		// later on
		//
		if (operationName == null) {
			throw new RuntimeOperationsException(new IllegalArgumentException(
					"Operation name cannot be null"),
					"Cannot invoke a null operation in " + m_className);
		}

		Object obj = null;
		try {
			obj = ReflectionUtil.invoke(this, operationName, params);
		} catch (Exception e) {
			HANDLER.handle(this,
					"Could not invoke method", e, operationName, params);
		}

		return obj;
	}

	/**
	 * This method provides the exposed attributes and operations of the Dynamic
	 * MBean. It provides this information using an MBeanInfo object.
	 */
	public MBeanInfo getMBeanInfo() {

		// Return the information we want to expose for management:
		// the dMBeanInfo private field has been built at instanciation time
		//
		return m_beanInfo;
	}
}
