<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.model.impl.Documentproject"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@ page import="es.sm2.openppm.core.logic.security.actions.UtilAction" %>
<%@ page import="es.sm2.openppm.front.servlets.UtilServlet" %>
<%@ page import="es.sm2.openppm.core.logic.setting.GeneralSetting" %>
<%@ page import="es.sm2.openppm.front.utils.SettingUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
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
  ~ File: approve_investment.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:56
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<c:set var="tab" scope="request"><%=Constants.TAB_INVESTMENT%></c:set>

<script type="text/javascript">
<!--

var utilAjax = new AjaxCall('<%=UtilServlet.REFERENCE%>','<fmt:message key="error"/>');

var investmentValidator;

 function consultDocuments() {

    // Call get documents
    var params={
        accion:     "<%= UtilAction.JX_CONSULT_DOCUMENTS.getAction() %>",
        idProject: 	$("#idProject").val(),
        documentationType: "<%= Constants.DOCUMENT_INVESTMENT %>",
        tab: "<%= Constants.TAB_INVESTMENT %>"
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

function openApproveInvestment(idProject) {
	var oTT = TableTools.fnGetInstance( 'tb_investments' );
	oTT.fnSelectNone();
	
	projectAjax.call({
			accion: "<%=ProjectServlet.JX_CONS_PROJECT %>",
			<%=Project.IDPROJECT%>: idProject
		}, function (data) {
			
			document.forms["frm_investment"].reset();
			$('#res_info').text('');
			
			$('#<%=Project.IDPROJECT%>').val(idProject);
			$('#<%=Documentproject.PROJECT%>_<%=Constants.DOCUMENT_INVESTMENT %>').val(idProject);
			
			// Documents
            consultDocuments();

			$('#buttonCloseCancel').hide();
			$('#resultN, #resultS, #resultX').attr("disabled", true);

			$('#res_num_competitors').prop("disabled", true);
			$('#res_num_competitors').css({'background-color': '#FFFFFF'});
			$('#res_final_position').prop("disabled", true);
			$('#res_final_position').css({'background-color': '#FFFFFF'});
			$('#res_info').prop("disabled", true);
			$('#res_info').css({'background-color': '#FFFFFF'});
            $('#changeInvestmentChecks').show();


            if (data.<%=Project.INVESTMENTSTATUS%> == '<%=Constants.INVESTMENT_APPROVED%>') {
				winProposal();
				$('#res_num_competitors').val(data.<%=Project.NUMCOMPETITORS%>);
				$('#res_final_position').val(data.<%=Project.FINALPOSITION%>);
				$('#res_info').text(data.<%=Project.CLIENTCOMMENTS%>);				
			}
			else if (data.<%=Project.INVESTMENTSTATUS%> == '<%=Constants.INVESTMENT_REJECTED%>') {
				loseProposal();
				$('#res_final_position').val(data.<%=Project.FINALPOSITION%>);
				$('#res_num_competitors').val(data.<%=Project.NUMCOMPETITORS%>);
				$('#res_info').text(data.<%=Project.CLIENTCOMMENTS%>);
				$('#buttonCloseCancel').show();
			}
			else if (data.<%=Project.INVESTMENTSTATUS%> == '<%=Constants.INVESTMENT_INACTIVATED%>') {
				cancelProposal();
				$('#res_info').text(data.<%=Project.CLIENTCOMMENTS%>);
				$('#buttonCloseCancel').show();
			}
			else {
                if (<%=(SettingUtil.getBoolean(request, GeneralSetting.ONLY_PMO_CHANGE_STATUS) && !SecurityUtil.isUserInRole(request, Constants.ROLE_PMO))%>) {
                    $('#changeInvestmentChecks').hide();
                }
				<%if (SecurityUtil.hasPermission(request, ProjectServlet.JX_APPROVE_INVESTMENT)) {%>
				$('#resultN, #resultS, #resultX').attr("disabled", false);
				<%}%>
				$('#resultN, #resultS, #resultX').attr("checked", false);
				
				$('#tb_result_info, #tb_result_common').hide();
				$('#buttonInv').show();
				
				<%if (SecurityUtil.hasPermission(request, ProjectServlet.JX_APPROVE_INVESTMENT)) {%>
				if (data.<%=Project.INVESTMENTSTATUS%> == '<%=Constants.INVESTMENT_IN_PROCESS%>') {
					$('#res_num_competitors').prop("disabled", false);
					$('#res_final_position').prop("disabled", false);
					$('#res_info').prop("disabled", false);	
				}
				<%}%>
			}
			$('#buttonApprove, #buttonReject, #buttonCancelInv').hide();
			
			$('#investment-popup').dialog('open');
	});
}

function winProposal() {
	// Proposal win, hide lose and cancel table

	$('#resultN, #resultX').attr("checked", false);
	$('#resultS').attr("checked", true);

	$('#tb_result_common, #buttonApprove, #tb_result_info').show();
	$('#buttonReject, #buttonCancelInv').hide();
	
	$('#res_num_competitors').val('');
	$('#res_final_position').val('1');
	$('#res_final_position').attr('readonly', true);
}

function loseProposal() {
	// Proposal losed, hide win and cancel table

	$('#resultS, #resultX').attr("checked", false);
	$('#resultN').attr("checked", true);

	$('#buttonApprove, #buttonCancelInv').hide();
	$('#tb_result_common, #buttonReject, #tb_result_info').show();
	
	$('#res_final_position').val("1");
	$('#res_final_position').attr('readonly', false);
	$('#res_num_competitors').val('');
}

function cancelProposal() {
	$('#resultN, #resultS').attr("checked", false);
	$('#resultX').attr("checked", true);
	$('#buttonCancelInv, #tb_result_info').show();
	$('#tb_result_common, #buttonApprove, #buttonReject').hide();
}

function updateInvestment(accion) {	
	if (investmentValidator.form()) {	
		document.frm_investment.accion.value=accion;		
		projectAjax.call($('#frm_investment').serialize(), function (data) {
			
			investmentsTable.fnRemoveSelected();
			loadProjects();
			
			$('#investment-popup').dialog('close');
		});
	}
}
function approveInvestment() {	
	updateInvestment("<%=ProjectServlet.JX_APPROVE_INVESTMENT%>");
	$('#dialog-confirm').dialog("close");
}
function rejectInvestment() {
	updateInvestment("<%=ProjectServlet.JX_REJECT_INVESTMENT%>");
	$('#dialog-confirm').dialog("close");
}
function cancelInvestment() {
	updateInvestment("<%=ProjectServlet.JX_CANCEL_INVESTMENT%>");
}
function resetInvestment() {
	updateInvestment("<%=ProjectServlet.JX_RESET_INVESTMENT%>");
}

function confirmLink(idDoc, link) {
	$('#toggleEditLink<%=Constants.DOCUMENT_INVESTMENT %>').hide();
	$('#toggleALink<%=Constants.DOCUMENT_INVESTMENT %>').show();
	
	$("#<%=Documentproject.IDDOCUMENTPROJECT%>").val(idDoc);
	
	$('#a_link<%=Constants.DOCUMENT_INVESTMENT %>')
		.text(link)
		.attr('href',link);
	
	$('#res_document_link<%=Constants.DOCUMENT_INVESTMENT %>').val(link);	
}

function modifyLink() {
	$('#toggleEditLink<%=Constants.DOCUMENT_INVESTMENT %>').show();
	$('#toggleALink<%=Constants.DOCUMENT_INVESTMENT %>').hide();	
}

readyMethods.add(function() {

	$('div#investment-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 600, 
		resizable: false,
		open: function(event, ui) { investmentValidator.resetForm(); }
	});
	
	investmentValidator = $("#frm_investment").validate({
		errorContainer: 'div#investment-errors',
		errorLabelContainer: 'div#investment-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#investment-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Project.CLIENTCOMMENTS %>: { maxlength: 1000 }
		}
	});
});
//-->
</script>


