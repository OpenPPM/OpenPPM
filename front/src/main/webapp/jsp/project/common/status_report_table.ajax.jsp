<%@page import="es.sm2.openppm.core.common.Constants"%>
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
  ~ File: status_report_table.ajax.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:56
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<table cellpadding="0" cellspacing="1" style="width:100%" class="tabledata">
	<thead style="background-color:#DDE8F4;">
		<tr>
			<th width="20%"><fmt:message key="project_name"/></th>
			<th width="5%"><fmt:message key="date"/></th>
			<th width="5%"><fmt:message key="status"/></th>
			<th width="10%"><fmt:message key="type"/></th>
			<th width="56%"><fmt:message key="desc"/></th>
			<th width="4%">&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:set var="counter" value="${0 }"/>
		<c:forEach var="projectExecutivereport" items="${projects}">
			<c:choose>
				<c:when test="${ projectExecutivereport.executivereport != null and projectExecutivereport.projectfollowup != null  }">
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td rowspan="8" class="left">${tl:escape(projectExecutivereport.project.projectName)}</td>
						<td rowspan="4" class="center"><fmt:formatDate value="${projectExecutivereport.executivereport.statusDate}" pattern="${datePattern}" /></td>
						<td rowspan="4" class="left"><fmt:message key="project_status.${projectExecutivereport.project.status}" /></td>
						<td rowspan="2" class="left"><fmt:message key="executive_report.internal"/></td>
						<td rowspan="2" class="lfet">${op:generateBreak(tl:escape(projectExecutivereport.executivereport.internal))}</td>
						<td rowspan="8" class="center"><img onclick="viewProject(${projectExecutivereport.project.idProject},'${projectExecutivereport.status}');" title="<fmt:message key="view"/>" class="link" src="images/view.png"></td>
					</tr>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }"/>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td rowspan="2" class="left"><fmt:message key="executive_report.external"/></td>
						<td rowspan="2" class="left">${op:generateBreak(tl:escape(projectExecutivereport.executivereport.external))}</td>
					</tr>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }"/>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td rowspan="4" class="center"><fmt:formatDate value="${projectExecutivereport.projectfollowup.followupDate}" pattern="${datePattern}" /></td>
						<td class="left"><div class="statusReport${projectExecutivereport.projectfollowup.generalFlag}">&nbsp;</div></td>
						<td class="left"><fmt:message key="general"/></td>
						<td class="left">${op:generateBreak(tl:escape(projectExecutivereport.projectfollowup.generalComments))}</td>
					</tr>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td class="left"><div class="statusReport${projectExecutivereport.projectfollowup.riskFlag}">&nbsp;</div></td>
						<td class="left"><fmt:message key="risk"/></td>
						<td class="left">${op:generateBreak(tl:escape(projectExecutivereport.projectfollowup.risksComments))}</td>
					</tr>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td class="left"><div class="statusReport${projectExecutivereport.projectfollowup.scheduleFlag}">&nbsp;</div></td>
						<td class="left"><fmt:message key="schedule"/></td>
						<td class="left">${op:generateBreak(tl:escape(projectExecutivereport.projectfollowup.scheduleComments))}</td>
					</tr>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td class="left"><div class="statusReport${projectExecutivereport.projectfollowup.costFlag}">&nbsp;</div></td>
						<td class="left"><fmt:message key="cost"/></td>
						<td class="left">${op:generateBreak(tl:escape(projectExecutivereport.projectfollowup.costComments))}</td>
					</tr>
					<c:set var="counter" value="${counter + 1 }"/>
				</c:when>
				<c:when test="${ projectExecutivereport.executivereport == null and projectExecutivereport.projectfollowup != null  }">
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td rowspan="4" class="left">${tl:escape(projectExecutivereport.project.projectName)}</td>
						<td rowspan="4" class="center"><fmt:formatDate value="${projectExecutivereport.projectfollowup.followupDate}" pattern="${datePattern}" /></td>
						<td class="left"><div class="statusReport${projectExecutivereport.projectfollowup.generalFlag}">&nbsp;</div></td>
						<td class="left"><fmt:message key="general"/></td>
						<td class="left">${op:generateBreak(tl:escape(projectExecutivereport.projectfollowup.generalComments))}</td>
						<td rowspan="4" class="center"><img onclick="viewProject(${projectExecutivereport.project.idProject},'${projectExecutivereport.status}');" title="<fmt:message key="view"/>" class="link" src="images/view.png"></td>
					</tr>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td class="left"><div class="statusReport${projectExecutivereport.projectfollowup.riskFlag}">&nbsp;</div></td>
						<td class="left"><fmt:message key="risk"/></td>
						<td class="left">${op:generateBreak(tl:escape(projectExecutivereport.projectfollowup.risksComments))}</td>
					</tr>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td class="left"><div class="statusReport${projectExecutivereport.projectfollowup.scheduleFlag}">&nbsp;</div></td>
						<td class="left"><fmt:message key="schedule"/></td>
						<td class="left">${op:generateBreak(tl:escape(projectExecutivereport.projectfollowup.scheduleComments))}</td>
					</tr>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td class="left"><div class="statusReport${projectExecutivereport.projectfollowup.costFlag}">&nbsp;</div></td>
						<td class="left"><fmt:message key="cost"/></td>
						<td class="left">${op:generateBreak(tl:escape(projectExecutivereport.projectfollowup.costComments))}</td>
					</tr>
					<c:set var="counter" value="${counter + 1 }"/>
				</c:when>
				<c:when test="${ projectExecutivereport.executivereport != null and projectExecutivereport.projectfollowup == null  }">
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td rowspan="4" class="left">${tl:escape(projectExecutivereport.project.projectName)}</td>
						<td rowspan="4" class="center"><fmt:formatDate value="${projectExecutivereport.executivereport.statusDate}" pattern="${datePattern}" /></td>
						<td rowspan="4" class="left"><fmt:message key="project_status.${projectExecutivereport.project.status}" /></td>
						<td rowspan="2" class="left"><fmt:message key="executive_report.internal"/></td>
						<td rowspan="2" class="left">${op:generateBreak(tl:escape(projectExecutivereport.executivereport.internal))}</td>
						<td rowspan="4" class="center"><img onclick="viewProject(${projectExecutivereport.project.idProject},'${projectExecutivereport.project.status}');" title="<fmt:message key="view"/>" class="link" src="images/view.png"></td>
					</tr>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }"/>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }">
						<td rowspan="2" class="left"><fmt:message key="executive_report.external"/></td>
						<td rowspan="2" class="left">${op:generateBreak(tl:escape(projectExecutivereport.executivereport.external))}</td>
					</tr>
					<tr class="${ (counter mod 2) == 0 ? 'even' : 'odd'  }"/>
					<c:set var="counter" value="${counter + 1 }"/>
				</c:when>
			</c:choose>
		</c:forEach>
	</tbody>
</table>