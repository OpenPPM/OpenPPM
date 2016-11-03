<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>

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
  ~ File: mant_seller.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:06
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.sellers.new" var="new_seller_title" />
<fmt:message key="maintenance.sellers.edit" var="edit_seller_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_seller">
	<fmt:param><fmt:message key="seller"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_seller">
	<fmt:param><fmt:message key="seller"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtSellerNameRequired">
	<fmt:param><b><fmt:message key="maintenance.sellers.name"/></b></fmt:param>
</fmt:message>


<c:set var="p_mant_seller_true"  value="true" />
<c:set var="p_mant_seller_false"  value="false" />

<%-- 
Request Attributes:
	list_maintenance: Seller list
--%>

<script language="javascript" type="text/javascript" >

var sellersTable;
var sellerValidator;

var buttonsSeller = '<img onclick="editSeller(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
        '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
        '<img onclick="deleteSeller(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">';

function addSeller() {
	var f = document.frm_seller_pop;
	f.reset();
	f.id.value = "";
	
	$('#seller_information').text('');
	
	if(!$("#qualified").is(':checked')) {
		$("#qualified_date").datepicker('disable');	
	}
	
	$('#seller-popup legend').html('${new_seller_title}');
	$('#seller-popup').dialog('open');
	
	return false;
}

function saveSeller() {

	var f = document.frm_seller_pop;
	
	if (sellerValidator.form()) {
		
		if($("#selected_seller").is(':checked')) {			
			$("#selected_seller").val(<%=Constants.SELECTED%>);
		}
		else {
			$("#selected_seller").val(<%=Constants.UNSELECTED%>);
		}
		
		if($("#qualified").is(':checked')) {
			$("#qualified").val(<%=Constants.SELECTED%>);	
		}
		else {
			$("#qualified").val(<%=Constants.UNSELECTED%>);
		}
		
		if($("#sole_source").is(':checked')) {
			$("#sole_source").val(<%=Constants.SELECTED%>);	
		}
		else {
			$("#sole_source").val(<%=Constants.UNSELECTED%>);
		}
		
		if($("#single_source").is(':checked')) {
			$("#single_source").val(<%=Constants.SELECTED%>);	
		}
		else {
			$("#single_source").val(<%=Constants.UNSELECTED%>);
		}
		
		
		var params={
			accion: "<%=MaintenanceServlet.JX_SAVE_SELLER %>",
			id: f.id.value,
			name: f.name.value,
			information: f.information.value,
			selected: f.selected.value,
			qualified: f.qualified.value,
			qualifiedDate: f.qualified_date.value,
			sole: f.sole.value,
			single: f.single.value
		};
	
		mainAjax.call(params, function(data){
			var selected_checked = "";
			var qualified_checked = "";
			var sole_checked = "";
			var single_checked = "";
			
			if (f.selected.checked){selected_checked = "checked";}else{selected_checked = "&nbsp;";}
			if (f.qualified.checked){qualified_checked = "checked";}else{qualified_checked = "&nbsp;";}
			if (f.sole.checked){sole_checked = "checked";}else{sole_checked = "&nbsp;";}
			if (f.single.checked){single_checked = "checked";}else{single_checked = "&nbsp;";}

            var dataRow = [
				data.idSeller,
				escape(f.name.value),
				escape(f.information.value),
				'<input type="checkbox" disabled="disabled" ' + qualified_checked + '/>',
				selected_checked,
				qualified_checked,
				f.qualified_date.value,
				sole_checked,
				single_checked,
                '<nobr>'+buttonsSeller+'</nobr>'
		    ];
			
			if (f.id.value == data.idSeller){ sellersTable.fnUpdateAndSelect(dataRow); }
			else { sellersTable.fnAddDataAndSelect(dataRow); }

		 	f.reset();
		 	$('#seller-popup').dialog('close');
		});	
	}	
}

function editSeller(element) {	
		
		var seller =sellersTable.fnGetData( element.parentNode.parentNode.parentNode );
		
		var f = document.forms["frm_seller_pop"];
		f.id.value 		= seller[0];
		f.name.value 	= unEscape(seller[1]);
		$('#seller_information').text(seller[2]);
		if (seller[4] == "checked"){f.selected.checked = "true";}else{f.selected.checked = "";}
		if (seller[5] == "checked"){f.qualified.checked = "true";}else{f.qualified.checked = "";}
		f.qualified_date.value	= seller[6];
		if (seller[7] == "checked"){f.sole.checked = "true";}else{f.sole.checked = "";}
		if (seller[8] == "checked"){f.single.checked = "true";}else{f.single.checked = "";}		

		if(!$("#qualified").is(':checked')) {
			$("#qualified_date").datepicker('disable');	
		}
		else {
			$("#qualified_date").datepicker('enable');
		}
		
		// Display followup info
		$('#seller-popup legend').html('${edit_seller_title}');
		$('#seller-popup').dialog('open');
}

