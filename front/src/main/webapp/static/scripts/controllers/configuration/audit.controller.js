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
 * File: audit.controller.js
 * Create User: javier.hernandez
 * Create Date: 16/09/2015 10:28:37
 */

'use strict';

/**
 * @ngdoc function
 * @name openppm.controller:ConfigurationAuditCtrl
 * @description
 * # ConfigurationAuditCtrl
 * Controller of the openppm
 */
define(['app', 'ConfigurationService'], function () {
  'use strict';

  var controller = ['$scope', 'CONSTANTS', '$ConfigurationService', '$q',
    function ($scope, CONSTANTS, $ConfigurationService, $q) {

      $scope.alert = {};

      $ConfigurationService.getConfigurationForView(CONSTANTS.CONFIGURATION.AUDIT).then(function(configurations) {
        $scope.configurations = configurations;
      });

      $scope.save = function() {

        var promises = [];
        $scope.configurations.forEach(function(configuration) {

          promises.push(configuration.data.$update());
        });

        $q.all(promises)
          .then(function() {
            $scope.alert.type = CONSTANTS.ALERT.SUCCESS;
            $scope.alert.show = true;
            $scope.alert.label = 'FORM.ACTION.SUCCESS';
          },function() {
            $scope.alert.type = CONSTANTS.ALERT.DANGER;
            $scope.alert.show = true;
            $scope.alert.label = 'FORM.ACTION.DANGER';
        });
      };

  }];

  return {
    controller: controller,
    parent: 'site'
  };
});
