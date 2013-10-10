/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, PredicateIterator.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.utility;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class implements the Iterator interface and provides access to a
 * predicate that will filter out certains elements using the Predicate interface.
 */
public class PredicateIterator<T> implements Iterator<T> {

    private Iterator<T> iter  = null;
    private Predicate<T> pred = null;
    private T next    = null;
    boolean doneNext       = false;

    public PredicateIterator(Iterator<T> iter, Predicate<T> pred) {
        this.iter = iter;
        this.pred = pred;        
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        doneNext = true;
        boolean hasNext;
        while (hasNext = iter.hasNext()) {
            next = iter.next();
            if (pred.predicate(next)) {
                break;
            }
        }
        return hasNext;
    }

    public T next() {
        if (!doneNext) {
            boolean has = hasNext();
            if (!has) {
                throw new NoSuchElementException();
            }
        }
        doneNext = false;
        return next;
    }

}
