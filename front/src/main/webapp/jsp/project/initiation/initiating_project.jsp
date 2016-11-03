<%@page import="es.sm2.openppm.core.model.impl.Projectcharter"%>
<%@page import="es.sm2.openppm.front.servlets.ChangePOServlet"%>
<%@page import="es.sm2.openppm.front.servlets.FollowProjectsServlet"%>
<%@page import="es.sm2.openppm.utils.functions.ValidateUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectInitServlet"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@ page import="es.sm2.openppm.core.logic.setting.RequiredFieldSetting" %>
<%@ page import="es.sm2.openppm.core.logic.setting.GeneralSetting" %>

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
  ~ File: initiating_project.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%
	HashMap<String, String> settings = SettingUtil.getSettings(request);
%>

<fmt:message key="msg.error.no_stakeholders_approve_project" var="msg_error_stakeholders" />
<fmt:message key="accept" var="msg_accept" />
<fmt:message key="msg.error.approve_project" var="msg_approve_project" />
<fmt:message key="yes" var="msg_yes" />
<fmt:message key="no" var="msg_no" />
<fmt:message key="tcv_desc" var="msg_tcv_desc" />
<fmt:message key="ni_desc" var="msg_ni_desc" />
<fmt:message key="bac_desc" var="msg_bac_desc" />
<fmt:message key="error" var="fmt_error" />

<fmt:message key="msg.info.execdate_before_project_initdate" var="fmt_plannedInitDate_before_project_initdate"/>
<fmt:message key="msg.error.planned_close_date_before_exec_date" var="fmt_planned_close_date_before_exec_date" />

<c:set var="is_pmo" value="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PMO)%>" />

<c:set var="stk_supporter"><%=Constants.TYPE_STAKEHOLDER_SUPPORTER%></c:set>
<c:set var="stk_neutral"><%=Constants.TYPE_STAKEHOLDER_NEUTRAL%></c:set>
<c:set var="stk_opponent"><%=Constants.TYPE_STAKEHOLDER_OPPONENT%></c:set>

<fmt:message var="fmt_internal" key="stakeholder.internal"/>
<fmt:message var="fmt_external" key="stakeholder.external"/>
<fmt:message var="fmt_supporter" key="stakeholder.supporter"/>
<fmt:message var="fmt_neutral" key="stakeholder.neutral"/>
<fmt:message var="fmt_opponent" key="stakeholder.opponent"/>

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
<fmt:message key="maintenance.error_msg_a" var="fmtBudgetYearRequired">
	<fmt:param><b><fmt:message key="project.budget_year"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtCategoryRequired">
	<fmt:param><b><fmt:message key="category"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtFunctionalManagerRequired">
	<fmt:param><b><fmt:message key="functional_manager"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtSponsorRequired">
	<fmt:param><b><fmt:message key="sponsor"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtStageGateRequired">
	<fmt:param><b><fmt:message key="stage_gate"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtPriorityRequired">
	<fmt:param><b><fmt:message key="project.priority"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtClassificationLevelRequired">
	<fmt:param><b><fmt:message key="classificationLevel"/></b></fmt:param>
</fmt:message>
<fmt:message key="validation.integer_positive" var="integerPositive" />
<fmt:message key="validation.valid_number" var="validNumber" />

<c:choose>
	<c:when test="${op:isCharEquals(type, typeInvestment)}">
		<c:set var="tab" scope="request"><%=Constants.TAB_INVESTMENT%></c:set>
		<c:set var="reqIM">*</c:set>	
		<c:set var="reqValidate"><%=Project.EMPLOYEEBYINVESTMENTMANAGER%>_name</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="reqPM">*</c:set>
		<c:set var="reqValidate">projectmanager_name</c:set>
	</c:otherwise>
</c:choose>
<c:if test="${not op:isStringEquals(project.status, status_initiating)}">
	<c:set var="disableValidate">readonly</c:set>
</c:if>

<c:set var="hasPermission">${op:hasPermissionSetting(settings, user,project.status,tab)}</c:set>

<%--
Request Attributes:
	contractTypes:	List of contract types
	programs:		List of programs
	employees:		List of employees of the perforg
	companies:		List of Companies
	project:		Project to plan
	projCharter:	Project Charter of project to plan
	profiles:		List of Resourceprofiles
	perforgs:		List of Performing Organization
--%>

<script type="text/javascript">
<!--
// Rules for validate project
var validator;
var initAjax = new AjaxCall('<%=ProjectInitServlet.REFERENCE%>','<fmt:message key="error" />');
var stakeholdersTable;

function changePO(){
	var f = document.forms["frm_project"];
	f.action = "<%=ChangePOServlet.REFERENCE%>";
	f.accion.value = "";
	loadingPopup();
	f.submit();
}

function resetEmployees() {
	$('#projectmanager').val('');
	$('#projectmanager_name').val('');
	$('#functionalmanager').val('');
	$('#functionalmanager_name').val('');
	$('#sponsor').val('');
	$('#sponsor_name').val('');	
}

function searchProjectMember(name, role) {
	var perforg = $('#perforg').val();
	if (perforg != '') {
		searchEmployeePop(name, perforg, role);
	}
}

function viewDocCharter() {
	var f = document.forms["frm_project"];
	f.action = "<%=ProjectInitServlet.REFERENCE%>";
	f.accion.value = "<%=ProjectInitServlet.VIEW_CHARTER%>";
	f.submit();	
}

