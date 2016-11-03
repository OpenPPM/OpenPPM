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
  ~ File: login.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<compress:html
	enabled="<%= Settings.PERFORMANCE_COMPRESS_HTML%>"
	compressJavaScript="false"
	removeComments="true">
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="<%=Constants.DEF_LOCALE %>" scope="request"/>
<html>

<fmt:message key="error" var="fmt_error" />

<head>
<title><fmt:message key="title_two"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<%-- Include All JS and CSS --%>
<jsp:include page="common/head_includes.jsp" flush="true">
	<jsp:param value="false" name="compresJS"/>
</jsp:include>

<script type="text/javascript">
<!--
var profileSelected = false;

var browser = {
		
	// Name and version of browser
	nameAndVersion: function () {
		
		var browsers 		= ["chrome", "firefox", "msie"];
		var nameAndVersion 	= [];
		var regExp			= "[/ ][0-9]*";
		var string			= "";
		
		for (var i = 0; i < browsers.length; i++) {
			
			if (navigator.userAgent.toLowerCase().match(browsers[i] + regExp) != null) {
				
				string = navigator.userAgent.toLowerCase().match(browsers[i] + regExp)[0].replace(" ", "/");
				
				nameAndVersion 	= string.split('/');
				
				break;
			}
		}
		
		return nameAndVersion;
	},
	// Compatible browsers
	compatible: function() {
		
		var nameAndVersion 	= browser.nameAndVersion();
		
		if (nameAndVersion.length > 0) {
			
			if (nameAndVersion[0] == 'msie' && parseFloat(nameAndVersion[1]) < 7) {
				$("#unsupportedBrowser").show();
			}
			else if (nameAndVersion[0] == 'firefox' && parseFloat(nameAndVersion[1]) < 9) {
				$("#unsupportedBrowser").show();
			}
		}
	}
};

readyMethods.add(function() {

	browser.compatible();
    
	$("input.boton").button();
	$('#j_username').focus();
});
//-->
</script>
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
				
					<div id="unsupportedBrowser" class="ui-state-highlight ui-corner-all" style="display:none">
						<div style="text-align: center; width: 88px; margin: 10px auto 0;">
							<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
							<strong><fmt:message key="msg.info"/>: </strong>
						</div>
						<p>
							<fmt:message key="msg.unsupported_browser" />
						</p>
					</div>
				
					<c:if test="${error != null }">
						<c:set var="showError">style="display:block !important;"</c:set>
					</c:if>
					<div id="login-error" class="ui-state-error ui-corner-all hide" ${showError}>
						<h4><fmt:message key="msg.error_login.title"/></h4>
						<p>
							<c:choose>
								<c:when test="${error }"><fmt:message key="msg.error_login.message"/></c:when>
								<c:otherwise>${error }</c:otherwise>
							</c:choose>
						</p>
					</div>
					
					<c:if test="${notLogin eq null }">
						<form name="frm_login" id="frm_login" method="post" action="<%=response.encodeURL("j_security_check") %>">
							<fieldset id="login_module" style="width:30%; margin: 0 auto">
								<legend><fmt:message key="login"/></legend>
								<table align="center" width="100%">
									<tr><td>&nbsp;</td></tr>
									<tr>
										<th width="35%" class="left"><fmt:message key="user"/>&nbsp;*</th>
										<td width="65%">
											<input type="text" name="j_username" id="j_username" class="campo" value="" />
										</td>
									</tr>
									<tr>
										<th class="left"><fmt:message key="pass"/>&nbsp;*</th>
										<td>
											<input type="password" name="j_password" id="j_password" class="campo" value="" />
										</td>
									</tr>
									<tr><td>&nbsp;</td></tr>
									<tr>
										<td colspan="2" align="center">
											<input type="submit" class="boton" value="<fmt:message key="login"/>" style="width:auto !important" />
										</td>
									</tr>
								</table>
							</fieldset>
						</form>
					</c:if>
				</div>
			</div>
			
			<div id="footer">
				<jsp:include page="common/footer.jsp" flush="true" />
				<jsp:include page="common/about_popup.jsp" flush="true" />
			</div>
			
		</div> <%-- end wrapper --%>
	</div><%-- end page-wrap --%>

</body>
</html>
</compress:html>