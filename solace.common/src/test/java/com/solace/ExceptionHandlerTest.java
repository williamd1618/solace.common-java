/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, ExceptionHandlerTest.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace;

import java.lang.reflect.Array;
import java.util.*;

import org.junit.*;
import org.jmock.*;
import org.jmock.lib.legacy.ClassImposteriser;

import static org.junit.Assert.*;

import com.solace.*;
import com.solace.logging.Logger;
import com.solace.utility.*;

public class ExceptionHandlerTest {
	static Logger LOGGER = Logger
			.getLogger("com.solace.ExceptionHandlerTest");

	// create the mock context that will factory the proxies
	// note the setImposteriser -- if factorying interfaces this would not need
	// to be set in the anonymous class
	private Mockery context = null;

	public void setupForMock() {
		System.out.println("setupForMock called.");
		context = new Mockery() {
			{
				setImposteriser(ClassImposteriser.INSTANCE);
			}
		};
	}
	
	public ExceptionHandlerTest() {

	}

	@Test
	public void testHandler() throws Exception {
		ExceptionHandlers p = new ExceptionHandlers();
		ExceptionHandlers.Target t = new ExceptionHandlers.Target();
		ExceptionHandlers.Target.Handler h = new ExceptionHandlers.Target.Handler();

		t.setClazz("com.solace.ExceptionHandlerTest");

		h.setException("com.solace.ArgumentException");
		h.setClazz("com.solace.MyExceptionHandler");

		t.getHandler().add(h);

		p.getTarget().add(t);

		ExceptionHandler.addToConfiguration(t);

		for (int i = 0; i < 1; i++)
			try {
				throw new ArgumentException("random exception encountered");
			} catch (Exception e) {
				ExceptionHandler.getInstance(this.getClass()).handle(this, "foo", e, 1,
						2, 3);
				LOGGER.info("testHandler Completed");
			}
	}
	
	@Test	
	public void springInjectedHandler() throws Exception {
		boolean found = false;
		for (int i = 0; i < 1; i++)
			try {
				throw new ArgumentException("random exception encountered", "a","b","c");
			} catch (Exception e) {
				found = true;
				ExceptionHandler.getInstance(this.getClass()).handle(this, "foo", e, 1,
						2, 3);
				LOGGER.info("springInjectedHandler Completed");
			}

		assertEquals(true, found);
	}
	
	@Test	
	public void nestedExceptionTest() throws Exception {
		boolean found = false;
		for (int i = 0; i < 1; i++)
			try {
				throw new ArgumentException("random exception encountered", 
						new ArgumentException("inner exception", 1,2,3),
						"a","b","c");
			} catch (Exception e) {
				found = true;
				ExceptionHandler.getInstance(this.getClass()).handle(this, "foo", e, 1,
						2, 3);
				LOGGER.info("nestedExceptionTest Completed");
			}

		assertEquals(true, found);
	}



	/**
	 * Validates in the programmed example that the default IExceptionHandler
	 * fires after the targeted IExceptionHandler.
	 */
	@Test
	public void validateDefaultFiresAfterTarget() {
		setupForMock();

		// create a mock test that wraps a proxy around the IExceptionHandlers
		final DefaultExceptionHandler def = context
				.mock(DefaultExceptionHandler.class);
		final MyExceptionHandler my = context.mock(MyExceptionHandler.class);

		// creates a sequence to make sure that the invocation occurs in a
		// particular fashion
		// if not the test will fail
		final Sequence calls = context.sequence("calls");

		// create the wiring that would not need to be done in your application
		// merely done for the instrumented example.
		Map<Class<? extends Exception>, IExceptionHandler> defaults = new HashMap<Class<? extends Exception>, IExceptionHandler>();
		defaults.put(java.lang.Exception.class, def);

		Map<Class<?>, IExceptionHandler> registry = new HashMap<Class<?>, IExceptionHandler>();
		registry.put(this.getClass(), my);

		// expectations of a sequence that the concrete is called
		// before the default
		context.checking(new Expectations() {
			{
				// tells jmock that exactly one instance of MyExceptionHandler
				// will fire and the method handle will be invoked
				// with a non-null Object as the sender
				// a non-null string as the message
				// a non-null Exception
				// and any Object... params
				oneOf(my)
						.handle(with(aNonNull(Object.class)),
								with(aNonNull(String.class)),
								with(aNonNull(Exception.class)),
								with(any(Array.class)));
				inSequence(calls);

				// same for the DefaultExceptionHandler
				oneOf(def)
						.handle(with(aNonNull(Object.class)),
								with(aNonNull(String.class)),
								with(aNonNull(Exception.class)),
								with(any(Array.class)));
				inSequence(calls);
			}
		});

		// set the values into the ExceptionHandler class
		// this would not occur in your application
		ExceptionHandler.setRegistry(registry);
		ExceptionHandler.setDefaults(defaults);

		// create the test scenario that will validate the
		// mock tests
		try {
			throw new ArgumentException("foo");
		} catch (Exception e) {
			// invoke the mock test
			ExceptionHandler.getInstance(this.getClass()).handle(this, "foo", e, 1, 2);
			LOGGER.info("validateDefaultFiresAfterTarget Completed");
		}
		
		context = null;
	}

}
