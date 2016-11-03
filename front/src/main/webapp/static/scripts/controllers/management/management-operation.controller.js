/**
 * @ngdoc function
 * @name testApp.controller:ManagementOperationResource
 * @description
 * # ManagementOperationResource
 * Controller of the openppm
 */
define(['app', 'moment', 'moment-range', 'TimeSheetOperationResource', 'OperationResource', 'CacheService', 'ContactResource'], function (app, moment) {
  'use strict';

  var controller = ['$scope', 'CONSTANTS', '$modal', '$compile', '$sce', '$document', 'TimeSheetOperation', 'Operation', '$CacheService', 'Contact', '$translate', '$q', 'principal',
    function ($scope, CONSTANTS, $modal, $compile, $sce, $document, TimeSheetOperation, Operation, $CacheService, Contact, $translate, $q, principal) {

      $scope.principal = principal;

      $scope.showWeekend = false;

      $translate(['MODEL.POOL.NAME', 'VIEW.PROJECT.SELLER', 'MODEL.NAME', 'MODEL.STATUS', 'DATE.NAME', 'DATE.HOURS']).
        then(function (labels) {

          $scope.labelList = {
            pool: labels['MODEL.POOL.NAME'],
            seller: labels['VIEW.PROJECT.SELLER'],
            name: labels['MODEL.NAME'],
            status: labels['MODEL.STATUS'],
            date: labels['DATE.NAME'],
            hours: labels['DATE.HOURS']
          };
        });

      $scope.addContact = function($item) {

        var add = true;
        var i = 0;
        while(add && i < $scope.contacts.length) {
          add = $scope.contacts[i++].code !== $item.code;
        }
        if (add) {
          $scope.contacts.push($item);
        }

        $scope.userSelected = undefined;
      };

      $scope.removeContact = function(contact) {
        $scope.contacts.splice($scope.contacts.indexOf(contact), 1);
      };

      $scope.exportToCSV = function() {

        var data = [[$scope.labelList.date, $scope.labelList.name, $scope.labelList.hours, $scope.labelList.status]];

        var addRow = function(time, week, day) {

          var date = moment(week.initDate).add((day-1), 'days').format(CONSTANTS.DATE.FORMAT_DATE_MOMENT);
          var hours = week['hoursDay'+day] || 0;
          var status = hours > 0? week.status : '';

          data.push([date, time.name, hours, status]);
        };

        $scope.timeSheetOperationList.forEach(function(time) {

          // Add hours
          time.$$weekList.forEach(function(week){

            addRow(time, week, 1);
            addRow(time, week, 2);
            addRow(time, week, 3);
            addRow(time, week, 4);
            addRow(time, week, 5);
            addRow(time, week, 6);
            addRow(time, week, 7);
          });

        });

        return data;
      };

      /**
       * Approve timesheet operation
       */
      $scope.approve = function() {

        // Reset alert
        $scope.success = false;

        // Scroll to week
        if (angular.isDefined()) {
          $document.scrollToElement($scope.lastWeekSelected, 30, 1000);
        }

        var promises = [];

        $scope.timeSheetOperationList.forEach(function(timeSheetOperation) {

          var weekList = [];
          timeSheetOperation.weekList.forEach(function(week) {
            if (week.$$selected && week.code) {
              weekList.push(angular.copy(week));
            }
          });

          if (weekList.length > 0) {

            var timeSheetToSave = angular.copy(timeSheetOperation);
            timeSheetToSave.weekList = weekList;
            promises.push(timeSheetToSave.$approve());
          }
        });

        if (promises.length > 0) {
          $q.all(promises).then(function () {
            $scope.find(true);
          });
        }
      };

      /**
       * Reject timesheet operation
       */
      $scope.reject = function() {

        // Reset alert
        $scope.success = false;

        var promises = [];

        $scope.timeSheetOperationList.forEach(function(timeSheetOperation) {

          var weekList = [];
          timeSheetOperation.weekList.forEach(function(week) {
            if (week.$$selected) {
              weekList.push(angular.copy(week));
            }
          });

          if (weekList.length > 0) {

            var timeSheetToSave = angular.copy(timeSheetOperation);
            timeSheetToSave.weekList = weekList;
            promises.push(timeSheetToSave.$reject());
          }
        });

        if (promises.length > 0) {

          $q.all(promises).then(function () {
            $scope.find(true);
          });
        }
      };

      /**
       * Reset filters
       */
      $scope.reset = function() {

        $scope.warning  = false;
        $scope.success  = false;
        $scope.contacts = [];

        $scope.filters = {
          since: moment().startOf('month').toDate(),
          //since: moment().startOf('month').subtract(1, 'years').toDate(),
          //until: moment().endOf('month').subtract(1, 'years').subtract(15, 'days').add(2, 'months').toDate(),
          until: moment().endOf('month').add(2, 'months').toDate(), // TODO delete subtract and add
          codeOperation: $scope.operationList.length > 0 ? $scope.operationList[$scope.operationList.length -1].code : undefined
        };

        $scope.operation    = $scope.operationList.length > 0 ? $scope.operationList[$scope.operationList.length -1] : undefined;
        $scope.userSelected = undefined;
        $scope.events       = [];
        $scope.calendars    = [];
        $scope.timeSheetOperationList = undefined;
      };

      // Set select data
      $scope.operationList = Operation.query({availableForApprove: true}, function() {
        $scope.reset();
      });

      $scope.poolList = principal.isUserInProfile(CONSTANTS.RESOURCE_PROFILES.RESOURCE) ? [] : $CacheService.getPoolList();
      $scope.skillList = principal.isUserInProfile(CONSTANTS.RESOURCE_PROFILES.RESOURCE) ? [] : $CacheService.getSkillList();
      $scope.jobCategoryList = principal.isUserInProfile(CONSTANTS.RESOURCE_PROFILES.RESOURCE) ? [] : $CacheService.getJobCategoryList();

      $scope.CONSTANTS = CONSTANTS;
      $scope.timeSheetOperationList = undefined;

      /**
       * Find timesheet operations
       *
       * @param success
       */
      $scope.find = function(success) {

        var since = moment($scope.filters.since).startOf('week');
        var until = moment($scope.filters.until).endOf('week');

        $scope.isLookingFor = true;
        $scope.warning = false;
        $scope.success = success;

        var diffMonths = until.diff(since, 'days');

        if (diffMonths > 100) {
          $scope.warning = true;
        }

        $scope.filters.codeContact = [];
        if ($scope.contacts.length > 0) {

          $scope.contacts.forEach(function(contact) {
            $scope.filters.codeContact.push(contact.code);
          });
        }

        // Set code operation
        if (angular.isDefined($scope.filters) && angular.isDefined($scope.filters.codeOperation)) {
          $scope.filters.codeOperation = $scope.operation.code;
        }

        $scope.timeSheetOperationListCopy = TimeSheetOperation.query($scope.filters, function () {

          // copy to arrive information
          $scope.timeSheetOperationList= angular.copy($scope.timeSheetOperationListCopy);

          $scope.dateList = [];

          moment().range(since, until).by('weeks', function (moment) {

            $scope.dateList.push({
              since: moment.startOf('week').toDate(),
              until: angular.copy(moment).endOf('week').toDate()
            });

            $scope.timeSheetOperationList.forEach(function (timeSheetOperation) {

              if (timeSheetOperation.$$weekList === undefined) {
                timeSheetOperation.$$weekList = [];
              }

              var weekItem = {};

              timeSheetOperation.weekList.forEach(function (week) {

                if (week.initDate === moment.format(CONSTANTS.DATE.FORMAT_DATE_API)) {

                  if (
                    // RM only modify APP1 or APP2
                  ((week.status === CONSTANTS.TIME_SHEET.STATUS.APP0 || week.status === CONSTANTS.TIME_SHEET.STATUS.APP3) &&
                  principal.isUserInProfile(CONSTANTS.RESOURCE_PROFILES.RESOURCE_MANAGER)) ||

                    // LOG/PMO only modify APP2 or APP3
                  ((week.status === CONSTANTS.TIME_SHEET.STATUS.APP0 || week.status === CONSTANTS.TIME_SHEET.STATUS.APP1) &&
                  (principal.isUserInProfile(CONSTANTS.RESOURCE_PROFILES.PMO) || principal.isUserInProfile(CONSTANTS.RESOURCE_PROFILES.LOGISTIC))) ||

                    // TM Not modify anything
                  principal.isUserInProfile(CONSTANTS.RESOURCE_PROFILES.RESOURCE)) {

                    week.code = undefined;
                  }
                  return weekItem = week;
                }
              });

              timeSheetOperation.$$weekList.push(weekItem);
            });
          });

          $scope.isLookingFor = false;
          loadCalendars();
        });

      };

      /**
       * Day class for calendar
       *
       * @param date
       * @param mode
       * @returns {string}
       */
      $scope.getDayClass = function(date, mode) {

        var dayClass= '';

        if ("day" === mode &&
          (5 === moment(date).weekday() || 6 === moment(date).weekday())) {

          dayClass = 'weekend';
        }

        return dayClass;
      };

      /**
       * Find users
       *
       * @param $viewValue
       * @returns {*}
       */
      $scope.getUsers = function($viewValue) {

        return Contact.query({query: $viewValue, disableLoading:true, codePool: $scope.filters.pool,
          codeSkill: $scope.filters.codeSkill, codeJobCategory: $scope.filters.codeJobCategory}).$promise.then(function(response) {
          return response;
        });
      };

      $scope.getPopoverHtml = function(timeSheetOperation) {

        var html = '';
        if (timeSheetOperation.pool) {
          html += '<div><label>'+$scope.labelList.pool+':</label> '+timeSheetOperation.pool.name+'</div>';
        }

        if (timeSheetOperation.seller) {
          html += '<div><label>'+$scope.labelList.seller+':</label> '+timeSheetOperation.seller.name+'</div>';
        }

        return html;
      };

      $scope.getBadgeColor = function(status, hours) {

        var color = '';

        if (hours > 0) {
          switch (status) {
            case CONSTANTS.TIME_SHEET.STATUS.APP0:
              color = 'label-info';
              break;
            case CONSTANTS.TIME_SHEET.STATUS.APP1:
              color = 'label-danger';
              break;
            case CONSTANTS.TIME_SHEET.STATUS.APP2:
              color = 'label-warning';
              break;
            case CONSTANTS.TIME_SHEET.STATUS.APP3:
              color = 'label-success';
              break;
            case CONSTANTS.TIME_SHEET.REJECT:
              color = 'label-light';
              break;
          }
        }

        return color;
      };

      $scope.changeSelect = function(timeSheetOperation) {

        timeSheetOperation.$$selected = !timeSheetOperation.$$selected;

        timeSheetOperation.$$weekList.forEach(function(week) {

          week.$$selected = timeSheetOperation.$$selected && week.code !== undefined;
        });
      };

      /**
       * Event clicked
       * @param event
       */
      $scope.eventClicked = function (event){

        if(event.code !== undefined) {
          var week = angular.element('#week-' + event.code);
          $document.scrollToElement(week, 30, 1000);
        }
      };

      /**
       * Render Tooltip
       *
       * @param event
       * @param element
       */
      $scope.eventRender = function(event, element) {

        var id = event.title.replace(/\W/g,"_");
        element.attr({'tooltip-html': id});

        $scope[id] = $sce.trustAsHtml(event.title);
        $compile(element)($scope);
      };

      $scope.events = [];

      var loadCalendars = function() {

        // Load Events
        $scope.events = [];

        if ($scope.timeSheetOperationList) {

          var addEvent = function(name, start, end, week, sumHours) {

            // Get title
            //
            var title = ' ';

            if (principal.isUserInProfile(CONSTANTS.RESOURCE_PROFILES.RESOURCE)) {
              title += sumHours + 'h';
            }
            else {
              title += name + ' (' + sumHours +'h)';
            }

            // Get class name
            //
            var className = '';

            if (week.suggestReject) {
              className = $scope.getBadgeColor(CONSTANTS.TIME_SHEET.REJECT, 8);
            }
            else {
              className = $scope.getBadgeColor(week.status, 8)
            }


            $scope.events.push({
              title: title,
              start: angular.copy(start),
              end: angular.copy(end),
              className: className,
              allDay: true,
              code: week.code
            });
          };

          $scope.timeSheetOperationList.forEach(function (time) {

            time.weekList.forEach(function (week) {

              var start = moment(week.initDate, CONSTANTS.DATE.FORMAT_DATE_API);
              var end   = angular.copy(start);

              var lastHours = false;

              var consecutiveDays = 0;
              var sumHours        = 0;

              for (var i = 1; i <= 7; i++) {

                var hours     = week['hoursDay'+i];
                var hasHours  = (hours !== undefined && hours > 0);

                if (!hasHours && lastHours) {

                  var endCopy = angular.copy(end);

                  // Only one day
                  if (consecutiveDays === 1) {
                    endCopy.subtract(1,'days');
                  }

                  // add
                  addEvent(time.name, start, endCopy, week, sumHours);

                  // Reset vars
                  lastHours       = false;
                  consecutiveDays = 0;
                  sumHours        = 0;

                  // Set next start end
                  end.add(1,'days');
                  start = angular.copy(end);
                }
                else if (hasHours) {

                  consecutiveDays ++;
                  sumHours += hours;

                  lastHours = true;

                  end.add(1,'days');
                }
                else {

                  start = angular.copy(end);

                  start.add(1,'days');
                  end.add(1,'days');
                }
              }

              // add
              if (lastHours) {
                addEvent(time.name, start, end, week);
              }

            });
          });
        }


        $scope.calendars = [];
        var since = moment($scope.filters.since).startOf('month');
        var until = moment($scope.filters.until).endOf('month');

        var diffMonths = until.diff(since, 'months');

        for (var i = 0; i <= diffMonths; i++) {

          var date = angular.copy(since);
          date.add(i, 'month');

          var config = {
            name:'cal'+i,
            views: {
              month: {
                eventLimit: 5
              }
            },
            lang: principal.getIdentify().locale,
            height: 700,
            firstDay: 1,
            header:{
              left: '',
              center: 'title',
              right: ''
            },
            weekends:$scope.showWeekend,
            eventClick:$scope.eventClicked,
            eventRender: $scope.eventRender,
            defaultDate: date,
            fixedWeekCount: false
          };

          $scope.calendars.push(config);
        }

        // Event sources array
        $scope.eventSources = [$scope.events];
      };

      /* SUPPORT FUNCTIONS */

      $scope.closeSuccessAlert = function() {

        $scope.success = false
      };

    }];

  return {
    controller: controller,
    parent: 'site',
    resolve: {
      checkPermission: ['principal', '$state', 'CONSTANTS', function(principal, $state, CONSTANTS) {

        var user = principal.getIdentify();

        if (user.profile.code !== CONSTANTS.RESOURCE_PROFILES.PMO &&
          user.profile.code !== CONSTANTS.RESOURCE_PROFILES.LOGISTIC &&
          user.profile.code !== CONSTANTS.RESOURCE_PROFILES.RESOURCE &&
          user.profile.code !== CONSTANTS.RESOURCE_PROFILES.RESOURCE_MANAGER) {

          $state.go('not-allowed');
        }
      }]
    }
  };
});
