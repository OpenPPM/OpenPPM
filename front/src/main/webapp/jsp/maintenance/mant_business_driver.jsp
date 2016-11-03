<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Businessdriverset"%>
<%@page import="es.sm2.openppm.core.model.impl.Businessdriver"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@taglib uri="Visual" prefix="visual" %>

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
  ~ File: mant_business_driver.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_business_driver">
	<fmt:param><fmt:message key="business_driver"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_business_driver">
	<fmt:param><fmt:message key="business_driver"/></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: list
--%>

<script language="javascript" type="text/javascript" >
// GLOBAL VARS
var mainAjax = new AjaxCall('<%=MaintenanceServlet.REFERENCE%>','<fmt:message key="error"/>');
var businessDriverTable;

// OBJECTS
var businessDriver = {
	createTable: function() {
		return $('#businessDriverTable').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"bInfo": false,
			"aaSorting": [[1,'asc'], [2,'asc']],
			"aoColumns": [ 
				{ "sClass": "center", "bVisible": false },
				{ "sClass": "left" },
				{ "sClass": "left" },
				{ "sClass": "center", "bVisible": false },
				{ "sClass": "center"},
				{ "sClass": "center", "bSortable": false}
	      	]
		});
	},
	add: function() {
		businessDriverPopup.formReset(document.businessDriverPopupForm);
		
		businessDriverPopup.open();
	},
	view: function(element) {
		var row = businessDriverTable.fnGetData( element.parentNode.parentNode);
		
		var f = document.businessDriverPopupForm;
		
		businessDriverPopup.formReset(f);
		
		f.<%= Businessdriver.IDBUSINESSDRIVER %>.value 			= row[0];
		f.<%= Businessdriver.CODE %>.value 						= unEscape(row[1]);
		f.<%= Businessdriver.NAME %>.value 						= unEscape(row[2]);
		f.<%= Businessdriverset.IDBUSINESSDRIVERSET %>.value 	= row[3];
		
		businessDriverPopup.open();
	},
	save: function() {
		var f = document.businessDriverPopupForm;
		
		var params={
			accion: 										"<%=MaintenanceServlet.JX_SAVE_BUSINESS_DRIVER %>",
			<%= Businessdriver.IDBUSINESSDRIVER %>: 		f.<%= Businessdriver.IDBUSINESSDRIVER %>.value,
			<%= Businessdriver.CODE %>: 					f.<%= Businessdriver.CODE %>.value,
			<%= Businessdriver.NAME %>: 					f.<%= Businessdriver.NAME %>.value,
			<%= Businessdriverset.IDBUSINESSDRIVERSET %>: 	f.<%= Businessdriverset.IDBUSINESSDRIVERSET %>.value
		};
		
		mainAjax.call(params, function(data){
			
			var dataRow = [ 
              	data.<%= Businessdriver.IDBUSINESSDRIVER %>,
				escape(f.<%= Businessdriver.CODE %>.value), 
				escape(f.<%= Businessdriver.NAME %>.value),
				f.<%= Businessdriverset.IDBUSINESSDRIVERSET %>.value,
				escape(f.<%= Businessdriverset.IDBUSINESSDRIVERSET %>.options[f.<%= Businessdriverset.IDBUSINESSDRIVERSET %>.selectedIndex].text),
				'<img onclick="businessDriver.view(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="businessDriver.deleteBD(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">'
		    ];
			
			if (f.<%= Businessdriver.IDBUSINESSDRIVER %>.value == data.<%= Businessdriver.IDBUSINESSDRIVER %>) { 
				businessDriverTable.fnUpdateAndSelect(dataRow); 
			}
			else { 
				businessDriverTable.fnAddDataAndSelect(dataRow); 
			}

			businessDriverPopup.close();
		});	
		
	},
	deleteBD: function(element) {
		confirmUI(
				'${msg_title_confirm_delete_business_driver}','${msg_confirm_delete_business_driver}',
				'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
				function() {
					
					var row = businessDriverTable.fnGetData( element.parentNode.parentNode);
					
					var params={
						accion: 								"<%=MaintenanceServlet.JX_DELETE_BUSINESS_DRIVER %>",
						<%= Businessdriver.IDBUSINESSDRIVER %>: row[0]
					};
					
					mainAjax.call(params, function(data){
						businessDriverTable.fnDeleteSelected();
					});	
				}
		);
	}
	
};

readyMethods.add(function () {
	
	// CREATE TABLES
	businessDriverTable = businessDriver.createTable();
	
	// EVENTS
	$('#businessDriverTable tbody tr').live('click',  function (event) {
		businessDriverTable.fnSetSelectable(this,'selected_internal');
	});
	
});

</script>

<fieldset>
	<legend><fmt:message key="maintenance.business_driver" /></legend>
	<%-- Business driver table --%>
	<table id="businessDriverTable" class="tabledata" width="100%">
		<thead>
			<tr>
				 <th width="0%"></th><%-- idBusinessDriver --%>
				 <th width="12%"><fmt:message key="business_driver.code" /></th>
				 <th width="50%"><fmt:message key="business_driver.name" /></th>
				 <th width="0%"></th><%-- idBusinessDriverSet --%>
				 <th width="30%"><fmt:message key="business_driver_set" /></th>
				 <th width="8%">
				 	<img onclick="businessDriver.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="businessDriver" items="${list}">
				<tr>
					<td>${businessDriver.idBusinessDriver}</td>
					<td>${tl:escape(businessDriver.code)}</td>
					<td>${tl:escape(businessDriver.name)}</td>
					<td>${businessDriver.businessdriverset.idBusinessDriverSet}</td>
					<td>${tl:escape(businessDriver.businessdriverset.name)}</td>
					<td>
						<img onclick="businessDriver.view(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
						&nbsp;&nbsp;&nbsp;
						<img onclick="businessDriver.deleteBD(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>	

<div class="spacer"></div>

<jsp:include page="popups/business_driver_popup.jsp" flush="true" />
