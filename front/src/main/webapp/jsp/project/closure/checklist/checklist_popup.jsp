<%@page import="es.sm2.openppm.core.model.impl.Documentproject"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject.DocumentType"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectClosureServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Closurecheckproject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.core.common.Constants"%>

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

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtClosureCheckRequired">
	<fmt:param><b><fmt:message key="closure_check"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtDateRealizedFormat">
	<fmt:param><b><fmt:message key="date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var validatorCheckList;
var date;

var checkListPopup = {
		
	save: function() {
		
		if (validatorCheckList.form()) {
			
			var f = document.frm_checklist_popup;
			
		 	var params = {
	 			accion: 											"<%= ProjectClosureServlet.JX_SAVE_CLOSURE_CHECK_PROJECT %>",
	 			<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>: 	f.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>.value,
	 			<%= Project.IDPROJECT %>:							$("#id").val(),
	 			<%= Closurecheckproject.CLOSURECHECK %>:			f.<%= Closurecheckproject.CLOSURECHECK %>.value,
	 			<%= Closurecheckproject.COMMENTS %>: 				f.<%= Closurecheckproject.COMMENTS %>.value,
	 			<%= Closurecheckproject.DATEREALIZED %>:			f.<%= Closurecheckproject.DATEREALIZED %>.value,
	 			<%= Closurecheckproject.REALIZED %>: 				f.<%= Closurecheckproject.REALIZED %>.checked,
	 			<%= Closurecheckproject.MANAGER %>: 				f.<%= Closurecheckproject.MANAGER %>.value,
	 			<%= Closurecheckproject.DEPARTAMENT %>: 			f.<%= Closurecheckproject.DEPARTAMENT %>.value
			}; 
			
		 	closureAjax.call(params, function(data){
		 		
		 		var f = document.frm_checklist_popup;
				
				var dataRow = [
					data.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>,
					f.<%= Closurecheckproject.CLOSURECHECK %>.value,
					escape($("#closureCheckName").html()),
					escape($("#closureCheckDescription").attr("bt-xtitle")),
					escape(f.<%= Closurecheckproject.COMMENTS %>.value),
					f.<%= Closurecheckproject.DATEREALIZED %>.value,
					'<input type="checkbox" disabled="disabled" '+($('#<%= Closurecheckproject.REALIZED %>').is(':checked')?'checked':'')+'/>',
					f.<%= Closurecheckproject.REALIZED %>.checked ? "true" : "false",
					escape(f.<%= Closurecheckproject.MANAGER %>.value),
					f.<%= Closurecheckproject.DEPARTAMENT %>.value,
					'<nobr>'+
						'<img title="<fmt:message key="append"/>" class="link append" src="images/clip.png">'+
						'&nbsp;&nbsp;&nbsp;'+
						'<img title="<fmt:message key="view"/>" class="link view" src="images/view.png">'+
					'</nobr>',
					f.<%= Documentproject.IDDOCUMENTPROJECT %>.value,
					escape(f.documentComments.value),
					escape(f.documentFile.value),
					escape(f.documentLink.value)
		        ];
				
				if (f.<%=Closurecheckproject.IDCLOSURECHECKPROJECT %>.value == "") {
					
					checkListTable.fnAddDataAndSelect(dataRow); 
				} 
				else {
					dataRow[10] = checkListTable.fnFindData(data.<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>)[10];
					checkListTable.fnUpdateAndSelect(dataRow); 
				}
					
				$("#checklist-popup").dialog('close');
		 	}); 
		}
		
	}
};

