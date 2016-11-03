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
  ~ File: mant_risk_category.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.risk_category.new" var="new_risk_category_title" />
<fmt:message key="maintenance.risk_category.edit" var="edit_risk_category_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_risk_category">
	<fmt:param><fmt:message key="maintenance.risk_category"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_risk_category">
	<fmt:param><fmt:message key="maintenance.risk_category"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtCategoryNameRequired">
	<fmt:param><b><fmt:message key="maintenance.risk_category.name"/></b></fmt:param>
</fmt:message>
<%-- 
Request Attributes:
	list_maintenance: Risk Category list
--%>

<script language="javascript" type="text/javascript" >

var riskCategorysTable;
var riskCategoryValidator;

function addRiskCategory() {
	var f = document.frm_risk_category_pop;
	f.reset();
	f.id.value = "";
	
	$('#risk_category_description').text('');
	
	$('#risk-categorys-popup legend').html('${new_risk_category_title}');
	$('#risk-categorys-popup').dialog('open');
}

function saveRiskCategory() {

	var f = document.frm_risk_category_pop;
	
	if (riskCategoryValidator.form()) {
		var params={
			accion: "<%=MaintenanceServlet.JX_SAVE_RISK_CATEGORY %>",
			id: f.id.value,
			name: f.risk_category_name.value,
			description: f.risk_category_description.value	
		};
		mainAjax.call(params, function(data){
			var dataRow = [
				data.id,
				escape(data.name),
				escape(data.description),
				'<nobr><img onclick="editRiskCategory(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteRiskCategory(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
			];
			
			if (f.id.value == data.id) { riskCategorysTable.fnUpdateAndSelect(dataRow); }
			else { riskCategorysTable.fnAddDataAndSelect(dataRow); }

			f.reset();
			$('#risk-categorys-popup').dialog('close');		
		});	
	}
}

function editRiskCategory(element) {
	
	var riskCategory =riskCategorysTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	var f = document.forms["frm_risk_category_pop"];
	f.id.value 				= riskCategory[0];
	f.name.value 			= unEscape(riskCategory[1]);
	$('#risk_category_description').text(unEscape(riskCategory[2]));
	// Display followup info
	$('#risk-categorys-popup legend').html('${edit_risk_category_title}');
	$('#risk-categorys-popup').dialog('open');
}

function deleteRiskCategory(element) {

	confirmUI(
		'${msg_title_confirm_delete_risk_category}','${msg_confirm_delete_risk_category}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var riskCategory = riskCategorysTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_risk_categorys;
	
			f.accion.value = "<%=MaintenanceServlet.DEL_RISK_CATEGORY %>";
			f.id.value = riskCategory[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	riskCategorysTable = $('#tb_risk_categorys').dataTable({
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

	$('#tb_risk_categorys tbody tr').live('click',  function (event) {		
		riskCategorysTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#risk-categorys-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { riskCategoryValidator.resetForm(); }
	});

	riskCategoryValidator = $("#frm_risk_category_pop").validate({
		errorContainer: $('div#risk-category-errors'),
		errorLabelContainer: 'div#risk-category-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#risk-category-numerrors').html(this.numberOfInvalids());
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

});

</script>

<form id="frm_risk_categorys" name="frm_risk_categorys" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />
	
	<fieldset>
		<legend><fmt:message key="maintenance.risk_category" /></legend>
			<table id="tb_risk_categorys" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.risk_category" /></th>
					 <th width="30%"><fmt:message key="maintenance.risk_category.name" /></th>
					 <th width="62%"><fmt:message key="maintenance.risk_category.description" /></th>
					 <th width="8%">
						<img onclick="addRiskCategory()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="risk" items="${list}">
						<tr>
							<td>${risk.idRiskcategory}</td>
							<td>${tl:escape(risk.name)}</td>
							<td>${tl:escape(risk.description)}</td>
							<td>
								<nobr>
									<img onclick="editRiskCategory(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteRiskCategory(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="risk-categorys-popup">

	<div id="risk-category-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="risk-category-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_risk_category_pop" id="frm_risk_category_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.risk_category.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
				 <th><fmt:message key="maintenance.risk_category.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="risk_category_id"/>
						<input type="text" name="name" id="risk_category_name" class="campo" maxlength="50"/>
					</td>
				</tr>
				<tr>
				 <th><fmt:message key="maintenance.risk_category.description" /></th>
				 <th>&nbsp;</th>
				</tr>
				<tr>
					<td>
						<textarea  cols="5" rows="3" name="description" id="risk_category_description" class="campo" style="width:98%;"></textarea>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveRiskCategory(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>