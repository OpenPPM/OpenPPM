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
  ~ File: mant_geography.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:07
  --%>


<%-- Message --%>
<fmt:message key="maintenance.geography.new" var="new_geography_title" />
<fmt:message key="maintenance.geography.edit" var="edit_geography_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_geography">
	<fmt:param><fmt:message key="maintenance.geography"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_geography">
	<fmt:param><fmt:message key="maintenance.geography"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtGeographyNameRequired">
	<fmt:param><b><fmt:message key="maintenance.geography.name"/></b></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: Geography list
--%>

<script language="javascript" type="text/javascript" >

var geographysTable;
var geographyValidator;

function addGeography() {
	var f = document.forms["frm_geography_pop"];
	f.reset();
	f.id.value = "";
	
	$('#geography_description').text('');
	
	$('#geographys-popup legend').html('${new_geography_title}');
	$('#geographys-popup').dialog('open');
}

function saveGeography() {

	var f = document.frm_geography_pop;
	
	var params={
		accion: "<%=MaintenanceServlet.JX_SAVE_GEOGRAPHY %>",
		id: f.id.value,
		name: f.geography_name.value,
		description: f.geography_description.value	
	};
	
	if (geographyValidator.form()) {
		mainAjax.call(params, function(data){
			var dataRow = [
				data.id,
				data.name,
				data.description,
				'<nobr><img onclick="editGeography(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteGeography(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
		    ];
			
			if (f.id.value == data.id) { geographysTable.fnUpdateAndSelect(dataRow); }
			else { geographysTable.fnAddDataAndSelect(dataRow); }

			f.reset();
			$('#geographys-popup').dialog('close');	
		});	
	}
}

function editGeography(element) {
	
		var geography =geographysTable.fnGetData( element.parentNode.parentNode.parentNode );
					
		var f = document.forms["frm_geography_pop"];
		f.id.value 				= geography[0];
		f.name.value 			= unEscape(geography[1]);
		$('#geography_description').text(unEscape(geography[2]));
		// Display followup info
		$('#geographys-popup legend').html('${edit_geography_title}');
		$('#geographys-popup').dialog('open');
}

function deleteGeography(element) {
	confirmUI(
		'${msg_title_confirm_delete_geography}','${msg_confirm_delete_geography}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var geography = geographysTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_geographys;
			
			f.accion.value = "<%=MaintenanceServlet.DEL_GEOGRAPHY %>";
			f.id.value = geography[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	geographysTable = $('#tb_geographys').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"aaSorting": [[ 0, "asc" ]],
			"aoColumns": [ 
						{ "sClass": "left", "bVisible": false },
						{ "sClass": "left" },
						{ "sClass": "left" },
			            { "sClass": "center", "bSortable": false } 
			      		]
				
	});

	$('#tb_geographys tbody tr').live('click',  function (event) {
		geographysTable.fnSetSelectable(this,'selected_internal');
	} );

	$('div#geographys-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { geographyValidator.resetForm(); }
	});
	
	geographyValidator = $("#frm_geography_pop").validate({
		errorContainer: $('div#geography-errors'),
		errorLabelContainer: 'div#geography-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#geography-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			name: {required: true },
			description: { maxlength : 100 }
		},
		messages:{
			name:{ required:'${fmtGeographyNameRequired}'}
		}
	});
});

</script>

<form id="frm_geographys" name="frm_geographys" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	
	<fieldset>
		<legend><fmt:message key="maintenance.geography" /></legend>
			<table id="tb_geographys" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.geography" /></th>
					 <th width="20%"><fmt:message key="maintenance.geography.name" /></th>
					 <th width="72%"><fmt:message key="maintenance.geography.description" /></th>
					 <th width="8%">
						<img onclick="addGeography()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="geography" items="${list}">
						<tr>
							<td>${geography.idGeography}</td>
							<td>${tl:escape(geography.name)}</td>
							<td>${tl:escape(geography.description)}</td>
							<td>
								<nobr>
									<img onclick="editGeography(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteGeography(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="geographys-popup">

	<div id="geography-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="geography-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_geography_pop" id="frm_geography_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.geography.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
				 <th><fmt:message key="maintenance.geography.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="geography_id" />
						<input type="text" name="name" id="geography_name" class="campo" maxlength="50"/>
					</td>
				</tr>
				<tr>
				 <th><fmt:message key="maintenance.geography.description" /></th>
				 <th>&nbsp;</th>
				</tr>
				<tr>
					<td>
						<textarea name="description" id="geography_description" class="campo" style="width:98%;"></textarea>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveGeography(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

