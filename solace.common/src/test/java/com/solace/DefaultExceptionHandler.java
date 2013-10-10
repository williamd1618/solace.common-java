/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, DefaultExceptionHandler.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace;

import com.solace.IExceptionHandler;
import com.solace.logging.Logger;

public class DefaultExceptionHandler implements IExceptionHandler {
	static Logger LOGGER = Logger.getLogger(DefaultExceptionHandler.class);

	@Override
	public void handle(Object sender, String msg, Exception e, Object... data) {
		LOGGER.debug("Default exception handler fired!");
	}
}
