/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.caching, and file, JaxbTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.caching;

import org.junit.*;

import java.io.*;
import java.util.List;

import javax.xml.bind.*;
import javax.xml.datatype.*;

import com.solace.caching.*;


public class JaxbTests {
	
//	@Test
	public void creationTest() throws Exception {
		try
		{
			Caches caches = new Caches();
			
			Caches.CacheConfig.Type t = new Caches.CacheConfig.Type();
			t.setValue("foo");			
			
			Caches.CacheConfig c = new Caches.CacheConfig();
			c.setName("test");
			c.setType(t);
			
			caches.getCache().add(c);
						
			JAXBContext jc = JAXBContext.newInstance(com.solace.caching.Caches.class);
			
			StringWriter writer = new StringWriter();
			
			Marshaller m = jc.createMarshaller();
			
			m.marshal(caches, writer);	
			
			String val = writer.toString();
			
			System.out.println(val);
		}
		catch ( Exception e)
		{
			Assert.fail(e.getMessage());
		}
	}
//
//	@Test
//	@Ignore
	public void marshalTest() throws Exception {
		
		String xmlInput = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><caches xmlns=\"http://www.example.org/caches\"><cache name=\"test\"><type value=\"foo\"/></cache></caches>";
		try {

			JAXBContext jc = JAXBContext.newInstance(com.solace.caching.Caches.class);
			
			Unmarshaller u = jc.createUnmarshaller();				
			
			Caches c = (Caches)u.unmarshal(
					new ByteArrayInputStream(xmlInput.getBytes()));
			
			Assert.assertEquals("equals", "test", c.getCache().get(0).getName());
			Assert.assertEquals("equals", "foo", c.getCache().get(0).getType().getValue());						
		} 
		catch ( Exception e )
		{
			Assert.fail(e.getMessage());
		}

	}
	
//	@Test
//	@Ignore
	public void withProperties() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<caches xmlns=\"http://www.example.org/caches\">");
		sb.append("<cache name=\"test\">");
		sb.append("<type value=\"foo\">");
		sb.append("<properties>");
		sb.append("<property name=\"name\" value=\"value\"/>");
		sb.append("</properties>");
		sb.append("</type>");
		sb.append("</cache>");
		sb.append("</caches>");
		
		try
		{
			JAXBContext jc = JAXBContext.newInstance(com.solace.caching.Caches.class);
			
			Unmarshaller u = jc.createUnmarshaller();				
			
			Caches c = (Caches)u.unmarshal(
					new ByteArrayInputStream(sb.toString().getBytes()));
			
			Assert.assertEquals("properties did not serialize", "name",
					c.getCache().get(0).getType().getProperty().get(0).getName());
		}
		catch ( Exception e )
		{
			Assert.fail(e.getMessage());
		}
	}

}
