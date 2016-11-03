<%@page import="es.sm2.openppm.core.model.impl.Businessdriverset"%>
<%@page import="es.sm2.openppm.core.model.impl.Businessdriver"%>
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
  ~ File: business_driver_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtSetRequired">
	<fmt:param><b><fmt:message key="business_driver_set"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="business_driver.name"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
// GLOBAL VARS
var businessDriverValidator;

// OBJECTS
var businessDriverPopup = {
		initialize: function() {
			$('div#businessDriverPopup').dialog({
				autoOpen: false, 
				modal: true, 
				width: 500, 
				minWidth: 500, 
				resizable: false,
				open: function(event, ui) { businessDriverValidator.resetForm(); }
			});
		},
		open: function () {
			$('div#businessDriverPopup').dialog('open');
		},
		close: function() {
			$('div#businessDriverPopup').dialog('close');
		},
		formValidator: function() {
			return  $("#businessDriverPopupForm").validate({
				errorContainer: 'div#businessDriver-errors',
				errorLabelContainer: 'div#businessDriver-errors ol',
				wrapper: 'li',
				showErrors: function(errorMap, errorList) {
					$('span#businessDriver-numerrors').html(this.numberOfInvalids());
					this.defaultShowErrors();
				},
				rules: {
					<%= Businessdriver.NAME %>: "required",
					<%= Businessdriverset.IDBUSINESSDRIVERSET %>: "required"
				},
				messages: {
					<%= Businessdriver.NAME %>: { required: '${fmtNameRequired}' },
					<%= Businessdriverset.IDBUSINESSDRIVERSET %>: { required: '${fmtSetRequired}' }
				}
			});
		},
		save: function(){
			if (businessDriverValidator.form()) {
				businessDriver.save();
			}
		},
		formReset: function(form) {
			form.reset();
			// los hidden se tienen que resetear a mano
			form.<%= Businessdriver.IDBUSINESSDRIVER %>.value = '';
		}
};

readyMethods.add(function() {
	businessDriverPopup.initialize();
	
	businessDriverValidator = businessDriverPopup.formValidator();
});

</script>

<div id="businessDriverPopup" class="popup">

	<div id="businessDriver-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="businessDriver-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="businessDriverPopupForm" id="businessDriverPopupForm" method="post" action="<%=MaintenanceServlet.REFERENCE%>" >
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="idManten" value="<%= Constants.MANT_BUSINESS_DRIVER %>" />
		<input type="hidden" name="<%= Businessdriver.IDBUSINESSDRIVER %>" id="<%= Businessdriver.IDBUSINESSDRIVER %>" value="" />
		
		<fieldset>
			<legend><fmt:message key="business_driver"/></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="20%"><fmt:message key="business_driver.code"/></th>
					<th class="left" width="80%"><fmt:message key="business_driver.name"/>*</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="<%= Businessdriver.CODE %>" class="campo" maxlength="20" style="width: 103px;" />
					</td>
					<td>
						<input type="text" name="<%= Businessdriver.NAME %>" class="campo" maxlength="50" />
					</td>
					
				</tr>
				<tr>
					<th class="left" colspan="2"><fmt:message key="business_driver_set"/>*</th>
				</tr>
				<tr>
					<td colspan="2">
						<select name="<%= Businessdriverset.IDBUSINESSDRIVERSET %>" id="<%= Businessdriverset.IDBUSINESSDRIVERSET %>" class="campo" style=" width: 430px;margin-right: 5px;">
							<option value=""><fmt:message key="select_opt" /></option>
							<c:forEach var="businessdriverset" items="${businessdriversets}">
								<option value="${businessdriverset.idBusinessDriverSet}">${businessdriverset.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	
    	<div class="popButtons">
   			<input type="submit" class="boton" onclick="businessDriverPopup.save(); return false;" value="<fmt:message key="save" />" />
			<a href="javascript:businessDriverPopup.close();" class="boton"><fmt:message key="close" /></a>
   		</div>
    		
    </form>
</div>