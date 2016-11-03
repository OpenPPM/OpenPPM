<%@page import="es.sm2.openppm.core.model.impl.Jobcategory"%>
<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
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
  ~ File: mant_job_category.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete">
	<fmt:param><fmt:message key="job_category"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm">
	<fmt:param><fmt:message key="job_category"/></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: list
--%>

<script language="javascript" type="text/javascript" >
// GLOBAL VARS
var mainAjax = new AjaxCall('<%=MaintenanceServlet.REFERENCE%>','<fmt:message key="error"/>');
var jobCategoryTable;

// OBJECTS
var jobCategory = {
	createTable: function() {
		return $('#jobCategoryTable').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"bInfo": false,
			"aaSorting": [[1,'asc']],
			"aoColumns": [ 
				{ "sClass": "center", "bVisible": false },
				{ "sClass": "left" },
				{ "sClass": "left" },
				{ "sClass": "center", "bSortable": false}
	      	]
		});
	},
	add: function() {
		jobCategoryPopup.formReset(document.jobCategoryPopupForm);
		
		jobCategoryPopup.open();
	},
	view: function(element) {
		var row = jobCategoryTable.fnGetData(element.parentNode.parentNode);
		
		var f = document.jobCategoryPopupForm;
		
		jobCategoryPopup.formReset(f);
		
		f.<%= Jobcategory.IDJOBCATEGORY %>.value 			= row[0];
		f.<%= Jobcategory.NAME %>.value 					= unEscape(row[1]);
		f.<%= Jobcategory.DESCRIPTION %>.value 				= unEscape(row[2]);
		
		jobCategoryPopup.open();
	},
	save: function() {
		var f = document.jobCategoryPopupForm;
		
		var params={
			accion: 							"<%= MaintenanceServlet.JX_SAVE_JOB_CATEGORY %>",
			<%= Jobcategory.IDJOBCATEGORY %>: 	f.<%= Jobcategory.IDJOBCATEGORY %>.value,
			<%= Jobcategory.NAME %>: 			f.<%= Jobcategory.NAME %>.value,
			<%= Jobcategory.DESCRIPTION %>: 	f.<%= Jobcategory.DESCRIPTION %>.value,
		};
		
		mainAjax.call(params, function(data) {
			
			var dataRow = [ 
              	data.<%= Jobcategory.IDJOBCATEGORY %>,
				escape(f.<%= Jobcategory.NAME %>.value), 
				escape(f.<%= Jobcategory.DESCRIPTION %>.value),
				'<img onclick="jobCategory.view(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="jobCategory.deleteBD(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">'
		    ];
			
			if (f.<%= Jobcategory.IDJOBCATEGORY %>.value == data.<%= Jobcategory.IDJOBCATEGORY %>) { 
				jobCategoryTable.fnUpdateAndSelect(dataRow); 
			}
			else { 
				jobCategoryTable.fnAddDataAndSelect(dataRow); 
			}

			jobCategoryPopup.close();
		});	
		
	},
	deleteBD: function(element) {
		confirmUI(
				'${msg_title_confirm_delete}','${msg_confirm_delete}',
				'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
				function() {
					
					var row = jobCategoryTable.fnGetData(element.parentNode.parentNode);
					
					var params={
						accion: 							"<%= MaintenanceServlet.JX_DELETE_JOB_CATEGORY %>",
						<%= Jobcategory.IDJOBCATEGORY %>: 	row[0]
					};
					
					mainAjax.call(params, function(data){
						jobCategoryTable.fnDeleteSelected();
					});	
				}
		);
	}
	
};

readyMethods.add(function () {
	// CREATE TABLES
	jobCategoryTable = jobCategory.createTable();
	
	// EVENTS
	$('#jobCategoryTable tbody tr').live('click',  function (event) {
		jobCategoryTable.fnSetSelectable(this,'selected_internal');
	});
});

</script>

<fieldset>
	<legend><fmt:message key="maintenance.job_category" /></legend>
	<%-- Risk templates table --%>
	<table id="jobCategoryTable" class="tabledata" width="100%">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th>
				<th width="22%"><fmt:message key="job_category.name"/></th>
				<th width="70%"><fmt:message key="job_category.description"/></th>
				 <th width="8%">
				 	<img onclick="jobCategory.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${list}">
				<tr>
					<td>${item.idJobCategory}</td>
					<td>${item.name}</td>
					<td>${item.description}</td>
					<td>
						<img onclick="jobCategory.view(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
						&nbsp;&nbsp;&nbsp;
						<img onclick="jobCategory.deleteBD(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>	

<div class="spacer"></div>

<jsp:include page="popups/job_category_popup.jsp" flush="true" />
