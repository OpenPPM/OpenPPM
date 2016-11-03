<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@ page import="es.sm2.openppm.core.model.impl.Operation" %>

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
  ~ File: mant_operation.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:07
  --%>

<fmt:setLocale value="${locale}" scope="request"/>


<%-- Message --%>
<fmt:message key="maintenance.operation.new" var="new_operation_title" />
<fmt:message key="maintenance.operation.edit" var="edit_operation_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_operation">
	<fmt:param><fmt:message key="maintenance.operation"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_operation">
	<fmt:param><fmt:message key="maintenance.operation"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtOperationNameRequired">
	<fmt:param><b><fmt:message key="maintenance.operation.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtOperationCodeRequired">
	<fmt:param><b><fmt:message key="maintenance.operation.code"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_select_a" var="fmtOperationOpAccountRequired">
	<fmt:param><b><fmt:message key="maintenance.operation.op_account"/></b></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: Operation list
	listOpAccounts: List of Operationaccount
--%>

<script language="javascript" type="text/javascript" >

var operationTable;
var operationValidator;

function addOperation() {
	var f = document.frm_operation_pop;
	f.reset();
	f.id.value = "";
	
	$('#operation-popup legend').html('${new_operation_title}');
	$('#operation-popup').dialog('open');
}

function saveOperation() {

	var f = document.forms["frm_operation_pop"];
	
	if (operationValidator.form()) {
		
		var params = {
			accion: "<%=MaintenanceServlet.JX_SAVE_OPERATION %>",
			id: f.id.value,
			name: f.operation_name.value,
			code: f.operation_code.value,
			idOpAccount : f.idOpAccount.value,
			availableForManager: $('#availableForManager').prop('checked'),
			availableForApprove: $('#availableForApprove').prop('checked'),
            <%= Operation.EXCLUDEEXTERNALS %>: $('#excludeExternals').prop('checked')
		};
		
		mainAjax.call(params, function(data){
			var dataRow = [
				data.id,
				escape(data.name),
				escape(data.code),
				escape($(f.opeartion_account).find('option:selected').text()),
				'<nobr><img onclick="editOperation(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteOperation(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>',
				data.idOpAccount,
				$('#availableForManager').prop('checked'),
                $('#excludeExternals').prop('checked'),
				$('#availableForApprove').prop('checked')
			];

			if (f.id.value == data.id) { operationTable.fnUpdateAndSelect(dataRow); }
			else { operationTable.fnAddDataAndSelect(dataRow); }
			
			f.reset();
			$('#operation-popup').dialog('close');		
		});	
	}
}

function editOperation(element) {

		var operation =operationTable.fnGetData( element.parentNode.parentNode.parentNode );
	
		var f = document.forms["frm_operation_pop"];

		f.id.value 				= operation[0];
		f.name.value 			= unEscape(operation[1]);
		f.code.value			= unEscape(operation[2]);
		f.idOpAccount.value 	= operation[5];

		$('#availableForManager').prop('checked', ((operation[6]+'') === 'true'));
        $('#excludeExternals').prop('checked', ((operation[7]+'') === 'true'));
		$('#availableForApprove').prop('checked', ((operation[8]+'') === 'true'));

		// Display followup info
		$('#operation-popup legend').html('${edit_operation_title}');
		$('#operation-popup').dialog('open');
}

function deleteOperation(element) {
	confirmUI(
		'${msg_title_confirm_delete_operation}','${msg_confirm_delete_operation}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var operation = operationTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_operation;
	
			f.accion.value = "<%=MaintenanceServlet.DEL_OPERATION %>";
			f.id.value = operation[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	operationTable = $('#tb_operation').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"aoColumns": [ 
			{ "sClass": "left", "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
			{ "sClass": "left" },
			{ "sClass": "center", "bSortable" : false },
			{ "bVisible": false },
			{ "bVisible": false },
			{ "bVisible": false },
            { "bVisible": false }
   		]		
	});

	$('#tb_operation tbody tr').live('click',  function (event) {
		operationTable.fnSetSelectable(this,'selected_internal');
	});

	$('div#operation-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { operationValidator.resetForm(); }
	});

	operationValidator = $("#frm_operation_pop").validate({
		errorContainer: $('div#operation-errors'),
		errorLabelContainer: 'div#operation-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#operation-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			name: {required: true , maxlength: 18 },
		    code: {required: true , maxlength: 18 },
		    idOpAccount: {required: true }
		},
		messages:{
			name: {required: '${fmtOperationNameRequired}' },
		    code: {required: '${fmtOperationCodeRequired}' },
		    idOpAccount: {required: '${fmtOperationOpAccountRequired}' }
		}
	});
});

</script>

<form id="frm_operation" name="frm_operation" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	
	<fieldset>
		<legend><fmt:message key="maintenance.operation" /></legend>
			<table id="tb_operation" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.operation" /></th>
					 <th width="42%"><fmt:message key="maintenance.operation.name" /></th>
					 <th width="20%"><fmt:message key="maintenance.operation.code" /></th>
					 <th width="20%"><fmt:message key="maintenance.operation.op_account" /></th>
					 <th width="8%">
						<img onclick="addOperation()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					 <th width="0%">&nbsp;</th>
					 <th width="0%">&nbsp;</th>
                     <th width="0%">&nbsp;</th>
                     <th width="0%">&nbsp;</th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="operation" items="${list}">
						<tr>
							<td>${operation.idOperation}</td>
							<td>${tl:escape(operation.operationName)}</td>
							<td>${tl:escape(operation.operationCode)}</td>
							<td>${tl:escape(operation.operationaccount.description)}</td>
							<td>
								<nobr>
									<img onclick="editOperation(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteOperation(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
							<td>${operation.operationaccount.idOpAccount}</td>
							<td>${operation.availableForManager }</td>
                            <td>${operation.excludeExternals }</td>
							<td>${operation.availableForApprove }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>


<div id="operation-popup">

	<div id="operation-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="operation-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_operation_pop" id="frm_operation_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.operation.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th width="45%"><fmt:message key="maintenance.operation.code" />&nbsp;*</th>
					<th width="45%"><fmt:message key="maintenance.operation.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="operation_id" />
						<input type="text" name="code" id="operation_code" class="campo" maxlength="20"/>
					</td>
					<td>
						<input type="text" name="name" id="operation_name" class="campo" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<th><fmt:message key="operation.exclude_external_resources"/></th>
					<th><fmt:message key="maintenance.operation.op_account" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="checkbox" name="excludeExternals" id="excludeExternals"/></td>
					<td>
						<select class="campo" name="idOpAccount" id="opeartion_account">
							<option value=""><fmt:message key="select_opt" /></option>
							<c:forEach var="opAccount" items="${listOpAccounts}">
								<option value="${opAccount.idOpAccount}">${opAccount.description}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th colspan="2"><fmt:message key="operation.available_rm"/></th>
				</tr>
                <tr>
					<td class="center">
						<label>
							<fmt:message key="OPERATION.AVAILABLE_FOR_MANAGER"/>
							<input type="checkbox" name="availableForManager" id="availableForManager"/>
						</label>
					</td>
					<td class="center">
						<label>
							<fmt:message key="OPERATION.AVAILABLE_FOR_APPROVE"/>
							<input type="checkbox" name="availableForApprove" id="availableForApprove"/>
						</label>
					</td>
				</tr>
    		</table>
    	</fieldset>
   	   	<div class="popButtons">
   	   		<input type="submit" class="boton" onclick="saveOperation(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>