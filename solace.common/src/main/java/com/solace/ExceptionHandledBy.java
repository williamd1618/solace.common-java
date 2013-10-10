/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ExceptionHandledBy.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace;

import java.lang.annotation.*;

@Target( {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ExceptionHandledBy {
	
	/**
	 * This list of exceptions to chack for
	 * 
	 * @return an array of exception types
	 */
	Class<? extends Exception>[] handleFor() default {};
	
	/**
	 * The handler classes for the exception and args to be passed to
	 * 
	 * @return a list of {@link IExceptionHandler} types
	 */
	Class<? extends IExceptionHandler>[] handlers() default {};

	/**
	 * We are defining an Object here b/c we have to have a literal default
	 * and we shouldn't use base Exception b/c it could be rethrown as that.
	 * @return a class
	 */
	Class<? extends Object> rethrowAs() default Object.class;
	
	/**
	 * Will tell {@link ExceptionHandlingAspect} to check to see if the 
	 * caught exception is a derived type of an exception caught in {@link ExceptionHandledBy#handleFor()} 
	 * <p>
	 * <b>the default is true</p>
	 * @return
	 */
	boolean checkForDerived() default true;
}
