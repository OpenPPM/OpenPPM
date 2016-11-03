<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>

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
  ~ File: capacity_planning.ajax.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:04
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script>
$(document).ready(function() {
	//Activate tooltip
	createBT('.btitle');
});
</script>
<c:choose>
	<c:when test="${not empty listDates && not empty capacityPlanningDetails}">
	
		<div style="padding-top:5px;">
			<div style="float: left; width: 500px; overflow: auto;">
				<table class="tabledataScroll" width="100%">
					<thead>
						<tr>
							<th style="border-radius: 5px 5px 0 0 !important;"><fmt:message key="project"/></th>
							<th style="border-radius: 5px 5px 0 0 !important;"><fmt:message key="activity"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="capacityPlanningDetail" items="${capacityPlanningDetails}">	
							<tr>
								<%-- Project names --%>
								<td>
									<nobr>
										<c:choose>
											<c:when test="${not empty capacityPlanningDetail && not empty capacityPlanningDetail.projectName}">
												${tl:escape(capacityPlanningDetail.projectName)}
											</c:when>
										</c:choose>
									</nobr>
								</td>
								<%-- Activities --%>
								<td>
									<nobr>
										<c:choose>
											<c:when test="${not empty capacityPlanningDetail && not empty capacityPlanningDetail.activityName}">
												${tl:escape(capacityPlanningDetail.activityName)}
											</c:when>
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
			<div style="overflow: auto;">
				<table class="tabledataScroll" width="100%">
					<%-- Dates --%>
					<thead>
						<tr>
							<c:forEach var="dateHead" items="${listDates}">
								<th><nobr>${dateHead}</nobr></th>
							</c:forEach>
						</tr>
					</thead>
					<tbody>
						<%-- Ftes --%>
						<c:forEach var="capacityPlanningDetail" items="${capacityPlanningDetails}">
							<tr>
								<c:forEach var="week" items="${capacityPlanningDetail.weeks}">
									<c:choose>
										<c:when test="${not empty week.fteAssigned && not empty week.ftePreAssigned}">
											<td class="center">
												<span class="btitle" bt-xtitle='<fmt:message key="resource.assigned"/>: ${week.fteAssigned}% + <fmt:message key="resource.preassigned"/>: ${week.ftePreAssigned}%'>
													${week.fteAssigned + week.ftePreAssigned}%
												</span>
											</td>
										</c:when>
										<c:when test="${not empty week.fteAssigned}">
											<td class="center" style="color:green">
												${week.fteAssigned}%
											</td>
										</c:when>
										<c:when test="${not empty week.ftePreAssigned}">
											<td class="center" style="color:orange">
												${week.ftePreAssigned}%
											</td>
										</c:when>
										<c:otherwise>
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
							
						 		<c:set var="totalPercents" value="0"/>
							 		
								<c:forEach var="capacityPlanningDetail" items="${capacityPlanningDetails}">
									<c:set var="totalPercents" value="${totalPercents + capacityPlanningDetail.weeks[iterate].fteAssigned + capacityPlanningDetail.weeks[iterate].ftePreAssigned}"/>
								</c:forEach>
								
								<td class="center ${totalPercents > 100?"red":"" }">
									<nobr>
										<span>
											<c:choose>
												<c:when test="${totalPercents > 0.0}">
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
			<div style="width: 100px;">&nbsp;&nbsp;</div>
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