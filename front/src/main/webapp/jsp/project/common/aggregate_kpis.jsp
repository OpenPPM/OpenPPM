<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@page import="es.sm2.openppm.core.javabean.ProjectScore"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="Visual" prefix="visual" %>

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
  ~ File: aggregate_kpis.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>


<script type="text/javascript">
<!--

var projectKpi = {
	// Calculation	
	calculation: function(target, threshold, value) {
		
		var calculation = null;
		
		if (target != null && threshold != null && value != null && (target - threshold) != 0.0) {
			calculation = ((value-threshold)/(target - threshold)) == -0.0 ? 0.0 : ((value-threshold)/(target - threshold));
		}
		
		return calculation;
	},
	// Calculated adjusted value
	adjustedValue: function(calculation) {
		
		var adjustedValue = null;			
    		
		if (calculation != null) {
			
	   		if (calculation < 0.0) {
	   			adjustedValue = 0.0;
	   		}
	   		else if (calculation > 1.0) {
	   			adjustedValue = 100.0;
	   		}
	   		else {
	   			adjustedValue = calculation * 100;
	   		}
		}
		
		return adjustedValue;
	},
	// Risk color
	riskColor: function(adjustedValue, lowerThreshold, value) {
		
		var riskColor = "";
		
		if (adjustedValue != null) {
			
			if (adjustedValue == 100) {
				riskColor = "risk_low";
			}
			else if (adjustedValue == 0 && lowerThreshold != value) {
				riskColor = "risk_high";
			}
			else {
				riskColor = "risk_medium";
			}
		}
		
		return riskColor;
	}
};

function showKpis(){
	
	if (typeof dataInfoTable === 'undefined') { 
		loadKPIs = true; 
	}
	else {
		var params = {
			accion: "<%= ProjectServlet.JX_SHOW_AGGREGATE_KPIS %>",
			ids: dataInfoTable.ids
		};
		
		projectAjax.call(params,function (data) {
			
			var riskColor;
			var calculation;
			var adjValue;
			
			kpiTable.fnClearTable();
			
			$(data).each(function() {
				
				calculation = projectKpi.calculation(this.upperThreshold, this.lowerThreshold, this.value);
				adjValue 	= projectKpi.adjustedValue(calculation);
				riskColor 	= projectKpi.riskColor(adjValue, this.lowerThreshold, this.value);
				
				var dataRow = [
					this.idProjectKpi,
					'<div style="display:none;" class="ragValue">'+ (calculation == null ? -999.0 : calculation) + '</div>' +
					'<div class="riskRating '+ riskColor + '">&nbsp;</div>',
					this.project.projectName,
					this.metrickpi == null? this.specificKpi : this.metrickpi.name,
					toCurrency(this.upperThreshold),
					toCurrency(this.lowerThreshold),
					toCurrency(this.weight),
					toCurrency(this.value)
				];
				
				kpiTable.fnAddData(dataRow);
			});
		});
	}
}

readyMethods.add(function () {

	kpiTable = $('#tb_kpis').dataTable({
		"sPaginationType": "full_numbers",
		"iDisplayLength": 25,
		"oLanguage": datatable_language,
		"bAutoWidth": false,
		"aoColumns": [ 
             { "bVisible": false },
             { "sClass": "left",  "sWidth":  "4%", "sSortDataType": "dom-div", "sType": "numeric"}, 
             { "sClass": "left",  "sWidth": "30%" }, 
             { "sClass": "left",  "sWidth": "26%" },
             { "sClass": "right", "sWidth": "10%" }, 
             { "sClass": "right", "sWidth": "10%" }, 
             { "sClass": "right", "sWidth": "10%" }, 
             { "sClass": "right", "sWidth": "10%" }
    	]
	});
	
	$('#indicatorsReload').on('click', function(e){
		e.stopPropagation();
		showKpis();
	});
	
	/* Create an array with the values of all the div boxes in a column */
	$.fn.dataTableExt.afnSortData['dom-div'] = function  ( oSettings, iColumn ) {
		
		var aData 	= [];
		var cont 	= 0;
		
		for (var i = 0; i < iColumn; i++) {
			
			if (oSettings.aoColumns[i].bVisible == false) {
				cont += 1;
			}
		}
		
		$( 'td:eq('+(iColumn-cont)+') .ragValue', oSettings.oApi._fnGetTrNodes(oSettings) ).each( function () {
			aData.push( $(this).html() );
		} );

		return aData;
	};
});


//-->
</script>

<fmt:message key="kpi.name" var="titleKpi"/>
<c:set var="indicatorsBTN">
	<img id="indicatorsReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
</c:set>
<visual:panel id="panel_kpis" title="${titleKpi}" callback="showKpis" buttons="${indicatorsBTN}" showTiltePanel="false">
	<table id="tb_kpis" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th>&nbsp;</th>
				<th><fmt:message key="rag" /></th>
				<th><fmt:message key="project" /></th>
				<th><fmt:message key="kpi.metric" /></th>
				<th><fmt:message key="kpi.upper_threshold" /></th>
				<th><fmt:message key="kpi.lower_threshold" /></th>
				<th><fmt:message key="kpi.weight" />&nbsp;%</th>
				<th><fmt:message key="kpi.value" /></th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
</visual:panel>