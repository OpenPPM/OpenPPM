<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
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
  ~ File: new_income_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="editIncome"><img onclick="editIncome(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png"></c:set>
	<c:set var="deleteIncome"><img onclick="deleteIncome(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></c:set>
</c:if>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtValueDateRequired">
	<fmt:param><b><fmt:message key="funding.value"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtDueDateRequired">
	<fmt:param><b><fmt:message key="funding.due_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtDueDateFormat">
	<fmt:param><b><fmt:message key="funding.due_date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.maximum_allowed" var="fmtValueMax">
	<fmt:param><b><fmt:message key="funding.value"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var incomeValidator;

function saveIncome() {

	if (incomeValidator.form()) {
		
		var f = document.frm_income;
		
		var params = {
			accion: "<%=ProjectPlanServlet.JX_SAVE_INCOME%>",
			id: document.forms["frm_project"].id.value,
			income_id: f.income_id.value,
			planned_date: f.income_planned_date.value,
			planned_amount: f.income_planned_amount.value,
			description: f.income_description.value
		};
		
		planAjax.call(params, function (data) {
			var dataRow = [
		        data.id,
		        data.planned_date,
		        data.planDaysToDate,
		        toCurrency(data.planned_amount),
		        escape(data.description),
				'${editIncome}'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'${deleteIncome}'
			];
			
			if (f.income_id.value != data.id) { incomesTable.fnAddDataAndSelect(dataRow); }
			else { incomesTable.fnUpdateAndSelect(dataRow); }
			
			f.reset();
			$('#income-popup').dialog('close');
		});
	} 
}

readyMethods.add(function() {
	$('div#income-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 330, 
		resizable: false,
		open: function(event, ui) { incomeValidator.resetForm(); }
	});

	incomeValidator = $("#frm_income").validate({
		errorContainer: 'div#income-errors',
		errorLabelContainer: 'div#income-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#income-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			income_planned_date: { required: true, date: true },
			income_planned_amount: { required: true, maxlength: <%=Constants.MAX_CURRENCY%> }
		},
		messages: {
			income_planned_date: { required: '${fmtDueDateRequired}', date: '${fmtDueDateFormat}' },
			income_planned_amount: { required: '${fmtValueDateRequired}', maxlength:'${fmtValueMax}' }
		}
	});

	var dates = $('#income_planned_date').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function() {
			if (incomeValidator.numberOfInvalids() > 0) incomeValidator.form();
		}
	});
});
//-->
</script>

<div id="income-popup" class="popup">

	<div id="income-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="income-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_income" id="frm_income" action="<%=ProjectPlanServlet.REFERENCE%>">
		<input type="hidden" name="income_id"/>
		<fieldset>
			<legend><fmt:message key="funding"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="50%"><fmt:message key="funding.due_date"/>&nbsp;*</th>
					<th class="left" width="50%"><fmt:message key="funding.value"/>&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="income_planned_date" id="income_planned_date" class="campo fecha" />
					</td>
					<td>
						<input type="text" name="income_planned_amount" id="income_planned_amount" class="campo importe" />
					</td>
				</tr>
				<tr>
					<th class="left" colspan=2><fmt:message key="income.description"/></th>					
				</tr>
				<tr>
					<td colspan=2>
						<textarea name="income_description" id="income_description" class="campo" style="width: 100%"></textarea>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveIncome(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>