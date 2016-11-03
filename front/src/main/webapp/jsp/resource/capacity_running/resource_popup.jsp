<%@page import="es.sm2.openppm.core.model.impl.Jobcategory"%>
<%@page import="java.util.Date"%>
<%@page import="es.sm2.openppm.utils.DateUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.model.impl.Skill"%>
<%@page import="es.sm2.openppm.front.servlets.ResourceServlet"%>
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
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--

function closeResource() { $('div#resource-popup').dialog('close'); }

function viewResource(fullName, idEmployee) {

	$('#idEmployee').val(idEmployee);
	$('#legendResource').text(fullName);
	$('#resourceTable').html('');
	$('div#resource-popup').dialog('open');	
	$('#legendResourceCapacityRunning').empty();
}

function showInfo() {

	var f = document.frm_resource;
	f.accion.value = '<%=ResourceServlet.JX_VIEW_CAPACITY_RUNNING_RESOURCE%>';
	f.resourceType.value = ($('#resourceType').is(':checked')?'<%=Project.ENTITY %>':'<%= Jobcategory.ENTITY %>');

	resourceAjax.call($("#frm_resource").serialize(),function(data) {
		
		<%-- Data --%>
		$('#resourceTable').html(data);
		
		<%-- Legend --%>
		$('#legendResourceCapacityRunning').empty();
		
		createLegend('#legendResourceCapacityRunning','<fmt:message key="project" />','#FFFFFF');
		createLegend('#legendResourceCapacityRunning','<fmt:message key="operation" />','#41C159');
		
	},'html');
}

readyMethods.add(function() {
	$('div#resource-popup').dialog({ autoOpen: false, modal: true, width: 800, resizable: false });
});
//-->
</script>

<div id="resource-popup" class="popup" style="overflow: hidden;">
		
	<fieldset style="padding: 10px;">
		<legend id="legendResource"></legend>
		<fmt:message key="msg.info.resource_capacity_group"/>:&nbsp;&nbsp;&nbsp;
		<input type="radio" name="type" id="resourceType" value="<%=Project.ENTITY %>" style="width: 10px;" checked/><fmt:message key="project"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="radio" name="type" value="<%= Jobcategory.ENTITY %>" style="width: 10px;"/><fmt:message key="job_category"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		
		<a href="javascript:showInfo();" class="boton"><fmt:message key="show" /></a>
   	</fieldset>
   	<div id="resourceTable"></div>
   	<div style="margin: 10px;">&nbsp;</div>
 	<div id="legendResourceCapacityRunning" class="legendChart" style="float: left;"></div>
   	<div class="popButtons">
		<a href="javascript:closeResource();" class="boton"><fmt:message key="close" /></a>
   	</div>
</div>