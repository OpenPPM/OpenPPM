<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectInitServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Fundingsource"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectfundingsource"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
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
  ~ File: fundingsource.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:02
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_select" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="funding_source"/></b></fmt:param>
</fmt:message>
<fmt:message key="yes" var="msgYes" />
<fmt:message key="no" var="msgNo" />
<fmt:message key="msg.confirm_delete" var="msgDelete">
	<fmt:param><fmt:message key="funding_source"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleDelete">
	<fmt:param><fmt:message key="funding_source"/></fmt:param>
</fmt:message>

<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
	<c:set var="buttonsFundingSource"><img onclick="fundingsource.view(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">&nbsp;&nbsp;&nbsp;<img onclick="fundingsource.remove(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></c:set>
</c:if>

<script type="text/javascript">

var fundingsourceTable, foundingValidator, fundingsourcePopup;

var fundingsource = {
	view : function(element) {
		foundingValidator.resetForm();
		var fundingSource = fundingsourceTable.fnGetData( element.parentNode.parentNode.parentNode );
		
		var f = document.forms["frmFundingSource"];
		f.reset();

		f.<%=Projectfundingsource.IDPROJFUNDINGSOURCE %>.value 	= fundingSource[0];
		f.<%=Fundingsource.IDFUNDINGSOURCE %>.value				= fundingSource[1];
		f.<%=Projectfundingsource.PERCENTAGE %>.value 			= fundingSource[3];
		f.<%=Projectfundingsource.EPIGRAFEEURO %>.value 		= fundingSource[4];
		f.<%=Projectfundingsource.EUROS %>.value 				= fundingSource[5];
		f.<%=Projectfundingsource.EPIGRAFEDOLAR %>.value 		= fundingSource[6];
		f.<%=Projectfundingsource.DOLARES %>.value 				= fundingSource[7];
		
		<%if (Settings.FUNDINGSOURCE_CURRENCY) {%>
		var num = fundingsourceTable.fnGetPosition(element.parentNode.parentNode.parentNode) + 1;
		
		if (f.<%=Projectfundingsource.EPIGRAFEEURO %>.value == '') {
			f.<%=Projectfundingsource.EPIGRAFEEURO %>.value = $('#<%=Project.CHARTLABEL%>').val().replace('-', '')+(num+'<fmt:message key="currency.e_text"/>');
		}
		if (f.<%=Projectfundingsource.EPIGRAFEDOLAR %>.value == '') {
			f.<%=Projectfundingsource.EPIGRAFEDOLAR %>.value = $('#<%=Project.CHARTLABEL%>').val().replace('-', '')+(num+'<fmt:message key="currency.d_text"/>');
		}
		<%}%>
		
		fundingsourcePopup.dialog('open');
	},
	add : function() {
		foundingValidator.resetForm();
		
		var f = document.forms["frmFundingSource"];
		f.reset();
		f.<%=Projectfundingsource.IDPROJFUNDINGSOURCE %>.value 	='';
		
		<%if (Settings.FUNDINGSOURCE_CURRENCY) {%>
		
		var num = fundingsourceTable.fnGetData().length + 1;
		
		f.<%=Projectfundingsource.EPIGRAFEEURO %>.value  = $('#<%=Project.CHARTLABEL%>').val().replace('-', '')+(num+'<fmt:message key="currency.e_text"/>');
		f.<%=Projectfundingsource.EPIGRAFEDOLAR %>.value = $('#<%=Project.CHARTLABEL%>').val().replace('-', '')+(num+'<fmt:message key="currency.d_text"/>');
		
		<%}%>
		
		fundingsourcePopup.dialog('open');
	},
	save : function() {
		if (foundingValidator.form()) {
			
			var f = document.forms["frmFundingSource"];
			
			initAjax.call($("#frmFundingSource").serializeArray(), function (data) {
				
				var dataRow = [
	      		    data.<%=Projectfundingsource.IDPROJFUNDINGSOURCE %>,
					f.<%=Fundingsource.IDFUNDINGSOURCE %>.value,
					$(f.<%=Fundingsource.IDFUNDINGSOURCE %>).find('option:selected').text(),
					f.<%=Projectfundingsource.PERCENTAGE %>.value,
					f.<%=Projectfundingsource.EPIGRAFEEURO %>.value,
					f.<%=Projectfundingsource.EUROS %>.value,
					f.<%=Projectfundingsource.EPIGRAFEDOLAR %>.value,
					f.<%=Projectfundingsource.DOLARES %>.value,
					'<nobr>${buttonsFundingSource}</nobr>'
	  			];
				if (f.<%=Projectfundingsource.IDPROJFUNDINGSOURCE %>.value == data.<%=Projectfundingsource.IDPROJFUNDINGSOURCE %>) {
					fundingsourceTable.fnUpdateAndSelect(dataRow);
				}
				else { fundingsourceTable.fnAddDataAndSelect(dataRow); }
				fundingsourcePopup.dialog('close');
			});
		}
	},
	reload : function() {
		
		var params = {
			accion: "<%=ProjectInitServlet.JX_LIST_FUNDINGSOURCES%>",
			id: document.forms["frm_project"].id.value
		};
		
		initAjax.call(params, function (data) {
			
			fundingsourceTable.fnClearTable();
			
			$(data).each(function() {
				var dataRow = [
	      		    this.<%=Projectfundingsource.IDPROJFUNDINGSOURCE %>,
					this.<%=Fundingsource.IDFUNDINGSOURCE %>,
					this.<%=Fundingsource.NAME %>,
					this.<%=Projectfundingsource.PERCENTAGE %>,
					this.<%=Projectfundingsource.EPIGRAFEEURO %>,
					this.<%=Projectfundingsource.EUROS %>,
					this.<%=Projectfundingsource.EPIGRAFEDOLAR %>,
					this.<%=Projectfundingsource.DOLARES %>,
					'<nobr>${buttonsFundingSource}</nobr>'
	  			];
				
				fundingsourceTable.fnAddData(dataRow);
			});
		});
	},
	remove : function(element) {
		confirmUI('${msgTitleDelete}', '${msgDelete}', '${msgYes}', '${msgNo}', function() {
			
			var fundingSource = fundingsourceTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var params = {
				accion: "<%=ProjectInitServlet.JX_DELETE_FUNDINGSOURCE%>", 
				<%=Projectfundingsource.IDPROJFUNDINGSOURCE %>: fundingSource[0]
			};
				
			initAjax.call(params, function (data) { fundingsourceTable.fnDeleteSelected(); });
		});
	},
	close: function() { fundingsourcePopup.dialog('close'); }
};

