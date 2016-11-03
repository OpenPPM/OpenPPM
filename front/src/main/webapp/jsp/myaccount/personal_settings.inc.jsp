<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.front.servlets.MyAccountServlet"%>
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
  ~ File: personal_settings.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="maintenance.error_msg_a" var="fmtFullNameRequired">
	<fmt:param><b><fmt:message key="maintenance.contact.full_name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtJobTitleRequired">
	<fmt:param><b><fmt:message key="maintenance.contact.job_title"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtFileAsRequired">
	<fmt:param><b><fmt:message key="maintenance.contact.file_as"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtEmailRequired">
	<fmt:param><b><fmt:message key="maintenance.contact.email"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtPasswordRequired">
	<fmt:param><b><fmt:message key="pass"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtPasswordNewRequired">
	<fmt:param><b><fmt:message key="pass.new"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.info.mincharacters" var="fmtPasswordNewMin">
	<fmt:param><b><fmt:message key="pass.new"/></b></fmt:param>
	<fmt:param><b>5</b></fmt:param>
</fmt:message>
<fmt:message key="msg.info.mincharacters" var="fmtPasswordConfirmMin">
	<fmt:param><b><fmt:message key="pass.confirm"/></b></fmt:param>
	<fmt:param><b>5</b></fmt:param>
</fmt:message>

<c:set var="defaultLocale"><%=Settings.LOCALE_LANGUAGE_DEFAULT %>_<%=Settings.LOCALE_COUNTRY_DEFAULT %></c:set>
<script language="javascript" type="text/javascript">
var contactValidator;

function updateContact() {
	if (contactValidator.form()) {
		loadingPopup();
		document.forms["frm_my_account"].submit();
	}
}

readyMethods.add(function () {
	
	contactValidator = $("#frm_my_account").validate({
		errorContainer: 'div#contact-errors',
		errorLabelContainer: 'div#contact-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#contact-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Contact.FULLNAME %>: { required: true },
			<%=Contact.JOBTITLE %>: { required: true },
			<%=Contact.FILEAS %>: { required: true },
			<%=Contact.EMAIL %>: { required: true, email: true },
			<%=Contact.NOTES %>: { maxlength: 200 },
			password: { required: function() { return ($("#passwordNew").val() != "" || $("#passwordConfirm").val() != "");} },
			passwordNew: { required: function() { return ($("#password").val() != ""); }, minlength: 5 },
			passwordConfirm: { equalTo: "#passwordNew", minlength: 5  }
		},
		messages: {
			<%=Contact.FULLNAME %>: { required: '${fmtFullNameRequired}' },
			<%=Contact.JOBTITLE %>: { required: '${fmtJobTitleRequired}' },
			<%=Contact.FILEAS %>: { required: '${fmtFileAsRequired}' },
			<%=Contact.EMAIL %>: { required: '${fmtEmailRequired}', email: '<fmt:message key="maintenance.error_email"/>' },
			password: { required: '<fmt:message key="pass.required"/>' },
			passwordNew: { required: '${fmtPasswordNewRequired}', minlength: '${fmtPasswordNewMin}' },
			passwordConfirm: { equalTo: '<fmt:message key="pass.confirm.please"/>', minlength: '${fmtPasswordConfirmMin}'  }
		}
	});
	
	$('#locale').change(function() {
		$this = $(this); 
		$this.attr('class','campo flag '+($this.val() ==''?'${defaultLocale}':$this.val()));
	});
});

</script>

