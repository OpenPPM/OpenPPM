<%@page import="es.sm2.openppm.core.model.impl.Riskcategories"%>
<%@page import="es.sm2.openppm.front.servlets.RiskTemplatesServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Riskregistertemplate"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
  ~ File: risk_template.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtCodeRequired">
	<fmt:param><b><fmt:message key="risk.code"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="risk.name"/></b></fmt:param>
</fmt:message>

<%-- VARS --%>
<c:set var="probTrivial"><%=Constants.PROBABILITY_TRIVIAL%></c:set>
<c:set var="probMinor"><%=Constants.PROBABILITY_MINOR%></c:set>
<c:set var="probModerate"><%=Constants.PROBABILITY_MODERATE%></c:set>
<c:set var="probHigh"><%=Constants.PROBABILITY_HIGH%></c:set>
<c:set var="probSevere"><%=Constants.PROBABILITY_SEVERE%></c:set>

<c:set var="impTrivial"><%=Constants.IMPACT_TRIVIAL%></c:set>
<c:set var="impMinor"><%=Constants.IMPACT_MINOR%></c:set>
<c:set var="impModerate"><%=Constants.IMPACT_MODERATE%></c:set>
<c:set var="impHigh"><%=Constants.IMPACT_HIGH%></c:set>
<c:set var="impSevere"><%=Constants.IMPACT_SEVERE%></c:set>

<c:set var="riskOpportunity"><%=Constants.RISK_OPPORTUNITY%></c:set>
<c:set var="riskThreat"><%=Constants.RISK_THREAT%></c:set>

<c:set var="statusClose"><%=Constants.CHAR_CLOSED%></c:set>
<c:set var="statusOpen"><%=Constants.CHAR_OPEN%></c:set>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--

var validator;
var riskTemplatesAjax = new AjaxCall('<%= RiskTemplatesServlet.REFERENCE %>','<fmt:message key="error"/>');

var riskTemplate = {
	validator: function() {
		
		return $("#riskTemplateForm").validate({
			errorContainer: 'div#riskTemplate-errors',
			errorLabelContainer: 'div#riskTemplate-errors ol',
			wrapper: 'li',
			showErrors: function(errorMap, errorList) {
				$('span#numerrors').html(this.numberOfInvalids());
				this.defaultShowErrors();
			},
			rules: {
				<%= Riskregistertemplate.RISKCODE %>: {required: true },
				<%= Riskregistertemplate.RISKNAME %>: {required: true },
				<%= Riskregistertemplate.DESCRIPTION %>: { maxlength : 500 },
				<%= Riskregistertemplate.RISKTRIGGER %>: { maxlength : 500 },
				<%= Riskregistertemplate.RESPONSEDESCRIPTION %>: { maxlength : 500 },
				<%= Riskregistertemplate.MITIGATIONACTIONSREQUIRED %>: { maxlength : 500 },
				<%= Riskregistertemplate.CONTINGENCYACTIONSREQUIRED %>: { maxlength : 500 },
				<%= Riskregistertemplate.RESIDUALRISK %>: { maxlength : 500 },
				<%= Riskregistertemplate.FINALCOMMENTS %>: { maxlength : 500 }
			},
			messages: {
				<%= Riskregistertemplate.RISKCODE %>: {required: '${fmtCodeRequired}'},
				<%= Riskregistertemplate.RISKNAME %>: {required: '${fmtNameRequired}'},
			}
		});
	},
	save: function() {
		
		if (this.validator().form()) {
			
			var f = document.riskTemplateForm;
			
			f.accion.value = "<%= RiskTemplatesServlet.SAVE_RISK_TEMPLATE %>",
			
			loadingPopup();
			f.submit();
		}
	},
	back: function() {
		
		var f = document.riskTemplateForm;
		
		f.accion.value = "",
		
		loadingPopup();
		f.submit();
	}
};

