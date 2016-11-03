<%@page import="es.sm2.openppm.front.utils.RequestUtil"%>
<%@page import="es.sm2.openppm.core.common.Configurations"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
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
	
	$.cookie('resourcePlanning.statusAssigned', 	$("#<%=Constants.RESOURCE_ASSIGNED%>").prop('checked'), { expires: 365 });
	$.cookie('resourcePlanning.statusPreAssigned',	$("#<%=Constants.RESOURCE_PRE_ASSIGNED %>").prop('checked'), { expires: 365 });
	$.cookie('resourcePlanning.statusProposed', 	$("#<%=Constants.RESOURCE_PROPOSED %>").prop('checked'), { expires: 365 });
	$.cookie('resourcePlanning.statusReleased', 	$("#<%=Constants.RESOURCE_RELEASED %>").prop('checked'), { expires: 365 });
	$.cookie('resourcePlanning.statusTurnedDown', 	$("#<%=Constants.RESOURCE_TURNED_DOWN %>").prop('checked'), { expires: 365 });
    $.cookie('resourcePlanning.statusUnAssigned', 	$("#<%=Constants.RESOURCE_UNASSIGNED %>").prop('checked'), { expires: 365 });
	
	$.cookie('resourcePlanning.name', $("#<%=Contact.FULLNAME%>").val(), { expires: 365 });
	$.cookie('resourcePlanning.since', $("#since").val(), { expires: 365 });
	$.cookie('resourcePlanning.until', $("#until").val(), { expires: 365 });
	$.cookie('resourcePlanning.orderName', $("#orderName").val(), { expires: 365 });

	return false;
}

function resetFilter() {
	
	$("#<%=Constants.RESOURCE_PRE_ASSIGNED %>").attr('checked',false);
	$("#<%=Constants.RESOURCE_ASSIGNED%>").attr('checked',true);
	$("#<%=Constants.RESOURCE_PROPOSED %>").attr('checked',false);
	$("#<%=Constants.RESOURCE_RELEASED %>").attr('checked',false);
	$("#<%=Constants.RESOURCE_TURNED_DOWN %>").attr('checked',false);
    $("#<%=Constants.RESOURCE_UNASSIGNED %>").attr('checked',false);
	
	$("#<%=Contact.FULLNAME%>").val('');
	
	<%-- First and last day of the after month --%>
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
	
	$.cookie('resourcePlanning.statusAssigned', 	null);
	$.cookie('resourcePlanning.statusPreAssigned',	null);
	$.cookie('resourcePlanning.statusProposed', 	null);
	$.cookie('resourcePlanning.statusReleased', 	null);
	$.cookie('resourcePlanning.statusTurnedDown', 	null);
    $.cookie('resourcePlanning.statusUnAssigned', 	null);
	
	$.cookie('resourcePlanning.name', null);
	$.cookie('resourcePlanning.since', null);
	$.cookie('resourcePlanning.until', null);
	$.cookie('resourcePlanning.orderName', null);
}

function loadFilterState() {
	
	$("#<%=Constants.RESOURCE_PRE_ASSIGNED%>")	.attr('checked',$.cookie('resourcePlanning.statusPreAssigned') == 'true'?true:false);   
	$("#<%=Constants.RESOURCE_ASSIGNED%>")		.attr('checked',$.cookie('resourcePlanning.statusAssigned') == 'true'?true:false);                  
	$("#<%=Constants.RESOURCE_PROPOSED %>")	 	.attr('checked',$.cookie('resourcePlanning.statusProposed') == 'true'?true:false);                 
	$("#<%=Constants.RESOURCE_RELEASED %>")	 	.attr('checked',$.cookie('resourcePlanning.statusReleased') == 'true'?true:false);                 
	$("#<%=Constants.RESOURCE_TURNED_DOWN %>")	.attr('checked',$.cookie('resourcePlanning.statusTurnedDown') == 'true'?true:false);              
	
	$("#<%=Contact.FULLNAME%>").val($.cookie('resourcePlanning.name' ) != null?$.cookie('resourcePlanning.name' ):'');
	$("#since").val($.cookie('resourcePlanning.since') != null?$.cookie('resourcePlanning.since'):'');
	$("#until").val($.cookie('resourcePlanning.until') != null?$.cookie('resourcePlanning.until'):'');
	$("#orderName").val($.cookie('resourcePlanning.orderName') != null?$.cookie('resourcePlanning.orderName'):'');
	
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
			<td>
				<input type="checkbox" name="filter" id="<%=Constants.RESOURCE_ASSIGNED %>" value="<%=Constants.RESOURCE_ASSIGNED %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.assigned"/>
			</td>
			<td>
				<input type="checkbox" name="filter" id="<%=Constants.RESOURCE_PRE_ASSIGNED %>" value="<%=Constants.RESOURCE_PRE_ASSIGNED %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.preassigned"/>
			</td>
			<td>
				<input type="checkbox" name="filter" id="<%=Constants.RESOURCE_RELEASED %>" value="<%=Constants.RESOURCE_RELEASED %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.released"/>
			</td>
			<td>
				<input type="checkbox" name="filter" id="<%=Constants.RESOURCE_PROPOSED %>" value="<%=Constants.RESOURCE_PROPOSED %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.proposed"/>
			</td>
			<td>
				<input type="checkbox" name="filter" id="<%=Constants.RESOURCE_TURNED_DOWN %>" value="<%=Constants.RESOURCE_TURNED_DOWN %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.turneddown"/>
			</td>
            <td>
                <input type="checkbox" name="filter" id="<%=Constants.RESOURCE_UNASSIGNED %>" value="<%=Constants.RESOURCE_UNASSIGNED %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.unassigned"/>
            </td>
			<td class="right">
				<input type="submit" class="boton" onclick="return applyFilter()" style="width: 95px;" value="<fmt:message key="filter.apply" />"/>
				<a href="javascript:resetFilter();" class="boton"><fmt:message key="filter.reset" /></a>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
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
			<td></td>
			<td></td>
		</tr>
	</table>
</fieldset>