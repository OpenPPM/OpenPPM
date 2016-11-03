<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

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
  ~ File: plan_team_calendar.ajax.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:49
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<div style="padding-top:15px;">
	<div style="float: left; width: 250px; overflow: hidden;">
		<table class="tabledataScroll" width="100%">
			<thead>
				<tr>
					<th><fmt:message key="contact.fullname"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="team" items="${teamCalendars}">
					<tr>
						<td><nobr>${tl:escape(team.name) }</nobr></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div style="overflow: auto;">
		<table class="tabledataScroll" width="100%">
			<thead>
				<tr>
					<c:forEach var="date" items="${dates}">
						<th><nobr>${date }</nobr></th>
					</c:forEach>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="team" items="${teamCalendars}">
					<tr>
						<c:forEach var="value" items="${team.values }">
							<td class="center"><div class="${value }">&nbsp;&nbsp;&nbsp;</div></td>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>