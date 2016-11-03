/**
 * OpenPPM extents for JQuery
 * @author: Juanma Lopez
 */

/*************************************************/
/** Add some functionality to JQuery.dataTables **/

/**
 * Get selected row from dataTable
 * @param dataTable
 * @author juanma.lopez
 */
function fnGetSelected( dataTable )
{
	var aReturn = new Array();
	var aTrs = dataTable.fnGetNodes();
	
	for ( var i=0 ; i<aTrs.length ; i++ )
	{
		if ( $(aTrs[i]).hasClass('row_selected') )
		{
			aReturn.push( aTrs[i] );
		}
	}
	
	return aReturn;
}


function fnSetSelectable(dataTable, tr) {
	if (!$(tr).hasClass('dataTables_empty')) {
		var selected = !$(tr).hasClass('row_selected');
			
		$(dataTable.fnSettings().aoData).each(function (){
			if (this.nTr != tr) { $(this.nTr).removeClass('row_selected'); }
		});

		if (selected) {
			//$(event.target.parentNode).addClass('row_selected');
			$(tr).addClass('row_selected');
		}
	}
}


/**
 * Put ID from column_id into input_id when event is fired
 * @param dataTable: dataTable
 * @param td: TD clicked
 * @param event: event
 * @param colum_id: dataTable column where ID is
 * @param input_id: Hidden input where place ID selected
 * @author juanma.lopez
 */
function fnSetSelectedIDInput(dataTable, td, event, column_id, input_id) {
	
	if (!$(td).hasClass('dataTables_empty')) {
		var selected = !$(event.target.parentNode).hasClass('row_selected');
		
		$(dataTable.fnSettings().aoData).each(function (){
			$(this.nTr).removeClass('row_selected');
		});

		if (selected) {
			// Get the position of the current data from the node
			var aPos = dataTable.fnGetPosition( td.parentNode );
			// Get the data array for this row
			var aData = dataTable.fnGetData( aPos[0] );
			// Put ID into input hidden
			$('#'+input_id).attr('value', aData[column_id]);

			$(event.target.parentNode).addClass('row_selected');
		}
		else $('#'+input_id).attr('value', '');
	}
}

/**
 * Declare a new type for columns: es_date.
 * Order by this type is order by dd/mm/YY ascending
 * @author Andy McMaster
 */
jQuery.fn.dataTableExt.oSort['es_date-asc']  = function(a,b) {
	var x = -1;
	if (a != "") {
		var ukDatea = a.split('/');
		x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
	}
	
	var y = -1;
	if (b != "") {
		var ukDateb = b.split('/');
		y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;
	}
	
	return ((x < y) ? -1 : ((x > y) ?  1 : 0));
};

/**
 * Declare a new type for columns: es_date.
 * Order by this type is order by dd/mm/YY descending
 * @author Andy McMaster
 */
jQuery.fn.dataTableExt.oSort['es_date-desc'] = function(a,b) {
	var x = -1;
	if (a != "") {
		var ukDatea = a.split('/');
		x = (ukDatea[2] + ukDatea[1] + ukDatea[0]) * 1;
	}
	
	var y = -1;
	if (b != "") {
		var ukDateb = b.split('/');
		y = (ukDateb[2] + ukDateb[1] + ukDateb[0]) * 1;
	}
	
	return ((x < y) ? 1 : ((x > y) ?  -1 : 0));
};

/**
 * This plug-in will provide date sorting for the "dd/mm/YYYY hh:ii:ss" formatting
 *
*/
function format_date(european_date) {

    // First trim the date
    trimed_date = european_date.replace(/^\s+/g, '').replace(/\s+$/g, '');

    // Then transform it to an integer
    if (trimed_date != '') {
        var frDatea = trimed_date.split(' ');
        var frTimea = frDatea[1].split(':');
        var frDatea2 = frDatea[0].split('/');

        return (frDatea2[2] + frDatea2[1] + frDatea2[0] + frTimea[0] + frTimea[1] + frTimea[2]) * 1;
    }else return 10000000000000;    // = l'an 1000 ...
}

jQuery.fn.dataTableExt.oSort['date-euro-asc'] = function(x, y) {
    x = format_date(x);
    y = format_date(y);

    return ((x < y) ? -1 : ((x > y) ? 1 : 0));
};

jQuery.fn.dataTableExt.oSort['date-euro-desc'] = function(x, y) {
    x = format_date(x);
    y = format_date(y);

    return ((x < y) ? 1 : ((x > y) ? -1 : 0));
};

/**
 * Declare a new type for columns: percent
 * Order by this type is order by NN% ascending
 */
