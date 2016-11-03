<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page import="es.sm2.openppm.front.utils.CustomizationUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="es.sm2.openppm.front.utils.SettingUtil" %>
<%@ page import="es.sm2.openppm.core.logic.setting.GeneralSetting" %>
<%@ page import="es.sm2.openppm.front.utils.SecurityUtil" %>

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
  ~ File: staffing_table_popup.ajax.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>
<c:set var="statusInitiating"><%=Constants.STATUS_INITIATING%></c:set>
<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<script>
$(document).ready(function() {
	$('.btitle').bt({
		fill: '#F9FBFF',
		cssStyles: {color: '#343C4E', width: 'auto'},
		width: 250,
		padding: 10,
		cornerRadius: 5,
		spikeLength: 15,
		spikeGirth: 5,
		shadow: true,
		positions: 'right'
	});
});
</script>
<div style="width: 918px; padding-top:5px;">
	<div style="float: left; width: 250px; overflow: auto;">
		<table class="tabledataScroll" width="100%">
			<%-- Title --%>
			<thead>
				<tr>
					<th style="border-radius: 5px 5px 0 0 !important;">${titleTable }</th>
				</tr>
			</thead>
			<%-- Projects or job categories --%>
			<tbody>
				<c:forEach var="memberFte" items="${ftEs }">	
					<tr class="${op:isStringEquals(memberFte.project.status, statusInitiating)?'orangeRow':''}">
						<td>
							<nobr>
								<c:choose>
									<c:when test="${not empty memberFte.project}">
										${tl:escape(memberFte.project.projectName)}
                                        <c:if test="<%= (SettingUtil.getBoolean(settings, GeneralSetting.PM_VIEW_OTHER_PROJECTS) &&
                                                        SecurityUtil.isUserInRole(request, Constants.ROLE_PM)) || !SecurityUtil.isUserInRole(request, Constants.ROLE_PM)  %>">
                                            (${tl:escape(memberFte.project.employeeByProjectManager.contact.fullName) })
                                        </c:if>
									</c:when>
									<c:when test="${not empty memberFte.member.jobcategory}">
										${tl:escape(memberFte.member.jobcategory.name) }&nbsp;<span class="btitle" title="${tl:escape(memberFte.projectNames)}"><img style="width: 16px" src="images/info.png"></span>
									</c:when>
									<c:otherwise>
										<fmt:message key="job_category.empty"/>&nbsp;<span class="btitle" title="${tl:escape(memberFte.projectNames)}"><img style="width: 16px" src="images/info.png"></span>
									</c:otherwise>
								</c:choose>
							</nobr>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<%-- Total --%>
			 <tfoot>
			    <tr class="total">
			      <td><fmt:message key="total"/></td>		
			    </tr>
			 </tfoot>
		</table>
	</div>
	<div style="overflow: auto; float: left; width: 520px;">
		<table class="tabledataScroll" width="100%">
			<%-- Dates --%>
			<thead>
				<tr>
					<c:forEach var="dateHead" items="${listDates}">
						<th><nobr>${dateHead }</nobr></th>
					</c:forEach>
				</tr>
			</thead>
			<%-- Data --%>
			<tbody>
				<c:forEach var="memberFte" items="${ftEs}">
					<tr class="${op:isStringEquals(memberFte.project.status, statusInitiating)?'orangeRow':''}">
						<c:forEach var="fte" items="${memberFte.ftes }">
							<c:choose>
								<c:when test="${fte > 0}">
                                    <c:set var="backgroundColor" value="<%=CustomizationUtil.getInstance().generateColorCP(settings, pageContext.getAttribute(\"fte\"))%>"/>
                                    <td class='center ${fte > 100?"red":"" } assigned' style="${backgroundColor}">
                                        ${fte}%
                                    </td>
								</c:when>
								<c:when test="${fte == 0}">
									<td class="assigned"></td>
								</c:when>
								<c:otherwise><%-- No assignation --%>
									<td></td>
								</c:otherwise>																	
							</c:choose>
						</c:forEach>
					</tr>
				</c:forEach>
			</tbody>
			<%-- Totals --%>
			<tfoot>
				<tr class="total">
				
					<c:set var="iterate" value="0"/>
					<c:forEach var="date" items="${listDates}">
					
				 		<c:set var="totalHours" value="0"/>
				 		<c:set var="totalPercents" value="0"/>
					 		
						<c:forEach var="memberFte" items="${ftEs}">
							<c:if test="${memberFte.ftes[iterate] != -1}">
								<c:set var="totalPercents" value="${totalPercents + memberFte.ftes[iterate]}"/>
							</c:if>
							<c:set var="totalHours" value="${totalHours + memberFte.listHours[iterate]}"/>
						</c:forEach>

                        <c:set var="backgroundColor" value="<%=CustomizationUtil.getInstance().generateColorCP(settings, pageContext.getAttribute(\"totalPercents\"))%>"/>
						<td class="center ${totalPercents > 100?"red":"" }" style="${backgroundColor}">
							<nobr>
								<span>
									<c:choose>
										<c:when test="${totalPercents > 0}">
											${totalPercents}%
										</c:when>
										<c:otherwise>
											&nbsp;
										</c:otherwise>																
									</c:choose>
								</span>
							</nobr>
						</td>
				
						<c:set var="iterate" value="${iterate+1}"/>	
					</c:forEach>	
				</tr>
			</tfoot>
		</table>	
	</div>
</div>