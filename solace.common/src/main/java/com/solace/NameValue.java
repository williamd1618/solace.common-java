/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, NameValue.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace;

/**
 * A utility class to aggregate two concepts in a NameValue fashion.
 * Used by CommonException
 * @author dwilliams
 *
 */
public class NameValue {

	String m_name;
	Object m_value;

	public NameValue(String _name, Object _value) {
		m_name = _name;
		m_value = _value;
	}

	public String getName() {
		return m_name;
	}

	public Object getValue() {
		return m_value;
	}

	@Override
	public String toString() {
		return m_name + "=" + ((m_value != null) ? m_value.toString() : "NULL");
	}
}
