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
 * File: Constants.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

/**
 * 
 */
package es.sm2.openppm.core.common;

import es.sm2.openppm.utils.functions.ValidateUtil;

import java.util.Locale;

/**
 * @author juanma.lopez
 *
 */
public class Constants {

	public enum ProbabilityRisk {
		SEVERE(90),
		HIGH(70),
		MODERATE(50),
		MINOR(30),
		TRIVIAL(10);
		
		private Integer value;
		
		private ProbabilityRisk(Integer value) {
			this.value = value;
		}

		/**
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}
	}
	
	public enum ImpactRisk {
		TRIVIAL(5),
		MINOR(10),
		MODERATE(20),
		HIGH(40),
		SEVERE(80);
		
		private Integer value;
		
		private ImpactRisk(Integer value) {
			this.value = value;
		}

		/**
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}
	}

    public enum RiskRating {
        HIGH(1500, "risk_high"),
        MEDIUM(500, "risk_medium"),
        NORISK(0, "no_risk"),
        LOW("risk_low");

        private Integer value;
        private String name;

        private RiskRating(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        private RiskRating(String name){ this.name = name; }

        /**
         * @return the value
         */
        public Integer getValue() {
            return value;
        }

        /**
         * @return the name
         */
        public String getName() { return name; }
    }

    public enum LessonProcessGroup {

        INITIATION,
        PLANNING,
        EXECUTION,
        MONITORING_AND_CONTROL,
        CLOSURE;

        private LessonProcessGroup(){}
    }

    public enum LessonType{

        OPPORTUNITY,
        THREAT;

        private LessonType(){}
    }

    public enum Version{

        VERSION_APP,
        VERSION_NAME_APP,
        EASTER_EGG;

        private Version(){}
    }

    public static final int ROLE_RESOURCE 	= 1; // Resource
	public static final int ROLE_PM 		= 2; // Project Manager
	public static final int ROLE_IM 		= 3; // Investment Manager
	public static final int ROLE_FM 		= 4; // Functional Managers
	public static final int ROLE_PROGM		= 5; // Program Managers
	public static final int ROLE_RM 		= 6; // Resource Managers
	public static final int ROLE_PMO 		= 7; // PMO
	public static final int ROLE_SPONSOR	= 8; // Sponsor
	public static final int ROLE_PORFM		= 9; // Porfolio Managers
	public static final int ROLE_ADMIN		= 10; // Administrator of company
	public static final int ROLE_STAKEHOLDER= 11; // Stakeholder
    public static final int ROLE_LOGISTIC   = 12; // Logistic
	
	// Projects tabs
	public static final int TAB_INITIATION	    = 1;
	public static final int TAB_PLAN		    = 2;
	public static final int TAB_RISK		    = 3;
	public static final int TAB_CONTROL		    = 4;
	public static final int TAB_PROCURAMENT	    = 5;
	public static final int TAB_CLOSURE		    = 6;
	public static final int TAB_INVESTMENT	    = 7;
    public static final int TAB_LEARNED_LESSON  = 8;
	
	// Security login
	public static final String SECURITY_LOGIN_LDAP					= "LDAP";
	public static final String SECURITY_LOGIN_BBDD					= "BBDD";
	public static final String SECURITY_LOGIN_MIXED 				= "MIXED";
	public static final String SECURITY_LOGIN_EXTERNAL				= "EXTERNAL";
	public static final String SECURITY_LOGIN_EXTERNAL_SEARCH_LDAP	= "EXTERNAL_SEARCH_LDAP";
	
	// Project or Investment
	public static final char TYPE_PROJECT 		= 'P';
	public static final char TYPE_INVESTMENT 	= 'I';
	
	// Status for Project
    public static final String STATUS_INITIATING    = "INITIATING";
    public static final String STATUS_PLANNING      = "PLANNING";
    public static final String STATUS_CONTROL       = "CONTROL";
    public static final String STATUS_CLOSED        = "CLOSED";
    public static final String STATUS_ARCHIVED      = "ARCHIVED";
	
	// Status for investment
	public static final String INVESTMENT_IN_PROCESS	= "IN_PROCESS";
	public static final String INVESTMENT_APPROVED	    = "APPROVED";
	public static final String INVESTMENT_CLOSED		= "CLOSED";
    public static final String INVESTMENT_INACTIVATED   = "INACTIVATED";
	public static final String INVESTMENT_REJECTED	    = "REJECTED";
    public static final String INVESTMENT_ARCHIVED	    = "ARCHIVED";

	
	// Type for costs
	public static final int COST_TYPE_EXPENSE	= 1;
	public static final int COST_TYPE_DIRECT	= 2;

