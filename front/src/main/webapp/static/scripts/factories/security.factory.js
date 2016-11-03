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
 * File: security.factory.js
 * Create User: javier.hernandez
 * Create Date: 16/07/2015 13:45:54
 */

/**
 * Created by javier.hernandez on 24/04/2015.
 */

define(['angular', 'angular-cookies'], function(angular) {
  'use strict';

  var module = angular.module('openppm.security', ['ngCookies']);

  /**
   * Principal for login and security
   */
  module.factory('principal', ['$q', '$http', '$log', '$cookieStore', '$translate', '$window',
    function($q, $http, $log, $cookieStore, $translate, $window) {

      var _identity,
        _authenticated = false,
        _username = null,
        _password = null,
        _baseURL = '../rest/user',
        profiles;

      var onError = function() {
        _identity = undefined;
        profiles  = undefined;
        _authenticated = false;
        changeLocale();
      };

      var cookieData = $cookieStore.get('authOp');

      if (angular.isDefined(cookieData) && angular.isObject(cookieData)) {
        _identity = angular.fromJson(cookieData);
        _authenticated = true;
      }

      var changeLocale = function() {

        if (angular.isDefined(_identity) && angular.isDefined(_identity.locale) ) {
          $translate.use(_identity.locale);
        }
        else {
          $translate.use('en_US')
        }
      };

      return {
        logout: function() {
          _identity = undefined;
          profiles  = undefined;
          _authenticated = false;
          $cookieStore.remove('authOp');
          $window.location.href = '../login?a=logoff';
        },
        isIdentityResolved: function() {
          return angular.isDefined(_identity);
        },
        isProfilesResolved: function() {
          return angular.isDefined(profiles);
        },
        isAuthenticated: function() {
          return _authenticated;
        },
        setUsername: function (username) {
          _username = username;
        },
        setPassword: function (password) {
          _password = password;
        },
        getIdentify: function() {
          return _identity;
        },
        getProfiles: function() {
          return profiles;
        },
        isUserInProfile: function(profile) {
          return profile && _identity && profile === _identity.profile.code;
        },
        changeProfile: function(code) {

          $http.put(_baseURL+'/profile', {code: code}).
            success(function(user) {

              _identity = user;
              $window.location.href = '../home';
            });
        },
        identity: function() {

          var deferred = $q.defer();

          deferred.notify(_identity);

          // check and see if we have retrieved the identity data from the server. if we have, reuse it by immediately resolving
          if (angular.isDefined(_identity)) {
            deferred.resolve(_identity);
          }
          else {

            $http.get(_baseURL).
              success(function(data) {

                if (angular.isObject(data)) {

                  _identity = data;
                  _authenticated = true;
                  changeLocale();

                  deferred.resolve(_identity);
                }

              }).
              error(function() {
                onError();
                deferred.resolve(null);
              });
          }

          return deferred.promise;
        },
        profiles: function() {

          var deferred = $q.defer();

          deferred.notify(profiles);

          if (angular.isDefined(profiles)) {
            deferred.resolve(profiles);
          }
          else {

            $http.get(_baseURL+'/profile').
              success(function(data) {
                profiles = data;
                deferred.resolve(profiles);
              })
              .error(function() {
                onError();
                deferred.resolve(null);
              });
          }

          return deferred.promise;
        }
      };
    }
  ]);

  /**
   * Auto login and logout
   */
  module.factory('authorization', ['$rootScope', '$state', 'principal', '$window',
    function($rootScope, $state, principal, $window) {
      return {
        authorize: function() {
          return principal.identity()
            .then(function() {

              if (!principal.isAuthenticated()) {

                // user is not authenticated. stow the state they wanted before you
                // send them to the signin state, so you can return them when you're done
                $rootScope.returnToState = $rootScope.toState;
                $rootScope.returnToStateParams = $rootScope.toStateParams;

                // now, send them to the signin state so they can log in
                //$state.go('login');
                $window.location.href = '../home';
              }
            });
        },
        logout: function() {
          principal.logout();
          //$state.go('login');
          $window.location.href = '../home';
        }
      };
    }
  ]);
});
