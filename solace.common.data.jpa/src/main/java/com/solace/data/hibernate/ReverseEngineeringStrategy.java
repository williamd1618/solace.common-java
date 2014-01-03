/*******************************************************************************
 * Copyright (c) 2013 <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a>.
 * All rights reserved. This program, solace.common.data, and file, ReverseEngineeringStrategy.java, and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     <a href="mailto:daniel.williams@gmail.com">Daniel Williams</a> - initial API and implementation
 ******************************************************************************/
package com.solace.data.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.solace.logging.*;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.TableIdentifier;

public class ReverseEngineeringStrategy extends
		DelegatingReverseEngineeringStrategy {

	private static final Logger LOGGER = Logger
			.getLogger(ReverseEngineeringStrategy.class);

	private static final List<String> EXCLUDE = Arrays.asList("ID",
			"CREATE_DATE", "LAST_UPDATE_DATE", "VERSION");

	private ReverseEngineeringSettings settings = null;

	@Override
	public String tableToClassName(TableIdentifier tableIdentifier) {
		String classname = super.tableToClassName(tableIdentifier);
		
		if ( classname.contains("Tb") )
			classname = classname.replace("Tb", "");

		LOGGER.debug("Table: {} is Class: {}", tableIdentifier.getName(),
				classname);

		return classname;
	}
	
	public ReverseEngineeringStrategy(org.hibernate.cfg.reveng.ReverseEngineeringStrategy strategy) {
		super(strategy);
	}
}
