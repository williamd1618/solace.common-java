/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, AspectJTimingAspect.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.logging;

import org.perf4j.aop.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Is a derivation of the control structures in Perf4j. This modifies the
 * strategy for jdk5 aspectj compile time weaving by monitoring the runtime of
 * all public methods. This will additionally guarantee that all methods debug
 * out to the particular logging class an Enter an Exit method that will provide
 * additional information in debugging.
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 * 
 */
@Aspect
public abstract class AspectJTimingAspect extends AbstractTimingAspect {

	private static final Logger LOGGER = Logger
			.getLogger(AspectJTimingAspect.class);

	private static final String ENTER = "[ENTER] {}";
	private static final String EXIT = "[EXIT] {}";
	private static final String INPUT = "Input parameters: {}";

	/**
	 * Will do the typical invocation of the logging instances via perf4j but
	 * will do them instead of on annotated methods but on all public methods.
	 * Additionally will guarantee that no instrumentation is needed for the
	 * enter and exit in the typical application logs
	 * 
	 * @param pjp
	 *            a ProceedingJoinPoint compiled in via aspectj
	 * @return returns the object if any
	 * @throws Throwable
	 */
	@Around(value = "execution(public * *(..))", argNames = "pjp")
	public Object doPerfLogging(final ProceedingJoinPoint pjp) throws Throwable {
		Object o = null;
		Object caller = pjp.getThis();

		Logger logger = null;
		if (pjp.getThis() != null)
			logger = Logger.getLogger(pjp.getThis().getClass());
		else
			logger = Logger.getLogger("main");

		Profiled p = DefaultProfiled.INSTANCE;

		try {
			if (logger.isDebugEnabled()) {
				logger.debug(ENTER, pjp.getSignature().toShortString());
				if (pjp.getArgs() != null && pjp.getArgs().length > 0) {
					StringBuilder sb = new StringBuilder();
					for (Object param : pjp.getArgs())
						sb.append(param).append("; ");

					logger.debug(INPUT, sb.toString());
				}
			}
			o = super.doPerfLogging(pjp, p);
		} finally {
			if (logger.isDebugEnabled())
				logger.debug(EXIT, pjp.getSignature().toShortString());
		}
		return o;
	}

	/**
	 * Will override the value of the tag that will be aggregated via perf4j.
	 * This structure is going to be the class.methodName
	 */
	@Override
	protected String getStopWatchTag(Profiled profiled,
			AbstractJoinPoint joinPoint, Object returnValue,
			Throwable exceptionThrown) {

		if (joinPoint == null || joinPoint.getExecutingObject() == null
				|| joinPoint.getExecutingObject().getClass() == null)
			return joinPoint.getMethodName();
		else
			return joinPoint.getExecutingObject().getClass().getSimpleName()
					+ "." + joinPoint.getMethodName();
	}
}
