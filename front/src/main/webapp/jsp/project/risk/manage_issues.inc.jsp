<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectRiskServlet"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
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
  ~ File: manage_issues.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="error" var="fmt_error" />
<fmt:message key="data_not_found" var="data_not_found" />

<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_issue">
	<fmt:param><fmt:message key="issue"/></fmt:param>
</fmt:message>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="editIssue"><img onclick="return editIssue(this);" title="<fmt:message key="edit"/>" class="link" src="images/view.png"></c:set>
</c:if>

<c:set var="priorityH"><%=Constants.PRIORITY_HIGH%></c:set>
<c:set var="priorityM"><%=Constants.PRIORITY_MEDIUM%></c:set>
<c:set var="priorityL"><%=Constants.PRIORITY_LOW%></c:set>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtRaiseDateRequired">
	<fmt:param><b><fmt:message key="issue.raise_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtDueDateRequired">
	<fmt:param><b><fmt:message key="issue.due_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtassignedToRequired">
	<fmt:param><b><fmt:message key="issue.assigned_to"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtRatingRequired">
	<fmt:param><b><fmt:message key="issue.rating"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtRaiseDateFormat">
	<fmt:param><b><fmt:message key="issue.raise_date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message><fmt:message key="msg.error.invalid_format" var="fmtDueDateFormat">
	<fmt:param><b><fmt:message key="issue.due_date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message><fmt:message key="msg.error.invalid_format" var="fmtCloseDateFormat">
	<fmt:param><b><fmt:message key="issue.close_date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
//Cost editable?
var issuesTable;
var validatorIssue;

function editIssue(element) {
	
	var issue = issuesTable.fnGetData( element.parentNode.parentNode.parentNode );

	var f = document.forms["frm_issue"];
	f.reset();
	f.issue_id.value = issue[0];
	f.priority.value = issue[2];
	f.date_logged.value = issue[1];
	f.target_date.value = issue[3];
	f.assigned_to.value = unEscape(issue[6]);		
	f.date_closed.value = issue[4];
    f.owner.value = unEscape(issue[11]);

	$('#issue-popup').dialog('open');
	
	$("#issueDescription").text(unEscape(issue[8]));	
	$("#resolution").text(unEscape(issue[9]));

}

function addIssue() {
	$('#issue-popup').dialog('open');
	
	var f = document.forms["frm_issue"];
	f.reset();
	f.issue_id.value = "";
	$("#resolution").text("");
	$("#issueDescription").text("");
}

function saveIssue() {
	var f = document.forms["frm_issue"];

	if (validatorIssue.form()) {
		var idIssue = f.issue_id.value;
		
		var params = {
			accion: "<%=ProjectRiskServlet.JX_SAVE_ISSUE%>",
			id: document.forms["frm_project"].id.value,
			issue_id: idIssue,
			priority: f.priority.value,
			date_logged: f.date_logged.value,
			target_date: f.target_date.value,
			assigned_to: f.assigned_to.value,
			description: f.description.value,
			resolution: f.resolution.value,
			date_closed: f.date_closed.value,
            owner: f.owner.value
		};

		// Save milestone by Ajax Call
		riskAjax.call(params, function (data) {
				var dataRow = [
				    data.id,
					data.date_logged,
					data.priority,
					data.target_date,
					data.date_closed,
					data.status,
					escape(data.assigned_to),
					data.priority_desc,
                    escape(data.description),
					escape(data.resolution),
					'<nobr>${editIssue}' + 
					'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
					'<img src="images/delete.jpg" class="link" onclick="deleteIssue(event, '+ data.id +');" /></nobr>',
                    escape(data.owner)
	            ];
					
				if (idIssue == '') { issuesTable.fnAddDataAndSelect(dataRow); }
				else { issuesTable.fnUpdateAndSelect(dataRow); }
				
				f.reset();

				$('#issue-popup').dialog('close');
		});
	} 
}

function deleteIssueConfirmed() {
	$('#dialog-confirm').dialog("close"); 
	var f = document.forms["frm_project"];
	f.accion.value = "<%=ProjectRiskServlet.DELETE_ISSUE%>";
	f.submit();
}

function deleteIssue(e, id) {
	var target = getTargetFromEvent(e);

	confirmUI('', '${msg_confirm_delete_issue}', 
			"${msg_yes}", "${msg_no}", 
			deleteIssueConfirmed);
	document.forms["frm_project"].issue_id.value = id;
}

