<%@page import="es.sm2.openppm.core.common.Configurations"%>
<%@page import="es.sm2.openppm.core.model.impl.Label"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.utils.OpenppmUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Fundingsource"%>
<%@page import="es.sm2.openppm.core.model.impl.Geography"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectactivity"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Category"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.model.impl.Stagegate"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectInitServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@ page import="es.sm2.openppm.core.logic.setting.VisibilityProjectSetting" %>
<%@ page import="es.sm2.openppm.core.logic.setting.VisibilityInvestmentSetting" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
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
  ~ File: list_investments.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:03
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%
	HashMap<String, String> settings = SettingUtil.getSettings(request);
%>

<fmt:message key="error" var="fmtError" />
<fmt:message key="data_not_found" var="dataNotFound" />
<c:choose>
	<c:when test="<%=SecurityUtil.hasPermission(request, ProjectServlet.JX_APPROVE_INVESTMENT) || SecurityUtil.hasPermission(request, ProjectServlet.LIST_INVESTMENTS) %>">
		<c:set var="buttonInv">
			&nbsp;<img onclick="viewInvestment('+aData[0]+')" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
			'&nbsp;&nbsp;&nbsp;<img onclick="openApproveInvestment('+aData[0]+')" title="<fmt:message key="investment.change_status"/>" class="link ico" src="images/modify.png">
		</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="buttonInv">
			&nbsp;<img onclick="viewInvestment('+aData[0]+')" title="<fmt:message key="view"/>" class="link" src="images/view.png">
		</c:set>
	</c:otherwise>
</c:choose>
<c:set var="investmentScript">
	{
		"sExtends" : "text",
		"sButtonText": '<fmt:message key="export.csv" />',
		"fnClick": function ( nButton, oConfig, oFlash ) { investmentsToCSV(); }
	},
	{
		"sExtends" : "text",
		"sButtonText": '<fmt:message key="executive_report" />',
		"fnClick": function ( nButton, oConfig, oFlash ) { generateStatusReport(); }
	},
	{
		"sExtends" : "select_none",
		"sButtonText": "<fmt:message key="tabledata.deselect_all"/>"
	}
</c:set>

<script language="javascript" type="text/javascript" >
function investmentsToCSV() {
	var f = document.forms["frm_investments"];
	f.action = "<%=ProgramServlet.REFERENCE%>";
	f.accion.value = "<%=ProgramServlet.EXPORT_INVESTMENTS_CSV%>";
	f.ids.value = dataInfoTable.ids;
	if(f.ids.value == "") {
		alertUI("${error_to_csv}", "${msg_no_export_to_csv}");
	}
	else {
		f.submit();	
	}		
}
</script>

<c:if test="<%=(SecurityUtil.isUserInRole(request, Constants.ROLE_PMO) || SecurityUtil.isUserInRole(request, Constants.ROLE_IM)) %>">
	<c:set var="investmentScript">
		${investmentScript }
		,{
			"sExtends" : "text",
			"sButtonText": '<fmt:message key="new" />',
			"fnClick": function ( nButton, oConfig, oFlash ) { newInvestment(); },
			"sButtonClass": "newproject",
			"sButtonClassHover": "newproject"
		}
	</c:set>
<script language="javascript" type="text/javascript" >
<!--
	function newInvestment() {
		var f = document.forms["frm_investments"];
	
		f.action = '<%=ProjectServlet.REFERENCE%>';
		f.accion.value = "<%=ProjectServlet.NEW_INVERTMENT%>";
		loadingPopup();
		f.submit();
	}
//-->
</script>
</c:if>
<script language="javascript" type="text/javascript" >
var investmentsTable,
	bubbleChart,
	dataInfoTable,
	loadDashboard = false,
	loadGantt = false,
	loadStatusReport = false;

var programAjax = new AjaxCall('<%=ProgramServlet.REFERENCE%>','<fmt:message key="error" />');
var projectAjax = new AjaxCall('<%=ProjectServlet.REFERENCE%>','<fmt:message key="error" />');

function viewInvestment(id) {
	var f = document.forms["frm_investments"];
	f.id.value = id;
	f.action = "<%=ProjectInitServlet.REFERENCE%>";
	f.accion.value = "<%=ProjectInitServlet.VIEW_INVESTMENT%>";
	loadingPopup();
	f.submit();
}

function generateStatusReport() {
	
	var f = document.forms["frm_investments"];
	
	f.action = '<%=ProjectServlet.REFERENCE%>';
	f.accion.value = "<%=ProjectServlet.GENERATE_STATUS_REPORT%>";
	f.ids.value = dataInfoTable.ids;
	f.submit();
}

