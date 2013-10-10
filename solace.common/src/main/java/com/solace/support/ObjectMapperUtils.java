package com.solace.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.fasterxml.jackson.databind.SerializationFeature.*;



public class ObjectMapperUtils {

	static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.enable(INDENT_OUTPUT);
		mapper.enable(WRITE_ENUMS_USING_TO_STRING);
	}

	public static synchronized ObjectMapper getObjectMapper() {
		return mapper;
	}

	
	public static synchronized ObjectMapper getObjectMapper(Class<?>... classes) {
		mapper.registerSubtypes(classes);		
		return mapper;
	}

}
