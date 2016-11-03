<%@page import="es.sm2.openppm.front.utils.RequestUtil"%>
<%@page import="es.sm2.openppm.core.common.Configurations"%>
<%@page import="es.sm2.openppm.utils.functions.ValidateUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.utils.StringPool"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
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
  ~ File: select_rol.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:27
  --%>

<compress:html
	enabled="<%= Settings.PERFORMANCE_COMPRESS_HTML%>"
	compressJavaScript="false"
	removeComments="true"><%@page import="es.sm2.openppm.front.servlets.LoginServlet"%>
<%@page import="es.sm2.openppm.front.servlets.HomeServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://granule.com/tags" prefix="g" %>

<html>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:message key="error" var="fmt_error" />

<head>
<title><fmt:message key="title_two"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<%-- Include All JS and CSS --%>
<jsp:include page="common/head_includes.jsp" flush="true">
	<jsp:param value="true" name="compresJS"/>
</jsp:include>

<%-- User Settings --%>
<%HashMap<String, String> settings = SettingUtil.getSettings(request); %>

<c:choose>
	<c:when test="<%=Settings.PERFORMANCE_COMPRESS_JS %>">
		<g:compress>
			<script type="text/javascript">
			<!--
			readyMethods.add(function() {
				$("input.boton").button();
				$('#listPerfOrgs').filterSelect({'selectFilter':'listProfiles'});
				
				$('#listProfiles').val(<%= RequestUtil.getConfiguration(request, Configurations.CR_EMPLOYEE) %>);
			});
			//-->
			</script>
		</g:compress>
	</c:when>
	<c:otherwise>
		<script type="text/javascript">
		<!--
		readyMethods.add(function() {
			$("input.boton").button();
			$('#listPerfOrgs').filterSelect({'selectFilter':'listProfiles'});
			
			$('#listProfiles').val(<%= RequestUtil.getConfiguration(request, Configurations.CR_EMPLOYEE) %>);
		});
		//-->
		</script>
	</c:otherwise>
