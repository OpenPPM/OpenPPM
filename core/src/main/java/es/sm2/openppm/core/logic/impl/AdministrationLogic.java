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
 * File: AdministrationLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.ContactDAO;
import es.sm2.openppm.core.dao.EmployeeDAO;
import es.sm2.openppm.core.dao.PerformingOrgDAO;
import es.sm2.openppm.core.dao.ResourceProfileDAO;
import es.sm2.openppm.core.dao.SecurityDAO;
import es.sm2.openppm.core.utils.EmailUtil;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.core.utils.StringUtils;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;


public class AdministrationLogic  {

	private static final  Logger LOGGER = Logger.getLogger(AdministrationLogic.class);
	
	
	/**
	 * Assigning role to a contact
	 * 
	 * @param contact
	 * @param security
	 * @param performingorg 
	 * @param role 
	 * @param resourceBundle
	 * @param settings
	 * @param requestURL
	 * @param session2 
	 * @return 
	 * @throws Exception
	 */
	public Contact createRole(Contact contact, Security security, Performingorg performingorg, int role, 
			ResourceBundle resourceBundle, HashMap<String, String> settings, StringBuffer requestURL, Session session) throws Exception {
		
			
		ResourceProfileDAO resourceProfileDAO 	= new ResourceProfileDAO(session);
		ContactDAO contactDAO 					= new ContactDAO(session);
		SecurityDAO securityDAO 				= new SecurityDAO(session);

		// Error control 
		//
		if (contact == null ) {
			throw new Exception("Not found contact");
		}
		
		if (security == null ) {
			throw new Exception("Not found security");
		}
		
		if (resourceBundle == null ) {
			throw new Exception("Not found resourceBundle");
		}
		
		List<Resourceprofiles> resourceprofiles = resourceProfileDAO.findByRelation(Resourceprofiles.IDPROFILE, role);
		
		Resourceprofiles profile = null;
		
		if (resourceprofiles == null || resourceprofiles.isEmpty()) {
			throw new Exception("Not found profile");
		}
		else {
			profile = resourceprofiles.get(0);
		}
		
		if (requestURL == null ) {
			throw new Exception("Not found requestURL");
		}
		
		// New contact 
		//
		if (contact.getIdContact() == -1) {
			
			contact.setIdContact(null);
			contact = contactDAO.makePersistent(contact);
			
			// New security 
			//
			String password = security.getLogin();
			String to 		= contact.getEmail();
			
			if ( Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_MAIL_NOTIFICATIONS, Settings.DEFAULT_MAIL_NOTIFICATIONS)) && 
					!ValidateUtil.isNull(to) && !"".equals(to)) {
				password = StringUtils.getRandomString(8);
			}	
			
			security.setPassword(StringUtils.md5(password));
			security.setContact(contact);
			
			securityDAO.makePersistent(security);
			
			// New employee role
			//
			Employee employee = new Employee();
			employee.setContact(contact);
			employee.setPerformingorg(performingorg);
			employee.setResourceprofiles(profile);
			employee.setProfileDate(new Date());
			
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			
			employeeDAO.makePersistent(employee);
			
