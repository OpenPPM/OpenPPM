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
 * File: Settings.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.common;

import es.sm2.openppm.core.logic.setting.common.TypeSetting;
import es.sm2.openppm.core.reports.annotations.ReportExtension;
import es.sm2.openppm.utils.StringPool;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;

public final class Settings {

	public enum SettingType implements GenericSetting {
		BASELINE_DATES("true",
		    			"setting.baseline_dates", 
		    			"setting.baseline_dates_info"),

		DOCUMENT_STORAGE("setting.project.document.storage", //TODO hacer update del nombre en bbdd
		    			"mixed", 
		    			"setting.project.document.storage", 
		    			"setting.project.document.storage_info"),

		CLOSED_PROJECTS_CAPACITY_PLANNING("true",
						"setting.show_closed_projects_capacity_planning", 
						"setting.show_closed_projects_capacity_planning_info"),

        CAPACITY_PLANNING_LEVEL1("30"),
        CAPACITY_PLANNING_LEVEL2("60"),
        CAPACITY_PLANNING_LEVEL3("100"),
        CAPACITY_PLANNING_LEVEL1_COLOR("#FC7383"),
        CAPACITY_PLANNING_LEVEL2_COLOR("#FABA5A"),
        CAPACITY_PLANNING_LEVEL3_COLOR("#7DEED9"),
        
        CAPACITY_PLANNING_LEVEL1_COLOR_TEXT("#E60000"),
        CAPACITY_PLANNING_LEVEL2_COLOR_TEXT("#E60000"),
        CAPACITY_PLANNING_LEVEL3_COLOR_TEXT("#E60000"),
        
        CAPACITY_PLANNING_OUTOFRANGE_COLOR("#FABA5A"),
        CAPACITY_PLANNING_OUTOFRANGE_TEXT("#999966"),

        PRELOAD_HOURS_WITH_WORKLOAD("true"),
        MIGRATION_DOCUMENTS("true"),
        REPORT_EXTENSION(ReportExtension.ODT.data()),

        LOCALE(LOCALE_LANGUAGE_DEFAULT+StringPool.UNDERLINE+Settings.LOCALE_COUNTRY_DEFAULT);

		private String name;
    	private String defaultValue;
    	private String message;
    	private String messageInfo;
        private TypeSetting typeSetting;

        private SettingType(String defaultValue) {

            this(null, defaultValue, null, null);
        }
        private SettingType(String defaultValue, String message,
                            String messageInfo) {
            this(null, defaultValue, message, messageInfo);
        }

    	private SettingType(String name, String defaultValue, String message,
    			String messageInfo) {
    		
    		this.name 			= name;
    		this.defaultValue 	= defaultValue;
    		this.message 		= message;
    		this.messageInfo 	= messageInfo;
    	}

		public Enum[] getEnumElements() {
			return null;
		}

        public boolean isVisible() {
            return true;
        }

        public TypeSetting getTypeSetting() {
            return this.typeSetting == null?TypeSetting.CHECKBOX: this.typeSetting;
        }

    	/**
		 * @return the name
		 */
		public String getName() {
			return name == null?this.name():this.name;
		}

		/**
		 * @return the defaultValue
		 */
		public String getDefaultValue() {
			return defaultValue;
		}

		/**
		 * @return the messageInfo
		 */
		public String getMessageInfo() {
			return this.messageInfo == null ?"SETTING."+this.name()+"_INFO":messageInfo;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
            return this.message == null ?"SETTING."+this.name():message;
		}
	}
	
	
	private static final Logger LOGGER = Logger.getLogger(Settings.class);
	
	/*********************************************************************/
	/*************************** BY COMPANY ******************************/
	/*********************************************************************/
	// ***** SETTINGS NOT VISIBLE
	public static final String SETTING_INFORMATION					= "information-app";
	
	public static final String SETTING_FOLLOWUP_INFORMATION_CSV		= "setting.followup_information_csv";
	public static final String DEFAULT_FOLLOWUP_INFORMATION_CSV		= "false";
	
	public static final String SETTING_CHANGE_REQUEST_CLASSNAME		= "setting.change_request_classname";
	public static final String DEFAULT_CHANGE_REQUEST_CLASSNAME		= "es.sm2.openppm.logic.change.ChangeRequest";
	
