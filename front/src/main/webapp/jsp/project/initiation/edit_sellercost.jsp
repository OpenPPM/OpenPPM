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
  ~ File: edit_sellercost.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="update" var="msg_update" />
<fmt:message key="add" var="msg_add" />
<fmt:message key="yes" var="msg_yes" />
<fmt:message key="no" var="msg_no" />
<fmt:message key="msg.confirm_delete" var="msg_delete_sellercost">
	<fmt:param><fmt:message key="sellercost"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_delete_sellercost">
	<fmt:param><fmt:message key="sellercost"/></fmt:param>
</fmt:message>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="deleteSellerCost"><img onclick="deleteSellerCost(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></c:set>
</c:if>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="sellerscosts.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtCostRequired">
	<fmt:param><b><fmt:message key="sellerscosts.cost"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.maximum_allowed" var="fmtCostMax">
	<fmt:param><b><fmt:message key="sellerscosts.cost"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var sel_validator;

function viewSellerCost(element) {
	
	sel_validator.resetForm();
	var sellercost = sellersCostsTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	var f = document.forms["frm_sellercost"];

	// Recover sellercost info
	f.sel_id.value		= sellercost[0];
	f.sel_name.value	= sellercost[1];
	f.sel_cost.value	= sellercost[2];
	f.cost_old.value	= sellercost[2];
	f.<%=Currency.IDCURRENCY %>.value = sellercost[3];
	
	$('#btn_savesellercost').button("option", "label", '${msg_update}');
	
	$('#sellercost-popup').dialog('open');
}

function deleteSellerCostConfirmated () {
	$('#dialog-confirm').dialog("close"); 

	var f = document.forms["frm_sellercost"];
	
	var params = {
		accion: "<%=ProjectInitServlet.JX_DELETE_SELLERCOST%>", 
		id: document.forms["frm_project"].id.value, // Project ID
		sel_id: f.sel_id.value
	};
		
	initAjax.call(params, function (data) {
		
		<%if (Settings.FUNDINGSOURCE_CURRENCY) {%>
		$('#currencyOptional1').val(data.<%=Project.CURRENCYOPTIONAL1%>);
		$('#currencyOptional2').val(data.<%=Project.CURRENCYOPTIONAL2%>);
		fundingsource.reload();
		<%}%>
		
	    var total = toNumber($('#totalsellerscosts').html());
		var oldCost = toNumber(sellersCostsTable.fnGetSelectedCol(2));

	    var newTotal = parseFloat(total) - parseFloat(oldCost);
	    
	    sellersCostsTable.fnDeleteSelected();
	    
		$('#totalsellerscosts').html(toCurrency(newTotal));
		updateTotalCosts();
	});
}

function deleteSellerCost(element) {
	
	var sellercost = sellersCostsTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	$('#sel_id').val(sellercost[0]);
	
	$('#dialog-confirm-msg').html('${msg_delete_sellercost}');
	$('#dialog-confirm').dialog(
			'option', 
			'buttons', 
			{
				"${msg_no}": function() { 
					$('#dialog-confirm').dialog("close"); 
				},
				"${msg_yes}": deleteSellerCostConfirmated
			}
	);
	
	$('#dialog-confirm').dialog(
			'option',
			'title',
			'${msg_title_delete_sellercost}'
	);
	$('#dialog-confirm').dialog('open');
}

function resetSellerCost() {
	var f = document.forms["frm_sellercost"];
	f.sel_id.value = '';
	f.reset();
}

function newSellerCost() {
	
	resetSellerCost();
	sel_validator.resetForm();
	
	$('#btn_savesellercost').button("option", "label", '${msg_add}');
	$('#sellercost-popup').dialog('open');

	var f = document.forms["frm_sellercost"];
	f.cost_old.value	=  0;
	
	f.sel_name.focus();
}

