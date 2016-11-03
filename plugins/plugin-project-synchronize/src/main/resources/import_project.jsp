<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectcharter"%>
<%@page import="es.sm2.openppm.front.servlets.SynchronizeServlet"%>
<%@page import="es.sm2.openppm.utils.functions.ValidateUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
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
  ~ File: import_project.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%
	HashMap<String, String> settings = SettingUtil.getSettings(request);
%>

<fmt:message key="yes" var="fmtYes"/>
<fmt:message key="no" var="fmtNo"/>
<fmt:message key="msg.confirm" var="fmtConfirm"/>
<fmt:message key="msg.warning.import_project" var="fmtMsg"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtShortNameRequired">
	<fmt:param><b><fmt:message key="project.chart_label"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtProjectNameRequired">
	<fmt:param><b><fmt:message key="project_name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtInvestmentManagerRequired">
	<fmt:param><b><fmt:message key="investment_manager"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtProjectManagerRequired">
	<fmt:param><b><fmt:message key="project_manager"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtProgramRequired">
	<fmt:param><b><fmt:message key="program"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtPlannedStartDateRequired">
	<fmt:param><b><fmt:message key="baseline_start"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtPlannedFinishDateRequired">
	<fmt:param><b><fmt:message key="baseline_finish"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtPlannedStartDateFormat">
	<fmt:param><b><fmt:message key="baseline_start"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtPlannedFinishDateFormat">
	<fmt:param><b><fmt:message key="baseline_finish"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtPlannedStartDateAfterFinish">
	<fmt:param><b><fmt:message key="baseline_start"/></b></fmt:param>
	<fmt:param><b><fmt:message key="baseline_finish"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtPlannedFinishDateBeforeStart">
	<fmt:param><b><fmt:message key="baseline_finish"/></b></fmt:param>
	<fmt:param><b><fmt:message key="baseline_start"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.out_of_range" var="fmtPriorityOutOfRange">
	<fmt:param><b><fmt:message key="project.priority"/></b></fmt:param>
	<fmt:param>0 - 99</fmt:param>
</fmt:message>
<fmt:message key="msg.error.out_of_range" var="fmtProbabilityOutOfRange">
	<fmt:param><b><fmt:message key="proposal.win_probability"/></b></fmt:param>
	<fmt:param>0 - 99</fmt:param>
</fmt:message>
<fmt:message key="msg.error.min_value" var="fmtSacMinValue">
	<fmt:param><b>0</b></fmt:param>
	<fmt:param><b><fmt:message key="sac"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.min_value" var="fmtEffortMinValue">
	<fmt:param><b>0</b></fmt:param>
	<fmt:param><b><fmt:message key="effort"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.maximum_allowed" var="fmtTcvMax">
	<fmt:param><b><fmt:message key="tcv"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.maximum_allowed" var="fmtBacMax">
	<fmt:param><b><fmt:message key="bac"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.maximum_allowed" var="fmtNiMax">
	<fmt:param><b><fmt:message key="project.net_value"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var validator;

var importData = {
	save : function() {
		if (validator.form() && validateDates()) {
			loadingPopup();
			document.frm_project.submit();
		}
	},
	confirm: function() {
		confirmUI('${fmtConfirm}','${fmtMsg}','${fmtYes}','${fmtNo}', importData.save);
	}
};

function validateDates() {

	stateValidate = false;
	
	if(!dateBefore($('#proj_initdate').html(), $('#plannedInitDate').val(), '${datePattern}') && $('#plannedInitDate').val() != "") { 
		informationSuccess("${fmt_plannedInitDate_before_project_initdate}");
	}
	if(!dateBefore($('#plannedInitDate').val(), $('#plannedFinishDate').val(), '${datePattern}')) { 
		alertUI("${fmt_error}","${fmt_planned_close_date_before_exec_date}");
	}
	else {
		stateValidate = true;
	}
	return stateValidate;
}

function confirmIniLink() {
	$('#toggleEditLinkIni').hide();
	$('#toggleALinkIni').show();
	
	$('#a_linkIni')
		.text($('#<%=Project.LINKDOC%>').val()).attr('href',$('#<%=Project.LINKDOC%>').val());
}
function modifyIniLink() {

	$('#toggleALinkIni').hide();
	$('#toggleEditLinkIni').show();
}

function searchProjectMember(name, role) {
	var perforg = $('#perforg').val();
	if (perforg != '') {
		searchEmployeePop(name, perforg, role);
	}
}

