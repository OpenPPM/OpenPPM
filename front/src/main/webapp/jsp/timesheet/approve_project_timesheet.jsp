<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.TimeSheetServlet"%>
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
  ~ File: approve_project_timesheet.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="msg.suggestReject" var="msgDescSuggestReject"/>
<fmt:message key="calendar.non_working" var="msgCalendarNonWorking"/>
<fmt:message key="calendar.non_default_work_week" var="msgCalendarNonDefaultWorkWeek"/>
<fmt:message key="calendar.exception_day" var="msgCalendarExceptionDay"/>
<fmt:message key="applevel.approval_pending_pm" var="msgPendingApproval"/>
<fmt:message key="applevel.approval_pending_tm" var="msgNoPosibilityApprovalPM"/>
<fmt:message key="applevel.approval_completed" var="msgApproved"/>

<fmt:message key="applevel.app1" var="msgApp1"/>
<fmt:message key="applevel.app2" var="msgApp2"/>
<fmt:message key="applevel.app3" var="msgApp3"/>

<fmt:message key="applevel.app1_desc" var="msgDescApp1"/>
<fmt:message key="applevel.app2_desc" var="msgDescApp2"/>
<fmt:message key="applevel.app3_desc" var="msgDescApp3"/>

<c:set var="app1" scope="request" value="<%=Constants.TIMESTATUS_APP1 %>"/>
<c:set var="app2" scope="request" value="<%=Constants.TIMESTATUS_APP2 %>"/>

<c:set scope="request" var="btn_app1"><img onclick="javascript:approveTimeTracking(this);" title="<fmt:message key="approve"/>" class="ico link" src="images/approve.png"/>&nbsp;&nbsp;</c:set>
<c:set var="btn_comments" scope="request"><img onclick="javascript:viewComments(this);" title="<fmt:message key="comments.view"/>" class="ico link" src="images/comments_delete.png"/></c:set>
<c:set var="btn_reject" scope="request"><img onclick="javascript:rejectTimeTracking(this);" title="<fmt:message key="reject"/>" class="ico link" src="images/reject.png"/>&nbsp;&nbsp;</c:set>
<c:set var="approveRol"><%= SettingUtil.getApprovalRol(request) %></c:set>

<c:choose>
	<c:when test="<%=SettingUtil.getApprovalRol(request) > 0 %>">
		<c:set var="approveLevel">app2</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="approveLevel">app3</c:set>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
<!--
var timesheetAjax = new AjaxCall('<%=TimeSheetServlet.REFERENCE%>','<fmt:message key="error"/>');
var employeesTable;

var approveProjectTimeSheet = {
		nextWeek: function() {
			var f = document.frm_timesheet;
			f.accion.value = '<%=TimeSheetServlet.NEXT_WEEK%>';
			loadingPopup();
			f.submit();
		},
		prevWeek: function() {
			var f = document.frm_timesheet;
			f.accion.value = '<%=TimeSheetServlet.PREV_WEEK%>';
			loadingPopup();
			f.submit();
		},
		createTablesLegend: function() {
			$('#legendTS').empty();
			
		 	createLegend("#legendNonWorking", "${msgCalendarNonWorking}", "#2F67AD", "black");
		 	createLegend("#legendNonDefaultWorkWeek", "${msgCalendarNonDefaultWorkWeek}", "#BC9ACD", "black");
		 	createLegend("#legendExceptionDay", "${msgCalendarExceptionDay}", "#991D1D", "black");
		 	createLegend("#legendPendingApproval", "${msgPendingApproval}", "orange", "black");
		 	createLegend("#legendSuggestReject", "${msgDescSuggestReject}", "#FAF143", "black");
		 	createLegend("#legendSuggestRejectGeneral", "${msgDescSuggestReject}", "#FAF143", "black");
		 	createLegend("#legendApproved", "${msgApproved}", "#2ECC53", "black");
		 	createLegend("#legendNoApproval", "${msgNoPosibilityApprovalPM}", "#E52727", "black");
		 	
		 	$('.legendChart').css("text-align","left");
		 	$('.legendChart span').css("margin-left","0px");
		},
		initialize: function() {
			$("#next").on("click", approveProjectTimeSheet.nextWeek);
			$("#prev").on("click", approveProjectTimeSheet.prevWeek);
			
			approveProjectTimeSheet.createTablesLegend();
		}
	};

