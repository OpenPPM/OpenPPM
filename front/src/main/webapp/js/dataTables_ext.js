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
 * File: dataTables_ext.js
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
* Proyecto :OpenPPM
* Autor : Javier Hernandez
* Fecha : Jueves, 28 de Julio, 2011
*
* -------------------------------------------------------------------------
*/

$.fn.dataTableExt.oApi.fnFindData = function (oSettings, value, iCol) {
	
	var table = this;
	var i = iCol || 0;
	var rowReturn = null;
	
	$(oSettings.aoData).each(function (){
		
		var row = table.fnGetData( this.nTr);
		if (row[i] == value) { rowReturn = row; return row; }
	});
	return rowReturn;
};

$.fn.dataTableExt.oApi.fnGetSelectedCol = function (oSettings, iCol) {
	var i = iCol || 0;
	var aData = this.fnGetSelectedData();
	if (aData != null) {
		return aData[i];
	}
	return null;
};

$.fn.dataTableExt.oApi.fnGetSelectedsCol = function (oSettings, iCol) {
	var i = iCol || 0;
	var aData = this.fnGetSelectedsData();
	if (aData != null) {
		var columns = '';
		$(aData).each(function (){
			
			columns += (columns == ''?'':',')+this[i];
		});
		return columns;
	}
	return null;
};

$.fn.dataTableExt.oApi.fnGetSelected = function (oSettings) {
	var aReturn = new Array();
	$(oSettings.aoData).each(function (){
		if ($(this.nTr).hasClass('row_selected') || $(this.nTr).hasClass('DTTT_selected') || $(this.nTr).hasClass('selected_internal')) {
			aReturn.push( this.nTr );
		}
	});
	return aReturn;
};

$.fn.dataTableExt.oApi.fnGetSelectedPos = function (oSettings) {
	var selected = this.fnGetSelected();
	if (selected.length > 0) {
		
		return this.fnGetPosition(selected[0]);
	}
	else {
		return null;
	}
};

$.fn.dataTableExt.oApi.fnGetSelectedsData = function () {
	var aReturn = new Array();
	var table = this;
	var anSelected = this.fnGetSelected();
	if (anSelected.length > 0) {
		
		$(anSelected).each(function (){
			
			var aPos = table.fnGetPosition( this );
			aReturn.push(table.fnGetData( aPos ));
		});
		
		return aReturn;
	}
	else {
		return null;
	}
};

$.fn.dataTableExt.oApi.fnGetSelectedData = function () {
	var anSelected = this.fnGetSelected();
	if (anSelected.length > 0) {
		var aPos = this.fnGetPosition( anSelected[0] );
		var aData = this.fnGetData( aPos );
		
		
		
		return aData;
	}
	else {
		return null;
	}
};
$.fn.dataTableExt.oApi.fnRemoveSelected = function (oSettings) {
	
	$(oSettings.aoData).each(function (){
		$(this.nTr).removeClass('row_selected').removeClass('DTTT_selected');
	});
};

$.fn.dataTableExt.oApi.fnSetSelectable = function (oSettings,tr, className) {
	
	var classSelected = (typeof className === 'undefined'?'row_selected':className); 
	
	if (!$(tr).hasClass('dataTables_empty')) {
		var selected = !$(tr).hasClass(classSelected);
			
		$(oSettings.aoData).each(function (){
			if (tr != this.nTr) { $(this.nTr).removeClass(classSelected); }
		});

		if (selected) {
			$(tr).addClass(classSelected);
		}
	}
};

/**
 * Select all rows
 *
 * @param oSettings
 */
$.fn.dataTableExt.oApi.fnSetSelectableAll = function (oSettings) {
  TableTools.fnGetInstance(oSettings.sTableId).fnSelectAll("true");
};

/**
 * Deselect all rows
 *
 * @param oSettings
 */
$.fn.dataTableExt.oApi.fnSetSelectableNone = function (oSettings) {
  TableTools.fnGetInstance(oSettings.sTableId).fnSelectNone("true");
};

$.fn.dataTableExt.oApi.fnAddDataAndSelect = function (oSettings,data) {
	
	var column = this.fnAddData(data);
	var tr = oSettings.aoData[column].nTr;
	this.fnSetSelectable(tr,'selected_internal');
	this.fnPageChange('last');
};

$.fn.dataTableExt.oApi.fnDisplayStart = function ( oSettings, iStart, bRedraw )
{
	if ( typeof bRedraw == 'undefined' )
	{
		bRedraw = true;
	}
	
	oSettings._iDisplayStart = iStart;
	oSettings.oApi._fnCalculateEnd( oSettings );
	
	if ( bRedraw )
	{
		oSettings.oApi._fnDraw( oSettings );
	}
};

