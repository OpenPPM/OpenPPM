<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Activityseller"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.model.impl.Seller"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectactivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>

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
  ~ File: scope_activity_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:01
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="error" var="fmt_error" />

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtEVRequired">
	<fmt:param><b><fmt:message key="activity.earned_value"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.max_value" var="fmtEVMax">
	<fmt:param><b><fmt:message key="activity.earned_value"/></b></fmt:param>
	<fmt:param>&nbsp;</fmt:param>
</fmt:message>
<fmt:message key="msg.error.zero_value" var="fmtZeroValueEV">
	<fmt:param><b><fmt:message key="percent_complete"/></b></fmt:param>
	<fmt:param><b><fmt:message key="activity.earned_value"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.zero_value" var="fmtZeroValuePOC">
	<fmt:param><b><fmt:message key="activity.earned_value"/></b></fmt:param>
	<fmt:param><b><fmt:message key="percent_complete"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var validatorScopeActivity;

function saveScopeActivity() {

	if (validatorScopeActivity.form()) {
		
		var f = document.forms["frm_scope_activity"];

		f.id.value = $('#id').val();
		f.action = "<%=ProjectControlServlet.REFERENCE%>";
		f.accion.value = "<%=ProjectControlServlet.SAVE_SCOPE_ACTIVITY_CONTROL %>";
		loadingPopup();
		f.submit();
	}
}

var activityCalculation = {

    calculateEV : function() {

        var bac = parseStringToFloat($("#scope_bac").val());
        var poc = parseStringToFloat($("#scope_poc").val());

        var calcEV  = (poc * bac) / 100;

        return round(calcEV);
    },
    calculatePOC: function() {

        var ev  = parseStringToFloat($("#scope_ev").val());
        var bac = parseStringToFloat($("#scope_bac").val());

        var calcPOC = (bac == 0 ? 0 : (ev / bac) * 100);

        return round(calcPOC);
    }
};

function updatePOC() {

    var pocCalc = activityCalculation.calculatePOC();

    $("#warningCalcAct").hide();

	$("#scope_poc").val(toCurrency(pocCalc));
}

function updateEV() {

    var evCalc  = activityCalculation.calculateEV();

    $("#warningCalcAct").hide();

	$("#scope_ev").val(toCurrency(evCalc));
}

function fmtEVMax() {
	return '${fmtEVMax}<b>'+ $("#scope_bac").val() +' </b>';
}

readyMethods.add(function() {
	
	$('div#scope-activity-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false,
		open: function(event, ui) { validatorScopeActivity.resetForm(); }
	});

	//Validate required fields
	validatorScopeActivity = $("#frm_scope_activity").validate({
		errorContainer: 'div#scope-activity-errors',
		errorLabelContainer: 'div#scope-activity-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#scope-activity-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			ev: {
				required: true,
				maxCurrency: function() { return $("#scope_bac").val(); },
				zeroValue: function() { return $("#scope_poc").val(); }
			},
			poc: {
				//zeroValue: function() { return $("#scope_ev").val(); }
			},
			<%= Projectactivity.COMMENTSPOC %>: { maxlength: 1500 }
		},
		messages: {
			ev: { required: '${fmtEVRequired}', maxCurrency: fmtEVMax , zeroValue: '${fmtZeroValueEV}'},
			poc: {zeroValue: '${fmtZeroValuePOC}'}
		}
	});

    // Event update status date
    $("#updateStatusDate").click(function() {
        saveStatusDate($("#actualDate").val());

    });

    // Event warning calculation
    $("#scope_ev, #scope_poc").bind('change',function() {

        var ev      = parseStringToFloat($("#scope_ev").val());
        var poc     = parseStringToFloat($("#scope_poc").val());
        var pocCalc = activityCalculation.calculatePOC();
        var evCalc  = activityCalculation.calculateEV();

        if (evCalc !== ev || poc !== pocCalc) {
            $("#warningCalcAct").show();
        }
        else {
            $("#warningCalcAct").hide();
        }
    });
});
//-->
</script>

