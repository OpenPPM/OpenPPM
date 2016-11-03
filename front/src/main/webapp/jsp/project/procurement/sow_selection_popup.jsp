<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectProcurementServlet"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>

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
  ~ File: sow_selection_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="maintenance.procurement.new_sow" var="new_sow_title" />
<fmt:message key="maintenance.procurement.edit_sow" var="edit_sow_title" />

<fmt:message key="maintenance.error_msg_select_a" var="fmtSellerRequired">
	<fmt:param><b><fmt:message key="procurement.seller_name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_select_a" var="fmtActivityRequired">
	<fmt:param><b><fmt:message key="procurement.activity_name"/></b></fmt:param>
</fmt:message>

<c:set var="select_option"><fmt:message key="select_opt" /></c:set>

<%-- Message for Confirmation update activity sellers --%>
<fmt:message key="procurement.indirect.msg_update" var="msg_confirm_update_activityseller"></fmt:message>
<fmt:message key="procurement.indirect.msg_title_update" var="msg_title_confirm_update_activityseller"></fmt:message>

<fmt:message key="yes" var="yes"></fmt:message>
<fmt:message key="no" var="no"></fmt:message>

<script language="javascript" type="text/javascript" >
<!--
var sowValidator;
var newOption;

function addSow() {
	var f = document.forms["frm_sow_popup"];
	f.reset();
	f.idSow.value = "";
	f.projectAssociate.value = "";
	$("#<%=Activityseller.STATEMENTOFWORK %>").text("");	
	f.<%=Activityseller.PROCUREMENTDOCUMENTS %>.value 	= "";	
	$("#a_linkIni").text("");
	
	$('#<%=Activityseller.PROJECTACTIVITY %>').find("option").remove();
	newOption = "<option value=''>${select_option}</option>";
	$('#<%=Activityseller.PROJECTACTIVITY %>').append(newOption);
	
	$('#sow_popup legend').html('${new_sow_title}');
	$('#sow_popup').dialog('open');
}

function updateAll(updateAll) {
	
	var f = document.forms["frm_sow_popup"];
	
	f.action 			= "<%=ProjectProcurementServlet.REFERENCE%>";
	f.accion.value 		= "<%=ProjectProcurementServlet.SAVE_SOW %>";
	f.updateAll.value 	= updateAll;
	
	loadingPopup();
	f.submit();
}

function saveSow() {
	
	if (sowValidator.form()) {	
		
		var sameTypeOfProvider;
		
		var f = document.forms["frm_sow_popup"];		
		
		var params = {
			accion: "<%=ProjectProcurementServlet.JX_SAME_TYPE_OF_PROVIDER %>",
			idSow: f.idSow.value,
			<%= Activityseller.SELLER %>: f.<%= Activityseller.SELLER %>.value,
			idProject: f.id.value,
			indirect: f.indirect.checked
		};
		
		<%-- Check is same type of provider --%>
		procurementAjax.call(params, function(data) {
			
			if (typeof data.sameTypeOfProvider !== 'undefined') {
				sameTypeOfProvider = data.sameTypeOfProvider;
			}
			
			<%--If not same type of provider show options update all and save this --%>
			if (!sameTypeOfProvider) {
				
				confirmUI(
						'${msg_title_confirm_update_activityseller}','${msg_confirm_update_activityseller}',
						'${yes}', '${no}',
						function() { // Yes
							updateAll(true);
						},
						function() { // No
							
							if (typeof $("#indirect").attr('checked') === 'undefined') {
								$("#indirect").attr('checked', true);
							}
							else {
								$("#indirect").attr('checked', false);
							}
							
							$("#indirectDiv").show('highlight', 4000);
							
							updateAll(false);
						}
					);
			}
			<%-- Save sow if same type of provider --%>
			else {
				updateAll(false);
			}
		
		});
	}
}

