<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

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
  ~ File: mant_operationaccount.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>


<%-- Message --%>
<fmt:message key="maintenance.operationaccount.new" var="new_operationaccount_title" />
<fmt:message key="maintenance.operationaccount.edit" var="edit_operationaccount_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_operationaccount">
	<fmt:param><fmt:message key="maintenance.operationaccount"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_operationaccount">
	<fmt:param><fmt:message key="maintenance.operationaccount"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtOperationAccountDescriptionRequired">
	<fmt:param><b><fmt:message key="maintenance.operationaccount.description"/></b></fmt:param>
</fmt:message>


<%-- 
Request Attributes:
	list_maintenance: Operationaccount list
--%>

<script language="javascript" type="text/javascript" >

var operationAccountsTable;
var operationAccountValidator;

function addOperationAccount() {
	var f = document.frm_operationaccount_pop;
	f.reset();
	f.id.value = "";
	
	$('#operationaccount-popup legend').html('${new_operationaccount_title}');
	$('#operationaccount-popup').dialog('open');
}

function saveOperationAccount() {

	var f = document.frm_operationaccount_pop;
	
	if (operationAccountValidator.form()) {
		
		var params={
			accion: "<%=MaintenanceServlet.JX_SAVE_OPERATIONACCOUNT %>",
			id: f.id.value,
			description: f.operationaccount_description.value	
		};
		
		mainAjax.call(params, function(data){
			var dataRow = [
				data.id,
				escape(data.description),
				'<nobr><img onclick="editOperationAccount(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteOperationAccount(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'          
			];

			if (f.id.value == data.id) { operationAccountsTable.fnUpdateAndSelect(dataRow); }
			else { operationAccountsTable.fnAddDataAndSelect(dataRow); }
			
			f.reset();
			$('#operationaccount-popup').dialog('close');
		});	
	}
}

function editOperationAccount(element) {
		
		var operationAccount = operationAccountsTable.fnGetData( element.parentNode.parentNode.parentNode );
					
		var f = document.forms["frm_operationaccount_pop"];
		f.id.value 				= unEscape(operationAccount[0]);
		f.description.value 	= unEscape(operationAccount[1]);
		// Display followup info
		$('#operationaccount-popup legend').html('${edit_operationaccount_title}');
		$('#operationaccount-popup').dialog('open');
}

function deleteOperationAccount(element) {
	confirmUI(
		'${msg_title_confirm_delete_operationaccount}','${msg_confirm_delete_operationaccount}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var operationAccount = operationAccountsTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_operationaccount;

			f.accion.value = "<%=MaintenanceServlet.DEL_OPERATIONACCOUNT %>";
			f.id.value = operationAccount[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	operationAccountsTable = $('#tb_operationaccount').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"aaSorting": [[ 0, "asc" ]],
			"aoColumns": [ 
				{ "sClass": "center", "bVisible" : false },
				{ "sClass": "left" },
		        { "sClass": "center", "bSortable" : false}
	      	]	
	});

	$('#tb_operationaccount tbody tr').live('click',  function (event) {
		operationAccountsTable.fnSetSelectable(this,'selected_internal');
	});

	$('div#operationaccount-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { operationAccountValidator.resetForm(); }
	});

	operationAccountValidator = $("#frm_operationaccount_pop").validate({
		errorContainer: $('div#operationaccount-errors'),
		errorLabelContainer: 'div#operationaccount-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#operationaccount-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			description: {required: true }
		},
		messages:{
			description: {required:'${fmtOperationAccountDescriptionRequired}'}
		}
	});
});

</script>

<form id="frm_operationaccount" name="frm_operationaccount" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />
	<fieldset>
		<legend><fmt:message key="maintenance.operationaccount" /></legend>
			<table id="tb_operationaccount" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.operationaccount" /></th>
					 <th width="92%"><fmt:message key="maintenance.operationaccount.description" /></th>
					 <th width="8%">
						<img onclick="addOperationAccount()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="operationaccount" items="${list}">
						<tr>
							<td>${operationaccount.idOpAccount}</td>
							<td>${tl:escape(operationaccount.description)}</td>
							<td>
								<nobr>
									<img onclick="editOperationAccount(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteOperationAccount(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="operationaccount-popup">

	<div id="operationaccount-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="operationaccount-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_operationaccount_pop" id="frm_operationaccount_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.operationaccount.new"/></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
				 <th><fmt:message key="maintenance.operationaccount.description" />&nbsp;*</th>
				 <th>&nbsp;</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="operationaccount_id" />
						<input type="text" name="description" id="operationaccount_description" class="campo" maxlength="50"/>
					</td>
				</tr>
    		</table>
    	</fieldset>
   	   	<div class="popButtons">
   	   		<input type="submit" class="boton" onclick="saveOperationAccount(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

