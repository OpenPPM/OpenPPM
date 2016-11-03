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
  ~ File: mant_business_driver_set.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_business_driver_set">
	<fmt:param><fmt:message key="business_driver_set"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_business_driver_set">
	<fmt:param><fmt:message key="business_driver_set"/></fmt:param>
</fmt:message>

<%-- VARS JSP --%>
<c:set var="Inconsistent">I</c:set>

<%-- 
Request Attributes:
	list_maintenance: list
--%>

<script language="javascript" type="text/javascript" >
// GLOBAL VARS
var mainAjax = new AjaxCall('<%=MaintenanceServlet.REFERENCE%>','<fmt:message key="error"/>');
var businessDriverSetTable;
var businessDriverTable;

// OBJECTS
var businessDriverSet = {
	createTable: function() {
		return $('#businessDriverSetTable').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"bInfo": false,
			"aaSorting": [[1,'asc'], [2,'asc']],
			"aoColumns": [ 
				{ "sClass": "center", "bVisible": false },
				{ "sClass": "left" },
				{ "sClass": "center"},
				{ "sClass": "center", "bSortable": false}
	      	]
		});
	},
	add: function() {
		businessDriverSetPopup.formReset(document.businessDriverSetPopupForm);
		
		businessDriverSetPopup.open();
	},
	edit: function(element) {
		var row = businessDriverSetTable.fnGetData( element.parentNode.parentNode);
		
		var f = document.businessDriverSetPopupForm;
		
		businessDriverSetPopup.formReset(f);
		
		f.<%= Businessdriverset.IDBUSINESSDRIVERSET %>.value 	= row[0];
		f.<%= Businessdriverset.NAME %>.value 					= unEscape(row[1]);
		
		businessDriverSetPopup.open();
	},
	save: function() {
		var f = document.businessDriverSetPopupForm;
		
		var params={
			accion: 										"<%= MaintenanceServlet.JX_SAVE_BUSINESS_DRIVER_SET %>",
			<%= Businessdriverset.IDBUSINESSDRIVERSET %>: 	f.<%= Businessdriverset.IDBUSINESSDRIVERSET %>.value,
			<%= Businessdriverset.NAME %>: 					f.<%= Businessdriverset.NAME %>.value
		};
		
		mainAjax.call(params, function(data){
			
			var dataRow = [ 
              	data.<%= Businessdriverset.IDBUSINESSDRIVERSET %>,
				escape(f.<%= Businessdriverset.NAME %>.value),
				data.priorization == '${Inconsistent}' ? '<fmt:message key="business_driver_set.inconsistent" />' : '<fmt:message key="business_driver_set.consistent" />',
				'<img onclick="businessDriverSet.view(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="businessDriverSet.edit(this)" title="<fmt:message key="view"/>" class="link" src="images/modify.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="businessDriverSet.deleteBD(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">'
		    ];
			
			if (f.<%= Businessdriverset.IDBUSINESSDRIVERSET %>.value == data.<%= Businessdriverset.IDBUSINESSDRIVERSET %>) { 
				businessDriverSetTable.fnUpdateAndSelect(dataRow); 
			}
			else { 
				businessDriverSetTable.fnAddDataAndSelect(dataRow); 
			}

			businessDriverSetPopup.close();
		});	
		
	},
	deleteBD: function(element) {
		confirmUI(
				'${msg_title_confirm_delete_business_driver_set}','${msg_confirm_delete_business_driver_set}',
				'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
				function() {
					
					var row = businessDriverSetTable.fnGetData( element.parentNode.parentNode);
					
					var params={
						accion: 										"<%= MaintenanceServlet.JX_DELETE_BUSINESS_DRIVER_SET %>",
						<%= Businessdriverset.IDBUSINESSDRIVERSET %>: 	row[0]
					};
					
					mainAjax.call(params, function(data){
						businessDriverSetTable.fnDeleteSelected();
						
						$("#viewBusinessDrivers").css("display","none");
					});	
				}
		);
	},
	view: function(element) {
		var row = businessDriverSetTable.fnGetData( element.parentNode.parentNode);
		
		var params={
			accion: 										"<%= MaintenanceServlet.JX_VIEW_BUSINESS_DRIVERS %>",
			<%= Businessdriverset.IDBUSINESSDRIVERSET %>: 	row[0]
		};
		
		mainAjax.call(params, function(data){
			
			businessDriverTable.fnClearTable();
			
			var totalPriority = 0.0;
			
			$(data.businessDrivers).each(function() {
				var dataRow = [ 
				              	this.<%= Businessdriver.IDBUSINESSDRIVER %>,
								escape(this.<%= Businessdriver.CODE %>), 
								escape(this.<%= Businessdriver.NAME %>),
								toCurrency(this.<%= Businessdriver.RELATIVEPRIORIZATION %>),
								'<img onclick="businessDriver.edit(this)" title="<fmt:message key="view"/>" class="link" src="images/modify.png">'
							];
				
				totalPriority += typeof this.<%= Businessdriver.RELATIVEPRIORIZATION %> === 'undefined'? 0.0 : this.<%= Businessdriver.RELATIVEPRIORIZATION %>;
				
				businessDriverTable.fnAddData(dataRow);
			});
			
			$("#viewBusinessDrivers").css("display","block");
			
			$("#totalPriority").html(toCurrency(totalPriority));
			
		});	
	}
};

