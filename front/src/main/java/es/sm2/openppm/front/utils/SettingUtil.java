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
 * Module: front
 * File: SettingUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import es.sm2.openppm.core.common.GenericSetting;
import org.apache.log4j.Logger;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.model.impl.Employee;

public class SettingUtil extends es.sm2.openppm.core.utils.SettingUtil {

public final static Logger LOGGER = Logger.getLogger(SettingUtil.class);
	
	private SettingUtil() {}
	
	
	/**
	 * Get settings from request
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getSettings(HttpServletRequest req) {
		return (HashMap<String, String>) req.getAttribute("settings");
	}
	
	/**
	 * Get Setting approval rol
	 * @param req
	 * @return
	 */
	public static int getApprovalRol(HttpServletRequest req) {
		
		int rol = 0;
		
    	try {
    		
    		Employee user = SecurityUtil.consUser(req);
    		
			rol = Integer.parseInt(getSetting(
					user.getContact().getCompany(),
					Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET,
					Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET));
		}
    	catch (Exception e) { ExceptionUtil.sendToLogger(LOGGER, e, null); }
    	
    	return rol;
	}
	
	
	/**
	 * Get setting value from settings HttpServletRequest. If not exist, get default value. Boolean format
     *
	 * @param req
	 * @param settingName
	 * @param defaultValue
	 * @return
	 */
    @Deprecated
	public static Boolean getBoolean(HttpServletRequest req, String settingName, String defaultValue) {
		HashMap<String, String> settings = getSettings(req);
		return getBoolean(settings, settingName, defaultValue);
	}

    /**
     * Get setting value from settings HttpServletRequest. If not exist, get default value. Boolean format
     *
     * @param req
     * @param setting
     * @return
     */
    public static Boolean getBoolean(HttpServletRequest req, GenericSetting setting) {
		HashMap<String, String> settings = getSettings(req);
		return getBoolean(settings, setting);
	}


    /**
     * Get if is checked
     *
     * @param settings
     * @param setting
     * @return
     */
    public static String isChecked(HashMap<String, String> settings,  GenericSetting setting) {
        return getBoolean(settings, setting)?"checked":"";
    }
}
