<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
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
  ~ File: footer.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:44
  --%>

<script type="text/javascript">
    <!--

    readyMethods.add(function () {

        $.ajax({
            url: 'rest/util/setting',
            type: 'GET',
            dataType: 'json',
            contentType: 'application/json',
            data: {},
            success: function (data) {

                $("#versionAPP").append(data.<%= Constants.Version.VERSION_APP.name() %>);
                $("#versionNameAPP").append(data.<%= Constants.Version.VERSION_NAME_APP.name() %>);
                $("#easterEGG").attr("href", data.<%= Constants.Version.EASTER_EGG.name() %>);
            }
        });
    });

    //-->
</script>

<c:choose>
	<c:when test="${locale ne null and not empty locale  }">
		<fmt:setLocale value="${locale}" scope="request"/>
	</c:when>
	<c:otherwise>
		<fmt:setLocale value="<%=Constants.DEF_LOCALE %>" scope="request"/>
	</c:otherwise>
</c:choose>

<a href="#" onclick="return aboutPopup();">
	<fmt:message key="title"></fmt:message> -
</a>
<a href="" id="easterEGG" target="_blank">
	<b style="font-size: 11px;" id="versionNameAPP"></b> <span style="font-size:9px" id="versionAPP">v</span>
</a>
