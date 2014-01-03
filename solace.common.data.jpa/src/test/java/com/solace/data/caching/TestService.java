/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, TestService.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.caching;

import java.util.*;

import static org.junit.Assert.*;

import org.springframework.stereotype.*;

import com.solace.logging.*;

import org.springframework.transaction.annotation.*;

@Service(value = "cachedService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
public class TestService {

	static final Logger LOGGER = Logger.getLogger(TestService.class);

	public void createParent() throws Exception {
		Parent parent = new Parent("Some", "Dude", 35, new java.util.Date(1975,
				1, 1), new java.util.Date(1995, 1, 1));

		parent.save();

		long time;

		for (int i = 0; i < 10; i++) {
			time = find(parent.getId());

			LOGGER.debug("{} ms", Long.toString(time));
		}
	}

	public void loadVolume() {
		for (int i = 0; i < 1000; i++) {
			Parent parent = new Parent(Integer.toString(i),
					Integer.toString(i), 35, new java.util.Date(1975, 1, 1),
					new java.util.Date(1995, 1, 1));

			parent.save();
		}
	}

	public void findVolume() throws Exception {
//		for (int i = 0; i < 5; i++) {
//			Thread t = new Thread(new Runnable() {
//				@Override
//				public void run() {
					
					for (int i = 0; i < 1000; i++) {
						long time = find(i);
						LOGGER.debug("{} ms", Long.toString(time));

//						Parent p = DaoManager.getParentDao().findById((long)i);
					}
					
		// }
		// });
		// t.start();
		// t.join();
		// }
	}

	public long find(long id) throws Exception {
		Calendar c = Calendar.getInstance();

		long now = c.getTimeInMillis();

		Parent p = DaoManager.getParentDao().findById(id);

		assertNotNull(p);

		return c.getTimeInMillis() - now;
	}
	
	public void simpleTest() throws Exception {
		
		Person p = new Person("foo","bar",1,new Date(1));
		
		p.save();
		
		Person p2 = DaoManager.getPersonDao().findById(p.getId());
		
		assertEquals(p,p2);	
	}
}
