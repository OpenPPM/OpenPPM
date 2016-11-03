<%@page import="es.sm2.openppm.core.model.impl.Changecontrol"%>
<%@page import="es.sm2.openppm.core.model.impl.Wbsnode"%>
<%@page import="es.sm2.openppm.core.model.impl.Changerequestwbsnode"%>
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
  ~ File: changerequest_wbsnode_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:01
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="error" var="fmt_error" />

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtWBSRequired">
	<fmt:param><b><fmt:message key="wbs_node"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtEffortRequired">
	<fmt:param><b><fmt:message key="change.estimated_effort"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtCostRequired">
	<fmt:param><b><fmt:message key="change.estimated_cost"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--

var validatorChangeRequestWBSNodePopup;

var changeRequestWBSNodePopup = {
		
		// Form
		form: function () {
			return document.forms["frm_change_request_activity"];
		},
		// Initialization
		initialization: function() {
			
			$('div#change-request-activity-popup').dialog({
				autoOpen: false, 
				modal: true, 
				width: 400, 
				resizable: false,
				open: function(event, ui) { validatorChangeRequestWBSNodePopup.resetForm(); }
			});
		},
		// Open
		open: function() {
			
			$("#changeRequestWBSNodeAjaxError").hide();
			
			$('#change-request-activity-popup').dialog('open');
		},
		// Close
		close: function() {
			$('#change-request-activity-popup').dialog('close');
		},
		// Save
		save: function() {
			
			if (validatorChangeRequestWBSNodePopup.form()) {
				
				var f = changeRequestWBSNodePopup.form();
				
				// Get control accounts associated
				var params = {
					accion: "<%=ProjectControlServlet.JX_SAVE_CHANGEREQUEST_WBSNODE %>",
					<%= Changerequestwbsnode.IDCHANGEREQUESTWBSNODE %>: f.<%= Changerequestwbsnode.IDCHANGEREQUESTWBSNODE %>.value,
					<%= Changecontrol.IDCHANGE %>: f.<%= Changecontrol.IDCHANGE %>.value,
					<%= Wbsnode.IDWBSNODE %>: f.<%= Wbsnode.IDWBSNODE %>.value,
					<%= Changerequestwbsnode.ESTIMATEDEFFORT %>: parseFloat(toNumber(f.<%= Changerequestwbsnode.ESTIMATEDEFFORT %>.value)),
					<%= Changerequestwbsnode.ESTIMATEDCOST %>:  parseFloat(toNumber(f.<%= Changerequestwbsnode.ESTIMATEDCOST %>.value))
				};
					
				controlAjax.call(params, function(data) {
	
					if (typeof data.idError === 'undefined') {
						
						var dataRow = [
							data.<%= Changerequestwbsnode.IDCHANGEREQUESTWBSNODE %>,
							$("#<%= Wbsnode.IDWBSNODE %> :selected").val(),
							$("#<%= Wbsnode.IDWBSNODE %> :selected").html(),
							$("#<%= Changerequestwbsnode.ESTIMATEDEFFORT %>").val(),
							$("#<%= Changerequestwbsnode.ESTIMATEDCOST %>").val(),
							'<nobr><img onclick="changeRequestWBSNode.view(this);" title="<fmt:message key="view"/>" class="link" src="images/view.png"></nobr>'+
							'&nbsp;&nbsp;'+
							'<img onclick="changeRequestWBSNode.delete(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">'
				        ];
						
						var f = changeRequestWBSNodePopup.form();
						
						if (f.<%= Changerequestwbsnode.IDCHANGEREQUESTWBSNODE %>.value != data.<%= Changerequestwbsnode.IDCHANGEREQUESTWBSNODE %>) {
							changeRequestWBSNodeTable.fnAddDataAndSelect(dataRow); 
						}
						else {
							changeRequestWBSNodeTable.fnUpdateAndSelect(dataRow);
						}
						
						changeRequestWBSNode.errorUnmark();
						
						changeRequestWBSNodePopup.close();
					}
					
				});
			} 
		},
		// Reset
		reset: function() {
			
			var f = changeRequestWBSNodePopup.form();
			
			f.<%= Changerequestwbsnode.IDCHANGEREQUESTWBSNODE %>.value 	= "";
			f.<%= Changecontrol.IDCHANGE %>.value 						= "";
			f.<%= Wbsnode.IDWBSNODE %>.value 							= "";
			f.<%= Changerequestwbsnode.ESTIMATEDEFFORT %>.value 		= "";
			f.<%= Changerequestwbsnode.ESTIMATEDCOST %>.value 			= "";
		}
};

