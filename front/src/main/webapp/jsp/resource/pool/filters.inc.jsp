<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
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
  ~ File: filters.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:04
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script language="javascript" type="text/javascript" >
<!--
function applyFilter() {

	actionApplyFilter();
	$.cookie('resource.name', $("filterName").val(), { expires: 365 });

	return false;
}

function resetFilter() {
	
	$("#filterName").val('');
	$.cookie('resource.name', null);
}

function loadFilterState() {
	
	$("#filterName").val($.cookie('resource.name' ) != null?$.cookie('resource.name' ):'');

	actionApplyFilter();
}

//-->
</script>

<fieldset style="margin-bottom: 15px; padding-top:10px; ">
	<table width="100%" cellpadding="1" cellspacing="1">
		<tr>
			<td width="20%"><b><fmt:message key="contact.fullname"/>:</b></td>
			<td width="60%"><input type="text" id="filterName" class="campo"/></td>
			<td width="20%" class="right">
				<input type="submit" class="boton" style="width: 95px;" value="<fmt:message key="filter.apply" />"/>
				<a href="javascript:resetFilter();" class="boton"><fmt:message key="filter.reset" /></a>
			</td>
		</tr>
	</table>
</fieldset>