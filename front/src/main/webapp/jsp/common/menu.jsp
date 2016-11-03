<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.front.servlets.AssignmentServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ExpenseSheetServlet"%>
<%@page import="es.sm2.openppm.front.servlets.TimeSheetServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ResourceServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@page import="es.sm2.openppm.front.servlets.CalendarServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@page import="es.sm2.openppm.front.servlets.PaymentProjectsServlet"%>
<%@ page import="es.sm2.openppm.core.logic.setting.GeneralSettingAuthorization" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%@taglib uri="http://java.sun.com/jstl/fmt"		prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt"	prefix="c" %>

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
  ~ File: menu.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:44
  --%>

<% Employee user = (Employee)request.getSession().getAttribute("user"); %>

<fmt:setLocale value="${locale}" scope="request"/>
<script>
	function viewOptionMenu(action, accion) {
		var f = document.frm_menu;
		f.action = action;
		f.accion.value = accion;
		loadingPopup();
		f.submit();
	}
</script>
<form name="frm_menu" method="post" action="">
	<input type="hidden" name="accion" value=""/>
</form>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="26"> <%-- <td height="26" background="images/fondo_.gif"> --%>
		<table style="width:100%" border="0" align="left" cellpadding="0" cellspacing="0">
			<tr>
				<td>
					<ul class="sf-menu menu_sup">
					
					<%--  *********************  --%>
					<%--  ***** Menus PMO *****  --%>
					
					<c:if test="${param.idProfile == role_pmo}">
						
						<%--  Menu Investments --%>
						<li>
							<a href="./projects?accion=<%=ProjectServlet.LIST_INVESTMENTS %>" class=""><fmt:message key="menu.investments" /></a>
						</li>
						
						<%--  Menu Projects --%>
						<li>
							<a href="./projects" class=""><fmt:message key="menu.projects" /></a>
						</li>
						
						<%--  Menu Programs --%>
						<li>
							<a href="./<%=ProgramServlet.REFERENCE %>" class=""><fmt:message key="menu.programs"/></a>
						</li>
						
						<c:if test="<%=SettingUtil.getApprovalRol(request) == user.getResourceprofiles().getIdProfile() %>">
							<%--  Menu Time & Expense Approvals --%>
							<li>
								<a href="./<%=TimeSheetServlet.REFERENCE %>" class=""><fmt:message key="menu.time_approvals"/></a>
							</li>
							<li>
								<a href="./<%=ExpenseSheetServlet.REFERENCE %>" class=""><fmt:message key="menu.expense_approvals"/></a>
							</li>
						</c:if>
						<c:if test="<%=Settings.SHOW_MANAGE_OPERATIONS && SettingUtil.getBoolean(SettingUtil.getSettings(request),GeneralSettingAuthorization.ACTIVE_MANAGEMENT_OPERATION)%>">
							<li>
								<a href="./static/index-new.html#/management-operation" class=""><fmt:message key="VIEW.MANAGEMENT.OPERATION.TITLE"/></a>
							</li>
						</c:if>
						
						<%--  Menu Resource Capacity Planning --%>
						<li>
							<a href="javascript:viewOptionMenu('<%=ResourceServlet.REFERENCE %>','<%=ResourceServlet.VIEW_CAPACITY_PLANNING %>');" class=""><fmt:message key="menu.resource_capacity_planning"/></a>
						</li>
						
						<%--  Menu Resource Capacity Running --%>
						<li>
							<a href="javascript:viewOptionMenu('<%=ResourceServlet.REFERENCE %>','<%=ResourceServlet.VIEW_CAPACITY_RUNNING %>');" class=""><fmt:message key="menu.resource_capacity_running"/></a>
						</li>

                        <%--  Menu Payment projects --%>
                        <li>
                            <a href="javascript:viewOptionMenu('<%=PaymentProjectsServlet.REFERENCE %>','');" class=""><fmt:message key="menu.payment_projects"/></a>
                        </li>

                        <%--  Menu Learned lessons --%>
                        <c:if test="<%=Settings.SHOW_LEARNED_LESSONS%>">
                            <li>
                                <a href="./static/index.html#/learned-lessons" class=""><fmt:message key="VIEW.LEARNED_LESSONS"/></a>
                            </li>
                        </c:if>
						
						<%--  Menu Organizational Project Assets (OPA) --%>
						<li>
							<a href="#" class=""><fmt:message key="menu.organizational_project_assets"/></a>
							
							<ul>
								<li>
									<a href="<%=CalendarServlet.REFERENCE %>" class=""><fmt:message key="menu.base_calendar"/></a>
								</li>
								<li>
									<a href="#" class=" disabled" style="display: none"><fmt:message key="menu.risk_database"/></a>
								</li>
								<li>
									<a href="#" class=" disabled" style="display: none"><fmt:message key="menu.lessons_learned_database"/></a>
								</li>
								<li>
									<a href="#" class=" disabled" style="display: none"><fmt:message key="menu.kpi_database"/></a>
								</li>
								<li>
									<a href="#" class=" disabled" style="display: none"><fmt:message key="menu.maturity_asessments"/></a>
								</li>
								<li>
									<a href="<%=MaintenanceServlet.REFERENCE %>" class=""><fmt:message key="menu.maintenance" /></a>
								</li>
							</ul>
						</li>

					</c:if>
					
					<%--  ************************************  --%>
					<%--  ***** Menus PORFOLIO MANAGER *****  --%>
					
					<c:if test="${param.idProfile == role_porf_manager}">
						
						<%--  Menu Investments --%>
						<li>
							<a href="./projects?accion=<%=ProjectServlet.LIST_INVESTMENTS %>" class=""><fmt:message key="menu.investments" /></a>
						</li>
						
						<%--  Menu Projects --%>
						<li>
							<a href="./projects" class=""><fmt:message key="menu.projects" /></a>
						</li>
						
						<%--  Menu Programs --%>
						<li>
							<a href="./<%=ProgramServlet.REFERENCE %>" class=""><fmt:message key="menu.programs"/></a>
						</li>
					
					</c:if>
					
					<%--  ************************************  --%>
					<%--  ***** Menus FUNCTIONAL MANAGER *****  --%>
					
					<c:if test="${param.idProfile == role_fun_manager}">
						
						<%--  Menu Investments --%>
						<li>
							<a href="./projects?accion=<%=ProjectServlet.LIST_INVESTMENTS %>" class=""><fmt:message key="menu.investments" /></a>
						</li>
						
						<%--  Menu Projects --%>
						<li>
							<a href="./projects" class=""><fmt:message key="menu.projects" /></a>
						</li>
						
						<%--  Menu Programs --%>
						<li>
							<a href="./<%=ProgramServlet.REFERENCE %>" class=""><fmt:message key="menu.programs"/></a>
						</li>
						
						<c:if test="<%=SettingUtil.getApprovalRol(request) == user.getResourceprofiles().getIdProfile() %>">
							<%--  Menu Time & Expense Approvals --%>
							<li>
								<a href="./<%=TimeSheetServlet.REFERENCE %>" class=""><fmt:message key="menu.time_approvals"/></a>
							</li>
							<li>
								<a href="./<%=ExpenseSheetServlet.REFERENCE %>" class=""><fmt:message key="menu.expense_approvals"/></a>
							</li>
						</c:if>
					</c:if>
					
					<%--  *************************  --%>
					<%--  ***** Menus SPONSOR *****  --%>
					
					<c:if test="${param.idProfile == role_sponsor}">
						
						<%--  Menu Projects --%>
						<li>
							<a href="./projects" class=""><fmt:message key="menu.projects" /></a>
						</li>
						
					</c:if>
					
					<%--  *************************  --%>
					<%--  ***** Menus STAKEHOLDER *****  --%>
					
					<c:if test="${param.idProfile == role_stakeholder}">
						
						<%--  Menu Projects --%>
						<li>
							<a href="./projects" class=""><fmt:message key="menu.projects" /></a>
						</li>
						
					</c:if>
					
					<%--  ************************************  --%>
					<%--  ***** Menus INVESTMENT MANAGER *****  --%>
					
					<c:if test="${param.idProfile == role_inv_manager}">
					
						<%--  Menu Investments --%>
						<li>
							<a href="./projects?accion=<%=ProjectServlet.LIST_INVESTMENTS %>" class=""><fmt:message key="menu.investments" /></a>
						</li>
						
						<%--  Menu Investment Approvals --%>
