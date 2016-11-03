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
 * File: static-data.service.js
 * Create User: javier.hernandez
 * Create Date: 11/09/2015 12:12:01
 */

/**
 * Created by javier.hernandez on 11/09/2015.
 */

define(['app'], function(app) {
  'use strict';

  app.service('$StaticDataService', [ 'CONSTANTS', '$resource',
    function(CONSTANTS, $resource){


      /*
       * Stored data
       */

      // Learned Lessons
      var impactOnSatisfactions = null;
      var processGroups = null;
      var learnedLessonTypes = null;
      var learnedLessonImportance = null;

      // Notifications
      var notificationModes = null;

      // Audit
      var auditColumns = null;

      // Configuration
      var configurationMapping = null;

      var getValues = function(json) {
        return $resource('static-data/'+json).query();
      };

      var getObject = function(json) {
        return $resource('static-data/'+json).get();
      };

      // Implemented methods
      return {

        // Learned Lessons
        getImpactOnSatisfactions: function () {
          return impactOnSatisfactions || (impactOnSatisfactions = getValues(CONSTANTS.STATIC_DATA.LEARNED_LESSON.IMPACT_ON_SATISFACTION));
        },
        getProcessGroups: function () {
          return processGroups || (processGroups = getValues(CONSTANTS.STATIC_DATA.LEARNED_LESSON.PROCESS_GROUP));
        },
        getLearnedLessonTypes: function () {
          return learnedLessonTypes || (learnedLessonTypes = getValues(CONSTANTS.STATIC_DATA.LEARNED_LESSON.TYPE));
        },
        getLearnedLessonImportance: function () {
          return learnedLessonImportance || (learnedLessonImportance = getValues(CONSTANTS.STATIC_DATA.LEARNED_LESSON.IMPORTANCE));
        },
        
        // Notifications
        getNotificationModes: function () {
          return notificationModes || (notificationModes = getValues(CONSTANTS.STATIC_DATA.NOTIFICATION.MODE));
        },

        // Audit
        getAuditColumns: function () {
          return (auditColumns || (auditColumns = getObject(CONSTANTS.STATIC_DATA.AUDIT.LOCATIONS)));
        },

        // Configuration
        getConfigurationMapping: function () {
          return (configurationMapping || (configurationMapping = getObject(CONSTANTS.STATIC_DATA.CONFIGURATION.MAPPING)));
        }

      };

    }]);
});