function saveProject(viewCharter) {
	
	if (validator.form() && validateDates()) {
		
		dependences.addToResponse();
		
		var f 			= document.forms["frm_project"];
		f.action 		= "<%=ProjectInitServlet.REFERENCE%>";
		f.accion.value 	= '<%=ProjectInitServlet.JX_SAVE_PROJECT%>';
		
		initAjax.call($('#frm_project').serializeArray(), function(data) {
			
			$("#<%=Project.PLANNEDINITDATE%>").val(data.<%=Project.PLANNEDINITDATE%>);
			$("#<%=Project.PLANNEDFINISHDATE%>").val(data.<%=Project.PLANNEDFINISHDATE%>);
			if (viewCharter) { viewDocCharter(); }
		});
	} 
}

function deleteProject(idSelect) { $(idSelect+' option:selected').remove(); }

function newContactPopup() {
	$('#new-contact-popup').dialog('open');
}

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
		.text($('#<%=Project.LINKDOC%>')
		.val()).attr('href',$('#<%=Project.LINKDOC%>').val());
}
function modifyIniLink() {

	$('#toggleALinkIni').hide();
	$('#toggleEditLinkIni').show();
}
function updateTotalCosts() {
	
	var totalsellerscosts	= toNumber($('#totalsellerscosts').text());
	totalsellerscosts		= parseFloat(totalsellerscosts == ""?0:totalsellerscosts);
	
	var totalinfrastructurecosts = toNumber($('#totalinfrastructurecosts').text());
	totalinfrastructurecosts	 = parseFloat(totalinfrastructurecosts == ""?0:totalinfrastructurecosts);
	
	var totallicensescosts 	= toNumber($('#totallicensescosts').text());
	totallicensescosts 		= parseFloat(totallicensescosts == ""?0:totallicensescosts);
	
	var totalCosts = totalsellerscosts + totallicensescosts + totalinfrastructurecosts;
	
	$("#sumCosts").val(toCurrency(totalCosts));
}

function reportProject() {
	var f 			= document.forms["frm_project"];
	f.action 		= "<%=FollowProjectsServlet.REFERENCE%>";
	f.accion.value 	= "<%=FollowProjectsServlet.REPORT_PROJECT%>";
	$('#frm_project').attr("target","_blank");
	f.submit();
	
	$('#frm_project').removeAttr("target");
}

function loadChangeRequest() {
    $.ajax({
        url: 'rest/project/${project.idProject}/change-request',
        type: 'GET',
        dataType: 'json',
        contentType: 'application/json',
        data: {resolution: true},
        success: function (response) {

            var total = 0;

            $(response).each( function() {
                if (this.nodeDTOList !== undefined) {
                    $(this.nodeDTOList).each(function () {
                        total += this.estimatedCost;
                    });
                }
            });

            $('#totalChangeRequest').text(toCurrency(total));
        }
    });
}

// When document is ready
readyMethods.add(function() {

    loadChangeRequest();
	stakeholdersTable = $('#tb_stakeholders_master').dataTable({
		"oLanguage": datatable_language,
		"bPaginate": false,
		"bAutoWidth": false,
		"aoColumns": [
			{"bVisible": false },							  
		  	{"bVisible": false },
		  	{"bVisible": false },
		  	{"bVisible": false },
		  	{"bVisible": false },
            {"sClass": "left" }, 
            {"sClass": "left" }, 
            {"sClass": "left" },
            {"sClass": "left" },
            {"bVisible": false },
            {"bVisible": false },
            {"bVisible": false },
            {"bVisible": false },
            {"sClass": "center", "bSortable" : false },
            {"bVisible": false },
            {"bVisible": false }
     	],
     	"aaSorting": [[13, 'asc'], [0, 'asc']]
	});
	
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
			'${reqValidate}' : { required: true },
			program: { required: true }, 
			<%=Project.PLANNEDINITDATE%> : { required: true, date : true, minDateTo: '#<%=Project.PLANNEDFINISHDATE%>' },
			<%=Project.PLANNEDFINISHDATE%> : { required: true, date : true, maxDateTo: '#<%=Project.PLANNEDINITDATE%>' },
			strategic_value: { range: [0,99] },
			'<%=Project.PROBABILITY%>${disableValidate}': { range: [0,99] },
			sac: { min: 0, integerPositive: true },
			effort: { number:true, min: 0 , integerPositive: true},
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
			effort : { number: '${validNumber}', min : '${fmtEffortMinValue}', integerPositive: '${integerPositive}'},
			tcv: { maxlength: '${fmtTcvMax}' },
			bac: { maxlength: '${fmtBacMax}' },
			ni: { maxlength: '${fmtNiMax}' }
		}
	});
	
	updateTotalCosts();
	
	$('#<%=Project.BUDGETYEAR%>').filterSelect({
		selectFilter:'program',
		emptyAll: true,
		showEmpty: true,
		between: true
	});
	
	$('#programSelect').val('${project.program.idProgram}');
	
	$('#<%=Project.BUDGETYEAR%>').rules('add',{ 
		required: <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_BUDGET_YEAR, Settings.DEFAULT_INVESTMENT_REQUIRED_BUDGET_YEAR))%>,
		messages: {required: '${fmtBudgetYearRequired}' }
	});
	
	$('#<%=Project.CATEGORY%>').rules('add',{ 
		required: <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_CATEGORY, Settings.DEFAULT_INVESTMENT_REQUIRED_CATEGORY))%>,
		messages: {required: '${fmtCategoryRequired}' }
	});
	
	$("#functionalmanager_name").rules('add',{ 
		required: <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER, Settings.DEFAULT_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER))%>,
		messages: {required: '${fmtFunctionalManagerRequired}' }
	});
	
	$("#sponsor_name").rules('add',{ 
		required: <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_SPONSOR, Settings.DEFAULT_INVESTMENT_REQUIRED_SPONSOR))%>,
		messages: {required: '${fmtSponsorRequired}' }
	});
		
	$('#<%=Project.STAGEGATE%>').rules('add',{ 
		required: <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_STAGE_GATE, Settings.DEFAULT_INVESTMENT_REQUIRED_STAGE_GATE))%>,
		messages: {required: '${fmtStageGateRequired}' }
	});
	
	$("#strategic_value").rules('add',{ 
		required: <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_PRIORITY, Settings.DEFAULT_INVESTMENT_REQUIRED_PRIORITY))%>,
		messages: {required: '${fmtPriorityRequired}' }
	});

    $("#classificationlevel").rules('add',{
        required: <%=SettingUtil.getBoolean(settings, RequiredFieldSetting.CLASSIFICATION_LEVEL)%>,
        messages: {required: '${fmtClassificationLevelRequired}' }
    });

	$("#<%= Project.CLASSIFICATIONLEVEL%>").selectDescription({
		callback: function (data) {

			var $projectWarning = $('#projectWarning');
			if ($projectWarning) {

				var idClassificationlevel = data.find('option:selected').val();

				if (idClassificationlevel != $projectWarning.attr("idClassificationlevel")) {
					$projectWarning.show();
				}
				else {
					$projectWarning.hide();
				}
			}
		}
	});

    // Attributes effort by setting
    <% if (SettingUtil.getBoolean(settings, GeneralSetting.SUMMATION_EFFORT)) { %>
        $("#effort").attr("readonly", "");
        $("#effort").addClass("disabled");
    <% } %>

});
//-->
</script>
<c:set var="isPMO" value="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PMO)%>"/>

