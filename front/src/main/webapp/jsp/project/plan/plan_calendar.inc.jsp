<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Calendarbase"%>
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
  ~ File: plan_calendar.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:48
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
var exceptionsTable;

readyMethods.add(function() {

	$('#calendarDate').datepicker({ dateFormat: '${datePickerPattern}',numberOfMonths: ${numberOfMonths}, firstDay: 1, changeMonth: true });

	exceptionsTable = $('#tb_exceptions').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"bInfo": false,
		"aoColumns": [
		              { "bVisible": false }, 
		              { "sClass": "left", "sWidth": "50%" }, 
		              { "sClass": "center", "sWidth": "22%", "sType": "es_date" },
		              { "sClass": "center", "sWidth": "22%", "sType": "es_date" },
		              { "sClass": "center", "sWidth": "6%", "bSortable": false }
		      		]
	});
	
	$('#tb_exceptions tbody tr').live('click', function (event) {		
		exceptionsTable.fnSetSelectable(this,'selected_internal');
	} );
});
</script>
<c:if test="${op:hasPermission(user,project.status,tab)}">
<script type="text/javascript">
function saveCalendarBase() {

	var f = document.frm_project;
	planAjax.call({
		accion: "<%=ProjectPlanServlet.JX_SAVE_CALENDAR %>",
		idProject: f.id.value,
		weekStart: f.week_starts.value,
		fiscalYearStart: f.fiscal_starts.value,
		startTime1: f.start_time_1.value,
		startTime2: f.start_time_2.value,
		endTime1: f.end_time_1.value,
		endTime2: f.end_time_2.value,
		hoursDay: f.hours_day.value,
		hoursWeek: f.hours_week.value,
		daysMonth: f.days_month.value
	});
}

readyMethods.add(function() {
	$("#sel_calendar").change(function () {
		
		var f = document.frm_project;
		f.accion.value =	"<%=ProjectPlanServlet.CHANGE_CALENDAR_BASE%>";
		f.<%=Calendarbase.IDCALENDARBASE%>.value = $(this).val();
		loadingPopup();
		f.submit();
		
	});
});
</script>
</c:if>

