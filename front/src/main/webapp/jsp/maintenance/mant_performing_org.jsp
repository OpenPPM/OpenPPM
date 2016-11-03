<%@page import="es.sm2.openppm.core.model.impl.Performingorg"%>
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
  ~ File: mant_performing_org.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.perfoming_org.new" var="new_perfoming_org_title" />
<fmt:message key="maintenance.perfoming_org.edit" var="edit_perfoming_org_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_performing_org">
	<fmt:param><fmt:message key="maintenance.perfoming_org"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_performing_org">
	<fmt:param><fmt:message key="maintenance.perfoming_org"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtPerformingOrgFunctionalManagerRequired">
	<fmt:param><b><fmt:message key="maintenance.perfoming_org.functional_manager"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg" var="fmtPerformingOrgNameRequired">
	<fmt:param><b><fmt:message key="maintenance.perfoming_org.name"/></b></fmt:param>
</fmt:message>


<c:set var="p_mant_performing_org_onsaas_true"  value="true" />
<c:set var="p_mant_performing_org_onsaas_false"  value="false" />


<%-- 
Request Attributes:
	list_maintenance: 	Performing Org list
	listCompanies:		List Companies
	listEmployees:		List Employees
--%>

<script language="javascript" type="text/javascript" >

var performingorgsTable;
var performingorgValidator;

function addPerformingOrg() {
	var f = document.frm_performingorg_pop;
	f.reset();
	f.id.value = "";
	
	$('#performingorgs-popup legend').html('${new_perfoming_org_title}');
	$('#performingorgs-popup').dialog('open');
}

function savePerformigOrg() {

	var f = document.frm_performingorg_pop;
	var checked = '';

	if (f.performingorg_onsaas.checked){
		f.performingorg_onsaas.value = "true";
	}else{
		f.performingorg_onsaas.value = "false";
	}
	
	if (performingorgValidator.form()) {
		
		var params={
			accion: "<%=MaintenanceServlet.JX_SAVE_PERFORG %>",
			id: f.performingorg_id.value,
			manager: f.performingorg_manager.value,
			onsaas: f.performingorg_onsaas.value,
			name: f.performingorg_name.value
		};
		
		mainAjax.call(params, function(data){	
			var dataRow = [
				data.<%=Performingorg.IDPERFORG%>,
				escape($(f.performingorg_manager).find('option:selected').text()),
				'<input type="checkbox" disabled="disabled" '+checked+'/>',
				escape(f.performingorg_name.value),
				checked,
				'<nobr><img onclick="editPerformingOrg(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deletePerformingOrg(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>',
				f.performingorg_manager.value      
			];
			
			if (f.id.value == data.<%=Performingorg.IDPERFORG%>) { performingorgsTable.fnUpdateAndSelect(dataRow); }
			else { performingorgsTable.fnAddDataAndSelect(dataRow); }

			f.reset();
			$('#performingorgs-popup').dialog('close');				
		});	
	}	
}

function editPerformingOrg(element) {
	
	var performing = performingorgsTable.fnGetData( element.parentNode.parentNode.parentNode );

	var f = document.forms["frm_performingorg_pop"];
	f.id.value 		= performing[0];
	f.name.value 	= unEscape(performing[3]);
	f.manager.value = performing[6];
	
	if (performing[5] == "checked"){f.onsaas.checked = "true";}else{f.onsaas.checked = "";}
	
	$('#performingorgs-popup legend').html('${edit_perfoming_org_title}');
	$('#performingorgs-popup').dialog('open');
}

function deletePerformingOrg(element) {

	confirmUI(
		'${msg_title_confirm_delete_performing_org}','${msg_confirm_delete_performing_org}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var performing = performingorgsTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_performingorgs;
	
			f.accion.value = "<%=MaintenanceServlet.DEL_PERFORG %>";
			f.id.value = performing[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	performingorgsTable = $('#tb_performingorgs').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aoColumns": [ 
			{ "bVisible": false },
            { "sClass": "left" },
            { "bVisible": false },
            { "sClass": "left" },
            { "bVisible": false },
            { "sClass": "center", "bSortable" : false },
            { "bVisible": false }
      	]
	});

	$('#tb_performingorgs tbody tr').live('click',  function (event) {
		performingorgsTable.fnSetSelectable(this,'selected_internal');
	});

	$('div#performingorgs-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { performingorgValidator.resetForm(); }
	});

	performingorgValidator = $("#frm_performingorg_pop").validate({
		errorContainer: $('div#performingorg-errors'),
		errorLabelContainer: 'div#performingorg-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#performingorg-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			name: { required: true },
		    manager: { required: true }
		},
		messages:{
			name: { required: '${fmtPerformingOrgNameRequired}' },
		    manager: { required: '${fmtPerformingOrgFunctionalManagerRequired}' }
		}
	});
});

</script>

<form id="frm_performingorgs" name="frm_performingorgs" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />
	<fieldset>
		<legend><fmt:message key="maintenance.perfoming_org" /></legend>
			<table id="tb_performingorgs" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
						 <th width="0%"><fmt:message key="maintenance.perfoming_org" /></th>
						 <th width="30%"><fmt:message key="maintenance.perfoming_org.functional_manager" /></th>
						 <th width="0%"><fmt:message key="maintenance.perfoming_org.onsaas" /></th>
						 <th width="60%"><fmt:message key="maintenance.perfoming_org.name" /></th>
						 <th width="0%">&nbsp;</th>
						 <th width="10%">
							<img onclick="addPerformingOrg()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
						 </th>
						 <th width="0%">&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="performing" items="${perfOrgs}">
						<tr>
							<td>${performing.idPerfOrg}</td>
							<td>${tl:escape(performing.employee.contact.fullName)}</td>
							<td><input type="checkbox" disabled="disabled" <c:if test="${performing.onSaaS == p_mant_performing_org_onsaas_true}">checked</c:if>/></td>
							<td>${tl:escape(performing.name)}</td>
							<td><c:if test="${performing.onSaaS == p_mant_performing_org_onsaas_true}">checked</c:if></td>
							<td>
								<nobr>
									<img onclick="editPerformingOrg(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deletePerformingOrg(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
							<td>${performing.employee.idEmployee}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="performingorgs-popup">

	<div id="performingorg-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="performingorg-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_performingorg_pop" id="frm_performingorg_pop">
		<input type="hidden" name="id" id="performingorg_id" />
		<input type="hidden" name="onsaas" id="performingorg_onsaas" />
		<fieldset>
			<legend><fmt:message key="maintenance.perfoming_org.new"/></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="maintenance.perfoming_org.name" />&nbsp;*</th>
				 	<!-- <th><fmt:message key="maintenance.perfoming_org.onsaas" /></th> -->
				</tr>
				<tr>
					<td>
						<input type="text" name="name" id="performingorg_name" class="campo" maxlength="45"/>
					</td>
					<!--
					<td>
						<input type="checkbox" name="onsaas" id="performingorg_onsaas" class="campo"/>
					</td>
					-->
				</tr>
				<tr>
					<th><fmt:message key="maintenance.perfoming_org.functional_manager" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<select class="campo" name="manager" id="performingorg_manager">
							<option value=""><fmt:message key="select_opt" /></option>
							<c:forEach var="performingorgManager" items="${listEmployees}">
								<option value="${performingorgManager.idEmployee}">${performingorgManager.contact.fullName}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="savePerformigOrg(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>
