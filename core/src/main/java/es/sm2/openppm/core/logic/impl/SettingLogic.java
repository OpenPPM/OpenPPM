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
 * File: SettingLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.ApplicationSetting;
import es.sm2.openppm.core.common.GenericSetting;
import es.sm2.openppm.core.dao.SettingDAO;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;


/**
 * Logic object for domain model class Setting
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public class SettingLogic extends AbstractGenericLogic<Setting, Integer> {

	/**
	 * Find setting
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String findSetting(String name) throws Exception {
		
		StringBuilder values = new StringBuilder();
		Transaction tx	= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			SettingDAO settingDAO = new SettingDAO(session);
			List<Setting> settings = settingDAO.findSetting(name);
			
			if (ValidateUtil.isNotNull(settings)) {
				
				for (Setting setting : settings) {
					
					values.append(setting.getValue()).append(" ");
				}
			}
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return values.toString();
	}
	
	/**
	 * Find setting
	 * @param company
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Setting findSetting(Company company, String name) throws Exception {
		
		Setting setting = null;
		Transaction tx	= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			SettingDAO settingDAO = new SettingDAO(session);
			setting = settingDAO.findSetting(company, name);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return setting;
	}

    /**
     * Find all Settings for company
     *
     * @param company
     * @return
     * @throws Exception
     */
	public List<Setting> findByCompany(Company company) throws Exception {
		
		List<Setting> settings = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			SettingDAO settingDAO = new SettingDAO(session);
			
			settings = settingDAO.findByCompany(company, null);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return settings;
	}

	/**
	 * Find settings
	 *
	 * @param company
	 * @param applicationSettingList
	 * @return
	 * @throws Exception
	 */
	public List<Setting> findByCompany(Company company, Class<? extends ApplicationSetting>...applicationSettingList) throws Exception {

		List<Setting> settingMergeList = new ArrayList<Setting>();

		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();

			SettingDAO settingDAO = new SettingDAO(session);

			// Prepare values for filter query
			HashMap<String, String> values = new HashMap<String, String>();
			for (Class<? extends ApplicationSetting> applicationSetting : applicationSettingList) {
				for (ApplicationSetting setting : applicationSetting.getEnumConstants()) {
					values.put(setting.name(), setting.getDefaultValue());

				}
			}

			// Get BD settings
			List<Setting> settings = settingDAO.findByCompany(company, values.keySet());

			// Merge BD settings with default values

			for (String key : values.keySet()) {

				Setting settingFound = null;

				int i = 0;
				while (i < settings.size()) {
					Setting setting = settings.get(i++);

					if (key.equals(setting.getName())) {
						settingFound = setting;
					}
				}

				if (settingFound == null) {
					settingFound = new Setting();
					settingFound.setName(key);
					settingFound.setValue(values.get(key));
				}

				settingMergeList.add(settingFound);
			}

			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }

		return settingMergeList;
	}

    /**
     * Update Setting
     *
     * @param company
     * @param genericSetting
     * @param settingValue
     * @throws Exception
     */
    public void updateSetting(Company company, GenericSetting genericSetting, String settingValue) throws Exception {

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            SettingDAO settingDAO = new SettingDAO(session);

            Setting setting = settingDAO.findSetting(company, genericSetting.getName());

            if (ValidateUtil.isNotNull(settingValue)) {
                if (setting == null) {
                    setting = new Setting();
                    setting.setName(genericSetting.getName());
                    setting.setCompany(company);
                }

                setting.setValue(settingValue);
                settingDAO.makePersistent(setting);
            }
            else { // if the value is null delete setting
                if (setting != null) {
                    settingDAO.makeTransient(setting);
                }
            }

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }

	/**
	 * Update Settings
	 *
	 * @param settingList
	 * @throws Exception
	 */
    public void updateSettingList(List<Setting> settingList) throws Exception {

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            SettingDAO settingDAO = new SettingDAO(session);

			if (ValidateUtil.isNotNull(settingList)) {

				Company company = null;
				Set<String> keyList = new HashSet<String>();

				for (Setting setting : settingList) {
					keyList.add(setting.getName());

					if (company == null) {
						company = setting.getCompany();
					}
				}

				// List of settings stored
				List<Setting> settingListStored = settingDAO.findByCompany(company, keyList);

				// Update setting or create
				for (Setting setting : settingList) {

					boolean create = true;
					int i = 0;
					while (create && i < settingListStored.size()) {
						Setting settingStored = settingListStored.get(i++);

						// Update setting
						if (setting.getName().equals(settingStored.getName())) {

							create = false;
							settingStored.setValue(setting.getValue());
							settingDAO.makePersistent(setting);
						}
					}

					// Create setting
					if (create) {
						settingDAO.makePersistent(setting);
					}
				}

			}

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }

	/**
	 * Get application settings by company in map format
	 *
	 * @param company
	 * @param applicationSettingList
	 * @return
	 * @throws Exception
	 */
	public HashMap<ApplicationSetting, String> findByCompanyApplication(Company company, Class<? extends ApplicationSetting>...applicationSettingList) throws LogicException {

		List<Setting> settingList = null;
		try {
			settingList = findByCompany(company, applicationSettingList);
		} catch (Exception e) {
			throw new LogicException(e.getMessage(), e.getCause());
		}

		// Prepare values for filter query
		HashMap<ApplicationSetting, String> values = new HashMap<ApplicationSetting, String>();
		for (Class<? extends ApplicationSetting> applicationSetting : applicationSettingList) {
			for (ApplicationSetting appSetting : applicationSetting.getEnumConstants()) {

				for (Setting setting : settingList) {

					if (appSetting.name().equals(setting.getName())) {
						values.put(appSetting, setting.getValue());
					}
				}

			}
		}

		return values;
	}
}

