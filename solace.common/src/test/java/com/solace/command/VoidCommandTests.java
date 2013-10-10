/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, VoidCommandTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.command;

import org.junit.*;

import com.solace.CommonException;
import com.solace.command.Command;

public class VoidCommandTests {
	
	public class VoidCommand extends Command<Void> {

		public VoidCommand(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Void doExecute(Object... args) throws CommonException {
			for(Object obj : args)
				System.out.println(obj);
			
			return null;
		}

		@Override
		protected boolean validate(Object... args) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
	
	public class ReturnCommand extends Command<Integer> {

		public ReturnCommand(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected Integer doExecute(Object... args) throws CommonException {
			return 1;
		}

		@Override
		protected boolean validate(Object... args) {
			return true;
		}
		
	}
	
	@Test
	public void voidTest() throws CommonException
	{
		VoidCommand cmd = new VoidCommand("foo");
		
		cmd.execute("foo","bar");
		
		int i = (new ReturnCommand("foo")).execute();
		
		System.out.println(i);
		
		Assert.assertEquals(true, true);
	}	
}