	public static final String SETTING_PROJECT_CHARTER_CLASSNAME	= "setting.project.charter.classname";
	public static final String DEFAULT_PROJECT_CHARTER_CLASSNAME	= "es.sm2.openppm.logic.charter.ProjectCharterTalaia";

	public static final String SETTING_PROJECT_EXECUTIVE_CLASSNAME	= "setting.project.executive.classname";
	public static final String DEFAULT_PROJECT_EXECUTIVE_CLASSNAME	= null;
	
	public static final String SETTING_PROJECT_CHARTER_EXTENSION	= "setting.project.charter.extension";
	public static final String DEFAULT_PROJECT_CHARTER_EXTENSION	= ".docx";
	
	public static final String SETTING_PROJECT_CLOSE_CLASSNAME		= "setting.project.close.classname";
	public static final String DEFAULT_PROJECT_CLOSE_CLASSNAME		= "";
	
	public static final String SETTING_PROJECT_CLOSE_EXTENSION		= "setting.project.close.extension";
	public static final String DEFAULT_PROJECT_CLOSE_EXTENSION		= ".docx";
	
	public static final String SETTING_LOGO		= "setting.logo";
	public static final String DEFAULT_LOGO		= "/head/OpenPPM.jpg";
	
	public static final String SETTING_PERFORMANCE_REPORTS		= "setting.performance_reports";
	public static final String DEFAULT_PERFORMANCE_REPORTS		= "false";
	// ***** END NO VISIBLE
	
	// SECURITY
	public static final String SETTING_LDAP_SERVER 			= "setting.ldap.server";
	public static final String SETTING_LDAP_PORT 			= "setting.ldap.port";
	public static final String SETTING_LDAP_ACCOUNT 		= "setting.ldap.account";
	public static final String SETTING_LDAP_PASSWORD 		= "setting.ldap.password";
	public static final String SETTING_LDAP_BASE 			= "setting.ldap.base";
	public static final String SETTING_LDAP_IDENTIFICATOR 	= "setting.ldap.identificator";
	public static final String SETTING_LDAP_NAME 			= "setting.ldap.name";
	public static final String SETTING_LDAP_LASTNAME 		= "setting.ldap.lastname";
	public static final String SETTING_LDAP_MAIL		 	= "setting.ldap.mail";
	
	public static final String SETTING_LOGIN_TYPE 			= "setting.login.type";
	public static final String DEFAULT_LOGIN_TYPE 			= Constants.SECURITY_LOGIN_BBDD;
	
	public static final String DEFAULT_LOGOFF_URL_ACTION	= "logoff";
	public static final String SETTING_LOGOFF_URL 			= "setting.logoff.url";
	public static final String DEFAULT_LOGOFF_URL 			= "login?a="+DEFAULT_LOGOFF_URL_ACTION;
	
	// Use disable to not delete projects physically
	public static final String SETTING_DISABLE_PROJECT		 	= "setting.project.disable";
	public static final String DEFAULT_DISABLE_PROJECT 			= "FALSE";
	
	// Disable edition in initiation for the Project Manager if the project is not in initiating state
	public static final String SETTING_DISABLE_EDITION_INITIATING_PM	= "setting.disable.edition_initiating_pm";
	public static final String DEFAULT_DISABLE_EDITION_INITIATING_PM	= "FALSE";
	
	// Mail
	public static final String SETTING_MAIL_NOTIFICATIONS 					= "setting.mail.notification";
	public static final String SETTING_MAIL_SMTP_HOST 						= "setting.mail.smtp.host";
	public static final String SETTING_MAIL_SMTP_USER 						= "setting.mail.smtp.user";
	public static final String SETTING_MAIL_SMTP_PASS 						= "setting.mail.smtp.pass";
	public static final String SETTING_MAIL_SMTP_NO_REPLY					= "setting.mail.smtp.no_reply";
	public static final String SETTING_MAIL_SMTP_PORT 						= "setting.mail.smtp.port";
	public static final String SETTING_MAIL_SMTP_TLS 						= "setting.mail.smtp.tls";
	public static final String SETTING_MAIL_STATUS_REPORT 					= "setting.mail.notification_status_report";
	public static final String SETTING_MAIL_DAYS_STATUS_REPORT 				= "setting.mail.notification_days_status_report";
	
