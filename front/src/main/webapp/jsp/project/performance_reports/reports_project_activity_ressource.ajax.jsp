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
  ~ File: reports_project_activity_ressource.ajax.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:03
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--

readyMethods.add(function() {
});
//-->
</script>
<div style="padding-top:15px; max-height: 600px; overflow: auto;">
	<c:choose>
		<c:when test="${data != null}" >
		<div style="float: left; width: 300px; overflow: hidden;">
			<table class="tabledataScroll" style="width:100%">
				<thead>
					<tr>
						<th style="border-radius: 5px 5px 0 0 !important;"><fmt:message key="project"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="project" items="${projects }">
						<tr>
							<td><nobr>${project }</nobr></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div style="float: left; width: 250px; overflow: hidden;">
			<table class="tabledataScroll" style="width:100%">
				<thead>
					<tr>
						<th style="border-radius: 5px 5px 0 0 !important;"><fmt:message key="activity"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="activity" items="${activities }">
						<tr>
							<td><nobr>${activity }</nobr></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div style="overflow-x: auto;">
			<table class="tabledataScroll" style="width:100%">
				<thead>
					<tr>
						<c:forEach var="ressource" items="${ressources}">
							<th><nobr>${ressource }</nobr></th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="project" items="${data}" begin="1">
						<tr>
							<c:forEach var="ressource" items="${project}" begin="3">
								<td class="center">
									<c:choose>
										<c:when test="${ressource == null}">
											${tl:toCurrency(0.0)}
										</c:when>
										<c:otherwise>
											${tl:toCurrency(ressource)}
										</c:otherwise>
									</c:choose>
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		</c:when>
		<c:otherwise>
			<fmt:message key="data_not_found"/>
		</c:otherwise>
	</c:choose>
</div>