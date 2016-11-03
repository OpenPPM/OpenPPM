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
  ~ File: payment.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<%--
  Created by IntelliJ IDEA.
  User: jordi.ripoll
  Date: 11/09/2014
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.model.impl.Timesheet"%>
<%@page import="es.sm2.openppm.front.servlets.TimeSheetServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Procurementpayments"%>
<%@ page import="es.sm2.openppm.front.servlets.PaymentProjectsServlet" %>
<%@ page import="es.sm2.openppm.core.logic.security.actions.PaymentProjectAction" %>
<%@ page import="es.sm2.openppm.core.logic.security.actions.UtilAction" %>
<%@ page import="es.sm2.openppm.front.servlets.UtilServlet" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored ="false"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtProjectRequired">
    <fmt:param><b><fmt:message key="project"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtSellerRequired">
    <fmt:param><b><fmt:message key="seller"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtActualDateFormat">
    <fmt:param><b><fmt:message key="procurement.payment_schedule.actual_date"/></b></fmt:param>
    <fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtActualStartDateAfterFinish">
    <fmt:param><b><fmt:message key="procurement.payment_schedule.actual_date"/></b></fmt:param>
    <fmt:param><b>${minDate}</b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--

var paymAjax = new AjaxCall('<%=PaymentProjectsServlet.REFERENCE%>','<fmt:message key="error"/>');
var utilAjax = new AjaxCall('<%=UtilServlet.REFERENCE%>','<fmt:message key="error"/>');

var newPaymentValidator;

// Javascript objects
//
var payment = {

    add : function () {

        if (newPaymentValidator.form()) {

            // Call
            paymAjax.call($('#frmNewPayment').serializeArray(), function(data){

                // Reset form
                var f = document.frmNewPayment;
                var idProject = $("#idProject").val();
                f.reset();
                $("#idProject").val(idProject);

            });
        }
    },
    consultSellers : function() {

        // Call get sellers
        var params={
            accion:     "<%= PaymentProjectAction.JX_CONSULT_SELLERS.getAction() %>",
            idProject: 	$("#idProject").val()
        };

        paymAjax.call(params, function(data) {

            // Load sellers
            $("#idSeller").empty();

            $("#idSeller").append('<option value="" selected><fmt:message key="select_opt"/></option>');

            if (typeof data !== 'undefined' && typeof data.sellers !== 'undefined') {

                for (var i=0;i<data.sellers.length;i++) {
                    $("#idSeller").append('<option value="'+data.sellers[i].idSeller+'">' + data.sellers[i].name + '</option>');
                }
            }
        });

    },
    consultDocuments : function() {

        // Reset validator
        newPaymentValidator.resetForm();

        // Call get documents
        var params={
            accion:     "<%= UtilAction.JX_CONSULT_DOCUMENTS.getAction() %>",
            idProject: 	$("#idProject").val(),
            documentationType: "<%= Constants.DOCUMENT_PROCUREMENT %>",
            tab: "<%= Constants.TAB_PROCURAMENT %>"
        };

        utilAjax.call(params, function(data) {

            // but the dialogue persists
            $("#document-popup").dialog('destroy').remove();

            // Load html
            $("#loadDocuments").html(data);

            // Format Buttons
            //
            $("#buttonFile").addClass("botonDocument");
            $("#buttonCancel").addClass("botonDocument");
            $("#buttonLink").addClass("botonDocument");

            $("input.botonDocument").button();

        },'html');
    }
}

// Init event
//
readyMethods.add(function() {

    // Reset form
    var f = document.frmNewPayment;
    f.reset();

    // Event change project consult sellers
    $("#idProject").change(function(){
        payment.consultSellers();
        payment.consultDocuments();
    });

    // Event button save
    $("#addNewPayment").click(payment.add);

    // Load validator
    newPaymentValidator = $("#frmNewPayment").validate({
        errorContainer: $('div#newPayment-errors'),
        errorLabelContainer: 'div#newPayment-errors ol',
        wrapper: 'li',
        showErrors: function (errorMap, errorList) {
            $('span#newPayment-numerrors').html(this.numberOfInvalids());
            this.defaultShowErrors();
        },
        rules : {
            idProject: {required: true},
            idSeller: {required: true},
            <%=Procurementpayments.PURCHASEORDER %>: { maxlength: 50 },
            <%=Procurementpayments.PAYMENTSCHEDULEINFO %>: { maxlength: 200 },
            <%=Procurementpayments.ACTUALDATE %>: {date:true, maxDateTo: '#minDate' }
        },
        messages : {
            idProject: {required: '${fmtProjectRequired}' },
            idSeller: {required: '${fmtSellerRequired}' },
            <%=Procurementpayments.ACTUALDATE %>: {date: '${fmtActualDateFormat}', maxDateTo: '${fmtActualStartDateAfterFinish}'}
        }
    });

    // Calendar
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
            if (newPaymentValidator.numberOfInvalids() > 0) newPaymentValidator.form();
        }
    });
});

