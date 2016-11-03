'use strict';

/**
 * @ngdoc function
 * @name openppm.controller:NotificationAdminCtrl
 * @description
 * # NotificationAdminCtrl
 * Controller of the openppm
 */
define(['app', 'NotificationConfigResource', 'StaticDataService'], function () {
  'use strict';

  var controller = ['$scope', 'CONSTANTS', 'NotificationConfig', '$StaticDataService',
    function ($scope, CONSTANTS, NotificationConfig, $StaticDataService) {

      // Load Static Data
      $scope.notificationModes = $StaticDataService.getNotificationModes();
      $scope.CONSTANTS = CONSTANTS;
      $scope.notificationSelected = {profiles:[null]};
      $scope.notifications = NotificationConfig.query();

      $scope.selectNotification = function(notification) {

        notification.selected = true;
        $scope.notificationSelected.selected = false;
        $scope.notificationSelected = notification;
      };

      $scope.save = function() {

        $scope.notifications.forEach(function(notification) {

          if (notification.code === undefined) {
            notification.$save();
          }
          else {
            notification.$update();
          }
        });
      };
  }];

  return {
    controller: controller,
    parent: 'site'
  };
});
