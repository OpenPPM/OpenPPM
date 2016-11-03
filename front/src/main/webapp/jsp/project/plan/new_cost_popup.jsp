<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

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
  ~ File: new_cost_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<c:set var="expense_id"><%=Constants.COST_TYPE_EXPENSE%></c:set>
<fmt:message key="cost.type.expense" var="expense_value"></fmt:message>
<c:set var="direct_id"><%=Constants.COST_TYPE_DIRECT%></c:set>
<fmt:message key="cost.type.direct" var="direct_value"></fmt:message>

<c:set var="type_expenses"><fmt:message key="expenses"/></c:set>
<c:set var="type_direct"><fmt:message key="direct_costs"/></c:set>

<fmt:message key="error" var="fmt_error" />

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtChargeAccountRequired">
	<fmt:param><b><fmt:message key="cost.charge_account"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtPVRequired">
	<fmt:param><b><fmt:message key="cost.value"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var costValidator;
var filterCost;

function saveCost() {

	if (costValidator.form()) {

		var f = document.frm_cost;
		
		var params = {
			accion: "<%=ProjectPlanServlet.JX_SAVE_COST%>",
			id: document.forms["frm_project"].id.value,
			cost_type: f.cost_cost_type.value,
			cost_id: f.cost_id.value,
			planned: f.cost_planned.value,
			budget_account: f.budget_account.value,
			desc: f.desc.value
		};
		
		planAjax.call(params,function(data) {
			
			var typeCost = (data.type == '${expense_id}' ? '${expense_value }' : '') +
			(data.type == '${direct_id}' ? '${direct_value }' : '');

			var tableData		= (data.type == '${direct_id}'?costsTable:expensesTable);
			var functionCost	= (data.type == '${direct_id}'?'editCost':'editExpense');
			
			var dataRow = [
				data.id,
				typeCost,
				escape($('#budget_account option:selected').text()), 
				escape(data.desc), 
				toCurrency(data.planned),
				'<nobr><img onclick="'+functionCost+'(this,\''+typeCost+'\', \''+data.type+'\')" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteCost(this, \''+data.type+'\')" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>',
				$('#budget_account').val()
			];
			
			if (f.cost_id.value != data.id) { tableData.fnAddDataAndSelect(dataRow); }
			else { tableData.fnUpdateAndSelect(dataRow); }
			
			f.reset();
			$('#cost-popup').dialog('close');
		});
	}
}

function closeCost() {
	$('div#cost-popup').dialog('close');
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
			cost_planned: { required: true },
			budget_account: { required: true }
		},
		messages: {
			cost_planned: { required: '${fmtPVRequired}' },
			budget_account: { required: '${fmtChargeAccountRequired}' }
		}
	});
	filterCost = $('#cost_cost_type').filterSelect({
		selectFilter : 'budget_account',
		prefix : 'sub'
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

	<form name="frm_cost" id="frm_cost" action="<%=ProjectPlanServlet.REFERENCE%>" >
		<input type="hidden" name="cost_id"/>		
		<input type="hidden" name="type" id="type"/>
		<input type="hidden" name="cost_cost_type" id="cost_cost_type" />
		<fieldset>
			<legend id="legend"><fmt:message key="cost.detail"/></legend>
				
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>					
					<th class="left" width="80%"><fmt:message key="cost.charge_account"/>&nbsp;*</th>
					<th class="left" width="20%"><fmt:message key="cost.value"/>&nbsp;*</th>
				</tr>
				<tr>					
					<td>
						<select name="budget_account" id="budget_account" class="campo">
							<c:forEach var="budgetaccount" items="${budgetaccounts}">
								<option class="sub${budgetaccount.typeCost }" value="${budgetaccount.idBudgetAccount}">${tl:escape(budgetaccount.description)}</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<input type="text" name="cost_planned" id="cost_planned" class="campo importe" />
					</td>
				</tr>
				<tr>
					<th class="left" colspan="3"><fmt:message key="cost.desc"/></th>
				</tr>
				<tr>
					<td colspan="2">
						<input type="text" name="desc" class="campo" maxlength="40" />
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
			<c:if test="${op:hasPermission(user,project.status,tab)}">
				<input type="submit" class="boton" onclick="saveCost(); return false;" value="<fmt:message key="save" />" />
			</c:if>
			<a href="javascript:closeCost();" class="boton"><fmt:message key="close" /></a>
    	</div>
    </form>
</div>