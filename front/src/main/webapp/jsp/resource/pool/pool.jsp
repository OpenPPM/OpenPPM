<%@page import="es.sm2.openppm.front.servlets.ResourceCalendarServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.front.servlets.ResourceServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

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
  ~ File: pool.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:04
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script language="javascript" type="text/javascript" >
<!--
var resourceAjax = new AjaxCall('<%=ResourceServlet.REFERENCE%>','<fmt:message key="error" />');
var resourceTable;

function calendarResource(idEmployee) {

	var f = document.forms.frm_resource_calendar;
	f.idEmployee.value = idEmployee;
	loadingPopup();
	f.submit();
}

function actionApplyFilter() {

	resourceTable.fnClearTable();
	
	var params = {
		accion: '<%=ResourceServlet.JX_FILTER_RESOURCE_POOL%>',
		<%=Contact.FULLNAME%>: $("#filterName").val()
	};
	resourceAjax.call(params, function(data) {

		$(data).each(function(){
			resourceTable.fnAddData([
          		this.idEmployee,
          		escape(this.contact.fullName),
          		escape(this.contact.fileAs),
          		escape(this.contact.jobTitle),
          		escape(this.contact.businessPhone),
          		escape(this.contact.mobilePhone),
          		escape(this.contact.email),
          		escape(this.contact.businessAddress),
          		escape(this.contact.notes),
          		toCurrency(this.costRate),
          		'<nobr><img onclick="viewResource(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">&nbsp;&nbsp;&nbsp;' +
          		'<img onclick="calendarResource('+this.idEmployee+')" title="<fmt:message key="menu.resource_calendar"/>" class="link" src="images/calendar.png"></nobr>'
          	]);
		});
	});
}

readyMethods.add(function () {

	resourceTable = $('#tb_resource_pool').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[1,'asc']],
		"bFilter": false,
		"iDisplayLength": 50,
		"aoColumns": [ 
            { "bVisible": false }, 
            { "sClass": "left" },
            { "sClass": "left" },
            { "sClass": "left" },
            { "bVisible": false },
            { "bVisible": false },
            { "sClass": "left" },
            { "bVisible": false },
            { "bVisible": false },
            { "sClass": "center" },
            { "sClass": "center", "bSortable": false }
     	]
	});
	
	loadFilterState();
	
	$('#frm_resource').submit(applyFilter);
});
//-->
</script>

<form id="frm_resource_calendar" name="frm_resource_calendar" action="<%=ResourceCalendarServlet.REFERENCE %>" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idEmployee" value="" />
</form>
<form id="frm_resource" name="frm_resource" method="post">
	<input type="hidden" name="accion" value="" />
	
	<jsp:include page="filters.inc.jsp" flush="true"/>
	
	<div>
		<table id="tb_resource_pool" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="0%">&nbsp;</th>
					<th width="25%"><fmt:message key="contact.fullname" /></th>
					<th width="20%"><fmt:message key="contact.fileas" /></th>
					<th width="20%"><fmt:message key="contact.jobtitle" /></th>
					<th width="0%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
					<th width="20%"><fmt:message key="contact.email" /></th>
					<th width="0%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
					<th width="9%"><fmt:message key="maintenance.employee.cost_rate" /></th>
					<th width="6%">&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
</form>
<div>&nbsp;</div>

<jsp:include page="resource_popup.jsp" flush="true"/>