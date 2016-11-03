<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

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
  ~ File: search_employee_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:46
  --%>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:message key="error" var="fmt_error" />

<%-- 
Request Attributes:
	profiles:		List of Resourceprofiles
	perforgs:		List of Performing Organization
--%>

<script type="text/javascript">
<!--
var searchTable;
var otherFunction;
var idSearchEmployee;
var utilAjax = new AjaxCall('<%=UtilServlet.REFERENCE%>','<fmt:message key="error"/>');

function searchEmployee() {
	var f = document.forms["frm_search_employee"];
	
	var params = {
		accion: "<%=UtilServlet.JX_SEARCH_EMPLOYEE %>",
		name: f.search_name.value,
		jobTitle: f.search_jobtitle.value,
		idProfile: f.serach_profiles.value,
		idPerfOrg: f.serach_perforg.value
	};
	
	utilAjax.call(params, function(data) {
		searchTable.fnClearTable();
		for (var i = 0; i < data.length; i++) {
			searchTable.fnAddData([
					data[i].idEmployee,
					data[i].fullname,
					data[i].perfOrg,
					data[i].jobTitle,
					data[i].company,
					'<img class="icono"  title="<fmt:message key="add"/>" src="images/AddContact.png" onclick="sendEmployee(this);" />'
				]);
		}
	});
}

function sendEmployee(element) {

	var employee = searchTable.fnGetData( element.parentNode.parentNode );

	$('#'+idSearchEmployee).val(employee[0]);
	$('#'+idSearchEmployee+"_name").val(employee[1]);
	
	if (otherFunction != null) { otherFunction(employee); }
	
	closeSearch();
}

function deleteEmployee() {

	$('#'+idSearchEmployee).val("-1");
	$('#'+idSearchEmployee+"_name").val("");

	closeSearch();
}

function searchEmployeePop(id, perfOrg, rol,extraFunction) {
	searchTable.fnClearTable();
	document.forms["frm_search_employee"].reset();
	
	otherFunction = extraFunction;
	
	idSearchEmployee = id;
	$('div#search-employee-popup').dialog('open');
	if (perfOrg > 0) {
		$('#serach_perforg').val(perfOrg);
		$('#serach_perforg').attr('disabled',true);
	}
	else {
		$('#serach_perforg').attr('disabled',false);
	}

	if (rol > 0) {
		$('#serach_profiles').val(rol);
		$('#serach_profiles').attr('disabled',true);
	}
	else {
		$('#serach_profiles').attr('disabled',false);
	}
		
}

function closeSearch() {
	$('div#search-employee-popup').dialog('close');
}
readyMethods.add(function() {

	$('div#search-employee-popup').dialog({ autoOpen: false, modal: true, width: 750, resizable: false,
		open: function(event, ui) { $('#search_name_employee').focus(); }
	});

	$.fn.dataTableExt.oStdClasses.sWrapper = "modified_wrapper";
	
	searchTable = $('#tb_search_employee').dataTable({
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
            { "bVisible": false },
            { "sClass": "center", "bSortable" : false }
     	]
	});

	$("#frm_search_employee").bind("submit", function() { searchEmployee(); return false; });
});
//-->
</script>

<div id="search-employee-popup" class="popup">

	<form name="frm_search_employee" id="frm_search_employee">
		<fieldset>
			<legend><fmt:message key="search_employee"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th width="20%"><fmt:message key="contact.fullname"/></th>
					<th width="20%"><fmt:message key="contact.jobtitle"/></th>
					<th width="24%"><fmt:message key="profile"/></th>
					<th width="24%"><fmt:message key="perforg"/></th>
					<th width="12%">&nbsp;</th>
				</tr>
				<tr>
					<td><input class="campo" type="text" name="search_name" id="search_name_employee"/></td>
					<td><input class="campo" type="text" name="search_jobtitle" /></td>
					<td>
						<select name="serach_profiles" id="serach_profiles" class="campo">
							<option value="-1"><fmt:message key="select_opt"/></option>
							<c:forEach var="profile" items="${profiles }">
								<option value="${profile.idProfile }">${profile.profileName }</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<select name="serach_perforg" id="serach_perforg" class="campo">
							<option value="-1"><fmt:message key="select_opt"/></option>
							<c:forEach var="perfOrg" items="${perforgs}">
								<option value="${perfOrg.idPerfOrg}">${perfOrg.name}</option>
							</c:forEach>
						</select>
					</td>
					<td>
					<input type="submit" class="boton" value="<fmt:message key="search" />" /></td>
				</tr>
				<tr><td colspan="5">&nbsp;</td></tr>
				<tr>
					<td colspan="5">
						<table id="tb_search_employee" class="tabledata" cellspacing="1" >
							<thead>
								<tr>
									<th width="1%">&nbsp;</th>
									<th width="35%"><fmt:message key="contact.fullname"/></th>
									<th width="30%"><fmt:message key="perforg"/></th>
									<th width="30%"><fmt:message key="contact.jobtitle"/></th>
									<th width="0%">&nbsp;</th>
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
			<a href="javascript:deleteEmployee();" class="boton" ><fmt:message key="delete" /></a>
			<a href="javascript:closeSearch();" class="boton" ><fmt:message key="cancel" /></a>
    	</div>
    </form>
</div>