function getClassRating(riskRating) {

    var classRating = 'risk_low';

    // Parse
    if (typeof riskRating === 'string') {
        riskRating = parseInt(riskRating);
    }

    // Class
    if (riskRating > 1500) {
        classRating = 'risk_high';
    }
    else if (riskRating > 500) {
        classRating = 'risk_medium';
    }
    else if (riskRating == 0) {
        classRating = '';
    }

    return classRating;
}

 function updateRiskRating(prob, impact) {

    $('#risk_rating').removeClass('risk_high');
    $('#risk_rating').removeClass('risk_medium');
    $('#risk_rating').removeClass('risk_low');

    var p = typeof prob === 'string' ? parseInt(prob) : prob;
    var i = typeof impact === 'string' ? parseInt(impact) : impact;

    // Operation
    var riskRating = p * i;

    $('#risk_rating').addClass(getClassRating(riskRating));
}

function updateRiskRatingSelect() {

    var prob    = $('#risk_probability option:selected').val();
    var impact  = $('#risk_impact option:selected').val();

    if (prob != "" && impact != "") {
        updateRiskRating(prob, impact);
    }
    else {
        $('#risk_rating').attr("class", "riskRating");
    }
}

readyMethods.add(function () {

	riskTemplate.validator();

    updateRiskRatingSelect();

	$('#risk_probability, #risk_impact').change(function() {
		updateRiskRatingSelect();
	});
	
	$("#<%= Riskregistertemplate.RISKCATEGORY%>").selectDescription();
});


//-->
</script>