function editSow(element) {	
	
	var sow = sowTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	var f = document.forms["frm_sow_popup"];
	
	f.idSow.value 										= sow[0];
	f.<%=Activityseller.SELLER %>.value 				= sow[1];
	f.indirect.checked 									= $('#indirect_'+sow[0]).is(':checked');;
	$("#<%=Activityseller.STATEMENTOFWORK %>").text(unEscape(sow[4]));		
	f.<%=Activityseller.PROCUREMENTDOCUMENTS %>.value 	= unEscape(sow[7]);
	$("#a_linkIni").attr("href", unEscape(sow[7]));
	$("#a_linkIni").text(unEscape(sow[7]));

	newOption = "<option value='" + sow[5] + "' selected>" + unEscape(sow[6]) + "</option>";
	loadActivities(newOption);
	
	f.projectAssociate.value 		= sow[8];
	f.projectAssociateName.value 	= sow[9];
	
	$('div#sow_popup legend').html('${edit_sow_title}');	
	$('div#sow_popup').dialog('open');
}

function loadActivities(option){
	var f = document.forms["frm_sow_popup"];
	var seller = f.<%=Activityseller.SELLER %>.options[f.<%=Activityseller.SELLER %>.selectedIndex].value;
	if (seller != "-1") {
		
		var params = {
			accion: "<%=ProjectProcurementServlet.JX_CONS_SELLER_ACTIVITIES %>",
			idProject: f.id.value,
			idSeller: seller
		};
		
		procurementAjax.call(params, function(data) {
			$('#<%=Activityseller.PROJECTACTIVITY %>').find("option").remove();
			newOption = "<option value=''>${select_option}</option>";
			$('#<%=Activityseller.PROJECTACTIVITY %>').append(newOption);
			if(option != "") {
				$('#<%=Activityseller.PROJECTACTIVITY %>').append(option);	
			}					
			for(var i = 0; i < data.length; i++) {
				newOption = "<option value='" + data[i][0] + "'>" + data[i][1] + "</option>";
				$('#<%=Activityseller.PROJECTACTIVITY %>').append(newOption);
			}
		});
	}	
}

function confirmIniLink() {
	$('#toggleEditLinkIni').hide();
	$('#toggleALinkIni').show();
	
	$('#a_linkIni')
		.text($('#<%=Activityseller.PROCUREMENTDOCUMENTS %>')
		.val()).attr('href',$('#<%=Activityseller.PROCUREMENTDOCUMENTS %>').val());
}
function modifyIniLink() {

	$('#toggleALinkIni').hide();
	$('#toggleEditLinkIni').show();
}

function closeSow() {
	$('div#sow_popup').dialog('close');
}
//When document is ready
readyMethods.add(function() {
	
	$('div#sow_popup').dialog({ 
		autoOpen: false, 
		modal: true, 
		width: 550, 
		minWidth: 300, 
		resizable: false,
		open: function(event, ui) { sowValidator.resetForm(); }
	});
	
	// validate the form when it is submitted
	sowValidator = $("#frm_sow_popup").validate({
		errorContainer: 'div#sow-errors',
		errorLabelContainer: 'div#sow-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Activityseller.SELLER %> : {required: true },
			<%=Activityseller.PROJECTACTIVITY %> : {required: true },
			<%=Activityseller.STATEMENTOFWORK %>: { maxlength: 200 }
		},
		messages: {
			<%=Activityseller.SELLER %> : {required: '${fmtSellerRequired}' },
			<%=Activityseller.PROJECTACTIVITY%> : {required: '${fmtActivityRequired}' }
		}
	});
	
});
//-->
</script>

