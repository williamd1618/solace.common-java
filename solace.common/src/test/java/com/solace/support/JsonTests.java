/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, JsonTests.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.support;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import com.google.gson.Gson;
import com.solace.support.json.Json;

import static org.junit.Assert.*;

public class JsonTests {
	
	
	@Test
	public void outTest() {
		TestClass p = new TestClass("foo");

		List<ChildClass> c = new ArrayList<ChildClass>();

		c.add(new ChildClass("foo"));

		c.add(new ChildClass("bar"));

		p.setChildren(c);

		Gson g = new Gson();

		String json = g.toJson(p);

		assertTrue(json != null && json.length() > 0);

		System.out.println(json);
	}

	@Test
	public void collectionTest() {

		TestClass p = new TestClass("foo");

		List<ChildClass> c = new ArrayList<ChildClass>();

		c.add(new ChildClass("foo"));

		c.add(new ChildClass("bar"));

		p.setChildren(c);

		TestClass p1 = new TestClass("foo");

		List<ChildClass> c1 = new ArrayList<ChildClass>();

		c1.add(new ChildClass("foo"));

		c1.add(new ChildClass("bar"));

		p1.setChildren(c);

		List<TestClass> tests = new ArrayList<TestClass>();
		tests.add(p);
		tests.add(p1);

		Gson g = new Gson();

		String json = g.toJson(tests);

		assertTrue(json != null && json.length() > 0);

		System.out.println(json);
	}

	@Test
	public void listTest() {
		TestClass p = new TestClass("foo");

		List<ChildClass> c = new ArrayList<ChildClass>();

		c.add(new ChildClass("foo"));

		c.add(new ChildClass("bar"));

		p.setChildren(c);

		TestClass p1 = new TestClass("foo");

		List<ChildClass> c1 = new ArrayList<ChildClass>();

		c1.add(new ChildClass("foo"));

		c1.add(new ChildClass("bar"));

		p1.setChildren(c);

		List<TestClass> tests = new ArrayList<TestClass>();
		tests.add(p);
		tests.add(p1);

		System.out.println(Json.toJson(tests));
	}

	@Test
	public void arrayTest() {
		TestClass p = new TestClass("foo");

		List<ChildClass> c = new ArrayList<ChildClass>();

		c.add(new ChildClass("foo"));

		c.add(new ChildClass("bar"));

		p.setChildren(c);

		System.out.println(Json.toJsonArray(p));
	}

	@Test
	public void fromJson() {
		String json = "{\"value\":\"foo\",\"children\":[{\"value\":\"foo\"},{\"value\":\"bar\"}]}";

		TestClass p = Json.<TestClass> fromJson(json, TestClass.class);

		assert (p.getValue().equals("foo"));

		assert (p.getChildren().get(0).getValue().equals("foo"));
	}

	@Test
	public void fromJsonArray() {
		try {
			String json = "[{\"value\":\"foo\",\"children\":[{\"value\":\"foo\"},{\"value\":\"bar\"}]},{\"value\":\"foo\",\"children\":[{\"value\":\"foo\"},{\"value\":\"bar\"}]}]";

			List<TestClass> p = Json.<TestClass> fromJsonArray(json,
					TestClass.class);

			assert (p.size() == 2);
			assert (p.get(0).getValue().equals(p.get(1).getValue()));
			assert (p.get(0).getChildren().get(0).getValue().equals(p.get(1)
					.getChildren().get(0).getValue()));
			assert (p.get(0).getChildren().size() == p.get(1).getChildren().size());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
