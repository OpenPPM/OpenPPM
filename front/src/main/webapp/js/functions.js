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
 * File: functions.js
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:32
 */

/** -------------------------------------------------------------------------
*
* Desarrollado por SM2 Baleares S.A.
*
* -------------------------------------------------------------------------
* Este fichero solo podra ser copiado, distribuido y utilizado
* en su totalidad o en parte, de acuerdo con los terminos y
* condiciones establecidas en el acuerdo/contrato bajo el que se
* suministra.
* -------------------------------------------------------------------------
*
* Proyecto : OpenPPM
* Autor : Javier Hernandez
* Fecha : Miercoles, 27 de Julio, 2011
*
* -------------------------------------------------------------------------
*/

/**
 * For call functions in one document ready
 */
$(document).ready(function() {

	$('#loading-ajax').ajaxStart(function(m) {
	  $(this).dialog('open');
	});

	$('#loading-ajax').ajaxStop(function() {
		if($(this).dialog('isOpen')) {
			$(this).dialog('close');
		}
	});
		
	// Execute all methods
	readyMethods.execute();
	
	setTextImporteEvents();
	setTextNumberOnlyEvents();
	
	// All a tags with class button are JQuery.Button
	loadButtons();
	
	// Show fieldSet in cookie
	if ($.cookie('openppm.fieldSetCookieShow') != null) {
		$($.cookie('openppm.fieldSetCookieShow')).siblings('div.legend').trigger('click');
	}
	
	//Para dar formato a todos los input file
	SI.Files.stylizeAll();
	
	// Tooltip no permissions
	$(".noPermissions").attr("bt-xtitle","No permissions");
	createBT('.noPermissions', "most");
});
/*********************************************/

function loadButtons() {
	$("a.boton, button.boton, button.DTTT_button, input[type=submit], input[type=button]").button();
}
function createBT(selector, position) {
	$(selector).bt({
		fill: '#F9FBFF',
		cssStyles: {color: '#343C4E', width: 'auto'},
		width: 250,
		padding: 10,
		cornerRadius: 5,
		spikeLength: 15,
		spikeGirth: 5,
		shadow: true,
		positions: typeof position == "undefined" ?  "top" : position
	});
}
var console;
if (typeof console === 'undefined') {
	
	Console = function() { this.type = 'Debug IE'; };
	Console.prototype.log = function(value) {
		
		if (typeof alertUI === 'function') { alertUI('Debug: '+typeof value,value + ''); }
		else { alert('Debug: '+typeof value +' - '+value); }
	};
	console = new Console();
}

function show(id) { $("#"+id).show('fast'); }
function hide(id) { $("#"+id).hide('fast'); }
function changeCookie(id, callBack) {
	if ($("#"+id).is(":visible")) {
		hideCookie(id);
		$("#"+id+"Btn").attr('src','images/ico_mas.gif');
		$("#"+id).parent().addClass("hidePrint");
	}
	else {
		showCookie(id, callBack);
		$("#"+id+"Btn").attr('src','images/ico_menos.gif');
		$("#"+id).parent().removeClass("hidePrint");
	}
}
function showCookie(id, callBack) {
	if (id != null) {
		id = '#'+id;
		var ids = $.cookie('openppm.fieldSetCookieShow');
		if (ids != null) {
			ids = ids.split(',');
			var insert = true;
			$(ids).each(function() { if (this == id) { return insert = false; } });
			if (insert) { ids.push(id); }
		}
		else { ids = id; }
		$.cookie('openppm.fieldSetCookieShow',ids, { expires: 365 });
		if(typeof callBack === 'function') { callBack.call(); }
		$(id).show('fast');
	}
}

function hideCookie(id) {
	if (id != null) {
		id = '#'+id;
		var ids = $.cookie('openppm.fieldSetCookieShow');
		if (ids != null) {
			ids = ids.split(',');
			$(ids).each(function(i) { if (this == id) { return ids.splice(i,1);} });
		}
		if (ids != null && ids.length > 0) { $.cookie('openppm.fieldSetCookieShow',ids, { expires: 365 });	}
		else { $.cookie('openppm.fieldSetCookieShow',null);}
	}
	$(id).hide('fast');
}

