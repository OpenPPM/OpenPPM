<%@page import="es.sm2.openppm.core.common.Settings.SettingType"%>
<%@page import="es.sm2.openppm.core.logic.security.actions.AdminAction"%>
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
  ~ File: customization.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:26
  --%>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<script>

var customizationSettings = {

    load : function() {
        $(".selectColor").spectrum({
            cancelText: '<fmt:message key="cancel"/>',
            chooseText: '<fmt:message key="CHOOSE"/>'
        });

        $('#saveMiscelanea').click(customizationSettings.save);
    },
    save : function() {
        adminAjax.call($('#frmCustomization').serializeArray());
    }
};

readyMethods.add(function() { customizationSettings.load(); });
</script>

<fmt:message key="setting.mail.customization" var="titleCustomization"/>
<visual:panel title="${titleCustomization}">
    <form id="frmCustomization" action="<%=AdministrationServlet.REFERENCE%>" method="post">
        <input type="hidden" name="accion" value="<%=AdminAction.SAVE_CUSTOMIZATION.getAction()%>" />

        <H3>
            <fmt:message key="SETTINGS.CUSTOMIZATION.LEVEL_TITLE"/>
        </H3>
        <table cellpadding="5px" class="settingTable">
            <tr>
                <td ><img class="btitle" title="<fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL1.getMessageInfo() %>"/> <fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL1_COLOR.getMessageInfo() %>"/>" src="images/info.png"></td>
                <th>
                    <fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL1.getMessage() %>"/>
                </th>
                <td>
                    <input type="text" class="campo" style="width: 25px"
                           name="<%=SettingType.CAPACITY_PLANNING_LEVEL1.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL1)%>"/>%
                </td>
                <th>
                    <fmt:message key="BACKGROUND_COLOR"/>
                </th>
                <td>
                    <input type="text" class="campo selectColor" style="width: 60px"
                           name="<%=SettingType.CAPACITY_PLANNING_LEVEL1_COLOR.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL1_COLOR)%>"/>
                </td>
                <th>
                    <fmt:message key="TEXT_COLOR"/>
                </th>
                <td>
                    <input type="text" class="campo selectColor" style="width: 60px"
                           name="<%=SettingType.CAPACITY_PLANNING_LEVEL1_COLOR_TEXT.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL1_COLOR_TEXT)%>"/>
                </td>
            </tr>
            <tr>
                <td ><img class="btitle" title="<fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL2.getMessageInfo() %>"/> <fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL2_COLOR.getMessageInfo() %>"/>" src="images/info.png"></td>
                <th>
                    <fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL2.getMessage() %>"/>
                </th>
                <td>
                    <input type="text" class="campo" style="width: 25px"
                           name="<%=SettingType.CAPACITY_PLANNING_LEVEL2.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL2)%>"/>%
                </td>
                <th>
                    <fmt:message key="BACKGROUND_COLOR"/>
                </th>
                <td>
                    <input type="text" class="campo selectColor" style="width: 60px"
                           name="<%=SettingType.CAPACITY_PLANNING_LEVEL2_COLOR.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL2_COLOR)%>"/>
                </td>
                <th>
                    <fmt:message key="TEXT_COLOR"/>
                </th>
                <td>
                    <input type="text" class="campo selectColor" style="width: 60px"
                           name="<%=SettingType.CAPACITY_PLANNING_LEVEL2_COLOR_TEXT.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL2_COLOR_TEXT)%>"/>
                </td>
            </tr>
            <tr>
                <td ><img class="btitle" title="<fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL3.getMessageInfo() %>"/> <fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL3_COLOR.getMessageInfo() %>"/>" src="images/info.png"></td>
                <th>
                    <fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL3.getMessage() %>"/>
                </th>
                <td>
                    <input type="text" class="campo" style="width: 25px"
                           name="<%=SettingType.CAPACITY_PLANNING_LEVEL3.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL3)%>"/>%
                </td>
                <th>
                    <fmt:message key="BACKGROUND_COLOR"/>
                </th>
                <td>
                    <input type="text" class="campo selectColor" style="width: 60px"
                           name="<%=SettingType.CAPACITY_PLANNING_LEVEL3_COLOR.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL3_COLOR)%>"/>
                </td>
                <th>
                    <fmt:message key="TEXT_COLOR"/>
                </th>
                <td>
                    <input type="text" class="campo selectColor" style="width: 60px"
                           name="<%=SettingType.CAPACITY_PLANNING_LEVEL3_COLOR_TEXT.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL3_COLOR_TEXT)%>"/>
                </td>
            </tr>
            
             <tr>
                <td ><img class="btitle" title="<fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL3.getMessageInfo() %>"/> <fmt:message key="<%=SettingType.CAPACITY_PLANNING_LEVEL3_COLOR.getMessageInfo() %>"/>" src="images/info.png"></td>
                <th>
                    <fmt:message key="out_of_range"/>
                </th>
                <th/>
               	<th>
                    <fmt:message key="BACKGROUND_COLOR"/>
                </th>
                <td>
                    <input type="text" class="campo selectColor" style="width: 60px"
                           name="<%=SettingType.CAPACITY_PLANNING_OUTOFRANGE_COLOR.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_OUTOFRANGE_COLOR)%>"/>
                </td>
                <th>
                    <fmt:message key="TEXT_COLOR"/>
                </th>
                <td>
                    <input type="text" class="campo selectColor" style="width: 60px"
                           name="<%=SettingType.CAPACITY_PLANNING_OUTOFRANGE_TEXT.getName()%>"
                           value="<%= SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_OUTOFRANGE_TEXT)%>"/>
                </td>
            </tr>
        </table>
        <div align="right" style="margin:10px;">
            <input type="button" class="boton" id="saveMiscelanea" value="<fmt:message key="save" />"/>
        </div>
    </form>
</visual:panel>