<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="Visual" prefix="visual" %>

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
  ~ File: status_report.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:56
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="msg_to_csv" var="msg_to_csv" />
<fmt:message key="msg.error.export_to_csv" var="error_to_csv" />
<fmt:message key="msg.no_export_to_csv" var="msg_no_export_to_csv">
	<fmt:param><fmt:message key="projects"/></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
function color_css(status) {

	switch(status) {
		case 'H': type_css = 'high_importance'; break;
		case 'M': type_css = 'medium_importance'; break;
		case 'L': type_css = 'low_importance'; break;
		case 'N': type_css = 'normal_importance'; break;
		default: type_css = '';
	}
	return type_css;
}

function infoStatusReport(dat) {

	if (typeof dataInfoTable === 'undefined') { loadStatusReport = true; }
	else {
		
		applyInfoSort();
		
		var params = {
			accion: "<%=ProgramServlet.JX_STATUS_REPORT%>",
			property: dataInfoTable.propiedad,
			order: dataInfoTable.orden,
			ids: dataInfoTable.ids,
			page: "${param.page}"
		};
		
		programAjax.call(params,function(data) {
			$('#statusReportTable').html(data);
			
			show('fieldProgStatusReport');
		},'html');
	}
}
readyMethods.add(function () {

	$('#statusReportToCSV').on('click',function(event) {
		event.stopPropagation();
		
		var f = document.forms["frm_projects"];
		f.action = "<%=ProgramServlet.REFERENCE%>";
		f.accion.value = "<%=ProgramServlet.EXPORT_STATUS_REPORT_CSV %>";
		f.ids.value = dataInfoTable.ids;
		if(f.ids.value == "") {
			alertUI("${error_to_csv}", "${msg_no_export_to_csv}");
		}
		else {
			f.submit();	
		}	
	});
	
	$('#statusReportReload').on('click',function(event) {
		event.stopPropagation();
		infoStatusReport();	
	}); 
	
});
//-->
</script>

<fmt:message key="status_report" var="titleStatusReport"/>
<c:set var="btnStatusReport">
	<img id="statusReportToCSV" src="images/csv.png" title="${msg_to_csv}">
	<img id="statusReportReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
</c:set>
<visual:panel id="fieldProgStatusReport" title="${titleStatusReport }" buttons="${btnStatusReport }" callback="infoStatusReport">
	<div id="headerReportButtons" class="right" style="padding-bottom:10px;"></div>
	<div id="statusReportTable"></div>
</visual:panel>