	// Column (Visibility)
	public static final String SETTING_PROJECT_COLUMN_RAG 				= "setting.project.column.rag";
	public static final String SETTING_PROJECT_COLUMN_STATUS 			= "setting.project.column.status";
	public static final String SETTING_PROJECT_COLUMN_ACCOUNTING_CODE 	= "setting.project.column.accounting_code";
	public static final String SETTING_PROJECT_COLUMN_NAME			 	= "setting.project.column.name";
	public static final String SETTING_PROJECT_COLUMN_SHORT_NAME	 	= "setting.project.column.short_name";
	public static final String SETTING_PROJECT_COLUMN_DESCRIPTION	 	= "setting.project.column.description";
	public static final String SETTING_PROJECT_COLUMN_BUDGET		 	= "setting.project.column.budget";
	public static final String SETTING_PROJECT_COLUMN_PRIORITY		 	= "setting.project.column.priority";
	public static final String SETTING_PROJECT_COLUMN_POC		 		= "setting.project.column.poc";
	public static final String SETTING_PROJECT_COLUMN_BASELINE_START	= "setting.project.column.baseline_start";
	public static final String SETTING_PROJECT_COLUMN_BASELINE_FINISH	= "setting.project.column.baseline_finish";
	public static final String SETTING_PROJECT_COLUMN_START				= "setting.project.column.start";
	public static final String SETTING_PROJECT_COLUMN_FINISH			= "setting.project.column.finish";
	public static final String SETTING_PROJECT_COLUMN_INTERNAL_EFFORT	= "setting.project.column.internal_effort";
	public static final String SETTING_PROJECT_COLUMN_EXTERNAL_COST		= "setting.project.column.external_cost";
	public static final String SETTING_PROJECT_COLUMN_ACTUAL_COST		= "setting.project.column.actual_cost";
	public static final String SETTING_PROJECT_COLUMN_KPI				= "setting.project.column.kpi";
	public static final String SETTING_PROJECT_COLUMN_PROGRAM			= "setting.project.column.program";
	public static final String SETTING_PROJECT_COLUMN_BAC				= "setting.project.bac";
	public static final String SETTING_PROJECT_COLUMN_PM				= "setting.project.column.pm";
	public static final String SETTING_PROJECT_COLUMN_PLANNED_START		= "setting.project.column.planned_start";
	public static final String SETTING_PROJECT_COLUMN_PLANNED_FINISH	= "setting.project.column.planned_finish";
	
	public static final String SETTING_INVESTMENT_COLUMN_STATUS 			= "setting.investment.column.status";
	public static final String SETTING_INVESTMENT_COLUMN_ACCOUNTING_CODE 	= "setting.investment.column.accounting_code";
	public static final String SETTING_INVESTMENT_COLUMN_NAME			 	= "setting.investment.column.name";
	public static final String SETTING_INVESTMENT_COLUMN_SHORT_NAME	 		= "setting.investment.column.short_name";
	public static final String SETTING_INVESTMENT_COLUMN_BUDGET		 		= "setting.investment.column.budget";
	public static final String SETTING_INVESTMENT_COLUMN_PRIORITY		 	= "setting.investment.column.priority";
	public static final String SETTING_INVESTMENT_COLUMN_POC		 		= "setting.investment.column.probability";
	public static final String SETTING_INVESTMENT_COLUMN_BASELINE_START		= "setting.investment.column.baseline_start";
	public static final String SETTING_INVESTMENT_COLUMN_BASELINE_FINISH	= "setting.investment.column.baseline_finish";
	public static final String SETTING_INVESTMENT_COLUMN_START				= "setting.investment.column.start";
	public static final String SETTING_INVESTMENT_COLUMN_FINISH				= "setting.investment.column.finish";
	public static final String SETTING_INVESTMENT_COLUMN_INTERNAL_EFFORT	= "setting.investment.column.internal_effort";
	public static final String SETTING_INVESTMENT_COLUMN_EXTERNAL_COST		= "setting.investment.column.external_cost";
	public static final String SETTING_INVESTMENT_COLUMN_BAC				= "setting.investment.bac";
	public static final String SETTING_INVESTMENT_COLUMN_PROGRAM			= "setting.investment.column.program";
	public static final String SETTING_INVESTMENT_COLUMN_PLANNED_START		= "setting.investment.column.planned_start";
	public static final String SETTING_INVESTMENT_COLUMN_PLANNED_FINISH		= "setting.investment.column.planned_finish";
	
