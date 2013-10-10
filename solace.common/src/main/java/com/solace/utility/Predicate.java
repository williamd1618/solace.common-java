/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, Predicate.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.utility;

/**
 * This interface provides only one method named "predicate".
 * This interface can be used with other classes to allow an Iterator
 *     to only return elements that pass some preliminary test.
 */

public interface Predicate<T> {

    boolean predicate(T element);

}
