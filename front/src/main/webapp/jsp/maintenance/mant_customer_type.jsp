<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Customertype"%>
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
  ~ File: mant_customer_type.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDelete">
	<fmt:param><fmt:message key="customer_type"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDelete">
	<fmt:param><fmt:message key="customer_type"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="customer_type.name"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >

var customerTypeTable;
var customerTypeValidator;

function addCustomerType() {
	var f = document.frm_customerType_pop;
	f.reset();
	f.<%=Customertype.IDCUSTOMERTYPE%>.value = '';
	$(f.<%=Customertype.DESCRIPTION%>).text('');
	
	$('#customerType-popup').dialog('open');
}

function saveCustomerType() {

	if (customerTypeValidator.form()) {
		
		mainAjax.call($("#frm_customerType_pop").serialize(), function (data) {
			
			var f	 = document.frm_customerType_pop;
			
			var dataRow = [
				data.<%=Customertype.IDCUSTOMERTYPE%>,
				escape(f.<%=Customertype.NAME%>.value),
				escape(f.<%=Customertype.DESCRIPTION%>.value),
				'<nobr><img onclick="editCustomerType(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteCustomerType(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
            ];

			if (f.<%=Customertype.IDCUSTOMERTYPE%>.value == "") { customerTypeTable.fnAddDataAndSelect(dataRow); } 
			else { customerTypeTable.fnUpdateAndSelect(dataRow); }
				
			$('#customerType-popup').dialog('close');
		});	
	}	
}

function editCustomerType(element) {
	
	var aData = customerTypeTable.fnGetData( element.parentNode.parentNode.parentNode );
	var f	  = document.frm_customerType_pop;
	f.reset();
	
	f.<%=Customertype.IDCUSTOMERTYPE%>.value = aData[0];
	f.<%=Customertype.NAME%>.value			 = unEscape(aData[1]);
	$(f.<%=Customertype.DESCRIPTION%>).text(unEscape(aData[2]));
	
	$('#customerType-popup').dialog('open');
}

function deleteCustomerType(element) {
	
	confirmUI(
		'${msgTitleConfirmDelete}','${msgConfirmDelete}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var aData = customerTypeTable.fnGetData( element.parentNode.parentNode.parentNode );
			var f	  = document.frm_customerType_pop;

			f.accion.value	 = "<%=MaintenanceServlet.DEL_CUSTOMER_TYPE %>";
			f.<%=Customertype.IDCUSTOMERTYPE%>.value	= aData[0];
			
			loadingPopup();
			f.submit();
	});
}


readyMethods.add(function () {

	customerTypeTable = $('#tb_customerType').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"aoColumns": [ 
			{ "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "sClass": "center", "bSortable" : false } 
     	]
	});

	$('#tb_customerType tbody tr').live('click',  function (event) {
		customerTypeTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#customerType-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 400,
		resizable: false,
		open: function(event, ui) { customerTypeValidator.resetForm(); }
	});

	customerTypeValidator = $("#frm_customerType_pop").validate({
		errorContainer: $('div#customerType-errors'),
		errorLabelContainer: 'div#customerType-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#customerType-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Customertype.NAME%>: { required: true },
			<%=Customertype.DESCRIPTION%>: { maxlength: 200 }
		},
		messages:{
			<%=Customertype.NAME%>: { required: '${fmtNameRequired}'}
		}
	});

});

</script>

<fieldset>
	<legend><fmt:message key="customer_type" /></legend>
	<table id="tb_customerType" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
			 <th width="0%">&nbsp;</th>
			 <th width="30%"><fmt:message key="customer_type.name" /></th>
			 <th width="60%"><fmt:message key="customer_type.description" /></th>
			 <th width="10%">
				<img onclick="addCustomerType()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
			 </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="customerType" items="${customerTypes}">
				<tr>
					<td>${customerType.idCustomerType}</td>
					<td>${tl:escape(customerType.name)}</td>
					<td>${tl:escape(customerType.description)}</td>
					<td>
						<nobr>
							<img onclick="editCustomerType(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" >
							&nbsp;&nbsp;&nbsp;
							<img onclick="deleteCustomerType(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
						</nobr>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>

<div id="customerType-popup" class="popup">

	<div id="customerType-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="CustomerType-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_customerType_pop" id="frm_customerType_pop">
		<input type="hidden" name="accion" value="<%=MaintenanceServlet.JX_SAVE_CUSTOMER_TYPE%>"/>
		<input type="hidden" name="<%=Customertype.IDCUSTOMERTYPE%>"/>
		<input type="hidden" name="idManten" value="<%=Constants.MANT_CUSTOMER_TYPE%>"/>
		<fieldset>
			<legend><fmt:message key="customer_type"/></legend>
			<table cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="customer_type.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="<%=Customertype.NAME %>" class="campo" maxlength="50"/></td>
				</tr>
				<tr>
					<th><fmt:message key="customer_type.description" /></th>
				</tr>
				<tr>
					<td><textarea rows="4" name="<%=Customertype.DESCRIPTION%>" class="campo" style="width: 98%"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveCustomerType(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

