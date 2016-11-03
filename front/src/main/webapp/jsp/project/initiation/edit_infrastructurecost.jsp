<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.model.impl.Currency"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.front.servlets.ProjectInitServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

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
  ~ File: edit_infrastructurecost.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="update" var="msg_update" />
<fmt:message key="add" var="msg_add" />
<fmt:message key="yes" var="msg_yes" />
<fmt:message key="no" var="msg_no" />
<fmt:message key="msg.confirm_delete" var="msg_delete_infrastructurecost">
	<fmt:param><fmt:message key="infrastructurecost"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_delete_infrastructurecost">
	<fmt:param><fmt:message key="infrastructurecost"/></fmt:param>
</fmt:message>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="deleteInfrastructureCost"><img onclick="deleteInfrastructureCost(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></c:set>
</c:if>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="infrastructurecosts.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtCostRequired">
	<fmt:param><b><fmt:message key="infrastructurecosts.cost"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.maximum_allowed" var="fmtCostMax">
	<fmt:param><b><fmt:message key="infrastructurecosts.cost"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var inf_validator;

function viewInfrastructureCost(element) {
	
	inf_validator.resetForm();

	var infrastructurecost = infrastructuresCostsTable.fnGetData( element.parentNode.parentNode.parentNode );

	var f = document.forms["frm_infrastructurecost"];

	// Recover infrastructurecost info
	f.inf_id.value		= infrastructurecost[0];
	f.inf_name.value	= infrastructurecost[1];
	f.inf_cost.value	= infrastructurecost[2];
	f.cost_old.value	= infrastructurecost[2];
	f.<%=Currency.IDCURRENCY %>.value = infrastructurecost[3];
	
	$('#btn_saveinfrastructurecost').button("option", "label", '${msg_update}');
	
	$('#infrastructurecost-popup').dialog('open');
}

function deleteInfrastructureCostConfirmated () {
	$('#dialog-confirm').dialog("close"); 

	var f = document.forms["frm_infrastructurecost"];
	
	var params = {
		accion: "<%=ProjectInitServlet.JX_DELETE_INFRASTRUCTURECOST%>", 
		id: document.forms["frm_project"].id.value, // Project ID
		inf_id: f.inf_id.value
	};
		
	initAjax.call(params, function (data) {
		
		<%if (Settings.FUNDINGSOURCE_CURRENCY) {%>
		$('#currencyOptional1').val(data.<%=Project.CURRENCYOPTIONAL1%>);
		$('#currencyOptional2').val(data.<%=Project.CURRENCYOPTIONAL2%>);
		fundingsource.reload();
		<%}%>
		
	    var total = toNumber($('#totalinfrastructurecosts').html());
	    
		var oldCost = toNumber(infrastructuresCostsTable.fnGetSelectedCol(2));

	    var newTotal = parseFloat(total) - parseFloat(oldCost);
	    
	    infrastructuresCostsTable.fnDeleteSelected();
	    
		$('#totalinfrastructurecosts').html(toCurrency(newTotal));
		updateTotalCosts();
	});
}

function deleteInfrastructureCost(element) {
		
	var infrastructurecost = infrastructuresCostsTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	$('#inf_id').val(infrastructurecost[0]);
	
	$('#dialog-confirm-msg').html('${msg_delete_infrastructurecost}');
	$('#dialog-confirm').dialog(
			'option', 
			'buttons', 
			{
				"${msg_no}": function() { 
					$('#dialog-confirm').dialog("close"); 
				},
				"${msg_yes}": deleteInfrastructureCostConfirmated
			}
	);
	
	$('#dialog-confirm').dialog(
			'option',
			'title',
			'${msg_title_delete_infrastructurecost}'
	);
	$('#dialog-confirm').dialog('open');
}

function resetInfrastructureCost() {
	var f = document.forms["frm_infrastructurecost"];
	f.inf_id.value = '';
	f.reset();
}

function newInfrastructureCost() {
	
	resetInfrastructureCost();
	inf_validator.resetForm();
	
	$('#btn_saveinfrastructurecost').button("option", "label", '${msg_add}');
	$('#infrastructurecost-popup').dialog('open');
	
	var f = document.forms["frm_infrastructurecost"];
	f.cost_old.value	=  0;

	f.inf_name.focus();
}