	// General
	public static final String SETTING_TIME_SESSION						= "setting.time.session";
	public static final String SETTING_TIME_SESSION_ADVISE				= "setting.time.session.advise";
	public static final String SETTING_PROJECT_PROBABILITY				= "setting.project.probability";
	public static final String SETTING_PROJECT_INIT_EXTERNAL_COST_SHOW	= "setting.project.initiating.external_costs.show";
	public static final String SETTING_WORKINGCOSTS_QUARTERS			= "setting.workingcosts.quarters";
	public static final String SETTING_WORKINGCOSTS_INTERNAL_COST		= "setting.workingcosts.internal_costs";
	public static final String SETTING_WORKINGCOSTS_DEPARTMENTS			= "setting.workingcosts.departments";
	public static final String SETTING_LAST_LEVEL_FOR_APPROVE_SHEET		= "setting.last_level_for_approve_sheet";
	public static final String SETTING_STATUS_REPORT_ORDER				= "setting.status_report_order";
	public static final String SETTING_CSV_COLUMN_IDPROJECT				= "setting.colummn.csv.idproject";
	public static final String SETTING_PROJECT_EXCEEDED_BUDGET			= "setting.project.exceeded_budget";
	public static final String SETTING_SHOW_DOCUMENTS					= "setting.show_documents";
	public static final String SETTING_SHOW_PRIORIZATION_PROGRAMS		= "setting.show_priorization_programs";
	public static final String SETTING_SHOW_SCENARIO_ANALYSIS			= "setting.show_scenario_analysis";
	public static final String SETTING_DAYS_STATUS_REPORT				= "setting.days_status_report";
	
	public static final String SETTING_PROJECT_DOCUMENT_STORAGE			= "setting.project.document.storage";
	public static final String DEFAULT_PROJECT_DOCUMENT_STORAGE			= "link";
	
	public static final String SETTING_RESOURCE_MANAGER_APPROVE_OPERATION	= "setting.rm_approve_operation";
	public static final String DEFAULT_RESOURCE_MANAGER_APPROVE_OPERATION	= "app3";
	
	public static final String SETTING_PROJECT_DOCUMENT_FOLDER	= "setting.project.document.folder";
	public static final String DEFAULT_PROJECT_DOCUMENT_FOLDER	= "/docs-talaia";
	
	public static final String SETTING_SHOW_INACTIVATED				= "SETTING_SHOW_INACTIVATED";
	public static final String SETTING_SHOW_INACTIVATED_DEFAULT		= "true";
	
	public static final String SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER			= "SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER";
	public static final String SETTING_EXCLUDE_HOURS_RESOURCE_WITH_PROVIDER_DEFAULT	= "false";
	
	public static final String SETTING_VALIDATE_DUPLICATE_CHART_LABEL			= "SETTING_VALIDATE_DUPLICATE_CHART_LABEL";
	public static final String SETTING_VALIDATE_DUPLICATE_CHART_LABEL_DEFAULT	= "false";
	
	public static final String SETTING_PROJECT_CLOSE_CHANGE_PRIORITY			= "setting.project.close_change_priority";
	public static final String SETTING_PROJECT_CLOSE_CHANGE_PRIORITY_DEFAULT	= "true";
	
	public static final String SETTING_TYPE_APPROBATION_RESOURCE			= "SETTING_TYPE_APPROBATION_RESOURCE";
	public static final String SETTING_TYPE_APPROBATION_RESOURCE_DEFAULT	= Constants.TYPE_APPROBATION_RM;
	
	public static final String SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE			= "SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE";
	public static final String SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE_DEFAULT	= "false";
	
