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
 * File: ProjectCloseTemplate.java
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
 * Represents a project close document implemented by a particular customer.
 * <br>
 * Subclasses should be immutable.
 *
 * @author Dani
 */
@Deprecated
public abstract class ProjectCloseTemplate {

	//protected static final String outputFile = Settings.TEMP_DIR_GENERATE_DOCS;
	
	/**
	 * Get an instance of the project close template specified by the properties file.
	 *
	 * @return The specified ProjectCloseTemplate
	 * @throws LogicException If no project close template was specified, or if it could not be instantiated.
	 */
	public static ProjectCloseTemplate getProjectCloseTemplate(HashMap<String, String> settings) throws LogicException {
		
		String projectCloseTemplateName = SettingUtil.getString(settings, Settings.SETTING_PROJECT_CLOSE_CLASSNAME, Settings.DEFAULT_PROJECT_CLOSE_CLASSNAME);
		
		return instantiateProjectCloseTemplate ( projectCloseTemplateName );
	}


	private static ProjectCloseTemplate instantiateProjectCloseTemplate(String projectCloseTemplateName)
	throws LogicException {
		
		if ( projectCloseTemplateName == null ) {
			throw new LogicException( 
					"The project close template was not set. " +
					"Set the property project.close.template." );
		}
		try {
			return ( ProjectCloseTemplate ) ReflectUtil.classForName( projectCloseTemplateName ).newInstance();
		}
		catch ( ClassNotFoundException cnfe ) {
			throw new LogicException( 
					"Project close template class not found: " + projectCloseTemplateName );
		}
		catch ( Exception e ) {
			throw new LogicException( 
					"Could not instantiate given project close template class: " + projectCloseTemplateName );
		}
	}

	
	public abstract File generateClose (ResourceBundle langResource, Project project, Projectcharter projectCharter, 
			HashMap<String, String> settings, String...args) throws Exception;	
}
