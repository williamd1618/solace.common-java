/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ELogLevel.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.logging;

/**
 * What levels of logging do we support and how should they be mapped in 
 * our logger
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 *
 */
public enum ELogLevel {
	Debug(),
	Warn(),
	Info(),
	Error(),
	Fatal();
}
