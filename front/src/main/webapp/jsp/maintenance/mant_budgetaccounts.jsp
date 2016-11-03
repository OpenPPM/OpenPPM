<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>


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
  ~ File: mant_budgetaccounts.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>


<%-- Message --%>
<fmt:message key="maintenance.budgetaccounts.new" var="new_budgetaccounts_title" />
<fmt:message key="maintenance.budgetaccounts.edit" var="edit_budgetaccounts_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_budgetaccounts">
	<fmt:param><fmt:message key="maintenance.budgetaccounts"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_budgetaccounts">
	<fmt:param><fmt:message key="maintenance.budgetaccounts"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtBudgetAccountsDescriptionRequired">
	<fmt:param><b><fmt:message key="maintenance.budgetaccounts.description"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg" var="fmtBudgetAccountsTypeCostRequired">
	<fmt:param><b><fmt:message key="maintenance.budgetaccounts.type_cost"/></b></fmt:param>
</fmt:message>


<c:set var="expense_id"><%=Constants.COST_TYPE_EXPENSE%></c:set>
<fmt:message key="cost.type.expense" var="expense_value"></fmt:message>
<c:set var="direct_id"><%=Constants.COST_TYPE_DIRECT%></c:set>
<fmt:message key="cost.type.direct" var="direct_value"></fmt:message>

<%-- 
Request Attributes:
	list_maintenance: BudgetAccounts list
--%>

<script language="javascript" type="text/javascript" >

var budgetAccountsTable;
var budgetAccountsValidator;

function addBudgetAccounts() {
	var f = document.forms["frm_budgetaccounts_pop"];
	f.reset();
	f.id.value = "";
	
	$('#budgetaccounts-popup legend').html('${new_budgetaccounts_title}');
	$('#budgetaccounts-popup').dialog('open');
}

function saveBudgetAccounts() {

	var f = document.frm_budgetaccounts_pop;
	
	if (budgetAccountsValidator.form()) {
		
		var params={
			accion: "<%=MaintenanceServlet.JX_SAVE_BUDGETACCOUNTS %>",
			id: f.id.value,
			description: f.budgetaccounts_description.value,
			type_cost: f.type_cost.value	
		};
		mainAjax.call(params, function(data){
			var dataRow = [
				data.id,
				escape(data.description),
				data.type_cost,
				escape(data.type_cost_desc), 
				'<nobr><img onclick="editBudgetAccounts(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="return deleteBudgetAccounts(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'           
			];
			
			if (f.id.value == data.id) { budgetAccountsTable.fnUpdateAndSelect(dataRow); }
			else { budgetAccountsTable.fnAddDataAndSelect(dataRow); }

			f.reset();
			$('#budgetaccounts-popup').dialog('close');
		});	
	}	
}

function editBudgetAccounts(element) {
	
		var budgetAccounts = budgetAccountsTable.fnGetData( element.parentNode.parentNode.parentNode );	
	
		var f = document.forms["frm_budgetaccounts_pop"];

		f.id.value 				= budgetAccounts[0];
		f.description.value 	= unEscape(budgetAccounts[1]);
		f.type_cost.value		= budgetAccounts[2];
		// Display followup info
		$('#budgetaccounts-popup legend').html('${edit_budgetaccounts_title}');
		$('#budgetaccounts-popup').dialog('open');
}

function deleteBudgetAccounts(element) {
	
	confirmUI(
		'${msg_title_confirm_delete_budgetaccounts}','${msg_confirm_delete_budgetaccounts}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var budgetaccounts = budgetAccountsTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_budgetaccounts;
			
			f.accion.value = "<%=MaintenanceServlet.DEL_BUDGETACCOUNTS %>";
			f.id.value = budgetaccounts[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	budgetAccountsTable = $('#tb_budgetaccounts').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"aaSorting": [[ 0, "asc" ]],
			"aoColumns": [ 
				{ "sClass": "center", "bVisible": false },
				{ "sClass": "left" },
				{ "sClass": "left", "bVisible": false },
				{ "sClass": "left" },
	            { "sClass": "center", "bSortable" : false}
      		]	
	});

	$('#tb_budgetaccounts tbody tr').live('click',  function (event) {		
		budgetAccountsTable.fnSetSelectable(this,'selected_internal');
	});

	$('div#budgetaccounts-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { budgetAccountsValidator.resetForm(); }
	});

	budgetAccountsValidator = $("#frm_budgetaccounts_pop").validate({
		errorContainer: $('div#budgetaccounts-errors'),
		errorLabelContainer: 'div#budgetaccounts-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#budgetaccounts-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			description: { required: true },
		    type_cost: { required: true }
		},
		messages:{
			description:{ required:'${fmtBudgetAccountsDescriptionRequired}'},
			type_cost:{ required:'${fmtBudgetAccountsTypeCostRequired}'}
		}
	});
});

</script>

<form id="frm_budgetaccounts" name="frm_budgetaccounts" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	
	<fieldset>
		<legend><fmt:message key="maintenance.budgetaccounts" /></legend>
			<table id="tb_budgetaccounts" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.budgetaccounts" /></th>
					 <th width="62%"><fmt:message key="maintenance.budgetaccounts.description" /></th>
					 <th width="0%">&nbsp;</th>
					 <th width="30%"><fmt:message key="maintenance.budgetaccounts.type_cost" /></th>
					 <th width="8%">
						<img onclick="addBudgetAccounts()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="budgetaccounts" items="${list}">
						<tr>
							<td>${budgetaccounts.idBudgetAccount}</td>
							<td>${tl:escape(budgetaccounts.description)}</td>
							<td>${budgetaccounts.typeCost}</td>
							<td>
								<c:if test="${budgetaccounts.typeCost eq expense_id}">${tl:escape(expense_value)}</c:if>
								<c:if test="${budgetaccounts.typeCost eq direct_id}">${tl:escape(direct_value)}</c:if>
							</td>
							<td>
								<nobr>
									<img onclick="editBudgetAccounts(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteBudgetAccounts(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>


<div id="budgetaccounts-popup">

	<div id="budgetaccounts-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="budgetaccounts-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_budgetaccounts_pop" id="frm_budgetaccounts_pop">
		<input type="hidden" name="id" id="budgetaccounts_id" />
		
		<fieldset>
			<legend><fmt:message key="maintenance.budgetaccounts.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="maintenance.budgetaccounts.description" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="description" id="budgetaccounts_description" class="campo" maxlength="100"/>
					</td>
				</tr>
				
				<tr>
					<th width="75%"><fmt:message key="maintenance.budgetaccounts.type_cost" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<select name="type_cost" id="type_cost" class="campo">
							<option value="${expense_id }">${expense_value }</option>
							<option value="${direct_id }">${direct_value }</option>
						</select>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveBudgetAccounts(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>
