<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.front.servlets.UtilServlet"%>

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
  ~ File: search_contact_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:44
  --%>

<%
Employee user = SecurityUtil.validateLoggedUser(request);
%>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:message key="error" var="fmt_error" />

<%-- 
Request Attributes:
	profiles:		List of Resourceprofiles
	perforgs:		List of Performing Organization
--%>

<script type="text/javascript">
<!--
var searchTableContact;
var idSearchContact;

var utilAjax = new AjaxCall('<%=UtilServlet.REFERENCE%>','<fmt:message key="error"/>');

function searchContact() {
	var f = document.forms["frm_search_contact"];
	
	utilAjax.call($('#frm_search_contact').serialize(), function(data){
		searchTableContact.fnClearTable();
		for (var i = 0; i < data.length; i++) {
			
			searchTableContact.fnAddData([
					data[i].idContact,
					data[i].fullname,
					data[i].fileAs,
					data[i].jobTitle,
					data[i].phone,
					'<img class="icono"  title="<fmt:message key="add"/>" src="images/AddContact.png" onclick="sendContact(this);" />'
				]);
		}
	});
}

function sendContact(element) {
		
	var contact = searchTableContact.fnGetData( element.parentNode.parentNode );
	
	$('#'+idSearchContact).val(contact[0]);
	$('#'+idSearchContact+"_name").val(contact[1]);
	
	closeSearchContact();
}

function searchContactPop(id, fullName) {
	
	searchTableContact.fnClearTable();
	document.forms["frm_search_contact"].reset();
	
	idSearchContact = id;
	
	if (typeof fullName !== 'undefined' && fullName != '') {
		$('#search_name').val(fullName);
		searchContact();
	}
	
	$('div#search-contact-popup').dialog('open');
}

function closeSearchContact() {
	$('div#search-contact-popup').dialog('close');
}
readyMethods.add(function() {

	$('div#search-contact-popup').dialog({ autoOpen: false, modal: true, width: 750, resizable: false,
		open: function(event, ui) { $('#search_name').focus(); }
	});

	$.fn.dataTableExt.oStdClasses.sWrapper = "modified_wrapper";
	
	searchTableContact = $('#tb_search_contact').dataTable({
		"oLanguage": datatable_language,
		"bFilter": false,
		"bInfo": false,
		"bLengthChange": false,
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"aoColumns": [
	        { "bVisible": false }, 
	        { "sClass": "left" },
	        { "sClass": "left" },
	        { "sClass": "left" },
	        { "sClass": "left" },
	        { "sClass": "center", "bSortable" : false }
		]
	});

	$("#frm_search_contact").bind("submit", function() { searchContact(); return false; });
});
//-->
</script>

<div id="search-contact-popup" class="popup">

	<form name="frm_search_contact" id="frm_search_contact">
		<input type="hidden" name="accion" value="<%=UtilServlet.JX_SEARCH_CONTACT%>"/>
		<fieldset>
			<legend><fmt:message key="search_contact"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th width="28%"><fmt:message key="contact.fullname"/></th>
					<th width="28%"><fmt:message key="maintenance.contact.file_as"/></th>
					<th width="32%"><fmt:message key="maintenance.employee.perf_org"/></th>
					<th width="12%">&nbsp;</th>
				</tr>
				<tr>
					<td><input class="campo" type="text" name="search_name" id="search_name"/></td>
					<td><input class="campo" type="text" name="search_fileas" /></td>
					<td>
						<select class="campo" name="search_perforg" id="search_perforg">
							<option value="-1"><fmt:message key="select_opt" /></option>
							<c:forEach var="perfOrg" items="${perforgs}">
								<option value="${perfOrg.idPerfOrg}">${perfOrg.name}</option>
							</c:forEach>
						</select>
					</td>
					<td><input type="submit" class="boton" value="<fmt:message key="search" />" /></td>
				</tr>
				<tr><td colspan="5">&nbsp;</td></tr>
				<tr>
					<td colspan="5">
						<table id="tb_search_contact" class="tabledata" cellspacing="1" >
							<thead>
								<tr>
									<th width="1%">&nbsp;</th>
									<th width="30%"><fmt:message key="maintenance.contact.full_name"/></th>
									<th width="25%"><fmt:message key="maintenance.contact.file_as"/></th>
									<th width="25%"><fmt:message key="contact.jobtitle"/></th>
									<th width="15%"><fmt:message key="maintenance.contact.mobile_phone"/></th>
									<th width="5%">&nbsp;</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</td>
				</tr>
    		</table>
    	</fieldset>
   	   	<div class="popButtons">
			<a href="javascript:closeSearchContact();" class="boton" ><fmt:message key="cancel" /></a>
    	</div>
    </form>
</div>