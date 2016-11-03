<%@page import="es.sm2.openppm.core.model.impl.Projectlabel"%>
<%@page import="es.sm2.openppm.core.model.impl.Label"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectInitServlet"%>
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
  ~ File: labels.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:01
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_select" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="label"/></b></fmt:param>
</fmt:message>
<fmt:message key="yes" var="msgYes" />
<fmt:message key="no" var="msgNo" />
<fmt:message key="msg.confirm_delete" var="msgDelete">
	<fmt:param><fmt:message key="label"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleDelete">
	<fmt:param><fmt:message key="label"/></fmt:param>
</fmt:message>

<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
	<c:set var="buttonsProjectLabels"><img title="<fmt:message key="view"/>" class="link viewLabel" src="images/view.png">&nbsp;&nbsp;&nbsp;<img title="<fmt:message key="delete"/>" class="link removeLabel" src="images/delete.jpg"></c:set>
</c:if>

<script type="text/javascript">

var labelsTable, labelValidator, labelPopup;

var labels = {
	view : function() {
		labelValidator.resetForm();
		var row = labelsTable.fnGetData( this.parentNode.parentNode.parentNode );
		
		var f = document.forms["frmLabel"];
		f.reset();

		f.<%=Projectlabel.IDPROJECTLABEL%>.value = row[0];
		f.<%=Label.IDLABEL%>.value				 = row[1];
		
		labelPopup.dialog('open');
	},
	add : function() {
		labelValidator.resetForm();
		
		var f = document.forms["frmLabel"];
		f.reset();
		f.<%=Projectlabel.IDPROJECTLABEL%>.value 	='';
		
		labelPopup.dialog('open');
	},
	save : function() {
		if (labelValidator.form()) {
			
			var f = document.forms["frmLabel"];
			
			initAjax.call($("#frmLabel").serializeArray(), function (data) {
				
				var dataRow = [
	      		    data.<%=Projectlabel.IDPROJECTLABEL %>,
					f.<%=Label.IDLABEL%>.value,
					$(f.<%=Label.IDLABEL%>).find('option:selected').text(),
					$(f.<%=Label.IDLABEL%>).find('option:selected').prop("title"),
					'<nobr>${buttonsProjectLabels}</nobr>'
	  			];
				if (f.<%=Projectlabel.IDPROJECTLABEL %>.value == data.<%=Projectlabel.IDPROJECTLABEL %>) {
					labelsTable.fnUpdateAndSelect(dataRow);
				}
				else { labelsTable.fnAddDataAndSelect(dataRow); }
				labelPopup.dialog('close');
			});
		}
	},
	remove : function() {
		
		var row = labelsTable.fnGetData( this.parentNode.parentNode.parentNode );
		
		var params = {
			accion: "<%=ProjectInitServlet.JX_DELETE_LABEL%>", 
			<%=Projectlabel.IDPROJECTLABEL%>: row[0]
		};
		
		confirmUI('${msgTitleDelete}', '${msgDelete}', '${msgYes}', '${msgNo}', function() {
		
			initAjax.call(params, function (data) { labelsTable.fnDeleteSelected(); });
		});
	},
	close: function() { labelPopup.dialog('close'); }
};

readyMethods.add(function() {
	
	labelsTable = $('#tb_labels').dataTable({
		"oLanguage": datatable_language,
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": false,
		"aoColumns": [
			{ "bVisible": false },
			{ "bVisible": false },
			{ "bVisible": true },
			{ "bVisible": true },
			{ "sClass": "center", "bSortable": false, "bVisible": ${op:hasPermissionSetting(settings, user,project.status,tab)} }
		]
	});
	
	$('#tb_labels tbody tr').live('click', function (event) {		
		labelsTable.fnSetSelectable(this,'selected_internal');
	});
	
	labelValidator = $("#frmLabel").validate({
		errorContainer: 'div#labelErrors',
		errorLabelContainer: 'div#labelErrors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#labelNumerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Label.IDLABEL%>: "required"
		},
		messages: {
			<%=Label.IDLABEL%>: { required: '${fmtNameRequired}'}
		}
	});
	
	labelPopup = $('#labelPopup').dialog({ 
		autoOpen: false, 
		modal: true, 
		width: 500, 
		resizable: false,
		open: function(event, ui) { labelValidator.resetForm(); }
	});

	$(".removeLabel").live('click', labels.remove);
	$(".viewLabel").live('click', labels.view);
	$("#addLabel").on('click', labels.add);
	$("#saveLabel").on('click', labels.save);
	$("#closeLabel").on('click', labels.close);
});
</script>

<table id="tb_labels" class="tabledata" cellspacing="1" width="100%">
	<thead>
		<tr>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th width="38%"><fmt:message key="labels.name" /></th>
			<th width="46%"><fmt:message key="labels.description" /></th>
			<th width="8%">
				<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
					<img id="addLabel" title="<fmt:message key="add"/>" class="link" src="images/add.png">
				</c:if>
			</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="projectLabel" items="${projectLabels}">
		<tr>
			<td>${projectLabel.idProjectLabel}</td>
			<td>${projectLabel.label.idLabel}</td>
			<td>${tl:escape(projectLabel.label.name)}</td>
			<td>${tl:escape(projectLabel.label.description)}</td>
			<td>
				<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
					<nobr>${buttonsProjectLabels }</nobr>
				</c:if>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>
<div style="width: 100px;">&nbsp;</div>

<div id="labelPopup" class="popup">
	<div id="labelErrors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="labelNumerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	<form name="frmLabel" id="frmLabel" action="<%=ProjectInitServlet.REFERENCE %>">
		<input type="hidden" name="accion" value="<%=ProjectInitServlet.JX_SAVE_LABEL %>" />
		<input type="hidden" name="<%=Projectlabel.IDPROJECTLABEL%>"/>
		<input type="hidden" name="idProject" value="${project.idProject}"/>
		<fieldset>
			<legend><fmt:message key="labels"/></legend>
			<table width="100%" cellpadding="2" cellspacing="1" align="center">
				<tr>
					<th width="100%"><fmt:message key="label" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<select name="<%=Label.IDLABEL%>" class="campo">
							<option value='' selected><fmt:message key="select_opt" /></option>
							<c:forEach var="label" items="${labels }">
								<option title="${label.description}" value="${label.idLabel}">${label.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
		</fieldset>
		<div class="popButtons">
			<input type="button" class="boton" id="saveLabel" value="<fmt:message key="save" />" />
			<input type="button" class="boton" id="closeLabel" value="<fmt:message key="close" />" />
		</div>
	</form>
</div>