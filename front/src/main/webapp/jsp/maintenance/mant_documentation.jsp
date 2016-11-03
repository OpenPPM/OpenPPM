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
  ~ File: mant_documentation.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.documentation_manuals.new" var="new_document_title" />
<fmt:message key="maintenance.documentation_manuals.edit" var="edit_document_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_documentation">
	<fmt:param><fmt:message key="maintenance.documentation_manuals.documentation"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_documentation">
	<fmt:param><fmt:message key="maintenance.documentation_manuals.documentation"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="msg.error.no_selected_file" var="error_no_selected_file" />
<fmt:message key="msg.error.no_selected_file_info" var="error_no_selected_file_msg" />
<fmt:message key="maintenance.error_msg" var="fmtNamePopupRequired">
	<fmt:param><b><fmt:message key="document.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg" var="fmtFileRequired">
	<fmt:param><b><fmt:message key="maintenance.documentation_manuals.file"/></b></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: Documentation list
--%>

<script language="javascript" type="text/javascript" >

var docsMansTable;
var docsMansValidator;

function setFile(dest, src) {
	$("#"+src).val($(dest).val());
}

function addDocumentation() {
	var f = document.frm_docs_manuals_pop;
	f.reset();
	f.idDocumentation.value = "-1";	
	
	$('#file_path').val('');
	$('#namePopup').val('');
	
	$('#docs_manuals-popup legend').html('${new_document_title}');
	$('#docs_manuals-popup').dialog('open');
}

function saveDocumentation() {
	if (docsMansValidator.form()) {
		var f = document.frm_docs_manuals_pop;
		f.action = "<%=MaintenanceServlet.REFERENCE%>";
		f.accion.value = "<%=MaintenanceServlet.SAVE_DOCUMENTATION%>";
		f.idManten.value = "<%=Constants.MANT_DOCUMENTATION%>";
		$('#docs_manuals-popup').dialog('close');
		loadingPopup();
		f.submit();	
	}	
}

function editDocumentation(element) {
	
	var documentation = docsMansTable.fnGetData( element.parentNode.parentNode.parentNode );	
	var f = document.forms["frm_docs_manuals_pop"];	
	f.idDocumentation.value = documentation[0];
	f.namePopup.value 		= unEscape(documentation[1]);
	f.file_path.value 		= unEscape(documentation[2]);	
	$('#docs_manuals-popup legend').html('${edit_document_title}');
	$('#docs_manuals-popup').dialog('open');
}

function deleteDocumentation(element) {
	
	confirmUI(
		'${msg_title_confirm_delete_documentation}','${msg_confirm_delete_documentation}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var documentation = docsMansTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_docs_manuals;

			f.accion.value = "<%=MaintenanceServlet.DEL_DOCUMENTATION%>";
			f.id.value = documentation[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	docsMansTable = $('#tb_docs_mans').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"bAutoWidth": false,
		"aoColumns": [ 
			{ "sClass": "left","bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "sClass": "center", "bSortable": false } 
		]		
	});

	$('#tb_docs_mans tbody tr').live('click',  function (event) {
		docsMansTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#docs_manuals-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { docsMansValidator.resetForm(); }		
	});
	
	docsMansValidator = $("#frm_docs_manuals_pop").validate({
		errorContainer: $('div#docs_manuals-errors'),
		errorLabelContainer: 'div#docs_manuals-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#docs_manuals-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			namePopup: {required: true },
			file_path: {required: true }
		},
		messages:{
			namePopup:{ required:'${fmtNamePopupRequired}'},
			file_path:{ required:'${fmtFileRequired}'}
		}
	});
});

</script>

<form id="frm_docs_manuals" name="frm_docs_manuals" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	
	<fieldset>
		<legend><fmt:message key="maintenance.documentation_manuals" /></legend>
			<table id="tb_docs_mans" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%">&nbsp;</th>					 
					 <th width="60%"><fmt:message key="maintenance.documentation_manuals.name" /></th>
					 <th width="30%"><fmt:message key="maintenance.documentation_manuals.file_name" /></th>
					 <th width="10%">
						<img onclick="addDocumentation();" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="document" items="${list}">
						<tr>
							<td>${document.idDocumentation}</td>
							<td>${tl:escape(document.namePopup)}</td>
							<td>${tl:escape(document.nameFile)}</td>							
							<td>
								<nobr>
									<img onclick="editDocumentation(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteDocumentation(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="docs_manuals-popup">

	<div id="docs_manuals-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="docs_manuals-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_docs_manuals_pop" id="frm_docs_manuals_pop" method="POST" enctype="multipart/form-data" >		
		<input type="hidden" id="idDocumentation" name="idDocumentation" />
		<input type="hidden" name="idManten" value="" />
		<input type="hidden" name="accion" />
		
		<fieldset>
			<legend><fmt:message key="maintenance.documentation_manuals.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="10%"><fmt:message key="document.name"/>:&nbsp;</th>
					<td width="90%">
						<input class="campo" type="text" name="namePopup" id="namePopup" />
					</td>
				</tr>				
				<tr>
					<td>						
						<label class="file_change" style="margin-right:5px;">
							<input type="file" name="file" id="file" onChange="setFile(this,'file_path');"/>
						</label>
					</td>
					<td>
						<input class="campo" type="text" name="file_path" id="file_path" value="" readonly />
					</td>
				</tr>				 		
			</table>
    	</fieldset>
    	<div class="popButtons">
    		<a href="javascript:saveDocumentation();" class="boton"><fmt:message key="save" /></a>
	   	</div>
    </form>
</div>