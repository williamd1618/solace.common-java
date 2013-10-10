package com.solace.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Provides meta-data around how to inject hierarachical relationships.
 * 
 * @author <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DependencyRequired {	
	Class<?> instanceType();
	
	Class<?> castTo() default Class.class;
	
	String setter() default "";
	
	EDependencyScope scope() default EDependencyScope.Singleton;
}
