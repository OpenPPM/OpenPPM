<%@page import="es.sm2.openppm.core.model.impl.Wbstemplate"%>
<%@page import="es.sm2.openppm.core.model.impl.Company"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>

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
  ~ File: new_wbsnode_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:48
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtParentRequired">
	<fmt:param><b><fmt:message key="wbs.parent"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="wbs.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.info.updated_milestones" var="fmtUpdateMilestones"></fmt:message>
<fmt:message key="msg.info.updated_wbs" var="fmtUpdateWBS"></fmt:message>
<fmt:message key="msg.info.updated_wbs_budget" var="fmtUpdateWBSEV">
	<fmt:param><fmt:message key="activity.ev"/></fmt:param>
	<fmt:param><fmt:message key="percent_complete"/></fmt:param>
</fmt:message>
<fmt:message key="msg.confirm" var="msgConfirm" />
<fmt:message key="msg.confirm.ca_to_not_ca" var="msgCAToNotCA" />
<fmt:message key="msg.confirm.remove_ca_app0" var="msgRemoveCA" />

<c:set var="importIcon"><img onclick="importWBSTemplate(this)" class="position_right icono" src="images/add_proj.png" title="<fmt:message key="wbs.import_wbs"/>"/></c:set>

<script type="text/javascript">

var wbsNodeValidator;

var planAjax = new AjaxCall('<%=ProjectPlanServlet.REFERENCE%>','<fmt:message key="error"/>');


function importWBSTemplate(element){
	
	var wbsTemplate = wbsTemplatesTable.fnGetData( element.parentNode.parentNode );
	
	var f = document.forms["frm_wbsnode"];
	
	var params = {
		accion: "<%= ProjectPlanServlet.JX_IMPORT_WBS %>", 
		idWBSTemplate: wbsTemplate[0],
		idWBSParent: f.wbs_parent_import.value,
		replace:  f.wbs_parent_replace.checked,
		idProject: $("#id").val()
	};
	
	planAjax.call(params, function (data) {
		var f = document.forms["frm_project"];
		f.accion.value = "";
		loadingPopup();
		$("#validate").val(true);
		f.submit();
	});
	
}

function viewWBSSelection(){
	$('#selection').css("display","block");
	$('#wbsNodo').css("display","none");
	$('#wbsTemplates').css("display","none");
}


function viewWBSNodo(){
	$('#selection').css("display","none");
	$('#wbsNodo').css("display","block");
	$('#wbsTemplates').css("display","none");
}

function viewWBSTemplates(){
	
	var params = {
		accion: "<%= ProjectPlanServlet.JX_VIEW_TEMPLATES_WBS %>"
	};
		
	wbsTemplatesTable.fnClearTable();
	
	planAjax.call(params, function(data) {
		
		$(data.wbsTemplates).each(function() {
			
			var kpi = [
				this.<%= Wbstemplate.IDWBSNODE %>,
				this.<%= Wbstemplate.NAME %>,
				'${importIcon}'
			];
			
			wbsTemplatesTable.fnAddData(kpi);
		});
		
		$('#selection').css("display","none");
		$('#wbsNodo').css("display","none");
		$('#wbsTemplates').css("display","block");
		
	});
}

function saveWbsNode() {
	var f 		= document.forms["frm_wbsnode"];
	f.id.value 	= document.forms["frm_project"].id.value;
	
	if ((f.isCA.value == "true") && ($('input#wbs_ca').prop('checked') == false) && (f.emptyBudget.value != "") && (f.emptyBudget.value != "0,00")) {
		
		confirmUI("${msgConfirm }", "${msgCAToNotCA}", "${msg_yes}", "${msg_no}",
			function() {
			validateAndSaveWBS(f);
		});
		
	}
	else {
		validateAndSaveWBS(f);
	}
	
}

function validateAndSaveWBS(f) {
	
	var paramsCheck = {
		accion: "<%= ProjectPlanServlet.JX_WBS_NODE_VALIDATE %>",
		wbs_id: f.wbs_id.value,
		wbs_ca: f.wbs_ca.checked
	};
	
	planAjax.call(paramsCheck, function (data) {
		
		if (data.confirm) {
			
			confirmUI("${msgConfirm }", data.confirmMsg, "${msg_yes}", "${msg_no}",
				function() {
					saveWBS(f);
			});
		}
		else {
			saveWBS(f);
		}
	});
}

function saveWBS(f) {

	if(wbsNodeValidator.form()) {

		var params = {
			accion: "<%= ProjectPlanServlet.JX_WBS_NODE_VALIDATE_AND_SAVE %>",
			id: f.id.value,
			wbs_id: f.wbs_id.value,
			wbs_code: f.wbs_code.value,
			wbs_parent: f.wbs_parent.value,
			wbs_name: f.wbs_name.value,
			wbs_desc: f.wbs_desc.value,
			wbs_ca: f.wbs_ca.checked,
			wbs_budget: f.wbs_budget.value
		};

		planAjax.call(params, function (data) {

			document.forms["frm_project"].message.value = '${fmtUpdateWBS}';

			if (data.updateMilestones) {
				document.forms["frm_project"].messageUpdateMilestones.value = '${fmtUpdateMilestones}';
			}

			if(data.updateEV){
				document.forms["frm_project"].messageUpdateEV.value = '${fmtUpdateWBSEV}';
			}

			toTab(1, this);
		});
	}

}

function closeWbsNode() {
	$('div#wbsnode-popup').dialog('close');
}

