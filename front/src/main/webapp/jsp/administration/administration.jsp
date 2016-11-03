<%@page import="es.sm2.openppm.front.servlets.AdministrationServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

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
  ~ File: administration.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:25
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtValueRequired">
	<fmt:param><b><fmt:message key="setting.value"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
var adminAjax = new AjaxCall('<%=AdministrationServlet.REFERENCE%>','<fmt:message key="error"/>');

readyMethods.add(function() {
	var $tabs = $('#tabs').tabs();
	$tabs.tabs('select', <%= (Integer)request.getAttribute("selectedTab")%>);

	createBT('.btitle');
});
//-->
</script>

<div id="tabs">
	<ul>
		<li><a href="#generalTab"><fmt:message key="settings.general_settings"/></a></li>
		<li><a href="#requiredTab"><fmt:message key="settings.required_fields"/></a></li>
		<li><a href="#emailTab"><fmt:message key="settings.email_settings"/></a></li>
		<li><a href="#security"><fmt:message key="settings.security"/></a></li>
		<li><a href="#visibilityTab"><fmt:message key="settings.visibility_columns"/></a></li>
		<li><a href="#createPo"><fmt:message key="settings.manage_po"/></a></li>
		<li><a href="#createAdmin"><fmt:message key="settings.create_admin"/></a></li>
	</ul>
	<div id="generalTab" style="padding: 1em 1.4em 0em;">		
		<jsp:include page="general_settings.inc.jsp" flush="true" />
        <jsp:include page="customization.inc.jsp" flush="true" />
    </div>
	<div id="emailTab" style="padding: 1em 1.4em 0em;">
		<jsp:include page="email_settings.inc.jsp" flush="true" />
	</div>
	<div id="security" style="padding: 1em 1.4em 0em;">		
		<jsp:include page="security_settings.inc.jsp" flush="true" />		
	</div>
	<div id="visibilityTab" style="padding: 1em 1.4em 0em;">
		<jsp:include page="visibility_settings.inc.jsp" flush="true" />
	</div>
	<div id="createPo" style="padding: 1em 1.4em 0em;">
		<jsp:include page="create_po.inc.jsp" flush="true" />
	</div>
	<div id="createAdmin" style="padding: 1em 1.4em 0em;">
		<jsp:include page="create_admin.inc.jsp" flush="true" />
	</div>
	<div id="requiredTab" style="padding: 1em 1.4em 0em;">
		<jsp:include page="required_settings.inc.jsp" flush="true" />
	</div>
</div>