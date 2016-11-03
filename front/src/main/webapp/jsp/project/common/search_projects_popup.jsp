<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.UtilServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject"%>
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
  ~ File: search_projects_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:56
  --%>

<c:set var="is_pmo" value="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PMO) %>" />

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--
var tableForUpdate;
var searchProjectsTable;
var removeButtons;

function openSearchProject(table, btns) {
	
	tableForUpdate = table;
	removeButtons = btns;
	$("#search-projects-popup").dialog('open');
}

function searchProject() {
	
	utilAjax.call($('#frm_search_project').serialize(),function(data){
		
		searchProjectsTable.fnClearTable();
		
		for (var i = 0; i < data.length; i++) {
			
			if ((data[i].<%=Project.IDPROJECT%>+'') != '${project.idProject}') {
				searchProjectsTable.fnAddData([
						data[i].<%=Project.IDPROJECT%>,
						escape(data[i].<%=Project.PROJECTNAME%>),
						escape(data[i].<%=Project.ACCOUNTINGCODE%>),
						escape(data[i].<%=Project.CHARTLABEL%>),
						escape(data[i].<%=Project.PERFORMINGORG%>)
					]);
			}
		}
		
		$('#searchName').focus();
	});
}

function addProjectSelected() {
	var selecteds = searchProjectsTable.fnGetSelectedsData();
	
	$(selecteds).each(function() {
		
		dependences.addData(tableForUpdate, this);
	});
	
	$("#search-projects-popup").dialog('close');
}

function closeSearchProjects() { $('#search-projects-popup').dialog('close'); }

readyMethods.add(function() {
	$("#search-projects-popup").dialog({
		autoOpen: false,
		width: 600,
		resizable: false,
		modal: true,
		open: function(event, ui) { $('#searchName').focus(); }
	});
	
	searchProjectsTable = $('#tb_searchproject').dataTable({
		"sDom": 'T<"clear">lfrtip',
		"oLanguage": datatable_language,
		"sPaginationType": "full_numbers",
		"bFilter": false,
		"oTableTools": { "sRowSelect": "multi", "aButtons": [] },
		"iDisplayLength": 10,
		"aoColumns": [
			{ "bVisible": false },
			{  "sClass": "left" },
			{ "sClass": "left"},
			{  "sClass": "left" },
			{ "bVisible": $('#is_pmo').val() == "true"}
		]
	});
	
});
//-->
</script>

<div id="search-projects-popup" class="popup">	
	<fieldset>
		<legend><fmt:message key="menu.projects" /></legend>
		
		<form id="frm_search_project" name="frm_search_project" onsubmit="searchProject; return false;">
			<input type="hidden" id="accion" name="accion" value="<%=UtilServlet.JX_SEARCH_PROJECTS %>" />
			<input type="hidden" id="is_pmo" name="is_pmo" value="${is_pmo}" />
			
			<table cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<td>
						<b><fmt:message key="search" />:</b>
					</td>
					
					<c:choose>
						<c:when test="${is_pmo}">
							<td>
								<input type="text" name="<%=Project.PROJECTNAME %>" id="searchName" class="campo" style="width: 225px;">
							</td>
							<td>
								<select class="campo" id="perf_org" name="perf_org" style="width: 120px;">
									<option value=""><fmt:message key="select_opt" /></option>
									<c:forEach var="po" items="${performingOrgs}">
										<option value="${po.idPerfOrg}">${tl:escape(po.name)}</option>
									</c:forEach>
								</select>
							</td>
						</c:when>
						<c:otherwise>
							<td colspan="4">
								<input type="text" name="<%=Project.PROJECTNAME %>" id="searchName" class="campo" style="width: 350px;">
							</td>
						</c:otherwise>
					</c:choose>
					
					<td align="right">
						<a href="javascript:searchProject();" class="boton"><fmt:message key="filter.apply" /></a>
					</td>
				</tr>		 		
			</table>
			<div>&nbsp;</div>
			<table id="tb_searchproject" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th width="30%"><fmt:message key="project" /></th>
						<th width="20%"><fmt:message key="project.accounting_code" /></th>
						<th width="20%"><fmt:message key="project.chart_label" /></th>
						<th width="30%"><fmt:message key="perf_organization" /></th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</form>		
	</fieldset>	
	<div class="popButtons">
		<a href="javascript:addProjectSelected();" class="boton"><fmt:message key="add" /></a>
		<a href="javascript:closeSearchProjects();" class="boton"><fmt:message key="cancel" /></a>
   	</div>			
</div>