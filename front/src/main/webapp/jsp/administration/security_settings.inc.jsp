<%@page import="es.sm2.openppm.utils.StringPool"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.front.servlets.AdministrationServlet"%>
<%@ page import="es.sm2.openppm.core.logic.setting.SecuritySetting" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="Visual" prefix="visual" %>

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
  ~ File: security_settings.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:27
  --%>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<script>
var securityValidator;

var security = {
	test : function() {
		adminAjax.call({accion:"<%=AdministrationServlet.JX_TEST_LDAP %>"}, function (data) { alertUI(data.info,data.info); });
	},
	changeLogin : function() {
		
		security.loadLogin();
		
		if ($("#logoffUrl").val() == '') { security.defaultLogoff(); }
	},
	loadLogin : function() {
		
		if ($("#loginType").val() == '<%= Constants.SECURITY_LOGIN_LDAP%>'
			|| $("#loginType").val() == '<%= Constants.SECURITY_LOGIN_EXTERNAL_SEARCH_LDAP%>'
			|| $("#loginType").val() == '<%= Constants.SECURITY_LOGIN_MIXED%>') { $(".ldapSettings").show(); }
		else { $(".ldapSettings").hide(); }
		
		if ($("#loginType").val() == '<%= Constants.SECURITY_LOGIN_EXTERNAL%>'
			|| $("#loginType").val() == '<%= Constants.SECURITY_LOGIN_EXTERNAL_SEARCH_LDAP%>') { $(".logoffSettings").show(); }
		else { $(".logoffSettings").hide(); }
	},
	defaultLogoff : function() { $("#logoffUrl").val("<%=Settings.DEFAULT_LOGOFF_URL%>"); }
};
readyMethods.add(function() {

	security.loadLogin();
	
	securityValidator = $("#frm_security_settings").validate({
		errorContainer: 'div#security-errors',
		errorLabelContainer: 'div#security-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#security-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		}
	});

	$('#testSecurity').click(security.test);
	$('#defaultLogoff').click(security.defaultLogoff);
	
	$('#saveSecurity').on('click', function() {

		if (securityValidator.form()) {
			loadingPopup();
			document.forms.frm_security_settings.submit();
		}
	});
	
	$('#loginType').on('change', security.changeLogin);
});
</script>
<form name="frm_security_settings" id="frm_security_settings" action="<%=AdministrationServlet.REFERENCE%>" method="post">
	<input type="hidden" name="accion" value="<%=AdministrationServlet.SAVE_SECURITY_SETTINGS%>" />	
	
	<div id="security-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="security-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	
	<fmt:message key="setting.security.title" var="titleSecuritySettings"/>
	<visual:panel title="${titleSecuritySettings}">
		<table cellpadding="5px" class="settingTable" style="width: 100%">
			<tr>
				<td width="3%"><img class="btitle" title="<fmt:message key="setting.login.type_info"/>" src="images/info.png"></td>
				<th width="25%"><fmt:message key="<%=Settings.SETTING_LOGIN_TYPE %>"/></th>			
				<td width="20%">						
					<select name="<%=Settings.SETTING_LOGIN_TYPE %>" class="campo" style="width:100%;" id="loginType">
						<option value="<%= Constants.SECURITY_LOGIN_BBDD%>" <%=SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_BBDD) ? "selected" : ""%>>
							<fmt:message key="login.type.bbdd" />
						</option>
						<option value="<%= Constants.SECURITY_LOGIN_LDAP%>" <%=SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_LDAP) ? "selected" : ""%>>
							<fmt:message key="login.type.ldap" />
						</option>
						<option value="<%= Constants.SECURITY_LOGIN_MIXED%>" <%=SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_MIXED) ? "selected" : ""%>>
							<fmt:message key="login.type.mixed" />
						</option>
						<option value="<%= Constants.SECURITY_LOGIN_EXTERNAL%>" <%=SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL) ? "selected" : ""%>>
							<fmt:message key="login.type.external" />
						</option>
						<option value="<%= Constants.SECURITY_LOGIN_EXTERNAL_SEARCH_LDAP%>" <%=SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL_SEARCH_LDAP) ? "selected" : ""%>>
							<fmt:message key="login.type.external_search_ldap" />
						</option>
					</select>
				</td>
				
				<td>&nbsp;</td>
				
				<td class="logoffSettings"><img class="btitle" title="<fmt:message key="setting.logoff.url_info"/>" src="images/info.png"></td>
				<th class="logoffSettings"><fmt:message key="<%=Settings.SETTING_LOGOFF_URL%>"/></th>
				<td class="logoffSettings">
					<input maxlength="200" type="text" class="campo" style="width: 56%" name="<%=Settings.SETTING_LOGOFF_URL%>" id="logoffUrl" value="<%=SettingUtil.getString(settings, Settings.SETTING_LOGOFF_URL, StringPool.BLANK) %>" />
					<input type="button" class="boton" id="defaultLogoff" value="<fmt:message key="default" />"/>
				</td>
			</tr>
		</table>
	</visual:panel>
	
	<fmt:message key="setting.ldap.title" var="titleLdapSettings"/>
	<visual:panel title="${titleLdapSettings}" cssClass="ldapSettings">
		<table cellpadding="5px" class="settingTable" style="width: 100%">
			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.ldap.server_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_LDAP_SERVER%>"/></th>
				<td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_LDAP_SERVER%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_LDAP_SERVER, StringPool.BLANK) %>" /></td>
				
				<td>&nbsp;</td>
				
				<td><img class="btitle" title="<fmt:message key="setting.ldap.base_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_LDAP_BASE%>"/></th>
				<td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_LDAP_BASE%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_LDAP_BASE, StringPool.BLANK) %>" /></td>			
			</tr>
			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.ldap.port_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_LDAP_PORT%>"/></th>
				<td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_LDAP_PORT%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_LDAP_PORT, StringPool.BLANK) %>" /></td>
					
				<td>&nbsp;</td>				
				
				<td><img class="btitle" title="<fmt:message key="setting.ldap.identificator_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_LDAP_IDENTIFICATOR%>"/></th>
				<td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_LDAP_IDENTIFICATOR%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_LDAP_IDENTIFICATOR, StringPool.BLANK) %>" /></td>
			</tr>
			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.ldap.account_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_LDAP_ACCOUNT%>"/></th>
				<td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_LDAP_ACCOUNT%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_LDAP_ACCOUNT, StringPool.BLANK) %>" /></td>
				
				<td>&nbsp;</td>				
				
				<td><img class="btitle" title="<fmt:message key="setting.ldap.name_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_LDAP_NAME%>"/></th>
				<td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_LDAP_NAME%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_LDAP_NAME, StringPool.BLANK) %>" /></td>
			</tr>
			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.ldap.password_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_LDAP_PASSWORD%>"/></th>
				<td><input maxlength="200" type="password" class="campo" name="<%=Settings.SETTING_LDAP_PASSWORD%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_LDAP_PASSWORD, StringPool.BLANK) %>" /></td>
					
				<td>&nbsp;</td>				
				
				<td><img class="btitle" title="<fmt:message key="setting.ldap.lastname_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_LDAP_LASTNAME%>"/></th>
				<td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_LDAP_LASTNAME%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_LDAP_LASTNAME, StringPool.BLANK) %>" /></td>
			</tr>
			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.ldap.mail_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_LDAP_MAIL%>"/></th>
				<td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_LDAP_MAIL%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_LDAP_MAIL, StringPool.BLANK) %>" /></td>
					
				<td>&nbsp;</td>
								
				<td></td>
				<th></th>
				<td></td>
			</tr>
			<%-- Required settings settings --%>
			<visual:settings startColumn="1" numberOfColumns="2" fields="<%= SecuritySetting.values() %>" />
		</table>	
	</visual:panel>
	<div align="right" style="margin:10px;">
		<input type="button" class="boton ldapSettings" id="testSecurity" value="<fmt:message key="test.stored_security" />"/>
		<input type="button" class="boton" id="saveSecurity" value="<fmt:message key="save" />"/>
	</div>
</form>