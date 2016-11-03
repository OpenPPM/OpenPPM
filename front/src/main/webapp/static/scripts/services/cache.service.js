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
 * File: maintenance.service.js
 * Create User: javier.hernandez
 * Create Date: 11/09/2015 10:23:46
 */

/**
 * Created by javier.hernandez on 11/09/2015.
 */

define(['app', 'MaintenanceResource', 'ProgramResource', 'SettingResource', 'EmployeeResource'], function(app) {
  'use strict';

  app.service('$CacheService', [ 'CONSTANTS', 'Maintenance', 'Program', 'Setting', 'Employee',
    function(CONSTANTS, Maintenance, Program, Setting, Employee){


      // Stored data
      var knowledgeAreas = null;
      var profiles = null;
      var customers = null;
      var fundingSources = null;
      var sellers = null;
      var contractTypes = null;
      var geographicAreas = null;
      var customerTypes = null;
      var categories = null;
      var performingOrganizations = null;
      var labels = null;
      var stageGates = null;
      var classificationLevels = null;
      var poolList = null;
      var skillList = null;
      var jobCategoryList = null;
      var technologies = null;

      var programs = null;
      var projectManagers = null;
      var functionalManagers = null;
      var sponsors = null;

      var settings = null;

      // Implemented methods
      return {

        /**
         * Maintenance
         */
        getKnowledgeAreas: function () {
          return knowledgeAreas || (knowledgeAreas = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.KNOWLEDGE_AREA}));
        },
        getProfiles : function() {
          return profiles || (profiles = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.PROFILE}));
        },
        getCustomers : function() {
          return customers || (customers= Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.CUSTOMER}));
        },
        getFundingSources : function() {
          return fundingSources || (fundingSources = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.FUNDING_SOURCE}));
        },
        getSellers : function() {
          return sellers || (sellers = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.SELLER}));
        },
        getContractTypes : function() {
          return contractTypes || (contractTypes = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.CONTRACT_TYPE}));
        },
        getGeographicAreas : function() {
          return geographicAreas || (geographicAreas = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.GEOGRAPHIC_AREA}));
        },
        getCustomerTypes: function() {
          return customerTypes || (customerTypes = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.CUSTOMER_TYPE}));
        },
        getCategories: function() {
          return categories || (categories = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.CATEGORY}));
        },
        getPerformingOrganizations: function() {
          return performingOrganizations || (performingOrganizations = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.PERFORMING_ORGANIZATION}));
        },
        getLabels: function() {
          return labels || (labels = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.LABEL}));
        },
        getStageGates: function() {
          return stageGates || (stageGates = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.STAGE_GATE}));
        },
        getClassificationLevels: function() {
          return classificationLevels || (classificationLevels = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.CLASSIFICATION_LEVEL}));
        },
        getPoolList: function() {
          return poolList || (poolList = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.POOL}));
        },
        getSkillList: function() {
          return skillList || (skillList = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.SKILL}));
        },
        getJobCategoryList: function() {
          return jobCategoryList || (jobCategoryList = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.JOB_CATEGORY}));
        },
        getTechnologies: function() {
          return technologies || (technologies = Maintenance.query({MAINTENANCE_TYPE: CONSTANTS.MAINTENANCE_TYPE.TECHNOLOGY}));
        },

        /**
         * Others
         */
        getPrograms: function () {
          return programs || (programs = Program.query());
        },
        getProjectManagers: function() {
          return projectManagers || (projectManagers = Employee.query({codeProfile: CONSTANTS.RESOURCE_PROFILES.PROJECT_MANAGER}));
        },
        getFunctionalManagers: function() {
          return functionalManagers || (functionalManagers = Employee.query({codeProfile: CONSTANTS.RESOURCE_PROFILES.FUNCTIONAL_MANAGER}));
        },
        getSponsors: function() {
          return sponsors || (sponsors = Employee.query({codeProfile: CONSTANTS.RESOURCE_PROFILES.SPONSOR}));
        },
        getSettings: function() {
          return settings || (settings = Setting.query());
        }
      };

    }]);
});