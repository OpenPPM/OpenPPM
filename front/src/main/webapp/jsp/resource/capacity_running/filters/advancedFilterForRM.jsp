<%@page import="es.sm2.openppm.front.utils.RequestUtil"%>
<%@page import="es.sm2.openppm.core.common.Configurations"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
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
  ~ File: advancedFilterForRM.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script language="javascript" type="text/javascript" >
<!--
function applyFilter() {
	
	actionApplyFilter();
	
	$.cookie('resourceRunning.name', $("#<%=Contact.FULLNAME%>").val(), { expires: 365 });
	$.cookie('resourceRunning.since', $("#since").val(), { expires: 365 });
	$.cookie('resourceRunning.until', $("#until").val(), { expires: 365 });
	$.cookie('resourceRunning.orderName', $("#orderName").val(), { expires: 365 });

	return false;
}

function resetFilter() {
	
	$("#<%=Contact.FULLNAME%>").val('');

	<%-- First and last day of the month --%>
	$('#since').val($('#firstMonthDay').val());
	$("#until").val($('#lastMonthDay').val());
	
	$("#orderName").val('');

	<%-- Configurations --%>
	$('#<%= Configurations.PROJECT %>Select').val('');
	$('#<%= Configurations.JOBCATEGORY %>Select').val('');
	$('#<%= Configurations.LIST_FILTERS_PM %>Select').val('');
	$('#<%= Configurations.LIST_FILTERS_RESOURCE_POOL %>Select').val('');
	$('#<%= Configurations.LIST_FILTERS_SELLERS %>Select').val('');
	$('#<%= Configurations.LIST_FILTERS_CATEGORIES %>Select').val('');
	
	$.cookie('resourceRunning.name', null);
	$.cookie('resourceRunning.since', null);
	$.cookie('resourceRunning.until', null);
	$.cookie('resourceRunning.orderName', null);
}

function loadFilterState() {
	
	$("#<%=Contact.FULLNAME%>").val($.cookie('resourceRunning.name' ) != null?$.cookie('resourceRunning.name' ):'');
	$("#since").val($.cookie('resourceRunning.since') != null?$.cookie('resourceRunning.since'):'');
	$("#until").val($.cookie('resourceRunning.until') != null?$.cookie('resourceRunning.until'):'');
	$("#orderName").val($.cookie('resourceRunning.orderName') != null?$.cookie('resourceRunning.orderName'):'');
	
	<%-- Configurations --%>
	loadMultiple('<%= Configurations.PROJECT %>Select','<%= RequestUtil.getConfiguration(request, Configurations.PROJECT) %>');
	loadMultiple('<%= Configurations.LIST_FILTERS_PM %>Select','<%= RequestUtil.getConfiguration(request, Configurations.LIST_FILTERS_PM) %>');
	loadMultiple('<%= Configurations.JOBCATEGORY %>Select','<%= RequestUtil.getConfiguration(request, Configurations.JOBCATEGORY) %>');
	loadMultiple('<%= Configurations.LIST_FILTERS_RESOURCE_POOL %>Select','<%= RequestUtil.getConfiguration(request, Configurations.LIST_FILTERS_RESOURCE_POOL) %>');
	loadMultiple('<%= Configurations.LIST_FILTERS_SELLERS %>Select','<%= RequestUtil.getConfiguration(request, Configurations.LIST_FILTERS_SELLERS) %>');
	loadMultiple('<%= Configurations.LIST_FILTERS_CATEGORIES %>Select','<%= RequestUtil.getConfiguration(request, Configurations.LIST_FILTERS_CATEGORIES) %>');
}

function advancedFilter() { $('#advancedFilter').toggle('blind'); }

readyMethods.add(function () {
	
	var sinceUntil = $('#since, #until').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "since" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			sinceUntil.not(this).datepicker("option", option, date);
		}
	});
});
//-->
</script>

