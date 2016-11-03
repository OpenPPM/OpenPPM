<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectProcurementServlet"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.core.model.impl.Projectactivity"%>
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
  ~ File: procurement_project.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<jsp:useBean id="now" class="java.util.Date" scope="page" />
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>

<fmt:setLocale value="${locale}" scope="request" />

<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_seller">
	<fmt:param><fmt:message key="seller"/></fmt:param>
</fmt:message>

<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_payment">
	<fmt:param><fmt:message key="procurement.payment_schedule.procurement_payments"/></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--

var procurementAjax = new AjaxCall('<%=ProjectProcurementServlet.REFERENCE%>','<fmt:message key="error"/>');

function deleteSeller(id) {
	
	document.forms["frm_project"].idSeller.value = id;
	confirmUI(
		'', '${msg_confirm_delete_seller}', 
		"${msg_yes}", "${msg_no}", 
		function () {
			$('#dialog-confirm').dialog("close"); 
			var f = document.forms["frm_project"];
			f.accion.value = "<%=ProjectProcurementServlet.DELETE_SELLER %>";
			loadingPopup();
			f.submit();
	});
}

function deletePayment(id) {
	document.forms["frm_project"].idPayment.value = id;
	confirmUI(
		'', '${msg_confirm_delete_payment}', 
		"${msg_yes}", "${msg_no}", 
		function () {
			$('#dialog-confirm').dialog("close"); 
			var f = document.forms["frm_project"];
			f.action = "<%= ProjectProcurementServlet.REFERENCE %>";
			f.accion.value = "<%= ProjectProcurementServlet.DELETE_PAYMENT %>";
			loadingPopup();
			f.submit();
	});
}

//When document is ready
readyMethods.add(function() {	
	sowTable = $('#tb_sow').dataTable({
		"oLanguage": datatable_language,
		"iDisplayLength": 25,
		"aaSorting": [[2,'asc'], [6,'asc']],
		"bFilter": false,				
		"sPaginationType": "full_numbers",		
		"aoColumns": [
		              { "bVisible": false },		               
		              { "bVisible": false },
		              { "sClass": "left"},
		              { "sClass": "center", "bSortable": false},
		              { "sClass": "left"},
		              { "bVisible": false },
		              { "sClass": "left"},
		              { "sClass": "left"},
		              { "bVisible": false },
		              { "sClass": "left"},	
		              { "sClass": "center", "bSortable": false }
		      		]
	});
	
	workTable = $('#tb_work').dataTable({
		"oLanguage": datatable_language,
		"iDisplayLength": 25,
		"aaSorting": [[2,'asc']],
		"iDisplayLength": 50,
		"bFilter": false,				
		"sPaginationType": "full_numbers",	
		"aoColumns": [
		              { "bVisible": false },	
		              { "bVisible": false },
		              { "sClass": "left"},
		              { "sClass": "center", "bSortable": false},
		              { "sClass": "left"},
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "left"},		              
		              { "sClass": "center", "bSortable": false },
		              { "bVisible": false },	
		              { "bVisible": false }	
		      		]
	});
	
	paymentTable = $('#tb_payment').dataTable({
		"oLanguage": datatable_language,
		"iDisplayLength": 25,
		"aaSorting": [[2,'asc']],
		"bFilter": false,				
		"sPaginationType": "full_numbers",	
		"aoColumns": [	              
		              { "bVisible": false },
		              { "bVisible": false },
		              { "sClass": "left"},
		              { "sClass": "right"},
		              { "sClass": "left"},
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "center", "sType": "es_date" },
		              { "sClass": "right"},
		              { "sClass": "right"},
		              { "sClass": "left"},
                      { "bVisible": false },
                      { "bVisible": false },
		              { "sClass": "center", "bSortable": false }
		      		]
	});
	
	budgetTable = $('#tb_budget').dataTable({
		"oLanguage": datatable_language,
		"iDisplayLength": 25,
		"aaSorting": [[0,'asc']],
		"bFilter": false,				
		"sPaginationType": "full_numbers",	
		"aoColumns": [
		              { "sClass": "left"},
		              { "sClass": "center"},
		              { "sClass": "left"},
		              { "sClass": "right"},
		              { "sClass": "right"}           
		      		]
	});
	
	sellerTable = $('#tb_seller').dataTable({
		"oLanguage": datatable_language,
		"iDisplayLength": 25,
		"aaSorting": [[2,'asc']],
		"bFilter": false,				
		"sPaginationType": "full_numbers",	
		"aoColumns": [
		              { "bVisible": false },
		              { "bVisible": false },
		              { "sClass": "left"},
		              { "sClass": "center", "bSortable": false},
		              { "sClass": "left"},
		              { "sClass": "left"},
		              { "sClass": "center", "bSortable": false }
		      		]
	});
	
});
//-->
</script>

