<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.model.impl.Milestones"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectactivity"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@ page import="es.sm2.openppm.core.logic.setting.GeneralSetting" %>
<%@ page import="es.sm2.openppm.front.utils.SettingUtil" %>
<%@ page import="es.sm2.openppm.front.utils.SecurityUtil" %>

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
  ~ File: planning_project.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_wbs">
	<fmt:param><fmt:message key="wbs_node"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_wbs">
	<fmt:param><fmt:message key="wbs_node"/></fmt:param>
</fmt:message>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_milestone">
	<fmt:param><fmt:message key="milestone"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_milestone">
	<fmt:param><fmt:message key="milestone"/></fmt:param>
</fmt:message>
<fmt:message key="yes" var="msg_yes" />
<fmt:message key="no" var="msg_no" />
<fmt:message key="data_not_found" var="data_not_found" />
<fmt:message key="loading_chart" var="loading_chart" />

<fmt:message key="msg.confirm" var="msgConfirm" />
<fmt:message key="investment.no_way_back" var="msgNoWayBack" />

<fmt:message key="wbs.confirm_export" var="msgConfirmExport" />

<fmt:message key="error" var="fmt_error" />
<fmt:message key="data_not_found" var="dataNotFound" />

<c:set var="hasPermission" value="${op:hasPermission(user,project.status,tab)}"/>

<c:set var="titleWbsChart"><fmt:message key="wbs_chart"/></c:set>
<c:set var="titleScheduleChart"><fmt:message key="schedule_chart"/></c:set>

<script>
//Rules for validate project
var planValidator;
var planAjax = new AjaxCall('<%=ProjectPlanServlet.REFERENCE%>','<fmt:message key="error"/>');

// Data Tables
var wbsNodesTable;
var activitiesTable;
var milestonesTable;
var openFlag = false;

function exportWBS(element){
	var wbsNode = wbsNodesTable.fnGetData( element.parentNode.parentNode );
	
	confirmUI("${msgConfirm }", "${msgConfirmExport}", "${msg_yes}", "${msg_no}",
		function() {
	
			var params = {
				accion: "<%= ProjectPlanServlet.JX_EXPORT_WBS %>", 
				idWBSNode: wbsNode[0]
			};
			
			planAjax.call(params);
		}
	);
}


function saveProject() {
	if (planValidator.form()) {
		var f = document.forms["frm_project"];
		f.accion.value = "<%=ProjectPlanServlet.SAVE_PROJECT%>";
		loadingPopup();
		f.submit();
	}
}

function approveProject() {
	confirmUI("${msgConfirm }", "${msgNoWayBack }", "${msg_yes}", "${msg_no}",
			function() {
		var f = document.forms["frm_project"];
		f.accion.value = "<%=ProjectPlanServlet.APPROVE_PROJECT%>";
		loadingPopup();
		f.submit();
	});
}

function addWBS() {
	
	var params = {
		accion: "<%=ProjectPlanServlet.JX_CONS_WBSNODES%>", 
		id: $('#id').val()
	};
	
	planAjax.call(params, function (data) {
		var f = document.frm_wbsnode;
		f.reset();
		f.wbs_id.value = '';
		$(f.wbs_desc).text('');

		var options = '';
    	for (var i = 0; i < data.length; i++) {
	    	options += '<option value="' + data[i].id + '">' + escape(data[i].code) + data[i].name + '</option>';
   		}
    	$("#wbs_parent").attr('disabled',false).html(options);

    	$("#wbs_parent_import").attr('disabled',false).html(options);
    	
    	// Show new WBS Node popup
    	viewWBSSelection();
    	$('#wbsnode-popup').dialog('open');
	});
}

