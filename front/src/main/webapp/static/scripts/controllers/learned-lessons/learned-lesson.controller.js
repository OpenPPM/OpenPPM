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
 * File: learned-lesson.controller.js
 * Create User: jordi.ripoll
 * Create Date: 17/08/2015 13:17:42
 */

'use strict';
define(['app', 'LearnedLessonResource', 'ProjectResource', 'CacheService', 'StaticDataService', 'ContactResource', 'UserConfigResource'], function () {
  'use strict';

  var controller = ['$scope', '$log', 'CONSTANTS', '$modal', 'ngTableParams', '$CacheService','$translate', 'Program',
    'utils', '$state', 'principal', '$filter', 'ngDialog', 'LearnedLesson', 'Project', '$document', '$StaticDataService', '$timeout', 'Contact', 'UserConfig',
    function ($scope, $log, CONSTANTS, $modal, ngTableParams, $CacheService,$translate, Program,
    utils, $state, principal, $filter, ngDialog, LearnedLesson, Project, $document, $StaticDataService, $timeout, Contact, UserConfig){

      /*************************************************************/
      /*                    DECLARATION OF VARIABLES               */
      /*************************************************************/

      $scope.CONSTANTS = CONSTANTS;

      $scope.user = principal.getIdentify();

      // set Programs if program manager
      if (angular.equals($scope.user.profile.code, CONSTANTS.RESOURCE_PROFILES.PROGRAM_MANAGER)) {

        Program.query({code: $scope.user.code}, function(data) {

          $scope.user.programs = data;
        });
      }

      $scope.errors = [];

      $scope.zero = 0;

      // Object for the multi options such as impactTime, impactCost or Ranking.
      $scope.ngEngine = {};

      // Comparisons for filters such as impactTime
      $scope.comparisons          = CONSTANTS.COMPARISON;

      // Initialize translations
      $scope.translations = [];
      $scope.translations['LEARNED_LESSON'] = {};
      $translate(['VIEW.LEARNED_LESSON.NAME']).then(function(translation) {

        // Lesson Learned required translations
        $scope.translations['LEARNED_LESSON'].field = translation['VIEW.LEARNED_LESSON.NAME'];
      });

      // Column names
      $scope.columnNames = [
        'VIEW.PROJECT.ID_PROJECT',
        'VIEW.PROJECT.PROJECT_OR_DOMAIN',
        'VIEW.LEARNED_LESSON.ID_LLAA',
        'VIEW.LEARNED_LESSON.SUBJECT',
        'VIEW.PROJECT.OWNER',
        'PROCESS_GROUP.NAME',
        'VIEW.LEARNED_LESSON.KNOWLEDGE_AREA',
        'TYPE.NAME',
        'VIEW.LEARNED_LESSON.ACTIONS',
        'VIEW.LEARNED_LESSON.RECOMMENDATIONS',
        'VIEW.LEARNED_LESSON.RANK'
      ];

      // Fields that are visible on the table object value
      var visibleField = [
        'project.code',
        'project.name',
        'code',
        'name',
        'owner.name',
        'processGroup',
        'knowledgeArea.name',
        'type',
        'actions.length',
        'recommendations.length',
        'ranking'
      ];

      // Columns used in the projects linking table
      $scope.projectColumnNames = [

        'VIEW.PROJECT.ID_PROJECT',
        'VIEW.PROJECT.PROJECT_OR_DOMAIN',
        'RESOURCE_PROFILES.PROJECT_MANAGER',
        'VIEW.PROJECT.STATUS.NAME',
        'VIEW.PROJECT.%COMPLETE',
        'VIEW.PROJECT.BAC'
      ];

      // Fields that are visible on the table object value
      var projectVisibleField = [
        'code',
        'name',
        'projectManager.name',
        'status',
        'poc',
        'bac'
      ];

      // Number of elements displayed in each table
      $scope.displayCount = '10';
      $scope.projectDisplayCount = '10';

      // Learned lesson & project filters
      $scope.learnedLessonSearch = {};
      $scope.projectSearch = {};

      // Data arrays
      var learnedLessons = [];
      var linkProjects = [];


      /*************************************************************/
      /*                        GET CACHE DATA                     */
      /*************************************************************/

      // Cache data
      $scope.programs             = $CacheService.getPrograms();
      $scope.customers            = $CacheService.getCustomers();
      $scope.sellers              = $CacheService.getSellers();
      $scope.geographies          = $CacheService.getGeographicAreas();
      $scope.customerTypes        = $CacheService.getCustomerTypes();
      $scope.fundingSources       = $CacheService.getFundingSources();
      $scope.knowledgeAreas       = $CacheService.getKnowledgeAreas();
      $scope.profiles             = $CacheService.getProfiles();
      $scope.projectManagers      = $CacheService.getProjectManagers();

      // Static data
      $scope.processGroups        = $StaticDataService.getProcessGroups();
      $scope.impactSatisfactions  = $StaticDataService.getImpactOnSatisfactions();
      $scope.types                = $StaticDataService.getLearnedLessonTypes();
      $scope.importances          = $StaticDataService.getLearnedLessonImportance();

      $scope.owners               = Contact.query({usedInLLAA:true});

      // Show/Hide variables & functions
      $scope.generalLearnedLesson = false;
      $scope.linkEnabled = false;

      /**
       * Shows the project link table
       */
      $scope.showLinkProjects = function() {

        $scope.linkEnabled = true;
        var elementToScroll = angular.element(document.getElementById('projectLinkPanelScrollAnchor'));
        $document.scrollToElement(elementToScroll,30,1000);
      };

      /*************************************************************/
      /*                      ROUTING FUNCTIONS                    */
      /*************************************************************/

      /**
       * View project
       *
       * @param id
       * @param status
       */
      $scope.viewProject = function (id) {

        var f = document.forms["frm_projects"];

        f.id.value      = id;
        f.accion.value  = '';
        f.action        = '../' + CONSTANTS.SERVLET.PROJECTLEARNEDLESSON;

        f.submit();
      };

      /**
       *  Sets the route to the detail page.
       *
       * @param learnedLesson
       */
      $scope.viewLessonLearnedDetail  = function(learnedLesson) {

        // PMO & PgM update
        if ($scope.user.profile.code === CONSTANTS.RESOURCE_PROFILES.PMO && angular.isDefined(learnedLesson)
            || ($scope.user.profile.code === CONSTANTS.RESOURCE_PROFILES.PROGRAM_MANAGER && angular.isDefined(learnedLesson)
                && !angular.isDefined(learnedLesson.project))) {

          $state.go('learned-lessons-detail', {code: learnedLesson.code});
        }
        // PMO & PgM new
        else if (($scope.user.profile.code === CONSTANTS.RESOURCE_PROFILES.PMO && !angular.isDefined(learnedLesson))
                 || ($scope.user.profile.code === CONSTANTS.RESOURCE_PROFILES.PROGRAM_MANAGER && !angular.isDefined(learnedLesson))) {

          $state.go('learned-lessons-detail');
        }
        // PM is the project manager of this project
        else if ($scope.user.profile.code === CONSTANTS.RESOURCE_PROFILES.PROJECT_MANAGER &&
          angular.isDefined(learnedLesson) &&
          angular.isDefined(learnedLesson.project) &&
          $scope.user.code === learnedLesson.project.projectManager.code) {

          $state.go('learned-lessons-detail', {code: learnedLesson.code});
        }
        // PgM is the program manager of this project
        else if ($scope.user.profile.code === CONSTANTS.RESOURCE_PROFILES.PROGRAM_MANAGER &&
          angular.isDefined(learnedLesson) &&
          angular.isDefined(learnedLesson.program) &&
          $scope.llaaInPrograms($scope.user.programs, learnedLesson.program)) {

          $state.go('learned-lessons-detail', {code: learnedLesson.code});
        }
        else if (angular.isDefined(learnedLesson)) {

          $state.go('learned-lessons-detail', {code: learnedLesson.code, notEditable:true});
        }
      };

      /*************************************************************/
      /*                        TABLE FUNCTIONS                    */
      /*************************************************************/

      /**
       * LLAA sorting and width class configuration
       *
       * @param visibleField
       * @returns {{sortable: *, sort-asc: Array, sort-desc: Array}}
       */
      $scope.getSortingClass = function(columnName) {

        if ($scope.tableParams !== undefined) {
          // Proper json to ng-class for ngTable
          return {
            'sortable': visibleField[$scope.columnNames.indexOf(columnName)],
            'sort-asc': $scope.tableParams.isSortBy(visibleField[$scope.columnNames.indexOf(columnName)], 'asc'),
            'sort-desc': $scope.tableParams.isSortBy(visibleField[$scope.columnNames.indexOf(columnName)], 'desc'),
            'table-idPr-width': visibleField[$scope.columnNames.indexOf(columnName)] === 'idProject',
            'table-prName-widt': visibleField[$scope.columnNames.indexOf(columnName)] === 'projectName',
            'table-idLLAA-width': visibleField[$scope.columnNames.indexOf(columnName)] === 'code',
            'table-Owner-width': visibleField[$scope.columnNames.indexOf(columnName)] === 'owner.name',
            'table-processGroup-width': visibleField[$scope.columnNames.indexOf(columnName)] === 'processGroup',
            'table-knowledgeArea-width': visibleField[$scope.columnNames.indexOf(columnName)] === 'knowledgeArea.name',
            'table-type-width': visibleField[$scope.columnNames.indexOf(columnName)] === 'type',
            'table-subject-width': visibleField[$scope.columnNames.indexOf(columnName)] === 'name',
            'table-actions-width': visibleField[$scope.columnNames.indexOf(columnName)] === 'actions.length',
            'table-recommendations-width': visibleField[$scope.columnNames.indexOf(columnName)] === 'recommendations.length',
            'table-ranking-width': visibleField[$scope.columnNames.indexOf(columnName)] === 'ranking'
          }
        }
      };

      /**
       * LLAA sorting configuration
       *
       * @param visibleField
       */
      $scope.sortTable = function(columnName) {

        var sortingData = {};

        // Sets the proper name values to ng-click for ngTable
        sortingData[visibleField[$scope.columnNames.indexOf(columnName)]] =
          $scope.tableParams.isSortBy(visibleField[$scope.columnNames.indexOf(columnName)], 'asc')
          ? 'desc'
          : 'asc';
        $scope.tableParams.sorting(sortingData);
      };

      /**
       * Link project sorting and width class configuration of the LLAA table
       *
       * @param visibleField
       * @returns {{sortable: *, sort-asc: Array, sort-desc: Array}}
       */
      $scope.linkGetSortingClass = function(projectColumnName) {

        if ($scope.projectTableParams !== undefined) {
          // Proper json to ng-class for ngTable
          return {
            'sortable': projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)],
            'sort-asc': $scope.tableParams.isSortBy(projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)], 'asc'),
            'sort-desc': $scope.tableParams.isSortBy(projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)], 'desc'),
            'link-table-idPr-width': projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)] === 'code',
            'link-table-prName-width': projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)] === 'name',
            'link-table-prManager-width': projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)] === 'projectManager.name',
            'link-table-status-width': projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)] === 'status',
            'link-table-complete-width': projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)] === 'poc',
            'link-table-bac-width': projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)] === 'bac'
          }
        }
      };

      /**
       * Link project sorting configuration
       *
       * @param visibleField
       */
      $scope.linkSortTable = function(projectColumnName) {

        var sortingData = {};

        // Sets the proper name values to ng-click for ngTable
        sortingData[projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)]]
          = $scope.projectTableParams.isSortBy(projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)], 'asc')
          ? 'desc'
          : 'asc';
        $scope.projectTableParams.sorting(sortingData);
      };

      /**
       * Updates de number of elements displayed in the learned lesson table
       */
      $scope.updateDisplayCount = function() {

        $scope.tableParams.count($scope.displayCount);
        $scope.tableParams.reload();
      };

      /**
       * Updates the number of elements displayed in the project link table
       */
      $scope.updateProjectDisplayCount = function() {

        $scope.projectTableParams.count($scope.projectDisplayCount);
        $scope.projectTableParams.reload();
      };

      /**
       * Refresh project table
       */
      $scope.refreshProjectTable = function() {

        var elementToScroll = angular.element(document.getElementById('errorMessage'));
        $document.scrollToElement(elementToScroll,100,1000);

        // Restarting table operations
        $scope.projects = undefined;
        $scope.linkEnabled = false;
      };

      $scope.closeErrorMessage = function() {
        $scope.errorMessage = false;
      };



      // Translates
      $scope.getRanking = function() {

        var title = {ranking: ''};

        if ($scope.learnedLessonSelected !== null && angular.isDefined($scope.learnedLessonSelected)) {
          title.ranking = $scope.learnedLessonSelected.ranking;
        }
        return title;
      };

      /*************************************************************/
      /*                  USER CONFIGURATION FUNCTIONS             */
      /*************************************************************/

      /**
       * Load configurations by user
       */
      var loadConfigurations = function() {

        $scope.configurations = UserConfig.query({type:'LEARNED_LESSON'}, function() {

          // Get configurations by user
          var mapConfigurations = {};

          // Save in hash
          angular.forEach($scope.configurations, function(value, index) {
            mapConfigurations[$scope.configurations[index].key] = $scope.configurations[index].value;
          });

          // LOAD FILTERS LLAA
          $scope.learnedLessonSearch.llaaQuery              = mapConfigurations[CONSTANTS.SEARCH.LLAAQUERY] !== '' ? mapConfigurations[CONSTANTS.SEARCH.LLAAQUERY] : null;
          $scope.learnedLessonSearch.llaaIncludeGlobal      = mapConfigurations[CONSTANTS.SEARCH.LLAAINCLUDEGLOBAL] !== '' ? mapConfigurations[CONSTANTS.SEARCH.LLAAINCLUDEGLOBAL] : null;
          $scope.learnedLessonSearch.llaaPrograms           = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.LLAAPROGRAMS]);
          $scope.learnedLessonSearch.llaaCustomers          = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.LLAACUSTOMERS]);
          $scope.learnedLessonSearch.llaaSellers            = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.LLAASELLERS]);
          $scope.learnedLessonSearch.llaaGeographies        = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.LLAAGEOGRAPHIES]);
          $scope.learnedLessonSearch.llaaCustomerTypes      = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.LLAACUSTOMERTYPES]);
          $scope.learnedLessonSearch.llaaFundingSources     = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.LLAAFUNDINGSOURCES]);
          $scope.learnedLessonSearch.llaaProcessGroups      = mapConfigurations[CONSTANTS.SEARCH.LLAAPROCESSGROUPS] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAAPROCESSGROUPS] !== ''
                                                              ? mapConfigurations[CONSTANTS.SEARCH.LLAAPROCESSGROUPS].split(',') : null;
          $scope.learnedLessonSearch.llaaKnowledgeAreas     = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.LLAAKNOWLEDGEAREAS]);
          $scope.learnedLessonSearch.llaaImportanceActions  = mapConfigurations[CONSTANTS.SEARCH.LLAAIMPORTANCEACTIONS] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAAIMPORTANCEACTIONS] !== ''
                                                              ? mapConfigurations[CONSTANTS.SEARCH.LLAAIMPORTANCEACTIONS].split(',') : null;
          $scope.learnedLessonSearch.llaaImportanceRecommendations = mapConfigurations[CONSTANTS.SEARCH.LLAAIMPORTANCERECOMMENDATIONS] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAAIMPORTANCERECOMMENDATIONS] !== ''
                                                                     ? mapConfigurations[CONSTANTS.SEARCH.LLAAIMPORTANCERECOMMENDATIONS].split(',') : null;
          $scope.learnedLessonSearch.llaaOwner              = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.LLAAOWNER]);
          $scope.learnedLessonSearch.llaaJobProfile         = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.LLAAJOBPROFILE]);
          $scope.learnedLessonSearch.llaaType               = mapConfigurations[CONSTANTS.SEARCH.LLAATYPE] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAATYPE] !== ''
                                                            ? mapConfigurations[CONSTANTS.SEARCH.LLAATYPE].split(',') : null;
          $scope.learnedLessonSearch.llaaImpactSatisfaction = mapConfigurations[CONSTANTS.SEARCH.LLAAIMPACTSATISFACTION] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAAIMPACTSATISFACTION] !== ''
                                                            ? mapConfigurations[CONSTANTS.SEARCH.LLAAIMPACTSATISFACTION].split(',') : null;

          $scope.learnedLessonSearch.llaaMinImpactTime = mapConfigurations[CONSTANTS.SEARCH.LLAAMINIMPACTTIME] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAAMINIMPACTTIME] !== ''
            ? parseFloat(mapConfigurations[CONSTANTS.SEARCH.LLAAMINIMPACTTIME])
            : null;
          $scope.learnedLessonSearch.llaaMaxImpactTime = mapConfigurations[CONSTANTS.SEARCH.LLAAMAXIMPACTTIME] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAAMAXIMPACTTIME] !== ''
            ? parseFloat(mapConfigurations[CONSTANTS.SEARCH.LLAAMAXIMPACTTIME])
            : null;

          $scope.ngEngine.$impactTime = CONSTANTS.SHOW_ALL;

          if ($scope.learnedLessonSearch.llaaMinImpactTime != null && $scope.learnedLessonSearch.llaaMaxImpactTime != null) {
            $scope.ngEngine.$impactTime = CONSTANTS.BETWEEN;
          }
          else if ($scope.learnedLessonSearch.llaaMinImpactTime != null) {
            $scope.ngEngine.$impactTime = CONSTANTS.GREATER_OR_EQUAL;
          }
          else if ($scope.learnedLessonSearch.llaaMaxImpactTime != null) {
            $scope.ngEngine.$impactTime = CONSTANTS.LESS_OR_EQUAL;
          }

          $scope.learnedLessonSearch.llaaMinImpactCost = mapConfigurations[CONSTANTS.SEARCH.LLAAMINIMPACTCOST] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAAMINIMPACTCOST] !== ''
            ? parseFloat(mapConfigurations[CONSTANTS.SEARCH.LLAAMINIMPACTCOST])
            : null;
          $scope.learnedLessonSearch.llaaMaxImpactCost = mapConfigurations[CONSTANTS.SEARCH.LLAAMAXIMPACTCOST] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAAMAXIMPACTCOST] !== ''
            ? parseFloat(mapConfigurations[CONSTANTS.SEARCH.LLAAMAXIMPACTCOST])
            : null;

          $scope.ngEngine.$impactCost = CONSTANTS.SHOW_ALL;

          if ($scope.learnedLessonSearch.llaaMinImpactCost != null && $scope.learnedLessonSearch.llaaMaxImpactCost != null) {
            $scope.ngEngine.$impactCost = CONSTANTS.BETWEEN;
          }
          else if ($scope.learnedLessonSearch.llaaMinImpactCost != null) {
            $scope.ngEngine.$impactCost = CONSTANTS.GREATER_OR_EQUAL;
          }
          else if ($scope.learnedLessonSearch.llaaMaxImpactCost != null) {
            $scope.ngEngine.$impactCost = CONSTANTS.LESS_OR_EQUAL;
          }

          $scope.learnedLessonSearch.llaaMinRanking = mapConfigurations[CONSTANTS.SEARCH.LLAAMINRANKING] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAAMINRANKING] !== ''
            ? parseFloat(mapConfigurations[CONSTANTS.SEARCH.LLAAMINRANKING])
            : null;
          $scope.learnedLessonSearch.llaaMaxRanking = mapConfigurations[CONSTANTS.SEARCH.LLAAMAXRANKING] !== undefined && mapConfigurations[CONSTANTS.SEARCH.LLAAMAXRANKING] !== ''
            ? parseFloat(mapConfigurations[CONSTANTS.SEARCH.LLAAMAXRANKING])
            : null;

          $scope.ngEngine.$ranking = CONSTANTS.SHOW_ALL;

          if ($scope.learnedLessonSearch.llaaMinRanking != null && $scope.learnedLessonSearch.llaaMaxRanking != null) {
            $scope.ngEngine.$ranking = CONSTANTS.BETWEEN;
          }
          else if ($scope.learnedLessonSearch.llaaMinRanking != null) {
            $scope.ngEngine.$ranking = CONSTANTS.GREATER_OR_EQUAL;
          }
          else if ($scope.learnedLessonSearch.llaaMaxRanking != null) {
            $scope.ngEngine.$ranking = CONSTANTS.LESS_OR_EQUAL;
          }

          $scope.learnedLessonSearch.llaaSince = mapConfigurations[CONSTANTS.SEARCH.LLAASINCE] !== '' ? mapConfigurations[CONSTANTS.SEARCH.LLAASINCE] : null;
          $scope.learnedLessonSearch.llaaUntil = mapConfigurations[CONSTANTS.SEARCH.LLAAUNTIL] !== '' ? mapConfigurations[CONSTANTS.SEARCH.LLAAUNTIL] : null;


          // LOAD FILTERS PROJECT

          $scope.learnedLessonSearch.projectName              = mapConfigurations[CONSTANTS.SEARCH.PROJECTNAME] !== '' ? mapConfigurations[CONSTANTS.SEARCH.PROJECTNAME] : null;

          $scope.learnedLessonSearch.status                   = mapConfigurations[CONSTANTS.SEARCH.STATUS] !== undefined && mapConfigurations[CONSTANTS.SEARCH.STATUS] !== '' ?
            mapConfigurations[CONSTANTS.SEARCH.STATUS].split(',') : null;

          angular.forEach($scope.learnedLessonSearch.status, function(value){
            $scope.selectedStatus[value] = true;
          });

          $scope.learnedLessonSearch.customerTypes            = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.CUSTOMERTYPES]);
          $scope.learnedLessonSearch.customers                = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.CUSTOMERS]);
          $scope.learnedLessonSearch.programs                 = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.PROGRAMS]);
          $scope.learnedLessonSearch.categories               = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.CATEGORIES]);
          $scope.learnedLessonSearch.performingOrganizations  = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.PERFORMINGORGANIZATIONS]);
          $scope.learnedLessonSearch.projectManagers          = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.PROJECTMANAGERS]);
          $scope.learnedLessonSearch.functionalManagers       = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.FUNCTIONALMANAGERS]);
          $scope.learnedLessonSearch.sellers                  = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.SELLERS]);
          $scope.learnedLessonSearch.labels                   = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.LABELS]);
          $scope.learnedLessonSearch.stageGates               = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.STAGEGATES]);
          $scope.learnedLessonSearch.contractTypes            = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.CONTRACTTYPES]);
          $scope.learnedLessonSearch.geographies              = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.GEOGRAPHIES]);
          $scope.learnedLessonSearch.classificationLevels     = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.CLASSIFICATIONLEVELS]);
          $scope.learnedLessonSearch.technologies             = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.TECHNOLOGIES]);
          $scope.learnedLessonSearch.sponsors                 = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.SPONSORS]);
          $scope.learnedLessonSearch.fundingSources           = addCodeObject(mapConfigurations[CONSTANTS.SEARCH.FUNDINGSOURCES]);

          $scope.learnedLessonSearch.internalProject          = mapConfigurations[CONSTANTS.SEARCH.INTERNALPROJECT] !== '' ? mapConfigurations[CONSTANTS.SEARCH.INTERNALPROJECT] : null;
          $scope.learnedLessonSearch.isGeoSelling             = mapConfigurations[CONSTANTS.SEARCH.ISGEOSELLING] !== '' ? mapConfigurations[CONSTANTS.SEARCH.ISGEOSELLING] : null;
          $scope.learnedLessonSearch.rag                      = mapConfigurations[CONSTANTS.SEARCH.RAG] !== '' ? mapConfigurations[CONSTANTS.SEARCH.RAG] : null;
          $scope.learnedLessonSearch.isIndirectSeller         = mapConfigurations[CONSTANTS.SEARCH.ISINDIRECTSELLER] !== '' ? mapConfigurations[CONSTANTS.SEARCH.ISINDIRECTSELLER] : null;
          $scope.learnedLessonSearch.includingAdjustment      = mapConfigurations[CONSTANTS.SEARCH.INCLUDINGADJUSTMENT] !== '' ? mapConfigurations[CONSTANTS.SEARCH.INCLUDINGADJUSTMENT] : null;

          $scope.learnedLessonSearch.minPriority              = mapConfigurations[CONSTANTS.SEARCH.MINPRIORITY] !== undefined && mapConfigurations[CONSTANTS.SEARCH.MINPRIORITY] !== ''
                                                                ? parseInt(mapConfigurations[CONSTANTS.SEARCH.MINPRIORITY]) : null;
          $scope.learnedLessonSearch.maxPriority              = mapConfigurations[CONSTANTS.SEARCH.MAXPRIORITY] !== undefined && mapConfigurations[CONSTANTS.SEARCH.MAXPRIORITY] !== ''
                                                                ? parseInt(mapConfigurations[CONSTANTS.SEARCH.MAXPRIORITY]) : null;
          $scope.learnedLessonSearch.minRiskRating            = mapConfigurations[CONSTANTS.SEARCH.MINRISKRATING] !== undefined && mapConfigurations[CONSTANTS.SEARCH.MINRISKRATING] !== ''
                                                                ? parseInt(mapConfigurations[CONSTANTS.SEARCH.MINRISKRATING]) : null;
          $scope.learnedLessonSearch.maxRiskRating            = mapConfigurations[CONSTANTS.SEARCH.MAXRISKRATING] !== undefined && mapConfigurations[CONSTANTS.SEARCH.MAXRISKRATING] !== ''
                                                                ? parseInt(mapConfigurations[CONSTANTS.SEARCH.MAXRISKRATING]) : null;

          $scope.ngEngine.$priority = CONSTANTS.SHOW_ALL;

          if ($scope.learnedLessonSearch.minPriority != null && $scope.learnedLessonSearch.maxPriority != null) {
            $scope.ngEngine.$priority = CONSTANTS.BETWEEN;
          }
          else if ($scope.learnedLessonSearch.minPriority != null) {
            $scope.ngEngine.$priority = CONSTANTS.GREATER_OR_EQUAL;
          }
          else if ($scope.learnedLessonSearch.maxPriority != null) {
            $scope.ngEngine.$priority = CONSTANTS.LESS_OR_EQUAL;
          }

          $scope.ngEngine.$riskRating = CONSTANTS.SHOW_ALL;

          if ($scope.learnedLessonSearch.minRiskRating != null && $scope.learnedLessonSearch.maxRiskRating != null) {
            $scope.ngEngine.$riskRating = CONSTANTS.BETWEEN;
          }
          else if ($scope.learnedLessonSearch.minRiskRating != null) {
            $scope.ngEngine.$riskRating = CONSTANTS.GREATER_OR_EQUAL;
          }
          else if ($scope.learnedLessonSearch.maxRiskRating != null) {
            $scope.ngEngine.$riskRating = CONSTANTS.LESS_OR_EQUAL;
          }

          $scope.learnedLessonSearch.budgetYear = mapConfigurations[CONSTANTS.SEARCH.BUDGETYEAR] !== '' ? mapConfigurations[CONSTANTS.SEARCH.BUDGETYEAR] : null;
          $scope.learnedLessonSearch.since = mapConfigurations[CONSTANTS.SEARCH.SINCE] !== '' ? mapConfigurations[CONSTANTS.SEARCH.SINCE] : null;
          $scope.learnedLessonSearch.until = mapConfigurations[CONSTANTS.SEARCH.UNTIL] !== '' ? mapConfigurations[CONSTANTS.SEARCH.UNTIL] : null;

          // Load table
          $scope.findLLAA();
        });
      };

      /**
       *  Resets the learned lesson parameters from learnedLessonSearch.
       */
      $scope.resetLessonSearchParams = function(){

        $scope.learnedLessonSearch.llaaQuery = null;
        $scope.learnedLessonSearch.llaaIncludeGlobal = null;
        $scope.learnedLessonSearch.llaaPrograms = null;
        $scope.learnedLessonSearch.llaaCustomers = null;
        $scope.learnedLessonSearch.llaaSellers = null;
        $scope.learnedLessonSearch.llaaGeographies = null;
        $scope.learnedLessonSearch.llaaCustomerTypes = null;
        $scope.learnedLessonSearch.llaaFundingSources = null;
        $scope.learnedLessonSearch.llaaProcessGroups = null;
        $scope.learnedLessonSearch.llaaKnowledgeAreas = null;
        $scope.learnedLessonSearch.llaaImportanceActions = null;
        $scope.learnedLessonSearch.llaaImportanceRecommendations = null;
        $scope.learnedLessonSearch.llaaOwner = null;
        $scope.learnedLessonSearch.llaaJobProfile = null;
        $scope.learnedLessonSearch.llaaType = null;
        $scope.learnedLessonSearch.llaaImpactSatisfaction = null;
        $scope.learnedLessonSearch.llaaInternalProject = null;
        $scope.learnedLessonSearch.llaaIsGeoSelling = null;
        $scope.learnedLessonSearch.llaaRag = null;
        $scope.learnedLessonSearch.llaaMaxImpactTime = null;
        $scope.learnedLessonSearch.llaaMinImpactTime = null;
        $scope.learnedLessonSearch.llaaMaxImpactCost = null;
        $scope.learnedLessonSearch.llaaMinImpactCost = null;
        $scope.learnedLessonSearch.llaaMaxRanking = null;
        $scope.learnedLessonSearch.llaaMinRanking = null;
        $scope.learnedLessonSearch.llaaSince = null;
        $scope.learnedLessonSearch.llaaUntil = null;

        $scope.ngEngine.$impactTime = CONSTANTS.SHOW_ALL;
        $scope.ngEngine.$impactCost = CONSTANTS.SHOW_ALL;
        $scope.ngEngine.$ranking = CONSTANTS.SHOW_ALL;
      };

      // Reset multivalue inputs.
      $scope.resetImpactTime = function() {

        $scope.learnedLessonSearch.llaaMinImpactTime = null;
        $scope.learnedLessonSearch.llaaMaxImpactTime = null;
      };

      $scope.resetImpactCost = function() {

        $scope.learnedLessonSearch.llaaMinImpactCost = null;
        $scope.learnedLessonSearch.llaaMaxImpactCost = null;
      };

      $scope.resetRanking = function() {

        $scope.learnedLessonSearch.llaaMinRanking = null;
        $scope.learnedLessonSearch.llaaMaxRanking = null;
      };


      /*************************************************************/
      /*                FIND FUNCTIONS (LLAA & PROJECT)            */
      /*************************************************************/

      /**
       * Find LLAA by initial filters
       */
      $scope.findLLAA = function() {

        $scope.learnedLessonSearch.llaaSince  = $filter('date')($scope.learnedLessonSearch.llaaSince, 'yyyy-MM-dd');
        $scope.learnedLessonSearch.llaaUntil  = $filter('date')($scope.learnedLessonSearch.llaaUntil, 'yyyy-MM-dd');
        $scope.learnedLessonSearch.since      = $filter('date')($scope.learnedLessonSearch.since, 'yyyy-MM-dd');
        $scope.learnedLessonSearch.until      = $filter('date')($scope.learnedLessonSearch.until, 'yyyy-MM-dd');

        // Remove codes select multiples
        //
        var filterValues = angular.copy($scope.learnedLessonSearch);

        filterValues.llaaPrograms             = removeCode($scope.learnedLessonSearch.llaaPrograms);
        filterValues.llaaCustomers            = removeCode($scope.learnedLessonSearch.llaaCustomers);
        filterValues.llaaSellers              = removeCode($scope.learnedLessonSearch.llaaSellers);
        filterValues.llaaGeographies          = removeCode($scope.learnedLessonSearch.llaaGeographies);
        filterValues.llaaCustomerTypes        = removeCode($scope.learnedLessonSearch.llaaCustomerTypes);
        filterValues.llaaFundingSources       = removeCode($scope.learnedLessonSearch.llaaFundingSources);
        filterValues.llaaKnowledgeAreas       = removeCode($scope.learnedLessonSearch.llaaKnowledgeAreas);
        filterValues.llaaOwner                = removeCode($scope.learnedLessonSearch.llaaOwner);
        filterValues.llaaJobProfile           = removeCode($scope.learnedLessonSearch.llaaJobProfile);

        filterValues.customerTypes            = removeCode($scope.learnedLessonSearch.customerTypes);
        filterValues.customers                = removeCode($scope.learnedLessonSearch.customers);
        filterValues.programs                 = removeCode($scope.learnedLessonSearch.programs);
        filterValues.categories               = removeCode($scope.learnedLessonSearch.categories);
        filterValues.performingOrganizations  = removeCode($scope.learnedLessonSearch.performingOrganizations);
        filterValues.projectManagers          = removeCode($scope.learnedLessonSearch.projectManagers);
        filterValues.functionalManagers       = removeCode($scope.learnedLessonSearch.functionalManagers);
        filterValues.sellers                  = removeCode($scope.learnedLessonSearch.sellers);
        filterValues.labels                   = removeCode($scope.learnedLessonSearch.labels);
        filterValues.stageGates               = removeCode($scope.learnedLessonSearch.stageGates);
        filterValues.contractTypes            = removeCode($scope.learnedLessonSearch.contractTypes);
        filterValues.geographies              = removeCode($scope.learnedLessonSearch.geographies);
        filterValues.classificationLevels     = removeCode($scope.learnedLessonSearch.classificationLevels);
        filterValues.technologies             = removeCode($scope.learnedLessonSearch.technologies);
        filterValues.sponsors                 = removeCode($scope.learnedLessonSearch.sponsors);
        filterValues.fundingSources           = removeCode($scope.learnedLessonSearch.fundingSources);

        filterValues.internalProject          = filterValues.internalProject !== '' ? filterValues.internalProject : null;
        filterValues.isGeoSelling             = filterValues.isGeoSelling !== '' ? filterValues.isGeoSelling : null;
        filterValues.rag                      = filterValues.rag !== '' ? filterValues.rag : null;
        filterValues.isIndirectSeller         = filterValues.isIndirectSeller !== '' ? filterValues.isIndirectSeller : null;


        // Get LLAAs
        learnedLessons  = LearnedLesson.query(filterValues, function() {

          $scope.tableParams = new ngTableParams (
            {
              page: 1,             // show first page
              count: 10,            // Initial value
              sorting: {
                code : 'asc'            // initial sorting
              }
            },
            {
              total: learnedLessons.length, // length of data
              getData: function ($defer, params) {

                // Indicator of the number of lessons learned shown in the learned lesson table
                params.showInfo = {
                  first: (params.page() * params.count()) - params.count()+1
                };
                params.showInfo.last = params.showInfo.first + params.count() -1;

                if (params.showInfo.last > params.total()) { params.showInfo.last = params.total(); }

                // use build-in angular filter
                var orderedData = params.sorting
                  ? $filter('orderBy')(learnedLessons, params.orderBy())
                  : learnedLessons;

                $scope.lessons = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve($scope.lessons);
              }
            }
          );

          /**
           * Sets the proper sort indicator to the custom header.
           * @param columnName
           * @returns {*}
           */
          $scope.sortIndicator = function(columnName) {

            if ($scope.tableParams.isSortBy(visibleField[$scope.columnNames.indexOf(columnName)], 'asc')) {

              return {'fa': true, 'fa-fw': true,'fa-sort-asc': true, 'fa-lg': true};
            } else if ($scope.tableParams.isSortBy(visibleField[$scope.columnNames.indexOf(columnName)], 'desc')) {

              return {'fa': true, 'fa-fw': true,'fa-sort-desc': true, 'fa-lg': true};
            } else {

              return {'fa': true, 'fa-fw': true,'fa-sort': true, 'fa-lg': true};
            }
          };
        });
      };

      /**
       * Find projects based on the filter
       */
      $scope.findProjects = function() {

        $scope.projectSearch.since = $filter('date')($scope.projectSearch.since, 'yyyy-MM-dd');
        $scope.projectSearch.until = $filter('date')($scope.projectSearch.until, 'yyyy-MM-dd');

        // Linkable projects query
        linkProjects = Project.query($scope.projectSearch, function() {

          angular.forEach(linkProjects, function(item){

            if (item.bac === undefined) {
              item.bac = 0;
            }
          });

          // Config link projects table
          $scope.projectTableParams = new ngTableParams (
            {
              page: 1,             // show first page
              count: 10,            // Initial value
              sorting: {
                code: 'asc'            // initial sorting
              }
            },
            {
              total: linkProjects.length, // length of data
              getData: function ($defer, params) {

                // Indicator of the number of linkable projects shown in the link table

                params.showInfo = {
                  first: (params.page() * params.count()) - params.count()+1
                };
                params.showInfo.last = params.showInfo.first + params.count() -1;

                if (params.showInfo.last > params.total()) { params.showInfo.last = params.total(); }

                // use build-in angular filter
                var orderedData = params.sorting
                  ? $filter('orderBy')(linkProjects, params.orderBy())
                  : linkProjects;

                $scope.projects = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve($scope.projects);
              }
            }
          );

          var elementToScroll = angular.element(document.getElementById('projectTableScrollAnchor'));
          $document.scrollToElement(elementToScroll,30,1000);

          /**
           * Sets the proper sort indicator to the custom header of the project link table.
           * @param projectColumnName
           * @returns {*}
           */
          $scope.linkSortIndicator = function(projectColumnName) {

            if ($scope.projectTableParams.isSortBy(projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)], 'asc')) {

              return {'fa': true, 'fa-fw': true,'fa-sort-asc': true, 'fa-lg': true};
            } else if ($scope.projectTableParams.isSortBy(projectVisibleField[$scope.projectColumnNames.indexOf(projectColumnName)], 'desc')) {

              return {'fa': true, 'fa-fw': true,'fa-sort-desc': true, 'fa-lg': true};
            } else {

              return {'fa': true, 'fa-fw': true,'fa-sort': true, 'fa-lg': true};
            }
          };

        });
      };

      // Initialize filters by user
      loadConfigurations();

      /*************************************************************/
      /*                   DATA MANAGING FUNCTIONS                 */
      /*************************************************************/

      /**
       * Function that starts the save of the learnedLesson - project relations
       */
      $scope.saveLinks = function() {

        // Reload errors
        $scope.errorMessage = false;
        $scope.errors = [];

        learnedLessons.forEach( function(learnedLesson) {

          if (learnedLesson.selected) {

            linkProjects.forEach(function (project) {

              if (project.selected) {

                learnedLesson.$assignToProject({codeProject: project.code},
                  function() {
                    $scope.showFloatingMessage = true;
                  },
                  function (error) {

                    $scope.errorMessage = true;
                    $scope.errors.push(error.data.message);
                    $scope.showFloatingMessage = false;
                });
              }
            });
          }

          learnedLesson.selected = false;
        });

        $scope.refreshProjectTable();

        showFloatingMessage();
      };

      /**
       * Delete learned lesson from the table
       *
       * @param item
       */
      $scope.deleteLearnedLesson = function (item) {

        $scope.learnedLessonSelected = item;

        ngDialog.openConfirm({
          template: 'views/learned-lessons/confirm-delete-dialog.html',
          disableAnimation: true,
          showClose: false,
          className: 'ngdialog-theme-plain',
          scope: $scope
        }).then(function () { // in case that is confirmed

          item.$remove(function() {

            learnedLessons.splice(learnedLessons.indexOf(item), 1);

            $scope.tableParams.reload();
          });

        });
      };

      /*************************************************************/
      /*                      SUPPORT FUNCTIONS                    */
      /*************************************************************/

      /**
       * Show floating message
       */
      function showFloatingMessage() {

        //TODO jordi.ripoll - 23/09/2015 - hay un plugin que lo hara
        $timeout(function() {
            $scope.transition = true;
          },
          1000
        );
        // Hide floating message in five seconds
        $timeout(function() {
            $scope.transition = false;
            $scope.showFloatingMessage = false;
          },
          5000
        );
      };

      /**
       * Remove code obejct array
       *
       * @param source
       * @returns {Array}
       */
      var removeCode = function(source) {

        var target = [];
        if (source !== undefined && source !== null) {
          source.forEach(function (value) {
            target.push(value.code);
          });
        }

        return target;
      };

      /**
       * Add code object to array
       *
       * @param source
       * @returns {*}
       */
      var addCodeObject = function(source) {

        var target = null;

        if (source !== undefined && source !== null && source !== '') {

          target = [];

          var arraySource = source.split(',');

          arraySource.forEach(function (value) {
            target.push({code:value});
          });
        }

        return target;
      };

      $scope.llaaInPrograms = function(programs, learnedLessonProgram) {

        var isIn = false;

        if(angular.isDefined(programs) && angular.isDefined(learnedLessonProgram)) {

          var i = 0;
          while(i < programs.length && !isIn) {

            if (angular.equals(programs[i].code, learnedLessonProgram.code)) {

              isIn = true;
            }
            i++;
          }
        }

        return isIn;
      };

    }
  ];

  return {
    controller: controller,
    parent: 'site'
  };
});


