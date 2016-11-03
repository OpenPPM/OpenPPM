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
 * File: util.js
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:24
 */

/**
 * UI Launches confirm alert
 * @param title
 * @param msg Confirm description
 * @param msg_yes Message to confirm
 * @param msg_no Message to cancel
 * @param funcionConfirm Function to execute on confirm
 * @param functionClose Function to execute on close confirm
 * @return
 */

function confirmUI(title, msg, msg_yes, msg_no, funcionConfirm, functionClose) {
	$('#dialog-confirm-msg').html(msg);
	$('#dialog-confirm').dialog("option", "buttons",
		{
			'No': function() {
				$('#dialog-confirm').dialog("close");
				if (typeof functionClose === 'function') { functionClose(); }
			},
			'Yes': function() {
				$('#dialog-confirm').dialog("close");
				if (typeof funcionConfirm === 'function') { funcionConfirm(); }
			}
		}
	);
	$('#dialog-confirm').dialog('option', 'title', title);
	$('#dialog-confirm').dialog('open');
}
function closeConfirmUI() {
	$('#dialog-confirm').dialog("close"); 
}

function confirmTextUI(title, msg, msg_yes, msg_no, funcion_confirm) {
	$('#textInfoPopup').val('');
	$('#dialogConfirmTextMsg').html(msg);
	$('#dialogConfirmText').dialog("option", "buttons",
	    {
			'No': function() {$('#dialogConfirmText').dialog("close"); },
			'Yes': function() { $('#dialogConfirmText').dialog("close"); funcion_confirm($('#textInfoPopup').val()); }
		}
	);
	$('#dialogConfirmText').dialog('option', 'title', title);
	$('#dialogConfirmText').dialog('open');
}


/**
 * Return target from event.
 * Tested in IE 7 & Firefox 3.5 or +
 * 
 * @param e Event
 * @return
 * @author Juanma Lopez
 */
function getTargetFromEvent(e) {
	var target;
	
	if (!e) var e = window.event;
	if (e.target) target = e.target;
	else if (e.srcElement) target = e.srcElement;
	
	return target;
}

/**
 * Return all possible combinatios of a full name for 'File As' field
 * @param name
 * @return
 */
function getFileAs(name) {
	var split_name = name.split(" ");
	var possible_names = new Array();
	for (var i = 0; i < split_name.length; i ++) {
		var segunda_parte = "";
		var j;
		for (j = 0; j < i; j++) {
			segunda_parte += split_name[j] + (j+1==i?"":" ");
		}
		var primera_parte = "";
		for (var k = j; k < split_name.length; k ++) {
			primera_parte += split_name[k] + (k+1==split_name.length?"":" ");
		}
		if (segunda_parte != "")
			segunda_parte = ", " + segunda_parte;
		possible_names.push(primera_parte + segunda_parte);
	}

	return possible_names;
}

/**
 * Return if a date is less than or equal to another
 * @param dinit
 * @param dend
 * @return
 */
function dateBefore(dinit, dend, pattern){
	var d_init = toDate(dinit, pattern);
	var d_end = toDate(dend, pattern);
	if (d_init <= d_end){ return true; }
	
	return false;
}

/**
 * Convert a string in given pattern (or dd/mm/yyyy by default) to JS Date
 * @param value
 * @return
 */
function toDate(value, pattern) {
	var sValue = String(value);
	if ( sValue.length < 10 || sValue.charAt(2) != "/" || sValue.charAt(5) != "/" ) {
		return false;
	}
	
	var year, month, day;
	day 	= sValue.substring(0, 2);
	month 	= sValue.substring(3, 5) -1;
	year	= sValue.substring(6);
	
	return Date.UTC( year, month , day, 0, 0, 0);
}

/**
 * Remove chars from mask number
 * @param mask
 * @return
 */
function rmvChar(mask) {
	var s = mask.replace(/\./gi, "");
	s = s.replace(/\,/gi, ".");
	
	return s; // For decimals
}

/**
 * Remove "," char from mask 
 * @param mask
 * @return
 */
function rmvSeparator(mask) {
	return mask.replace(",", "");
}

/**
 * Format number
 * @param num
 * @param decimal
 * @param separation
 * @return
 */
function formatNumber(num, separation, decimal) {
	return formatNum(num, separation, decimal, false);
}
function formatInteger(num) {
	return formatNum(num);
}
function formatNum(num, separation, decimal, integer){
	num += '';
	var splitStr = num.split(".");
	var splitLeft = splitStr[0];
	
	var regx = /(\d+)(\d{3})/;
	while (regx.test(splitLeft)) {
		splitLeft = splitLeft.replace(regx, '$1' + separation + '$2');
	}
	
	if (integer) return splitLeft;
	
	var splitRight = splitStr.length > 1 ? decimal + splitStr[1] : '';
	
	// Only two decimals
	var right = '';
	if (splitRight[1] >= 0) {
		right = decimal + splitRight[1];
		if (splitRight[2] >= 0) {
			right = right + "" + splitRight[2];
		}
	}
		
	return splitLeft + right;
}

/**
 * Return radio button selected value
 * @param radio_input. document.form.radio_button_group_name
 * @return
 */
function get_radio_value(radio_input) {
	var value = null;
	for (var i=0; i < radio_input.length; i++){
		if (radio_input[i].checked) {
			value = radio_input[i].value;
		}
	}
	return value;
}

/**
 * Check if JS variable is null or is empty
 * @param variable
 * @return
 */
function isEmpty(variable) {
	return (variable== null || variable==undefined || variable=="");
}


function isDecimal(expression) {
	return (String(expression).search(/^\d+(\.\d+)?$/) != -1);
}
/*
Array.prototype.move = function (old_index, new_index) {
    if (new_index >= this.length) {
        var k = new_index - this.length;
        while ((k--) + 1) {
            this.push(undefined);
        }
    }
    this.splice(new_index, 0, this.splice(old_index, 1)[0]);
    return this; // for testing purposes
};
*/