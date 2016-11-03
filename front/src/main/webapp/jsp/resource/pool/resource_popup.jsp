<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
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
  ~ File: resource_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:04
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script language="javascript" type="text/javascript" >
<!--

function viewResource(element) {

	var aData = resourceTable.fnGetData(element.parentNode.parentNode.parentNode);

	$('#<%=Contact.FULLNAME%>').text(aData[1]);
    $('#<%=Contact.FILEAS%>').text(aData[2]);	
    $('#<%=Contact.JOBTITLE%>').text(aData[3]);
    $('#<%=Contact.BUSINESSPHONE%>').text(aData[4]);
    $('#<%=Contact.MOBILEPHONE%>').text(aData[5]);
    $('#<%=Contact.EMAIL%>').text(aData[6]);
    $('#<%=Contact.BUSINESSADDRESS%>').text(aData[7]);
    $('#<%=Contact.NOTES%>').text(aData[8]);	
	
	$('#resource-popup').dialog('open');
}

function closeResource() { $('#resource-popup').dialog('close'); }

readyMethods.add(function () {
	$('#resource-popup').dialog({ autoOpen: false, modal: true, width: 950, resizable: false });
});
//-->
</script>

<div id="resource-popup">
	<fieldset>
		<legend><fmt:message key="resource"/></legend>
		<table border="0" cellpadding="2" cellspacing="1" width="100%">
			<tr>
				 <th><fmt:message key="maintenance.contact.full_name" /></th>
				 <th><fmt:message key="maintenance.contact.file_as" /></th>
				 <th><fmt:message key="maintenance.contact.job_title" /></th>
			</tr>
			<tr>
				<td id="<%=Contact.FULLNAME%>"></td>
				<td id="<%=Contact.FILEAS%>"></td>
				<td id="<%=Contact.JOBTITLE%>"></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				 <th><fmt:message key="maintenance.contact.business_phone" /></th>
				 <th><fmt:message key="maintenance.contact.mobile_phone" /></th>
				 <th><fmt:message key="maintenance.contact.email" /></th>
			</tr>
			<tr>
				<td id="<%=Contact.BUSINESSPHONE%>"></td>
				<td id="<%=Contact.MOBILEPHONE%>"></td>
				<td id="<%=Contact.EMAIL%>"></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				 <th colspan="3"><fmt:message key="maintenance.contact.business_address" /></th>
			</tr>
			<tr>
				<td colspan="3" id="<%=Contact.BUSINESSADDRESS%>"></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				 <th colspan="3"><fmt:message key="maintenance.contact.notes" /></th>
			</tr>			
			<tr>
				<td colspan="3" id="<%=Contact.NOTES%>"></td>
			</tr>
   		</table>
   	</fieldset>
   	<div class="popButtons">
		<a href="javascript:closeResource();" class="boton"><fmt:message key="close" /></a>   		
   	</div>
</div>