readyMethods.add(function() {
	
	validator = $("#frm_project").validate({
		errorContainer: 'div#project-errors',
		errorLabelContainer: 'div#project-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Project.CHARTLABEL%>: { required: true },
			projectname: { required: true },
			projectmanager_name : { required: true },
			<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>_name : { required: true },
			program: { required: true }, 
			<%=Project.PLANNEDINITDATE%> : { required: true, date : true, minDateTo: '#<%=Project.PLANNEDFINISHDATE%>' },
			<%=Project.PLANNEDFINISHDATE%> : { required: true, date : true, maxDateTo: '#<%=Project.PLANNEDINITDATE%>' },
			strategic_value: { range: [0,99] },
			'<%=Project.PROBABILITY%>${disableValidate}': { range: [0,99] },
			sac: { min: 0, integerPositive: true },
			effort: { min: 0 },
			successcriteria: { maxlength: 1500 },
			projectobjectives: { maxlength: 1500 },
			businessneed: { maxlength: 1500 },
			mainconstraints: { maxlength: 1500 },
			mainassumptions: { maxlength: 1500 },
			<%=Projectcharter.MAINDELIVERABLES%>: { maxlength: 1500 },
			<%=Projectcharter.EXCLUSIONS%>: { maxlength: 1500 },
			mainrisks: { maxlength: 1500 },
			tcv: { maxlength: <%=Constants.MAX_CURRENCY%> },
			bac: { maxlength: <%=Constants.MAX_CURRENCY%> },
			ni: { maxlength: <%=Constants.MAX_CURRENCY%> }
		},
		messages: {
			<%=Project.CHARTLABEL%> : {required: '${fmtShortNameRequired}' },
			projectname : {required: '${fmtProjectNameRequired}' },
			projectmanager_name : {required: '${fmtProjectManagerRequired}' },
			<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>_name : {required: '${fmtInvestmentManagerRequired}' },
			program : {required: '${fmtProgramRequired}' },
			<%=Project.PLANNEDINITDATE%>: { required: '${fmtPlannedStartDateRequired}', date: '${fmtPlannedStartDateFormat}', minDateTo: '${fmtPlannedStartDateAfterFinish}' },
			<%=Project.PLANNEDFINISHDATE%>: { required: '${fmtPlannedFinishDateRequired}', date: '${fmtPlannedFinishDateFormat}', maxDateTo: '${fmtPlannedFinishDateBeforeStart}' },
			strategic_value : { range : '${fmtPriorityOutOfRange}' },
			'<%=Project.PROBABILITY%>${disableValidate}' : { range : '${fmtProbabilityOutOfRange}' },
			sac : { min : '${fmtSacMinValue}' },
			effort : { min : '${fmtEffortMinValue}' },
			tcv: { maxlength: '${fmtTcvMax}' },
			bac: { maxlength: '${fmtBacMax}' },
			ni: { maxlength: '${fmtNiMax}' }
		}
	});
	
	$('#<%=Project.BUDGETYEAR%>').filterSelect({
		selectFilter:'program',
		emptyAll: true,
		showEmpty: true,
		between: true
	});
	
	$('#programSelect').val('${project.program.idProgram}');
	
	var dates = $('#plannedInitDate, #plannedFinishDate').datepicker({
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		onSelect: function(selectedDate) {
			var option = this.id == "plannedInitDate" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			dates.not(this).datepicker("option", option, date);
			if (validator.numberOfInvalids() > 0) validator.form();
		}
	});
	
	$("#saveImport").on('click',importData.confirm);
});
//-->
</script>

