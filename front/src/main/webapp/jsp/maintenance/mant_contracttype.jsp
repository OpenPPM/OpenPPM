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
  ~ File: mant_contracttype.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<%-- Message --%>
<fmt:message key="maintenance.contracttype.new" var="new_contracttype_title" />
<fmt:message key="maintenance.contracttype.edit" var="edit_contracttype_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_contracttype">
	<fmt:param><fmt:message key="maintenance.contracttype"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_contracttype">
	<fmt:param><fmt:message key="maintenance.contracttype"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtContractTypeDescriptionRequired">
	<fmt:param><b><fmt:message key="maintenance.contracttype.description"/></b></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: Contracttype list
--%>

<script language="javascript" type="text/javascript" >

var contractTypesTable;
var contractTypeValidator;

function addContractType() {
	var f = document.frm_contracttype_pop;
	f.reset();
	f.id.value = "";
	
	$('#contracttype-popup legend').html('${new_contracttype_title}');
	$('#contracttype-popup').dialog('open');
}

function saveContractType() {
	
	var f = document.forms["frm_contracttype_pop"];
	
	var params={
		accion: "<%=MaintenanceServlet.JX_SAVE_CONTRACTTYPE %>",
		id: f.id.value,
		description: f.contracttype_description.value	
	};
	
	if (contractTypeValidator.form()) {
		mainAjax.call(params, function(data){
			var dataRow = [
				data.id,
				escape(data.description),     
				'<nobr><img onclick="editContractType(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteContractType(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
			];
			
			if (f.id.value == data.id) { contractTypesTable.fnUpdateAndSelect(dataRow); }
			else { contractTypesTable.fnAddDataAndSelect(dataRow); }
			
			f.reset();
			$('#contracttype-popup').dialog('close');
		});	
	}
}

function editContractType(element) {
	
	var contractType =contractTypesTable.fnGetData( element.parentNode.parentNode.parentNode );

	var f = document.forms["frm_contracttype_pop"];

	f.id.value 				= contractType[0];
	f.description.value 	= unEscape(contractType[1]);
	// Display followup info
	$('#contracttype-popup legend').html('${edit_contracttype_title}');
	$('#contracttype-popup').dialog('open');
}

function deleteContractType(element) {
	
	confirmUI(
		'${msg_title_confirm_delete_contracttype}','${msg_confirm_delete_contracttype}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var contractType = contractTypesTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_contracttype;

			f.accion.value = "<%=MaintenanceServlet.DEL_CONTRACTTYPE %>";
			f.id.value = contractType[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	contractTypesTable = $('#tb_contracttypes').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"aoColumns": [ 
			{ "sClass": "center", "bVisible": false },
			{ "sClass": "left" },
	        { "sClass": "center", "bSortable" : false}
      	]
	});

	$('#tb_contracttypes tbody tr').live('click',  function (event) {
		contractTypesTable.fnSetSelectable(this,'selected_internal');
	});

	$('div#contracttype-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 500,
		resizable: false,
		open: function(event, ui) { contractTypeValidator.resetForm(); }
	});

	contractTypeValidator = $("#frm_contracttype_pop").validate({
		errorContainer: $('div#contracttype-errors'),
		errorLabelContainer: 'div#contracttype-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#contracttype-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			description: {required: true }
		},
		messages:{
			description:{ required:'${fmtContractTypeDescriptionRequired}'}
		}
	});
});

</script>

<form id="frm_contracttype" name="frm_contracttype" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	
	<fieldset>
		<legend><fmt:message key="maintenance.contracttype" /></legend>
			<table id="tb_contracttypes" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.contracttype" /></th>
					 <th width="92%"><fmt:message key="maintenance.contracttype.description" /></th>
					 <th width="8%">
						<img onclick="addContractType()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="contracttype" items="${list}">
						<tr>
							<td>${contracttype.idContractType}</td>
							<td>${tl:escape(contracttype.description)}</td>
							<td>
								<nobr>
									<img onclick="editContractType(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteContractType(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="contracttype-popup">

	<div id="contracttype-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="contracttype-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_contracttype_pop" id="frm_contracttype_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.contracttype.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
				 <th><fmt:message key="maintenance.contracttype.description" />&nbsp;*</th>
				 <th>&nbsp;</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="contracttype_id" />
						<input type="text" name="description" id="contracttype_description" class="campo" maxlength="50"/>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveContractType(); return false;" value="<fmt:message key="save"/>" />
    	</div>
    </form>
</div>

