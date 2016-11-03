<%@page import="es.sm2.openppm.core.model.impl.Projectassociation"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
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
  ~ File: dependencies.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
	<c:set var="removeLeadButtons"><img class="link removeLeads" title="<fmt:message key="delete"/>" src="images/delete.jpg"/></c:set>
	<c:set var="removeDependentButtons"><img class="link removeDependences" title="<fmt:message key="delete"/>" src="images/delete.jpg"/></c:set>
</c:if>

<script type="text/javascript">

var stakeholdersTable, leadsTable, dependentsTable;

var dependences = {
	
	getTable: function(element) {
		
		var id = $(element).parents("table").prop("id");
		return (id == "tb_leads"?leadsTable:dependentsTable);
	},
	viewProject: function() {
		
		var dataTable = dependences.getTable(this);
		
		var aData = dataTable.fnGetData(this.parentNode.parentNode.parentNode);
		
		var f = document.frm_project;
		f.id.value = aData[0];
		toTab(0);
	},
	addData: function(dataTable, item) {
		
		var deleteBtns = (dataTable == leadsTable
				?'<nobr>${removeLeadButtons}<img title="<fmt:message key="view"/>" class="link viewProject" src="images/view.png"></nobr>'
				:'<nobr>${removeDependentButtons}<img title="<fmt:message key="view"/>" class="link viewProject" src="images/view.png"></nobr>');
		
		var checkBtn = (dataTable == leadsTable?'updateLeads':'updateDependences');
		
		var row = [
				item[0],
				'false',
				item[1],
				'<input type="checkbox" class="'+checkBtn+'" />',
				deleteBtns
			];
			dataTable.fnAddData(row);
	},
	addLeads: function() {
		
		openSearchProject(leadsTable);
	},
	addDependences: function() {
		
		openSearchProject(dependentsTable);
	},
	removeLeads: function() {
		
		leadsTable.fnDeleteRow(this.parentNode.parentNode.parentNode);
	},
	removeDependences: function() {
		
		dependentsTable.fnDeleteRow(this.parentNode.parentNode.parentNode);
	},
	updateLeads: function() {
		
		var pos = leadsTable.fnGetPosition(this.parentNode.parentNode);
		var aData = leadsTable.fnGetData(pos);
		aData[1] = $(this).is(":checked");
	},
	updateDependences: function() {
		
		var pos = dependentsTable.fnGetPosition(this.parentNode.parentNode);
		var aData = dependentsTable.fnGetData(pos);
		aData[1] = $(this).is(":checked");
	},
	addToResponse: function() {
		
		var leads = new Array();
		var dependens = new Array();
		
		$(leadsTable.fnGetData()).each(function() {
			
			var projectLead = {
					<%=Project.IDPROJECT%> : this[0],
					<%=Projectassociation.UPDATEDATES%> : this[1],
			};
			
			leads.push(projectLead);
		});
		
		$(dependentsTable.fnGetData()).each(function() {
			
			var projectLead = {
					<%=Project.IDPROJECT%> : this[0],
					<%=Projectassociation.UPDATEDATES%> : this[1],
			};
			
			dependens.push(projectLead);
		});
		$("#infoDependens").val($.stringify(dependens));
		$("#infoLeads").val($.stringify(leads));
	}
};

readyMethods.add(function() {
	
	leadsTable = $('#tb_leads').dataTable({
		"oLanguage": datatable_language,
		"bPaginate": false,
		"bAutoWidth": false,
		"aoColumns": [
			{"bVisible": false },							  
		  	{"bVisible": false },
			{"sClass": "left" },
			{"sClass": "center", "bSortable" : false },
			{"sClass": "center", "bSortable" : false, "bVisible": ${op:hasPermissionSetting(settings, user,project.status,tab)} }
     	]
	});
	
	dependentsTable = $('#tb_dependents').dataTable({
		"oLanguage": datatable_language,
		"bPaginate": false,
		"bAutoWidth": false,
		"aoColumns": [
			{"bVisible": false },							  
		  	{"bVisible": false },
			{"sClass": "left" },
			{"sClass": "center", "bSortable" : false },
			{"sClass": "center", "bSortable" : false, "bVisible": ${op:hasPermissionSetting(settings, user,project.status,tab)}  }
     	]
	});
	
	$('.viewProject').live('click',dependences.viewProject);
	$('.updateDependences').live('click',dependences.updateDependences);
	$('.updateLeads').live('click',dependences.updateLeads);
	$('.removeLeads').live('click',dependences.removeLeads);
	$('.removeDependences').live('click',dependences.removeDependences);
	$('#addLeads').on('click',dependences.addLeads);
	$('#addDependences').on('click',dependences.addDependences);
});
</script>

<table style="width: 100%">
	<tr>
		<th style="width: 50%"><fmt:message key="projects.lead"/></th>
		<th><fmt:message key="projects.dependent"/></th>
	</tr>
</table>

<div style="width: 49%; margin-right: 5px;">
	<table id="tb_leads" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th>
				<th width="0%">&nbsp;</th>
				<th width="65%"><fmt:message key="labels.name" /></th>
				<th width="20%"><fmt:message key="update.dates" /></th>
				<th width="5%">
					<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
						<img class="link" id="addLeads"  title="<fmt:message key="add"/>" src="images/add.png"/>
					</c:if>
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="lead" items="${project.projectassociationsForDependent}">
				<tr>
					<td>${lead.projectByLead.idProject}</td>
					<td>${lead.updateDates eq true}</td>
					<td>${tl:escape(lead.projectByLead.projectName)}</td>
					<td>
						<input type="checkbox" class="updateLeads" ${lead.updateDates?'checked':''} />
					</td>
					<td>
						<nobr>
							${removeLeadButtons }
							<img title="<fmt:message key="view"/>" class="link viewProject" src="images/view.png">
						</nobr>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div style="width: 49%; float: right; margin-left: 5px">
	<table id="tb_dependents" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th>
				<th width="0%">&nbsp;</th>
				<th width="65%"><fmt:message key="labels.name" /></th>
				<th width="20%"><fmt:message key="update.dates" /></th>
				<th width="5%">
					<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
						<img class="link" id="addDependences" title="<fmt:message key="add"/>" src="images/add.png"/>
					</c:if>
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="dependent" items="${project.projectassociationsForLead}">
				<tr>
					<td>${dependent.projectByDependent.idProject}</td>
					<td>${dependent.updateDates eq true}</td>
					<td>${tl:escape(dependent.projectByDependent.projectName)}</td>
					<td>
						<input type="checkbox" class="updateDependences" ${dependent.updateDates?'checked':''} />
					</td>
					<td>
						<nobr>
							${removeDependentButtons }
							<img title="<fmt:message key="view"/>" class="link viewProject" src="images/view.png">
						</nobr>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
