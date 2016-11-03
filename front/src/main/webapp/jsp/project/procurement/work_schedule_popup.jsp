<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectProcurementServlet"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.core.model.impl.Activityseller"%>

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
  ~ File: work_schedule_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="error" var="fmtError" />
<fmt:message key="maintenance.procurement.new_work" var="new_work_title" />
<fmt:message key="maintenance.procurement.edit_work" var="edit_work_title" />

<fmt:message key="error" var="fmtError" />

<fmt:message key="maintenance.error_msg_select_a" var="fmtSellerRequired">
	<fmt:param><b><fmt:message key="procurement.seller_name"/></b></fmt:param>
</fmt:message>

<fmt:message key="msg.error.invalid_format" var="fmtBaselineStartDateFormat">
	<fmt:param><b><fmt:message key="procurement.work_schedule.baseline_start"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtBaselineFinishDateFormat">
	<fmt:param><b><fmt:message key="procurement.work_schedule.baseline_finish"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtStartDateFormat">
	<fmt:param><b><fmt:message key="procurement.work_schedule.start"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtFinishDateFormat">
	<fmt:param><b><fmt:message key="procurement.work_schedule.finish"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_after_date" var="fmtBaselineStartDateAfterFinish">
	<fmt:param><b><fmt:message key="procurement.work_schedule.baseline_start"/></b></fmt:param>
	<fmt:param><b><fmt:message key="procurement.work_schedule.baseline_finish"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_after_date" var="fmtStartDateAfterFinish">
	<fmt:param><b><fmt:message key="procurement.work_schedule.start"/></b></fmt:param>
	<fmt:param><b><fmt:message key="procurement.work_schedule.finish"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtBaselineFinishDateBeforeStart">
	<fmt:param><b><fmt:message key="procurement.work_schedule.baseline_finish"/></b></fmt:param>
	<fmt:param><b><fmt:message key="procurement.work_schedule.baseline_start"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtFinishDateBeforeStart">
	<fmt:param><b><fmt:message key="procurement.work_schedule.finish"/></b></fmt:param>
	<fmt:param><b><fmt:message key="procurement.work_schedule.start"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >
<!--
var workValidator;

function editWork(element) {	
	
	var work = workTable.fnGetData( element.parentNode.parentNode );
	
	var f = document.forms["frm_work_popup"];
	
	f.idWork.value 									= work[0];	
	f.<%=Activityseller.SELLER %>.value 			= work[1];
	$("#workSellerName").text(unEscape(work[2]));
	$("#workSellerIndirect").attr("checked", $('#indirect_'+work[0]).is(':checked'));
	$("#workActivityName").text(unEscape(work[4]));	
	f.<%=Activityseller.BASELINESTART %>.value 		= work[5];
	f.<%=Activityseller.STARTDATE %>.value 			= work[6];
	f.<%=Activityseller.BASELINEFINISH %>.value 	= work[7];
	f.<%=Activityseller.FINISHDATE %>.value 		= work[8];
	$("#<%=Activityseller.WORKSCHEDULEINFO %>").text(unEscape(work[9]));		
	
	$('#activityStartSchedule').text(work[11]);
	$('#activityFinishSchedule').text(work[12]);
	
	$('div#work_popup legend').html('${edit_work_title}');	
	$('div#work_popup').dialog('open');
}

function saveWork() {
	
	if (workValidator.form()) {		
		var f = document.forms["frm_work_popup"];		
		f.action = "<%=ProjectProcurementServlet.REFERENCE%>";
		f.accion.value = "<%=ProjectProcurementServlet.SAVE_WORK %>";
		loadingPopup();
		f.submit();
	}
}

function closeWork() {
	$('div#work_popup').dialog('close'); 
}

