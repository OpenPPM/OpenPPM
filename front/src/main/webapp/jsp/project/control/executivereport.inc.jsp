<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.model.impl.Executivereport"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>

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
  ~ File: executivereport.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:01
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_select_a" var="fmtStatusDateRequired">
	<fmt:param><b><fmt:message key="executive_report.status_date"/></b></fmt:param>
</fmt:message>

<fmt:message key="msg.error.max_value" var="fmtInternalMaxLength">
    <fmt:param><b><fmt:message key="executive_report.internal"/></b></fmt:param>
    <fmt:param>3000&nbsp;</fmt:param>
</fmt:message>

<fmt:message key="msg.error.max_value" var="fmtExternalMaxLength">
    <fmt:param><b><fmt:message key="executive_report.external"/></b></fmt:param>
    <fmt:param>3000&nbsp;</fmt:param>
</fmt:message>

<div ng-controller="ValidateExecutiveReportController">

	<fieldset>
		<legend><fmt:message key="executive_report"/></legend>
		<div id="executiveReportErrors" class="ui-state-error ui-corner-all"
			 ng-show="isSubmit && (frm_project.executivereportstatusDate.$invalid
									|| frm_project.internal.$invalid
									|| frm_project.external.$invalid)">
			<p>
				<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
				<strong><fmt:message key="msg.error_title"/></strong>
			</p>
			<ol>
				<li ng-show="frm_project.executivereportstatusDate.$invalid;">${fmtStatusDateRequired}</li>
				<li ng-show="frm_project.internal.$invalid;">${fmtInternalMaxLength}</li>
				<li ng-show="frm_project.external.$invalid;">${fmtExternalMaxLength}</li>
			</ol>
		</div>

		<table width="100%">
			<tr><td colspan="3">&nbsp;</td></tr>
			<tr>
				<th class="left" width="8%">
					<fmt:message key="executive_report.status_date" />&nbsp;*
				</th>
				<c:choose>
					<c:when test="${op:hasPermissionSetting(settings, user,project.status,tab) and op:isStringEquals(project.status, status_control)}">
						<th>
							<input type="text" name="<%=Executivereport.ENTITY+Executivereport.STATUSDATE %>" ng-model="executivereportstatusDate"
								   id="<%=Executivereport.ENTITY+Executivereport.STATUSDATE%>" class="campo fecha" required />
						</th>
					</c:when>
					<c:otherwise>
						<th><fmt:formatDate value="${executivereport.statusDate}" pattern="${datePattern}" /></th>
					</c:otherwise>
				</c:choose>
				<th><div id="headerReportButtons" class="right"></div></th>
			</tr>
			<tr><td colspan="3">&nbsp;</td></tr>
			<tr>
				<th class="titulo" colspan="3"><fmt:message key="executive_report.internal" /></th>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${op:hasPermissionSetting(settings, user,project.status,tab) and op:isStringEquals(project.status, status_control)}">
						<td colspan="3">
							<textarea rows="3" name="<%=Executivereport.INTERNAL%>" ng-model="internal" ng-maxlength="3000" id="<%=Executivereport.INTERNAL%>" class="campo" style="width:98%;"></textarea>
						</td>
					</c:when>
					<c:otherwise>
						<td colspan="3" style="padding: 5px">
							<textarea rows="3" name="<%=Executivereport.INTERNAL%>" id="<%=Executivereport.INTERNAL%>" class="campo" style="width:98%;" readonly="readonly" >${executivereport.internal}</textarea>
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr><td colspan="3">&nbsp;</td></tr>
			<tr>
				<th class="titulo" colspan="3"><fmt:message key="executive_report.external" /></th>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${op:hasPermissionSetting(settings, user,project.status,tab) and op:isStringEquals(project.status, status_control)}">
						<td colspan="3">
							<textarea rows="3" name="<%=Executivereport.EXTERNAL%>" ng-model="external"  ng-maxlength="3000" id="<%=Executivereport.EXTERNAL%>" class="campo" style="width:98%;"></textarea>
						</td>
					</c:when>
					<c:otherwise>
						<td colspan="3" style="padding: 5px">
								${op:generateBreak(tl:escape(executivereport.external))}
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>

		<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
			<div align="right" style="margin-top:10px;">
				<input type="button" class="boton" id="saveExecutivereport" ng-click="valid=!frm_project.$invalid; save()" value="<fmt:message key="save" />" />
			</div>
		</c:if>
	</fieldset>
</div>

<script type="text/javascript">

	appController.controller('ValidateExecutiveReportController', function($scope) {

		$scope.isSubmit = false;
		$scope.valid = false;
		$scope.internal = '${tl:escapeUnicode(executivereport.internal)}';
		$scope.external = '${tl:escapeUnicode(executivereport.external)}';
		$scope.executivereportstatusDate = '<fmt:formatDate value="${executivereport.statusDate}" pattern="${datePattern}" />';

		$scope.save = function() {
			$scope.isSubmit = true;

			if ($scope.valid) {
				var params = {
					accion: '<%=ProjectControlServlet.JX_SAVE_EXECUTIVEREPORT %>',
					idProject: $('#id').val(),
					executivereportstatusDate: $scope.executivereportstatusDate,
					internal: $scope.internal,
					external: $scope.external
				};

				controlAjax.call(params);
			}
		};
	});

	readyMethods.add(function() {

		$('#<%=Executivereport.ENTITY+Executivereport.STATUSDATE %>').datepicker({
			dateFormat: '${datePickerPattern}',
			firstDay: 1,
			showOn: 'button',
			buttonImage: 'images/calendario.png',
			buttonImageOnly: true,
			numberOfMonths: ${numberOfMonths},
			changeMonth: true
		});

	});
</script>

