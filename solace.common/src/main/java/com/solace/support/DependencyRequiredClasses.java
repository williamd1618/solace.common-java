package com.solace.support;

import java.lang.annotation.RetentionPolicy;

import java.lang.annotation.Retention;

/**
 * Used to identify additional sub-autowire class instance dependencies. This
 * will allow for us to autowire sub-property components as long as they are
 * registered into the IoC context.
 * 
 * @author <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DependencyRequiredClasses {

	DependencyRequired[] required();

}
