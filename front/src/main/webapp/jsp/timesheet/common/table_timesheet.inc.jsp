<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.front.servlets.TimeSheetServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page import="es.sm2.openppm.core.logic.setting.GeneralSetting" %>
<%@ page import="java.util.HashMap" %>
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
  ~ File: table_timesheet.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="comments" var="msgComments"/>
<fmt:message key="yes" var="msgYes"/>
<fmt:message key="no" var="msgNo"/>

<fmt:message key="reject.resource.suggest_reject" var="msgSuggestReject"/>

<fmt:message key="timesheet.activity" var="msgTSActivity"/>

<c:set var="pendingTMRow" value='<%=Settings.SHOW_TIMESHEET_COLOR?"pendingTM":"" %>'/>
<c:set var="pendingTMColumn" value='<%=Settings.SHOW_TIMESHEET_COLOR?"pendingTM":"" %>'/>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<script type="text/javascript">
<!--
var timeSheetTable;

var tableTimeSheet = {
		
	suggestReject: function(element) {
			
		var aData = timeSheetTable.fnGetData(element.parentNode.parentNode.parentNode);
			
		confirmTextUI("${msgSuggestReject}", "${msgComments}",
					"${msgYes}","${msgNo}",
			function(msg) {
				var a = "<%=TimeSheetServlet.JX_SUGGEST_REJECT%>";		
				var params = {
					accion: 	   a,
					idTimeSheet:   aData[1],
					comments:      msg
				};
			
				timesheetAjax.call(params, function(data) {
				
					$('#changeButtons_'+aData[1]).html('<nobr>&nbsp;${btn_comments}</nobr>');
					$('#changeRowColor_'+aData[1]).prop("title",msg);
					$('#changeRowColor_'+aData[1]).prop("class","colorYellow");	
					$('#changeRowColor_'+aData[1]).children('td').removeClass('${pendingTMColumn}');
					$('#changeRowColor_'+aData[1]).children('td').removeClass('orangeRow');
					$('#changeRowColor_'+aData[1]).children('td').removeClass('colorGreen');
 					$('#tb_timesheet .colorYellow').bt({
 						fill: '#FAF143', 
 						cssStyles: {color: '#343C4E', width: 'auto'},
 						width: 250,
 						padding: 10,
 						cornerRadius: 5,
 						spikeLength: 15,
 						spikeGirth: 5,
 						shadow: true,
 						positions: 'top'
 					});
				});
			}
		);
	},
	markApprovalSuggestReject:function() {
		
		$(".employeeSR_true").css("background-color", "#FFA101");
		$(".employeeSR_true").attr("title", "${msgSuggestReject}");
	},
	initialize: function() {
		
		$('.btitle').bt({
			fill: '#F9FBFF', 
			cssStyles: {color: '#343C4E', width: 'auto'},
			width: 250,
			padding: 10,
			cornerRadius: 5,
			spikeLength: 15,
			spikeGirth: 5,
			shadow: true,
			positions: 'top'
		});
		$('#tb_timesheet .colorYellow').bt({
			fill: '#FAF143', 
			cssStyles: {color: '#343C4E', width: 'auto'},
			width: 250,
			padding: 10,
			cornerRadius: 5,
			spikeLength: 15,
			spikeGirth: 5,
			shadow: true,
			positions: 'top'
		});
		$(".btnSuggestReject").live('click', function (){
			tableTimeSheet.suggestReject(this);
		});
	}
};

function calcHours() {
	
	var total = 0;
	var totalDays = [0,0,0,0,0,0,0];
	
	$(timeSheetTable.fnGetNodes()).each(function() {

		var hours = 0;
		
		$(this).find('input.campo').each(function(i) {
			
			hours += Number($(this).val());
			totalDays[i] += Number($(this).val());
		});
		
		$(this).find('.total').text(toCurrency(hours.toFixed(1), 1));
		
		total += hours;
	});
	
	$(totalDays).each(function(i) { $('#total_'+(i+1)).text(toCurrency(this.toFixed(1), 1)); });
	$('#total_week').text(toCurrency(total.toFixed(1), 1));
}

