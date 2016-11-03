'use strict';

/**
 * @ngdoc function
 * @name openppm.controller:NotificationUserCtrl
 * @description
 * # NotificationUserCtrl
 * Controller of the openppm
 */
define(['app', 'NotificationResource', 'StaticDataService'], function () {
  'use strict';

  var controller = ['$scope', 'CONSTANTS', 'Notification', '$StaticDataService',
    function ($scope, CONSTANTS, Notification, $StaticDataService) {

      // Load Static Data
      $scope.notificationModes = $StaticDataService.getNotificationModes();
      $scope.CONSTANTS = CONSTANTS;

      // Prepare data for view
      $scope.notificationsData = [];

      var active = false;
      var notifications = Notification.query(function() {

        notifications.forEach(function (notification) {

          var firstProfile = true;
          notification.profiles.forEach(function (profile, index) {

            $scope.notificationsData.push({
              notification: notification,
              index: index,
              firstProfile: firstProfile,
              classRow: active ? 'active' : ''
            });

            firstProfile = false;

          });
          active = !active;
        });
      });

      $scope.save = function() {

        notifications.forEach(function(notification) {

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