<fieldset>
	<legend><fmt:message key="risk_template" /></legend>
	<div style="padding: 15px;">
		<form name="riskTemplateForm" id="riskTemplateForm" method="post" action="<%= RiskTemplatesServlet.REFERENCE %>">
			<input type="hidden" name="accion" value="" />
			<input type="hidden" name="<%= Riskregistertemplate.IDRISK %>" id="<%= Riskregistertemplate.IDRISK %>" value="${risk.idRisk}" />
            <input type="hidden" id="risk_probability_hidden" value="${risk.probability}"/>
            <input type="hidden" id="risk_impact_hidden" value="${risk.impact}"/>
			
			<div id="riskTemplate-errors" class="ui-state-error ui-corner-all hide"  style="margin-bottom: 15px;">
				<p>
					<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
					<strong><fmt:message key="msg.error_title"/></strong>
					&nbsp;(<b><span id="numerrors"></span></b>)
				</p>
				<ol></ol>
			</div>
			
			<%-- RISK DISCOVERY AND ASSESSMENT --%>
			<fmt:message key="risk.risk_discovery" var="titleRiskDiscovery"/>
			<visual:panel id="field_risk_discovery" title="${titleRiskDiscovery}" cssClass="panel1">
				<table id="tb_risk_discovery" border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr>
						<th width="10%" class="left"><fmt:message key="risk.code"/>&nbsp;*</th>
						<td width="8%"><input type="text" name="<%= Riskregistertemplate.RISKCODE %>" id="<%= Riskregistertemplate.RISKCODE %>" class="campo code" maxlength="5" value="${tl:escape(risk.riskCode)}"/></td>
						
						<th width="5%" class="right"><fmt:message key="risk.name"/>&nbsp;*</th>
						<td width="13%"><input type="text" name="<%= Riskregistertemplate.RISKNAME %>" id="<%= Riskregistertemplate.RISKNAME %>" class="campo" maxlength="50" value="${tl:escape(risk.riskName)}"/></td>
						
						<%-- Risk category --%>
						<th width="11%" class="right"><fmt:message key="riskCategory"/></th>
						<td width="20%" >
							<select name="<%= Riskregistertemplate.RISKCATEGORY%>" id="<%= Riskregistertemplate.RISKCATEGORY%>" class="campo">
								<option value="" selected><fmt:message key="select_opt"/></option>
								<c:forEach var="riskCategory" items="${risksCategory}">
									<option value="${riskCategory.idRiskcategory}"
										${risk.riskcategory.idRiskcategory == riskCategory.idRiskcategory? "selected" : ""}
										description="${tl:escape(riskCategory.description)}">${tl:escape(riskCategory.name)}</option>
								</c:forEach>
							</select>
						</td>
						
						<th width="6%" class="right"><fmt:message key="risk.owner"/></th>
						<td width="13%"><input type="text" name="<%= Riskregistertemplate.OWNER %>" id="<%= Riskregistertemplate.OWNER %>" class="campo" maxlength="100" value="${tl:escape(risk.owner)}"/></td>
						
						<th width="5%" class="right"><fmt:message key="risk.status"/>&nbsp;</th>
						<td width="10%">
							<select name="<%= Riskregistertemplate.STATUS %>" id="<%= Riskregistertemplate.STATUS %>" class="campo">								
								<option value="<%=Constants.CHAR_OPEN%>"
									<c:if test="${risk.status eq statusOpen}">
										selected										
									</c:if>	
								><fmt:message key="risk.status.open" /></option>
								<option value="<%=Constants.CHAR_CLOSED %>"
									<c:if test="${risk.status eq statusClose}">
										selected										
									</c:if>	
								><fmt:message key="risk.status.closed" /></option>								
							</select>
						</td>
					</tr>
					<tr>
						<th colspan="10" class="left"><fmt:message key="risk.description"/></th>
					</tr>
					<tr>
						<td colspan="10"><textarea name="<%= Riskregistertemplate.DESCRIPTION %>" id="<%= Riskregistertemplate.DESCRIPTION %>" class="campo" style="width:98%;">${tl:escape(risk.description)}</textarea></td>
					</tr>
					<tr>
						<th class="left"><fmt:message key="risk.probability" /></th>
						<td>
							<select name="<%= Riskregistertemplate.PROBABILITY %>" id="risk_probability" class="campo">
								<option value=""><fmt:message key="select_opt" /></option>
								<option value="<%=Constants.PROBABILITY_TRIVIAL %>"
									<c:if test="${risk.probability eq probTrivial}">
										selected										
									</c:if>																		
								><fmt:message key="risk.probability.trivial" /></option>
								<option value="<%= Constants.PROBABILITY_MINOR %>"
									<c:if test="${risk.probability eq probMinor}">
										selected										
									</c:if>
								><fmt:message key="risk.probability.minor" /></option>
								<option value="<%=Constants.PROBABILITY_MODERATE %>"
									<c:if test="${risk.probability eq probModerate}">
										selected										
									</c:if>
								><fmt:message key="risk.probability.moderate" /></option>
								<option value="<%=Constants.PROBABILITY_HIGH %>"
									<c:if test="${risk.probability eq probHigh}">
										selected										
									</c:if>
								><fmt:message key="risk.probability.high" /></option>
								<option value="<%=Constants.PROBABILITY_SEVERE %>"
									<c:if test="${risk.probability eq probSevere}">
										selected										
									</c:if>
								><fmt:message key="risk.probability.severe" /></option>
							</select>
						</td>
						<th class="right"><fmt:message key="risk.impact" /></th>
						<td>
							<select name="<%= Riskregistertemplate.IMPACT %>" id="risk_impact" class="campo">
								<option value=""><fmt:message key="select_opt" /></option>
								<option value="<%=Constants.IMPACT_TRIVIAL %>"
									<c:if test="${risk.impact eq impTrivial}">
										selected										
									</c:if>
								><fmt:message key="risk.impact.trivial" /></option>
								<option value="<%=Constants.IMPACT_MINOR %>"
									<c:if test="${risk.impact eq impMinor}">
										selected										
									</c:if>
								><fmt:message key="risk.impact.minor" /></option>
								<option value="<%=Constants.IMPACT_MODERATE %>"
									<c:if test="${risk.impact eq impModerate}">
										selected										
									</c:if>
								><fmt:message key="risk.impact.moderate" /></option>
								<option value="<%=Constants.IMPACT_HIGH %>"
									<c:if test="${risk.impact eq impHigh}">
										selected										
									</c:if>
								><fmt:message key="risk.impact.high" /></option>
								<option value="<%=Constants.IMPACT_SEVERE %>"
									<c:if test="${risk.impact eq impSevere}">
										selected										
									</c:if>
								><fmt:message key="risk.impact.severe" /></option>
							</select>
						</td>
						<th class="right" colspan="2">
							<div style="float: left; width: 180px; margin-right: 10px;"><fmt:message key="risk.rating"/></div>
							<div id="risk_rating" class="riskRating">&nbsp;</div>
						</th>
						<th class="right"><fmt:message key="risk.potential_cost" /></th>
						<td><input type="text" name="<%= Riskregistertemplate.POTENTIALCOST %>" id="<%= Riskregistertemplate.POTENTIALCOST %>" class="campo importe" style="width: 75%;" value="${risk.potentialCost}" maxlength="14"/></td>
						<th class="right"><fmt:message key="risk.potential_delay" /></th>
						<td><input type="text" name="<%= Riskregistertemplate.POTENTIALDELAY %>" id="<%= Riskregistertemplate.POTENTIALDELAY %>" class="campo integerPositive code" maxlength="5" value="${tl:escape(risk.potentialDelay)}"/>&nbsp;<fmt:message key="days" /></td>
					</tr>
					<tr>
						<th class="left" colspan="10"><fmt:message key="risk.risk_trigger" /></th>
					</tr>
					<tr>
						<td colspan="10"><textarea name="<%= Riskregistertemplate.RISKTRIGGER %>" id="<%= Riskregistertemplate.RISKTRIGGER %>" class="campo" style="width:98%;">${tl:escape(risk.riskTrigger)}</textarea></td>
					</tr>
				</table>
			</visual:panel>
		
			<%-- RISK RESPONSE --%>
			<fmt:message key="risk_planning" var="titleRiskPlanning"/>
			<visual:panel id="field_risk_planning" title="${titleRiskPlanning}" cssClass="panel1">
				<table id="tb_risk_planning" border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr>
						<th class="left" width="10%"><fmt:message key="risk.type"/></th>
						<td width="13%">
							<select name="<%= Riskregistertemplate.RISKTYPE %>" id="<%= Riskregistertemplate.RISKTYPE %>" class="campo">
								<option value="-1"><fmt:message key="select_opt" /></option>
								<option value="<%=Constants.RISK_OPPORTUNITY %>"
									<c:if test="${risk.riskType eq riskOpportunity}">
										selected										
									</c:if>
								><fmt:message key="risk.type.opportunity" /></option>
								<option value="<%=Constants.RISK_THREAT %>"
									<c:if test="${risk.riskType eq riskThreat}">
										selected										
									</c:if>
								><fmt:message key="risk.type.threat" /></option>
							</select>
						</td>
						<th class="right" width="11%"><fmt:message key="risk.response"/></th>
						<td width="13%">
							<select name="response" id="response" class="campo">
								<option value="-1"><fmt:message key="select_opt" /></option>
								<c:forEach var="category" items="${riskCategories}">
									<option id="response_${category.idRiskCategory}" class="${category.riskType}" value="${category.idRiskCategory}"
									<c:if test="${risk.riskcategories.idRiskCategory eq category.idRiskCategory}">
										selected										
									</c:if>
									>${category.description}</option>
								</c:forEach>
							</select>
						</td>
						<th width="35%">&nbsp;</th>
					</tr>
					<tr>
						<th colspan="5" class="left"><fmt:message key="risk.response_description" /></th>
					</tr>
					<tr>
						<td colspan="5"><textarea name="<%= Riskregistertemplate.RESPONSEDESCRIPTION %>" id="<%= Riskregistertemplate.RESPONSEDESCRIPTION %>" class="campo" style="width:98%" >${tl:escape(risk.responseDescription)}</textarea></td>
					</tr>
					<tr>
						<th colspan="5" class="left"><fmt:message key="risk.mitigation_actions" /></th>
					</tr>
					<tr>
						<td colspan="5"><textarea name="<%= Riskregistertemplate.MITIGATIONACTIONSREQUIRED %>" id="<%= Riskregistertemplate.MITIGATIONACTIONSREQUIRED %>" class="campo" style="width:98%">${tl:escape(risk.mitigationActionsRequired)}</textarea></td>
					</tr>
					<tr>
						<th class="left"><fmt:message key="risk.mitigation_cost" /></th>
						<td><input type="text" name="<%= Riskregistertemplate.PLANNEDMITIGATIONCOST %>" id="<%= Riskregistertemplate.PLANNEDMITIGATIONCOST %>" class="campo importe" maxlength="14" style="width:100px" value="${risk.plannedMitigationCost}"/></td>
						<td colspan="3">&nbsp;</td>
					</tr>
					<tr>
						<th colspan="5" class="left"><fmt:message key="risk.contingency_actions" /></th>
					</tr>
					<tr>
						<td colspan="5"><textarea name="<%= Riskregistertemplate.CONTINGENCYACTIONSREQUIRED %>" id="<%= Riskregistertemplate.CONTINGENCYACTIONSREQUIRED %>" class="campo" style="width:98%">${tl:escape(risk.contingencyActionsRequired)}</textarea></td>
					</tr>
					<tr>
						<th class="left"><fmt:message key="risk.contingency_cost" /></th>
						<td><input type="text" name="<%= Riskregistertemplate.PLANNEDCONTINGENCYCOST %>" id="<%= Riskregistertemplate.PLANNEDCONTINGENCYCOST %>" class="campo importe" maxlength="14" style="width:100px" value="${risk.plannedContingencyCost}"/></td>
						<td colspan="3">&nbsp;</td>
					</tr>
					<tr>
						<th colspan="5" class="left"><fmt:message key="risk.residual_risk" /></th>
					</tr>
					<tr>
						<td colspan="5"><textarea name="<%= Riskregistertemplate.RESIDUALRISK %>" id="<%= Riskregistertemplate.RESIDUALRISK %>" class="campo" style="width:98%" >${tl:escape(risk.residualRisk)}</textarea></td>
					</tr>
					<tr>
						<th class="left"><fmt:message key="risk.residual_cost" /></th>
						<td><input type="text" name="<%= Riskregistertemplate.RESIDUALCOST %>" id="<%= Riskregistertemplate.RESIDUALCOST %>" class="campo importe" maxlength="14" style="width:100px" value="${risk.residualCost}" /></td>
						<td colspan="3">&nbsp;</td>
					</tr>
				</table>
			</visual:panel>
		
			<%-- RISK FINAL DISPOSITION --%>
			<fmt:message key="risk.final_disposition" var="titleFinalDisposition"/>
			<visual:panel id="field_risk_disposition" title="${titleFinalDisposition}" cssClass="panel1">
				<table id="tb_risk_disposition" border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr>
						<th class="left" width="3%"><fmt:message key="risk.materialized"/></th>
						<td  width="2%">
							<input type="checkbox" name="<%= Riskregistertemplate.MATERIALIZED %>" id="<%= Riskregistertemplate.MATERIALIZED %>" style="width: 20px;"
								<c:if test="${risk.materialized eq true or risk.materialized eq 'true'}">
									checked
								</c:if>
							/>
						</td>
						<th class="right"  width="4%"><fmt:message key="risk.actual_cost" /></th>
						<td  width="3%">
							<input type="text" name="<%= Riskregistertemplate.ACTUALMATERIALIZATIONCOST %>" id="<%= Riskregistertemplate.ACTUALMATERIALIZATIONCOST %>" class="campo importe" style="width: 100px;" maxlength="14" value="${risk.actualMaterializationCost}" />
						</td>
						<th class="right"  width="3%"><fmt:message key="risk.actual_delay" /></th>
						<td  width="1%">
							<input type="text" name="<%= Riskregistertemplate.ACTUALMATERIALIZATIONDELAY %>" id="<%= Riskregistertemplate.ACTUALMATERIALIZATIONDELAY %>" class="campo integerPositive code" maxlength="5" value="${tl:escape(risk.actualMaterializationDelay)}" />&nbsp;<fmt:message key="days" />
						</td>
					</tr>
					<tr>
						<th colspan="6" class="left"><fmt:message key="risk.final_comments" /></th>
					</tr>
					<tr>
						<td colspan="6"><textarea name="<%= Riskregistertemplate.FINALCOMMENTS %>" id="<%= Riskregistertemplate.FINALCOMMENTS %>" class="campo" style="width:98%">${tl:escape(risk.finalComments)}</textarea></td>
					</tr>
				</table>
			</visual:panel>
			
			<div align="right" style="margin-top:10px;">
				<a href="javascript: riskTemplate.back();" class="boton"><fmt:message key="close" /></a>
				<a href="javascript: riskTemplate.save();" class="boton"><fmt:message key="save" /></a>
			</div>
		</form>
	</div>
</fieldset>

<%-- Charge plugins --%>
<div id="plugin"></div>
