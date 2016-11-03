<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.utils.StringPool"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="java.util.HashMap"%>
<%@ page import="es.sm2.openppm.core.reports.ReportUtil" %>
<%@ page import="es.sm2.openppm.core.reports.annotations.ReportType" %>

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
  ~ File: closure_report.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<script language="javascript" type="text/javascript" >
<!--

    function showCanceledFields(){

        if ($('#canceled').is(":checked") == true) {
            $('#canceledFields').show("blind");
        }
        else {

            $('#canceledFields').hide("blind");
            $('#dateCanceled').val('');
            $('#commentCanceled').val('');
        }
    }

    readyMethods.add(function () {

        $('#dateCanceled').datepicker({
            showOn: 'button',
            buttonImage: 'images/calendario.png',
            numberOfMonths: ${numberOfMonths},
            buttonImageOnly: true,
            onSelect: function(selectedDate) {
                var instance = $(this).data("datepicker");
                var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
            }
        });

        showCanceledFields();

        $('#canceled').change(function(){
            showCanceledFields();
        });

    });

//-->
</script>

<fmt:message key="closure.report" var="titleCloseReport"/>
<visual:panel id="fieldClousureReport" title="${titleCloseReport }">


    <div align="right" style="margin-right: 10px;" id="projectCloseCharter">
            <c:if test="<%=!SettingUtil.getString(settings, Settings.SETTING_PROJECT_CLOSE_CLASSNAME, Settings.DEFAULT_PROJECT_CLOSE_CLASSNAME).equals(StringPool.BLANK) ||
                ReportUtil.getInstance().isReportActive(ReportType.PROJECT_CLOSE)%>">
                <a href="javascript:generateProjectClose();" class="boton"><fmt:message key="generate_closure_report" /></a>
            </c:if>
    </div>

    <table width="100%">
        <tr>
            <th class="left"><fmt:message key="closure.project_results" /></th>
            <th class="left"><fmt:message key="closure.goal_achievement" /></th>
        </tr>
        <tr>
            <td class="center">
                <textarea name="projectResults" class="campo show_scroll" rows="8" style="width:90%;">${closure.projectResults}</textarea>
            </td>
            <td class="center">
                <textarea name="goalAchievement" id="goalAchievement" class="campo show_scroll" rows="8" style="width:90%;">${closure.goalAchievement}</textarea>
            </td>
        </tr>
    </table>
    <%-- Canceled project --%>
    <div style="margin-top: 20px;">
        <input name="canceled" style="width:20px;" id="canceled" value="true" type="checkbox" ${projectData.canceled ? "checked" : ""}/>
        <fmt:message key="closure.canceled" />
    </div>
    <div id="canceledFields" style="margin-top: 10px; margin-bottom: 20px;">
        <table width="100%">
            <tr>
                <td>
                    <fmt:message key="dates.canceled"/>:&nbsp;
                    <input type="text" name="dateCanceled" id="dateCanceled" class="campo fecha" value="<fmt:formatDate value='${projectData.dateCanceled}' pattern='${datePattern}'/>"/>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <th class="left"><fmt:message key="closure.comments.canceled" /></th>
            </tr>
            <tr>
                <td class="center">
                    <textarea name="commentCanceled" id="commentCanceled" class="campo show_scroll" rows="8" style="width:95%;">${projectData.commentCanceled}</textarea>
                </td>
            </tr>
        </table>
    </div>
    <c:if test="${op:hasPermission(user,project.status,tab)}">
        <div align="right" style="margin-top:10px; margin-right: 15px;">
            <a href="javascript:saveClosure();" class="boton"><fmt:message key="save" /></a>
        </div>
    </c:if>

    <c:if test="<%=Settings.SHOW_TABLE_WORKINGCOST_CLOSURE%>">
        <script type="text/javascript">
            <!--
            readyMethods.add(function() {
                $('#tb_workingcosts').dataTable({
                    "oLanguage": datatable_language,
                    "bPaginate": false,
                    "bLengthChange": false,
                    "bFilter": false,
                    "aoColumns": [
                        { "bVisible": false },
                        { "bVisible": true },
                        { "bVisible": true },
                        { "sClass": "right"},
                        { "sClass": "right"}
                    ]
                });
            });
            //-->
        </script>

        <div>
            <div class="hColor" style="margin-top: 10px;"><fmt:message key="working_hours" /></div>
            <table id="tb_workingcosts" class="tabledata" cellspacing="1" width="100%">
                <thead>
                <tr>
                    <th>&nbsp;</th>
                    <th width="34%"><fmt:message key="workingcosts.resource" /></th>
                    <th width="31%"><fmt:message key="workingcosts.department" /></th>
                    <th width="10%"><fmt:message key="workingcosts.planned_hours" /></th>
                    <th width="10%"><fmt:message key="workingcosts.actual_hours" /></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="workingcosts" items="${project.workingcostses}">
                    <tr>
                        <td>${workingcosts.idWorkingCosts}</td>
                        <td>${tl:escape(workingcosts.resourceName)}</td>
                        <td>${tl:escape(workingcosts.resourceDepartment)}</td>
                        <td>${workingcosts.effort}</td>
                        <td>${workingcosts.realEffort}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>


</visual:panel>