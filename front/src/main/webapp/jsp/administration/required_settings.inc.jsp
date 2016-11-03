<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.logic.setting.RequiredFieldSetting"%>
<%@page import="es.sm2.openppm.front.servlets.AdministrationServlet"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>
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
  ~ File: required_settings.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:26
  --%>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<script>
readyMethods.add(function() {

	$('#saveRequired').on('click', function() {
		loadingPopup();
		document.forms.frm_required_settings.submit();
	});
	
});
</script>
<form name="frm_required_settings" id="frm_required_settings" action="<%=AdministrationServlet.REFERENCE%>" method="post">
	<input type="hidden" name="accion" value="<%=AdministrationServlet.SAVE_REQUIRED_SETTINGS%>" />
	
	<table width="100%" class="settingTable">
		<tr>
			<td width="3%"><img class="btitle" title="<fmt:message key="setting.investment.required.category_info"/>" src="images/info.png"></td>					
			<th width="23%">
				<fmt:message key="<%=Settings.SETTING_INVESTMENT_REQUIRED_CATEGORY%>"/>
			</th>
			<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_REQUIRED_CATEGORY%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_CATEGORY, Settings.DEFAULT_INVESTMENT_REQUIRED_CATEGORY).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
			</td>
			<td width="2%">&nbsp;</td>		

			<td width="3%"><img class="btitle" title="<fmt:message key="setting.investment.required.functional_manager_info"/>" src="images/info.png"></td>					
			<th width="23%">
				<fmt:message key="<%=Settings.SETTING_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER%>"/>
			</th>
			<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER, Settings.DEFAULT_INVESTMENT_REQUIRED_FUNCTIONAL_MANAGER).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
			</td>
			<td width="2%">&nbsp;</td>		
		
			<td width="3%"><img class="btitle" title="<fmt:message key="setting.investment.required.sponsor_info"/>" src="images/info.png"></td>					
			<th width="23%">
				<fmt:message key="<%=Settings.SETTING_INVESTMENT_REQUIRED_SPONSOR%>"/>
			</th>
			<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_REQUIRED_SPONSOR%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_SPONSOR, Settings.DEFAULT_INVESTMENT_REQUIRED_SPONSOR).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
			</td>
			<td width="2%">&nbsp;</td>		
		</tr>
		<tr>
			<td width="3%"><img class="btitle" title="<fmt:message key="setting.investment.required.budget_year_info"/>" src="images/info.png"></td>					
			<th width="23%">
				<fmt:message key="<%=Settings.SETTING_INVESTMENT_REQUIRED_BUDGET_YEAR%>"/>
			</th>
			<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_REQUIRED_BUDGET_YEAR%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_BUDGET_YEAR, Settings.DEFAULT_INVESTMENT_REQUIRED_BUDGET_YEAR).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
			</td>
			<td width="2%">&nbsp;</td>		

			<td width="3%"><img class="btitle" title="<fmt:message key="setting.investment.required.priority_info"/>" src="images/info.png"></td>					
			<th width="23%">
				<fmt:message key="<%=Settings.SETTING_INVESTMENT_REQUIRED_PRIORITY%>"/>
			</th>
			<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_REQUIRED_PRIORITY%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_PRIORITY, Settings.DEFAULT_INVESTMENT_REQUIRED_PRIORITY).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
			</td>
			<td width="2%">&nbsp;</td>		
			
			<td width="3%"><img class="btitle" title="<fmt:message key="setting.investment.required.stage_gate_info"/>" src="images/info.png"></td>					
			<th width="23%">
				<fmt:message key="<%=Settings.SETTING_INVESTMENT_REQUIRED_STAGE_GATE%>"/>
			</th>
			<td width="5%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_INVESTMENT_REQUIRED_STAGE_GATE%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_INVESTMENT_REQUIRED_STAGE_GATE, Settings.DEFAULT_INVESTMENT_REQUIRED_STAGE_GATE).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
			</td>
			<td width="2%">&nbsp;</td>		
		</tr>
        <%-- Required settings settings --%>
        <visual:settings startColumn="1" numberOfColumns="3" fields="<%= RequiredFieldSetting.values() %>" />
	</table>
	
	<div align="right" style="margin:10px;">
		<input type="button" class="boton" id="saveRequired" value="<fmt:message key="save" />"/>
	</div>
</form>