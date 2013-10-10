/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ReflectionUtil.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.utility;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.solace.logging.*;

/**
 * The ReflectionUtil class is simply a utility class for dynamically creating
 * classes.
 * 
 * @author dwilliams
 * 
 */
public class ReflectionUtil {

	static final Logger LOGGER = Logger.getLogger(ReflectionUtil.class);

	/**
	 * A class that dynamically reflects a type
	 * 
	 * @param <T>
	 *            the programmable type
	 * @param _name
	 *            the name of the class
	 * @param _ctorArgs
	 *            any constructor args
	 * @return returns the type
	 * @throws Exception
	 *             any and all exceptiosn thrown
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T createInstance(String _name,
			Object... _ctorArgs) throws Exception {
		T instance = null;

		Logger.EventLogger event = LOGGER.begin("createInstance");

		LOGGER.debug("Creating an instance of: {}", _name);

		Class<? extends Object> classToMake = Class.forName(_name);

		if (classToMake != null)
			if (_ctorArgs != null && _ctorArgs.length > 0) {
				Class[] types = new Class[_ctorArgs.length];

				for (int i = 0; i < _ctorArgs.length; i++)
					types[i] = _ctorArgs[i].getClass();

				instance = (T) classToMake.getConstructor(types).newInstance(
						_ctorArgs);
			} else
				instance = (T) classToMake.newInstance();

		event.end();

		return instance;
	}

	/**
	 * Invokes a method. Pass in java.lang.Void if it is not to return anything.
	 * 
	 * @throws NoSuchMethodException
	 *             if the method is not found
	 * @throws InvocationTargetException
	 *             target is not defined
	 * @throws IllegalAccessException
	 *             if the method is not private
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T invoke(Object _o, String _method,
			Object... _args) throws NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		ArrayList<Class<?>> inputs = new ArrayList<Class<?>>();
		for (Object o : _args)
			inputs.add(o.getClass());
		Method m = _o.getClass().getMethod(_method,
				inputs.toArray(new Class<?>[0]));
		
		return (T) m.invoke(_o, _args);				
	}

	/**
	 * 
	 * @param _o
	 *            the instance
	 * @param _method
	 *            the method
	 * @param _args
	 *            any input args
	 * @return a boolean if found
	 * @throws SecurityException
	 *             if the java runtime system credentials do not allow
	 * @throws NoSuchMethodException
	 *             method not found
	 */
	public static boolean hasMethod(Object _o, String _method, Object... _args) {

		boolean retVal = true;

		ArrayList<Class<?>> inputs = new ArrayList<Class<?>>();
		for (Object o : _args)
			inputs.add(o.getClass());
		try {
			Method m = _o.getClass().getMethod(_method,
					(Class<?>[]) inputs.toArray());
		} catch (Exception e) {
			retVal = false;
		}

		return retVal;
	}
}