	public static final String SETTING_CREATE_FOLLOWUP_WITH_PROJECT_CREATION			= "SETTING_SHOW_PLANNED_VALUE";
	public static final String SETTING_CREATE_FOLLOWUP_WITH_PROJECT_CREATION_DEFAULT	= "true";
	
	public static final String SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT					= "SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT";
	public static final String SETTING_SHOW_EXECUTIVEREPORT_IN_STATUS_REPORT_DEFAULT			= "true";
	
	public static final String SETTING_SHOW_STATUS_REPORT_IN_INVESTMENT							= "SETTING_SHOW_STATUS_REPORT_IN_INVESTMENT";
	public static final String SETTING_SHOW_STATUS_REPORT_IN_INVESTMENT_DEFAULT					= "true";
	
	public static final String SETTING_SHOW_REJECTED_INVESTMENT_IN_CLOSED_PROJECTS				= "SETTING_SHOW_REJECTED_INVESTMENT_IN_CLOSED_PROJECTS";
	public static final String SETTING_SHOW_REJECTED_INVESTMENT_IN_CLOSED_PROJECTS_DEFAULT		= "false";
	
	// EVM
	public static final String SETTING_CALCULATE_EVM					= "SETTING_CALCULATE_EVM";
	public static final String SETTING_CALCULATE_EVM_DEFAULT			= Constants.CALCULATE_EVM_HOURS_COST;
	public static final String SETTING_USE_BAC_PLANNING					= "SETTING_USE_BAC_PLANNING";
	public static final String SETTING_USE_BAC_PLANNING_DEFAULT			= "false";
	
	// Fields (Required)
	public static final String SETTING_INVESTMENT_REQUIRED_CATEGORY				= "category";
	public static final String SETTING_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER	= "functional_manager";
	public static final String SETTING_INVESTMENT_REQUIRED_SPONSOR				= "sponsor";
	public static final String SETTING_INVESTMENT_REQUIRED_BUDGET_YEAR			= "project.budget_year";
	public static final String SETTING_INVESTMENT_REQUIRED_PRIORITY				= "project.priority";
	public static final String SETTING_INVESTMENT_REQUIRED_STAGE_GATE			= "stage_gate";
	
	// Default values for Mail
	public static final String DEFAULT_MAIL_NOTIFICATIONS 					= "false";
	public static final String DEFAULT_MAIL_SMTP_HOST 						= "";
	public static final String DEFAULT_MAIL_SMTP_USER 						= "";
	public static final String DEFAULT_MAIL_SMTP_PASS 						= "";
	public static final String DEFAULT_MAIL_SMTP_NO_REPLY					= "";
	public static final String DEFAULT_MAIL_SMTP_PORT 						= "";
	public static final String DEFAULT_MAIL_SMTP_TLS 						= "false";
	public static final String DEFAULT_MAIL_STATUS_REPORT 					= "true";
	public static final String DEFAULT_MAIL_DAYS_STATUS_REPORT 				= "1";
	
