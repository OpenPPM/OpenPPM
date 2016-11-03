<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

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
  ~ File: new_iwo_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="error" var="fmt_error" />
<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtValueRequired">
	<fmt:param><b><fmt:message key="iwo.value"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtDescRequired">
	<fmt:param><b><fmt:message key="iwo.desc"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var iwoValidator;

function saveIwo() {
	var f = document.forms["frm_iwo"];

	//Validate required fields
	if (iwoValidator.form()) {
		// Save IWO by Ajax Call
		var params = {
			accion: "<%=ProjectPlanServlet.JX_SAVE_IWO%>",
			id: document.forms["frm_project"].id.value,
			iwo_id: f.iwo_id.value,
			planned: f.iwo_planned.value,
			desc: f.iwo_desc.value
		};
		
		planAjax.call(params, function (data) {
			var dataRow = [
	              	data.id,
			      	toCurrency(data.planned),
			      	escape(data.desc), 
					'<nobr><img onclick="editIWO(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
					'&nbsp&nbsp;&nbsp;&nbsp;&nbsp;'+
					'<img onclick="deleteIWO(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
				];
			
			if (f.iwo_id.value != data.id) { iwosTable.fnAddDataAndSelect(dataRow); }
			else { iwosTable.fnUpdateAndSelect(dataRow); }
					
			f.reset();
			$('#iwo-popup').dialog('close');
		});
	} 
}

readyMethods.add(function() {
	$('div#iwo-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false,
		open: function(event, ui) { iwoValidator.resetForm(); }
	});

	iwoValidator = $("#frm_iwo").validate({
		errorContainer: 'div#iwo-errors',
		errorLabelContainer: 'div#iwo-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#iwo-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			iwo_desc: { required: true },
			iwo_planned: { required: true }
		},
		messages: {
			iwo_desc: { required: '${fmtDescRequired}' },
			iwo_planned: { required: '${fmtValueRequired}' }
		}
	});

});
//-->
</script>

<div id="iwo-popup" class="popup">

	<div id="iwo-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="iwo-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_iwo" id="frm_iwo" action="<%=ProjectPlanServlet.REFERENCE%>" >
		<input type="hidden" name="iwo_id" class="campo" />
		<fieldset>
			<legend><fmt:message key="new_iwo"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="30%"><fmt:message key="iwo.value"/>&nbsp;*</th>
					<th class="left" width="70%">&nbsp;</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="iwo_planned" id="iwo_planned" class="campo importe" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<tr>	
					<th class="left" colspan="2"><fmt:message key="iwo.desc"/>&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="2">
						<input type="text" name="iwo_desc" id="iwo_desc" class="campo" maxlength="40" />
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveIwo(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>