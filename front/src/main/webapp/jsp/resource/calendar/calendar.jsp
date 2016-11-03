<%@page import="es.sm2.openppm.core.model.impl.Operation"%>
<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.front.servlets.ResourceCalendarServlet"%>
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
  ~ File: calendar.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:04
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<c:choose>
	<c:when test="${showMonths eq 4 }">
		<script language="javascript" type="text/javascript" >var numberOfMonths = 4;</script>
	</c:when>
	<c:when test="${showMonths eq 8 }">
		<script language="javascript" type="text/javascript" >var numberOfMonths = [2,4];</script>
	</c:when>
	<c:when test="${showMonths eq 12 }">
		<script language="javascript" type="text/javascript" >var numberOfMonths = [3,4];</script>
	</c:when>
</c:choose>
	
<script language="javascript" type="text/javascript" >
<!--
var resourceCalAjax = new AjaxCall('<%=ResourceCalendarServlet.REFERENCE%>','<fmt:message key="error" />');
var changeData = false;

function saveDates() {

	var params = {
		accion : '<%=ResourceCalendarServlet.JX_SAVE_DATES%>',
		idEmployee : '${employee.idEmployee}',
		idOperation : '${idOperation}',
		dates : $('#calendar').multiDatesPicker('getDates'),
		year: '${year}'
	};

	resourceCalAjax.call(params, function() { changeData = false; });
}

function checkViewOperation() {

	if (changeData) {
		confirmUI('<fmt:message key="msg.info"/>',
				'<fmt:message key="msg.info.continue_anyway"/>',
				'<fmt:message key="yes"/>',
				'<fmt:message key="no"/>', viewOperation);
	}
	else { viewOperation(); }
}

function viewOperation() {
	var f = document.forms.frm_resource_calendar;
	loadingPopup();
	f.submit();
}
readyMethods.add(function () {
	
	$('#idOperation').change(function(){
		checkViewOperation();
	});

	$('#calendar').multiDatesPicker({
		firstDay: 1,
		dateFormat: 'dd/mm/yy',
		changeMonth: false,
		changeYear: false,
		numberOfMonths: numberOfMonths,
		defHighlight: 'selected-day',
		minDate: '<fmt:formatDate value="${since}" pattern="${datePattern}" />',
		maxDate: '<fmt:formatDate value="${until}" pattern="${datePattern}" />',
		onSelect: function() { changeData = true; }
	});
});
//-->
</script>

<c:if test="${dates ne null and not empty dates}">
<c:forEach var="date" items="${dates }">
	<c:choose>
		<c:when test="${strDates eq null }">
			<c:set var="strDates"><fmt:formatDate value="${date.dateForOperation}" pattern="${datePattern}" /></c:set>
		</c:when>
		<c:otherwise>
			<c:set var="strDates">${strDates }','<fmt:formatDate value="${date.dateForOperation}" pattern="${datePattern}" /></c:set>
		</c:otherwise>
	</c:choose>
</c:forEach>
<script language="javascript" type="text/javascript" >
<!--
readyMethods.add(function () {
	$('#calendar').multiDatesPicker('addDates',['${strDates}']);
});
//-->
</script>
</c:if>

<form id="frm_resource_calendar" name="frm_resource_calendar" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="<%=Employee.IDEMPLOYEE %>" value="${employee.idEmployee }" />
	<input type="hidden" name="since" value="<fmt:formatDate value="${since}" pattern="${datePattern}" />" />
	<input type="hidden" name="until" value="<fmt:formatDate value="${until}" pattern="${datePattern}" />" />
	
	<fieldset>
		<table border="0" cellpadding="2" cellspacing="1" width="100%">
			<tr>
				 <th><fmt:message key="maintenance.contact.full_name" /></th>
				 <th><fmt:message key="maintenance.contact.file_as" /></th>
				 <th><fmt:message key="maintenance.contact.job_title" /></th>
			</tr>
			<tr>
				<td id="<%=Contact.FULLNAME%>">${employee.contact.fullName }</td>
				<td id="<%=Contact.FILEAS%>">${employee.contact.fileAs }</td>
				<td id="<%=Contact.JOBTITLE%>">${employee.contact.jobTitle }</td>
			</tr>
   		</table>
   	</fieldset>
 	<c:if test="${not empty operations }">
	   	<div style="padding: 10px 0 5px;" class="center">
		   	<nobr>
			   	<label class="title"><fmt:message key="operation"/>:</label>&nbsp;
			   	<select class="campo" style="width: 250px" name="<%=Operation.IDOPERATION%>" id="<%=Operation.IDOPERATION%>">
			   		<c:forEach var="operation" items="${operations }">
			   			<option value="${operation.idOperation }" ${operation.idOperation eq idOperation?"selected":""}>${tl:escape(operation.operationName) }</option>
			   		</c:forEach>
			   	</select>&nbsp;
			   	<label class="title"><fmt:message key="year"/>:</label>&nbsp;
			   	<select class="campo" style="width: 65px" name="year">
			   		<c:forEach var="item" items="${years }">
			   			<option value="${item }" ${item eq year?"selected":""}>${item }</option>
			   		</c:forEach>
			   	</select>&nbsp;
			   	<label class="title"><fmt:message key="show"/>:</label>&nbsp;
			   	<select class="campo" style="width: 90px" name="showMonths">
		   			<option value="4" ${showMonths eq 4?"selected":""}>4 <fmt:message key="months"/></option>
		   			<option value="8" ${showMonths eq 8?"selected":""}>8 <fmt:message key="months"/></option>
		   			<option value="12" ${showMonths eq 12?"selected":""}>12 <fmt:message key="months"/></option>
			   	</select>&nbsp;
			   	<a href="javascript:checkViewOperation();" class="boton"><fmt:message key="refresh"/></a>
		   	</nobr>
	   	</div>
	   	<div id="calendar" style="margin: 15px auto; width: 755px;"></div>
	   	<div class="right">
		   	<a href="javascript:saveDates();" class="boton"><fmt:message key="save"/></a>
	   	</div>
   	</c:if>
</form>
<div>&nbsp;</div>