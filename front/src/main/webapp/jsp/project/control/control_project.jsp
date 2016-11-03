<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Wbsnode"%>
<%@page import="es.sm2.openppm.core.model.impl.Changerequestwbsnode"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectfollowup"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.model.impl.Seller"%>
<%@page import="es.sm2.openppm.core.model.impl.Milestones"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectactivity"%>
<%@page import="es.sm2.openppm.core.model.impl.Timeline"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@page import="es.sm2.openppm.front.servlets.FollowProjectsServlet"%>
<%@page import="es.sm2.openppm.front.servlets.PluginServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
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
  ~ File: control_project.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<jsp:useBean id="now" class="java.util.Date" scope="page" />
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@ page import="es.sm2.openppm.core.model.impl.Executivereport" %>

<fmt:setLocale value="${locale}" scope="request" />

<fmt:message key="actions.projects.save_grsc" var="act_save_grsc" />
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete" />
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete" />
<fmt:message key="msg_to_odt" var="msg_to_odt" />
<fmt:message key="select_opt" var="msg_select_opt" />
<fmt:message key="data_not_found" var="data_not_found" />
<fmt:message key="loading_chart" var="loading_chart" />
<fmt:message key="wbs.possibleErrorEV" var="possibleErrorEV" />
<fmt:message key="activity.has_seller" var="msghasSeller" />
<fmt:message key="activity.no_money" var="msgNoMoney" />
<fmt:message key="msg_to_excel" var="msg_to_excel" />

<fmt:message key="error" var="fmt_error" />
<fmt:message key="data_not_found" var="dataNotFound" />

<%-- Messages confirmation --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_milestone">
	<fmt:param><fmt:message key="milestone"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_milestone">
	<fmt:param><fmt:message key="milestone"/></fmt:param>
</fmt:message>

<fmt:message key="msg.confirm_delete" var="msgConfirmDeleteChangeRequest">
	<fmt:param><fmt:message key="change_request"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDeleteChangeRequest">
	<fmt:param><fmt:message key="change_request"/></fmt:param>
</fmt:message>

<c:set var="titleWbsChart"><fmt:message key="wbs_chart"/></c:set>
<c:set var="titleScheduleChart"><fmt:message key="schedule_chart"/></c:set>
<%-- 
Request Attributes:
	project:		Project to control	
	followups:		List of project Followup
	followupsES:	List of project Followups order ascendent
	milestones: 	Project milestones
	changes:		List of changes (included requested)
	changeTypes:	Change types List (for control_change_popup.jsp)
--%>

<script type="text/javascript">
<!--
var controlAjax = new AjaxCall('<%=ProjectControlServlet.REFERENCE%>','<fmt:message key="error"/>');
var controlGetAjax = new AjaxCall('<%=ProjectControlServlet.REFERENCE%>','<fmt:message key="error"/>','JSON', 'GET', false);
var planAjax 	= new AjaxCall('<%=ProjectPlanServlet.REFERENCE%>','<fmt:message key="error"/>');

//Validate project to approve?
var approveRequired = false;
var statusReportValidator;

var followupsTable;
var activitiesTable;
var milestonesTable;
var activityScopeTable;
var changeListTable;
var changeRequestTable;
var wbsscroll;
var earnedValueManagementChart;
var chartSVChart;
var costVarianceChart;
var statusReportTable;

function saveGRSC() {
	
	if (statusReportValidator.form()) {
		var f = document.frm_project;
	
		var idFollowup = f.followup_control.value;
		
		if (idFollowup != '') {
			
			var params = {
				accion: "<%=ProjectControlServlet.JX_SAVE_FOLLOWUP %>", 
				// Project data
				id: f.id.value,
				followup_id: idFollowup,
				general_status: f.general_status.value,
				general_desc: f.general_desc.value,
				risk_status: f.risk_status.value,
				risk_desc: f.risk_desc.value,
				schedule_status: f.schedule_status.value,
				schedule_desc: f.schedule_desc.value,
				cost_status: f.cost_status.value,
				cost_desc: f.cost_desc.value
			};
			
			controlAjax.call(params, function(data) {
				
				var form = document.forms["frm_project"];
				form.accion.value = "";
				loadingPopup();
				form.submit();
			});
		}
	}
}

function editFollowup(element) {
	
	var followup = followupsTable.fnGetData( element.parentNode.parentNode );
	
	var f = document.forms["frm_followup"];

	// Recover followup info
	f.followup_id.value	= followup[0];
	f.ev.value 			= followup[5];
	f.ac.value 			= followup[6];
	f.statusDate.value 	= followup[1];

	var ev = toNumber(f.ev.value);
	var bac = toNumber(f.bac.value);
	
	if (ev >= 0 && bac > 0) {
		f.poc.value = toCurrency(ev*100/bac);
	}
	
	
	// Update AC
	//
	var params = {
		accion: "<%= ProjectControlServlet.JX_UPDATE_AC %>", 
		id: f.id.value,
		statusDate: f.statusDate.value
	};
	
	controlAjax.call(params, function(data) {
		
		$('#sumTimeSheet').html(data.sumTimeSheet);
		
		// Display followup info
		$('#followup-popup').dialog('open');
	});
	
	
}

function createCostCharts(data) {

	//JQPlot parameters chartFollowup 
	var dataSeries = [data.listValuesPv,data.listValuesEv,data.listValuesAc];
	var optionsObj = {
		labelX: '<fmt:message key="days"/>',
		labelY: '<fmt:message key="values"/>',
		title:	'<fmt:message key="cost_baseline_chart.eamed_value_management_chart"/>',
		seriesColors: ["#000000", "#088A08", "#FF0000"],
		enableCursor: true,
        padding: 1.1
	};
	
	earnedValueManagementChart = drawLineChart('chartFollowup', dataSeries, optionsObj, '${dataNotFound}');
	
	if(typeof earnedValueManagementChart === 'undefined' || earnedValueManagementChart == null){
		$("#legendChartFollowup").html('');
	}
	else {
		$('#legendChartFollowup').empty();
		createLegend('#legendChartFollowup','<fmt:message key="cost_baseline_chart.planned_value"/>','#000000');
		createLegend('#legendChartFollowup','<fmt:message key="cost_baseline_chart.eamed_value"/>','#088A08');
		createLegend('#legendChartFollowup','<fmt:message key="cost_baseline_chart.acual_cost"/>','#FF0000');
	}
	
	//JQPlot parameters chartTimeVariance
	var dataSeries = [data.listValuesSVt];
	var optionsObj = {
		labelX: '<fmt:message key="days"/>',
		labelY: '<fmt:message key="values"/>',
		title:	'<fmt:message key="cost_baseline_chart.svt"/>',
		seriesColors: ["#FF0000"],
		enableCursor: true,
        padding: 1.1
	};
	
	chartSVChart = drawLineChart('chartTimeVariance',dataSeries, optionsObj, '${dataNotFound}');
	
	//JQPlot parameters chartCostVariance
	var dataSeries = [data.listValuesCv];
	var optionsObj = {
		labelX: '<fmt:message key="days"/>',
		labelY: '<fmt:message key="values"/>',
		title:	'<fmt:message key="cost_baseline_chart.cost_variance"/>',
		seriesColors: ["#FF0000"],
		enableCursor: true,
        padding: 1.1
	};

	costVarianceChart = drawLineChart('chartCostVariance',dataSeries, optionsObj, '${dataNotFound}');
}

function costCharts() {
	var params = {
		accion: "<%=ProjectControlServlet.JX_COST_CONTROL_CHART %>",
		id: $("#id").val()
	};
	
	controlAjax.call(params, function(data) {
		if (typeof earnedValueManagementChart === 'undefined' || typeof chartSVChart === 'undefined' || typeof costVarianceChart === 'undefined') {
			createCostCharts(data);
		}
		else {
			if (earnedValueManagementChart != null) { earnedValueManagementChart.destroy(); } 
			if (chartSVChart != null) { chartSVChart.destroy(); }
			if (costVarianceChart != null) { costVarianceChart.destroy(); }
			createCostCharts(data);
		}
	});
}

