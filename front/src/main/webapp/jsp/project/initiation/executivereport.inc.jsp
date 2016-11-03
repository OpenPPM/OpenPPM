<%@page import="es.sm2.openppm.core.model.impl.Executivereport"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectInitServlet"%>
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
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_select_a" var="fmtStatusDateRequired">
	<fmt:param><b><fmt:message key="executive_report.status_date"/></b></fmt:param>
</fmt:message>



<script type="text/javascript">

var executiveReportValidator;

var executiveReport = {
	save : function() {
		if (executiveReportValidator.form()) {
			
			initAjax.call($("#frmExecutiveReport").serializeArray());
		}
	}
};

readyMethods.add(function() {
	
	executiveReportValidator= $("#frmExecutiveReport").validate({
		errorContainer: 'div#executiveReportErrors',
		errorLabelContainer: 'div#executiveReportErrors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#executiveReportNumerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Executivereport.STATUSDATE%> : { required: true },
			<%=Executivereport.INTERNAL%>: { maxlength: 3000},
			<%=Executivereport.EXTERNAL%>: { maxlength: 3000}
		},
		messages: {
			<%=Executivereport.STATUSDATE%> : { required: '${fmtStatusDateRequired}' }
		}
	});
	
	$('#<%=Executivereport.STATUSDATE %>').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function() {
			if (executiveReportValidator.numberOfInvalids() > 0) executiveReportValidator.form();
		}
	});
	
	$("#saveExecutivereport").on('click', executiveReport.save);
	
});
</script>


	<div id="executiveReportErrors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="executiveReportNumerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	<form name="frmExecutiveReport" id="frmExecutiveReport" action="<%=ProjectInitServlet.REFERENCE %>">
		<input type="hidden" name="accion" value="<%=ProjectInitServlet.JX_SAVE_EXECUTIVEREPORT %>" />
		<input type="hidden" name="<%=Executivereport.IDEXECUTIVEREPORT%>"/>
		<input type="hidden" name="idProject" value="${project.idProject}"/>
		<table width="100%">
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<td class="left title" width="8%"><fmt:message key="executive_report.status_date" />&nbsp;*</th>
				<c:choose>
					<c:when test="${op:hasPermissionSetting(settings, user,project.status,tab) and (op:isStringEquals(project.status, status_initiating) or op:isStringEquals(project.status, status_planning))}">
						<td>
							<input type="text" name="<%=Executivereport.STATUSDATE %>" id="<%=Executivereport.STATUSDATE%>" class="campo fecha"
								value="<fmt:formatDate value="${executivereport.statusDate}" pattern="${datePattern}" />" />
						</td>
					</c:when>
					<c:otherwise>
						<td><fmt:formatDate value="${executivereport.statusDate}" pattern="${datePattern}" /></td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<th class="titulo" colspan="2"><fmt:message key="executive_report.internal" /></th>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${op:hasPermissionSetting(settings, user,project.status,tab) and (op:isStringEquals(project.status, status_initiating) or op:isStringEquals(project.status, status_planning))}">
						<td colspan="2">
							<textarea rows="3" name="<%=Executivereport.INTERNAL%>" id="<%=Executivereport.INTERNAL%>" class="campo" style="width:98%;">${executivereport.internal}</textarea>
						</td>
					</c:when>
					<c:otherwise>
						<td colspan="2" style="padding: 5px">
							${op:generateBreak(tl:escape(executivereport.internal))}
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<th class="titulo" colspan="2"><fmt:message key="executive_report.external" /></th>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${op:hasPermissionSetting(settings, user,project.status,tab) and (op:isStringEquals(project.status, status_initiating) or op:isStringEquals(project.status, status_planning))}">
						<td colspan="2">
							<textarea rows="3" name="<%=Executivereport.EXTERNAL%>" id="<%=Executivereport.EXTERNAL%>" class="campo" style="width:98%;">${executivereport.external}</textarea>
						</td>
					</c:when>
					<c:otherwise>
						<td colspan="2" style="padding: 5px">
							${op:generateBreak(tl:escape(executivereport.external))}
						</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		
		<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab) and (op:isStringEquals(project.status, status_initiating) or op:isStringEquals(project.status, status_planning))}">
			<div align="right" style="margin-top:10px;">
				<input type="button" class="boton" id="saveExecutivereport" value="<fmt:message key="save" />" />
			</div>
		</c:if>	
	</form>