readyMethods.add(function() {
	
	changeRequestWBSNodePopup.initialization();
	
	//Validate required fields
	validatorChangeRequestWBSNodePopup = $("#frm_change_request_activity").validate({
		errorContainer: 'div#change-request-activity-errors',
		errorLabelContainer: 'div#change-request-activity-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#change-request-activity-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%= Wbsnode.IDWBSNODE %>: {required : true},
			<%= Changerequestwbsnode.ESTIMATEDEFFORT %>: {required: true},
			<%= Changerequestwbsnode.ESTIMATEDCOST %>: {required: true}
		},
		messages: {
			<%= Wbsnode.IDWBSNODE %>: {required: '${fmtWBSRequired}'},
			<%= Changerequestwbsnode.ESTIMATEDEFFORT %>: {required: '${fmtEffortRequired}' },
			<%= Changerequestwbsnode.ESTIMATEDCOST %>: {required: '${fmtCostRequired}'}
		}
	});
	
	$("#saveChangeRequestWBSNode").on('click', function() {
		changeRequestWBSNodePopup.save();
	});
	
	$("#closeChangeRequestWBSNode").on('click', function() {
		changeRequestWBSNodePopup.close();
	});

});
//-->
</script>

<div>&nbsp;</div>

<div id="change-request-activity-popup" class="popup">

	<%-- Ajax errors --%>
	<div id ="changeRequestWBSNodeAjaxError" class="ui-state-error ui-corner-all" style="margin-bottom: 10px; padding: 0pt 0.7em; display:none">
		<p></p> 
	</div>

	<div id="change-request-activity-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="change-request-activity-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_change_request_activity" id="frm_change_request_activity" action="<%=ProjectControlServlet.REFERENCE%>" method="post">
		<input type="hidden" name="<%= Changecontrol.IDCHANGE %>" value="" />
		<input type="hidden" name="<%= Changerequestwbsnode.IDCHANGEREQUESTWBSNODE %>" value="" />
		<input type="hidden" name="accion" value="" />
		
		<fieldset>
			<table border="0" cellpadding="2" cellspacing="1" style="width:100%">
				<tr>
					<th class="left" colspan="2"><fmt:message key="wbs.ca_long"/>&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="2">
						<select name="<%= Wbsnode.IDWBSNODE %>" id="<%= Wbsnode.IDWBSNODE %>" class="campo">
							<option value='' selected><fmt:message key="select_opt"/></option>
							<c:forEach var="node" items="${wbsnodes}">
								<c:if test="${node.isControlAccount }">
									<option value="${node.idWbsnode}">${node.name}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th class="left"><fmt:message key="change.estimated_effort"/>&nbsp;*</th>
					<th class="left"><fmt:message key="change.estimated_cost"/>&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="<%= Changerequestwbsnode.ESTIMATEDEFFORT %>" id="<%= Changerequestwbsnode.ESTIMATEDEFFORT %>" class="campo importe negativo" />
					</td>
					<td>
						<input type="text" name="<%= Changerequestwbsnode.ESTIMATEDCOST %>" id="<%= Changerequestwbsnode.ESTIMATEDCOST %>" class="campo importe negativo" />
					</td>
				</tr>
    		</table>
    	</fieldset>
   		<div class="popButtons">
    		<input type="button" class="boton" id="saveChangeRequestWBSNode" value="<fmt:message key="save" />" />
   			<input type="button" class="boton" id="closeChangeRequestWBSNode" value="<fmt:message key="close" />" />
    	</div>
    </form>
</div>