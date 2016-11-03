'use strict';

/**
 * @ngdoc function
 * @name openppm.controller:NotificationListCtrl
 * @description
 * # NotificationListCtrl
 * Controller of the openppm
 */
define(['app', 'NotificationResource'], function () {
  'use strict';

  var controller = ['$scope', 'Notification','CONSTANTS', function ($scope, Notification, CONSTANTS) {

    $scope.filterActions = {
      sinceOpened: false,
      untilOpened: false,
      advanced: false
    };

    $scope.filters = {
      since: null,
      until: null,
      subject: null,
      body: null,
      status: null,
      includeRead: false
    };

    $scope.notifications = [];
    $scope.CONSTANTS = CONSTANTS;

    $scope.find = function() {

      $scope.notifications = Notification.query($scope.filters);
    };

    $scope.viewNotification = function(notification) {

      if (notification.readDate === undefined) {
        notification.$markRead().then(function () {
          notification.$$showBody = !notification.$$showBody;
        });
      }
      else {
        notification.$$showBody = !notification.$$showBody;
      }
    };

    $scope.statusMessage = function(notification) {

      if (notification.status === 'ERROR') {
        return notification.messageError;
      }
      else {
        return notification.status;
      }
    };

    // Row class
    $scope.activeRow = function($index) {
      return ( $index % 2 === 0) ?'':'active';
    };

    $scope.checkAll = false;
    // Events

    $scope.onChangeAll = function() {

      $scope.notifications.forEach(function(notification){
        notification.$$checked = $scope.checkAll;
      });
    };

    $scope.onChangeNotification = function() {

    };

  }];

  return {
    controller: controller,
    parent: 'site'
  };
});