function saveSellerCost() {
	
	if (sel_validator.form()) {
		var f = document.forms["frm_sellercost"];				
		var params = {
			accion: "<%=ProjectInitServlet.JX_SAVE_SELLERCOST%>", 
			id: document.forms["frm_project"].id.value, // Project ID
			sel_id: f.sel_id.value,
			sel_name: f.sel_name.value,
			sel_cost: f.sel_cost.value,
			<%=Currency.IDCURRENCY %>: f.<%=Currency.IDCURRENCY %>.value
		};			
		initAjax.call(params, function (data) {
			
			<%if (Settings.FUNDINGSOURCE_CURRENCY) {%>
			$('#currencyOptional1').val(data.<%=Project.CURRENCYOPTIONAL1%>);
			$('#currencyOptional2').val(data.<%=Project.CURRENCYOPTIONAL2%>);
			fundingsource.reload();
			<%}%>
			
			var idSellerCost = data.id;			
			var dataRow = [
      		    idSellerCost,
      		    f.sel_name.value,
				f.sel_cost.value,
				f.<%=Currency.IDCURRENCY %>.value,
				f.sel_cost.value+$(f.<%=Currency.IDCURRENCY %>).find('option:selected').attr('type'),
				'<nobr><img onclick="viewSellerCost(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'${deleteSellerCost}</nobr>'
  			];			
			if (f.sel_id.value == data.id) { sellersCostsTable.fnUpdateAndSelect(dataRow); }
			else { sellersCostsTable.fnAddDataAndSelect(dataRow); }			

			var total = toNumber($('#totalsellerscosts').html());
			total = (total == "" ? 0 : total);			
			var oldCost = toNumber(f.cost_old.value);
			var newCost = toNumber(f.sel_cost.value);			
			var newTotal = parseFloat(total) - parseFloat(oldCost) + parseFloat(newCost);			
			$('#totalsellerscosts').html(toCurrency(newTotal));
			updateTotalCosts();			
			if(f.sel_id.value != "" ) {
				$('#sellercost-popup').dialog('close');
			}
			else {
				f.sel_id.value = "";
				f.sel_name.value = "";
				f.sel_cost.value = "";
				f.<%=Currency.IDCURRENCY %>.value = "";
			}					
		});
	} 
}

function closeSellerCostDetail() {
	sel_validator.resetForm();
	resetSellerCost();
	$('#sellercost-popup').dialog('close');
}


readyMethods.add(function() {
	
	//Validate required fields
	sel_validator = $("#frm_sellercost").validate({
		errorContainer: 'div#sellercost-errors',
		errorLabelContainer: 'div#sellercost-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#sellercost_numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			sel_name: "required",
			sel_cost: { required: true, maxlength: <%=Constants.MAX_CURRENCY%> }
		},
		messages: {
			sel_name: { required: '${fmtNameRequired}'},
			sel_cost: { required: '${fmtCostRequired}', maxlength:'${fmtCostMax}' }
		}
	});
	
	$('#sellercost-popup').dialog({ 
		autoOpen: false, 
		modal: true, 
		width: 500, 
		resizable: false,
		open: function(event, ui) { sel_validator.resetForm(); }
	});
});
//-->
</script>

<c:if test="<%=!Settings.FUNDINGSOURCE_CURRENCY%>">
	<c:set var="fundingSourceCurrency">style="display:none;"</c:set>
</c:if>

<div id="sellercost-popup" class="popup">
	<div id="sellercost-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="sellercost_numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	<form name="frm_sellercost" id="frm_sellercost" action="<%=ProjectInitServlet.REFERENCE %>">
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="sel_id" id="sel_id" />
		<input type="hidden" name="cost_old" id="cost_old" value="0" />
		<fieldset>
			<legend><fmt:message key="sellercost"/></legend>
			<table width="100%" cellpadding="2" cellspacing="1" align="center">
				<tr>
					<th class="left" width="55%"><fmt:message key="sellerscosts.name" />&nbsp;*</th>
					<th class="left" width="20%"><fmt:message key="sellerscosts.cost" />&nbsp;*</th>
					<th class="left" width="20%" ${fundingSourceCurrency }><fmt:message key="currency" /></th>
				</tr>
				<tr>
					<td><input type="text" id="sel_name" name="sel_name" class="campo" maxlength="75" /></td>
					<td><input type="text" id="sel_cost" name="sel_cost" class="campo importe" style="align:left;" /></td>
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
				<input type="submit" class="boton" id="btn_savesellercost" onclick="saveSellerCost(); return false;" value="<fmt:message key="add" />" />
			</c:if>
			<a href="javascript:closeSellerCostDetail();" class="boton" id="btn_closesellercost"><fmt:message key="close" /></a>
		</div>
	</form>
</div>