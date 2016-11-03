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
  ~ File: search_program_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:45
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
var searchTableProgram;
var idSearchProgram;
var utilAjax = new AjaxCall('<%=UtilServlet.REFERENCE%>','<fmt:message key="error"/>');

function searchProgram() {
	var f = document.forms["frm_search_program"];
	
	
	var params = {
		accion: "<%= UtilServlet.JX_SEARCH_PROGRAM %>",
		name: f.search_name.value,
		idPerfOrg: f.search_perforg.value
	};
	
	utilAjax.call(params, function(data) {
		searchTableProgram.fnClearTable();
		
		for (var i = 0; i < data.length; i++) {
			searchTableProgram.fnAddData([
					data[i].idProgram,
					data[i].programName,
					data[i].perfOrg,
					'<img class="icono"  title="<fmt:message key="add"/>" src="images/AddContact.png" onclick="sendProgram(this);" />'
				]);
		}
	});
	
}

function sendProgram(element) {
	var program = searchTableProgram.fnGetData( element.parentNode.parentNode );
	
	$('#'+idSearchProgram).val(program[0]);
	$('#'+idSearchProgram+"_name").val(program[1]);
	
	closeSearchProgram();
	
}

function searchProgramPop(id,perfOrg) {
	searchTableProgram.fnClearTable();
	document.forms["frm_search_program"].reset();
	
	idSearchProgram = id;
	
	$('div#search-program-popup').dialog('open');
	if (perfOrg > 0) {
		$('#search_perforg').val(perfOrg);
		$('#search_perforg').attr('disabled',true);
	}
}

function closeSearchProgram() {
	$('div#search-program-popup').dialog('close');
}

function deleteProgram() {

	$('#'+idSearchProgram).val("-1");
	$('#'+idSearchProgram+"_name").val("");

	closeSearchProgram();
}

readyMethods.add(function() {

	$('div#search-program-popup').dialog({ autoOpen: false, modal: true, width: 750, resizable: false,
		open: function(event, ui) { $('#search_name').focus(); }
	});
	
	$.fn.dataTableExt.oStdClasses.sWrapper = "modified_wrapper";
	
	searchTableProgram = $('#tb_search_program').dataTable({
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
            { "sClass": "center", "bSortable" : false }
     	]
	});

	$("#frm_search_program").bind("submit", function() { searchProgram(); return false; });
});
//-->
</script>

<div id="search-program-popup" class="popup">

	<form name="frm_search_program" id="frm_search_program">
		<fieldset>
			<legend><fmt:message key="search_program"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th width="40%"><fmt:message key="program.name"/></th>
					<th width="25%"><fmt:message key="perforg"/></th>
					<th width="35%">&nbsp;</th>
				</tr>
				<tr>
					<td><input class="campo" type="text" name="search_name" id="search_name"/></td>
					<td>
						<select name="search_perforg" id="search_perforg" class="campo">
							<option value="-1"><fmt:message key="select_opt"/></option>
							<c:forEach var="perfOrg" items="${perforgs}">
								<option value="${perfOrg.idPerfOrg}">${perfOrg.name}</option>
							</c:forEach>
						</select>
					</td>
					<td align="right">
						<input type="submit" class="boton" value="<fmt:message key="search" />" />
					</td>
				</tr>
				<tr><td colspan="5">&nbsp;</td></tr>
				<tr>
					<td colspan="5">
						<table id="tb_search_program" class="tabledata" cellspacing="1" >
							<thead>
								<tr>
									<th width="0%">&nbsp;</th>
									<th width="55%"><fmt:message key="program"/></th>
									<th width="40%"><fmt:message key="perforg"/></th>
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
   	   		<a href="javascript:deleteProgram();" class="boton" ><fmt:message key="delete" /></a>
			<a href="javascript:closeSearchProgram();" class="boton" ><fmt:message key="cancel" /></a>
    	</div>
    </form>
</div>