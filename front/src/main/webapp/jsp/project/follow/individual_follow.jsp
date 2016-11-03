<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
  ~ File: individual_follow.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:57
  --%>

<fmt:message key="data_not_found" var="dataNotFound" />

<fmt:setLocale value="${locale}" scope="request"/>

<c:set var="activity" value="${project.rootActivity}"></c:set>	
<c:set var="followup" value="${project.lastWithDataFollowup}"></c:set>
<c:set var="controlStatus" value="<%=Constants.STATUS_CONTROL%>" ></c:set>

<c:set var="valInitDate" scope="request"><fmt:formatDate value="${activity.actualInitDate}" pattern="${datePattern}" /></c:set>
<c:set var="valEndDate" scope="request"><fmt:formatDate value="${activity.actualEndDate}" pattern="${datePattern}" /></c:set>

<c:choose>
	<c:when test="${activity.actualInitDate == null && activity.actualEndDate == null}">
		<c:set var="valInitDate" scope="request"><fmt:formatDate value="${activity.planInitDate}" pattern="${datePattern}" /></c:set>
		<c:set var="valEndDate" scope="request"><fmt:formatDate value="${activity.planEndDate}" pattern="${datePattern}" /></c:set>
	</c:when>
	<c:when test="${activity.actualInitDate == null}">
		<c:set var="valInitDate" scope="request"><fmt:formatDate value="${activity.planInitDate}" pattern="${datePattern}" /></c:set>
	</c:when>
	<c:when test="${activity.actualEndDate == null}">
		<c:set var="valEndDate" scope="request"><fmt:formatDate value="${activity.planEndDate}" pattern="${datePattern}" /></c:set>
	</c:when>
</c:choose>

<c:set var="infoPrint"><fmt:message key="info.print"/></c:set>

<c:set var="titleWbsChart"><fmt:message key="wbs_chart"/></c:set>
<c:set var="titleScheduleChart"><fmt:message key="schedule_chart"/></c:set>

<script type="text/javascript">
<!--
var controlAjax = new AjaxCall('<%=ProjectControlServlet.REFERENCE%>','<fmt:message key="error"/>');
var activitiesTable;
var activityScopeTable;
var riskTable;
var changeListTable;
var changeRequestTable;

