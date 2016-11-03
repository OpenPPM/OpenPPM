<%@page import="es.sm2.openppm.utils.functions.ValidateUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectkpi"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="Openppm" prefix="op" %>

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
  ~ File: control_kpi.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:message key="data_not_found" var="dataNotFound" />
<fmt:message key="insufficient_values" var="insufficientValues" />

<script language="javascript" type="text/javascript" >
<!--
var kpiTable;
var kpiChart;

function drawKpiChart() {
	
	var params = {
		accion: "<%=ProjectControlServlet.JX_KPI_CHART %>",
		id: $("#id").val()
	};
	
	controlAjax.call(params, function(data) {
		
		if(!data.insufficientDates){
			
			//JQPlot parameters kpi chart
			var dataSeries = data.kpisValues;
			var optionsObj = {
				title:	'<fmt:message key="chart.kpi"/>',
				enableCursor: true,
				axes: {
					xaxis:{
						label: '<fmt:message key="dates"/>',
						renderer:$.jqplot.DateAxisRenderer,
						tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
						tickOptions:{
							formatString:"%d/%m/%Y",
							angle: -45
						},
						min: data.minDate,
						max: data.maxDate,
						tickInterval: data.tickInterval
					},
					yaxis:{
						label: '<fmt:message key="values"/>',
						labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
						labelOptions: {
							angle: -90
						},
						tickOptions:{
							formatString: '%.2f'
						},
						min: 0,
						pad: 1.2
					}
				},
				seriesColors: data.kpisColors,
				formatString: '%s'
			};
			
			if(typeof kpiChart === 'undefined'){
				$("#kpiChart").empty();
				kpiChart = drawLineChart('kpiChart', dataSeries, optionsObj, '${dataNotFound}');
			}
			else {
				$("#kpiChart").empty();
				//kpiChart.destroy();
				kpiChart = drawLineChart('kpiChart', dataSeries, optionsObj, '${dataNotFound}');
			}
			
			
			if(typeof kpiChart === 'undefined'){
				$("#legendChartKpi").html('');
			}
			else {
				$('#legendChartKpi').empty();
				
				var i;
				for(i=0;i<data.kpisNames.length;i++){
					createLegend('#legendChartKpi',data.kpisNames[i],data.kpisColors[i]);
				}
			}
			
		}
		else {
			$('#legendChartKpi').empty();
			$("#kpiChart")
			.attr( 'style', 'margin: 20px auto;text-align:center')
			.html('${insufficientValues}');
		}
	});
}