function deleteSeller(element) {

	confirmUI(
		'${msg_title_confirm_delete_seller}','${msg_confirm_delete_seller}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var seller = sellersTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_sellers;

			f.accion.value = "<%=MaintenanceServlet.DEL_SELLER %>";
			f.id.value = seller[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {
	
	sellersTable = $('#tb_sellers').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"iDisplayLength": 50,
		"aaSorting": [[ 0, "asc" ]],
		"aoColumns": [ 
			{ "sClass": "left", "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
			{ "sClass": "left", "bSortable" : false },
			{ "bVisible": false},
			{ "bVisible": false },
			{ "sClass": "center" },
			{ "bVisible": false},
			{ "bVisible": false},
            { "sClass": "center", "bSortable" : false} 
   		]	
	});

	$('#tb_sellers tbody tr').live('click',  function (event) {
		sellersTable.fnSetSelectable(this,'selected_internal');
	});
		
	$('div#seller-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { sellerValidator.resetForm(); }
	});
	
	sellerValidator = $("#frm_seller_pop").validate({
		errorContainer: $('div#seller-errors'),
		errorLabelContainer: 'div#seller-errors ol',		
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#seller-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			name: { required: true },
			information : { maxlength : 100 }
		},
		messages: {
			name: { required: '${fmtSellerNameRequired}'}			
		}
	});
	
	var date = $('#qualified_date').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true		
	});	
	
	$('#qualified').click(function() {
		if($("#qualified").is(':checked')) {
			$("#qualified_date").datepicker('enable');	
		}
		else {
			$("#qualified_date").datepicker('disable');
			$("#qualified_date").val("");
		}
	});	
});

</script>

<%-- Load plugin --%>
<div id="ratingSuppliers"></div>

<form id="frm_sellers" name="frm_sellers" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />
	<fieldset>
		<legend><fmt:message key="maintenance.sellers" /></legend>
			<table id="tb_sellers" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 	<th width="0%"><fmt:message key="maintenance.seller" /></th>
					 	<th width="25%"><fmt:message key="maintenance.sellers.name" /></th>
					 	<th width="42%"><fmt:message key="maintenance.sellers.information" /></th>
					 	<th width="10%"><fmt:message key="maintenance.sellers.qualified" /></th>
					 	<th width="0%"></th>
					 	<th width="0%"></th>
					 	<th width="10%"><fmt:message key="maintenance.sellers.qualified_date" /></th>
					 	<th width="0%"></th>
					 	<th width="0%"></th>
					 	<th width="8%">
							<img onclick="return addSeller();" title="<fmt:message key="new"/>" class="link" src="images/add.png">
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="seller" items="${listSellers}">
						<tr>
							<td>${seller.idSeller}</td>
							<td>${tl:escape(seller.name)}</td>
							<td>${tl:escape(seller.information)}</td>
							<td>
								<input type="checkbox" disabled="disabled" <c:if test="${seller.qualified == p_mant_seller_true}">checked</c:if>/>										
							</td>
							<td>
								<c:if test="${seller.selected == p_mant_seller_true}">checked</c:if>
							</td>
							<td>
								<c:if test="${seller.qualified == p_mant_seller_true}">checked</c:if>
							</td>
							<td><fmt:formatDate value="${seller.qualificationDate}" pattern="${datePattern}"/></td>
							<td>
								<c:if test="${seller.soleSource == p_mant_seller_true}">checked</c:if>
							</td>
							<td>
								<c:if test="${seller.singleSource == p_mant_seller_true}">checked</c:if>
							</td>
							<td>
								<nobr>
									<img onclick="editSeller(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteSeller(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="seller-popup">

	<div id="seller-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="seller-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_seller_pop" id="frm_seller_pop">
		<input type="hidden" name="id" id="seller_id"/>
		<div class="hide">
			<fmt:message key="maintenance.sellers.selected" />
			<input type="checkbox" name="selected" id="selected_seller" />
			<fmt:message key="maintenance.sellers.single_source" />
			<input type="checkbox" name="single" id="single_source" />
		</div>
		<fieldset>
			<legend><fmt:message key="maintenance.sellers.new"/></legend>
			<table cellpadding="2" cellspacing="1" style="width:100%; border:0;">
				<tr>
					<th style="width:46%"><fmt:message key="maintenance.sellers.name" />&nbsp;*</th>
					<th style="width:18%"><fmt:message key="maintenance.sellers.sole_source" /></th>					
					<th style="width:12%"><fmt:message key="maintenance.sellers.qualified" /></th>
					<th style="width:24%"><fmt:message key="maintenance.sellers.qualified_date" /></th>
				</tr>
				<tr>
					<td><input type="text" name="name" id="seller_name" class="campo" maxlength="45"/></td>
					<td><input type="checkbox" name="sole" id="sole_source" /></td>
					<td><input type="checkbox" name="qualified" id="qualified" /></td>
					<td><input type="text" name="qualified_date" id="qualified_date" class="campo fecha" /></td>
				</tr>
				<tr>
				 	<th colspan="4">
				 		<fmt:message key="maintenance.sellers.information" />
				 	</th>
				</tr>
				<tr>	
					<td colspan="4">
						<textarea name="information" id="seller_information" class="campo" style="width:98%;" rows="6"></textarea>						
					</td>
				</tr>
    		</table>
    	</fieldset>
   		<div class="popButtons">
   			<input type="submit" class="boton" onclick="saveSeller(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

