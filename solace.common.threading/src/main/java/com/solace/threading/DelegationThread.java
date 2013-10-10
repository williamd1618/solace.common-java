/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, DelegationThread.java, and the accompanying materials
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

import java.util.concurrent.*;

/**
 * DelegationThread DelegationThread serves as the controller for a pool of
 * WorkerThreads. Most methods throw Exception so that the developer can
 * implement their own exception handling if they prefer. The Runnable interface
 * does not throw Exception so we need to handle it via handleException.
 * <p>
 * I've removed Configuration implementation and signaling during periodic work
 * and will leave it up to the implementer to handle using synchronized access
 * to its WorkerTheads using setters and/or utility methods
 * <p>
 * Note:All WorkerThreads must be added to the DelegationThread after the call
 * to start
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com>Daniel Williams</a>
 * 
 */
public class DelegationThread<T extends WorkerThread> extends
		com.solace.threading.Thread implements IManager<T> {

	private static final String STOPPING_DELEGATION_THREAD_AND_WORKER_THREADS = "Stopping DelegationThread and WorkerThreads ...";

	private static final String DO_STOP = "doStop";

	private static final String MANAGE_S = "Manage: {}";

	private static final String PERIODIC_WORK = "periodicWork";

	private static final String UN_INIT = "unInit";

	private static final String POPULATING_CONFIGURATION_OPTIONS = "Populating configuration options ....";

	private static final String POPULATE_CONFIGURAITON = "populateConfiguraiton";

	private static final String THE_WORK_RETRIEVER_IS_CURRENTLY_UNASSIGNED_PLEASE_MAKE_SURE_THAT_YOU_HAVE_OVERRIDE_THE_DO_INIT_METHOD = "The WorkRetriever is currently unassigned.  Please make sure that you have override the DoInit method.";

	private static final String GET_UNIT_OF_WORK = "getUnitOfWork";

	private static final String ADD_WORKER_THREAD = "addWorkerThread";

	private static final String WORKER_THREAD_WAS_NOT_IN_A_WAIT_STATE_RETURNING_AND_WAITING = "WorkerThread was not in a wait state, returning and waiting ...";

	private static final String SET_UNIT_OF_WORK_AND_SIGNALLING_THREAD_D = "Set unit of work and signalling thread {}";

	private static final String NO_UNIT_OF_WORK_WAITING = "No unit of work ... waiting.";

	private static final String UNHANDLED_EXCEPTION_S = "Unhandled exception: {}";

	private static final String SIGNALLING_D = "Signalling: {}";

	private static final String NO_WAITING_THREADS_SLEEPING_FOR_D_MS = "No waiting threads ... sleeping for {} ms";

	private static final String RUN = "run";

	private static final String NULL = "null";

	private static final String CONFIGURATION_LOADER_S = "ConfigurationLoader: {}";

	private static final String PERIODIC_WORK_HANDLER_S = "PeriodicWorkHandler: {}";

	private static final String WORK_RETRIEVER_S = "WorkRetriever: {}";

	private static Logger LOGGER = Logger.getLogger(DelegationThread.class);

	// holds waiting threads
	protected Queue<T> m_waitingThreads = null;

	// holds active threads
	protected Queue<T> m_activeThreads = null;

	// used to control whether or not a periodic update is being undergone
	private boolean m_periodicUpdating = false;

	// Configuration for systemv
	// protected Configuration m_configuration = null;

	private Date m_periodicTime = null;

	/**
	 * Returns the time of the last time the
	 * 
	 * @return the date of the last periodic occurrence
	 */
	protected Date getLastPeriodicOccurrence() {
		return m_periodicTime;
	}

	protected IPeriodicWorkHandler m_periodicWorkHandler = null;

	protected IPeriodicWorkHandler getPeriodicWorkHandler() {
		return m_periodicWorkHandler;
	}

	protected void setPeriodicWorkHandler(IPeriodicWorkHandler _handler) {
		m_periodicWorkHandler = _handler;
	}

	protected WorkRetriever m_workRetriever = null;

	protected WorkRetriever getWorkRetriever() {
		return this.m_workRetriever;
	}

	protected void setWorkRetriever(WorkRetriever _retriever) {
		this.m_workRetriever = _retriever;
	}

	protected IConfigurationLoader m_loader = null;

	protected IConfigurationLoader getConfigurationLoader() {
		return this.m_loader;
	}

	protected void setConfigurationLoader(IConfigurationLoader _loader) {
		this.m_loader = _loader;
	}

	/**
	 * The sleep interval in ms if no worker threads are available
	 */
	protected long m_sleepInterval = 10;

	/**
	 * The sleep interval in ms if no worker threads are available. Default is
	 * 10 ms.
	 */
	public long getSleepInterval() {
		return this.m_sleepInterval;
	}

	public void setSleepInterval(long _interval) {
		this.m_sleepInterval = _interval;
	}

	/**
	 * How often PeriodicWork is called in ms.
	 * 
	 */
	protected long m_periodicInterval = 3600000;

	/**
	 * How often PeriodicWork is called in ms. default is 60 minutes.
	 */
	public long getPeriodicInterval() {
		return this.m_periodicInterval;
	}

	public void setPeriodicInterval(long _interval) {
		this.m_periodicInterval = _interval;
	}

	/**
	 * How long to sleep if no units of work are found in ms.
	 */
	protected long m_noUoWSleepInterval = 300000;

	/**
	 * How long to sleep if no units of work are found in ms. Default is 5
	 * minutes.
	 * 
	 * @return a long representing interval in ms
	 */
	public long getNoUnitOfWorkSleepInterval() {
		return this.m_noUoWSleepInterval;
	}

	public void setNoUnitOfWorkSleepInterval(long _interval) {
		this.m_noUoWSleepInterval = _interval;
	}

	/**
	 * A Setter for WorkerThreads primarily for Configuration system public
	 * IList<WorkerThread> WorkerThreads
	 */
	public void addWorkerThreads(List<T> _threads) throws Exception {
		for (T thread : _threads)
			this.addWorkerThread(thread);
	}

	/**
	 * DelegationThread Default constructor. Initializes queues and sets
	 * m_periodicTime to now
	 */
	public DelegationThread() {
		this(null, null, null);
	}

	public DelegationThread(WorkRetriever _workRetriever,
			IPeriodicWorkHandler _periodicHandler, ILifecycle _lifecycle) {
		this(_workRetriever, _periodicHandler, _lifecycle,
				new DelegationThreadConfigurationLoader());
	}

	public DelegationThread(WorkRetriever _retriever,
			IPeriodicWorkHandler _handler, ILifecycle _lifecycle,
			IConfigurationLoader _configLoader) {

		super(null, _lifecycle);

		setWorkRetriever(_retriever);
		LOGGER.info(String.format(WORK_RETRIEVER_S,
				(getWorkRetriever() != null) ? getWorkRetriever().getClass()
						.toString() : NULL));

		setPeriodicWorkHandler(_handler);
		LOGGER.info(String.format(PERIODIC_WORK_HANDLER_S, (this
				.getPeriodicWorkHandler() != null) ? this
				.getPeriodicWorkHandler().getClass().toString() : NULL));

		// getLifecycle is exposed from Thread
		LOGGER.info(String.format("Lifecycle: {}",
				(getLifecycle() != null) ? getLifecycle().getClass().toString()
						: NULL));

		this.setConfigurationLoader(_configLoader);
		LOGGER.info(String.format(CONFIGURATION_LOADER_S,
				(getConfigurationLoader() != null) ? getConfigurationLoader()
						.getClass().toString() : NULL));

		m_activeThreads = new ConcurrentLinkedQueue<T>();

		m_waitingThreads = new ConcurrentLinkedQueue<T>();

		m_periodicTime = new Date();
	}

	/**
	 * run main thread execution cycle that delegates UnitsOfWork
	 * 
	 */
	@Override
	public final void run() {

		Logger.EventLogger event = LOGGER.begin(RUN);

		try {

			// initialization could potentially throw an Exception
			// during call to doPeriodic
			init();

			Object uow = null;

			while (isRunning()) {

				// deterime duration since last periodic call
				long sinceLastPeriodicUpdate = (new Date()).getTime()
						- m_periodicTime.getTime();

				// m_periodicInterval is in minutes -- convert to seconds
				long secondsBetweenPeriodicUpdate = m_periodicInterval * 60;

				// check to see if periodic work must be done
				if (sinceLastPeriodicUpdate > secondsBetweenPeriodicUpdate)
					periodicWork();

				T thread = null;

				// attempt to get a thread
				if ((thread = m_waitingThreads.poll()) == null) {
					// if not, wait some interval or be signalled
					synchronized (this) {
						LOGGER.info(NO_WAITING_THREADS_SLEEPING_FOR_D_MS,
								m_sleepInterval);
						wait(m_sleepInterval);
					}
				} else {
					// if the thread is running and its in a wait state
					// get a UnitOfWork
					if (thread.isRunning()
							&& thread.getState() == java.lang.Thread.State.WAITING) {
						LOGGER.info(SIGNALLING_D, thread.getId());

						try {
							uow = getUnitOfWork();
						} catch (Exception e) {
							LOGGER.error(UNHANDLED_EXCEPTION_S, e, e
									.getMessage());
							uow = null;
						}

						// if no UnitOfWork returned sleep m_noUoWSleepInterval
						if (uow == null) {
							synchronized (this) {
								LOGGER.debug(NO_UNIT_OF_WORK_WAITING);
								wait(m_noUoWSleepInterval);
							}
						}

						// we may set null and if we so we simply
						// cycle the thread back into a waiting state
						thread.setUnitOfWork(uow);

						// signal the thread to come out of wait
						synchronized (thread) {
							LOGGER.debug(
									SET_UNIT_OF_WORK_AND_SIGNALLING_THREAD_D,
									thread.getId());
							thread.notify();
						}
					}
					// if the thread is running but not in a wait state
					// we add it back to waitingThreads
					// and wait
					else if (thread.isRunning()) {
						m_waitingThreads.add(thread);

						// lock this so we can be signalled
						synchronized (this) {
							LOGGER
									.debug(WORKER_THREAD_WAS_NOT_IN_A_WAIT_STATE_RETURNING_AND_WAITING);
							wait(m_sleepInterval);
						}
					}
					// if a boundary condition is set to be killed while it is
					// in a
					// wait state
					// then we simply signal it to die and circle back for the
					// next
					else {
						synchronized (thread) {
							thread.notify();
						}
					}
				}
			}
		} catch (Exception e) {
			// getExceptionHandler().handleException(e);
		}

		event.end();
	}

	/**
	 * addWorkerThread Adds a WorkerThread to the pools, register manager,
	 * configures, and starts
	 * 
	 * @param _thread
	 */
	@SuppressWarnings("unchecked")
	public final void addWorkerThread(T _thread) throws Exception {
		Logger.EventLogger event = LOGGER.begin(ADD_WORKER_THREAD);

		synchronized (m_waitingThreads) {
			synchronized (m_activeThreads) {
				m_waitingThreads.add(_thread);
				m_activeThreads.add(_thread);
			}
		}

		_thread.registerManager((IManager<WorkerThread>) this);

		_thread.start();

		event.end();
	}

	/**
	 * Will poll for a thread until one is retrieved and can be removed
	 */
	public void removeWorkerThread() throws Exception {
		Thread thread = null;

		while (thread == null) {
			thread = m_waitingThreads.poll();

			if (thread == null)
				sleep(this.m_sleepInterval);
			else
				m_activeThreads.remove(thread);
		}
	}

	/**
	 * getUnitOfWork Must be implemented by developer
	 * 
	 * @return returns a UnitOfWork
	 */
	protected Object getUnitOfWork() {
		Logger.EventLogger event = LOGGER.begin(GET_UNIT_OF_WORK);

		Object retVal = null;

		try {
			WorkRetriever retriever;
			if ((retriever = getWorkRetriever()) != null)
				retVal = retriever.getWork();
		} catch (Exception ex) {
			LOGGER.error(UNHANDLED_EXCEPTION_S, ex, ex.getMessage());
		}

		event.end();

		return retVal;
	}

	/**
	 * init Calls user defined doInit and periodicWork
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		Logger.EventLogger event = LOGGER.begin("init");

		if (getWorkRetriever() == null)
			LOGGER
					.info(THE_WORK_RETRIEVER_IS_CURRENTLY_UNASSIGNED_PLEASE_MAKE_SURE_THAT_YOU_HAVE_OVERRIDE_THE_DO_INIT_METHOD);

		populateConfiguration();
		doInit();
		periodicWork();

		event.end();
	}

	/**
	 * Invokes the loading of configuration in whatever fashion it may be
	 */
	protected void populateConfiguration() {
		Logger.EventLogger event = LOGGER.begin(POPULATE_CONFIGURAITON);

		LOGGER.info(POPULATING_CONFIGURATION_OPTIONS);

		IConfigurationLoader loader;

		if ((loader = getConfigurationLoader()) != null)
			loader.Load(this);

		event.end();
	}

	/**
	 * doInit Can be overriden by developer for business specific initialization
	 * (e.g. Connections)
	 * 
	 * @throws Exception
	 */
	public void doInit() throws Exception {
	}

	/**
	 * unInit Calls develoepr defined unInit
	 */
	private final void unInit() throws Exception {
		Logger.EventLogger event = LOGGER.begin(UN_INIT);

		doUnInit();

		event.end();
	}

	/**
	 * doUnInit Developer specific implementation fo shutdown (e.g closing
	 * connections)
	 * 
	 * @throws Exception
	 */
	protected void doUnInit() throws Exception {
	}

	/**
	 * periodicWork Does periodicWork and monitors when it was last done Calls
	 * user define doPeriodicWork
	 * 
	 * @throws Exception
	 */
	private void periodicWork() throws Exception {
		Logger.EventLogger event = LOGGER.begin(PERIODIC_WORK);

		synchronized (this) {
			m_periodicUpdating = true;

			doPeriodicWork();

			m_periodicUpdating = false;
		}

		// ensure that at least one millisecond has passed
		// so the periodicTime stamp will be different
		// update the periodic time for the next interval
		m_periodicTime = new Date();

		sleep(1);

		event.end();
	}

	/**
	 * doPeriodicWork Developer defined periodic work (e.g. retrieving
	 * configurations)
	 * 
	 * @throws Exception
	 */
	public void doPeriodicWork() throws Exception {
	}

	/**
	 * manage
	 * 
	 */
	@Override
	public void manageObject(T _thread) throws Exception {
		LOGGER.debug(MANAGE_S, _thread.getId());

		// long sinceLastRefresh = (new Date()).getTime()
		// - _thread.getLastRefresh().getTime();

		// if there is a configuration and we're not periodic updating
		// and we have expired our periodic update interval
		// then reconfigure thread
		// if (m_configuration != null && !m_periodicUpdating
		// && sinceLastRefresh > m_periodicInterval * 60)
		// _thread.configure(m_configuration);

		if (!m_waitingThreads.contains(_thread))
			m_waitingThreads.add(_thread);
	}

	/**
	 * doStop implementation for DelegationThread
	 */
	@Override
	protected void doStop() throws Exception {
		Logger.EventLogger event = LOGGER.begin(DO_STOP);

		LOGGER.info(STOPPING_DELEGATION_THREAD_AND_WORKER_THREADS);

		for (Thread thread : m_activeThreads) {
			synchronized (thread) {
				thread.stop();
			}
		}

		m_activeThreads.clear();
		m_waitingThreads.clear();

		// if I'm in a waiting state wake it back up so it dies
		if (getState() == java.lang.Thread.State.WAITING
				|| getState() == java.lang.Thread.State.TIMED_WAITING) {
			synchronized (this) {
				this.notify();
			}
		}

		unInit();

		event.end();
	}
}
