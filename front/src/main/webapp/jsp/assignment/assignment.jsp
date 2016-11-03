<%@page import="es.sm2.openppm.front.servlets.AssignmentServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="Visual" prefix="visual" %>
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
  ~ File: assignment.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
    var assignmentAjax = new AjaxCall('<%=AssignmentServlet.REFERENCE%>','<fmt:message key="error"/>');
</script>

<%-- Hidden values --%>
<input type="hidden" id="dateIn" value="<fmt:formatDate value="${dateIn}" pattern="${datePattern}"/>"/>
<input type="hidden" id="dateOut" value="<fmt:formatDate value="${dateOut}" pattern="${datePattern}"/>"/>

<%-- Assignaments --%>
<fmt:message key="menu.assignments" var="titleAssignaments"/>
<visual:panel id="assignaments" title="${titleAssignaments }">
	<jsp:include page="assignment.inc.jsp" flush="true"/>
</visual:panel>

<%-- Capacity planning --%>
<c:set var="capacityPlanningBtn">
	<img id="capacityPlanningToCsv" src="images/csv.png" title="<fmt:message key="msg_to_csv"/>">
</c:set>
<fmt:message key="capacity_planning" var="titleCapacityPlanning"/>
<visual:panel id="capacityPlanning" title="${titleCapacityPlanning }" buttons="${capacityPlanningBtn}">
	<jsp:include page="capacity_planning.inc.jsp" flush="true"/>
</visual:panel>

<%-- Imputations --%>
<c:set var="imputationsBtn">
    <img id="imputationsToCsv" src="images/csv.png" title="<fmt:message key="msg_to_csv"/>">
</c:set>
<fmt:message key="imputations" var="titleImputations"/>
<visual:panel id="imputations" title="${titleImputations}"  buttons="${imputationsBtn}">
    <jsp:include page="imputations.inc.jsp" flush="true"/>
</visual:panel>