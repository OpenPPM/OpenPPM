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
 * File: main.js
 * Create User: javier.hernandez
 * Create Date: 16/07/2015 13:15:54
 */

/**
 * Created by javier.hernandez on 23/04/2015.
 */
'use strict';

require.config({
  paths: {

    // Bower
    'angular': '../bower_components/angular/angular',
    'angular-cookies': '../bower_components/angular-cookies/angular-cookies',
    'ui-router': '../bower_components/angular-ui-router/release/angular-ui-router.min',
    'ui-router-extra': '../bower_components/ui-router-extras/release/ct-ui-router-extras',
    'angular-translate': '../bower_components/angular-translate/angular-translate',
    'angular-translate-loader': '../bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min',
    'ui-bootstrap': '../bower_components/angular-bootstrap/ui-bootstrap',
    'ui-bootstrap-tpls': '../bower_components/angular-bootstrap/ui-bootstrap-tpls',
    'angular-resource': '../bower_components/angular-resource/angular-resource',
    'angular-messages': '../bower_components/angular-messages/angular-messages',
    'angular-route': '../bower_components/angular-route/angular-route',
    'angular-sanitize': '../bower_components/angular-sanitize/angular-sanitize',
    'angular-ui-calendar': '../bower_components/angular-ui-calendar/src/calendar',
    'blockUI': '../bower_components/angular-block-ui/dist/angular-block-ui.min',
    'ngTable': '../bower_components/ng-table/dist/ng-table.min',
    'ng-dialog': '../bower_components/ngDialog/js/ngDialog.min',
    'angular-scroll': '../bower_components/angular-scroll/angular-scroll.min',
    'valdr': '../bower_components/valdr/valdr.min',
    'valdr-message': '../bower_components/valdr/valdr-message.min',
    'ng-csv': '../bower_components/ng-csv/build/ng-csv',
    'angular-dynamic-locale': '../bower_components/angular-dynamic-locale/dist/tmhDynamicLocale',

    // jquery plugins
    'jquery': '../bower_components/jquery/dist/jquery.min',
    'moment': '../bower_components/moment/moment',
    'moment-range': '../bower_components/moment-range/lib/moment-range.min',

    'fullcalendar': '../bower_components/fullcalendar/dist/fullcalendar',
    'fullcalendar-es': '../bower_components/fullcalendar/dist/lang/es',

    // Application
    'app': 'app',
    'openppm.constants': 'constants',

    // Controllers
    'ErrorCtrl': 'controllers/common/error.controller',
    'HomeCtrl': 'controllers/home.controller',

    'LearnedLessonCtrl': 'controllers/learned-lessons/learned-lesson.controller',
    'LearnedLessonDetailCtrl': 'controllers/learned-lessons/learned-lesson-detail.controller',

    'NotificationAdminCtrl': 'controllers/notifications/notification-admin.controller',
    'NotificationListCtrl': 'controllers/notifications/notification-list.controller',
    'NotificationUserCtrl': 'controllers/notifications/notification-user.controller',
    //'LoginCtrl': 'controllers/login.controller',

    'AuditProjectDataCtrl': 'controllers/audit/project-data.controller',
    'ConfigurationAuditCtrl': 'controllers/configuration/audit.controller',
    'DocumentCtrl': 'controllers/common/document.controller',
    'ManagementOperationCtrl': 'controllers/management/management-operation.controller',

    // Directives
    'GenericDirective': 'directives/generic.directive',

    // Factories
    'InterceptorsFactory': 'factories/interceptors.factory',
    'SecurityFactory': 'factories/security.factory',
    'UtilsFactory': 'factories/utils.factory',

    // Resource Factories
    'NotificationConfigResource': 'factories/resources/notification-config.resource',
    'NotificationResource': 'factories/resources/notification.resource',
    'LearnedLessonResource': 'factories/resources/learned-lesson.resource',
    'ProjectResource': 'factories/resources/project.resource',
    'ActionLessonResource': 'factories/resources/action-lesson.resource',
    'RecommendationLessonResource': 'factories/resources/recommendation-lesson.resource',
    'MaintenanceResource': 'factories/resources/maintenance.resource',
    'ProgramResource': 'factories/resources/program.resource',
    'AuditResource': 'factories/resources/audit.resource',
    'ContactResource': 'factories/resources/util/contact.resource',
    'ConfigurationResource': 'factories/resources/util/configuration.resource',
    'DocumentResource': 'factories/resources/document.resource',
    'TimeSheetOperationResource': 'factories/resources/management/time-sheet-operation.resource',
    'OperationResource': 'factories/resources/management/operation.resource',
    'SettingResource': 'factories/resources/util/setting.resource',
    'EmployeeResource': 'factories/resources/employee.resource',
    'UserConfigResource': 'factories/resources/util/user-config.resource',

    // Services
    'CacheService': 'services/cache.service',
    'StaticDataService': 'services/static-data.service',
    'ConfigurationService': 'services/configuration.service',

    // Filters
    'generalFilter': 'filters/general.filter'
  },
  shim: {
    'angular': { exports: 'angular', deps: ['jquery'] },
    'fullcalendar': { exports: 'fullcalendar', deps : ['jquery', 'moment']},
    'fullcalendar-es': { deps : ['moment', 'fullcalendar' ]},
    'moment': { deps : ['jquery'] },
    'moment-range': {deps: ['moment']},

    'angular-cookies': { exports: 'angular-cookies', deps: ['angular'] },
    'ui-router': { exports: 'ui-router', deps : ['angular'] },
    'ui-router-extra': { exports: 'ui-router-extra', deps : ['angular', 'ui-router'] },
    'angular-translate': { exports: 'angular-translate', deps : ['angular'] },
    'angular-validation': { exports: 'angular-validation', deps : ['angular'] },
    'angular-validation-rule': { exports: 'angular-validation-rule', deps : ['angular-validation'] },
    'angular-translate-loader': { exports: 'angular-translate-loader', deps : ['angular', 'angular-translate'] },
    'ui-bootstrap': { exports: 'bootstrap', deps : ['angular'] },
    'ui-bootstrap-tpls': { exports: 'ui-bootstrap-tpls', deps : ['angular', 'ui-bootstrap']},
    'blockUI': { exports: 'blockUI', deps: ['angular'] },
    'angular-resource': { deps : ['angular'] },
    'ngTable': { deps: ['angular'] },
    'ng-dialog': { deps: ['angular'] },
    'angular-scroll': { deps: ['angular'] },
    'angular-sanitize': { deps: ['angular'] },
    'ng-csv': { deps: ['angular', 'angular-sanitize'] },
    'valdr': { deps: ['angular'] },
    'valdr-message': { deps: ['angular', 'valdr'] },
    'angular-ui-calendar': { deps: ['angular', 'moment', 'fullcalendar-es'] },
    'angular-dynamic-locale': { deps: ['angular']},

    // Application
    //'NotificationResource': { exports: 'NotificationResource', deps: ['app', 'angular'] },
    'generalFilter': { exports: 'generalFilter'}

  }
});

require(['angular', 'app'], function(angular) {
  angular.element(document).ready(function() {
    angular.bootstrap(document, ['openppm']);
  });
});