readyMethods.add(function() {
	
	fundingsourceTable = $('#tb_fundingsource').dataTable({
		"oLanguage": datatable_language,
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": false,
		"aoColumns": [
			{ "bVisible": false },
			{ "bVisible": false },
			{ "bVisible": true },
			{ "sClass": "center" },
			{ "bVisible": <%=Settings.FUNDINGSOURCE_CURRENCY%> },
			{ "sClass": "right", "bVisible": <%=Settings.FUNDINGSOURCE_CURRENCY%> },
			{ "bVisible": <%=Settings.FUNDINGSOURCE_CURRENCY%> },
			{ "sClass": "right", "bVisible": <%=Settings.FUNDINGSOURCE_CURRENCY%> },
			{ "sClass": "center", "bSortable": false }
		]
	});
	
	$('#tb_fundingsource tbody tr').live('click', function (event) {		
		fundingsourceTable.fnSetSelectable(this,'selected_internal');
	});
	
	foundingValidator = $("#frmFundingSource").validate({
		errorContainer: 'div#fundingSourceErrors',
		errorLabelContainer: 'div#fundingSourceErrors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#fundingSourceNumerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Fundingsource.IDFUNDINGSOURCE%>: "required"
		},
		messages: {
			<%=Fundingsource.IDFUNDINGSOURCE%>: { required: '${fmtNameRequired}'}
		}
	});
	
	fundingsourcePopup = $('#fundingSourcePopup').dialog({ 
		autoOpen: false, 
		modal: true, 
		width: 500, 
		resizable: false,
		open: function(event, ui) { foundingValidator.resetForm(); }
	});
	
	<%if (Settings.FUNDINGSOURCE_CURRENCY) {%>
	$('#percentage').on('change', function() {
		
		var f = document.forms["frmFundingSource"];
		
		var euros	= toNumber($('#currencyOptional1').val());
		var dolares = toNumber($('#currencyOptional2').val());
		
		var percentage = $(this).val();
		
		f.<%=Projectfundingsource.EUROS %>.value = toCurrency(euros * percentage / 100);
		f.<%=Projectfundingsource.DOLARES %>.value = toCurrency(dolares * percentage / 100);
	});
	<%}%>
});
</script>

