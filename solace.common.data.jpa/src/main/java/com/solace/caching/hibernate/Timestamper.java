/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, Timestamper.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching.hibernate;

import java.util.*;

/**
 * leveraged by the hibernate classes in caching for synchronized timestamps 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 *
 */
class Timestamper {
	
	private static long baseDateMs = 1L;
	
    private static short counter = 0;
    private static long time;
    private final int BinDigits = 12;
    
    public final short OneMs = 1 << BinDigits; //(4096 is the value)
	
	static
	{
		Calendar instance = Calendar.getInstance();
		
		instance.set(1970,0,1);
		
		baseDateMs = instance.getTimeInMillis();
	}
	
	public static synchronized long next()
	{
        return System.currentTimeMillis();
	}
}