<fieldset style="margin-bottom: 15px; padding-top:10px; ">
	<table width="100%" cellpadding="1" cellspacing="1">
		<tr>
			<td>
				<b><fmt:message key="filter" />:&nbsp;<a class="advancedFilter" href="javascript:advancedFilter();"><fmt:message key="filter.advanced"/></a></b>
			</td>
			<td><b><fmt:message key="contact.fullname"/>:</b></td>
			<td colspan="2"><input type="text" id="<%=Contact.FULLNAME%>" name="<%=Contact.FULLNAME%>" class="campo"/></td>
			<td colspan="3" class="center">
				<b><fmt:message key="dates.since"/>:&nbsp;</b>
				<input type="text" name="since" id="since" class="campo fecha" />
				<b>&nbsp;&nbsp;&nbsp;<fmt:message key="dates.until"/>:&nbsp;</b>
				<input type="text" name="until" id="until" class="campo fecha" />
			</td>
			<td class="left">
				<nobr>
					<b><fmt:message key="order"/>:</b>&nbsp;
					<select name="orderName" id="orderName" class="campo" style="width: 100px;">
						<option value="<%=Constants.ASCENDENT%>"><fmt:message key="order.asc"/></option>
						<option value="<%=Constants.DESCENDENT%>"><fmt:message key="order.desc"/></option>
					</select>
				</nobr>
			</td>
			<td class="right">
				<input type="submit" class="boton" onclick="return applyFilter()" style="width: 95px;" value="<fmt:message key="filter.apply" />"/>
				<a href="javascript:resetFilter();" class="boton"><fmt:message key="filter.reset" /></a>
			</td>
		</tr>
	</table>
	<div>&nbsp;</div>
	<table width="100%" class="center hide" id="advancedFilter">
		<tr>
			<th width="33%"><fmt:message key="projects"/></th>
			<th width="33%"><fmt:message key="project_manager"/></th>
			<th width="33%"><fmt:message key="job_category"/></th>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="<%=Configurations.PROJECT%>"/>
				<select id="<%=Configurations.PROJECT%>Select" class="campo" multiple style="height: 72px;">
					<c:forEach var="project" items="${projects }">
						<option value="${project.idProject }">${project.projectName }</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<input type="hidden" name="<%=Configurations.LIST_FILTERS_PM%>"/>
				<select id="<%=Configurations.LIST_FILTERS_PM%>Select" class="campo" multiple style="height: 72px;">
					<c:forEach var="pm" items="${projectManagers}">
						<option value="${pm.idEmployee}">${pm.contact.fullName}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<input type="hidden" name="<%=Configurations.JOBCATEGORY%>"/>
				<select id="<%=Configurations.JOBCATEGORY%>Select" class="campo" multiple style="height: 72px;">
					<c:forEach var="jobCategory" items="${jobs}">
						<option value="${jobCategory.idJobCategory}">${jobCategory.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		
		<tr>
			<th width="33%"><fmt:message key="maintenance.employee.resource_pool"/></th>
			<th width="33%"><fmt:message key="seller" /></th>
			<th width="33%"><fmt:message key="category"/></th>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="<%=Configurations.LIST_FILTERS_RESOURCE_POOL%>"/>
				<select id="<%=Configurations.LIST_FILTERS_RESOURCE_POOL%>Select" class="campo" multiple style="height: 72px;">
					<c:forEach var="resourcePool" items="${resourcePools}">
						<option value="${resourcePool.idResourcepool}">${resourcePool.name}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<input type="hidden" name="<%=Configurations.LIST_FILTERS_SELLERS%>"/>
				<select id="<%=Configurations.LIST_FILTERS_SELLERS%>Select" style="height: 72px;" multiple="multiple" class="campo">
				<c:forEach var="seller" items="${sellers }">
					<option value="${seller.idSeller }">${seller.name }</option>
				</c:forEach>
				</select>
			</td>
			<td>
				<input type="hidden" name="<%=Configurations.LIST_FILTERS_CATEGORIES%>"/>
				<select id="<%=Configurations.LIST_FILTERS_CATEGORIES%>Select"  style="height: 72px;" multiple="multiple" class="campo">
				<c:forEach var="category" items="${categories}">
					<option value="${category.idCategory }">${category.name }</option>
				</c:forEach>
				</select>
			</td>
		</tr>
	</table>
</fieldset>