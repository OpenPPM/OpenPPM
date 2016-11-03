<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.model.impl.Timesheet"%>
<%@page import="es.sm2.openppm.front.servlets.TimeSheetServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
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
  ~ File: timesheet.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="allow_working_non_labor" var="msgNonLaborDay"/>
<fmt:message key="restore_calendar_exceptions" var="msgRestoreCalendar"/>

<fmt:message key="save" var="msgSave"/>
<fmt:message key="reject.resource.suggest_reject" var="msgSuggestReject"/>
<fmt:message key="msg.suggestReject" var="msgSuggestRejectLegend"/>

<fmt:message key="approve" var="msgApprove"/>
<fmt:message key="comments.view" var="msgCommentsView"/>
<fmt:message key="comments" var="msgComments"/>
<fmt:message key="yes" var="msgYes"/>
<fmt:message key="no" var="msgNo"/>

<fmt:message key="approve.all" var="msgApproveAll"/>
<fmt:message key="save.all" var="msgSaveAll"/>
<fmt:message key="clear_all_hours" var="msgClearAllHours"/>
<fmt:message key="allow_working_non_labor" var="msgAllowWorkingNonLabor"/>
<fmt:message key="add_operation" var="msgAddOperation"/>

<fmt:message key="error" var="msgError"/>
<fmt:message key="msg.error.no_hours" var="msgErrorNoHours"/>

<fmt:message key="filter" var="msgFilter"/>
<fmt:message key="select_opt" var="msgSelectOpt"/>
<fmt:message key="applevel.app0" var="msgAppLevelApp0"/>
<fmt:message key="applevel.app0_desc" var="msgAppLevelApp0Desc"/>
<fmt:message key="applevel.app1" var="msgAppLevelApp1"/>
<fmt:message key="applevel.app1_desc" var="msgAppLevelApp1Desc"/>
<fmt:message key="applevel.app2" var="msgAppLevelApp2"/>
<fmt:message key="applevel.app2_desc" var="msgAppLevelApp2Desc"/>
<fmt:message key="applevel.app3" var="msgAppLevelApp3"/>
<fmt:message key="applevel.app3_desc" var="msgAppLevelApp3Desc"/>

<fmt:message key="calendar.non_working" var="msgCalendarNonWorking"/>
<fmt:message key="calendar.non_default_work_week" var="msgCalendarNonDefaultWorkWeek"/>
<fmt:message key="calendar.exception_day" var="msgCalendarExceptionDay"/>

<fmt:message key="applevel.approval_pending_pmo" var="msgPendingApproval"/>
<fmt:message key="applevel.approval_pending_tm" var="msgPendingTM"/>
<fmt:message key="applevel.approval_completed" var="msgApproved"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDeleteOperation">
	<fmt:param><fmt:message key="operation"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDeleteOperation">
	<fmt:param><fmt:message key="operation"/></fmt:param>
</fmt:message>

<c:set var="app0" scope="request" value="<%=Constants.TIMESTATUS_APP0 %>"/>
<c:set var="app1" scope="request" value="<%=Constants.TIMESTATUS_APP1 %>"/>
<c:if test="${not editDisabled }">
	<c:set scope="request" var="btn_app0">
		<img onclick="javascript:saveTimeTracking(this);" title="${msgSave}" class="ico link" class="btnSave" src="images/save.png"/>&nbsp;&nbsp;
		<img onclick="javascript:approveTimeTracking(this);" title="${msgApprove}" class="ico link" class="btnApprove" src="images/approve.png"/>&nbsp;&nbsp;
	</c:set>	
</c:if>
<c:set var="btn_comments" scope="request">
	<img onclick="javascript:viewComments(this);" title="${msgCommentsView}" class="ico link"  src="images/comments_delete.png"/>
</c:set>
<c:set scope="request" var="btnSuggestReject">	
	<img  title="${msgSuggestReject}" class="btnSuggestReject ico link"  src="images/suggestReject.png"/>
</c:set>

<c:set var="pendingTM" value='<%=Settings.SHOW_TIMESHEET_COLOR?"pendingTM":"" %>'/>

<script type="text/javascript">
<!--
var timesheetAjax = new AjaxCall('<%=TimeSheetServlet.REFERENCE%>',"${msgError}");

var timeSheet = {
	nextWeek:function() {
		var f = document.frm_timesheet;
		f.accion.value = '<%=TimeSheetServlet.NEXT_WEEK%>';
		loadingPopup();
		f.submit();
	},
	prevWeek:function() {
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
	 	createLegend("#legendSuggestReject", "${msgSuggestRejectLegend}", "#FAF143", "black");
	 	createLegend("#legendPendingApproval", "${msgPendingApproval}", "orange", "black");
	 	createLegend("#legendApproved", "${msgApproved}", "#2ECC53", "black");
	 	
	 	<% if (Settings.SHOW_TIMESHEET_COLOR) { %>
		 	createLegend("#legendNoApproval", "${msgPendingTM}", "#D8E6FD", "black");
		<% } %>
		
	 	$('.legendChart').css("text-align","left");
	 	$('.legendChart span').css("margin-left","0px");
	},
	initialize:function() {
		
		$("#next").on("click", timeSheet.nextWeek);
		$("#prev").on("click", timeSheet.prevWeek);
		
		timeSheet.createTablesLegend();
	}
};