<div id="sow_popup" class="popup">

	<div id="sow-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_sow_popup" id="frm_sow_popup" method="post">	
		<input type="hidden" name="id" value="${project.idProject}" /> <!-- idProject -->
		<input type="hidden" name="idSow" />		
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="updateAll" value="false" />
		<fieldset>
			<legend></legend>
			<table border="0" cellpadding="2" cellspacing="1" style="width:100%">
				<tr>
					<th class="left" width="20%"><fmt:message key="procurement.seller_name"/>&nbsp;*</th>
					<td width="57%">
						<select name="<%=Activityseller.SELLER %>" id="<%=Activityseller.SELLER %>" class="campo" onchange="loadActivities('');">
							<option value=""><fmt:message key="select_opt" /></option>
							<c:forEach var="seller" items="${sellers}">
								<option value="${seller.idSeller}">${seller.name}</option>
							</c:forEach>
						</select>							
					</td>
					<th width="8%"><fmt:message key="procurement.indirect"/></th>	
					<td width="8%">
						<div id="indirectDiv" style="width: 28px;">
							<input type="checkbox" id="indirect" name="indirect" style="width:20px; vertical-align: top;">
						</div>
					</td>					
				</tr>
				<tr>
					<th class="left"><fmt:message key="procurement.activity_name"/>&nbsp;*</th>
					<td colspan = "3">
						<select name="<%=Activityseller.PROJECTACTIVITY %>" id="<%=Activityseller.PROJECTACTIVITY %>" class="campo">							
						</select>
					</td>
				</tr>
				<!-- Project associate -->
				<tr>
					<th class="left"><fmt:message key="project.associate"/></th>
					<td colspan="3">
						<input type="hidden" id="projectAssociate" name="projectAssociate" value=""/>
						<c:choose>
							<c:when test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PMO)%>">
								<input type="text" id="projectAssociateName" class="campo" name="projectAssociateName" value="" style="margin-right: 2px; width: 338px;" readonly/>
								<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" style="margin-right: 7px;" onclick="openSearchProject('#projectAssociate');" />
							</c:when>
							<c:otherwise>
								<input type="text" id="projectAssociateName" class="campo" name="projectAssociateName" value="" style="margin-right: 2px;" readonly/>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<!--  -->
				<tr>
					<th class="left" colspan ="4"><fmt:message key="procurement.sow_selection.sow"/>&nbsp;</th>
				</tr>
				<tr>
					<td colspan ="4">
						<textarea name="<%=Activityseller.STATEMENTOFWORK %>" id="<%=Activityseller.STATEMENTOFWORK %>" class="campo" style="width:98%;" ></textarea>						
					</td>
				</tr>
				<tr>
					<th class="left" colspan ="4"><fmt:message key="procurement.sow_selection.procurement_docs"/>&nbsp;</th>
				</tr>
				<tr>
					<td colspan="4">
						<c:choose>
							<c:when test="${project.linkDoc == null or empty project.linkDoc}">
								<c:set var="linkDocument">style="display:none;"</c:set>
							</c:when>
							<c:otherwise>
								<c:set var="inputDocument">style="display:none;"</c:set>
							</c:otherwise>
						</c:choose>
						<div ${inputDocument } id="toggleEditLinkIni">
							<c:if test="${op:hasPermission(user,project.status,tab)}">
								<img style="width:16px;" src="images/approve.png" onclick="confirmIniLink()"/>
							</c:if>
							<input type="text" name="<%=Activityseller.PROCUREMENTDOCUMENTS %>" id="<%=Activityseller.PROCUREMENTDOCUMENTS %>" style="width: 450px;" maxlength="200" class="campo" value="" />
						</div>
						<div ${linkDocument} id="toggleALinkIni">
							<c:if test="${op:hasPermission(user,project.status,tab)}">
								<img style="width:16px;" src="images/modify.png" onclick="modifyIniLink()"/>
							</c:if>
							<a href="" id="a_linkIni"></a>
						</div>
					</td>
				</tr>
			</table>			
    	</fieldset>
    	<div class="popButtons">
    		<c:if test="${op:hasPermission(user,project.status,tab)}">
    			<input type="submit" class="boton" onclick="saveSow(); return false;" value="<fmt:message key="save" />" />
			</c:if>
			<a href="javascript:closeSow();" class="boton"><fmt:message key="close" /></a>
    	</div>
    </form>
</div>

<jsp:include page="../common/search_projects_popup_one_select.jsp" flush="true"/>