	public static final String PATH_SEPARATOR = System.getProperty("file.separator");
	
	// Manteniment type
	public static final int MANT_COMPANY 		  		= 1;
	public static final int MANT_PERFORMING_ORG   		= 2;
	public static final int MANT_PROGRAM 		  		= 3;
	public static final int MANT_SKILL 			  		= 4;
	public static final int MANT_CONTACT 		  		= 5;
	public static final int MANT_RESOURCE 		  		= 6;
	public static final int MANT_BUDGETACCOUNTS  		= 7;
	public static final int MANT_CONTRACTTYPE    		= 8;
	public static final int MANT_EXPENSEACCOUNTS  		= 9;
	public static final int MANT_CATEGORY		  		= 10;
	public static final int MANT_GEOGRAPHY		  		= 11;
	public static final int MANT_OPERATIONACCOUNT 		= 12;
	public static final int MANT_OPERATION		  		= 13;
	public static final int MANT_CUSTOMERS	  	  		= 14;
	public static final int MANT_SELLERS	  	 		= 15;
	public static final int MANT_METRIC_KPI	  	  		= 16;
	public static final int MANT_CUSTOMER_TYPE	  		= 17;
	public static final int MANT_FUNDINGSOURCE	  		= 18;
	public static final int MANT_DOCUMENTATION	  		= 19;
	public static final int MANT_CHANGETYPE		  		= 20;
	public static final int MANT_WBS			  		= 21;
	public static final int MANT_BUSINESS_DRIVER  		= 22;
	public static final int MANT_BUSINESS_DRIVER_SET 	= 23;
	public static final int MANT_RISK_TEMPLATES 		= 24;
	public static final int MANT_JOB_CATEGORY			= 25;
	public static final int MANT_LABEL					= 26;
	public static final int MANT_STAGE_GATES			= 27;
	public static final int MANT_RESOURCE_POOL			= 28;
	public static final int MANT_MILESTONES_TYPE		= 29;
	public static final int MANT_CLOSURE_CHECK			= 30;
	public static final int MANT_RISK_CATEGORY		  	= 31;
	public static final int MANT_PROBLEM_CHECK		  	= 32;
	public static final int MANT_MILESTONE_CATEGORY		= 33;  
	public static final int MANT_CLASSIFICATION_LEVEL	= 34;
    public static final int MANT_BSC_DIMENSION          = 35;
    public static final int MANT_TECHNOLOGIES_MAP	    = 36;
	public static final int MANT_KNOWLEDGE_AREAS	    = 37;
	public static final int MANT_PROFILES	    		= 38;
	public static final int MANT_STAKEHOLDER_CLASSIFICATION = 39;
    public static final int MANT_TIMELINE               = 40;


	// Level for time and expense sheet
	public static final char APP0 		= '0';
	public static final char APP1 		= '1';
	public static final char APP2 		= '2';
	public static final char APP3 		= '3';
	public static final char APP4 		= '4';
	public static final char APPREJECT  = 'R';
	
	//TIME SHEET STATUS
	public static final String TIMESTATUS_APP0  = "app0";
	public static final String TIMESTATUS_APP1  = "app1";
	public static final String TIMESTATUS_APP2  = "app2";
	public static final String TIMESTATUS_APP3  = "app3";
	
	//EXPENSE SHEET STATUS
	public static final String EXPENSE_STATUS_APP0  = "app0";
	public static final String EXPENSE_STATUS_APP1  = "app1";
	public static final String EXPENSE_STATUS_APP2  = "app2";
	public static final String EXPENSE_STATUS_APP3  = "app3";

	/**
	 * Only used in GenericServlet and DateUtil to generate Calendar
	 */
	public static Locale DEF_LOCALE 			= new Locale(Settings.LOCALE_LANGUAGE_DEFAULT, Settings.LOCALE_COUNTRY_DEFAULT);
	public static Locale DEF_LOCALE_NUMBER 		= new Locale("es", "ES");

	// FIXME generate format by user logged - remove constants
    public static final String DATE_PATTERN_DASH    = "yyyy-MM-dd";
	public static final String DATE_PATTERN 		= "dd/MM/yyyy";
    public static final String DATE_PATTERN_NARROW  = "dd/MM/yy";
	public static final String TIME_PATTERN 		= "dd/MM/yyyy HH:mm:ss";
	public static final String TIME_PATTERN_SIMPLE 	= "dd/MM HH:mm";
	public static final String ONLY_TIME_PATTERN 	= "HH:mm";
	public static final String DATE_YEAR_PATTERN 	= "yyyy";

