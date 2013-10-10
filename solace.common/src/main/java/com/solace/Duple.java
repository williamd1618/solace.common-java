/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common, and file, Duple.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace;

/**
 * Simply a way to easily aggregate two concepts.
 * 
 * @author dwilliams
 * 
 * @param <L>
 * @param <R>
 */
public class Duple<L, R> {

	private L m_left;

	private R m_right;

	public Duple() {

	}

	public Duple(L left, R right) {
		m_left = left;
		m_right = right;
	}

	public L getLeft() {
		return m_left;
	}

	public void setLeft(L left) {
		m_left = left;
	}

	public R getRight() {
		return m_right;
	}

	public void setRight(R right) {
		m_right = right;
	}

	@Override
	public String toString() {
		return String.format("L:{} : R:{}", m_left.toString(), m_right
				.toString());
	}
	
	@Override	
	public boolean equals(Object arg0) {
		
		if ( !(arg0 instanceof Duple) )
			return false;
		else
		{
			Duple d = (Duple) arg0;
			
			return d.getLeft().equals(getLeft()) && d.getRight().equals(getRight());
		}	
	}
}