jQuery.fn.dataTableExt.oSort['percent-asc']  = function(a,b) {
	var x = (a == "-") ? 0 : a.replace( /%/, "" );
	var y = (b == "-") ? 0 : b.replace( /%/, "" );
	x = parseFloat( x );
	y = parseFloat( y );
	return ((x < y) ? -1 : ((x > y) ?  1 : 0));
};

/**
 * Declare a new type for columns: percent
 * Order by this type is order by NN% descending
 */
jQuery.fn.dataTableExt.oSort['percent-desc'] = function(a,b) {
	var x = (a == "-") ? 0 : a.replace( /%/, "" );
	var y = (b == "-") ? 0 : b.replace( /%/, "" );
	x = parseFloat( x );
	y = parseFloat( y );
	return ((x < y) ?  1 : ((x > y) ? -1 : 0));
};



/** Add some functionality to JQuery.dataTables **/
/*************************************************/

/**
 * Ui Launches Alert
 * @param title
 * @param msg
 */
function alertUI(title, msg) {
	$('#dialog-error-msg').html(msg);
	$('#dialog-error').dialog(
			'option', 
			'buttons',
			{
				"Accept": function() { 
					$('#dialog-error').dialog("close"); 
				}
			}
	);
	
	$('#dialog-error').dialog('option', 'title', title);
	$('#dialog-error').dialog('open');
}


/****************** Validations ***********************/
jQuery.validator.addMethod("rangeDouble", function(value, element, param) {
	
	if(  parseFloat(value) < parseFloat(param[0]) || parseFloat(value) > parseFloat(param[1])){
		return false;
	}
	return true;
}, "Out of range ");

jQuery.validator.addMethod("rangeCurrency", function(value, element, param) {

    if (!isDecimal(value)) { value = toNumber(value); }

    var range = param.split(",");

    if(  parseFloat(value) < parseFloat(range[0]) || parseFloat(value) > parseFloat(range[1])){
        return false;
    }
    return true;
}, "Out of range ");

jQuery.validator.addMethod("zeroValue", function(value, element, param) {
	
	if (!isDecimal(value)) { value = toNumber(value); }
	if (!isDecimal(param)) { param = toNumber(param); }
	
	if (parseFloat(param) == 0 && parseFloat(value) != 0) { 
		return false; 
	}
	return true;
}, "Zero Value ");

jQuery.validator.addMethod("maxCurrency", function(value, element, param) {
	
	if (!isDecimal(value)) { value = toNumber(value); }
	if (!isDecimal(param)) { param = toNumber(param); }
	
	if (parseFloat(value) > parseFloat(param)) {  return false; }
	return true;
}, "Max Value ");

jQuery.validator.addMethod("minDateTo", function(value, element, param) {
	if (value != '' && $(param).val() != '' && !dateBefore(value, $(param).val(), 'dd/mm/yyyy')) { 
		return false;
	}
	return true;
}, "La fecha es demasiado grande");

jQuery.validator.addMethod("maxDateTo", function(value, element, param) {
	if (value != '' && $(param).val() != '' && !dateBefore($(param).val(), value, 'dd/mm/yyyy')) { 
		return false;
	}
	return true;
}, "La fecha es demasiado grande");

jQuery.validator.addMethod(
	"date",
		function(value, element) {
			var check = false;
			var re = /^\d{1,2}\/\d{1,2}\/\d{4}$/;
			if( re.test(value)){
				var adata = value.split('/');
				var gg = parseInt(adata[0],10);
				var mm = parseInt(adata[1],10);
				var aaaa = parseInt(adata[2],10);
				var xdata = new Date(aaaa,mm-1,gg);
				if ( ( xdata.getFullYear() == aaaa ) && ( xdata.getMonth () == mm - 1 ) && ( xdata.getDate() == gg ) )
				check = true;
				else
				check = false;
			} else
			check = false;
			return this.optional(element) || check;
		}, 
	"Please enter a correct date"
);

jQuery.validator.addMethod("integer", function(value, element) {
	return this.optional(element) || /^-?\d+$/.test(value);
}, "A positive or negative non-decimal number please");

jQuery.validator.addMethod("integerPositive", function(value, element) {
	return this.optional(element) || /^\d+$/.test(value);
}, "A positive non-decimal number please");

jQuery.validator.addMethod("notEqualTo", function(value, element, param) {
	return (value != $(param).val()); 
}, "No pueden ser iguales");

$.fn.dataTableExt.oApi.fnResetAllFilters = function (oSettings, bDraw/*default true*/) {
    for(iCol = 0; iCol < oSettings.aoPreSearchCols.length; iCol++) {
            oSettings.aoPreSearchCols[ iCol ].sSearch = '';
    }
    oSettings.oPreviousSearch.sSearch = '';

    if(typeof bDraw === 'undefined') bDraw = true;
    if(bDraw) this.fnDraw();
};