<c:if test="<%=!SecurityUtil.hasPermission(request, ProjectServlet.JX_APPROVE_INVESTMENT) %>">				
<script type="text/javascript">
	readyMethods.add(function() {
		
		$('.popInv')
			.attr('disabled',true)
			.css({'background-color': '#FFFFFF'});
		$('.popInvButtons').hide();
	});
</script>
</c:if>

<c:if test="<%=(SettingUtil.getBoolean(request, GeneralSetting.ONLY_PMO_CHANGE_STATUS) && !SecurityUtil.isUserInRole(request, Constants.ROLE_PMO))%>">
    <c:set var="hideChangueInvestment">style="display:none;"</c:set>
</c:if>
<div id="investment-popup" class="popup">
	<div id="investment-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="investment-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	
	<fieldset id="changeInvestmentChecks">
		<legend><fmt:message key="result_proposal" /></legend>
		<form id="frm_investment" name="frm_investment" method="post">
			<input type="hidden" name="<%=Project.IDPROJECT%>" id="<%=Project.IDPROJECT%>"/>
			<input type="hidden" name="accion"/>		
			<table width="100%">
				<tr ${hideChangueInvestment}>
					<td style="width:25%">
							<input class="popInv" type="radio" name="result" id="resultS" value="S" style="width:14px;" onclick="winProposal();" />
						<label for="resultS">
							<img src="images/approve.png" width="16px" height="16px" style="vertical-align:bottom" />
							<fmt:message key="investments.approve"/>
						</label>
					</td>
					<td style="width:25%">
						<input class="popInv" type="radio" name="result" id="resultN" value="N" style="width:14px;" onclick="loseProposal();" />
						<label for="resultN">
							<img src="images/ico_down.jpg" width="16px" height="16px" style="vertical-align:bottom" />
							<fmt:message key="investments.reject"/>
						</label>
					</td>
					<td style="width:50%">
						<input class="popInv" type="radio" name="result" id="resultX" value="X" style="width:14px;" onClick="cancelProposal();" />
						<label for="resultX">
							<img src="images/reject.png" width="16px" height="16px" style="vertical-align:bottom" />
							<fmt:message key="investments.inactive"/>
						</label>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<table width="100%" id="tb_result_common" style="display:none;">
							<tr>
								<th class="left" width="50%"><fmt:message key="proposal.final_position"/></th>
								<th class="left" width="50%"><fmt:message key="proposal.num_competitors"/></th>
							</tr>
							<tr>
								<td class="center">
									<input type="text" name="<%=Project.FINALPOSITION %>" id="res_final_position" class="campo integer popInv" />
								</td>
								<td class="center">
									<input type="text" name="<%=Project.NUMCOMPETITORS %>" id="res_num_competitors" class="campo integer popInv"/>
								</td>
							</tr>
						</table>
						<table width="100%" id="tb_result_info" style="display:none;">
							<tr>
								<th class="left" style="width:25%"><fmt:message key="investment.relevant_information"/></th>								
							</tr>
							<tr>
								<td style="width:75%">
									<textarea name="<%=Project.CLIENTCOMMENTS %>" id="res_info" class="campo popInv" style="width:100%;" cols="90" rows="6"></textarea>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>		
	</fieldset>
	<fieldset>
		<legend><fmt:message key="documentation.investment"/></legend>

        <%-- Load documents --%>
        <div id="loadDocuments"></div>
	</fieldset>

    <div ${hideChangueInvestment}>
        <div class="popButtons popInvButtons">
            <a href="#" id="buttonApprove" class="boton" onclick="approveInvestment();"><fmt:message key="investment.approve"/></a>
            <a href="#" id="buttonReject" class="boton" onclick="rejectInvestment();"><fmt:message key="investment.reject" /></a>
            <a href="#" id="buttonCancelInv" class="boton" onclick="cancelInvestment();"><fmt:message key="investment.cancel" /></a>
            <a href="#" id="buttonCloseCancel" class="boton" onclick="return resetInvestment();"><fmt:message key="investment.reset" /></a>
        </div>
    </div>

</div>