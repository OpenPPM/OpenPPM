<%@page import="es.sm2.openppm.front.servlets.ProjectClosureServlet"%>
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
  ~ File: edit_documentation_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--
function viewDocumentation(element) {
	
	var doc = docRepositoryTable.fnGetData( element.parentNode.parentNode );
	
	var f = document.frm_edit_documentation_popup;
	
	f.idDocument.value = doc[0];
	f.docType.value = doc[1];
	f.docLink.value = $(doc[2]).text();
	f.docComment.value = doc[3];
		
	
	$('#edit-documentation-popup').dialog('open');
}

function saveDocumentation() {
	
	var f = document.forms["frm_edit_documentation_popup"];	
	
	var params = {
		accion: '<%=ProjectClosureServlet.JX_SAVE_DOCUMENTATION%>', 
		project: f.id.value, // Project ID
		document: f.idDocument.value,
		comment: f.docComment.value
	};
		
	closureAjax.call(params, function () {
		
		var dataRow = [
	        f.idDocument.value,
	       	escape(f.docType.value),
	       	'<a target="_blank" href="' + escape(f.docLink.value) + '">' + escape(f.docLink.value) + '</a>',
	        escape(f.docComment.value),
			'<img onclick="viewDocumentation(this);" title="<fmt:message key="view"/>" class="link" src="images/view.png">'
		];

		docRepositoryTable.fnUpdateAndSelect(dataRow);

		$('#edit-documentation-popup').dialog('close');
	});
}

readyMethods.add(function() {
	$('div#edit-documentation-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false
	});
});
//-->
</script>

<div id="edit-documentation-popup">
	
	<form name="frm_edit_documentation_popup" method="post">
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="id" value="${project.idProject}" />		
		<input type="hidden" name="idDocument" />
		<input type="hidden" name="docType" />
		<input type="hidden" name="docLink" />		
		
		<fieldset>
			<legend><fmt:message key="document" /></legend>
			<table width="100%">
				<tr>
					<th class="left"><fmt:message key="document_comment" /></th>						
				</tr>
				<tr>
					<td class="center">
						<textarea name="docComment" id="docComment" class="campo show_scroll" rows="3" style="width:97%;"></textarea>
					</td>						
				</tr>
			</table>
		</fieldset>
		<div class="popButtons">
			<a href="#" class="boton" onClick="return saveDocumentation();"><fmt:message key="save" /></a>
		</div>
	</form>
</div>