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
 * File: Configurations.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.common;


public class Configurations {

	// TYPE CONFIGURATION
	public static final String TYPE_LIST_FILTERS		    = "LIST_FILTERS";
	public static final String TYPE_CHOOSE_ROLE			    = "CHOOSE_ROLE";
	public static final String TYPE_ASSIGNMENTS			    = "ASSIGNMENTS";
	public static final String TYPE_CAPACITY_PLANNING	    = "CAPACITY_PLANNING";
	public static final String TYPE_CAPACITY_RUNNING	    = "CAPACITY_RUNNING";
    public static final String TYPE_ASSIGNATION_APPROVALS	= "ASSIGNATION_APPROVALS";
    public static final String TYPE_TIME_APPROVALS	        = "TIME_APPROVALS";
    public static final String TYPE_LEARNED_LESSON	        = "LEARNED_LESSON";

	// GENERIC
	public static final String TABLE  = "TABLE";
	public static final String FILTER_DISABLED  = "FILTER_DISABLED";

	// Lists
	public static final String LIST_FILTERS_CONTRACT_TYPE 			= "CONTRACT_TYPE";
	public static final String LIST_FILTERS_FM 						= "FUNCTIONAL_MANAGER";
	public static final String LIST_FILTERS_SELLERS 				= "SELLER";
	public static final String LIST_FILTERS_CATEGORIES 				= "CATEGORY";
	public static final String LIST_FILTERS_RESOURCE_POOL 			= "RESOURCE_POOL";
	public static final String LIST_FILTERS_PM 						= "PROJECT_MANAGER";
	public static final String LIST_FILTERS_CLASSIFICATION_LEVEL 	= "CLASSIFICATION_LEVEL";
    public static final String LIST_FILTERS_FOUNDING_SOURCE 	    = "FUNDING_SOURCE";
    public static final String LIST_FILTERS_TECHNOLOGY              = "TECHNOLOGY";

	public static final String BOOLEAN_FILTER_SELLERS 				= "INDIRECT_SELLER";
	public static final String BOOLEAN_FILTER_PRIORITY_ADJUSTMENT 	= "PRIORITY_ADJUSTMENT";

	public static final String FILTER_RISK_RATING 					= "RISK_RATING";
	public static final String FILTER_FIRST_RISK_RATING 			= "FIRST_RISK_RATING";
	public static final String FILTER_LAST_RISK_RATING 				= "LAST_RISK_RATING";
	
	
	// CHOOSE_ROLE
	public static final String CR_PO		= "CHOOSE_ROLE_PO";
	public static final String CR_EMPLOYEE	= "CHOOSE_ROLE_EMPLOYEE";

	// ASSIGNMENTS
	public static final String SINCE		    = "SINCE";
	public static final String UNTIL		    = "UNTIL";
	public static final String TYPE			    = "TYPE";
	public static final String ORDER		    = "ORDER";
	public static final String PROJECT		    = "PROJECT";
    public static final String PROJECT_NAME		= "projectName";
	public static final String JOBCATEGORY	    = "JOBCATEGORY";
	public static final String STATUS		    = "STATUS";
	
	// CAPACITY RUNNING
	public static final String SHOW_OPERATIONS	= "SHOW_OPERATIONS";


    public enum LearnedLesson {
        LLAAQUERY,
        LLAAINCLUDEGLOBAL,
        LLAAPROGRAMS,
        LLAACUSTOMERS,
        LLAASELLERS,
        LLAAGEOGRAPHIES,
        LLAACUSTOMERTYPES,
        LLAAFUNDINGSOURCES,
        LLAAPROCESSGROUPS,
        LLAAKNOWLEDGEAREAS,
        LLAAIMPORTANCEACTIONS,
        LLAAIMPORTANCERECOMMENDATIONS,
        LLAAOWNER,
        LLAAJOBPROFILE,
        LLAATYPE,
        LLAAIMPACTSATISFACTION,
        LLAAINTERNALPROJECT,
        LLAAISGEOSELLING,
        LLAARAG,
        LLAAMAXIMPACTTIME,
        LLAAMINIMPACTTIME,
        LLAAMINIMPACTCOST,
        LLAAMAXIMPACTCOST,
        LLAAMAXRANKING,
        LLAAMINRANKING,
        LLAASINCE,
        LLAAUNTIL,
        PROJECTNAME,
        CUSTOMERTYPES,
        CUSTOMERS,
        PROGRAMS,
        CATEGORIES,
        PROJECTMANAGERS,
        FUNCTIONALMANAGERS,
        SELLERS,
        LABELS,
        STAGEGATES,
        CONTRACTTYPES,
        GEOGRAPHIES,
        CLASSIFICATIONLEVELS,
        TECHNOLOGIES,
        SPONSORS,
        FUNDINGSOURCES,
        PERFORMINGORGANIZATIONS,
        STATUS,
        ISGEOSELLING,
        RAG,
        MINPRIORITY,
        MAXPRIORITY,
        ISINDIRECTSELLER,
        MINRISKRATING,
        MAXRISKRATING,
        INCLUDINGADJUSTMENT,
        BUDGETYEAR,
        INTERNALPROJECT,
        SINCE,
        UNTIL;

        private LearnedLesson() {}
    }

}