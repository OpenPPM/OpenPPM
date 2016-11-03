<%@page import="es.sm2.openppm.core.model.impl.Classificationlevel"%>
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
  ~ File: mant_classification_level.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDelete">
	<fmt:param><fmt:message key="classificationLevel"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDelete">
	<fmt:param><fmt:message key="classificationLevel"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="name"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >

var classificationLevelTable;
var classificationLevelValidator;

var classificationLevel = {
		
	add: function() {
		
		var f = document.frm_classification_level_pop;
		
		f.reset();
		f.<%= Classificationlevel.IDCLASSIFICATIONLEVEL %>.value = '';
		$(f.<%= Classificationlevel.DESCRIPTION %>).text('');
		
		$('#classification-level-popup').dialog('open');
	},
	edit: function(element) {
		
		var aData = classificationLevelTable.fnGetData( element.parentNode.parentNode.parentNode );
		
		var f = document.frm_classification_level_pop;
		f.reset();
		
		f.<%= Classificationlevel.IDCLASSIFICATIONLEVEL %>.value 	= aData[0];
		f.<%= Classificationlevel.NAME %>.value						= unEscape(aData[1]);
		$(f.<%= Classificationlevel.DESCRIPTION %>).text(unEscape(aData[2]));
		
		$('#classification-level-popup').dialog('open');
	},
	del: function(element) {
		
		confirmUI(
			'${msgTitleConfirmDelete}','${msgConfirmDelete}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var aData = classificationLevelTable.fnGetData( element.parentNode.parentNode.parentNode );
				var f	  = document.frm_classification_level_pop;

				f.accion.value	 = "<%= MaintenanceServlet.DEL_CLASSIFICATION_LEVEL %>";
				f.<%= Classificationlevel.IDCLASSIFICATIONLEVEL %>.value	= aData[0];
				
				loadingPopup();
				f.submit();
		});
	},
	save: function() {
		
		if (classificationLevelValidator.form()) {
			
			mainAjax.call($("#frm_classification_level_pop").serializeArray(), function (data) {
				
				var f = document.frm_classification_level_pop;
				
				var dataRow = [
					data.<%= Classificationlevel.IDCLASSIFICATIONLEVEL %>,
					escape(f.<%= Classificationlevel.NAME %>.value),
					escape(f.<%= Classificationlevel.DESCRIPTION %>.value),
					''
	            ];

				if (f.<%= Classificationlevel.IDCLASSIFICATIONLEVEL %>.value == "") { 
					classificationLevelTable.fnAddDataAndSelect(dataRow); 
				} 
				else {
					classificationLevelTable.fnUpdateAndSelect(dataRow); 
				}
					
				$('#classification-level-popup').dialog('close');
			});	
		}
	},
	buttons: function(disable) {
		var $buttons = $('<nobr/>');
		$buttons.append($('<img/>', {onclick: 'classificationLevel.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
				.append('&nbsp;&nbsp;&nbsp;')
				.append($('<img/>', {onclick: 'classificationLevel.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
				.append('&nbsp;&nbsp;&nbsp;');
		return $buttons;	
	}
};

readyMethods.add(function () {

	classificationLevelTable = $('#tb_classification_level').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"fnRowCallback": function( nRow, aData) {
			$('td:eq(2)', nRow).html( classificationLevel.buttons(aData[4]) );
			return nRow;
		},
		"aoColumns": [ 
			{ "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "sClass": "center", "bSortable" : false }
     	]
	});

	$('#tb_classification_level tbody tr').live('click',  function (event) {
		classificationLevelTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#classification-level-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 600,
		resizable: false,
		open: function(event, ui) { classificationLevelValidator.resetForm(); }
	});

	classificationLevelValidator = $("#frm_classification_level_pop").validate({
		errorContainer: $('div#classification_level-errors'),
		errorLabelContainer: 'div#classification_level-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#classification_level-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%= Classificationlevel.NAME %>: { required: true , maxlength: 200},
			<%= Classificationlevel.DESCRIPTION %>: { maxlength: 2000 },
			idManager: { required: true }
		},
		messages:{
			<%= Classificationlevel.NAME %>: { required: '${fmtNameRequired}'},
			idManager: { required: '${fmtResourceManagerRequired}' }
		}
	});

	$("#saveClassificationLevel").on('click', classificationLevel.save);
});

</script>

<fieldset>
	<legend><fmt:message key="classificationLevel" /></legend>
	<table id="tb_classification_level" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				 <th width="0%">&nbsp;</th>
				 <th width="30%"><fmt:message key="name" /></th>
				 <th width="60%"><fmt:message key="description" /></th>
				 <th width="10%">
					<img onclick="classificationLevel.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="classLevel" items="${list}">
				<tr>
					<td>${classLevel.idClassificationlevel}</td>
					<td>${tl:escape(classLevel.name)}</td>
					<td>${tl:escape(classLevel.description)}</td>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>

<div id="classification-level-popup" class="popup">

	<%-- Errors message --%>
	<div id="classification_level-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="classification_level-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_classification_level_pop" id="frm_classification_level_pop">
		<input type="hidden" name="accion" value="<%= MaintenanceServlet.JX_SAVE_CLASSIFICATION_LEVEL %>"/>
		<input type="hidden" name="<%= Classificationlevel.IDCLASSIFICATIONLEVEL %>"/>
		<input type="hidden" name="idManten" value="<%= Constants.MANT_CLASSIFICATION_LEVEL %>"/>
		<fieldset>
			<legend><fmt:message key="classificationLevel"/></legend>
			<table cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="name" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="<%= Classificationlevel.NAME %>" class="campo" maxlength="200"/></td>
				</tr>
				<tr>
					<th><fmt:message key="description" /></th>
				</tr>
				<tr>
					<td><textarea rows="4" name="<%= Classificationlevel.DESCRIPTION %>" class="campo" style="width: 98%" maxlength="2000"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="button" class="boton" id="saveClassificationLevel" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

