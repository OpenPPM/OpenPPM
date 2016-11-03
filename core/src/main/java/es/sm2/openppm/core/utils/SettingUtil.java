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
 * File: SettingUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.GenericSetting;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.SettingDAO;
import es.sm2.openppm.core.logic.impl.SettingLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;

public class SettingUtil {

public final static Logger LOGGER = Logger.getLogger(SettingUtil.class);
	
	protected SettingUtil() {}
	
	/**
	 * Send password
	 * 
	 * @param settings
	 * @return
	 */
	public static boolean sendPassword(HashMap<String, String> settings) {
		
		return Constants.SECURITY_LOGIN_BBDD.equals(SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE))
				|| Constants.SECURITY_LOGIN_MIXED.equals(SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE));
	}
	
	
	/**
	 * Get setting (only for logics)
	 * @param session
	 * @param company
	 * @param nameSetting
	 * @param defaultValue
	 * @return
	 */
	public static String getSetting(Session session, Company company, String nameSetting, String defaultValue) {
		
		Setting setting = null;
		
    	try {
			SettingDAO settingDAO = new SettingDAO(session);
			
			setting = settingDAO.findSetting(company, nameSetting);
		}
    	catch (Exception e) { LOGGER.error(e.getMessage(), e); }

    	String value = setting != null ? setting.getValue(): defaultValue;
    	
    	return value;
	}

    /**
     * Get setting in boolean format for logic
     *
     * @param session
     * @param company
     * @param setting
     * @return
     */
    public static Boolean getBoolean(Session session, Company company,  GenericSetting setting) {
        return Boolean.valueOf(getString(session, company, setting));
    }

    /**
     * Get setting in string format for logic
     *
     * @param session
     * @param company
     * @param setting
     * @return
     */
    public static String getString(Session session, Company company,  GenericSetting setting) {
        return getSetting(session, company, setting.getName(), setting.getDefaultValue());
    }

	/**
	 * Get setting
	 * @param company
	 * @param nameSetting
	 * @param defaultValue
	 * @return
	 */
	public static String getSetting(Company company, String nameSetting, String defaultValue) {
		
		Setting setting = null;
		
    	try {
    		
    		SettingLogic settingLogic = new SettingLogic();
			setting = settingLogic.findSetting(company, nameSetting);
		}
    	catch (Exception e) { LOGGER.error(e); }

    	String value = setting != null ? setting.getValue(): defaultValue;
    	
    	return value;
	}
	
	/**
	 * Get Settings by Company
	 * @param company
	 * @return
	 * @throws Exception 
	 */
	public static HashMap<String, String> getSettings (Company company) throws Exception {
		
		HashMap<String, String> settings = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			settings = getSettings(session, company);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return settings;
	}
	
	/**
	 * Get Settings by Company
	 * 
	 * @param session
	 * @param company
	 * @return
	 */
	public static HashMap<String, String> getSettings (Session session, Company company) throws Exception {
		
		List<Setting> settingsList = null;
		HashMap<String, String> settings = null;
		
		SettingDAO settingDAO = new SettingDAO(session);
		
		settingsList = settingDAO.findByCompany(company, null);
		
		settings = new HashMap<String, String>();
		for(Setting setting : settingsList) {
			settings.put(setting.getName(), setting.getValue());
		}
		
		return settings;
	}
	
	/**
	 * Get setting value from settings HashMap. If not exist, get default value.
	 * @param settings
	 * @param settingName
	 * @param defaultValue
	 * @return
	 */
	public static String getString(Map<String, String> settings, String settingName, String defaultValue) {
		return (settings.containsKey(settingName) ? settings.get(settingName) : defaultValue);
	}
	
	/**
	 * Get setting value from settings HashMap. If not exist, get default value by generic interface <code>GenericSetting</code>
	 * 
	 * @param settings
	 * @param setting
	 * @return
	 */
	public static String getString(Map<String, String> settings,  GenericSetting setting) {
		return (settings.containsKey(setting.getName()) ? settings.get(setting.getName()) : setting.getDefaultValue());
	}
	
	/**
	 * Get setting value from settings HashMap. If not exist, get default value. Boolean format
	 * @param settings
	 * @param settingName
	 * @param defaultValue
	 * @return
	 */
	@Deprecated
	public static Boolean getBoolean(Map<String, String> settings, String settingName, String defaultValue) {
		return Boolean.valueOf(settings.containsKey(settingName) ? settings.get(settingName) : defaultValue);
	}

	/**
	 * Get Setting by generic interface <code>GenericSetting</code>
	 * 
	 * @param settings
	 * @param setting
	 * @return
	 */
	public static Boolean getBoolean(Map<String, String> settings, GenericSetting setting) {
		return Boolean.valueOf(settings.containsKey(setting.getName()) ? settings.get(setting.getName()) : setting.getDefaultValue());
	}

    /**
     * Get Setting by generic interface <code>GenericSetting</code>
     *
     * @param settings
     * @param setting
     * @return
     */
    public static Integer getInteger(HashMap<String, String> settings, GenericSetting setting) {
        return Integer.valueOf(settings.containsKey(setting.getName()) ? settings.get(setting.getName()) : setting.getDefaultValue());
    }
	
	/**
	 * Get setting value from settings HashMap. If not exist, get default value. List format (Split ",") 
	 * @param settings
	 * @param settingName
	 * @param defaultValue
	 * @return
	 */
	public static String[] getList(HashMap<String, String> settings, String settingName, String defaultValue) {
		return (settings.containsKey(settingName) ? settings.get(settingName) : defaultValue).split(",");
	}
	
	
	public static Integer getInteger(HashMap<String, String> settings, String settingName, String defaultValue) {
		return Integer.parseInt(settings.containsKey(settingName) ? settings.get(settingName) : defaultValue);
	}
}
