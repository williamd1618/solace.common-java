/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, PeriodicDelegationThreadTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package com.solace.threading;


import java.util.Calendar;
import java.util.Date;

import org.junit.*;
import static org.junit.Assert.*;

import com.solace.threading.*;

/**
 * @author dwilliams
 *
 */
public class PeriodicDelegationThreadTests {

	@org.junit.Test
	@SuppressWarnings("unchecked")
	public void testDate() throws Exception {
		Date d, d2;
		
		d = new Date();
		d2 = (Date) d.clone();
		
		Assert.assertEquals(d, d2);
				
		PeriodicityCalculator c = new PeriodicityCalculator();
		
		if ( c.isValidExecutionWindow(d) )	
			assertTrue(d.getTime() > d2.getTime());
						
	}
	
	
	
	public class PeriodicityCalculator implements IPeriodicityCalculator
	{

		@Override
		public boolean isValidExecutionWindow(Date nextExecutionWindow) {
			nextExecutionWindow.setMinutes(
					nextExecutionWindow.getMinutes() + 5);
			
			return true;
			
		}
		
	}
}
