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
  ~ File: edit_licensecost.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="update" var="msg_update" />
<fmt:message key="add" var="msg_add" />
<fmt:message key="yes" var="msg_yes" />
<fmt:message key="no" var="msg_no" />
<fmt:message key="msg.confirm_delete" var="msg_delete_licensecost">
	<fmt:param><fmt:message key="licensecost"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_delete_licensecost">
	<fmt:param><fmt:message key="licensecost"/></fmt:param>
</fmt:message>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="deleteLicenseCost"><img onclick="deleteLicenseCost(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></c:set>
</c:if>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="licensescosts.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtCostRequired">
	<fmt:param><b><fmt:message key="licensescosts.cost"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.maximum_allowed" var="fmtCostMax">
	<fmt:param><b><fmt:message key="licensescosts.cost"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var lic_validator;

function viewLicenseCost(element) {
	
	lic_validator.resetForm();
	var licensecost = licensesCostsTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	var f = document.forms["frm_licensecost"];

	// Recover licensecost info
	f.lic_id.value		= licensecost[0];
	f.lic_name.value	= licensecost[1];
	f.lic_cost.value	= licensecost[2];
	f.cost_old.value	= licensecost[2];
	f.<%=Currency.IDCURRENCY %>.value = licensecost[3];
	
	$('#btn_savelicensecost').button("option", "label", '${msg_update}');
	
	$('#licensecost-popup').dialog('open');
}

function deleteLicenseCostConfirmated () {
	$('#dialog-confirm').dialog("close"); 

	var f = document.forms["frm_licensecost"];
	
	var params = {
		accion: "<%=ProjectInitServlet.JX_DELETE_LICENSECOST%>", 
		id: document.forms["frm_project"].id.value, // Project ID
		lic_id: f.lic_id.value
	};
		
	initAjax.call(params, function (data) {
		
		<%if (Settings.FUNDINGSOURCE_CURRENCY) {%>
		$('#currencyOptional1').val(data.<%=Project.CURRENCYOPTIONAL1%>);
		$('#currencyOptional2').val(data.<%=Project.CURRENCYOPTIONAL2%>);
		fundingsource.reload();
		<%}%>
		
	    var total = toNumber($('#totallicensescosts').html());
		var oldCost = toNumber(licensesCostsTable.fnGetSelectedCol(2));

	    var newTotal = parseFloat(total) - parseFloat(oldCost);
	    
	    licensesCostsTable.fnDeleteSelected();
	    
		$('#totallicensescosts').html(toCurrency(newTotal));
		updateTotalCosts();
	});
}

function deleteLicenseCost(element) {

	var licensecost = licensesCostsTable.fnGetData( element.parentNode.parentNode.parentNode );
		
	$('#lic_id').val(licensecost[0]);
	
	$('#dialog-confirm-msg').html('${msg_delete_licensecost}');
	$('#dialog-confirm').dialog(
			'option', 
			'buttons', 
			{
				"${msg_no}": function() { 
					$('#dialog-confirm').dialog("close"); 
				},
				"${msg_yes}": deleteLicenseCostConfirmated
			}
	);
	
	$('#dialog-confirm').dialog(
			'option',
			'title',
			'${msg_title_delete_licensecost}'
	);
	$('#dialog-confirm').dialog('open');
}

function resetLicenseCost() {
	var f = document.forms["frm_licensecost"];
	f.lic_id.value = '';
	f.reset();
}

function newLicenseCost() {
	
	resetLicenseCost();
	lic_validator.resetForm();
	
	$('#btn_savelicensecost').button("option", "label", '${msg_add}');
	$('#licensecost-popup').dialog('open');
	
	var f = document.forms["frm_licensecost"];
	f.cost_old.value = 0;
	
	f.lic_name.focus();
}

