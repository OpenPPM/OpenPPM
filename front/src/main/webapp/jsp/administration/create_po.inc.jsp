<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.servlets.AdministrationServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="Visual" prefix="visual" %>

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
  ~ File: create_po.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:25
  --%>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtPerformingOrgNameRequired">
	<fmt:param><b><fmt:message key="maintenance.perfoming_org.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtFullNameRequired">
	<fmt:param><b><fmt:message key="maintenance.contact.full_name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtJobTitleRequired">
	<fmt:param><b><fmt:message key="maintenance.contact.job_title"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtFileAsRequired">
	<fmt:param><b><fmt:message key="maintenance.contact.file_as"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtEmailRequired">
	<fmt:param><b><fmt:message key="maintenance.contact.email"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtUserNameRequired">
	<fmt:param><b><fmt:message key="maintenance.contact.username"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtNotEqual">
	<fmt:param><b><fmt:message key="maintenance.contact.full_name"/> <fmt:message key="different"/><fmt:message key="pmo"/></b></fmt:param>
</fmt:message>
<script>
var performingorgValidator;

	<%-- ------------------- --%>
	<%-- Functions panel PO --%>
	<%-- ------------------- --%>
	<%-- Create role --%>
	var new_po = {
			
			show: function() {
				
				document.forms.frm_performingorg_create.option_panel_po.value = '<%= Constants.NEW %>';	
				
				$(".band.po:visible").hide();
				$('#new_po').show();
			}
	};
	
	<%-- Select role --%>
	var select_po = {
			
			show: function() {
				
				document.forms.frm_performingorg_create.option_panel_po.value = '<%= Constants.SELECT %>';
				
				$(".band.po:visible").hide();
				$('#select_po').show();
			}
	};

	<%-- ------------------- --%>
	<%-- Functions panel PMO --%>
	<%-- ------------------- --%>
	<%-- Create role --%>
	var new_pmo = {
			
			show: function() {
				
				document.forms.frm_performingorg_create.option_panel_pmo.value = '<%= Constants.NEW %>';	
				
				$(".band.pmo:visible").hide();
				$('#new_pmo').show();
			}
	};
	
	<%-- Select role --%>
	var select_pmo = {
			
			show: function() {
				
				document.forms.frm_performingorg_create.option_panel_pmo.value = '<%= Constants.SELECT %>';
				
				$(".band.pmo:visible").hide();
				$('#select_pmo').show();
			}
	};
	
	<%-- ------------------ --%>
	<%-- Functions panel FM --%>
	<%-- ------------------ --%>
	<%-- Create role --%>
	var new_fm = {
			
			show: function() {
				
				document.forms.frm_performingorg_create.option_panel_fm.value = '<%= Constants.NEW %>';	
				
				$(".band.fm:visible").hide();
				$('#new_fm').show();
			}
	};
	
	<%-- Select role --%>
	var select_fm = {
			
			show: function() {
				
				document.forms.frm_performingorg_create.option_panel_fm.value = '<%= Constants.SELECT %>';
				
				$(".band.fm:visible").hide();
				$('#select_fm').show();
			}
	};
	
	<%-- Copy role --%>
	var copy_fm = {
			
			show: function() {
				
				document.forms.frm_performingorg_create.option_panel_fm.value = '<%= Constants.COPY %>';
				
				$(".band.fm:visible").hide();
			}
	};
	

