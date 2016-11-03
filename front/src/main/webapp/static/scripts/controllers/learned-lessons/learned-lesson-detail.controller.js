/**
 * Created by francisco.bisquerra on 19/08/2015.
 */

'use strict';
define(['app' , 'UtilsFactory', 'LearnedLessonResource', 'ActionLessonResource', 'RecommendationLessonResource',
  'CacheService', 'ProjectResource', 'StaticDataService'], function () {
  'use strict';

  var controller = ['$scope', '$timeout', '$location', '$stateParams', 'ngTableParams', '$filter','utils',
    'CONSTANTS', '$http', '$modal', 'principal', '$state', '$translate', 'getLearnedLesson', 'ActionLesson',
    'RecommendationLesson', '$q', '$window', '$CacheService','$StaticDataService',
    function ($scope, $timeout, $location, $stateParams, ngTableParams, $filter, utils,
              CONSTANTS, $http, $modal, principal, $state, $translate, getLearnedLesson, ActionLesson,
              RecommendationLesson, $q, $window, $CacheService, $StaticDataService) {

      $scope.CONSTANTS = CONSTANTS;

      // Not editable
      $scope.notEditable = $stateParams.notEditable;

      // get User
      $scope.user = principal.getIdentify();

      // Data provided to the different selects in learned-lesson-detail
      $scope.knowledgeAreas       = $CacheService.getKnowledgeAreas();
      $scope.profiles             = $CacheService.getProfiles();
      $scope.customers            = $CacheService.getCustomers();
      $scope.fundingSources       = $CacheService.getFundingSources();
      $scope.sellers              = $CacheService.getSellers();
      $scope.contractTypes        = $CacheService.getContractTypes();
      $scope.geographies          = $CacheService.getGeographicAreas();
      $scope.programs             = $CacheService.getPrograms();

      // Static Data
      $scope.processGroups = $StaticDataService.getProcessGroups();
      $scope.impactSatisfactions  = $StaticDataService.getImpactOnSatisfactions();
      $scope.types = $StaticDataService.getLearnedLessonTypes();
      $scope.importance = $StaticDataService.getLearnedLessonImportance();

      // GET DTO OBJECT
      $scope.learnedLesson = getLearnedLesson;

      //Submit variables
      $scope.lessonSubmit = false;
      $scope.actionSubmit = false;
      $scope.recommendationSubmit = false;

      /**
       * Rest new object
       */
      $scope.save = function (valid) {

        /* TODO francisco.bisquerra - 08/10/2015 - arreglo para formatear la fecha en funcion de la zona horaria, en caso de no hacerlo
         *   angularjs formatea la fecha segun el formato de california ocasionando erroes en las fechas debidos a la diferencia horaria  */
        $scope.learnedLesson.dateRaised = $filter('date')($scope.learnedLesson.dateRaised, 'yyyy-MM-dd');

        var actionsFinished = true;
        var recommendationsFinished = true;

        // check for any item being edited
        var i = 0;
        while(i < actions.length && actionsFinished) {

          if(actions[i].$edit === true) {

            actionsFinished = false
          }
          i++;
        }

        i=0;
        while(i < recommendations.length && recommendationsFinished) {

          if (recommendations[i].$edit === true) {

            recommendationsFinished = false
          }
          i++;
        }

        if (valid && actionsFinished && recommendationsFinished) {

          if ($scope.learnedLesson.code === undefined) {

            $scope.learnedLesson.$save(function () {
              // Collection of promises
              var promises = [];
              // Save/Update actions
              angular.forEach(actions, function (item) {

                //delete item.$edit;
                promises.push(item.$save({codeLearnedLesson: $scope.learnedLesson.code}));
              });

              // Save/Update recommendations
              angular.forEach(recommendations, function (item) {

                // delete item.$edit;
                promises.push(item.$save({codeLearnedLesson: $scope.learnedLesson.code}));
              });

              $q.all(promises).then(function () {

                nextStep();
              });

            });
          }
          else {

            $scope.learnedLesson.$update(function () {
              nextStep();
            });
          }
        }
      };

      var nextStep = function() {

        if ($stateParams.toProject) {

          var f = document.forms["frm_projects"];

          f.id.value      = $stateParams.codeProject;
          f.accion.value  = '';
          f.action        = '../' + CONSTANTS.SERVLET.PROJECTLEARNEDLESSON;

          f.submit();
        }
        else {
          $state.go('learned-lessons');
        }
      };

      /**
       * Cancel
       */
      $scope.cancel = nextStep;

      // Column names
      $scope.columnNamesActions =  [ 'VIEW.LEARNED_LESSON.ACTIONS_DETAIL', 'VIEW.LEARNED_LESSON.IMPORTANCE' ];

      // Column names
      $scope.columnNamesRecommendations = [ 'VIEW.LEARNED_LESSON.RECOMMENDATIONS_DETAIL', 'VIEW.LEARNED_LESSON.IMPORTANCE' ];

      var fields = [ 'name', 'importance' ];

      // Accessing from the add button or from view without related project
      if ($stateParams.code === '' && angular.isUndefined($scope.learnedLesson.idProject)) {

        // Set date raised, owner value
        $scope.learnedLesson.owner = {name: principal.getIdentify().name};
        $scope.learnedLesson.dateRaised = new Date();
      }

      // START Sorting functions

      /**
       * Sorting class configuration for actions happened
       *
       * @param actionLessonField
       * @returns {{sortable: *, sort-asc: Array, sort-desc: Array}}
       */
      $scope.getSortingClassActions = function(actionLessonField) {

        if ($scope.actionsTableParams !== undefined) {

          // Proper json to ng-class for ngTable
          return {
            'sortable': fields[$scope.columnNamesActions.indexOf(actionLessonField)],
            'sort-asc': $scope.actionsTableParams.isSortBy(fields[$scope.columnNamesActions.indexOf(actionLessonField)], 'asc'),
            'sort-desc': $scope.actionsTableParams.isSortBy(fields[$scope.columnNamesActions.indexOf(actionLessonField)], 'desc'),
            'table-description-width': fields[$scope.columnNamesActions.indexOf(actionLessonField)] === 'name',
            'table-importance-width': fields[$scope.columnNamesActions.indexOf(actionLessonField)] === 'importance'
          }
        }
      };

      /**
       * Sorting class configuration for recomendations in the future
       *
       * @param recommendationField
       * @returns {{sortable: *, sort-asc: Array, sort-desc: Array}}
       */
      $scope.getSortingClassRecommendations = function(recommendationField) {

        if ($scope.recommendationsTableParams !== undefined) {

          // Proper json to ng-class for ngTable
          return {
            'sortable': fields[$scope.columnNamesRecommendations.indexOf(recommendationField)],
            'sort-asc': $scope.recommendationsTableParams.isSortBy(fields[$scope.columnNamesRecommendations.indexOf(recommendationField)], 'asc'),
            'sort-desc': $scope.recommendationsTableParams.isSortBy(fields[$scope.columnNamesRecommendations.indexOf(recommendationField)], 'desc'),
            'table-description-width': fields[$scope.columnNamesRecommendations.indexOf(recommendationField)] === 'name',
            'table-importance-width': fields[$scope.columnNamesRecommendations.indexOf(recommendationField)] === 'importance'
          }
        }
      };

      /**
       * Actions happened sorting configuration
       *
       * @param actionLessonField
       */
      $scope.sortTableAcitons = function(actionLessonField) {

        var sortingData = {};

        // Sets the proper name values to ng-click for ngTable
        sortingData[fields[$scope.columnNamesActions.indexOf(actionLessonField)]] =
          $scope.actionsTableParams.isSortBy(fields[$scope.columnNamesActions.indexOf(actionLessonField)], 'asc')
            ? 'desc'
            : 'asc';

        $scope.actionsTableParams.sorting(sortingData);
      };

      /**
       * Recommendations in the future sorting configuration
       *
       * @param recommendationField
       */
      $scope.sortTableRecommendations = function(recommendationField) {

        var sortingData = {};

        // Sets the proper name values to ng-click for ngTable
        sortingData[fields[$scope.columnNamesRecommendations.indexOf(recommendationField)]] =
          $scope.recommendationsTableParams.isSortBy(fields[$scope.columnNamesRecommendations.indexOf(recommendationField)], 'asc')
            ? 'desc'
            : 'asc';

        $scope.recommendationsTableParams.sorting(sortingData);
      };

      // END Sorting functions

      // START Table configurations


      var configActionsTable = function() {

        // Actions happened table configuration
        $scope.actionsTableParams = new ngTableParams(
          {
            sorting: {
              description: 'asc'            // initial sorting
            }
          },
          {
            getData: function ($defer, params) {

              // use build-in angular filter
              var orderedData = params.sorting
                ? $filter('orderBy')(actions, params.orderBy())
                : actions;

              $defer.resolve(orderedData);
            }
          });

          /**
           * Sets the proper sort indicator to the custom header of the recommendations table.
           * @param projectColumnName
           * @returns {*}
           */
          $scope.actionLessonSortIndicator = function(actionLessonField) {

            if ($scope.actionsTableParams.isSortBy(fields[$scope.columnNamesActions.indexOf(actionLessonField)], 'asc')) {

              return {'fa': true, 'fa-fw': true,'fa-sort-asc': true};
            } else if ($scope.actionsTableParams.isSortBy(fields[$scope.columnNamesActions.indexOf(actionLessonField)], 'desc')) {

              return {'fa': true, 'fa-fw': true,'fa-sort-desc': true};
            } else {

              return {'fa': true, 'fa-fw': true,'fa-sort': true};
            }
          };
      };

      var configRecommendationsTable = function() {

        // Recommendations in the future table configuration
        $scope.recommendationsTableParams = new ngTableParams(
          {
            sorting: {
              description: 'asc'            // initial sorting
            }
          },
          {
            getData: function ($defer, params) {

              // use build-in angular filter
              var orderedData = params.sorting
                ? $filter('orderBy')(recommendations, params.orderBy())
                : recommendations;

              $defer.resolve(orderedData);
            }
          });

        /**
         * Sets the proper sort indicator to the custom header of the recommendations table.
         * @param projectColumnName
         * @returns {*}
         */
        $scope.recommendationSortIndicator = function(recommendationField) {

          if ($scope.recommendationsTableParams.isSortBy(fields[$scope.columnNamesRecommendations.indexOf(recommendationField)], 'asc')) {

            return {'fa': true, 'fa-fw': true,'fa-sort-asc': true};
          } else if ($scope.recommendationsTableParams.isSortBy(fields[$scope.columnNamesRecommendations.indexOf(recommendationField)], 'desc')) {

            return {'fa': true, 'fa-fw': true,'fa-sort-desc': true};
          } else {

            return {'fa': true, 'fa-fw': true,'fa-sort': true};
          }
        };
      };

      if ($stateParams.code !== '') {

        var actions = ActionLesson.query({codeLearnedLesson: $stateParams.code}, function() {

          configActionsTable();
        });

        var recommendations = RecommendationLesson.query({codeLearnedLesson: $stateParams.code}, function() {

         configRecommendationsTable();
        });
      } else {

        var actions = [], recommendations = [];

        configActionsTable();
        configRecommendationsTable();
      }

      // END Table configuration

      // START action table functions

      /**
       * Shows the inputs to modify the action row
       */
      $scope.addActionLesson = function () {

        actions.push(new ActionLesson({$edit : true}));
        $scope.actionsTableParams.reload();
      };

      /**
       * Deletes an element from the actions table and array
       *
       * @param actionLesson
       */
      $scope.deleteActionLesson = function(actionLesson) {

        var removeItemTable = function(actionLesson) {

          actions.splice(actions.indexOf(actionLesson), 1);
          $scope.actionsTableParams.reload();
        };

        if (actionLesson.code !== undefined) {

          actionLesson.$remove({codeLearnedLesson: $scope.learnedLesson.code},function() {
            removeItemTable(actionLesson);
          });
        }
        else {
          removeItemTable(actionLesson);
        }
      };

      /**
       *
       * @param actionLesson
       */
      $scope.saveActionLesson = function (actionLesson, valid) {

        if (valid){

          if (actionLesson.name !== '' && actionLesson.importance !== '') {

            if ($scope.learnedLesson.code !== undefined && actionLesson.code === undefined) {
              actionLesson.$save({codeLearnedLesson: $scope.learnedLesson.code});
            }
            else if (actionLesson.code !== undefined) {
              actionLesson.$update({codeLearnedLesson: $scope.learnedLesson.code});
            }

            actionLesson.$edit = false;
          }
        }
      };

      /**
       * Deletes the row if there is no information
       * @param actionLesson
       */
      $scope.cancelActionLesson = function (actionLesson) {

        if (actionLesson.code === undefined) {

          $scope.deleteActionLesson(actionLesson);
        }
        actionLesson.$edit = false;
      };

      // END action table functions


      // START recommendation table functions

      /**
       * Shows the inputs to modify the action row
       */
      $scope.addRecommendationLesson = function () {

        recommendations.push(new RecommendationLesson({$edit : true}));
        $scope.recommendationsTableParams.reload();
      };


      /**
       * Deletes an element from the recommendation table and array
       *
       * @param recommendation
       */
      $scope.deleteRecommendationLesson = function(recommendation) {

        var removeItemTable = function(recommendation) {

          recommendations.splice(recommendations.indexOf(recommendation), 1);
          $scope.recommendationsTableParams.reload();
        };

        if (recommendation.code !== undefined) {

          recommendation.$remove({codeLearnedLesson: $scope.learnedLesson.code}, function() {
            removeItemTable(recommendation);
          });
        }
        else {
          removeItemTable(recommendation);
        }
      };

      /**
       *
       * @param recommendation
       */
      $scope.saveRecommendationChanges = function (recommendation, valid) {

        if (valid) {

          if (recommendation.name !== '' && recommendation.importance !== '') {

            if ($scope.learnedLesson.code !== undefined && recommendation.code === undefined) {
              recommendation.$save({codeLearnedLesson: $scope.learnedLesson.code});
            }
            else if (recommendation.code !== undefined) {
              recommendation.$update({codeLearnedLesson: $scope.learnedLesson.code});
            }

            recommendation.$edit = false;
          }
        }
      };

      /**
       * Deletes the row if there is no information
       *
       * @param recommendation
       */
      $scope.cancelRecommendationChanges = function (recommendation) {

        if (recommendation.code === undefined) {

          $scope.deleteRecommendationLesson(recommendation);
        }
        recommendation.$edit = false;
      };

      // END recommendations table functions

      // Calendar inputs controller

      $scope.opened = false;

      $scope.openCalendar = function() {

        $scope.opened = true;
      };


      $scope.isDisabledProgramManager = function() {

        var isDisabled = false;

        if ($scope.notEditable) {

          isDisabled = true;
        }
        else if ($scope.user.profile.code === CONSTANTS.RESOURCE_PROFILES.PROGRAM_MANAGER
                  && angular.isDefined($scope.learnedLesson.project)) {

          isDisabled = true;
        }

        return isDisabled;
      };

    }];

  // Get data from REST for later usage on the table actions and the table recommendations
  var resolve = {

    getLearnedLesson: ['$stateParams', 'CONSTANTS', 'LearnedLesson', 'Project',
      function ($stateParams, CONSTANTS, LearnedLesson, Project) {

        var leanedLesson = new LearnedLesson();

        // Update
        if ($stateParams.code !== "" && $stateParams.code != undefined) {

          leanedLesson = LearnedLesson.get({code : $stateParams.code}, function(data) {

            data.dateRaised = new Date(data.dateRaised);
          });

          return leanedLesson;
        }
        // Create
        else if ($stateParams.codeProject !== undefined && $stateParams.codeProject.length > 0) {

            leanedLesson.project = Project.get({code: $stateParams.codeProject});

            leanedLesson.project.$promise.
              then(function () {

              // New
              if (angular.isUndefined($stateParams.codeProject) || $stateParams.codeProject === '') {

                  // Set default project
                  leanedLesson.program       = leanedLesson.project.program;
                  delete leanedLesson.program.programManager;
                  leanedLesson.customer      = leanedLesson.project.customer;
                  leanedLesson.contractType  = leanedLesson.project.contracttype;
                  leanedLesson.geography     = leanedLesson.project.geography;

                }

                leanedLesson.program       = leanedLesson.project.program;
                delete leanedLesson.program.programManager;
              });
          }

          return leanedLesson;

      }
    ]
  };

  return {

    controller: controller,
    resolve: resolve,
    parent: 'site'
  };
});