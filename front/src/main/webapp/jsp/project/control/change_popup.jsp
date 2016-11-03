<%@page import="es.sm2.openppm.core.model.impl.Changecontrol"%>
<%@page import="es.sm2.openppm.core.model.impl.Wbsnode"%>
<%@page import="es.sm2.openppm.core.model.impl.Changerequestwbsnode"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@taglib uri="Visual" prefix="visual" %>

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
  ~ File: change_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="select_opt" var="msg_select_opt" />
<fmt:message key="change.priority.low" var="low" />
<fmt:message key="change.priority.normal" var="normal" />
<fmt:message key="change.priority.high" var="high" />
<fmt:message key="error" var="fmt_error" />

<%-- 
Request Attributes:
	changeTypes:	Change types List
--%>
		
<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtTypeRequired">
	<fmt:param><b><fmt:message key="change.change_type"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtPriorityRequired">
	<fmt:param><b><fmt:message key="change.priority"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtValidDateRequired">
	<fmt:param><b><fmt:message key="date"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtDescRequired">
	<fmt:param><b><fmt:message key="desc"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtResolutionRequired">
	<fmt:param><b><fmt:message key="change.resolution"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtOtherRequired">
	<fmt:param><b><fmt:message key="change.originator"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtValidDateFormat">
	<fmt:param><b><fmt:message key="date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtResolutionDateFormat">
	<fmt:param><b><fmt:message key="change.resolution_date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_changerequest_wbsnode">
	<fmt:param><fmt:message key="wbs.ca_long"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_changerequest_wbsnode">
	<fmt:param><fmt:message key="wbs.ca_long"/></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var changeRequestWBSNodeTable;
var changeValidator;
var analyzeRequired = false;
var resolveRequired = false;

function showAnalyzeInputs() {
	$('.analyzed_inputs').show();
	analyzeRequired = true;
	return false;
}

function showResolutionInputs() {
	$('.resolution_inputs').show();
	resolveRequired = true;
	return false;
}

function analyzeChangeRequest() {
	showAnalyzeInputs();
	
	$('#btn_analyze').hide();
}

function resolveChangeRequest() {
	showResolutionInputs();

	$('#btn_resolve').hide();
}

function resetChangeRequest() {
	
	document.forms["frm_change"].reset();
	document.forms["frm_change"].change_id.value = '';
	$('.analyzed_inputs').hide();
	$('#btn_analyze').hide();
	$('.resolution_inputs').hide();
	$('#btn_resolve').hide();
	$('#originator').show();
	analyzeRequired = false;
	resolveRequired = false;
	
	changeRequestWBSNode.errorUnmark();
	
	return false;
}