//When document is ready
readyMethods.add(function() {
	
	$('div#work_popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 550, 
		minWidth: 300, 
		resizable: false,
		open: function(event, ui) { workValidator.resetForm(); }
	});
	
	// validate the form when it is submitted
	workValidator = $("#frm_work_popup").validate({
		errorContainer: 'div#work-errors',
		errorLabelContainer: 'div#work-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Activityseller.SELLER %> : {required: true },
			<%=Activityseller.BASELINESTART%> : { date : true, minDateTo: '#<%=Activityseller.BASELINEFINISH%>' },
			<%=Activityseller.BASELINEFINISH%> : { date : true, maxDateTo: '#<%=Activityseller.BASELINESTART%>' },
			<%=Activityseller.STARTDATE%> : { date : true, minDateTo: '#<%=Activityseller.FINISHDATE%>' },
			<%=Activityseller.FINISHDATE%> : { date : true, maxDateTo: '#<%=Activityseller.STARTDATE%>' },
			<%=Activityseller.WORKSCHEDULEINFO %>: { maxlength: 200 }
		},
		messages: {
			<%=Activityseller.SELLER %> : {required: '${fmtSellerRequired}' },
			<%=Activityseller.BASELINESTART%>: { date: '${fmtBaselineStartDateFormat}', minDateTo: '${fmtBaselineStartDateAfterFinish}' },
			<%=Activityseller.BASELINEFINISH%>: { date: '${fmtBaselineFinishDateFormat}', maxDateTo: '${fmtBaselineFinishDateBeforeStart}' },
			<%=Activityseller.STARTDATE%>: { date: '${fmtStartDateFormat}', minDateTo: '${fmtStartDateAfterFinish}' },
			<%=Activityseller.FINISHDATE%>: { date: '${fmtFinishDateFormat}', maxDateTo: '${fmtFinishDateBeforeStart}' }
		}
	});
});
//-->
</script>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<script type="text/javascript">
	<!--
		readyMethods.add(function() {
			var datesBaseline = $('#<%=Activityseller.BASELINESTART %>, #<%=Activityseller.BASELINEFINISH %>').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,
				onSelect: function(selectedDate) {
					var option = this.id == "<%=Activityseller.BASELINESTART %>" ? "minDate" : "maxDate";
					var instance = $(this).data("datepicker");
					var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
					datesBaseline.not(this).datepicker("option", option, date);
					if (workValidator.numberOfInvalids() > 0) workValidator.form();
				}
			});
			
			var dates = $('#<%=Activityseller.STARTDATE %>, #<%=Activityseller.FINISHDATE %>').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,
				onSelect: function(selectedDate) {
					var option = this.id == "<%=Activityseller.STARTDATE %>" ? "minDate" : "maxDate";
					var instance = $(this).data("datepicker");
					var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
					dates.not(this).datepicker("option", option, date);
					if (workValidator.numberOfInvalids() > 0) workValidator.form();
				}
			});
			
		});
	//-->
	</script>
</c:if>
		

<div id="work_popup" class="popup">

	<div id="work-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_work_popup" id="frm_work_popup">	
		<input type="hidden" name="id" value="${project.idProject}" /> <!-- idProject -->
		<input type="hidden" name="idWork" />		
		<input type="hidden" name="accion" />
		<input type="hidden" name="<%=Activityseller.SELLER %>" />
		
		<fieldset>
			<legend></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="30%"><fmt:message key="procurement.activity_name"/></th>
					<td colspan = "3">
						<span id="workActivityName"></span>							
					</td>							
				</tr>
				<tr>
					<th class="left"><fmt:message key="activity.start_full"/>&nbsp;</th>
					<td width="25%" id="activityStartSchedule"></td>					
					<th class="left" width="20%"><fmt:message key="activity.finish_full"/>&nbsp;</th>
					<td width="30%" id="activityFinishSchedule"></td>
				</tr>
				<tr><td colspan="4">&nbsp;</td></tr>
				<tr>
					<th class="left" width="30%"><fmt:message key="procurement.seller_name"/></th>
					<td>
						<span id="workSellerName"></span>							
					</td>	
					<th class="left"><fmt:message key="procurement.indirect"/></th>
					<td>
						<input type="checkbox" id="workSellerIndirect" style="width:20px; vertical-align: top;" disabled>
					</td>						
				</tr>
				<tr>
					<th class="left"><fmt:message key="procurement.work_schedule.baseline_start"/>&nbsp;</th>
					<td width="25%"><input type="text" name="<%=Activityseller.BASELINESTART %>" id="<%=Activityseller.BASELINESTART %>" class="campo fecha" /></td>					
					<th class="left" width="20%"><fmt:message key="procurement.work_schedule.start"/>&nbsp;</th>
					<td width="30%"><input type="text" name="<%=Activityseller.STARTDATE %>" id="<%=Activityseller.STARTDATE %>" class="campo fecha" /></td>
				</tr>
				<tr>
					<th class="left"><fmt:message key="procurement.work_schedule.baseline_finish"/>&nbsp;</th>
					<td><input type="text" name="<%=Activityseller.BASELINEFINISH %>" id="<%=Activityseller.BASELINEFINISH %>" class="campo fecha" /></td>
					<th class="left"><fmt:message key="procurement.work_schedule.finish"/>&nbsp;</th>
					<td><input type="text" name="<%=Activityseller.FINISHDATE %>" id="<%=Activityseller.FINISHDATE %>" class="campo fecha" /></td>
				</tr>
				<tr><td colspan="4">&nbsp;</td></tr>
				<tr>
					<th class="left" colspan ="4"><fmt:message key="procurement.work_schedule.work_info"/>&nbsp;</th>
				</tr>
				<tr>
					<td colspan ="4">
						<textarea name="<%=Activityseller.WORKSCHEDULEINFO %>" id="<%=Activityseller.WORKSCHEDULEINFO %>" class="campo" style="width:98%;" ></textarea>						
					</td>
				</tr>
			</table>			
    	</fieldset>
    	<div class="popButtons">
    		<c:if test="${op:hasPermission(user,project.status,tab)}">
    			<input type="submit" class="boton" onclick="saveWork(); return false;" value="<fmt:message key="save" />" />
			</c:if>
			<a href="javascript:closeWork();" class="boton"><fmt:message key="close" /></a>
    	</div>
    </form>
</div>