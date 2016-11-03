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
 * File: generic.directive.js
 * Create User: javier.hernandez
 * Create Date: 21/09/2015 10:27:58
 */

/**
 * Created by Javier on 06/09/2015.
 */
/**
 * Created by javier.hernandez on 17/04/2015.
 */
define(['angular'], function(angular) {
  'use strict';

  angular.module('openppm.generic', ['ui.bootstrap'])

    .factory('SettingInternal', ['$resource', function ($resource) {
      return $resource('../rest/util/setting', {}, { 'query': { method:'GET'}});
    }])
    .factory('SettingConfigInternal', ['$resource', function ($resource) {
      return $resource('../rest/util/config/setting/:name', { name: '@name' }, { 'update': { method:'PUT'} });
    }])

    .service('$CacheInternalService', [ 'CONSTANTS', 'SettingInternal', 'SettingConfigInternal',
      function(CONSTANTS, SettingInternal, SettingConfigInternal){
        var settings, settingConfigList = null;
        return {
          getSettings: function() { return settings || (settings = SettingInternal.query()); },
          getSettingConfigList: function() { return settingConfigList || (settingConfigList = SettingConfigInternal.query()); }
        };
      }])

    .directive('ogHead', function () {
      return {
        restrict: 'E',
        templateUrl: 'views/templates/og-head.html',
        controller: ['$scope', 'CONSTANTS', 'principal', '$CacheInternalService',
          function($scope, CONSTANTS, principal, $CacheService) {

            $scope.principal = principal;
            $scope.CONSTANTS = CONSTANTS;
            $scope.settings = $CacheService.getSettings();
            //TODO jordi.ripoll - 01/12/2015 - comentado porque la funcionalidad no esta acabada y no deja entrar con otros roles
            //$scope.settingConfigList = $CacheService.getSettingConfigList();

            // Loading all data for controllers
            //
            principal.identity().then(function(data) {
              $scope.user = data;
            });

            principal.profiles().then(function(data) {
              $scope.profiles = data;
            });

            $scope.changeProfile = function(code) {
              principal.changeProfile(code);
            };

            $scope.viewOptionMenu = function (action, accion) {

              var f = document.frm_menu;

              f.action = action;
              f.accion.value = accion;
              f.submit();
            };
          }]
      };
    })

    .directive('ogFooter', function () {
      return {
        restrict: 'E',
        templateUrl: 'views/templates/og-footer.html',
        controller: ['$scope', 'CONSTANTS', '$modal', '$CacheInternalService',
          function($scope, CONSTANTS, $modal, $CacheService) {

            $scope.CONSTANTS  = CONSTANTS;
            $scope.settings   = $CacheService.getSettings();

            $scope.open = function () {

              $modal.open({
                templateUrl: 'about-modal.html',
                controller: 'AboutModalInstanceCtrl'
              });
            }
          }]
      };
    })




    .directive('ogFilterProject', function () {
      return {
        restrict: 'E',
        templateUrl: 'views/templates/project/filters.html',
        scope: {
          ngModel: '=',
          ngEngine: '=?',
          //: '=?',
          find: '&onFind',
          linking: '=inLinking',
          selectedStatus: '=?'
        },
        controller: ['$scope', 'CONSTANTS', '$injector',
          function($scope, CONSTANTS, $injector) {

            $scope.CONSTANTS = CONSTANTS;

            $scope.comparisons = CONSTANTS.COMPARISON;

            var $CacheService = $injector.get('$CacheService');

            $scope.customerTypes = $CacheService.getCustomerTypes();
            $scope.customers = $CacheService.getCustomers();
            $scope.programs = $CacheService.getPrograms();
            $scope.categories = $CacheService.getCategories();
            $scope.performingOrganizations = $CacheService.getPerformingOrganizations();
            $scope.projectManagers = $CacheService.getProjectManagers();
            $scope.functionalManagers = $CacheService.getFunctionalManagers();
            $scope.sellers = $CacheService.getSellers();
            $scope.labels = $CacheService.getLabels();
            $scope.stageGates = $CacheService.getStageGates();
            $scope.contractTypes = $CacheService.getContractTypes();
            $scope.geographies = $CacheService.getGeographicAreas();
            $scope.classificationLevels = $CacheService.getClassificationLevels();
            $scope.technologies = $CacheService.getTechnologies();
            $scope.sponsors = $CacheService.getSponsors();
            $scope.fundingSources = $CacheService.getFundingSources();

            $scope.availableStatus = [];
            angular.forEach(CONSTANTS.STATUS, function(key) {
              $scope.availableStatus.push(key);
            });

            if (angular.isUndefined($scope.selectedStatus)) {
              $scope.selectedStatus = {};
            }

            angular.forEach($scope.status, function(value){
              $scope.selectedStatus[value] = true;
            });

            $scope.changeStatus = function() {

              $scope.ngModel.status = [];
              angular.forEach($scope.selectedStatus, function(value, key){

                if ($scope.selectedStatus[key]) {
                  $scope.ngModel.status.push(key);
                }
              });
            };

            $scope.reset = function() {


              $scope.ngModel.projectName = null;
              $scope.ngModel.customerTypes = null;
              $scope.ngModel.customers = null;
              $scope.ngModel.programs = null;
              $scope.ngModel.categories = null;
              $scope.ngModel.projectManagers = null;
              $scope.ngModel.functionalManagers = null;
              $scope.ngModel.sellers = null;
              $scope.ngModel.labels = null;
              $scope.ngModel.stageGates = null;
              $scope.ngModel.contractTypes = null;
              $scope.ngModel.geographies = null;
              $scope.ngModel.classificationLevels = null;
              $scope.ngModel.technologies = null;
              $scope.ngModel.sponsors = null;
              $scope.ngModel.fundingSources = null;
              $scope.ngModel.performingOrganizations = null;
              $scope.ngModel.isGeoSelling = null;
              $scope.ngModel.rag = null;
              $scope.ngModel.minPriority = null;
              $scope.ngModel.maxPriority = null;
              $scope.ngModel.isIndirectSeller = null;
              $scope.ngModel.minRiskRating = null;
              $scope.ngModel.maxRiskRating = null;
              $scope.ngModel.includingAdjustment = null;
              $scope.ngModel.budgetYear = null;
              $scope.ngModel.internalProject = null;
              $scope.ngModel.since = null;
              $scope.ngModel.until = null;

              if (!angular.isUndefined($scope.ngEngine)) {

                $scope.ngEngine.$riskRating = CONSTANTS.SHOW_ALL;
                $scope.ngEngine.$priority = CONSTANTS.SHOW_ALL;
              }
              $scope.ngModel.status = [];
              $scope.selectedStatus = {};
            };

            $scope.resetRisk = function() {

              $scope.ngModel.minRiskRating = null;
              $scope.ngModel.maxRiskRating = null;
            };

            $scope.resetPriority = function() {

              $scope.ngModel.minPriority= null;
              $scope.ngModel.maxPriority = null;
            };
          }]
      };
    })

    .directive('numbersOnly', function () {
      return {
        require: 'ngModel',
        link: function (scope, element, attr, ngModelCtrl) {
          function fromUser(text) {
            if (text) {
              var transformedInput = text.replace(/[^0-9]/g, '');

              if (transformedInput !== text) {
                ngModelCtrl.$setViewValue(transformedInput);
                ngModelCtrl.$render();
              }
              return transformedInput;
            }
            return undefined;
          }
          ngModelCtrl.$parsers.push(fromUser);
        }
      };
    })

    .controller('AboutModalInstanceCtrl', ['$scope', '$modalInstance', function ($scope, $modalInstance) {

      $scope.close = function () {
        $modalInstance.dismiss('cancel');
      };
    }])

  // Directive used for rerendering after a locale change
  .directive('datepickerPopupWrap', ['$rootScope', function($rootScope) {

        return {

          restrict: 'A',
          require: 'ngModel',

          link: function ($scope, $element, attrs, ngModel) {

            // Force popup rerender whenever locale changes
            $rootScope.$on('localeChanged', ngModel.$render);
          }
        };
      }]);
});