/* SCHEDULE CHART */
function scheduleChart() {


    // TODO francisco.bisquerra - 25/11/2015 - pasar una ordenacion al gantt en funcion del orden de las actividades.
	// Aqui no hay ordenacion
	var idsBySortingArray = [];
	for (var i = 0; i <  activitiesTable.fnGetData().length; i++) {
		idsBySortingArray[i] =  activitiesTable.fnGetData()[i][0];
	}

	var params = {
		accion: "<%=ProjectControlServlet.JX_CONTROL_GANTT_CHART%>",
		id: '${project.idProject}',	
		filter_start: $('#filter_start').val(),
		filter_finish: $('#filter_finish').val(),
		idsBySorting: idsBySortingArray.join(",")
	};
	
	controlAjax.call(params, function(data) {
		if(typeof data === 'undefined' || data.tasks.length == 0){
			$("#chartGantt").html('${dataNotFound}').css('text-align','center');
			$("#legendGantt").html('');
		}else{
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
			}else{
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

/* WBS CHART */
function wbsChart() {
	
	var params = {
		accion: "<%=ProjectControlServlet.JX_WBS_CHART %>",
		id: '${project.idProject}',
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

			$(".wbsAssociatedSeller img.seller").attr("style","display:none");
			$(".wbsAssociatedSeller span.seller").css("display","block");
		}
	});
}

readyMethods.add(function () {
	
	/* SCOPE */
	activityScopeTable = $('#tb_activities_scope').dataTable({
		"oLanguage": datatable_language,
		"aoColumns": [ 
             { "bVisible": false }, 
             { "bVisible": true }, 
             { "sClass": "right" },
             { "sClass": "right"}, 
             { "sClass": "right"}
     	],
		"bInfo": false,
		"bLengthChange": false,
		"bFilter": false,
		"bPaginate": false,
		"bSort": false
	});
	
	/* DELIVERABLES */
	checklistTable = $('#tb_checklist').dataTable({
		"oLanguage": datatable_language,
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"bAutoWidth": false,
		"aaSorting": [[2,'asc']],
		"bInfo": false,
		"bFilter": false,
		"aoColumns": [
			  { "bVisible": false },
			  { "bVisible": false },
              { "sClass": "left" },
              { "sClass": "left" },
              { "sClass": "left" },
              { "bVisible": false },
			  { "sClass": "center" },
			  { "sClass": "center", "sType": "es_date" },
			  { "bVisible": false }
      		]
	});
	
	/* QUANTITATIVE INFORMATION */
	kpiTable = $('#tb_kpi').dataTable({
		"oLanguage": datatable_language,
		"aoColumns": [ 
             { "bVisible": false }, 
             { "bVisible": false },
             { "sClass": "center", "bSortable": false }, 
             { "sClass": "left" },
             { "sClass": "left" },
             { "sClass": "right" }, 
             { "sClass": "right" }, 
             { "sClass": "right" }, 
             { "sClass": "right" },
             { "sClass": "right" },
             { "sClass": "right" }
    	],
		"bLengthChange": false,
		"bFilter": false,
		"bPaginate": false,
		"bInfo": false,
		"bSort": false
	});
	
	/* SCHEDULE */
	milestonesTable = $('#tb_milestones').dataTable({
		"oLanguage": datatable_language,
		"aoColumns": [
		              { "bVisible": false },
		              { "bVisible": true }, 
		              { "bVisible": true },
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center" },
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" },
		              { "bVisible": true },
		              { "bVisible": true },
		              { "bVisible": false }
   		],
		"bInfo": false,
   		"bLengthChange": false,
 		"bFilter": false,
 		"bPaginate": false,
 		"bSort": false
	});
	
	activitiesTable = $('#tb_activities').dataTable({
		"oLanguage": datatable_language,
		"aoColumns": [ 
		              { "bVisible": false },
		              { "bVisible": true }, 
		              { "bVisible": false },
		              { "sClass": "center", "sType": "es_date" }, 
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" }, 
		              { "sClass": "center", "sType": "es_date" },
		              { "bVisible": false }
		],
		"bInfo": false,
		"bLengthChange": false,
		"bFilter": false,
		"bPaginate": false,
		"bSort": false
	});	

	/* DATES SCHEDULE CHART */
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
	
	/* DOCUMENT REPOSITORY */
	$('#tb_doc_repository').dataTable({
		"oLanguage": datatable_language,		
		"bAutoWidth": false,
		"aoColumns": [
		              { "bVisible": false },		              
		              { "sClass": "left" },
		              { "sClass": "left" },
		              { "sClass": "left" },
                      { "sClass": "left"},
                      { "sClass": "center", sType: "date-euro"},
		              { "bVisible": false }
		 ],
		"bInfo": false,
		"bLengthChange": false,
		"bFilter": false,
		"bPaginate": false,
		"bSort": false
	});
	
	/* RISKS */
	riskTable = $('#tb_risks').dataTable({
		"oLanguage": datatable_language,
		"bAutoWidth": false,
		"aoColumns": [
             { "bVisible": false },
             { "sClass": "center" },
         	 { "sClass": "left" },
             { "sClass": "center", "sType": "es_date" },		              
             { "sClass": "center", "sType": "es_date" },
             { "sClass": "center"  },
             { "sSortDataType": "dom-div", "sType": "numeric" },
             { "sClass": "center"  }
    	],
    	"bLengthChange": false,
		"bFilter": false,
		"bPaginate": false,
		"bInfo": false,
		"bSort": false
	});

	/* CHANGES */
	changeListTable = $('#tb_changes_list').dataTable({
		"oLanguage": datatable_language,
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
		              { "bVisible": false },
		              { "bVisible": false }
		      		],
    	"bLengthChange": false,
   		"bFilter": false,
   		"bPaginate": false,
   		"bInfo": false,
   		"bSort": false
	});

	changeRequestTable = $('#tb_changes_request').dataTable({
		"oLanguage": datatable_language,
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
		              { "bVisible": false }
		      		],
   		"bLengthChange": false,
   		"bFilter": false,
   		"bPaginate": false,
   		"bInfo": false,
   		"bSort": false
	});
	
	/* ISSUES */
	issuesTable = $('#tb_issues').dataTable({
		"oLanguage": datatable_language,
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"aoColumns": [
		              { "bVisible": false },
		              { "sClass": "center", "sType": "es_date" },
		              { "bVisible": false },
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" },
		              { "bVisible": true },
		              { "bVisible": false },
		              { "sClass": "left" },
		              { "sClass": "left" }, 
		              { "sClass": "left" }
		      		],
   		"bLengthChange": false,
   		"bFilter": false,
   		"bPaginate": false,
   		"bInfo": false,
   		"bSort": false
	});	
	
	/* SCALE WBSCHART */
	$('#scaleNumber').change(function() {
		var scale =  $('#scaleNumber').val();
		$('.jOrgChart').attr("style"," transform: scale(" + scale + "," + scale +");" +
									" -ms-transform: scale(" + scale + "," + scale +");" +
									" -moz-transform: scale(" + scale + "," + scale +");" +
									" -webkit-transform: scale(" + scale + "," + scale +");" +
									" -o-transform: scale(" + scale + "," + scale +");");
		
		$('#legendWbs').attr("style"," transform: scale(" + scale + "," + scale +");" +
									" -ms-transform: scale(" + scale + "," + scale +");" +
									" -moz-transform: scale(" + scale + "," + scale +");" +
									" -webkit-transform: scale(" + scale + "," + scale +");" +
									" -o-transform: scale(" + scale + "," + scale +");");
	});
	
	$('#tb_labels').dataTable({
		"oLanguage": datatable_language,
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"aoColumns": [
			{ "bVisible": true },
			{ "bVisible": true }
		],
		"bLengthChange": false,
   		"bFilter": false,
   		"bPaginate": false,
   		"bInfo": false,
   		"bSort": false
	});

    // Print charts
    $('#printGantt').click(function() {

        chartToImage('#chartGantt', "${titleScheduleChart}");
    });

    $('#printWBS').click(function() {

        chartToImage('#chartWBS div.jOrgChart > table', "${titleWbsChart}");
    });
	
	/* INITIALIZATION */
	wbsChart();
	scheduleChart();
	
	/* OPEN ALL PANELS */
	openCloseAllPanels(true);
	
	/* ACTIVATE TOOLTIP */
	createBT('.btitle');
});

