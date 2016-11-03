<%@page import="es.sm2.openppm.core.common.Configurations"%>
<%@page import="es.sm2.openppm.core.model.impl.Category"%>
<%@page import="es.sm2.openppm.utils.DateUtil"%>
<%@page import="java.util.Date"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ResourceServlet"%>
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
  ~ File: capacity_planning.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script language="javascript" type="text/javascript" >
<!--
var resourceAjax = new AjaxCall('<%=ResourceServlet.REFERENCE%>','<fmt:message key="error" />');

<%-- EXPORT CSV --%>
function exportCSV() {

	var f = document.forms.frm_resource; 
	f.accion.value = '<%=ResourceServlet.CAPACITY_PLANNING_TO_CSV%>';
	f.submit();
}

<%-- EXPORT DETAIL CSV --%>
function exportDetailCSV() {

	var f = document.forms.frm_resource; 
	f.accion.value = '<%= ResourceServlet.CAPACITY_PLANNING_DETAIL_TO_CSV %>';
	f.idsEmployees.value = $('#idsEmployees').html();
	
	f.submit();
}

<%-- APPLY FILTER --%>
function actionApplyFilter() {

	var f = document.forms.frm_resource;
	
	f.accion.value = '<%=ResourceServlet.JX_UPDATE_CAPACITY_PLANNING%>';
	
	<%--Set values to Configurations --%>
	if (typeof f.<%=Configurations.PROJECT%> !== 'undefined') {
		f.<%=Configurations.PROJECT%>.value 					= $('#<%= Configurations.PROJECT %>Select').val();
	}
	
	if (typeof f.<%=Configurations.JOBCATEGORY%> !== 'undefined') {
		f.<%=Configurations.JOBCATEGORY%>.value 				= $('#<%= Configurations.JOBCATEGORY %>Select').val();
	}
	
	if (typeof f.<%=Configurations.LIST_FILTERS_PM%> !== 'undefined') {
		f.<%=Configurations.LIST_FILTERS_PM%>.value 			= $('#<%= Configurations.LIST_FILTERS_PM %>Select').val();
	}
	
	if (typeof f.<%=Configurations.LIST_FILTERS_RESOURCE_POOL%> !== 'undefined') {
		f.<%=Configurations.LIST_FILTERS_RESOURCE_POOL%>.value 	= $('#<%= Configurations.LIST_FILTERS_RESOURCE_POOL %>Select').val();
	}
	
	if (typeof f.<%=Configurations.LIST_FILTERS_SELLERS%> !== 'undefined') {
		f.<%=Configurations.LIST_FILTERS_SELLERS%>.value 		= $('#<%= Configurations.LIST_FILTERS_SELLERS %>Select').val();
	}
	
	if (typeof f.<%=Configurations.LIST_FILTERS_CATEGORIES%> !== 'undefined') {
		f.<%=Configurations.LIST_FILTERS_CATEGORIES%>.value 	= $('#<%= Configurations.LIST_FILTERS_CATEGORIES %>Select').val();
	}
	
	resourceAjax.call($("#frm_resource").serialize(),function(data) {
		$('#staffingTable').html(data);
	},'html');
}

readyMethods.add(function () {
	loadFilterState();
});
//-->
</script>

<form id="frm_resource" name="frm_resource" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idEmployee" id="idEmployee" value="" />
	<input type="hidden" name="resourceType" value="" />
	<input type="hidden" name="firstMonthDay" id="firstMonthDay" value="${firstMonthDay}" />
	<input type="hidden" name="lastMonthDay" id="lastMonthDay" value="${lastMonthDay}" />
	<input type="hidden" name="idsEmployees" id="idsEmployeesList" value="" />
	
	<%-- FILTERS --%>
	<c:choose>
		<c:when test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PM) %>">
			<jsp:include page="filters/advancedFilterForPM.jsp" flush="true"/>
		</c:when>
		<c:when test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_RM) %>">
			<jsp:include page="filters/advancedFilterForRM.jsp" flush="true"/>
		</c:when>
		<c:otherwise>
			<jsp:include page="filters/filters.inc.jsp" flush="true"/>
		</c:otherwise>	
	</c:choose>
	
	<%-- PROJECT STAFFING TABLE --%>
	<div id="staffingTable"></div>
	<div style="margin: 10px;">&nbsp;</div>
</form>

<jsp:include page="resource_popup.jsp" flush="true"/>