function controlScopeActivity(element) {
	
	var activity = activityScopeTable.fnGetData( element.parentNode.parentNode );

	var f = document.forms["frm_scope_activity"];
	// Recover activity info
	f.id.value 									= $('#id').val();
	f.activity_id.value							= activity[0];
	f.name.value								= unEscape(activity[1]);
	f.bac.value 								= activity[2];
	f.ev.value 									= activity[3];
	f.poc.value 								= activity[5];
	f.<%= Projectactivity.COMMENTSPOC %>.value	= unEscape(activity[6]);
	
	$("#activityDescription").html(activity[10]);

	// Recover seller associated
	$("#associated").css("display", "none");
	if (activity[6] == "true") {
		var params = {
			accion: "<%=ProjectControlServlet.JX_CONS_SELLER %>",
			<%= Projectactivity.IDACTIVITY %>: activity[0]
		};
		
		controlAjax.call(params, function(data) {
			if (data.sellers.length > 0) {
				var associated = data.sellers[0];
				
				$("#<%= Seller.NAME %>Associated").html(associated.<%= Seller.NAME %>);
				$("#<%= Project.PROJECTNAME %>Associated").html(associated.<%= Project.PROJECTNAME %>);
				$("#<%= Projectactivity.POC %>Associated").html(associated.<%= Projectactivity.POC %>);
				if (parseFloat(toNumber($("#<%= Projectactivity.POC %>Associated").html())) > parseFloat(toNumber($("#scope_poc").val()))) {
					$("#<%= Projectactivity.POC %>Associated").css("color", "#FF0000");
				}
				$("#<%= Project.STATUSDATE %>Associated").html(associated.<%= Project.STATUSDATE %>);
				
				$("#associated").css("display", "block");
			}
		});
	}
	
	// Recover timesheet
	var params = {
		accion: "<%=ProjectControlServlet.JX_CONS_ACTIVITY_TIMESHEET %>",
		<%= Projectactivity.IDACTIVITY %>: activity[0]
	};
	
	controlAjax.call(params, function(data) {
			
		$("#hours" + "<%= Constants.TIMESTATUS_APP1 %>").html(data.hoursapp1);
		$("#hours" + "<%= Constants.TIMESTATUS_APP2 %>").html(data.hoursapp2);
		$("#hours" + "<%= Constants.TIMESTATUS_APP3 %>").html(data.hoursapp3);
		
		$("#AC" + "<%= Constants.TIMESTATUS_APP1 %>").html(data.ACapp1);
		$("#AC" + "<%= Constants.TIMESTATUS_APP2 %>").html(data.ACapp2);
		$("#AC" + "<%= Constants.TIMESTATUS_APP3 %>").html(data.ACapp3);
		
	});

    $("#warningCalcAct").hide();

	// Display activity info
	$('#scope-activity-popup').dialog('open');
}

function controlActivity(element) {
	
	var activity = activitiesTable.fnGetData( element.parentNode.parentNode );
	
	var f = document.forms["frm_activity"];
	// Recover activity info
	f.id.value 										= $('#id').val();
	f.activity_id.value								= activity[0];
	f.name.value 									= unEscape(activity[1]);
	f.<%=Projectactivity.ACTUALINITDATE %>.value 	= activity[5];
	f.<%=Projectactivity.ACTUALENDDATE %>.value 	= activity[6];
	f.<%= Projectactivity.COMMENTSDATES %>.value	= unEscape(activity[8]);
	
	if (activity[3] == '' || activity[4] == '') {
		
		activityActDates.datepicker("disable");
		$("#activityEditWarning").show();
	}
	else {
		
		activityActDates.datepicker("enable");
		$("#activityEditWarning").hide();
	}
		
	// Display activity info
	$('#activity-popup').dialog('open');
}

/* MILESTONES */

function addMilestone() {
	
	var f = document.frm_milestone;
	
	f.reset();
	f.milestone_id.value = '';
	$("#milestoneDesc").text('');
	$('#<%=Milestones.ACHIEVEDCOMMENTS %>').text('');
	
	$('#<%= Milestones.MILESTONETYPE %>').trigger('change');
	$('#<%= Milestones.MILESTONECATEGORY %>').trigger('change');
	
	// Display milestone info
	$('#milestone-popup').dialog('open');
}

var milestone = {
	// View	milestone
	view: function(element) {
		
		var milestone = milestonesTable.fnGetData( element.parentNode.parentNode );
		
		var f = document.forms["frm_milestone"];
		
		// Recover followup info
		f.milestone_id.value			= milestone[0];
		f.activity.value 				= unEscape(milestone[14]);
		f.<%= Milestones.NAME %>.value	= unEscape(milestone[2]);
		f.plannedDate.value				= milestone[4];
		f.achieved.value 				= milestone[7];
		$('#<%=Milestones.ACHIEVEDCOMMENTS %>').text(unEscape(milestone[8]));
		f.<%= Milestones.MILESTONETYPE %>.value		= unEscape(milestone[9]);
		f.<%= Milestones.MILESTONECATEGORY %>.value	= unEscape(milestone[11]);
		
		updateActivityDates(milestone[14]);
		
		f.estimatedDate.value			= milestone[6];
		
		$("#milestoneDesc").text(unEscape(milestone[15]));
		f.report_type.checked 			= (milestone[16] == "Y"?"true":"");
		
		$('#<%= Milestones.MILESTONETYPE %>').trigger('change');
		$('#<%= Milestones.MILESTONECATEGORY %>').trigger('change');
		
		// Display milestone info
		$('#milestone-popup').dialog('open');
	},
	// Delete milestone
	delete: function(element) {
		
		confirmUI(
			'${msg_title_confirm_delete_milestone}','${msg_confirm_delete_milestone}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var milestoneRow = milestonesTable.fnGetData( element.parentNode.parentNode );
				
				var params = {
					accion: "<%=ProjectControlServlet.JX_DELETE_MILESTONE%>", 
					milestone_id: milestoneRow[0]
				};
				
				controlAjax.call(params, function(data){
					milestonesTable.fnDeleteSelected();
				});
		});
	}
};

function scheduleChart() {
	
	var activitiesTableSettings = activitiesTable.fnSettings();
	
	var idsBySortingArray = [];
	for (var i = 0; i < activitiesTableSettings.asDataSearch.length; i++) {
		idsBySortingArray[i] = activitiesTableSettings.asDataSearch[i].match(/^([0-9]*)\s/)[1];
	}
	
	var params = {
		accion: "<%=ProjectControlServlet.JX_CONTROL_GANTT_CHART %>",
		id: $("#id").val(),
		filter_start: $('#filter_start').val(),
		filter_finish: $('#filter_finish').val(),
		idsBySorting: idsBySortingArray.join(",")
	};
	
	controlAjax.call(params, function(data) {
		
		if (typeof data === 'undefined' || data.tasks.length == 0) {
			
			$("#chartGantt").html('${dataNotFound}').css('text-align','center');
			$("#legendGantt").html('');
		}
		else {
			
			var monthNames = ['<fmt:message key="month.min_1"/>','<fmt:message key="month.min_2"/>','<fmt:message key="month.min_3"/>',
			                  '<fmt:message key="month.min_4"/>','<fmt:message key="month.min_5"/>','<fmt:message key="month.min_6"/>',
			                  '<fmt:message key="month.min_7"/>','<fmt:message key="month.min_8"/>','<fmt:message key="month.min_9"/>',
			                  '<fmt:message key="month.min_10"/>','<fmt:message key="month.min_11"/>','<fmt:message key="month.min_12"/>'];

			var paramsGantt = {
	    		data: data.tasks,
	    		monthNames: monthNames,
	    		controlLine: data.controlLine,
	    		cellWidth:$('#numberOfMonths').val()
		    };
			
			if ($("#chartGantt").html()) {
				
				$("#chartGantt").empty();
				$("#chartGantt").ganttView(paramsGantt);
			}
			else {
				$("#chartGantt").ganttView(paramsGantt);
				createLegend('#legendGantt','<fmt:message key="cost.actual" />','#4567aa');
				createLegend('#legendGantt','<fmt:message key="percent_complete" />','#000000');
				createLegend('#legendGantt','<fmt:message key="cost.planned" />','#A9A9A9');
				createLegend('#legendGantt','<fmt:message key="milestone_planned" />','url(images/gantt/milestonePlanned.png) no-repeat; font-size: 13px');
				createLegend('#legendGantt','<fmt:message key="milestone_estimated" />','url(images/gantt/milestoneEstimated.png) no-repeat; font-size: 13px');
				createLegend('#legendGantt','<fmt:message key="milestone_real" />','url(images/gantt/milestoneReal.png) no-repeat; font-size: 13px');
				createLegend('#legendGantt','<fmt:message key="project.status_date" />','url(images/gantt/statusDate.png) no-repeat');
				createLegend('#legendGantt','<fmt:message key="today" />',';border-right: 2px solid red; padding-right: 0px; margin-left: 15px;border-radius:0px');
			}
			
			// Styles for two years
			if ($('#numberOfMonths').val() == 1) {
				$("div.ganttview-blocks").css("margin-top", "42px");
				$("div.ganttview-vtheader").css("margin-top", "41px");
				$(".ganttview-hzheader-month").css("height", "40px");
			}
		}
	});
}

function wbsChart() {
	
	var params = {
		accion: "<%=ProjectControlServlet.JX_WBS_CHART %>",
		id: $("#id").val(),
		globalBudget: parseFloat(toNumber($('#globalBudget').text())),
		globalEV: parseFloat(toNumber($('#globalEV').text()))
	};
	
	$('#chartWBS').html('${loading_chart}');
	
	controlAjax.call(params, function(data) {
		if (typeof data.name === 'undefined') {
			$("#chartWBS").html('${dataNotFound}');
		}
		else {
			$('#chartWBS').html(initWbs(data));	
		    $("#wbsOrg").jOrgChart({
				chartElement : '#chartWBS'
			});
		    
		    $('#legendWbs').empty();
		    createLegend('#legendWbs', '<fmt:message key="work_group"/>', "white",  "gray");
			createLegend('#legendWbs', '<fmt:message key="wbs.ca_long"/>', "#FFD876", "gray");
			createLegend('#legendWbs', '<fmt:message key="wbs.associated_seller"/>',  "#FFD876", "#64D4FA");
            createLegend('#legendWbs', '<fmt:message key="wbs.no_budget"/>',  "#FFD876", "#fa9300");
			
			createBT('.btitleChart');
		}
	});
}

