'use strict';

define('app',
  [
    'angular',
    'ui-router-extra',
    'angular-translate',
    'angular-translate-loader',
    'ui-bootstrap-tpls',
    'moment',
    'angular-resource',
    'ngTable',
    'ng-dialog',
    'angular-scroll',
    'valdr-message',
    'angular-sanitize',
    'angular-ui-calendar',
    'ng-csv',
    'angular-dynamic-locale',
    // APP
    'openppm.constants',
    'InterceptorsFactory',
    'SecurityFactory',
    'UtilsFactory',
    'GenericDirective',
    'generalFilter'
  ], function (angular) {

    /**
     * @ngdoc overview
     * @name extranetLeoApp
     * @description
     *
     * Main module of the application.
     */
    var app = angular.module('openppm', [
      'ui.router',
      'ct.ui.router.extras',
      'ui.bootstrap',
      'ngResource',
      'ngTable',
      'pascalprecht.translate',
      'ngDialog',
      'duScroll',
      'valdr',
      'ngSanitize',
      'ui.calendar',
      'ngCsv',
      'tmh.dynamicLocale',
      // APP
      'openppm.constants',
      'openppm.interceptors',
      'openppm.security',
      'openppm.utils',
      'openppm.generic',
      'openppm.filters'
    ]);


    /**
     * Router controller
     */
    app.config(function ($stateProvider, $urlRouterProvider, $controllerProvider, $filterProvider, $compileProvider,
                         $provide, $futureStateProvider, $logProvider, valdrProvider, blockUIConfig) { // TODO javier.hernandez - 10/09/2015 - poner injections con array de strings

      // Load Validations
      valdrProvider.setConstraintUrl('configuration/validations.json');

      // Disable automatically blocking of the user interface
      blockUIConfig.autoBlock = false;

      // Disable for production mode
      $logProvider.debugEnabled(true); // TODO javier.hernandez - 08/06/2015 - Disable in production mode

      var resolveStateViewFactory = function($q, futureState) {

        var d = $q.defer();

        // Create state by configuration
        // Resolve the promise with the full UI-Router state.
        d.resolve({
          name: futureState.name,
          url: futureState.url,
          views: {
            'content@': {
              templateUrl: futureState.templateUrl
            }
          }
        });

        // The state factory returns the promise
        return d.promise;
      };

      var resolveStateFactory = function($q, futureState) {

        var d = $q.defer();

        // Load state configuration and resolver dependencies
        require([futureState.src], function (stateConfiguration) {

          // Create state by configuration
          var fullState = {
            name: futureState.name,
            url: futureState.url,
            parent: stateConfiguration.parent,
            views: {
              'content@': {
                templateUrl: futureState.templateUrl,
                controller: stateConfiguration.controller
              }
            },
            deepStateRedirect:stateConfiguration.deepStateRedirect,
            resolve: stateConfiguration.resolve,
            params: stateConfiguration.params
          };

          // Resolve the promise with the full UI-Router state.
          d.resolve(fullState);
        });

        // The state factory returns the promise
        return d.promise;
      };

      // Loading states from .json file during runtime
      var loadAndRegisterFutureStates = function ($q, $http) {

        var d = $q.defer();

        $http.get('futureStates.json').then(function (resp) {

          angular.forEach(resp.data, function (futureState) {
            // Register each state returned from $http.get() with $futureStateProvider
            $futureStateProvider.futureState(futureState);
          });
          d.resolve();
        });

        return d.promise;
      };

      $futureStateProvider.stateFactory('resolveState', resolveStateFactory);
      $futureStateProvider.stateFactory('resolveStateView', resolveStateViewFactory);
      $futureStateProvider.addResolve(loadAndRegisterFutureStates);

      app.controller = $controllerProvider.register;
      app.directive = $compileProvider.directive;
      app.filter = $filterProvider.register;
      app.factory = $provide.factory;
      app.service = $provide.service;
      app.value = $provide.value;

      $urlRouterProvider.otherwise('/home');

      $stateProvider
        .state('site', {
          'abstract': true,
          resolve: {
            authorize: ['authorization', function(authorization) { return authorization.authorize(); }]
          }
        });
    });

    /**
     * Locale configuration
     */
    app.config(function ($translateProvider) {

      $translateProvider.useStaticFilesLoader({
        prefix: 'translations/locale-',
        suffix: '.json'
      });
      $translateProvider.preferredLanguage('en_US');
      $translateProvider.useSanitizeValueStrategy('escaped');
    });

    /**
     * Interceptors configuration
     */
    app.config(function ($httpProvider) {
      $httpProvider.interceptors.push('httpRequestInterceptor');
      $httpProvider.interceptors.push('httpRejectionInterceptor');
      $httpProvider.interceptors.push('loadingHttpInterceptor');
    });

    /**
     * Auto login configuration
     */
    app.run(['$rootScope', '$state', '$stateParams', 'authorization', 'principal', '$log', 'blockUI','valdr', '$http',
      function ($rootScope, $state, $stateParams, authorization, principal, $log, blockUI, valdr, $http) {

        $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error){

          event.preventDefault();
          $log.error('Change navigation status', error, toState);
        });

        $rootScope.$on('$stateChangeSuccess', function(event){
          event.preventDefault();

          // End transition
          blockUI.stop();
        });

        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {

          // Start transition
          blockUI.start();

          // track the state the user wants to go to; authorization service needs this
          $rootScope.toState = toState;
          $rootScope.toStateParams = toStateParams;
          // if the principal is resolved, do an authorization check immediately. otherwise,
          // it'll be done when the state it resolved.
          //if (principal.isIdentityResolved()) { authorization.authorize(); }
        });
      }
    ]);

      /**
       * Set the localeLocation with the path used in the application
       */
    app.config(function(tmhDynamicLocaleProvider) {

      tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');

    });

    /**
     * Angular locale configuration, retrieves the user locale and sets it for later usage
     */
    app.run(['principal', 'tmhDynamicLocale', function(principal, tmhDynamicLocale) {

      principal.identity().then(function(user) {

        if (user.locale !== '' && user.locale !== undefined) {

          // Get the user locale and format it to change the actual locale with this one
          var locale = user.locale.toLowerCase().split('_');

          if (locale.length > 1) {

            // Change the locale
            tmhDynamicLocale.set(locale[0] + '-' + locale[1]);
          }
        }
      })
    }]);

    /**
     * @ngdoc function
     * @name app.controller:HeaderCtrl
     * @description
     * # HeaderCtrl
     * Controller of the header
     */

      // Controller assigned to the header
    app.controller("HeaderCtrl", ['$scope', '$log', 'CONSTANTS', 'principal', '$CacheInternalService',
      function ($scope, $log, CONSTANTS, principal, $CacheService) {

        //TODO mariano.fontana - 03/09/2015 - Delete this promise.then once implemented
        principal.identity().then(function(data) {

          $scope.user = data;
        });

        $scope.settings   = $CacheService.getSettings();
        $scope.CONSTANTS  = CONSTANTS;

        $scope.selectRol= function () {
          document.forms["form_change_rol"].submit();
        };

        $scope.viewOptionMenu = function (action, accion) {

          var f = document.frm_menu;

          f.action = action;
          f.accion.value = accion;
          f.submit();
        };

        return{
          parent: 'site'
        }
      }
    ]);

    /**
     * @ngdoc function
     * @name app.controller:FooterCtrl
     * @description
     * # FooterCtrl
     * Controller of the footer
     */
    app.controller('FooterCtrl', ['$scope', '$modal', 'CONSTANTS', '$CacheInternalService',
      function ($scope, $modal, CONSTANTS, $CacheService) {

      $scope.CONSTANTS  = CONSTANTS;
      $scope.settings   = $CacheService.getSettings();

      $scope.open = function(size){

        $modal.open({

          animation: $scope.animationsEnabled = false,
          templateUrl: 'views/header-footer/footer-popup.html',
          controller: 'modalInstanceCtrl',
          size: size
        })
      }

    }]);

    // Controller of popup of the footer
    app.controller('modalInstanceCtrl', ['$scope', '$modalInstance', function ($scope, $modalInstance){

      $scope.ok = function () {

        $modalInstance.close();
      };

      $scope.cancel = function () {

        $modalInstance.dismiss('cancel');
      };
    }]);

    return app;
  });

