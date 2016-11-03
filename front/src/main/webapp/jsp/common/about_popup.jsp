<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
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
  ~ File: about_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:44
  --%>

<c:choose>
	<c:when test="${locale == null  }">
		<fmt:setLocale value="<%=Constants.DEF_LOCALE %>" scope="request"/>
	</c:when>
	<c:otherwise>
		<fmt:setLocale value="${locale}" scope="request"/>
	</c:otherwise>
</c:choose>

<script type="text/javascript">
<!--
function aboutPopup() {
	$('#about-popup').dialog('open');
}

readyMethods.add(function() {
	$('div#about-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false
	});
	
	$('div#about-popup').dialog('option', 'title', "About");
});
//-->
</script>

<c:if test="${scrollTop ne null }">
<script type="text/javascript">
<!--
readyMethods.add(function() { setTimeout("$(document).scrollTop(${scrollTop})",500); });
//-->
</script>
</c:if>

<div id="about-popup" class="popup">
	Copyright (C) 2010  SM2 BALEARES<br />
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
	<br /><br />
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
	<br /><br />
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
    <br /><br />
    Contact us in
    <a href="http://openppm.sourceforge.net/"><fmt:message key="about.web" /></a><br />
	<a href="mailto:info@open-ppm.org">info@open-ppm.org</a><br /><br />
	<fmt:message key="about.subsidized" /><br /><br />
	<img src="images/gplv3.png" />
</div>