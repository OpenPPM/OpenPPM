/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: core
 * File: ProjectCharterTemplate.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.charter;


import java.io.File;
import java.util.HashMap;
import java.util.ResourceBundle;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.utils.ReflectUtil;
import es.sm2.openppm.utils.exceptions.LogicException;


/**
 * Represents a project charter document implemented by a particular customer.
 * <br>
 * Subclasses should be immutable.
 *
 * @author Dani
 */
@Deprecated
public abstract class ProjectCharterTemplate {

	
	//protected static final String outputFile = Settings.TEMP_DIR_GENERATE_DOCS;
	
	/**
	 * Get an instance of the project charter template specified by the properties file.
	 *
	 * @return The specified ProjectCharterTemplate
	 * @throws LogicException If no project charter template was specified, or if it could not be instantiated.
	 */
	public static ProjectCharterTemplate getProjectChaterTemplate(HashMap<String, String> settings) throws LogicException {
		
		String projectCharterTemplateName = SettingUtil.getString(settings, Settings.SETTING_PROJECT_CHARTER_CLASSNAME, Settings.DEFAULT_PROJECT_CHARTER_CLASSNAME);
		
		return instantiateProjectCharterTemplate ( projectCharterTemplateName );
	}

	public static ProjectCharterTemplate getProjectChaterTemplate(String projectCharterTemplateName) throws LogicException {
		
		return instantiateProjectCharterTemplate ( projectCharterTemplateName );
	}

	private static ProjectCharterTemplate instantiateProjectCharterTemplate(String projectCharterTemplateName)
	throws LogicException {
		
		if ( projectCharterTemplateName == null ) {
			throw new LogicException( 
					"The project charter template was not set. " +
					"Set the property project.charter.template." );
		}
		try {
			return ( ProjectCharterTemplate ) ReflectUtil.classForName( projectCharterTemplateName ).newInstance();
		}
		catch ( ClassNotFoundException cnfe ) {
			throw new LogicException( 
					"Project charter template class not found: " + projectCharterTemplateName );
		}
		catch ( Exception e ) {
			throw new LogicException( 
					"Could not instantiate given project charter template class: " + projectCharterTemplateName );
		}
	}

	
	public abstract File generateCharter (ResourceBundle langResource, Project project, Projectcharter projectCharter, 
			HashMap<String, String> settings, String...args) 
	throws Exception;	
}