function showChartGantt() {	
	if (typeof dataInfoTable === 'undefined') { loadGantt = true; }
	else {
		applyInfoSort();
		
		var params = {
			accion: "<%=ProgramServlet.JX_CONS_GANTT%>",
			ids: dataInfoTable.ids,	
			propiedad: dataInfoTable.propiedad,
			orden: dataInfoTable.orden,
			since: $('#since').val(), 	
			until: $('#until').val(),
            showMilestones: $('#showMilestones').is(':checked')
		};
		
		programAjax.call(params, function(data) {
			
			if (data.noData) {
				
				$("#chart_gantt").html('${dataNotFound}');
				$("#chart_gantt").attr("style", "text-align: center; margin-top: 15px;");
				$("#legendGantt").html('');
			}
			else {
				
				$("#chart_gantt").attr("style", "margin: 20px auto; width: 100%;");
				
				var monthNames = ['<fmt:message key="month.min_1"/>','<fmt:message key="month.min_2"/>','<fmt:message key="month.min_3"/>',
				                  '<fmt:message key="month.min_4"/>','<fmt:message key="month.min_5"/>','<fmt:message key="month.min_6"/>',
				                  '<fmt:message key="month.min_7"/>','<fmt:message key="month.min_8"/>','<fmt:message key="month.min_9"/>',
				                  '<fmt:message key="month.min_10"/>','<fmt:message key="month.min_11"/>','<fmt:message key="month.min_12"/>'];
				
				var paramsGantt = {
		    		data: data.tasks,
		    		monthNames: monthNames,
		    		cellWidth:$('#numberOfMonths').val(),
                    showPoc: true
			    };
				
				if ($("#chart_gantt").html()) {
					$("#chart_gantt").empty();
					$("#chart_gantt").ganttView(paramsGantt);
				}
				else {
					$("#chart_gantt").ganttView(paramsGantt);
					createLegend('#legendGantt','<fmt:message key="cost.actual" />','#4567aa');
					createLegend('#legendGantt','<fmt:message key="actual.dependent" />','#FF8F35');
					createLegend('#legendGantt','<fmt:message key="percent_complete" />','#000000');
					createLegend('#legendGantt','<fmt:message key="cost.planned" />','#A9A9A9');
					createLegend('#legendGantt','<fmt:message key="planned.dependent" />','#FEC18F');
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
		    show('chart_valuechart_gantt');
		});
	}
}

function resetDatesGantt() {
	$("#since").val('');
	$("#until").val('');
    $('#showMilestones').prop('checked', 'checked');
	showChartGantt();
}

function applyInfoSort() {
	
	var numColumn = investmentsTable.fnSettings().aaSorting[0][0];
	var typeOrder = investmentsTable.fnSettings().aaSorting[0][1];
	var orderName = investmentsTable.fnSettings().aoColumns[numColumn].sName;
	
	dataInfoTable.propiedad = orderName;
	dataInfoTable.orden = typeOrder;
	
}

function loadProjects() {

	var aoData = [];

	var ids = $('#ids').val();
	if ($('#ids').val() != '' && ids != null && ids != 'null') { $("#filterColumnSelected").show(); }
	else { $("#filterColumnSelected").hide(); }
	
	aoData.push( { "name": "accion", "value": "<%=ProjectServlet.JX_FILTER_TABLE_INVESTMENTS %>" } );
	aoData.push( { "name": "<%=Project.INTERNALPROJECT%>", "value": $('#filterInternal').val() } );
	aoData.push( { "name": "<%=Project.ISGEOSELLING%>", "value": $('#requiresTravelling').val() } );
	aoData.push( { "name": "<%=Projectactivity.PLANINITDATE%>", "value": $('#filter_start').val() } );
	aoData.push( { "name": "<%=Projectactivity.PLANENDDATE%>", "value": $('#filter_finish').val() } );
	aoData.push( { "name": "<%=Project.BUDGETYEAR%>", "value": $('#<%=Project.BUDGETYEAR%>').val() } );
	aoData.push( { "name": "customertype.idCustomerType", "value": $('#idCustomerType').val()+"" } );
	aoData.push( { "name": "customer.idCustomer", "value": $('#idCustomer').val()+"" } );
	aoData.push( { "name": "program.idProgram", "value": $('#idProgram').val()+"" } );
	aoData.push( { "name": "employeeByProjectManager.idEmployee", "value": $('#idPM').val()+"" } );
	aoData.push( { "name": "employeeBySponsor.idEmployee", "value": $('#idSponsor').val()+"" } );
	aoData.push( { "name": "category.idCategory", "value": $('#<%=Category.IDCATEGORY%>').val()+"" } );
	aoData.push( { "name": "seller.idSeller", "value": $('#idSeller').val()+"" } );
	aoData.push( { "name": "geography.idGeography", "value": $('#<%=Geography.IDGEOGRAPHY%>').val()+"" } );
	aoData.push( { "name": "fundingsource.idFundingSource", "value": $('#<%=Fundingsource.IDFUNDINGSOURCE%>').val()+"" } );
	aoData.push( { "name": "<%=Project.IDPROJECT%>", "value": ids } );
	aoData.push( { "name": "<%=Project.PROJECTNAME%>", "value": $('#<%=Project.PROJECTNAME%>').val() } );
	aoData.push( { "name": "first", "value": $('#first').val() } );
	aoData.push( { "name": "last", "value": $('#last').val() } );
	aoData.push( { "name": "filterPriority", "value": $('#filterPriority').val() });
	aoData.push( { "name": "label.idLabel", "value": $('#<%= Label.IDLABEL %>').val()+"" } );
	aoData.push( { "name": "stageGate.idStageGate", "value": $('#<%= Stagegate.IDSTAGEGATE %>').val()+"" } );
	<%-- Load configurations --%>
	aoData.push( { "name": "<%= Configurations.LIST_FILTERS_CONTRACT_TYPE %>", "value": $('#<%= Configurations.LIST_FILTERS_CONTRACT_TYPE %>').val()+"" } );
	aoData.push( { "name": "<%= Configurations.LIST_FILTERS_FM %>", "value": $('#<%= Configurations.LIST_FILTERS_FM %>').val()+"" } );
	aoData.push( { "name": "<%= Configurations.BOOLEAN_FILTER_SELLERS %>", "value": $('#<%= Configurations.BOOLEAN_FILTER_SELLERS %>').val() } );
    aoData.push( { "name": "<%= Configurations.LIST_FILTERS_FOUNDING_SOURCE %>", "value": $('#<%= Configurations.LIST_FILTERS_FOUNDING_SOURCE %>').val()+"" } );
    aoData.push( { "name": "<%= Configurations.LIST_FILTERS_TECHNOLOGY %>", "value": $('#<%= Configurations.LIST_FILTERS_TECHNOLOGY %>').val()+"" } );
	
	<% if (SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)) {%>
	aoData.push( { "name": "performingorg.idPerfOrg", "value": $('#idPO').val()+"" } );
	<%}%>
	
    var checkboxs = document.getElementsByName('filter');

    for(var i = 0, inp; inp = checkboxs[i]; i++) {
        if (inp.type.toLowerCase() == 'checkbox' && inp.checked) {
			aoData.push( { "name": "<%=Project.INVESTMENTSTATUS%>", "value": inp.value } );
        }
    }
    
    investmentsTable.fnClearTable();
    
	projectAjax.call(aoData,function (data) {
		
		$('#sumBudget').text(toCurrency(data.sumBudget));
		$('#sumBac').text(toCurrency(data.sumBac));
		
		dataInfoTable = data.info;

		if (loadDashboard) { loadDashboard = false; showValueChart(); }
		if (loadGantt) { loadGantt = false; showChartGantt(); }
		if (loadStatusReport) { loadStatusReport = false; infoStatusReport(); }
		
		investmentsTable.fnAddData(data.aaData);
	});
}

function viewProject(id, status) {

	var f = document.forms["frm_investments"];
	f.id.value = id;

	f.accion.value = "";
	
	if (status == '<%=Constants.STATUS_PLANNING%>') {
		f.action = "<%=ProjectPlanServlet.REFERENCE%>";
	}
	else if (status == '<%=Constants.STATUS_CONTROL%>') {
		f.action = "<%=ProjectControlServlet.REFERENCE%>";
	}
	else {
		f.action = "<%=ProjectInitServlet.REFERENCE%>";
	}
	loadingPopup();
	f.submit();
}

readyMethods.add(function () {
	
	custType = $('#idCustomerType').filterSelect({'selectFilter':'idCustomer'});
	loadFilterState();
	
	investmentsTable = $('#tb_investments').dataTable({
		"sDom": 'T<"clear">lfrtip',
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"iDisplayLength": 25,
		"aaSorting": [[3,'desc']],
		"bFilter": false,
		"bStateSave": true,
		"iCookieDuration": 31536000,
		"sCookiePrefix":"openppm_inv_",
		"oTableTools": {
			"sRowSelect": "multi",
			"sSwfPath": "swf/copy_cvs_xls_pdf.swf",
			"aButtons": [ <%=pageContext.getAttribute("investmentScript")%> ]
		},
		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
			$('td:eq(<%=OpenppmUtil.numOfCulumnsForInvestment(request, settings)%>)', nRow).html('<nobr>${buttonInv}</nobr>');
					
			return nRow;
		},
		"aoColumns": [
                { "sName":"id", "bVisible": <%= SettingUtil.getBoolean(settings, VisibilityProjectSetting.PROJECT_COLUMN_IDPROJECT)%>, "sClass": "center", "sWidth": "4%"},
	            { "sName":"investmentStatus", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_STATUS, Settings.DEFAULT_INVESTMENT_COLUMN_STATUS))%>, "sClass": "left", "sWidth": "8%"}, 
	            { "sName":"accountingCode", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_ACCOUNTING_CODE, Settings.DEFAULT_INVESTMENT_COLUMN_ACCOUNTING_CODE))%>, "sClass": "left", "sWidth": "7%"}, 
	            { "sName":"projectName", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_NAME, Settings.DEFAULT_INVESTMENT_COLUMN_NAME))%>, "sClass": "left", "sWidth": "20%" },
	            { "sName":"chartLabel", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_SHORT_NAME, Settings.DEFAULT_INVESTMENT_COLUMN_SHORT_NAME))%>, "sClass": "left", "sWidth": "8%" },
	            <% if (SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)) {%>
	            { "sName":"performingorg", "sClass": "left", "sWidth": "12%", "bSortable": false },
	            <%}%>
	            { "sName":"program", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_PROGRAM, Settings.DEFAULT_INVESTMENT_COLUMN_PROGRAM))%>, "sClass": "left", "sWidth": "18%" },
	            { "sName":"tcv", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_BUDGET, Settings.DEFAULT_INVESTMENT_COLUMN_BUDGET))%>, "sClass": "right", "sWidth": "10%", "sType": "currency" },
	            { "sName":"bac", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_BAC, Settings.DEFAULT_PROJECT_COLUMN_BAC))%>, "sClass": "right",  "sWidth": "8%", "sType": "currency" },
                { "sName":"duration", "bVisible": <%= SettingUtil.getBoolean(settings, VisibilityInvestmentSetting.INVESTMENT_COLUMN_DURATION)%>, "sClass": "center", "sWidth": "4%"},
                { "sName":"priority", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_PRIORITY, Settings.DEFAULT_INVESTMENT_COLUMN_PRIORITY))%>, "sClass": "right", "sWidth": "5%" },
	            { "sName":"probability", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_POC, Settings.DEFAULT_INVESTMENT_COLUMN_POC))%>, "sClass": "right", "sWidth": "7%" },
	            { "sName":"baselineInitDate", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_BASELINE_START, Settings.DEFAULT_INVESTMENT_COLUMN_BASELINE_START))%>, "sClass": "center", "sWidth": "7%", "sType": "es_date" },
	            { "sName":"baselineFinishDate", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_BASELINE_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_BASELINE_FINISH))%>, "sClass": "center", "sWidth": "7%", "sType": "es_date" },
	            { "sName":"plannedInitDate", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_PLANNED_START, Settings.DEFAULT_INVESTMENT_COLUMN_PLANNED_START))%>, "sClass": "center", "sWidth": "7%", "sType": "es_date" },
	            { "sName":"plannedFinishDate", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_PLANNED_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_PLANNED_FINISH))%>, "sClass": "center", "sWidth": "7%", "sType": "es_date" },
	            { "sName":"startDate", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_START, Settings.DEFAULT_INVESTMENT_COLUMN_START))%>, "sClass": "center", "sWidth": "7%", "sType": "es_date" },
	            { "sName":"finishDate", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_FINISH))%>, "sClass": "center", "sWidth": "7%", "sType": "es_date" },
	            { "sName":"effort", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_INTERNAL_EFFORT, Settings.DEFAULT_INVESTMENT_COLUMN_INTERNAL_EFFORT))%>, "sClass": "right" },
	            { "sName":"externalCost", "bVisible": <%=Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_EXTERNAL_COST, Settings.DEFAULT_INVESTMENT_COLUMN_EXTERNAL_COST))%>, "sClass": "right", "sType": "currency", "bSortable": false },
                { "sName":"stagegate", "bVisible": <%= SettingUtil.getBoolean(settings, VisibilityInvestmentSetting.INVESTMENT_COLUMN_STAGEGATE)%>, "sClass": "left",   "sWidth": "10%" },
                { "sClass": "left", "sWidth": "7%", "bSortable": false }
      		]
	});
	
	var sinceUntil = $('#since, #until').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "since" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			sinceUntil.not(this).datepicker("option", option, date);
		}
	});
	
	createBT('.btitle');
	loadProjects();
});
</script>