<form name="frm_project" id="frm_project" method="POST" action="<%=SynchronizeServlet.REFERENCE%>">
	<input type="hidden" name="accion" value="<%=SynchronizeServlet.SAVE_DATA%>" />
	<input type="hidden" id="id" name="id" value="${project.idProject}" />
	<input type="hidden" id="idprojectcharter" name="idprojectcharter" value="${projectCharter.idProjectCharter}" />
	<input type="hidden" name="scrollTop" />
	
	<div id="project-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<fmt:message key="file.import_data" var="titleImportData"/>
	<visual:panel id="tbProjectSync" title="${titleImportData }">
		<table width="100%">
			<tr>
				<th class="left" width="25%"><fmt:message key="proposal.bid_manager"/></th>
				<th class="left" width="25%"><fmt:message key="project_manager" /></th>
				<th class="left" width="25%"><fmt:message key="program" /></th>
				<th class="left" width="25%"><fmt:message key="perforg"/></th>
			</tr>
			<tr>
				<td>${tl:escape(projectSync.employeeByInvestmentManager.fullName) }</td>
				<td>${tl:escape(projectSync.employeeByProjectManager.fullName) }</td>
				<td>${tl:escape(projectSync.program) }</td>
				<td>${tl:escape(projectSync.performingorg) }</td>
			</tr>
			<tr>
				<th class="left"><fmt:message key="business_manager"/></th>
				<th class="left"><fmt:message key="sponsor" /></th>
				<th class="left"><fmt:message key="customer" /></th>
				<th class="left"><fmt:message key="category"/></th>
			</tr>
			<tr>
				<td>${tl:escape(projectSync.employeeByFunctionalManager.fullName) }</td>
				<td>${tl:escape(projectSync.employeeBySponsor.fullName) }</td>
				<td>${tl:escape(projectSync.customer) }</td>
				<td>${tl:escape(projectSync.category) }</td>
			</tr>
			<tr>
				<th class="left"><fmt:message key="contract_type"/></th>
				<th class="left"><fmt:message key="geography" /></th>
				<th class="left"><fmt:message key="stage_gate" /></th>
				<th></th>
			</tr>
			<tr>
				<td>${tl:escape(projectSync.contracttype) }</td>
				<td>${tl:escape(projectSync.geography) }</td>
				<td>${tl:escape(projectSync.stageGate.name) }</td>
				<td></td>
			</tr>
		</table>
		<div class="right">
			<button id="saveImport" class="boton" type="button"><fmt:message key="save_and_import" /></button>
		</div>
	</visual:panel>
	
	<fmt:message key="basic_data" var="titleBasicData"/>
	<visual:panel id="tbBasicData" title="${titleBasicData}">
		<table width="100%">
			<tr><td>&nbsp;</td></tr>
			<tr>
				<th class="left" width="25%"><fmt:message key="project.chart_label"/>*</th>
				<th class="left" colspan="3"><fmt:message key="project_name" />*</th>
				<th class="center" width="12%"><fmt:message key="project.budget_year"/></th>
				<th class="left" width="13%"><fmt:message key="project.accounting_code" /></th>
			</tr>
			<tr>
				<td><input type="text" class="campo" name="<%=Project.CHARTLABEL%>" id="<%=Project.CHARTLABEL%>" maxlength="25" value="${project.chartLabel }" style="width: 162px;" /></td>
				<td colspan="3">
					<input type="text" name="projectname" class="campo" value="${project.projectName}" maxlength="80"/>
				</td>
				<td class="center"><input type="text" id="<%=Project.BUDGETYEAR%>" name="<%=Project.BUDGETYEAR%>" value="${project.budgetYear }" maxlength="4" class="campo" style="width: 32px;"></td>
				<td><input type="text" name="<%=Project.ACCOUNTINGCODE%>" id="<%=Project.ACCOUNTINGCODE%>" class="campo" maxlength="25" value="${project.accountingCode}" style="width: 162px;" /></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<th class="left">
					<fmt:message key="proposal.bid_manager"/>*
					<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="searchEmployeePop('<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>', $('<%=Project.PERFORMINGORG%>').val(), <%=Constants.ROLE_IM%>);" />
				</th>
				<th class="left" width="25%">
					<fmt:message key="project_manager" />*
					<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="return searchProjectMember('projectmanager', <%=Constants.ROLE_PM%>);"/>
				</th>
				<th class="left" colspan="2"><fmt:message key="program" />&nbsp;*</th>
				<th colspan="2"><fmt:message key="category"/></th>
			</tr>
			<tr>
				<td>
					<input type="hidden" id="<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>" name="<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>" value="${project.employeeByInvestmentManager.idEmployee }"/>
					<input type="text" id="<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>_name" class="campo" name="<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>_name" value="${project.employeeByInvestmentManager.contact.fullName }" readonly/>
				</td>
				<td>
					<input type="hidden" id="projectmanager" name="projectmanager" value="${project.employeeByProjectManager.idEmployee }"/>
					<input type="text" id="projectmanager_name" class="campo" name="projectmanager_name" value="<c:out value="${project.employeeByProjectManager.contact.fullName }" />" readonly/>
				</td>
				<td colspan="2">
					<select name="program" id="programSelect" class="campo">
						<option value=""><fmt:message key="select_opt"/></option>
						<c:forEach var="program" items="${programs}">
							<option
								class='${tl:isNull(program.initBudgetYear) ? "empty" : program.initBudgetYear }'
								until='${tl:isNull(program.initBudgetYear) ? "" : program.finishBudgetYear }' value="${program.idProgram}" 
								${project.program.idProgram == program.idProgram ? "selected" : ""}>
								${program.programName}
							</option>
						</c:forEach>
					</select>
				</td>
				<td colspan="2">
					<select name="<%=Project.CATEGORY%>" id="<%=Project.CATEGORY%>" class="campo">
						<option value=""><fmt:message key="select_opt"/></option>
						<c:forEach var="category" items="${categories}">
							<option value="${category.idCategory}"
								${project.category.idCategory == category.idCategory ? "selected" : ""}>${category.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<th class="left">
					<fmt:message key="business_manager" />
					<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="return searchProjectMember('functionalmanager', <%=Constants.ROLE_FM%>);" />
				</th>
				<th class="left">
					<fmt:message key="sponsor" />
					<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="return searchProjectMember('sponsor', <%=Constants.ROLE_SPONSOR%>);" />
				</th>
				<th class="left" colspan="2"><fmt:message key="customer" />
					<img class="position_right icono"  title="<fmt:message key="add"/>" src="images/AddContact.png" onclick="return newCustomerPopup();"/>
				</th>
				<th class="center"><fmt:message key="tcv" /></th>
				<th class="center"><fmt:message key="bac" /></th>
			</tr>
			<tr>
				<td>
					<input type="hidden" id="functionalmanager" name="functionalmanager" value="${project.employeeByFunctionalManager.idEmployee }"/>
					<input type="text" class="campo" id="functionalmanager_name" name="functionalmanager_name" value="<c:out value="${project.employeeByFunctionalManager.contact.fullName }" />" readonly/>
				</td>
				<td>
					<input type="hidden" id="sponsor" name="sponsor" value="${project.employeeBySponsor.idEmployee }"/>
					<input type="text" class="campo" id="sponsor_name" name="sponsor_name" value="<c:out value="${project.employeeBySponsor.contact.fullName }" />" readonly/>
				</td>
				<td colspan="2">
					<select name="customer" id="customer" class="campo">
						<option value="" selected><fmt:message key="select_opt"/></option>
						<c:forEach var="customer" items="${customers}">
							<option value="${customer.idCustomer}"
								${project.customer.idCustomer == customer.idCustomer ? "selected" : ""}>
								${customer.name}
								</option>
						</c:forEach>
					</select>
				</td>
				<td class="center">
					<input type="text" id="tcv" name="tcv" title="${msg_tcv_desc}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.tcv}" />
				</td>
				<td class="center"><input type="text" id="bac" name="bac" title="${msg_bac_desc}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.bac}" /></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<th class="left"><fmt:message key="baseline_start" />&nbsp;*</th>
				<th class="left"><fmt:message key="baseline_finish" />&nbsp;*</th>
				<th class="left"><fmt:message key="sac" /></th>
				<th class="center"><fmt:message key="strategic_value"/></th>
				<th class="center"><fmt:message key="proposal.win_probability"/></th>
				<th class="center">
					<c:choose>
						<c:when test="<%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_INIT_EXTERNAL_COST_SHOW, Settings.DEFAULT_PROJECT_INIT_EXTERNAL_COST_SHOW))%>">
							<fmt:message key="external_costs"/>
						</c:when>
						<c:otherwise>
							&nbsp;
						</c:otherwise>
					</c:choose>	
				</th>
			</tr>
			<tr>
				<td>
					<input type="text" name="plannedInitDate" id="plannedInitDate" class="campo fecha"
						value="<fmt:formatDate value="${project.plannedInitDate}" pattern="${datePattern}" />"
					/>
				</td>
				<td>
					<input type="text" name="plannedFinishDate" id="plannedFinishDate" class="campo fecha" 
						value="<fmt:formatDate value="${project.plannedFinishDate}" pattern="${datePattern}" />"
					/>
				</td>
				<td>
					<nobr>
						<input type="text" name="sac" id="sac" class="campo" style="width:40px;margin:0px 2px 0px 5px;" value="${project.duration}"/>d
					</nobr>
				</td>
				<td class="center">
					<nobr>
						<input type="text" name="strategic_value" maxlength="3" style="width: 24px;" class="campo right" value="${project.priority}"/>%
					</nobr>
				</td>
				<td class="center">
					<nobr>
						<c:choose>
							<c:when test="<%=ValidateUtil.isNull(SettingUtil.getList(settings, Settings.SETTING_PROJECT_PROBABILITY, Settings.DEFAULT_PROJECT_PROBABILITY)[0])%>">
								<input type="text" id="<%=Project.PROBABILITY %>" name="<%=Project.PROBABILITY %>" title="${msg_bac_desc}" class="campo right" style="width:24px;margin:0px 2px 0px 0px;" maxlength="3" value="${project.probability}"/>%
							</c:when>
							<c:otherwise>
								<select id="<%=Project.PROBABILITY %>" name="<%=Project.PROBABILITY %>" class="campo" style="width:50px;">
									<c:forEach var="value" items="<%=SettingUtil.getList(settings, Settings.SETTING_PROJECT_PROBABILITY, Settings.DEFAULT_PROJECT_PROBABILITY)%>">
										<option value="${value}" ${value eq project.probability?"selected":""}>${value }</option>
									</c:forEach>
								</select>%
							</c:otherwise>
						</c:choose>
					</nobr>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<th><fmt:message key="contract_type" /></th>
				<th><fmt:message key="geography"/></th>
				<th><fmt:message key="stage_gate"/></th>
				<th colspan="3">&nbsp;</th>														
			</tr>
			<tr>
				<td>
					<select name="contracttype" class="campo">
						<option value='' selected><fmt:message key="select_opt" /></option>
						<c:forEach var="contractType" items="${contractTypes}">
							<option value="${contractType.idContractType}"
								${project.contracttype.idContractType == contractType.idContractType ? "selected" : ""}>
								${contractType.description}
							</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<select name="<%=Project.GEOGRAPHY%>" class="campo">
						<option value=''><fmt:message key="select_opt" /></option>
						<c:forEach var="geography" items="${geographies}">
							<option value="${geography.idGeography}"
									${project.geography.idGeography == geography.idGeography ? "selected" : ""}>
								${geography.name}</option>
						</c:forEach>
					</select>
				</td>
				<td class="center">
					<select name="<%=Project.STAGEGATE%>" class="campo">
						<option value=''><fmt:message key="select_opt" /></option>
						<c:forEach var="stageGate" items="${stageGates}">
							<option value="${stageGate.idStageGate}"
									${project.stagegate.idStageGate == stageGate.idStageGate ? "selected" : ""}>
								${stageGate.name}</option>
						</c:forEach>
					</select>
				</td>
				<td class="center" colspan="3">
					<div style="width: 50%; float: left;">
						<nobr>
							<input name="<%=Project.ISGEOSELLING %>" style="width:20px;" value="true" type="checkbox" ${project.isGeoSelling ? "checked" : ""}/>
							<fmt:message key="proposal.requires_travelling" />
						</nobr>
					</div>
					<div style="width: 50%; float: right;">
						<nobr>
							<input name="internalProject" style="width:20px;" id="internalProject" value="true" type="checkbox" ${project.internalProject ? "checked" : ""}/>
							<fmt:message key="project.internal_project" />
						</nobr>
					</div>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<th colspan="2"><fmt:message key="document_link"/></th>
				<c:choose>
					<c:when test="<%=Settings.OPTIONAL_CURRENCY%>">
						<th class="center"><fmt:message key="currency.opcional1"/></th>
						<th class="center"><fmt:message key="currency.opcional2"/></th>
						<th class="center"><fmt:message key="currency.opcional3"/></th>
						<th class="center"><fmt:message key="currency.opcional4"/></th>
					</c:when>
					<c:otherwise><th colspan="4"></th></c:otherwise>
				</c:choose>
			</tr>
			<tr>
				<td colspan="2">
					<c:choose>
						<c:when test="${project.linkDoc == null or empty project.linkDoc}">
							<c:set var="linkDocument">style="display:none;"</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="inputDocument">style="display:none;"</c:set>
						</c:otherwise>
					</c:choose>
					<div ${inputDocument } id="toggleEditLinkIni">
						<c:if test="${op:hasPermission(user,project.status,tab)}">
							<img style="width:16px;" src="images/approve.png" onclick="confirmIniLink()"/>
						</c:if>
						<input type="text" name="<%=Project.LINKDOC%>" id="<%=Project.LINKDOC%>" style="width: 375px; *width: 365px !important;" maxlength="200" class="campo" value="${project.linkDoc}" />
					</div>
					<div ${linkDocument} id="toggleALinkIni">
						<c:if test="${op:hasPermission(user,project.status,tab)}">
							<img style="width:16px;" src="images/modify.png" onclick="modifyIniLink()"/>
						</c:if>
						<a href="${project.linkDoc}" target="_blank" id="a_linkIni">${project.linkDoc}</a>
					</div>
				</td>
				<c:choose>
					<c:when test="<%=Settings.OPTIONAL_CURRENCY%>">
						<td class="center"><input type="text" name="currencyOptional1" id="currencyOptional1" title="<fmt:message key="currency.opcional1"/>" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional1}"/></td>
						<td class="center"><input type="text" name="currencyOptional2" id="currencyOptional2" title="<fmt:message key="currency.opcional2"/>" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional2}"/></td>
						<td class="center"><input type="text" name="currencyOptional3" id="currencyOptional3" title="<fmt:message key="currency.opcional3"/>" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional3}" /></td>
						<td class="center"><input type="text" name="currencyOptional4" id="currencyOptional4" title="<fmt:message key="currency.opcional4"/>" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional4}" /></td>
					</c:when>
					<c:otherwise><td colspan="4"></td></c:otherwise>
				</c:choose>
			</tr>
		</table>
	</visual:panel>
	<%-- PROJECT CHARTER --%>
	<fmt:message key="project_charter" var="titleProjectCharter"/>
	<visual:panel id="tbProjectCharter" title="${titleProjectCharter }">
		<table width="100%">
			<tr>
				<th class="left"><fmt:message key="project_charter.project_description" /></th>
				<th class="left"><fmt:message key="project_charter.project_objectives" /></th>
			</tr>
			<tr>
				<td class="center">
					<textarea name="successcriteria" class="campo show_scroll" rows="8" style="width:90%;">${projectCharter.sucessCriteria}</textarea>
				</td>
				<td class="center">
					<textarea name="projectobjectives" id="projectobjectives" class="campo show_scroll" rows="8" style="width:90%;">${projectCharter.projectObjectives}</textarea>
				</td>
			</tr>
			<tr>
				<th class="left"><fmt:message key="project_charter.business_need" /></th>
				<th class="left"><fmt:message key="project_charter.main_constraints" /></th>
			</tr>
			<tr>
				<td class="center">
					<textarea name="businessneed" id="businessneed" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.businessNeed}</textarea>
				</td>
				<td class="center">
					<textarea name="mainconstraints" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.mainConstraints}</textarea>
				</td>
			</tr>
			<tr>
				<th class="left"><fmt:message key="project_charter.main_risks" /></th>
				<th class="left"><fmt:message key="project_charter.main_assumptions" /></th>
			</tr>
			<tr>
				<td class="center">
					<textarea name="mainrisks" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.mainRisks}</textarea>
				</td>
				<td class="center">
					<textarea name="mainassumptions" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.mainAssumptions}</textarea>							
				</td>
			</tr>
			<tr>
				<th class="left"><fmt:message key="project_charter.main_deliverables" /></th>
				<th class="left"><fmt:message key="project_charter.exclusions" /></th>
			</tr>
			<tr>
				<td class="center">
					<textarea name="<%=Projectcharter.MAINDELIVERABLES%>" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.mainDeliverables}</textarea>
				</td>
				<td class="center">
					<textarea name="<%=Projectcharter.EXCLUSIONS%>" class="campo show_scroll" rows="5" style="width:90%;">${projectCharter.exclusions}</textarea>							
				</td>
			</tr>
		</table>
			
		<table width="100%">
			<tr>
				<th width="50%">&nbsp;</th>
				<th width="30%" align="right"><fmt:message key="internal_effort" /></th>
				<th width="20%" align="right"><fmt:message key="project.net_value" /></th>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td align="right">
					<input type="text" name="effort" id="effort" class="campo right" maxlength="5" style="width:40px;margin:0px 2px 0px 5px;" value="${project.effort}" />h							
				</td>
				<td align="right">
					<input type="text" name="ni" title="${msg_ni_desc}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.netIncome}"/>
				</td>
			</tr>
		</table>
	</visual:panel>
</form>

<jsp:include page="../../common/search_employee_popup.jsp" flush="true" />
<jsp:include page="../../common/new_customer_popup.jsp" flush="true"/>