	public static final char RISK_LOW		= 'L';
	public static final char RISK_MEDIUM	= 'M';
	public static final char RISK_NORMAL	= 'N';
	public static final char RISK_HIGH		= 'H';

	// STATUS RESOURCE
	public static final String RESOURCE_ASSIGNED		= "assigned";
	public static final String RESOURCE_PRE_ASSIGNED	= "preassigned";
	public static final String RESOURCE_TURNED_DOWN		= "turneddown";
	public static final String RESOURCE_PROPOSED		= "proposed";
	public static final String RESOURCE_RELEASED		= "released";
    public static final String RESOURCE_UNASSIGNED		= "unassigned";
	
	public static final char SEPARATOR_CSV = ';';
	
	public static final String CONTROL_ACCOUNT 	= "CA";
	public static final String WORK_GROUP 		= "WG";
    public static final String OBS_TEAMMEMBER   = "teammember";
	
	// Level of the bubble for chart
	public static final double BUBBLE_MAX		= 60;
	public static final double BUBBLE_DEFAULT	= 40;
	public static final double BUBBLE_MIN		= 10;
	
		
	public static final int STAKEHOLDER_ONLY_NAME	= 1;
	public static final int STAKEHOLDER_USER		= 2;
	
	
	//Type Stakeholder
	public static final char TYPE_STAKEHOLDER_SUPPORTER = 'S';
	public static final char TYPE_STAKEHOLDER_NEUTRAL 	= 'N';
	public static final char TYPE_STAKEHOLDER_OPPONENT 	= 'O';
	
	// Issue Priority
	public static final Character PRIORITY_HIGH		= 'H';
	public static final Character PRIORITY_MEDIUM	= 'M';
	public static final Character PRIORITY_LOW		= 'L';
	
	// KPI STATUS
	public static final String KPI_STATUS_HIGH		= "KPI_STATUS_HIGH";
	public static final String KPI_STATUS_MEDIUM	= "KPI_STATUS_MEDIUM";
	public static final String KPI_STATUS_LOW		= "KPI_STATUS_LOW";
	
	// Risk probability (Percentage)
	public static final Integer PROBABILITY_TRIVIAL = 10;
	public static final Integer PROBABILITY_MINOR 	= 30;
	public static final Integer PROBABILITY_MODERATE= 50;
	public static final Integer PROBABILITY_HIGH 	= 70;
	public static final Integer PROBABILITY_SEVERE	= 90;
	
	// Risk analysis (Status)
	public static final String CHAR_CLOSED 	= "C";
	public static final String CHAR_OPEN 	= "O";
	public static final String CLOSED 	= "Closed";
	public static final String OPEN 	= "Open";
	
	// Risk Impact (Percentage)
	public static final Integer IMPACT_TRIVIAL 	= 5;
	public static final Integer IMPACT_MINOR 	= 10;
	public static final Integer IMPACT_MODERATE = 20;
	public static final Integer IMPACT_HIGH 	= 40;
	public static final Integer IMPACT_SEVERE	= 80;
	
	// Risk types
	public static final Integer RISK_OPPORTUNITY = 0;
	public static final Integer RISK_THREAT = 1;

    // TODO jordi.ripoll - 12/05/2015 - migrar constantes al enum
	// Types documents of project
	public static final String DOCUMENT_INITIATING			= "initiating";
	public static final String DOCUMENT_RISK				= "risk";
	public static final String DOCUMENT_PLANNING			= "planning";
	public static final String DOCUMENT_CONTROL				= "control";
	public static final String DOCUMENT_PROCUREMENT			= "procurement";
	public static final String DOCUMENT_CLOSURE				= "closure";
	public static final String DOCUMENT_INVESTMENT			= "investment";
	public static final String DOCUMENT_CLOSURE_CHECK_LIST	= "closure_check_list";

    public enum DocumentProjectType {
        DOCUMENT_INITIATING("initiating", "projectinit"),
        DOCUMENT_RISK("risk", "projectrisk"),
        DOCUMENT_PLANNING("planning", "projectplan"),
        DOCUMENT_CONTROL("control", "projectcontrol"),
        DOCUMENT_PROCUREMENT("procurement", "projectprocurement"),
        DOCUMENT_CLOSURE("closure", "projectclosure"),
        DOCUMENT_INVESTMENT("investment", "projects"),
        DOCUMENT_CLOSURE_CHECK_LIST("closure_check_list", "projectclosure"),
        DOCUMENT_TIMELINE("timeline", "projectcontrol");

