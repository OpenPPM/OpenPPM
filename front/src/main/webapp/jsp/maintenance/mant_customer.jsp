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
  ~ File: mant_customer.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:07
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.customers.new" var="new_customer_title" />
<fmt:message key="maintenance.customers.edit" var="edit_customer_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_customer">
	<fmt:param><fmt:message key="customer"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_customer">
	<fmt:param><fmt:message key="customer"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtCustomerNameRequired">
	<fmt:param><b><fmt:message key="maintenance.customers.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_select_a" var="fmtCustomerTypeRequired">
	<fmt:param><b><fmt:message key="maintenance.customers.type"/></b></fmt:param>
</fmt:message>


<%-- 
Request Attributes:
	list_maintenance: Customer list
--%>

<script language="javascript" type="text/javascript" >

var customersTable;
var customerValidator;

function addCustomer() {
	var f = document.frm_customer_pop;
	f.reset();
	f.id.value = "";
	
	$('#customer_description').text("");
	
	$('#customer-popup legend').html('${new_customer_title}');
	$('#customer-popup').dialog('open');
}

function saveCustomer() {

	var f = document.frm_customer_pop;
	
	if (customerValidator.form()) {
		
		var params={
			accion: "<%=MaintenanceServlet.JX_SAVE_CUSTOMER %>",
			id: f.id.value,
			name: f.name.value,
			description: f.description.value,
			type: f.type.value
		};
		
		mainAjax.call(params, function(data){
			var dataRow = [ 
              	data.idCustomer,
				escape(f.name.value), 
				escape(f.description.value),
				$(f.type).find('option:selected').text(),
				'<nobr><img onclick="editCustomer(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteCustomer(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>',
				f.type.value
		    ];
			
			if (f.id.value == data.idCustomer) { customersTable.fnUpdateAndSelect(dataRow); }
			else { customersTable.fnAddDataAndSelect(dataRow); }

		 	f.reset();
		 	$('#customer-popup').dialog('close');
		});	
	}	
}

function editCustomer(element) {
	
		var customer =customersTable.fnGetData( element.parentNode.parentNode.parentNode );
		
		var f = document.forms["frm_customer_pop"];
		f.id.value 		= customer[0];
		f.name.value 	= unEscape(customer[1]);		
		f.type.value	= customer[5];
		$('#customer_description').text(unEscape(customer[2]));
		// Display followup info
		$('#customer-popup legend').html('${edit_customer_title}');
		$('#customer-popup').dialog('open');
}

function deleteCustomer(element) {
	
	confirmUI(
		'${msg_title_confirm_delete_customer}','${msg_confirm_delete_customer}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var customer = customersTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_customers;

			f.accion.value = "<%=MaintenanceServlet.DEL_CUSTOMER %>";
			f.id.value = customer[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	customersTable = $('#tb_customers').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"iDisplayLength": 50,
			"aaSorting": [[ 0, "asc" ]],
			"bAutoWidth": false,
			"aoColumns": [ 
				{ "bVisible": false },
				{ "sClass": "left" },
				{ "sClass": "left" },
				{ "sClass": "center" },
	            { "sClass": "center", "bSortable" : false},
				{ "bVisible": false }
      		]	
	});

	$('#tb_customers tbody tr').live('click',  function (event) {
		customersTable.fnSetSelectable(this,'selected_internal');
	});

	$('div#customer-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { customerValidator.resetForm(); }
	});
	
	customerValidator = $("#frm_customer_pop").validate({
		errorContainer: $('div#customer-errors'),
		errorLabelContainer: 'div#customer-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#customer-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			name: { required: true },
			type: { required: true },
			description : { maxlength : 100 }
		},
		messages:{
			name: { required: '${fmtCustomerNameRequired}' },
			type: { required: '${fmtCustomerTypeRequired}' }
		}
	});
});

</script>

<form id="frm_customers" name="frm_customers" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />
	<fieldset>
		<legend><fmt:message key="maintenance.customers" /></legend>
			<table id="tb_customers" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 	<th width="0%"><fmt:message key="maintenance.customer" /></th>
					 	<th width="20%"><fmt:message key="maintenance.customers.name" /></th>
					 	<th width="60%"><fmt:message key="maintenance.customers.description" /></th>
					 	<th width="12%"><fmt:message key="maintenance.customers.type" /></th>
					 	<th width="8%">
							<img onclick="addCustomer()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 	</th>
					 	<th width="0%">&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="customer" items="${listCustomers}">
						<tr>
							<td>${customer.idCustomer}</td>
							<td>${tl:escape(customer.name)}</td>
							<td>${tl:escape(customer.description)}</td>
							<td>${tl:escape(customer.customertype.name)}</td>
							<td>
								<nobr>								
									<img onclick="editCustomer(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteCustomer(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
							<td>${customer.customertype.idCustomerType}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="customer-popup">

	<div id="customer-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="customer-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_customer_pop" id="frm_customer_pop" >
		<fieldset>
			<legend><fmt:message key="maintenance.customers.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th width="50%"><fmt:message key="maintenance.customers.name" />&nbsp;*</th>
					<th width="50%"><fmt:message key="maintenance.customers.type" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="customer_id"/>
						<input type="text" name="name" id="customer_name" class="campo" maxlength="45"/>
					</td>
					<td>
						<select name="type" id="customer_type" class="campo">
							<option value=""><fmt:message key="select_opt" /></option>
							<c:forEach var="type" items="${types }">
								<option value="${type.idCustomerType }">${type.name }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
				 	<th colspan = 2>
				 		<fmt:message key="maintenance.customers.description" />
				 	</th>				 	
				</tr>
				<tr>
					<td colspan = 2>
						<textarea name="description" id="customer_description" class="campo" style="width:98%;" ></textarea>						
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveCustomer(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