function saveTimeTrackingAll() {
	
	var f = document.frm_timesheet;
	f.accion.value = '<%=TimeSheetServlet.SAVE_ALL_TIMESHEET%>';
	loadingPopup();
	f.submit();
	
}

function deleteOperation(element) {

	var aData = timeSheetTable.fnGetData(element.parentNode.parentNode.parentNode);
	
	confirmUI('${msgTitleConfirmDeleteOperation}', '${msgConfirmDeleteOperation}',
			"${msgYes}","${msgNo}",
			function(msg) {
				
				var f = document.frm_timesheet;
				f.accion.value = '<%=TimeSheetServlet.DEL_OPERATION%>';
				f.idTimeSheetOperation.value = aData[1];
				loadingPopup();
				f.submit();
			}
	);
}

function approveTimeTrackingAll() {
	
	confirmTextUI("${msgApprove}", "${msgComments}",
			"${msgYes}","${msgNo}",
			function(msg) {
				
				var f = document.frm_timesheet;
				f.accion.value = '<%=TimeSheetServlet.APPROVE_ALL_TIMESHEET%>';
				f.comments.value = msg;
				loadingPopup();
				f.submit();
			}
	);
}

function saveTimeTracking(element) {
	
	var aData = timeSheetTable.fnGetData(element.parentNode.parentNode.parentNode);
	
	var params = {
		accion: '<%=TimeSheetServlet.JX_SAVE_TIMESHEET%>',
		idTimeSheet: aData[1],
		day1: $('#day1_'+aData[1]).val(),
		day2: $('#day2_'+aData[1]).val(),
		day3: $('#day3_'+aData[1]).val(),
		day4: $('#day4_'+aData[1]).val(),
		day5: $('#day5_'+aData[1]).val(),
		day6: $('#day6_'+aData[1]).val(),
		day7: $('#day7_'+aData[1]).val()
	};
	
	
	timesheetAjax.call(params, function(data) {
		
		$('#changeRowColor_'+aData[1]).removeClass('${pendingTM}');
		$('#changeRowColor_'+aData[1]).removeClass("whiteFont");
		
		$("#changeRowColor_"+aData[1]+" .campo").each( function() {
			var value = $(this).val();	
			if (value > 0){
				$(this).parents('td').addClass("${pendingTM}");
			}
			else {
				$(this).parents('td').removeClass("${pendingTM}");
			}
		});
		$("#changeRowColor_"+aData[1]+" .campo:contains(0.0)").parent("td").removeClass('${pendingTM}');
		$("#changeRowColor_"+aData[1]+" .total:contains(0.0)").parent("#changeRowColor_"+aData[1]).addClass('${pendingTM}');
	});
}
function approveTimeTracking(element) {
	
	var aData = timeSheetTable.fnGetData(element.parentNode.parentNode.parentNode);

	var total = 0;
	total += Number($('#day1_'+aData[1]).val());
	total += Number($('#day2_'+aData[1]).val());
	total += Number($('#day3_'+aData[1]).val());
	total += Number($('#day4_'+aData[1]).val());
	total += Number($('#day5_'+aData[1]).val());
	total += Number($('#day6_'+aData[1]).val());
	total += Number($('#day7_'+aData[1]).val());

	if (total > 0) {
		confirmTextUI("${msgApprove}", "${msgComments}",
				"${msgYes}","${msgNo}",
				function(msg) {
					var params = {
						accion: '<%=TimeSheetServlet.JX_APPROVE_TIMESHEET%>',
						idTimeSheet: aData[1],
						day1: $('#day1_'+aData[1]).val(),
						day2: $('#day2_'+aData[1]).val(),
						day3: $('#day3_'+aData[1]).val(),
						day4: $('#day4_'+aData[1]).val(),
						day5: $('#day5_'+aData[1]).val(),
						day6: $('#day6_'+aData[1]).val(),
						day7: $('#day7_'+aData[1]).val(),
						comments: msg
					};
					
					timesheetAjax.call(params, function(data) {
						$(element.parentNode.parentNode.parentNode)
							.find('input.hour_sheet:not([readonly])')
							.attr('readonly',true);
						$('#changeStatus_'+aData[1]).text(data.status);
						$('#changeButtons_'+aData[1]).html('<nobr>${btnSuggestReject}&nbsp;&nbsp;${btn_comments}</nobr>');
						$('#changeRowColor_'+aData[1]).removeClass('${pendingTM}');
						$("#changeRowColor_"+aData[1]+" .campo").each( function() {
							$(this).parents('td').removeClass("${pendingTM}"); 							
							var value = $(this).val();
							if (value > 0){
								$(this).parents('td').addClass("orangeRow");
							}
						});
						
					});
				}
		);
	}
	else { alertUI("${msgError}","${msgErrorNoHours}"); }
	$("#changeRowColor_"+aData[1]+" .total:contains(0.0)").parent("#changeRowColor_"+aData[1]).addClass('${pendingTM}');
}

