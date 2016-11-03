<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
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
  ~ File: reports.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:03
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--

readyMethods.add(function() {
	
	var sinceUntilRunning = $('#runningSince, #runningUntil').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		changeMonth: true,
		numberOfMonths: ${numberOfMonths},
		onSelect: function(selectedDate) {
			var option = this.id == "runningSince" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			sinceUntilRunning.not(this).datepicker("option", option, date);
		}
	});
});
//-->
</script>

<fmt:message key="performance.reports" var="performanceReports"/>
<visual:panel id="performanceReports" title="${performanceReports }">

	<form name="frmPerformanceReports" action="<%=ProjectServlet.REFERENCE %>" method="post">
		<input type="hidden" name="accion" value=""/>
		<input type="hidden" name="ids"/>
		<div style="padding-top:10px; margin-bottom: 25px;">
		
			<div style="float:left;">
		     	<span style="margin-right:5px;">
					<fmt:message key="dates.since"/>:&nbsp;
					<input type="text" name="since" id="runningSince" class="campo fecha" value="<fmt:formatDate value="${since}" pattern="${datePattern}" />"/>
		       	</span>
		   		<span style="margin-right:5px;">
					<fmt:message key="dates.until"/>:&nbsp;
					<input type="text" name="until" id="runningUntil" class="campo fecha" value="<fmt:formatDate value="${until}" pattern="${datePattern}" />"/>
				</span>
				<span style="margin-right:5px;">
					<fmt:message key="maintenance.employee.resource_pool"/>:&nbsp;
					<select style="width:200px" name="idResourcePool" id="idResourcePool" class="campo">
						<option value=""><fmt:message key="select_opt"/></option>
						<c:forEach var="item" items="${resourcepools}">
							<option value="${item.idResourcepool}">${item.name}</option>
						</c:forEach>
					</select>
				</span>
			</div>
			
			<c:choose>
				<c:when test="<%= SecurityUtil.isUserInRole(request, Constants.ROLE_PMO) %>">
					<c:set var="showAll">block;</c:set>
				</c:when>
				<c:otherwise>
					<c:set var="showAll">none;</c:set>
				</c:otherwise>
			</c:choose>
			
			<div style="float: left;display:${showAll}">
				<fmt:message key="performance.show_all"/>:&nbsp;
				<input type="checkbox" name="showAll" id="showAll" style="width:20px;" checked/>
			</div>
			
		</div>
	</form>
	
	<%-- PROJECT X RESOURCE --%>
	<jsp:include page="reports_project_resource.inc.jsp" flush="true"/>
	
	<%-- PROJECT X MONTH --%>
	<jsp:include page="reports_project_month.inc.jsp" flush="true"/>
	
	<%-- PROJECT X ACTIVITY X RESOURCE --%>
	<jsp:include page="reports_project_activity_resource.inc.jsp" flush="true"/>
	
	<%-- PROJECT X ACTIVITY X MONTH --%>
	<jsp:include page="reports_project_activity_month.inc.jsp" flush="true"/>
	
</visual:panel>