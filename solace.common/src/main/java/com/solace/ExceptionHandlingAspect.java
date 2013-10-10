/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ExceptionHandlingAspect.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.*;

import com.solace.*;
import com.solace.logging.*;

/**
 * The ExceptionHandlingAspect will allow for a jdk5 aspectj pointcut for an
 * around implementation.
 * <p>
 * See
 * {@link ExceptionHandlingAspect#handle(ProceedingJoinPoint, ExceptionHandled)}
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 * 
 */
@Aspect
public class ExceptionHandlingAspect {

	static Map<Class<? extends IExceptionHandler>, IExceptionHandler> instances = new ConcurrentHashMap<Class<? extends IExceptionHandler>, IExceptionHandler>();

	static final Exception EXCEPTION = new Exception();

	static final Logger LOGGER = Logger
			.getLogger(ExceptionHandlingAspect.class);

	/**
	 * Will fire around methods decorated with {@link ExceptionHandled} in
	 * following fashion:
	 * <p>
	 * <ol>
	 * <li>if {@link ExceptionHandled#logExceptionStack()} is true then a
	 * uniform exception will be printed to {@link Logger#error(String)}</li>
	 * <li>if {@link ExceptionHandled#handlers()} is not empty the exception
	 * will be:</li>
	 * <ol>
	 * <li>evaluated to see if it is an instance of
	 * {@link ExceptionHandledBy#handleFor()} <b>OR</b> if the exception is
	 * derived from a class in {@link ExceptionHandledBy#handleFor()} if
	 * {@link ExceptionHandledBy#checkForDerived()} is <b>true</b></li>
	 * <li>if one or both of the previous conditions are met the exception will
	 * be applied to each {@link IExceptionHandler} in
	 * {@link ExceptionHandledBy#handlers()}</li>
	 * <li>finally, if {@link ExceptionHandledBy#rethrowAs()} is set to a class
	 * derived off of {@link Exception} the new exception will be created
	 * through {@link Exception#Exception(String, Throwable)} and rethrown</li>
	 * </ol>
	 * </ol>
	 * <p>
	 * It should be noted that the rethrow could be held in
	 * {@link ExceptionHandled} however, at this point, we don't quite know how
	 * people will want to use the advice. Until Because of this we will set one
	 * at the global level to be handled after all evaluations have occurred.
	 * Now if, however, a nested {@link ExceptionHandledBy} has a rethrowAs set
	 * the global will be missed as the rethrow will occur inside of the loop
	 * evaluation.
	 * 
	 * @param pjp
	 *            The ProceedingJoinPoint with metadata around the method that
	 *            will be called
	 * @param exceptionHandled
	 *            A collection of exception types to be handled. If defined we
	 *            will proceed calls down to the ExceptionHandler. If none will
	 *            proceed then we automatically move forward.
	 * @return the object returned by the joinpoint
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	@Around(value = "call(* *(..)) && @annotation(exceptionHandled)", argNames = "pjp,exceptionHandled")
	public Object handle(final ProceedingJoinPoint pjp,
			final ExceptionHandled exceptionHandled) throws Throwable {

		Object retVal = null;

		try {

			retVal = pjp.proceed();

		} catch (Exception e) {

			if (exceptionHandled.logExceptionStack()) {
				Logger logger = null;
				
				if (pjp.getThis() == null) 
					logger = Logger.getLogger("main");
				else
					logger = Logger.getLogger(pjp.getThis().getClass());
				
				logger.error(
						"Exception caught:\nInput arguments: {}", e,
						buildArgsString(pjp.getArgs()));
			}

			for (ExceptionHandledBy by : exceptionHandled.handlers()) {

				for (Class<? extends Exception> eClass : by.handleFor()) {
					if (eClass.equals(e.getClass())
							|| (by.checkForDerived() && eClass
									.isAssignableFrom(e.getClass()))) {
						for (Class<? extends IExceptionHandler> handlerClass : by
								.handlers()) {
							IExceptionHandler handler = null;

							if ((handler = instances.get(handlerClass)) == null) {
								handler = handlerClass.newInstance();
								instances.put(handlerClass, handler);
							}

							// invoke the IExceptionHandler by leveraging
							// the code from the JoinPoint
							// passed in the ProceedingJoinPoint instance
							handler.handle(pjp.getThis(), e.getMessage(), e,
									pjp.getArgs());
						}

						if (Exception.class.isAssignableFrom(by.rethrowAs())) {
							Class<? extends Exception> rethrowClass = (Class<? extends Exception>) by
									.rethrowAs();

							LOGGER.debug("Rethrowing as {}", rethrowClass
									.toString());

							Constructor<? extends Exception> ctor = rethrowClass
									.getConstructor(String.class,
											Throwable.class);

							Exception rethrow = ctor.newInstance(
									e.getMessage(), e);

							throw rethrow;
						}
					}
				}
			}

			if (Exception.class.isAssignableFrom(exceptionHandled.rethrowAs())) {
				Class<? extends Exception> rethrowClass = (Class<? extends Exception>) exceptionHandled
						.rethrowAs();

				LOGGER.debug("Global rethrow as {}", rethrowClass
						.toString());

				Constructor<? extends Exception> ctor = rethrowClass
						.getConstructor(String.class, Throwable.class);

				Exception rethrow = ctor.newInstance(e.getMessage(), e);

				throw rethrow;
			}
		}

		return retVal;
	}

	static String buildArgsString(Object[] args) {
		StringBuilder sb = new StringBuilder();

		if (args != null && args.length > 0) {
			for (Object o : args)
				sb.append(o).append("; ");
		}

		return sb.toString();
	}
}
