<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@page import="es.sm2.openppm.front.servlets.RiskTemplatesServlet"%>

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
  ~ File: maintenance.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:06
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<c:set var="p_mant_category"  value="<%=Constants.MANT_CATEGORY%>" />
<c:set var="p_mant_company"  value="<%=Constants.MANT_COMPANY%>" /> 
<c:set var="p_mant_contact"  value="<%=Constants.MANT_CONTACT%>" />
<c:set var="p_mant_contracttype"  value="<%=Constants.MANT_CONTRACTTYPE%>" />
<c:set var="p_mant_resource"  value="<%=Constants.MANT_RESOURCE%>" />
<c:set var="p_mant_expenseaccounts"  value="<%=Constants.MANT_EXPENSEACCOUNTS%>" />
<c:set var="p_mant_geography"  value="<%=Constants.MANT_GEOGRAPHY%>" />
<c:set var="p_mant_operation"  value="<%=Constants.MANT_OPERATION%>" />
<c:set var="p_mant_operationaccount"  value="<%=Constants.MANT_OPERATIONACCOUNT%>" />
<c:set var="p_mant_performing_org"  value="<%=Constants.MANT_PERFORMING_ORG%>" />
<c:set var="p_mant_budgetaccounts"  value="<%=Constants.MANT_BUDGETACCOUNTS%>" />
<c:set var="p_mant_skill"  value="<%=Constants.MANT_SKILL%>" />
<c:set var="p_mant_customers"  value="<%=Constants.MANT_CUSTOMERS%>" />
<c:set var="p_mant_sellers"  value="<%=Constants.MANT_SELLERS%>" />
<c:set var="p_mant_metrickpi"  value="<%=Constants.MANT_METRIC_KPI%>" />
<c:set var="p_mant_customer_type"  value="<%=Constants.MANT_CUSTOMER_TYPE%>" />
<c:set var="p_mant_fundingsource"  value="<%=Constants.MANT_FUNDINGSOURCE%>" />
<c:set var="p_mant_documentation"  value="<%=Constants.MANT_DOCUMENTATION%>" />
<c:set var="p_mant_changetype"  value="<%=Constants.MANT_CHANGETYPE%>" />
<c:set var="p_mant_wbs_templates"  value="<%=Constants.MANT_WBS%>" />
<c:set var="p_mant_business_driver"  value="<%=Constants.MANT_BUSINESS_DRIVER%>" />
<c:set var="p_mant_business_driver_set"  value="<%=Constants.MANT_BUSINESS_DRIVER_SET%>" />
<c:set var="p_mant_risk_templates"  value="<%=Constants.MANT_RISK_TEMPLATES%>" />
<c:set var="p_mant_job_category"  value="<%=Constants.MANT_JOB_CATEGORY%>" />
<c:set var="p_mant_label"  value="<%=Constants.MANT_LABEL%>" />
<c:set var="p_mant_stage_gates"  value="<%=Constants.MANT_STAGE_GATES %>" />
<c:set var="p_mant_resource_pool"  value="<%=Constants.MANT_RESOURCE_POOL %>" />
<c:set var="p_mant_milestones_type"  value="<%=Constants.MANT_MILESTONES_TYPE %>" />
<c:set var="p_mant_closure_check"  value="<%=Constants.MANT_CLOSURE_CHECK %>" />
<c:set var="p_mant_risk_category"  value="<%=Constants.MANT_RISK_CATEGORY%>" />
<c:set var="p_mant_problem_check"  value="<%=Constants.MANT_PROBLEM_CHECK%>" />
<c:set var="p_mant_milestone_category"  value="<%=Constants.MANT_MILESTONE_CATEGORY%>" />
<c:set var="p_mant_classification_level"  value="<%= Constants.MANT_CLASSIFICATION_LEVEL %>" />
<c:set var="p_mant_bsc_dimension" value="<%=Constants.MANT_BSC_DIMENSION%>"/>
<c:set var="p_mant_technologies_map"  value="<%= Constants.MANT_TECHNOLOGIES_MAP %>" />
<c:set var="p_mant_knowledge_areas"  value="<%= Constants.MANT_KNOWLEDGE_AREAS %>" />
<c:set var="p_mant_stakeholder_classification"  value="<%= Constants.MANT_STAKEHOLDER_CLASSIFICATION %>" />
<c:set var="p_mant_profiles"  value="<%= Constants.MANT_PROFILES %>" />
<c:set var="p_mant_timeline"  value="<%= Constants.MANT_TIMELINE %>" />


<script type="text/javascript">

var mainAjax = new AjaxCall('<%=MaintenanceServlet.REFERENCE%>','<fmt:message key="error"/>');