	// Default values for Column (Visibility)
	public static final String DEFAULT_PROJECT_COLUMN_RAG 				= "true";
	public static final String DEFAULT_PROJECT_COLUMN_STATUS 			= "true";
	public static final String DEFAULT_PROJECT_COLUMN_ACCOUNTING_CODE 	= "true";
	public static final String DEFAULT_PROJECT_COLUMN_NAME			 	= "true";
	public static final String DEFAULT_PROJECT_COLUMN_SHORT_NAME	 	= "true";
	public static final String DEFAULT_PROJECT_COLUMN_DESCRIPTION	 	= "true";
	public static final String DEFAULT_PROJECT_COLUMN_BUDGET		 	= "true";
	public static final String DEFAULT_PROJECT_COLUMN_PRIORITY		 	= "false";
	public static final String DEFAULT_PROJECT_COLUMN_POC		 		= "true";
	public static final String DEFAULT_PROJECT_COLUMN_BASELINE_START	= "true";
	public static final String DEFAULT_PROJECT_COLUMN_BASELINE_FINISH	= "true";
	public static final String DEFAULT_PROJECT_COLUMN_START				= "true";
	public static final String DEFAULT_PROJECT_COLUMN_FINISH			= "true";
	public static final String DEFAULT_PROJECT_COLUMN_INTERNAL_EFFORT	= "false";
	public static final String DEFAULT_PROJECT_COLUMN_EXTERNAL_COST		= "true";
	public static final String DEFAULT_PROJECT_COLUMN_ACTUAL_COST		= "false";
	public static final String DEFAULT_PROJECT_COLUMN_KPI				= "false";
	public static final String DEFAULT_PROJECT_COLUMN_PROGRAM			= "true";
	public static final String DEFAULT_PROJECT_COLUMN_BAC				= "false";
	public static final String DEFAULT_PROJECT_COLUMN_PM				= "false";
	public static final String DEFAULT_PROJECT_COLUMN_PLANNED_START		= "false";
	public static final String DEFAULT_PROJECT_COLUMN_PLANNED_FINISH	= "false";
	
	
	public static final String DEFAULT_INVESTMENT_COLUMN_STATUS 			= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_ACCOUNTING_CODE 	= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_NAME				= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_SHORT_NAME	 		= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_BUDGET		 		= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_PRIORITY			= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_POC		 		= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_BASELINE_START		= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_BASELINE_FINISH	= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_START				= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_FINISH				= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_INTERNAL_EFFORT	= "false";
	public static final String DEFAULT_INVESTMENT_COLUMN_EXTERNAL_COST		= "true";
	public static final String DEFAULT_INVESTMENT_COLUMN_BAC				= "false";
	public static final String DEFAULT_INVESTMENT_COLUMN_PROGRAM			= "false";
	public static final String DEFAULT_INVESTMENT_COLUMN_PLANNED_START		= "false";
	public static final String DEFAULT_INVESTMENT_COLUMN_PLANNED_FINISH		= "false";
	
	// Default values for General
	public static final String DEFAULT_TIME_SESSION							= "180";
	public static final String DEFAULT_TIME_SESSION_ADVISE					= "170";
	public static final String DEFAULT_NEW_PROJECT_RESOURCE_BY_PM_FTE		= "50";
	public static final String DEFAULT_PROJECT_PROBABILITY					= "";
	public static final String DEFAULT_PROJECT_INIT_EXTERNAL_COST_SHOW		= "true";
	public static final String DEFAULT_WORKINGCOSTS_QUARTERS				= "false";
	public static final String DEFAULT_WORKINGCOSTS_INTERNAL_COST			= "30";
	public static final String DEFAULT_WORKINGCOSTS_DEPARTMENTS				= "";
	public static final String DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET			= "0";
	public static final String DEFAULT_STATUS_REPORT_ORDER					= "asc";
	public static final String DEFAULT_CSV_COLUMN_IDPROJECT					= "false";
	public static final String DEFAULT_PROJECT_EXCEEDED_BUDGET				= "false";
	public static final String DEFAULT_SETTING_SHOW_DOCUMENTS				= "false";
	public static final String DEFAULT_SETTING_SHOW_PRIORIZATION_PROGRAMS	= "true";
	public static final String DEFAULT_SETTING_SHOW_SCENARIO_ANALYSIS		= "true";
	public static final String DEFAULT_SETTING_DAYS_STATUS_REPORT			= "4";
	
	// Default values for Fields (Required)
	public static final String DEFAULT_INVESTMENT_REQUIRED_CATEGORY				= "false";
	public static final String DEFAULT_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER	= "false";
	public static final String DEFAULT_INVESTMENT_REQUIRED_SPONSOR				= "false";
	public static final String DEFAULT_INVESTMENT_REQUIRED_BUDGET_YEAR			= "false";
	public static final String DEFAULT_INVESTMENT_REQUIRED_PRIORITY				= "false";
	public static final String DEFAULT_INVESTMENT_REQUIRED_STAGE_GATE			= "false";
	
	/*********************************************************************/
	/************************* BY PROPERTIES *****************************/
	/*********************************************************************/
	
	public static final ResourceBundle properties = ResourceBundle.getBundle("openppm");
	
	// Default Locale language and country
	public static final String LOCALE_LANGUAGE_DEFAULT	= notRequired("locale.language.default", "en");
	public static final String LOCALE_COUNTRY_DEFAULT	= notRequired("locale.country.default", "US");
	public static final String[] LOCALE_AVAILABLES		= notRequired("locale.available", "en_US").split(",");
	
