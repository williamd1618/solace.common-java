/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, LoggerTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.logging;

import java.io.File;

import com.solace.logging.*;
import com.solace.support.json.ClassUtils;

import org.junit.*;

public class LoggerTests {
	private static com.solace.logging.Logger LOGGER = com.solace.logging.Logger
			.getLogger(LoggerTests.class);

	// @org.junit.BeforeClass
	// public static void classSetup() throws Exception {
	// String file = ClassUtils
	// .getParentDirectory(LoggerTests.class)
	// .getAbsolutePath()
	// + File.separator + File.separator + "log4j.properties";
	//
	// org.apache.log4j.PropertyConfigurator.configure(file);
	//
	// //
	// ClassUtils.getParentDirectory(DelegationThreadTests.class).getAbsolutePath()
	// // + File.separator + "conf" + File.separator + ;
	// // org.apache.log4j.PropertyConfigurator
	// // .configure("./conf/log4j.properties");
	// }

	@Test
	public void testLogger() throws Exception {
		try {
//			
//			org.apache.log4j.Logger root = LOGGER.getRootLogger();
//			if (!root.getAllAppenders().hasMoreElements()) {
//				root.setLevel(Level.DEBUG);
//				root.addAppender(new ConsoleAppender(new PatternLayout(
//						"%-5p [%t]: %m%n")));
//
//				System.out
//						.println("log4j was not initialized ... initializing with a ConsoleAppender for DEBUG");
//			}
			LOGGER.debug("this is a test {}", "testLogger");

		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test	
	public void eventLogger() throws Exception {
		com.solace.logging.Logger.EventLogger event = LOGGER
				.begin("eventLogger");

		try {
			LOGGER.debug("this is a test {}", "eventLogger");

			event.end();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}
	}

}