function approveTimeTracking(element) {
	
	var aData = timeSheetTable.fnGetData(element.parentNode.parentNode.parentNode);
	
	confirmTextUI('<fmt:message key="approve"/>', '<fmt:message key="comments"/>',
			'<fmt:message key="yes"/>','<fmt:message key="no"/>',
			function(msg) {
				var params = {
					accion: '<%=TimeSheetServlet.JX_APPROVE_TIMESHEET%>',
					idTimeSheet: aData[1],
					comments: msg
				};
				
				timesheetAjax.call(params, function() {
					
					viewTimeSheet();
				});
			}
	);
}

function rejectTimeTracking(element) {
	
	var aData = timeSheetTable.fnGetData(element.parentNode.parentNode.parentNode);
	
	confirmTextUI('<fmt:message key="reject"/>', '<fmt:message key="comments"/>',
			'<fmt:message key="yes"/>','<fmt:message key="no"/>',
			function(msg) {
				var params = {
					accion: '<%=TimeSheetServlet.JX_REJECT_TIMESHEET%>',
					idTimeSheet: aData[1],
					comments: msg
				};
				
				timesheetAjax.call(params, function() {
					viewTimeSheet();
					
				});
			}
	);
}

function approveTimeTrackingAll() {
	confirmTextUI('<fmt:message key="approve"/>', '<fmt:message key="comments"/>',
			'<fmt:message key="yes"/>','<fmt:message key="no"/>',
			function(msg) {
				
				var f = document.frm_timesheet;
				f.accion.value = '<%=TimeSheetServlet.APPROVE_ALL_TIMESHEET%>';
				f.comments.value = msg;
				loadingPopup();
				f.submit();
			}
	);
}

function rejectTimeTrackingAll() {
	confirmTextUI('<fmt:message key="approve"/>', '<fmt:message key="comments"/>',
			'<fmt:message key="yes"/>','<fmt:message key="no"/>',
			function(msg) {
				
				var f = document.frm_timesheet;
				f.accion.value = '<%=TimeSheetServlet.REJECT_ALL_TIMESHEET%>';
				f.comments.value = msg;
				loadingPopup();
				f.submit();
			}
	);
}

function approveTimeTrackingSel() {
	
	if (employeesTable.fnGetSelectedsCol() == null) {
		alertUI('<fmt:message key="msg.info"/>','<fmt:message key="msg.info.selcet_row"/>');
	}
	else {
		confirmTextUI('<fmt:message key="approve"/>', '<fmt:message key="comments"/>',
				'<fmt:message key="yes"/>','<fmt:message key="no"/>',
				function(msg) {
					
					var f = document.frm_timesheet;
					f.accion.value = '<%=TimeSheetServlet.APPROVE_SEL_TIMESHEET%>';
					f.comments.value = msg;
					f.idEmployees.value = employeesTable.fnGetSelectedsCol();
					f.idProjects.value = employeesTable.fnGetSelectedsCol(2);
					loadingPopup();
					f.submit();
				}
		);
	}
}

function rejectTimeTrackingSel() {
	
	if (employeesTable.fnGetSelectedsCol() == null) {
		alertUI('<fmt:message key="msg.info"/>','<fmt:message key="msg.info.selcet_row"/>');
	}
	else {
		confirmTextUI('<fmt:message key="reject"/>', '<fmt:message key="comments"/>',
				'<fmt:message key="yes"/>','<fmt:message key="no"/>',
				function(msg) {
					
					var f = document.frm_timesheet;
					f.accion.value = '<%=TimeSheetServlet.REJECT_SEL_TIMESHEET%>';
					f.comments.value = msg;
					f.idEmployees.value = employeesTable.fnGetSelectedsCol();
					f.idProjects.value = employeesTable.fnGetSelectedsCol(2);
					loadingPopup();
					f.submit();
				}
		);
	}
}

function viewTimeSheet(idEmployee, idProject) {
	
	var f = document.frm_timesheet;
	f.accion.value = '';

	if (idEmployee != null) {
		
		f.idEmployee.value	= idEmployee;
	}
	if (idProject != null) {
		
		f.idProject.value	= idProject;
	}	
	loadingPopup();
	f.submit();
}