        private String name;
        private String servlet;

        private DocumentProjectType(String name, String servlet) {
            this.name = name;
            this.servlet = servlet;
        }

        /**
         * @return the value
         */
        public String getName() {
            return name;
        }

        /**
         *
         * @return
         */
        public String getServlet() {
            return servlet;
        }

        /**
         * Find enum by name
         *
         * @param name
         * @return
         */
        public static DocumentProjectType findByName(String name) {

            DocumentProjectType documentFound = DocumentProjectType.DOCUMENT_INVESTMENT;

            if (ValidateUtil.isNotNull(name)) {

                for (DocumentProjectType docProjectType : DocumentProjectType.values()) {

                    if (docProjectType.getName().equals(name)) {
                        documentFound = docProjectType;
                    }
                }
            }

            return documentFound;
        }
    }

	// Charge Cost
	public static final int SELLER_CHARGE_COST			= 1;
	public static final int INFRASTRUCTURE_CHARGE_COST	= 2;
	public static final int LICENSE_CHARGE_COST			= 3;
	
	/**
	 * @Deprecated  Replaced by Boolean.TRUE
	 */
	@Deprecated
	public static final boolean SELECTED					= true;
	/**
	 * @Deprecated  Replaced by Boolean.FALSE
	 */
	@Deprecated
	public static final boolean UNSELECTED					= false;

    public static final String UNCLASSIFIED					= "UNCLASSIFIED";
    public static final String CLASSIFIED					= "CLASSIFIED";

	// MAXLENGTH FOR NUMBERS
	public static final int MAX_CURRENCY = 14;
	
	// Yes or no
	public static final String YES = "Y";
	public static final String NO = "N";
	
	public static final String ASCENDENT = "asc";
	public static final String DESCENDENT = "desc";
	public static final String WORKLOAD_AVERAGE = "Workload Average";

	public static final double DEFAULT_HOUR_WEEK	= 40;
	public static final double DEFAULT_HOUR_DAY		= 8;
	
	public static final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
	
	// Weeks days
	public static final String SATURDAY = "S";
	public static final String SUNDAY = "D";
	
	// Sheet Info
	public static final int MONTH_YEAR_AREA_COLUMN = 22;
	public static final int MONTH_ROW = 1;
	public static final int YEAR_ROW = 3;
	public static final int AREA_ROW = 5;
	public static final int NAME_SURNAME_COLUMN = 33;
	public static final int NAME_ROW = 1;
	public static final int SURNAME_ROW = 3;
	public static final int DATE_ROW = 7;
	public static final int DATE_COLUMN = 35;
	public static final int WORKING_DAY = 8;
	
	// Time Sheet
	public static final int INIT_DAYS_ROW = 15;	
	public static final int INIT_CF_ROW = 8;
	public static final int INIT_TIMESHEET_COLUMN = 2;
	public static final int CF_COLUMN_INCREMENT = 2;
	public static final int CALCULATED_ROW = 52;
	public static final int INIT_CALCULATED_ROW = 4;
	public static final int START_CALCUL = 16;
	public static final int END_CALCUL = 52;
	public static final int NUMBER_WEEK_DAYS = 7;
	public static final int INTERN_PROJECT_COLUMN = 16;
	public static final int OPERATION_PROSPECCION = 1;
	public static final int OPERATION_PROPUESTAS = 2;
	public static final int OPERATION_GESTION = 3;
	public static final int OPERATION_FORMACION = 4;
	public static final int OPERATION_VACACIONES = 5;
	public static final int OPERATION_ENFERMEDAD = 6;
	public static final int OPERATION_OTROS = 7;
	public static final int OPERATION_ESPERA = 8;	
	public static final int OPERATION_PROSPECCION_COLUMN = 18;
	public static final int OPERATION_PROPUESTAS_COLUMN = 20;
	public static final int OPERATION_GESTION_COLUMN = 22;
	public static final int OPERATION_FORMACION_COLUMN = 24;
	public static final int OPERATION_VACACIONES_COLUMN = 28;
	public static final int OPERATION_ENFERMEDAD_COLUMN = 29;
	public static final int OPERATION_OTROS_COLUMN = 30;
	public static final int OPERATION_ESPERA_COLUMN = 26;
	public static final int INIT_PERSONAL_COLUMN = 28;
	public static final int COMMENTS = 31;
	
