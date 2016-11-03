<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.model.impl.Jobcategory"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ResourceServlet"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>
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

<fmt:message key="project.status_initiating" var="msgProjectStatusInitiating"/>
<fmt:message key="resource.assigned" var="msgAssigned"/>
<fmt:message key="<%=Settings.SettingType.CAPACITY_PLANNING_LEVEL2.getMessage()%>" var="msgLevel2"/>
<fmt:message key="<%=Settings.SettingType.CAPACITY_PLANNING_LEVEL3.getMessage()%>" var="msgLevel3"/>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<%-- Legend--%>
<c:set var="msgLevel1">
    <fmt:message key="<%=Settings.SettingType.CAPACITY_PLANNING_LEVEL1.getMessage()%>"/>:&nbsp;<%=SettingUtil.getString(settings, Settings.SettingType.CAPACITY_PLANNING_LEVEL1)%>%&nbsp;&nbsp;
</c:set>
<c:set var="msgLevel2">
    <fmt:message key="<%=Settings.SettingType.CAPACITY_PLANNING_LEVEL2.getMessage()%>"/>:&nbsp;<%=SettingUtil.getString(settings, Settings.SettingType.CAPACITY_PLANNING_LEVEL2)%>%&nbsp;&nbsp;
</c:set>
<c:set var="msgLevel3">
    <fmt:message key="<%=Settings.SettingType.CAPACITY_PLANNING_LEVEL3.getMessage()%>"/>:&nbsp;<%=SettingUtil.getString(settings, Settings.SettingType.CAPACITY_PLANNING_LEVEL3)%>%&nbsp;&nbsp;
</c:set>
<c:set var="msgLevel4">
    <fmt:message key="out_of_range"/>
</c:set>

<script type="text/javascript">
<!--

function closeResource() { $('div#resource-popup').dialog('close'); }

function viewResource(fullName, idEmployee) {

	$('#idEmployee').val(idEmployee);
	$('#legendResource').text(fullName);
	$('#resourceTable').html('');
	
	// Reset legends
	$('.legendChartPopup').empty();

	$('div#resource-popup').dialog('open');	
}

function showInfo() {

	var f = document.frm_resource;
	f.accion.value = '<%=ResourceServlet.JX_VIEW_CAPACITY_PLANNING_RESOURCE%>';
	f.resourceType.value = ($('#resourceType').is(':checked')?'<%=Project.ENTITY %>':'<%= Jobcategory.ENTITY %>');

	resourceAjax.call($("#frm_resource").serialize(),function(data) {
		$('#resourceTable').html(data);
	},'html');
	
	<%-- Legends --%>
	$('.legendChartPopup').empty();
	
	if ($('#resourceType').is(':checked')) {
		createLegend("#legendPopup1", "${msgProjectStatusInitiating}", "orange", "black");
	}
	
	createLegend("#legendPopup2", "${msgAssigned}", "#AEEFAE", "black");
    createLegend("#legendPopup3", "${msgLevel1}", "<%=SettingUtil.getString(settings, Settings.SettingType.CAPACITY_PLANNING_LEVEL1_COLOR)%>", "black");
    createLegend("#legendPopup4", "${msgLevel2}", "<%=SettingUtil.getString(settings, Settings.SettingType.CAPACITY_PLANNING_LEVEL2_COLOR)%>", "black");
    createLegend("#legendPopup5", "${msgLevel3}", "<%=SettingUtil.getString(settings, Settings.SettingType.CAPACITY_PLANNING_LEVEL3_COLOR)%>", "black");
    createLegend("#legendPopup6", "${msgLevel4}", "<%=SettingUtil.getString(settings, Settings.SettingType.CAPACITY_PLANNING_OUTOFRANGE_COLOR)%>", "black");
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
   	<%-- Legends --%>
	<div id="legendPopup1" class="legendChart legendChartPopup" style="float: left;"></div>
    <div id="legendPopup2" class="legendChart legendChartPopup" style="float: left;"></div>
    <div id="legendPopup3" class="legendChart legendChartPopup" style="float: left;"></div>
    <div id="legendPopup4" class="legendChart legendChartPopup" style="float: left;"></div>
    <div id="legendPopup5" class="legendChart legendChartPopup" style="float: left;"></div>
    <div id="legendPopup6" class="legendChart legendChartPopup" style="float: left;"></div>

	<%-- Buttons --%>
   	<div class="popButtons" style="float:right;">
		<a href="javascript:closeResource();" class="boton"><fmt:message key="close" /></a>
   	</div>
</div>