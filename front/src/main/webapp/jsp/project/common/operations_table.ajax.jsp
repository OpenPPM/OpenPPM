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
  ~ File: operations_table.ajax.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:56
  --%>

<%--
  Created by IntelliJ IDEA.
  User: jordi.ripoll
  Date: 21/07/2014
  Time: 9:40
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored ="false"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<fmt:setLocale value="${locale}" scope="request"/>

<c:choose>
    <c:when test="${operationsFte != null && not empty operationsFte}">

        <div style="padding-top:15px;">
            <div style="float: left; width: 150px; overflow: hidden;">
                <table class="tabledataScroll" width="100%">
                    <thead>
                    <tr>
                        <th style="border-radius: 5px 5px 0 0 !important;"><fmt:message key="operation"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="operationFte" items="${operationsFte}">
                        <tr>
                            <td><nobr>${tl:escape(operationFte.operation.operationName)}(${tl:escape(operationFte.operation.operationName)})</nobr></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div id="datesOperation" style="overflow: auto; width: 592px;">
                <table class="tabledataScroll" width="100%">
                    <thead>
                        <tr>
                            <c:forEach var="date" items="${dates}">
                                <th><nobr>${date}</nobr></th>
                            </c:forEach>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="operationFte" items="${operationsFte}">
                        <tr>
                            <c:forEach var="hoursByDay" items="${operationFte.hours}">
                                <td class="center">
                                    <nobr>
                                        <c:if test="${hoursByDay != null}">&nbsp;${hoursByDay}h</c:if>
                                    </nobr>
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div style="width: 100px;">&nbsp;</div>
        <div style="margin-bottom: 10px; padding: 0pt 0.7em;" class="ui-state-highlight ui-corner-all">
            <p>
                <span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
                <strong><fmt:message key="msg.info"/>: </strong>
            </p>
            <p>
                <fmt:message key="msg.info.capacity"/>
            </p>
        </div>
    </c:otherwise>
</c:choose>