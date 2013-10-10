/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, WorkRetriever.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.threading;

public abstract class WorkRetriever {
    
	private long m_retrieveCount = 0;

    private static final Object m_synchRoot = new Object();

    public long getRetrieveCount()
    {
        return m_retrieveCount;
    }


    public void resetRetrieveCount()
    {
        synchronized (m_synchRoot)
        {
            m_retrieveCount = 0;
        }
    }

    public Object getWork()
    {
        Object retVal = null;

        synchronized (m_synchRoot)
        {
            retVal = doGetWork();
            m_retrieveCount++;
        }

        return retVal;
    }

    protected abstract Object doGetWork();

}
