<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.core.model.impl.Teammember"%>
<%@page import="es.sm2.openppm.utils.functions.ValidateUtil"%>
<%@page import="es.sm2.openppm.front.utils.RequestUtil"%>
<%@page import="es.sm2.openppm.core.common.Configurations"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.AssignmentServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Jobcategory"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="java.util.Date"%>
<%@page import="es.sm2.openppm.utils.DateUtil"%>
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
  ~ File: capacity_planning.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%
	HashMap<String, String> settings = SettingUtil.getSettings(request);
%>

<script type="text/javascript">

var capacityPlanning = {
	
	load : function() {
		
		$("#filterApplyCP").on('click',capacityPlanning.filterApply);
		$("#filterResetCP").on('click',capacityPlanning.filterReset);
		$("#showFiltersCP").on('click',function() { $('#advancedFilterCP').toggle('blind'); });
		$("#orderName").val('<%= RequestUtil.getConfiguration(request, Configurations.ORDER) %>');
		
		loadMultiple('<%= Configurations.PROJECT %>','<%= RequestUtil.getConfiguration(request, Configurations.PROJECT) %>');
		loadMultiple('<%= Configurations.JOBCATEGORY %>','<%= RequestUtil.getConfiguration(request, Configurations.JOBCATEGORY) %>');
		loadMultipleCheck('<%= Configurations.STATUS %>','<%= RequestUtil.getConfiguration(request, Configurations.STATUS) %>');
		
		$('#capacityPlanningToCsv').on('click',function(e) {
			e.stopPropagation();
			capacityPlanning.exportCsv();	
		});
	},
	filterApply : function() {
		
		var $since = $('#sinceCP');
		var $until = $('#untilCP');
		
		if ($since.val() == '') { $since.val('<fmt:formatDate value="<%=DateUtil.getFirstMonthDay(new Date())%>" pattern="${datePattern}"/>'); }
		if ($until.val() == '') { $until.val('<fmt:formatDate value="<%=DateUtil.getLastMonthDay(new Date())%>" pattern="${datePattern}"/>'); }
		
		var status = '';
		$('input[name=<%=Configurations.STATUS %>]', '#frmCapacityPlanning').each(function(){
			
			if ($(this).is(':checked')) {
			
				status += (status === ''?'':',')+$(this).val();
			}
		});
		
		var params = {
			accion: '<%=AssignmentServlet.JX_FILTER_CAPACITY_PLANNING%>',
			<%=Configurations.SINCE %>: $since.val(),
			<%=Configurations.UNTIL %>: $until.val(),
			<%=Configurations.TYPE %>: $('input[name=<%=Configurations.TYPE %>]:checked', '#frmCapacityPlanning').val(),
			<%=Configurations.STATUS %>: status,
			<%=Configurations.ORDER %>: $('#<%= Configurations.ORDER %>').val(),
			<%=Configurations.PROJECT %>: $('#<%= Configurations.PROJECT %>').val()+"",
			<%=Configurations.JOBCATEGORY %>: $('#<%= Configurations.JOBCATEGORY %>').val()+""
		};
		
		assignmentAjax.call(params,function(data) {
			$('#capacityPlanningTable').html(data);
		},'html');
		
	},
	filterReset : function() {
		
		$("#sinceCP").val('');
		$("#untilCP").val('');
		$("input[name=<%=Configurations.STATUS %>]").prop('checked', false);
		$("#<%= Configurations.PROJECT %>").val('');
		$("#<%= Configurations.JOBCATEGORY %>").val('');
	},
	exportCsv : function () {
		
		var f = document.forms.frmCapacityPlanning;
		f.accion.value = "<%=AssignmentServlet.EXPORT_CAPACITY_PLANNING_CSV%>";
		f.submit();
	}
	
};

readyMethods.add(function() {
	
	var sinceUntilCP = $('#sinceCP, #untilCP').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "sinceCP" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			sinceUntilCP.not(this).datepicker("option", option, date);
		}
	});
	
	capacityPlanning.load();
});

</script>

