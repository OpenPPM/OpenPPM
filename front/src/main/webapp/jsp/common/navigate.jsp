<%@ page import="es.sm2.openppm.front.utils.SettingUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="es.sm2.openppm.core.common.Constants" %>
<%@ page import="es.sm2.openppm.front.servlets.ProjectPlanServlet" %>
<%@ page import="es.sm2.openppm.front.servlets.ProjectControlServlet" %>
<%@ page import="es.sm2.openppm.front.servlets.ProjectInitServlet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

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
  ~ File: navigate.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:44
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%
    HashMap<String, String> settings = SettingUtil.getSettings(request);
%>

<script language="javascript" type="text/javascript" >
<!--
var navigate = {
    toProject : function(id, status) {

        var f = document.forms.navigate;
        f.id.value = id;

        f.accion.value = "";
        if (status == '<%=Constants.STATUS_PLANNING%>') {
            f.action = "<%=ProjectPlanServlet.REFERENCE%>";
        }
        else if (status == '<%=Constants.STATUS_CONTROL%>' || typeof status === 'undefined') {
            f.action = "<%=ProjectControlServlet.REFERENCE%>";
        }
        else {
            f.action = "<%=ProjectInitServlet.REFERENCE%>";
        }
        loadingPopup();
        f.submit();
    }
};
//-->
</script>

<form name="navigate" method="post">
    <input type="hidden" name="accion" value="" />
    <input type="hidden" name="id" />
</form>