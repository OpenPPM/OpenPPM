<%@page import="es.sm2.openppm.core.common.Configurations"%>
<%@page import="java.util.Date"%>
<%@page import="es.sm2.openppm.utils.DateUtil"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
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
  ~ File: control_resource.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="data_not_found" var="dataNotFound" />

<script language="javascript" type="text/javascript" >
<!--
var staffingTable;
var histogramChart;

function chartHistogram(DOMId,visible) {

	var params = {
		accion: "<%=ProjectControlServlet.JX_HISTOGRAM_CHART%>",
		idProject: '${project.idProject}',
		since: $('#histogramStart').val(),
		until: $('#histogramFinish').val()
	};
	
	controlAjax.call(params, function(data) {

		//legend and colors
		$('#legendHistogram').empty();
		var seriesColors = new Array();
		$(data.legend).each(function() {
			createLegend('#legendHistogram',this.name,this.color);
			seriesColors.push(this.color);
		});
		
		//JQPlot parameters 
		var dataSeries = [data.dataSeries];
		var optionsObj = {
			ticks: data.categories,
			seriesColors: seriesColors,
			enableCursor:true,
			formatString:'%.2f',
			labelY:'<fmt:message key="chart.percentage"/>'
		};
		
		if (typeof histogramChart === 'undefined') { 
			histogramChart = drawBarChart('chartHistogram', data.dataSeries, optionsObj, '${dataNotFound}');
		}
		else { 
			histogramChart.destroy();
			histogramChart = drawBarChart('chartHistogram', data.dataSeries, optionsObj, '${dataNotFound}');
		}
	});
}

function updateFteTable() {
	
	var params = {
		accion:"<%=ProjectControlServlet.JX_UPDATE_STAFFING_TABLE%>",
		idProject:'${project.idProject}',
		since: $("#resourcesStartCapacityRunning").val(),
		until: $("#resourcesFinishCapacityRunning").val(),
		SHOW_OPERATIONS: $('#<%= Configurations.SHOW_OPERATIONS %>').prop('checked')
	};
	
	controlAjax.call(params, function(data) {
		$('#staffingTableCapacityRunning').html(data);
	},'html');
}

readyMethods.add(function () {

	var histogramDates = $('#histogramStart, #histogramFinish').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "histogramStart" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			histogramDates.not(this).datepicker("option", option, date);
		}
	});
	
	var dates = $('#resourcesStartCapacityRunning, #resourcesFinishCapacityRunning').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "resourcesStartCapacityRunning" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			dates.not(this).datepicker("option", option, date);
		}
	});

	$('#histogramChartReload').on('click', function(e){
		e.stopPropagation();
		chartHistogram();
	});
	
	$('#histogramChartZoomInfo').on('click', function(e){
		e.stopPropagation();
	});
	
});
//-->
</script>

<%--  CHART HISTOGRAM --%>
<fmt:message key="project.resource_histogram" var="titleResourceHistogram"/>
<c:set var="histogramChartBTN">
	<img id="histogramChartReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
	<img class="btitle" id="histogramChartZoomInfo" src="images/info.png" title="<fmt:message key="chart.reset_zoom_double_click.info"/>">
</c:set>
<visual:panel id="fieldControlHistogram" title="${titleResourceHistogram}" cssClass="panel2" callback="chartHistogram" buttons="${histogramChartBTN}">
	<div>
   		<span style="margin-right:5px;">
			<fmt:message key="dates.since"/>:&nbsp;
			<input type="text" id="histogramStart" class="campo fecha alwaysEditable" value="${valActInitDate}"/>
   		</span>
       	<span style="margin-right:5px;">
			<fmt:message key="dates.until"/>:&nbsp;
			<input type="text" id="histogramFinish" class="campo fecha alwaysEditable"
			value="<fmt:formatDate pattern="${datePattern}" value="<%=DateUtil.getLastWeekDay(new Date())%>"/>"/>
		</span>
		<a href="javascript:chartHistogram();" class="boton"><fmt:message key="project.chart" /></a>
	</div>
	<div id="chartHistogram" style="margin: 20px auto;text-align:center"></div>
	<div id="legendHistogram" class="legendChart"></div>
</visual:panel>

<%-- PROJECT STAFFING TABLE --%>
<fmt:message key="menu.resource_capacity_running" var="titleProjectStaffing"/>
<visual:panel id="fieldControlStaffing" title="${titleProjectStaffing }" cssClass="panel2" >
	<div>
   		<span style="margin-right:5px;">
			<fmt:message key="dates.since"/>:&nbsp;
			<input type="text" id="resourcesStartCapacityRunning" class="campo fecha alwaysEditable" value="${valActInitDate}"/>
    		</span>
       	<span style="margin-right:5px;">
			<fmt:message key="dates.until"/>:&nbsp;
			<input type="text" id="resourcesFinishCapacityRunning" class="campo fecha alwaysEditable"
			value="<fmt:formatDate pattern="${datePattern}" value="<%=DateUtil.getLastWeekDay(new Date())%>"/>"/>
		</span>
		<span style="margin-right:5px;">
			<fmt:message key="capacity_running.showOperations"/>:&nbsp;
			<input type="checkbox" style="width:20px;" id="<%= Configurations.SHOW_OPERATIONS %>" name="<%= Configurations.SHOW_OPERATIONS %>">
		</span>
		<a href="javascript:updateFteTable();" class="boton"><fmt:message key="staff.refresh" /></a>
	</div>
	<div id="staffingTableCapacityRunning"></div>
</visual:panel>
