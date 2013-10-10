/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, Command.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.command;

import com.solace.*;

/**
 * The Command base class is meant to be the derivation point for all things in a chain of responsibility
 * 
 * In order to extend the command class to be a void return pass in T as java.lang.Void and return null
 * 
 * @author dwilliams
 *
 * @param <T> the return type
 * @see java.lang.Void
 */
public abstract class Command<T> {
	
	private String m_name;
	
	public Command() 
	{
		
	}
	
	public Command(String name) 
	{
		m_name = name;
	}
	
	public String getName() {
		return m_name;
	}

	/**
	 * The execute command provides the command interface for everything else to hit against
	 * @param args
	 * @return
	 * @throws ArgumentException
	 */
	public final T execute(Object... args) throws ArgumentException, CommonException {
		if ( validate(args))
			return doExecute(args);
		else
			throw new ArgumentException("Arguments are incorrect", args);
	}
	
	protected abstract boolean validate(Object... args);
	
	protected abstract T doExecute(Object... args) throws CommonException;
}
 