var businessDriver = {
	createTable: function() {
		return $('#businessDriverTable').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"bInfo": false,
			"bAutoWidth": false,
			"aaSorting": [[1,'asc'], [2,'asc']],
			"aoColumns": [ 
				{ "sClass": "center", "bVisible": false },
				{ "sClass": "left" },
				{ "sClass": "left" },
				{ "sClass": "center"},
				{ "sClass": "center", "bSortable": false}
	      	]
		});
	},
	edit: function(element) {
		var row = businessDriverTable.fnGetData( element.parentNode.parentNode);
		
		var f = document.businessDriverPopupForm;
		
		businessDriverPopup.formReset(f);
		
		f.<%= Businessdriver.IDBUSINESSDRIVER %>.value 			= row[0];
		f.<%= Businessdriver.CODE %>.value 						= unEscape(row[1]);
		f.<%= Businessdriver.NAME %>.value 						= unEscape(row[2]);
		f.<%= Businessdriver.RELATIVEPRIORIZATION %>.value 		= row[3];
		
		businessDriverPopup.open();
	},
	save : function() {
		var f = document.businessDriverPopupForm;
		
		var params={
			accion: 									"<%= MaintenanceServlet.JX_SAVE_BUSINESS_DRIVER %>",
			<%= Businessdriver.IDBUSINESSDRIVER %>: 	f.<%= Businessdriver.IDBUSINESSDRIVER %>.value,
			<%= Businessdriver.RELATIVEPRIORIZATION %>: toNumber(f.<%= Businessdriver.RELATIVEPRIORIZATION %>.value)
		};
		
		mainAjax.call(params, function(data) {
	 		consMaintenance();
		});	
	}
};


readyMethods.add(function () {
	
	// CREATE TABLES
	businessDriverSetTable 	= businessDriverSet.createTable();
	businessDriverTable 	= businessDriver.createTable();
	
	// EVENTS
	$('#businessDriverSetTable tbody tr').live('click',  function (event) {
		businessDriverSetTable.fnSetSelectable(this,'DTTT_selected');
	});
	
	$('#businessDriverTable tbody tr').live('click',  function (event) {
		businessDriverTable.fnSetSelectable(this,'selected_internal');
	});
	
});

</script>

<fieldset>
	<legend><fmt:message key="maintenance.business_driver_set" /></legend>
	<%-- Business driver set table --%>
	<table id="businessDriverSetTable" class="tabledata" width="100%">
		<thead>
			<tr>
				 <th width="0%"></th><%-- idBusinessDriverSet --%>
				 <th width="60%"><fmt:message key="business_driver_set.name" /></th>
				 <th width="30%"><fmt:message key="business_driver_set.priorization" /></th>
				 <th width="10%">
				 	<img onclick="businessDriverSet.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="businessDriverSet" items="${list}">
				<tr>
					<td>${businessDriverSet.idBusinessDriverSet}</td>
					<td>${tl:escape(businessDriverSet.name)}</td>
					<td>
						<c:choose>
							<c:when test="${op:isCharEquals(businessDriverSet.priorization, Inconsistent)}">
								<fmt:message key="business_driver_set.inconsistent" />
							</c:when>
							<c:otherwise>
								<fmt:message key="business_driver_set.consistent" />
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<img onclick="businessDriverSet.view(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
						&nbsp;&nbsp;&nbsp;
						<img onclick="businessDriverSet.edit(this)" title="<fmt:message key="view"/>" class="link" src="images/modify.png">
						&nbsp;&nbsp;&nbsp;
						<img onclick="businessDriverSet.deleteBD(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<%-- Business Drivers associated with Business Driver Set --%>
	<div id="viewBusinessDrivers" class="content" style="display:none;">
		<fmt:message key="maintenance.business_driver" var="titleBusinessDriver"/>
		<visual:panel id="WBSPanel" title="${titleBusinessDriver}">
			<%-- Business driver table --%>
			<table id="businessDriverTable" class="tabledata" width="100%">
				<thead>
					<tr>
						 <th width="0%"></th><%-- idBusinessDriver --%>
						 <th width="12%"><fmt:message key="business_driver.code" /></th>
						 <th width="70%"><fmt:message key="business_driver.name" /></th>
						 <th width="10%"><fmt:message key="business_driver.priority" />&nbsp;%</th>
						 <th width="8%"></th>
					</tr>
				</thead>
				<tbody></tbody>
				<tfoot>
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td><div id="totalPriority" class="center"></div></td>
						<td>&nbsp;</td>
					</tr>
				</tfoot>
			</table>
		</visual:panel>
	</div>
	
</fieldset>

<div class="spacer"></div>

<jsp:include page="popups/business_driver_set_popup.jsp" flush="true" />
<jsp:include page="popups/business_driver_priorization_popup.jsp" flush="true" />