readyMethods.add(function() {

	<%-- ----------- --%>
	<%-- Validations --%>
	<%-- ----------- --%>
	$.validator.addMethod("notEqualTo", 
		function(value, element, param) {
		   	return value != $(param).val();
		});
	
	performingorgValidator = $("#frm_performingorg_create").validate({
		errorContainer: $('div#performingorg-errors'),
		errorLabelContainer: 'div#performingorg-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#performingorg-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			//PO
			name: { required: true },
			//PMO
			idContact_name_pmo: { required: true }, 
			full_name_pmo: { required: true },
		    job_title_pmo: { required: true },
			file_as_pmo: { required: true },
			email_pmo: { required: true, email: true },
			login_pmo: { required: true },
			contact_notes_pmo: { maxlength: 200 },
			//FM
			idContact_name_fm: { required: true }, 
			full_name_fm: { required: true },
		    job_title_fm: { required: true },
			file_as_fm: { required: true },
			email_fm: { required: true, email: true },
			login_fm: { required: true , notEqualTo: "#login_name_pmo"},
			contact_notes_fm: { maxlength: 200 }
		},
		messages:{
			//PO
			name: { required: '${fmtPerformingOrgNameRequired}' },
			//PMO
			idContact_name_pmo: { required: '${fmtFullNameRequired}' },
			full_name_pmo: { required: '${fmtFullNameRequired}' },
		    job_title_pmo: { required: '${fmtJobTitleRequired}' },
			file_as_pmo: { required: '${fmtFileAsRequired}' },
			email_pmo: { required: '${fmtEmailRequired}', email: '<fmt:message key="maintenance.error_email"/>' },
			login_pmo: { required: '${fmtUserNameRequired}' },
			//FM
			idContact_name_fm: { required: '${fmtFullNameRequired}' },
			name_fm: { required: '${fmtPerformingOrgNameRequired}' },
			full_name_fm: { required: '${fmtFullNameRequired}' },
		    job_title_fm: { required: '${fmtJobTitleRequired}' },
			file_as_fm: { required: '${fmtFileAsRequired}' },
			email_fm: { required: '${fmtEmailRequired}', email: '<fmt:message key="maintenance.error_email"/>' },
			login_fm: { required: '${fmtUserNameRequired}', notEqualTo: '${fmtNotEqual}'}
		}
	});
	
	<%-- New check box in FM --%>
	var newRadioInputFM = '<input type="radio" onclick="copy_fm.show();" style="width:15px;" value="select" name="options_create_fm">';
	$("#selection_fm fieldset").append('<fmt:message key="copy_contact"/>:&nbsp;' + newRadioInputFM);
	
	<%-- -------------- --%>
	<%-- Initialization --%>
	<%-- -------------- --%>
	
	<%-- Panel PO --%>
	document.forms.frm_performingorg_create.options_create_po[0].checked = "true";
	new_po.show();
	
	<%-- Panel PMO --%>
	document.forms.frm_performingorg_create.options_create_pmo[0].checked = "true";
	new_pmo.show();
	
	<%-- Panel FM --%>
	document.forms.frm_performingorg_create.options_create_fm[2].checked = "true";
	
	<%-- ------ --%>
	<%-- Events --%>
	<%-- ------ --%>
	
	<%-- Panel PO --%>
	$("#selection_po :radio:eq(0)").on('change', function() {
		new_po.show();
	});
	
	$("#selection_po :radio:eq(1)").on('change', function() {
		select_po.show();
	});
	
	<%-- Panel PMO --%>
	$("#selection_pmo :radio:eq(0)").on('change', function() {
		new_pmo.show();
	});
	
	$("#selection_pmo :radio:eq(1)").on('change', function() {
		select_pmo.show();
	});
	
	<%-- Panel FM --%>
	$("#selection_fm :radio:eq(0)").on('change', function() {
		new_fm.show();
	});
	
	$("#selection_fm :radio:eq(1)").on('change', function() {
		select_fm.show();
	});
	
	$("#selection_fm :radio:eq(2)").on('change', function() {
		copy_fm.show();
	});
	
	<%-- Button --%>
	$('#createPO').on('click', function() {

		var f = document.forms.frm_performingorg_create;	
		
		<%-- Validate login in use for pmo and fm --%>
		if (performingorgValidator.form()) {
		
			var params;
			
			if (f.options_create_pmo[0].checked) {
				
				params = {
					accion: '<%=AdministrationServlet.JX_USERNAME_IN_USE%>',
					loginPMO: $('#frm_performingorg_create #login_name_pmo').val()
				};
			}
			
			if (f.options_create_fm[0].checked) {
				
				params = {
					accion: '<%=AdministrationServlet.JX_USERNAME_IN_USE%>',
					loginFM: $('#frm_performingorg_create #login_name_fm').val()
				};
			}
			
			if (f.options_create_pmo[0].checked && f.options_create_fm[0].checked) {
				
				params = {
					accion: '<%=AdministrationServlet.JX_USERNAME_IN_USE%>',
					loginPMO: $('#frm_performingorg_create #login_name_pmo').val(),
					loginFM: $('#frm_performingorg_create #login_name_fm').val()
				};
			}
			
			if (typeof params !== 'undefined') {
				
				adminAjax.call(params,function() {
					
					loadingPopup();
					
					<%-- Submit --%>
					document.forms.frm_performingorg_create.submit();
				});
			}
			else {
				<%-- Submit --%>
				document.forms.frm_performingorg_create.submit();
			}
		}
	});
});
</script>
<form name="frm_performingorg_create" id="frm_performingorg_create" action="<%= AdministrationServlet.REFERENCE %>" method="post">
	<input type="hidden" name="accion" value="<%= AdministrationServlet.CREATE_PO %>" />
	<%-- Options --%>
	<input type="hidden" name="option_panel_po" value="new" />
	<input type="hidden" name="option_panel_pmo" value="new" />
	<input type="hidden" name="option_panel_fm" value="copy" />
	
	<%-- Errors --%>
	<div id="performingorg-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="performingorg-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	
	<%-- PO --%>
	<fmt:message key="maintenance.perfoming_org.manage" var="titleOrg"/>
	<visual:panel title="${titleOrg}">
	
		<div id="selection_po" style="text-align: center; display:block;">
			<fieldset>
				<legend><fmt:message key="wbs.selection"/></legend>
				<fmt:message key="new_po"/>:&nbsp;<input type="radio" name="options_create_po" value="create" style="margin-right: 10px; width: 15px;" /> 
				<fmt:message key="search_po"/>:&nbsp;<input type="radio" name="options_create_po" value="select" style="width:15px;" />
			</fieldset>
		</div>

		<div id="new_po" class="band po" style="display:none">
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="maintenance.perfoming_org.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="text" name="name" id="performingorg_name" class="campo" maxlength="45"/>
					</td>
				</tr>
	   		</table>
   		</div>
   		
   		<div id="select_po" class="band po" style="display:none">
	   		<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th width="32%"><fmt:message key="maintenance.employee.perf_org"/></th>
				</tr>
				<tr>
					<td>
						<select class="campo" name="search_perforg" id="search_perforg">
							<option value="-1"><fmt:message key="select_opt" /></option>
							<c:forEach var="perfOrg" items="${perforgs}">
								<option value="${perfOrg.idPerfOrg}">${perfOrg.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
		</div>
   	</visual:panel>
  	
   	<%-- PMO --%>
  	<fmt:message key="pmo" var="titlePMO"/>
	<visual:panel title="${titlePMO }">
		<jsp:include page="create_role.jsp" flush="true" >
			<jsp:param value="pmo" name="role"/>
		</jsp:include>
	</visual:panel>
	
	<%-- FM --%>
	<fmt:message key="functional_manager" var="titleFM"/>
	<visual:panel title="${titleFM }">
		<jsp:include page="create_role.jsp" flush="true" >
			<jsp:param value="fm" name="role"/>
		</jsp:include>
	</visual:panel>
	
	<%-- Buttons --%>
	<div align="right" style="margin:10px;">
		<input type="button" class="boton" id="createPO" value="<fmt:message key="create" />"/>
	</div>
</form>

<%-- Includes --%>
<jsp:include page="../common/search_contact_popup.jsp" flush="true" />