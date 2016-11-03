<%@ page import="es.sm2.openppm.front.utils.SettingUtil" %>
<%@ page import="es.sm2.openppm.core.logic.setting.VisibilityProjectSetting" %>
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
  ~ File: info_project.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>


<c:if test="${not op:hasPermissionSetting(settings, user,project.status,tab)}">
    <script type="text/javascript">
        readyMethods.add(function() {
            $('.campo').attr('disabled',true);
            $('.campo').css({'background-color': '#FFFFFF'});
            $(':checkbox').attr('disabled',true);
            $(':input.alwaysEditable').attr('disabled',false);
            $(':input[type=hidden]').attr('disabled',false);
        });
    </script>
</c:if>

<div id="infoProject" style="margin-top:15px;">
    <table width="100%" >
        <tr>
            <c:if test="<%= SettingUtil.getBoolean(SettingUtil.getSettings(request), VisibilityProjectSetting.PROJECT_COLUMN_IDPROJECT) %>">
                <th class="center" width="3%" style="vertical-align: bottom;"><fmt:message key="id" />&nbsp;<fmt:message key="project" /></th>
            </c:if>
            <th class="center" width="5%" style="vertical-align: bottom;"><fmt:message key="status" /></th>
            <th class="left" width="10%" style="vertical-align: bottom;"><fmt:message key="program" /></th>
            <th class="left" width="12%" style="vertical-align: bottom;"><fmt:message key="program.manager" /></th>
            <th class="left" width="24%" style="vertical-align: bottom;"><fmt:message key="project_manager" /></th>
            <th class="center" width="7%" style="vertical-align: bottom;"><fmt:message key="project.accounting_code" /></th>
            <th class="center" width="7%" style="vertical-align: bottom;"><fmt:message key="project.date.initiation" /></th>
            <th class="center" width="7%" style="vertical-align: bottom;"><fmt:message key="project.date.planning" /></th>
            <th class="center" width="7%" style="vertical-align: bottom;"><fmt:message key="project.date.execution" /></th>
            <th class="center" width="7%" style="vertical-align: bottom;"><fmt:message key="project.date.closing" /></th>
            <th class="center" width="7%" style="vertical-align: bottom;"><fmt:message key="project.date.archive" /></th>
        </tr>
        <tr>
            <c:if test="<%= SettingUtil.getBoolean(SettingUtil.getSettings(request), VisibilityProjectSetting.PROJECT_COLUMN_IDPROJECT) %>">
                <td class="center" style="vertical-align: top;">${project.idProject}</td>
            </c:if>
            <td class="center" style="vertical-align: top;">
                <c:choose>
                    <c:when test="${op:isCharEquals(type, typeInvestment)}">
                       <fmt:message key="investments.status.${project.investmentStatus}" />
                    </c:when>
                    <c:otherwise>
                       <fmt:message key="project_status.${project.status}" />
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="left" style="vertical-align: top;">${project.program.programName }</td>
            <td class="left" style="vertical-align: top;">${project.program.employee.contact.fullName }</td>
            <td class="left" style="vertical-align: top;">${project.employeeByProjectManager.contact.fullName }</td>
            <td class="center" style="vertical-align: top;">${project.accountingCode }</td>
            <td class="center" style="vertical-align: top;"><fmt:formatDate value="${project.initDate}" pattern="${datePattern}" /></td>
            <td class="center" style="vertical-align: top;"><fmt:formatDate value="${project.planDate}" pattern="${datePattern}" /></td>
            <td class="center" style="vertical-align: top;"><fmt:formatDate value="${project.execDate}" pattern="${datePattern}" /></td>
            <td class="center" style="vertical-align: top;"><fmt:formatDate value="${project.endDate}" pattern="${datePattern}" /></td>
            <td class="center" style="vertical-align: top;"><fmt:formatDate value="${project.archiveDate}" pattern="${datePattern}" /></td>
        </tr>
    </table>
</div>