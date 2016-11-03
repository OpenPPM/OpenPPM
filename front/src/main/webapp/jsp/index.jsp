<%@page import="es.sm2.openppm.front.servlets.PluginServlet"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.utils.StringPool"%>
<%@page import="es.sm2.openppm.utils.functions.ValidateUtil"%>
<%@page import="es.sm2.openppm.utils.params.ParamUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
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
  ~ File: index.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<compress:html
	enabled="<%= Settings.PERFORMANCE_COMPRESS_HTML%>"
	compressJavaScript="false"
	removeComments="true">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://granule.com/tags" prefix="g" %>

<%@page import="es.sm2.openppm.front.utils.OpenppmUtil"%>
<%@page import="es.sm2.openppm.front.servlets.UtilServlet"%>
<%@page import="es.sm2.openppm.front.servlets.MyAccountServlet"%>
<%@page import="es.sm2.openppm.front.servlets.LoginServlet"%>
<%@page import="es.sm2.openppm.front.servlets.HomeServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Employee"%>

<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<c:choose>
	<c:when test="${locale ne null and not empty locale  }">
		<fmt:setLocale value="${locale}" scope="request"/>
	</c:when>
	<c:otherwise>
		<fmt:setLocale value="<%=Constants.DEF_LOCALE %>" scope="request"/>
	</c:otherwise>
</c:choose>

<c:set var="status_initiating" scope="request"><%=Constants.STATUS_INITIATING%></c:set>
<c:set var="status_planning" scope="request"><%=Constants.STATUS_PLANNING%></c:set>
<c:set var="status_control" scope="request"><%=Constants.STATUS_CONTROL%></c:set>
<c:set var="status_closed" scope="request"><%=Constants.STATUS_CLOSED%></c:set>
<c:set var="statusArchived" scope="request"><%=Constants.STATUS_ARCHIVED%></c:set>

<c:set var="typeProject" scope="request"><%=Constants.TYPE_PROJECT%></c:set>
<c:set var="typeInvestment" scope="request"><%=Constants.TYPE_INVESTMENT%></c:set>

<c:set var="role_pmo" scope="request"><%=Constants.ROLE_PMO%></c:set>
<c:set var="role_pgm_manager" scope="request"><%=Constants.ROLE_PROGM%></c:set>
<c:set var="role_fun_manager" scope="request"><%=Constants.ROLE_FM%></c:set>
<c:set var="role_inv_manager" scope="request"><%=Constants.ROLE_IM%></c:set>
<c:set var="role_prj_manager" scope="request"><%=Constants.ROLE_PM%></c:set>
<c:set var="role_res_manager" scope="request"><%=Constants.ROLE_RM%></c:set>
<c:set var="role_sponsor" scope="request"><%=Constants.ROLE_SPONSOR%></c:set>
<c:set var="role_resource" scope="request"><%=Constants.ROLE_RESOURCE%></c:set>
<c:set var="role_porf_manager" scope="request"><%=Constants.ROLE_PORFM%></c:set>
<c:set var="role_stakeholder" scope="request"><%=Constants.ROLE_STAKEHOLDER%></c:set>
<c:set var="role_logistic" scope="request"><%=Constants.ROLE_LOGISTIC%></c:set>
<c:set var="role_admin" scope="request"><%=Constants.ROLE_ADMIN%></c:set>

<c:if test="${rootActivity ne null }">
	<c:set var="valPlanInitDate" scope="request"><fmt:formatDate value="${rootActivity.planInitDate}" pattern="${datePattern}" /></c:set>
	<c:set var="valPlanEndDate" scope="request"><fmt:formatDate value="${rootActivity.planEndDate}" pattern="${datePattern}" /></c:set>
	<c:set var="valActInitDate" scope="request"><fmt:formatDate value="${rootActivity.actualInitDate}" pattern="${datePattern}" /></c:set>
	<c:set var="valActEndDate" scope="request"><fmt:formatDate value="${rootActivity.actualEndDate}" pattern="${datePattern}" /></c:set>
	<c:set var="valFirstDate" scope="request"><fmt:formatDate value="${rootActivity.firstDate}" pattern="${datePattern}" /></c:set>
	<c:set var="valLastDate" scope="request"><fmt:formatDate value="${rootActivity.lastDate}" pattern="${datePattern}" /></c:set>
	<input type="hidden" id="valPlanInitDate" value="${valPlanInitDate }"/>
	<input type="hidden" id="valPlanEndDate" value="${valPlanEndDate }"/>
	<input type="hidden" id="valActInitDate" value="${valActInitDate }"/>
	<input type="hidden" id="valActEndDate" value="${valActEndDate }"/>
	<input type="hidden" id="valFirstDate" value="${valFirstDate }"/>
	<input type="hidden" id="valLastDate" value="${valLastDate }"/>
