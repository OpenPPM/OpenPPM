<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.servlets.AdministrationServlet"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings.SettingType"%>
<%@page import="java.util.HashMap"%>
<%@ page import="es.sm2.openppm.core.logic.setting.NotificationType" %>
<%@ page import="es.sm2.openppm.core.logic.setting.EmailType" %>
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
  ~ File: email_settings.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:26
  --%>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtValueRequired">
	<fmt:param><b><fmt:message key="setting.value"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtServerRequired">
	<fmt:param><b><fmt:message key="<%= Settings.SETTING_MAIL_SMTP_HOST %>"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtPortRequired">
	<fmt:param><b><fmt:message key="<%= Settings.SETTING_MAIL_SMTP_PORT %>"/></b></fmt:param>
</fmt:message>


<script>
var emailValidator;

readyMethods.add(function() {

	emailValidator = $("#frm_mail_settings").validate({
		errorContainer: 'div#email-errors',
		errorLabelContainer: 'div#email-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#general-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			"<%= Settings.SETTING_MAIL_SMTP_HOST %>": { required: true }
		},
		messages: {
			"<%= Settings.SETTING_MAIL_SMTP_HOST %>": '${fmtServerRequired}'
		}
		
	});

	$('#saveEmail').on('click', function() {
		var f = document.forms.frm_mail_settings;
		if ($("#mailNotification").prop("checked")) {
			if (emailValidator.form()) {
				loadingPopup();
				f.submit();
			}
		}
		else {
			loadingPopup();
			f.submit();
		}
	});
	
	var optsSpinner = {
		decimals:0, 
		min:0,
		max:365
	};
	
	$('#spinnerStatusReport').spinner(optsSpinner);

    // Select language
    $('#<%= SettingType.LOCALE.getName() %>').change(function() {

        $this = $(this);
        $this.attr('class','campo flag '+($this.val() ==''?'${defaultLocale}':$this.val()));
    });
});
</script>