//-->
</script>

<!-- PANEL   -->
<!--         -->
<fmt:message key="payment.new" var="titlePayment"/>
<visual:panel title="${titlePayment}" callback="" showTiltePanel="false">

    <div id="newPayment-errors" class="ui-state-error ui-corner-all hide">
        <p>
            <span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
            <strong><fmt:message key="msg.error_title"/></strong>
            &nbsp;(<b><span id="newPayment-numerrors"></span></b>)
        </p>
        <ol></ol>
    </div>

    <form name="frmNewPayment" id="frmNewPayment" method="post">
        <input type="hidden" name="accion" value="<%= PaymentProjectAction.JX_ADD_PAYMENT.getAction() %>" />
        <input type="hidden" name="minDate" id="minDate"  value="${minDate}"/>

        <table border="0" cellpadding="2" cellspacing="1" width="100%">
            <tr>
                <th class="left" ><fmt:message key="project"/>&nbsp;*</th>
                <td >
                    <select name="idProject" id="idProject" class="campo">
                        <option value="" selected><fmt:message key="select_opt"/></option>
                        <c:forEach var="project" items="${projects}">
                            <option value="${project.idProject}">${project.chartLabel}</option>
                        </c:forEach>
                    </select>
                </td>
                <th class="right" ><fmt:message key="seller"/>&nbsp;*</th>
                <td colspan="5">
                    <select name="idSeller" id="idSeller" class="campo">
                        <option value=""><fmt:message key="select_opt" /></option>
                        <c:forEach var="seller" items="${procurementSellers}">
                            <option value="${seller.idSeller}">${seller.name}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>

            <tr>
                <th class="left"><fmt:message key="payment.payment_schedule.purchase_order"/>&nbsp;</th>
                <td >
                    <input type="text" name="<%=Procurementpayments.PURCHASEORDER %>" id="<%=Procurementpayments.PURCHASEORDER %>" class="campo" maxlength="50" />
                </td>
                <th class="right" ><fmt:message key="payment.payment_schedule.actual_date"/>&nbsp;</th>
                <td ><input type="text" name="<%=Procurementpayments.ACTUALDATE %>" id="<%=Procurementpayments.ACTUALDATE %>" class="campo fecha" /></td>
                <th class="right"><fmt:message key="procurement.payment_schedule.tax_deductible"/>&nbsp;</th>
                <td><input type="checkbox" class="campo" name="<%= Procurementpayments.DEDUCTIBLE %>" id="<%= Procurementpayments.DEDUCTIBLE %>"/></td>
                <th class="right"><fmt:message key="procurement.payment_schedule.actual_payment"/>&nbsp;</th>
                <td ><input type="text" name="<%=Procurementpayments.ACTUALPAYMENT %>" id="<%=Procurementpayments.ACTUALPAYMENT %>" class="campo importe negativo" style="width: 85%;"/></td>
            </tr>
            <tr>
                <th class="left" colspan ="8"><fmt:message key="payment.payment_schedule.payment_info"/>&nbsp;</th>
            </tr>
            <tr>
                <td colspan ="8">
                    <textarea name="<%=Procurementpayments.PAYMENTSCHEDULEINFO %>" id="<%=Procurementpayments.PAYMENTSCHEDULEINFO %>" class="campo" style="width:98%;" maxlength="200"></textarea>
                </td>
            </tr>
            <tr>
                <th class="left" colspan ="8"><fmt:message key="procurement.payment_schedule.concept"/>&nbsp;</th>
            </tr>
            <tr>
                <td colspan ="8">
                    <textarea name="<%= Procurementpayments.CONCEPT %>" id="<%= Procurementpayments.CONCEPT %>" class="campo" style="width:98%;" maxlength="2000"></textarea>
                </td>
            </tr>
        </table>
    </form>

    <div>&nbsp;</div>

    <%-- DOCUMENTS --%>
    <%--           --%>
    <%-- Form to documents --%>
    <form name="frm_project" id="frm_project" method="post">
        <input type="hidden" name="accion" value="" />
        <input type="hidden" id="idDocument" name="idDocument" />
    </form>

    <%-- Load documents --%>
    <div id="loadDocuments"></div>

    <div>&nbsp;</div>

    <%-- BUTTON --%>
    <%--           --%>
    <div class="popButtons">
        <input type="button" style="width: 70px;" class="boton" id="addNewPayment" value="<fmt:message key="save"/>" />
    </div>

</visual:panel>