<div id="scope-activity-popup" class="popup">

	<div id="scope-activity-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="scope-activity-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_scope_activity" id="frm_scope_activity" action="<%=ProjectControlServlet.REFERENCE%>" method="post">
		<input type="hidden" name="id" value="" />
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="activity_id" value="" />
        <c:set var="statusDate">
            <fmt:formatDate value="${project.statusDate}" pattern="${datePattern}"/>
        </c:set>

		
		<fieldset>
			<legend><fmt:message key="edit_activity" /></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" colspan="2"><fmt:message key="activity.name" /></th>
					<th class="left"><fmt:message key="project.status_date" /></th>
				</tr>
				<tr>
					<td colspan="2"><input type="text" name="name" class="campo" readonly="readonly" /></td>
                    <td class="center">
                    <c:choose>
                        <c:when test="${statusDate ne actualDate}">
                            <span class="<%=Project.STATUSDATE %> alert alert-warning" style="padding: 5px;">${statusDate}</span>
                            <img id="updateStatusDate" title="<fmt:message key="UPDATE_STATUS_DATE" />" class="link" style="display: inline-block;" src="images/reload.png">
                        </c:when>
                        <c:otherwise>
                            <span class="<%=Project.STATUSDATE %>" style="padding: 5px;">${statusDate}</span>
                            <img id="updateStatusDate" title="<fmt:message key="UPDATE_STATUS_DATE" />" class="link" style="display: none;" src="images/reload.png">
                        </c:otherwise>
                    </c:choose>
                    </td>
				</tr>
				<tr>
					<th class="left" width="40%"><fmt:message key="activity.earned_value" />&nbsp;*</th>
					<th class="left" width="35%"><fmt:message key="percent_complete" /></th>
					<th class="left" width="25%"><fmt:message key="activity.budget" /></th>
				</tr>
				<tr>
					<td>
						<input type="text" name="ev" id="scope_ev" class="campo importe" style="width:90px;" />
						<a href="javascript:updateEV();" class="boton"><fmt:message key="calculate" /></a>
					</td>
					<td>
						<input type="text" name="poc" id="scope_poc" class="campo importe" style="width:60px;" />
						<a href="javascript:updatePOC();" class="boton"><fmt:message key="calculate" /></a>
					</td>
					<td><input type="text" name="bac" id="scope_bac" class="importe" readonly="readonly" style="width:90%;" /></td>
				</tr>
                <tr>
                    <td colspan="3">
                        <div class="alert alert-warning" id="warningCalcAct" style="display: none;">
                            <fmt:message key="ACTIVITY.WARNING_CALCULATION" />
                        </div>
                    </td>
                </tr>
				<tr>
					<th class="left" colspan="3"><fmt:message key="activity.description" /></th>
				</tr>
				<tr>
					<td colspan="3" id="activityDescription"></td>
				</tr>
				<tr>
					<th class="left" colspan="3"><fmt:message key="activity.comments" /></th>
				</tr>
				<tr>
					<td colspan="3"><textarea style="width:98%;" class="campo valid" id="<%= Projectactivity.COMMENTSPOC %>" name="<%= Projectactivity.COMMENTSPOC %>" rows="8" maxlength="1500"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	
    	<div style="display:block"> 
	    	<fieldset>
				<legend><fmt:message key="followup.ac"/>:</legend>
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr>
						<th class="center" width="33%"><fmt:message key="hours" />&nbsp;<%= Constants.TIMESTATUS_APP1 %></th>
						<th class="center" width="33%"><fmt:message key="hours" />&nbsp;<%= Constants.TIMESTATUS_APP2 %></th>
						<th class="center" width="33%"><fmt:message key="hours" />&nbsp;<%= Constants.TIMESTATUS_APP3 %></th>
					</tr>
					<tr>
						<td><div class="center" id="hours<%= Constants.TIMESTATUS_APP1 %>"></div></td>
						<td><div class="center" id="hours<%= Constants.TIMESTATUS_APP2 %>"></div></td>
						<td><div class="center" id="hours<%= Constants.TIMESTATUS_APP3 %>"></div></td>
					</tr>
					<tr>
						<th class="center" width="33%">AC&nbsp;<%= Constants.TIMESTATUS_APP1 %></th>
						<th class="center" width="33%">AC&nbsp;<%= Constants.TIMESTATUS_APP2 %></th>
						<th class="center" width="33%">AC&nbsp;<%= Constants.TIMESTATUS_APP3 %></th>
					</tr>
					<tr>
						<td><div class="center" id="AC<%= Constants.TIMESTATUS_APP1 %>"></div></td>
						<td><div class="center" id="AC<%= Constants.TIMESTATUS_APP2 %>"></div></td>
						<td><div class="center" id="AC<%= Constants.TIMESTATUS_APP3 %>"></div></td>
					</tr>
	    		</table>
			</fieldset>
    	</div>
    	
    	<div id="associated" style="display:none">
	    	<fieldset>
				<legend><fmt:message key="activity.associated" />:</legend>
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr>
						<th class="left" width="25%"><fmt:message key="seller"/></th>
						<th class="left" width="20%"><fmt:message key="project"/></th>
						<th class="left" width="25%"><fmt:message key="percent_complete"/></th>
						<th class="left" width="30%"><fmt:message key="project.status_date" /></th>
					</tr>
					<tr>
						<td><div id="<%= Seller.NAME %>Associated"></div></td>
						<td><div id="<%= Project.PROJECTNAME %>Associated"></div></td>
						<td class="center"><div id="<%= Projectactivity.POC %>Associated"></div></td>
						<td><div id="<%= Project.STATUSDATE %>Associated"></div></td>
					</tr>
	    		</table>
			</fieldset>
		</div>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveScopeActivity(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>