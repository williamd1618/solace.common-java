/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, Json.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.support.json;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.solace.logging.Logger;
import com.solace.support.ObjectMapperUtils;

/**
 * A default JSON serialization utility class that will take object to json and
 * back.
 * <p>
 * By default the {@link JsonIgnore} annotation can be applied to fields to
 * prevent them from being operated against.
 * <p>
 * Functionality in this class can be extended by leveraging the Gson API via
 * the {@link #getBuilder()} method. Once this has been exposed modifications
 * can be applied accordingly BUT the
 * 
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 * 
 */
public class Json {

	private static final Logger LOGGER = Logger.getLogger(Json.class);

	static Gson gson;

	static GsonBuilder builder;

	static ObjectMapper mapper = ObjectMapperUtils.getObjectMapper();

//	/**
//	 * Exposes the GsonBuilder to extend the functionality on a static or
//	 * application basis.
//	 * 
//	 * @return
//	 */
//	public static GsonBuilder getBuilder() {
//		return builder;
//	}
//
//	/**
//	 * simply rebuild the gson interfacte
//	 */
//	public static void rebuild() {
//		synchronized (gson) {
//			gson = builder.create();
//		}
//	}

	/**
	 * Will return a json string from an object
	 * 
	 * @param o
	 *            object to serialize
	 * @return string
	 */
	public static String toJson(Object o) {
		try {
			return mapper.writeValueAsString(o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// LOGGER.debug(o.getClass().getCanonicalName());
		// return gson.toJsonTree(o).toString();
	}

	/**
	 * Will make sure the string is an array
	 * 
	 * @param o
	 *            object to serialize
	 * @return string
	 */
	public static String toJsonArray(Object o) {
		
		return toJson(o);
	}

	/**
	 * Will return a type-safe object of clazz from the json string. The static
	 * inline generic will optimize runtime by creating multiple static type
	 * signatures.
	 * 
	 * @param <T>
	 *            the type we are providing type safety for
	 * @param json
	 *            the string
	 * @param clazz
	 *            the type that json will serialize to
	 * @return a instance of <T>
	 */
	public static <T> T fromJson(String json, Class<?> clazz) {
		// return gson.<T> fromJson(json, clazz);
		try {
			return (T) mapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Will take a JsonArray set and convert it to a list of T
	 * 
	 * @param <T>
	 *            the class type
	 * @param json
	 *            the input string
	 * @param clazz
	 *            the type the formatter is expecting
	 * @return a List of T
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public static <T> List<T> fromJsonArray(String json, Class<?> clazz) throws JsonParseException, JsonMappingException, IOException {
		
		List<T> list = mapper.readValue(json, new TypeReference<List<T>>() { });


		return list;
	}
}
