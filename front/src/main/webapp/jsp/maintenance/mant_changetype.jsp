<%@page import="es.sm2.openppm.core.model.impl.Changetype"%>
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
  ~ File: mant_changetype.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:07
  --%>

<%-- Message --%>
<fmt:message key="maintenance.change_types.new" var="new_change_types_title" />
<fmt:message key="maintenance.change_types.edit" var="edit_change_types_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_change_types">
	<fmt:param><fmt:message key="change_type"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_change_types">
	<fmt:param><fmt:message key="change_type"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtChangeTypeDescriptionRequired">
	<fmt:param><b><fmt:message key="maintenance.change_types.description"/></b></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: Changetype list
--%>

<script language="javascript" type="text/javascript" >

var changeTypesTable;
var changeTypeValidator;

function addChangeType() {
	var f = document.frm_changetype_pop;
	f.reset();
	f.id.value = "";
	
	$('#changetype-popup legend').html('${new_change_types_title}');
	$('#changetype-popup').dialog('open');
}

function saveChangeType() {
	
	var f = document.forms["frm_changetype_pop"];
	
	var params={
		accion: "<%=MaintenanceServlet.JX_SAVE_CHANGETYPE%>",
		id: f.id.value,
		description: f.changetype_description.value	
	};
	
	if (changeTypeValidator.form()) {
		mainAjax.call(params, function(data){
			var dataRow = [
				data.<%=Changetype.IDCHANGETYPE%>,
				escape(f.changetype_description.value),     
				'<nobr><img onclick="editChangeType(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteChangeType(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
			];
			
			if (f.id.value == data.<%=Changetype.IDCHANGETYPE%>) { changeTypesTable.fnUpdateAndSelect(dataRow); }
			else { changeTypesTable.fnAddDataAndSelect(dataRow); }
			
			f.reset();
			$('#changetype-popup').dialog('close');
		});	
	}
}

function editChangeType(element) {
	
	var changeType = changeTypesTable.fnGetData( element.parentNode.parentNode.parentNode );

	var f = document.forms["frm_changetype_pop"];

	f.id.value 				= changeType[0];
	f.description.value 	= unEscape(changeType[1]);
	// Display followup info
	$('#changetype-popup legend').html('${edit_change_types_title}');
	$('#changetype-popup').dialog('open');
}

function deleteChangeType(element) {
	
	confirmUI(
		'${msg_title_confirm_delete_change_types}','${msg_confirm_delete_change_types}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var changeType = changeTypesTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_changetype;

			f.accion.value = "<%=MaintenanceServlet.DEL_CHANGETYPE%>";
			f.id.value = changeType[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	changeTypesTable = $('#tb_changetypes').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"aoColumns": [ 
			{ "sClass": "center", "bVisible": false },
			{ "sClass": "left" },
	        { "sClass": "center", "bSortable" : false}
      	]
	});

	$('#tb_changetypes tbody tr').live('click',  function (event) {		
		changeTypesTable.fnSetSelectable(this,'selected_internal');
	});

	$('div#changetype-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 500,
		resizable: false,
		open: function(event, ui) { changeTypeValidator.resetForm(); }
	});

	changeTypeValidator = $("#frm_changetype_pop").validate({
		errorContainer: $('div#changetype-errors'),
		errorLabelContainer: 'div#changetype-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#changetype-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			description: {required: true }
		},
		messages:{
			description:{ required:'${fmtChangeTypeDescriptionRequired}'}
		}
	});
});

</script>

<form id="frm_changetype" name="frm_changetype" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	
	<fieldset>
		<legend><fmt:message key="maintenance.change_types" /></legend>
			<table id="tb_changetypes" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.change_types" /></th>
					 <th width="92%"><fmt:message key="maintenance.change_types.description" /></th>
					 <th width="8%">
						<img onclick="addChangeType()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="changetype" items="${list}">
						<tr>
							<td>${changetype.idChangeType}</td>
							<td>${tl:escape(changetype.description)}</td>
							<td>
								<nobr>
									<img onclick="editChangeType(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteChangeType(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="changetype-popup">

	<div id="changetype-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="changetype-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_changetype_pop" id="frm_changetype_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.change_types.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
				 <th><fmt:message key="maintenance.change_types.description" />&nbsp;*</th>
				 <th>&nbsp;</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="changetype_id" />
						<input type="text" name="description" id="changetype_description" class="campo" maxlength="50"/>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveChangeType(); return false;" value="<fmt:message key="save"/>" />
    	</div>
    </form>
</div>

