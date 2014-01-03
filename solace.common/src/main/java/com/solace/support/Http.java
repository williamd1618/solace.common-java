package com.solace.support;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import com.solace.support.json.Json;

public class Http {	
	
	/**
	 * Will parse an HttpResponse into the appropriate object type.
	 * @param response
	 * @param clazz
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static <T> T getResponseObject(HttpResponse response, Class<T> clazz) throws ParseException, IOException {		
		return Json.<T>fromJson(EntityUtils.toString(response.getEntity()), clazz);
	}
}
