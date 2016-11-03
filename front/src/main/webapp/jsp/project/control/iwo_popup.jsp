<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>

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
  ~ File: iwo_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="editIwo"><img onclick="return editIwo(this);" class="link" src="images/view.png" title="<fmt:message key="view"/>"/></c:set>
</c:if>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="msg.error.invalid_format" var="fmtIwoDateFormat">
	<fmt:param><b><fmt:message key="iwo.date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtIwoActualRequired">
	<fmt:param><b><fmt:message key="iwo.actual"/></b></fmt:param>
</fmt:message>


<script type="text/javascript">
<!--
var iwoValidator;

function saveIwo() {
	var f = document.forms["frm_iwo"];

	//Validate required fields
	if (iwoValidator.form()) {
		
		controlAjax.call($('#frm_iwo').serialize(),function(data) {
			
			var iwo = iwosTable.fnGetSelectedData();
			
			iwosTable.fnUpdateAndSelect( [
			        iwo[0],
			        iwo[1],
			        toCurrency(data.actual),
			        data.iwoDate,
			        escape(data.desc), 
			        toCurrency(data.actual),
			        '${editIwo}'
				]);
			
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
			iwo_actual: { required: true },
			iwo_date: {date: true}
		},
		messages: {
			iwo_actual: { required: '${fmtIwoActualRequired}' },
			iwo_date: {date: '${fmtIwoDateFormat}' }
		}
	});
	
	var dates = $('#iwo_date').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function() {
			if (iwoValidator.numberOfInvalids() > 0) iwoValidator.form();
		}
	});
});
//-->
</script>

<div id="iwo-popup" class="popup">

	<div id="iwo-errors" class="ui-state-error ui-corner-all hide">
		<fmt:message key="msg.error.valid_number_iwo" var="valid_number_iwo"/>
		<h4><fmt:message key="msg.error_title"/></h4>
		<ol>
			<li><label for="iwo_actual" class="error"><fmt:message key="maintenance.error_msg_a"><fmt:param value="${valid_number_iwo}" /></fmt:message></label></li>
		</ol>
	</div>

	<form name="frm_iwo" id="frm_iwo" action="<%=ProjectControlServlet.REFERENCE%>">
		<input type="hidden" name="accion" value="<%=ProjectControlServlet.JX_SAVE_IWO%>"/>
		<input type="hidden" name="iwo_id"/>
		
		<fieldset>
			<legend><fmt:message key="cost.detail"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="33%"><fmt:message key="iwo.planned"/>&nbsp;*</th>
					<th class="left" width="33%"><fmt:message key="iwo.actual"/>&nbsp;*</th>
					<th class="left" width="34%"><fmt:message key="iwo.date"/>&nbsp;</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="iwo_planned" id="iwo_planned" class="importe" readonly/>
					</td>
					<td>
						<input type="text" name="iwo_actual" id="iwo_actual" class="campo importe"/>
					</td>
					<td>
						<input type="text" name="iwo_date" id="iwo_date" class="campo fecha" />
					</td>
				</tr>
				<tr>	
					<th class="left" colspan="3"><fmt:message key="iwo.desc"/>&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="3">
						<input type="text" name="iwo_desc" id="iwo_desc" class="campo" maxlength="40" />
					</td>
				</tr>
				<tr><td colspan="3">&nbsp;</td></tr>
			</table>
    	</fieldset>
    	<div class="popButtons">
			<c:if test="${not op:isStringEquals(project.status, status_closed)}">
				<input type="submit" class="boton" onclick="saveIwo(); return false;" value="<fmt:message key="save" />" />
			</c:if>
    	</div>
    </form>
</div>