function loadWBSNode(wbsNode) {
	
	var f = document.forms["frm_wbsnode"];
	f.reset();
	f.wbs_id.value = wbsNode[0];
	f.wbs_parent.value = wbsNode[4];
	f.wbs_code.value = unEscape(wbsNode[1]);
	f.wbs_name.value = unEscape(wbsNode[2]);
	f.wbs_budget.value = wbsNode[6];
	f.wbs_ca.checked = $('#wbscheck_'+wbsNode[0]).is(':checked');
	$(f.wbs_desc).text(unEscape(wbsNode[3]));
	if (f.wbs_ca.checked && '${hasPermission}' == 'true') {		
		$('input#wbs_budget').attr('disabled',false);
	}
	else {
		$('input#wbs_budget').attr('disabled',true);
	}
	
	f.isCA.value 		= $('#wbscheck_'+wbsNode[0]).is(':checked');
	f.emptyBudget.value = wbsNode[6];
	
	// Show edit WBS Node popup
	viewWBSNodo();
	$('#wbsnode-popup').dialog('open');
}

function editWBS(element) {
	
	var wbsNode = wbsNodesTable.fnGetData( element.parentNode.parentNode );
	
	if (wbsNode[4] == '') {
		$("#wbs_parent").attr('disabled',true);
		$("select#wbs_parent").html('');
   		loadWBSNode(wbsNode);
    }
   	else {
	
   		var params = {
			accion: "<%=ProjectPlanServlet.JX_CONS_WBSNODES%>", 
			id: $('#id').val(),
			idNode: wbsNode[0]
		};
   		
   		planAjax.call(params, function (data) {
	    	var options = '';
	    	for (var i = 0; i < data.length; i++) {
		    	options += '<option value="' + data[i].id + '">' + escape(data[i].code) + escape(data[i].name) + '</option>';
	   		}
	    	$("select#wbs_parent").html(options);
	    	
	    	if('${hasPermission}' == 'true') {
	    		$("#wbs_parent").attr('disabled',false);	
	    	}
	    	
	    	loadWBSNode(wbsNode);
		});
   	}
}

function deleteWBS(id) {
	var f = document.forms["frm_project"];
	f.wbs_id.value = id;
	
	confirmUI(
		'${msg_title_confirm_delete_wbs}','${msg_confirm_delete_wbs}',
		'${msg_yes}','${msg_no}', function() {
			
			$('#dialog-confirm').dialog("close");
			f.accion.value = "<%=ProjectPlanServlet.DELETE_WBSNODE%>";
			loadingPopup();
			f.submit();
	});
}

function viewActivity(element) {
	
	var activity = activitiesTable.fnGetData( element.parentNode.parentNode );

	var f = document.forms["frm_activity"];
	f.reset();
	f.idactivity.value = activity[1];
	f.name.value = unEscape(activity[2]);
	f.init_date.value = activity[3];
	f.end_date.value = activity[4];
	f.<%=Projectactivity.PV%>.value = activity[5];
	$("#act_end_date").datepicker("option", "minDate", $('#act_init_date').val());
	$("#act_init_date").datepicker("option", "maxDate", $('#act_end_date').val());
	$("#wbs_dictionary").text(unEscape(activity[6]));
	
	$('#activity-popup').dialog('open');
}


function addMilestone() {
	var f = document.frm_milestone;
	f.reset();
	f.milestone_id.value = '';
	$("#milestoneDesc").text('');
	$("#<%=Milestones.NOTIFICATIONTEXT%>").text('');
	updateActivityDates();
	$('#milestone-popup').dialog('open');
	
	$('#<%= Milestones.MILESTONETYPE %>').trigger('change');
	$('#<%= Milestones.MILESTONECATEGORY %>').trigger('change');
	$("#<%=Milestones.NOTIFY %>").trigger("change");
}

