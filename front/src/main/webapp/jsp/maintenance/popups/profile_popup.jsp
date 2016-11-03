<%--<%@page import="es.sm2.openppm.core.model.impl.profileset"%>--%>
<%--<%@page import="es.sm2.openppm.core.model.impl.profile"%>--%>
<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
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
  ~ File: profile_popup.jsp
  ~ Create User: jordi.ripoll
  ~ Create Date: 01/08/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="name"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
// GLOBAL VARS
var profileValidator;

// OBJECTS
var profilePopup = {
		initialize: function() {
			$('div#profilePopup').dialog({
				autoOpen: false, 
				modal: true, 
				width: 500, 
				minWidth: 500, 
				resizable: false,
				open: function(event, ui) { profileValidator.resetForm(); }
			});
		},
		open: function () {
			$('div#profilePopup').dialog('open');
		},
		close: function() {
			$('div#profilePopup').dialog('close');
		},
		formValidator: function() {
			return  $("#profilePopupForm").validate({
				errorContainer: 'div#profile-errors',
				errorLabelContainer: 'div#profile-errors ol',
				wrapper: 'li',
				showErrors: function(errorMap, errorList) {
					$('span#profile-numerrors').html(this.numberOfInvalids());
					this.defaultShowErrors();
				},
				rules: {
					name: "required"
				},
				messages: {
					name: { required: '${fmtNameRequired}' }
				}
			});
		},
		save: function(){
			if (profileValidator.form()) {
				profile.save();
			}
		},
		formReset: function(form) {
			form.reset();
			// los hidden se tienen que resetear a mano
			form.id.value = '';
		}
};

readyMethods.add(function() {

	profilePopup.initialize();

	profileValidator = profilePopup.formValidator();

    $("#saveProfile").on('click', profilePopup.save);

    $("#closeProfile").on('click', profilePopup.close);
});

</script>

<div id="profilePopup" class="popup">

	<div id="Profile-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="profile-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="profilePopupForm" id="profilePopupForm">
		<input type="hidden" name="id" id="id" value="" />
		<fieldset>
			<legend><fmt:message key="profile"/></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="80%"><fmt:message key="name"/>*</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="name" class="campo" maxlength="300" />
					</td>
				</tr>
				<tr>
					<th class="left" colspan="2"><fmt:message key="description"/></th>
				</tr>
				<tr>
                    <td><textarea rows="4" name="description" class="campo" style="width: 98%" maxlength="2000"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
        <div class="popButtons">
            <input type="button" class="boton" id="saveProfile" value="<fmt:message key="save" />" />
            <input type="submit" class="boton" id="closeProfile" value="<fmt:message key="close" />" />
        </div>
    </form>
</div>