readyMethods.add(function() {
	
	employeesTable = $('#tb_employees').dataTable({
		"sDom": 'T<"clear">lfrtip',
		"oLanguage": datatable_language,
		"bInfo": false,
		"aaSorting": [[1, 'asc']],
		"iDisplayLength": 50,
		"sPaginationType": "full_numbers",
		"oTableTools": { "sRowSelect": "multi", "aButtons": [] },
		"aoColumns": [
              { "bVisible": false },
              { "sClass": "left", "sWidth": "30%" },
              { "bVisible": false },
              { "sClass": "left", "sWidth": "40%" },
              { "sClass": "center", "sWidth": "5%"},
              { "sClass": "center", "sWidth": "5%"},
              { "sClass": "center", "sWidth": "5%"},
              { "sClass": "center", "sWidth": "5%"},
              { "sClass": "center", "sWidth": "5%"},
              { "sClass": "center", "sWidth": "5%", "bSortable": false }
     	]
	});
	
	var timeSheetDate = $('#dateTimeSheets').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var f = document.frm_timesheet;
			f.accion.value = '<%=TimeSheetServlet.CHANGE_WEEK%>';
			loadingPopup();
			f.submit();
		}
	});
	
	var timeSheetDate = $('#dateTimeSheetsResource').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var f = document.frm_timesheet;
			f.date.value = f.dateResource.value;
			f.accion.value = '<%=TimeSheetServlet.CHANGE_WEEK%>';
			loadingPopup();
			f.submit();
		}
	});
	
	$("#filterProject").change( function() {
		employeesTable.fnFilter($("#filterProject").val(),2);
	});
	
	approveProjectTimeSheet.initialize();
});

//-->
</script>