	// Expenses Sheet
	public static final int EXPENSE_INIT_ROW = 64;
	public static final int EXPENSE_DATE_COLMUMN = 0;
	public static final int EXPENSE_DESC_COLMUMN = 4;
	public static final int EXPENSE_INCREMENT = 8;
	public static final int EXPENSE_GENERAL_COLUMN = 13;
	public static final int EXPENSE_PROJECT_COLUMN = 21;
	public static final int EXPENSE_CF_ROW = 60;
	public static final int EXPENSE_CF_COLUMN = 22;
	public static final int EXPENSE_CF_INCREMENT = 8;
	
	// Monthly Info
	public static final int CLIENT_ROW = 6;
	public static final int CLIENT_COLUMN = 0;
	public static final String CLIENT_TEXT = "Cliente: ";
	public static final int TECNICO_ROW = 8;
	public static final int TECNICO_COLUMN = 0;
	public static final String TECNICO_TEXT = "Técnico SM2 BALEARES: ";
	public static final int MONTHLY_MONTH_ROW = 10;
	public static final int MONTHLY_MONTH_COLUMN = 6;
	public static final int MONTHLY_YEAR_ROW = 10;
	public static final int MONTHLY_YEAR_COLUMN = 9;
	
	// Monthly Sheet	
	public static final int MONTHLY_INIT_DAYS_ROW = 13;
	public static final int MONTHLY_START_TIME_1_COLUMN = 2;
	public static final int MONTHLY_START_TIME_2_COLUMN = 4;
	public static final int MONTHLY_END_TIME_1_COLUMN = 3;
	public static final int MONTHLY_END_TIME_2_COLUMN = 5;
	public static final int MONTHLY_TOTAL_COLUMN = 6;
	
	// Calendar
	public static final int NUMBEROFMONTHS = 2;
	
	// Restrictions
	public static final String EQUAL_RESTRICTION = "eq";
	public static final String NOT_EQUAL_RESTRICTION = "ne";
	public static final String GREATER_OR_EQUAL_RESTRICTION = "ge";
	public static final String ILIKE_RESTRICTION = "ilike";
	public static final String LESS_OR_EQUAL_RESTRICTION = "le";
	public static final String DISJUNCTION = "disjunction";
	
	// JQPLOT
	public static final String ONE_DAY 		= "1 day";
	public static final String TWO_DAY 		= "2 day";
	public static final String ONE_WEEK 	= "1 week";
	public static final String ONE_MONTH 	= "1 month";
	
	public static final String CONSISTENT 	= "CONSISTENT";
	public static final String INCONSISTENT = "INCONSISTENT";
	
	// Files
	public static final String FILE_CONTENT_TYPE_CSV		= "text/csv";
	public static final String FILE_CONTENT_TYPE_GENERIC	= "application/octetstream";
	public static final String FILE_CONTENT_TYPE_WORD		= "application/msword";
	public static final String FILE_CONTENT_TYPE_PDF		= "application/pdf";
	public static final String FILE_CONTENT_TYPE_ODT		= "application/vnd.oasis.opendocument.text";
	
	// New select and copy
	public static final String NEW 		= "new";
	public static final String SELECT 	= "select";
	public static final String COPY 	= "copy";
	
	// TYPE APPROVATION RESOURCE
	public static final String TYPE_APPROBATION_RM 				= "RM";
	public static final String TYPE_APPROBATION_AUTOMATIC 		= "AUTOMATIC";
	public static final String TYPE_APPROBATION_AUTOMATIC_PM 	= "AUTOMATIC_PM";
	
	// Followup
	public static final char HIGH_IMPORTANCE 	= 'H';
	public static final char MEDIUM_IMPORTANCE 	= 'M';
	public static final char LOW_IMPORTANCE 	= 'L';
	public static final char NORMAL_IMPORTANCE 	= 'N';
	
	// PAGES
	public static final String PAGE_PROJECT		= "PROJECT";
	public static final String PAGE_INVESTMENT	= "INVESTMENT";
	public static final String PAGE_PROGRAM		= "PROGRAM";
	
	// Calculate EVM
	public static final String CALCULATE_EVM_HOURS_COST			= "HOURS_COST";
	public static final String CALCULATE_EVM_EXTERNAL_TM		= "EXTERNAL_TM";

    // Bundle
    public static final String SETTINGS = "settings";

    // Currency
    public static final String CURRENCY_FTL_PATTERN = ",##0.00";
}