readyMethods.add(function() {
	
	// Dialog
	$('div#checklist-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false,
		open: function(event, ui) { validatorCheckList.resetForm(); }
	});

	// Validator
	validatorCheckList = $("#frm_checklist_popup").validate({
		errorContainer: 'div#checklist-errors',
		errorLabelContainer: 'div#checklist-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#checklist-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%= Closurecheckproject.CLOSURECHECK %>: { required: true },
			<%= Closurecheckproject.COMMENTS %> : { maxlength : 500 },
			<%= Closurecheckproject.DATEREALIZED %> : { date: true }
		},
		messages: {
			<%= Closurecheckproject.CLOSURECHECK %>: { required: '${fmtClosureCheckRequired}'},
			<%= Closurecheckproject.DATEREALIZED %>: '${fmtDateRealizedFormat}'
		}
	});
	
	// Datepicker
	date = $('#<%= Closurecheckproject.DATEREALIZED %>').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true
	});
	
	$('#saveClosureCheckProject').live('click', function (event) {		
		checkListPopup.save();
	});	
	
    $('#closureCheckDescription').bt({
        fill: '#F9FBFF',
        cssStyles: {color: '#343C4E', width: 'auto'},
        width: 350,
        padding: 10,
        cornerRadius: 5,
        spikeLength: 15,
        spikeGirth: 5,
        shadow: true,
        positions: 'right'
    });

});
//-->
</script>
<div id="checklist-popup" class="popup">

	<%-- Errors --%>
	<div id="checklist-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="checklist-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	
	<form name="frm_checklist_popup" id="frm_checklist_popup" method="post">
		<input type="hidden" name="<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>" id="<%= Closurecheckproject.IDCLOSURECHECKPROJECT %>" value=""/>
		<input type="hidden" name="<%= Closurecheckproject.CLOSURECHECK %>" id="<%= Closurecheckproject.CLOSURECHECK %>" value=""/>
		<%-- Document data --%>
		<input type="hidden" name="<%= Documentproject.IDDOCUMENTPROJECT %>" value=""/>
		<input type="hidden" name="documentComments" value=""/>
		<input type="hidden" name="documentFile" value=""/>
		<input type="hidden" name="documentLink" value=""/>
		
		<fieldset>
			<legend><fmt:message key="closure_check_list"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" style="width:100%">
			
				<tr>
					<th class="left" colspan="2"><fmt:message key="closure_check"/></th>
					<th class="center" ><fmt:message key="realized"/></th>
				</tr>
				<tr>
					<%-- Closure checks --%>
					<td colspan="2">
						<div style="float: left; margin-right: 5px;" id="closureCheckName"></div>
						<div>
							<img src="images/info.png" id="closureCheckDescription" style="display: inline;" title="" bt-xtitle="">
						</div>
					</td>
					<%-- Check realized --%>
					<td align="center">
						<input type="checkbox" name="<%= Closurecheckproject.REALIZED %>" id="<%= Closurecheckproject.REALIZED %>" class="closureCheckRealized" style="width: 16px;"
							<c:if test="${check.realized eq true}"> checked</c:if>
						/>
					</td>
				</tr>
			
				<%-- Comments --%>
				<tr>
					<th class="left" colspan="3"><fmt:message key="comments"/></th>
				</tr>
				<tr>
					<td colspan="3">
						<textarea  name="<%= Closurecheckproject.COMMENTS %>" id="closureCheckProjectComments" class="campo valid" maxlength="1500" rows="8" style="width:98%;"></textarea>
					</td>
				</tr>
				
				<tr>
					<th class="left" ><fmt:message key="date"/></th>
					<th class="left" ><fmt:message key="owner"/></th>
					<th class="left" ><fmt:message key="workingcosts.department"/></th>
				</tr>
				<tr>
					<%-- Date realized --%>
					<td>
						<input type="text" id="<%= Closurecheckproject.DATEREALIZED %>" name="<%= Closurecheckproject.DATEREALIZED %>" class="campo fecha"/>
					</td>
					
					<%-- Manager --%>
					<td>
						<input type="text" id="<%= Closurecheckproject.MANAGER %>" name="<%= Closurecheckproject.MANAGER %>" class="campo"/>
					</td>
					
					<%-- Departaments --%>
					<td>
						<c:choose>
							<c:when test="${empty departaments}">
								<input type="text" id="<%= Closurecheckproject.DEPARTAMENT %>" name="<%= Closurecheckproject.DEPARTAMENT %>" class="campo"/>
							</c:when>
							<c:otherwise>
								<select id="<%= Closurecheckproject.DEPARTAMENT %>" name="<%= Closurecheckproject.DEPARTAMENT %>" class="campo">
									<option value='' selected><fmt:message key="select_opt"/></option>
									<c:forEach var="departament" items='${departaments}'>
										<option value="${departament}">${departament}</option>
									</c:forEach>
								</select>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<button type="button" class="boton" id="saveClosureCheckProject"><fmt:message key="save" /></button>
    	</div>
    </form>
</div>
