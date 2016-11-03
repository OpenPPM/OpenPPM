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
 * File: general.filter.js
 * Create User: jordi.ripoll
 * Create Date: 16/09/2015 10:01:21
 */

/**
 * Created by javier.hernandez on 24/04/2015.
 */

define(['angular'], function(angular) {
  'use strict';

  var module = angular.module('openppm.filters', []);

  // Currency exemple 1,500.80 to 1.500,80
  module.filter('toCurrency', [ function () {
    return function (num, numberOfDecimals) {

      if (typeof numberOfDecimals === 'undefined') {
        numberOfDecimals = 2;
      }

      num = num+'';

      if (typeof num !== 'undefined' && num != '') {

        var sign;
        var decimals;
        var i;

        //num = num.toString().replace(/\$|\,/g, '.');
        if (isNaN(num)) {
          num = "0";
        }

        sign = (num == (num = Math.abs(num))); // absolute value

        num = Math.floor(num * Math.pow(10, numberOfDecimals) + 0.50000000001); // round down

        decimals = num % Math.pow(10, numberOfDecimals);

        num = Math.floor(num / Math.pow(10, numberOfDecimals)).toString();

        if (numberOfDecimals > 1 && decimals < Math.pow(10, numberOfDecimals-1)) {
          decimals = '0' + decimals;
        }

        for (i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++) {
          num = num.substring(0, num.length - (4 * i + 3)) + '.' + num.substring(num.length - (4 * i + 3));
        }

        return (((sign) ? '' : '-') + num + ',' + decimals);
      }

      return '';
    };
  }]);

  // Percentatge with formatter toCurrency
  module.filter('toPercentage', ['$filter', function ($filter) {
    return function (input, numberOfDecimals) {
      return $filter('toCurrency')(input * 100, numberOfDecimals) +'%';
    };
  }]);

});
