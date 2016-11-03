<%@page import="es.sm2.openppm.core.model.impl.Milestonetype"%>
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
  ~ File: mant_milestones_type.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDelete">
	<fmt:param><fmt:message key="milestone_type"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDelete">
	<fmt:param><fmt:message key="milestone_type"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="name"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >

var milestoneTypeTable;
var milestoneTypeValidator;

var milestoneType = {
		
	add: function() {
		
		var f = document.frm_milestone_type_pop;
		
		f.reset();
		f.<%= Milestonetype.IDMILESTONETYPE %>.value = '';
		$(f.<%= Milestonetype.DESCRIPTION %>).text('');
		
		$('#milestone-type-popup').dialog('open');
	},
	edit: function(element) {
		
		var aData = milestoneTypeTable.fnGetData( element.parentNode.parentNode.parentNode );
		
		var f = document.frm_milestone_type_pop;
		f.reset();
		
		f.<%= Milestonetype.IDMILESTONETYPE %>.value 	= aData[0];
		f.<%= Milestonetype.NAME %>.value			= unEscape(aData[1]);
		$(f.<%= Milestonetype.DESCRIPTION %>).text(unEscape(aData[2]));
		
		$('#milestone-type-popup').dialog('open');
	},
	del: function(element) {
		
		confirmUI(
			'${msgTitleConfirmDelete}','${msgConfirmDelete}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var row = milestoneTypeTable.fnGetData( element.parentNode.parentNode.parentNode );
				
				var params={
					accion: 								"<%=MaintenanceServlet.JX_DELETE_MILESTONE_TYPE %>",
					<%= Milestonetype.IDMILESTONETYPE %>: 	row[0]
				};
				
				mainAjax.call(params, function(data){
					milestoneTypeTable.fnDeleteSelected();
				});	
		});
	},
	save: function() {
		
		if (milestoneTypeValidator.form()) {
			
			mainAjax.call($("#frm_milestone_type_pop").serializeArray(), function (data) {
				
				var f = document.frm_milestone_type_pop;
				
				var dataRow = [
					data.<%= Milestonetype.IDMILESTONETYPE %>,
					escape(f.<%= Milestonetype.NAME %>.value),
					escape(f.<%= Milestonetype.DESCRIPTION %>.value),
					''
	            ];

				if (f.<%= Milestonetype.IDMILESTONETYPE %>.value == "") { 
					milestoneTypeTable.fnAddDataAndSelect(dataRow); 
				} 
				else {
					milestoneTypeTable.fnUpdateAndSelect(dataRow); 
				}
					
				$('#milestone-type-popup').dialog('close');
			});	
		}
	},
	buttons: function(disable) {
		var $buttons = $('<nobr/>');
		$buttons.append($('<img/>', {onclick: 'milestoneType.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
				.append('&nbsp;&nbsp;&nbsp;')
				.append($('<img/>', {onclick: 'milestoneType.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
				.append('&nbsp;&nbsp;&nbsp;');
		return $buttons;	
	}
};

readyMethods.add(function () {

	milestoneTypeTable = $('#tb_milestone_type').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"fnRowCallback": function( nRow, aData) {
			$('td:eq(2)', nRow).html( milestoneType.buttons(aData[4]) );
			return nRow;
		},
		"aoColumns": [ 
			{ "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "sClass": "center", "bSortable" : false }
     	]
	});

	$('#tb_milestone_type tbody tr').live('click',  function (event) {
		milestoneTypeTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#milestone-type-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 400,
		resizable: false,
		open: function(event, ui) { milestoneTypeValidator.resetForm(); }
	});

	milestoneTypeValidator = $("#frm_milestone_type_pop").validate({
		errorContainer: $('div#milestone_type-errors'),
		errorLabelContainer: 'div#milestone_type-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#milestone_type-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%= Milestonetype.NAME %>: { required: true },
			<%= Milestonetype.DESCRIPTION %>: { maxlength: 200 }
		},
		messages:{
			<%= Milestonetype.NAME %>: { required: '${fmtNameRequired}'}
		}
	});

	$("#saveMilestoneType").on('click', milestoneType.save);
});

</script>

<fieldset>
	<legend><fmt:message key="maintenance.milestones_type" /></legend>
	<table id="tb_milestone_type" class="tabledata" cellspacing="1" style="width:100%">
		<thead>
			<tr>
				 <th width="0%">&nbsp;</th>
				 <th width="30%"><fmt:message key="name" /></th>
				 <th width="60%"><fmt:message key="description" /></th>
				 <th width="10%">
					<img onclick="milestoneType.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="milestoneType" items="${list}">
				<tr>
					<td>${milestoneType.idMilestoneType}</td>
					<td>${tl:escape(milestoneType.name)}</td>
					<td>${tl:escape(milestoneType.description)}</td>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>

<div id="milestone-type-popup" class="popup">

	<%-- Errors message --%>
	<div id="milestone_type-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="milestone_type-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_milestone_type_pop" id="frm_milestone_type_pop">
		<input type="hidden" name="accion" value="<%= MaintenanceServlet.JX_SAVE_MILESTONE_TYPE %>"/>
		<input type="hidden" name="<%= Milestonetype.IDMILESTONETYPE %>"/>
		<input type="hidden" name="idManten" value="<%= Constants.MANT_MILESTONES_TYPE %>"/>
		<fieldset>
			<legend><fmt:message key="maintenance.milestones_type"/></legend>
			<table cellpadding="2" cellspacing="1" style="width:100%">
				<tr>
					<th><fmt:message key="name" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="<%= Milestonetype.NAME %>" class="campo" maxlength="50"/></td>
				</tr>
				<tr>
					<th><fmt:message key="description" /></th>
				</tr>
				<tr>
					<td><textarea rows="4" name="<%= Milestonetype.DESCRIPTION %>" class="campo" style="width: 98%"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="button" class="boton" id="saveMilestoneType" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