readyMethods.add(function () {

	kpiTable = $('#tb_kpi').dataTable({
		"sPaginationType": "full_numbers",
		"iDisplayLength": 50,
        "aaSorting": [[ 3, "asc" ]],
		"oLanguage": datatable_language,
		"aoColumns": [ 
             { "bVisible": false }, 
             { "bVisible": false },
             { "sClass": "center", "bSortable": false }, 
             { "sClass": "left" },
             { "sClass": "left" },
             { "sClass": "right" }, 
             { "sClass": "right" }, 
             { "sClass": "right" }, 
             { "sClass": "right" },
             { "sClass": "right" },
             { "sClass": "right" },
             { "sClass": "center", "bSortable": false }
    	]
	});

	$('#tb_kpi tbody tr').live('click', function (event) {
		kpiTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('#kpiToCSV').on('click',function(event) {
		event.stopPropagation();
		var f = document.frm_project;
		f.accion.value = "<%=ProjectControlServlet.EXPORT_KPI_CSV %>";
		f.submit();
	});	
	
	$('#botonResetZoomKpiChart').click(function() {
		if (typeof kpiChart !== 'undefined') { kpiChart.resetZoom(); }
	});
	
	$('#kpisChartReload').on('click', function(e){
		e.stopPropagation();
		drawKpiChart();
	});
	
	$('#kpisChartZoomInfo').on('click', function(e){
		e.stopPropagation();
	});
	
});
//-->
</script>

<fmt:message key="kpi.name" var="titleKpiName"/>
<c:set var="btnKpi"><img id="kpiToCSV" src="images/csv.png" title="${msg_to_excel}"></c:set>
<visual:panel id="fieldControlKpi" title="${titleKpiName}" buttons="${btnKpi}">
	<table id="tb_kpi" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th>
				<th width="0%">&nbsp;</th>
				<th width="4%"><fmt:message key="rag" /></th>
				<th width="20%"><fmt:message key="kpi.metric" /></th>
				<th width="29%"><fmt:message key="kpi.definition" /></th>
				<th width="6%"><fmt:message key="kpi.upper_threshold" /></th>
				<th width="8%"><fmt:message key="kpi.lower_threshold" /></th>
				<th width="8%"><fmt:message key="kpi.value" /></th>
				<th width="8%"><fmt:message key="kpi.normalized_value" />&nbsp;%</th>
				<th width="8%"><fmt:message key="kpi.weight" />&nbsp;%</th>
				<th width="5%"><fmt:message key="kpi.score" />&nbsp;%</th>
				<th width="4%">&nbsp;</th>
			</tr>
		</thead>
		<tbody>
			<c:set var="totalWeight" value="${0}"/>
			<c:set var="totalScore" value="${0}"/>
			<c:set var="showTotal" value="true"/>
			<c:forEach var="kpi" items="${project.projectkpis}">
				<c:set var="riskColor"></c:set>
				<c:set var="totalWeight" value="${kpi.weight + totalWeight}"/>
				<c:set var="totalScore" value="${kpi.score + totalScore}"/>
				<c:set var="adjustedValue" value="${kpi.adjustedValue}"/>
				<c:if test="${empty kpi.value}">
					<c:set var="showTotal" value="false"/>
				</c:if>
				<tr>
					<td>${kpi.idProjectKpi}</td>
					<td>${kpi.metrickpi.idMetricKpi}</td>
					<td>
						<c:if test="${!empty kpi.value}">
							<c:choose>
								<c:when test="${kpi.adjustedValue == 100}">
									<c:set var="riskColor">risk_low</c:set>
								</c:when>
								<c:when test="${kpi.adjustedValue == 0 && kpi.lowerThreshold != kpi.value}">
									<c:set var="riskColor">risk_high</c:set>
								</c:when>
								<c:otherwise>
							 		<c:set var="riskColor">risk_medium</c:set>
							 	</c:otherwise>			
							</c:choose>
						</c:if>
						<span class="${riskColor}">&nbsp;&nbsp;&nbsp;</span>
					</td>
					<td>
						<c:choose>
							<c:when test="${kpi.metrickpi.idMetricKpi == null}">
								${tl:escape(kpi.specificKpi)}
							</c:when>
							<c:otherwise>
								${tl:escape(kpi.metrickpi.name)}
							</c:otherwise>
						</c:choose>
					</td>
					<td>${tl:escape(kpi.metrickpi.definition)}</td>
					<td>${tl:toCurrency(kpi.upperThreshold)}</td>
					<td>${tl:toCurrency(kpi.lowerThreshold)}</td>
					<td>${tl:toCurrency(kpi.value)}</td>
					<td>${tl:toCurrency(kpi.adjustedValue)}</td>
					<td>${tl:toCurrency(kpi.weight)}</td>
					<td>${tl:toCurrency(kpi.score)}</td>
					<td>
						<c:if test="${op:hasPermission(user,project.status,tab)}">
							<img onclick="editKpi(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
		<c:if test="${fn:length(project.projectkpis) > 0}">
			<tfoot>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>
						<div class="center" id="projectColorRAG">
							<span class="${project.kpiStatus}" style="border: 1px solid #BDBDBD; border-radius: 5px;">&nbsp;&nbsp;&nbsp;</span>
						</div>
					</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td><div class="right" id="projectUpperThreshold">${tl:toCurrency(project.upperThreshold)}</div></td>
					<td><div class="right" id="projectLowerThreshold">${tl:toCurrency(project.lowerThreshold)}</div></td>
					<td>
						<div class="right" id="totalValue">
							<c:if test="${showTotal == true}">
								${tl:toCurrency(totalScore)}
							</c:if>
						</div>
					</td>
					<td><div class="right" id="totalNormalizedValue"></div></td>
					<td><div class="right">${tl:toCurrency(totalWeight)}</div></td>
					<td><div class="right" id="totalScore">${tl:toCurrency(totalScore)}</div></td>
					<td>&nbsp;</td>
				</tr>
			</tfoot>
		</c:if>
	</table>
	
	<fmt:message key="chart.kpi" var="kpiChart"/>
	<c:set var="kpisChartBTN">
		<img id="kpisChartReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
		<img class="btitle" id="kpisChartZoomInfo" src="images/info.png" title="<fmt:message key="chart.reset_zoom_double_click.info"/>">
	</c:set>
	<visual:panel id="kpiChartPanel" title="${kpiChart}" cssClass="panel3" callback="drawKpiChart" buttons="${kpisChartBTN}">
		<div id="kpiChart" style="margin: 20px auto;"></div>
		<div id="legendChartKpi" class="legendChart"></div>
	</visual:panel>
	
</visual:panel>