<!-- Form to download a file -->
<form name="frm_project" id="frm_project" method="post">
	<input type="hidden" name="accion" value="" />	
	<input type="hidden" id="idDocument" name="idDocument" />
	<input type="hidden" name="scrollTop" />
</form>

<form id="frm_investments" name="frm_investments" method="post" onsubmit="return false;">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" id="id" name="id" />
	<input type="hidden" id="ids" name="ids" />

	<jsp:include page="common/filter_investment.jsp" flush="true">
		<jsp:param value="investmentsTable" name="table"/>
	</jsp:include>
	<div>
		<table id="tb_investments" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
                    <th><fmt:message key="id" />&nbsp;<fmt:message key="investment" /></th>
					<th><fmt:message key="status" /></th>
					<th><fmt:message key="project.accounting_code" /></th>
					<th><fmt:message key="investment" /></th>
					<th><fmt:message key="project.chart_label" /></th>
					<c:if test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)%>">
						<th><fmt:message key="perf_organization" /></th>
					</c:if>
					<th><fmt:message key="program" /></th>
					<th>
						<fmt:message key="activity.budget" />
						<br><span id="sumBudget"></span>
					</th>
					<th>
						<fmt:message key="table.bac" />
						<br><span id="sumBac"></span>
                    </th>
                    <th><fmt:message key="sac" /></th>
					<th><fmt:message key="table.priority" /></th>
					<th><fmt:message key="table.probability" /></th>
					<th><fmt:message key="investment.baseline_start" /></th>
					<th><fmt:message key="investment.baseline_finish" /></th>
					<th><fmt:message key="planned_start" /></th>
					<th><fmt:message key="planned_finish" /></th>
					<th><fmt:message key="calendar.start" /></th>
					<th><fmt:message key="calendar.finish" /></th>
					<th><fmt:message key="internal_effort" /></th>
					<th><fmt:message key="external_costs" /></th>
                    <th><fmt:message key="stage_gate" /></th>
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
</form>

