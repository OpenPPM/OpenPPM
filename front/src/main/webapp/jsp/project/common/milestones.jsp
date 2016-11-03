<%@page import="java.util.Date"%>
<%@page import="es.sm2.openppm.core.model.impl.Milestones.MilestonePending"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Milestones"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@taglib uri="Visual" prefix="visual" %>

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
  ~ File: milestones.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="msg_to_csv" var="msg_to_csv" />
<fmt:message key="msg_to_pdf" var="msg_to_pdf" />

<fmt:message key="msg.error.export_to_csv" var="error_to_csv" />
<fmt:message key="msg.no_export_to_csv" var="msg_no_export_to_csv">
	<fmt:param><fmt:message key="milestones"/></fmt:param>
</fmt:message>

<script>

var programAjax = new AjaxCall('<%=ProgramServlet.REFERENCE%>','<fmt:message key="error" />');

var milestonesTable;

var milestones = {
		
	// Form milestones	
	form: document.forms["frm_milestones"],
	// Create table
	createTable: function() {
		return $('#tb_milestones').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"bInfo": false,
			"aaSorting": [],
			"iDisplayLength": 50,
			"aoColumns": [
			              { "sClass": "left" },
			              { "sClass": "left" },
			              { "sClass": "left" },
			              { "sClass": "left" },
			              { "sClass": "left" },
			              { "sClass": "left" },
			              { "sClass": "center", "sType": "es_date" },	
			              { "sClass": "center", "sType": "es_date" },
			              { "sClass": "center", "sType": "es_date" },	
			              { "sClass": "center", "bSortable": false }
			      		]
		});
	},
	// Show table milestones	
	showTable: function() {

		var idsProjects;
		
		// Get ids projects
		if (typeof dataInfoTable !== 'undefined'){
			idsProjects = dataInfoTable.ids;
		}
		
		if (typeof idsProjects !== 'undefined') {
		
			var params = {
				accion: "<%=ProgramServlet.JX_CONS_MILESTONES%>",
				ids: idsProjects,
				milestoneDateSince: $("#milestoneDateSince").val(),
				milestoneDateUntil: $("#milestoneDateUntil").val(),
				<%= Milestones.MILESTONETYPE %>: $("#" + "<%= Milestones.MILESTONETYPE %>").val(),
				<%= Milestones.MILESTONECATEGORY %>: $("#" + "<%= Milestones.MILESTONECATEGORY %>").val(),
				milestonePending: $("#milestonePending").val()
			};

			programAjax.call(params, function(data) {
		
				if (typeof data.milestones === 'undefined') {
					milestonesTable.fnClearTable();
				}
				else {
					
					milestonesTable.fnClearTable();
					
					var milestonesList = data.milestones;

					for (var i = 0; i < milestonesList.length; i++) {
						
						milestonesTable.fnAddData([
							milestonesList[i].projectName,
							milestonesList[i].projectShortName,
							milestonesList[i].milestoneTypeName,
							milestonesList[i].milestoneCategoryName,
							milestonesList[i].name,
							milestonesList[i].description,
							milestonesList[i].planned,
							milestonesList[i].estimated,
							milestonesList[i].actual,
							'<nobr><img onclick="viewProject(' + milestonesList[i].projectId +
							',\''+ '<%= Constants.STATUS_PLANNING %>' +'\');" title="<fmt:message key="view"/>" class="link" src="images/view.png"></nobr>'
						]);
					}
				}
			});
		}
	},
	// Reset filters
	resetFilters: function() {

        var params1 = {
            accion: "<%=ProgramServlet.JX_SET_DEFAULT_DATES%>"
        };

        programAjax.call(params1, function(data) {

            $("#milestoneDateSince").val(data.milestoneDateSince);
            $("#milestoneDateUntil").val(data.milestoneDateUntil);

        });

        $("#" + "<%= Milestones.MILESTONETYPE %>").val('');
        $("#milestonePending").val('ALL');
        $("#" + "<%= Milestones.MILESTONECATEGORY %>").val('');
	},
	// Export to CSV
	exportToCSV: function() {
		
		var f = this.form;
		
		f.action 					= "<%=ProgramServlet.REFERENCE%>";
		f.accion.value 				= "<%=ProgramServlet.EXPORT_MILESTONES_CSV %>";
		f.ids.value 				= dataInfoTable.ids;
		f.milestoneDateSince.value 	= $("#milestoneDateSince").val();
		f.milestoneDateUntil.value 	= $("#milestoneDateUntil").val();
		f.<%= Milestones.MILESTONETYPE %>.value = $("#" + "<%= Milestones.MILESTONETYPE %>").val();
		
		if(f.ids.value == "" || typeof milestonesTable.fnGetNodes() === 'undefined' || milestonesTable.fnGetNodes().length == 0) {
			alertUI("${error_to_csv}", "${msg_no_export_to_csv}");
		}
		else {
			f.submit();	
		}
	}
};

