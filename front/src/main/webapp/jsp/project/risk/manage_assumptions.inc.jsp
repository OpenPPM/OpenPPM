<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectRiskServlet"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
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
  ~ File: manage_assumptions.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="editAssumption"><img onclick="editAssumption(this);" title="<fmt:message key="edit"/>" class="link" src="images/view.png"></c:set>
</c:if>

<c:if test="${not op:isStringEquals(project.status, status_closed) and op:hasPermission(user,project.status,tab)}">
	<c:set var="editLogAssumption"><img onclick="editLogAssumption(this);" title="<fmt:message key="edit"/>" class="link" src="images/view.png"></c:set>
</c:if>

<c:set var="hasPermission" value ="${op:hasPermission(user,project.status,tab)}"/>

<fmt:message key="error" var="fmtError" />
<fmt:message key="data_not_found" var="data_not_found" />

<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_assumption">
	<fmt:param><fmt:message key="assumption"/></fmt:param>
</fmt:message>

<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_log_assumption">
	<fmt:param><fmt:message key="assumption_log"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtCodeRequired">
	<fmt:param><b><fmt:message key="assumption.code"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtDescRequired">
	<fmt:param><b><fmt:message key="assumption.description"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtLogDateRequired">
	<fmt:param><b><fmt:message key="date"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtDescChangeRequired">
	<fmt:param><b><fmt:message key="assumption_log.description_change"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtLogDateFormat">
	<fmt:param><b><fmt:message key="date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var assumptionTable;
var validatorAssumption;

// Assumption functions
function editAssumption(element) {
	
	var assumption = assumptionTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	var f = document.forms["frm_assumption"];
	f.reset();
	f.assumption_id.value = assumption[0];
	f.assumption_code.value = unEscape(assumption[1]);
	f.name.value = unEscape(assumption[2]);
	f.originator.value = unEscape(assumption[4]);
	f.doc.value = unEscape(assumption[5]);
	
	$('#assumption-popup').dialog('open');		
	f.assumption_code.focus();
	
	$("#assumption_description").text(unEscape(assumption[3]));
	
	refreshAssumptionLog(assumption[0]);
}

function addAssumption() {
	$('#assumption-popup').dialog('open');
	
	var f = document.forms["frm_assumption"];
	f.reset();
	f.assumption_id.value = "";
	f.assumption_code.focus();
	$("#assumption_description").text("");
	assumptionLogTable.fnClearTable();
}

function saveAssumption() {
	var f = document.forms["frm_assumption"];

	if (validatorAssumption.form()) {
		var idAssumption = f.assumption_id.value;
		
		// Save milestone by Ajax Call
		
		var params = {
			accion: "<%=ProjectRiskServlet.JX_SAVE_ASSUMPTION%>",
			id: document.forms["frm_project"].id.value,
			assumption_id: idAssumption,
			code: f.assumption_code.value,
			name: f.name.value,
			originator: f.originator.value,
			description: f.assumption_description.value,
			doc: f.doc.value				
		};
		
		riskAjax.call(params, function(data) {
				
			var deleteAssumption = '';
			if('${hasPermission}' == 'true') {
				deleteAssumption = '<img src="images/delete.jpg" class="link" onclick="deleteAssumption(event, '+ data.id +');" />';
			}		
			
			var dataRow = [
				data.id,
				escape(data.code),
				escape(data.name),
				escape(data.description),
				escape(data.originator),
				escape(data.doc),
				'<nobr>${editAssumption}' +
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
				deleteAssumption + '</nobr>'
            ];
			
			if (idAssumption == '') { assumptionTable.fnAddDataAndSelect(dataRow); }
			else { assumptionTable.fnUpdateAndSelect(dataRow); }
			
			f.reset();

			$('#assumption-popup').dialog('close');
		});
	} 
}

function deleteAssumptionConfirmed() {
	$('#dialog-confirm').dialog("close");
	var f = document.forms["frm_project"];
	f.accion.value = "<%=ProjectRiskServlet.DELETE_ASSUMPTION%>";
	f.submit();
}

function deleteAssumption(e, id) {
	var target = getTargetFromEvent(e);

	confirmUI('', '${msg_confirm_delete_assumption}', 
			"${msg_yes}", "${msg_no}", 
			deleteAssumptionConfirmed);
	document.forms["frm_project"].assumption_id.value = id;
}

