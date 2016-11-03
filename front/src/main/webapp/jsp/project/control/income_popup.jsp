<%@page import="es.sm2.openppm.core.model.impl.Projectactivity"%>
<%@page import="es.sm2.openppm.core.model.impl.Incomes"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

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
  ~ File: income_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="maintenance.error_msg_a" var="fmtActualBillAmmountRequired">
	<fmt:param><b><fmt:message key="funding.actual_value"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtActualBillDateRequired">
	<fmt:param><b><fmt:message key="funding.actual_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtActualBillDateFormat">
	<fmt:param><b><fmt:message key="funding.actual_date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="editIncome"><img onclick="editIncome(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png"></c:set>
</c:if>
<script type="text/javascript">
<!--
var incomeValidator;

function editIncome(element) {
	var income = incomesTable.fnGetData( element.parentNode.parentNode );
	
	var f = document.forms["frm_income"];
	f.<%=Incomes.IDINCOME %>.value				= income[0];
	f.<%=Incomes.ACTUALBILLDATE %>.value		= income[3];
	f.<%=Incomes.ACTUALBILLAMMOUNT %>.value		= income[5];
	$("#<%=Incomes.ACTUALDESCRIPTION %>").text(unEscape(income[6]));
	
	$('#income-popup').dialog('open');
}

function saveIncome() {

	if (incomeValidator.form()) {
		
		controlAjax.call($('#frm_income').serialize(), function (data) {
			
			var f = document.frm_income;
			
			var dataRow = [
		        f.<%=Incomes.IDINCOME%>.value,
		        data.<%=Incomes.PLANNEDBILLDATE%>,
		        toCurrency(data.<%=Incomes.PLANNEDBILLAMMOUNT%>),
		        f.<%=Incomes.ACTUALBILLDATE%>.value,
		        data.actDaysToDate,
		        f.<%=Incomes.ACTUALBILLAMMOUNT%>.value,
		        escape($('#<%=Incomes.ACTUALDESCRIPTION%>').val()),
				'${editIncome}'
			];

			incomesTable.fnUpdateAndSelect(dataRow);
			
			$('#income-popup').dialog('close');
			f.reset();
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
			<%=Incomes.ACTUALBILLDATE %>: { required: true, date: true },
			<%=Incomes.ACTUALBILLAMMOUNT %>: { required: true },
			<%=Incomes.ACTUALDESCRIPTION %>: { maxlength:200 }
		},
		messages: {
			<%=Incomes.ACTUALBILLDATE %>: { required: '${fmtActualBillDateRequired}', date: '${fmtActualBillDateFormat}' },
			<%=Incomes.ACTUALBILLAMMOUNT %>: { required: '${fmtActualBillAmmountRequired}' }
		}
	});

	var dates = $('#<%=Incomes.ACTUALBILLDATE %>').datepicker({
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
	
	<form name="frm_income" id="frm_income" action="<%=ProjectControlServlet.REFERENCE%>" >
		<input type="hidden" name="accion" value="<%=ProjectControlServlet.JX_SAVE_INCOME%>"/>
		<input type="hidden" name="<%=Incomes.IDINCOME%>"/>
		
		<fieldset>
			<legend><fmt:message key="funding"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="50%"><fmt:message key="funding.actual_date"/>&nbsp;*</th>
					<th class="left" width="50%"><fmt:message key="funding.actual_value"/>&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="<%=Incomes.ACTUALBILLDATE %>" id="<%=Incomes.ACTUALBILLDATE %>" class="campo fecha" />
					</td>
					<td>
						<input type="text" name="<%=Incomes.ACTUALBILLAMMOUNT %>" class="campo importe" />
					</td>
				</tr>
				<tr>
					<th class="left" colspan=2><fmt:message key="income.description"/></th>					
				</tr>
				<tr>
					<td colspan=2>
						<textarea name="<%=Incomes.ACTUALDESCRIPTION %>" id="<%=Incomes.ACTUALDESCRIPTION %>" class="campo" style="width: 100%"></textarea>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveIncome(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>