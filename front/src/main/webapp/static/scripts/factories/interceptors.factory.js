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
 * File: interceptors.factory.js
 * Create User: javier.hernandez
 * Create Date: 16/07/2015 13:42:42
 */

/**
 * Created by javier.hernandez on 24/04/2015.
 */
define(['angular', 'blockUI', 'angular-cookies'], function(angular) {
  'use strict';

  var module = angular.module('openppm.interceptors', ['blockUI', 'ngCookies']);

  module.factory('loadingHttpInterceptor', ['$q','blockUI', '$timeout',function($q, blockUI, $timeout) {
    return {


      // Show loading on request start
      'request': function(config) {

        var url = config.url.split('/');

        // Only add token on REST requests
        if (url[1] === 'rest') {
          if (!config.disableLoading && config.params !== undefined && !config.params.disableLoading) {
            blockUI.start();
          }
        }
        return config;
      },

      // Hide loading on request success
      'response': function(response) {
        $timeout(function() { blockUI.stop(); });
        return response;
      },

      // Hide loading on request error
      'responseError': function(rejection) {

        blockUI.stop();
        return $q.reject(rejection);
      }
    };
  }]);

  /**
   * Add security token to http request
   */
  module.factory('httpRequestInterceptor',['$cookieStore', function ($cookieStore) {
    return {
      request: function (config) {

          // TODO javier.hernandez - 16/07/2015 - seguridad token
       /* var url = config.url.split('/');

        // Only add token on REST requests
        if (url[1] === 'webservices') {

          var cookieData = $cookieStore.get('_identity');

          if (angular.isDefined(cookieData) && angular.isObject(cookieData)) {

            cookieData = angular.fromJson(cookieData);

            if (angular.isDefined(cookieData.token)) {
              config.headers['X-Auth-Token'] = cookieData.token;
            }
            else {
              config.headers['X-Auth-Token'] = '';
            }
          }
        }*/
        return config;
      }
    };
  }]);

  /**
   * Capture error interceptor por ajax calls
   */
  module.factory('httpRejectionInterceptor',['$q', '$injector',
    function ($q, $injector) {
      return {
        responseError: function(rejection) {

          // Logout token is out of date
          if (rejection.status === 401) {

            // Inject dependency and logout
            $injector.get('authorization').logout();
          }
          else if (rejection.status === 405) {

            // Inject dependency and navigate to view
            $injector.get('$state').go('not-allowed');
          }
          else if (rejection.status === 500) {

            // Inject dependency and navigate to view
            $injector.get('$state').go('error', {error: rejection.data.message});
          }

          return $q.reject(rejection);
        }
      };
    }]);
});
