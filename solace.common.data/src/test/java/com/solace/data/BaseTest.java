/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, BaseTest.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;

import org.junit.After;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.solace.ArgumentException;
import com.solace.logging.*;

public class BaseTest {

	static final Logger LOGGER = Logger.getLogger(BaseTest.class);

	private Connection cnxn;

	public static final String DAO_FACTORY_CONFIG_FILE = "daofactories.xml";

	public static final String INPUT_SQL_FILE = "input.sql";

	protected ApplicationContext context;

	List<File> files = new ArrayList<File>();

	/**
	 * 
	 * @param from
	 * @param to
	 * @throws Exception
	 */
	protected void copyFile(String from, String to) throws Exception {

		// /src/test/resources

		File dir = new File(".");

		String d = dir.getCanonicalPath();

		System.out.println("dir: " + d);

		// if ( !(new File(d + from)).exists() )
		// d += "/src/test/resources";

		File src = new File(d + from);

		if (!src.exists())
			throw new ArgumentException(String.format("{} does not exist", src
					.getCanonicalPath()));

		File dest = new File(d + to);

		LOGGER.debug("copying {} to {}", src.getCanonicalPath(), dest
				.getCanonicalPath());

		if (dest.exists())
			dest.delete();

		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dest);
		
		StringBuffer content = new StringBuffer();
		
		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			content.append(new String(buf));
			out.write(buf, 0, len);
			buf = new byte[1024];
		}
		LOGGER.debug("content: \n" + content.toString());
		in.close();
		out.flush();
		out.close();

		if (!dest.exists())
			throw new ArgumentException(String.format("{} was not copied", dest
					.getCanonicalPath()));

		files.add(dest);
	}

	public void setUp() throws Exception {
		try {
			LOGGER.info("Starting in-memory HSQL database for unit tests");
			Class.forName("org.hsqldb.jdbcDriver");
			cnxn = DriverManager
					.getConnection("jdbc:hsqldb:mem:test", "sa", "");

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			fail("Exception during HSQL database startup.");
		}

		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream(
							(INPUT_SQL_FILE))));

			StringBuffer sb = new StringBuffer();

			String s = null;

			while ((s = r.readLine()) != null)
				sb.append(s);

			r.close();

			Statement st = cnxn.createStatement();

			LOGGER.info("Executing/n" + sb.toString());

			if (st.execute(sb.toString()))
				LOGGER.debug("successfully created.");
			else
				LOGGER.debug("bombed");

			st.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@After
	public void tearDown() throws Exception {

		LOGGER.info("Stopping in-memory HSQL database.");
		try {
			cnxn.createStatement().execute("SHUTDOWN");
		} catch (Exception ex) {
		}

		if (context != null) {
			context = null;
		}
		
		for (File f : files) {
			if (f.exists()) {
				LOGGER.info("deleting {}", f.getCanonicalPath());
				f.delete();
			}
		}
	}
}
