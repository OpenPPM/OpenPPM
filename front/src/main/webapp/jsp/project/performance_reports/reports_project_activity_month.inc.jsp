<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
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
  ~ File: reports_project_activity_month.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:03
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="msg.error.export_to_csv" var="error_to_csv" />
<fmt:message key="msg.no_export_to_csv" var="msg_no_export_to_csv">
	<fmt:param><fmt:message key="projects"/></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var projectActivityMonth = {
	show: function() {
		if (typeof dataInfoTable === 'undefined') { loadActivityMonthCR = true; }
		else {
			var params = {
				accion: "<%=ProjectServlet.JX_REPORT_PROJECT_ACTIVITY_MONTH%>",
				ids: dataInfoTable.ids,
				since: $('#runningSince').val(),
				until: $('#runningUntil').val(),
				showAll: $('#showAll').is(':checked'),
				idResourcePool:  $('#idResourcePool').val()
			};
			
			projectAjax.call(params, function(data) {
				$('#activityMonthTable').html(data);	
			},'html');
		}
	},
	toCSV: function() {
		
		var f = document.forms.frmPerformanceReports;
		
		f.showAll.value = f.showAll.checked;
		f.ids.value = dataInfoTable.ids;
		f.accion.value = "<%=ProjectServlet.REPORT_PROJECT_ACTIVITY_MONTH_CSV%>";
		if(f.ids.value == "") { alertUI("${error_to_csv}", "${msg_no_export_to_csv}"); }
		else { f.submit(); }
	}
};


readyMethods.add(function() {

	$('#activityMonthCRToCSV').on('click',function(e) {
		e.stopPropagation();
		projectActivityMonth.toCSV();	
	});
	
	$('#activityMonthCRReload').on('click', function(e){
		e.stopPropagation();
		projectActivityMonth.show();
	});
});
//-->
</script>

<fmt:message key="performance.reports.project_activity_month" var="fmtActivityMonthCR"/>
<c:set var="activityMonthBTN">
	<img id="activityMonthCRReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
	<img id="activityMonthCRToCSV" src="images/csv.png" title="<fmt:message key="msg_to_csv"/>">
</c:set>
<visual:panel id="activityMonthCR" title="${fmtActivityMonthCR }" callback="projectActivityMonth.show" buttons="${activityMonthBTN }" cssClass="panel2">
	<div id="activityMonthTable"></div>
</visual:panel>