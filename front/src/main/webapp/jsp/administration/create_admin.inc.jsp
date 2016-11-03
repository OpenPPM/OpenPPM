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
<%@taglib uri="Utilidades" prefix="tl" %>

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
  ~ File: create_admin.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:26
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

<script>

var newAdminValidator;

<%-- Functions panel admin --%>
<%-- ------------------------- --%>
	<%-- Create role --%>
	var new_admin = {
			
			show: function() {
				$(".band.admin:visible").hide();
				$('#new_admin').show();
			}
	};
	
	<%-- Select role --%>
	var select_admin = {
			
			show: function() {
				$(".band.admin:visible").hide();
				$('#select_admin').show();
			}
	};		

readyMethods.add(function() {

	<%-- initialization--%>
	document.forms.frm_create_admin.reset();
	
	<%-- Validations --%>
	newAdminValidator = $("#frm_create_admin").validate({
		errorContainer: $('div#createadmin-errors'),
		errorLabelContainer: 'div#createadmin-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#createadmin-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			idContact_name_admin: { required: true },
			full_name_admin: { required: true },
		    job_title_admin: { required: true },
			file_as_admin: { required: true },
			email_admin: { required: true, email: true },
			login_admin: { required: true },
			contact_notes_admin: { maxlength: 200 }
		},
		messages:{
			idContact_name_admin: { required: '${fmtFullNameRequired}' },
			full_name_admin: { required: '${fmtFullNameRequired}' },
		    job_title_admin: { required: '${fmtJobTitleRequired}' },
			file_as_admin: { required: '${fmtFileAsRequired}' },
			email_admin: { required: '${fmtEmailRequired}', email: '<fmt:message key="maintenance.error_email"/>' },
			login_admin: { required: '${fmtUserNameRequired}' }
		}
	});
	
	<%-- -------------- --%>
	<%-- Initialization --%>
	<%-- -------------- --%>
	<%-- Panel PMO --%>
	document.forms.frm_create_admin.options_create_admin[0].checked = "true";
	new_admin.show();
	
	<%-- ------ --%>
	<%-- Events --%>
	<%-- ------ --%>
	
	<%-- Panel admin --%>
	$("#selection_admin :radio:eq(0)").on('change', function() {
		new_admin.show();
	});
	
	$("#selection_admin :radio:eq(1)").on('change', function() {
		select_admin.show();
	});
	
	<%-- Button --%>
	$('#adminButton').on('click', function(e){
		
		var f = document.forms.frm_create_admin;
		
		if (f.options_create_admin[0].checked && newAdminValidator.form()) {
			
			var params = {
				accion: '<%=AdministrationServlet.JX_USERNAME_IN_USE%>',
				loginAdmin: $('#frm_create_admin #login_name_admin').val()
			};
			
			adminAjax.call(params,function() {
				
				loadingPopup();
				
				f.idContact_admin.value = "";
				f.submit();
			});
			
		}
		else if (f.options_create_admin[1].checked && newAdminValidator.form()) {
			f.submit();
		}
	});
	
});

</script>
	
<form name="frm_create_admin" id="frm_create_admin" action="<%= AdministrationServlet.REFERENCE %>" method="post">
	<input type="hidden" name="accion" value="<%=AdministrationServlet.CREATE_ADMIN %>" />

	<%-- Errors --%>
	<div id="createadmin-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="createadmin-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	
	<%-- Panel --%>
	<fmt:message key="settings.create_admin" var="titleAdmin"/>
	<c:set var="refreshAdminBTN">
		<img id="refreshAdmin" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
	</c:set>
	<visual:panel title="${titleAdmin}" buttons="${refreshAdminBTN}">
		<jsp:include page="create_role.jsp" flush="true" >
			<jsp:param value="admin" name="role"/>
		</jsp:include>
	</visual:panel>
	  	
	<%-- Space --%>
	<div style="width: 100px; display:block">&nbsp;</div>
	
	<%-- Buttons --%>
	<div id="buttonsAdmin" align="right" style="margin:10px;">
		<input type="button" class="boton" id="adminButton" value="<fmt:message key="create" />"/>
	</div>
</form>