package com.solace.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateTime {
	
	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	
	public static java.util.Date getNow() {
		return Calendar.getInstance().getTime();
	}
	
	public static java.util.Date getUtcNow() {
		return Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
	}
	
	
	public static java.util.Date parse(String _value) {
		try {
			return dateFormat.parse(_value);
		} catch ( Exception e ) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static String format(java.util.Date _date) {
		return dateFormat.format(_date);
	}

}