$.fn.dataTableExt.oApi.fnDeleteSelected = function () {
	
	var row = this.fnGetSelectedPos();
	if (row != null) {
		
		this.fnDeleteRow( row, null, true );
	}
};


$.fn.dataTableExt.oApi.fnUpdateAndSelect = function (oSettings,data,row,int) {
	
	var iStart = oSettings._iDisplayStart;
	var rowTemp = (row > 0?row:this.fnGetSelectedPos());
	var intTemp = (int > 0?int:0);
	
	this.fnUpdate(data,rowTemp,intTemp);
	
	var tr = oSettings.aoData[rowTemp].nTr;
	this.fnSetSelectable(tr,'selected_internal');
	this.fnDisplayStart(iStart);
};

$.fn.dataTableExt.oApi.fnUpdateRow = function (oSettings,data,row,int) {
	
	var rowTemp = (row > 0?row:this.fnGetSelectedPos());
	var intTemp = (int > 0?int:0);
	
	this.fnUpdate(data,rowTemp,intTemp);
};

$.extend( $.fn.dataTableExt.oSort, {
	 
    "currency-asc": function ( aP, bP ) {
    	
    	var a = parseFloat(toNumber(aP));
    	var b = parseFloat(toNumber(bP));
        return ((a < b) ? -1 : ((a > b) ? 1 : 0));
    },
    "currency-desc": function ( aP, bP ) {
    	
    	var a = parseFloat(toNumber(aP));
    	var b = parseFloat(toNumber(bP));
        return ((a < b) ? 1 : ((a > b) ? -1 : 0));
    }
});

$.fn.dataTableExt.afnSortData['orderInputNumber'] = function  ( oSettings, iColumn )
{
	
	var cont = 0;
	for (var i = 0; i < iColumn; i++) {
		
		if (oSettings.aoColumns[i].bVisible == false) {
			cont += 1;
		}
	}
	
	return $.map( oSettings.oApi._fnGetTrNodes(oSettings), function (tr, i) {
		return $('td:eq('+(iColumn-cont)+') input', tr).val();
	} );
};


/* Create an array with the values of all the span boxes in a column */
$.fn.dataTableExt.afnSortData['dom-span'] = function  ( oSettings, iColumn )
{
	var aData 	= [];
	var cont 	= 0;
	
	for (var i = 0; i < iColumn; i++) {
		
		if (oSettings.aoColumns[i].bVisible == false) {
			cont += 1;
		}
	}
	
	$('td:eq('+(iColumn-cont)+') span', oSettings.oApi._fnGetTrNodes(oSettings) ).each( function () {
		aData.push( $(this).html() == "" ? "-99999999" : $(this).html());
	});
	
	return aData;
};

/*
 * Save state por data tables in BD
 */

$.fn.dataTableExt.oApi.fnAddEventsChange = function (oSettings, configurationTable, configurationProperties) {

	var table = this;

	if (configurationTable != null) {

		// Apply search
		if (typeof configurationTable.search !==  'undefined' && configurationTable.search !== '') {

			table.fnFilter(configurationTable.search);
		}

		// Apply order
		if (typeof configurationTable.order !==  'undefined' && configurationTable.order !== '') {

			table.fnSort( [ [configurationTable.order.column,configurationTable.order.type] ] );
		}
	}

	$('#'+oSettings.sTableId+' th').on('click',function() {

		table.fnSaveChanges(configurationProperties);
	});

	$('#'+oSettings.sTableId+'_length select').on('change',function() {

		table.fnSaveChanges(configurationProperties);
	});

	$('#'+oSettings.sTableId+'_filter input').on('change',function() {

		table.fnSaveChanges(configurationProperties);
	});
};

$.fn.dataTableExt.oApi.fnSaveChanges = function (oSettings, configuration) {

	var value = {
		order : {
			column: employeesTable.fnSettings().aaSorting[0][0],
			type: employeesTable.fnSettings().aaSorting[0][1]
		},
		numberRecords: oSettings._iDisplayLength,
		search: oSettings.oPreviousSearch.sSearch
	};

	var params = {
		accion: 'JX_SAVE_CONFIGURATION',
		type: configuration.type,
		name: configuration.name,
		value: $.stringify(value)
	};

	$.ajax({
		type: "POST",
		url: "util",
		data: params,
 		global: false
	});
};