</c:choose>
</head>
<body>
	
	<div id="up-page"></div>
		
	<div id="page-wrap">
		<jsp:include page="common/infoApp.jsp" flush="true"/>
		<div id="wrapper" class="left" >
		
			<div id="header">
				<div id="login">
					<div>&nbsp;</div>                                                                                                                                                                                                                                                                                                                                       
					<div>&nbsp;</div>
					<div>&nbsp;</div>
				</div>
			
				<div id="header-image">
					<div id="logo-left">
						<a href="${pageContext.request.contextPath}/home">
							<img src="<%=Settings.URL_LOGO%>" />
						</a>
					</div>
					<div id="logo-right">
						<a href="${pageContext.request.contextPath}/home">
							<img src="images/logo_miniTalaia.jpg" />
						</a>
					</div>
				</div>
			</div>
			
			<div id="container" class="left">
				<div id="window-login">
					<c:if test="${error != null }">
						<c:set var="showError">style="display:block !important;"</c:set>
					</c:if>
					<div id="login-error" class="ui-state-error ui-corner-all leftPadding hide" ${showError }>
						<h4><fmt:message key="msg.error_login.title"/></h4>
						<p><fmt:message key="msg.error_login.message"/></p>
					</div>
					
					<c:choose>
						<c:when test="<%= Settings.PERFORMANCE_COMPRESS_JS %>">
							<g:compress>
								<jsp:include page="common/messages.jsp" flush="true" />
							</g:compress>
						</c:when>
						<c:otherwise>
							<jsp:include page="common/messages.jsp" flush="true" />
						</c:otherwise>
					</c:choose>
				
					<form name="frm_login" id="frm_login" method="post" action="<%=HomeServlet.REFERENCE%>">
						<input type="hidden" name="accion" value="<%=HomeServlet.CHOOSE_ROL%>">
						<fieldset id="choose_rol" style="width:50%; margin: 0 auto;">
							<legend><fmt:message key="msg.info.choose_profile"/></legend>
							<table align="center" width="100%">
								<tr><td colspan="2">&nbsp;</td></tr>
								<tr>
									<th style="text-align: left;" width="50%"><fmt:message key="perf_organization"/></th>
									<th style="text-align: left;" width="50%"><fmt:message key="profile"/></th>
								</tr>
								<tr>
									<td>
										<select class="campo" style="width: 95%;" name="<%=Configurations.CR_PO %>" id="listPerfOrgs">
											<c:set var="isInAll">false</c:set>
											<c:set var="poSelected" value="<%= RequestUtil.getConfiguration(request, Configurations.CR_PO) %>"/>
											<c:forEach var="emp" items="${employees }">
												<c:if test="${emp.performingorg eq null and not isInAll}">
													<c:set var="isInAll">true</c:set>
													<option value="rolPorfolio" <%= "rolPorfolio".equals(RequestUtil.getConfiguration(request, Configurations.CR_PO))?"selected":"" %>><fmt:message key="perf_organization.all"/></option>
												</c:if>
											</c:forEach>
											<c:forEach var="org" items="${organizactions }">
												<c:set var="idPerfOrg">${org.idPerfOrg }</c:set>
												<option value="${org.idPerfOrg }" ${idPerfOrg eq poSelected?"selected":""}>${org.name }</option>
											</c:forEach>
										</select>
									</td>
									<td>
										<select class="campo" style="width: 90%;" name="<%=Configurations.CR_EMPLOYEE %>" id="listProfiles">
											<c:forEach var="emp" items="${employees }">
												<option class="${(emp.performingorg eq null)?"rolPorfolio":emp.performingorg.idPerfOrg}" value="${emp.idEmployee }">${emp.resourceprofiles.profileName }</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr><td>&nbsp;</td></tr>
								<tr>
									<td colspan="2" align="center">
										<input type="submit" class="boton" style="width:auto !important" value="<fmt:message key="accept"/>"/>
										
										<c:if test="${user != null }">
											<c:choose>
												<c:when test="<%=SettingUtil.getString(settings ,Settings.SETTING_LOGIN_TYPE,Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_BBDD)
													|| SettingUtil.getString(settings ,Settings.SETTING_LOGIN_TYPE,Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_LDAP)
													|| SettingUtil.getString(settings ,Settings.SETTING_LOGIN_TYPE,Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_MIXED)%>">
													
													<a href="login?a=<%=LoginServlet.LOGOFF%>" class="boton"><fmt:message key="logoff"/></a>
												</c:when>
												<c:when test="<%=(SettingUtil.getString(settings ,Settings.SETTING_LOGIN_TYPE,Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL)
													|| SettingUtil.getString(settings ,Settings.SETTING_LOGIN_TYPE,Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL_SEARCH_LDAP))
													&& ValidateUtil.isNotNull(SettingUtil.getString(settings ,Settings.SETTING_LOGOFF_URL, StringPool.BLANK))%>">
													
													<a href="<%=SettingUtil.getString(settings ,Settings.SETTING_LOGOFF_URL, StringPool.BLANK)%>" class="boton"><fmt:message key="logoff"/></a>
												</c:when>
											</c:choose>
						                </c:if>
									</td>
								</tr>
							</table>
						</fieldset>
					</form>
				</div>
			</div>

			<div id="footer">
				<c:choose>
					<c:when test="<%=Settings.PERFORMANCE_COMPRESS_JS %>">
						<g:compress>
							<jsp:include page="common/footer.jsp" flush="true" />
							<jsp:include page="common/about_popup.jsp" flush="true" />
						</g:compress>
					</c:when>
					<c:otherwise>
						<jsp:include page="common/footer.jsp" flush="true" />
						<jsp:include page="common/about_popup.jsp" flush="true" />
					</c:otherwise>
				</c:choose>
			</div>

		</div> <%-- end wrapper --%>
	</div><%-- end page-wrap --%>

</body>
</html>
</compress:html>