<fieldset style="margin-bottom: 15px; padding-top:10px; ">

	<form id="frmCapacityPlanning" name="frmCapacityPlanning" method="post" action="<%= AssignmentServlet.REFERENCE%>">
		<input type="hidden" name="accion" value="<%=AssignmentServlet.JX_FILTER_CAPACITY_PLANNING%>"/>
		<table style="width: 100%">
			<tr>
				<td>
					<b><fmt:message key="filter" />:&nbsp;<span class="advancedFilter textLink" id="showFiltersCP"><fmt:message key="filter.advanced"/></span></b>
				</td>
				<td colspan="3">
					<c:if test="<%=SettingUtil.getBoolean(settings, Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE, Settings.SETTING_SHOW_STATUS_FOR_RESOURCE_ROLE_DEFAULT)%>">
						<table style="width: 100%">
							<tr>
								<td>
									<input type="checkbox" name="<%=Configurations.STATUS %>" value="<%=Constants.RESOURCE_ASSIGNED %>" value="<%=Constants.RESOURCE_ASSIGNED %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.assigned"/>
								</td>
								<td>
									<input type="checkbox" name="<%=Configurations.STATUS %>" value="<%=Constants.RESOURCE_PRE_ASSIGNED %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.preassigned"/>
								</td>
								<td>
									<input type="checkbox" name="<%=Configurations.STATUS %>" value="<%=Constants.RESOURCE_RELEASED %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.released"/>
								</td>
								<td>
									<input type="checkbox" name="<%=Configurations.STATUS %>" value="<%=Constants.RESOURCE_PROPOSED %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.proposed"/>
								</td>
								<td>
									<input type="checkbox" name="<%=Configurations.STATUS %>" value="<%=Constants.RESOURCE_TURNED_DOWN %>" style="width:20px;"/>&nbsp;<fmt:message key="resource.turneddown"/>
								</td>
							</tr>
						</table>
					</c:if>
				</td>
				<td class="right">
					<input type="button" class="boton" id="filterApplyCP" style="width: 95px;" value="<fmt:message key="filter.apply" />"/>
					<input type="button" class="boton" id="filterResetCP" style="width: 95px;" value="<fmt:message key="filter.reset" />"/>
				</td>
			</tr>
			<tr>
				<th colspan="2"></th>
				<th colspan="2">
					<fmt:message key="msg.info.capacity_group"/>
				</th>
				<th></th>
			</tr>
			<tr>
				<td class="center">
					<b><fmt:message key="dates.since"/>:&nbsp;</b>
					<input type="text" name="<%=Configurations.SINCE %>" id="sinceCP" class="campo fecha" value="<%= RequestUtil.getConfiguration(request, Configurations.SINCE) %>"/>
				</td>
				<td>
					<b><fmt:message key="dates.until"/>:&nbsp;</b>
					<input type="text" name="<%=Configurations.UNTIL %>" id="untilCP" class="campo fecha" value="<%= RequestUtil.getConfiguration(request, Configurations.UNTIL) %>"/>
				</td>
				<td>
					<input type="radio" name="<%=Configurations.TYPE %>" id="resourceType" value="<%=Project.ENTITY %>"
						<%=Project.ENTITY.equals(RequestUtil.getConfiguration(request, Configurations.TYPE)) || ValidateUtil.isNull(RequestUtil.getConfiguration(request, Configurations.TYPE)) ?"checked":"" %>
						style="width: 10px;"/><fmt:message key="project"/>
				</td>
				<td>
					<input type="radio" name="<%=Configurations.TYPE %>" value="<%= Jobcategory.ENTITY %>"
						<%=Jobcategory.ENTITY.equals(RequestUtil.getConfiguration(request, Configurations.TYPE))?"checked":"" %>
						style="width: 10px;"/><fmt:message key="job_category"/>
				</td>
				<td>
					<nobr>
						<b><fmt:message key="order"/>:</b>&nbsp;
						<select name="<%=Configurations.ORDER %>" id="<%=Configurations.ORDER %>" class="campo" style="width: 150px;">
							<option value="<%=Constants.ASCENDENT%>"><fmt:message key="order.asc"/></option>
							<option value="<%=Constants.DESCENDENT%>"><fmt:message key="order.desc"/></option>
						</select>
					</nobr>
				</td>
			</tr>
		</table>
		<div>&nbsp;</div>
		<table style="width:100%;" class="center hide" id="advancedFilterCP">
			<tr>
				<th width="50%"><fmt:message key="projects"/></th>
				<th width="50%"><fmt:message key="job_category"/></th>
			</tr>
			<tr>
				<td>
					<select id="<%=Configurations.PROJECT%>" name="<%=Configurations.PROJECT%>" class="campo" multiple style="height: 72px;">
						<c:forEach var="project" items="${projects }">
							<option value="${project.idProject }">${project.projectName }</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<select id="<%=Configurations.JOBCATEGORY%>" name="<%=Configurations.JOBCATEGORY%>" class="campo" multiple style="height: 72px;">
						<c:forEach var="jobCategory" items="${jobs}">
							<option value="${jobCategory.idJobCategory}">${jobCategory.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
	</form>
</fieldset>

<div id="capacityPlanningTable"></div>