readyMethods.add(function() {
	$('div#wbsnode-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false,
		open: function(event, ui) { wbsNodeValidator.resetForm(); }
	});
	
	wbsNodeValidator = $("#frm_wbsnode").validate({
		errorContainer: 'div#wbsnode-errors',
		errorLabelContainer: 'div#wbsnode-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#wbsnode-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			wbs_name: "required",
		    wbs_parent: "required",
		    wbs_desc: { maxlength: 2500 }
		},
		messages: {
			wbs_name: { required: '${fmtNameRequired}' },
		    wbs_parent: { required: '${fmtParentRequired}' }
		}
	});

	$('input#wbs_ca').change(function(ev) {
		if ($('input#wbs_ca').prop('checked')) {
			$('input#wbs_budget').attr('disabled',false);
		}
		else {
			$('input#wbs_budget').attr('disabled',true);		
		}
	});
	
	wbsTemplatesTable = $('#tb_wbstemplates').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,		
		"bInfo": false,
		"bLengthChange": false,
		"iDisplayLength": 25,
		"bAutoWidth": false,
		"aoColumns": [ 
             { "bVisible": false }, 
             { "sClass": "center", "bSortable": true },
             { "sClass": "center", "bSortable": false} 
     	]
	});
	
	
	
});
</script>

<div id="wbsnode-popup" class="popup">

	<div id="wbsnode-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="wbsnode-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_wbsnode" id="frm_wbsnode" method="post" action="<%=ProjectPlanServlet.REFERENCE%>" >
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="id" value="" />
		<input type="hidden" name="wbs_id" id="wbs_id" value="" />
		<input type="hidden" name="isCA" id="isCA" value="" />
		<input type="hidden" name="emptyBudget" id="emptyBudget" value="" />		
		
		<%-- SELECT OPTION --%>
		<div id="selection" style="text-align: center; margin-bottom: 12px; margin-top: 14px; display:block;">
			<fieldset>
				<legend><fmt:message key="wbs.selection"/></legend>
				<fmt:message key="wbs.create_node"/>:&nbsp;<input type="radio" name="options_add" value="create" style="margin-right: 10px; width: 15px;" onclick="viewWBSNodo();"/> 
				<fmt:message key="wbs.import_wbs"/>:&nbsp;<input type="radio" name="options_add" value="import" style="width:15px;" onclick="viewWBSTemplates();"/>
			</fieldset>
		</div>
		
		<%-- WBS NODO --%>
		<div id="wbsNodo" style="display: none;">
			<fieldset>
				<legend><fmt:message key="wbs_node"/></legend>
			
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr>
						<th class="left" width="64%"><fmt:message key="wbs.parent"/></th>
						<th class="left" width="36%"><fmt:message key="wbs.code"/></th>
					</tr>
					<tr>
						<td>
							<select name="wbs_parent" id="wbs_parent" class="campo"></select>
						</td>
						<td>
							<input type="text" name="wbs_code" class="campo" maxlength="25" />
						</td>
					</tr>
					<tr>
						<th class="left"><fmt:message key="wbs.name"/>&nbsp;*</th>
						<th class="left"><fmt:message key="wbs.budget"/></th>
					</tr>
					<tr>
						<td>
							<input type="text" id="wbs_name" name="wbs_name" class="campo" maxlength="80" />
						</td>
						<td>
							<input type="text" name="wbs_budget" id="wbs_budget" class="campo importe" value="${tl:toCurrency(0)}" disabled/>
						</td>
					</tr>
					<tr>
						<th class="left"><fmt:message key="wbs.ca_desc"/>
							<input style="width:20px; vertical-align: top;" type="checkbox" name="wbs_ca" id="wbs_ca"/>
						</th>
						<td></td>					
					</tr>
					<tr><th class="left" colspan="2"><fmt:message key="wbs.dictionary"/></th></tr>
					<tr>
						<td colspan="2">
							<textarea name="wbs_desc" class="campo" rows="10" style="width:98%;"></textarea>
						</td>
					</tr>
	    		</table>
	    	</fieldset>
	    	
	    	<div class="popButtons">
	    		<c:if test="${op:hasPermission(user,project.status,tab)}">
	    			<input type="submit" class="boton" onclick="saveWbsNode(); return false;" value="<fmt:message key="save" />" />
				</c:if>
				<a href="javascript:closeWbsNode();" class="boton"><fmt:message key="close" /></a>
    		</div>
    		
	    </div>
	    
	    <%-- IMPORT TREE --%>
	    <div id="wbsTemplates" style="display:none; margin-bottom: 10px;">
		    <fieldset>
				<legend><fmt:message key="wbs.import_wbs"/></legend>
				
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr>
						<th class="left" width="80%"><fmt:message key="wbs.parent"/></th>
						<th class="center" width="20%"><fmt:message key="wbs.parent_replace"/></th>
					</tr>
					<tr>
						<td>
							<select name="wbs_parent_import" id="wbs_parent_import" class="campo" style="width:100%; margin-left:0px;"></select>
						</td>
						<td>
							<input name="wbs_parent_replace" id="wbs_parent_replace" type="checkbox" value="">
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table>
				
				<table id="tb_wbstemplates" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							<th width="0%"></th>
							<th width="90%"><fmt:message key="wbs.name"/></th>
							<th width="10%">&nbsp;</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
				
			</fieldset>
	    </div>
    	
    </form>
</div>