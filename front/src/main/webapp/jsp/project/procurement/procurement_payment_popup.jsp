<%@page import="es.sm2.openppm.core.model.impl.Procurementpayments"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectProcurementServlet"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.core.model.impl.Activityseller"%>

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
  ~ File: procurement_payment_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Validations --%>
<fmt:message key="error" var="fmtError" />
<fmt:message key="maintenance.procurement.edit_payment" var="edit_payment_title" />
<fmt:message key="maintenance.procurement.new_payment" var="new_payment_title" />
<fmt:message key="maintenance.error_msg_select_a" var="fmtSellerRequired">
	<fmt:param><b><fmt:message key="procurement.seller_name"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtPlannedDateFormat">
    <fmt:param><b><fmt:message key="procurement.payment_schedule.planned_date"/></b></fmt:param>
    <fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtActualDateFormat">
    <fmt:param><b><fmt:message key="procurement.payment_schedule.actual_date"/></b></fmt:param>
    <fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtPlannedStartDateAfterFinish">
    <fmt:param><b><fmt:message key="procurement.payment_schedule.planned_date"/></b></fmt:param>
    <fmt:param><b>${minDate}</b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtActualStartDateAfterFinish">
    <fmt:param><b><fmt:message key="procurement.payment_schedule.actual_date"/></b></fmt:param>
    <fmt:param><b>${minDate}</b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >
<!--
var paymentValidator;

function addPayment() {
	var f = document.forms["frm_payment_popup"];
	f.reset();
	f.idPayment.value = "";
	$("#<%=Procurementpayments.PAYMENTSCHEDULEINFO %>").text("");
    $("#<%=Procurementpayments.DEDUCTIBLE %>").attr("checked", false);
    $("#<%=Procurementpayments.CONCEPT %>").text("");
	$('div#payment_popup legend').html('${new_payment_title}');
	$('div#payment_popup').dialog('open');
}

function editPayment(element) {	
	
	var payment = paymentTable.fnGetData( element.parentNode.parentNode.parentNode );
		
	var f = document.forms["frm_payment_popup"];
	f.reset();
	
	f.idPayment.value 								= payment[0];	
	f.<%=Procurementpayments.SELLER %>.value 		= payment[1];
	f.<%=Procurementpayments.PURCHASEORDER %>.value = unEscape(payment[4]);
	f.<%=Procurementpayments.PLANNEDDATE%>.value 	= payment[5];
	f.<%=Procurementpayments.ACTUALDATE%>.value 	= payment[6];
	f.<%=Procurementpayments.PLANNEDPAYMENT%>.value = payment[7];
	f.<%=Procurementpayments.ACTUALPAYMENT%>.value 	= payment[8];
	$("#<%=Procurementpayments.PAYMENTSCHEDULEINFO %>").text(unEscape(payment[9]));
    $("#<%=Procurementpayments.DEDUCTIBLE %>").attr("checked", payment[10] === 'true');
    $("#<%=Procurementpayments.CONCEPT %>").text(unEscape(payment[11]));
		
	$('div#payment_popup legend').html('${edit_payment_title}');	
	$('div#payment_popup').dialog('open');
}

function savePayment() {
	
	if (paymentValidator.form()) {		
		var f = document.forms["frm_payment_popup"];		
		f.action = "<%=ProjectProcurementServlet.REFERENCE%>";
		f.accion.value = "<%=ProjectProcurementServlet.SAVE_PAYMENT %>";
		loadingPopup();
		f.submit();
	}
}

function closePayment() {
	$('div#payment_popup').dialog('close');
}

//When document is ready
readyMethods.add(function() {
	
	$('div#payment_popup').dialog({ 
		autoOpen: false, 
		modal: true, 
		width: 650,
		resizable: false,
		open: function(event, ui) { paymentValidator.resetForm(); }
	});
	
	// validate the form when it is submitted
	paymentValidator = $("#frm_payment_popup").validate({
		errorContainer: 'div#payment-errors',
		errorLabelContainer: 'div#payment-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Procurementpayments.SELLER %> : { required: true },
            <%=Procurementpayments.PURCHASEORDER %>: { maxlength: 50 },
			<%=Procurementpayments.PAYMENTSCHEDULEINFO %>: { maxlength: 200 },
            <%=Procurementpayments.PLANNEDDATE %>: {date:true, maxDateTo: '#minDate' },
            <%=Procurementpayments.ACTUALDATE %>: {date:true, maxDateTo: '#minDate' }
		},
		messages: {
			<%=Procurementpayments.SELLER %> : {required: '${fmtSellerRequired}' },
            <%=Procurementpayments.PLANNEDDATE %>: {date: '${fmtPlannedDateFormat}', maxDateTo: '${fmtPlannedStartDateAfterFinish}'},
            <%=Procurementpayments.ACTUALDATE %>: {date: '${fmtActualDateFormat}', maxDateTo: '${fmtActualStartDateAfterFinish}'}
        }
	});
});
//-->
</script>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<script type="text/javascript">
	<!--
		readyMethods.add(function() {

			$('#<%=Procurementpayments.PLANNEDDATE %>').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,
                minDate:  $("#minDate").val(),  // Restriction only dates two years before actual date
				onSelect: function() {
					if (paymentValidator.numberOfInvalids() > 0) paymentValidator.form();
				}
			});
			
			$('#<%=Procurementpayments.ACTUALDATE %>').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,
                minDate:  $("#minDate").val(), // Restriction only dates two years before actual date
				onSelect: function() {
					if (paymentValidator.numberOfInvalids() > 0) paymentValidator.form();
				}
			});
		});
	//-->
	</script>