/**
 * Objeto genérico para llamadas de Ajax
 * @param _url
 * @param _msgError
 * @returns
 */
function AjaxCall(_url,_titleError,_type, _typeMethod, _global) {
	this.url = _url || "";
	this.dataType = (typeof _type === 'undefined'?"json":_type);
	this.typeMethod = (typeof _typeMethod === 'undefined'?"POST":_typeMethod);
	this.titleError = (typeof _titleError === 'undefined'?"Error":_titleError);
	this.msgError = "Puede deberse ha que se ha perdido la sesion";
	this.msgParse = "El objeto obtenido esta mal formado";
    this.global = (typeof _global === 'undefined'?true:_global);
}
AjaxCall.prototype.call = function(parametros,_success,_dataType) {

    var thiss = this;
    var dataType = (typeof _dataType === 'undefined' ? thiss.dataType : _dataType);
    $.ajax({
        url: thiss.url,
        dataType: dataType,
        type: thiss.typeMethod,
        data: (parametros),
        global: thiss.global,
        success: function (data) {

            if (typeof data === 'undefined' || data == null) {
                alertUI(thiss.titleError, 'Comunication refused');
            }
            else if (typeof data.error === 'string' && typeof data.idError === 'string') {
                $("#" + data.idError + " p:eq(0)").empty();
                var icon = '<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>';
                $("#" + data.idError + " p:eq(0)").append(icon + data.error);
                $("#" + data.idError).show();
            }
            else if (typeof data.error === 'string') {
                alertUI(thiss.titleError, data.error);
            }
            else {
                try {
                    if (typeof data.information !== 'undefined') {
                        informationSuccess(data.information, 'alert');
                    }
                    if (typeof data.success !== 'undefined') {
                        informationSuccess(data.success, 'success');
                    }
                    if (typeof _success === 'function') {
                        _success(data);
                    }
                }
                catch (errorSuccess) {
                    alertUI(thiss.titleError, '<b>' + errorSuccess + '</b><br><br>Function:<br>' + _success);
                }
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            var message = (typeof errorThrown !== 'undefined' ? errorThrown.message : (textStatus == 'parsererror' ? thiss.msgParse + ': (' + errorThrown + ')' : thiss.msgError));
            alertUI(thiss.titleError, message);
        }
    });
};


function toCurrency(num, numberOfDecimals) {
	
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
}

function toNumber(currencyNum) {

    if (typeof currencyNum !== 'undefined') {

        currencyNum = currencyNum.toString().replace(/\./g,"");
        currencyNum = currencyNum.toString().replace(/\,/g,".");

        if (isNaN(currencyNum)) {
            currencyNum = "0";
        }
    }

	return currencyNum;
}

/**
 *  Parse string number to float
 *
 * @param stringValue
 * @returns {Number*}
 */
function parseStringToFloat(stringValue) {

  var numberValue;

  if (stringValue !== undefined && stringValue !== "" && typeof stringValue == "string") {

    numberValue = parseFloat((!isDecimal(stringValue) ? toNumber(stringValue) : stringValue));

    numberValue = round(numberValue);
  }
  else {
    numberValue = 0;
  }

  return numberValue;
}

/**
 * Round number 2 decimal
 *
 * @param number
 * @returns {*}
 */
function round(number){

  var numberRounded;

  if (typeof number === "number"){
    numberRounded = Math.round(number * 100) / 100
  }

  return numberRounded;
}


function selectMultiple(prefix, name) {
	if ($.cookie(prefix+name) != null) {
		
		var values = ($.cookie(prefix+name)).split(',');
		if (values.length > 0) {
			for (var i = 0; i < values.length;i++) {
				$("#"+name+" option[value="+values[i]+"]").attr('selected', true);
			}
		}
		
	}
}

function loadMultiple(id, value) {
	if (value != '') {
		
		var values = value.split(',');
		if (values.length > 0) {
			for (var i = 0; i < values.length;i++) {
				$("#"+id+" option[value="+values[i]+"]").attr('selected', true);
			}
		}
		
	}
}

function loadMultipleCheck(name, values) {
	if (values != '') {
		
		var valuesArray = values.split(',');
		if (valuesArray.length > 0) {
			for (var i = 0; i < valuesArray.length;i++) {
				$('input[name='+name+'][value='+valuesArray[i]+']').prop('checked', true);
			}
		}
		
	}
}

function escape(value) {
	if (value == null) {
		return '';
	}
	if(typeof value == "string") {
		value = value.replace(/&/g,"&amp;");
		value = value.replace(/</g,"&lt;");
		value = value.replace(/>/g,"&gt;");
		value = value.replace(/"/g,'&#034;');
		value = value.replace(/'/g,"&#039;");
		value = value.replace(/€/g,"&euro;");
	}
	return value;
}

function unEscape(value) {
	if (value == null) {
		return '';
	}
	
	if(typeof value == "string") {
		value = value.replace(/&amp;/g,"&");
		value = value.replace(/&lt;/g,"<");
		value = value.replace(/&gt;/g,">");
		value = value.replace(/&#034;/g,'"');
		value = value.replace(/&#039;/g,"'");
		value = value.replace(/&euro;/g,"€");	
	}	

	return value;
}

/****************** Eventos ***********************/

function setTextNumberOnlyEvents(classNumber) {
	
	var $numbers = typeof classNumber === 'undefined' ? $('input:text.number') : $('input:text.'+classNumber);
	$numbers.attr("autocomplete","off");
	
	$numbers.bind({
		keydown: function(event) {
			if (!(((event.which == 67 || event.which == 86) && event.ctrlKey) || event.which == 46 || event.which == 8  || event.which == 9 || event.which == 37 || event.which == 39) &&
					!((event.which >= 48 && event.which <= 57) || (event.which >= 96 && event.which <= 105))) {

	                event.preventDefault();
	        }
		}
	});
}
function setTextImporteEvents(classImport) {
	
	var $importes = typeof classImport === 'undefined' ? $('input:text.importe') : $('input:text.'+classImport);
	
	// Todos los input de clase importe, deshabilitar el autocompletado
	$importes.attr("autocomplete","off");
	
	$importes.each(function() {
		$(this).val(toCurrency($(this).val()));
	});
	
	$importes.bind({
		
		keydown: function(event) {

			if (event.which == 110 || event.which == 190) {
				
				if ($(this).val().indexOf('.') != -1)  { event.preventDefault(); }
			}
			else if (($(this).hasClass('negativo') && event.which == 109)) {
				
				if ($(this).val().indexOf('-') != -1)  { event.preventDefault(); }
			}
			else if (!(((event.which == 67 || event.which == 86) && event.ctrlKey) || event.which == 46 || event.which == 8  || event.which == 9 || event.which == 37 || event.which == 39) &&
					!((event.which >= 48 && event.which <= 57) || (event.which >= 96 && event.which <= 105))) {

	                event.preventDefault();
	        }
		},
		
		focus: function(event) {
			if (!$(this).prop("readonly")) {
				this.value=toNumber(this.value);
			}
		},
		
		blur: function(event) {
			if (!$(this).prop("readonly")) {
				this.value=toCurrency(this.value);
			}
		}
	});
}
/**
 * Notifications
 */
function notification(msg, timeout, layout, type) {
	noty({
		"text":		msg+"",
		"layout":	layout,
		"type":		type,
		"textAlign":"center",
		"easing":	"swing",
		"animateOpen":{"height":"toggle"},
		"animateClose":{"height":"toggle"},
		"speed":	"500",
		"timeout":	timeout*1000,
		"closable":	true,
		"closeOnSelfClick":true
	});
}

/* Create legends */
function createLegend (selector ,name, colorBackground, colorBorder) {

    var $legend = $('<nobr/>');

    if (typeof colorBorder === 'undefined') {
        $legend.append($('<span/>', {'style': 'background:'+ colorBackground +';'}).append('&nbsp;&nbsp;'));
    }
    else {
        $legend.append($('<span/>', {'style': 'background:'+ colorBackground +';'+ 'border:1px solid '+ colorBorder +';'}).append('&nbsp;&nbsp;'));
    }

    $(selector).append($legend.append(name));
}

/* Open(true) and Close(false) all panels */
function openCloseAllPanels(option){
	if(option){
		$(".panel .content").attr('style','display:block;');
		$('.panel .legend img[src^="images/ico_"]').attr('src','images/ico_menos.gif');
		$('.panel').removeClass("hidePrint");
	}
	else {
		$(".panel .content").attr('style','display:none;');
		$('.panel .legend img[src^="images/ico_"]').attr('src','images/ico_mas.gif');
		$('.panel').addClass("hidePrint");
	}
}

/* Open panel */
function openPanel(id) {
	
	var panel 	= $("#"+ id).parent();
	var legend 	= $(panel).children('.legend');
	var content = $(panel).children('.content');
	
	$(panel).removeClass("hidePrint");
	$(legend).find('img[src^="images/ico_"]').attr('src','images/ico_menos.gif');
	$(content).attr('style','display:block;');
}

//Create an array with the values of all the input boxes in a column 
$.fn.dataTableExt.afnSortData['dom-text'] = function  ( oSettings, iColumn ) {
	
	var aData 	= [];
	var cont 	= 0;
	
	for (var i = 0; i < iColumn; i++) {
		
		if (oSettings.aoColumns[i].bVisible == false) {
			cont += 1;
		}
	}
	
	$( 'td:eq('+(iColumn-cont)+') input', oSettings.oApi._fnGetTrNodes(oSettings) ).each( function () {
		aData.push( this.value );
	} );

	return aData;
};

jQuery.extend({
    stringify  : function stringify(obj) {         
        if ("JSON" in window) {
            return JSON.stringify(obj);
        }

        var t = typeof (obj);
        if (t != "object" || obj === null) {
            // simple data type
            if (t == "string") obj = '"' + obj + '"';

            return String(obj);
        } else {
            // recurse array or object
            var n, v, json = [], arr = (obj && obj.constructor == Array);

            for (n in obj) {
                v = obj[n];
                t = typeof(v);
                if (obj.hasOwnProperty(n)) {
                    if (t == "string") {
                        v = '"' + v + '"';
                    } else if (t == "object" && v !== null){
                        v = jQuery.stringify(v);
                    }

                    json.push((arr ? "" : '"' + n + '":') + String(v));
                }
            }

            return (arr ? "[" : "{") + String(json) + (arr ? "]" : "}");
        }
    }
});

function exportImage(id) {

    html2canvas(document.getElementById(id), {
        onrendered: function(canvas) {
            var img = canvas.toDataURL("image/jpg");
            window.open(img, '_blank');
        }
    });
}

function chartToImage(selector, chartName) {

  // Set style to ensure correct printing
  $(selector).css("position", "absolute");

  // Save current scroll position to avoid plugin behavior.
  var scrollPosition = document.body.scrollTop;

  html2canvas($(selector), {

    onrendered: function(canvas) {

      var image = canvas.toDataURL("chart/jpg");
      var newWindow = window.open("image", "chart");

      newWindow.document.write("<img style=\"height: 70px; max-width: 500px; border-radius: 5px 5px 5px 5px;\" src=\"" +
                                  $('#logo-left > a > img').attr('src') +
                               "\"/>");

      newWindow.document.write("<div style=\"color: #2F67AD; font-size: 20px; font-weight: bold; " +
                              "font-family: Arial, Helvetica, sans-serif; padding: 20px;\">" +
                                chartName +
                              "</div>");

      newWindow.document.write("<img src='" + image + "'/>");

      // Set scroll to original value, correcting plugin scroll top.
      window.scrollTo(0, scrollPosition);
    }
  });

  $(selector).css("position", "");
}

function loadPlugin(ajaxCallBack, name, selector, template, typeModification, forms) {
	
	var params = {};

    if (forms !== '') {

        $(forms).each(function() {

            $(this).find('input').each(function() {

                $input = $(this);

                if ($input.prop('name') != '' && $input.val() != '') {
                    params[$input.prop('name')] = $input.val();
                }
            });
        });
    }

    params.operationPlugin = name;
    params.pluginAction = 'plugin-html';
    params.template = template;
	
	ajaxCallBack.call(params, function(data) {
		
		if (typeModification === 'HTML') {
			
			$(selector).html(data);
		}
		else if (typeModification === 'PREPEND') {
			
			$(selector).prepend(data);
		}
		else if (typeModification === 'APPEND') {
			
			$(selector).append(data);
		}
	},'html');
}

