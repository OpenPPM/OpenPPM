<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

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
  ~ File: mant_company.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:07
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_company">
	<fmt:param><fmt:message key="maintenance.company"/></fmt:param>
</fmt:message>

<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_company">
	<fmt:param><fmt:message key="maintenance.company"/></fmt:param>
</fmt:message>

<fmt:message key="maintenance.company.new" var="new_company_title" />
<fmt:message key="maintenance.company.edit" var="edit_company_title" />
<fmt:message key="yes" var="msg_yes" />
<fmt:message key="no" var="msg_no" />

<fmt:message key="error" var="fmt_error" />
<%-- 
Request Attributes:
	list_maintenance: Company list
--%>

<script language="javascript" type="text/javascript" >

var companiesTable;
var companyValidator;

function addCompany() {
	var f = document.forms["frm_company_pop"];
	f.reset();
	f.id.value = "";
	
	$('#companies-popup legend').html('${new_company_title}');
	$('#companies-popup').dialog('open');
	
	return false;
}

function saveCompany() {

	var f = document.forms["frm_company_pop"];
	
	if (companyValidator.form()) {
		
		var params = {
			accion: "<%=MaintenanceServlet.JX_SAVE_COMPANY %>",
			id: f.id.value,
			name: f.company_name.value
		};
		
		mainAjax.call(params, function(data) {
			var dataRow = [
				data.id,
				data.name,
				'<img src="images/delete.jpg" title="<fmt:message key="delete_company" />" onclick="return deleteCompany(event, '+data.id+');"/>'
			];
						
			if (f.id.value == data.id) { companiesTable.fnUpdateAndSelect(dataRow); }
			else { companiesTable.fnAddDataAndSelect(dataRow); }
			
			f.reset();
			$('#companies-popup').dialog('close');
		});	
	}	
	return false;
}

function editCompany() {
	
	var anSelected = fnGetSelected( companiesTable );
	if(anSelected.length > 0){	
		
		var aPos = companiesTable.fnGetPosition( anSelected[0] );
		var aData = companiesTable.fnGetData(aPos);
		var company = aData;
					
		var f = document.forms["frm_company_pop"];

		f.id.value 		= company[0];
		f.name.value 	= company[1];
		// Display followup info
		$('#companies-popup legend').html('${edit_company_title}');
		$('#companies-popup').dialog('open');
	}
	return false;
}

function deleteCompany(event, id) {
	var target = getTargetFromEvent(event);

	$('#dialog-confirm-msg').html('${msg_confirm_delete_company}');
	$('#dialog-confirm').dialog(
			'option', 
			'buttons', 
			{
				"${msg_no}": function() { 
					$('#dialog-confirm').dialog("close"); 
				},
				"${msg_yes}": function() {
					deleteCompanyConfirmated(id);
				}
			}
	);
	
	$('#dialog-confirm').dialog(
			'option',
			'title',
			'${msg_title_confirm_delete_company}'
	);
	$('#dialog-confirm').dialog('open');
	return false;
}

function deleteCompanyConfirmated(id) {
	$('#dialog-confirm').dialog("close"); 
	var f = document.forms["frm_companies"];

	f.accion.value = "<%=MaintenanceServlet.DEL_COMPANY %>";	
	f.id.value = id;
	f.idManten.value = $('select#idManten').val();
	f.submit();
	
	return false;
}


readyMethods.add(function () {

	companiesTable = $('#tb_companies').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"aaSorting": [[ 0, "asc" ]],
			"aoColumns": [ 
						{ "sClass": "center", "sWidth": "1%", "bVisible" : false },
						{ "sClass": "left", "sWidth": "80%", "bVisible" : true },
			            { "sClass": "center", "sWidth": "20%", "bVisible" : true, "bSortable" : false}
			      		]
				
	});

	$('#tb_companies tbody tr').live('click',  function (event) {		
		companiesTable.fnSetSelectable(this,'selected_internal');
	} );

	$('div#companies-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 400,
		resizable: false,
		open: function(event, ui) { companyValidator.resetForm(); }
	});

	companyValidator = $("#frm_company_pop").validate({
		errorContainer: $('div#company-errors'),
		errorLabelContainer: $("ol", $('div#company-errors')),
		wrapper: 'li',
		meta: "validate", 
		rules: {
			name: {
		    required: true
		    }
		}
	});

});

</script>

<form id="frm_companies" name="frm_companies" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	
	<fieldset>
		<legend><fmt:message key="maintenance.company" /></legend>
		
		<table width="100%">
			<tr>
				<td>
					<table id="tb_companies" class="tabledata" cellspacing="1" width="100%">
						<thead>
							<tr>
							 <th><fmt:message key="maintenance.company" /></th>
							 <th><fmt:message key="maintenance.company.name" /></th>
							 <th>&nbsp;</th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="company" items="${list}">
								<tr>
									<td>${company.idCompany}</td>
									<td width="90%">${company.name}</td>
									<td width="10%"><img src="images/delete.jpg" onclick="return deleteCompany(event, ${company.idCompany});" title="<fmt:message key="delete_company" />"/></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<a href="#" class="boton" onClick="return addCompany();"><fmt:message key="add" /></a>
					<a href="#" class="boton" onClick="return editCompany();"><fmt:message key="edit" /></a>
				</td>
			</tr>
		</table>
	</fieldset>
</form>


<div id="companies-popup">

	<div id="company-errors" class="ui-state-error ui-corner-all hide">
		<fmt:message key="manager.name" var="name"/>
		<h4><fmt:message key="msg.error_title"/></h4>
		<ol>
			<li><label for="company_name" class="error"><fmt:message key="maintenance.error_msg_a"><fmt:param value="${name}" /></fmt:message></label></li>
		</ol>
	</div>

	<form name="frm_company_pop" id="frm_company_pop" method="post">
		<fieldset>
			<legend><fmt:message key="maintenance.company.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="maintenance.company.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="company_id" />
						<input type="text" name="name" id="company_name" class="campo" maxlength="50"/>
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td>
						<a href="#" class="boton" onClick="return saveCompany();"><fmt:message key="save" /></a>
					</td>
				</tr>
    		</table>
    	</fieldset>
    </form>
</div>
