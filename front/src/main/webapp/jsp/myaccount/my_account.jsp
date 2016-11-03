<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.servlets.MyAccountServlet"%>
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
  ~ File: my_account.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script language="javascript" type="text/javascript" >
var accountAjax = new AjaxCall('<%=MyAccountServlet.REFERENCE%>','<fmt:message key="error" />');

readyMethods.add(function () {
	$('#tabs').tabs();
});

</script>

<div id="tabs">
	<ul>
		<li><a href="#generalTab" ><fmt:message key="settings.general_settings"/></a></li>
		<c:if test="<%=Settings.DEVELOPER %>">
		<li><a href="#tokenTab" ><fmt:message key="token.generate"/></a></li>
		</c:if>
	</ul>
	<div id="generalTab">		
		<jsp:include page="personal_settings.inc.jsp" flush="true" />		
	</div>
	<c:if test="<%=Settings.DEVELOPER %>">
	<div id="tokenTab">		
		<jsp:include page="token_settings.inc.jsp" flush="true" />		
	</div>
	</c:if>
</div>