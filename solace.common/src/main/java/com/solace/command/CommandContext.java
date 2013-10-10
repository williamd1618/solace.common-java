/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, CommandContext.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.command;

import java.util.*;

/**
 * The CommandContext is meant to be a context adaptor for information to be passed in a 
 * chain of responsibility.
 * @author dwilliams
 *
 */
public final class CommandContext {
	
	private Command<?> m_command;
	private Object[] m_inputs;
	private Object[] m_outputs;
	private ECommandState m_finalState;
	List<ECommandState> m_states = new ArrayList<ECommandState>();
	
	/**
	 * A CommandContext has to take in information on the Command that is executing
	 * as well as its next Command
	 * @param command the command that is executing
	 * @param args its input arguments
	 */
	public CommandContext(Command<?> command, Object... args)
	{
		this.m_command = command;
		m_inputs = args;
		m_states.add(ECommandState.EExecuting);
	}
	
	/**
	 * The name of the command
	 * @return the string identifier
	 */
	public String getCommandName()
	{
		return m_command.getName();
	}
	
	
	/**
	 * get the output args of the command
	 * @return command args
	 */
	public Object[] getOutputs() {
		return m_outputs;
	}
	
	/**
	 * get the input args
	 * @return an object array
	 */
	public Object[] getInputs() {
		return m_inputs;
	}
	
	/**
	 * adds the output params
	 */
	public void setOutputs(Object... args)
	{
		m_outputs = args;
	}
	
	/**
	 * A command can go through many states.  This audits the state and sets the final state
	 * @param state
	 */
	public void addState(ECommandState state)
	{
		m_states.add(state);
		m_finalState = state;
	}	
	
	/**
	 * Get a list of all of the states that the Command has gone through
	 * @return
	 */
	public List<ECommandState> getStates() {
		return m_states;
	}
	
	/**
	 * returns the final state
	 * @return
	 */
	public ECommandState getFinalState() {
		return m_finalState;
	}
}
