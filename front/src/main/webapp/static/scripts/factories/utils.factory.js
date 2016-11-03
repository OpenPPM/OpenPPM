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
 * File: learned-lesson.service.js
 * Create User: mariano.fontana
 * Create Date: 25/08/2015 14:06:20
 */

/**
 * Created by francisco.bisquerra on 24/08/2015.
 */

define(['angular'], function(angular) {
  'use strict';

  var module = angular.module('openppm.utils', []);
  module.factory('utils', [ function () {

    var root = {};

    /**
     * Filter the data array based on the filter input value
     * @param orderedData
     * @param visibleFields
     * @param filter
     * @returns {Array}
     */
    root.fullFilter = function (orderedData, visibleFields, filter) {

      var filteredData = [];

      // For each orderedData element
      angular.forEach(orderedData, function (element) {

        var i = 0;
        var elementPushed = 0;
        var keys = Object.keys(element);

        // While no pushed and still there attributes to check
        while (!elementPushed && i < keys.length) {

          // If key is in visibleFields
          if (visibleFields.indexOf(keys[i]) !== -1) {

            // If contains filter push to filteredData
            if (element[keys[i]] !== null && element[keys[i]].toString().indexOf(filter) >= 0) {

              // add to filteredData
              this.push(element);
              elementPushed = 1;
            }
          }
          i++;
        }
      }, filteredData);

      return filteredData;
    };

    return root;
  }]);
});
