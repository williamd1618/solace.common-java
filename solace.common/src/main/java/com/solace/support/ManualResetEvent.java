package com.solace.support;

/**
 * Will be used to trigger events in ServerThreads
 * 
 * @author <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>
 * 
 */
public class ManualResetEvent {
	private final Object monitor = new Object();
	private volatile boolean open = false;

	public ManualResetEvent(boolean open) {
		this.open = open;
	}

	public void waitOne() throws InterruptedException {
		synchronized (monitor) {
			while (open == false) {
				monitor.wait();
			}
		}
	}

	public boolean waitOne(long millisecondsTimeout, boolean exitContext)
			throws InterruptedException {
		synchronized (monitor) {
			
			// if the open is initialized to false then !false will return !exitContext
			// e.g. if set() has not been called and exitContext is true then we return false ow. true
			// we want to wait on exitContext
			boolean wait = (!open)?!exitContext : exitContext;
			
			if ( wait == !exitContext)
				monitor.wait(millisecondsTimeout);
			
			return wait;
		}
	}

	public void set() {// open start
		synchronized (monitor) {
			open = true;
			monitor.notifyAll();
		}
	}

	public void reset(boolean value) {// close stop
		open = value;
	}
}
