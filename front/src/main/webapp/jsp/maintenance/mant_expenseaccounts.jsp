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
  ~ File: mant_expenseaccounts.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.expenseaccounts.new" var="new_expenseaccounts_title" />
<fmt:message key="maintenance.expenseaccounts.edit" var="edit_expenseaccounts_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_expenseaccount">
	<fmt:param><fmt:message key="maintenance.expenseaccounts"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_expenseaccount">
	<fmt:param><fmt:message key="maintenance.expenseaccounts"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtExpenseAccountDescriptionRequired">
	<fmt:param><b><fmt:message key="maintenance.expenseaccounts.description"/></b></fmt:param>
</fmt:message>


<%-- 
Request Attributes:
	list_maintenance: Expenseaccounts list
--%>

<script language="javascript" type="text/javascript" >

var expenseAccountsTable;
var expenseAccountValidator;

function addExpenseAccount() {
	var f = document.forms["frm_expenseaccount_pop"];
	f.reset();
	f.id.value = "";
	
	$('#expenseaccount_description').text('');
	
	$('#expenseaccount-popup legend').html('${new_expenseaccounts_title}');
	$('#expenseaccount-popup').dialog('open');
}

function saveExpenseAccount() {

	var f = document.frm_expenseaccount_pop;
	
	if (expenseAccountValidator.form()) {
		
		var params={
			accion: "<%=MaintenanceServlet.JX_SAVE_EXPENSEACCOUNTS %>",
			id: f.id.value,
			description: f.expenseaccount_description.value		
		};
		
		mainAjax.call(params, function (data) {
			var dataRow = [
				data.id,
				escape(data.description),
				'<nobr><img onclick="editExpenseAccount(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteExpenseAccount(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
		    ];
		
			if (f.id.value == data.id) { expenseAccountsTable.fnUpdateAndSelect(dataRow); }
			else { expenseAccountsTable.fnAddDataAndSelect(dataRow); }
	
			f.reset();
			$('#expenseaccount-popup').dialog('close');
		});	
	}	
}

function editExpenseAccount(element) {
		
		var  expenseAccount = expenseAccountsTable.fnGetData( element.parentNode.parentNode.parentNode );
					
		var f = document.forms["frm_expenseaccount_pop"];

		f.id.value 				= expenseAccount[0];
		$('#expenseaccount_description').text(unEscape(expenseAccount[1]));
		// Display followup info
		$('#expenseaccount-popup legend').html('${edit_expenseaccounts_title}');
		$('#expenseaccount-popup').dialog('open');
}

function deleteExpenseAccount(element) {
	
	confirmUI(
		'${msg_title_confirm_delete_expenseaccount}','${msg_confirm_delete_expenseaccount}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var expenseAccount = expenseAccountsTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_expenseaccount;
			
			f.accion.value = "<%=MaintenanceServlet.DEL_EXPENSEACCOUNTS %>";
			f.id.value = expenseAccount[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	expenseAccountsTable = $('#tb_expenseaccounts').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"aaSorting": [[ 0, "asc" ]],
			"aoColumns": [ 
				{ "sClass": "center", "bVisible": false },
				{ "sClass": "left" },
		        { "sClass": "center", "bSortable" : false}
	      	]	
	});

	$('#tb_expenseaccounts tbody tr').live('click',  function (event) {
		expenseAccountsTable.fnSetSelectable(this,'selected_internal');
	});

	$('div#expenseaccount-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { expenseAccountValidator.resetForm(); }
	});

	expenseAccountValidator = $("#frm_expenseaccount_pop").validate({
		errorContainer: $('div#expenseaccount-errors'),
		errorLabelContainer: 'div#expenseaccount-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#expenseaccount-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			description: {required: true , maxlength : 100 }
		},
		messages:{
			description:{ required:'${fmtExpenseAccountDescriptionRequired}'}
		}
	});
});

</script>

<form id="frm_expenseaccount" name="frm_expenseaccount" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	<fieldset>
		<legend><fmt:message key="maintenance.expenseaccounts" /></legend>
			<table id="tb_expenseaccounts" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.expenseaccounts" /></th>
					 <th width="92%"><fmt:message key="maintenance.expenseaccounts.description" /></th>
					 <th width="8%">
						<img onclick="addExpenseAccount()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="expenseaccount" items="${list}">
						<tr>
							<td>${expenseaccount.idExpenseAccount}</td>
							<td>${tl:escape(expenseaccount.description)}</td>
							<td>
								<nobr>
									<img onclick="editExpenseAccount(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteExpenseAccount(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="expenseaccount-popup">

	<div id="expenseaccount-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="expenseaccount-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_expenseaccount_pop" id="frm_expenseaccount_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.expenseaccounts.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
				 <th><fmt:message key="maintenance.expenseaccounts.description" />&nbsp;*</th>
				 <th>&nbsp;</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="expenseaccount_id" />
						<textarea name="description" id="expenseaccount_description" class="campo" style="width:98%;"></textarea>
					</td>
				</tr>
    		</table>
    	</fieldset>
   	   	<div class="popButtons">
   	   		<input type="submit" class="boton" onclick="saveExpenseAccount(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

