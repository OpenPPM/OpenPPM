<%@page import="es.sm2.openppm.front.servlets.ProjectRiskServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="Utilidades" prefix="tl" %>
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
  ~ File: manage_risks_project.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- 
Request Attributes:
	project
	assumptions
	risks
	riskCategories
	issues
--%>

<script type="text/javascript">
var riskAjax = new AjaxCall('<%=ProjectRiskServlet.REFERENCE%>','<fmt:message key="error"/>');
</script>
<div id="projectTabs">
	<jsp:include page="../common/header_project.jsp">
		<jsp:param value="R" name="actual_page"/>
	</jsp:include>

    <%-- INFORMATION OF PROJECT --%>
    <jsp:include page="../common/info_project.jsp" flush="true" />

    <div id="panels" style="padding: 15px;">
		<form name="frm_project" id="frm_project" method="post">
			<input type="hidden" name="accion" value="" />
			<input type="hidden" name="id" id="id" value="${project.idProject}" />
			<input type="hidden" name="assumption_id" id="assumption_id" />
			<input type="hidden" name="risk_id" id="risk_id" />
			<input type="hidden" name="assumption_log_id" id="assumption_log_id" />
			<input type="hidden" name="reassessment_log_id" />		
			<input type="hidden" name="issue_id" id="issue_id" />
			<input type="hidden" id="idDocument" name="idDocument" />
			<input type="hidden" id="idImportProjects" name="idImportProjects" value=""/>		
			<input type="hidden" id="idImportRiskTemplates" name="idImportRiskTemplates"/>	
			<input type="hidden" name="scrollTop" value="" />
		</form>

		<jsp:include page="manage_risks.inc.jsp" flush="true" />
		<jsp:include page="manage_issues.inc.jsp"></jsp:include>
		<jsp:include page="manage_assumptions.inc.jsp" flush="true" />
		
		<fmt:message var="documentationTitle" key="documentation.risk"/>
		<visual:panel title="${documentationTitle}">
			<jsp:include page="../common/project_documentation.jsp">
				<jsp:param name="documentationType" value="<%=Constants.DOCUMENT_RISK %>"/>
			</jsp:include>
		</visual:panel>
	</div>
</div>

<jsp:include page="manage_assumption_popup.jsp" flush="true" />
<jsp:include page="manage_issue_popup.jsp" flush="true" />


<fmt:message key="projects" var="textOptionOne"/>
<fmt:message key="templates" var="textOptionTwo"/>
<jsp:include page="../common/select_two_options_popup.jsp" flush="true" >
	<jsp:param name="functionOptionOne" value="searchProjects.open()"/>
	<jsp:param name="textOptionOne" value='${textOptionOne}'/>
	<jsp:param name="functionOptionTwo" value='openSearchTemplatesRisk()'/>
	<jsp:param name="textOptionTwo" value='${textOptionTwo}'/>
</jsp:include>

<fmt:message key="risk.import" var="titleConfirmMsg"/>
<fmt:message key="risk.msg_import" var="confirmMsg"/>
<jsp:include page="../common/search_projects_and_import_popup.jsp" flush="true">
	<jsp:param name="titleConfirmMsg" value='${titleConfirmMsg}'/>
	<jsp:param name="confirmMsg" value='${confirmMsg}'/>
	<jsp:param name="accion" value="<%=ProjectRiskServlet.IMPORT_RISKS%>"/>
</jsp:include>

<jsp:include page="search_template_risks_and_import_popup.jsp" flush="true"/>