<table id="tb_fundingsource" class="tabledata" cellspacing="1" width="100%">
	<thead>
		<tr>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th width="38%"><fmt:message key="funding_source" /></th>
			<th width="8%"><fmt:message key="funding_source.percentage" />&nbsp;%</th>
			<th width="15%"><fmt:message key="funding_source.epigrafe.euro" /></th>
			<th width="8%"><fmt:message key="currency.euro" /></th>
			<th width="15%"><fmt:message key="funding_source.epigrafe.dolar" /></th>
			<th width="8%"><fmt:message key="currency.dolar" /></th>
			<th width="8%">
				<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
					<img onclick="fundingsource.add()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
				</c:if>
			</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="projFunding" items="${projFundingsources}">
		<tr>
			<td>${projFunding.idProjFundingSource}</td>
			<td>${projFunding.fundingsource.idFundingSource}</td>
			<td>${projFunding.fundingsource.name}</td>
			<td>${projFunding.percentage}</td>
			<td>${projFunding.epigrafeEuro}</td>
			<td>${tl:toCurrency(projFunding.euros)}</td>
			<td>${projFunding.epigrafeDolar}</td>
			<td>${tl:toCurrency(projFunding.dolares)}</td>
			<td>
				<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
					<nobr>${buttonsFundingSource }</nobr>
				</c:if>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>
<div style="width: 100px;">&nbsp;</div>

<c:if test="<%=!Settings.FUNDINGSOURCE_CURRENCY%>">
	<c:set var="fundingSourceCurrency">style="display:none;"</c:set>
</c:if>

<div id="fundingSourcePopup" class="popup">
	<div id="fundingSourceErrors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="fundingSourceNumerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	<form name="frmFundingSource" id="frmFundingSource" action="<%=ProjectInitServlet.REFERENCE %>">
		<input type="hidden" name="accion" value="<%=ProjectInitServlet.JX_SAVE_FUNDINGSOURCE %>" />
		<input type="hidden" name="<%=Projectfundingsource.IDPROJFUNDINGSOURCE%>"/>
		<input type="hidden" name="idProject" value="${project.idProject}"/>
		<fieldset>
			<legend><fmt:message key="funding_source"/></legend>
			<table width="100%" cellpadding="2" cellspacing="1" align="center">
				<tr>
					<th width="80%"><fmt:message key="funding_source" />&nbsp;*</th>
					<th width="20%"><fmt:message key="funding_source.percentage" /></th>
				</tr>
				<tr>
					<td>
						<select name="<%=Fundingsource.IDFUNDINGSOURCE %>" class="campo">
							<option value='' selected><fmt:message key="select_opt" /></option>
							<c:forEach var="fundingsource" items="${fundingsources }">
								<option value="${fundingsource.idFundingSource}">${fundingsource.name}</option>
							</c:forEach>
						</select>
					</td>
					<td><input type="text" name="<%=Projectfundingsource.PERCENTAGE %>" id="percentage" class="campo integer"/></td>
				</tr>
			</table>
			<table width="100%" cellpadding="2" cellspacing="1" align="center" ${fundingSourceCurrency }>
				<tr>
					<th width="30%"><fmt:message key="funding_source.epigrafe.euro" /></th>
					<th width="20%"><fmt:message key="currency.euro" /></th>
					<th width="30%"><fmt:message key="funding_source.epigrafe.dolar" /></th>
					<th width="20%"><fmt:message key="currency.dolar" /></th>
				</tr>
				<tr>
					<td><input type="text" name="<%=Projectfundingsource.EPIGRAFEEURO %>" class="campo" maxlength="50"/></td>
					<td><input type="text" name="<%=Projectfundingsource.EUROS %>" class="campo importe" readonly/></td>
					<td><input type="text" name="<%=Projectfundingsource.EPIGRAFEDOLAR %>" class="campo" maxlength="50"/></td>
					<td><input type="text" name="<%=Projectfundingsource.DOLARES %>" class="campo importe" readonly/></td>
				</tr>
			</table>
		</fieldset>
		<div class="popButtons">
			<input type="submit" class="boton" onclick="fundingsource.save(); return false;" value="<fmt:message key="save" />" />
			<input type="button" class="boton" onclick="fundingsource.close(); return false;" value="<fmt:message key="close" />" />
		</div>
	</form>
</div>