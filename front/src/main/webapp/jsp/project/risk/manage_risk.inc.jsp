<%@page import="es.sm2.openppm.core.model.impl.Riskregister"%>
<%@page import="es.sm2.openppm.utils.StringPool"%>
<%@page import="es.sm2.openppm.core.model.impl.Historicrisk"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.front.servlets.ProjectRiskServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="Openppm" prefix="op" %>

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
  ~ File: manage_risk.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:49
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

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

<script type="text/javascript">
<!--

function nameProbability(value){
	if (value == <%=Constants.PROBABILITY_TRIVIAL %>) {
		return '<fmt:message key="risk.probability.trivial" />';
	}
	else if (value == <%=Constants.PROBABILITY_MINOR %>) {
		return '<fmt:message key="risk.probability.minor" />';
	}
	else if (value == <%=Constants.PROBABILITY_MODERATE %>) {
		return '<fmt:message key="risk.probability.moderate" />';
	}
	else if (value == <%=Constants.PROBABILITY_HIGH %>) {
		return '<fmt:message key="risk.probability.high" />';
	}
	else if (value == <%=Constants.PROBABILITY_SEVERE %>) {
		return '<fmt:message key="risk.probability.severe" />';
	}
	else {
		return " - ";
	}
}

function nameImpact(value){
	if (value == <%=Constants.IMPACT_TRIVIAL %>) {
		return '<fmt:message key="risk.impact.trivial" />';
	}
	else if (value == <%=Constants.IMPACT_MINOR %>) {
		return '<fmt:message key="risk.impact.minor" />';
	}
	else if (value == <%=Constants.IMPACT_MODERATE %>) {
		return '<fmt:message key="risk.impact.moderate" />';
	}
	else if (value == <%=Constants.IMPACT_HIGH %>) {
		return '<fmt:message key="risk.impact.high" />';
	}
	else if (value == <%=Constants.IMPACT_SEVERE %>) {
		return '<fmt:message key="risk.impact.severe" />';
	}
	else {
		return '<%= StringPool.BLANK_DASH %>' ;
	}
}

function classRiskRating (idDivRiskRating, riskRate) {
	
	$("#" + idDivRiskRating).css({'width': '80px', 'margin':'0 auto'});
	$("#" + idDivRiskRating).append('<div>&nbsp;</div>');
	$("#" + idDivRiskRating + " div").css({'width': '25%'});
	$("#" + idDivRiskRating + " div").attr("class", "riskRating");

    riskRate = typeof riskRate === 'string' ? parseInt(riskRate) : riskRate;

    $("#" + idDivRiskRating + " div").addClass(getClassRating(riskRate));
	
	$("#" + idDivRiskRating).append('<div>(' + riskRate + ')</div>');
	$("#" + idDivRiskRating+ " div").css({'float': 'left', 'margin-left':'10px'});
}

function showHistoricRisk() {

    if ($("#risk").is(":visible")) {

        var idDivRiskRating = "";

        var f = document.forms["frm_risk"];

        var params = {
            accion: "<%=ProjectRiskServlet.JX_SHOW_HISTORIC_RISK %>",
            idRisk: f.risk_id.value
        };

        historicRiskTable.fnClearTable();

        riskAjax.call(params, function (data) {

            $(data.historicrisks).each(function() {

                idDivRiskRating = "riskRating_" + this.<%= Historicrisk.IDHISTORICRISK %>;

                var historicRisk = [
                    this.<%= Historicrisk.IDHISTORICRISK%>,
                    '<div style="display:none;" class="riskValue order">'+(parseInt(this.<%= Historicrisk.PROBABILITY %>) * parseInt(this.<%= Historicrisk.IMPACT %>)) + '</div>' +
                    '<div id="' + idDivRiskRating + '"></div>',
                    nameProbability(this.<%= Historicrisk.PROBABILITY %>),
                    nameImpact(this.<%= Historicrisk.IMPACT %>),
                    this.<%=Historicrisk.EMPLOYEE %>,
                    this.<%=Historicrisk.ACTUALDATE %>
                ];

                historicRiskTable.fnAddData(historicRisk);

                classRiskRating(idDivRiskRating, parseInt(this.<%= Historicrisk.PROBABILITY %>) * parseInt(this.<%= Historicrisk.IMPACT %>));

            });

        });
    }
};

