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
  ~ File: mant_milestones_category.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.milestone_category.new" var="new_milestone_category_title" />
<fmt:message key="maintenance.milestone_category.edit" var="edit_milestone_category_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_milestone_category">
	<fmt:param><fmt:message key="maintenance.milestone_category"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_milestone_category">
	<fmt:param><fmt:message key="maintenance.milestone_category"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtCategoryNameRequired">
	<fmt:param><b><fmt:message key="maintenance.milestone_category.name"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >

var milestoneCategorysTable;
var milestoneCategoryValidator;

var milestoneCategory = {
	enable : function(element) {
		var $tr = $(element).parents('tr');
		var item = milestoneCategorysTable.fnGetData( $tr[0] );
		
		var params = {
			accion: "<%=MaintenanceServlet.JX_ENABLE_MILESTONE_CATEGORY%>",
		idMilestoneCategory: item[0]
		};
		
		mainAjax.call(params, function (data) {
			item[4] = 'false';
			milestoneCategorysTable.fnUpdate(item, $tr[0]);
		});
	},
	disable : function(element) {
		var $tr = $(element).parents('tr');
		var item = milestoneCategorysTable.fnGetData( $tr[0] );

		var params = {
			accion: "<%=MaintenanceServlet.JX_DISABLE_MILESTONE_CATEGORY%>",
			idMilestoneCategory: item[0]
		};
		
		mainAjax.call(params, function (data) {
			item[4] = 'true';
			milestoneCategorysTable.fnUpdate(item, $tr[0]);
		});
	},
	add: function() {
		var f = document.frm_milestone_category_pop;
		f.reset();
		f.id.value = "";
		
		$('#milestone_category_description').text('');
		
		$('#milestone-categorys-popup legend').html('${new_milestone_category_title}');
		$('#milestone-categorys-popup').dialog('open');
	},
	edit: function(element) {
		var milestoneCategory =milestoneCategorysTable.fnGetData( element.parentNode.parentNode.parentNode );
	
		var f = document.forms["frm_milestone_category_pop"];
		f.id.value 				= milestoneCategory[0];
		f.name.value 			= unEscape(milestoneCategory[1]);
		$('#milestone_category_description').text(unEscape(milestoneCategory[2]));
		// Display followup info
		$('#milestone-categorys-popup legend').html('${edit_milestone_category_title}');
		$('#milestone-categorys-popup').dialog('open');
		
	},
	del: function(element) {
		confirmUI(
			'${msg_title_confirm_delete_milestone_category}','${msg_confirm_delete_milestone_category}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var milestoneCategory = milestoneCategorysTable.fnGetData( element.parentNode.parentNode.parentNode );
				
				var f = document.frm_milestone_categorys;
		
				f.accion.value = "<%=MaintenanceServlet.DEL_MILESTONE_CATEGORY %>";
				f.id.value = milestoneCategory[0];
				f.idManten.value = $('select#idManten').val();
				
				loadingPopup();
				f.submit();
		});
	},
	save: function() {
		var f = document.frm_milestone_category_pop;
		
		if (milestoneCategoryValidator.form()) {
			
			var params={
				accion: "<%=MaintenanceServlet.JX_SAVE_MILESTONE_CATEGORY %>",
				id: f.id.value,
				name: f.milestone_category_name.value,
				description: f.milestone_category_description.value	
			};
			mainAjax.call(params, function(data){
				var dataRow = [
					data.id,
					escape(data.name),
					escape(data.description),
					'',
					data.disable
				];
				
				if (f.id.value == data.id) { milestoneCategorysTable.fnUpdateAndSelect(dataRow); }
				else { milestoneCategorysTable.fnAddDataAndSelect(dataRow); }

				f.reset();
				$('#milestone-categorys-popup').dialog('close');		
			});	
		}
	},
	buttons : function(disable) {
		var $buttons = $('<nobr/>');
		$buttons.append($('<img/>', {onclick: 'milestoneCategory.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
				.append('&nbsp;&nbsp;&nbsp;')
				.append($('<img/>', {onclick: 'milestoneCategory.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
				.append('&nbsp;&nbsp;&nbsp;');

		if (disable == 'true') {
			$buttons.append($('<img/>', {onclick: 'milestoneCategory.enable(this)', title: '<fmt:message key="enable"/>', 'class': 'link', src: 'images/lock.png'}));
		}
		else {
			$buttons.append($('<img/>', {onclick: 'milestoneCategory.disable(this)', title: '<fmt:message key="disable"/>', 'class': 'link', src: 'images/lock_open.png'}));
		}
		return $buttons;
	}
};


readyMethods.add(function () {

	milestoneCategorysTable = $('#tb_milestone_categorys').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"bAutoWidth": false,
		"fnRowCallback": function( nRow, aData) {
			$('td:eq(2)', nRow).html( milestoneCategory.buttons(aData[4]) );
			return nRow;
		},
		"aoColumns": [ 
			{ "sClass": "left","bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "sClass": "center", "bSortable": false },
			{ "bVisible": false }
		]		
	});

	$('#tb_milestone_categorys tbody tr').live('click',  function (event) {		
		milestoneCategorysTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#milestone-categorys-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { milestoneCategoryValidator.resetForm(); }
	});

	milestoneCategoryValidator = $("#frm_milestone_category_pop").validate({
		errorContainer: $('div#milestone-category-errors'),
		errorLabelContainer: 'div#milestone-category-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#milestone-category-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			name: { required: true },
			description: {maxlength: 100 }
		},
		messages:{
			name:{ required:'${fmtCategoryNameRequired}'}
		}
	});
	
	$('#frm_milestone_category_pop').submit(function() {
		milestoneCategory.save();
		return false;
	});
	
	$('#showDisable').on('change', function() {
		if ($(this).prop('checked')) { milestoneCategorysTable.fnFilter( '', 4); }
		else { milestoneCategorysTable.fnFilter( 'false', 4); }
	});
	
	$('#showDisable').trigger('change');
});

</script>

<form id="frm_milestone_categorys" name="frm_milestone_categorys" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />
	
	<fieldset>
		<legend><fmt:message key="maintenance.milestone_category" /></legend>
			<div class="title" style="padding:10px;">
				<fmt:message key="show"/>&nbsp;<fmt:message key="disabled"/>&nbsp;<input type="checkbox" id="showDisable" style="width: auto;"/>
			</div>
			<table id="tb_milestone_categorys" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.milestone_category" /></th>
					 <th width="30%"><fmt:message key="maintenance.milestone_category.name" /></th>
					 <th width="62%"><fmt:message key="maintenance.milestone_category.description" /></th>
					 <th width="8%">
						<img onclick="milestoneCategory.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					  <th>&nbsp;</th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="milestone" items="${list}">
						<tr>
							<td>${milestone.idMilestoneCategory}</td>
							<td>${tl:escape(milestone.name)}</td>
							<td>${tl:escape(milestone.description)}</td>
							<td></td>
							<td>${tl:defBoolean(milestone.disable)}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="milestone-categorys-popup">

	<div id="milestone-category-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="milestone-category-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_milestone_category_pop" id="frm_milestone_category_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.milestone_category.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
				 <th><fmt:message key="maintenance.milestone_category.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="milestone_category_id"/>
						<input type="text" name="name" id="milestone_category_name" class="campo" maxlength="50"/>
					</td>
				</tr>
				<tr>
				 <th><fmt:message key="maintenance.milestone_category.description" /></th>
				 <th>&nbsp;</th>
				</tr>
				<tr>
					<td>
						<textarea  cols="5" rows="3" name="description" id="milestone_category_description" class="campo" style="width:98%;"></textarea>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>