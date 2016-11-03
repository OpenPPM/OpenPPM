<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>

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
  ~ File: new_customer_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:44
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="error" var="fmt_error" />

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="customer_name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtTypeRequired">
	<fmt:param><b><fmt:message key="customer_type"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var customerValidator;

var projectAjax = new AjaxCall('<%=ProjectServlet.REFERENCE%>','<fmt:message key="error" />');

function newCustomerPopup() {
	document.forms['frm_newcustomer'].reset();
	$('#new-customer-popup').dialog('open');
}

function saveCustomer() {
	var f = document.forms["frm_newcustomer"];
	
	if (customerValidator.form()) {
		
		projectAjax.call($('#frm_newcustomer').serialize(), function(data) {
			// Insert into customers select and select it
			$('#<%=Project.CUSTOMER %>').append(
		        $('<option selected></option>').val(data.id).html(f.customer_name.value)
		    );

			f.reset();
			
			$('#new-customer-popup').dialog('close');
		});
	} 
	return false;
}

readyMethods.add(function() {
	$('div#new-customer-popup').dialog({ 
		autoOpen: false, 
		modal: true, 
		width: 450, 
		minWidth: 450, 
		resizable: false,
		open: function(event, ui) { customerValidator.resetForm(); }
	});
	
	//Validate required fields
	customerValidator = $("#frm_newcustomer").validate({
		errorContainer: 'div#newcustomer-errors',
		errorLabelContainer: 'div#newcustomer-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#newcustomer-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			customer_name: "required",
			customer_type: "required"
		},
		messages: {
			customer_name: { required: '${fmtNameRequired}' },
			customer_type: { required: '${fmtTypeRequired}' }
		}
	});
});
//-->
</script>

<div id="new-customer-popup" class="popup">

	<div id="newcustomer-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="newcustomer-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_newcustomer" id="frm_newcustomer" action="project">
		<input type="hidden" name="accion" value="<%=ProjectServlet.JX_SAVE_CUSTOMER %>" />
	
		<fieldset>
			<legend><fmt:message key="new_customer"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left"><fmt:message key="customer_name"/>&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="customer_name" class="campo" maxlength="50" />
					</td>
				</tr>
				<tr>
					<th class="left"><fmt:message key="customer_type"/>&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<select id="customer_type" name="customer_type" class="campo">
							<option value=""><fmt:message key="select_opt" /></option>
							<c:forEach var="type" items="${customertypes }">
								<option value="${type.idCustomerType }">${type.name }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveCustomer(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>