function viewMilestone(element) {

	var milestone = milestonesTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	var f = document.forms["frm_milestone"];
	
	// Set milestone info
	f.milestone_id.value				= milestone[0];
	f.activity.value					= milestone[1];
	f.<%= Milestones.NAME %>.value		= unEscape(milestone[3]);
	$("#milestoneDesc").text(unEscape(milestone[4]));
	f.planned_date.value				= milestone[5];
	f.report_type.checked 				= (milestone[8] == "Y"?"true":"");
	f.label.value						= unEscape(milestone[9]);
	f.<%= Milestones.MILESTONETYPE%>.value = unEscape(milestone[11]);
	f.<%= Milestones.MILESTONECATEGORY%>.value = unEscape(milestone[13]);
	f.<%=Milestones.NOTIFY%>.checked	= (milestone[15] == "true"?"true":"");
	f.<%=Milestones.NOTIFYDAYS%>.value	= milestone[16];
	$("#<%=Milestones.NOTIFICATIONTEXT%>").text(unEscape(milestone[17]));
	
	updateActivityDates(milestone[1]);

	$('#<%= Milestones.MILESTONETYPE %>').trigger('change');
	$('#<%= Milestones.MILESTONECATEGORY %>').trigger('change');
	$("#<%=Milestones.NOTIFY %>").trigger("change");
	$('#milestone-popup').dialog('open');
}

function deleteMilestoneConfirmated(id, activityName) {
	$('#dialog-confirm').dialog("close");
	
	var params = {
		accion: "<%=ProjectPlanServlet.JX_DELETE_MILESTONE%>", 
		milestone_id: id,
		activity_name: activityName
	};
	
	planAjax.call(params, function(data){
		milestonesTable.fnDeleteSelected();
	});
}

function deleteMilestone(element) {
	
	var milestone = milestonesTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	$('#dialog-confirm-msg').html('${msg_confirm_delete_milestone}');
	$('#dialog-confirm').dialog(
			'option', 
			'buttons', 
			{
				"${msg_no}": function() { 
					$('#dialog-confirm').dialog("close"); 
				},
				"${msg_yes}": function() {
					deleteMilestoneConfirmated(milestone[0], milestone[2]);
				}
			}
	);
	
	$('#dialog-confirm').dialog(
			'option',
			'title',
			'${msg_title_confirm_delete_milestone}'
	);
	$('#dialog-confirm').dialog('open');
	return false;
}