function consMaintenance() {
	
	var f = document.forms["frm_mantenimientos"];
	
	if ($("#idManten").val() == "<%= Constants.MANT_RISK_TEMPLATES %>") {

		f.action 		= "<%= RiskTemplatesServlet.REFERENCE %>";
		f.accion.value 	= '';
	}
	else {
		
		f.action 		= "<%=MaintenanceServlet.REFERENCE %>";
		f.accion.value 	= "<%=MaintenanceServlet.CONS_MAINTENANCE %>";
    }
	
	loadingPopup();
	f.submit();
}



</script>

<%-- Ajax errors --%>
<div id ="idError" class="ui-state-error ui-corner-all" style="margin-bottom: 10px; padding: 0pt 0.7em; display:none">
	<p> 
		<span class="ui-icon ui-icon-alert" style="float: left; margin-right: 0.3em;"></span> 
		<strong><fmt:message key="msg.error"/>: </strong> 
		<img class="link" onclick="hide('idError');" style="float:right; margin-right:10px;" src="images/reject.png"> 
	</p> 
	<p></p> 
</div>

<form name="frm_mantenimientos" method="post">
	<input type="hidden" name="accion" value="" />
	<table>
		<tr>
			<th width="20%">
				<fmt:message key="menu.maintenance" />
			</th>
			<td width="40%">
				<select class="campo" name="idManten" id="idManten">
					<optgroup label="<fmt:message key="user_settings"/>">
						<option value="<%=Constants.MANT_CONTACT%>" <c:if test="${param.idManten == p_mant_contact}">selected</c:if>><fmt:message key="maintenance.contacts"/></option>
						<option value="<%=Constants.MANT_RESOURCE%>" <c:if test="${param.idManten == p_mant_resource}">selected</c:if>><fmt:message key="maintenance.profiles"/></option>
				    </optgroup>
                    <optgroup label="<fmt:message key="human_resources_settings"/>">
                        <option value="<%=Constants.MANT_SKILL%>" <c:if test="${param.idManten == p_mant_skill}">selected</c:if>><fmt:message key="maintenance.skills"/></option>
                        <option value="<%=Constants.MANT_JOB_CATEGORY%>" <c:if test="${param.idManten == p_mant_job_category}">selected</c:if>><fmt:message key="maintenance.job_category"/></option>
                        <option value="<%= Constants.MANT_RESOURCE_POOL %>" <c:if test="${idManten == p_mant_resource_pool }">selected</c:if>><fmt:message key="maintenance.employee.resource_pool"/></option>
                    </optgroup>
					<optgroup label="<fmt:message key="project_attributes"/>">
						<option value="<%=Constants.MANT_CATEGORY%>" <c:if test="${param.idManten == p_mant_category}">selected</c:if>><fmt:message key="maintenance.categories"/></option>
						<option value="<%=Constants.MANT_GEOGRAPHY%>" <c:if test="${param.idManten == p_mant_geography}">selected</c:if>><fmt:message key="maintenance.geographic_places"/></option>
						<option value="<%=Constants.MANT_CONTRACTTYPE%>" <c:if test="${param.idManten == p_mant_contracttype}">selected</c:if>><fmt:message key="maintenance.contract_types"/></option>
						<option value="<%=Constants.MANT_FUNDINGSOURCE%>" <c:if test="${param.idManten == p_mant_fundingsource}">selected</c:if>><fmt:message key="maintenance.fundingsource"/></option>
						<option value="<%= Constants.MANT_MILESTONES_TYPE %>" <c:if test="${idManten == p_mant_milestones_type }">selected</c:if>><fmt:message key="maintenance.milestones_type"/></option>
						<option value="<%=Constants.MANT_MILESTONE_CATEGORY%>" <c:if test="${param.idManten == p_mant_milestone_category}">selected</c:if>><fmt:message key="maintenance.milestones_categories"/></option>
                        <option value="<%=Constants.MANT_LABEL%>" <c:if test="${idManten == p_mant_label}">selected</c:if>><fmt:message key="labels"/></option>
                        <option value="<%= Constants.MANT_TECHNOLOGIES_MAP %>" <c:if test="${idManten == p_mant_technologies_map }">selected</c:if>><fmt:message key="TECHNOLOGIES_MAP"/></option>
                        <option value="<%= Constants.MANT_STAGE_GATES %>" <c:if test="${idManten == p_mant_stage_gates }">selected</c:if>><fmt:message key="stage_gates"/></option>
                        <option value="<%= Constants.MANT_CLASSIFICATION_LEVEL %>" <c:if test="${idManten == p_mant_classification_level}">selected</c:if>><fmt:message key="classificationLevel"/></option>
                        <option value="<%= Constants.MANT_CLOSURE_CHECK %>" <c:if test="${idManten == p_mant_closure_check }">selected</c:if>><fmt:message key="closure_check"/></option>
						<option value="<%=Constants.MANT_STAKEHOLDER_CLASSIFICATION%>" <c:if test="${idManten == p_mant_stakeholder_classification}">selected</c:if>><fmt:message key="STAKEHOLDER_CLASSIFICATION"/></option>
                        <option value="<%=Constants.MANT_TIMELINE%>" <c:if test="${idManten == p_mant_timeline}">selected</c:if>><fmt:message key="TIMELINE_TYPE"/></option>
                    </optgroup>
                    <optgroup label="<fmt:message key="risk_change_management"/>">
                        <option value="<%=Constants.MANT_RISK_CATEGORY%>" <c:if test="${param.idManten == p_mant_risk_category}">selected</c:if>><fmt:message key="maintenance.risks_categories"/></option>
                        <option value="<%=Constants.MANT_CHANGETYPE%>" <c:if test="${idManten == p_mant_changetype}">selected</c:if>><fmt:message key="maintenance.change_types"/></option>
                    </optgroup>
                    <optgroup label="<fmt:message key="finance_accounts_settings"/>">
                        <option value="<%=Constants.MANT_BUDGETACCOUNTS%>" <c:if test="${param.idManten == p_mant_budgetaccounts}">selected</c:if>><fmt:message key="maintenance.project_charge_accounts"/></option>
                        <option value="<%=Constants.MANT_EXPENSEACCOUNTS%>" <c:if test="${param.idManten == p_mant_expenseaccounts}">selected</c:if>><fmt:message key="maintenance.expense_charge_accounts"/></option>
                        <option value="<%=Constants.MANT_OPERATION%>" <c:if test="${param.idManten == p_mant_operation}">selected</c:if>><fmt:message key="maintenance.operation"/></option>
                        <option value="<%=Constants.MANT_OPERATIONACCOUNT%>" <c:if test="${param.idManten == p_mant_operationaccount}">selected</c:if>><fmt:message key="maintenance.operationaccount"/></option>
                    </optgroup>
                    <optgroup label="<fmt:message key="customers_providers"/>">
                        <option value="<%=Constants.MANT_CUSTOMER_TYPE%>" <c:if test="${param.idManten == p_mant_customer_type}">selected</c:if>><fmt:message key="maintenance.customer_types"/></option>
                        <option value="<%=Constants.MANT_CUSTOMERS%>" <c:if test="${param.idManten == p_mant_customers}">selected</c:if>><fmt:message key="maintenance.customers"/></option>
                        <option value="<%=Constants.MANT_SELLERS%>" <c:if test="${param.idManten == p_mant_sellers}">selected</c:if>><fmt:message key="maintenance.sellers"/></option>
                    </optgroup>
                    <optgroup label="<fmt:message key="program.settings"/>">
                        <option value="<%=Constants.MANT_BUSINESS_DRIVER%>" <c:if test="${idManten == p_mant_business_driver}">selected</c:if>><fmt:message key="maintenance.business_driver"/></option>
                        <option value="<%=Constants.MANT_BUSINESS_DRIVER_SET%>" <c:if test="${idManten == p_mant_business_driver_set}">selected</c:if>><fmt:message key="maintenance.business_driver_set"/></option>
                    </optgroup>
                    <optgroup label="<fmt:message key="company"/>">
                        <option value="<%=Constants.MANT_PERFORMING_ORG%>" <c:if test="${param.idManten == p_mant_performing_org}">selected</c:if>><fmt:message key="maintenance.performing_organizations"/></option>
                    </optgroup>
                    <optgroup label="<fmt:message key="templates_indicators"/>">
                        <option value="<%=Constants.MANT_METRIC_KPI%>" <c:if test="${param.idManten == p_mant_metrickpi}">selected</c:if>><fmt:message key="maintenance.metric_kpi"/></option>
                        <option value="<%=Constants.MANT_WBS%>" <c:if test="${idManten == p_mant_wbs_templates}">selected</c:if>><fmt:message key="maintenance.wbs_templates"/></option>
                        <option value="<%= Constants.MANT_PROBLEM_CHECK %>" <c:if test="${idManten == p_mant_problem_check }">selected</c:if>><fmt:message key="problem_check"/></option>
                        <option value="<%=Constants.MANT_RISK_TEMPLATES%>" <c:if test="${idManten == p_mant_risk_templates}">selected</c:if>><fmt:message key="maintenance.risk_templates"/></option>
                        <option value="<%=Constants.MANT_BSC_DIMENSION%>" <c:if test="${idManten == p_mant_bsc_dimension}">selected</c:if>><fmt:message key="maintenance.bscdimension"/></option>
                    </optgroup>

					<!-- learned lessons -->
					<optgroup label="<fmt:message key="VIEW.LEARNED_LESSON"/>">
						<option value="<%=Constants.MANT_PROFILES%>" <c:if test="${idManten == p_mant_profiles}">selected</c:if>><fmt:message key="maintenance.profiles"/></option>
                        <option value="<%=Constants.MANT_KNOWLEDGE_AREAS%>" <c:if test="${idManten == p_mant_knowledge_areas}">selected</c:if>><fmt:message key="MAINTENANCE.KNOWLEDGE_AREAS"/></option>
                    </optgroup>

                    <optgroup label="<fmt:message key="other"/>">
                        <option value="<%=Constants.MANT_DOCUMENTATION%>" <c:if test="${idManten == p_mant_documentation}">selected</c:if>><fmt:message key="maintenance.documentation_manuals"/></option>
					</optgroup>
				</select>
			</td>
			<td align="left" width="25%">
				<a href="#" class="boton" onClick="return consMaintenance();"><fmt:message key="show" /></a>
			</td>
		</tr>
	</table>
	
