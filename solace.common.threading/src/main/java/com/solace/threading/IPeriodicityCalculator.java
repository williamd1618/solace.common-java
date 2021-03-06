/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, IPeriodicityCalculator.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.threading;

/**
 * Implementing classes represent algorithms used by the DelegationPeriodicThread
 * for determining when the thread should be processing units of work
 * @author dwilliams
 *
 */
public interface IPeriodicityCalculator {
	
	public boolean isValidExecutionWindow(java.util.Date _nextExecutionWindow);

}
