/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, TimingAspect.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.logging;

import org.aspectj.lang.annotation.Aspect;
import org.perf4j.aop.AbstractTimingAspect;
import org.perf4j.LoggingStopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

@Aspect
public class TimingAspect extends AspectJTimingAspect {
	@Override
	protected Log4JStopWatch newStopWatch(String loggerName, String levelName) {
		Level level = Level.toLevel(levelName, Level.INFO);
		return new Log4JStopWatch(Logger.getLogger(loggerName), level, level);
	}

}
