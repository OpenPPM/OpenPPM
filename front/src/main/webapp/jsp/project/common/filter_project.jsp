<%@page import="es.sm2.openppm.core.model.impl.Classificationlevel"%>
<%@page import="es.sm2.openppm.core.model.impl.Activityseller"%>
<%@page import="es.sm2.openppm.core.model.impl.Stagegate"%>
<%@page import="es.sm2.openppm.core.javabean.FiltroTabla"%>
<%@page import="es.sm2.openppm.core.model.impl.Fundingsource"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Geography"%>
<%@page import="es.sm2.openppm.core.model.impl.Category"%>
<%@page import="es.sm2.openppm.core.model.impl.Customer"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.model.impl.Label"%>
<%@page import="es.sm2.openppm.core.common.Configurations"%>
<%@page import="es.sm2.openppm.front.utils.RequestUtil"%>
<%@ page import="es.sm2.openppm.core.model.impl.Technology" %>

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
  ~ File: filter_project.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--
var custType;

function applyFilter() {
	
	$('#ids').val(${param.table}.fnGetSelectedsCol());
	$.cookie('projects.ids', $("#ids").val(), { expires: 365 });
	loadProjects();
	
	$.cookie('projects.statusIni', $("#statusIni").prop('checked'), { expires: 365 });
	$.cookie('projects.statusPla', $("#statusPla").prop('checked'), { expires: 365 });
	$.cookie('projects.statusCon', $("#statusCon").prop('checked'), { expires: 365 });
	$.cookie('projects.statusClo', $("#statusClo").prop('checked'), { expires: 365 });
    $.cookie('projects.statusArchived', $("#statusArchived").prop('checked'), { expires: 365 });
	$.cookie('projects.filterInternal', $("#filterInternal").val(), { expires: 365 });
	$.cookie('projects.requiresTravelling', $("#requiresTravelling").val(), { expires: 365 });
	
	$.cookie('projects.filter_start', $("#filter_start").val(), { expires: 365 });
	$.cookie('projects.filter_finish', $("#filter_finish").val(), { expires: 365 });
	
	$.cookie('projects.idCustomerType', $("#idCustomerType").val(), { expires: 365 });
	$.cookie('projects.idCustomer', $("#idCustomer").val(), { expires: 365 });
	$.cookie('projects.<%=Category.IDCATEGORY %>', $("#<%=Category.IDCATEGORY %>").val(), { expires: 365 });
	$.cookie('projects.idProgram', $("#idProgram").val(), { expires: 365 });
	$.cookie('projects.idPM', $("#idPM").val(), { expires: 365 });
	$.cookie('projects.idSponsor', $("#idSponsor").val(), { expires: 365 });
	$.cookie('projects.idSeller', $("#idSeller").val(), { expires: 365 });
	$.cookie('geography.idGeography', $("#<%=Geography.IDGEOGRAPHY%>").val(), { expires: 365 });
	$.cookie('projects.idLabel', $("#<%= Label.IDLABEL %>").val(), { expires: 365 });
	$.cookie('projects.idStageGate', $("#<%= Stagegate.IDSTAGEGATE %>").val(), { expires: 365 });
	
	$.cookie('projects.<%=Project.BUDGETYEAR %>', $("#<%=Project.BUDGETYEAR %>").val(), { expires: 365 });
	$.cookie('projects.<%=Project.PROJECTNAME %>', $("#<%=Project.PROJECTNAME %>").val(), { expires: 365 });
	
	$.cookie('ragStatus', $("#ragStatus").val(), { expires: 365 });
	$.cookie('first', $("#first").val(), { expires: 365 });
	$.cookie('last', $("#last").val(), { expires: 365 });
	$.cookie('filterPriority', $("#filterPriority").val(), { expires: 365 });
	
	<% if (SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)) {%>
	$.cookie('projects.idPO', $("#idPO").val(), { expires: 365 });
	<%}%>
}