<%-- STATUS REPORT --%>
<c:if test="<%= Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_SHOW_STATUS_REPORT_IN_INVESTMENT, Settings.SETTING_SHOW_STATUS_REPORT_IN_INVESTMENT_DEFAULT))%>">
	<jsp:include page="common/status_report.inc.jsp" flush="true">
		<jsp:param name="page" value="<%=Constants.PAGE_INVESTMENT %>"/>
	</jsp:include>
</c:if>

<%-- SCHEDULE CHART --%>
<fmt:message key="schedule_chart" var="titleScheduleChart"/>
<visual:panel id="chart_valuechart_gantt" title="${titleScheduleChart }" callback="showChartGantt">
	<div style="padding-top:10px;">
    	<span style="margin-right:5px;">
			<fmt:message key="dates.since"/>:&nbsp;
			<input type="text" id="since" class="campo fecha alwaysEditable"/>
    	</span>
   		<span style="margin-right:5px;">
			<fmt:message key="dates.until"/>:&nbsp;
			<input type="text" id="until" class="campo fecha alwaysEditable"/>
		</span>
		<span style="margin-right:5px;">
			<fmt:message key="ZOOM"/>:&nbsp;
			<select id="numberOfMonths" class="campo alwaysEditable" style="width:120px">
				<option value="1">24</option>
				<option value="2" selected>12</option>
  				<option value="4">6</option>
				<option value="6">4</option>
			  	<option value="8">3</option>
			</select>
		</span>
        <label style="margin-right:5px;">
            <input type="checkbox" id="showMilestones" checked style="width: 15px"/>&nbsp
            <fmt:message key="MILESTONES.SHOW"/>
        </label>
        <a href="javascript:exportImage('chart_gantt');" class="boton"><fmt:message key="EXPORT"/></a>
		<a href="javascript:showChartGantt();" class="boton"><fmt:message key="chart.refresh"/></a>
		<a href="javascript:resetDatesGantt();" class="boton"><fmt:message key="filter.default"/></a>
       </div>
	<div id="chart_gantt" style="margin: 20px auto;"></div>
	<div id="legendGantt" class="legendChart"></div>
</visual:panel>

<%-- BUBBLE CHART --%>
<jsp:include page="../charts/bubble_chart.jsp" flush="true">
	<jsp:param name="page" value="investments"/>
</jsp:include>

<jsp:include page="common/approve_investment.jsp" flush="true"/>