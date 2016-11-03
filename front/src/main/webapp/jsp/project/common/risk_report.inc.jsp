<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%--
  ~ Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
  ~ This program is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ This program has been created in the hope that it will be useful.
  ~ It is distributed WITHOUT ANY WARRANTY of any Kind,
  ~ without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  ~ See the GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program. If not, see http://www.gnu.org/licenses/.
  ~
  ~ For more information, please contact SM2 Software & Services Management.
  ~ Mail: info@talaia-openppm.com
  ~ Web: http://www.talaia-openppm.com
  ~
  ~ Module: front
  ~ File: risk_report.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:56
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="msg_to_csv" var="msg_to_csv" />
<fmt:message key="msg.error.export_to_csv" var="error_to_csv" />
<fmt:message key="msg.no_export_to_csv" var="msg_no_export_to_csv">
	<fmt:param><fmt:message key="projects"/></fmt:param>
</fmt:message>

<%-- Print messages --%>
<fmt:message key="print" var="print" />
<fmt:message key="print_message" var="printMessage" />
<fmt:message key="print_title" var="printTitle" />

<script type="text/javascript">
<!--

var riskTable;

// Load risk data
function infoRiskReport() {

	if (typeof dataInfoTable === 'undefined') { loadRiskReport = true; }
	else {
		
	 	applyInfoSort();
		
		var params = {
			accion: "<%=ProgramServlet.JX_RISK_REPORT%>",
			ids: dataInfoTable.ids
		};
		
		programAjax.call(params,function(data) {
			
			riskTable.fnClearTable();
			
			$(data).each(function() {
				
				var dataRow = [
					this.projectName,
					this.projectID,
					this.riskCode,
					this.riskName,
					this.riskRaisedDate,
					this.riskDueDate,
					this.riskStatus,
					this.riskRating,
					this.riskDescription,
					this.buttons
				];
				
				riskTable.fnAddData(dataRow);
			});
			
		});
	}
}


readyMethods.add(function () {

	// Event reload
	$('#riskReportReload').on('click',function(event) {
		event.stopPropagation();
		infoRiskReport();	
	}); 
	
	// Initialization table
	riskTable = $('#tb_risks').dataTable({
		"oLanguage": datatable_language,
		"bFilter": false,
		"bInfo": true,
		"bPaginate": true,
		"iDisplayLength": 50,
		"bAutoWidth": false,
		"sPaginationType": "full_numbers",
		"aaSorting": [[ 3, "desc" ],[ 1, "asc" ]],
		"aoColumns": [
             { "bVisible": false }, <%-- Project name --%>
             { "bVisible": false }, <%-- Project id --%>
             { "sClass": "center" },
         	 { "sClass": "left" },
             { "sClass": "center", "sType": "es_date" },		              
             { "sClass": "center", "sType": "es_date" },
             { "sClass": "center"  },
             { "sSortDataType": "riskRating", "sType": "numeric" },
             { "sClass": "center"  },
             { "sClass": "center", "bSortable": false }
          	],
         	"fnDrawCallback": function ( oSettings ) {
                if ( oSettings.aiDisplay.length == 0 ) {return;}
                var nTrs = $('#tb_risks tbody tr');
                var iColspan = nTrs[0].getElementsByTagName('td').length;
                var sLastGroup = "";
                for ( var i=0 ; i<nTrs.length ; i++ ) {
                    var iDisplayIndex = oSettings._iDisplayStart + i;
                    var sGroup = oSettings.aoData[ oSettings.aiDisplay[iDisplayIndex] ]._aData[0];
                    if ( sGroup != sLastGroup ) {
                        var nGroup = document.createElement( 'tr' );
                        var nCell = document.createElement( 'td' );
                        nCell.colSpan = iColspan;
                        nCell.className = "groupRow";
                        nCell.innerHTML = sGroup;
                        nGroup.appendChild( nCell );
                        nTrs[i].parentNode.insertBefore( nGroup, nTrs[i] );
                        sLastGroup = sGroup;
                    }
                }
            },
            "aoColumnDefs": [
    			{ "bVisible": false, "aTargets": [ 0 ] }
    		],
    		"aaSortingFixed": [[ 0, 'asc' ]],
    		"aaSorting": [[ 2, 'asc' ]],
         	"sDom": 'T<"clear">lfrtip',
            "oTableTools": {
    			"aButtons": [  {
                    "sExtends": "print",
                    "sButtonText": "${print}",
                    "sInfo": "<div class='print'>${printMessage}</div>"
                	}
    			]
    		}
   	});
	
	// Order risk rating
	$.fn.dataTableExt.afnSortData['riskRating'] = function  ( oSettings, iColumn )
	{
		var aData 	= [];
		var cont 	= 0;
		
		for (var i = 0; i < iColumn; i++) {
			
			if (oSettings.aoColumns[i].bVisible == false) {
				cont += 1;
			}
		}
		
		$( 'td:eq('+(iColumn-cont)+') .riskValue', oSettings.oApi._fnGetTrNodes(oSettings) ).each( function () {
			aData.push( $(this).html() );
		} );

		return aData;
	};
});
//-->
</script>

<fmt:message key="risk_report" var="titleRiskReport"/>
<c:set var="btnRiskReport">
	<img id="riskReportReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
</c:set>
<visual:panel id="fieldProgRiskReport" title="${titleRiskReport }" buttons="${btnRiskReport }" callback="infoRiskReport">
	<div id="headerReportButtons" class="right" style="padding-bottom:10px;"></div>
	<table id="tb_risks" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th> <%-- Project name --%>
				<th width="0%">&nbsp;</th> <%-- Project id --%>
				<th width="9%"><fmt:message key="risk.code"/></th>
				<th width="20%"><fmt:message key="risk.name"/></th>
				<th width="10%"><fmt:message key="risk.date_raised"/></th>					
				<th width="10%"><fmt:message key="risk.due_date"/></th>
				<th width="10%"><fmt:message key="risk.status"/></th>
				<th width="10%"><fmt:message key="risk.rating"/></th>
				<th width="11%"><fmt:message key="risk.response"/></th>
				<th width="5%">
				
				</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>	
</visual:panel>