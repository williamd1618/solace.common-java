/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, DefaultJsonExclusionStrategy.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.support.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * By default we automatically exclude anything annotated 
 * with {@link JsonIgnore}
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 *
 */
public class DefaultJsonExclusionStrategy implements ExclusionStrategy {
	
	public DefaultJsonExclusionStrategy() {
		
	}

	public boolean shouldSkipClass(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean shouldSkipField(FieldAttributes f) {
		return f.getAnnotation(JsonIgnore.class) != null;
	}
}
