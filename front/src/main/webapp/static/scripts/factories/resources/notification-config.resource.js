/**
 * Created by Javier on 06/09/2015.
 */
'use strict';

define(['app'], function(app) {
  'use strict';

  app.factory('NotificationConfig', ['$resource', function ($resource) {

    /**
     * Notification resource
     */
    return $resource('../rest/config/notification/:code', { code: '@code'},
      {
          'update': { method:'PUT'}
      });
  }]);

});