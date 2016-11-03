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
 * File: audit-project-data.controller.js
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 18:07:51
 */

'use strict';

/**
 * @ngdoc function
 * @name openppm.controller:AuditProjectDataCtrl
 * @description
 * # AuditProjectDataCtrl
 * Controller of the openppm
 */
define(['app', 'moment', 'AuditResource', 'StaticDataService', 'ProjectResource', 'ContactResource'], function (app, moment) {
  'use strict';

  var controller = ['$scope', 'CONSTANTS', 'Audit', '$StaticDataService', 'Project', 'Contact', '$filter', '$translate',
    function ($scope, CONSTANTS, Audit, $StaticDataService, Project, Contact, $filter, $translate) {

      // Load Static Data
      $scope.CONSTANTS = CONSTANTS;
      $scope.columns = $StaticDataService.getAuditColumns();


      $translate(['TABLE.NO_DATA_FOUND', 'TABLE.FIND_DATA', 'VIEW.AUDIT.PROJECT_DATA.QUOTA_EXCEEDED', 'VIEW.ERROR.TITLE']).
          then(function (labels) {

            $scope.labelList = {
              noDataFound: labels['TABLE.NO_DATA_FOUND'],
              findData: labels['TABLE.FIND_DATA'],
              quotaExceeded: labels['VIEW.AUDIT.PROJECT_DATA.QUOTA_EXCEEDED'],
              error: labels['VIEW.ERROR.TITLE']
            };

            $scope.alert = {
              show: true,
              type: CONSTANTS.ALERT.INFO,
              label: $scope.labelList.findData,
              icon: 'glyphicon-info-sign'
            };
          });



      $scope.reset = function() {
        $scope.filters = {
          since: moment().startOf('month').toDate(),
          until: moment().endOf('month').toDate()
        };
        $scope.projectSelected = undefined;
        $scope.userSelected = undefined;
      };

      $scope.audits = undefined;
      $scope.reset();

      $scope.getLabel = function(column) {
        return column.label || column.type;
      };

      $scope.getData = function(project, column) {

        var data = angular.copy(project);
        column.type.split("_").forEach(function(item) {
          if (data !== undefined) { data = data[item]; }
        });

        if (column.filter === undefined) {
          return data;
        }
        else {
          return $filter(column.filter)(data, CONSTANTS.DATE.FORMAT_DATE)
        }
      };

      /**
       * Find audit data
       */
      $scope.find = function() {

        $scope.alert.show = false;

        $scope.filters.codeProject = $scope.projectSelected === undefined || $scope.projectSelected === null ? undefined : $scope.projectSelected.code;
        $scope.filters.codeUser = $scope.userSelected === undefined || $scope.userSelected === null ? undefined : $scope.userSelected.code;

        $scope.audits = [];

        var columns = $scope.columns[CONSTANTS.AUDIT.LOCATIONS.PROJECT_DATA];

        var audits = Audit.query($scope.filters, function() {

          var columnActive = [];
          columns.forEach(function(column){

            if (column.show) { columnActive.push(column); }

            column.add = false;
            column.value = undefined;
          });

          audits.forEach(function(audit) {

            columnActive.forEach(function(column){

              var actualValue = $scope.getData(audit.project, column);
              column.add = column.value !== actualValue;

              column.value = actualValue;
            });

            var add = false;
            var i = 0;
            while ( i < columnActive.length && !add) {
              add = columnActive[i++].add;
            }

            if (add) {
              $scope.audits.push(audit);
            }
          });

          if ($scope.audits.length === 0) {
            $scope.alert = {
              show: true,
              type: CONSTANTS.ALERT.WARNING,
              label: $scope.labelList.noDataFound,
              icon: 'glyphicon-warning-sign'
            };
          }

        }, function(response) {

          $scope.alert = {
            show: true,
            type: CONSTANTS.ALERT.DANGER,
            label: $scope.labelList.quotaExceeded,
            icon: 'glyphicon-remove-circle'
          };
          if (response.status !== 412) {
            $scope.alert.label = $scope.labelList.error + response.data.message;

          }
        });

      };

      /**
       * Find projects
       *
       * @param $viewValue
       * @returns {*}
       */
      $scope.getProjects = function($viewValue) {

        return Project.query({projectName: $viewValue, disableLoading:true}).$promise.then(function(response) {
          return response;
        });
      };

      /**
       * Find users
       *
       * @param $viewValue
       * @returns {*}
       */
      $scope.getUsers = function($viewValue) {

        return Contact.query({query: $viewValue, disableLoading:true}).$promise.then(function(response) {
          return response;
        });
      }
  }];

  return {
    controller: controller,
    resolve: {
      checkPermission: ['principal', '$state', 'CONSTANTS', function(principal, $state, CONSTANTS){

        var user = principal.getIdentify();

        if (user.profile.code !== CONSTANTS.RESOURCE_PROFILES.ADMIN) {
          $state.go('not-allowed');
        }
      }]
    },
    parent: 'site'
  };
});