</c:if>

<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=9">
		<title><fmt:message key="title_two"/></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<%-- Include All JS and CSS --%>
		<jsp:include page="common/head_includes.jsp" flush="true">
			<jsp:param value="true" name="compresJS"/>
		</jsp:include>
		<%
			Employee user = SecurityUtil.validateLoggedUser(request);
				
				Integer idProfile = -1;
				if (user != null && user.getResourceprofiles() != null) {
					idProfile = user.getResourceprofiles().getIdProfile();
				}	
				
				String formulario = null;
				if (request.getParameter("nextForm") != null) {
					formulario = request.getParameter("nextForm") + ".jsp";
				}
				
				HashMap<String, String> settings = SettingUtil.getSettings(request);
		%>
	</head>
	<body ng-app="openppmController">

		<script type="application/javascript">var appController = angular.module('openppmController', []);</script>

		<%-- Notifications --%>
		<c:if test="${user != null}">
			<jsp:include page="notification/notification.jsp" flush="true"/>
			<script type="text/javascript" src="js/jquery/notification/notification-center.js"></script>
			<script type="text/javascript">
				notificationUtils.options = {
		        	unread:'<fmt:message key="notification.unread" />',
		        	all:'<fmt:message key="notification.all" />',
		        	notificationListEmptyText: '<fmt:message key="notification.empty" />',
		        	reference: '<%=UtilServlet.REFERENCE%>'
		        };
				readyMethods.add(function () {
					notificationUtils.createAll(<%=request.getAttribute("notificationCenter")%>);
				});
			</script>
		</c:if>
		<div id="up-page"></div>
		<div id="page-wrap">
			<jsp:include page="common/infoApp.jsp" flush="true"/>
			<div id="page-timeout">
				<fmt:message key="session_will_expire_in" /><input type="text" id="session-time-expire" readonly="readonly" style="border:0px" /><fmt:message key="minutes" />
				<a href="#" class="boton" onClick="return enlargeSesion();"><fmt:message key="extend-session" /></a>
			</div>
			<form name="form_change_rol" method="post" action="<%=HomeServlet.REFERENCE%>">
				<input name="accion" type="hidden" value="<%=HomeServlet.SELECT_ROL%>"/>
			</form>
			<div id="wrapper" class="left" >
			
				<div id="header">
				
					<div id="login">
						<table width="100%" border="0" cellspacing="5" cellpadding="0">
							<tr>
								<td class="login_info left" style="padding-left:13px;">
									<c:if test="${user != null }">
										<a href="javascript:docsAndManualsPopup();"><fmt:message key="docs_and_manuals" /></a>
									</c:if>
								</td>
								<td align="right" width="50%" class="login_info">&nbsp;</td>
							</tr>
							<tr>
								<td class="login_info left" style="padding-left:13px;">
									<c:if test="${user != null }">
										<ul style="margin-left: -41px;margin-top: 0px;">
											<li class="notification-menu-item" style="float:left">
						                		<fmt:message key="profile" />: <%=user.getResourceprofiles().getProfileName()%>${user.performingorg eq null?"":" - "}${user.performingorg eq null?"":user.performingorg.name}
						                	</li>
						                	<li class="notification-menu-item" style="float:left">
						                		&nbsp;|&nbsp;<a href="javascript:selectRol();"><fmt:message key="msg.info.switch"/></a>
						                	</li>
										</ul>
					                </c:if>
								</td>
								<td align="right" width="50%" class="login_info">
									<c:if test="${user != null}">
										<c:choose>
											<c:when test="<%=SettingUtil.getString(settings ,Settings.SETTING_LOGIN_TYPE,Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_BBDD)
												|| SettingUtil.getString(settings ,Settings.SETTING_LOGIN_TYPE,Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_LDAP)
												|| SettingUtil.getString(settings ,Settings.SETTING_LOGIN_TYPE,Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_MIXED)%>">
		
											    <li class="notification-menu-item">
											    	|<a href="login?a=<%=LoginServlet.LOGOFF%>" style="padding-left: 5px;"><fmt:message key="logoff"/></a>
											    </li>
											</c:when>
											<c:when test="<%=(SettingUtil.getString(settings ,Settings.SETTING_LOGIN_TYPE,Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL)
												|| SettingUtil.getString(settings ,Settings.SETTING_LOGIN_TYPE,Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL_SEARCH_LDAP))
												&& ValidateUtil.isNotNull(SettingUtil.getString(settings ,Settings.SETTING_LOGOFF_URL, StringPool.BLANK))%>">
		
												<li class="notification-menu-item">
													|<a href="<%=SettingUtil.getString(settings, Settings.SETTING_LOGOFF_URL, StringPool.BLANK)%>" style="padding-left: 5px;"><fmt:message key="logoff"/></a>
												</li>
											</c:when>
										</c:choose>
									    <li class="notification-menu-item">
											<a href="<%=MyAccountServlet.REFERENCE%>" style="padding-right: 5px;"><fmt:message key="user" />: <%=user.getContact().getFullName()%></a>
										</li>
										<li class="notification-menu-item">
											<span style="padding-left: 5px;padding-right: 5px;">|</span>
										</li>
										
										<%-- Notifications --%>
									   	<li id="notificationCenter" class="notification-menu-item"><img src="images/notification/bell.png"/></li>
					                </c:if>
					            </td>
							</tr>
						</table>
					</div>
					
					<div id="header-image">
						<div id="logo-left">
							<a href="${pageContext.request.contextPath}/home">
								<c:choose>
									<c:when test="<%=SettingUtil.getString(settings ,Settings.SETTING_LOGO,null) == null%>">
										<img src="<%=Settings.URL_LOGO%>" />
									</c:when>
									<c:otherwise>
										<img src="imageData/<%=SettingUtil.getString(settings ,Settings.SETTING_LOGO,null)%>" />
									</c:otherwise>
								</c:choose>

							</a>
						</div>
						<div id="logo-right">
							<a href="${pageContext.request.contextPath}/home">
								<img src="images/logo_miniTalaia.jpg" />
							</a>
						</div>
					</div>
					
					<div id="menu">
						<c:if test="${user != null }">
							<c:choose>
								<c:when test="<%=Settings.PERFORMANCE_COMPRESS_JS %>">
									<g:compress>
										<jsp:include page="common/menu.jsp" flush="true">
											<jsp:param name="idProfile" value="<%=idProfile %>" />
										</jsp:include>
									</g:compress>
								</c:when>
								<c:otherwise>
									<jsp:include page="common/menu.jsp" flush="true">
										<jsp:param name="idProfile" value="<%=idProfile %>" />
									</jsp:include>
								</c:otherwise>
							</c:choose>
						</c:if>
					</div>
					
				</div>
				
				<div id="container" class="left">
					<div class="tit_pagina" style="padding:5px 5px 20px;">${title }</div>
						<c:choose>
							<c:when test="<%=Settings.PERFORMANCE_COMPRESS_JS %>">
								<g:compress>
									<jsp:include page="common/messages.jsp" flush="true" />
								</g:compress>
							</c:when>
							<c:otherwise>
								<jsp:include page="common/messages.jsp" flush="true" />
							</c:otherwise>
						</c:choose>
					<c:if test="<%=!ValidateUtil.isNull(formulario)%>">
						<%--***********************************  CONTENT  **********************************--%>
						<c:choose>
							<c:when test="<%=Settings.PERFORMANCE_COMPRESS_JS %>">
								<g:compress>
									<jsp:include page="<%=formulario%>" flush="true" />
								</g:compress>
							</c:when>
							<c:otherwise>
								<jsp:include page="<%=formulario%>" flush="true" />
							</c:otherwise>
						</c:choose>
						<%--**********************************************************************************--%>
					</c:if>
				</div>
				<c:if test="${user != null}">
					<jsp:include page="common/navigate.jsp" flush="true" />
				</c:if>
				<div id="footer">
					<c:choose>
						<c:when test="<%=Settings.PERFORMANCE_COMPRESS_JS %>">
							<g:compress>
								<jsp:include page="common/footer.jsp" flush="true" />
								<jsp:include page="common/about_popup.jsp" flush="true" />
								<c:if test="${user != null }">
									<jsp:include page="common/docs_and_manuals_popup.jsp" flush="true" />
								</c:if>
							</g:compress>
						</c:when>
						<c:otherwise>
							<jsp:include page="common/footer.jsp" flush="true" />
							<jsp:include page="common/about_popup.jsp" flush="true" />
							<c:if test="${user != null }">
								<jsp:include page="common/docs_and_manuals_popup.jsp" flush="true" />
							</c:if>
						</c:otherwise>
					</c:choose>
				</div>
			</div> <%-- end wrapper --%>
		</div><%-- end page-wrap --%>
	</body>
</html>
<c:if test="${not empty pluginsLoad }">
	<script>
		var pluginAjax 	= new AjaxCall('<%=PluginServlet.REFERENCE%>','<fmt:message key="error"/>');
	</script>
</c:if>

<c:forEach var="load" items="${pluginsLoad }">
	<script>
		readyMethods.add(function() {
			loadPlugin(pluginAjax, '${load.pluginName }', '${load.selector }', '${load.template }', '${load.typeModification }', '${load.forms}');
		});
	</script>
</c:forEach>

</compress:html>