	// Performance
	public static final boolean PERFORMANCE_COMPRESS_HTML	= Boolean.valueOf(notRequired("performance.compress_html",	"false"));
	public static final boolean PERFORMANCE_COMPRESS_JS		= Boolean.valueOf(notRequired("performance.compress_js",	"false"));
	
	public static final boolean DEVELOPER = Boolean.valueOf(notRequired("developer",	"false"));

	public static final String LOGO = notRequired("LOGO", null);
	public static final String URL_LOGO = LOGO == null?"images"+DEFAULT_LOGO:"imageData/"+LOGO;

	// Login 
	public static final Integer MAX_LOGIN_ATTEMPS	= Integer.parseInt(notRequired("login.max_login_attempts", "3"));
	public static final int BLOCK_WAIT_TIME			= Integer.parseInt(notRequired("login.block_wait_time", "3")); // Minutes
	public static final String APLICATION_ROL		= notRequired("login.openppm.rol", "openppm");
	
	// Path
	public static final String TEMP_DIR_GENERATE_DOCS	= notRequired("temp_dir_generate_docs", System.getProperty("java.io.tmpdir"));
	
	// Time show for information message by default (in seconds)
	public static final int INFO_TIME_DEFAULT = Integer.parseInt(notRequired("time.info_default","4"));
	
	// Currency
	public static final boolean FUNDINGSOURCE_CURRENCY		= Boolean.valueOf(notRequired("fundingsource.currency", "false"));
	public static final boolean OPTIONAL_CURRENCY			= Boolean.valueOf(notRequired("optional.currency", "false"));
	
	// REPORTS
	public static final boolean REPORT_EXECUTIVE		= Boolean.valueOf(notRequired("REPORT.EXECUTIVE",	"false"));

    // Version
	public static final String VERSION_APP = notRequired(Constants.Version.VERSION_APP.name(), "");
	public static final String VERSION_NAME_APP = notRequired(Constants.Version.VERSION_NAME_APP.name(), "");
	public static final String EASTER_EGG = notRequired(Constants.Version.EASTER_EGG.name(), "#");

	public static final boolean SHOW_TIMESHEET_COLOR	= Boolean.valueOf(notRequired("SHOW_TIMESHEET_COLOR",	"true"));
	
	// Panels
	public static final boolean SHOW_PANEL_LESSONS_LEARNED	    = Boolean.valueOf(notRequired("SHOW_PANEL_LESSONS_LEARNED",	"true"));
	public static final boolean SHOW_PANEL_RISK_ADJUST		    = Boolean.valueOf(notRequired("SHOW_PANEL_RISK_ADJUST",	"false"));
    public static final boolean SHOW_TABLE_WORKINGCOST_CLOSURE	= Boolean.valueOf(notRequired("SHOW_TABLE_WORKINGCOST_CLOSURE",	"true"));

	// Signature email
	public static final String SHOW_EMAIL_SIGNATURE_CONFIGURATION			= "SHOW_EMAIL_SIGNATURE_CONFIGURATION";
	public static final String DEFAULT_SHOW_EMAIL_SIGNATURE_CONFIGURATION	= "false";

    // Modules
    public static final boolean SHOW_NOTIFICATIONS	      = Boolean.valueOf(notRequired("SHOW_NOTIFICATIONS", "false"));
    public static final boolean SHOW_LEARNED_LESSONS      = Boolean.valueOf(notRequired("SHOW_LEARNED_LESSONS", "false"));
    public static final boolean SHOW_AUDIT                = Boolean.valueOf(notRequired("SHOW_AUDIT", "false"));
    public static final boolean SHOW_MANAGE_OPERATIONS    = Boolean.valueOf(notRequired("SHOW_MANAGE_OPERATIONS", "false"));

	private static final String notRequired(String key, String defaultValue) {
		
		String value = null;
		
		try { value = properties.getString(key); }
		catch (Exception e) { value = defaultValue; }
		
		LOGGER.info("Value for key '" + key + "': " + value);

		return value;
	}
}

