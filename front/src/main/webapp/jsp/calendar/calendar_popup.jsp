<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.CalendarServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Calendarbaseexceptions"%>

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
  ~ Create Date: 15/03/2015 12:48:03
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
<fmt:message key="maintenance.error_msg_a" var="msg_error_init_date">
	<fmt:param><fmt:message key="msg.error.valid_date_init"/></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="msg_error_end_date">
	<fmt:param><fmt:message key="msg.error.valid_date_end"/></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="msg_error_calendar_name">
	<fmt:param><fmt:message key="calendar.name"/></fmt:param>
</fmt:message>
<fmt:message key="calendar.new_exception" var="newException">
</fmt:message>
<fmt:message key="calendar.edit_exception" var="editException">
</fmt:message>

<script type="text/javascript">
<!--
var validatorCalendar;
var calendarPopup = {
		
		create: function() {
			if ($("#idCalendarBase").val() > 0){
				f = document.forms["frmCalendarPopup"];
				f.reset();
				
				f.calendar_exception_id.value =  "";
				
				$('#calendarStart').datepicker("option","maxDate", "");
				$('#calendarEnd').datepicker("option","minDate", "");
				
				$("#legend_calendar").html("${newException}");
				$("#pop_exception").show();
				$("#pop_workweek").hide();
				$("#calendar-popup").dialog('open');
			}
			else {
				alertUI("${fmt_error}","${fmt_calendar_base}");
			}
			return false;
		},	
		
	save: function() {
		
		var f = document.forms["frmCalendarPopup"];
		var dataRow;
		
		if (validatorCalendar.form()) {
			var params = {
				accion: "<%=CalendarServlet.JX_SAVE_CALENDAR_EXCEPTION %>",			
				idCalendarBase: 		 	$("#idCalendarBase").val(),
				idCalendarBaseException: 	f.calendar_exception_id.value,
				name: 					    f.calendar_name.value,
				startDate: 					f.calendarStart.value,
				endDate: 					f.calendarEnd.value
			};
	
			calendarAjax.call(params, function(data) {				
				 	dataRow = [
	             	data.idCalendarBaseException,
	             	data.description,
	             	data.startDate,
	             	data.finishDate,
	                '<img title="<fmt:message key="edit"/>" class="editCalendarException" class="link" src="images/modify.png" />' 
			        +'&nbsp;&nbsp;&nbsp;'
			        +'<img title="<fmt:message key="delete"/>" class="removeCalendarException" class="link" src="images/delete.jpg"/>' 
				];
				
				if (f.calendar_exception_id.value == data.<%=Calendarbaseexceptions.IDCALENDARBASEEXCEPTION%>) {
					exceptionsTable.fnUpdateAndSelect(dataRow);
				}
				else { 
					exceptionsTable.fnAddDataAndSelect(dataRow);
				}
				$("#calendar-popup").dialog("close");
			});	
		}
	},
	
	edit: function(element) {
	 	if ($("#idCalendarBase").val() > 0){
	 		var calendarExceptions = exceptionsTable.fnGetData(element.parentNode.parentNode);

	 		f = document.forms["frmCalendarPopup"];
 			
	 		f.calendar_exception_id.value 	 =  calendarExceptions[0];		
 			f.calendar_name.value 			 =	unEscape(calendarExceptions[1]);
 			f.calendarStart.value 			 = 	calendarExceptions[2];
 			f.calendarEnd.value 			 = 	calendarExceptions[3];	
		
 			$('#calendarStart').datepicker("option","maxDate", "");
 			$('#calendarEnd').datepicker("option","minDate", "");
		
 			$("#legend_calendar").html("${editException}");
 			$("#pop_exception").show();
 			$("#pop_workweek").hide();
 			$("#calendar-popup").dialog('open');
 		}
 		else {
 			alertUI("${fmt_error}","${fmt_calendar_base}");
 		}
 		return false;
	},
	
	initialize: function() {
		$("#saveException").on('click',calendarPopup.save);
	}
};

readyMethods.add(function() {
	$('div#calendar-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false,
		open: function(event, ui) { validatorCalendar.resetForm(); }
	});

	var dates = $('#calendarStart, #calendarEnd').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "calendarStart" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			dates.not(this).datepicker("option", option, date);
			if (validatorCalendar.numberOfInvalids() > 0) validatorCalendar.form();
		}
	});
	validatorCalendar = $("#frmCalendarPopup").validate({
		errorContainer: 'div#calendar-errors',
		errorLabelContainer: 'div#calendar-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#calendar-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			calendar_name: { required: true },
			calendarStart: { required: true, date: true, minDateTo: '#calendarEnd' },
			calendarEnd: { required: true, date: true, maxDateTo: '#calendarStart' }
		},
		messages: {
			calendar_name: { required: '${fmtNameRequired}' },
			calendarStart: { required: '${fmtStartRequired}', date: '${fmtStartDateFormat}', minDateTo: '${fmtStartDateAfterFinish}' },
			calendarEnd: { required: '${fmtFinishRequired}', date: '${fmtFinishDateFormat}', maxDateTo: '${fmtFinishDateBeforeStart}' }
		}
	});
	calendarPopup.initialize();

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
	<form name="frmCalendarPopup" id="frmCalendarPopup" action="calendar" method="post">
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
						<input type="hidden" name="calendar_exception_id" id="calendar_exception_id" value=""/>
						<input type="text" name="calendar_name" class="campo" />
					</td>
					<td>
						<input type="text" name="calendarStart" id="calendarStart" class="campo fecha" />
					</td>
					<td>
						<input type="text" name="calendarEnd" id="calendarEnd" class="campo fecha" />
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<button type="button" class="boton" id="saveException"><fmt:message key="save" /></button>
    	</div>
    </form>
</div>
