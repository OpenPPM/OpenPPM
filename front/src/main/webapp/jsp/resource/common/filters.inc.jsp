<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.core.model.impl.Teammember"%>
<%@page import="es.sm2.openppm.front.servlets.ResourceServlet"%>
<%@ page import="es.sm2.openppm.core.common.Configurations" %>
<%@ page import="es.sm2.openppm.front.utils.RequestUtil" %>
<%@ page import="es.sm2.openppm.core.model.impl.Configuration" %>
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
  ~ File: filters.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:04
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script language="javascript" type="text/javascript" >
<!--

function applyFilter() {

        actionApplyFilter();

        $.cookie('resource.statusAssigned', 	$("#<%=Constants.RESOURCE_ASSIGNED%>").prop('checked'), { expires: 365 });
        $.cookie('resource.statusPreAssigned',	$("#<%=Constants.RESOURCE_PRE_ASSIGNED %>").prop('checked'), { expires: 365 });
        $.cookie('resource.statusProposed', 	$("#<%=Constants.RESOURCE_PROPOSED %>").prop('checked'), { expires: 365 });
        $.cookie('resource.statusReleased', 	$("#<%=Constants.RESOURCE_RELEASED %>").prop('checked'), { expires: 365 });
        $.cookie('resource.statusTurnedDown', 	$("#<%=Constants.RESOURCE_TURNED_DOWN %>").prop('checked'), { expires: 365 });

        $.cookie('resource.name', $("#<%=Contact.FULLNAME%>").val(), { expires: 365 });
        $.cookie('resource.since', $("#since").val(), { expires: 365 });

        $.cookie('resource.until', $("#until").val(), { expires: 365 });
        $.cookie('resource.<%=Project.EMPLOYEEBYPROJECTMANAGER%>', $("#<%=Project.EMPLOYEEBYPROJECTMANAGER%>").val(), { expires: 365 });

        $.cookie('resource.<%=Teammember.JOBCATEGORY%>', $("#<%=Teammember.JOBCATEGORY%>").val(), { expires: 365 });

        return false;
}

function resetFilter() {

    $("#<%=Constants.RESOURCE_PRE_ASSIGNED %>").attr('checked',true);
    $("#<%=Constants.RESOURCE_ASSIGNED%>").attr('checked',false);
    $("#<%=Constants.RESOURCE_PROPOSED %>").attr('checked',false);
    $("#<%=Constants.RESOURCE_RELEASED %>").attr('checked',false);

    $("#<%=Constants.RESOURCE_TURNED_DOWN %>").attr('checked',false);

    $("#<%=Contact.FULLNAME%>").val('');
    $("#since").val('');
    $("#until").val('');

    $("#<%=Project.EMPLOYEEBYPROJECTMANAGER%>").val('');

    $("#<%=Teammember.JOBCATEGORY%>").val('');

    $.cookie('resource.statusAssigned', 	null);
    $.cookie('resource.statusPreAssigned',	null);
    $.cookie('resource.statusProposed', 	null);
    $.cookie('resource.statusReleased', 	null);
    $.cookie('resource.statusTurnedDown', 	null);

    $.cookie('resource.name', null);
    $.cookie('resource.since', null);
    $.cookie('resource.until', null);

    $.cookie('resource.<%=Project.EMPLOYEEBYPROJECTMANAGER%>', null);
    $.cookie('resource.<%=Teammember.JOBCATEGORY%>', null);

    <%-- Load configurations --%>
    $("#<%= Configurations.PROJECT_NAME %>").val('');
}

function loadFilterState() {
	
	$("#<%=Constants.RESOURCE_PRE_ASSIGNED %>")	.attr('checked',$.cookie('resource.statusPreAssigned') == 'true'?true:false);   
	$("#<%=Constants.RESOURCE_ASSIGNED%>")		.attr('checked',$.cookie('resource.statusAssigned') == 'true'?	 true:false);                  
	$("#<%=Constants.RESOURCE_PROPOSED %>")	 	.attr('checked',$.cookie('resource.statusProposed') == 'true'?	 true:false);                 
	$("#<%=Constants.RESOURCE_RELEASED %>")	 	.attr('checked',$.cookie('resource.statusReleased') == 'true'?	 true:false);                 
	$("#<%=Constants.RESOURCE_TURNED_DOWN %>")	.attr('checked',$.cookie('resource.statusTurnedDown') == 'true'? true:false);

	$("#<%=Contact.FULLNAME%>").val($.cookie('resource.name' ) != null?$.cookie('resource.name' ):'');
	$("#since").val($.cookie('resource.since') != null?$.cookie('resource.since'):'');

    $("#until").val($.cookie('resource.until') != null?$.cookie('resource.until'):'');
	selectMultiple('resource.','<%=Project.EMPLOYEEBYPROJECTMANAGER%>');

    selectMultiple('resource.','<%=Teammember.JOBCATEGORY%>');

    <%-- Load configurations --%>
    if ('<%= RequestUtil.getConfiguration(request, Configurations.PROJECT_NAME) %>' != null) {
        $("#<%= Configurations.PROJECT_NAME %>").val('<%= RequestUtil.getConfiguration(request, Configurations.PROJECT_NAME) %>');
    }
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
			<td class="right">
				<input type="submit" class="boton" onclick="return applyFilter()" style="width: 95px;" value="<fmt:message key="filter.apply" />"/>
				<a href="javascript:resetFilter();" class="boton"><fmt:message key="filter.reset" /></a>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
        <tr>
            <td><b><fmt:message key="search" />:</b></td>
            <td colspan="2"><input type="text" id="<%=Configurations.PROJECT_NAME %>" class="campo"/></td>
        </tr>
        <tr><td>&nbsp;</td></tr>
		<tr>
			<td><b><fmt:message key="contact.fullname"/>:</b></td>
			<td colspan="3"><input type="text" id="<%=Contact.FULLNAME%>" class="campo"/></td>
			<td colspan="3">
				<b><fmt:message key="dates.since"/>:&nbsp;</b>
				<input type="text" name="since" id="since" class="campo fecha" />
				<b>&nbsp;&nbsp;&nbsp;<fmt:message key="dates.until"/>:&nbsp;</b>
				<input type="text" name="until" id="until" class="campo fecha" />
			</td>
		</tr>
	</table>
	<div>&nbsp;</div>
	<table width="100%" class="center hide" id="advancedFilter">
		<tr>
			<th width="50%"><fmt:message key="project_manager"/></th>
			<th width="50%"><fmt:message key="job_category"/></th>
		</tr>
		<tr>
			<td>
				<select id="<%=Project.EMPLOYEEBYPROJECTMANAGER%>" class="campo" multiple style="height: 72px;">
					<c:forEach var="pm" items="${projectManagers }">
						<option value="${pm.idEmployee }">${pm.contact.fullName }</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select id="<%=Teammember.JOBCATEGORY%>" class="campo" multiple style="height: 72px;">
					<c:forEach var="jobCategory" items="${jobs}">
						<option value="${jobCategory.idJobCategory}">${jobCategory.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>
</fieldset>