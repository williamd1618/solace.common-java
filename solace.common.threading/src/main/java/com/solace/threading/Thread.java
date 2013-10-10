/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, Thread.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.threading;

import com.solace.*;
import com.solace.logging.*;

/**
 * Thread A wrapper class of the java thread class to allow for more control of
 * startup and shutdown in derived classes
 * 
 * @author dwilliams
 * 
 */
public class Thread implements java.lang.Runnable {

	private static final String STOPPING_THREAD_D_OF_TYPE_S = "Stopping thread: {} of type: {}";

	private static final String THREAD_D_OF_TYPE_S_HAS_STOPPED = "Thread: {} of type: {} has stopped";

	private static final String THREAD_STATE_IS_S = "Thread state is {}";

	private static final String RUN = "run";

	private static final Logger LOGGER = Logger.getLogger(Thread.class);

	// running
	private boolean m_running = false;

	// composition thread
	private java.lang.Thread m_thread = null;

	// input implementation
	private Runnable m_runnable = null;

	protected ILifecycle m_lifecycle = null;

	/**
	 * Thread default constructor
	 */
	public Thread() {
		this(null, null);
	}

	/**
	 * Thread Take in a Runnable to execute
	 * 
	 * @param _runnable
	 */
	public Thread(Runnable _runnable) {
		this(_runnable, null);
	}

	public Thread(Runnable _runnable, ILifecycle _lifecycle) {
		m_runnable = _runnable;
		m_lifecycle = _lifecycle;

		m_thread = new java.lang.Thread(this);
	}

	/**
	 * start Starts the composed thread.
	 * 
	 * @see java.lang.Thread#start()
	 */
	public final void start() {
		Logger.EventLogger event = LOGGER.begin("start");

		synchronized (this) {
			m_running = true;

			if (m_lifecycle != null) {
				LOGGER.info(String.format("Thread started with Lifecyle: {}",
						m_lifecycle.getClass().getName()));
				m_lifecycle.begin();
			}

			m_thread.start();
		}

		event.end();
	}

	/**
	 * stop stops the thread and spins until it is a wait state. This is an
	 * indicator that all work is done and it can shutdown safely leaving data
	 * in a sane condition. Once in a wait state the doStop method is called for
	 * any thread specific releases. Finally we notify ourself to come out of
	 * the wait.
	 * 
	 * @throws Exception
	 */
	public final void stop() throws Exception {
		Logger.EventLogger event = LOGGER.begin("stop");

		LOGGER.debug(STOPPING_THREAD_D_OF_TYPE_S, getId(),
				getClass().getName());

		m_running = false;

		// if we're not in a waiting state of some sort yet
		// let's spin until we are
		while (getState() != java.lang.Thread.State.WAITING
				&& getState() != java.lang.Thread.State.TIMED_WAITING
				&& getState() != java.lang.Thread.State.TERMINATED)
			sleep(100);

		if (m_lifecycle != null)
			m_lifecycle.end();

		doStop();

		synchronized (this) {
			notify();
		}

		LOGGER.debug(THREAD_D_OF_TYPE_S_HAS_STOPPED, getId(),
				getClass().getName());

		event.end();
	}

	/**
	 * run can be overriden from a derived thread. if a Runnable is passed in
	 * then that method is called.
	 */
	public void run() {
		Logger.EventLogger event = LOGGER.begin(RUN);

		try {
			if (m_runnable != null)
				m_runnable.run();
		} catch (Exception e) {
			// getExceptionHandler().handleException(e);
		} finally {
			event.end();
		}
	}

	/**
	 * doStop Can be overridden by developers for business specific shutdown
	 * code
	 * 
	 * @throws Exception
	 */
	protected void doStop() throws Exception {
	}

	/**
	 * isRunning returns a boolean state variable
	 * 
	 * @return a state variable
	 */
	public final synchronized boolean isRunning() {
		return m_running;

	}

	/**
	 * wrapper for sleep
	 * 
	 * @param _duration
	 *            duration to sleep
	 * @throws Exception
	 * 
	 */
	public final static void sleep(long _duration) throws InterruptedException {
		java.lang.Thread.sleep(_duration);
	}

	/**
	 * join Wrapper for the join method.
	 * 
	 * @see java.lang.Thread#join()
	 */
	public final void join() {
		this.join();
	}

	/**
	 * getState wrapper method to return state
	 * 
	 * @return state of internal thread.
	 */
	public final java.lang.Thread.State getState() {
		// if a thread has not started it is in this state
		java.lang.Thread.State state = java.lang.Thread.State.NEW;

		if (m_thread != null)
			state = m_thread.getState();

		LOGGER.debug(THREAD_STATE_IS_S, state);

		return state;
	}

	/**
	 * setName Wrapper method to set the name of the thread
	 * 
	 * @param _name
	 *            name of the thread
	 */
	public final void setName(String _name) {
		m_thread.setName(_name);
	}

	/**
	 * getName Wrapper method to get the name of the thread
	 * 
	 * @return name of thread
	 */
	public final String getName() {
		return m_thread.getName();
	}

	/**
	 * getId Wrapper method to return the long system identifier of the thread
	 * 
	 * @return id of the thread
	 */
	public final long getId() {
		return m_thread.getId();
	}

	/**
	 * finalized getter for ILifecycle to be invoked on a thread
	 * 
	 * @return an instance of the thread lifecycle
	 */
	protected final ILifecycle getLifecycle() {
		return this.m_lifecycle;
	}

	/**
	 * finalized setter for ILifecycle to be invoked on a thread
	 * 
	 * @param _lifecycle
	 */
	protected final void setLifecycle(ILifecycle _lifecycle) {
		this.m_lifecycle = _lifecycle;
	}
}