// Log Assumption functions
var assumptionLogTable;
var validatorLogAssumption;

function addLogAssumption() {
	if (document.frm_assumption.assumption_id.value != "") {
		
		var f = document.frm_assumption_log;
		f.reset();
		f.assumption_log_id.value = "";
		$("#assumption_log_change").text("");
		
		$('#assumption-log-popup').dialog('open');
	}
	else {
		alertUI('${fmtError}','<fmt:message key="assumption.save_before_create"/>');
	}
}

function editLogAssumption(element) {
	
	var log = assumptionLogTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	var f = document.forms["frm_assumption_log"];
	f.reset();
	f.assumption_log_id.value = log[0];
	f.assumption_log_date.value = log[1];	
	$("#assumption_log_change").text(unEscape(log[2]));
	
	$('#assumption-log-popup').dialog('open');
	f.assumption_log_date.focus();
}

function saveLogAssumption() {
	var f = document.forms["frm_assumption_log"];

	if (validatorLogAssumption.form()) {
		var idAssumptionLog = f.assumption_log_id.value;
		
		var params = {
			accion: "<%=ProjectRiskServlet.JX_SAVE_ASSUMPTION_LOG%>",
			assumption_id: document.forms["frm_assumption"].assumption_id.value,
			assumption_log_id: idAssumptionLog,
			date: f.assumption_log_date.value,
			change: f.assumption_log_change.value
		};
		
		riskAjax.call(params, function (data) {
			
			var deleteLogAssumption = '';
			if('${hasPermission}' == 'true') {
				deleteLogAssumption = '<img src="images/delete.jpg" class="link" onclick="deleteLogAssumption(event, '+ data.id +');" />';
			}
					
			var dataRow = [
                data.id,
				data.date,
				escape(data.change),
				'<nobr>${editLogAssumption}' +
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
				deleteLogAssumption + '</nobr>'
            ];
			
			if (idAssumptionLog == '') { assumptionLogTable.fnAddDataAndSelect(dataRow); }
			else { assumptionLogTable.fnUpdateAndSelect(dataRow); }
			
			f.reset();

			$('#assumption-log-popup').dialog('close');
		});
	} 
}

function deleteLogAssumptionConfirmed() {
	$('#dialog-confirm').dialog("close"); 
	var f = document.forms["frm_project"];
	f.accion.value = "<%=ProjectRiskServlet.DELETE_LOG_ASSUMPTION%>";
	f.submit();
}

function deleteLogAssumption(e, id) {
	var target = getTargetFromEvent(e);

	confirmUI('', '${msg_confirm_delete_log_assumption}', 
			"${msg_yes}", "${msg_no}", 
			deleteLogAssumptionConfirmed);
	document.forms["frm_project"].assumption_log_id.value = id;
	
}

function refreshAssumptionLog(idAssumption) {
	if (idAssumption > 0) {
		
		var params = {
			accion: "<%=ProjectRiskServlet.JX_CONS_LOG_ASSUMPTIONS%>",
			assumption_id: idAssumption
		};
		
		riskAjax.call(params, function(data) {
			assumptionLogTable.fnClearTable();
			
			if (data.length > 0) {
				for (var i=0; i<data.length; i++) {
					
					var deleteLogAssumption = '';
					if('${hasPermission}' == 'true') {
						deleteLogAssumption = '<img src="images/delete.jpg" class="link" onclick="deleteLogAssumption(event, '+ data[i].id +');" />';
					}
					
					assumptionLogTable.fnAddData([
						data[i].id,
						data[i].date,
						escape(data[i].change),
						'<nobr>${editLogAssumption}' + 
						'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' +
						deleteLogAssumption + '</nobr>'
      					]);
				}
			}
		});
	}
}


