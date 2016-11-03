<%@page import="es.sm2.openppm.core.model.impl.Projectcalendarexceptions"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.CalendarServlet"%>

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
  ~ File: calendar_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="calendar.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtStartRequired">
	<fmt:param><b><fmt:message key="calendar.start"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtFinishRequired">
	<fmt:param><b><fmt:message key="calendar.finish"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtStartDateFormat">
	<fmt:param><b><fmt:message key="calendar.start"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtFinishDateFormat">
	<fmt:param><b><fmt:message key="calendar.finish"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtStartDateAfterFinish">
	<fmt:param><b><fmt:message key="calendar.start"/></b></fmt:param>
	<fmt:param><b><fmt:message key="calendar.finish"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtFinishDateBeforeStart">
	<fmt:param><b><fmt:message key="calendar.finish"/></b></fmt:param>
	<fmt:param><b><fmt:message key="calendar.start"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.confirm_delete" var="fmtExceptionDeleteMsg">
	<fmt:param><fmt:message key="calendar.exceptions"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="fmtExceptionDeleteTitle">
	<fmt:param><fmt:message key="calendar.exceptions"/></fmt:param>
</fmt:message>
<fmt:message key="calendar.new_exception" var="newException" />

<script type="text/javascript">
<!--
var validatorCalendar;
var datesCalendar;

function newException() {

	f = document.forms["frm_calendar_popup"];
	f.reset();
	f.idExceptionWork.value = "";

	// Reset minDate and maxDate restrictions
	datesCalendar.datepicker("option", "minDate", null);
	datesCalendar.datepicker("option", "maxDate", null);
	
	$("#pop_exception").show();
	$("#pop_workweek").hide();
	$("#legend_calendar").html("${newException}");
	$("#calendar-popup").dialog('open');
}

function viewException(element) {
	var aData = exceptionsTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	var f = document.frm_calendar_popup;
	newException();
	
	f.idExceptionWork.value = aData[0];
	f.calendar_name.value	= unEscape(aData[1]);
	f.calendar_start.value	= aData[2];
	f.calendar_end.value	= aData[3];
}

function deleteException(element) {
	
	confirmUI(
			'${fmtExceptionDeleteTitle}','${fmtExceptionDeleteMsg}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var aData = exceptionsTable.fnGetData( element.parentNode.parentNode.parentNode );
				
				planAjax.call({
						accion : '<%=ProjectPlanServlet.JX_DELETE_CALENDAR_EXCEPTION %>',
						<%=Projectcalendarexceptions.IDPROJECTCALENDAREXCEPTION%>: aData[0]
					},function(data) {
						exceptionsTable.fnDeleteSelected();
				});
	});
}

function saveDateCalendar() {

	if (validatorCalendar.form()) {

		var f = document.frm_calendar_popup;
		f.accion.value = "<%=ProjectPlanServlet.JX_SAVE_CALENDAR_EXCEPTION %>";
		
		planAjax.call($(f).serialize(),function(data) {
			
			var dataRow = [
            	data.<%=Projectcalendarexceptions.IDPROJECTCALENDAREXCEPTION%>,
            	escape(f.calendar_name.value),
            	f.calendar_start.value,
            	f.calendar_end.value,
            	'<nobr><img onclick="viewException(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
            	'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
            	'<img onclick="deleteException(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
            ];
			
			if (f.idExceptionWork.value != data.<%=Projectcalendarexceptions.IDPROJECTCALENDAREXCEPTION%>) { exceptionsTable.fnAddDataAndSelect(dataRow); }
			else { exceptionsTable.fnUpdateAndSelect(dataRow); }

			$("#calendar-popup").dialog("close");
		});
	}
}

readyMethods.add(function() {
	$('div#calendar-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false,
		open: function(event, ui) { validatorCalendar.resetForm(); }
	});

	datesCalendar = $('#calendar_start, #calendar_end').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "calendar_start" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			datesCalendar.not(this).datepicker("option", option, date);
			if (validatorCalendar.numberOfInvalids() > 0) validatorCalendar.form();
		}
	});
	validatorCalendar = $("#frm_calendar_popup").validate({
		errorContainer: 'div#calendar-errors',
		errorLabelContainer: 'div#calendar-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#calendar-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			calendar_name: { required: true },
			calendar_start: { required: true, date: true, minDateTo: '#calendar_end' },
			calendar_end: { required: true, date: true, maxDateTo: '#calendar_start' }
		},
		messages: {
			calendar_name: { required: '${fmtNameRequired}' },
			calendar_start: { required: '${fmtStartRequired}', date: '${fmtStartDateFormat}', minDateTo: '${fmtStartDateAfterFinish}' },
			calendar_end: { required: '${fmtFinishRequired}', date: '${fmtFinishDateFormat}', maxDateTo: '${fmtFinishDateBeforeStart}' }
		}
	});
});
//-->
</script>

<div id="calendar-popup" class="popup">

	<div id="calendar-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="calendar-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_calendar_popup" id="frm_calendar_popup" onsubmit="">
		<input type="hidden" name="accion"/>
		<input type="hidden" name="idExceptionWork"/>
		<input type="hidden" name="<%=Project.IDPROJECT %>" value="${project.idProject }"/>
		
		<fieldset>
			<legend id="legend_calendar"></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="40%"><fmt:message key="calendar.name"/>&nbsp;*</th>
					<th class="left" width="30%"><fmt:message key="calendar.start"/>&nbsp;*</th>
					<th class="left" width="30%"><fmt:message key="calendar.finish"/>&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="calendar_name" class="campo" />
					</td>
					<td>
						<input type="text" name="calendar_start" id="calendar_start" class="campo fecha" />
					</td>
					<td>
						<input type="text" name="calendar_end" id="calendar_end" class="campo fecha" />
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
			<input type="submit" class="boton" id="pop_exception" onclick="saveDateCalendar(); return false;" value="<fmt:message key="save"/>" />
    	</div>
    </form>
</div>