<%@page import="es.sm2.openppm.core.common.GenericSetting"%>
<%@page import="es.sm2.openppm.core.logic.setting.common.TypeSetting"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
  ~ File: startTag.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<%--
params: startColumn, numberOfColumns, fields
 --%>

<%
HashMap<String, String> settings = SettingUtil.getSettings(request);

GenericSetting[] fields 	= (GenericSetting[])request.getAttribute("fields");
Integer startColumn 		= (Integer)request.getAttribute("startColumn");
Integer numberOfColumns 	= (Integer)request.getAttribute("numberOfColumns");
%>

<%-- Generic add notifications --%>
<%
    Integer column = startColumn;
    for (GenericSetting field : fields) {
        if (field.isVisible()) {
            if (column == 1) {
        %>
                <tr>
        <%
            }
        %>
        <td><img class="btitle" title="<fmt:message key="<%=field.getMessageInfo()%>"/>" src="images/info.png"></td>
        <th><fmt:message key="<%=field.getMessage()%>"/></th>
        <td>
            <c:choose>
                <c:when test="<%=TypeSetting.CHECKBOX.equals(field.getTypeSetting())%>">
                    <input type="checkbox" class="campo" name="<%=field.getName()%>" value="true" <%=SettingUtil.isChecked(settings, field) %>/>
                </c:when>
                <c:when test="<%=TypeSetting.INPUT.equals(field.getTypeSetting())%>">
                    <input type="text" class="campo" name="<%=field.getName()%>" value="<%=SettingUtil.getString(settings, field) %>"/>
                </c:when>
                <c:when test="<%=TypeSetting.PASSWORD.equals(field.getTypeSetting())%>">
                    <input type="password" class="campo" name="<%=field.getName()%>" value="<%=SettingUtil.getString(settings, field) %>"/>
                </c:when>
                <c:when test="<%=TypeSetting.SELECT.equals(field.getTypeSetting())%>">
                    <select name="<%=field.getName()%>" class="campo">
                        <% for (Enum optionEnum : field.getEnumElements()) {%>
                            <option value="<%=optionEnum.name()%>" <%=optionEnum.name().equals(SettingUtil.getString(settings, field))? "selected":""%> ><%=optionEnum.name()%></option>
                        <%}%>
                    </select>
                </c:when>
            </c:choose>
        </td>



        <%
            if (column.equals(numberOfColumns)) {
                column = 0;
        %>
                </tr>
        <%
            }
            else {

        %>
                <td>&nbsp;</td>
        <%
            }
            column ++;
            }
    }
%>