<form name="frm_timesheet" id="frm_timesheet" action="<%=TimeSheetServlet.REFERENCE%>" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="comments" value="" />
	<input type="hidden" name="idEmployees" value="" />
	<input type="hidden" name="idProjects" value="" />
	<input type="hidden" name="idEmployee" value="${employee.idEmployee }" />
	<input type="hidden" name="idProject" value="${project.idProject }" />
	
	<fieldset>
		<table width="100%" cellpadding="4" cellspacing="1">
			<tr>
				<td style="font-size: 9px;"><b><fmt:message key="applevel.app0" />:</b>&nbsp;<fmt:message key="applevel.approval_pending_tm" /></td>
				<td style="font-size: 9px;"><b><fmt:message key="applevel.app1" />:</b>&nbsp;<fmt:message key="applevel.approval_pending_pm" /></td>
				<td style="font-size: 9px;"><b><fmt:message key="applevel.app2" />:</b>&nbsp;<fmt:message key="applevel.approval_pending_pmo" /></td>
				<td style="font-size: 9px;"><b><fmt:message key="applevel.app3" />:</b>&nbsp;<fmt:message key="applevel.hours_validated" /></td>
				<td colspan="2">&nbsp;</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="7" cellspacing="1">
			<tr>
				<th width="5%"><fmt:message key="project"/>:</th>
				<td width="40%">
					<select id="filterProject" class="campo" style="width:100%;">
						<option value="" selected><fmt:message key="select_opt"/></option>
						<c:forEach var="project" items="${projects }">
							<option value="${project.idProject }">${project.projectName}</option>
						</c:forEach>
					</select>
				</td>
				<td width="20%">&nbsp;</td>
				<th width="35%" align="right">
				    <img src="images/arrow_left.png" id="prev" style="margin-right:10px; cursor:pointer;"/>
					<img src="images/arrow_right.png" id="next" style="cursor:pointer;"/>
					&nbsp;${initWeek}&nbsp;-&nbsp;${endWeek}&nbsp;
					<span style="margin-right:5px;">
						<input type="hidden" name="date" id="dateTimeSheets" class="campo fecha alwaysEditable" value="${date}"/>
		    		</span>
				</th>
			</tr>
		</table>
		<table id="tb_employees" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th><fmt:message key="contact.fullname"/></th>
					<th>&nbsp;</th>
					<th><fmt:message key="project"/></th>
					<th><fmt:message key="applevel.app0"/></th>
					<th><fmt:message key="applevel.app1"/></th>
					<th><fmt:message key="applevel.app2"/></th>
					<th><fmt:message key="applevel.app3"/></th>
					<th><fmt:message key="total"/></th>
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="approval" items="${approvals}">
					<c:set var="trColorHours" value="${(approval.app0Hours eq 0.0 && approval.app1Hours eq 0.0 && approval.app2Hours eq 0.0 && approval.app3Hours eq 0.0)?'redRow whiteFont':''}${approval.suggestReject?'colorYellow':''}"/>
					<tr class="${trColorHours }">
						<td>${approval.idEmployee}</td>
						<td>${tl:escape(approval.employeeName)}</td>
						<td>${approval.idProject}</td>
						<td>
							<a href="javascript:navigate.toProject(${approval.idProject})">
								${tl:escape(approval.projectName)}
							</a>
						</td>
						<td class="${empty trColorHours and approval.app0Hours ne 0.0?'redRow':''}">
							<fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${approval.app0Hours}"/>
						</td>
						<td class="${empty trColorHours and approval.app1Hours ne 0.0?'orangeRow whiteFont':''}">
							<fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${approval.app1Hours}"/>
						</td>
						<td class="${empty trColorHours and approval.app2Hours ne 0.0?'colorGreen':''}">
							<fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${approval.app2Hours}"/>
						</td>
						<td class="${empty trColorHours and approval.app3Hours ne 0.0?'colorGreen':''}">
							<fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${approval.app3Hours}"/>
						</td>
						<td> 
							<fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${approval.totalHours}"/>
						</td> 
						<td><img src="images/view.png" onclick="viewTimeSheet(${approval.idEmployee}, ${approval.idProject})" title="<fmt:message key="view"/>"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div>&nbsp;</div>
		<table width="100%" cellpadding="4" cellspacing="1">
			<tr>
				<td><div id="legendNoApproval" class="legendChart"></div></td>
				<td><div id="legendPendingApproval" class="legendChart"></div></td>
				<td><div id="legendApproved" class="legendChart"></div></td>
				<td><div id="legendSuggestRejectGeneral" class="legendChart"></div></td>
			</tr>
		</table>
		<div>
			<a class="button_img position_right" href="javascript:rejectTimeTrackingSel();"><img src="images/reject.png"/><span><fmt:message key="reject"/></span></a>
			<a class="button_img position_right" href="javascript:approveTimeTrackingSel();"><img src="images/approve.png"/><span><fmt:message key="approve"/></span></a>
		</div>
						
		<div>&nbsp;</div>
		<c:if test="${timeSheets ne null and not empty timeSheets}">
			<fieldset>
				<legend>${tl:escape(employee.contact.fullName) } - ${tl:escape(project.projectName) }</legend>
				<div>&nbsp;</div>
				<table width="100%" border="0" cellpadding="7" cellspacing="1">
					<tr>
						<th width="5%">
							<c:if test="<%=SettingUtil.getApprovalRol(request) > 0%>">
								<fmt:message key="filter"/>:
							</c:if>
						</th>
						<td width="40%">
							<c:if test="<%=SettingUtil.getApprovalRol(request) > 0%>">
								<select id="filter" class="campo" style="width:100%;">
									<option value="" selected><fmt:message key="select_opt"/></option>
									<option value="<fmt:message key="applevel.app1"/>"><fmt:message key="applevel.app1_desc" /></option>
										<option value="<fmt:message key="applevel.app2"/>"><fmt:message key="applevel.app2_desc" /></option>
								</select>
							</c:if>
						</td>
						<td width="20%">&nbsp;</td>
						<th width="35%" align="right">
							&nbsp;${initWeek}&nbsp;-&nbsp;${endWeek}&nbsp;
							<span style="margin-right:5px;">
								<input type="hidden" name="dateResource" id="dateTimeSheetsResource" class="campo fecha alwaysEditable" value="${date}"/>
					    	</span>
						</th>
					</tr>
				</table>
				
				<%-- TIME SHEET TABLE --%>
				<jsp:include page="common/table_timesheet.inc.jsp" flush="false"/>
				<div style="margin-top: 10px;">
					<a class="button_img position_right" href="javascript:rejectTimeTrackingAll();"><img src="images/reject.png"/><span><fmt:message key="reject.all"/></span></a>
					<a class="button_img position_right" href="javascript:approveTimeTrackingAll();"><img src="images/approve.png"/><span><fmt:message key="approve.all"/></span></a>
				</div>
				
				<table width="100%" cellpadding="4" cellspacing="1">
					<tr>
						<td><div id="legendNonWorking" class="legendChart"></div></td>
						<td><div id="legendNonDefaultWorkWeek" class="legendChart"></div></td>
						<td><div id="legendExceptionDay" class="legendChart"></div></td>
						<td><div id="legendSuggestReject" class="legendChart"></div></td>
					</tr>
				</table>
			</fieldset>
		</c:if>
	</fieldset>
</form>
<jsp:include page="common/comments.inc.jsp" flush="true"/>