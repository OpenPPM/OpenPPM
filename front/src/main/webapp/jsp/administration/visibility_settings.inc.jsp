<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.servlets.AdministrationServlet"%>
<%@ page import="es.sm2.openppm.core.logic.setting.NotificationType" %>
<%@ page import="es.sm2.openppm.core.logic.setting.VisibilityProjectSetting" %>
<%@ page import="es.sm2.openppm.core.logic.setting.VisibilityInvestmentSetting" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
  ~ File: visibility_settings.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:27
  --%>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtValueRequired">
	<fmt:param><b><fmt:message key="setting.value"/></b></fmt:param>
</fmt:message>

<script>
readyMethods.add(function() {

	$('#saveVisibility').on('click', function() {
		loadingPopup();
		document.forms.frm_visibility_settings.submit();
	});
});
</script>
<form name="frm_visibility_settings" id="frm_visibility_settings" action="<%=AdministrationServlet.REFERENCE%>" method="post">
	<input type="hidden" name="accion" value="<%=AdministrationServlet.SAVE_VISIBILITY_SETTINGS%>" />
	
	<fmt:message key="settings.visibility_columns.investment" var="titleColInv"/>
	<visual:panel title="${titleColInv }">
		<table width="100%" class="settingTable">
			<tr>
				<td width="3%"><img class="btitle" title="<fmt:message key="setting.investment.column.status_info"/>" src="images/info.png"></td>
				<th width="23%">
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_STATUS%>"/>
				</th>
				<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_STATUS%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_STATUS, Settings.DEFAULT_INVESTMENT_COLUMN_STATUS).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td width="2%">&nbsp;</td>

				<td width="3%"><img class="btitle" title="<fmt:message key="setting.investment.column.accounting_code_info"/>" src="images/info.png"></td>
				<th width="23%">
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_ACCOUNTING_CODE%>"/>
				</th>
				<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_ACCOUNTING_CODE%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_ACCOUNTING_CODE, Settings.DEFAULT_INVESTMENT_COLUMN_ACCOUNTING_CODE).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td width="2%">&nbsp;</td>

				<td width="3%"><img class="btitle" title="<fmt:message key="setting.investment.column.name_info"/>" src="images/info.png"></td>
				<th width="23%">
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_NAME%>"/>
				</th>
				<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_NAME%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_NAME, Settings.DEFAULT_INVESTMENT_COLUMN_NAME).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>

			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.investment.column.short_name_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_SHORT_NAME%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_SHORT_NAME%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_SHORT_NAME, Settings.DEFAULT_INVESTMENT_COLUMN_SHORT_NAME).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.investment.column.budget_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_BUDGET%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_BUDGET%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_BUDGET, Settings.DEFAULT_INVESTMENT_COLUMN_BUDGET).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.investment.column.priority_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_PRIORITY%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_PRIORITY%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_PRIORITY, Settings.DEFAULT_INVESTMENT_COLUMN_PRIORITY).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>

			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.project.probability_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_POC%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_POC%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_POC, Settings.DEFAULT_INVESTMENT_COLUMN_POC).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.investment.column.baseline_start_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_BASELINE_START%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_BASELINE_START%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_BASELINE_START, Settings.DEFAULT_INVESTMENT_COLUMN_BASELINE_START).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.investment.column.baseline_finish_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_BASELINE_FINISH%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_BASELINE_FINISH%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_BASELINE_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_BASELINE_FINISH).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>

			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.investment.column.start_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_START%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_START%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_START, Settings.DEFAULT_INVESTMENT_COLUMN_START).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.investment.column.finish_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_FINISH%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_FINISH%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_FINISH).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.investment.column.internal_effort_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_INTERNAL_EFFORT%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_INTERNAL_EFFORT%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_INTERNAL_EFFORT, Settings.DEFAULT_INVESTMENT_COLUMN_INTERNAL_EFFORT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>

			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.investment.column.external_cost_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_EXTERNAL_COST%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_EXTERNAL_COST%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_EXTERNAL_COST, Settings.DEFAULT_INVESTMENT_COLUMN_EXTERNAL_COST).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.column.bac_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="project.bac"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_BAC%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_BAC, Settings.DEFAULT_INVESTMENT_COLUMN_BAC).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.column.program_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="program"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_PROGRAM%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_PROGRAM, Settings.DEFAULT_INVESTMENT_COLUMN_PROGRAM).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>
			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.investment.column.planned_start_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_PLANNED_START%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_PLANNED_START%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_PLANNED_START, Settings.DEFAULT_INVESTMENT_COLUMN_PLANNED_START).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.investment.column.planned_finish_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_INVESTMENT_COLUMN_PLANNED_FINISH%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_COLUMN_PLANNED_FINISH%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_COLUMN_PLANNED_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_PLANNED_FINISH).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

                <%-- Visibility settings --%>
                <visual:settings startColumn="3" numberOfColumns="3" fields="<%= VisibilityInvestmentSetting.values() %>" />
			</tr>
		</table>
	</visual:panel>

	<fmt:message key="settings.visibility_columns.project" var="titleColProj"/>
	<visual:panel title="${titleColProj }">
		<table width="100%" class="settingTable">
			<tr>
				<td width="3%"><img class="btitle" title="<fmt:message key="setting.project.column.rag_info"/>" src="images/info.png"></td>
				<th width="23%">
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_RAG%>"/>
				</th>
				<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_RAG%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_RAG, Settings.DEFAULT_PROJECT_COLUMN_RAG).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td width="2%">&nbsp;</td>

				<td width="3%"><img class="btitle" title="<fmt:message key="setting.project.column.status_info"/>" src="images/info.png"></td>
				<th width="23%">
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_STATUS%>"/>
				</th>
				<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_STATUS%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_STATUS, Settings.DEFAULT_PROJECT_COLUMN_STATUS).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td width="2%">&nbsp;</td>

				<td width="3%"><img class="btitle" title="<fmt:message key="setting.project.column.accounting_code_info"/>" src="images/info.png"></td>
				<th width="23%">
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_ACCOUNTING_CODE %>"/>
				</th>
				<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_ACCOUNTING_CODE%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_ACCOUNTING_CODE, Settings.DEFAULT_PROJECT_COLUMN_ACCOUNTING_CODE).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>

			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.project.column.name_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_NAME%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_NAME%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_NAME, Settings.DEFAULT_PROJECT_COLUMN_NAME).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.project.column.short_name_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_SHORT_NAME%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_SHORT_NAME%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_SHORT_NAME, Settings.DEFAULT_PROJECT_COLUMN_SHORT_NAME).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.project.column.budget_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_BUDGET%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_BUDGET%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_BUDGET, Settings.DEFAULT_PROJECT_COLUMN_BUDGET).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>

			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.project.column.priority_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_PRIORITY%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_PRIORITY%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_PRIORITY, Settings.DEFAULT_PROJECT_COLUMN_PRIORITY).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.project.column.poc_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_POC%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_POC%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_POC, Settings.DEFAULT_PROJECT_COLUMN_POC).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.project.column.baseline_start_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_BASELINE_START%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_BASELINE_START%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_BASELINE_START, Settings.DEFAULT_PROJECT_COLUMN_BASELINE_START).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>

			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.project.column.baseline_finish_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_BASELINE_FINISH%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_BASELINE_FINISH%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_BASELINE_FINISH, Settings.DEFAULT_PROJECT_COLUMN_BASELINE_FINISH).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.project.column.start_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_START%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_START%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_START, Settings.DEFAULT_PROJECT_COLUMN_START).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.project.column.finish_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_FINISH%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_FINISH%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_FINISH, Settings.DEFAULT_PROJECT_COLUMN_FINISH).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>

			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.project.column.internal_effort_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_INTERNAL_EFFORT%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_INTERNAL_EFFORT%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_INTERNAL_EFFORT, Settings.DEFAULT_PROJECT_COLUMN_INTERNAL_EFFORT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.project.column.external_cost_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_EXTERNAL_COST%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_EXTERNAL_COST%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_EXTERNAL_COST, Settings.DEFAULT_PROJECT_COLUMN_EXTERNAL_COST).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.project.column.actual_cost_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_ACTUAL_COST%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_ACTUAL_COST%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_ACTUAL_COST, Settings.DEFAULT_PROJECT_COLUMN_ACTUAL_COST).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>
			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.project.column.kpi_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_KPI%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_KPI%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_KPI, Settings.DEFAULT_PROJECT_COLUMN_KPI).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.column.bac_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="project.bac"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_BAC%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_BAC, Settings.DEFAULT_PROJECT_COLUMN_BAC).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.column.pm_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="project_manager"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_PM%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_PM, Settings.DEFAULT_PROJECT_COLUMN_PM).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>
			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.project.column.planned_start_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_PLANNED_START%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_PLANNED_START%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_PLANNED_START, Settings.DEFAULT_PROJECT_COLUMN_PLANNED_START).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

				<td><img class="btitle" title="<fmt:message key="setting.project.column.planned_finish_info"/>" src="images/info.png"></td>
				<th>
					<fmt:message key="<%=Settings.SETTING_PROJECT_COLUMN_PLANNED_FINISH%>"/>
				</th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_COLUMN_PLANNED_FINISH%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_COLUMN_PLANNED_FINISH, Settings.DEFAULT_PROJECT_COLUMN_PLANNED_FINISH).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td>&nbsp;</td>

                <%-- Visibility settings --%>
                <visual:settings startColumn="3" numberOfColumns="3" fields="<%= VisibilityProjectSetting.values() %>" />
			</tr>
        </table>
	</visual:panel>
	
	<fmt:message key="others" var="titleColOthers"/>
	<visual:panel title="${titleColOthers }">
		<table width="100%" class="settingTable">
			<tr>
				<td width="3%"><img class="btitle" title="<fmt:message key="setting.workingcosts.quarters_info"/>" src="images/info.png"></td>			
				<th width="23%"><fmt:message key="<%=Settings.SETTING_WORKINGCOSTS_QUARTERS %>"/></th>
				<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_WORKINGCOSTS_QUARTERS%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_WORKINGCOSTS_QUARTERS, Settings.DEFAULT_WORKINGCOSTS_QUARTERS).equals(String.valueOf(Constants.SELECTED)) ? "checked" : "" %>/>
				</td>
				<td width="2%">&nbsp;</td>
				
				
				<td width="3%"><img class="btitle" title="<fmt:message key="setting.show_documents_info"/>" src="images/info.png"></td>					
				<th width="23%"><fmt:message key="<%=Settings.SETTING_SHOW_DOCUMENTS %>"/></th>
				<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_SHOW_DOCUMENTS %>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_SHOW_DOCUMENTS, Settings.DEFAULT_SETTING_SHOW_DOCUMENTS).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
				<td width="2%">&nbsp;</td>
				
				
				<td width="3%"><img class="btitle" title="<fmt:message key="setting.project.initiating.external_costs.show_info"/>" src="images/info.png"></td>
				<th width="23%"><fmt:message key="<%=Settings.SETTING_PROJECT_INIT_EXTERNAL_COST_SHOW %>"/></th>
				<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_PROJECT_INIT_EXTERNAL_COST_SHOW%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_PROJECT_INIT_EXTERNAL_COST_SHOW, Settings.DEFAULT_PROJECT_INIT_EXTERNAL_COST_SHOW).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>
		</table>
	</visual:panel>
	
	<div align="right" style="margin:10px;">
		<input type="button" class="boton" id="saveVisibility" value="<fmt:message key="save" />"/>
	</div>
</form>