			// Send email 
			//
			if (!ValidateUtil.isNull(to) && Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_MAIL_NOTIFICATIONS, Settings.DEFAULT_MAIL_NOTIFICATIONS))) {
				
				try {
					
					EmailUtil email = new EmailUtil(settings, null, to);
					
					ParamResourceBundle bundle = new ParamResourceBundle("msg.mail.subject",resourceBundle.getString("maintenance.employee.email.login"));
					
					email.setSubject(bundle.getMessage(resourceBundle));
					
					StringBuilder bodytext = new StringBuilder();
					bodytext.append(
							new ParamResourceBundle("maintenance.employee.email.new_profile_msg", contact.getFullName(), profile.getProfileName()).toString(resourceBundle) +
							StringPool.NEW_LINE + 
							StringPool.NEW_LINE + 
							new ParamResourceBundle("maintenance.employee.email.user_register", contact.getFullName()).toString(resourceBundle) +
							StringPool.NEW_LINE + 
							StringPool.NEW_LINE + 
							resourceBundle.getString("url")+ StringPool.COLON_SPACE + requestURL.substring(0, requestURL.lastIndexOf(StringPool.FORWARD_SLASH)) + StringPool.NEW_LINE + 
							resourceBundle.getString("maintenance.employee.username") + StringPool.COLON_SPACE + security.getLogin()
							);
					
					if (SettingUtil.sendPassword(settings)) {
						bodytext.append(StringPool.NEW_LINE + resourceBundle.getString("maintenance.employee.password")+ StringPool.COLON_SPACE + password);
					}
					
					email.setBodyText(bodytext.toString());
					email.send();
				}
				catch (Exception e) {
					LOGGER.error(e);
				}
			}
		}
		// Select contact 
		//
		else {
			
			contact = contactDAO.findById(contact.getIdContact());
			
			// Select security 
			//
			List<Security> securities = securityDAO.findByRelation(Security.CONTACT, contact);
			
			for (Security s : securities) {
				security = s;
			}
			
			// New employee role
			//
			Employee employee = new Employee();
			employee.setContact(contact);
			employee.setResourceprofiles(profile);
			employee.setPerformingorg(performingorg);
			employee.setProfileDate(new Date());
			
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			
			if (employeeDAO.findByContactAndPOAndRol(contact.getIdContact(), null, role) == null){
				employeeDAO.makePersistent(employee);
			}
			else {
				throw new Exception(resourceBundle.getString("msg.error.contact_has_profile"));
			}
			
			// Send email 
			//
			String to = contact.getEmail();
			
			if (!ValidateUtil.isNull(to) && Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_MAIL_NOTIFICATIONS, Settings.DEFAULT_MAIL_NOTIFICATIONS))) {
				
				try {
					
					EmailUtil email = new EmailUtil(settings, null, to);
					
					ParamResourceBundle bundle = new ParamResourceBundle("msg.mail.subject",resourceBundle.getString("maintenance.employee.email.new_profile_title"));
					
					email.setSubject(bundle.getMessage(resourceBundle));
					
					StringBuilder bodytext = new StringBuilder();
					bodytext.append(
							new ParamResourceBundle("maintenance.employee.email.new_profile_msg", contact.getFullName(), profile.getProfileName()).toString(resourceBundle) +
							StringPool.NEW_LINE + 
							StringPool.NEW_LINE + 
							resourceBundle.getString("url")+ StringPool.COLON_SPACE  + requestURL.substring(0, requestURL.lastIndexOf(StringPool.FORWARD_SLASH)) + 
							StringPool.NEW_LINE
							);
					
					email.setBodyText(bodytext.toString());
					email.send();
				}
				catch (Exception e) {
					LOGGER.error(e);
				}
			}
		}
		
		return contact;
	}


	/**
	 * Create PO
	 * 
	 * @param contactPMO
	 * @param securityPMO 
	 * @param optionPMO
	 * @param contactFM
	 * @param securityFM 
	 * @param optionFM
	 * @param performingorg
	 * @param resourceBundle
	 * @param settings
	 * @param requestURL
	 * @throws Exception 
	 */
	public void createPO(Contact contactPMO, Security securityPMO, String optionPMO,
			Contact contactFM, Security securityFM, String optionFM, Performingorg performingorg,
			ResourceBundle resourceBundle, HashMap<String, String> settings,
			StringBuffer requestURL) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
		
			PerformingOrgDAO performingOrgDAO 	= new PerformingOrgDAO(session);
			EmployeeDAO employeeDAO 			= new EmployeeDAO(session);
			
			// Create PO 
			//
			performingorg = performingOrgDAO.makePersistent(performingorg);
			
			// Create contact with rol PMO
			//
			contactPMO = createRole(contactPMO, securityPMO, performingorg, Constants.ROLE_PMO, resourceBundle, settings, requestURL, session);
			
			if (Constants.COPY.equals(optionFM)) {
				
				// Create contact with rol FM
				//
				contactFM = createRole(contactPMO, securityFM, performingorg, Constants.ROLE_FM, resourceBundle, settings, requestURL, session);
			}
			else {
				
				// Create contact with rol FM
				//
				contactFM = createRole(contactFM, securityFM, performingorg, Constants.ROLE_FM, resourceBundle, settings, requestURL, session);
			}
			
			// Assign FM to PO 
			//
			List<Employee> employees = employeeDAO.consEmployeesByUserAndRol(contactFM, Constants.ROLE_FM);
			
			for (Employee employee : employees) {
				performingorg.setEmployee(employee);
			}
			
			performingOrgDAO.makePersistent(performingorg);
		
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }	
	}


	/**
	 * Create admin
	 * 
	 * @param contact
	 * @param security
	 * @param resourceBundle
	 * @param settings
	 * @param requestURL
	 * @throws Exception
	 */
	public void createAdmin(Contact contact, Security security,
			ResourceBundle resourceBundle, HashMap<String, String> settings,
			StringBuffer requestURL) throws Exception {
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
		
			// Create contact with role ADMIN 
			//
			createRole(contact, security, null, Constants.ROLE_ADMIN, resourceBundle, settings, requestURL, session);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }	
			
		}
	
}