<div id="projectTabs">
	<jsp:include page="../common/header_project.jsp">
		<jsp:param value="PR" name="actual_page"/>
	</jsp:include>

    <%-- INFORMATION OF PROJECT --%>
    <jsp:include page="../common/info_project.jsp" flush="true" />

	<div id="panels" style="padding: 15px;">
		<form name="frm_project" id="frm_project" method="post">
			<input type="hidden" name="id" id="id" value="${project.idProject}" />
			<input type="hidden" name="accion" value="" />
			<input type="hidden" name="idSeller" />
			<input type="hidden" name="idPayment" />
			<input type="hidden" id="idDocument" name="idDocument" />
			<input type="hidden" name="scrollTop" />
			
			<%-- SOW & SELECTION --%>
			<fmt:message key="procurement.sow_selection" var="titleSowSelection"/>
			<visual:panel id="sow_selection" title="${titleSowSelection }">		
				<table id="tb_sow" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th> <%-- idSeller --%>
							<th width="15%"><fmt:message key="procurement.seller_name"/></th>	
							<th width="5%"><fmt:message key="procurement.indirect"/></th>						
							<th width="25%"><fmt:message key="procurement.sow_selection.sow"/></th>
							<th width="0%">&nbsp;</th> <%-- idActivity --%>
							<th width="15%"><fmt:message key="procurement.activity_name"/></th>
							<th width="18%"><fmt:message key="procurement.sow_selection.procurement_docs"/></th>
							<th width="0%">&nbsp;</th> <%-- idProject --%>
							<th width="12%"><fmt:message key="project.associate"/></th>
							<th width="5%">
								<c:if test="${op:hasPermission(user,project.status,tab)}">
									<img onclick="addSow()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
								</c:if>								
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="activitySeller" items="${activitySellers}">
							<tr>
								<td>${activitySeller.idActivitySeller}</td>
								<td>${activitySeller.seller.idSeller}</td>
								<td>${tl:escape(activitySeller.seller.name)}</td>
								<td><input type="checkbox" id="indirect_${activitySeller.idActivitySeller}" ${activitySeller.indirect ? "checked" :"" } disabled/></td>
								<td>${tl:escape(activitySeller.statementOfWork)}</td>
								<td>${activitySeller.projectactivity.idActivity}</td>
								<td>${tl:escape(activitySeller.projectactivity.activityName)}</td>
								<td>${tl:escape(activitySeller.procurementDocuments)}</td>
								<td>${tl:escape(activitySeller.project.idProject)}</td>	
								<td>${tl:escape(activitySeller.project.projectName)}</td>														
								<td>	
									<nobr>		
										<img onclick="editSow(this)" title="<fmt:message key="edit"/>" class="link" src="images/view.png">
										<c:if test="${op:hasPermission(user,project.status,tab)}">
											&nbsp;&nbsp;&nbsp;
											<img src="images/delete.jpg" class="link" onclick="deleteSeller(${activitySeller.idActivitySeller})" />
										</c:if>
									</nobr>			
								</td>								
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</visual:panel>
			
			<%-- WORK SHEDULE --%>
			<fmt:message key="procurement.work_schedule" var="titleWorkSchedule"/>
			<visual:panel id="work_schedule" title="${titleWorkSchedule }">
				<table id="tb_work" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th> <!-- idSeller -->
							<th width="15%"><fmt:message key="procurement.seller_name"/></th>
							<th width="5%"><fmt:message key="procurement.indirect"/></th>
							<th width="15%"><fmt:message key="procurement.activity_name"/></th>
							<th width="10%"><fmt:message key="procurement.work_schedule.baseline_start"/></th>
							<th width="10%"><fmt:message key="procurement.work_schedule.start"/></th>
							<th width="10%"><fmt:message key="procurement.work_schedule.baseline_finish"/></th>
							<th width="10%"><fmt:message key="procurement.work_schedule.finish"/></th>
							<th width="23%"><fmt:message key="procurement.work_schedule.work_info"/></th>							
							<th width="3%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="activitySeller" items="${activitySellers}">
							<tr>
								<td>${activitySeller.idActivitySeller}</td>
								<td>${activitySeller.seller.idSeller}</td>
								<td>${tl:escape(activitySeller.seller.name)}</td>
								<td><input type="checkbox" ${activitySeller.indirect ? "checked" :"" } disabled/></td>
								<td>${tl:escape(activitySeller.projectactivity.activityName)}</td>
								<td><fmt:formatDate value="${activitySeller.baselineStart}" pattern="${datePattern}"/></td>
								<td><fmt:formatDate value="${activitySeller.startDate}" pattern="${datePattern}"/></td>
								<td><fmt:formatDate value="${activitySeller.baselineFinish}" pattern="${datePattern}"/></td>
								<td><fmt:formatDate value="${activitySeller.finishDate}" pattern="${datePattern}"/></td>
								<td>${tl:escape(activitySeller.workScheduleInfo)}</td>
								<td>			
									<img onclick="editWork(this)" title="<fmt:message key="edit"/>" class="link" src="images/view.png">			
								</td>								
								<td><fmt:formatDate value="${activitySeller.projectactivity.planInitDate}" pattern="${datePattern}"/></td>
								<td><fmt:formatDate value="${activitySeller.projectactivity.planEndDate}" pattern="${datePattern}"/></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>				
			</visual:panel>
			
			<%-- PAYMENTS SCHEDULE --%>
			<fmt:message key="procurement.payment_schedule" var="titlePaymentSchedule"/>
			<visual:panel id="payments_schedule" title="${titlePaymentSchedule }">
				<div class="hColor"><fmt:message key="procurement.payment_schedule.procurement_budget"/></div>
				<table id="tb_budget" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>							
							<th width="30%"><fmt:message key="procurement.seller_name"/></th>
							<th width="10%"><fmt:message key="procurement.payment_schedule.number_payment"/></th>
							<th width="40%"><fmt:message key="procurement.payment_schedule.purchase_order"/></th>
							<th width="10%"><fmt:message key="procurement.payment_schedule.planned_payment"/></th>
							<th width="10%"><fmt:message key="procurement.payment_schedule.actual_payment"/></th>						
						</tr>
					</thead>					
					<tbody>						
						<c:forEach var="procBudget" items="${procBudgets}">											
							<tr>																
								<td>${tl:escape(procBudget.seller)}</td>
								<td>${procBudget.nPayments}</td>
								<td>
                                    <c:if test="${!procBudget.purchaseOrder eq null}">${tl:escape(procBudget.purchaseOrder)}</c:if>
                                </td>
								<td>${tl:toCurrency(procBudget.plannedPayment)}</td>
								<td>${tl:toCurrency(procBudget.actualPayment)}</td>																								
							</tr>
						</c:forEach>						 
					</tbody>
				</table>		
				
				<div>&nbsp;</div>
				
				<div class="hColor"><fmt:message key="procurement.payment_schedule.procurement_payments"/></div>
				
				<table id="tb_payment" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th> <!-- idSeller -->
							<th width="10%"><fmt:message key="procurement.seller_name"/></th>
							<th width="10%"><fmt:message key="procurement.payment_schedule.n_payment"/></th>
							<th width="10%"><fmt:message key="procurement.payment_schedule.purchase_order"/></th>
							<th width="10%"><fmt:message key="procurement.payment_schedule.planned_date"/></th>
							<th width="10%"><fmt:message key="procurement.payment_schedule.actual_date"/></th>
							<th width="10%"><fmt:message key="procurement.payment_schedule.planned_payment"/></th>
							<th width="10%"><fmt:message key="procurement.payment_schedule.actual_payment"/></th>
							<th width="22%"><fmt:message key="procurement.payment_schedule.payment_info"/></th>
                            <th width="0%"><fmt:message key="procurement.payment_schedule.tax_deductible"/></th>
                            <th width="0%"><fmt:message key="procurement.payment_schedule.concept"/></th>
                            <th width="8%">
								<c:if test="${op:hasPermission(user,project.status,tab)}">
									<img onclick="addPayment()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
								</c:if>
							</th>
						</tr>
					</thead>
					<tbody>					
						<c:set var="actual" value="-1"/>
						<c:forEach var="procPayment" items="${procPayments}">												
							<c:choose>										
								<c:when test="${actual eq procPayment.seller.idSeller}">									
									<c:set var="cont" value="${cont + 1}" />																						 											
								</c:when>
								<c:otherwise>
									<c:set var="cont" value="1" />
									<c:set var="actual" value="${procPayment.seller.idSeller}" />									
								</c:otherwise>
							</c:choose>							
							<tr>
								<td>${procPayment.idProcurementPayment}</td>
								<td>${procPayment.seller.idSeller}</td>
								<td>${procPayment.seller.name}</td>
								<td>									
									<c:out value="${cont}" />&nbsp;&nbsp;&nbsp;																																				
								</td>
								<td>${tl:escape(procPayment.purchaseOrder)}</td>
								<td><fmt:formatDate value="${procPayment.plannedDate}" pattern="${datePattern}"/></td>
								<td><fmt:formatDate value="${procPayment.actualDate}" pattern="${datePattern}"/></td>								
								<td>${tl:toCurrency(procPayment.plannedPayment)}</td>
								<td>${tl:toCurrency(procPayment.actualPayment)}</td>
								<td>${tl:escape(procPayment.paymentScheduleInfo)}</td>
                                <td>${procPayment.deductible}</td>
                                <td>${tl:escape(procPayment.concept)}</td>
								<td>
									<nobr>
										<img onclick="editPayment(this)" title="<fmt:message key="edit"/>" class="link" src="images/view.png">
										<c:if test="${op:hasPermission(user,project.status,tab)}">
											&nbsp;&nbsp;&nbsp;
											<img src="images/delete.jpg" class="link" onclick="deletePayment(${procPayment.idProcurementPayment})" />
										</c:if>
									</nobr>			
								</td>								
							</tr>
						</c:forEach>
					</tbody>
				</table>								
			</visual:panel>
			
			<%-- SELLERS PERFORMANCE --%>
			<fmt:message key="procurement.seller_performance" var="titleSellerPerformance"/>
			<visual:panel id="sellers_performance" title="${titleSellerPerformance }">
				<table id="tb_seller" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th width="0%">&nbsp;</th>
							<th width="0%">&nbsp;</th> <!-- idSeller -->
							<th width="25%"><fmt:message key="procurement.seller_name"/></th>
							<th width="5%"><fmt:message key="procurement.indirect"/></th>
							<th width="25%"><fmt:message key="procurement.activity_name"/></th>							
							<th width="45%"><fmt:message key="procurement.seller_performance.seller_info"/></th>							
							<th width="5%">&nbsp;</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="activitySeller" items="${activitySellers}">
							<tr>
								<td>${activitySeller.idActivitySeller}</td>
								<td>${activitySeller.seller.idSeller}</td>
								<td>${tl:escape(activitySeller.seller.name)}</td>
								<td><input type="checkbox" ${activitySeller.indirect ? "checked" :"" } disabled/></td>
								<td>${tl:escape(activitySeller.projectactivity.activityName)}</td>								
								<td>${tl:escape(activitySeller.sellerPerformanceInfo)}</td>
								<td>		
									<img onclick="editSeller(this)" title="<fmt:message key="edit"/>" class="link" src="images/view.png">			
								</td>								
							</tr>
						</c:forEach>
					</tbody>
				</table>							
			</visual:panel>
		</form>
		
		<%--  DOCUMENTATION --%>
		<fmt:message key="documentation.procurement" var="titleDocumentationProcurement"/>
		<visual:panel title="${titleDocumentationProcurement}">
			<jsp:include page="../common/project_documentation.jsp">		
				<jsp:param name="documentationType" value="<%=Constants.DOCUMENT_PROCUREMENT %>"/>
			</jsp:include>
		</visual:panel>
	</div>
</div>

<jsp:include page="sow_selection_popup.jsp" flush="true" />
<jsp:include page="work_schedule_popup.jsp" flush="true" />
<jsp:include page="procurement_payment_popup.jsp" flush="true" />
<jsp:include page="seller_performance_popup.jsp" flush="true" />