/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ClassUtils.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.support.json;

import java.io.File;
import java.net.*;

import com.solace.logging.*;

public class ClassUtils {

//	private static final Logger LOGGER = Logger.getLogger(ClassUtils.class);
	
	/**
	 * getParentDirectory Returns the directory of this class. If the class is
	 * contained in a jar the jar acts as a virtual root and the directory of
	 * the jar is returned.
	 * 
	 * @param _class
	 * @return
	 */
	public static File getParentDirectory(Class<? extends Object> _class)
			throws Exception {
//		Logger.EventLogger event = LOGGER.begin("getParentDirectory");

		String path = determinePath(_class);

//		event.end();

		return new File(path);
	}

	private static String determinePath(Class<? extends Object> _class)
			throws Exception {
//		Logger.EventLogger event = LOGGER.begin("determinePath");

		URL url = _class.getResource("/");

		File file = new File(url.toURI());
		
//		event.end();

		return file.toString();
	}

//	private static String determineLocalDir(String path) {
//		path = path.substring(0, path.lastIndexOf(File.separator));
//
//		LOGGER.debug(path);
//
//		if (path.endsWith("ar!")) {
//			path = path.substring(0, path.lastIndexOf(File.separator));
//		}
//
//		return path;
//	}
}
