/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ECommandState.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.command;

/**
 * ECommandState is to represent the chain of execution states that a command can go through.
 * @author dwilliams
 *
 */
public enum ECommandState {
	EInitializing("Initializing"),
	EPendingExecution("Pending Execution"),
	EExecuting("Executing"),
	EPostExecution("Post Execution"),
	EFailed("Failed"),
	EFailing("Failing"),
	ESuccess("Success"),
	ERetriableFailure("Retriable Failure");
	
	private String m_key;
	
	private ECommandState(String key)
	{
		m_key = key;
	}
	
	public String getValue() {
		return m_key;
	}
}
