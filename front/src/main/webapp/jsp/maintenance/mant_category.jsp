<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@ page import="es.sm2.openppm.core.model.impl.Category" %>

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
  ~ File: mant_category.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:07
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.category.new" var="new_category_title" />
<fmt:message key="maintenance.category.edit" var="edit_category_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_category">
	<fmt:param><fmt:message key="maintenance.category"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_category">
	<fmt:param><fmt:message key="maintenance.category"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtCategoryNameRequired">
	<fmt:param><b><fmt:message key="maintenance.category.name"/></b></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: Category list
--%>

<script language="javascript" type="text/javascript" >

var categorysTable;
var categoryValidator;

function addCategory() {
	var f = document.frm_category_pop;
	f.reset();
	f.id.value = "";
	
	$('#category_description').text('');
    $("#<%= Category.ENABLE %>").attr("checked", true);
	
	$('#categorys-popup legend').html('${new_category_title}');
	$('#categorys-popup').dialog('open');
}

function saveCategory() {

	var f = document.frm_category_pop;
	
	if (categoryValidator.form()) {

		var params = {
			accion: "<%=MaintenanceServlet.JX_SAVE_CATEGORY %>",
			id: f.id.value,
			name: f.category_name.value,
			description: f.category_description.value,
            enable: $('#enable').is(':checked')
		};

		mainAjax.call(params, function(data) {

			var dataRow = [
				data.id,
				escape(data.name),
				escape(data.description),
                data.enable ? 'true' : 'false',
                '<input type="checkbox" disabled="disabled" '+(data.enable ? 'checked' : '')+'/>',
				'<nobr><img onclick="editCategory(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteCategory(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
			];
			
			if (f.id.value == data.id) { categorysTable.fnUpdateAndSelect(dataRow); }
			else { categorysTable.fnAddDataAndSelect(dataRow); }

			f.reset();
			$('#categorys-popup').dialog('close');		
		});	
	}
}

function editCategory(element) {
	
		var category = categorysTable.fnGetData( element.parentNode.parentNode.parentNode );

		var f = document.forms["frm_category_pop"];
		f.id.value 				= category[0];
		f.name.value 			= unEscape(category[1]);
		$('#category_description').text(unEscape(category[2]));
        $("#<%= Category.ENABLE %>").attr("checked", category[3] === 'true' ? true : false);

		// Display followup info
		$('#categorys-popup legend').html('${edit_category_title}');
		$('#categorys-popup').dialog('open');
}

function deleteCategory(element) {
	
	confirmUI(
		'${msg_title_confirm_delete_category}','${msg_confirm_delete_category}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var category = categorysTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_categorys;

			f.accion.value = "<%=MaintenanceServlet.DEL_CATEGORY %>";
			f.id.value = category[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	categorysTable = $('#tb_categorys').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"bAutoWidth": false,
		"aoColumns": [ 
			{ "sClass": "left","bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "bVisible": false },
            { "sClass": "left" },
            { "sClass": "center", "bSortable": false }
		]		
	});

	$('#tb_categorys tbody tr').live('click',  function (event) {		
		categorysTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#categorys-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { categoryValidator.resetForm(); }
	});

	categoryValidator = $("#frm_category_pop").validate({
		errorContainer: $('div#category-errors'),
		errorLabelContainer: 'div#category-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#category-numerrors').html(this.numberOfInvalids());
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

<form id="frm_categorys" name="frm_categorys" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	
	<fieldset>
		<legend><fmt:message key="maintenance.category" /></legend>
			<table id="tb_categorys" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.category" /></th>
					 <th width="30%"><fmt:message key="maintenance.category.name" /></th>
					 <th width="54%"><fmt:message key="maintenance.category.description" /></th>
                     <th></th> <%-- enable --%>
                     <th width="8%"><fmt:message key="enabled" /></th>
					 <th width="8%">
						<img onclick="addCategory()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					</tr>
				</thead>
				
				<tbody>
					<c:forEach var="category" items="${list}">
						<tr>
							<td>${category.idCategory}</td>
							<td>${tl:escape(category.name)}</td>
							<td>${tl:escape(category.description)}</td>
                            <td>${category.enable}</td>
                            <td>
                                <input type="checkbox" disabled="disabled"
                                    <c:if test="${category.enable eq true}">checked</c:if>
                                />
                            </td>
							<td>
								<nobr>
									<img onclick="editCategory(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteCategory(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="categorys-popup">

	<div id="category-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="category-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_category_pop" id="frm_category_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.category.new"/></legend>
		
            <table border="0" cellpadding="2" cellspacing="1" width="100%">
                <tr>
                    <th><fmt:message key="maintenance.category.name" />&nbsp;*</th>
                    <th><fmt:message key="enabled" />&nbsp;*</th>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" name="id" id="category_id"/>
                        <input type="text" name="name" id="category_name" class="campo" maxlength="50"/>
                    </td>
                    <td>
                        <input type="checkbox" name="<%= Category.ENABLE %>" id="<%= Category.ENABLE %>" class="campo" maxlength="50"/>
                    </td>
                </tr>
                <tr>
                    <th colspan="2"><fmt:message key="maintenance.category.description" /></th>
                </tr>
                <tr>
                    <td colspan="2">
                        <textarea  cols="5" rows="3" name="description" id="category_description" class="campo" style="width:98%;"></textarea>
                    </td>
                </tr>
            </table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveCategory(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

