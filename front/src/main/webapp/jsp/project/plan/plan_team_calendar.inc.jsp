<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
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
  ~ File: plan_team_calendar.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:49
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--
function updateTeamCalendar() {
	
	planAjax.call({
			accion:"<%=ProjectPlanServlet.JX_UPDATE_TEAM_CALENDAR%>",
			idProject:'${project.idProject}',
			since: $("#calStart").val(),
			until: $("#calFinish").val()
		},function(data) {
			$('#teamCalendarContent').html(data);
		},'html'
	);
}

readyMethods.add(function() {
	var dates = $('#calStart, #calFinish').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "calStart" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			dates.not(this).datepicker("option", option, date);
		}
	});
});

//-->
</script>

<fmt:message key="team.calendar" var="titleTeamCalendar"/>
<visual:panel id="fieldPlanTeamCal" title="${titleTeamCalendar }">
	<div>
   		<span style="margin-right:5px;">
			<fmt:message key="dates.since"/>:&nbsp;
			<input type="text" id="calStart" class="campo fecha alwaysEditable" value="${valPlanInitDate}"/>
   		</span>
       	<span style="margin-right:5px;">
			<fmt:message key="dates.until"/>:&nbsp;
			<input type="text" id="calFinish" class="campo fecha alwaysEditable" value="${valLastDate}"/>
		</span>
		<a href="javascript:updateTeamCalendar();" class="boton"><fmt:message key="team.calendar.refresh" /></a>
	</div>
	<div id="teamCalendarContent"></div>
	<div style="padding-top:15px;">
		<table style="margin:0 auto; padding-top: 15px" cellpadding="4" cellspacing="4">
			<tr>
				<td><div class="workDay"></div></td>
				<td><fmt:message key="calendar.working"></fmt:message></td>
				<td><div class="notWorkDay"></div></td>
				<td><fmt:message key="calendar.non_working"></fmt:message></td>
				<td><div class="exceptionDay"></div></td>
				<td><fmt:message key="calendar.exception_day"></fmt:message></td>
				<td><div class="nonWorkingDay"></div></td>
				<td><fmt:message key="calendar.non_default_work_week"></fmt:message></td>
			</tr>
		</table>
	</div>
</visual:panel>