function addChangeRequest() {
	
	$('#change_request_id').val();
	
	resetChangeRequest();
	
	openChangeRequest();
	
	if (typeof changeRequestWBSNodeTable !== 'undefined') {
		changeRequestWBSNodeTable.fnClearTable();
	}
	
	return false;
}

function editChangeRequestList(element) {
	var aData = changeListTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	aData[13] = aData[17];
	
	changeRequestWBSNode.consult(aData);
}

function editChangeRequestStatus(element) {
	var aData = changeRequestTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	changeRequestWBSNode.consult(aData);
}

function deleteChangeRequestStatus(element) {
	
	confirmUI(
		'${msgTitleConfirmDeleteChangeRequest}','${msgConfirmDeleteChangeRequest}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var row = changeRequestTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var params = {
				accion: "<%=ProjectControlServlet.JX_DELETE_REQUEST_CHANGE %>",
				id: 	row[0]
			};
			
			controlAjax.call(params, function(data) {
				changeRequestTable.fnDeleteSelected();
			});
			
	});
}

function editChangeRequest(aData) {
	
	resetChangeRequest();
	
	openChangeRequest();
	
	// Set Change Request info
	var f = document.forms["frm_change"];
	f.change_id.value 	= aData[0];
	f.type.value 		= aData[8];
	f.priority.value 	= aData[4];
	f.date.value 		= aData[5];
	f.desc.value 		= unEscape(aData[2]);
	f.solution.value 	= unEscape(aData[9]);
	
	$("#sel_originator").val(unEscape(aData[6]));
	
	if ($("#sel_originator").val() != "-1") {
		$('#originator').hide();
	}
	else {
		$('#originator').show();
	}
	
	f.originator.value	= unEscape(aData[6]);
	
	if (changeRequestWBSNodeTable.fnGetNodes().length > 0) { // Analyzed
		
		f.impact.value = unEscape(aData[12]);
		
		showAnalyzeInputs();

		if (aData[13] != '') { // Resolved
			
			if (aData[13] == 'true') {
				$("input[name='resolution']")[0].checked = true;
			} else {
				$("input[name='resolution']")[1].checked = true;
			}
		
			f.resolution_date.value 	= aData[14];
			f.resolution_reason.value 	= unEscape(aData[15]);
			f.resolution_name.value 	= aData[18];
			
			showResolutionInputs();				
		}
		else {
			$('#btn_resolve').show();
		}
	}
	else {
		$('#btn_analyze').show();
	}
}

function toChangeODT(idChange) {
	if (idChange != '') {
		var f = document.forms["frm_project"];
		f.action = "<%=ProjectControlServlet.REFERENCE%>";
		f.accion.value = "<%=ProjectControlServlet.EXPORT_CHANGE %>";
		f.change_export_id.value = idChange;
		f.submit();
		return false;
	}
	return false;
}

function fnChangeListDetails ( nTr ) {
	var aData = changeListTable.fnGetData( nTr );
	var out = "";

	var params = {
		accion: "<%=ProjectControlServlet.JX_CONS_CHANGE %>",
		change_id: aData[0]
	};
	
	// Consult control change info
	controlAjax.call(params, function(data) {
		out = '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">';
		out += '<tr><td colspan="3">Description:</td><td colspan="3">'+data.desc+'</td></tr>';
		out += '<tr>';
		out += '<td>Change type:</td><td>'+data.type_desc+'</td>';
		out += '<td>Priority:</td><td>'+data.priority+'</td>';
		out += '<td>Originator:</td><td>'+data.originator+'</td>';
		out += '</tr>';
		out += '<tr><td colspan="3">Solution:</td><td colspan="3">'+data.solution+'</td></tr>';
		out += '</table>';
	});

	return out;
}

function changeColor(idSelect) {

	if (idSelect != '') {
		$('#'+idSelect).removeClass();
		$('#'+idSelect).addClass('campo');
        $('#'+idSelect).addClass('statusReport');
        $('#'+idSelect).addClass('select_color');
		$('#'+idSelect).addClass($('#'+idSelect).children("[selected]").attr('class'));
	}
}

function validateDates(value, msg) {

	stateValidate = false;
	if(!dateBefore('${valActInitDate}', value, '${datePattern}')
			|| !dateBefore(value, '${valActEndDate}', '${datePattern}')) {
	 
		alertUI("${fmt_error}",msg);
	}
	else {
		stateValidate = true;
	}
	return stateValidate;

}

function applyOrder() {
	
	var f = document.forms.frm_project;
	
	f.scrollTop.value = $(document).scrollTop();
	
	toTab(3);
}

function reportProject(){
	var f = document.forms["frm_project"];
	f.action = "<%=FollowProjectsServlet.REFERENCE%>";
	f.accion.value = "<%=FollowProjectsServlet.REPORT_PROJECT%>";
	$('#frm_project').attr("target","_blank");
	f.submit();
	$('#frm_project').removeAttr("target");
}

