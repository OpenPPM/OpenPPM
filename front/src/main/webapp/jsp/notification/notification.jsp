<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.model.impl.Notification"%>

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
  ~ File: notification.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script language="javascript" type="text/javascript" >

var notificationPopup = {
		close: function() {
			$('#notification-popup').dialog('close');
		}	
};

readyMethods.add(function () {

	// Event dialog
	$('div#notification-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 600,
		resizable: false
	});
	
	// Event click close
	$("#closeNotification").on('click', function(){
		$('#notification-popup').dialog('close');
	});
});

</script>

<div id="notification-popup" class="popup">

	<fieldset>
		<legend><fmt:message key="notification"/></legend>
		<table cellpadding="2" cellspacing="1" style="width:100%">
			<tr>
				<th align="left" width="20%"><fmt:message key="notification.subject"/>:</th>
				<th align="right" width="35%">
					<fmt:message key="notification.send"/>:
					<span style="color: #444444;font-weight: normal;" id="sendToMail"></span>
				</th>
				<th align="right" width="45%"><fmt:message key="notification.creationdate"/>:&nbsp;<span style="color: #444444;font-weight: normal;" id="creationDate"></span></th>
			</tr>
			<tr>
				<td colspan="3">
					<span id="subject"></span>
				</td>
			</tr>
			<tr>
				<th align="left" colspan="3"><fmt:message key="notification.body"/>:</th>
			</tr>
			<tr>
				<td colspan="3">
					<span id="body"></span>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<br>
					<%-- Error message --%>
					<div id="messageError" style="display:none; margin-bottom: 10px; padding: 0pt 0.7em;" class="ui-state-highlight ui-corner-all">
						<span></span>
					</div>
				</td>
			</tr>
   		</table>
   	</fieldset>
   	<div class="popButtons">
   		<input type="button" class="boton" id="closeNotification" value="<fmt:message key="close" />" />
   	</div>
</div>