</c:if>

<div id="payment_popup" class="popup">

	<div id="payment-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_payment_popup" id="frm_payment_popup">	
		<input type="hidden" name="id" value="${project.idProject}" /> <!-- idProject -->
		<input type="hidden" name="idPayment" />
        <input type="hidden" name="minDate" id="minDate"  value="${minDate}"/>
        <input type="hidden" name="accion" />
		
		<fieldset>
			<legend></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="30%"><fmt:message key="procurement.seller_name"/>&nbsp;*</th>
					<td colspan = "3">
						<select name="<%=Procurementpayments.SELLER %>" id="<%=Procurementpayments.SELLER %>" class="campo">
							<option value=""><fmt:message key="select_opt" /></option>
                            <c:forEach var="seller" items="${procurementSellers}">
                                <option value="${seller.idSeller}">${seller.name}</option>
                            </c:forEach>
						</select>							
					</td>							
				</tr>
				<tr>
					<th class="left"><fmt:message key="procurement.payment_schedule.purchase_order"/>&nbsp;</th>
					<td colspan = "3">
						<input type="text" name="<%=Procurementpayments.PURCHASEORDER %>" id="<%=Procurementpayments.PURCHASEORDER %>" class="campo" maxlength="50"/>
					</td>							
				</tr>	
				<tr>
					<th class="left"><fmt:message key="procurement.payment_schedule.planned_date"/>&nbsp;</th>
					<td width="25%"><input type="text" name="<%=Procurementpayments.PLANNEDDATE %>" id="<%=Procurementpayments.PLANNEDDATE %>" class="campo fecha" /></td>					
					<th class="left" width="20%"><fmt:message key="procurement.payment_schedule.actual_date"/>&nbsp;</th>
					<td width="30%"><input type="text" name="<%=Procurementpayments.ACTUALDATE %>" id="<%=Procurementpayments.ACTUALDATE %>" class="campo fecha" /></td>
				</tr>	
				<tr>
					<th class="left"><fmt:message key="procurement.payment_schedule.planned_payment"/>&nbsp;</th>
					<td><input type="text" name="<%=Procurementpayments.PLANNEDPAYMENT %>" id="<%=Procurementpayments.PLANNEDPAYMENT %>" class="campo importe negativo" /></td>
					<th class="left"><fmt:message key="procurement.payment_schedule.actual_payment"/>&nbsp;</th>
					<td><input type="text" name="<%=Procurementpayments.ACTUALPAYMENT %>" id="<%=Procurementpayments.ACTUALPAYMENT %>" class="campo importe negativo" /></td>
				</tr>	
				<tr>
					<th class="left" colspan ="4"><fmt:message key="procurement.payment_schedule.payment_info"/>&nbsp;</th>
				</tr>
				<tr>
					<td colspan ="4">
						<textarea name="<%=Procurementpayments.PAYMENTSCHEDULEINFO %>" id="<%=Procurementpayments.PAYMENTSCHEDULEINFO %>" class="campo" style="width:98%;" maxlength="200"></textarea>
					</td>
				</tr>
                <tr>
                    <th class="left"><fmt:message key="procurement.payment_schedule.tax_deductible"/>&nbsp;</th>
                    <td><input type="checkbox" style="width:auto;" name="<%= Procurementpayments.DEDUCTIBLE %>" id="<%= Procurementpayments.DEDUCTIBLE %>"/></td>
                </tr>
                <tr>
                    <th class="left" colspan ="4"><fmt:message key="procurement.payment_schedule.concept"/>&nbsp;</th>
                </tr>
                <tr>
                    <td colspan ="4">
                        <textarea name="<%= Procurementpayments.CONCEPT %>" id="<%= Procurementpayments.CONCEPT %>" class="campo" style="width:98%;" maxlength="2000"></textarea>
                    </td>
                </tr>
			</table>			
    	</fieldset>
    	<div class="popButtons">
    		<c:if test="${op:hasPermission(user,project.status,tab)}">
    			<input type="submit" class="boton" onclick="savePayment(); return false;" value="<fmt:message key="save" />" />
			</c:if>
			<a href="javascript:closePayment();" class="boton"><fmt:message key="close" /></a>
    	</div>
    </form>
</div>