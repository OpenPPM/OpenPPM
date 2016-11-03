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
 * File: configuration.service.js
 * Create User: javier.hernandez
 * Create Date: 17/09/2015 08:44:10
 */

/**
 * Created by javier.hernandez on 11/09/2015.
 */

define(['app', 'ConfigurationResource', 'StaticDataService'], function(app) {
  'use strict';

  app.service('$ConfigurationService', [ '$q', '$StaticDataService', 'Configuration', 'principal', '$state', 'CONSTANTS',
    function($q, $StaticDataService, Configuration, principal, $state, CONSTANTS){


      // Implemented methods
      return {

        /**
         * Maintenance
         */
        getConfigurationForView: function (typeMapping) {

          var deferred = $q.defer();

          if (principal.getIdentify().profile.code !== CONSTANTS.RESOURCE_PROFILES.ADMIN) {
            $state.go('not-allowed');
            deferred.resolve([]);
          }
          else {

            $StaticDataService.getConfigurationMapping().$promise.then(function (response) {

              var mapping = response[typeMapping];

              Configuration.query(function (response) {

                var configurations = [];

                response.forEach(function (configuration) {

                  var mappingConfiguration = mapping[configuration.key];

                  if (mappingConfiguration !== undefined) {

                    if (mappingConfiguration.type === 'CHECKBOX') {
                      configuration.value = configuration.value === 'true'
                    }

                    configurations.push({
                      data: configuration,
                      mapping: angular.copy(mapping[configuration.key])
                    })
                  }

                  deferred.resolve(configurations);

                });
              });
            });
          }

          return deferred.promise;
        }
      };

    }]);
});