</form>

<c:choose>
	<c:when test="${param.idManten == p_mant_company}">
		<jsp:include page="mant_company.jsp"></jsp:include>	
	</c:when>
	<c:when test="${param.idManten == p_mant_performing_org}">
		<jsp:include page="mant_performing_org.jsp"></jsp:include>
	</c:when>	
	<c:when test="${param.idManten == p_mant_skill}">
		<jsp:include page="mant_skill.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_contact}">
		<jsp:include page="mant_contact.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_resource}">
		<jsp:include page="mant_resource.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_budgetaccounts}">
		<jsp:include page="mant_budgetaccounts.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_contracttype}">
		<jsp:include page="mant_contracttype.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_expenseaccounts}">
		<jsp:include page="mant_expenseaccounts.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_category}">
		<jsp:include page="mant_category.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_geography}">
		<jsp:include page="mant_geography.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_operationaccount}">
		<jsp:include page="mant_operationaccount.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_operation}">
		<jsp:include page="mant_operation.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_customers}">
		<jsp:include page="mant_customer.jsp"></jsp:include>
	</c:when>	
	<c:when test="${param.idManten == p_mant_sellers}">
		<jsp:include page="mant_seller.jsp"></jsp:include>
	</c:when>	
	<c:when test="${param.idManten == p_mant_metrickpi}">
		<jsp:include page="mant_metric_kpi.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_customer_type}">
		<jsp:include page="mant_customer_type.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_fundingsource}">
		<jsp:include page="mant_funding_source.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_documentation}">
		<jsp:include page="mant_documentation.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_changetype}">
		<jsp:include page="mant_changetype.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_wbs_templates}">
		<jsp:include page="mant_wbs_templates.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_business_driver}">
		<jsp:include page="mant_business_driver.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_business_driver_set}">
		<jsp:include page="mant_business_driver_set.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_risk_templates}">
		<jsp:include page="mant_risk_templates.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_job_category}">
		<jsp:include page="mant_job_category.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_label}">
		<jsp:include page="mant_label.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_stage_gates}">
		<jsp:include page="mant_stage_gates.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_resource_pool}">
		<jsp:include page="mant_resource_pool.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_milestones_type}">
		<jsp:include page="mant_milestones_type.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_closure_check}">
		<jsp:include page="mant_closure_check.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_risk_category}">
		<jsp:include page="mant_risk_category.jsp"></jsp:include>
	</c:when>
		<c:when test="${idManten == p_mant_problem_check}">
		<jsp:include page="mant_problem_check.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_milestone_category}">
		<jsp:include page="mant_milestones_category.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_classification_level}">
		<jsp:include page="mant_classification_level.jsp"></jsp:include>
	</c:when>
    <c:when test="${param.idManten == p_mant_bsc_dimension}">
        <jsp:include page="mant_bsc_dimension.jsp"></jsp:include>
    </c:when>
    <c:when test="${param.idManten == p_mant_technologies_map}">
        <jsp:include page="mant_technologies_map.jsp"></jsp:include>
    </c:when>
	<c:when test="${param.idManten == p_mant_knowledge_areas}">
		<jsp:include page="mant_knowledge_areas.jsp"></jsp:include>
	</c:when>
	<c:when test="${param.idManten == p_mant_stakeholder_classification}">
		<jsp:include page="mant_stakeholder_classification.jsp"></jsp:include>
	</c:when>
	<c:when test="${idManten == p_mant_profiles}">
		<jsp:include page="mant_profiles.jsp"></jsp:include>
	</c:when>
    <c:when test="${idManten == p_mant_timeline}">
        <jsp:include page="mant_timeline_type.jsp"></jsp:include>
    </c:when>
</c:choose>