readyMethods.add(function() {
	issuesTable = $('#tb_issues').dataTable({
		"oLanguage": datatable_language,
		"bFilter": true,
		"bInfo": true,
		"bPaginate": true,
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"aoColumns": [
		              { "bVisible": false },
		              { "sClass": "center", "sType": "es_date" },
		              { "bVisible": false },
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" },
		              { "bVisible": true },
		              { "bVisible": false },
		              { "sClass": "left" },
		              { "sClass": "left" }, 
		              { "sClass": "left" }, 
		              { "sClass": "center", "bSortable": false },
                      { "bVisible": false }
		      		]
	});	
	
	$('#tb_issues tbody tr').live('click', function (event) {		
		issuesTable.fnSetSelectable(this,'selected_internal');
	} );

	//Validate required fields
	validatorIssue = $("#frm_issue").validate({
		errorContainer: 'div#issue-errors',
		errorLabelContainer: 'div#issue-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#issue-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			date_logged: {required: true, date: true },
			target_date: {required: true, date: true },
			assigned_to: {required: true },
			priority: {required: true },
			date_closed: { date: true },
			description : { maxlength : 3000 },
			resolution  : { maxlength : 3000 },
            owner  : { maxlength : 100 }
		},
		messages: {
			date_logged: {required: '${fmtRaiseDateRequired}', date: '${fmtRaiseDateFormat}' },
			target_date: {required: '${fmtDueDateRequired}', date: '${fmtDueDateFormat}' },
			assigned_to: {required: '${fmtassignedToRequired}' },
			priority: {required: '${fmtRatingRequired}' },
			date_closed: { date: '${fmtCloseDateFormat}' }
		}
	});
		
	$('div#issue-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 550, 
		minWidth: 550, 
		resizable: false,
		open: function(event, ui) { validatorIssue.resetForm(); }
	});
});
//-->
</script>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<script type="text/javascript">
	<!--
		readyMethods.add(function() {
			$('#issue_date_logged').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,				
				onSelect: function() {
					if (validatorIssue.numberOfInvalids() > 0) validatorIssue.form();
				}
			});

			$('#issue_target_date').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,
				onSelect: function() {
					if (validatorIssue.numberOfInvalids() > 0) validatorIssue.form();
				}
			});

			$('#issue_date_closed').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,
				onSelect: function() {
					if (validatorIssue.numberOfInvalids() > 0) validatorIssue.form();
				}
			});
		});
	//-->
	</script>
</c:if>

<fmt:message key="issue_log" var="titleIssuesLog"/>
<visual:panel id="field_issue_controlling" title="${titleIssuesLog }">
	<table id="tb_issues" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th>
				<th width="8%"><fmt:message key="issue.raise_date"/></th>
				<th width="0%">&nbsp;</th>
				<th width="8%"><fmt:message key="issue.due_date"/></th>
				<th width="8%"><fmt:message key="issue.close_date"/></th>
				<th width="6%"><fmt:message key="issue.status"/></th>
				<th width="0%"><fmt:message key="issue.assigned_to"/></th>
				<th width="6%"><fmt:message key="issue.rating"/></th>
				<th width="28%"><fmt:message key="issue.description"/></th>
				<th width="28%"><fmt:message key="issue.resolution"/></th>
				<th width="8%">
					<c:if test="${not op:isStringEquals(project.status, status_closed) and op:hasPermission(user,project.status,tab)}">
						<img onclick="addIssue();" title="<fmt:message key="add"/>" class="link" src="images/add.png">				
					</c:if>
				</th>
                <th width="0%"><fmt:message key="issue.owner"/></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="issue" items="${issues}">
				<tr>
					<td>${issue.idIssue }</td>
					<td><fmt:formatDate value="${issue.dateLogged }" pattern="${datePattern}" /></td>
					<td>${issue.priority }</td>
					<td><fmt:formatDate value="${issue.targetDate }" pattern="${datePattern}" /></td>
					<td><fmt:formatDate value="${issue.dateClosed }" pattern="${datePattern}" /></td>
					<td>
						<c:choose>
							<c:when test="${not empty issue.dateClosed }"><fmt:message key="issue_closed"/></c:when>
							<c:otherwise><fmt:message key="issue_opened" /></c:otherwise>
						</c:choose>
					</td>
					<td>${tl:escape(issue.assignedTo)}</td>
					<td>
						<c:if test="${op:isCharEquals(issue.priority, priorityH)}"><fmt:message key="priority.high" /></c:if>
						<c:if test="${op:isCharEquals(issue.priority, priorityM)}"><fmt:message key="priority.medium" /></c:if>
						<c:if test="${op:isCharEquals(issue.priority, priorityL)}"><fmt:message key="priority.low" /></c:if>
					</td>
					<td>${tl:escape(issue.description)}</td>
					<td>${tl:escape(issue.resolution)}</td>
					<td>
						<nobr>
							<c:if test="${not op:isStringEquals(project.status, status_closed)}">
								<img onclick="editIssue(this);" title="<fmt:message key="edit"/>" class="link" src="images/view.png">
								&nbsp;&nbsp;&nbsp;
							</c:if>
							<c:if test="${op:hasPermission(user,project.status,tab)}">
								<img src="images/delete.jpg" class="link" onclick="deleteIssue(event, ${issue.idIssue});" />
							</c:if>
						</nobr>							
					</td>
                    <td>${tl:escape(issue.owner)}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>		
</visual:panel>