function saveInfrastructureCost() {
	
	if (inf_validator.form()) {
		var f = document.forms["frm_infrastructurecost"];
		var params = {
			accion: "<%=ProjectInitServlet.JX_SAVE_INFRASTRUCTURECOST%>", 
			id: document.forms["frm_project"].id.value, // Project ID
			inf_id: f.inf_id.value,
			inf_name: f.inf_name.value,
			inf_cost: f.inf_cost.value,
			<%=Currency.IDCURRENCY %>: f.<%=Currency.IDCURRENCY %>.value
		};
			
		initAjax.call(params, function (data) {
			
			<%if (Settings.FUNDINGSOURCE_CURRENCY) {%>
			$('#currencyOptional1').val(data.<%=Project.CURRENCYOPTIONAL1%>);
			$('#currencyOptional2').val(data.<%=Project.CURRENCYOPTIONAL2%>);
			fundingsource.reload();
			<%}%>
			
			var idInfrastructureCost = data.id;

			var dataRow = [
		    	idInfrastructureCost,
			    f.inf_name.value,
			    f.inf_cost.value,
			    f.<%=Currency.IDCURRENCY %>.value,
			    f.inf_cost.value+$(f.<%=Currency.IDCURRENCY %>).find('option:selected').attr('type'),
				'<nobr><img onclick="viewInfrastructureCost(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'${deleteInfrastructureCost}</nobr>'
     		];
			if (f.inf_id.value == data.id) { infrastructuresCostsTable.fnUpdateAndSelect(dataRow); }
			else { infrastructuresCostsTable.fnAddDataAndSelect(dataRow); }
			
			var total = toNumber($('#totalinfrastructurecosts').html());
			total = (total==""?0:total);
			
			var oldCost = toNumber(f.cost_old.value);
			var newCost = toNumber(f.inf_cost.value);
			
			var newTotal = parseFloat(total) - parseFloat(oldCost) + parseFloat(newCost);
			
			$('#totalinfrastructurecosts').html(toCurrency(newTotal));
			updateTotalCosts();
			
			if(f.inf_id.value != "" ) {
				$('#infrastructurecost-popup').dialog('close');
			}
			else {
				f.inf_id.value = "";
				f.inf_name.value = "";
				f.inf_cost.value = "";
				f.<%=Currency.IDCURRENCY %>.value = "";
			}		
		});
	} 
}

function closeInfrastructureCostDetail() {
	inf_validator.resetForm();
	resetInfrastructureCost();
	$('#infrastructurecost-popup').dialog('close');
}


readyMethods.add(function() {
	
	//Validate required fields
	inf_validator = $("#frm_infrastructurecost").validate({
		errorContainer: 'div#infrastructurecost-errors',
		errorLabelContainer: 'div#infrastructurecost-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#infrastructurecost_numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			inf_name: "required",
			inf_cost: { required: true, maxlength: <%=Constants.MAX_CURRENCY%> }
		},
		messages: {
			inf_name: { required: '${fmtNameRequired}' },
			inf_cost: { required: '${fmtCostRequired}', maxlength:'${fmtCostMax}' }
		}
	});
	
	$('#infrastructurecost-popup').dialog({ 
		autoOpen: false, 
		modal: true, 
		width: 500, 
		resizable: false,
		open: function(event, ui) { inf_validator.resetForm(); }
	});
});
//-->
</script>

<c:if test="<%=!Settings.FUNDINGSOURCE_CURRENCY%>">
	<c:set var="fundingSourceCurrency">style="display:none;"</c:set>
</c:if>

<div id="infrastructurecost-popup" class="popup">
	<div id="infrastructurecost-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="infrastructurecost_numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	<form name="frm_infrastructurecost" id="frm_infrastructurecost" action="<%=ProjectInitServlet.REFERENCE %>">
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="inf_id" id="inf_id" />
		<input type="hidden" name="cost_old" id="cost_old" value="0" />
		<fieldset>
			<legend><fmt:message key="infrastructurecost"/></legend>
			<table width="100%" cellpadding="2" cellspacing="1" align="center">
				<tr>
					<th class="left" width="55%"><fmt:message key="infrastructurecosts.name" />&nbsp;*</th>
					<th class="left" width="20%"><fmt:message key="infrastructurecosts.cost" />&nbsp;*</th>
					<th class="left" width="20%" ${fundingSourceCurrency }><fmt:message key="currency" /></th>
				</tr>
				<tr>
					<td><input type="text" id="inf_name" name="inf_name" class="campo" maxlength="75" /></td>
					<td><input type="text" id="inf_cost" name="inf_cost" class="campo importe" style="align:left;" /></td>
					<td ${fundingSourceCurrency }>
						<select name="<%=Currency.IDCURRENCY %>" class="campo">
							<option value='' selected type=""><fmt:message key="select_opt" /></option>
							<c:forEach var="currency" items="${currencies }">
								<option value="${currency.idCurrency}" type="${currency.currency}">${currency.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
		</fieldset>
		<div class="popButtons">
			<c:if test="${not op:isStringEquals(project.status, status_closed)}">
				<input type="submit" class="boton" id="btn_saveinfrastructurecost" onclick="saveInfrastructureCost(); return false;" value="<fmt:message key="add" />" />
			</c:if>
			<a href="javascript:closeInfrastructureCostDetail();" class="boton" id="btn_closeinfrastructurecost"><fmt:message key="close" /></a>
		</div>
	</form>
</div>