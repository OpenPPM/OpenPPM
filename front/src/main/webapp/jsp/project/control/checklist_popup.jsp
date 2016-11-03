<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Wbsnode"%>
<%@page import="es.sm2.openppm.core.model.impl.Checklist"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

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
  ~ File: checklist_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="maintenance.error_msg_a" var="fmtCompleteRequired">
	<fmt:param><b><fmt:message key="checklists.percentage_complete"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtWbsRequired">
	<fmt:param><b><fmt:message key="wbs.wbs"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtCodeRequired">
	<fmt:param><b><fmt:message key="checklists.code"/></b></fmt:param>
</fmt:message>

<fmt:message key="msg.confirm_delete" var="fmtChecklistDeleteMsg">
	<fmt:param><fmt:message key="deliverable"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="fmtChecklistDeleteTitle">
	<fmt:param><fmt:message key="deliverable"/></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtDateFormat">
	<fmt:param><b><fmt:message key="date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.out_of_range" var="fmtCompleteOutOfRange">
	<fmt:param><b><fmt:message key="checklists.percentage_complete"/></b></fmt:param>
	<fmt:param>0 - 100</fmt:param>
</fmt:message>
<script type="text/javascript">
<!--
var validatorChecklist;

function saveCheklist() {
	
	if (validatorChecklist.form()) {
		
		controlAjax.call($("#frm_checklist").serialize(),function(data) {
			
			var f = document.frm_checklist;
			
			var dataRow = [
			        data.<%=Checklist.IDCHECKLIST%>,
			        f.<%=Wbsnode.IDWBSNODE%>.value,
			        escape($(f.<%=Wbsnode.IDWBSNODE%>).find('option:selected').text()),
			        escape(f.<%=Checklist.CODE%>.value),
			        escape(f.<%=Checklist.NAME%>.value),
			        escape(f.<%=Checklist.DESCRIPTION%>.value),
			        f.<%=Checklist.PERCENTAGECOMPLETE%>.value,
			        data.<%=Checklist.ACTUALIZATIONDATE%>,
			        escape(f.<%=Checklist.COMMENTS%>.value),
			        '<img onclick="editChecklist(this)" class="link" src="images/view.png" title="<fmt:message key="view"/>"/>'
				];
			
			if (f.<%=Checklist.IDCHECKLIST%>.value == '') { checklistTable.fnAddDataAndSelect(dataRow); }
			else { checklistTable.fnUpdateAndSelect(dataRow); }
				
			f.reset();
			$('div#checklist-popup').dialog('close');
		});
	}
}

function addChecklist() {
	var f = document.frm_checklist;
	f.reset();
	f.<%=Checklist.IDCHECKLIST%>.value = "";
	$(f.<%=Checklist.DESCRIPTION%>).text('');
	
	$('div#checklist-popup').dialog('open');
}

function editChecklist(element) {
	var f = document.forms["frm_checklist"];
	f.reset();
	
	var aData = checklistTable.fnGetData( element.parentNode.parentNode );
	
	f.<%=Checklist.IDCHECKLIST%>.value			= aData[0];
	f.<%=Wbsnode.IDWBSNODE%>.value				= aData[1];
	f.<%=Checklist.CODE%>.value					= unEscape(aData[3]);
	f.<%=Checklist.NAME%>.value					= unEscape(aData[4]);
	$(f.<%=Checklist.DESCRIPTION%>).text(unEscape(aData[5]));
	f.<%=Checklist.PERCENTAGECOMPLETE%>.value	= aData[6];
	f.<%=Checklist.ACTUALIZATIONDATE%>.value	= aData[7];
	f.<%=Checklist.COMMENTS%>.value				= unEscape(aData[8]);
	
	$('div#checklist-popup').dialog('open');
}

readyMethods.add(function() {

	$('div#checklist-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 400, 
		resizable: false,
		open: function(event, ui) { validatorChecklist.resetForm(); }
	});

	//Validate required fields
	validatorChecklist = $("#frm_checklist").validate({
		errorContainer: 'div#checklist-errors',
		errorLabelContainer: 'div#checklist-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#checklist-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Wbsnode.IDWBSNODE%>: { required: true },
			<%=Checklist.PERCENTAGECOMPLETE%>: { required: true, range: [0,100] },
			<%=Checklist.DESCRIPTION%>: { maxlength: 1500 },
			<%=Checklist.COMMENTS%>: { maxlength: 1500 },
			<%=Checklist.NAME%>: { maxlength: 50 },
			<%=Checklist.ACTUALIZATIONDATE%>: { date: true },
			<%=Checklist.CODE%>: { required: true }
		},
		messages: {
			<%=Wbsnode.IDWBSNODE%>: { required: '${fmtWbsRequired}' },
			<%=Checklist.PERCENTAGECOMPLETE%>: { required: '${fmtCompleteRequired}', range: '${fmtCompleteOutOfRange}' },
			<%=Checklist.ACTUALIZATIONDATE%>: { date: '${fmtDateFormat}' },
			<%=Checklist.CODE%>: { required: '${fmtCodeRequired}' }
		}
	});
	var dates = $(document.frm_checklist.<%=Checklist.ACTUALIZATIONDATE%>).datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function() {
			if (validatorChecklist.numberOfInvalids() > 0) validatorChecklist.form();
		}
	});
});
//-->
</script>

<div id="checklist-popup" class="popup">

	<div id="checklist-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="checklist-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_checklist" id="frm_checklist" action="<%=ProjectControlServlet.REFERENCE%>" >
		<input type="hidden" name="accion" value="<%=ProjectControlServlet.JX_UPDATE_CHECKLIST %>"/>
		<input type="hidden" name="<%=Checklist.IDCHECKLIST %>" />
		
		<fieldset>
			<legend><fmt:message key="deliverable" /></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th colspan="2"><fmt:message key="wbs.wbs" />&nbsp;*</th>
					<th class="left" width="33%"><fmt:message key="checklists.code" />&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="2" width="80%">
						<select class="campo" name="<%=Wbsnode.IDWBSNODE%>">
							<c:forEach var="node" items="${wbsnodes }">
								<option value="${node.idWbsnode }">${node.code} ${node.name }</option>
							</c:forEach>
						</select>
					</td>
					<td width="20%"><input type="text" name="<%=Checklist.CODE%>" class="campo" maxlength="10" style="width:90%;"/></td>
				</tr>
				<tr>
					<th class="left" width="54%"><fmt:message key="checklists.name" /></th>
					<th class="left"><fmt:message key="date" />&nbsp;</th>
					<th class="left"><fmt:message key="checklists.percentage_complete" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="<%=Checklist.NAME%>" class="campo" maxlength="50"/></td>
					<td><input type="text" name="<%=Checklist.ACTUALIZATIONDATE%>" class="campo fecha" maxlength="10"/></td>
					<td><input type="text" name="<%=Checklist.PERCENTAGECOMPLETE%>" class="campo" maxlength="3" style="width:90%;"/></td>
				</tr>
				<tr>
					<th class="left" colspan="3"><fmt:message key="checklists.desc" /></th>
				</tr>
				<tr>
					<td colspan="3">
						<textarea class="campo" name="<%=Checklist.DESCRIPTION%>" style="width: 98%" rows="8" maxlength="1500" ></textarea>
					</td>
				</tr>
				<tr>
					<th class="left" colspan="3"><fmt:message key="checklists.comments" /></th>
				</tr>
				<tr>
					<td colspan="3">
						<textarea class="campo" name="<%=Checklist.COMMENTS%>" style="width: 98%" rows="8" maxlength="1500"></textarea>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveCheklist(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>