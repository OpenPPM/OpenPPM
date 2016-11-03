<%@page import="es.sm2.openppm.core.model.impl.Label"%>
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
  ~ File: mant_label.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:06
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDelete">
	<fmt:param><fmt:message key="label"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDelete">
	<fmt:param><fmt:message key="label"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="labels.name"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >

var labelTable;
var labelValidator;

var labels = {
		
	enable : function(element) {
		var $tr = $(element).parents('tr');
		var item = labelTable.fnGetData( $tr[0] );
		
		var params = {
			accion: "<%=MaintenanceServlet.JX_ENABLE_LABEL %>",
			<%=Label.IDLABEL%>: item[0]
		};
			
		mainAjax.call(params, function (data) {
			item[4] = 'false';
			labelTable.fnUpdate(item, $tr[0]);
		});
	},
	disable : function(element) {
		var $tr = $(element).parents('tr');
		var item = labelTable.fnGetData( $tr[0] );

		var params = {
			accion: "<%=MaintenanceServlet.JX_DISABLE_LABEL %>",
			<%=Label.IDLABEL%>: item[0]
		};
		
		mainAjax.call(params, function (data) {
			item[4] = 'true';
			labelTable.fnUpdate(item, $tr[0]);
		});
	},
	add: function() {
		
		var f = document.frm_label_pop;
		f.reset();
		f.<%=Label.IDLABEL%>.value = '';
		$(f.<%=Label.DESCRIPTION%>).text('');
		
		$('#label-popup').dialog('open');
	},
	edit: function(element) {
		
		var aData = labelTable.fnGetData( element.parentNode.parentNode.parentNode );
		var f	  = document.frm_label_pop;
		f.reset();
		
		f.<%=Label.IDLABEL%>.value = aData[0];
		f.<%=Label.NAME%>.value			 = unEscape(aData[1]);
		$(f.<%=Label.DESCRIPTION%>).text(unEscape(aData[2]));
		
		$('#label-popup').dialog('open');
	},
	del: function(element) {
		
		confirmUI(
			'${msgTitleConfirmDelete}','${msgConfirmDelete}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var aData = labelTable.fnGetData( element.parentNode.parentNode.parentNode );
				var f	  = document.frm_label_pop;

				f.accion.value	 = "<%=MaintenanceServlet.DEL_LABEL %>";
				f.<%=Label.IDLABEL%>.value	= aData[0];
				
				loadingPopup();
				f.submit();
		});
	},
	save: function() {
		
		if (labelValidator.form()) {
			
			mainAjax.call($("#frm_label_pop").serializeArray(), function (data) {
				
				var f	 = document.frm_label_pop;
				
				var dataRow = [
					data.<%=Label.IDLABEL%>,
					escape(f.<%=Label.NAME%>.value),
					escape(f.<%=Label.DESCRIPTION%>.value),
					'',
					data.<%=Label.DISABLE%>
	            ];

				if (f.<%=Label.IDLABEL%>.value == "") { labelTable.fnAddDataAndSelect(dataRow); } 
				else { labelTable.fnUpdateAndSelect(dataRow); }
					
				$('#label-popup').dialog('close');
			});	
		}
	},
	buttons: function(disable) {
		
		var $buttons = $('<nobr/>');
		$buttons.append($('<img/>', {onclick: 'labels.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
				.append('&nbsp;&nbsp;&nbsp;')
				.append($('<img/>', {onclick: 'labels.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
				.append('&nbsp;&nbsp;&nbsp;');

		if (disable == 'true') {
			$buttons.append($('<img/>', {onclick: 'labels.enable(this)', title: '<fmt:message key="enable"/>', 'class': 'link', src: 'images/lock.png'}));
		}
		else {
			$buttons.append($('<img/>', {onclick: 'labels.disable(this)', title: '<fmt:message key="disable"/>', 'class': 'link', src: 'images/lock_open.png'}));
		}
		return $buttons;	
	}
};

readyMethods.add(function () {

	labelTable = $('#tb_label').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"fnRowCallback": function( nRow, aData) {
			$('td:eq(2)', nRow).html( labels.buttons(aData[4]) );
			return nRow;
		},
		"aoColumns": [ 
			{ "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "sClass": "center", "bSortable" : false },
            { "bVisible": false }
     	]
	});

	$('#tb_label tbody tr').live('click',  function (event) {
		labelTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#label-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 400,
		resizable: false,
		open: function(event, ui) { labelValidator.resetForm(); }
	});

	labelValidator = $("#frm_label_pop").validate({
		errorContainer: $('div#label-errors'),
		errorLabelContainer: 'div#label-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#label-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Label.NAME%>: { required: true },
			<%=Label.DESCRIPTION%>: { maxlength: 200 }
		},
		messages:{
			<%=Label.NAME%>: { required: '${fmtNameRequired}'}
		}
	});

	$('#showDisable').on('change', function() {
		if ($(this).prop('checked')) { labelTable.fnFilter( '', 4); }
		else { labelTable.fnFilter( 'false', 4); }
	});
	
	$('#showDisable').trigger('change');
	
	$("#saveLabel").on('click', labels.save);
});

</script>

<fieldset>
	<legend><fmt:message key="label" /></legend>
	<div class="title" style="padding:10px;">
		<fmt:message key="show"/>&nbsp;<fmt:message key="disabled"/>&nbsp;<input type="checkbox" id="showDisable" style="width: auto;"/>
	</div>
	<table id="tb_label" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				 <th width="0%">&nbsp;</th>
				 <th width="30%"><fmt:message key="labels.name" /></th>
				 <th width="60%"><fmt:message key="labels.description" /></th>
				 <th width="10%">
					<img onclick="labels.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
				 <th width="0%">&nbsp;</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="label" items="${list}">
				<tr>
					<td>${label.idLabel}</td>
					<td>${tl:escape(label.name)}</td>
					<td>${tl:escape(label.description)}</td>
					<td></td>
					<td>${tl:defBoolean(label.disable)}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>

<div id="label-popup" class="popup">

	<div id="label-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="Label-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_label_pop" id="frm_label_pop">
		<input type="hidden" name="accion" value="<%=MaintenanceServlet.JX_SAVE_LABEL%>"/>
		<input type="hidden" name="<%=Label.IDLABEL%>"/>
		<input type="hidden" name="idManten" value="<%=Constants.MANT_LABEL%>"/>
		<fieldset>
			<legend><fmt:message key="label"/></legend>
			<table cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="labels.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="<%=Label.NAME %>" class="campo" maxlength="50"/></td>
				</tr>
				<tr>
					<th><fmt:message key="labels.description" /></th>
				</tr>
				<tr>
					<td><textarea rows="4" name="<%=Label.DESCRIPTION%>" class="campo" style="width: 98%"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="button" class="boton" id="saveLabel" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

