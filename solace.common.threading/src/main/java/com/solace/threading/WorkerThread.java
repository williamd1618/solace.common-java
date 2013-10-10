/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, WorkerThread.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.threading;

import java.util.*;
import com.solace.*;
import com.solace.logging.*;

/**
 * WorkerThread An abstract base that can be derived for processing purposes
 * within a delegation pattern.
 * <p>
 * Original implementation had configuration functionality; however, I'm leaving
 * this up to the contract of the DelegationThread and the WorkerThread
 * 
 * @author dwilliams
 * 
 */
public class WorkerThread extends Thread implements IProcessor,
		IManageable<WorkerThread> {
	private static final String RETURNING_TO_MANAGER = "Returning to manager.";

	private static final String PROCESS = "process";

	private static final Logger LOGGER = Logger.getLogger(WorkerThread.class);

	// Manager reference
	private IManager<WorkerThread> m_manager = null;

	// marks when the last refresh of the configuration occurred
	// initialize to 1 millisecond from 1/1/1970
	private Date m_lastRefresh = new Date(1);

	protected Object m_unitOfWork = null;

	protected IWorkProcessor m_processor = null;

	/**
	 * WorkerThread Default constructor
	 */
	public WorkerThread() {
		super();
	}

	public WorkerThread(ILifecycle _lifecycle, IWorkProcessor _processor) {
		this();

		this.m_lifecycle = _lifecycle;

		this.m_processor = _processor;
	}

	public IWorkProcessor getWorkProcessor() {
		return m_processor;
	}

	public void setWorkProcessor(IWorkProcessor _processor) {
		m_processor = _processor;
	}

	/**
	 * run Overridden from Thread Allows for the developer to simply implement
	 * buisness logic in doProcess
	 * 
	 * @see com.solace.threading.Thread#run()
	 */
	@Override
	public final void run() {
		Logger.EventLogger event = LOGGER.begin("run");

		try {

			while (isRunning()) {

				if (isRunning() && m_unitOfWork != null)
					this.process();

				synchronized (m_manager) {
					if (isRunning())
						returnToManager();

					m_manager.notify();
				}

				synchronized (this) {
					this.wait();
				}
			}

			LOGGER.debug(String.format("Thread: {} is dying.", this.getId()));
		} catch (Exception e) {
			// getExceptionHandler().handleException(e);
		} finally {
			event.end();
		}
	}

	/**
	 * process called by this to call handler logic
	 */
	@Override
	public final void process() {
		Logger.EventLogger event = LOGGER.begin(PROCESS);

		if (this.getWorkProcessor() != null)
			getWorkProcessor().process(m_unitOfWork);

		event.end();
	}

	/**
	 * returnToManager returns this to Manager
	 * 
	 */
	@Override
	public void returnToManager() throws Exception {
		LOGGER.debug(RETURNING_TO_MANAGER);

		if (m_manager != null)
			m_manager.manageObject(this);
	}

	/**
	 * registerManager Registers a Manager with this
	 * 
	 */
	@Override
	public void registerManager(IManager<WorkerThread> _manager)
			throws Exception {
		LOGGER.debug(String.format("Register manager ... {}", _manager
				.getClass().getName()));
		m_manager = _manager;
	}

	/**
	 * setUnitOfWork Sets a UnitOfWork to be processed
	 * 
	 */
	public void setUnitOfWork(Object _uow) {
		m_unitOfWork = _uow;
	}

	/**
	 * getLastRefresh
	 * 
	 * @return Date last time configure was called
	 */
	public Date getLastRefresh() {
		return m_lastRefresh;
	}
}
