<%@page import="es.sm2.openppm.front.servlets.AssignmentServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Timesheet"%>
<%@page import="es.sm2.openppm.front.servlets.TimeSheetServlet"%>
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
  ~ File: assignment.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="allow_working_non_labor" var="msgNonLaborDay"/>
<fmt:message key="restore_calendar_exceptions" var="msgRestoreCalendar"/>

<c:set var="statusInit" value="<%=Constants.STATUS_INITIATING %>"/>
<c:set var="statusPlan" value="<%=Constants.STATUS_PLANNING %>"/>
<c:set var="statusControl" value="<%=Constants.STATUS_CONTROL %>"/>
<c:set var="statusClosed" value="<%=Constants.STATUS_CLOSED %>"/>
<c:set var="statusArchived" value="<%=Constants.STATUS_ARCHIVED %>"/>

<script type="text/javascript">
<!--
var assignmentTable;

var assignament = {
		
	dateFilter : function()  {
		var f = document.forms["frm_assignment"];
		f.accion.value = "<%=AssignmentServlet.FILTER_ASSIGNMENTS%>";
		f.submit();
	},
	resetFilter : function() {
		$("#projectFilter").val('');
		$("#since").val('');
		$("#until").val('');
		$("#projectStatusFilter").val('');
		
		assignmentTable.fnResetAllFilters();
	}
};


readyMethods.add(function() {
	
	assignmentTable = $('#tb_assignment').dataTable({
		"oLanguage": datatable_language,
		"bInfo": false,
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"aoColumns": [
			{ "bVisible": false },
			{ "bVisible": false },
			{ "sClass": "left"},
			{ "sClass": "left"},
			{ "sClass": "center"},
			{ "sClass": "center"},
			{ "sClass": "right"}			
     	],
     	"fnDrawCallback": function ( oSettings ) {
            if ( oSettings.aiDisplay.length == 0 ) {return;}
            var nTrs = $('#tb_assignment tbody tr');
            var iColspan = nTrs[0].getElementsByTagName('td').length;
            var sLastGroup = "";
            for ( var i=0 ; i<nTrs.length ; i++ ) {
                var iDisplayIndex = oSettings._iDisplayStart + i;
                var sGroup = oSettings.aoData[ oSettings.aiDisplay[iDisplayIndex] ]._aData[0];
                if ( sGroup != sLastGroup ) {
                    var nGroup = document.createElement( 'tr' );
                    var nCell = document.createElement( 'td' );
                    nCell.colSpan = iColspan;
                    nCell.className = "groupRow";
                    nCell.innerHTML = sGroup;
                    nGroup.appendChild( nCell );
                    nTrs[i].parentNode.insertBefore( nGroup, nTrs[i] );
                    sLastGroup = sGroup;
                }
            }
        },
        "aoColumnDefs": [
			{ "bVisible": false, "aTargets": [ 0 ] }
		],
		"aaSortingFixed": [[ 0, 'asc' ]],
		"aaSorting": [[ 2, 'asc' ]],
        "sDom": 'lfr<"giveHeight"t>ip'
	});
	
	$('#since, #until').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {			
			assignament.dateFilter();
		}
	});
	
	$("#projectFilter").change( function() {
		assignmentTable.fnFilter($("#projectFilter").val(),1);
	});
	
	$("#projectStatusFilter").change( function() {
		assignmentTable.fnFilter($("#projectStatusFilter").val(),2);
	});
	
	$('#since').keyup( function() { if($('#since').val().length == 10 || $('#since').val().length == 0) {assignament.dateFilter();} } );
	$('#until').keyup( function() { if($('#until').val().length == 10 || $('#until').val().length == 0) {assignament.dateFilter();} } );
		
	assignmentTable.fnResetAllFilters();
	
	$("#resetFilterAssignament").on('click',assignament.resetFilter);
});

//-->
</script>

<form name="frm_assignment" id="frm_assignment" action="<%=AssignmentServlet.REFERENCE%>" method="post">
	
	<input type="hidden" name="accion" value="" />	
	
	<fieldset style="margin-bottom: 15px; padding-top: 10px; padding-bottom: 5px;">
		<div class="hColor"><fmt:message key="filter"/></div>
		<table width="100%" border="0" cellpadding="7" cellspacing="1">
			<tr>
				<th width="5%" align="left">
					<fmt:message key="project"/>:
				</th>
				<td width="24%">
					<select id="projectFilter" class="campo" style="width:100%;">
						<option value="" selected><fmt:message key="select_opt"/></option>
						<c:forEach var="project" items="${projects}">
							<option value="${project.idProject}">${project.projectName}</option>
						</c:forEach>
					</select>
				</td>
				
				<th width="14%" align="right">
					<fmt:message key="project_status"/>:
				</th>
				<td width="12%">
					<select id="projectStatusFilter" class="campo" style="width:100%;">
						<option value="" selected><fmt:message key="select_opt"/></option>
						<option value="${statusInit}"><fmt:message key="project_status.${statusInit}" /></option>
						<option value="${statusPlan}"><fmt:message key="project_status.${statusPlan}" /></option>
						<option value="${statusControl}"><fmt:message key="project_status.${statusControl}" /></option>
						<option value="${statusClosed}"><fmt:message key="project_status.${statusClosed}" /></option>
                        <option value="${statusArchived}"><fmt:message key="project_status.${statusArchived}" /></option>
					</select>
				</td>
				
				<th width="8%" align="right">
					<fmt:message key="dates"/>:
				</th>
				<td width="23%">
					<span style="margin-right:5px;">
						<fmt:message key="dates.since"/>:&nbsp;
						<input type="text" id="since" name="since" class="campo fecha" value="<fmt:formatDate value="${dateIn}" pattern="${datePattern}"/>" maxlength="10" />
		       		</span>
		    		<span style="margin-right:5px;">
						<fmt:message key="dates.until"/>:&nbsp;
						<input type="text" id="until" name="until" class="campo fecha" value="<fmt:formatDate value="${dateOut}" pattern="${datePattern}" />" maxlength="10" />
					</span>
				</td>
			</tr>
		</table>
		<div class="popButtons">
			<input type="button" class="boton" id="resetFilterAssignament" value="<fmt:message key="filter.reset" />" />
		</div>
	</fieldset>
				
	<table id= "tb_assignment" width="100%" class="tabledata" cellspacing="1">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th>					
				<th width="0%">&nbsp;</th>
				<th width="45%"><fmt:message key="project"/></th>
				<th width="15%"><fmt:message key="project_manager"/></th>					
				<th width="10%"><fmt:message key="team_member.date_in"/></th>
				<th width="10%"><fmt:message key="team_member.date_out"/></th>
				<th width="10%"><fmt:message key="team_member.fte"/></th>					
			</tr>
		</thead>
		<tbody>
			<c:forEach var="member" items="${teamMembers}">
				<tr>
					<td>${member.projectactivity.project.projectName}</td>
					<td>${member.projectactivity.project.idProject}</td>
					<td>${member.projectactivity.activityName}</td>
					<td>${member.projectactivity.project.employeeByProjectManager.contact.fullName}</td>												
					<td><fmt:formatDate value="${member.dateIn}" pattern="${datePattern}"/></td>
					<td><fmt:formatDate value="${member.dateOut}" pattern="${datePattern}"/></td>
					<td>${member.fte}%</td>					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div>&nbsp;</div>
</form>