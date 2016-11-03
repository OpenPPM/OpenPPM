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
  ~ File: staffing_table.ajax.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<c:choose>
	<c:when test="${not empty ftEs}">
	
		<div class="right">
			<a class="button_img" href="javascript:exportCSV();"><img src="images/csv.png" style="width: 16px"/><span><fmt:message key="export.csv"/></span></a>
		</div>
		<div style="padding-top:10px;">
			<div style="float: left; width: 250px; overflow: hidden;">
				<table class="tabledataScroll" width="100%">
					<thead>
						<tr>
							<th><fmt:message key="contact.fullname"/></th>
							<th>&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="memberFte" items="${ftEs}">
							<tr>
								<td><nobr>${tl:escape(memberFte.member.employee.contact.fullName)}</nobr></td>
								<td class="center">
									<img class="right" onclick="viewResource('${tl:escape(memberFte.member.employee.contact.fullName)}',${memberFte.member.employee.idEmployee })" title="<fmt:message key="view"/>" class="link" src="images/view.png">
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div style="overflow: auto;">
				<table class="tabledataScroll" width="100%">
					<thead>
						<tr>
							<c:forEach var="dateHead" items="${listDates}">
								<th><nobr>${dateHead }</nobr></th>
							</c:forEach>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="memberFte" items="${ftEs}">
							<tr>
								<c:set var="iterate" value="0"/>
								<c:forEach var="fte" items="${memberFte.ftes}">
									<c:choose>
										<c:when test="${fte > 0}">
											<td class="center">
												<nobr>
													${fte}%
													<c:if test="${not empty memberFte.listHours}">
														&nbsp;
														<fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${memberFte.listHours[iterate]}"/>h
													</c:if>
												</nobr>
											</td>
										</c:when>
										<c:otherwise><td></td></c:otherwise>																
									</c:choose>
									<c:set var="iterate" value="${iterate+1}"/>
								</c:forEach>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</c:when>
	<c:otherwise>
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