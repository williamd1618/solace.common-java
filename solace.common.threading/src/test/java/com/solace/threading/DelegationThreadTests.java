/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, DelegationThreadTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.threading;

import java.io.File;
import java.util.Calendar;

import org.junit.*;
import static org.junit.Assert.*;

import com.solace.logging.*;
import com.solace.threading.DelegationThread;
import com.solace.threading.IWorkProcessor;
import com.solace.threading.Thread;
import com.solace.threading.WorkRetriever;
import com.solace.threading.WorkerThread;
import com.solace.utility.*;

public class DelegationThreadTests {

	private static final Logger LOGGER = Logger
			.getLogger(DelegationThreadTests.class);

	public class MyWorkRetriever extends WorkRetriever {
		@Override
		protected Object doGetWork() {
			return new Object() {
				// overridden default ctor
				// of an anonymous inner class
				{
				}

				public String toString() {
					return "foo";
				}
			};
		}
	}

	@Test
	public void start() throws Exception {

		try {
			DelegationThread<WorkerThread> delegator = new DelegationThread<WorkerThread>(
					new MyWorkRetriever(), null, null);

			delegator.addWorkerThread(new WorkerThread(null,
					new IWorkProcessor() {
						@Override
						public void process(Object obj) {
							try {
								System.out.println((obj == null) ? "null" : obj
										.toString());
								Thread.sleep(10);
							} catch (Exception e) {
							}
						}
					}));
			// delegator.addWorkerThread(new WorkerThread());

			delegator.start();

			// sleep for 10 seconds
			Thread.sleep(10000);

			// make sure its state is running
			if (!delegator.isRunning())
				fail("delegator failed to start");

			// fire stop, this should not return
			// until all threads have completed
			delegator.stop();

			// no need for this
			// delegator.join();

			if (delegator.isRunning())
				fail("delegator is still running and should have stopped by now.");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
}
