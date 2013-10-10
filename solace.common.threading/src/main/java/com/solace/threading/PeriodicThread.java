/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, PeriodicThread.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.threading;

import java.util.*;

import com.solace.logging.*;

/**
 * PeriodThread Does periodic work on some interval.
 * 
 * @author Daniel Williams (dwilliams@9squared.com)
 * 
 */
public abstract class PeriodicThread extends com.solace.threading.Thread {

	private static final String PROCESS = "process";

	private static final String RUN = "run";

	private static final String CREATING_PERIODIC_THREAD_WITH_PERIODICITY_D_MS_NEXT_RUN_S = "Creating PeriodicThread with Periodicity: {} ms, NextRun: {}.";

	private static final Logger LOGGER = Logger.getLogger(PeriodicThread.class);

	// periodicity in ms
	protected long m_periodicity = 1000;

	public long getPeriodicity() {
		return this.m_periodicity;
	}

	// targeted Date/Time to run
	protected Date m_nextRun = new Date();

	public Date getNextRunTime() {
		return this.m_nextRun;
	}

	/**
	 * PeriodicThread Default Constructor
	 */
	public PeriodicThread() {
		super();

		LOGGER
				.info(
						CREATING_PERIODIC_THREAD_WITH_PERIODICITY_D_MS_NEXT_RUN_S,
						m_periodicity, m_nextRun.toString());
	}

	/**
	 * PeriodicThread
	 * 
	 * @param _periodicity
	 *            periodicity
	 */
	public PeriodicThread(long _periodicity) {
		this();
		m_periodicity = _periodicity;
	}

	/**
	 * PeriodicThread
	 * 
	 * @param _nextRun
	 *            Date to start processing
	 */
	public PeriodicThread(Date _nextRun) {
		this();
		m_nextRun = _nextRun;
	}

	public PeriodicThread(long _periodicity, ILifecycle _lifecycle,
			Date _runTime) {
		this(_runTime);

		this.m_lifecycle = _lifecycle;
		this.m_periodicity = _periodicity;
	}

	/**
	 * run overriden run method from Thread. Will not start until m_nextRun if
	 * the value is great than now(). If otherwise will start immediately.
	 * m_nextRun can be recalculated by the derived implementation to allow it
	 * to run at a given time. However, if not, then it executes by its
	 * periodicity.
	 * 
	 */
	@Override
	public final void run() {
		Logger.EventLogger event = LOGGER.begin(RUN);

		try {

			if ((new Date()).compareTo(m_nextRun) < 0) {
				synchronized (this) {
					wait(m_nextRun.getTime() - (new Date()).getTime());
				}
			}

			while (isRunning()) {
				process();

				if ((new Date()).compareTo(m_nextRun) < 0) {
					synchronized (this) {
						wait(m_nextRun.getTime() - (new Date()).getTime());
					}
				} else {
					synchronized (this) {
						wait(m_periodicity);
					}
				}
			}
		} catch (Exception e) {
//			getExceptionHandler().handleException(e);
		} finally {
			event.end();
		}
	}

	/**
	 * process Calls business logic in doProcess
	 * 
	 */
	public final void process() {
		Logger.EventLogger event = LOGGER.begin(PROCESS);
		doProcess();
		event.end();
	}

	/**
	 * doProcess Business implementation Should recalculate periodicity and/or
	 * m_nextRun
	 */
	public abstract void doProcess();
}