readyMethods.add(function() {
	assumptionTable = $('#tb_assumptions').dataTable({
		"oLanguage": datatable_language,
		"bFilter": true,
		"bInfo": true,
		"bPaginate": true,
		"sPaginationType": "full_numbers",
		"aaSorting": [[ 1, "asc" ]],
		"aoColumns": [
		              { "bVisible": false },
		              { "sClass": "center" },
		              { "bVisible": false },
		              { "sClass": "left" },
		              { "bVisible": false },
		              { "bVisible": false },
		              { "sClass": "center", "bSortable": false }
		      		]
	});

	$('#tb_assumptions tbody tr').live('click', function (event) {		
		assumptionTable.fnSetSelectable(this,'selected_internal');
	} );

	assumptionLogTable = $('#tb_assumption_logs').dataTable( {
		"oLanguage": datatable_language,
		"bFilter": true,
		"bInfo": true,
		"bPaginate": true,
		"sPaginationType": "full_numbers",
		"aaSorting": [[ 1, "asc" ]],
		"aoColumns": [
		              { "bVisible": false },
		              { "sClass": "center",  "sType": "es_date" },
		              { "sClass": "left" },
		              { "sClass": "center", "bSortable": false }
		      		]
	} );

	$('#tb_assumption_logs tbody tr').live('click', function (event) {		
		assumptionLogTable.fnSetSelectable(this,'selected_internal');
	} );

	//Validate required fields
	validatorAssumption = $("#frm_assumption").validate({
		errorContainer: 'div#assumption-errors',
		errorLabelContainer: 'div#assumption-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#assumption-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			assumption_code: {required: true },
			assumption_description: {required: true, maxlength:3000 }
		},
		messages: {
			assumption_code: {required: '${fmtCodeRequired}' },
			assumption_description: {required: '${fmtDescRequired}' }
		}
	});
		
	$('div#assumption-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 650, 
		minWidth: 550, 
		resizable: false,
		open: function(event, ui) { validatorAssumption.resetForm(); }
	});

	validatorLogAssumption = $("#frm_assumption_log").validate({
		errorContainer: 'div#assumption-log-errors',
		errorLabelContainer: 'div#assumption-log-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#assumption-log-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			assumption_log_date: {required: true, date: true},
			assumption_log_change: {required: true, maxlength:3000 }
		},
		messages: {
			assumption_log_date: {required: '${fmtLogDateRequired}', date: '${fmtLogDateFormat}' },
			assumption_log_change: {required: '${fmtDescChangeRequired}' }
		}
	});
	
	$('div#assumption-log-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 550, 
		minWidth: 550, 
		resizable: false,
		open: function(event, ui) { validatorLogAssumption.resetForm(); }
	});
});
//-->
</script>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<script type="text/javascript">
	<!--
		readyMethods.add(function() {
			$('#assumption_log_date').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,
				onSelect: function() {
					if (validatorLogAssumption.numberOfInvalids() > 0) validatorLogAssumption.form();
				}
			});				
		});
	//-->
	</script>
</c:if>

<fmt:message key="assumptions" var="titleAssumtions"/>
<visual:panel id="field_assumption_control" title="${titleAssumtions }">
	<table id="tb_assumptions" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th>
				<th width="15%"><fmt:message key="assumption.code"/></th>
				<th width="0%">&nbsp;</th>
				<th width="77%"><fmt:message key="assumption.description"/></th>
				<th width="0%">&nbsp;</th>
				<th width="0%">&nbsp;</th>
				<th width="8%">
					<c:if test="${not op:isStringEquals(project.status, status_closed) and op:hasPermission(user,project.status,tab)}">
						<img onclick="addAssumption();" title="<fmt:message key="add"/>" class="link" src="images/add.png">				
					</c:if>
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="assum" items="${assumptions}">
				<tr>
					<td>${assum.idAssumption }</td>
					<td>${tl:escape(assum.assumptionCode)}</td>
					<td>${tl:escape(assum.assumptionName)}</td>
					<td>${tl:escape(assum.description)}</td>
					<td>${tl:escape(assum.originator)}</td>
					<td>${tl:escape(assum.assumptionDoc)}</td>						
					<td>
						<nobr>
							<c:if test="${not op:isStringEquals(project.status, status_closed)}">
								<img onclick="editAssumption(this);" title="<fmt:message key="edit"/>" class="link" src="images/view.png">
								&nbsp;&nbsp;&nbsp;
							</c:if>
							<c:if test="${op:hasPermission(user,project.status,tab)}">
								<img src="images/delete.jpg" class="link" onclick="deleteAssumption(event, ${assum.idAssumption});" />
							</c:if>
						</nobr>							
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>		
</visual:panel>