function resetFilter() {
	
	$("#statusIni").attr('checked',true);
	$("#statusPla").attr('checked',true);
	$("#statusCon").attr('checked',true);
	$("#statusClo").attr('checked',false);
    $("#statusArchived").attr('checked',false);
	$("#filterInternal").val('');
	$("#requiresTravelling").val('');
	$.cookie('projects.statusIni', null);
	$.cookie('projects.statusPla', null);
	$.cookie('projects.statusCon', null);
	$.cookie('projects.statusClo', null);
	$.cookie('projects.statusArchived', null);
	$.cookie('projects.filterInternal', false);
	$.cookie('projects.requiresTravelling', false);
	
	$("#filter_start").val('');
	$("#filter_finish").val('');
	$.cookie('projects.filter_start', null);
	$.cookie('projects.filter_finish', null);
	
	$("#idCustomerType").val('');
	custType.filterSelect('filter');
	$("#idCustomer").val('');
	$("#<%=Category.IDCATEGORY %>").val('');
	$("#idProgram").val('');
	$("#idPM").val('');
	$("#idSponsor").val('');
	$("#idSeller").val('');
	$("#<%=Geography.IDGEOGRAPHY%>").val('');
	$("#<%= Label.IDLABEL %>").val('');
	$("#<%= Stagegate.IDSTAGEGATE %>").val('');
	
	$.cookie('projects.idCustomerType', null);
	$.cookie('projects.idCustomer', null);
	$.cookie('projects.<%=Category.IDCATEGORY %>', null);
	$.cookie('projects.idProgram', null);
	$.cookie('projects.idSponsor', null);
	$.cookie('projects.idPM', null);
	$.cookie('projects.idSeller', null);
	$.cookie('geography.idGeography', null);
	$.cookie('ragStatus', null);
	$.cookie('projects.idLabel', null);
	$.cookie('projects.idStageGate', null);
	
	$("#<%=Project.BUDGETYEAR %>").val('');
	$("#<%=Project.PROJECTNAME %>").val('');
	$.cookie('projects.<%=Project.BUDGETYEAR %>', null);
	$.cookie('projects.<%=Project.PROJECTNAME %>', null);
	
	$("#ragStatus").val('').trigger('change');
	$("#first").val('');
	$("#last").val('');
	$("#filterPriority").val('').trigger('change');
	
	<% if (SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)) {%>
		$("#idPO").val('');
		$.cookie('projects.idPO', null);
	<%}%>
	
	<%-- Load configurations --%>
	$("#<%= Configurations.BOOLEAN_FILTER_SELLERS %>").val('');

    // Combos
    $("#<%= Configurations.LIST_FILTERS_CONTRACT_TYPE %>").val('');
    $("#<%= Configurations.LIST_FILTERS_FM %>").val('');
	$("#<%= Configurations.LIST_FILTERS_CLASSIFICATION_LEVEL %>").val('');
    $("#<%= Configurations.LIST_FILTERS_FOUNDING_SOURCE %>").val('');
    $("#<%= Configurations.LIST_FILTERS_TECHNOLOGY %>").val('');

	$("#<%= Configurations.BOOLEAN_FILTER_PRIORITY_ADJUSTMENT %>").val('');
	$("#<%= Configurations.FILTER_FIRST_RISK_RATING %>").val('');
	$("#<%= Configurations.FILTER_LAST_RISK_RATING %>").val('');
	$("#<%= Configurations.FILTER_RISK_RATING %>").val('').trigger('change');
}

