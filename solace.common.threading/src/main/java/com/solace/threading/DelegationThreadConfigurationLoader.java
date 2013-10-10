/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.threading, and file, DelegationThreadConfigurationLoader.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.threading;

public class DelegationThreadConfigurationLoader implements IConfigurationLoader {
	
	public DelegationThreadConfigurationLoader() {
		
	}

	@Override
	public void Load(DelegationThread<? extends WorkerThread> thread) {
		// TODO Auto-generated method stub
		
	}
	
	// C# implementation
//    /// <summary>
//    /// The DelegationThreadConfigurationLoader class is responsible for loading the properties associated
//    /// with the DelegationThread from the ConfigurationManager
//    /// </summary>
//    public class DelegationThreadConfigurationLoader : IConfigurationLoader
//    {
//        private static readonly ILog LOGGER = LogManager.GetLogger(typeof(DelegationThreadConfigurationLoader));
//
//        public const string DELEGATOR_SLEEP     = "DelegatorSleepInterval";
//        public const string DELEGATOR_PERIODIC  = "DelegatorPeriodicWorkInterval";
//        public const string NO_UNIT_OF_WORK     = "NoUnitOfWorkSleepIntersval";
//
//        #region IConfigurationLoader Members
//
//        /// <summary>
//        /// The Load method loads specific properties for the _thread
//        /// </summary>
//        /// <param name="_thread">an instance of a DelegationThread</param>
//        public virtual void Load(DelegationThread _thread)
//        {
//            string value = string.Empty;
//
//            if (LoadValue(DELEGATOR_SLEEP, ref value))
//                _thread.SleepInterval = long.Parse(value);
//
//            if (LoadValue(DELEGATOR_PERIODIC, ref value))
//                _thread.PeriodicInterval = long.Parse(value);
//
//            if (LoadValue(NO_UNIT_OF_WORK, ref value))
//                _thread.NoUnitOfWorkSleepInterval = long.Parse(value);
//        }
//
//        private bool LoadValue(string _key, ref string _value)
//        {
//            bool retVal = true;
//            
//            string value = string.Empty;
//
//            if (null == (value = ConfigurationManager.AppSettings[_key]))
//            {
//                retVal = false;
//                LOGGER.InfoFormat("No configuration option for key '{0}' found.", _key);
//            }
//            else
//            {
//                _value = value;
//                LOGGER.InfoFormat("Configuration for key: '{0}' returned a value of: '{1}'", _key, _value);
//            }
//            
//            return retVal;
//        }
//
//
//        #endregion
//    }
}