function saveChangeRequest() {
	
	//Validate required fields
	if (changeValidator.form() && 
			(!analyzeRequired || (analyzeRequired && changeRequestWBSNodeTable.fnGetNodes().length > 0) )) {
		
		changeRequestWBSNode.errorUnmark();
		
		var f = document.forms["frm_change"];

		if (resolveRequired) {
			
			// Resolution change refresh page
			f.action.value 		= '<%=ProjectControlServlet.REFERENCE%>';
			f.accion.value 		= "<%=ProjectControlServlet.SAVE_REQUEST_CHANGE %>";
			f.id.value 			= document.forms["frm_project"].id.value;
			f.change_id.value 	= f.change_id.value;
	
			f.scrollTop.value = $(document).scrollTop();
			
			f.submit();
		}
		else {
			
			var params = {
				accion: 			"<%=ProjectControlServlet.JX_SAVE_REQUEST_CHANGE %>",
				id: 				document.forms["frm_project"].id.value,
				change_id: 			f.change_id.value,
				type: 				f.type.value,
				priority: 			f.priority.value,
				date: 				f.date.value,
				desc: 				f.desc.value,
				solution: 			f.solution.value,
				originator: 		f.originator.value,
				impact: 			f.impact.value,
				resolution: 		$("input[name='resolution']:checked").val(),
				resolution_date: 	f.resolution_date.value,
				resolution_reason: 	f.resolution_reason.value,
				resolution_name: 	f.resolution_name.value	
			};
			
			controlAjax.call(params, function(data) {
				
				var deleteIcon = "";
				
				if (changeRequestWBSNodeTable.fnGetNodes().length == 0) {
					deleteIcon = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
					'<img onclick="deleteChangeRequestStatus(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">'
				}
				
				var dataRow = [
               			 data.id,
	               		 (data.wbsnode==''?'<fmt:message key="change.open"/>':'<fmt:message key="analyze"/>'),
 					     escape(data.desc),
 					     (data.priority == 'H' ? '${high}' : (data.priority == 'L' ? '${low}' : '${normal}')),
 					     data.priority,
 					     data.date,
 					     escape(data.originator),
 					     escape(data.type_desc),
 					     data.type,
 					     escape(data.solution),
 					     '',
 					     '',
 					     escape(data.impact),
 					     data.resolution,
 					     data.resolution_date,
 					     escape(data.resolution_reason),
 					     '<nobr>'+
	 					     '<img src="images/odt.png" class="link" onclick="toChangeODT(' +data.id+ ');" alt="[ODT]" title="${msg_to_odt}" />'+
	 					     '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
	 					     '<img onclick="editChangeRequestStatus(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
	 					     deleteIcon +
 					     '</nobr>'
      				];

				if (f.change_id.value == data.id) { changeRequestTable.fnUpdateAndSelect(dataRow); }
				else { 
					changeRequestTable.fnAddDataAndSelect(dataRow);
				}
				
				//editChangeRequest(dataRow);
				changeRequestWBSNode.consult(dataRow);
			});
		}
	}
	else if (analyzeRequired && changeRequestWBSNodeTable.fnGetNodes().length == 0) {
		changeRequestWBSNode.errorMark();
	}
}

function closeChangeRequest() {
	
	$("#change-popup").hide();
	$("#panels").show();
}

function openChangeRequest() {
	
	$("#panels").hide();
	$("#change-popup").show();
}

