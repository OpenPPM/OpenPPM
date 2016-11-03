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
 * File: ChangeRequestTemplate.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.charter;

import java.io.File;
import java.util.ResourceBundle;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.ReflectUtil;
import es.sm2.openppm.utils.exceptions.LogicException;

/**
 * Represents a change request document implemented by a particular customer.
 * <br>
 * Subclasses should be immutable.
 *
 * @author Juan.Romero
 */
@Deprecated
public abstract class ChangeRequestTemplate {
	
	/**
	 * Get an instance of the project change request specified by the properties file.
	 *
	 * @return The specified ChangeRequestTemplate
	 * @throws LogicException If no project change request template was specified, or if it could not be instantiated.
	 */
	public static ChangeRequestTemplate getChangeRequestTemplate(ResourceBundle idioma, Employee user) throws LogicException {
		
		
		String changeRequestTmpl = SettingUtil.getSetting(user.getContact().getCompany(), Settings.SETTING_CHANGE_REQUEST_CLASSNAME, Settings.DEFAULT_CHANGE_REQUEST_CLASSNAME);
		
		return instantiateChangeRequestTemplate (changeRequestTmpl);
	}

	private static ChangeRequestTemplate instantiateChangeRequestTemplate(String changeRequestTemplateName)
	throws LogicException {
		
		if (changeRequestTemplateName == null) {
			throw new LogicException( 
					"The project change request template was not set.");
		}
		try {
			return (ChangeRequestTemplate) ReflectUtil.classForName(changeRequestTemplateName).newInstance();
		}
		catch ( ClassNotFoundException cnfe ) {
			throw new LogicException( 
					"Change request template class not found: " + changeRequestTemplateName);
		}
		catch ( Exception e ) {
			throw new LogicException( 
					"Could not instantiate given chage request template class: " + changeRequestTemplateName);
		}
	}
	
	public abstract File generateChangeRequest (ResourceBundle idioma, Project project, Changecontrol change, 
			Employee preparedBy) throws Exception;	
}