<form id="frm_my_account" name="frm_my_account" method="post" action="<%=MyAccountServlet.REFERENCE%>">
	<input type="hidden" name="accion" value="<%=MyAccountServlet.UPDATE_MY_ACCOUNT %>" />
	
	<div id="contact-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="contact-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	<table border="0" cellpadding="2" cellspacing="1" width="100%">
		<tr>
			 <th width="33%"><fmt:message key="maintenance.contact.full_name" />&nbsp;*</th>
			 <th width="33%"><fmt:message key="maintenance.contact.file_as" />&nbsp;*</th>
			 <th width="34%"><fmt:message key="maintenance.contact.job_title" />&nbsp;*</th>
		</tr>
		<tr>
			<td><input type="text" name="<%=Contact.FULLNAME %>" class="campo" maxlength="50" value="${tl:escape(user.contact.fullName)}"/></td>
			<td><input type="text" name="<%=Contact.FILEAS %>" class="campo" maxlength="60" value="${tl:escape(user.contact.fileAs)}"/></td>
			<td><input type="text" name="<%=Contact.JOBTITLE %>" class="campo" maxlength="50" value="${tl:escape(user.contact.jobTitle)}"/></td>
		</tr>
		<tr>
			 <th colspan="2"><fmt:message key="maintenance.contact.business_address" /></th>
			 <th><fmt:message key="maintenance.contact.business_phone" /></th>
		</tr>
		<tr>
			<td colspan="2">
				<input type="text" name="<%=Contact.BUSINESSADDRESS %>" class="campo" maxlength="200" value="${tl:escape(user.contact.businessAddress)}"/>
			</td>
			<td>
				<input type="text" name="<%=Contact.BUSINESSPHONE %>" class="campo" maxlength="12" value="${tl:escape(user.contact.businessPhone)}"/>
			</td>
		</tr>
		<tr>
			 <th><fmt:message key="maintenance.contact.mobile_phone" /></th>
			 <th><fmt:message key="maintenance.contact.email" />&nbsp;*</th>
			<th><fmt:message key="maintenance.contact.username" /></th>
		</tr>
		<tr>
			<td>
				<input type="text" name="<%=Contact.MOBILEPHONE %>" class="campo" maxlength="12" value="${tl:escape(user.contact.mobilePhone)}"/>
			</td>
			<td>
				<input type="text" name="<%=Contact.EMAIL %>" class="campo" maxlength="50" value="${tl:escape(user.contact.email)}"/>
			</td>
			<td>
				<input type="text" class="campo" value="${tl:escape(username) }" disabled/>
			</td>
		</tr>
		<c:if test="<%=Constants.SECURITY_LOGIN_BBDD.equals(SettingUtil.getString(SettingUtil.getSettings(request), Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE))
			|| Constants.SECURITY_LOGIN_MIXED.equals(SettingUtil.getString(SettingUtil.getSettings(request), Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE)) %>">
		<tr>
			 <th><fmt:message key="pass.actual" /></th>
			 <th><fmt:message key="pass.new" /></th>
			<th><fmt:message key="pass.confirm" /></th>
		</tr>
		<tr>
			<td>
				<input type="password" name="password" id="password" class="campo" maxlength="20"/>
			</td>
			<td>
				<input type="password" name="passwordNew" id="passwordNew" class="campo" maxlength="20"/>
			</td>
			<td>
				<input type="password" name="passwordConfirm" id="passwordConfirm" class="campo" maxlength="20"/>
			</td>
		</tr>
		</c:if>
		<c:if test="<%=Settings.LOCALE_AVAILABLES.length > 1 %>">
		<tr>
			<th><fmt:message key="language"/>:&nbsp;</th>
		</tr>
		<tr>
			<td>
				<select class="campo flag ${tl:isNull(user.contact.locale)?defaultLocale:user.contact.locale}" name="locale" id="locale">
					<option selected="selected" class="flag ${defaultLocale }" value=""><fmt:message key="default"/></option>
					<c:forEach var="item" items="<%=Settings.LOCALE_AVAILABLES %>">
						<option class="flag ${item}" value="${item }" ${user.contact.locale eq item?"selected":""}>
							<fmt:message key="locale.${item }"/>
						</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		</c:if>			
		<tr>
			 <th colspan="3"><fmt:message key="maintenance.contact.notes" /></th>
		</tr>
		<tr>
			<td colspan="3">
				<textarea name="notes" name="<%=Contact.NOTES %>" class="campo" style="width:98%;">${tl:escape(user.contact.notes)}</textarea>
			</td>
		</tr>
		<tr><td colspan="3">&nbsp;</td></tr>
		<tr>
			<td colspan="3" class="right">
				<a href="javascript:updateContact();" class="boton"><fmt:message key="save" /></a>
			</td>
		</tr>
  	</table>
</form>
