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
 * File: AdministrationServlet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

/** -------------------------------------------------------------------------
*
* Desarrollado por SM2 Baleares S.A.
*
* -------------------------------------------------------------------------
* Este fichero solo podra ser copiado, distribuido y utilizado
* en su totalidad o en parte, de acuerdo con los terminos y
* condiciones establecidas en el acuerdo/contrato bajo el que se
* suministra.
* -------------------------------------------------------------------------
*
* Proyecto : OpenPPM
* Autor : Javier Hernandez
* Fecha : Jueves, 12 de Enero, 2012
*
* -------------------------------------------------------------------------
*/

package es.sm2.openppm.front.servlets;

import com.granule.json.JSONObject;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.GenericSetting;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.common.Settings.SettingType;
import es.sm2.openppm.core.exceptions.LoginNameInUseException;
import es.sm2.openppm.core.logic.impl.AdministrationLogic;
import es.sm2.openppm.core.logic.impl.PerformingOrgLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.logic.impl.SecurityLogic;
import es.sm2.openppm.core.logic.impl.SettingLogic;
import es.sm2.openppm.core.logic.security.actions.AdminAction;
import es.sm2.openppm.core.logic.setting.EmailType;
import es.sm2.openppm.core.logic.setting.GeneralSetting;
import es.sm2.openppm.core.logic.setting.GeneralSettingAuthorization;
import es.sm2.openppm.core.logic.setting.GeneralSettingTimeAndCosts;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.logic.setting.RequiredFieldSetting;
import es.sm2.openppm.core.logic.setting.SecuritySetting;
import es.sm2.openppm.core.logic.setting.VisibilityInvestmentSetting;
import es.sm2.openppm.core.logic.setting.VisibilityProjectSetting;
import es.sm2.openppm.core.logic.setting.common.TypeSetting;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.front.threads.UpdatePOCThread;
import es.sm2.openppm.front.threads.scheduler.MigrationJob;
import es.sm2.openppm.front.utils.ConnectLDAP;
import es.sm2.openppm.front.utils.DocumentUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SecurityUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.json.JsonUtil;
import es.sm2.openppm.utils.params.ParamUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AdministrationServlet extends AbstractGenericServlet {

	private static final long serialVersionUID = 1L;
	
	private static final  Logger LOGGER = Logger.getLogger(AdministrationServlet.class);
	
	public static final String REFERENCE = "administration";
	
	/***************** TABS ****************/
	public static final int TAB_GENERAL_SETTINGS 		= 0;
	public static final int TAB_REQUIRED_SETTINGS		= 1;
	public static final int TAB_MAIL_SETTINGS 			= 2;
	public static final int TAB_SECURITY_SETTINGS		= 3;
	public static final int TAB_VISIBILITY_SETTINGS		= 4;
	public static final int TAB_CREATE_PO				= 5;
	public static final int TAB_CREATE_ADMIN			= 6;
	
	/***************** Actions ****************/
	public static final String SAVE_GENERAL_SETTINGS 	= "save-general-settings";
	public static final String SAVE_MAIL_SETTINGS 		= "save-mail-settings";
	public static final String SAVE_SECURITY_SETTINGS 	= "save-security-settings";
	public static final String SAVE_VISIBILITY_SETTINGS = "save-visibility-settings";
	public static final String CREATE_PO				= "create-new-po";
	public static final String CREATE_ADMIN				= "create-new-admin";
	public static final String SAVE_REQUIRED_SETTINGS	= "save-required-settings";

    /************** Actions AJAX **************/
	public static final String JX_USERNAME_IN_USE		= "ajax-username-in-use";
    public static final String JX_TEST_LDAP				= "ajax-test-ldap";
    public static final String JX_UPDATE_POC_PROJECTS   = "ajax-update-poc-projects";

    /**
	 * @see HttpServlet#service(HttpServletRequest req, HttpServletResponse resp)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.service(req, resp);

		String accion = ParamUtil.getString(req, "accion");
		LOGGER.debug("Accion: " + accion);

		if (SecurityUtil.consUserRole(req) != -1 && SecurityUtil.hasPermission(req, ValidateUtil.isNullCh(accion, REFERENCE))) {

			// TODO nuevo panel con la funcionalidad de mostrar y ocultar el mensaje de informacion

			/***************** Actions ****************/
			if (ValidateUtil.isNull(accion) || REFERENCE.equals(accion)) { viewAdministration(req, resp, TAB_GENERAL_SETTINGS); }
			else if (SAVE_GENERAL_SETTINGS.equals(accion)) { saveGeneralSettings(req, resp); }
			else if (SAVE_MAIL_SETTINGS.equals(accion)) { saveMailSettings(req, resp); }
			else if (SAVE_SECURITY_SETTINGS.equals(accion)) { saveSecuritySettings(req, resp); }
			else if (SAVE_VISIBILITY_SETTINGS.equals(accion)) { saveColumnSettings(req, resp); }
			else if (CREATE_PO.equals(accion)) { createPO(req, resp); }
			else if (CREATE_ADMIN.equals(accion)) { createAdmin(req, resp); }
			else if (SAVE_REQUIRED_SETTINGS.equals(accion)) { saveRequiredSettings(req, resp); }

			/************** Actions AJAX **************/
			else if (JX_USERNAME_IN_USE.equals(accion)) { usernameInUseJX(req, resp); }
			else if (JX_TEST_LDAP.equals(accion)) { testLDAPJX(req, resp); }
		}
        else if (SecurityUtil.hasPermission(req, AdminAction.SAVE_CUSTOMIZATION, accion)) { saveCustomizationJX(req, resp); }
        else if (SecurityUtil.hasPermission(req, AdminAction.JX_UPDATE_POC_PROJECTS, accion)) { updatePOCProjectsJX(req, resp); }
        else if (SecurityUtil.hasPermission(req, AdminAction.MIGRATION_DOCUMENTS, accion)) { migrationDocuments(req, resp); }
		else if (!isForward()) {
			forwardServlet(ErrorServlet.REFERENCE+"?accion=403", req, resp);
		}
	}

    /**
     * Migration documents
     *
     * @param req
     * @param resp
     */
    private void migrationDocuments(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            // Migration
            //
            MigrationJob migrationJob = new MigrationJob();

            migrationJob.execute(null);

            // Update setting
            //
            SettingLogic settingLogic = new SettingLogic();

            settingLogic.updateSetting(getCompany(req), SettingType.MIGRATION_DOCUMENTS, "false");

            infoUpdated(req, SettingType.MIGRATION_DOCUMENTS.getMessage());
        }
        catch (Exception e) {
            ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
        }



        viewAdministration(req, resp, TAB_GENERAL_SETTINGS);
    }

    /**
     * Update POC projects
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void updatePOCProjectsJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();

        try {

            // Request
            boolean exclude = ParamUtil.getBoolean(req, "exclude");

            // Logic
            ProjectLogic projectLogic = new ProjectLogic(getSettings(req), getResourceBundle(req));

            // Get projects with status plan or execute
            List<String> projectStatus = new ArrayList<String>();
            projectStatus.add(Constants.STATUS_PLANNING);
            projectStatus.add(Constants.STATUS_CONTROL);

            // Logic
            List<Project> projects = projectLogic.findByPOAndProjectStatus(getUser(req).getPerformingorg(), projectStatus);

            if (ValidateUtil.isNotNull(projects)) {

                LOGGER.info("Update POC projects ("+ projects.size() + ") - Start");

                // Constructor thread
                UpdatePOCThread updatePOCThread = new UpdatePOCThread(projects, getSettings(req));

                // Throw thread
                updatePOCThread.start();
            }

            // Response
            JSONObject infoJSON = new JSONObject();

            out.print(infoJSON);
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally { out.close(); }
    }

    /**
     * Save Panel customization settings in general tab
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    private void saveCustomizationJX(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        try {

            saveSetting(req, SettingType.CAPACITY_PLANNING_LEVEL1);
            saveSetting(req, SettingType.CAPACITY_PLANNING_LEVEL2);
            saveSetting(req, SettingType.CAPACITY_PLANNING_LEVEL3);
            saveSetting(req, SettingType.CAPACITY_PLANNING_LEVEL1_COLOR);
            saveSetting(req, SettingType.CAPACITY_PLANNING_LEVEL2_COLOR);
            saveSetting(req, SettingType.CAPACITY_PLANNING_LEVEL3_COLOR);
            saveSetting(req, SettingType.CAPACITY_PLANNING_LEVEL1_COLOR_TEXT);
            saveSetting(req, SettingType.CAPACITY_PLANNING_LEVEL2_COLOR_TEXT);
            saveSetting(req, SettingType.CAPACITY_PLANNING_LEVEL3_COLOR_TEXT);
            saveSetting(req, SettingType.CAPACITY_PLANNING_OUTOFRANGE_COLOR);
            saveSetting(req, SettingType.CAPACITY_PLANNING_OUTOFRANGE_TEXT);
            
            out.print(infoUpdated(getResourceBundle(req), null, "customization"));
        }
        catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
        finally { out.close(); }
    }

    /**
     * Save Setting
     *
     * @param req
     * @param setting
     * @throws Exception
     */
    private void saveSetting(HttpServletRequest req, GenericSetting setting) throws Exception {

        String settingValue = ParamUtil.getString(req, setting.getName(), setting.getDefaultValue());
        SettingLogic settingLogic = new SettingLogic();
        settingLogic.updateSetting(getCompany(req), setting, settingValue);
    }

    /**
     * Save Setting boolean
     *
     * @param req
     * @param setting
     * @throws Exception
     */
    private void saveSettingBol(HttpServletRequest req, GenericSetting setting) throws Exception {

        String settingValue = ParamUtil.getString(req, setting.getName(), "false");
        SettingLogic settingLogic = new SettingLogic();
        settingLogic.updateSetting(getCompany(req), setting, settingValue);
    }

    /**
	 * Create admin
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void createAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			
			AdministrationLogic administrationLogic = new AdministrationLogic();
			
			// Request 
			//
			Integer idContact 				= ParamUtil.getInteger(req, "idContact_admin", -1);
			String contactFullName 			= ParamUtil.getString(req, "full_name_admin", "");
			String contactJobTitle			= ParamUtil.getString(req, "job_title_admin", "");	
			String contactFileAs			= ParamUtil.getString(req, "file_as_admin", "");	
			String contactBusinessPhone		= ParamUtil.getString(req, "business_phone_admin", "");
			String contactMobilePhone		= ParamUtil.getString(req, "mobile_phone_admin", "");
			String contactBusinessAddress	= ParamUtil.getString(req, "business_address_admin", "");
			String contactEmail				= ParamUtil.getString(req, "email_admin", "");	
			String contactNotes				= ParamUtil.getString(req, "notes_admin", "");	
			String login 					= ParamUtil.getString(req, "login_admin", "");
			
			// Objects 
			//
			Contact contact = new Contact();
			contact.setIdContact(idContact);
			contact.setCompany(getCompany(req));
			contact.setFullName(contactFullName);
			contact.setJobTitle(contactJobTitle);
			contact.setFileAs(contactFileAs);
			contact.setBusinessPhone(contactBusinessPhone);
			contact.setMobilePhone(contactMobilePhone);
			contact.setBusinessAddress(contactBusinessAddress);
			contact.setEmail(contactEmail);
			contact.setNotes(contactNotes);
				
			Security security = new Security();
			security.setLogin(login);
			security.setAttempts(0);
			security.setDateCreation(DateUtil.getCalendar().getTime());
			
			// Functionality
			//
			administrationLogic.createAdmin(contact, security, getResourceBundle(req), getSettings(req), req.getRequestURL());
			
			// Response 
			//
			infoCreated(req, "settings.create_admin");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		// Redirect 
		//
		viewAdministration(req, resp, TAB_CREATE_ADMIN);
	}

	/**
	 * Test LDAP
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void testLDAPJX(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		PrintWriter out = resp.getWriter();

    	try {
    		
    		// Create connection
    		ConnectLDAP connectLDAP = new ConnectLDAP(getSettings(req));
    		connectLDAP.createConnection();
    		
    		out.print(JsonUtil.toJSON("info", getResourceBundle(req).getString("success")));
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Save security Settings
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveSecuritySettings(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		try {
			// Get and save settings
			saveSetting(req, Settings.SETTING_LDAP_ACCOUNT, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LDAP_ACCOUNT, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LDAP_BASE, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LDAP_IDENTIFICATOR, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LDAP_LASTNAME, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LDAP_MAIL, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LDAP_NAME, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LDAP_PASSWORD, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LDAP_PORT, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LDAP_SERVER, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LOGOFF_URL, StringPool.BLANK);
			saveSetting(req, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE);

			// Save general settings
			saveVisibleGenericSettings(req, SecuritySetting.values());
		}
		catch (Exception e) {
			ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
		}		
		
    	viewAdministration(req, resp, TAB_SECURITY_SETTINGS);
	}

	/**
	 * Check if username is in use
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void usernameInUseJX(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		
		String loginAdmin 	= ParamUtil.getString(req, "loginAdmin", null);
		String loginPMO 	= ParamUtil.getString(req, "loginPMO", null);
		String loginFM 		= ParamUtil.getString(req, "loginFM", null);
		
		PrintWriter out = resp.getWriter();

    	try {
    		
    		SecurityLogic securityLogic = new SecurityLogic();
    		
    		if (loginAdmin != null) {
    			
    			Security security = new Security();
    			
    			security.setLogin(loginAdmin);
    			
    			if (securityLogic.isUserName(security)) { throw new LoginNameInUseException(getResourceBundle(req).getString("msg.error.user-name-in-use") + StringPool.COLON_SPACE + loginAdmin); }
    		}
    				
    		if (loginPMO != null) {
    			
    			Security security = new Security();
    			
    			security.setLogin(loginPMO);
    			
    			if (securityLogic.isUserName(security)) { throw new LoginNameInUseException(getResourceBundle(req).getString("msg.error.user-name-in-use") + StringPool.COLON_SPACE + loginPMO); }
    		}	
    		
    		if (loginFM != null) {
    			
    			Security security = new Security();
    			
    			security.setLogin(loginFM);
    			
    			if (securityLogic.isUserName(security)) { throw new LoginNameInUseException(getResourceBundle(req).getString("msg.error.user-name-in-use") + StringPool.COLON_SPACE + loginFM); }
    		}		
			
    		out.print(JsonUtil.infoTrue());
    	}
		catch (Exception e) { ExceptionUtil.evalueExceptionJX(out, req, getResourceBundle(req), LOGGER, e); }
		finally { out.close(); }
	}

	/**
	 * Create PO and PMO
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void createPO(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Request 
		//
		// PO
		String perforgName					= ParamUtil.getString(req, "name", null);
		
		// PMO
		Integer idContactPMO 				= ParamUtil.getInteger(req, "idContact_pmo", -1);
		String contactFullNamePMO 			= ParamUtil.getString(req, "full_name_pmo", "");
		String contactJobTitlePMO			= ParamUtil.getString(req, "job_title_pmo", "");	
		String contactFileAsPMO				= ParamUtil.getString(req, "file_as_pmo", "");	
		String contactBusinessPhonePMO		= ParamUtil.getString(req, "business_phone_pmo", "");
		String contactMobilePhonePMO		= ParamUtil.getString(req, "mobile_phone_pmo", "");
		String contactBusinessAddressPMO	= ParamUtil.getString(req, "business_address_pmo", "");
		String contactEmailPMO				= ParamUtil.getString(req, "email_pmo", "");	
		String contactNotesPMO				= ParamUtil.getString(req, "notes_pmo", "");	
		String loginPMO 					= ParamUtil.getString(req, "login_pmo");
		
		// FM
		Integer idContactFM 				= ParamUtil.getInteger(req, "idContact_fm", -1);
		String contactFullNameFM 			= ParamUtil.getString(req, "full_name_fm", "");
		String contactJobTitleFM			= ParamUtil.getString(req, "job_title_fm", "");	
		String contactFileAsFM				= ParamUtil.getString(req, "file_as_fm", "");	
		String contactBusinessPhoneFM		= ParamUtil.getString(req, "business_phone_fm", "");
		String contactMobilePhoneFM			= ParamUtil.getString(req, "mobile_phone_fm", "");
		String contactBusinessAddressFM		= ParamUtil.getString(req, "business_address_fm", "");
		String contactEmailFM				= ParamUtil.getString(req, "email_fm", "");	
		String contactNotesFM				= ParamUtil.getString(req, "notes_fm", "");	
		String loginFM 						= ParamUtil.getString(req, "login_fm");
		
		// Options
		String optionPMO					= ParamUtil.getString(req, "option_panel_pmo", "");	
		String optionFM 					= ParamUtil.getString(req, "option_panel_fm", "");
		String optionPO						= ParamUtil.getString(req, "option_panel_po", "");
		
		try {
			
			AdministrationLogic administrationLogic = new AdministrationLogic();
			PerformingOrgLogic performingOrgLogic 		= new PerformingOrgLogic();
			
			// Contact PMO
			//
			Contact contactPMO = new Contact();
			contactPMO.setIdContact(idContactPMO);
			contactPMO.setCompany(getCompany(req));
			contactPMO.setFullName(contactFullNamePMO);
			contactPMO.setJobTitle(contactJobTitlePMO);
			contactPMO.setFileAs(contactFileAsPMO);
			contactPMO.setBusinessPhone(contactBusinessPhonePMO);
			contactPMO.setMobilePhone(contactMobilePhonePMO);
			contactPMO.setBusinessAddress(contactBusinessAddressPMO);
			contactPMO.setEmail(contactEmailPMO);
			contactPMO.setNotes(contactNotesPMO);
			
			// Security PMO
			//
			Security securityPMO = new Security();
			securityPMO.setAttempts(0);
			securityPMO.setDateCreation(DateUtil.getCalendar().getTime());
			securityPMO.setLogin(loginPMO);
			
			// Contact FM
			//
			Contact contactFM = new Contact();
			contactFM.setIdContact(idContactFM);
			contactFM.setCompany(getCompany(req));
			contactFM.setFullName(contactFullNameFM);
			contactFM.setJobTitle(contactJobTitleFM);
			contactFM.setFileAs(contactFileAsFM);
			contactFM.setBusinessPhone(contactBusinessPhoneFM);
			contactFM.setMobilePhone(contactMobilePhoneFM);
			contactFM.setBusinessAddress(contactBusinessAddressFM);
			contactFM.setEmail(contactEmailFM);
			contactFM.setNotes(contactNotesFM);
			
			// Security FM
			//
			Security securityFM = new Security();
			securityFM.setAttempts(0);
			securityFM.setDateCreation(DateUtil.getCalendar().getTime());
			securityFM.setLogin(loginFM);
			
			// PO 
			//
			Performingorg performingorg = null;
			
			if (Constants.NEW.equals(optionPO)) {
				performingorg = new Performingorg();
				performingorg.setCompany(getCompany(req));
				performingorg.setName(perforgName);
			}
			else {
				performingorg = performingOrgLogic.findById(ParamUtil.getInteger(req, "search_perforg"));
			}
			
			// Functionality 
			//
			administrationLogic.createPO(contactPMO, securityPMO, optionPMO, contactFM, securityFM, optionFM, performingorg, getResourceBundle(req), getSettings(req), req.getRequestURL());
			
			// Response 
			//
			infoCreated(req, "settings.create_po");
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
		
		// Redirect 
		//
		viewAdministration(req, resp, TAB_CREATE_PO);
	}

	/**
	 * Save General Setting
	 * 
	 * @param req
	 * @param resp
	 */
	private void saveGeneralSettings(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            // Get and save boolean settings
            saveSettingBol(req, Settings.SETTING_CSV_COLUMN_IDPROJECT, Settings.DEFAULT_CSV_COLUMN_IDPROJECT);
            saveSettingBol(req, Settings.SETTING_PROJECT_EXCEEDED_BUDGET, Settings.DEFAULT_PROJECT_EXCEEDED_BUDGET);
            saveSettingBol(req, Settings.SETTING_SHOW_INACTIVATED, Settings.SETTING_SHOW_INACTIVATED_DEFAULT);
            saveSettingBol(req, Settings.SETTING_DISABLE_PROJECT, Settings.DEFAULT_DISABLE_PROJECT);
            saveSettingBol(req, Settings.SETTING_DISABLE_EDITION_INITIATING_PM, Settings.DEFAULT_DISABLE_EDITION_INITIATING_PM);
            saveSettingBol(req, Settings.SETTING_VALIDATE_DUPLICATE_CHART_LABEL, Settings.SETTING_VALIDATE_DUPLICATE_CHART_LABEL_DEFAULT);
            saveSettingBol(req, Settings.SETTING_PROJECT_CLOSE_CHANGE_PRIORITY, Settings.SETTING_PROJECT_CLOSE_CHANGE_PRIORITY_DEFAULT);
            saveSettingBol(req, Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER, Settings.SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER_DEFAULT);
            saveSettingBol(req, Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE, Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE_DEFAULT);
            saveSettingBol(req, Settings.SETTING_CREATE_FOLLOWUP_WITH_PROJECT_CREATION, Settings.SETTING_CREATE_FOLLOWUP_WITH_PROJECT_CREATION_DEFAULT);
            saveSettingBol(req, Settings.SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT, Settings.SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT_DEFAULT);
            saveSettingBol(req, Settings.SETTING_SHOW_STATUS_REPORT_IN_INVESTMENT, Settings.SETTING_SHOW_STATUS_REPORT_IN_INVESTMENT_DEFAULT);
            saveSettingBol(req, Settings.SETTING_USE_BAC_PLANNING, Settings.SETTING_USE_BAC_PLANNING_DEFAULT);
            saveSettingBol(req, Settings.SETTING_SHOW_REJECTED_INVESTMENT_IN_CLOSED_PROJECTS, Settings.SETTING_SHOW_REJECTED_INVESTMENT_IN_CLOSED_PROJECTS_DEFAULT);

            saveSettingBol(req, SettingType.BASELINE_DATES);
            saveSettingBol(req, SettingType.CLOSED_PROJECTS_CAPACITY_PLANNING);
            saveSettingBol(req, SettingType.PRELOAD_HOURS_WITH_WORKLOAD);

            // Save general settings
            saveVisibleGenericSettings(req, GeneralSetting.values());
            saveVisibleGenericSettingsAuthorization(req, GeneralSettingAuthorization.values());
            saveVisibleGenericSettingsTimeAndCosts(req, GeneralSettingTimeAndCosts.values());

            // Get and save settings
            saveSettingWithoutSpaces(req, Settings.SETTING_PROJECT_PROBABILITY, Settings.DEFAULT_PROJECT_PROBABILITY);

            saveSetting(req, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET, Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET);
            saveSetting(req, Settings.SETTING_STATUS_REPORT_ORDER, Settings.DEFAULT_STATUS_REPORT_ORDER);
            saveSetting(req, Settings.SETTING_TIME_SESSION, Settings.DEFAULT_TIME_SESSION);
            saveSetting(req, Settings.SETTING_TIME_SESSION_ADVISE, Settings.DEFAULT_TIME_SESSION_ADVISE);
            saveSetting(req, Settings.SETTING_PROJECT_DOCUMENT_STORAGE, Settings.DEFAULT_PROJECT_DOCUMENT_STORAGE);
            saveSetting(req, Settings.SETTING_WORKINGCOSTS_INTERNAL_COST, Settings.DEFAULT_WORKINGCOSTS_INTERNAL_COST);
            saveSetting(req, Settings.SETTING_WORKINGCOSTS_DEPARTMENTS, Settings.DEFAULT_WORKINGCOSTS_DEPARTMENTS);
            saveSetting(req, Settings.SETTING_RESOURCE_MANAGER_APPROVE_OPERATION, Settings.DEFAULT_RESOURCE_MANAGER_APPROVE_OPERATION);
            saveSetting(req, Settings.SETTING_PROJECT_DOCUMENT_FOLDER, Settings.DEFAULT_PROJECT_DOCUMENT_FOLDER);
            saveSetting(req, Settings.SETTING_DAYS_STATUS_REPORT, Settings.DEFAULT_SETTING_DAYS_STATUS_REPORT);
            saveSetting(req, Settings.SETTING_TYPE_APPROBATION_RESOURCE, Settings.SETTING_TYPE_APPROBATION_RESOURCE_DEFAULT);
            saveSetting(req, Settings.SETTING_CALCULATE_EVM, Settings.SETTING_CALCULATE_EVM_DEFAULT);
            saveSetting(req, SettingType.REPORT_EXTENSION);
        }
        catch (Exception e) {
            ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
        }

    	viewAdministration(req, resp, TAB_GENERAL_SETTINGS);
	}
	
	/**
	 * Save Mail Setting
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	private void saveMailSettings(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        try {
            // Get and save boolean settings
            saveSettingBol(req, Settings.SETTING_MAIL_NOTIFICATIONS					, Settings.DEFAULT_MAIL_NOTIFICATIONS				);
            saveSettingBol(req, Settings.SETTING_MAIL_SMTP_TLS     					, Settings.DEFAULT_MAIL_SMTP_TLS     				);
            saveSettingBol(req, Settings.SETTING_MAIL_STATUS_REPORT					, Settings.DEFAULT_MAIL_STATUS_REPORT				);

            // Get and save settings
            saveSetting(req, Settings.SETTING_MAIL_SMTP_HOST    				, Settings.DEFAULT_MAIL_SMTP_HOST    				);
            saveSetting(req, Settings.SETTING_MAIL_SMTP_USER    				, Settings.DEFAULT_MAIL_SMTP_USER    				);
            saveSetting(req, Settings.SETTING_MAIL_SMTP_PASS    				, Settings.DEFAULT_MAIL_SMTP_PASS    				);
            saveSetting(req, Settings.SETTING_MAIL_SMTP_NO_REPLY				, Settings.DEFAULT_MAIL_SMTP_NO_REPLY				);
            saveSetting(req, Settings.SETTING_MAIL_SMTP_PORT    				, Settings.DEFAULT_MAIL_SMTP_PORT    				);
            saveSetting(req, Settings.SETTING_MAIL_DAYS_STATUS_REPORT    		, Settings.DEFAULT_MAIL_DAYS_STATUS_REPORT    		);

            // Save Visual configuration
            saveVisibleGenericSettings(req, EmailType.values());

            // Save Notifications
            saveVisibleGenericSettings(req, NotificationType.values());
            saveSetting(req, SettingType.LOCALE);
        }
        catch (Exception e) {
            ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
        }

    	viewAdministration(req, resp, TAB_MAIL_SETTINGS);
	}
	
	/**
	 * Save Visibility Setting
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @throws ServletException
	 */
	private void saveColumnSettings(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        try {
            // Get and save boolean settings for project
            saveSettingBol(req, Settings.SETTING_PROJECT_INIT_EXTERNAL_COST_SHOW  , Settings.DEFAULT_PROJECT_INIT_EXTERNAL_COST_SHOW  );
            saveSettingBol(req, Settings.SETTING_SHOW_DOCUMENTS				  	  , Settings.DEFAULT_SETTING_SHOW_DOCUMENTS			  );
            saveSettingBol(req, Settings.SETTING_WORKINGCOSTS_QUARTERS            , Settings.DEFAULT_WORKINGCOSTS_QUARTERS         	  );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_RAG               , Settings.DEFAULT_PROJECT_COLUMN_RAG               );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_STATUS            , Settings.DEFAULT_PROJECT_COLUMN_STATUS            );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_ACCOUNTING_CODE   , Settings.DEFAULT_PROJECT_COLUMN_ACCOUNTING_CODE   );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_NAME              , Settings.DEFAULT_PROJECT_COLUMN_NAME              );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_SHORT_NAME        , Settings.DEFAULT_PROJECT_COLUMN_SHORT_NAME        );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_BUDGET            , Settings.DEFAULT_PROJECT_COLUMN_BUDGET            );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_PRIORITY          , Settings.DEFAULT_PROJECT_COLUMN_PRIORITY          );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_POC               , Settings.DEFAULT_PROJECT_COLUMN_POC               );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_BASELINE_START    , Settings.DEFAULT_PROJECT_COLUMN_BASELINE_START    );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_BASELINE_FINISH   , Settings.DEFAULT_PROJECT_COLUMN_BASELINE_FINISH   );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_START             , Settings.DEFAULT_PROJECT_COLUMN_START             );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_FINISH            , Settings.DEFAULT_PROJECT_COLUMN_FINISH            );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_INTERNAL_EFFORT   , Settings.DEFAULT_PROJECT_COLUMN_INTERNAL_EFFORT   );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_EXTERNAL_COST     , Settings.DEFAULT_PROJECT_COLUMN_EXTERNAL_COST     );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_ACTUAL_COST       , Settings.DEFAULT_PROJECT_COLUMN_ACTUAL_COST       );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_KPI       		  , Settings.DEFAULT_PROJECT_COLUMN_KPI       		  );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_BAC       		  , Settings.DEFAULT_PROJECT_COLUMN_BAC       		  );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_PM       		  , Settings.DEFAULT_PROJECT_COLUMN_PM       		  );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_PLANNED_START	  , Settings.DEFAULT_PROJECT_COLUMN_PLANNED_START	  );
            saveSettingBol(req, Settings.SETTING_PROJECT_COLUMN_PLANNED_FINISH	  , Settings.DEFAULT_PROJECT_COLUMN_PLANNED_FINISH	  );

            // Save enum
            saveVisibleGenericSettings(req, VisibilityProjectSetting.values());
            saveVisibleGenericSettings(req, VisibilityInvestmentSetting.values());


            // Get and save boolean settings for investments
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_STATUS         , Settings.DEFAULT_INVESTMENT_COLUMN_STATUS         );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_ACCOUNTING_CODE, Settings.DEFAULT_INVESTMENT_COLUMN_ACCOUNTING_CODE);
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_NAME           , Settings.DEFAULT_INVESTMENT_COLUMN_NAME           );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_SHORT_NAME     , Settings.DEFAULT_INVESTMENT_COLUMN_SHORT_NAME     );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_BUDGET         , Settings.DEFAULT_INVESTMENT_COLUMN_BUDGET         );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_PRIORITY       , Settings.DEFAULT_INVESTMENT_COLUMN_PRIORITY       );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_POC            , Settings.DEFAULT_INVESTMENT_COLUMN_POC            );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_BASELINE_START , Settings.DEFAULT_INVESTMENT_COLUMN_BASELINE_START );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_BASELINE_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_BASELINE_FINISH);
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_START          , Settings.DEFAULT_INVESTMENT_COLUMN_START          );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_FINISH         , Settings.DEFAULT_INVESTMENT_COLUMN_FINISH         );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_INTERNAL_EFFORT, Settings.DEFAULT_INVESTMENT_COLUMN_INTERNAL_EFFORT);
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_EXTERNAL_COST  , Settings.DEFAULT_INVESTMENT_COLUMN_EXTERNAL_COST  );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_BAC			  , Settings.DEFAULT_INVESTMENT_COLUMN_BAC  		  );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_PROGRAM		  , Settings.DEFAULT_INVESTMENT_COLUMN_PROGRAM  	  );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_PLANNED_START , Settings.DEFAULT_INVESTMENT_COLUMN_PLANNED_START );
            saveSettingBol(req, Settings.SETTING_INVESTMENT_COLUMN_PLANNED_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_PLANNED_FINISH);

        } catch (Exception e) {
            ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
        }

    	viewAdministration(req, resp, TAB_VISIBILITY_SETTINGS);
	}
	
	/**
	 * Save String setting Without spaces
	 * 
	 * @param req
	 * @param settingName
	 * @param defaultValue
	 */
    @Deprecated
	private void saveSettingWithoutSpaces(HttpServletRequest req, String settingName, String defaultValue) {
		
		String settingValue = ParamUtil.getString(req, settingName);
		
		if (ValidateUtil.isNotNull(settingValue)) {
			settingValue = settingValue.replace(StringPool.SPACE, StringPool.BLANK);
		}
		saveSettingMake(req, settingName, settingValue, defaultValue);
	}

	/**
	 * Save String setting
	 * 
	 * @param req
	 * @param settingName
	 */
    @Deprecated
	private void saveSetting(HttpServletRequest req, String settingName, String defaultValue) {
		
		String settingValue = ParamUtil.getString(req, settingName);
		saveSettingMake(req, settingName, settingValue, defaultValue);
	}
	
	/**
	 * Save Boolean Setting
	 * 
	 * @param req
	 * @param settingName
	 */
    @Deprecated
	private void saveSettingBol(HttpServletRequest req, String settingName, String defaultValue) {
		
		String settingValue = ParamUtil.getString(req, settingName, "false");
		saveSettingMake(req, settingName, settingValue, defaultValue);
	}

	/**
	 * Save Setting
	 * 
	 * @param req
	 * @param settingName
	 * @param settingValue
	 */
    @Deprecated
	private void saveSettingMake(HttpServletRequest req, String settingName, String settingValue, String defaultValue) {


        try {
            SettingLogic settingLogic = new SettingLogic();
            Setting setting = settingLogic.findSetting(getCompany(req), settingName);
			
			if (!StringPool.BLANK.equals(settingValue)) {
				if (setting == null) {
					setting = new Setting();
					setting.setName(settingName);
					setting.setCompany(getCompany(req));
				}
				
				setting.setValue(settingValue); 
				settingLogic.save(setting);
			}
			else { //if the value is null delete setting
				if (setting != null) {
					settingLogic.delete(setting);
				}
			}
			
		}
		catch (Exception e) { ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e); }
	}

	/**
	 * View settings
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void viewAdministration(HttpServletRequest req, HttpServletResponse resp, int selectedTab) throws ServletException, IOException {
		
		PerformingOrgLogic performingOrgLogic 		= new PerformingOrgLogic();
					
		try {
			// Logic PO
			List<Performingorg> perfOrgs = performingOrgLogic.findByRelation(Performingorg.COMPANY, getCompany(req));

			req.setAttribute("settings", SettingUtil.getSettings(getCompany(req)));
			req.setAttribute("perforgs", perfOrgs);
		} catch (Exception e) {
			LOGGER.error("Retrieve settings" ,e);
		}
		req.setAttribute("selectedTab", selectedTab);
		req.setAttribute("documentationList", DocumentUtils.getDocumentationList(req));

		req.setAttribute("title", getResourceBundle(req).getString("settings"));
		forward("/index.jsp?nextForm=administration/administration", req, resp);
	}
	
	/**
	 * Save required fields Settings
	 * 
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 */
	private void saveRequiredSettings(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		
		// Get and save boolean required settings
		saveSettingBol(req, Settings.SETTING_INVESTMENT_REQUIRED_CATEGORY, Settings.DEFAULT_INVESTMENT_REQUIRED_CATEGORY  );
		saveSettingBol(req, Settings.SETTING_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER, Settings.DEFAULT_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER );
		saveSettingBol(req, Settings.SETTING_INVESTMENT_REQUIRED_BUDGET_YEAR, Settings.DEFAULT_INVESTMENT_REQUIRED_BUDGET_YEAR );
		saveSettingBol(req, Settings.SETTING_INVESTMENT_REQUIRED_STAGE_GATE, Settings.DEFAULT_INVESTMENT_REQUIRED_STAGE_GATE);
		saveSettingBol(req, Settings.SETTING_INVESTMENT_REQUIRED_PRIORITY, Settings.DEFAULT_INVESTMENT_REQUIRED_PRIORITY );
		saveSettingBol(req, Settings.SETTING_INVESTMENT_REQUIRED_SPONSOR, Settings.DEFAULT_INVESTMENT_REQUIRED_SPONSOR );

        try {

            // Save Required Field Setting
            saveVisibleGenericSettings(req, RequiredFieldSetting.values());
        }
        catch (Exception e) {
            ExceptionUtil.evalueException(req, getResourceBundle(req), LOGGER, e);
        }

		viewAdministration(req, resp, TAB_REQUIRED_SETTINGS);
	}

    /**
     * Save generic settings if settings is visible
     *
     * @param req
     * @param fieldSettings
     * @throws Exception
     */
    private void saveVisibleGenericSettings(HttpServletRequest req, GenericSetting[] fieldSettings) throws Exception {

        for (GenericSetting fieldSetting : fieldSettings) {

            // Only save is setting is visible
            if (fieldSetting.isVisible()) {

                if (TypeSetting.CHECKBOX.equals(fieldSetting.getTypeSetting())) {

                    saveSettingBol(req, fieldSetting);
                } else {
                    saveSetting(req, fieldSetting);
                }
            }
        }
    }

    /**
     * Save generic settings authorization if settings is visible
     *
     * @param req
     * @param fieldSettings
     * @throws Exception
     */
    private void saveVisibleGenericSettingsAuthorization(HttpServletRequest req, GenericSetting[] fieldSettings) throws Exception {

        for (GenericSetting fieldSetting : fieldSettings) {

            // Only save is setting is visible
            if (fieldSetting.isVisible()) {

                if (TypeSetting.CHECKBOX.equals(fieldSetting.getTypeSetting())) {

                    saveSettingBol(req, fieldSetting);
                } else {
                    saveSetting(req, fieldSetting);
                }
            }
        }
    }

    /**
     * Save generic settings time and cost if settings is visible
     *
     * @param req
     * @param fieldSettings
     * @throws Exception
     */
    private void saveVisibleGenericSettingsTimeAndCosts(HttpServletRequest req, GenericSetting[] fieldSettings) throws Exception {

        for (GenericSetting fieldSetting : fieldSettings) {

            // Only save is setting is visible
            if (fieldSetting.isVisible()) {

                if (TypeSetting.CHECKBOX.equals(fieldSetting.getTypeSetting())) {

                    saveSettingBol(req, fieldSetting);
                } else {
                    saveSetting(req, fieldSetting);
                }
            }
        }
    }
}