<c:if test="${op:isStringEquals(project.status, status_initiating) or isPMO}">
	<script>
		readyMethods.add(function() {
			var dates = $('#plannedInitDate, #plannedFinishDate').datepicker({
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				numberOfMonths: ${numberOfMonths},
				buttonImageOnly: true,
				onSelect: function(selectedDate) {
					var option = this.id == "plannedInitDate" ? "minDate" : "maxDate";
					var instance = $(this).data("datepicker");
					var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
					dates.not(this).datepicker("option", option, date);
					if (validator.numberOfInvalids() > 0) validator.form();
				}
			});
		});
	</script>
</c:if>	

<div id="projectTabs">
	
	<c:choose>
		<c:when test="${op:isCharEquals(type, typeInvestment)}">
			<script>
			function reloadInvestemnt() {
				var f = document.forms["frm_project"];
				f.action = "<%=ProjectInitServlet.REFERENCE%>";
				f.accion.value = "";
				loadingPopup();
				f.submit();
			}
			readyMethods.add(function() {
				$('#projectTabs').tabs({
					select: function(event, ui) { event.preventDefault(); }
				});
			});
			</script>
			<ul>
				<li><a href="#investmentTab" onclick="reloadInvestemnt()"><fmt:message key="initiation"/></a></li>
			</ul>
		   <c:set var="status"><fmt:message key="investments.status.${project.investmentStatus}" /></c:set>
		</c:when>
		<c:otherwise>
		   <c:set var="status"><fmt:message key="project_status.${project.status}" /></c:set>
			<jsp:include page="../common/header_project.jsp">
				<jsp:param value="I" name="actual_page"/>
			</jsp:include>
		</c:otherwise>
	</c:choose>

    <%-- PROJECT DATA --%>
    <jsp:include page="../common/info_project.jsp" flush="true" />

	<div id="panels" style="padding: 15px;">
		<form name="frm_project" id="frm_project" method="post">
			<input type="hidden" name="accion" value="" />
			<input type="hidden" name="id" id="id" value="${project.idProject}" />
			<input type="hidden" name="status" id="status" value="${project.status}" />
			<input type="hidden" name="idprojectcharter" id="idprojectcharter" value="${projCharter.idProjectCharter}" />
			<input type="hidden" id="initdate" value="${project.initDate}" />
			<input type="hidden" name="type" value="${type}" />
			<input type="hidden" name="perforg" id="perforg" value="${project.performingorg.idPerfOrg }" />
			<input type="hidden" id="idDocument" name="idDocument" />
			<input type="hidden" id="infoDependens" name="infoDependens" />
			<input type="hidden" id="infoLeads" name="infoLeads" />
			<input type="hidden" name="scrollTop" />
			
			<div id="project-errors" class="ui-state-error ui-corner-all hide">
				<p>
					<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
					<strong><fmt:message key="msg.error_title"/></strong>
					&nbsp;(<b><span id="numerrors"></span></b>)
				</p>
				<ol></ol>
			</div>

			<c:if test="${not empty classificationlevel}">
				<div id="projectWarning" idClassificationlevel="${classificationlevel.idClassificationlevel }" style="margin-bottom: 10px; padding: 0pt 0.7em;" class="ui-state-highlight ui-corner-all hide">
					<p>
						<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
						<strong><fmt:message key="msg.info"/>: </strong>
					</p>
					<p>
						<fmt:message key="classificationLevel.warning">
							<fmt:param><b><fmt:message key="classificationLevel"/></b></fmt:param>
						</fmt:message>
					</p>
				</div>
			</c:if>
			
			<fmt:message key="basic_data" var="titleBasicData"/>
			<visual:panel id="tb_basic_data" title="${titleBasicData}">
			
				<%-- Add plugin --%>
				<div id="projectData" style="display:none;"></div>
				
				<table width="100%">
					<tr><td>&nbsp;</td></tr>
					<tr>
						<th class="left" ><fmt:message key="project.chart_label"/>*</th>
						<th class="left"  colspan="3"><fmt:message key="project_name" />*</th>
						<th class="left"  colspan="2"><fmt:message key="program" />&nbsp;*</th>
						<th class="center" ><fmt:message key="project.budget_year"/><c:if test="<%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_BUDGET_YEAR, Settings.DEFAULT_INVESTMENT_REQUIRED_BUDGET_YEAR))%>">*</c:if></th>
						<th class="left" ><fmt:message key="project.accounting_code" /></th>
					</tr>
					<tr>
						<td><input type="text" style="width:155px;" class="campo" name="<%=Project.CHARTLABEL%>" id="<%=Project.CHARTLABEL%>" maxlength="25" value="${project.chartLabel }" /></td>
						<td colspan="3">
							<c:choose>
								<c:when test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PMO)%>">
									<input type="text" name="projectname" class="campo" value="${tl:escape(project.projectName)}" maxlength="80"/>
								</c:when>
								<c:when test="${not op:isStringEquals(project.status, status_initiating) and not op:isStringEquals(project.status, status_planning)}">
									<input type="text" name="projectname" value="${tl:escape(project.projectName)}" maxlength="80" readonly/>
								</c:when>
								<c:otherwise>
									<input type="text" name="projectname" class="campo" value="${tl:escape(project.projectName)}" maxlength="80"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td colspan="2">
							<c:choose>
								<c:when test="${((op:isStringEquals(project.status,status_initiating) or op:isStringEquals(project.status, status_planning)) and op:hasPermission(user,project.status,tab)) or isPMO}">
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
								</c:when>
								<c:otherwise>
									<input type="hidden" name="program" id="program" value="${project.program.idProgram}"/>
									<span>${project.program.programName}</span>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="center"><input type="text" id="<%=Project.BUDGETYEAR%>" name="<%=Project.BUDGETYEAR%>" value="${project.budgetYear }" maxlength="4" class="campo" style="width: 32px;"></td>
						<td><input type="text" name="<%=Project.ACCOUNTINGCODE%>" id="<%=Project.ACCOUNTINGCODE%>" class="campo" maxlength="25" value="${project.accountingCode}" style="width: 162px;" /></td>
					</tr>
					
					<tr><td>&nbsp;</td></tr>
					
					<tr>
						<th class="left" colspan="2" style="width:25%">
							<fmt:message key="proposal.bid_manager"/>${reqIM}
							<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">				
								<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="searchEmployeePop('<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>', $('<%=Project.PERFORMINGORG%>').val(), <%=Constants.ROLE_IM%>);" />
							</c:if>
						</th>
						<th class="left" colspan="2" style="width:25%">
							<fmt:message key="project_manager" />${reqPM}
							<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">					
								<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="return searchProjectMember('projectmanager', <%=Constants.ROLE_PM%>);"/>
							</c:if>
						</th>
						<th class="left" colspan="2" style="width:25%">
							<fmt:message key="business_manager" /><c:if test="<%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER, Settings.DEFAULT_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER))%>">*</c:if>
							<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
								<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="return searchProjectMember('functionalmanager', <%=Constants.ROLE_FM%>);" />
							</c:if>
						</th>
						<th class="left" colspan="2" style="width:25%">
							<fmt:message key="sponsor" /><c:if test="<%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_SPONSOR, Settings.DEFAULT_INVESTMENT_REQUIRED_SPONSOR))%>">*</c:if>
							<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
								<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="return searchProjectMember('sponsor', <%=Constants.ROLE_SPONSOR%>);" />
							</c:if>
						</th>
					</tr>
					<tr>
						<td colspan="2">
							<input type="hidden" id="<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>" name="<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>" value="${project.employeeByInvestmentManager.idEmployee }"/>
							<input type="text" id="<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>_name" class="campo" name="<%=Project.EMPLOYEEBYINVESTMENTMANAGER%>_name" value="${project.employeeByInvestmentManager.contact.fullName }" readonly/>
						</td>
						<td colspan="2">
							<input type="hidden" id="projectmanager" name="projectmanager" value="${project.employeeByProjectManager.idEmployee }"/>
							<input type="text" id="projectmanager_name" class="campo" name="projectmanager_name" value="<c:out value="${project.employeeByProjectManager.contact.fullName }" />" readonly/>
						</td>
						<td colspan="2">
							<input type="hidden" id="functionalmanager" name="functionalmanager" value="${project.employeeByFunctionalManager.idEmployee }"/>
							<input type="text" class="campo" id="functionalmanager_name" name="functionalmanager_name" value="<c:out value="${project.employeeByFunctionalManager.contact.fullName }" />" readonly/>
						</td>
						<td colspan="2">
							<input type="hidden" id="sponsor" name="sponsor" value="${project.employeeBySponsor.idEmployee }"/>
							<input type="text" class="campo" id="sponsor_name" name="sponsor_name" value="<c:out value="${project.employeeBySponsor.contact.fullName }" />" readonly/>
						</td>
					</tr>
					
					<tr><td>&nbsp;</td></tr>
					
					<tr>
						<th colspan="2"><fmt:message key="category"/><c:if test="<%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_CATEGORY, Settings.DEFAULT_INVESTMENT_REQUIRED_CATEGORY))%>">*</c:if></th>
						<th class="left" colspan="2"><fmt:message key="customer" />
							<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
								<img class="position_right icono"  title="<fmt:message key="add"/>" src="images/AddContact.png" onclick="return newCustomerPopup();"/>
							</c:if>
						</th>
						<th class="center"><fmt:message key="tcv" /></th>
						<th class="center"><fmt:message key="bac" /></th>
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
                        <th class="center"><fmt:message key="PROJECT.TOR" /></th>
                    </tr>
					<tr>
						<td colspan="2">
							<select name="<%=Project.CATEGORY%>" id="<%=Project.CATEGORY%>" class="campo">
								<option value=""><fmt:message key="select_opt"/></option>
								<c:forEach var="category" items="${categories}">
									<option value="${category.idCategory}"
										${project.category.idCategory == category.idCategory ? "selected" : ""}>${category.name}</option>
								</c:forEach>
							</select>
						</td>
						<td colspan="2">
							<c:choose>
								<c:when test="${not op:isStringEquals(project.status, status_closed)}">
									<select name="customer" id="customer" class="campo">
										<option value="" selected><fmt:message key="select_opt"/></option>
										<c:forEach var="customer" items="${customers}">
											<option value="${customer.idCustomer}"
												${project.customer.idCustomer == customer.idCustomer ? "selected" : ""}>
												${customer.name}
												</option>
										</c:forEach>
									</select>
								</c:when>
								<c:otherwise>
									<input type="hidden" name="customer" id="customer" value="${project.customer.idCustomer}"/>
									<input type="text" value="${project.customer.name}"/>
								</c:otherwise>
							</c:choose>
						</td>
						<td class="center">
							<c:choose>
								<c:when test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PMO)%>">
									<input type="text" id="tcv" name="tcv" title="${msg_tcv_desc}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.tcv}" />
								</c:when>
								<c:when test="${not op:isStringEquals(project.status, status_initiating)}">
									<input type="text" id="tcv" name="tcv" title="${msg_tcv_desc}" class="importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.tcv}" readonly/>
								</c:when>
								<c:otherwise>
									<input type="text" id="tcv" name="tcv" title="${msg_tcv_desc}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.tcv}" />
								</c:otherwise>
							</c:choose>						
						</td>
						<td class="center"><input type="text" id="bac" name="bac" title="${msg_bac_desc}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.bac}" /></td>
						<td>
							<c:choose>
								<c:when test="<%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_INIT_EXTERNAL_COST_SHOW, Settings.DEFAULT_PROJECT_INIT_EXTERNAL_COST_SHOW))%>">
									<input type="text" id="sumCosts" value="" class="right" readonly />
								</c:when>
								<c:otherwise>
									<input type="text" id="sumCosts" value="" class="right" readonly style="visibility: hidden;" />
								</c:otherwise>
							</c:choose>
						</td>
                        <td class="right" id="totalChangeRequest"></td>
                    </tr>
					
					<tr><td>&nbsp;</td></tr>
					
					<tr>
						<th class="left"><fmt:message key="baseline_start" />&nbsp;*</th>
						<th class="left" style="width:12%"><fmt:message key="baseline_finish" />&nbsp;*</th>
						<th class="center" colspan="1"><fmt:message key="sac" /></th>
						<th class="center" colspan="1"><fmt:message key="strategic_value"/><c:if test="<%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_PRIORITY, Settings.DEFAULT_INVESTMENT_REQUIRED_PRIORITY))%>">*</c:if></th>
						<th class="center" colspan="1"><fmt:message key="proposal.win_probability"/></th>
						<th colspan="2"><fmt:message key="contract_type" /></th>
						<th colspan="2"><fmt:message key="geography"/></th>
					</tr>
					<tr>
						<c:choose>
							<c:when test="${not op:isStringEquals(project.status, status_initiating) and not isPMO}">
								<td><fmt:formatDate value="${project.plannedInitDate}" pattern="${datePattern}" /></td>
								<td>
                                    <fmt:formatDate value="${project.plannedFinishDate}" pattern="${datePattern}" />
                                    <input type="hidden" name="plannedInitDate" id="plannedInitDate"
                                           value="<fmt:formatDate value="${project.plannedInitDate}" pattern="${datePattern}" />"
                                            />
                                    <input type="hidden" name="plannedFinishDate" id="plannedFinishDate"
                                           value="<fmt:formatDate value="${project.plannedFinishDate}" pattern="${datePattern}" />"
                                            />
                                </td>
							</c:when>
							<c:otherwise>
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
							</c:otherwise>
						</c:choose>
						<td colspan="1" class="center">
							<nobr>
								<input type="text" name="sac" id="sac" class="campo" style="width:40px;margin:0px 2px 0px 5px;" value="${project.duration}"/>d
							</nobr>
						</td>
						<td class="center" colspan="1">
							<nobr>
								<input type="text" name="strategic_value" id="strategic_value" maxlength="3" style="width: 24px;" class="campo right" value="${project.priority}"/>%
							</nobr>
						</td>
						<td class="center" colspan="1">
							<nobr>
								<c:choose>
									<c:when test="<%=ValidateUtil.isNull(SettingUtil.getList(settings, Settings.SETTING_PROJECT_PROBABILITY, Settings.DEFAULT_PROJECT_PROBABILITY)[0])%>">
										<input type="text" id="<%=Project.PROBABILITY %>" name="<%=Project.PROBABILITY %>" title="${msg_bac_desc}" class="campo right" style="width:24px;margin:0px 2px 0px 0px;" maxlength="3" value="${project.probability}" ${disableValidate }/>%
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${op:isStringEquals(project.status, status_initiating)}">
												<select id="<%=Project.PROBABILITY %>" name="<%=Project.PROBABILITY %>" class="campo" style="width:50px;" ${disableValidate }>
													<c:forEach var="value" items="<%=SettingUtil.getList(settings, Settings.SETTING_PROJECT_PROBABILITY, Settings.DEFAULT_PROJECT_PROBABILITY)%>">
														<option value="${value}" ${value eq project.probability?"selected":""}>${value }</option>
													</c:forEach>
												</select>%
											</c:when>
											<c:otherwise>
												<input type="text" id="<%=Project.PROBABILITY %>" name="<%=Project.PROBABILITY %>" title="${msg_bac_desc}" class="campo right" style="width:24px;margin:0px 2px 0px 0px;" maxlength="3" value="${project.probability}" readonly />%	
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</nobr>
						</td>
						<td colspan="2">
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
						<td colspan="2">
							<select name="<%=Project.GEOGRAPHY%>" class="campo">
								<option value=''><fmt:message key="select_opt" /></option>
								<c:forEach var="geography" items="${geographies}">
									<option value="${geography.idGeography}"
											${project.geography.idGeography == geography.idGeography ? "selected" : ""}>
										${geography.name}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					
					<tr><td>&nbsp;</td></tr>
					
					<tr>
						<th colspan="2"><fmt:message key="stage_gate"/><c:if test="<%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_STAGE_GATE, Settings.DEFAULT_INVESTMENT_REQUIRED_STAGE_GATE))%>">*</c:if></th>
						<th colspan="2"><fmt:message key="classificationLevel"/><c:if test="<%=SettingUtil.getBoolean(settings, RequiredFieldSetting.CLASSIFICATION_LEVEL)%>">*</c:if></th>
						<th colspan="4"><fmt:message key="document_link"/></th>
					</tr>
					<tr>
						<td class="center" colspan="2">
							<select name="<%=Project.STAGEGATE%>" class="campo" id="<%=Project.STAGEGATE%>">
								<option value=''><fmt:message key="select_opt" /></option>
								<c:forEach var="stageGate" items="${stageGates}">
									<option value="${stageGate.idStageGate}"
											${project.stagegate.idStageGate == stageGate.idStageGate ? "selected" : ""}>
										${stageGate.name}</option>
								</c:forEach>
							</select>
						</td>
						<td colspan="2">
							<div id="contentClassificationLevel">
								<select name="<%= Project.CLASSIFICATIONLEVEL%>" id="<%= Project.CLASSIFICATIONLEVEL%>" class="campo">
									<option value="" selected><fmt:message key="select_opt"/></option>
									<c:forEach var="classificationLevel" items="${classificationsLevel}">
										<option value="${classificationLevel.idClassificationlevel}"
											${project.classificationlevel.idClassificationlevel == classificationLevel.idClassificationlevel ? "selected" : ""}
											description="${tl:escape(classificationLevel.description)}">${tl:escape(classificationLevel.name)}</option>
									</c:forEach>
								</select>
							</div>
						</td>
						<td colspan="4">
							<c:choose>
								<c:when test="${project.linkDoc == null or empty project.linkDoc}">
									<c:set var="linkDocument">style="display:none;"</c:set>
								</c:when>
								<c:otherwise>
									<c:set var="inputDocument">style="display:none;"</c:set>
								</c:otherwise>
							</c:choose>
							<div ${inputDocument } id="toggleEditLinkIni">
								<nobr>
									<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
										<img style="width:16px;" src="images/approve.png" onclick="confirmIniLink()"/>
									</c:if>
									<input type="text" name="<%=Project.LINKDOC%>" id="<%=Project.LINKDOC%>" style="" maxlength="200" class="campo" value="${project.linkDoc}" />
								</nobr>
							</div>
							<div ${linkDocument} id="toggleALinkIni">
								<nobr>
									<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
										<img style="width:16px;" src="images/modify.png" onclick="modifyIniLink()"/>
									</c:if>
									<a href="${project.linkDoc}" target="_blank" id="a_linkIni">${project.linkDoc}</a>
								</nobr>
							</div>
						</td>
					</tr>
					
					<tr><td>&nbsp;</td></tr>
					
					<tr>
						<th colspan="2"></th>
						<th colspan="2"></th>
						<c:choose>
							<c:when test="<%=Settings.OPTIONAL_CURRENCY%>">
								<th class="center" colspan="1"><fmt:message key="currency.opcional1"/></th>
								<th class="center" colspan="1"><fmt:message key="currency.opcional2"/></th>
								<th class="center" colspan="1"><fmt:message key="currency.opcional3"/></th>
								<th class="center" colspan="1"><fmt:message key="currency.opcional4"/></th>
							</c:when>
							<c:otherwise><th colspan="4"></th></c:otherwise>
						</c:choose>
					</tr>
					<tr>
						<td colspan="2" class="center">
							<div style="float: left;">
								<nobr>
									<input name="<%=Project.ISGEOSELLING %>" id="<%=Project.ISGEOSELLING %>"
                                           style="width:20px;" value="true" type="checkbox" ${project.isGeoSelling ? "checked" : ""}>
									<fmt:message key="proposal.requires_travelling" />
								</nobr>
							</div>
							<div style=" float: right;">
								<nobr>
									<input name="internalProject" style="width:20px;" id="internalProject" value="true" type="checkbox" ${project.internalProject ? "checked" : ""}>
									<fmt:message key="project.internal_project" />
								</nobr>
							</div>
						</td>
						<td colspan="2"></td>
						<c:choose>
							<c:when test="<%=Settings.OPTIONAL_CURRENCY%>">
								<td class="center" colspan="1"><input type="text" name="currencyOptional1" id="currencyOptional1" title="<fmt:message key="currency.opcional1"/>" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional1}"/></td>
								<td class="center" colspan="1"><input type="text" name="currencyOptional2" id="currencyOptional2" title="<fmt:message key="currency.opcional2"/>" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional2}"/></td>
								<td class="center" colspan="1"><input type="text" name="currencyOptional3" id="currencyOptional3" title="<fmt:message key="currency.opcional3"/>" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional3}" /></td>
								<td class="center" colspan="1"><input type="text" name="currencyOptional4" id="currencyOptional4" title="<fmt:message key="currency.opcional4"/>" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.currencyOptional4}" /></td>
							</c:when>
							<c:otherwise><td colspan="4"></td></c:otherwise>
						</c:choose>
					</tr>
					
					<tr><td>&nbsp;</td></tr>
				</table>

                <div style="overflow: auto;">
                    <jsp:include page="dependencies.inc.jsp" flush="true" />
                </div>

                <div align="right" style="margin-top:10px;display:${hasPermission ? 'block' : 'none'}" id="buttonsProjectData">
                    <%-- CHANGE PO --%>
                    <c:if test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PMO)%>">
                        <a href="javascript: changePO();" class="boton"><fmt:message key="perf_organization.change" /></a>
                    </c:if>
                    <a href="javascript: saveProject();" class="boton"><fmt:message key="save" /></a>
                </div>
			</visual:panel>
			
			<%-- STAKEHOLDERS --%>
			<fmt:message key="stakeholders" var="titleStakeholder"/>
			<visual:panel id="tb_stakeholders" title="${titleStakeholder }">
				<table id="tb_stakeholders_master" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th width="0%">&nbsp;</th>							
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="42%"><fmt:message key="stakeholder.name" /></th>
							<th width="25%"><fmt:message key="stakeholder.rol" /></th>
							<th width="15%"><fmt:message key="stakeholder.classification" /></th>
							<th width="10%"><fmt:message key="stakeholder.type" /></th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%"><fmt:message key="stakeholder.comments" /></th>
							<th width="8%">
								<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
									<img onclick="newStakeholder()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
								</c:if>
							</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="stakeholder" items="${stakeholders}">
							<tr>
								<td>${stakeholder.idStakeholder}</td>								
								<td>${tl:escape(stakeholder.requirements)}</td>
								<td>${tl:escape(stakeholder.expectations)}</td>
								<td>${tl:escape(stakeholder.influence)}</td>
								<td>${tl:escape(stakeholder.mgtStrategy)}</td>
								<c:choose>
									<c:when test="${stakeholder.contactName == null}">
										<td>${stakeholder.employee.contact.fullName}</td>
									</c:when>
									<c:otherwise>
										<td>${tl:escape(stakeholder.contactName)}</td>
									</c:otherwise>
								</c:choose>
								<td>${tl:escape(stakeholder.projectRole)}</td>
								<td>
									${tl:escape(stakeholder.stakeholderclassification.name)}
								</td>
								<td>
									<c:choose>
										<c:when test="${op:isCharEquals(stakeholder.type, stk_supporter)}">
											${fmt_supporter}
										</c:when>
										<c:when test="${op:isCharEquals(stakeholder.type, stk_neutral)}">
											${fmt_neutral}
										</c:when>
										<c:when test="${op:isCharEquals(stakeholder.type, stk_opponent)}">
											${fmt_opponent}
										</c:when>
									</c:choose>
								</td>
								<td>${stakeholder.stakeholderclassification.idStakeholderClassification}</td>
								<td>${tl:escape(stakeholder.type)}</td>
								<td>${tl:escape(stakeholder.department)}</td>
								<td>${tl:escape(stakeholder.comments)}</td>
								<td>
									<img onclick="viewStakeholder(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
									<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
										&nbsp;&nbsp;&nbsp;
										<img onclick="deleteStakeholder(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
									</c:if>
								</td>
								<td>${stakeholder.orderToShow}</td>
								<td>${stakeholder.employee.idEmployee}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</visual:panel>
			
			<%-- PROJECT CHARTER --%>
			<fmt:message key="project_charter" var="titleProjectCharter"/>
			<visual:panel id="tb_project_charter" title="${titleProjectCharter }">
				<div align="right" style="margin-top:10px;">
					<a class="boton" href="javascript:${(op:hasPermission(user,project.status,tab))?"saveProject(true)":"viewDocCharter()"};"><fmt:message key="generate_project_charter" /></a>
					<a href="javascript: reportProject();" class="boton"><fmt:message key="report"/></a>
					<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
						<a href="javascript: saveProject();" class="boton"><fmt:message key="save" /></a>
					</c:if>
				</div>
				<table width="100%">
					<tr>
						<th class="left"><fmt:message key="project_charter.project_description" /></th>
						<th class="left"><fmt:message key="project_charter.project_objectives" /></th>
					</tr>
					<tr>
						<td class="center">
							<textarea name="successcriteria" class="campo show_scroll" rows="8" style="width:90%;">${projCharter.sucessCriteria}</textarea>
						</td>
						<td class="center">
							<textarea name="projectobjectives" id="projectobjectives" class="campo show_scroll" rows="8" style="width:90%;">${projCharter.projectObjectives}</textarea>
						</td>
					</tr>
					<tr>
						<th class="left"><fmt:message key="project_charter.business_need" /></th>
						<th class="left"><fmt:message key="project_charter.main_constraints" /></th>
					</tr>
					<tr>
						<td class="center">
							<textarea name="businessneed" id="businessneed" class="campo show_scroll" rows="5" style="width:90%;">${projCharter.businessNeed}</textarea>
						</td>
						<td class="center">
							<textarea name="mainconstraints" class="campo show_scroll" rows="5" style="width:90%;">${projCharter.mainConstraints}</textarea>
						</td>
					</tr>
					<tr>
						<th class="left"><fmt:message key="project_charter.main_risks" /></th>
						<th class="left"><fmt:message key="project_charter.main_assumptions" /></th>
					</tr>
					<tr>
						<td class="center">
							<textarea name="mainrisks" class="campo show_scroll" rows="5" style="width:90%;">${projCharter.mainRisks}</textarea>
						</td>
						<td class="center">
							<textarea name="mainassumptions" class="campo show_scroll" rows="5" style="width:90%;">${projCharter.mainAssumptions}</textarea>							
						</td>
					</tr>
					<tr>
						<th class="left"><fmt:message key="project_charter.main_deliverables" /></th>
						<th class="left"><fmt:message key="project_charter.exclusions" /></th>
					</tr>
					<tr>
						<td class="center">
							<textarea name="<%=Projectcharter.MAINDELIVERABLES%>" class="campo show_scroll" rows="5" style="width:90%;">${projCharter.mainDeliverables}</textarea>
						</td>
						<td class="center">
							<textarea name="<%=Projectcharter.EXCLUSIONS%>" class="campo show_scroll" rows="5" style="width:90%;">${projCharter.exclusions}</textarea>							
						</td>
					</tr>
				</table>
					
				<jsp:include page="projectcharter_costs.inc.jsp" flush="true" />
	
				<table width="100%" id="effortAndNet">
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
							<input type="text" name="ni" id="ni" title="${msg_ni_desc}" class="campo importe" style="width:85px;margin:0px 2px 0px 0px;" value="${project.netIncome}"/>
						</td>
					</tr>
				</table>
			</visual:panel>
				
		</form>
		
		<%-- FUNDINGSOURCES --%>
		<fmt:message var="fundingTitle" key="funding_source" />
		<visual:panel id="fundingSourcePanel" title="${fundingTitle }">
			<jsp:include page="fundingsource.inc.jsp" flush="true" />
		</visual:panel>
		
		<%-- LABELS --%>
		<fmt:message var="labelTitle" key="labels" />
		<visual:panel id="labelsPanel" title="${labelTitle }">
			<jsp:include page="labels.inc.jsp" flush="true" />
		</visual:panel>

        <%-- TECHNOLOGIES --%>
        <fmt:message var="technologiesTitle" key="TECHNOLOGIES_MAP" />
        <visual:panel id="technologiesPanel" title="${technologiesTitle }">
            <jsp:include page="technologies.inc.jsp" flush="true" />
        </visual:panel>
		
		<%-- EXECUTIVE REPORT --%>
		<fmt:message var="executiveReportTitle" key="executive_report" />
		<visual:panel id="executiveReportTitlePanel" title="${executiveReportTitle }">
			<jsp:include page="executivereport.inc.jsp" flush="true" />
		</visual:panel>
		
		<%-- PROCCES LOG --%>
		<jsp:include page="procces_log.inc.jsp" flush="true" />
		
		<%-- DOCUMENTATION --%>
		<fmt:message var="documentationTitle" key="documentation.initiating"/>
		<visual:panel title="${documentationTitle }">
			<jsp:include page="../common/project_documentation.jsp">
				<jsp:param name="documentationType" value="<%=Constants.DOCUMENT_INITIATING%>"/>
				<jsp:param name="documentationTitle" value="${documentationTitle }"/>
			</jsp:include>
		</visual:panel>

        <c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
            <div style="margin-top:15px;" id="footerButtons" class="right"></div>
            <script type="text/javascript">
				readyMethods.add(function() {
					$("#tb_stakeholders_master tbody").sortable({
						cursor: "move",	
					   	update: function() {
					   		
					   		var ids = '';
					   		$("#tb_stakeholders_master tbody tr").each(function() {
					   			
					   			var stakeholder = stakeholdersTable.fnGetData(this);
					   			if (ids == '') { ids += stakeholder[0]; }
					   			else { ids += ','+stakeholder[0]; }
					   		});
					   		
					   		var params = {	    			
				    			accion: '<%=ProjectInitServlet.JX_UPDATE_STAKEHOLDER_ORDER%>',
				    			idProject: '${project.idProject}',
				    			ids : ids
				    		};
					    	initAjax.call(params);
					    }
					});
				});
			</script>		
		</c:if>
	</div>
</div>
<jsp:include page="edit_stakeholder.jsp" flush="true" />

<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">

	<jsp:include page="../../common/search_employee_popup.jsp" flush="true" />
	<jsp:include page="../../common/search_contact_popup.jsp" flush="true" />
	<jsp:include page="../../common/new_customer_popup.jsp" flush="true"/>
	<jsp:include page="../common/search_projects_popup.jsp" flush="true"/>
	
	<%--  Icludes if "projectcharter_costs.inc.jsp is included --%>
	<jsp:include page="edit_sellercost.jsp" flush="true" />
	<jsp:include page="edit_infrastructurecost.jsp" flush="true" />
	<jsp:include page="edit_licensecost.jsp" flush="true" />
	<jsp:include page="edit_workingcost.jsp" flush="true" />
</c:if>