function closeRisk() {
    $("#risk").hide();
};

readyMethods.add(function() {
	
	historicRiskTable = $('#tbHistoricRisk').dataTable({
		"oLanguage": datatable_language,
		"bFilter": true,
		"bInfo": true,
		"bPaginate": true,
		"iDisplayLength": 25,
		"bAutoWidth": false,
		"sPaginationType": "full_numbers",
		"aaSorting": [[ 5, "asc" ]],
		"aoColumns": [
             { "bVisible": false },
         	 { "sSortDataType": "dom-div", "sType": "numeric" },
             { "sClass": "center"  },
             { "sClass": "center"  },
             { "sClass": "center"  },
             { "sClass": "center", "sType":"es_date" }
    	]
	});
	
	$("#<%= Riskregister.RISKCATEGORY%>").selectDescription();
});
//-->
</script>


<div>&nbsp;</div>

<form name="frm_risk" id="frm_risk" method="post">
	<input type="hidden" name="id" value="${project.idProject}"/>
	<input type="hidden" name="risk_id" id="risk_id" value="${risk.idRisk}"/>
    <input type="hidden" id="risk_probability_hidden" value="${risk.probability}"/>
    <input type="hidden" id="risk_impact_hidden" value="${risk.impact}"/>
	<input type="hidden" name="saveReassessmentLog" id="saveReassessmentLog" value="false"/>
	<input type="hidden" name="accion" />
	<input type="hidden" name="scrollTop" />
	
	<fmt:message key="risk" var="titleRisk"/>
	<visual:panel title="${titleRisk }" cssClass="panel2">
		<div id="risk-errors" class="ui-state-error ui-corner-all hide" style="margin-top: 15px;">		
			<p>
				<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
				<strong><fmt:message key="msg.error_title"/></strong>
				&nbsp;(<b><span id="risk-numerrors"></span></b>)
			</p>
			<ol></ol>
		</div>
		
		<%-- RISK DISCOVERY AND ASSESSMENT --%>
		<fmt:message key="risk.risk_discovery" var="titleRiskDiscovery"/>
		<visual:panel title="${titleRiskDiscovery }" cssClass="panel3">
			<table id="tb_risk_discovery" border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th width="10%" class="left"><fmt:message key="risk.code"/>&nbsp;*</th>
					<td width="9%"><input type="text" name="risk_code" id="risk_code" class="campo code" maxlength="5" value="${tl:escape(risk.riskCode)}"/></td>
					<td width="0%" ></td>
					<th width="11%" class="right" ><fmt:message key="risk.date_raised"/>&nbsp;*</th>						
					<td width="14%"><input type="text" name="date_raised" id="date_raised" class="campo fecha" value="<fmt:formatDate value="${risk.dateRaised }" pattern="${datePattern}"/>"/></td>
					<th width="3%" class="right"><fmt:message key="risk.status"/>&nbsp;</th>
					<td width="12%">
						<select name="status" id="risk_status" class="campo">								
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
					<td width="7%" ></td>
					<th width="12%" class="right"><fmt:message key="risk.due_date"/>&nbsp;</th>
					<td width="10%"><input type="text" name="due_date" id="due_date" class="campo fecha" value="<fmt:formatDate value="${risk.dueDate}" pattern="${datePattern}"/>" /></td>
				</tr>
				<tr>
					<th class="left"><fmt:message key="risk.name"/>&nbsp;*</th>
					<td colspan="2"><input type="text" name="risk_name" id="risk_name" class="campo" maxlength="50" value="${tl:escape(risk.riskName)}"/></td>
					
					<%-- Risk category --%>
					<th class="right"><fmt:message key="riskCategory"/></th>
					<td colspan="3">
						<select name="<%= Riskregister.RISKCATEGORY%>" id="<%= Riskregister.RISKCATEGORY%>" class="campo">
							<option value="" selected><fmt:message key="select_opt"/></option>
							<c:forEach var="riskCategory" items="${risksCategory}">
								<option value="${riskCategory.idRiskcategory}"
									${risk.riskcategory.idRiskcategory == riskCategory.idRiskcategory? "selected" : ""}
									description="${tl:escape(riskCategory.description)}">${tl:escape(riskCategory.name)}</option>
							</c:forEach>
						</select>
					</td>
					
					<th class="right"><fmt:message key="risk.owner"/></th>
					<td colspan="2"><input type="text" name="owner" class="campo" maxlength="100" value="${tl:escape(risk.owner)}"/></td>
				</tr>
				<tr>
					<th colspan="10" class="left"><fmt:message key="risk.description"/></th>
				</tr>
				<tr>
					<td colspan="10"><textarea name="description" id="description" class="campo" style="width:98%;" rows="6">${tl:escape(risk.description)}</textarea></td>
				</tr>
				<tr>
					<th class="left"><fmt:message key="risk.probability" /></th>
					<td>
						<select name="probability" id="risk_probability" class="campo">
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
						<select name="impact" id="risk_impact" class="campo">
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
							><fmt:message key="risk.probability.severe" /></option>
						</select>
					</td>
					<th class="right">
						<nobr id="selectRiskImpact">
							<fmt:message key="risk.rating"/>
						</nobr>
					</th>
					<td><div id="risk_rating" class="riskRating">&nbsp;</div></td>
					<th class="right"><fmt:message key="risk.potential_cost" /></th>
					<td><input type="text" name="potential_cost" class="campo importe" style="width: 75%;" value="${risk.potentialCost}" maxlength="14"/></td>
					<th class="right"><fmt:message key="risk.potential_delay" /></th>
					<td><input type="text" name="potential_delay" class="campo integerPositive code" maxlength="5" value="${tl:escape(risk.potentialDelay)}"/>&nbsp;<fmt:message key="days" /></td>
				</tr>
				<tr>
					<th class="left" colspan="10"><fmt:message key="risk.risk_trigger" /></th>
				</tr>
				<tr>
					<td colspan="10"><textarea name="risk_trigger" class="campo" style="width:98%;" rows="6">${tl:escape(risk.riskTrigger)}</textarea></td>
				</tr>
			</table>
			
			<div class="popButtons">
				<c:if test="${op:hasPermission(user,project.status,tab)}">
					<a href="javascript:saveRisk();" class="boton"><fmt:message key="save" /></a>
				</c:if>  			
	  			<a href="javascript:closeRisk();" class="boton"><fmt:message key="close" /></a>
   			</div>
		</visual:panel>
		
		<%-- RISK RESPONSE --%>
		<fmt:message key="risk_planning" var="titleRiskPlanning"/>
		<visual:panel id="field_risk_planning" title="${titleRiskPlanning }" cssClass="panel3">
			<table id="tb_risk_planning" border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="10%"><fmt:message key="risk.type"/></th>
					<td width="13%">
						<select name="risk_type" id="risk_type" class="campo">
							<option value=""><fmt:message key="select_opt" /></option>
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
						<select name="response" id="responseRisk" class="campo">
							<option value="" class="response_empty"><fmt:message key="select_risk_type" /></option>
							<c:forEach var="category" items="${riskCategories}">
								<option id="response_${category.idRiskCategory}" class="response_${category.riskType}" value="${category.idRiskCategory}"
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
					<td colspan="5"><textarea name="response_description" id="response_description" class="campo" style="width:98%" rows="6">${tl:escape(risk.responseDescription)}</textarea></td>
				</tr>
				<tr>
					<th colspan="5" class="left"><fmt:message key="risk.mitigation_actions" /></th>
				</tr>
				<tr>
					<td colspan="5"><textarea name="mitigation_actions" id="mitigation_actions" class="campo" style="width:98%" rows="6">${tl:escape(risk.mitigationActionsRequired)}</textarea></td>
				</tr>
				<tr>
					<th class="left"><fmt:message key="risk.mitigation_cost" /></th>
					<td><input type="text" name="mitigation_cost" id="mitigation_cost" class="campo importe" maxlength="14" style="width:100px" value="${risk.plannedMitigationCost}"/></td>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<th colspan="5" class="left"><fmt:message key="risk.contingency_actions" /></th>
				</tr>
				<tr>
					<td colspan="5"><textarea name="contingency_actions" id="contingency_actions" class="campo" style="width:98%" rows="6">${tl:escape(risk.contingencyActionsRequired)}</textarea></td>
				</tr>
				<tr>
					<th class="left"><fmt:message key="risk.contingency_cost" /></th>
					<td><input type="text" name="contingency_cost" id="contingency_cost" class="campo importe" maxlength="14" style="width:100px" value="${risk.plannedContingencyCost}"/></td>
					<td colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<th colspan="5" class="left"><fmt:message key="risk.residual_risk" /></th>
				</tr>
				<tr>
					<td colspan="5"><textarea name="residual_risk" id="residual_risk" class="campo" style="width:98%" rows="6">${tl:escape(risk.residualRisk)}</textarea></td>
				</tr>
				<tr>
					<th class="left"><fmt:message key="risk.residual_cost" /></th>
					<td><input type="text" name="residual_cost" id="residual_cost" class="campo importe" maxlength="14" style="width:100px" value="${risk.residualCost}" /></td>
					<td colspan="3">&nbsp;</td>
				</tr>
			</table>
			
			<div class="popButtons">
				<c:if test="${op:hasPermission(user,project.status,tab)}">
					<a href="javascript:saveRisk();" class="boton"><fmt:message key="save" /></a>
				</c:if>  			
	  			<a href="javascript:hide('frm_risk');" class="boton"><fmt:message key="close" /></a>
   			</div>
		</visual:panel>
		
		<%-- RISK FINAL DISPOSITION --%>
		<fmt:message key="risk.final_disposition" var="titleFinalDisposition"/>
		<visual:panel id="field_risk_disposition" title="${titleFinalDisposition }" cssClass="panel3">
			<table id="tb_risk_disposition" border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="15%"><fmt:message key="risk.materialized"/></th>
					<td width="10%">
						<input type="checkbox" name="materialized" id="materialized" style="width: 20px;"
							<c:if test="${risk.materialized eq true or risk.materialized eq 'true'}">
								checked
							</c:if>
						/>
					</td>
					<th class="right" width="15%"><fmt:message key="risk.materialized_date"/></th>
					<td width="15%">
						<input type="text" name="date_materialized" id="risk_date_materialized" class="campo fecha" value="<fmt:formatDate value="${risk.dateMaterialization }" pattern="${datePattern}"/>" />
					</td>
					<th class="right"><fmt:message key="risk.actual_cost" /></th>
					<td>
						<input type="text" name="actual_cost" id="risk_actual_cost" class="campo importe" style="width: 100px;" maxlength="14" value="${risk.actualMaterializationCost}" />
					</td>
					<th class="right"><fmt:message key="risk.actual_delay" /></th>
					<td>
						<input type="text" name="actual_delay" id="risk_actual_delay" class="campo integerPositive code" maxlength="5" value="${tl:escape(risk.actualMaterializationDelay)}" />&nbsp;<fmt:message key="days" />
					</td>
				</tr>
				<tr>
					<th colspan="8" class="left"><fmt:message key="risk.final_comments" /></th>
				</tr>
				<tr>
					<td colspan="8"><textarea name="final_comments" id="risk_final_comments" class="campo" style="width:98%" rows="6">${tl:escape(risk.finalComments)}</textarea></td>
				</tr>
			</table>
			
			<div class="popButtons">
				<c:if test="${op:hasPermission(user,project.status,tab)}">
					<a href="javascript:saveRisk();" class="boton"><fmt:message key="save" /></a>
				</c:if>  			
	  			<a href="javascript:hide('frm_risk');" class="boton"><fmt:message key="close" /></a>
   			</div>
		</visual:panel>
		
		<%-- RISK REASSESSMENT LOG --%>
		<fmt:message key="risk.reassessment_log" var="titleReassessemtLog"/>
		<visual:panel id="field_risk_reassessment_log" title="${titleReassessemtLog }" cssClass="panel3">
			<table id="tb_risk_reassessment_log" width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<table id="tb_reassessment_logs" class="tabledata" cellspacing="1" width="100%">
							<thead>
								<tr>
									<th class="cabecera_tabla" width="0%">&nbsp;</th>
									<th class="cabecera_tabla" width="15%"><fmt:message key="date"/></th>
									<th class="cabecera_tabla" width="15%"><fmt:message key="user"/></th>
									<th class="cabecera_tabla" width="62%"><fmt:message key="assumption_log.description_change"/></th>
									<th class="cabecera_tabla" width="8%">
										<c:if test="${not op:isStringEquals(project.status, status_closed) and op:hasPermission(user,project.status,tab)}">
											&nbsp;<img onclick="return addRiskReassessment();" title="<fmt:message key="add"/>" class="link" src="images/add.png">												
										</c:if>
								</th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</td>
				</tr>
				<tr><td>&nbsp;</td></tr>					
			</table>
		</visual:panel>
		
		<%-- HISTORIC RISK --%>
		<fmt:message key="risk.historic" var="titleHistoricRisk"/>
		<visual:panel id="field_historic_risk" title="${titleHistoricRisk }" cssClass="panel3" callback="showHistoricRisk">
		<table id="tbHistoricRisk" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="0%">&nbsp;</th>
					<th width="20%"><fmt:message key="risk.rating"/></th>
					<th width="20%"><fmt:message key="risk.probability"/></th>
					<th width="20%"><fmt:message key="risk.impact"/></th>
					<th width="20%"><fmt:message key="user"/></th>
					<th width="20%"><fmt:message key="date"/></th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		</visual:panel>
		
		<div class="popButtons">
			<c:if test="${op:hasPermission(user,project.status,tab)}">
				<a href="javascript:saveRisk();" class="boton"><fmt:message key="save" /></a>
			</c:if>  			
  			<a href="javascript:hide('frm_risk');" class="boton"><fmt:message key="close" /></a>
   		</div>
   	</visual:panel>
</form>

<div id="reassessment-log-popup" class="popup">
	
	<div id="reassessment-log-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
		<strong><fmt:message key="msg.error_title"/></strong>
		&nbsp;(<b><span id="reassessment-log-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_reassessment_log" id="frm_reassessment_log" >
		<fieldset>
			<legend><fmt:message key="reassessment_log"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="30%"><fmt:message key="reassessment_log.date"/>&nbsp;*</th>
					<td width="70%">
						<input type="hidden" name="reassessment_log_id" />
						<input type="text" name="reassessment_log_date" id="reassessment_log_date" class="campo fecha" />
					</td>
				</tr>
				<tr>
					<th class="left" colspan="2"><fmt:message key="reassessment_log.change"/>&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="2"><textarea name="reassessment_log_change" id="reassessment_log_change" class="campo" style="width:98%;" rows="6"></textarea></td>
				</tr>
	   		</table>
	   	</fieldset>
	   	<c:if test="${op:hasPermission(user,project.status,tab)}">
	   		<div class="popButtons">
	   			<input type="submit" class="boton" onclick="saveLogReassessment(); return false;" value="<fmt:message key="save" />" />
	   		</div>
	   	</c:if>	   	
   </form>
</div>