// When document is ready
readyMethods.add(function() {
	
	$('#followup_control').change(function () {
		if ($(this).val() != '') {
			
			$(".statusReport").attr("disabled",false);
			
			var f = document.forms["frm_project"];
			
			var params = {
				accion: "<%=ProjectControlServlet.JX_CONS_FOLLOWUP %>",
				followup_id: $(this).val(),
				globalBudget: parseFloat(toNumber( $("#globalBudget").html()))
			};
			
			controlAjax.call(params, function(data) {

				f.general_status.value	= data.general_status;
				changeColor('general_status');
				f.general_desc.value 	= data.general_desc;
				f.risk_status.value 	= data.risk_status;
				changeColor('risk_status');
				f.risk_desc.value 		= data.risk_desc;
				f.schedule_status.value = data.schedule_status;
				changeColor('schedule_status');
				f.schedule_desc.value 	= data.schedule_desc;
				f.cost_status.value 	= data.cost_status;
				changeColor('cost_status');
				f.cost_desc.value 		= data.cost_desc;

                // Restart validations
                statusReportValidator.resetForm();
			});
		}
		else { $(".statusReport").attr("disabled",false); }
	});

    $('.statusReport.select_color').change(function() {
		changeColor($(this).attr('id'));
	});
	
	followupsTable = $('#tb_followups').dataTable({
		"oLanguage": datatable_language,
		"bFilter": false,
		"bInfo": false,
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"aaSorting": [[ 1, "asc" ]],
		"aoColumns": [
		              { "bVisible": false },
		              { "sClass": "center", "sType": "es_date"}, 
		              { "sClass": "right"},
		              { "sClass": "right"},
		              { "sClass": "right"},
		              { "sClass": "right"},
		              { "sClass": "right"},
		              { "sClass": "center"},
		              { "sClass": "center"},
		              { "sClass": "center"},
		              { "sClass": "center"},
		              { "sClass": "right"},
		              { "sClass": "right"},
		              { "sClass": "right"},
		              { "sClass": "right"},
		              { "sClass": "center", "bSortable": false }
		      		]
	});
	
	activitiesTable = $('#tb_activities').dataTable({
		"oLanguage": datatable_language,
		"bInfo": false,
		"aaSorting": [[ 1, "asc" ]],
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"aoColumns": [ 
		              { "bVisible": false },
		              { "bVisible": true }, 
		              { "bVisible": false },
		              { "sClass": "center", "sType": "es_date" }, 
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" }, 
		              { "sClass": "center", "sType": "es_date" },
		              { "bVisible": false, "sClass": "right" },
		              { "bVisible": false },
		              { "sClass": "center", "bSortable": false }
		      		]
	});	

	milestonesTable = $('#tb_milestones').dataTable({
		"oLanguage": datatable_language,
		"bInfo": false,
		"iDisplayLength": 50,
		"aaSorting": [[6,'asc']],
		"sPaginationType": "full_numbers",
		"aoColumns": [
		              { "bVisible": false },
		              { "bVisible": true }, 
		              { "bVisible": true },
		              { "sClass": "left" },
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "bSortable": false },
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" },
		              { "bVisible": true },
		              { "bVisible": false },
		              { "sClass": "left" },
		              { "bVisible": false },
		              { "sClass": "left" },
		              { "sClass": "center", "bSortable": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "bVisible": false }
		      		]
	});
	milestonesTable.fnSetColumnVis( 0, false );
	$('#tb_milestones tbody tr').live('click', function (event) {		
		milestonesTable.fnSetSelectable(this,'selected_internal');
	} );

	activityScopeTable = $('#tb_activities_scope').dataTable({
		"oLanguage": datatable_language,
		"bInfo": false,
		"aaSorting": [[1,'asc']],
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"aoColumns": [
             { "bVisible": false }, 
             { "bVisible": true }, 
             { "sClass": "right" },
             { "sClass": "right" },
             { "sClass": "right"},
             { "sClass": "right"},
             { "bVisible": false },
             { "bVisible": false },
             { "sClass": "center", "bSortable": false},
             { "sClass": "center", "bSortable": false},
             { "bVisible": false }
     	]
	});
	
	changeListTable = $('#tb_changes_list').dataTable({
		"oLanguage": datatable_language,
		"sPaginationType": "full_numbers",
		"iDisplayLength": 50,
		"aaSorting": [[ 4, "asc" ]],
		"aoColumns": [ 
		              { "bVisible": false }, 
		              { "bVisible": false }, 
		              { "bVisible": true }, 
		              { "bVisible": false },
		              { "bVisible": false },
		              { "sClass": "center", "sType": "es_date" }, 
		              { "bVisible": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "sClass": "right" },
		              { "sClass": "right" },
		              { "bVisible": false },
		              { "sClass": "center" },
		              { "sClass": "center", "sType": "es_date" },
		              { "bVisible": false },
		              { "sClass": "center", "bSortable": false },
		              { "bVisible": false },
		              { "bVisible": false }
		      		]
	});

	changeRequestTable = $('#tb_changes_request').dataTable({
		"oLanguage": datatable_language,
		"sPaginationType": "full_numbers",
		"iDisplayLength": 50,
		"aaSorting": [[ 4, "asc" ]],
		"aoColumns": [ 
		              { "bVisible": false }, 
		              { "bVisible": true }, 
		              { "bVisible": true }, 
		              { "sClass": "left" },
		              { "bVisible": false },
		              { "sClass": "center", "sType": "es_date" }, 
		              { "bVisible": true },
		              { "bVisible": true },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "sClass": "center", "bSortable": false }
		      		]
	});

	$('#tb_changes_request tbody tr').live('click', function (event) {
		changeRequestTable.fnSetSelectable(this,'selected_internal');
	} );
	
	statusReportTable = $('#statusReportTable').dataTable({
		"oLanguage": datatable_language,
		"sPaginationType": "full_numbers",
		"bInfo": false,
		"bSort": false,
		 "bLengthChange": true,
		"iDisplayLength": 20,
		"aLengthMenu": [[20],[40],[80]],
		"bFilter": false,
		"bAutoWidth": false,
		"aoColumns": [ 
		              { "bVisible": false }, 
		              { "bVisible": true }, 
		              { "bVisible": true }, 
		              { "bVisible": true }, 
		              { "bVisible": true }
		      		]
	});
	
	var dates = $('#filter_start, #filter_finish').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		changeMonth: true,
		numberOfMonths: ${numberOfMonths},
		onSelect: function(selectedDate) {
			var option = this.id == "filter_start" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			dates.not(this).datepicker("option", option, date);
		}
	});

	$('#botonResetZoomCostCharts').click(function() {
		
		if (typeof earnedValueManagementChart !== 'undefined' && earnedValueManagementChart != null) { 
			earnedValueManagementChart.resetZoom(); 
		}
		if (typeof chartSVChart !== 'undefined' && chartSVChart !== null) { 
			chartSVChart.resetZoom(); 
		}
		if (typeof costVarianceChart !== 'undefined' && costVarianceChart !== null) { 
			costVarianceChart.resetZoom(); 
		} 
	});

	createBT('.btitle');
	
	$('#evmToCSV').on('click',function(event) {
		event.stopPropagation();
		var f = document.forms["frm_project"];
		f.action = "<%=ProjectControlServlet.REFERENCE%>";
		f.accion.value = "<%=ProjectControlServlet.EXPORT_EVM_CSV %>";
		f.globalBudget.value = parseFloat(toNumber( $("#globalBudget").html()));
		
		f.submit();
	});
	
	$('#statusReportToCSV').on('click',function(event) {
		event.stopPropagation();
		var f = document.forms["frm_project"];
		f.action = "<%=ProjectControlServlet.REFERENCE%>";
		f.accion.value = "<%=ProjectControlServlet.EXPORT_SR_CSV %>";
		f.submit();
	});
	
	$('#wbsChartReload').on('click', function(e){
		e.stopPropagation();
		wbsChart();
	});
	
	/* order and save status report position */
	$('#statusReportTable_length').css("width","100%");
	
	$('#statusReportTable_length').append($('#orderSaveStatusReportTemp').html());
	
	$('#statusReportTable_length :first-child select').css("width", "6%");
	
	$('#orderSaveStatusReportTemp').detach();
	
	statusReportValidator = $("#frm_project").validate({
		errorContainer: 'div#statusReport-errors',
		errorLabelContainer: 'div#statusReport-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#statusReport-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Executivereport.ENTITY+Executivereport.STATUSDATE %>: { required: false },
			general_desc: { maxlength: 3000 },
			risk_desc: { maxlength: 3000 },
			schedule_desc: { maxlength: 3000 },
			cost_desc: { maxlength: 3000 }
		}
	});

	var globalAC = 0;

    // Load ACs
    $('.loadAc').each(function() {

        var $span = $(this);

        controlGetAjax.call({
            accion:'<%=ProjectControlServlet.JX_CALCULATE_ACTIVITY_AC%>',
            <%=Projectactivity.IDACTIVITY%>: $span.attr('idActivity')
        },
        function(data) {

            globalAC += data.ac;
            $span.text(toCurrency(data.ac));
            $('#globalAC').text(toCurrency(globalAC));
        });
    });

    // Print charts
    $('#ganttChartPrint').click(function(e) {

        e.stopPropagation();
        chartToImage('#chartGantt', "${titleScheduleChart}");
    });

    $('#wbsChartPrint').click(function(e) {

        e.stopPropagation();
        chartToImage('#chartWBS div.jOrgChart > table', "${titleWbsChart}");
    });
});

//-->
</script>

<c:if test="${op:hasPermission(user,project.status,tab)}">
    <script>

    function saveStatusDate(date) {

        var params = {
            accion:'<%=ProjectControlServlet.JX_UPDATE_STATUS_DATE%>',
            <%=Project.IDPROJECT%>:'${project.idProject}',
            <%=Project.STATUSDATE%>:date
        };

        controlAjax.call(params,function(data) {

            if ($("#actualDate").val() != data.<%=Project.STATUSDATE%>) {
                $("#frm_scope_activity .<%=Project.STATUSDATE%>").addClass("alert alert-warning");
                $("#updateStatusDate").show();
            }
            else {
                $("#frm_scope_activity .<%=Project.STATUSDATE%>").removeClass("alert alert-warning");
                $("#updateStatusDate").hide();
            }

            $(".<%=Project.STATUSDATE%>").html(data.<%=Project.STATUSDATE%>);
            $("#<%=Project.STATUSDATE%>").val(data.<%=Project.STATUSDATE%>);
        });
    }

    readyMethods.add(function() {
        var dates = $('#<%=Project.STATUSDATE %>').datepicker({
            dateFormat: '${datePickerPattern}',
            firstDay: 1,
            showOn: 'button',
            buttonImage: 'images/calendario.png',
            buttonImageOnly: true,
            numberOfMonths: ${numberOfMonths},
            changeMonth: true
        });

        $('#<%=Project.STATUSDATE %>').bind('change',function() {
            saveStatusDate($(this).val());
        });

        $('#followup_control').trigger('change');
    });
    </script>
</c:if>

<%-- Load plugin status report --%>
<div id="statusReportPlugin" style="display:none;"></div>

<jsp:include page="loadConfigurations.jsp"></jsp:include>