<fmt:message key="project.calendar" var="titleProjectCalendar"/>
<visual:panel id="fieldPlanProjCal" title="${titleProjectCalendar }">
	<input type="hidden" name="<%=Calendarbase.IDCALENDARBASE%>"/>
	<table id="tb_calendar" width="100%" cellpadding="3" cellspacing="2">
		<tr>
			<td width="17%"></td>
			<td width="16%"></td>
			<td width="17%"></td>
			<td width="16%"></td>
			<td width="17%"></td>
			<td width="17%"></td>
		</tr>
		<tr>
			<th colspan="2"><fmt:message key="calendar.base"></fmt:message></th>
		   	<td rowspan="4" colspan="4">
		   		<table width="100%" cellpadding="3" cellspacing="2">
		   			<tr>
		   				<td align="center">
		   					<div id="calendarDate"></div>
		   				</td>
		   				<td>
		   					<table width="100%" cellpadding="3" cellspacing="2">
		   						<tr>
		   							<td width="20%"><div class="workDay"></div></td>
		   							<td width="80%"><fmt:message key="calendar.working"></fmt:message></td>
		   						</tr>
		   						<tr>
		   							<td><div class="notWorkDay"></div></td>
		   							<td><fmt:message key="calendar.non_working"></fmt:message></td>
	   							</tr>
		   						<tr>
		   							<td><div class="exceptionDay"></div></td>
		   							<td><fmt:message key="calendar.exception_day"></fmt:message></td>
		   						</tr>
		   						<tr>
		   							<td><div class="nonWorkingDay"></div></td>
		   							<td><fmt:message key="calendar.non_default_work_week"></fmt:message></td>
		   						</tr>
		   					</table>
		   				</td>
		   			</tr>
		   		</table>
		   	</td>
		</tr>
		<tr>
			<td colspan="2">
				<select class="campo" name="sel_calendar" id="sel_calendar">
					<option value="-1"><fmt:message key="select_opt"></fmt:message></option>
					<c:forEach var="calendar" items="${listCalendars }">
						<option value="${calendar.idCalendarBase }" ${calendar.idCalendarBase eq project.projectcalendar.calendarbase.idCalendarBase?"selected":"" }>${calendar.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="calendar.week_starts"></fmt:message></th>
			<td>
				<select class="campo" name="week_starts" id="week_starts">
					<option value="-1"><fmt:message key="select_opt"></fmt:message></option>
					<option value="1" ${project.projectcalendar.weekStart eq 1?"selected":"" }><fmt:message key="week.monday"></fmt:message></option>
					<option value="2" ${project.projectcalendar.weekStart eq 2?"selected":"" }><fmt:message key="week.tuesday"></fmt:message></option>
					<option value="3" ${project.projectcalendar.weekStart eq 3?"selected":"" }><fmt:message key="week.wednesday"></fmt:message></option>
					<option value="4" ${project.projectcalendar.weekStart eq 4?"selected":"" }><fmt:message key="week.thursday"></fmt:message></option>
					<option value="5" ${project.projectcalendar.weekStart eq 5?"selected":"" }><fmt:message key="week.friday"></fmt:message></option>
					<option value="6" ${project.projectcalendar.weekStart eq 6?"selected":"" }><fmt:message key="week.saturday"></fmt:message></option>
					<option value="7" ${project.projectcalendar.weekStart eq 7?"selected":"" }><fmt:message key="week.sunday"></fmt:message></option>
				</select>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="calendar.fiscal_start"></fmt:message></th>
			<td>
				<select class="campo" name="fiscal_starts" id="fiscal_starts">
					<option value="-1"><fmt:message key="select_opt"></fmt:message></option>
					<c:forEach var="increment" begin="1" end="12" step="1">
						<option value="${increment }" ${project.projectcalendar.fiscalYearStart eq increment?"selected":"" }><fmt:message key="month.month_${increment }"></fmt:message></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="calendar.morning_start_time"></fmt:message></th>
			<td>
				<select class="campo" name="start_time_1" id="start_time_1">
					<option value="-1"><fmt:message key="select_opt"></fmt:message></option>
					<c:forEach var="iHour" begin="0" end="23" step="1">
						<c:choose>
							<c:when test="${iHour < 10}">
								<option value="${iHour }" ${project.projectcalendar.startTime1 eq iHour?"selected":"" }>0${iHour }:00</option>
								<option value="${iHour }.5" ${project.projectcalendar.startTime1 eq (iHour+0.5)?"selected":"" }>0${iHour }:30</option>
							</c:when>
							<c:otherwise>
								<option value="${iHour }" ${project.projectcalendar.startTime1 eq iHour?"selected":"" }>${iHour }:00</option>
								<option value="${iHour }.5" ${project.projectcalendar.startTime1 eq (iHour+0.5)?"selected":"" }>${iHour }:30</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</td>
			<th><fmt:message key="calendar.afternoon_start_time"></fmt:message></th>
			<td>
				<select class="campo" name="start_time_2" id="start_time_2">
					<option value="-1"><fmt:message key="select_opt"></fmt:message></option>
					<c:forEach var="iHour" begin="0" end="23" step="1">
						<c:choose>
							<c:when test="${iHour < 10}">
								<option value="${iHour }" ${project.projectcalendar.startTime2 eq iHour?"selected":"" }>0${iHour }:00</option>
								<option value="${iHour }.5" ${project.projectcalendar.startTime2 eq (iHour+0.5)?"selected":"" }>0${iHour }:30</option>
							</c:when>
							<c:otherwise>
								<option value="${iHour }" ${project.projectcalendar.startTime2 eq iHour?"selected":"" }>${iHour }:00</option>
								<option value="${iHour }.5" ${project.projectcalendar.startTime2 eq (iHour+0.5)?"selected":"" }>${iHour }:30</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</td>
			<th><fmt:message key="calendar.hours_per_day"></fmt:message></th>
			<td>
				<select class="campo" name="hours_day" id="hours_day">
					<option value="-1"><fmt:message key="select_opt"></fmt:message></option>
					<c:forEach var="iHours" begin="1" end="16" step="1">
						<option value="${iHours }" ${project.projectcalendar.hoursDay eq iHours?"selected":"" }>${iHours },0 <fmt:message key="hour_min" /></option>
						<option value="${iHours }.5" ${project.projectcalendar.hoursDay eq (iHours+0.5)?"selected":"" }>${iHours },5 <fmt:message key="hour_min" /></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th><fmt:message key="calendar.morning_end_time"></fmt:message></th>
			<td>
				<select class="campo" name="end_time_1" id="end_time_1">
					<option value="-1"><fmt:message key="select_opt"></fmt:message></option>
					<c:forEach var="iHour" begin="0" end="23" step="1">
						<c:choose>
							<c:when test="${iHour < 10}">
								<option value="${iHour }" ${project.projectcalendar.endTime1 eq iHour?"selected":"" }>0${iHour }:00</option>
								<option value="${iHour }.5" ${project.projectcalendar.endTime1 eq (iHour+0.5)?"selected":"" }>0${iHour }:30</option>
							</c:when>
							<c:otherwise>
								<option value="${iHour }" ${project.projectcalendar.endTime1 eq iHour?"selected":"" }>${iHour }:00</option>
								<option value="${iHour }.5" ${project.projectcalendar.endTime1 eq (iHour+0.5)?"selected":"" }>${iHour }:30</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</td>
			<th><fmt:message key="calendar.afternoon_end_time"></fmt:message></th>
			<td>
				<select class="campo" name="end_time_2" id="end_time_2">
					<option value="-1"><fmt:message key="select_opt"></fmt:message></option>
					<c:forEach var="iHour" begin="0" end="23" step="1">
						<c:choose>
							<c:when test="${iHour < 10}">
								<option value="${iHour }" ${project.projectcalendar.endTime2 eq iHour?"selected":"" }>0${iHour }:00</option>
								<option value="${iHour }.5" ${project.projectcalendar.endTime2 eq (iHour+0.5)?"selected":"" }>0${iHour }:30</option>
							</c:when>
							<c:otherwise>
								<option value="${iHour }" ${project.projectcalendar.endTime2 eq iHour?"selected":"" }>${iHour }:00</option>
								<option value="${iHour }.5" ${project.projectcalendar.endTime2 eq (iHour+0.5)?"selected":"" }>${iHour }:30</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</td>
			<th><fmt:message key="calendar.hours_per_week"></fmt:message></th>
			<td>
				<select class="campo" name="hours_week" id="hours_week">
					<option value="-1"><fmt:message key="select_opt"></fmt:message></option>
					<c:forEach var="hour" begin="1" end="45" step="1">
						<option value="${hour }" ${project.projectcalendar.hoursWeek eq hour?"selected":"" }>${hour },0 <fmt:message key="hour_min" /></option>
						<option value="${hour }.5" ${project.projectcalendar.hoursWeek eq (hour+0.5)?"selected":"" }>${hour },5 <fmt:message key="hour_min" /></option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
			<th><fmt:message key="calendar.days_per_month"></fmt:message></th>
			<td>
				<select class="campo" name="days_month" id="days_month">
					<option value="-1"><fmt:message key="select_opt"></fmt:message></option>
					<c:forEach var="day" begin="1" end="31" step="1">
						<option value="${day }" ${project.projectcalendar.daysMonth eq day?"selected":"" }>${day } <fmt:message key="week.day_min" /></option>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>
	
	<%-- CALENDAR EXCEPTIONS --%>
	<fmt:message key="calendar.exceptions" var="titleCalendarExceptions"/>
	<visual:panel id="fieldPlanCalExceptions" title="${titleCalendarExceptions }" cssClass="panel2">
		<table id="tb_exceptions" class="tabledata" cellspacing="1" style="width:100% !important;">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th><fmt:message key="calendar.name"/></th>
					<th><fmt:message key="calendar.start"/></th>
					<th><fmt:message key="calendar.finish"/></th>
					<th>
						<c:if test="${op:hasPermission(user,project.status,tab)}">
							<img onclick="newException()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
						</c:if>
					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="excepion" items="${project.projectcalendar.projectcalendarexceptionses }">
					<tr>
						<td>${excepion.idProjectCalendarException }</td>
						<td>${tl:escape(excepion.description) }</td>
						<td><fmt:formatDate value="${excepion.startDate}" pattern="${datePattern}"/></td>
						<td><fmt:formatDate value="${excepion.finishDate}" pattern="${datePattern}"/></td>
						<td>
							<c:if test="${op:hasPermission(user,project.status,tab)}">
								<nobr>
									<img onclick="viewException(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteException(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
								</nobr>
							</c:if>
						</td>
					</tr>
				</c:forEach>
				<c:forEach var="excepion" items="${project.projectcalendar.calendarbase.calendarbaseexceptionses }">
					<tr>
						<td>${excepion.idCalendarBaseException }</td>
						<td>${tl:escape(excepion.description) }</td>
						<td><fmt:formatDate value="${excepion.startDate}" pattern="${datePattern}"/></td>
						<td><fmt:formatDate value="${excepion.finishDate}" pattern="${datePattern}"/></td>
						<td>&nbsp;</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</visual:panel>
	<c:if test="${op:hasPermission(user,project.status,tab)}">
		<div class="right" style="margin-top:15px;">
			<a href="javascript:saveCalendarBase()" class="boton"><fmt:message key="save" /></a>
		</div>
	</c:if>
</visual:panel>