//ChangeRequesWBSNode Object
var changeRequestWBSNode = {
	
		// Consult
		consult: function(aData) {
			
			var idChange = aData[0];
			
			if (typeof idChange !== 'undefined') {
				
				// Get control accounts associated
				var params = {
					accion: "<%=ProjectControlServlet.JX_CONSULT_CHANGEREQUEST_WBSNODE %>",
					idChange: idChange
				};
					
				controlAjax.call(params, function(data) {
	
					changeRequestWBSNodeTable.fnClearTable();
					
					for (var i= 0; i < data.changesReqWBS.length; i++) {
						
						var dataRow = [
							data.changesReqWBS[i].<%= Changerequestwbsnode.IDCHANGEREQUESTWBSNODE %>,
							data.changesReqWBS[i].<%= Wbsnode.IDWBSNODE %>,
							data.changesReqWBS[i].wbsNodeName,
							data.changesReqWBS[i].<%= Changerequestwbsnode.ESTIMATEDEFFORT %>,
							data.changesReqWBS[i].<%= Changerequestwbsnode.ESTIMATEDCOST %>,
							'<nobr><img onclick="changeRequestWBSNode.view(this);" title="<fmt:message key="view"/>" class="link" src="images/view.png"></nobr>'+
							'&nbsp;&nbsp;'+
							'<img onclick="changeRequestWBSNode.delete(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">'
				        ];
						
						changeRequestWBSNodeTable.fnAddDataAndSelect(dataRow); 
					}
					
					editChangeRequest(aData);
				});
			}
			else {
				alertUI("Error", "No data idChange");
			}
		},
		// View
		view: function(element) {
			
			var changeRequestWBSNode = changeRequestWBSNodeTable.fnGetData(element.parentNode.parentNode.parentNode);	
			
			var f = changeRequestWBSNodePopup.form();
			
			// Set parameters
			f.<%= Changerequestwbsnode.IDCHANGEREQUESTWBSNODE %>.value 	= changeRequestWBSNode[0];
			f.<%= Changecontrol.IDCHANGE %>.value 						= $("#change_id").val();
			f.<%= Wbsnode.IDWBSNODE %>.value 							= changeRequestWBSNode[1];
			f.<%= Changerequestwbsnode.ESTIMATEDEFFORT %>.value 		= changeRequestWBSNode[3];
			f.<%= Changerequestwbsnode.ESTIMATEDCOST %>.value 			= changeRequestWBSNode[4];	
			
			changeRequestWBSNodePopup.open();
		},
		// Add
		add: function() {
			
			changeRequestWBSNodePopup.reset();
			
			var f = changeRequestWBSNodePopup.form();
			f.<%= Changecontrol.IDCHANGE %>.value = $("#change_id").val();
			
			changeRequestWBSNodePopup.open();
		},
		// Delete
		delete: function(element) {
			confirmUI(
					'${msg_title_confirm_delete_changerequest_wbsnode}','${msg_confirm_delete_changerequest_wbsnode}',
					'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
					function() {
						
						var dataRow = changeRequestWBSNodeTable.fnGetData(element.parentNode.parentNode);
						
						var params={
							accion: 											"<%= ProjectControlServlet.JX_DELETE_CHANGEREQUEST_WBSNODE %>",
							<%= Changerequestwbsnode.IDCHANGEREQUESTWBSNODE %>: dataRow[0]
						};
						
						controlAjax.call(params, function(data){
							changeRequestWBSNodeTable.fnDeleteSelected();
						});	
					}
			);
		},
		// Show error
		errorMark: function() {
			
			$("#change-ca-errors ol:eq(0)").html('<li><fmt:message key="wbs.ca_long"/></li>');
			
			$("#change-ca-errors").show();
			
			$("#tb_change_request_wbs").parent().css("border", "1px dotted #FF0000");
		},
		errorUnmark: function() {
			
			$("#change-ca-errors").hide();
			
			$("#tb_change_request_wbs").parent().css("border", "0px");
		}
		
		
};

readyMethods.add(function() {

	resetChangeRequest();
	
	$('select#sel_originator').change( function() {
		
		if (this.value == -1) {
			$("#originator").show();
			$("#originator").val('');
		}
		else {
			$("#originator").hide();
			$("#originator").val(this.value);
		}
	});
	
	changeValidator = $("#frm_change").validate({
		errorContainer: 'div#change-errors',
		errorLabelContainer: 'div#change-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#change-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			type: {"required": true},
			priority: {"required": true},
			date: { required: true, date: true },
			desc: { required: true, maxlength:3000 },
			resolution: {"resolveRequired": true},
			originator: {"otherRequired": true},
			resolution_reason: { maxlength:3000},
			impact: { maxlength:3000 },
			solution: { maxlength:3000 },
			resolution_date: { date: true }
		},
		messages: {
			type: { required: '${fmtTypeRequired}' },
			priority: { required: '${fmtPriorityRequired}' },
			date: {
				required: '${fmtValidDateRequired}',
				date: '${fmtValidDateFormat}'
			},
			desc: { required: '${fmtDescRequired}' },
			resolution: {"resolveRequired": '${fmtResolutionRequired}'},
			originator: {"otherRequired": '${fmtOtherRequired}'},
			resolution_date: { date: '${fmtResolutionDateFormat}' }
		}
	});

	$.validator.addMethod("otherRequired", function(value, element) {
		
		if ($("#sel_originator").val() == -1) {
			return !this.optional(element);
		}
		return true;
	});
	
	$.validator.addMethod("resolveRequired", function(value, element) {
		if (resolveRequired) {
			return !this.optional(element);
		}
		return true;
	});
	
	changeRequestWBSNodeTable = $('#tb_change_request_wbs').dataTable({
		"oLanguage": datatable_language,
		"bFilter": false,
		"bInfo": false,
		"bSortable": [[2,"asc"]],
		"bPaginate": true,
		"sPaginationType": "full_numbers",
		"aoColumns": [		              
		              { "bVisible": false },	
		              { "bVisible": false },		     
		              { "bVisible": true },
		              { "bVisible": true, "sClass": "right"},
		              {	"bVisible": true, "sClass": "right" },
		              { "sClass": "center", "bSortable": false }
		      		]
	});
	
	<%-- Button --%>
	$('#addChangeRequestWBSNode').on('click', function(e){
		changeRequestWBSNode.add();
	});

	$('#tb_change_request_wbs tbody tr').live('click',  function (event) {
		changeRequestWBSNodeTable.fnSetSelectable(this,'selected_internal');
	});
});
//-->
</script>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<script type="text/javascript">
	<!--
		readyMethods.add(function() {
			$('#change_request_date').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,
				onSelect: function() {
					if (changeValidator.numberOfInvalids() > 0) changeValidator.form();
				}
			});

			$('#resolution_date').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,
				onSelect: function() {
					if (changeValidator.numberOfInvalids() > 0) changeValidator.form();
				}
			});			
		});
	//-->
	</script>