<div id="projectTabs">
	<jsp:include page="../common/header_project.jsp">
		<jsp:param value="C" name="actual_page"/>
	</jsp:include>

    <%-- Information of Project --%>
    <jsp:include page="../common/info_project.jsp" flush="true" />
    <!-- END INFO PROJECT -->

	<div id="panels" style="padding: 15px;">
		
		<form name="frm_project" id="frm_project" method="post" >
			<input type="hidden" name="accion" value="" />
			<input type="hidden" name="operationPlugin" value="" />
			<input type="hidden" name="pluginAction" value="" />
			<input type="hidden" name="id" id="id" value="${project.idProject}" />
			<input type="hidden" name="status" value="${project.status}" />
			<input type="hidden" id="followup_id" />
			<input type="hidden" id="milestone_id" />
			<input type="hidden" id="activity_id" name="activity_id" />
			<input type="hidden" id="ev" name="ev" />
			<input type="hidden" id="change_request_id" name="change_request_id" />
			<input type="hidden" id="change_export_id" name="change_export_id" />
			<input type="hidden" id="idDocument" name="idDocument" />
			<input type="hidden" name="scrollTop" />
			<input type="hidden" name="globalBudget" />
            <input type="hidden" name="actualDate" id="actualDate" value="${actualDate}" />
			
			<%-- SCOPE CONTROL --%>
			<fmt:message key="scope_control" var="titleScopeControl"/>
			<visual:panel id="tb_scope" title="${titleScopeControl }">
				<div>
					<span class="hColor"><fmt:message key="project.status_date" />:</span>
					<input type="text" class="campo fecha" id="<%=Project.STATUSDATE %>"
						value="<fmt:formatDate value="${project.statusDate}" pattern="${datePattern}"/>"
						style="margin-left:20px;"
					/>
				</div>
				<table id="tb_activities_scope" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th>&nbsp;</th>
							<th width="50%"><fmt:message key="activity.name"/></th>
							<th width="10%"><fmt:message key="bac"/></th>
							<th width="10%"><fmt:message key="activity.ev"/></th>
							<th width="10%"><fmt:message key="activity.ac"/>&nbsp;(<fmt:message key="applevel.app3"/>)</th>
							<th width="10%"><fmt:message key="percent_complete"/></th>
							<th width="0%"><fmt:message key="activity.comments"/></th>
							<th width="0%"></th> <!-- has sellers -->
							<th width="6%"><fmt:message key="msg.info"/></th>
							<th width="4%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:set var="sumEVActivity" scope="request">0</c:set>
						<c:set var="sumBudget" scope="request">0</c:set>
						<c:forEach var="activity" items="${activities}">
							<c:if test="${activity.wbsnode.isControlAccount}">
								<c:choose>
									<c:when test="${activity.hasSellers eq true}">
										<c:set var="hasSeller">true</c:set>
									</c:when>
									<c:otherwise>
										<c:set var="hasSeller">false</c:set>
									</c:otherwise>
								</c:choose>
                                <c:choose>
                                    <c:when test="${activity.wbsnode.isControlAccount and (activity.wbsnode.budget == null || activity.wbsnode.budget == 0.0)}">
                                        <c:set var="noBudget">true</c:set>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="noBudget">false</c:set>
                                    </c:otherwise>
                                </c:choose>
								<tr>
									<td>${activity.idActivity}</td>
									<td>${tl:escape(activity.activityName)}</td>
									<td>${tl:toCurrency(activity.wbsnode.budget)}</td>
									<td>${tl:toCurrency(activity.ev)}</td>
                                    <td>
                                        <span class="loadAc" idActivity="${activity.idActivity}"><img src="images/load.gif" style="width: 15px;"/></span>
                                    </td>
									<td>${tl:toCurrency(activity.poc)}</td>
									<td>${tl:escape(activity.commentsPoc)}</td>
									<td>${hasSeller}</td>
									<td>
										<c:if test="${(activity.poc * activity.wbsnode.budget)/100 != activity.ev}">
											<img class="btitle" title="" src="images/info.png" bt-xtitle="${possibleErrorEV}">
											<c:if test="${hasSeller}">
												&nbsp;
											</c:if>
										</c:if>
										<c:if test="${hasSeller}">
											<img class="btitle" title="" src="images/seller.png" bt-xtitle="${msghasSeller}">
										</c:if>
                                        <c:if test="${noBudget}">
                                            <img class="btitle" title="" src="images/noMoney.png" bt-xtitle="${msgNoMoney}">
                                        </c:if>
									</td>
									<td>
										<c:if test="${op:hasPermission(user,project.status,tab)}">								
											<img onclick="controlScopeActivity(this)" title="<fmt:message key="edit"/>" class="link" src="images/view.png">
										</c:if>
									</td>
									<td>${tl:escape(activity.wbsdictionary)}</td>
								</tr>
								<c:set var="sumEVActivity" scope="request">${sumEVActivity + activity.ev }</c:set>
								<c:set var="sumBudget" scope="request">${sumBudget + activity.wbsnode.budget}</c:set>
							</c:if>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td>&nbsp;</td>
							<td><b><fmt:message key="total"/></b></td>
							<td class="right" id="globalBudget">${tl:toCurrency(sumBudget)}</td>
							<td class="right" id="globalEV">${tl:toCurrency(sumEVActivity)}</td>
							<td class="right" id="globalAC"></td>
                            <c:choose>
                                <c:when test="${project.poc != null}">
                                    <td class="right">${tl:toCurrency(project.poc*100)}</td>
                                </c:when>
                                <c:otherwise>
                                    <td class="right"> </td>
                                </c:otherwise>
                            </c:choose>
							<td>&nbsp;</td>
						</tr>
					</tfoot>
				</table>

				<%-- CHECKLIST --%>
				<jsp:include page="control_checklist.inc.jsp" flush="true" />
				
				<%-- CHART --%>
				<fmt:message key="wbs_chart" var="titleWbsChart"/>
				<c:set var="wbsChartBTN">
					<img id="wbsChartReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
				</c:set>
                <c:set var="wbsChartPrintBTN">
                    <img id="wbsChartPrint" style="height: 16px; width: 16px;" src="images/print.png" title="<fmt:message key="print"/>">
                </c:set>
				<visual:panel id="content-wbs" title="${titleWbsChart }" cssClass="panel2" buttons="${wbsChartBTN} ${wbsChartPrintBTN}" callback="wbsChart">
					<div style="overflow-y: hidden; overflow-x: auto; margin:0 auto;">
						<div id="chartWBS"></div>		
					</div>
					<div id="legendWbs" class="legendChart"></div>
				</visual:panel>
			</visual:panel>
            <!-- END SCOPE -->

			<%-- SCHEDULE CONTROL --%>	
			<fmt:message key="schedule_control" var="titleScheduleControl"/>
			<visual:panel id="tb_schedule" title="${titleScheduleControl }">
				<div class="hColor"><fmt:message key="activity_list"/></div>
				<div>
					<span class="hColor"><fmt:message key="project.status_date" />:</span>
					<span class="<%=Project.STATUSDATE %>">
						<fmt:formatDate value="${project.statusDate}" pattern="${datePattern}"/>
					</span>
				</div>
				<table id="tb_activities" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th><fmt:message key="activity.act"/></th>
							<th width="46%"><fmt:message key="activity.name"/></th>
							<th width="0%"><fmt:message  key="percent_complete"/></th>
							<th width="13%"><fmt:message key="baseline_start"/></th>
							<th width="13%"><fmt:message key="baseline_finish"/></th>
							<th width="10%"><fmt:message key="activity.actual_init_date"/></th>
							<th width="10%"><fmt:message key="activity.actual_end_date"/></th>
							<th width="0%"><fmt:message  key="activity.ev"/></th>
							<th width="0%"><fmt:message key="activity.comments"/></th>
							<th width="8%"></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="activity" items="${activities}">
							<c:if test="${activity.wbsnode.isControlAccount}">
								<tr>
									<td>${activity.idActivity}</td>
									<td>${tl:escape(activity.activityName)}</td>
									<td><fmt:formatNumber type="percent" minFractionDigits="2" maxFractionDigits="2" value="${activity.poc}"/></td>
									<td><fmt:formatDate value="${activity.planInitDate}" pattern="${datePattern}"/></td>
									<td><fmt:formatDate value="${activity.planEndDate}" pattern="${datePattern}"/></td>
									<td><fmt:formatDate value="${activity.actualInitDate}" pattern="${datePattern}"/></td>
									<td><fmt:formatDate value="${activity.actualEndDate}" pattern="${datePattern}"/></td>
									<td>${tl:toCurrency(activity.ev)}</td>
									<td>${tl:escape(activity.commentsDates)}</td>
									<td>
										<c:if test="${op:hasPermission(user,project.status,tab)}">
											<img onclick="controlActivity(this)" title="<fmt:message key="edit"/>" class="link" src="images/view.png">										
										</c:if>
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
				<div class="hColor" style="margin-top:10px"><fmt:message key="milestones.list"/></div>
				<table id="tb_milestones" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th>&nbsp;</th>
							<th width="14%"><fmt:message key="activity.name"/></th>
							<th width="9%"><fmt:message key="milestone.name"/></th>
							<th width="14%"><fmt:message key="milestone.desc"/></th>
							<th width="8%"><fmt:message  key="milestone.planned_date"/></th>							
							<th width="5%"><fmt:message  key="milestone.show_sign"/></th>
							<th width="8%"><fmt:message  key="milestone.due_date"/></th>	
							<th width="8%"><fmt:message  key="milestone.actual_date"/></th>
							<th width="15%"><fmt:message key="milestone.achievement_comments"/></th>
							<th width="0%">&nbsp;</th><%-- Id milestone type --%>
							<th width="8%"><fmt:message key="milestone.type"/></th>
							<th width="0%">&nbsp;</th><%-- Id milestone category --%>
							<th width="5%"><fmt:message key="milestone.category"/></th>
							<th width="5%">
								<c:if test="${op:hasPermission(user,project.status,tab)}">
									<img onclick="addMilestone()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
								</c:if>
							</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="milestone" items="${milestones}">
							<tr>
								<td>${milestone.idMilestone}</td>
								<td>${tl:escape(milestone.projectactivity.activityName)}</td>
								<td>${tl:escape(milestone.name)}</td>
								<td>${tl:escape(milestone.description)}</td>
								<td><fmt:formatDate value="${milestone.planned}" pattern="${datePattern}"/></td>
								<td>
									<input type="checkbox" disabled="disabled"
										<c:if test="${op:isCharEquals(milestone.reportType,'Y')}">checked</c:if> 
									/>
								</td>
								<td><fmt:formatDate value="${milestone.estimatedDate}" pattern="${datePattern}"/></td>
								<td><fmt:formatDate value="${milestone.achieved}" pattern="${datePattern}"/></td>
								<td>${tl:escape(milestone.achievedComments)}</td>
								<td>${tl:escape(milestone.milestonetype.idMilestoneType)}</td>
								<td>${tl:escape(milestone.milestonetype.name)}</td>
								<td>${tl:escape(milestone.milestonecategory.idMilestoneCategory)}</td>
								<td>${tl:escape(milestone.milestonecategory.name)}</td>
								<td>
									<c:if test="${op:hasPermission(user,project.status,tab) and (not op:isStringEquals(project.status, status_closed))}">
										<img onclick="milestone.view(this)" title="<fmt:message key="edit"/>" class="link" src="images/view.png">
										&nbsp;
										<c:if test="${milestone.planned eq null}">
											<img onclick="milestone.delete(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">		
										</c:if>							
									</c:if>
								</td>
								<td>${milestone.projectactivity.idActivity }</td>
								<td>${tl:escape(milestone.description)}</td>
								<td>${milestone.reportType}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<%-- CHART --%>
				<fmt:message key="schedule_chart" var="titleScheduleChart"/>
                <c:set var="ganttChartPrintBTN">
                    <img id="ganttChartPrint" style="height: 16px; width: 16px;" src="images/print.png" title="<fmt:message key="print"/>">
                </c:set>
				<visual:panel id="controlGanttChart" title="${titleScheduleChart }" buttons="${ganttChartPrintBTN}" cssClass="panel2">
					<div style="padding-top:10px;">
           				<span style="margin-right:5px;">
							<fmt:message key="dates.since"/>:&nbsp;
							<input type="text" id="filter_start" class="campo fecha alwaysEditable" value="${valFirstDate}"/>
           				</span>
           				<span style="margin-right:5px;">
							<fmt:message key="dates.until"/>:&nbsp;
							<input type="text" id="filter_finish" class="campo fecha alwaysEditable" value="${valLastDate}"/>
						</span>
						<span style="margin-right:5px;">
							<fmt:message key="ZOOM"/>:&nbsp;
							<select id="numberOfMonths" class="campo alwaysEditable" style="width:45px">
								<option value="1">24</option>
								<option value="2" selected>12</option>
				  				<option value="4">6</option>
								<option value="6">4</option>
							  	<option value="8">3</option>
							</select>
						</span>
						<a href="javascript:scheduleChart();" class="boton"><fmt:message key="chart.refresh" /></a>
           			</div>
					<div id="chartGantt" style="margin: 20px auto;"></div>
					<div id="legendGantt" class="legendChart"></div>
				</visual:panel>
				<div style="padding-top:12px;" id="controlGanttChartFooter"></div>
			</visual:panel>
            <!-- END SCOPE -->

			<%-- COST CONTROL --%>
			<fmt:message key="cost_control" var="titleCostControl"/>
			<visual:panel id="fieldControlCost" title="${titleCostControl }">
		
				<%-- CONTROL FUNDING --%>
				<jsp:include page="control_funding.inc.jsp" flush="true"/>
				
				<%-- EARNED VALUE MANAGEMENT --%>
				<fmt:message key="followup.earner_management" var="titleEarnedManagement"/>
				<c:set var="btnExcel"><img id="evmToCSV" class="link" src="images/csv.png" title="${msg_to_excel}"></c:set>
				<visual:panel id="fieldControlCostEV" title="${titleEarnedManagement }" cssClass="panel2" buttons="${btnExcel }">
					<table id="tb_followups" class="tabledata" cellspacing="1" width="100%">
						<thead>
							<tr>
								<th>&nbsp;</th>
								<th><fmt:message key="followup.date"/></th>
								<th><fmt:message key="funding.days_date"/></th>
								<th><fmt:message key="followup.es"/></th>
								<th><fmt:message key="followup.pv"/></th>
								<th><fmt:message key="followup.ev"/></th>
								<th><fmt:message key="followup.ac"/></th>
								<th><fmt:message key="followup.poc"/></th>
								<th><fmt:message key="followup.cpi"/></th>
								<th><fmt:message key="followup.spi"/></th>
								<th><fmt:message key="followup.spit"/></th>
								<th><fmt:message key="followup.cv"/></th>
								<th><fmt:message key="followup.sv"/></th>
								<th><fmt:message key="followup.svt"/></th>
								<th><fmt:message key="followup.eac"/></th>
								<th>&nbsp;</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="followup" items="${followupsES}">
								<%-- Send settings to calculate bac --%>
								<c:set target="${followup }" property="settings" value="${settings }"/>
								<c:set target="${followup }" property="budget" value="${sumBudget }"/>
								
								<c:set var="follES" value="${op:getES(followupsES,followup)}"/>
								<c:set var="daysToDate" value="${followup.daysToDate}"/>
								<tr>
									<td>${followup.idProjectFollowup}</td>
									<td><fmt:formatDate value="${followup.followupDate}" pattern="${datePattern}" /></td>
									<td>${daysToDate}</td>
									<td>${tl:toNumber(follES)}</td>
									<td>${tl:toCurrency(followup.pv)}</td>
									<td>${tl:toCurrency(followup.ev)}</td>
									<td>${tl:toCurrency(followup.ac)}</td>
									<td>
										<c:if test="${followup.poc != null}">
											${tl:toPercent(followup.poc)}
										</c:if>
									</td>
									<td>${tl:toCurrency(followup.cpi)}</td>
									<td>${tl:toCurrency(followup.spi)}</td>
									<td>${follES > 0 and daysToDate ne null && daysToDate > 0?tl:toCurrency((follES/daysToDate)):"" }</td>
									<td>${tl:toCurrency(followup.cv)}</td>
									<td>${tl:toCurrency(followup.sv)}</td>
									<td>
										<c:if test="${follES > 0 and daysToDate ne null}">
											${tl:toNumber(follES-daysToDate)}
										</c:if>
									</td>
									<td>${tl:toCurrency(followup.eac) }</td>
									<td>
										<c:if test="${op:hasPermission(user,project.status,tab)}">
											<img onclick="editFollowup(this)" class="link" src="images/view.png" title="<fmt:message key="view"/>"/>
										</c:if>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					
					<%-- CHART --%>
					<fmt:message key="cost_charts" var="titleCostCharts"/>
					<visual:panel id="costcharts" title="${titleCostCharts }" cssClass="panel3" callback="costCharts">
						<div style="padding:10px;" class="left">
							<a href="javascript:costCharts();" class="boton"><fmt:message key="chart.refresh" /></a>
							<input type="button" id="botonResetZoomCostCharts" value="<fmt:message key="chart.reset_zoom"/>" class="boton"/>
							<img src="images/info.png" title="<fmt:message key="chart.reset_zoom.info"/>" class="btitle">
						</div>
						<div id="chartFollowup" style="margin: 20px auto;"></div>
						<div id="legendChartFollowup" class="legendChart"></div>
						<div id="chartTimeVariance" style="margin: 20px auto;"></div>
						<div id="chartCostVariance" style="margin: 20px auto;"></div>
					</visual:panel>
				</visual:panel>
				
				<%-- COSTS --%>
				<jsp:include page="control_cost.inc.jsp"></jsp:include>
			</visual:panel>
            <!-- END COST -->

			<%-- HUMAN RESOURCES ASSIGNED --%>
			<fmt:message key="control_hr" var="titleControlHr"/>
			<visual:panel id="fieldControlHR" title="${titleControlHr }">
				<jsp:include page="../common/human_resources/human_resources.inc.jsp" flush="true" />
			</visual:panel>
            <!-- END HUMAN RESOURCES ASSIGNED -->

			<%-- CONTROL HUMAN RESOURCES --%>
			<fmt:message key="control.human_resources" var="titleControlHumanResources"/>
			<visual:panel id="fieldControlHumanResources" title="${titleControlHumanResources }">
				<jsp:include page="human_resources/control_resource.inc.jsp"></jsp:include>
			</visual:panel>
            <!-- END HUMAN RESOURCES -->

			<%-- STATUS REPORT --%>
			<fmt:message key="status_report" var="titleStatusReport"/>
			<c:set var="btnStatusReport"><img id="statusReportToCSV" src="images/csv.png" title="${msg_to_excel}"></c:set>
			<visual:panel id="fieldControlCualitative" title="${titleStatusReport }" buttons="${btnStatusReport }">
				
				<jsp:include page="executivereport.inc.jsp" flush="true" />
				
				<div>&nbsp;</div>
				<fieldset>
					<legend><fmt:message key="status_report"/></legend>
					<div id="statusReport-errors" class="ui-state-error ui-corner-all hide">
						<p>
							<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
							<strong><fmt:message key="msg.error_title"/></strong>
							&nbsp;(<b><span id="statusReport-numerrors"></span></b>)
						</p>
						<ol></ol>
					</div>
				
					<table width="100%" border="0" cellpadding="5" cellspacing="0">
						<tr>
							<c:if test="${op:hasPermission(user,project.status,tab)}">
								<td width="8%"><b><fmt:message key="date"/></b></td>
								<td colspan="2">
									<select name="followup_control" id="followup_control" class="campo">
										<option value="">${msg_select_opt}</option>
										<c:forEach var="followup" items="${followupsSel}">
											<option value="${followup.idProjectFollowup}" ${followup.idProjectFollowup == idNewFollowup?"selected":""  }><fmt:formatDate value="${followup.followupDate}" pattern="${datePattern}"/></option>
										</c:forEach>
									</select>
								</td>
							</c:if>
							<td width="72%">
                                <div id="buttonsStatusReport">
                                    <c:if test="${op:hasPermission(user,project.status,tab)}">
                                        <a href="javascript:openCreateFollowup()" class="boton"><fmt:message key="new"/></a>
                                    </c:if>
                                    <a href="javascript: reportProject();" class="boton" id="statusReportHTML"><fmt:message key="report"/></a>
                                </div>
							</td>
						</tr>
						<c:if test="${op:hasPermission(user,project.status,tab)}">
							<tr>
								<th colspan="4" class="titulo"><fmt:message key="general"/></th>
							</tr>
							<tr>
								<th><fmt:message key="status"/></th>
								<td>
									<select name="general_status" id="general_status" class="campo select_color statusReport" disabled>
										<option value="" selected><fmt:message key="sel_opt"/></option>
										<option class="high_importance" value="<%=Constants.RISK_HIGH %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="medium_importance" value="<%=Constants.RISK_MEDIUM %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="normal_importance" value="<%=Constants.RISK_NORMAL %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="low_importance" value="<%=Constants.RISK_LOW %>">&nbsp;&nbsp;&nbsp;</option>
									</select>
								</td>
								<th><fmt:message key="desc"/></th>
								<td>
									<textarea rows="3" name="general_desc" style="width: 99%" class="campo statusReport desc" disabled></textarea>
								</td>
							</tr>
							<tr>
								<th colspan="4" class="titulo"><fmt:message key="risk"/></th>
							</tr>
							<tr>
								<th><fmt:message key="status"/></th>
								<td>
									<select name="risk_status" id="risk_status" class="campo select_color statusReport" disabled>
										<option value="" selected><fmt:message key="sel_opt"/></option>
										<option class="high_importance" value="<%=Constants.RISK_HIGH %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="medium_importance" value="<%=Constants.RISK_MEDIUM %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="normal_importance" value="<%=Constants.RISK_NORMAL %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="low_importance" value="<%=Constants.RISK_LOW %>">&nbsp;&nbsp;&nbsp;</option>
									</select>
								</td>
								<th><fmt:message key="desc"/></th>
								<td>
									<textarea rows="3" name="risk_desc" style="width: 99%" class="campo statusReport desc" disabled></textarea>
								</td>
							</tr>
							<tr>
								<th colspan="4" class="titulo"><fmt:message key="schedule"/></th>
							</tr>
							<tr>
								<th><fmt:message key="status"/></th>
								<td>
									<select name="schedule_status" id="schedule_status" class="campo select_color statusReport" disabled>
										<option value="" selected><fmt:message key="sel_opt"/></option>
										<option class="high_importance" value="<%=Constants.RISK_HIGH %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="medium_importance" value="<%=Constants.RISK_MEDIUM %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="normal_importance" value="<%=Constants.RISK_NORMAL %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="low_importance" value="<%=Constants.RISK_LOW %>">&nbsp;&nbsp;&nbsp;</option>
									</select>
								</td>
								<th><fmt:message key="desc"/></th>
								<td>
									<textarea rows="3" name="schedule_desc" style="width: 99%" class="campo statusReport desc" disabled></textarea>
								</td>
							</tr>
							<tr>
								<th colspan="4" class="titulo"><fmt:message key="cost"/></th>
							</tr>
							<tr>
								<th><fmt:message key="status"/></th>
								<td>
									<select name="cost_status" id="cost_status" class="campo select_color statusReport" disabled>
										<option value="" selected><fmt:message key="sel_opt"/></option>
										<option class="high_importance" value="<%=Constants.RISK_HIGH %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="medium_importance" value="<%=Constants.RISK_MEDIUM %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="normal_importance" value="<%=Constants.RISK_NORMAL %>">&nbsp;&nbsp;&nbsp;</option>
										<option class="low_importance" value="<%=Constants.RISK_LOW %>">&nbsp;&nbsp;&nbsp;</option>
									</select>
								</td>
								<th><fmt:message key="desc"/></th>
								<td>
									<textarea rows="3" name="cost_desc" style="width: 99%" class="campo statusReport desc" disabled></textarea>
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<a href="javascript:saveGRSC();" class="boton" style="float: right;"><fmt:message key="save" /></a>
								</td>
							</tr>
						</c:if>
					</table>
				</fieldset>
					
				<div>&nbsp;</div>
				
				<div id="orderSaveStatusReportTemp">
					<span style="margin-left:67px;"><fmt:message key="order"/>:&nbsp;</span>
					<select name="orderFollowup" id="orderFollowup" class="campo alwaysEditable" style="width:9%;">
						<option value="<%=Constants.ASCENDENT%>" ${orderFollowup eq "asc" ? "selected" : ""}><fmt:message key="order.asc"/></option>
						<option value="<%=Constants.DESCENDENT%>" ${orderFollowup eq "desc" ? "selected" : ""}><fmt:message key="order.desc"/></option>
					</select>
					&nbsp;
					<a href="javascript:applyOrder();" class="boton"><fmt:message key="apply" /></a>
				</div>
				
				<div>
					<table id="statusReportTable" class="tabledata" cellspacing="1" width="100%">
						<thead>
							<tr>
								<th width="0%"></th>
								<th width="8%"><fmt:message key="date"/></th>
								<th width="5%"><fmt:message key="status"/></th>
								<th width="12%"><fmt:message key="type"/></th>
								<th width="75%"><fmt:message key="desc"/></th>
							</tr>
						</thead>
						<tbody>
							<c:set var="classTR">even</c:set>
							<c:forEach var="followup" items="${followupsSel}">
								<c:set var="classTR">${classTR == "even"?"odd":"even" }</c:set>
								<c:set value="" var="css_general"/>
								<c:choose>
									<c:when test="${op:isCharEquals(followup.generalFlag,'H')}"><c:set value="high_importance" var="css_general"/></c:when>
									<c:when test="${op:isCharEquals(followup.generalFlag,'M')}"><c:set value="medium_importance" var="css_general"/></c:when>
									<c:when test="${op:isCharEquals(followup.generalFlag,'L')}"><c:set value="low_importance" var="css_general"/></c:when>
									<c:when test="${op:isCharEquals(followup.generalFlag,'N')}"><c:set value="normal_importance" var="css_general"/></c:when>
								</c:choose>
								<c:set value="" var="css_risk"/>
								<c:choose>
									<c:when test="${op:isCharEquals(followup.riskFlag,'H')}"><c:set value="high_importance" var="css_risk"/></c:when>
									<c:when test="${op:isCharEquals(followup.riskFlag,'M')}"><c:set value="medium_importance" var="css_risk"/></c:when>
									<c:when test="${op:isCharEquals(followup.riskFlag,'L')}"><c:set value="low_importance" var="css_risk"/></c:when>
									<c:when test="${op:isCharEquals(followup.riskFlag,'N')}"><c:set value="normal_importance" var="css_risk"/></c:when>
								</c:choose>
								<c:set value="" var="css_schedule"/>
								<c:choose>
									<c:when test="${op:isCharEquals(followup.scheduleFlag,'H')}"><c:set value="high_importance" var="css_schedule"/></c:when>
									<c:when test="${op:isCharEquals(followup.scheduleFlag,'M')}"><c:set value="medium_importance" var="css_schedule"/></c:when>
									<c:when test="${op:isCharEquals(followup.scheduleFlag,'L')}"><c:set value="low_importance" var="css_schedule"/></c:when>
									<c:when test="${op:isCharEquals(followup.scheduleFlag,'N')}"><c:set value="normal_importance" var="css_schedule"/></c:when>
								</c:choose>
								<c:set value="" var="css_cost"/>
								<c:choose>
									<c:when test="${op:isCharEquals(followup.costFlag,'H')}"><c:set value="high_importance" var="css_cost"/></c:when>
									<c:when test="${op:isCharEquals(followup.costFlag,'M')}"><c:set value="medium_importance" var="css_cost"/></c:when>
									<c:when test="${op:isCharEquals(followup.costFlag,'L')}"><c:set value="low_importance" var="css_cost"/></c:when>
									<c:when test="${op:isCharEquals(followup.costFlag,'N')}"><c:set value="normal_importance" var="css_cost"/></c:when>
								</c:choose>
								<tr class="${classTR} ${classHide}">
									<td>${followup.idProjectFollowup}</td>
									<td class="center" style="background-color: #FFFFFF;border-bottom:0px;">
										<div style="position:relative;top: 35px;"><fmt:formatDate value="${followup.followupDate}" pattern="${datePattern}"/></div>
									</td>
									<td class="left"><div id="status_g_${followup.idProjectFollowup}" class="${css_general }">&nbsp;</div></td>
									<td class="left"><fmt:message key="general"/></td>
									<td id="desc_g_${followup.idProjectFollowup}" class="left">${op:generateBreak(tl:escape(followup.generalComments))}</td>
								</tr>
								<tr class="${classTR} ${classHide}">
									<td></td>
									<td style="background-color: #FFFFFF;border-bottom:0px;"></td>
									<td class="left"><div id="status_r_${followup.idProjectFollowup}" class="${css_risk }">&nbsp;</div></td>
									<td class="left"><fmt:message key="risk"/>&nbsp;
										<c:choose>
											<c:when test="${followup.riskRating >= 0}">
												<span id="riskRating_${followup.idProjectFollowup}">(${followup.riskRating})</span>
											</c:when>
											<c:otherwise>
												<span id="riskRating_${followup.idProjectFollowup}"></span>
											</c:otherwise>
										</c:choose>
									</td>
									<td id="desc_r_${followup.idProjectFollowup}" class="left">${op:generateBreak(tl:escape(followup.risksComments))}</td>
								</tr>
								<tr class="${classTR} ${classHide}">
									<td></td>
									<td style="background-color: #FFFFFF;border-bottom:0px;"></td>
									<td class="left"><div id="status_s_${followup.idProjectFollowup}" class="${css_schedule }">&nbsp;</div></td>
									<td class="left"><fmt:message key="schedule"/></td>
									<td id="desc_s_${followup.idProjectFollowup}" class="left">${op:generateBreak(tl:escape(followup.scheduleComments))}</td>
								</tr>
								<tr class="${classTR } ${classHide }">
									<td></td>
									<td style="background-color: #FFFFFF;"></td>
									<td class="left"><div id="status_c_${followup.idProjectFollowup}" class="${css_cost }">&nbsp;</div></td>
									<td class="left"><fmt:message key="cost"/></td>
									<td id="desc_c_${followup.idProjectFollowup}" class="left">${op:generateBreak(tl:escape(followup.costComments))}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</visual:panel>
            <!-- END STATUS REPORT -->

			<%-- TIMELINE --%>
			<fmt:message key="timeline" var="titleTimeline"/>
			<visual:panel id="timeline" title="${titleTimeline}">
				<jsp:include page="timeline.inc.jsp" flush="true" />
			</visual:panel>
            <!-- END TIMELINE -->
	
			<%-- CHANGE MANAGEMENT --%>
			<fmt:message key="change.management" var="titleChangeManagement"/>
			<visual:panel id="fieldControlChanges" title="${titleChangeManagement }">
				<div class="hColor" style="margin-top:10px;"><fmt:message key="change_control_list"/></div>		
				<table id="tb_changes_list" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="42%"><fmt:message key="change.desc"/></th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="10%"><fmt:message key="change.date"/></th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="9%"><fmt:message key="change.effort"/></th>
							<th width="10%"><fmt:message key="change.cost"/></th>
							<th width="0%">&nbsp;</th>
							<th width="10%"><fmt:message key="change.resolution"/></th>
							<th width="11%"><fmt:message key="change.resolution_date"/></th>
							<th width="0%">&nbsp;</th>
							<th width="8%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="change" items="${changes}">
							<c:if test="${not empty change.resolution}">
								<tr>
									<td>${change.idChange}</td>
									<td></td>
									<td>${tl:escape(change.description)}</td>
									<td>
										<c:if test="${op:isCharEquals(change.priority,'H')}"><fmt:message key="change.priority.high"/></c:if>
										<c:if test="${op:isCharEquals(change.priority,'N')}"><fmt:message key="change.priority.normal"/></c:if>
										<c:if test="${op:isCharEquals(change.priority,'L')}"><fmt:message key="change.priority.low"/></c:if>
									</td>
									<td>${change.priority}</td>
									<td><fmt:formatDate value="${change.changeDate}" pattern="${datePattern}"/></td>
									<td>${tl:escape(change.originator)}</td>
									<td>${tl:escape(change.changetype.description)}</td>
									<td>${change.changetype.idChangeType}</td>
									<td>${tl:escape(change.recommendedSolution)}</td>
									<c:set var="estimatedEffort">0.0</c:set>
									<c:set var="estimatedCost">0.0</c:set>
									<c:forEach var="changeRequestWBSNode" items="${change.changerequestwbsnodes}">
										<c:set var="estimatedEffort">${estimatedEffort + changeRequestWBSNode.estimatedEffort}</c:set>
										<c:set var="estimatedCost">${estimatedCost + changeRequestWBSNode.estimatedCost}</c:set>
									</c:forEach>
									<td>${tl:toCurrency(estimatedEffort)}</td>
									<td>${tl:toCurrency(estimatedCost)}</td>
									<td>${tl:escape(change.impactDescription)}</td>
									<td>
										<c:choose>
											<c:when test="${change.resolution}"><fmt:message key="change.accepted"/></c:when>
											<c:otherwise><fmt:message key="change.rejected"/></c:otherwise>
										</c:choose>
									</td>
									<td><fmt:formatDate value="${change.resolutionDate}" pattern="${datePattern}"/></td>
									<td>${tl:escape(change.resolutionReason)}</td>
									<td>
										<nobr>
											<img onclick="toChangeODT(${change.idChange});" class="link" src="images/odt.png" alt="[ODT]" title="${msg_to_odt}" />
											&nbsp;&nbsp;&nbsp;
											<img onclick="editChangeRequestList(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
										</nobr>
									</td>
									<td>${change.resolution}</td>
									<td>${change.resolutionName}</td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
				<div class="hColor" style="margin-top:10px;"><fmt:message key="request_change_list"/>&nbsp;<fmt:message key="in_process"/></div>
				<table id="tb_changes_request" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th width="0%">&nbsp;</th>
							<th width="8%"><fmt:message key="status"/></th>
							<th width="25%"><fmt:message key="change.desc"/></th>
							<th width="10%"><fmt:message key="change.priority"/></th>
							<th width="0%">&nbsp;</th>
							<th width="7%"><fmt:message key="change.date"/></th>
							<th width="20%"><fmt:message key="change.originator"/></th>
							<th width="20%"><fmt:message key="change.change_type"/></th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="10%">
								<c:if test="${op:hasPermission(user,project.status,tab)}">
									<img onclick="addChangeRequest()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
								</c:if>
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="change" items="${changes}">
							<c:if test="${empty change.resolution}">
								<tr>
									<td>${change.idChange}</td>
									<td>
										<c:choose>
											<c:when test="${change.wbsnode eq null}"><fmt:message key="change.open"/></c:when>
											<c:otherwise><fmt:message key="analyze"/></c:otherwise>
										</c:choose>
									</td>
									<td>${tl:escape(change.description)}</td>
									<td>
										<c:if test="${op:isCharEquals(change.priority,'H')}"><fmt:message key="change.priority.high"/></c:if>
										<c:if test="${op:isCharEquals(change.priority,'N')}"><fmt:message key="change.priority.normal"/></c:if>
										<c:if test="${op:isCharEquals(change.priority,'L')}"><fmt:message key="change.priority.low"/></c:if>
									</td>
									<td>${change.priority}</td>
									<td><fmt:formatDate value="${change.changeDate}" pattern="${datePattern}"/></td>
									<td>${tl:escape(change.originator)}</td>
									<td>${tl:escape(change.changetype.description)}</td>
									<td>${change.changetype.idChangeType}</td>
									<td>${tl:escape(change.recommendedSolution)}</td>
									<td></td>
									<td></td>
									<td>${tl:escape(change.impactDescription)}</td>
									<td>${change.resolution}</td>
									<td><fmt:formatDate value="${change.resolutionDate}" pattern="${datePattern}"/></td>
									<td>${tl:escape(change.resolutionReason)}</td>
									<td>
										<nobr>
											<img onclick="toChangeODT(${change.idChange});" class="link" src="images/odt.png" alt="[ODT]" title="${msg_to_odt}" />
											&nbsp;&nbsp;&nbsp;
											<img onclick="editChangeRequestStatus(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
											<c:if test="${empty change.changerequestwbsnodes}">
												&nbsp;&nbsp;&nbsp;
												<img onclick="deleteChangeRequestStatus(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
											</c:if>
										</nobr>
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</visual:panel>
            <!-- END MANAGEMENT -->

			<%-- KPI PROJECT --%>
			<jsp:include page="control_kpi.inc.jsp"></jsp:include>
            <!-- END KPI -->

		</form>
        <!-- END FORM PROJECT -->

		<%--  DOCUMENTATION --%>
		<fmt:message key="documentation.control" var="titleDocumentationTitle"/>
		<visual:panel title="${titleDocumentationTitle }">	
			<jsp:include page="../common/project_documentation.jsp">		
				<jsp:param name="documentationType" value="<%=Constants.DOCUMENT_CONTROL %>"/>
			</jsp:include>
		</visual:panel>
        <!-- END DOCUMENTATION -->

		<div style="padding-top:10px;" class="right" id="buttonsControlFooter"></div>
	</div>
    <!-- END PANELS -->

	<jsp:include page="change_popup.jsp" flush="true" />
    <!-- END CHANGUE POPUP -->
</div>
<!-- END TABS -->

<div id="popupData">
    <c:if test="${op:hasPermission(user,project.status,tab)}">
        <jsp:include page="scope_activity_popup.jsp" flush="true" />
        <jsp:include page="checklist_popup.jsp" flush="true" />
        <jsp:include page="activity_popup.jsp" flush="true" />
        <jsp:include page="new_milestone_popup.jsp" flush="true" />
        <jsp:include page="income_popup.jsp" flush="true" />
        <jsp:include page="followup_popup.jsp" flush="true" />
        <jsp:include page="cost_popup.jsp" flush="true" />
        <jsp:include page="iwo_popup.jsp" flush="true" />
        <jsp:include page="new_followup_popup.jsp" flush="true" />
        <jsp:include page="kpi_popup.jsp" flush="true" />
        <jsp:include page="timeline_popup.jsp" flush="true" />
        <jsp:include page="../common/human_resources/edit_teammember_popup.inc.jsp" flush="true" />
    </c:if>
</div>
<%-- Load plugins --%>
<div id="plugin"></div>