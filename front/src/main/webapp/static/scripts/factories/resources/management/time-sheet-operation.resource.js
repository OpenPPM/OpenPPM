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
 * File: management-operations.resource.js
 * Create User: javier.hernandez
 * Create Date: 25/09/2015 09:27:59
 */

/**
 * Created by javier.hernandez on 25/09/2015.
 */
'use strict';

define(['app'], function(app) {
  'use strict';

  app.factory('TimeSheetOperation', ['$resource', function ($resource) {

    var url = '../rest/management/time-sheet/operation/:code';
    /**
     * Notification resource
     */
    return $resource(url, { code: '@code'},
      {
        'approve': { method:'PUT', url: url+'/approve', params: { code: '@code'}},
        'reject': { method:'PUT', url: url+'/reject', params: { code: '@code'}}
      });
  }]);

});