function saveLicenseCost() {
	
	if (lic_validator.form()) {
		var f = document.forms["frm_licensecost"];
		var params = {
			accion: "<%=ProjectInitServlet.JX_SAVE_LICENSECOST%>", 
			id: document.forms["frm_project"].id.value, // Project ID
			lic_id: f.lic_id.value,
			lic_name: f.lic_name.value,
			lic_cost: f.lic_cost.value,
			<%=Currency.IDCURRENCY %>: f.<%=Currency.IDCURRENCY %>.value
		};
		initAjax.call(params, function (data) {
			
			<%if (Settings.FUNDINGSOURCE_CURRENCY) {%>
			$('#currencyOptional1').val(data.<%=Project.CURRENCYOPTIONAL1%>);
			$('#currencyOptional2').val(data.<%=Project.CURRENCYOPTIONAL2%>);
			fundingsource.reload();
			<%}%>
			
			var idLicenseCost = data.id;
			var dataRow = [
      		    idLicenseCost,
     		   	f.lic_name.value,
     		   	f.lic_cost.value,
				f.<%=Currency.IDCURRENCY %>.value,
				f.lic_cost.value+$(f.<%=Currency.IDCURRENCY %>).find('option:selected').attr('type'),
				'<nobr><img onclick="viewLicenseCost(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'${deleteLicenseCost}</nobr>'
     		];
			if (f.lic_id.value == data.id) { licensesCostsTable.fnUpdateAndSelect(dataRow); }
			else { licensesCostsTable.fnAddDataAndSelect(dataRow); }

			var total = toNumber($('#totallicensescosts').html());
			total = (total==""?0:total);
			
			var oldCost = toNumber(f.cost_old.value);
			var newCost = toNumber(f.lic_cost.value);
			
			var newTotal = parseFloat(total) - parseFloat(oldCost) + parseFloat(newCost);

			$('#totallicensescosts').html(toCurrency(newTotal));
			updateTotalCosts();
			
			if(f.lic_id.value != "" ) {
				$('#licensecost-popup').dialog('close');
			}
			else {
				f.lic_id.value = "";
				f.lic_name.value = "";
				f.lic_cost.value = "";
				f.<%=Currency.IDCURRENCY %>.value = "";
			}		
		});
	} 
}

function closeLicenseCostDetail() {
	lic_validator.resetForm();
	resetLicenseCost();
	$('#licensecost-popup').dialog('close');
}


readyMethods.add(function() {
	
	//Validate required fields
	lic_validator = $("#frm_licensecost").validate({
		errorContainer: 'div#licensecost-errors',
		errorLabelContainer: 'div#licensecost-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#licensecost_numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			lic_name: "required",
			lic_cost: { required: true, maxlength: <%=Constants.MAX_CURRENCY%> }
		},
		messages: {
			lic_name: { required: '${fmtNameRequired}' },
			lic_cost: { required: '${fmtCostRequired}', maxlength:'${fmtCostMax}' }
		}
	});
	
	$('#licensecost-popup').dialog({ 
		autoOpen: false, 
		modal: true, 
		width: 500, 
		resizable: false,
		open: function(event, ui) { lic_validator.resetForm(); }
	});
});
//-->
</script>

<c:if test="<%=!Settings.FUNDINGSOURCE_CURRENCY%>">
	<c:set var="fundingSourceCurrency">style="display:none;"</c:set>
</c:if>

<div id="licensecost-popup" class="popup">
	<div id="licensecost-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="licensecost_numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	<form name="frm_licensecost" id="frm_licensecost" action="<%=ProjectInitServlet.REFERENCE %>">
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="lic_id" id="lic_id" />
		<input type="hidden" name="cost_old" id="cost_old" value="0" />
		<fieldset>
			<legend><fmt:message key="licensecost"/></legend>
			<table width="100%" cellpadding="2" cellspacing="1" align="center">
				<tr>
					<th class="left" width="55%"><fmt:message key="licensescosts.name" />&nbsp;*</th>
					<th class="left" width="20%"><fmt:message key="licensescosts.cost" />&nbsp;*</th>
					<th class="left" width="20%" ${fundingSourceCurrency }><fmt:message key="currency" /></th>
				</tr>
				<tr>
					<td><input type="text" id="lic_name" name="lic_name" class="campo" maxlength="75" /></td>
					<td><input type="text" id="lic_cost" name="lic_cost" class="campo importe" style="align:left;" /></td>
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
				<input type="submit" class="boton" id="btn_savelicensecost" onclick="saveLicenseCost(); return false;" value="<fmt:message key="add" />" />
			</c:if>
			<input type="submit" class="boton" id="btn_closelicensecost" onclick="closeLicenseCostDetail(); return false;" value="<fmt:message key="close" />" />
		</div>
	</form>
</div>