function loadFilterState() {
	
	$("#ids").val($.cookie('projects.ids'));
	
	if ($.cookie('projects.statusIni') == 'false') {
		$("#statusIni").attr('checked',false);
	}
	else {
		$("#statusIni").attr('checked',true);
	}
	
	if ($.cookie('projects.statusPla') == 'false') {
		$("#statusPla").attr('checked',false);
	}
	else {
		$("#statusPla").attr('checked',true);
	}
	
	if ($.cookie('projects.statusCon') == 'false') {
		$("#statusCon").attr('checked',false);
	}
	else {
		$("#statusCon").attr('checked',true);
	}
	
	if ($.cookie('projects.statusClo') != null && $.cookie('projects.statusClo') == 'true') {
		$("#statusClo").attr('checked',true);
	}

	if ($.cookie('projects.statusArchived') != null && $.cookie('projects.statusArchived') == 'true') {
		$("#statusArchived").attr('checked',true);
	}
	
	if ($.cookie('projects.filterInternal') != null) {
		$("#filterInternal").val($.cookie('projects.filterInternal'));
	}
	
	if ($.cookie('projects.requiresTravelling') != null) {
		$("#requiresTravelling").val($.cookie('projects.requiresTravelling'));
	}
	
	selectMultiple('projects.','idCustomerType');
	selectMultiple('projects.','idCustomer');
	selectMultiple('projects.','<%=Category.IDCATEGORY %>');
	selectMultiple('projects.','idProgram');
	selectMultiple('projects.','idPM');
	selectMultiple('projects.','idSponsor');
	selectMultiple('projects.','idSeller');
	selectMultiple('projects.','idLabel');
	selectMultiple('projects.','idStageGate');
	selectMultiple('geography.','idGeography');
	selectMultiple('geography.','idGeography');
	
	if ($.cookie('projects.filter_start') != null) {
		$("#filter_start").val($.cookie('projects.filter_start'));
	}
	if ($.cookie('projects.filter_finish') != null) {
		$("#filter_finish").val($.cookie('projects.filter_finish'));
	}
	if ($.cookie('projects.<%=Project.BUDGETYEAR %>') != null) {
		$("#<%=Project.BUDGETYEAR %>").val($.cookie('projects.<%=Project.BUDGETYEAR %>'));
	}
	if ($.cookie('projects.<%=Project.PROJECTNAME %>') != null) {
		$("#<%=Project.PROJECTNAME %>").val($.cookie('projects.<%=Project.PROJECTNAME %>'));
	}
	if ($.cookie('ragStatus') != null) {
		$("#ragStatus").val($.cookie('ragStatus')).trigger('change');
	}
	if ($.cookie('first') != null) {
		$("#first").val($.cookie('first'));
	}
	if ($.cookie('last') != null) {
		$("#last").val($.cookie('last'));
	}
	if ($.cookie('filterPriority') != null) {
		$("#filterPriority").val($.cookie('filterPriority')).trigger('change');
	}
	
	<% if (SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)) {%>
		selectMultiple('projects.','idPO');
	<%}%>
	
	<%-- Load configurations --%>
	loadMultiple('<%= Configurations.LIST_FILTERS_CONTRACT_TYPE %>','<%= RequestUtil.getConfiguration(request, Configurations.LIST_FILTERS_CONTRACT_TYPE) %>');
	loadMultiple('<%= Configurations.LIST_FILTERS_FM %>','<%= RequestUtil.getConfiguration(request, Configurations.LIST_FILTERS_FM) %>');
	$("#<%= Configurations.BOOLEAN_FILTER_SELLERS %>").val('<%= RequestUtil.getConfiguration(request, Configurations.BOOLEAN_FILTER_SELLERS) %>');
	
	loadMultiple('<%= Configurations.LIST_FILTERS_CLASSIFICATION_LEVEL %>','<%= RequestUtil.getConfiguration(request, Configurations.LIST_FILTERS_CLASSIFICATION_LEVEL) %>');
    loadMultiple('<%= Configurations.LIST_FILTERS_FOUNDING_SOURCE %>','<%= RequestUtil.getConfiguration(request, Configurations.LIST_FILTERS_FOUNDING_SOURCE) %>');
    loadMultiple('<%= Configurations.LIST_FILTERS_TECHNOLOGY %>','<%= RequestUtil.getConfiguration(request, Configurations.LIST_FILTERS_TECHNOLOGY) %>');

	$("#<%= Configurations.BOOLEAN_FILTER_PRIORITY_ADJUSTMENT %>").val('<%= RequestUtil.getConfiguration(request, Configurations.BOOLEAN_FILTER_PRIORITY_ADJUSTMENT) %>');
	
	if ('<%= RequestUtil.getConfiguration(request, Configurations.FILTER_FIRST_RISK_RATING) %>' != null) {
		$("#<%= Configurations.FILTER_FIRST_RISK_RATING %>").val('<%= RequestUtil.getConfiguration(request, Configurations.FILTER_FIRST_RISK_RATING) %>');
	}
	if ('<%= RequestUtil.getConfiguration(request, Configurations.FILTER_LAST_RISK_RATING) %>' != null) {
		$("#<%= Configurations.FILTER_LAST_RISK_RATING %>").val('<%= RequestUtil.getConfiguration(request, Configurations.FILTER_LAST_RISK_RATING) %>');
	}
	if ('<%= RequestUtil.getConfiguration(request, Configurations.FILTER_RISK_RATING) %>' != null) {
		$("#<%= Configurations.FILTER_RISK_RATING %>").val('<%= RequestUtil.getConfiguration(request, Configurations.FILTER_RISK_RATING) %>').trigger('change');
	}
}

