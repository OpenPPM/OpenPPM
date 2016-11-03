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
 * File: constants.js
 * Create User: javier.hernandez
 * Create Date: 16/07/2015 13:13:06
 */
'use strict';

/**
 * Created by javier.hernandez on 08/06/2015.
 */

define(['angular'], function(angular) {
  'use strict';

  var module = angular.module('openppm.constants', []);

  /**
   * Define constants
   */
  module.constant('CONSTANTS', {

    // Conditional selects constants
    SHOW_ALL : 'SHOW_ALL',
    GREATER_OR_EQUAL : 'GREATER_OR_EQUAL',
    LESS_OR_EQUAL : 'LESS_OR_EQUAL',
    BETWEEN : 'BETWEEN',

    COMPARISON : ['SHOW_ALL', 'GREATER_OR_EQUAL', 'LESS_OR_EQUAL', 'BETWEEN'],

    // Role Constants
    "RESOURCE_PROFILES": {
      "RESOURCE": 1,
      "PROJECT_MANAGER": 2,
      "INVESTMENT_MANAGER": 3,
      "FUNCTIONAL_MANAGER": 4,
      "PROGRAM_MANAGER": 5,
      "RESOURCE_MANAGER": 6,
      "PMO": 7,
      "SPONSOR": 8,
      "PORFOLIO_MANAGER": 9,
      "ADMIN": 10,
      "STAKEHOLDER": 11,
      "LOGISTIC": 12
    },


    // Parameter Constants
    NOT_ID : '-1', // TODO javier.hernandez - 16/09/2015 - esto¿?

    // Maintenance Types
    MAINTENANCE_TYPE: {
      PROFILE:'PROFILE',
      KNOWLEDGE_AREA:'KNOWLEDGE_AREA',
      CONTRACT_TYPE:'CONTRACT_TYPE',
      GEOGRAPHIC_AREA:'GEOGRAPHIC_AREA',
      CUSTOMER:'CUSTOMER',
      FUNDING_SOURCE:'FUNDING_SOURCE',
      SELLER:'SELLER',
      CUSTOMER_TYPE: 'CUSTOMER_TYPE',
      CATEGORY: 'CATEGORY',
      PERFORMING_ORGANIZATION: 'PERFORMING_ORGANIZATION',
      LABEL: 'LABEL',
      STAGE_GATE: 'STAGE_GATE',
      CLASSIFICATION_LEVEL: 'CLASSIFICATION_LEVEL',
      POOL: 'POOL',
      SKILL: 'SKILL',
      JOB_CATEGORY: 'JOB_CATEGORY',
      TECHNOLOGY: 'TECHNOLOGY'
    },

    ACTIVATION: {
      ENABLED: 'ENABLED',
      DISABLED: 'DISABLED'
    },

    AUDIT: {
      LOCATIONS: {
        PROJECT_DATA: 'PROJECT_DATA'
      }
    },

    CONFIGURATION: {
      AUDIT: 'AUDIT'
    },

    STATIC_DATA: {
      LEARNED_LESSON: {
        IMPACT_ON_SATISFACTION: 'learned-lesson/impact-on-satisfaction.json',
        PROCESS_GROUP: 'learned-lesson/process-group.json',
        TYPE: 'learned-lesson/type.json',
        IMPORTANCE: 'learned-lesson/importance.json'
      },
      NOTIFICATION: {
        MODE:  'notification/mode.json'
      },
      AUDIT: {
        LOCATIONS: 'audit/locations.json'
      },
      CONFIGURATION: {
        MAPPING: 'configuration/mapping.json'
      }
    },

    PROJECT: {
      STATUS: {
        INITIATING:"INITIATING",
        PLANNING:"PLANNING",
        CONTROL:"CONTROL",
        CLOSED:"CLOSED",
        ARCHIVED:"ARCHIVED"
      }
    },

    STATUS: {
      INITIATING: 'INITIATING',
      PLANNING: 'PLANNING',
      CONTROL: 'CONTROL',
      CLOSED: 'CLOSED',
      ARCHIVED: 'ARCHIVED'
    },

    SERVLET: {
      PROJECTINIT: 'projectinit',
      PROJECTPLAN: 'projectplan',
      PROJECTCONTROL: 'projectcontrol',
      PROJECTLEARNEDLESSON: 'projectlearnedlesson'
    },

    DATE: {
      FORMAT_DATE: 'dd/MM/yyyy',
      FORMAT_DATE_MOMENT: 'DD/MM/YYYY',
      FORMAT_DATE_API: 'YYYY-MM-DD',
      FORMAT_DATE_TIME: 'dd/MM/yyyy hh:mm:ss'
    },

    ALERT: {
      SUCCESS: 'success',
      INFO: 'info',
      WARNING: 'warning',
      DANGER: 'danger'
    },

    TIME_SHEET: {
      STATUS: {
        APP0: 'APP0',
        APP1: 'APP1',
        APP2: 'APP2',
        APP3: 'APP3'
      },
      REJECT: 'REJECT'
    },

    SETTING: {
      ACTIVE_MANAGEMENT_OPERATION: 'ACTIVE_MANAGEMENT_OPERATION',
      SHOW: {
        LEARNED_LESSONS: "SHOW_LEARNED_LESSONS",
        NOTIFICATIONS: "SHOW_NOTIFICATIONS",
        MANAGE_OPERATIONS: "SHOW_MANAGE_OPERATIONS",
        AUDIT: "SHOW_AUDIT"
      }
    },

    SEARCH: {
      LLAAQUERY: 'LLAAQUERY',
      LLAAINCLUDEGLOBAL: 'LLAAINCLUDEGLOBAL',
      LLAAPROGRAMS: 'LLAAPROGRAMS',
      LLAACUSTOMERS: 'LLAACUSTOMERS',
      LLAASELLERS: 'LLAASELLERS',
      LLAAGEOGRAPHIES: 'LLAAGEOGRAPHIES',
      LLAACUSTOMERTYPES: 'LLAACUSTOMERTYPES',
      LLAAFUNDINGSOURCES: 'LLAAFUNDINGSOURCES',
      LLAAPROCESSGROUPS: 'LLAAPROCESSGROUPS',
      LLAAKNOWLEDGEAREAS: 'LLAAKNOWLEDGEAREAS',
      LLAAIMPORTANCEACTIONS: 'LLAAIMPORTANCEACTIONS',
      LLAAIMPORTANCERECOMMENDATIONS: 'LLAAIMPORTANCERECOMMENDATIONS',
      LLAAOWNER: 'LLAAOWNER',
      LLAAJOBPROFILE: 'LLAAJOBPROFILE',
      LLAATYPE: 'LLAATYPE',
      LLAAIMPACTSATISFACTION: 'LLAAIMPACTSATISFACTION',
      LLAAMINIMPACTTIME: 'LLAAMINIMPACTTIME',
      LLAAMAXIMPACTTIME: 'LLAAMAXIMPACTTIME',
      LLAAMINIMPACTCOST: 'LLAAMINIMPACTCOST',
      LLAAMAXIMPACTCOST: 'LLAAMAXIMPACTCOST',
      LLAAMINRANKING: 'LLAAMINRANKING',
      LLAAMAXRANKING: 'LLAAMAXRANKING',
      LLAASINCE: 'LLAASINCE',
      LLAAUNTIL: 'LLAAUNTIL',

      PROJECTNAME: 'PROJECTNAME',
      STATUS: 'STATUS',
      CUSTOMERTYPES: 'CUSTOMERTYPES',
      CUSTOMERS: 'CUSTOMERS',
      PROGRAMS: 'PROGRAMS',
      CATEGORIES: 'CATEGORIES',
      PERFORMINGORGANIZATIONS: 'PERFORMINGORGANIZATIONS',
      PROJECTMANAGERS: 'PROJECTMANAGERS',
      FUNCTIONALMANAGERS: 'FUNCTIONALMANAGERS',
      SELLERS: 'SELLERS',
      LABELS: 'LABELS',
      STAGEGATES: 'STAGEGATES',
      CONTRACTTYPES: 'CONTRACTTYPES',
      GEOGRAPHIES: 'GEOGRAPHIES',
      CLASSIFICATIONLEVELS: 'CLASSIFICATIONLEVELS',
      TECHNOLOGIES: 'TECHNOLOGIES',
      SPONSORS: 'SPONSORS',
      FUNDINGSOURCES: 'FUNDINGSOURCES',
      INTERNALPROJECT: 'INTERNALPROJECT',
      ISGEOSELLING: 'ISGEOSELLING',
      RAG: 'RAG',
      ISINDIRECTSELLER: 'ISINDIRECTSELLER',
      INCLUDINGADJUSTMENT: 'INCLUDINGADJUSTMENT',
      MINPRIORITY: 'MINPRIORITY',
      MAXPRIORITY: 'MAXPRIORITY',
      MINRISKRATING: 'MINRISKRATING',
      MAXRISKRATING: 'MAXRISKRATING',
      BUDGETYEAR: 'BUDGETYEAR',
      SINCE: 'SINCE',
      UNTIL: 'UNTIL'
    }
  });
});
