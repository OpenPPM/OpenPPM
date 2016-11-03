<%@page import="es.sm2.openppm.core.model.impl.Fundingsource"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

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
  ~ File: mant_funding_source.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.fundingsource.new" var="new_fundingsource_title" />
<fmt:message key="maintenance.fundingsource.edit" var="edit_fundingsource_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_fundingsource">
	<fmt:param><fmt:message key="maintenance.fundingsource"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_fundingsource">
	<fmt:param><fmt:message key="maintenance.fundingsource"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtFundingSourceNameRequired">
	<fmt:param><b><fmt:message key="maintenance.fundingsource.name"/></b></fmt:param>
</fmt:message>


<script language="javascript" type="text/javascript" >

var fundingSourceTable;
var fundingSourceValidator;

function addFundingSource() {
	var f = document.frm_fundingsource_pop;
	f.reset();
	f.id.value = "";
	
	$('#fundingsource-popup legend').html('${new_fundingsource_title}');
	$('#fundingsource-popup').dialog('open');
}

function saveFundingSource() {

	var f = document.frm_fundingsource_pop;
	
	if (fundingSourceValidator.form()) {
		
		var params={
			accion: "<%=MaintenanceServlet.JX_SAVE_FUNDINGSOURCE %>",
			id: f.id.value,
			name: f.fundingsource_name.value	
		};
		
		mainAjax.call(params, function(data){
			var dataRow = [
				data.<%=Fundingsource.IDFUNDINGSOURCE%>,				
				escape(data.<%=Fundingsource.NAME%>),
				'<nobr><img onclick="editFundingSource(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteFundingSource(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'          
			];

			if (f.id.value == data.<%=Fundingsource.IDFUNDINGSOURCE%>) { fundingSourceTable.fnUpdateAndSelect(dataRow); }
			else { fundingSourceTable.fnAddDataAndSelect(dataRow); }
			
			f.reset();
			$('#fundingsource-popup').dialog('close');
		});	
	}
}

function editFundingSource(element) {
		
		var fundingSource = fundingSourceTable.fnGetData( element.parentNode.parentNode.parentNode );
					
		var f = document.forms["frm_fundingsource_pop"];
		f.reset();
		
		f.id.value 		= fundingSource[0];
		f.name.value 	= unEscape(fundingSource[1]);
		
		// Display followup info
		$('#fundingsource-popup legend').html('${edit_fundingsource_title}');
		$('#fundingsource-popup').dialog('open');
}

function deleteFundingSource(element) {
	confirmUI(
		'${msg_title_confirm_delete_fundingsource}','${msg_confirm_delete_fundingsource}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var fundingSource = fundingSourceTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_fundingsource;

			f.accion.value = "<%=MaintenanceServlet.DEL_FUNDINGSOURCE %>";
			f.id.value = fundingSource[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	fundingSourceTable = $('#tb_fundingsource').dataTable({
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"aaSorting": [[ 0, "asc" ]],
			"aoColumns": [ 
				{ "sClass": "center", "bVisible" : false },
				{ "sClass": "left" },
		        { "sClass": "center", "bSortable" : false}
	      	]	
	});

	$('#tb_fundingsource tbody tr').live('click',  function (event) {
		fundingSourceTable.fnSetSelectable(this,'selected_internal');
	});

	$('div#fundingsource-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { fundingSourceValidator.resetForm(); }
	});

	fundingSourceValidator = $("#frm_fundingsource_pop").validate({
		errorContainer: $('div#fundingsource-errors'),
		errorLabelContainer: 'div#fundingsource-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#fundingsource-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			name: {required: true }
		},
		messages:{
			name: {required:'${fmtFundingSourceNameRequired}'}
		}
	});
});

</script>

<form id="frm_fundingsource" name="frm_fundingsource" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />
	<fieldset>
		<legend><fmt:message key="maintenance.fundingsource" /></legend>
			<table id="tb_fundingsource" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
					 <th width="0%"><fmt:message key="maintenance.fundingsource" /></th>
					 <th width="92%"><fmt:message key="maintenance.fundingsource.name" /></th>
					 <th width="8%">
						<img onclick="addFundingSource()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					 </th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="fundingsource" items="${listFundingSource}">
						<tr>
							<td>${fundingsource.idFundingSource}</td>
							<td>${tl:escape(fundingsource.name)}</td>
							<td>
								<nobr>
									<img onclick="editFundingSource(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" />
									&nbsp;&nbsp;&nbsp;
									<img onclick="deleteFundingSource(this)" title="<fmt:message key="delete"/>" src="images/delete.jpg"/>
								</nobr>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</fieldset>
</form>

<div id="fundingsource-popup">

	<div id="fundingsource-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="fundingsource-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_fundingsource_pop" id="frm_fundingsource_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.fundingsource.new"/></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
				 <th><fmt:message key="maintenance.fundingsource.name" />&nbsp;*</th>
				 <th>&nbsp;</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="fundingsource_id" />
						<input type="text" name="name" id="fundingsource_name" class="campo" maxlength="50"/>
					</td>
				</tr>
    		</table>
    	</fieldset>
   	   	<div class="popButtons">
   	   		<input type="submit" class="boton" onclick="saveFundingSource(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

