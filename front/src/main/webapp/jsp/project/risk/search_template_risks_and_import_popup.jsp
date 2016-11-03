<%@page import="es.sm2.openppm.core.model.impl.Riskregistertemplate"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectRiskServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Riskregister"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
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
  ~ File: search_template_risks_and_import_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:message key="risk.import_templates" var="titleConfirmMsg"/>
<fmt:message key="risk.msg_import_template" var="confirmMsg"/>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--
var riskAjax = new AjaxCall('<%=ProjectRiskServlet.REFERENCE%>','<fmt:message key="error"/>');

var searchRiskTemplatesTable;

function openSearchTemplatesRisk() {
	$("#searchRiskTemplatesPopup").dialog('open');
}

function searchRiskTemplates() {
	
	var f = document.frmSearchRiskTemplates;
	
	var params = {
		accion: "<%= ProjectRiskServlet.JX_CONS_RISK_TEMPLATES %>",
		<%= Riskregister.RISKNAME %>: f.<%= Riskregister.RISKNAME %>.value
	};
	
	riskAjax.call(params, function(data) {
		searchRiskTemplatesTable.fnClearTable();
			
		$(data).each(function() {
			var template = [
			    this.<%= Riskregistertemplate.IDRISK %>,            
				this.<%= Riskregistertemplate.RISKCODE %>,
				this.<%= Riskregistertemplate.RISKNAME %>,
				typeof this.<%= Riskregistertemplate.RISKTYPE %> === 'undefined'? '': this.<%= Riskregistertemplate.RISKTYPE %>,
				typeof this.<%= Riskregistertemplate.DESCRIPTION %> === 'undefined'? '': this.<%= Riskregistertemplate.DESCRIPTION %>
			];
			
			searchRiskTemplatesTable.fnAddData(template);
		});"src/main/webapp/jsp/project/common/search_projects_and_import_popup.jsp"
	});
	
}

function addRiskTemplatesSelected() {
	
	var selecteds = searchRiskTemplatesTable.fnGetSelectedsData();
	
	var idImportRiskTemplates = "";
	
	$(selecteds).each(function() {
		idImportRiskTemplates = idImportRiskTemplates + this[0] +',';
	});
	
	if(idImportRiskTemplates.length > 0){
		idImportRiskTemplates = idImportRiskTemplates.substring(0, idImportRiskTemplates.length - 1);
	}
	
	if(selecteds == null){
		alertUI('','<fmt:message key="risk.msg_not_templates_imported"/>');
	}
	else {
		confirmUI('${titleConfirmMsg}','${confirmMsg}','<fmt:message key="yes"/>','<fmt:message key="no"/>',
			function() {
				var f = document.forms["frm_project"];
				f.accion.value = "<%= ProjectRiskServlet.IMPORT_RISK_TEMPLATES %>";
				f.idImportRiskTemplates.value = idImportRiskTemplates;
				loadingPopup();
				f.submit();
			}
		);
		
		$("#searchRiskTemplatesPopup").dialog('close');
	}
}

function closeSearchRiskTemplates() { $('#searchRiskTemplatesPopup').dialog('close'); }

readyMethods.add(function() {
	
	$("#searchRiskTemplatesPopup").dialog({
		autoOpen: false,
		width: 600,
		resizable: false,
		modal: true,
		open: function(event, ui) { $('#searchName').focus(); }
	});
	
	searchRiskTemplatesTable = $('#tbSearchRiskTemplates').dataTable({
		"sDom": 'T<"clear">lfrtip',
		"oLanguage": datatable_language,
		"sPaginationType": "full_numbers",
		"bFilter": false,
		"aaSorting": [[ 1, "asc" ]],
		"oTableTools": { "sRowSelect": "multi", "aButtons": [] },
		"iDisplayLength": 10,
		"aoColumns": [
			{ "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
			{ "sClass": "left" },
			{ "sClass": "left" }
		]
	});
	
});
//-->
</script>

<div id="searchRiskTemplatesPopup" class="popup">	
	<fieldset>
		<legend><fmt:message key="templates" /></legend>
		
		<form id="frmSearchRiskTemplates" name="frmSearchRiskTemplates" onsubmit="searchRiskTemplates; return false;">
			<input type="hidden" id="accion" name="accion" value=""/>
			
			<table cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<td>
						<b><fmt:message key="search"/>:</b>
					</td>
					<td>
						<input type="text" name="<%= Riskregister.RISKNAME %>" id="searchName" class="campo" style="width: 225px;">
					</td>
					<%-- 
					<td>
						<select name="<%= Riskregister.RISKTYPE %>" id="<%= Riskregister.RISKTYPE %>" class="campo">
							<option value="-1"><fmt:message key="select_opt" /></option>
							<option value="<%=Constants.RISK_OPPORTUNITY %>"><fmt:message key="risk.type.opportunity" /></option>
							<option value="<%=Constants.RISK_THREAT %>"><fmt:message key="risk.type.threat" /></option>
						</select>
					</td>
					--%>
					<td>
						<div style="text-align: right;">
							<a href="javascript:searchRiskTemplates();" class="boton"><fmt:message key="filter.apply" /></a>
						</div>
					</td>
				</tr>		 		
			</table>
			<div>&nbsp;</div>
			<table id="tbSearchRiskTemplates" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th width="30%"><fmt:message key="risk.code"/></th>
						<th width="20%"><fmt:message key="risk.name" /></th>
						<th width="20%"><fmt:message key="risk.type" /></th>
						<th width="30%"><fmt:message key="risk.description" /></th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</form>		
	</fieldset>	
	<div class="popButtons">
		<a href="javascript:addRiskTemplatesSelected();" class="boton"><fmt:message key="import" /></a>
		<a href="javascript:closeSearchRiskTemplates();" class="boton"><fmt:message key="cancel" /></a>
   	</div>			
</div>