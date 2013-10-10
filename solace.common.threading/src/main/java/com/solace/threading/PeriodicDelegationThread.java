/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, PeriodicDelegationThread.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.threading;

import com.solace.logging.*;

public class PeriodicDelegationThread<T extends WorkerThread> extends DelegationThread<T> {
	private static Logger LOGGER = Logger.getLogger(PeriodicDelegationThread.class);
	
	/**
	 * Default ctor for the PeriodicDelegationThread
	 */
    public PeriodicDelegationThread()
    {       
    	this(null,null,null,null);
    }       

    /**
     * This constructor should be called if the default IConfigurationLoader is to be passed down to the
     * DelegationThread
     * @param _workRetriever responsible for retrieving work
     * @param _periodicHandler handles periodic operations
     * @param _lifecycle handles the lifecycle of the thread
     * @param _calculator calculates periodicity
     */
    public PeriodicDelegationThread(WorkRetriever _workRetriever, IPeriodicWorkHandler _periodicHandler,
        ILifecycle _lifecycle, IPeriodicityCalculator _calculator)        
    {
    	super(_workRetriever, _periodicHandler, _lifecycle);
        this.m_calculator = _calculator;
    }


    /**
     * This constructor should be used a IConfigurationLoader is to be specified that manages
     * configuration information for the PeriodicDelegationThread (e.g. passing parameters to the WorkRetriever)
     * @param _workRetriever
     * @param _periodicHandler
     * @param _lifecycle
     * @param _calculator
     * @param _configLoader
     */
    public PeriodicDelegationThread(WorkRetriever _workRetriever, IPeriodicWorkHandler _periodicHandler,
        ILifecycle _lifecycle, IPeriodicityCalculator _calculator, IConfigurationLoader _configLoader)        
    {
    	super(_workRetriever, _periodicHandler, _lifecycle, _configLoader);
    	this.m_calculator = _calculator;
    }
	

    private IPeriodicityCalculator m_calculator;
    
    protected IPeriodicityCalculator getPeriodicityCalculator()
    {
    	return this.m_calculator;
    }
}