function scheduleChart() {
	
	var activitiesTableSettings = activitiesTable.fnSettings();
	
	var idsBySortingArray = [];
	for (var i = 0; i < activitiesTableSettings.asDataSearch.length; i++) {
		idsBySortingArray[i] = activitiesTableSettings.asDataSearch[i].match(/^[0-9]*\s+([0-9]*).*/)[1];
	}
	
	var params = {
		accion: "<%=ProjectPlanServlet.JX_PLAN_GANTT_CHART%>",
		id: $("#id").val(),
		filter_start: $('#filter_start').val(),
		filter_finish: $('#filter_finish').val(),
		idsBySorting: idsBySortingArray.join(",")
	};
	
	planAjax.call(params, function (data) {
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
	    		cellWidth:$('#numberOfMonths').val()
		    };
			
			if ($("#chartGantt").html()) {
				$("#chartGantt").empty();
				$("#chartGantt").ganttView(paramsGantt);
			}else{
				$("#chartGantt").ganttView(paramsGantt);
				createLegend('#legendGantt','<fmt:message key="cost.planned" />','#A9A9A9');
				createLegend('#legendGantt','<fmt:message key="milestone_planned" />','url(images/gantt/milestonePlanned.png) no-repeat; font-size: 13px');
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

function chartWBS() {
	
	var params = {
		accion: "<%=ProjectPlanServlet.JX_WBS_CHART%>",
		id: $("#id").val()
	};
	
	$('#wbsChart').html('${loading_chart}');

	planAjax.call(params, function (data) {

		if (typeof data.name === 'undefined') {
			$("#wbsChart").html('${data_not_found}');
		}
		else {
		
		 	$('#wbsChart').html(initWbs(data));
		 	
		    $("#wbsOrg").jOrgChart({
				chartElement : '#wbsChart'
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

function hideScheduleChart() {
	$('#visibleGantt').hide();
}

function showScheduleChart() {
	$('#visibleGantt').show();
}

function validateWBS() {
	
	var params = {
		accion: "<%=ProjectPlanServlet.JX_WBS_VALIDATE%>", 
		<%=Project.IDPROJECT%>: $("#id").val()
	};
	
	planAjax.call(params);
}

// When document is ready
readyMethods.add(function() {
	
	wbsNodesTable = $('#tb_wbsnodes').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,		
		"bInfo": false,
		"aaSorting": [[1,'asc']],
		"iDisplayLength": 50,
		"aoColumns": [ 
             { "bVisible": false }, 
             { "sClass": "center" }, 
             { "bVisible": true },
             { "bVisible": true },
             { "bVisible": false },
             { "sClass": "center", "bSortable": false },
             { "sClass": "right", "sType": "numeric" },
             { "sClass": "center", "bSortable": false }
     	]
	});
	
	activitiesTable = $('#tb_activities').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"bInfo": false,
		"aaSorting": [[2,'asc']],
		"iDisplayLength": 50,
		"aoColumns": [ 
		              { "bVisible": false }, 
		              { "bVisible": false }, 
		              { "bVisible": true }, 
		              { "sClass": "center", "sType": "es_date" }, 
		              { "sClass": "center", "sType": "es_date" },
		              { "bVisible": false}, 
		              { "bVisible": false },
		              { "sClass": "center", "bSortable": false }
		      		]
	});

	milestonesTable = $('#tb_milestones').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"bInfo": false,
		"aaSorting": [[5,'asc']],
		"iDisplayLength": 50,
		"aoColumns": [
		              { "bVisible": false  }, 
		              { "bVisible": false  }, 
		              { "sClass": "left" },
		              { "sClass": "left" },
		              { "sClass": "left" }, 
		              { "sClass": "center", "sType": "es_date" },		              
		              { "sClass": "left","bSortable": false },
		              { "sClass": "left","bSortable": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "sClass": "left" },
		              { "bVisible": false },
		              { "sClass": "left" },
		              { "bVisible": false },
		              { "sClass": "center", "bSortable": false },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "bVisible": false }
		      		]
	});

	$('#tb_milestones tbody tr').live('click', function (event) {		
		milestonesTable.fnSetSelectable(this,'selected_internal');
	} );

	
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
	
	planValidator = $("#frm_project").validate({
		errorContainer: 'div#project-errors',
		errorLabelContainer: 'div#project-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#project-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			scope_statement: { maxlength: 1500 },
			schedule_hd_description: { maxlength: 1500 }
		}
	});
	
	$('#openCloseAll img').click(function(){
		
		openFlag = (openFlag == false ? true : false );
		
		if(openFlag){
			$('#openCloseAll img').attr('src','images/ico_menos_menos.png');
		}
		else {
			$('#openCloseAll img').attr('src','images/ico_mas_mas.png');
		}
		
		openCloseAllPanels(openFlag);
	});
	
	$('#wbsChartReload').on('click', function(e){
		e.stopPropagation();
		chartWBS();
	});

    // Print charts
    $('#ganttChartPrint').click(function(e) {

        e.stopPropagation();
        chartToImage('#chartGantt', "${titleScheduleChart}");
    });

    $('#wbsChartPrint').click(function(e) {

        e.stopPropagation();
        chartToImage('#wbsChart div.jOrgChart > table', "${titleWbsChart}");
    });
	
});
</script>

<c:if test="${validateWBS}">
<script>readyMethods.add(function() { validateWBS(); });</script>
</c:if>

<c:if test="${op:isStringEquals(project.status, status_initiating) or op:isStringEquals(project.status, status_planning)}">
<script>
	readyMethods.add(function() {
		wbsNodesTable.fnSetColumnVis( 7, true );
	});
</script>
</c:if>
	
<div id="projectTabs">
	<jsp:include page="../common/header_project.jsp">
		<jsp:param value="P1" name="actual_page"/>
	</jsp:include>

    <%-- Information of Project --%>
    <jsp:include page="../common/info_project.jsp" flush="true" />

    <div id="panels" style="padding: 15px;">

		<form name="frm_project" id="frm_project" method="post" action="<%=ProjectPlanServlet.REFERENCE%>">
			<input type="hidden" name="accion" value="" />
			<input type="hidden" name="id" id="id" value="${project.idProject}" />
			<input type="hidden" name="status" value="${project.status}" />
			<input type="hidden" name="wbs_id" id="wbs_id" />
			<input type="hidden" name="message" id="message" value="" />
			<input type="hidden" id="validate" name="validate" value="" />
			<input type="hidden" name="messageUpdateEV" id="messageUpdateEV" value="" />
			<input type="hidden" name="messageUpdateMilestones" id="messageUpdateMilestones" value="" />
			<input type="hidden" name="act_id" id="act_id" />
			<input type="hidden" name="milestone_id" id="milestone_id" />
			<%-- Params for Cost Plan --%>		
			<input type="hidden" name="income_id" id="income_id" />
			<input type="hidden" name="followup_id" id="followup_id" />
			<input type="hidden" name="cost_id" id="cost_id" />
			<input type="hidden" name="cost_type" id="cost_type" />
			<input type="hidden" name="iwo_id" id="iwo_id" />
			<input type="hidden" id="idDocument" name="idDocument" />
			<%-- Params for HR --%>
			<input type="hidden" name="idPerfOrg" id="idPerfOrg" value="${project.performingorg.idPerfOrg }" />
			<%-- Params for KPIs --%>
			<input type="hidden" id="idImportProjects" name="idImportProjects" value=""/>	
			<input type="hidden" name="scrollTop" value="" />
			
	   		<div id="project-errors" class="ui-state-error ui-corner-all hide">
				<p>
					<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
					<strong><fmt:message key="msg.error_title"/></strong>
					&nbsp;(<b><span id="project-numerrors"></span></b>)
				</p>
				<ol></ol>
			</div>
			
			<%-- Open all an close all --%>
			<div id="openCloseAll" style="display:none;">
				<img src="images/ico_mas_mas.png">
			</div>
			
			<%-- SCOPE PLAN --%>
			<fmt:message key="scope" var="titleScope"/>
			<visual:panel id="fieldPlanScope" title="${titleScope}">
				<div class="hColor"><fmt:message key="scope_statement"/></div>						
				<div style="margin-top:10px;">
					<textarea name="scope_statement" id="scope_statement" class="campo" rows="8" style="width: 99%;">${project.scopeStatement}</textarea>
				</div>
				<c:if test="${op:hasPermission(user,project.status,tab)}">
					<div align="right" style="margin-top:10px;">
						<a href="javascript: saveProject();" class="boton"><fmt:message key="save" /></a>
					</div>
				</c:if>
				<div class="hColor" style="margin-top:10px;">
					<fmt:message key="wbs"/>
				</div>
				<table id="tb_wbsnodes" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th width="0%"><fmt:message key="wbs.wbs"/></th>
							<th width="15%"><fmt:message key="wbs.code"/></th>
							<th width="35%"><fmt:message key="wbs.name"/></th>
                            <th width="29%"><fmt:message key="wbs.dictionary"/></th>
							<th width="0%"><fmt:message key="wbs.parent"/></th>
							<th width="4%"><fmt:message key="wbs.ca"/></th>
							<th width="9%"><fmt:message key="wbs.budget"/></th>
							<th width="8%">
								<c:if test="${op:hasPermission(user,project.status,tab)}">
									<img src="images/add.png" class="link" onclick="addWBS()" title="<fmt:message key="add"/>" />
								</c:if>
							</th>
						</tr>
					</thead>
					<c:set var="totalWBS" value="0"/>
					<c:forEach var="wbsNode" items="${wbsnodes}">
						<c:if test="${wbsNode.budget >= 0 and wbsNode.wbsnode != null}">
							<c:set var="totalWBS" value="${totalWBS + wbsNode.budget}"/>
						</c:if>
					</c:forEach>
					<tbody>
						<c:forEach var="wbsNode" items="${wbsnodes}">
							<tr>
                                <td>${wbsNode.idWbsnode}</td>
                                <td>${tl:escape(wbsNode.code)}</td>
                                <td>${tl:escape(wbsNode.name)}</td>
                                <td>${tl:escape(wbsNode.description)}</td>
                                <td>${wbsNode.wbsnode.idWbsnode}</td>
                                <td><input id="wbscheck_${wbsNode.idWbsnode}" value="${wbsNode.idWbsnode}" type="checkbox" ${wbsNode.isControlAccount ? "checked" :"" } disabled/></td>
                                <td id="budget_${wbsNode.idWbsnode}">
									<c:choose>
										<c:when test="${wbsNode.wbsnode == null and not wbsNode.isControlAccount}">
											${tl:toCurrency(totalWBS )}
										</c:when>
										<c:when test="${wbsNode.budget >= 0}">
											${tl:toCurrency(wbsNode.budget )}
										</c:when>
									</c:choose>
								</td>
                                <td>
									<img onclick="editWBS(this)" class="link" src="images/view.png" title="<fmt:message key="view"/>"/>
									<c:if test="${op:hasPermission(user,project.status,tab) and wbsNode.wbsnode ne null and not wbsNode.isControlAccount }">
										&nbsp;&nbsp;&nbsp;
										<img src="images/delete.jpg" class="link" onclick="deleteWBS(${wbsNode.idWbsnode})" title="<fmt:message key="delete"/>" />
									</c:if>
									<c:if test="${!wbsNode.isControlAccount}">
										<img onclick="exportWBS(this)" class="position_right icono" src="images/add_proj.png" title="<fmt:message key="wbs.export"/>"/>
									</c:if>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<a href="#" class="boton" onClick="validateWBS()"><fmt:message key="wbs.validate" /></a>
				
				<%-- CHECKLIST --%>
				<jsp:include page="plan_checklist.inc.jsp" flush="true"/>
				
				<%-- CHART --%>
				<fmt:message key="wbs" var="titleWbs"/>
				<c:set var="wbsChartBTN">
					<img id="wbsChartReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
				</c:set>
                <c:set var="wbsChartPrintBTN">
                    <img id="wbsChartPrint" style="height: 16px; width: 16px;" src="images/print.png" title="<fmt:message key="print"/>">
                </c:set>
				<visual:panel id="content-wbs" title="${titleWbs}" callback="chartWBS" buttons="${wbsChartBTN} ${wbsChartPrintBTN}" cssClass="panel2">
					<div style="overflow-y: hidden; overflow-x: auto; margin:0 auto;">
						<div id="wbsChart"></div>		
					</div>
					<div id="legendWbs" class="legendChart"></div>
				</visual:panel>
			</visual:panel>
	
			<%-- SCHEDULE PLAN --%>
			<fmt:message key="schedule" var="titleSchedule"/>
			<visual:panel id="fieldPlanSchedule" title="${titleSchedule}">
				<div class="hColor"><fmt:message key="schedule_high_level_description"/></div>						
				<div style="margin-top:10px;">
					<textarea name="schedule_hd_description" id="schedule_hd_description" class="campo" rows="8" style="width: 99%;">${project.hdDescription}</textarea>
				</div>
				<c:if test="${op:hasPermission(user,project.status,tab)}">
					<div align="right" style="margin-top:10px;">
						<a href="javascript: saveProject();" class="boton"><fmt:message key="save" /></a>
					</div>
				</c:if>
				<div class="hColor" style="margin-top:10px;"><fmt:message key="activity_list"/></div>
				<table id="tb_activities" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th width="0%"><fmt:message key="activity.wp"/></th>
							<th width="0%"><fmt:message key="activity.act"/></th>
							<th width="62%"><fmt:message key="activity.name"/></th>
							<th width="15%"><fmt:message key="baseline_start"/></th>
							<th width="15%"><fmt:message key="baseline_finish"/></th>
							<th width="0%"><fmt:message key="planned_value"/></th>
							<th width="0%">&nbsp;</th>
							<th width="8%">&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="activity" items="${activities}">
							<c:if test="${activity.wbsnode.isControlAccount}">
								<tr>
									<td>${activity.wbsnode.idWbsnode}</td>
									<td>${activity.idActivity}</td>
									<td>${tl:escape(activity.activityName)}</td>
									<td><fmt:formatDate value="${activity.planInitDate}" pattern="${datePattern}"/></td>
									<td><fmt:formatDate value="${activity.planEndDate}" pattern="${datePattern}"/></td>
									<td>${tl:toCurrency(activity.pv)}</td>
									<td>${tl:escape(activity.wbsdictionary)}</td>
									<td>
										<img onclick="viewActivity(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
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
							<th>&nbsp;</th>
							<th width="27%"><fmt:message key="activity.name"/></th>
							<th width="23%"><fmt:message key="milestone.name"/></th>
							<th width="20%"><fmt:message key="milestone.desc"/></th>
							<th width="5%"><fmt:message key="milestone.planned_date"/></th>							
							<th width="5%"><fmt:message key="milestone.show_sign"/></th>
							<th width="5%"><fmt:message key="milestone.notify"/></th>
							<th>&nbsp;</th>
							<th width="0%"><fmt:message key="milestone.label"/></th>
							<th width="8%"><fmt:message key="milestone.type"/></th>
							<th width="0%"></th><%-- Id MilestoneType --%>
							<th width="8%"><fmt:message key="milestone.category"/></th>
							<th width="0%"></th><%-- Id MilestoneCategory --%>
							<th width="8%">
								<c:if test="${op:hasPermission(user,project.status,tab)}">
									<img onclick="addMilestone()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
								</c:if>
							</th>
							<th>&nbsp;</th>
							<th>&nbsp;</th>
							<th>&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="milestone" items="${milestones}">
							<tr>
								<td>${milestone.idMilestone}</td>
								<td>${milestone.projectactivity.idActivity}</td>
								<td>${tl:escape(milestone.projectactivity.activityName)}</td>
								<td>${tl:escape(milestone.name)}</td>
								<td>${tl:escape(milestone.description)}</td>
								<td><fmt:formatDate value="${milestone.planned}" pattern="${datePattern}"/></td>
								<td>
									<input type="checkbox" disabled="disabled"
										<c:if test="${op:isCharEquals(milestone.reportType,'Y')}">
											checked
										</c:if>
									/>
								</td>
								<td>
									<input type="checkbox" disabled="disabled" ${milestone.notify?"checked":""}/>
								</td>
								<td>${milestone.reportType}</td>
								<td>${tl:escape(milestone.label)}</td>
								<td>${tl:escape(milestone.milestonetype.name)}</td>
								<td>${tl:escape(milestone.milestonetype.idMilestoneType)}</td>
								<td>${tl:escape(milestone.milestonecategory.name)}</td>
								<td>${tl:escape(milestone.milestonecategory.idMilestoneCategory)}</td>
								<td>
									<nobr>
										<img onclick="viewMilestone(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
										<c:if test="${op:hasPermission(user,project.status,tab)}">
											&nbsp;&nbsp;&nbsp;
											<img onclick="deleteMilestone(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
										</c:if>
									</nobr>
								</td>
								<td>${milestone.notify}</td>
								<td>${milestone.notifyDays}</td>
								<td>${tl:escape(milestone.notificationText)}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<%-- CHART GANTT --%>
				<fmt:message key="schedule_chart" var="titleScheduleChart"/>
                <c:set var="ganttChartPrintBTN">
                    <img id="ganttChartPrint" style="height: 16px; width: 16px;" src="images/print.png" title="<fmt:message key="print"/>">
                </c:set>
				<visual:panel id="visibleGantt" cssClass="panel2" title="${titleScheduleChart }" buttons="${ganttChartPrintBTN}">
           			<div style="padding-top:10px;">
           				<span style="margin-right:5px;">
							<fmt:message key="dates.since"/>:&nbsp;
							<input type="text" id="filter_start" class="campo fecha alwaysEditable" value="${valPlanInitDate}"/>
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
			</visual:panel>
			
			<%-- PLAN COSTS --%>
			<fmt:message key="plan-costs" var="titlePlanCosts"/>
			<visual:panel id="fieldPlanCosts" title="${titlePlanCosts }">
				<jsp:include page="plan_cost.inc.jsp" flush="true" />
			</visual:panel>
			
			<%-- PLAN HUMAN RESOURCES --%>
			<fmt:message key="plan_hr" var="titlePlanHr"/>
			<visual:panel id="fieldPlanHR" title="${titlePlanHr }">
				<jsp:include page="../common/human_resources/human_resources.inc.jsp" flush="true" />
			</visual:panel>
			
			<%-- PROJECT CALENDAR --%>
			<jsp:include page="plan_calendar.inc.jsp" flush="true"/>
			
			<%-- TEAM CALENDAR --%>
			<jsp:include page="plan_team_calendar.inc.jsp" flush="true"/>
			
			<%-- KPI --%>
			<jsp:include page="plan_kpi.inc.jsp" flush="true"/>
			
		</form>
		
		<fmt:message var="documentationTitle" key="documentation.planning"/>
		<visual:panel title="${documentationTitle }">
			<jsp:include page="../common/project_documentation.jsp">
				<jsp:param name="documentationType" value="<%=Constants.DOCUMENT_PLANNING %>"/>
			</jsp:include>
		</visual:panel>
			
		<c:if test="${op:hasPermission(user,project.status,tab)}">
			<div style="margin-top:15px;" class="right" id="planButtons">
				<c:if test="${op:isStringEquals(project.status, status_planning)}">

                    <c:if test="<%=(SettingUtil.getBoolean(request, GeneralSetting.ONLY_PMO_CHANGE_STATUS) && SecurityUtil.isUserInRole(request, Constants.ROLE_PMO)) ||
                            !SettingUtil.getBoolean(request, GeneralSetting.ONLY_PMO_CHANGE_STATUS)%>">

                        <a href="javascript:approveProject();" class="boton ui-special" ><fmt:message key="execute_project" /></a>
                    </c:if>
				</c:if>
			</div>						
		</c:if>
	</div>
</div>

<jsp:include page="new_wbsnode_popup.jsp" flush="true" />
<jsp:include page="edit_activity_popup.jsp" flush="true" />
<jsp:include page="new_milestone_popup.jsp" flush="true" />
<jsp:include page="new_cost_popup.jsp" flush="true" />
<jsp:include page="../common/human_resources/edit_teammember_popup.inc.jsp" flush="true" />

<div id="popupData">
    <c:if test="${op:hasPermission(user,project.status,tab)}">
        <jsp:include page="checklist_popup.jsp" flush="true" />
        <jsp:include page="new_income_popup.jsp" flush="true" />
        <jsp:include page="new_followup_popup.jsp" flush="true" />
        <jsp:include page="new_iwo_popup.jsp" flush="true" />
        <jsp:include page="calendar_popup.jsp" flush="true" />
        <jsp:include page="msg_rejected_teammember_popup.jsp" flush="true" />
        <jsp:include page="kpi_popup.jsp" flush="true" />
        <jsp:include page="../../common/search_metric_popup.jsp" flush="true" />

        <fmt:message key="kpi.import" var="titleConfirmMsg"/>
        <fmt:message key="kpi.msg_import" var="confirmMsg"/>
        <jsp:include page="../common/search_projects_and_import_popup.jsp" flush="true">
            <jsp:param name="titleConfirmMsg" value='${titleConfirmMsg}'/>
            <jsp:param name="confirmMsg" value='${confirmMsg}'/>
            <jsp:param name="accion" value="<%=ProjectPlanServlet.IMPORT_KPIS%>"/>
        </jsp:include>
    </c:if>
</div>