function allowNonLabor() {
	$('input.exceptionDay, input.nonWorkingDay').each(function() {
		$(this).attr('readonly', false);
	});
	$('a#non_labor span').html('${msgRestoreCalendar}');
	$('a#non_labor').unbind();
	$('a#non_labor').click(function() { restoreCalendar();});
}

function restoreCalendar() {
	$('input.exceptionDay, input.nonWorkingDay').each(function() {
		$(this).attr('readonly', true);
	});

	$('a#non_labor span').html('${msgNonLaborDay}');
	$('a#non_labor').unbind();
	$('a#non_labor').click(function() { allowNonLabor();});
}

readyMethods.add(function() {
	
	$('#non_labor').click(function() { allowNonLabor(); });
	$('#clearHours').click(function() { 
		$('input.hour_sheet:not([readonly])').val('');
	});
	
	$('#dateTimeSheets').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		showOtherMonths: true,
		selectOtherMonths: true,
		onSelect: function(selectedDate) {
			var f = document.frm_timesheet;
			f.accion.value = '<%=TimeSheetServlet.CHANGE_WEEK%>';
			loadingPopup();
			f.submit();
		}
	});
	
	timeSheet.initialize();
});

//-->
</script>
<div class="right" style="padding-bottom:5px;" id="headerTimeButtons"></div>

<fieldset>

	<form name="frm_timesheet" id="frm_timesheet" action="<%=TimeSheetServlet.REFERENCE%>" method="post">
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="comments" value="" />
		<input type="hidden" name="idOperation" value="" />
		<input type="hidden" name="idTimeSheetOperation" value="" />
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
				<th width="5%"> ${msgFilter}:</th>
				<td width="30%">
					<select id="filter" class="campo" style="width:100%;">
						<option value="" selected>${msgSelectOpt}</option>
						<option value="${msgAppLevelApp0}">${msgAppLevelApp0Desc}</option>
						<option value="${msgAppLevelApp1}">${msgAppLevelApp1Desc}</option>
						<c:if test="<%=SettingUtil.getApprovalRol(request) > 0%>">
							<option value="${msgAppLevelApp2}">${msgAppLevelApp2Desc}</option>
						</c:if>
						<option value="${msgAppLevelApp3}">${msgAppLevelApp3Desc}</option>
					</select>
				</td>
				<td width="35%" style="text-align: right;">&nbsp;</td>
				<th width="30%" align="right">
					<img src="images/arrow_left.png" id="prev" style="margin-right:10px; cursor:pointer;"/>
					<img src="images/arrow_right.png" id="next" style="cursor:pointer;"/>
					&nbsp;${initWeek}&nbsp;-&nbsp;${endWeek}&nbsp;
					<span style="margin-right:5px;">
						<input type="hidden" name="date" id="dateTimeSheets" class="campo fecha alwaysEditable" value="${date}"/>
		    		</span>
				</th>
			</tr>
		</table>
		<%-- TIME SHEET TABLE --%>
		<jsp:include page="common/table_timesheet.inc.jsp" flush="false"/>
		
		<table width="100%" cellpadding="4" cellspacing="1">
			<tr>
				<td><div id="legendNonWorking" class="legendChart"></div></td>
				<td><div id="legendNonDefaultWorkWeek" class="legendChart"></div></td>
				<td><div id="legendExceptionDay" class="legendChart"></div></td>
			</tr>
			<tr>
				<% if (Settings.SHOW_TIMESHEET_COLOR) { %>
					<td><div id="legendNoApproval" class="legendChart"></div></td>
				<% } %>
				<td><div id="legendPendingApproval" class="legendChart"></div></td>
				<td><div id="legendApproved" class="legendChart"></div></td>
				<td><div id="legendSuggestReject" class="legendChart"></div></td>
			</tr>
		</table>
	</form>
</fieldset>
<div style="padding: 15px;">
	<c:if test="${not editDisabled }">
		<a class="button_img position_right" href="javascript:approveTimeTrackingAll();"><img src="images/approve.png"/><span>${msgApproveAll }</span></a>
 		<a class="button_img position_right" href="javascript:saveTimeTrackingAll();"><img src="images/save.png"/><span>${msgSaveAll }</span></a> 
		
		<a class="button_img position_right" href="#" id="clearHours"><img src="images/undo.png"/><span>${msgClearAllHours }</span></a>
		<a class="button_img position_right" href="#" id="non_labor"><img src="images/calendar.png"/><span>${msgAllowWorkingNonLabor }</span></a>
		<a class="button_img position_right" href="javascript:addNewOperation();"><img src="images/add_proj.png"/><span>${msgAddOperation }</span></a>
	</c:if>
</div>
<div id="timeSheetFooter"></div>
<jsp:include page="operation_popup.jsp" flush="true"/>
<jsp:include page="common/comments.inc.jsp" flush="true"/>

