/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ExceptionHandled.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace;

import java.lang.annotation.*;

@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
/**
 * An annotation that will be used with an AOP runtime 
 * to denote how an exception will be handled.  This will allow for certain exceptions to
 * be handled by invoking the handlers AND/OR log the exception stack
 * if not an exception to be handled.  This will include derived exceptions.

 * ExceptionHandled will allow you to say "all exceptions are handled by these classes".
 */
public @interface ExceptionHandled {

	/**
	 * This is default to false to allow for as many exceptions if not handled
	 * via {@link #handleFor()} and {@link #handlers()}
	 * <p>
	 * This tells the {@link ExceptionHandlingAspect} to simply log an error for
	 * the class
	 * 
	 * @return a boolean that denotes whether or not we should log the exception
	 */
	boolean logExceptionStack() default false;
	
	/**
	 * Will provide a declarative way of mapping an exception type to a specific
	 * {@link IExceptionHandler} or {@link IExceptionHandler}s
	 * @return
	 */
	ExceptionHandledBy[] handlers() default {};
	
	/**
	 * We are defining an Object here b/c we have to have a literal default
	 * and we shouldn't use base Exception b/c it could be rethrown as that.
	 * @return a class
	 */
	Class<? extends Object> rethrowAs() default Object.class;
}
