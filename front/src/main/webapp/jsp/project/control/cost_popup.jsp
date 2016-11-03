<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
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
  ~ File: cost_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="msg.error.invalid_format" var="fmtCostDateFormat">
	<fmt:param><b><fmt:message key="cost.date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtCostActualRequired">
	<fmt:param><b><fmt:message key="cost.actual"/></b></fmt:param>
</fmt:message>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="editCost"><img onclick="editCost(this)" class="link" src="images/view.png" title="<fmt:message key="view"/>"/></c:set>									
</c:if>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="editExpense"><img onclick="editExpense(this)" class="link" src="images/view.png" title="<fmt:message key="view"/>"/></c:set>									
</c:if>

<script type="text/javascript">
<!--
var costValidator;

function saveCost() {
	var f = document.forms["frm_cost"];
	
	//Validate required fields
	if (costValidator.form()) {
		
		controlAjax.call($('#frm_cost').serialize(),function(data) {
			
			if(f.cost_cost_type.value == <%=Constants.COST_TYPE_DIRECT%>) {
				
				var cost = directsTable.fnGetSelectedData();
				
				cost[2] = escape(data.desc);
				cost[4] = toCurrency(data.actual);
				cost[5] = data.costDate;
				
				directsTable.fnUpdateAndSelect(cost);
			}
			else if(f.cost_cost_type.value == <%=Constants.COST_TYPE_EXPENSE%>) {
				
				var expense = expensesTable.fnGetSelectedData();
				expense[2] = escape(data.desc);
				expense[4] = toCurrency(data.actual);
				expense[5] = data.costDate;
				
				expensesTable.fnUpdateAndSelect(expense);
			}			
			f.reset();
			$('#cost-popup').dialog('close');
		});
	}
}

readyMethods.add(function() {
	$('div#cost-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false,
		open: function(event, ui) { costValidator.resetForm(); }
	});
	
	costValidator = $("#frm_cost").validate({
		errorContainer: 'div#cost-errors',
		errorLabelContainer: 'div#cost-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#cost-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			cost_actual: { required: true },
			cost_date: {date: true}
		},
		messages: {
			cost_actual: { required: '${fmtCostActualRequired}' },
			cost_date: {date: '${fmtCostDateFormat}' }
		}
	});
	
	var dates = $('#cost_date').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function() {
			if (costValidator.numberOfInvalids() > 0) costValidator.form();
		}
	});
});
//-->
</script>

<c:if test="${not empty cost}">
<script>
readyMethods.add(function() {
	$('#cost_type').attr("readonly", true);
});
</script>
</c:if>

<div id="cost-popup" class="popup">

	<div id="cost-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="cost-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	
	<form name="frm_cost" id="frm_cost" action="<%=ProjectControlServlet.REFERENCE%>" >
		<input type="hidden" name="accion" value="<%=ProjectControlServlet.JX_SAVE_COST%>"/>
		<input type="hidden" name="cost_id" />
		<input type="hidden" name="cost_cost_type" />
		
		<fieldset>
			<legend><fmt:message key="cost.detail"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th width="20%"><fmt:message key="cost.planned"/>&nbsp;*</th>
					<th width="20%"><fmt:message key="cost.actual"/>&nbsp;*</th>
					<th width="25%"><fmt:message key="cost.date"/>&nbsp;*</th>
					<th width="35%"><fmt:message key="calculated.expense_sheet"/></th>
				</tr>
				<tr>					
					<td class="center">
						<input type="text" name="cost_planned" id="cost_planned" class="importe"/>
					</td>
					<td>
						<input type="text" name="cost_actual" id="cost_actual" class="campo importe" />
					</td>
					<td class="center">
						<input type="text" name="cost_date" id="cost_date" class="campo fecha"/>
					</td>
					<td class="center">
						<span id="sumProjectExpenses">${tl:toCurrency(sumProjectExpenses)}</span>
						<span id="sumProjectCost">${tl:toCurrency(sumProjectCost)}</span>
					</td>
				</tr>
				<tr>
					<th class="left" colspan="3"><fmt:message key="cost.budget_account"/>&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="4">
						<input type="text" name="budget_account" id="budget_account" readonly>
					</td>
				</tr>
				<tr>
					<th class="left" colspan="3"><fmt:message key="cost.desc"/></th>
				</tr>
				<tr>
					<td colspan="4">
						<input type="text" name="desc" class="campo" maxlength="40" />
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
			<c:if test="${not op:isStringEquals(project.status, status_closed)}">
				<input type="submit" class="boton" onclick="saveCost(); return false;" value="<fmt:message key="save" />" />
			</c:if>
    	</div>
    </form>
</div>