//-->
</script>
	<!-- PRINT INFORMATION -->
	<div style="padding-right: 14px;" class="right hidePrint">
		<img class="btitle" title="" src="images/info.png" bt-xtitle="${infoPrint}">
	</div>
	
	<!-- INFORMATION OF PROJECT -->
	<fmt:message key="project" var="titleProject"/>
	<visual:panel title="${titleProject}"  >
		<table id="tb_projects" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th><fmt:message key="project.accounting_code" /></th>
					<th><fmt:message key="project" /></th>
					<th><fmt:message key="project.chart_label" /></th>
					<th><fmt:message key="activity.budget" /></th>
					<th><fmt:message key="table.priority" /></th>
					<th><fmt:message key="table.poc" /></th>
					<th><fmt:message key="start" /></th>
					<th><fmt:message key="finish" /></th>
				</tr>
			</thead>
			<tbody>
				<tr class="even">
					<td class="center">${project.accountingCode}</td>
					<td class="center">${project.projectName}</td>
					<td class="center">${project.chartLabel}</td>
					<td class="center">${tl:toCurrency(project.tcv)}</td>
					<td class="center">${project.priority}</td>
					<td class="center">${tl:toPercent(activity.pocCalc)}</td>
					
					<c:set var="statusInit" value="<%=Constants.STATUS_INITIATING %>"/>
					<c:set var="statusPlan" value="<%=Constants.STATUS_PLANNING %>"/>
					<c:set var="statusControl" value="<%=Constants.STATUS_CONTROL %>"/>
					<c:choose>
						<c:when test="${statusInit == project.status}">
							<c:set var="start" value="${project.plannedInitDate}"/>
							<c:set var="finish" value="${project.plannedFinishDate}"/>
						</c:when>
						<c:when test="${statusPlan == project.status && activity != null}">
							<c:set var="start" value="${activity.planInitDate}"/>
							<c:set var="finish" value="${activity.planEndDate}"/>
						</c:when>
						<c:when test="${statusControl == project.status && activity != null}">
							<c:choose>
								<c:when test="${activity.actualInitDate == null}">
									<c:set var="start" value="${activity.planInitDate}"/>
								</c:when>
								<c:otherwise>
									<c:set var="start" value="${activity.actualInitDate}"/>
								</c:otherwise>
							</c:choose>
							<c:set var="finish" value="${activity.planEndDate}"/>
						</c:when>
						<c:when test="${activity != null}">
							<c:set var="start" value="${activity.actualInitDate}"/>
							<c:set var="finish" value="${activity.actualEndDate}"/>
						</c:when>
						<c:otherwise>
							<c:set var="start" value=""/>
							<c:set var="finish" value=""/>
						</c:otherwise>
					</c:choose>
					
					<td class="center"><fmt:formatDate value="${start}" pattern="${datePattern}"/></td>
					<td class="center"><fmt:formatDate value="${finish}" pattern="${datePattern}"/></td>
				</tr>
			</tbody>
		</table>
	</visual:panel>

	<!-- SCOPE -->
	<fmt:message key="scope_control" var="titleScope"/>
	<visual:panel id="activitiesScope" title="${titleScope}" >
        <div>
            <span class="hColor"><fmt:message key="project.status_date" />:</span>
			<span class="<%=Project.STATUSDATE %>">
				<fmt:formatDate value="${project.statusDate}" pattern="${datePattern}"/>
			</span>
        </div>
		<table id="tb_activities_scope" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th width="62%"><fmt:message key="activity.name"/></th>
					<th width="10%"><fmt:message key="bac"/></th>
					<th width="10%"><fmt:message key="activity.ev"/></th>
					<th width="10%"><fmt:message key="percent_complete"/></th>
				</tr>
			</thead>
			<tbody>
				<c:set var="sumEVActivity" scope="request">0</c:set>
				<c:set var="sumBudget">0</c:set>
				<c:forEach var="activity" items="${activities}">
					<c:if test="${activity.wbsnode.isControlAccount}">
						<tr>
							<td>${activity.idActivity}</td>
							<td>${tl:escape(activity.activityName)}</td>
							<td>${tl:toCurrency(activity.wbsnode.budget)}</td>
							<td>${tl:toCurrency(activity.ev)}</td>
							<td>${tl:toCurrency(activity.poc)}</td>
						</tr>
						<c:set var="sumEVActivity" scope="request">${sumEVActivity + activity.ev }</c:set>
						<c:set var="sumBudget">${sumBudget + activity.wbsnode.budget}</c:set>
					</c:if>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr>
					<td>&nbsp;</td>
					<td><b><fmt:message key="total"/></b></td>
					<td class="right" id="globalBudget">${tl:toCurrency(sumBudget)}</td>
					<td class="right" id="globalEV">${tl:toCurrency(sumEVActivity)}</td>
                    <c:choose>
                        <c:when test="${project.poc != null}">
                            <td class="right">${tl:toCurrency(project.poc*100)}</td>
                        </c:when>
                        <c:otherwise>
                            <td class="right"> </td>
                        </c:otherwise>
                    </c:choose>
				</tr>
			</tfoot>
		</table>
	</visual:panel>
	
	<!-- DELIVERABLES -->
	<fmt:message key="deliverables" var="titleDeliverables"/>
	<visual:panel id="deliverables" title="${titleDeliverables}" >
		<table id="tb_checklist" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="0%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
					<th width="30%"><fmt:message key="wbs.wbs"/></th>
					<th width="8%"><fmt:message key="checklists.code"/></th>
					<th width="26%"><fmt:message key="checklists.name"/></th>
					<th width="0%"><fmt:message key="checklists.desc"/></th>
					<th width="8%"><fmt:message key="checklists.percentage_complete"/></th>
					<th width="10%"><fmt:message key="date"/></th>
					<th width="0%"><fmt:message key="checklists.comments"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="checklist" items="${checklists}">
					<tr>
						<td>${checklist.idChecklist}</td>
						<td>${checklist.wbsnode.idWbsnode}</td>
						<td>${tl:escape(checklist.wbsnode.code)} ${tl:escape(checklist.wbsnode.name)}</td>
						<td>${tl:escape(checklist.code)}</td>
						<td>${tl:escape(checklist.name)}</td>
						<td>${op:generateBreak(tl:escape(checklist.description))}</td>
						<td>${checklist.percentageComplete}</td>
						<td><fmt:formatDate value="${checklist.actualizationDate}" pattern="${datePattern}"/></td>
						<td>${checklist.comments}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</visual:panel>
	
	<!-- STATUS REPORT-->
	<fmt:message key="status_report" var="titleStatusReport"/>
	<visual:panel id="qualitativeInformation" title="${titleStatusReport}" >
		<table class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="20%"><fmt:message key="project_name"/></th>
					<th width="5%"><fmt:message key="date"/></th>
					<th width="5%"><fmt:message key="status"/></th>
					<th width="10%"><fmt:message key="type"/></th>
					<th width="56%"><fmt:message key="desc"/></th>
				</tr>
			</thead>
			<tbody id="add_datacualitative">
				<c:set var="followup" value="${project.lastFollowup}"></c:set>
				<c:if test="${op:isStringEquals(project.status, controlStatus)}">
					<!-- null control -->			
					<c:choose>
						<c:when test="${followup.generalFlag != null}">
							<c:set var="general_status" value="${followup.generalFlag}"/>
						</c:when>
						<c:otherwise>
							<c:set var="general_status" value=" "/>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${followup.generalComments != null}">
							<c:set var="general_desc" value="${op:generateBreak(tl:escape(followup.generalComments))}"/>
						</c:when>
						<c:otherwise>
							<c:set var="general_desc" value=" "/>
						</c:otherwise>
					</c:choose>
					<c:choose>
					 	<c:when test="${followup.riskFlag != null}">
					 		<c:set var="risk_status" value="${followup.riskFlag}"/>
					 	</c:when>
					 	<c:otherwise>
					 		<c:set var="risk_status" value=" "/>
					 	</c:otherwise>
					 </c:choose>
				 	<c:choose>
					 	<c:when test="${followup.risksComments != null}">
					 		<c:set var="risk_desc" value="${op:generateBreak(tl:escape(followup.risksComments))}"/>
					 	</c:when>
					 	<c:otherwise>
					 		<c:set var="risk_desc" value=" "/>
					 	</c:otherwise>
					 </c:choose>
				  	<c:choose>
					 	<c:when test="${followup.scheduleFlag != null}">
					 		<c:set var="schedule_status" value="${followup.scheduleFlag}"/>
					 	</c:when>
					 	<c:otherwise>
					 		<c:set var="schedule_status" value=" "/>
					 	</c:otherwise>
					 </c:choose>
				 	<c:choose>
					 	<c:when test="${followup.scheduleComments != null}">
					 		<c:set var="schedule_desc" value="${op:generateBreak(tl:escape(followup.scheduleComments))}"/>
					 	</c:when>
					 	<c:otherwise>
					 		<c:set var="schedule_desc" value=" "/>
					 	</c:otherwise>
					 </c:choose>
				  	<c:choose>
					 	<c:when test="${followup.costFlag != null}">
					 		<c:set var="cost_status" value="${followup.costFlag}"/>
					 	</c:when>
					 	<c:otherwise>
					 		<c:set var="cost_status" value=" "/>
					 	</c:otherwise>
					 </c:choose>
				  	<c:choose>
					 	<c:when test="${followup.costComments != null}">
					 		<c:set var="cost_desc" value="${op:generateBreak(tl:escape(followup.costComments))}"/>
					 	</c:when>
					 	<c:otherwise>
					 		<c:set var="cost_desc" value=" "/>
					 	</c:otherwise>
					 </c:choose>
					<tr class="even">
						<td class="left" rowspan="4">${project.projectName}</td>
						<td class="center" rowspan="4"><fmt:formatDate value="${followup.followupDate}" pattern="${datePattern}"/></td>
						<td class="left"><div class="${followup.classGeneral}">&nbsp;</div></td>
						<td class="left"><fmt:message key="general"/></td>
						<td class="left">${general_desc}</td>
					</tr>
					<tr class="even">
						<td class="left"><div class="${followup.classRisk}">&nbsp;</div></td>
						<td class="left"><fmt:message key="risk"/></td>
						<td class="left">${risk_desc}</td>
					</tr>
					<tr class="even">
						<td class="left"><div class="${followup.classSchedule}">&nbsp;</div></td>
						<td class="left"><fmt:message key="schedule"/></td>
						<td class="left">${schedule_desc}</td>
					</tr>
					<tr class="even">
						<td class="left"><div class="${followup.classCost}">&nbsp;</div></td>
						<td class="left"><fmt:message key="cost"/></td>
						<td class="left">${cost_desc}</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</visual:panel>
	
	<!-- QUANTITATIVE INFORMATION -->
	<fmt:message key="kpi.name" var="titleKpis"/>
	<visual:panel id="kpis" title="${titleKpis}" >
		<table id="tb_kpi" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="0%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
                    <th width="4%"><fmt:message key="rag" /></th>
					<th width="20%"><fmt:message key="kpi.metric" /></th>
					<th width="29%"><fmt:message key="kpi.definition" /></th>
					<th width="6%"><fmt:message key="kpi.upper_threshold" /></th>
					<th width="8%"><fmt:message key="kpi.lower_threshold" /></th>
					<th width="8%"><fmt:message key="kpi.normalized_value" />&nbsp;%</th>
					<th width="8%"><fmt:message key="kpi.weight" />&nbsp;%</th>
					<th width="8%"><fmt:message key="kpi.value" /></th>
					<th width="5%"><fmt:message key="kpi.score" />&nbsp;%</th>
				</tr>
			</thead>
			<tbody>
                <c:set var="totalWeight" value="${0}"/>
                <c:set var="totalScore" value="${0}"/>
                <c:set var="showTotal" value="true"/>
				<c:forEach var="kpi" items="${kpis}">
                    <c:set var="riskColor"></c:set>
					<c:set var="totalWeight" value="${kpi.weight + totalWeight}"/>
					<c:set var="totalScore" value="${kpi.score + totalScore}"/>
                    <c:set var="adjustedValue" value="${kpi.adjustedValue}"/>
                    <c:if test="${empty kpi.value}">
                        <c:set var="showTotal" value="false"/>
                    </c:if>
					<tr>
						<td>${kpi.idProjectKpi}</td>
						<td>${kpi.metrickpi.idMetricKpi}</td>
						<td>
                            <c:if test="${!empty kpi.value}">
                                <c:choose>
                                    <c:when test="${kpi.adjustedValue == 100}">
                                        <c:set var="riskColor">risk_low</c:set>
                                    </c:when>
                                    <c:when test="${kpi.adjustedValue == 0 && kpi.lowerThreshold != kpi.value}">
                                        <c:set var="riskColor">risk_high</c:set>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="riskColor">risk_medium</c:set>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
							<span class="${riskColor}">&nbsp;&nbsp;&nbsp;</span>
						</td>
                        <td>
                            <c:choose>
                                <c:when test="${kpi.metrickpi.idMetricKpi == null}">
                                    ${tl:escape(kpi.specificKpi)}
                                </c:when>
                                <c:otherwise>
                                    ${tl:escape(kpi.metrickpi.name)}
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${tl:escape(kpi.metrickpi.definition)}</td>
                        <td>${tl:toCurrency(kpi.upperThreshold)}</td>
                        <td>${tl:toCurrency(kpi.lowerThreshold)}</td>
                        <td>${tl:toCurrency(kpi.value)}</td>
                        <td>${tl:toCurrency(kpi.adjustedValue)}</td>
                        <td>${tl:toCurrency(kpi.weight)}</td>
                        <td>${tl:toCurrency(kpi.score)}</td>
					</tr>
				</c:forEach>
			</tbody>
			<c:if test="${fn:length(kpis) > 0}">
				<tfoot>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
                        <td>
                            <div class="center" id="projectColorRAG">
                                <span class="${project.kpiStatus}" style="border: 1px solid #BDBDBD; border-radius: 5px;">&nbsp;&nbsp;&nbsp;</span>
                            </div>
                        </td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
                        <td><div class="right" id="projectUpperThreshold">${tl:toCurrency(project.upperThreshold)}</div></td>
                        <td><div class="right" id="projectLowerThreshold">${tl:toCurrency(project.lowerThreshold)}</div></td>
                        <td>
                            <div class="right" id="totalValue">
                                <c:if test="${showTotal == true}">
                                    ${tl:toCurrency(totalScore)}
                                </c:if>
                            </div>
                        </td>
                        <td><div class="right" id="totalNormalizedValue"></div></td>
                        <td><div class="right">${tl:toCurrency(totalWeight)}</div></td>
                        <td><div class="right" id="totalScore">${tl:toCurrency(totalScore)}</div></td>
					</tr>
				</tfoot>
			</c:if>
		</table>
	</visual:panel>
	
	<!-- SCHEDULE -->
	<fmt:message key="schedule_control" var="titleScheduleControl"/>
	<visual:panel id="scheduleControl" title="${titleScheduleControl}" >
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
					<th width="0%"><fmt:message key="activity.act"/></th>
					<th width="48%"><fmt:message key="activity.name"/></th>
					<th width="0%"><fmt:message  key="percent_complete"/></th>
					<th width="13%"><fmt:message key="baseline_start"/></th>
					<th width="13%"><fmt:message key="baseline_finish"/></th>
					<th width="13%"><fmt:message key="activity.actual_init_date"/></th>
					<th width="13%"><fmt:message key="activity.actual_end_date"/></th>
					<th width="0%"><fmt:message  key="activity.ev"/></th>
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
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
		
		<div class="spacer"></div>
		
		<div class="hColor"><fmt:message key="milestones.list"/></div>
		
		<table id="tb_milestones" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th width="18%"><fmt:message key="activity.name"/></th>
					<th width="25%"><fmt:message key="milestone.name"/></th>
					<th width="8%"><fmt:message  key="milestone.planned_date"/></th>							
					<th width="5%"><fmt:message  key="milestone.show_sign"/></th>
					<th width="8%"><fmt:message  key="milestone.due_date"/></th>
					<th width="8%"><fmt:message  key="milestone.actual_date"/></th>
					<th width="23%"><fmt:message key="milestone.achievement_comments"/></th>
					<th width="5%"><fmt:message key="milestone.type"/></th>
					<th width="0%">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="milestone" items="${milestones}">
					<tr>
						<td>${milestone.idMilestone}</td>
						<td>${tl:escape(milestone.projectactivity.activityName)}</td>
						<td>${tl:escape(milestone.name)}</td>
						<td><fmt:formatDate value="${milestone.planned}" pattern="${datePattern}"/></td>
						<td>
							<input type="checkbox" disabled="disabled"
								<c:if test="${op:isCharEquals(milestone.reportType,'Y')}">checked</c:if> 
							/>
						</td>
						<td><fmt:formatDate value="${milestone.estimatedDate}" pattern="${datePattern}"/></td>
						<td><fmt:formatDate value="${milestone.achieved}" pattern="${datePattern}"/></td>
						<td>${op:generateBreak(tl:escape(milestone.achievedComments))}</td>
						<td>${op:generateBreak(tl:escape(milestone.milestonetype.name))}</td>
						<td>${milestone.projectactivity.idActivity }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</visual:panel>
	
	<!-- RISKS -->
	<fmt:message key="risk_register" var="titleRiskRegister"/>
	<visual:panel id="riskRegister" title="${titleRiskRegister}" >
		<table id="tb_risks" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="0%">&nbsp;</th>
					<th width="15%"><fmt:message key="risk.code"/></th>
					<th width="35%"><fmt:message key="risk.name"/></th>
					<th width="10%"><fmt:message key="risk.date_raised"/></th>					
					<th width="10%"><fmt:message key="risk.due_date"/></th>
					<th width="10%"><fmt:message key="risk.status"/></th>
					<th width="10%"><fmt:message key="risk.rating"/></th>
					<th width="10%"><fmt:message key="risk.response"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="risk" items="${risks}">
					<tr>
						<td>${risk.idRisk }</td>
						<td>${tl:escape(risk.riskCode)}</td>
						<td>${tl:escape(risk.riskName)}</td>
						<td><fmt:formatDate value="${risk.dateRaised}" pattern="${datePattern}"/></td>						
						<td><fmt:formatDate value="${risk.dueDate}" pattern="${datePattern}"/></td>						
						<td>
							<c:choose>
								<c:when test="${risk.status == 'C'}">
									<%=Constants.CLOSED%>
								</c:when>
								<c:when test="${risk.status == 'O'}">
									<%=Constants.OPEN%>
								</c:when>
							</c:choose>
						</td>				
						<td>
							<div style="margin: 0px auto; width: 80px;">
								<div style="display:none;">${risk.riskRating}</div>
								<div style="width: 25%; float: left; margin-left: 10px;"
									<c:choose>
										<c:when test="${risk.riskRating gt 1500 }">
											class="riskRating risk_high"
										</c:when>
										<c:when test="${risk.riskRating gt 500 }">
											class="riskRating risk_medium"
										</c:when>
										<c:when test="${risk.riskRating eq 0 }">
											class="riskRating"
										</c:when>
										<c:otherwise>
											class="riskRating risk_low"
										</c:otherwise>
									</c:choose>
								>&nbsp;
								</div>
								<c:if test="${risk.riskRating != 0}">
									<div style="float: left; margin-left: 10px;">(${risk.riskRating})</div>
								</c:if>
							</div>
						</td>		
						<td>${tl:escape(risk.riskcategories.description)}</td>						
					</tr>
				</c:forEach>
			</tbody>
		</table>	
	</visual:panel>
	
	<%-- CHANGE MANAGEMENT --%>
	<fmt:message key="change.management" var="titleChangeManagement"/>
	<visual:panel id="fieldControlChanges" title="${titleChangeManagement}" >
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
							<td>${op:generateBreak(tl:escape(change.description))}</td>
							<td>
								<c:if test="${op:isCharEquals(change.priority, 'H')}"><fmt:message key="change.priority.high"/></c:if>
								<c:if test="${op:isCharEquals(change.priority, 'N')}"><fmt:message key="change.priority.normal"/></c:if>
								<c:if test="${op:isCharEquals(change.priority, 'L')}"><fmt:message key="change.priority.low"/></c:if>
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
							<td>${change.resolution}</td>
							<td>${change.resolutionName}</td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
		<div>&nbsp;</div>
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
					<th width="0%">&nbsp;</th>
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
							<td>${op:generateBreak(tl:escape(change.description))}</td>
							<td>
								<c:if test="${op:isCharEquals(change.priority, 'H')}"><fmt:message key="change.priority.high"/></c:if>
								<c:if test="${op:isCharEquals(change.priority, 'N')}"><fmt:message key="change.priority.normal"/></c:if>
								<c:if test="${op:isCharEquals(change.priority, 'L')}"><fmt:message key="change.priority.low"/></c:if>
							</td>
							<td>${change.priority}</td>
							<td><fmt:formatDate value="${change.changeDate}" pattern="${datePattern}"/></td>
							<td>${tl:escape(change.originator)}</td>
							<td>${op:generateBreak(tl:escape(change.changetype.description))}</td>
							<td>${change.changetype.idChangeType}</td>
							<td>${op:generateBreak(tl:escape(change.recommendedSolution))}</td>
							<td>${change.wbsnode.idWbsnode}</td>
							<td>${change.estimatedEffort}</td>
							<td>${tl:toCurrency(change.estimatedCost)}</td>
							<td>${op:generateBreak(tl:escape(change.impactDescription))}</td>
							<td>${change.resolution}</td>
							<td><fmt:formatDate value="${change.resolutionDate}" pattern="${datePattern}"/></td>
							<td>${op:generateBreak(tl:escape(change.resolutionReason))}</td>
						</tr>
					</c:if>
				</c:forEach>
			</tbody>
		</table>
	</visual:panel>

	<!-- ISSUES -->
	<fmt:message key="issue_log" var="titleIssuesLog"/>
	<visual:panel id="field_issue_controlling" title="${titleIssuesLog}" >
		<table id="tb_issues" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="0%">&nbsp;</th>
					<th width="8%"><fmt:message key="issue.raise_date"/></th>
					<th width="0%">&nbsp;</th>
					<th width="8%"><fmt:message key="issue.due_date"/></th>
					<th width="8%"><fmt:message key="issue.close_date"/></th>
					<th width="6%"><fmt:message key="issue.status"/></th>
					<th width="0%"><fmt:message key="issue.assigned_to"/></th>
					<th width="6%"><fmt:message key="issue.rating"/></th>
					<th width="32%"><fmt:message key="issue.description"/></th>
					<th width="32%"><fmt:message key="issue.resolution"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="issue" items="${issues}">
					<tr>
						<td>${issue.idIssue }</td>
						<td><fmt:formatDate value="${issue.dateLogged }" pattern="${datePattern}" /></td>
						<td>${issue.priority }</td>
						<td><fmt:formatDate value="${issue.targetDate }" pattern="${datePattern}" /></td>
						<td><fmt:formatDate value="${issue.dateClosed }" pattern="${datePattern}" /></td>
						<td>
							<c:choose>
								<c:when test="${not empty issue.dateClosed }"><fmt:message key="issue_closed"/></c:when>
								<c:otherwise><fmt:message key="issue_opened" /></c:otherwise>
							</c:choose>
						</td>
						<td>${tl:escape(issue.assignedTo)}</td>
						<td>
							<c:if test="${issue.priority eq priorityH}"><fmt:message key="priority.high" /></c:if>
							<c:if test="${issue.priority eq priorityM}"><fmt:message key="priority.medium" /></c:if>
							<c:if test="${issue.priority eq priorityL}"><fmt:message key="priority.low" /></c:if>
						</td>
						<td>${op:generateBreak(tl:escape(issue.description))}</td>
						<td>${op:generateBreak(tl:escape(issue.resolution))}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</visual:panel>

	<%-- LABELS --%>
	<fmt:message var="labelTitle" key="labels" />
	<visual:panel id="labelsPanel" title="${labelTitle }">
		<table id="tb_labels" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="40%"><fmt:message key="labels.name" /></th>
					<th width="60%"><fmt:message key="labels.description" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="projectLabel" items="${projectLabels}">
				<tr>
					<td>${tl:escape(projectLabel.label.name)}</td>
					<td>${op:generateBreak(tl:escape(projectLabel.label.description))}</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</visual:panel>

	<!-- DOCUMENTATION -->
	<fmt:message key="closure.document_repository" var="titleDocumentRepository"/>
	<visual:panel id="documentRepository" title="${titleDocumentRepository}" >
		<jsp:include page="../closure/closure_document.inc.jsp" flush="true"/>
	</visual:panel>

	<!-- GANTT CHART -->
    <fmt:message key="schedule_chart" var="titleScheduleChart"/>
    <visual:panel id="scheduleChart" title="${titleScheduleChart}"  cssClass="">
        <div class="hidePrint" style="padding-top:10px;">
        <span style="margin-right:5px;">
            <fmt:message key="dates.since"/>:&nbsp;
            <input type="text" id="filter_start" class="campo fecha alwaysEditable" value="${valInitDate}"/>
            </span>
            <span style="margin-right:5px;">
            <fmt:message key="dates.until"/>:&nbsp;
            <input type="text" id="filter_finish" class="campo fecha alwaysEditable" value="${valEndDate}"/>
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
            <button id="printGantt" class="boton"><fmt:message key="PRINT_AS_IMAGE"/></button>
        </div>

        <div id="chartGantt" style="margin: 20px auto;"></div>
        <div id="legendGantt" class="legendChart"></div>
    </visual:panel>


	<!-- WBS CHART -->
    <fmt:message key="wbs_chart" var="titleWbsChart"/>
    <visual:panel id="wbsChart" title="${titleWbsChart}">
        <div style="overflow-y: auto; overflow-x: auto; margin:0 auto;">
            <div class="hidePrint" style="padding-top:10px;">
            <span style="margin-right:5px;">
                <fmt:message key="scale"/>:&nbsp;
                <select id="scaleNumber" class="campo alwaysEditable" style="width:50px">
                    <option value="1">1</option>
                    <option value="0.75">3/4</option>
                    <option value="0.6">2/3</option>
                    <option value="0.5">1/2</option>
                </select>
            </span>
                <button id="printWBS" class="boton"><fmt:message key="PRINT_AS_IMAGE"/></button>
            </div>
            <div id="chartWBS"></div>
        </div>
        <div id="legendWbs" class="legendChart"></div>
    </visual:panel>


