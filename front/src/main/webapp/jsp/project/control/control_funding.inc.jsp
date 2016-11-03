<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>


<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
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
  ~ File: control_funding.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:00
  --%>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:message key="data_not_found" var="dataNotFound" />

<script type="text/javascript">
<!--
var incomesTable;
var fundingChart;

function showFundingChart() {
	
	var params = {
		accion: "<%=ProjectControlServlet.JX_FINANCE_CHART%>",
		id: $("#id").val()
	};
		
	controlAjax.call(params, function (data) {
		
		var optionsObj = {
			labelX: '<fmt:message key="days"/>',
			labelY: '<fmt:message key="values"/>',
			enableCursor: false,
			seriesColors: ["rgba(75, 178, 197, 1)","rgba(255, 178, 75, 1)"],
			showPointLabels: false,
			animate: !$.jqplot.use_excanvas
		};
		
		var dataSets = [];
		if (typeof data.plannedValues !== 'undefined') { dataSets.push(data.plannedValues); }
		if (typeof data.actualValues !== 'undefined') { dataSets.push(data.actualValues); }
		
		
		if (typeof fundingChart === 'undefined') { 
			fundingChart = drawLineChart('chartFunding', dataSets, optionsObj, '${dataNotFound}');
			
			if(typeof fundingChart === 'undefined'){
				$("#legendChartFunding").html('');
			}
			else {
				createLegend('#legendChartFunding','<fmt:message key="cost.planned"/>','#4BB2C5');
				createLegend('#legendChartFunding','<fmt:message key="cost.actual"/>','#FFB24B');
			}
		}
		else {
			if (fundingChart != null) { fundingChart.destroy(); }
			fundingChart = drawLineChart('chartFunding', dataSets, optionsObj, '${dataNotFound}');
		}
	});
}

readyMethods.add(function() {
	
	incomesTable = $('#tb_incomes').dataTable({
		"oLanguage": datatable_language,
		"bFilter": false,
		"bInfo": false,
		"bPaginate": true,
		"sPaginationType": "full_numbers",
		"aaSorting": [[ 1, "asc" ]],
		"aoColumns": [
              { "bVisible": false }, 
              { "sClass": "center", "sType": "es_date" },
              { "sClass": "right"},
              { "sClass": "center", "sType": "es_date" },
              { "sClass": "center"},
              { "sClass": "right"},
              { "sClass": "left"}, 
              { "sClass": "center", "bSortable": false }
      		]
	});
	$('#tb_incomes tbody tr').live('click', function (event) {		
		incomesTable.fnSetSelectable(this,'selected_internal');
	});
});
//-->
</script>

<fmt:message key="funding.control" var="titleFundingControl"/>
<visual:panel id="fieldControlFunding" title="${titleFundingControl }" cssClass="panel2">
	<table id="tb_incomes" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th>
				<th width="10%"><fmt:message key="funding.due_date"/></th>
				<th width="10%"><fmt:message key="funding.due_value"/></th>
				<th width="10%"><fmt:message key="funding.actual_date"/></th>
				<th width="10%"><fmt:message key="funding.days_date"/></th>
				<th width="10%"><fmt:message key="funding.actual_value"/></th>
				<th width="47%"><fmt:message key="income.description"/></th>
				<th width="5%">&nbsp;</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="income" items="${project.incomeses}">
				<tr>
					<td>${income.idIncome}</td>
					<td><fmt:formatDate value="${income.plannedBillDate}" pattern="${datePattern}"/></td>
					<td>${tl:toCurrency(income.plannedBillAmmount)}</td>
					<td><fmt:formatDate value="${income.actualBillDate}" pattern="${datePattern}"/></td>
					<td>${income.actDaysToDate}</td>
					<td>${tl:toCurrency(income.actualBillAmmount)}</td>
					<td>${tl:escape(income.actualDescription)}</td>
					<td>
						<c:if test="${op:hasPermission(user,project.status,tab)}">
							<img onclick="editIncome(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<fmt:message key="funding.chart" var="titleFundingChart"/>
	<visual:panel id="fieldChartFunding" title="${titleFundingChart }" cssClass="panel3" callback="showFundingChart">
		<div style="padding:10px;">
			<a href="javascript:showFundingChart()" class="boton"><fmt:message key="chart.refresh" /></a>
		</div>
		<div id="chartFunding" style="margin: 20px auto;"></div>
		<div id="legendChartFunding" class="legendChart"></div>
	</visual:panel>
</visual:panel>