</c:if>

<div id="change-popup" style="display:none; padding: 15px;">
	<fmt:message key="create_change_request" var="titleChangeRequest"/>
	<visual:panel id="changeRequestWBSNode" title="${titleChangeRequest}">
		
		<div id="change-errors" class="ui-state-error ui-corner-all hide">
			<p>
				<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
				<strong><fmt:message key="msg.error_title"/></strong>
				&nbsp;(<b><span id="change-numerrors"></span></b>)
			</p>
			<ol></ol>
		</div>
		
		<div id="change-ca-errors" class="ui-state-error ui-corner-all hide">
			<p>
				<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
				<strong><fmt:message key="msg.error_title"/></strong>
				&nbsp;
			</p>
			<ol></ol>
		</div>
		
		<form name="frm_change" id="frm_change" action="<%=ProjectControlServlet.REFERENCE%>" method="post">
			<input type="hidden" name="id" /> <%-- id Project --%>
			<input type="hidden" name="change_id" id="change_id"/> <%-- id Changecontrol --%>
			<input type="hidden" name="accion" />
			<input type="hidden" name="scrollTop" />
			
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="45%"><fmt:message key="change.change_type" />&nbsp;*</th>
					<th class="left" width="25%"><fmt:message key="change.priority" />&nbsp;*</th>
					<th class="left" width="30%"><fmt:message key="change.date" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<select name="type" class="campo">
							<option value='' selected><fmt:message key="select_opt"/></option>
							<c:forEach var="type" items="${changeTypes}">
								<option value="${type.idChangeType}">${type.description}</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<select name="priority" class="campo">
							<option value="H"><fmt:message key="change.priority.high"/></option>
							<option value="N" selected><fmt:message key="change.priority.normal"/></option>
							<option value="L"><fmt:message key="change.priority.low"/></option>
						</select>
					</td>
					<td><input type="text" id="change_request_date" name="date" class="campo fecha" /></td>
				</tr>
				<tr>
					<th class="left" colspan="3"><fmt:message key="change.desc"/>&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="3">
						<textarea name="desc" class="campo" style="width: 100%;" rows="5"></textarea>
					</td>
				</tr>
				<tr>
					<th class="left" colspan="3"><fmt:message key="change.recommended_solution"/></th>
				</tr>
				<tr>
					<td colspan="3">
						<textarea name="solution" class="campo" style="width: 100%;" rows="5"></textarea>
					</td>
				</tr>
				<tr>
					<th class="left" colspan="3"><fmt:message key="change.originator"/>&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<select class="campo" id="sel_originator">
							<option value='-1'><fmt:message key="other"/></option>
							<c:forEach var="stakeholder" items="${project.stakeholders}">
								<c:choose>
									<c:when test="${stakeholder.contactName == null}">
										<option value="${stakeholder.employee.contact.fullName}">${tl:escape(stakeholder.employee.contact.fullName)}</option>
									</c:when>
									<c:otherwise>
										<option value="${stakeholder.contactName}">${tl:escape(stakeholder.contactName)}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</td>
					<td colspan="2">
						<input type="text" name="originator" id="originator" class="campo" maxlength="50" />
					</td>
				</tr>
				<%-- Control accounts --%>
				<tr class="analyzed_inputs">
					<th colspan="3">
						<fmt:message key="changerequests_wbsnode"/>
					</th>
				</tr>
				<tr class="analyzed_inputs">
					<td colspan="3">
						<table id="tb_change_request_wbs" class="tabledata" cellspacing="1" style="width:100%">
							<thead>
								<tr>					
									<th width="0%">&nbsp;</th>	<%-- id ChangeRequestWbsnode --%>
									<th width="0%">&nbsp;</th> 	<%-- id WBSNode --%>
									<th width="65%"><fmt:message key="wbs.ca_long"/></th>
									<th width="15%"><fmt:message key="change.estimated_effort"/></th>
									<th width="15%"><fmt:message key="change.estimated_cost"/></th>
									<th width="5%"><img src="images/add.png" id="addChangeRequestWBSNode" class="link" title="<fmt:message key="add"/>" /></th>
								</tr>
							</thead>
							<tbody></tbody>
						</table>
					</td>
				</tr>
				<tr class="analyzed_inputs">
					<th colspan="3" class="left"><fmt:message key="change.impact_desc"/></th>
				</tr>
				<tr class="analyzed_inputs">
					<td colspan="3">
						<textarea name="impact" class="campo" style="width: 100%;" rows="5"></textarea>
					</td>
				</tr>
				<tr class="resolution_inputs" >
					<th align="left" colspan="3">
						<span style="margin-right:35px;"><fmt:message key="change.accepted"/>&nbsp;*</span>
						<span style="margin-right:20px;"><fmt:message key="change.resolution_date"/>&nbsp;*</span>
						<span><fmt:message key="change.resolution_name"/>:&nbsp;</span>
					</th>
				</tr>
				<tr class="resolution_inputs">
					<td colspan="3">
						<span style="margin-right:10px;">
							<input type="radio" name="resolution" value="Y" style="width:20px" /><fmt:message key="yes" />
							<input type="radio" name="resolution" value="N" style="width:20px" /><fmt:message key="no" />
						</span>
						<span style="margin-right:10px;">
							<input type="text" name="resolution_date" id="resolution_date" class="campo fecha" />
						</span>
						<input type="text" name="resolution_name" id="resolution_name" class="campo" style="width:25%" />
					</td>
				</tr>
				<tr class="resolution_inputs">
					<th colspan="3" class="left"><fmt:message key="comments"/></th>
				</tr>
				<tr class="resolution_inputs">
					<td colspan="3">
						<textarea name="resolution_reason" class="campo" style="width: 100%;" rows="5"></textarea>
					</td>
				</tr>
	   		</table>
	    	<div class="popButtons">
				<c:if test="${op:hasPermission(user,project.status,tab)}">
					<span id="btn_analyze"><a href="javascript:analyzeChangeRequest();" class="boton"><fmt:message key="analyze" /></a></span>
					<span id="btn_resolve"><a href="javascript:resolveChangeRequest();" class="boton"><fmt:message key="change.solve" /></a></span>
					<input type="button" class="boton" onclick="saveChangeRequest();" value="<fmt:message key="save" />" />
				</c:if>
				<a href="javascript:closeChangeRequest();" class="boton"><fmt:message key="back" /></a>
	    	</div>
	    </form>
	</visual:panel>
</div>

<%-- Include popup --%>
<jsp:include page="changerequest_wbsnode_popup.jsp" flush="true" />