function advancedFilter() {
	$('#advancedFilter').toggle('blind');
}

readyMethods.add(function() {

	var dates = $('#filter_start, #filter_finish').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "filter_start" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			dates.not(this).datepicker("option", option, date);
		}
	});
	$('#ragStatus').change(function() {
		$(this).removeClass();
		$(this).addClass('campo');
		$(this).addClass('statusReport');
		$(this).addClass($(this).children("[selected]").attr('class'));
	}).trigger('change');
	
	$('#filterPriority').change(function() {
		
		var $option = $(this);
		
		if ($option.val() == "<%=FiltroTabla.GREATHER_EQUAL%>" || $option.val() == "<%=FiltroTabla.LESS_EQUAL%>") {
			$('#last').show();
			$('#first').hide();
		}
		else if ($option.val() == '<%=FiltroTabla.BETWEEN%>') {
			$('#first, #last').show();
		}
		else {
			$('#first, #last').hide();
		}
		
	}).trigger('change');
	
	$('#<%= Configurations.FILTER_RISK_RATING %>').change(function() {
		
		var $option = $(this);
		
		if ($option.val() == "<%=FiltroTabla.GREATHER_EQUAL%>" || $option.val() == "<%=FiltroTabla.LESS_EQUAL%>") {
			$('#<%= Configurations.FILTER_LAST_RISK_RATING %>').show();
			$('#<%= Configurations.FILTER_FIRST_RISK_RATING %>').hide();
		}
		else if ($option.val() == '<%=FiltroTabla.BETWEEN%>') {
			$('#<%= Configurations.FILTER_FIRST_RISK_RATING %>, #<%= Configurations.FILTER_LAST_RISK_RATING %>').show();
		}
		else {
			$('#<%= Configurations.FILTER_FIRST_RISK_RATING %>, #<%= Configurations.FILTER_LAST_RISK_RATING %>').hide();
		}
		
	}).trigger('change');

    createBT('.btitle');
});
//-->
</script>