<%--
						<li>
							<a href="#" class=" disabled"><fmt:message key="menu.investment_approvals"/></a>
						</li>
 --%>
					</c:if>
					
					<%--  **********************************  --%>
					<%--  ***** Menus RESOURCE MANAGER *****  --%>
					
					<c:if test="${param.idProfile == role_res_manager}">
						
						<%--  Menu Assignations Approvals --%>
						<li>
							<a href="./<%=ResourceServlet.REFERENCE %>" class=""><fmt:message key="menu.assignations_approvals"/></a>
						</li>
						
						<%--  Menu Resource Pool --%>
						<li>
							<a href="javascript:viewOptionMenu('<%=ResourceServlet.REFERENCE %>','<%=ResourceServlet.VIEW_RESOURCE_POOL %>');" class=""><fmt:message key="menu.resource_pool"/></a>
						</li>
						
						<%--  Menu Resource Capacity Planning --%>
						<li>
							<a href="javascript:viewOptionMenu('<%=ResourceServlet.REFERENCE %>','<%=ResourceServlet.VIEW_CAPACITY_PLANNING %>');" class=""><fmt:message key="menu.resource_capacity_planning"/></a>
						</li>
						
						<%--  Menu Resource Capacity Running --%>
						<li>
							<a href="javascript:viewOptionMenu('<%=ResourceServlet.REFERENCE %>','<%=ResourceServlet.VIEW_CAPACITY_RUNNING %>');" class=""><fmt:message key="menu.resource_capacity_running"/></a>
						</li>

						<c:if test="<%=Settings.SHOW_MANAGE_OPERATIONS && SettingUtil.getBoolean(SettingUtil.getSettings(request),GeneralSettingAuthorization.ACTIVE_MANAGEMENT_OPERATION)%>">
							<li>
								<a href="./static/index-new.html#/management-operation" class=""><fmt:message key="VIEW.MANAGEMENT.OPERATION.TITLE"/></a>
							</li>
						</c:if>
					
					</c:if>
					
					<%--  *********************************  --%>
					<%--  ***** Menus PROGRAM MANAGER *****  --%>
					
					<c:if test="${param.idProfile == role_pgm_manager}">
						
						<%--  Menu Investments --%>
						<li>
							<a href="./projects?accion=<%=ProjectServlet.LIST_INVESTMENTS %>" class=""><fmt:message key="menu.investments" /></a>
						</li>
						
						<%--  Menu Projects --%>
						<li>
							<a href="./projects" class=""><fmt:message key="menu.projects" /></a>
						</li>
						
						<%--  Menu Programs --%>
						<li>
							<a href="<%=ProgramServlet.REFERENCE %>" class=""><fmt:message key="menu.programs"/></a>
						</li>

						<%--  Menu KPI --%>
						<li>
							<%--
								<a href="./<%=ProgramServlet.REFERENCE %>?accion=<%=ProgramServlet.VIEW_KPI %>" class=""><fmt:message key="menu.kpi" /></a>
							 --%>
							<a href="#" class=" disabled"><fmt:message key="menu.kpi" /></a>
						</li>
					
						<%--  Performance Appraisals --%>
						<li>
							<a href="#" class=" disabled"><fmt:message key="menu.performance_appraisals"/></a>
						</li>

                        <%-- Menu Lessons Learned --%>
                        <c:if test="<%=Settings.SHOW_LEARNED_LESSONS%>">
                            <li>
                                <a href="./static/index.html#/learned-lessons" class=""><fmt:message key="VIEW.LEARNED_LESSONS"/></a>
                            </li>
                        </c:if>

					</c:if>
					
					<%--  *********************************  --%>
					<%--  ***** Menus PROJECT MANAGER *****  --%>
					
					<c:if test="${param.idProfile == role_prj_manager}">
					
						<%--  Menu Projects --%>
						<li>
							<a href="./projects" class=""><fmt:message key="menu.projects" /></a>
						</li>
					
						<%--  Menu Time & Expense Approvals --%>
						<li>
							<a href="./<%=TimeSheetServlet.REFERENCE %>" class=""><fmt:message key="menu.time_approvals"/></a>
						</li>

                        <c:if test="<%=SettingUtil.getApprovalRol(request) == user.getResourceprofiles().getIdProfile() %>">
                            <li>
                                <a href="./<%=ExpenseSheetServlet.REFERENCE %>" class=""><fmt:message key="menu.expense_approvals"/></a>
                            </li>
                        </c:if>

						<%--  Performance Appraisals --%>
						<li>
							<a href="#" class=" disabled"><fmt:message key="menu.performance_appraisals"/></a>
						</li>
						
						<%--  Menu Resource Capacity Planning --%>
						<li>
							<a href="javascript:viewOptionMenu('<%=ResourceServlet.REFERENCE %>','<%=ResourceServlet.VIEW_CAPACITY_PLANNING %>');" class=""><fmt:message key="menu.resource_capacity_planning"/></a>
						</li>
						
						<%--  Menu Resource Capacity Running --%>
						<li>
							<a href="javascript:viewOptionMenu('<%=ResourceServlet.REFERENCE %>','<%=ResourceServlet.VIEW_CAPACITY_RUNNING %>');" class=""><fmt:message key="menu.resource_capacity_running"/></a>
						</li>

                        <%--  Menu Learned lessons --%>
                        <c:if test="<%=Settings.SHOW_LEARNED_LESSONS%>">
                            <li>
                                <a href="./static/index.html#/learned-lessons" class=""><fmt:message key="VIEW.LEARNED_LESSONS"/></a>
                            </li>
                        </c:if>
					</c:if>
					
					<%--  **************************  --%>
					<%--  ***** Menus RESOURCE *****  --%>
					
					<c:if test="${param.idProfile == role_resource}">
					
						<%--  Assignments --%>
						<li>
							<a href="./<%=AssignmentServlet.REFERENCE %>" class=""><fmt:message key="assignments"/></a>
						</li>
					
						<%--  Menu Time Sheet --%>
						<li>
							<a href="./<%=TimeSheetServlet.REFERENCE %>" class=""><fmt:message key="menu.time_sheet"/></a>
						</li>
						
						<%--  Menu Expense Sheet --%>
						<li>
							<a href="./<%=ExpenseSheetServlet.REFERENCE %>" class=""><fmt:message key="menu.expenses_sheet"/></a>
						</li>
					
						<%--  Resource Calendar --%>
						<li>
							<a href="#" class=" disabled"><fmt:message key="menu.resource_calendar"/></a>
						</li>

						<c:if test="<%=Settings.SHOW_MANAGE_OPERATIONS && SettingUtil.getBoolean(SettingUtil.getSettings(request),GeneralSettingAuthorization.ACTIVE_MANAGEMENT_OPERATION)%>">
							<li>
								<a href="./static/index-new.html#/management-operation" class=""><fmt:message key="VIEW.STATE.OPERATION.TITLE"/></a>
							</li>
						</c:if>
					</c:if>

                    <%--  **************************  --%>
                    <%--  ***** Menus LOGISTIC *****  --%>

                    <c:if test="${param.idProfile == role_logistic}">

                        <%--  Menu Projects --%>
                        <li>
                            <a href="./projects" class=""><fmt:message key="menu.projects" /></a>
                        </li>

                        <%--  Menu Payment projects --%>
                        <li>
                            <a href="javascript:viewOptionMenu('<%=PaymentProjectsServlet.REFERENCE %>','');" class=""><fmt:message key="menu.payment_projects"/></a>
                        </li>

						<c:if test="<%=Settings.SHOW_MANAGE_OPERATIONS && SettingUtil.getBoolean(SettingUtil.getSettings(request),GeneralSettingAuthorization.ACTIVE_MANAGEMENT_OPERATION)%>">
							<li>
								<a href="./static/index-new.html#/management-operation" class=""><fmt:message key="VIEW.MANAGEMENT.OPERATION.TITLE"/></a>
							</li>
						</c:if>
                    </c:if>

                    <%--  **************************  --%>
                    <%--  ***** Menus ADMIN *****  --%>

                    <c:if test="${param.idProfile == role_admin}">

                        <c:if test="<%=Settings.SHOW_AUDIT%>">
                            <%--  Menu Learned lessons --%>
                            <li>
                                <a href="./static/index-new.html#/configuration/audit" class=""><fmt:message key="ADMIN.AUDIT"/></a>
                            </li>
                            <li>
                                <a href="./static/index-new.html#/audit/project-data" class=""><fmt:message key="VIEW.AUDIT.NAME"/></a>
                            </li>
                        </c:if>
                    </c:if>

					</ul>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>