// Ready methods	
readyMethods.add(function() {
	
	milestonesTable = milestones.createTable();

	$('#tb_milestones tbody tr').live('click', function (event) {		
		milestonesTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('#milestoneDateSince, #milestoneDateUntil').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {}
	});
	
	// Descriptions
	$("#<%= Milestones.MILESTONETYPE %>").selectDescription({width : '130px'});
	$("#<%= Milestones.MILESTONECATEGORY %>").selectDescription({width : '130px'});
	
	// Button export milestones table to CSV
	$('#milestonesToCSV').on('click',function(event) {
		
		event.stopPropagation();
		
		milestones.exportToCSV();
	});
	
	// Button search milestones
	$('#searchMilestones').on('click',function(event) {
		
		event.stopPropagation();
		
		milestones.showTable();
	});
	
	// Button reset filters milestones
	$('#resetFiltersMilestones').on('click',function(event) {
		
		event.stopPropagation();
		
		milestones.resetFilters();
	});
});

</script>	
<fmt:message key="milestones.list" var="titleMilestones"/>
<c:set var="btnMilestones">
	<img id="milestonesToCSV" src="images/csv.png" title="${msg_to_csv}">
	<span style="float:right;" id="milestonesToJasper"></span>
</c:set>
<visual:panel id="milestones" title="${titleMilestones}" buttons="${btnMilestones}">
	<form id="frm_milestones" name="frm_milestones" method="post">
		<input type="hidden" name="accion" value="" />
		<input type="hidden" id="ids" name="ids" />
		
		<div style="padding-top:10px;margin-bottom: 10px;float: left;">
			<%-- Filters dates --%>
			<span style="margin-right:5px;">
				<fmt:message key="dates.since"/>:&nbsp;
				<input type="text" id="milestoneDateSince" name="milestoneDateSince" class="campo fecha alwaysEditable"
					value="<fmt:formatDate value="<%= new Date() %>" pattern="${datePattern}"/>"/>
			</span>
		  	<span style="margin-right:5px;">
				<fmt:message key="dates.until"/>:&nbsp;
				<input type="text" id="milestoneDateUntil" name="milestoneDateUntil" class="campo fecha alwaysEditable"/>
			</span>
			<%-- Filter Milestone type --%>
			<span style="margin-right:5px;">
				<fmt:message key="type"/>:&nbsp;
				<select name="<%= Milestones.MILESTONETYPE %>" id="<%= Milestones.MILESTONETYPE %>" class="campo">
					<option value="" selected><fmt:message key="select_opt"/></option>
					<c:forEach var="milestoneType" items="${milestoneTypes}">
						<option value="${milestoneType.idMilestoneType}" description="${tl:escape(milestoneType.description)}">${tl:escape(milestoneType.name)}</option>
					</c:forEach>
				</select>
			</span>
			<%-- Filter Milestone category --%>
			<span style="margin-right:5px;">
				<fmt:message key="category"/>:&nbsp;
				<select name="<%= Milestones.MILESTONECATEGORY %>" id="<%= Milestones.MILESTONECATEGORY %>" class="campo">
					<option value="" selected><fmt:message key="select_opt"/></option>
					<c:forEach var="milestoneCategory" items="${milestoneCategories}">
						<option value="${milestoneCategory.idMilestoneCategory}" description="${tl:escape(milestoneCategory.description)}">${tl:escape(milestoneCategory.name)}</option>
					</c:forEach>
				</select>
			</span>
			<%-- Filter Milestone pendings --%>
			<span style="margin-right:5px;">
				<fmt:message key="milestone.pendings"/>:&nbsp;
				<select name="milestonePending" id="milestonePending" class="campo" style="width:70px;">
					<% for (MilestonePending milestonePending : MilestonePending.values()) { %>
						<c:choose>
							<c:when test="<%= MilestonePending.ALL.name().equals(milestonePending.name()) %>">
								<option value="<%= milestonePending.name() %>" selected><fmt:message key="<%= milestonePending.getText() %>"/></option>
							</c:when>
							<c:otherwise>
								<option value="<%= milestonePending.name() %>"><fmt:message key="<%= milestonePending.getText() %>" /></option>
							</c:otherwise>
						</c:choose>
					<% } %>
				</select>
			</span>
		</div>
		<div class="popButtons">
			<a id="searchMilestones" class="boton"><fmt:message key="search"/></a>
			<a id="resetFiltersMilestones" class="boton"><fmt:message key="filter.default"/></a>
		</div>
		
		<table id="tb_milestones" class="tabledata" cellspacing="1" style="width:100%">
			<thead>
				<tr>
					<th width="20%"><fmt:message key="project"/></th>
					<th width="10%"><fmt:message key="project.chart_label"/></th>
					<th width="5%"><fmt:message key="milestone.type"/></th>
					<th width="5%"><fmt:message key="milestone.category"/></th>
					<th width="20%"><fmt:message key="milestone.name"/></th>
					<th width="20%"><fmt:message key="milestone.desc"/></th>
					<th width="5%"><fmt:message key="milestone.planned_date"/></th>	
					<th width="5%"><fmt:message key="milestone.due_date"/></th>	
					<th width="5%"><fmt:message key="milestone.actual_date"/></th>	
					<th width="5%"></th>							
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</form>
</visual:panel>