<form name="frm_mail_settings" id="frm_mail_settings" action="<%=AdministrationServlet.REFERENCE%>" method="post">
	<input type="hidden" name="accion" value="<%=AdministrationServlet.SAVE_MAIL_SETTINGS%>" />
	
	<div id="email-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="email-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	
	<fmt:message key="setting.mail.configuration" var="titleConfiguration"/>
	<visual:panel title="${titleConfiguration}">
		<table width="100%" cellpadding="5px" class="settingTable">
			<tr>
				<td width="3%"><img class="btitle" title="<fmt:message key="setting.mail.smtp.host_info"/>" src="images/info.png"></td>
				<th width="25%"><fmt:message key="<%=Settings.SETTING_MAIL_SMTP_HOST%>"/></th>
				<td width="20%"><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_MAIL_SMTP_HOST%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_HOST, Settings.DEFAULT_MAIL_SMTP_HOST) %>" /></td>
				
				<td width="4%">&nbsp;</td>
				
				<td width="3%"><img class="btitle" title="<fmt:message key="setting.mail.smtp.port_info"/>" src="images/info.png"></td>				
				<th width="25%"><fmt:message key="<%=Settings.SETTING_MAIL_SMTP_PORT%>"/></th>
				<td width="20%"><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_MAIL_SMTP_PORT%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_PORT, Settings.DEFAULT_MAIL_SMTP_PORT) %>" /></td>
			</tr>
			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.mail.smtp.user_info"/>" src="images/info.png"></td>			
				<th><fmt:message key="<%=Settings.SETTING_MAIL_SMTP_USER %>"/></th>
				<td><input maxlength="200" type="text" class="campo" name="<%=Settings.SETTING_MAIL_SMTP_USER%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_USER, Settings.DEFAULT_MAIL_SMTP_USER) %>" /></td>
				
				<td>&nbsp;</td>		
				
				<td><img class="btitle" title="<fmt:message key="setting.mail.smtp.pass_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_MAIL_SMTP_PASS %>"/></th>
				<td><input maxlength="200" type="password" class="campo" name="<%=Settings.SETTING_MAIL_SMTP_PASS%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_PASS, Settings.DEFAULT_MAIL_SMTP_PASS) %>" /></td>
			</tr>		
						
			<tr>
				<td><img class="btitle" title="<fmt:message key="setting.mail.smtp.no_reply_info"/>" src="images/info.png"></td>				
				<th><fmt:message key="<%=Settings.SETTING_MAIL_SMTP_NO_REPLY %>"/></th>			
				<td><input maxlength="200" type="text" class="campo email" name="<%=Settings.SETTING_MAIL_SMTP_NO_REPLY%>" value="<%=SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_NO_REPLY, Settings.DEFAULT_MAIL_SMTP_NO_REPLY) %>" /></td>
	
				<td>&nbsp;</td>
	
				<td><img class="btitle" title="<fmt:message key="setting.mail.smtp.tls_info"/>" src="images/info.png"></td>
				<th><fmt:message key="<%=Settings.SETTING_MAIL_SMTP_TLS%>"/></th>
				<td><input type="checkbox" class="campo" name="<%=Settings.SETTING_MAIL_SMTP_TLS%>" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_MAIL_SMTP_TLS, Settings.DEFAULT_MAIL_SMTP_TLS).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>
			</tr>

            <%-- Email settings --%>
            <c:if test="<%=SettingUtil.getBoolean(settings, Settings.SHOW_EMAIL_SIGNATURE_CONFIGURATION, Settings.DEFAULT_SHOW_EMAIL_SIGNATURE_CONFIGURATION)%>">
                <visual:settings startColumn="1" numberOfColumns="2" fields="<%= EmailType.values() %>" />
            </c:if>
		</table>
	</visual:panel>
	
	<%-- Notifications --%>
	<fmt:message key="setting.mail.notifications" var="titleNotifications"/>
	<visual:panel title="${titleNotifications}">
		<table width="100%" cellpadding="5px" class="settingTable">
		
			<tr>
				<td width="3%"><img class="btitle" title="<fmt:message key="setting.mail.notification_info"/>" src="images/info.png"></td>		
				<th width="25%"><fmt:message key="<%=Settings.SETTING_MAIL_NOTIFICATIONS %>"/></th>
				<td width="20%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_MAIL_NOTIFICATIONS%>" id="mailNotification" value="true"
					<%=SettingUtil.getString(settings, Settings.SETTING_MAIL_NOTIFICATIONS, Settings.DEFAULT_MAIL_NOTIFICATIONS).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
				</td>		
						
				<td width="4%">&nbsp;</td>

				<td width="3%"><img class="btitle" title="<fmt:message key="setting.mail.notification_status_report_info"/>" src="images/info.png"></td>
				<th width="25%"><fmt:message key="<%=Settings.SETTING_MAIL_STATUS_REPORT %>"/></th>
				<td width="20%"><input type="checkbox" class="campo" name="<%=Settings.SETTING_MAIL_STATUS_REPORT%>" value="true" style="float:left"
					<%=SettingUtil.getString(settings, Settings.SETTING_MAIL_STATUS_REPORT, Settings.DEFAULT_MAIL_STATUS_REPORT).equals(String.valueOf(Constants.SELECTED)) ? "checked" : ""%>/>
					
					<c:set var="dayStatusReport"><%= SettingUtil.getString(settings, Settings.SETTING_MAIL_DAYS_STATUS_REPORT, Settings.DEFAULT_MAIL_DAYS_STATUS_REPORT) %></c:set>
					<div style="position:relative;float:left;margin-right: 2px;"><fmt:message key="in"/>:&nbsp;</div> 
					<div style="float:left;">
						<input type="text" class="campo" maxlength="3" id="spinnerStatusReport" name="<%=Settings.SETTING_MAIL_DAYS_STATUS_REPORT%>" value="${dayStatusReport}" />
					</div>
					<div style="position:relative;float:left"> <fmt:message key="days"/></div>
				</td>
			</tr>

            <tr>
                <td><img class="btitle" title="<fmt:message key="<%= SettingType.LOCALE.getMessageInfo() %>"/>" src="images/info.png"></td>
                <th><fmt:message key="<%= SettingType.LOCALE.getMessage() %>"/></th>
                <td>
                    <c:set var="selectedLanguage"><%=SettingUtil.getString(settings, SettingType.LOCALE.getName(), SettingType.LOCALE.getDefaultValue()) %></c:set>
                    <select class="campo flag <%=SettingUtil.getString(settings, SettingType.LOCALE.getName(), SettingType.LOCALE.getDefaultValue()) %>" name="<%= SettingType.LOCALE.getName() %>" id="<%= SettingType.LOCALE.getName() %>">
                        <c:forEach var="item" items="<%=Settings.LOCALE_AVAILABLES %>">
                            <option class="flag ${item}" value="${item}" ${selectedLanguage eq item?"selected":""}>
                                <fmt:message key="locale.${item}"/>
                            </option>
                        </c:forEach>
                    </select>
                </td>

                <td>&nbsp;</td>

                <%-- Email settings --%>
                <visual:settings startColumn="2" numberOfColumns="2" fields="<%= NotificationType.values() %>" />
            </tr>

		</table>
	</visual:panel>
	
	<div align="right" style="margin:10px;">
		<input type="button" class="boton" id="saveEmail" value="<fmt:message key="save" />"/>
	</div>
</form>