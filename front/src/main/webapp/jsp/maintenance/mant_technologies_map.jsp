<%@page import="es.sm2.openppm.core.model.impl.Technology"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@ page import="es.sm2.openppm.core.logic.security.actions.MaintenanceAction" %>

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
  ~ File: mant_technology.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDelete">
	<fmt:param><fmt:message key="TECHNOLOGY"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDelete">
	<fmt:param><fmt:message key="TECHNOLOGY"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="name"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >

var technologyTable;
var technologyValidator;

var technology = {
		
	add: function() {
		
		var f = document.frm_technology_pop;
		
		f.reset();
		f.<%= Technology.IDTECHNOLOGY %>.value = '';
		$(f.<%= Technology.DESCRIPTION %>).text('');
		
		$('#technology-popup').dialog('open');
	},
	edit: function(element) {
		
		var aData = technologyTable.fnGetData( element.parentNode.parentNode.parentNode );
		
		var f = document.frm_technology_pop;
		f.reset();
		
		f.<%= Technology.IDTECHNOLOGY %>.value 	= aData[0];
		f.<%= Technology.NAME %>.value						= unEscape(aData[1]);
		$(f.<%= Technology.DESCRIPTION %>).text(unEscape(aData[2]));
		
		$('#technology-popup').dialog('open');
	},
	del: function(element) {
		
		confirmUI(
			'${msgTitleConfirmDelete}','${msgConfirmDelete}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var aData = technologyTable.fnGetData( element.parentNode.parentNode.parentNode );

                var params = {
                    accion: "<%= MaintenanceAction.JX_DELETE_TECHNOLOGY %>",
                    <%= Technology.IDTECHNOLOGY %>: aData[0]
                };

                mainAjax.call(params, function (data) {
                    technologyTable.fnDeleteSelected();
                });

		});
	},
	save: function() {
		
		if (technologyValidator.form()) {
			
			mainAjax.call($("#frm_technology_pop").serializeArray(), function (data) {
				
				var f = document.frm_technology_pop;
				
				var dataRow = [
					data.<%= Technology.IDTECHNOLOGY %>,
					escape(f.<%= Technology.NAME %>.value),
					escape(f.<%= Technology.DESCRIPTION %>.value),
					''
	            ];

				if (f.<%= Technology.IDTECHNOLOGY %>.value == "") {
					technologyTable.fnAddDataAndSelect(dataRow);
				} 
				else {
					technologyTable.fnUpdateAndSelect(dataRow);
				}
					
				$('#technology-popup').dialog('close');
			});	
		}
	},
	buttons: function(disable) {
		var $buttons = $('<nobr/>');
		$buttons.append($('<img/>', {onclick: 'technology.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
				.append('&nbsp;&nbsp;&nbsp;')
				.append($('<img/>', {onclick: 'technology.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
				.append('&nbsp;&nbsp;&nbsp;');
		return $buttons;	
	}
};

readyMethods.add(function () {

	technologyTable = $('#tb_technology').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"fnRowCallback": function( nRow, aData) {
			$('td:eq(2)', nRow).html( technology.buttons(aData[4]) );
			return nRow;
		},
		"aoColumns": [ 
			{ "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "sClass": "center", "bSortable" : false }
     	]
	});

	$('#tb_technology tbody tr').live('click',  function (event) {
		technologyTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#technology-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 600,
		resizable: false,
		open: function(event, ui) { technologyValidator.resetForm(); }
	});

	technologyValidator = $("#frm_technology_pop").validate({
		errorContainer: $('div#technology-errors'),
		errorLabelContainer: 'div#technology-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#technology-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%= Technology.NAME %>: { required: true , maxlength: 200},
			<%= Technology.DESCRIPTION %>: { maxlength: 2000 },
			idManager: { required: true }
		},
		messages:{
			<%= Technology.NAME %>: { required: '${fmtNameRequired}'},
			idManager: { required: '${fmtResourceManagerRequired}' }
		}
	});

	$("#saveTechnology").on('click', technology.save);
});

</script>

<fieldset>
	<legend><fmt:message key="TECHNOLOGIES_MAP" /></legend>
	<table id="tb_technology" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				 <th width="0%">&nbsp;</th>
				 <th width="30%"><fmt:message key="name" /></th>
				 <th width="60%"><fmt:message key="description" /></th>
				 <th width="10%">
					<img onclick="technology.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="technology" items="${list}">
				<tr>
					<td>${technology.idTechnology}</td>
					<td>${tl:escape(technology.name)}</td>
					<td>${tl:escape(technology.description)}</td>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>

<div id="technology-popup" class="popup">

	<%-- Errors message --%>
	<div id="technology-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="technology-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_technology_pop" id="frm_technology_pop">
		<input type="hidden" name="accion" value="<%= MaintenanceAction.JX_SAVE_TECHNOLOGY %>"/>
		<input type="hidden" name="<%= Technology.IDTECHNOLOGY %>"/>
		<input type="hidden" name="idManten" value="<%= Constants.MANT_TECHNOLOGIES_MAP %>"/>
		<fieldset>
			<legend><fmt:message key="TECHNOLOGY"/></legend>
			<table cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="name" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="<%= Technology.NAME %>" class="campo" maxlength="200"/></td>
				</tr>
				<tr>
					<th><fmt:message key="description" /></th>
				</tr>
				<tr>
					<td><textarea rows="4" name="<%= Technology.DESCRIPTION %>" class="campo" style="width: 98%" maxlength="2000"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="button" class="boton" id="saveTechnology" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

