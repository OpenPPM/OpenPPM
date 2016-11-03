<%@page import="es.sm2.openppm.front.servlets.ExpenseSheetServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

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
  ~ File: add_expense_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtProjectRequired">
	<fmt:param><b><fmt:message key="project"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtOperationRequired">
	<fmt:param><b><fmt:message key="operation"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var newExpenseValidator;

function newExpense() { $('div#expense-popup').dialog('open'); }

function addExpense() {
	
	if (newExpenseValidator.form()) {
		
		var f = document.frm_expense_sheet;
		f.accion.value = "<%=ExpenseSheetServlet.ADD_EXPENSE %>";
		f.idProject.value	= ($('#pr_radio').is(':checked')?$('#codeproject_id').val():''); 
		f.idOperation.value = ($('#op_radio').is(':checked')?$('#operation_id').val():''); 
		loadingPopup();
		f.submit();
	}
}

jQuery.validator.addMethod("requiredDisabled", function(value, element) {
	if (!$(element).prop('disabled') && value == '' ) {
		return false;
	}
	return true;
}, "");

readyMethods.add(function() {
	$('div#expense-popup').dialog({
		autoOpen: false, modal: true, width: 550, resizable: false,
		open: function(event, ui) { newExpenseValidator.resetForm(); }
	});

	newExpenseValidator = $("#frmNewExpense").validate({
		errorContainer: 'div#frmNewExpenseErrors',
		errorLabelContainer: 'div#frmNewExpenseErrors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#frmNewExpenseNumerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			idOperation : { requiredDisabled: true },
			idProject : { requiredDisabled: true }
		},
		messages: {
			idOperation : "${fmtOperationRequired}",
			idProject : "${fmtProjectRequired}"
		}
	});
	
	$('.radio_code').click(function() {
		if ($(this).val() == "operation") {
			$('#operation_id').attr('disabled',false);
			$('#codeproject_id').attr('disabled',true);
		}
		else {
			$('#operation_id').attr('disabled',true);
			$('#codeproject_id').attr('disabled',false);
		}
	});
	
});
//-->
</script>

<div id="expense-popup">
	<form id="frmNewExpense">
	
		<div id="frmNewExpenseErrors" class="ui-state-error ui-corner-all hide">
			<p>
				<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
				<strong><fmt:message key="msg.error_title"/></strong>
				&nbsp;(<b><span id="frmNewExpenseNumerrors"></span></b>)
			</p>
			<ol></ol>
		</div>
		
		<fieldset>
			<legend><fmt:message key="new.expense"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<td width="5%"><input type="radio" id="op_radio" name="radio_code" class="radio_code" value="operation" checked="checked"/></td>
					<th class="left" width="25%"><label for="op_radio"><fmt:message key="operation"/></label></th>
					<td width="70%">
						<select name="idOperation" id="operation_id" class="campo">
							<option value=""><fmt:message key="select_opt"/></option>
							<c:forEach var="operation" items="${operations}">
								<option value="${operation.idOperation}">${tl:escape(operation.operationName)}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td width="5%"><input type="radio" id="pr_radio" name="radio_code" class="radio_code" value="project"/></td>
					<th class="left" width="25%"><label for="pr_radio"><fmt:message key="project"/></label></th>
					<td width="70%">
						<select name="idProject" id="codeproject_id" class="campo" disabled="disabled">
							<option value=""><fmt:message key="select_opt"/></option>
							<c:forEach var="project" items="${projects}">
								<option value="${project.idProject}">${tl:escape(project.projectName)}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
	   		</table>
	   	</fieldset>
	   	<div class="popButtons">
			<a href="javascript:addExpense();" class="boton"><fmt:message key="add" /></a>
	   	</div>
   	</form>
</div>