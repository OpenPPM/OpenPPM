<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@taglib uri="Utilidades" prefix="tl"%>
<%@taglib uri="Visual" prefix="visual"%>

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
  ~ File: procces_log.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request" />

<script type="text/javascript">
<!--
var projectLogTable;

//When document is ready
readyMethods.add(function() {

	projectLogTable = $('#projectLogTable').dataTable({
		"oLanguage": datatable_language,
		"bPaginate": false,
		"aaSorting": [[0, 'desc']],
		"aoColumns": [ 
             { "sClass": "center", sType: "date-euro"},
             { "bVisible": true },
             { "bVisible": true },
             { "bVisible": true }
		]
	});
});

//-->
</script>

<fmt:message var="projectLogTitle" key="project.process_log" />
<visual:panel id="projectLogPanel" title="${projectLogTitle }">

	<table id="projectLogTable" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="20%"><fmt:message key="date" /></th>
				<th width="30%"><fmt:message key="user" /></th>
				<th width="25%"><fmt:message key="project_status" /></th>
				<th width="25%"><fmt:message key="investments.status" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="log" items="${logs}">
				<tr>
					<td><fmt:formatDate value="${log.logDate }"	pattern="${dateTimePattern}" /></td>
					<td>${tl:escape(log.employee.contact.fullName) }</td>
					<td><fmt:message key="project_status.${log.projectStatus }" /></td>
					<td><fmt:message key="investments.status.${log.investmentStatus }" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</visual:panel>

