<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
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
  ~ File: followup_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="error" var="fmt_error" />
<fmt:message key="info.copy" var="acCopy">
	<fmt:param><fmt:message key="followup.ac"/></fmt:param>
</fmt:message>
<fmt:message key="info.copy" var="evCopy">
	<fmt:param><fmt:message key="activity.earned_value"/></fmt:param>
</fmt:message>
<fmt:message key="followup.calcEV" var="calcEV" />
<fmt:message key="followup.calcPOC" var="calcPOC" />




<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="editFollowup"><img onclick="return editFollowup(this);" class="link" src="images/view.png" title="<fmt:message key="view"/>"/></c:set>
</c:if>

<script type="text/javascript">
<!--
function saveFollowup() {

	loadingPopup();
	document.forms["frm_followup"].submit();
}

function updatePoc() {
	var f = document.forms["frm_followup"];

	var ev = toNumber(f.ev.value);
	var bac = toNumber(f.bac.value);

	if (ev >= 0 && bac > 0) {
		f.poc.value = toCurrency(ev*100/bac);
	}
}

function calculateFollowupEv() {
	var f = document.forms["frm_followup"];

	var poc = toNumber(f.poc.value);
	var bac = toNumber(f.bac.value);

	if (poc >= 0 && bac >=0) {
		f.ev.value = toCurrency((poc * bac / 100));
	} 
}

var followupPopupUtils = {
	
	// Id root
	//
	getIdRoot: function() {
		return "followup-popup";
	},
	// Copy text from id div to id input
	//
	copyDivToInput: function(idOrigin, idDestination) {
		
		var divId 	= "#" + this.getIdRoot() + " #" + idOrigin;
		var inputId = "#" + this.getIdRoot() + " #" + idDestination;
		
		$(inputId).val($(divId).html());
	}	
};

readyMethods.add(function() {

	$('div#followup-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		resizable: false
	});

	$('#btnUpdatePoc').on('click',function() { updatePoc(); });
	$('#btnCalcFollowupEv').on('click',function() { calculateFollowupEv(); });
	
	$('#acCopy img').on('click',function() { followupPopupUtils.copyDivToInput("sumTimeSheet","ac"); });
	$('#evCopy img').on('click',function() { followupPopupUtils.copyDivToInput("sumEVActivity","ev"); });
	
	createBT('.iconFollowup', "left");
});
//-->
</script>

<div id="followup-popup" class="popup">

	<form name="frm_followup" id="frm_followup" action="<%=ProjectControlServlet.REFERENCE%>" method="post">
		<input type="hidden" name="accion" value="<%=ProjectControlServlet.UPDATE_FOLLOWUP%>"/>
		<input type="hidden" name="id" value="${project.idProject }"/>
		<input type="hidden" name="followup_id"/>
		
		<fieldset>
			<legend><fmt:message key="followup.earner_management.item"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="30%"><fmt:message key="project.bac"/>&nbsp;</th>
					<th class="left" width="25%"><fmt:message key="followup.ac"/>&nbsp;*</th>
					<th class="left" width="20%"><fmt:message key="followup.status_date"/></th>
					<th class="left" width="25%"><fmt:message key="activity.ac.time_sheet"/></th>
				</tr>
				<tr>
					<td>
						<c:choose>
							<c:when test="<%=SettingUtil.getBoolean(request, Settings.SETTING_USE_BAC_PLANNING, Settings.SETTING_USE_BAC_PLANNING_DEFAULT)%>">
								<c:set var="useBadget" value="${sumBudget }" />
							</c:when>
							<c:otherwise><c:set var="useBadget" value="${project.bac }"/></c:otherwise>
						</c:choose>
						<input type="text" id="bac" name="bac" readonly value="${tl:toCurrency(useBadget) }"/>
					</td>
					<td>
						<input type="text" id="ac" name="ac" class="campo importe" style="width: 80%"/>
					</td>
					<td>
						<input type="text" name="statusDate" class="importe center" readonly/>
					</td>
					<td class="right">
						<div id ="sumTimeSheet" style="float: left; width: 80px;">${tl:toCurrency(sumTimeSheet)}</div>
						<div id = "acCopy"><img class="iconFollowup" bt-xtitle="${acCopy}" src="images/copyAndPaste.png" ></div>
					</td>
				</tr>
				<tr>
					<th class="left"><fmt:message key="activity.earned_value"/>&nbsp;*</th>
					<th class="left"><fmt:message key="followup.poc"/></th>
					<th class="left">&nbsp;</th>
					<th class="left"><fmt:message key="activity.ev.sum"/></th>
				</tr>
				<tr>
					<td>
						<div style="float: left;">
							<input type="text" id="ev" name="ev" class="campo importe" style="width: 80px;"/>
						</div>
						<div id = "btnCalcFollowupEv" style="padding-top: 2px; float: left;">
							<img class="btitle" bt-xtitle="${calcEV}" src="images/calculator.png" >
						</div>
					</td>
					<td>
						<div style="float: left;">
							<input type="text" id="poc" name="poc" class="campo integer" style="width: 70px;"/>
						</div>
						<div id = "btnUpdatePoc" style="padding-top: 2px; float: left;">
							<img class="btitle" bt-xtitle="${calcPOC}" src="images/calculator.png" >
						</div>
					</td>
					<td></td>
					<td class="right">
						<div id ="sumEVActivity" style="float: left; width: 80px;">${tl:toCurrency(sumEVActivity) }</div>
						<div id = "evCopy"><img class="iconFollowup" bt-xtitle="${evCopy}" src="images/copyAndPaste.png" ></div>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveFollowup(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>