<fieldset style="margin-bottom: 15px; padding-top:10px; ">
	<table width="100%">
		<tr>
			<td width="12%">
				<b><fmt:message key="filter" />:&nbsp;<a class="advancedFilter" href="javascript:advancedFilter();"><fmt:message key="filter.advanced"/></a></b>
			</td>
			<td width="12%">
				<input type="checkbox" name="filter" id="statusIni" value="${status_initiating}" style="width:20px;"/>&nbsp;
				<fmt:message key="project_status.INITIATING" />
			</td>
			<td width="12%">
				<input type="checkbox" name="filter" id="statusPla" value="${status_planning}" style="width:20px;"/>&nbsp;
				<fmt:message key="project_status.PLANNING" />
			</td>
			<td width="12%">
				<input type="checkbox" name="filter" id="statusCon" value="${status_control}" style="width:20px;"/>&nbsp;
				<fmt:message key="project_status.CONTROL" />
			</td>
			<td width="12%">
				<input type="checkbox" name="filter" id="statusClo" value="${status_closed}" style="width:20px;"  />&nbsp;
				<fmt:message key="project_status.CLOSED" />
			</td>
            <td width="12%">
				<input type="checkbox" name="filter" id="statusArchived" value="${statusArchived}" style="width:20px;"  />&nbsp;
				<fmt:message key="project_status.ARCHIVED" />
			</td>
			<td class="right">
				<input type="submit" class="boton" onclick="applyFilter()" style="width: 95px;" value="<fmt:message key="filter.apply" />"/>
				<a href="javascript:resetFilter();" class="boton"><fmt:message key="filter.reset" /></a>
			</td>
		</tr>
		<tr>
			<td>
				<b><fmt:message key="search" />:</b>
			</td>
			<td colspan="4">
				<input type="text" id="<%=Project.PROJECTNAME %>" class="campo" style="width: 300px;">
			</td>
			<th id="filterColumnSelected" class="hide"><fmt:message key="msg.info.filter_column_selected"/></th>
		</tr>
	</table>
	<div>&nbsp;</div>
	<table width="100%" class="center hide" id="advancedFilter">
		<tr>
			<th width="25%"><fmt:message key="customer_type" /></th>
			<th width="25%"><fmt:message key="customer" /></th>
			<th width="25%"><fmt:message key="program" /></th>
			<th width="25%"><fmt:message key="category" /></th>
		</tr>
		<tr>
			<td>
				<select id="idCustomerType" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="type" items="${cusType }">
						<option value="${type.idCustomerType }">${type.name }</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select id="idCustomer" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="customer" items="${customers }">
						<option value="${customer.idCustomer }" class="${customer.customertype.idCustomerType }">${customer.name }</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select id="idProgram" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="program" items="${programs }">
						<option value="${program.idProgram}">${program.programName }</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select id="<%=Category.IDCATEGORY %>" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="category" items="${categories }">
						<option value="${category.idCategory }">${category.name }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th width="25%"><fmt:message key="project_manager" /></th>
			<th width="25%"><fmt:message key="functional_manager" /></th>
			<th width="25%"><fmt:message key="seller" /></th>
			<th width="25%"><fmt:message key="sponsor" /></th>
		</tr>
		<tr>
			<td>
				<select id="idPM" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="pm" items="${projectManagers }">
						<option value="${pm.idEmployee}">${pm.contact.fullName }</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select id="<%= Configurations.LIST_FILTERS_FM %>" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="fm" items="${functionalManagers}">
						<option value="${fm.idEmployee}">${fm.contact.fullName}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select id="idSeller" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="seller" items="${sellers }">
						<option value="${seller.idSeller }">${seller.name }</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select id="idSponsor" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="sponsor" items="${sponsors }">
						<option value="${sponsor.idEmployee}">${sponsor.contact.fullName }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th width="25%"><fmt:message key="label" /></th>
			<th width="25%"><fmt:message key="stage_gates" /></th>
			<th width="25%"><fmt:message key="contract_type" /></th>
			<th width="25%"><fmt:message key="geography" /></th>
		</tr>
		<tr>
			<td>
				<select id="<%= Label.IDLABEL %>" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="label" items="${labels}">
						<option value="${label.idLabel}">${label.name}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select id="<%= Stagegate.IDSTAGEGATE %>" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="stageGate" items="${stageGates}">
						<option value="${stageGate.idStageGate}">${stageGate.name}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select id="<%= Configurations.LIST_FILTERS_CONTRACT_TYPE %>" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="contractType" items="${contractTypes}">
						<option value="${contractType.idContractType}">${contractType.description}</option>
					</c:forEach>
				</select>
			</td>
			<td>
				<select id="<%=Geography.IDGEOGRAPHY%>" style="height: 72px;" multiple="multiple" class="campo">
					<c:forEach var="geography" items="${geographys }">
						<option value="${geography.idGeography }">${geography.name }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
        <tr>
            <th style="width: 25%"><fmt:message key="rag" /></th>
            <th style="width: 25%"><fmt:message key="project.internal"/></th>
            <th style="width: 25%"><fmt:message key="proposal.requires_travelling"/></th>
            <th style="width: 25%"><fmt:message key="funding_source"/></th>
        </tr>
        <tr>
            <td>
                <select id="ragStatus" class="campo">
                    <option value="" selected><fmt:message key="show.all"/></option>
                    <option class="high_importance" value="<%=Constants.RISK_HIGH %>">&nbsp;&nbsp;&nbsp;</option>
                    <option class="medium_importance" value="<%=Constants.RISK_MEDIUM %>">&nbsp;&nbsp;&nbsp;</option>
                    <option class="normal_importance" value="<%=Constants.RISK_NORMAL %>">&nbsp;&nbsp;&nbsp;</option>
                    <option class="low_importance" value="<%=Constants.RISK_LOW %>">&nbsp;&nbsp;&nbsp;</option>
                </select>
            </td>
            <td>
                <select class="campo" id="filterInternal">
                    <option value=""><fmt:message key="project.internal.all"/></option>
                    <option value="false"><fmt:message key="project.internal.not"/></option>
                    <option value="true"><fmt:message key="project.internal.only"/></option>
                </select>
            </td>
            <td>
                <select class="campo" id="requiresTravelling">
                    <option value=""><fmt:message key="project.traveling.all"/></option>
                    <option value="false"><fmt:message key="project.traveling.not"/></option>
                    <option value="true"><fmt:message key="project.traveling.only"/></option>
                </select>
            </td>
            <td>
                <%-- Add all funding sources --%>
                <c:set var="idsFoundingSources"></c:set>
                <c:forEach var="fundingSource" items="${fundingSources }">
                    <c:choose>
                        <c:when test="${empty idsFoundingSources}">
                            <c:set var="idsFoundingSources">${fundingSource.idFundingSource}</c:set>
                        </c:when>
                        <c:otherwise>
                            <c:set var="idsFoundingSources">${idsFoundingSources},${fundingSource.idFundingSource}</c:set>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <select id="<%= Configurations.LIST_FILTERS_FOUNDING_SOURCE %>" class="campo" multiple="multiple">
                    <option value="${idsFoundingSources}"><fmt:message key="CLASSIFIED"/> <fmt:message key="funding_source"/></option>
                    <option value="<%=Constants.UNCLASSIFIED%>"><fmt:message key="UNCLASSIFIED"/> <fmt:message key="funding_source"/></option>
                    <c:forEach var="fundingSource" items="${fundingSources }">
                        <option value="${fundingSource.idFundingSource}">${fundingSource.name }</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <th><fmt:message key="project.priority" /></th>
            <th></th>
            <th></th>
            <th><fmt:message key="seller" /></th>
        </tr>
        <tr>
            <td>
                <input type="text" class="campo number" id="first" style="margin:0; width: 25px;" maxlength="3"/>
                <select class="campo" id="filterPriority"  style="margin-right:3px; width: 150px;">
                    <option value="" selected><fmt:message key="show.all"/></option>
                    <option value="<%=FiltroTabla.GREATHER_EQUAL%>"><fmt:message key="filter.greater_equal"/></option>
                    <option value="<%=FiltroTabla.LESS_EQUAL%>"><fmt:message key="filter.less_equal"/></option>
                    <option value="<%=FiltroTabla.BETWEEN%>"><fmt:message key="filter.between"/></option>
                </select>
                <input type="text" class="campo number" id="last" style="margin:0; width: 25px;" maxlength="3"/>
            </td>
            <td>
                <fmt:message key="project.budget_year" />
                <input type="text" class="campo" id="<%=Project.BUDGETYEAR %>" style="width:32px;" maxlength="4"/>
            </td>
            <td align="left">
                <fmt:message key="dates.since"/>:
                <input type="text" name="filter_start" id="filter_start" class="campo fecha" />
                &nbsp;&nbsp;
                <fmt:message key="dates.until"/>:
                <input type="text" name="filter_finish" id="filter_finish" class="campo fecha" />
            </td>
            <!--  Indirects directs sellers -->
            <td>
                <select class="campo" id="<%= Configurations.BOOLEAN_FILTER_SELLERS %>">
                    <option value=""><fmt:message key="procurement.all"/></option>
                    <option value="true"><fmt:message key="procurement.indirect"/></option>
                    <option value="false"><fmt:message key="procurement.direct"/></option>
                </select>
            </td>
        </tr>

        <%-- To porfolio manager --%>
        <tr>
            <th>
                <c:if test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM) %>">
                    <fmt:message key="perf_organization" />
                </c:if>
            </th>
        </tr>
        <tr>
            <td>
                <c:if test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM) %>">
                    <select id="idPO" style="height: 72px;" multiple="multiple"
                        class="campo">
                        <c:forEach var="po" items="${performingorgs }">
                            <option value="${po.idPerfOrg}">${po.name}</option>
                        </c:forEach>
                    </select>
                </c:if>
            </td>
        </tr>

        <%-- Project Risk Management --%>
        <tr>
            <th><fmt:message key="risk.rating" /></th>
            <th><fmt:message key="priorityAdjustment" /></th>
            <th><fmt:message key="classificationLevel" /></th>
            <th><fmt:message key="TECHNOLOGY" /></th>
        </tr>
        <tr>
            <td>
                <input type="text" class="campo number" id="<%= Configurations.FILTER_FIRST_RISK_RATING %>" style="margin:0; width: 35px;position:relative; top:-23px;"/>
                <select class="campo" id="<%= Configurations.FILTER_RISK_RATING %>"  style="margin-right:3px; width: 150px;position:relative; top:-23px;">
                    <option value="" selected><fmt:message key="show.all"/></option>
                    <option value="<%=FiltroTabla.GREATHER_EQUAL%>"><fmt:message key="filter.greater_equal"/></option>
                    <option value="<%=FiltroTabla.LESS_EQUAL%>"><fmt:message key="filter.less_equal"/></option>
                    <option value="<%=FiltroTabla.BETWEEN%>"><fmt:message key="filter.between"/></option>
                </select>
                <input type="text" class="campo number" id="<%= Configurations.FILTER_LAST_RISK_RATING %>" style="margin:0; width: 35px;position:relative; top:-23px;"/>
            </td>
            <td>
                <select class="campo" id="<%= Configurations.BOOLEAN_FILTER_PRIORITY_ADJUSTMENT %>" style="position:relative; top:-23px;">
                    <option value=""><fmt:message key="priorityAdjustment.all"/></option>
                    <option value="false"><fmt:message key="priorityAdjustment.not_including"/></option>
                    <option value="true"><fmt:message key="priorityAdjustment.only_including"/></option>
                </select>
            </td>
            <td>
                <select id="<%= Configurations.LIST_FILTERS_CLASSIFICATION_LEVEL %>" style="height: 72px;" multiple="multiple" class="campo">
                    <option value="<%=Constants.UNCLASSIFIED%>"><fmt:message key="UNCLASSIFIED"/> <fmt:message key="classificationLevel" /></option>
                    <c:forEach var="classificationlevel" items="${classificationsLevel}">
                        <option value="${classificationlevel.idClassificationlevel}" class="btitle" title="${classificationlevel.name}">${classificationlevel.name }</option>
                    </c:forEach>
                </select>
            </td>
            <td>
                <select id="<%= Configurations.LIST_FILTERS_TECHNOLOGY %>" style="height: 72px;" multiple="multiple" class="campo">
                    <c:forEach var="technology" items="${technologies}">
                        <option value="${technology.idTechnology}">${technology.name}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
	</table>
</fieldset>