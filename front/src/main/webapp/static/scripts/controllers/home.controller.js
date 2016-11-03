'use strict';
define(['app'], function () {
    'use strict';

    var controller = ['$scope', '$log', 'CONSTANTS', 'principal',
        function ($scope, $log, CONSTANTS, principal) {
            $scope.user = principal.getIdentify();
        }];

    return {
        controller: controller,
        parent: 'site'
    };
});