function sumColumn(column) {
	
	var hours = 0;
	$(column).each(function() { hours += Number($(this).val());	});
	 return hours;
}

function changeWeek(accion) {
	
	var f = document.frm_timesheet;
	f.accion.value = accion;
	loadingPopup();
	f.submit();
}

readyMethods.add(function() {
	
	timeSheetTable = $('#tb_timesheet').dataTable({
		"oLanguage": datatable_language,
		"bInfo": false,
		"bPaginate": false,
		"iDisplayLength": 100,
		"aoColumns": [
              { "bVisible": false },
              { "bVisible": false },
              { "sClass": "left", "sWidth": "30%" },
              { "sClass": "left", "sWidth": "15%" },
              { "sClass": "center", "sWidth": "5%", "bSortable": false},
              { "sClass": "center", "sWidth": "5%", "bSortable": false},
              { "sClass": "center", "sWidth": "5%", "bSortable": false},
              { "sClass": "center", "sWidth": "5%", "bSortable": false},
              { "sClass": "center", "sWidth": "5%", "bSortable": false},
              { "sClass": "center", "sWidth": "5%", "bSortable": false}, 
              { "sClass": "center", "sWidth": "5%", "bSortable": false},
              { "sClass": "center", "sWidth": "5%"},
              { "bVisible": false },
              { "sClass": "center", "sWidth": "10%", "bSortable": false },
              { "bVisible": false }
     	],
     	"fnDrawCallback": function ( oSettings ) {
            if ( oSettings.aiDisplay.length == 0 ) { return; }
             
            var nTrs = $('#tb_timesheet tbody tr');
            var iColspan = nTrs[0].getElementsByTagName('td').length;
            var sLastGroup = "";
            for ( var i=0 ; i<nTrs.length ; i++ ) {
                var iDisplayIndex = oSettings._iDisplayStart + i;
                var sGroup = oSettings.aoData[ oSettings.aiDisplay[iDisplayIndex] ]._aData[0];
                if ( sGroup != sLastGroup ) {
                    var nGroup = document.createElement( 'tr' );
                    var nCell = document.createElement( 'td' );
                    nCell.colSpan = iColspan;
                    nCell.className = "groupRow";
                    nCell.innerHTML = sGroup;
                    nGroup.appendChild( nCell );
                    nTrs[i].parentNode.insertBefore( nGroup, nTrs[i] );
                    sLastGroup = sGroup;
                }
            }
        },
        "aoColumnDefs": [
			{ "bVisible": false, "aTargets": [ 0 ] }
		],
		"aaSortingFixed": [[ 0, 'asc' ]],
		"aaSorting": [[ 2, 'asc' ]],
        "sDom": 'lfr<"giveHeight"t>ip'
	});
	
	$('#tb_timesheet tbody tr').live('click', function (event) {
		timeSheetTable.fnSetSelectable(this,'selected_internal');
	});
	
	$("#filter").change( function() {
		timeSheetTable.fnFilter($("#filter").val(),11);
	});
	
	timeSheetTable.fnResetAllFilters();
	
	calcHours();

	tableTimeSheet.initialize();

    $(".campo").on("keyup", function(){
        calcHours();
    });
});
//-->
</script>
<c:set var="rolePM" value="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PM) %>"/>
<table id="tb_timesheet" class="tabledata" cellspacing="1" width="100%">
	<thead>
		<tr>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th><fmt:message key="project_manager"/></th>
			<th>${weekDays[0] }&nbsp;<fmt:message key="week.monday_short"/></th>
			<th>${weekDays[1] }&nbsp;<fmt:message key="week.tuesday_short"/></th>
			<th>${weekDays[2] }&nbsp;<fmt:message key="week.wednesday_short"/></th>
			<th>${weekDays[3] }&nbsp;<fmt:message key="week.thursday_short"/></th>
			<th>${weekDays[4] }&nbsp;<fmt:message key="week.friday_short"/></th>
			<th>${weekDays[5] }&nbsp;<fmt:message key="week.saturday_short"/></th>
			<th>${weekDays[6] }&nbsp;<fmt:message key="week.sunday_short"/></th>
			<th><fmt:message key="total"/></th>
			<th><fmt:message key="status"/></th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_RESOURCE) %>">
				<c:set var="userTime" value="${user}"/>
			</c:when>
			<c:otherwise>
				<c:set var="userTime" value="${employee}"/>
			</c:otherwise>
		</c:choose>
		<c:set var="approvalRol" value="<%=SettingUtil.getApprovalRol(request) %>"/>
		<c:set var="userRol" value="<%=SecurityUtil.consUserRole(request) %>"/>	
		<c:set var="app0"  value="<%=Constants.TIMESTATUS_APP0 %>"/>
		<c:set var="app1"  value="<%=Constants.TIMESTATUS_APP1 %>"/>
		<c:set var="app2"  value="<%=Constants.TIMESTATUS_APP2 %>"/>
		<c:set var="app3"  value="<%=Constants.TIMESTATUS_APP3 %>"/>
		<c:forEach var="timeSheet" items="${timeSheets}">
			<%-- paint in colors status and suggestReject --%>
		    <c:choose>
		    	<%-- paint in yellow suggestReject rows --%>
		   		<c:when test="${timeSheet.suggestReject }">		   		
					<c:set var="trColorHours" value="${(timeSheet.suggestReject)? 'colorYellow' : timeSheet.status}"/>
		   			<%-- Do not paint td into suggestReject rows --%>
		   			<c:set var="tdStatusColor" value="" />
		   		</c:when>		
		   			<%-- Paint status into tr and td --%>
		   		<c:otherwise>
		   			<%-- Paint in red tr in app0 --%>
					<c:set var="allApp0" value="${timeSheet.hoursDay1 + timeSheet.hoursDay2 + timeSheet.hoursDay3 + timeSheet.hoursDay4 + timeSheet.hoursDay5 }"></c:set>
					<c:set var="trColorHours" value="${(allApp0 > 0 )? timeSheet.status : pendingTMRow}"/>
					<%-- Paint in red td in app0 --%>
					<c:if test="${timeSheet.status eq app0 }">
						<c:set var="tdStatusColor" value="${pendingTMRow}" />
					</c:if>
					<%-- Paint in orange td in app1 --%>
					<c:if test="${timeSheet.status eq app1 }">
						<c:set var="tdStatusColor" value="orangeRow" />
					</c:if>
					<%-- Paint in green td in app2 for PM role or orange for others roles--%>
					<c:if test="${timeSheet.status eq app2 }">
						<c:set var="tdStatusColor" value="${(rolePM eq true) ? 'colorGreen' : 'orangeRow'}" />
					</c:if>
					<%-- Paint in green td in app3 --%>
					<c:if test="${timeSheet.status eq app3}">
						<c:set var="tdStatusColor" value="colorGreen" />
					</c:if>
		   		</c:otherwise>
		    </c:choose>
			<tr id="changeRowColor_${timeSheet.idTimeSheet}" class="${trColorHours }" title="${(timeSheet.suggestReject)? timeSheet.suggestRejectComment : ''}">
				<td>
					<c:choose>
						<c:when test="${timeSheet.projectactivity ne null}"><b>${msgTSActivity}</b></c:when>
						<c:otherwise><b><fmt:message key="timesheet.operation"/></b></c:otherwise>
					</c:choose>
				</td>
				<td>${timeSheet.idTimeSheet}</td>
				<td>
					<c:choose>
						<c:when test="${timeSheet.projectactivity ne null }">
							<c:set var="activityName">
								<c:if test="<%=!SecurityUtil.isUserInRole(request, Constants.ROLE_PM) %>">
									${tl:escape(timeSheet.projectactivity.project.chartLabel) }&nbsp;-&nbsp;
									${tl:escape(timeSheet.projectactivity.project.projectName) }&nbsp;-&nbsp;
								</c:if>
								${tl:escape(timeSheet.projectactivity.activityName) }
							</c:set>
							<c:choose>
								<c:when test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_RESOURCE) %>">${activityName}</c:when>
								<c:otherwise>
									<a href="javascript:navigate.toProject(${timeSheet.projectactivity.project.idProject}, '${timeSheet.projectactivity.project.status}')">
										${activityName}
									</a>
								</c:otherwise>
							</c:choose>

							<c:if test="${not empty timeSheet.projectactivity.wbsdictionary }">
								<span class="btitle" title="${tl:escape(timeSheet.projectactivity.wbsdictionary)}">...</span>
							</c:if>
						</c:when>
						<c:otherwise>${tl:escape(timeSheet.operation.operationName) }</c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:if test="${timeSheet.projectactivity ne null }">
						${tl:escape(timeSheet.projectactivity.project.employeeByProjectManager.contact.fullName) }
					</c:if>
				</td>
				<td class = "${(timeSheet.hoursDay1 > 0)? tdStatusColor:''}">
					<input type="hidden" name="idTimeSheet" value="${timeSheet.idTimeSheet}"/>
					<c:choose>
						<c:when test="${timeSheet.projectactivity ne null and op:isExceptionDay(timeSheet.initDate, 0, timeSheet.projectactivity.idActivity, user.idEmployee) }">
							<c:set var="classException">exceptionDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:when test="${timeSheet.projectactivity ne null and !op:isWorkDay(timeSheet.initDate, 0, timeSheet.projectactivity.idActivity, userTime) }">
							<c:set var="classException">notWorkDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="classException"></c:set>
							<c:set var="readonly"></c:set>
						</c:otherwise>
					</c:choose>
					<input type="text" maxlength="4" name="day1_${timeSheet.idTimeSheet}" id="day1_${timeSheet.idTimeSheet}" 
					class="ui-corner-all hour_sheet day1 campo ${classException}" value='${classException eq "notWorkDay"?"": timeSheet.hoursDay1}' ${timeSheet.status eq app0?readonly:"readonly"}/>
				</td>
				<td class = "${(timeSheet.hoursDay2 > 0)? tdStatusColor:''}">
					<c:choose>
						<c:when test="${timeSheet.projectactivity ne null and op:isExceptionDay(timeSheet.initDate, 1, timeSheet.projectactivity.idActivity, user.idEmployee) }">
							<c:set var="classException">exceptionDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:when test="${timeSheet.projectactivity ne null and !op:isWorkDay(timeSheet.initDate, 1, timeSheet.projectactivity.idActivity, userTime) }">
							<c:set var="classException">notWorkDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="classException"></c:set>
							<c:set var="readonly"></c:set>
						</c:otherwise>
					</c:choose>
					<input type="text" maxlength="4" name="day2_${timeSheet.idTimeSheet}" id="day2_${timeSheet.idTimeSheet}" class="ui-corner-all hour_sheet day2 campo ${classException }" value='${classException eq "notWorkDay"?"":timeSheet.hoursDay2}' ${timeSheet.status eq app0?readonly:"readonly"}/>
				</td>
				<td class = "${(timeSheet.hoursDay3 > 0)? tdStatusColor : ''}">
					<c:choose>
						<c:when test="${timeSheet.projectactivity ne null and op:isExceptionDay(timeSheet.initDate, 2, timeSheet.projectactivity.idActivity, user.idEmployee) }">
							<c:set var="classException">exceptionDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:when test="${timeSheet.projectactivity ne null and !op:isWorkDay(timeSheet.initDate, 2, timeSheet.projectactivity.idActivity, userTime) }">
							<c:set var="classException">notWorkDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="classException"></c:set>
							<c:set var="readonly"></c:set>
						</c:otherwise>
					</c:choose>
					<input type="text" maxlength="4" name="day3_${timeSheet.idTimeSheet}" id="day3_${timeSheet.idTimeSheet}" class="ui-corner-all hour_sheet day3 campo ${classException }" value='${classException eq "notWorkDay"?"":timeSheet.hoursDay3}' ${timeSheet.status eq app0?readonly:"readonly"}/>
				</td>
				<td class = "${(timeSheet.hoursDay4 > 0)? tdStatusColor : ''}">
					<c:choose>
						<c:when test="${timeSheet.projectactivity ne null and op:isExceptionDay(timeSheet.initDate, 3, timeSheet.projectactivity.idActivity, user.idEmployee) }">
							<c:set var="classException">exceptionDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:when test="${timeSheet.projectactivity ne null and !op:isWorkDay(timeSheet.initDate, 3, timeSheet.projectactivity.idActivity, userTime) }">
							<c:set var="classException">notWorkDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="classException"></c:set>
							<c:set var="readonly"></c:set>
						</c:otherwise>
					</c:choose>
					<input type="text" maxlength="4" name="day4_${timeSheet.idTimeSheet}" id="day4_${timeSheet.idTimeSheet}" class="ui-corner-all hour_sheet day4 campo ${classException }" value='${classException eq "notWorkDay"?"":timeSheet.hoursDay4}' ${timeSheet.status eq app0?readonly:"readonly"}/>
				</td>
				<td class = "${(timeSheet.hoursDay5 > 0)? tdStatusColor : ''}">
					<c:choose>
						<c:when test="${timeSheet.projectactivity ne null and op:isExceptionDay(timeSheet.initDate, 4, timeSheet.projectactivity.idActivity, user.idEmployee) }">
							<c:set var="classException">exceptionDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:when test="${timeSheet.projectactivity ne null and !op:isWorkDay(timeSheet.initDate, 4, timeSheet.projectactivity.idActivity, userTime) }">
							<c:set var="classException">notWorkDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="classException"></c:set>
							<c:set var="readonly"></c:set>
						</c:otherwise>
					</c:choose>
					<input type="text" maxlength="4" name="day5_${timeSheet.idTimeSheet}" id="day5_${timeSheet.idTimeSheet}" class="ui-corner-all hour_sheet day5 campo ${classException }" value='${classException eq "notWorkDay"?"":timeSheet.hoursDay5}' ${timeSheet.status eq app0?readonly:"readonly"}/>
				</td>
				<td class = "${(timeSheet.hoursDay6 > 0)? tdStatusColor : ''}">
					<c:set var="readonly">readonly</c:set>
					<c:choose>
						<c:when test="${timeSheet.projectactivity ne null and op:isExceptionDay(timeSheet.initDate, 5, timeSheet.projectactivity.idActivity, user.idEmployee) }">
							<c:choose>
								<c:when test="${timeSheet.status eq app0 }">
									<c:set var="classException">exceptionDay</c:set>
								</c:when>
								<c:otherwise>
									<c:set var="classException">exceptionDayColor</c:set>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${timeSheet.projectactivity ne null and !op:isWorkDay(timeSheet.initDate, 5, timeSheet.projectactivity.idActivity, userTime) }">
							<c:set var="classException">notWorkDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:when test="${timeSheet.status eq app0 }">
							<c:set var="classException">nonWorkingDay</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="classException">nonWorkingDayColor</c:set>
						</c:otherwise>
					</c:choose>
					<input type="text" maxlength="4" name="day6_${timeSheet.idTimeSheet}" id="day6_${timeSheet.idTimeSheet}" class="ui-corner-all hour_sheet day6 campo ${classException }" value='${classException eq "notWorkDay"?"":timeSheet.hoursDay6}' ${timeSheet.status eq app0?readonly:"readonly"}/>
				</td>
				<td class = "${(timeSheet.hoursDay6 > 0)? tdStatusColor : ''}">
					<c:set var="readonly">readonly</c:set>
					<c:choose>
						<c:when test="${timeSheet.projectactivity ne null and op:isExceptionDay(timeSheet.initDate, 6, timeSheet.projectactivity.idActivity, user.idEmployee) }">
							<c:choose>
								<c:when test="${timeSheet.status eq app0 }">
									<c:set var="classException">exceptionDay</c:set>
								</c:when>
								<c:otherwise>
									<c:set var="classException">exceptionDayColor</c:set>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:when test="${timeSheet.projectactivity ne null and !op:isWorkDay(timeSheet.initDate, 6, timeSheet.projectactivity.idActivity, userTime) }">
							<c:set var="classException">notWorkDay</c:set>
							<c:set var="readonly">readonly</c:set>
						</c:when>
						<c:when test="${timeSheet.status eq app0 }">
							<c:set var="classException">nonWorkingDay</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="classException">nonWorkingDayColor</c:set>
						</c:otherwise>
					</c:choose>
					<input type="text" maxlength="4" name="day7_${timeSheet.idTimeSheet}" id="day7_${timeSheet.idTimeSheet}" class="ui-corner-all hour_sheet day7 campo ${classException }" value='${classException eq "notWorkDay"?"":timeSheet.hoursDay7}' ${timeSheet.status eq app0?readonly:"readonly"}/>
				</td>
				<td class="total">0</td>
				<td class="status" id="changeStatus_${timeSheet.idTimeSheet}">
					<fmt:message key="applevel.${timeSheet.status }"/>
				</td>
				<td id="changeButtons_${timeSheet.idTimeSheet}">
					<nobr>
						<c:if test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_RESOURCE) %>">
							<c:if test="${timeSheet.status eq app0 }">
								${btn_app0}
								<c:if test="${timeSheet.projectactivity eq null }">
									<img onclick="javascript:deleteOperation(this);" title="<fmt:message key="delete"/>" class="ico link" src="images/delete.png"/>
								</c:if>
							</c:if>
						</c:if>
						<c:if test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PM) %>">
							<c:choose>
								<c:when test="${timeSheet.status eq app1 }">${btn_reject }${btn_app1 }</c:when>
								<c:when test="<%=SettingUtil.getApprovalRol(request) == 0 %>">${btn_reject }</c:when>
							</c:choose>
						</c:if>
						<c:if test="${approvalRol eq userRol}">
							<c:if test="${timeSheet.status ne app0 }">${btn_reject}</c:if>						
							<c:if test="${timeSheet.status eq app2 }">${btn_app2 }</c:if>
						</c:if>
                        <c:if test="<%= SecurityUtil.isUserInRole(request, Integer.valueOf(SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET, Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET))) && SettingUtil.getBoolean(request, GeneralSetting.LAST_LEVEL_APPROVED_APP1)%>">
                            <c:choose>
                                <c:when test="${timeSheet.status eq app1}">${btn_supplant}</c:when>
                            </c:choose>
                        </c:if>
						<c:if test="${timeSheet.status ne app0 }">
							<c:if test="${timeSheet.suggestReject ne true}">	
								${btnSuggestReject}
							</c:if>
						</c:if>
						&nbsp;${btn_comments}
					</nobr>
				</td>
				<td><fmt:message key="applevel.${timeSheet.status }"/></td>
			</tr>
		</c:forEach>
	</tbody>
	<tfoot>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td id="total_1" class="day_total">0</td>
			<td id="total_2" class="day_total">0</td>
			<td id="total_3" class="day_total">0</td>
			<td id="total_4" class="day_total">0</td>
			<td id="total_5" class="day_total">0</td>
			<td id="total_6" class="day_total">0</td>
			<td id="total_7" class="day_total">0</td>
			<td id="total_week" class="total_week">0</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
	</tfoot>
</table>
