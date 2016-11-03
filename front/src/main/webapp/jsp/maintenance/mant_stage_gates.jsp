<%@page import="es.sm2.openppm.core.model.impl.Stagegate"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>

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
  ~ File: mant_stage_gates.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDelete">
	<fmt:param><fmt:message key="stage_gates"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDelete">
	<fmt:param><fmt:message key="stage_gates"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="stage_gate.name"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >

var stageGatesTable;
var stageGatesValidator;

var stageGates = {
		
	add: function() {
		
		var f = document.frm_stage_gates_pop;
		f.reset();
		f.<%= Stagegate.IDSTAGEGATE %>.value = '';
		$(f.<%= Stagegate.DESCRIPTION %>).text('');
		
		$('#stage-gates-popup').dialog('open');
	},
	edit: function(element) {
		
		var aData = stageGatesTable.fnGetData( element.parentNode.parentNode.parentNode );
		var f	  = document.frm_stage_gates_pop;
		f.reset();
		
		f.<%= Stagegate.IDSTAGEGATE %>.value 	= aData[0];
		f.<%= Stagegate.NAME %>.value			= unEscape(aData[1]);
		$(f.<%= Stagegate.DESCRIPTION %>).text(unEscape(aData[2]));
		
		$('#stage-gates-popup').dialog('open');
	},
	del: function(element) {
		
		confirmUI(
			'${msgTitleConfirmDelete}','${msgConfirmDelete}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var aData = stageGatesTable.fnGetData( element.parentNode.parentNode.parentNode );
				var f	  = document.frm_stage_gates_pop;

				f.accion.value	 = "<%= MaintenanceServlet.DEL_STAGE_GATE %>";
				f.<%= Stagegate.IDSTAGEGATE %>.value	= aData[0];
				
				loadingPopup();
				f.submit();
		});
	},
	save: function() {
		
		if (stageGatesValidator.form()) {
			
			mainAjax.call($("#frm_stage_gates_pop").serializeArray(), function (data) {
				
				var f = document.frm_stage_gates_pop;
				
				var dataRow = [
					data.<%= Stagegate.IDSTAGEGATE %>,
					escape(f.<%= Stagegate.NAME %>.value),
					escape(f.<%= Stagegate.DESCRIPTION %>.value),
					''
	            ];

				if (f.<%= Stagegate.IDSTAGEGATE %>.value == "") { 
					stageGatesTable.fnAddDataAndSelect(dataRow); 
				} 
				else {
					stageGatesTable.fnUpdateAndSelect(dataRow); 
				}
					
				$('#stage-gates-popup').dialog('close');
			});	
		}
	},
	buttons: function(disable) {
		var $buttons = $('<nobr/>');
		$buttons.append($('<img/>', {onclick: 'stageGates.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
				.append('&nbsp;&nbsp;&nbsp;')
				.append($('<img/>', {onclick: 'stageGates.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
				.append('&nbsp;&nbsp;&nbsp;');
		return $buttons;	
	}
};

readyMethods.add(function () {

	stageGatesTable = $('#tb_stage_gates').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"fnRowCallback": function( nRow, aData) {
			$('td:eq(2)', nRow).html( stageGates.buttons(aData[4]) );
			return nRow;
		},
		"aoColumns": [ 
			{ "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "sClass": "center", "bSortable" : false }
     	]
	});

	$('#tb_stage_gates tbody tr').live('click',  function (event) {
		stageGatesTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#stage-gates-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 400,
		resizable: false,
		open: function(event, ui) { stageGatesValidator.resetForm(); }
	});

	stageGatesValidator = $("#frm_stage_gates_pop").validate({
		errorContainer: $('div#stage_gates-errors'),
		errorLabelContainer: 'div#stage_gates-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#stage_gates-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Stagegate.NAME%>: { required: true },
			<%=Stagegate.DESCRIPTION%>: { maxlength: 200 }
		},
		messages:{
			<%=Stagegate.NAME%>: { required: '${fmtNameRequired}'}
		}
	});

	$("#saveStageGates").on('click', stageGates.save);
});

</script>

<fieldset>
	<legend><fmt:message key="stage_gates" /></legend>
	<table id="tb_stage_gates" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				 <th width="0%">&nbsp;</th>
				 <th width="30%"><fmt:message key="stage_gate.name" /></th>
				 <th width="60%"><fmt:message key="stage_gate.description" /></th>
				 <th width="10%">
					<img onclick="stageGates.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="stageGate" items="${list}">
				<tr>
					<td>${stageGate.idStageGate}</td>
					<td>${tl:escape(stageGate.name)}</td>
					<td>${tl:escape(stageGate.description)}</td>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>

<div id="stage-gates-popup" class="popup">

	<%-- Errors message --%>
	<div id="stage_gates-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="stage_gates-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_stage_gates_pop" id="frm_stage_gates_pop">
		<input type="hidden" name="accion" value="<%= MaintenanceServlet.JX_SAVE_STAGE_GATE %>"/>
		<input type="hidden" name="<%= Stagegate.IDSTAGEGATE %>"/>
		<input type="hidden" name="idManten" value="<%= Constants.MANT_STAGE_GATES %>"/>
		<fieldset>
			<legend><fmt:message key="stage_gates"/></legend>
			<table cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="stage_gate.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="<%=Stagegate.NAME %>" class="campo" maxlength="50"/></td>
				</tr>
				<tr>
					<th><fmt:message key="stage_gate.description" /></th>
				</tr>
				<tr>
					<td><textarea rows="4" name="<%=Stagegate.DESCRIPTION%>" class="campo" style="width: 98%"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="button" class="boton" id="saveStageGates" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

