<%@page import="es.sm2.openppm.core.model.impl.Resourcepool"%>
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
  ~ File: mant_resource_pool.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDelete">
	<fmt:param><fmt:message key="maintenance.employee.resource_pool"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDelete">
	<fmt:param><fmt:message key="maintenance.employee.resource_pool"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg" var="fmtResourceManagerRequired">
	<fmt:param><b><fmt:message key="maintenance.employee.resource_manager"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >

var resourcePoolTable;
var resourcePoolValidator;

var resourcePool = {
		
	add: function() {
		
		var f = document.frm_resource_pool_pop;
		
		f.reset();
		f.<%= Resourcepool.IDRESOURCEPOOL %>.value = '';
		$(f.<%= Resourcepool.DESCRIPTION %>).text('');
		
		<%-- Multiple combo --%>
		$("#employee_manager").val('');
		
		$('#resource-pool-popup').dialog('open');
	},
	edit: function(element) {
		
		var aData = resourcePoolTable.fnGetData( element.parentNode.parentNode.parentNode );
		
		var f = document.frm_resource_pool_pop;
		f.reset();
		
		f.<%= Resourcepool.IDRESOURCEPOOL %>.value 	= aData[0];
		f.<%= Resourcepool.NAME %>.value			= unEscape(aData[1]);
		$(f.<%= Resourcepool.DESCRIPTION %>).text(unEscape(aData[2]));
		
		<%-- Consult managepools --%>
		var params = {
			accion: "<%= MaintenanceServlet.JX_CONS_MANAGE_POOL %>",
			idResourcepool : aData[0]
		};
		
		mainAjax.call(params, function(data) {
			
			<%-- Multiple combo --%>
			$("#employee_manager").val('');
			
			var listResourceManager = data.listResourceManager;
			
			if(listResourceManager.length > 0) {
				for (var i = 0; i < listResourceManager.length; i++) {					
					$("#employee_manager option[value='" + listResourceManager[i].idEmployee + "']").attr("selected", true);
				}
			}
			
			$('#resource-pool-popup').dialog('open');
		});	
	},
	del: function(element) {
		
		confirmUI(
			'${msgTitleConfirmDelete}','${msgConfirmDelete}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var aData = resourcePoolTable.fnGetData( element.parentNode.parentNode.parentNode );
				var f	  = document.frm_resource_pool_pop;

				f.accion.value	 = "<%= MaintenanceServlet.DEL_RESOURCE_POOL %>";
				f.<%= Resourcepool.IDRESOURCEPOOL %>.value	= aData[0];
				
				loadingPopup();
				f.submit();
		});
	},
	save: function() {
		
		if (resourcePoolValidator.form()) {
			
			mainAjax.call($("#frm_resource_pool_pop").serializeArray(), function (data) {
				
				var f = document.frm_resource_pool_pop;
				
				var dataRow = [
					data.<%= Resourcepool.IDRESOURCEPOOL %>,
					escape(f.<%= Resourcepool.NAME %>.value),
					escape(f.<%= Resourcepool.DESCRIPTION %>.value),
					''
	            ];

				if (f.<%= Resourcepool.IDRESOURCEPOOL %>.value == "") { 
					resourcePoolTable.fnAddDataAndSelect(dataRow); 
				} 
				else {
					resourcePoolTable.fnUpdateAndSelect(dataRow); 
				}
					
				$('#resource-pool-popup').dialog('close');
			});	
		}
	},
	buttons: function(disable) {
		var $buttons = $('<nobr/>');
		$buttons.append($('<img/>', {onclick: 'resourcePool.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
				.append('&nbsp;&nbsp;&nbsp;')
				.append($('<img/>', {onclick: 'resourcePool.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
				.append('&nbsp;&nbsp;&nbsp;');
		return $buttons;	
	}
};

readyMethods.add(function () {

	resourcePoolTable = $('#tb_resource_pool').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"fnRowCallback": function( nRow, aData) {
			$('td:eq(2)', nRow).html( resourcePool.buttons(aData[4]) );
			return nRow;
		},
		"aoColumns": [ 
			{ "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "sClass": "center", "bSortable" : false }
     	]
	});

	$('#tb_resource_pool tbody tr').live('click',  function (event) {
		resourcePoolTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#resource-pool-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 400,
		resizable: false,
		open: function(event, ui) { resourcePoolValidator.resetForm(); }
	});

	resourcePoolValidator = $("#frm_resource_pool_pop").validate({
		errorContainer: $('div#resource_pool-errors'),
		errorLabelContainer: 'div#resource_pool-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#resource_pool-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%= Resourcepool.NAME %>: { required: true },
			<%= Resourcepool.DESCRIPTION %>: { maxlength: 200 },
			idManager: { required: true }
		},
		messages:{
			<%= Resourcepool.NAME %>: { required: '${fmtNameRequired}'},
			idManager: { required: '${fmtResourceManagerRequired}' }
		}
	});

	$("#saveResourcePool").on('click', resourcePool.save);
});

</script>

<fieldset>
	<legend><fmt:message key="maintenance.employee.resource_pool" /></legend>
	<table id="tb_resource_pool" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				 <th width="0%">&nbsp;</th>
				 <th width="30%"><fmt:message key="name" /></th>
				 <th width="60%"><fmt:message key="description" /></th>
				 <th width="10%">
					<img onclick="resourcePool.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="resourcePool" items="${list}">
				<tr>
					<td>${resourcePool.idResourcepool}</td>
					<td>${tl:escape(resourcePool.name)}</td>
					<td>${tl:escape(resourcePool.description)}</td>
					<td></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>

<div id="resource-pool-popup" class="popup">

	<%-- Errors message --%>
	<div id="resource_pool-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="resource_pool-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_resource_pool_pop" id="frm_resource_pool_pop">
		<input type="hidden" name="accion" value="<%= MaintenanceServlet.JX_SAVE_RESOURCE_POOL %>"/>
		<input type="hidden" name="<%= Resourcepool.IDRESOURCEPOOL %>"/>
		<input type="hidden" name="idManten" value="<%= Constants.MANT_RESOURCE_POOL %>"/>
		<fieldset>
			<legend><fmt:message key="maintenance.employee.resource_pool"/></legend>
			<table cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="name" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="<%= Resourcepool.NAME %>" class="campo" maxlength="50"/></td>
				</tr>
				<tr>
					<th><fmt:message key="description" /></th>
				</tr>
				<tr>
					<td><textarea rows="4" name="<%= Resourcepool.DESCRIPTION %>" class="campo" style="width: 98%"></textarea></td>
				</tr>
				<tr>
					<th><fmt:message key="maintenance.employee.resource_manager" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<select class="campo" name="idManager" id="employee_manager" style="height: 72px; width: 98%" multiple="multiple" class="campo">
							<c:forEach var="resourceManager" items="${listResourceManager}">
								<option value="${resourceManager.idEmployee}">${resourceManager.contact.fullName}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="button" class="boton" id="saveResourcePool" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

