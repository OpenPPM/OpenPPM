<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
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
  ~ File: mant_contact.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:07
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.contact.new" var="new_contact_title" />
<fmt:message key="maintenance.contact.edit" var="edit_contact_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_contact">
	<fmt:param><fmt:message key="maintenance.contact"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_contact">
	<fmt:param><fmt:message key="maintenance.contact"/></fmt:param>
</fmt:message>
<fmt:message key="msg.confirm_reset_password" var="msg_confirm_reset_password"/>
<fmt:message key="msg.confirm_title_reset_password" var="msg_title_confirm_reset_password"/>

<%-- Message for validations --%>
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
<fmt:message key="msg.error.not_especial_characters" var="fmtNotEspecialCharacters"/>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<script language="javascript" type="text/javascript" >

var contactsTable;
var contactValidator;

var contact = {
	enable : function(element) {
		var $tr = $(element).parents('tr');
		var item = contactsTable.fnGetData( $tr[0] );
		
		var params = {
			accion: "<%=MaintenanceServlet.JX_ENABLE_CONTACT %>",
			idContact: item[0]
		};
			
		mainAjax.call(params, function (data) {
			item[10] = 'false';
			contactsTable.fnUpdate(item, $tr[0]);
		});
	},
	disable : function(element) {
		var $tr = $(element).parents('tr');
		var item = contactsTable.fnGetData( $tr[0] );

		var params = {
			accion: "<%=MaintenanceServlet.JX_DISABLE_CONTACT %>",
			idContact: item[0]
		};
		
		mainAjax.call(params, function (data) {
			item[10] = 'true';
			contactsTable.fnUpdate(item, $tr[0]);
		});
	},
	add: function() {
		$('#login_name').attr('disabled',false);
		$('#reset_password').hide();
		var f = document.frm_contact_pop;
		f.reset();
		f.id.value = "";
		$(f.contact_notes).text('');
		
		<%if (!SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_BBDD)
				&& !SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL)) {%>
			$("#ldapUsers").show();
		<%}%>
		
		$('#legendContact').html('${new_contact_title}');
		$('#contacts-popup').dialog('open');
	},
	edit: function(element) {
		<% if (!SecurityUtil.isUserInRole(request, Constants.ROLE_PMO)) {%>
		$('#login_name').attr('disabled',true);
		<%}%>
		$('#reset_password').show();
		
		<%if (!SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_BBDD)
				&& !SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL)) {%>
		$("#ldapUsers").hide();
		<%}%>
	
		var item = contactsTable.fnGetData( element.parentNode.parentNode.parentNode );
		
		var f = document.forms["frm_contact_pop"];
		f.contact_id.value 					= item[0];
		f.contact_job_title.value 			= unEscape(item[2]);
		f.contact_file_as.value 			= unEscape(item[3]);
		f.contact_business_phone.value 		= unEscape(item[4]);
		f.contact_mobile_phone.value 		= unEscape(item[5]);
		f.contact_business_address.value 	= unEscape(item[6]);
		f.contact_full_name.value 			= unEscape(item[1]);
		f.contact_email.value 				= unEscape(item[7]);
		
		$('#contact_notes').text(unEscape(item[8]));
		f.login.value 						= "";
		
		var params = {
			accion: "<%=MaintenanceServlet.JX_GET_USERNAME %>",
			idContact: f.contact_id.value	
		};
		
		mainAjax.call(params, function (data) {
			f.login.value = data.login;
			<%if (SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_LDAP)
					|| SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL_SEARCH_LDAP)) {%>
				$("#login_name_info").val(data.login);
			<%}%>
		});
		
		$('#legendContact').html('${edit_contact_title}');
		$('#contacts-popup').dialog('open');
	},
	del: function(element) {
		confirmUI(
			'${msg_title_confirm_delete_contact}','${msg_confirm_delete_contact}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var contact = contactsTable.fnGetData( element.parentNode.parentNode.parentNode );
				
				var f = document.frm_contacts;

				f.accion.value = "<%=MaintenanceServlet.DEL_CONTACT %>";
				f.id.value = contact[0];
				f.idManten.value = $('select#idManten').val();
				
				loadingPopup();
				f.submit();
		});
	},
	resetPassword: function () {
		
		confirmUI(
			'${msg_title_confirm_reset_password}','${msg_confirm_reset_password}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var f = document.frm_contact_pop;
				
				var params={
					accion: "<%=MaintenanceServlet.JX_RESET_PASSWORD %>",
					idContact: f.contact_id.value	
				};
				
				mainAjax.call(params, function (data) {
					$('#contacts-popup').dialog('close');
				});
		});
	},
	save: function() {
		var f = document.frm_contact_pop;
		
		if (contactValidator.form()) {
			
			var params = {
				accion: "<%=MaintenanceServlet.JX_SAVE_CONTACT %>",
				id: f.contact_id.value,
				full_name : f.contact_full_name.value,
				job_title : f.contact_job_title.value,
				file_as : f.contact_file_as.value,
				business_phone: f.contact_business_phone.value,
				mobile_phone : f.contact_mobile_phone.value,
				business_address : f.contact_business_address.value,
				email : f.contact_email.value,
				notes : f.contact_notes.value,
				login: f.login.value
			};
			
			mainAjax.call(params, function (data) {
				var dataRow = [
					data.id,
					escape(data.full_name),
					escape(data.job_title),
					escape(data.file_as),
					escape(data.business_phone),
					escape(data.mobile_phone),
					escape(data.business_address),
					escape(data.email),
					escape(data.notes),
					'',
					data.disable
				];
				
				if (f.id.value == data.id) { contactsTable.fnUpdateAndSelect(dataRow); }
				else { contactsTable.fnAddDataAndSelect(dataRow); }
				
				f.reset();
				$('#contacts-popup').dialog('close');
			});	
			
		}
	},
	buttons : function(disable) {
		var $buttons = $('<nobr/>');
		$buttons.append($('<img/>', {onclick: 'contact.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
				.append('&nbsp;&nbsp;&nbsp;')
				.append($('<img/>', {onclick: 'contact.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
				.append('&nbsp;&nbsp;&nbsp;');

		if (disable == 'true') {
			$buttons.append($('<img/>', {onclick: 'contact.enable(this)', title: '<fmt:message key="enable"/>', 'class': 'link', src: 'images/lock.png'}));
		}
		else {
			$buttons.append($('<img/>', {onclick: 'contact.disable(this)', title: '<fmt:message key="disable"/>', 'class': 'link', src: 'images/lock_open.png'}));
		}
		return $buttons;
	}
};

readyMethods.add(function () {

	contactsTable = $('#tb_contacts').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"iDisplayLength": 50,
		"aaSorting": [[ 0, "asc" ]],
		"bAutoWidth": false,
		"fnRowCallback": function( nRow, aData) {
			$('td:eq(5)', nRow).html( contact.buttons(aData[10]) );
			return nRow;
		},
		"aoColumns": [ 
			{ "bVisible" : false },
            { "sClass": "left" },
            { "sClass": "left" },
            { "bVisible": false },
			{ "sClass": "right" },
            { "sClass": "right" },
            { "bVisible": false },
            { "sClass": "center" },
            { "bVisible": false },
            { "sClass": "center", "bSortable" : false },
            { "bVisible": false }
   		]	
	});

	$('#tb_contacts tbody tr').live('click',  function (event) {
		contactsTable.fnSetSelectable(this,'selected_internal');
	} );

	$('div#contacts-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 950,
		resizable: false,
		open: function(event, ui) { contactValidator.resetForm(); }
	});

	$.validator.addMethod("regexp", 
			function(value, element) { 
		  		return this.optional(element) || /^([a-zA-Z0-9_.]*)$/.test(value); 
			}, 
	"${fmtNotEspecialCharacters}");
	
	
	contactValidator = $("#frm_contact_pop").validate({
		errorContainer: 'div#contact-errors',
		errorLabelContainer: 'div#contact-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#contact-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			full_name: { required: true },
		    job_title: { required: true },
			file_as: { required: true },
			email: { required: true, email: true },
			login: { required: true , regexp:true},
			contact_notes: { maxlength: 200 }
		},
		messages: {
			full_name: { required: '${fmtFullNameRequired}' },
		    job_title: { required: '${fmtJobTitleRequired}' },
			file_as: { required: '${fmtFileAsRequired}' },
			email: { required: '${fmtEmailRequired}', email: '<fmt:message key="maintenance.error_email"/>' },
			login: { required: '${fmtUserNameRequired}' }
		}
	});

	$('#reset_password').click(contact.resetPassword);
	$('#frm_contact_pop').submit(function() {
		contact.save();
		return false;
	});
	$('#showDisable').on('change', function() {
		if ($(this).prop('checked')) { contactsTable.fnFilter( '', 10); }
		else { contactsTable.fnFilter( 'false', 10); }
	});

	$('#showDisable').trigger('change');
});
</script>

<c:if test="<%=!SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_BBDD) && !SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL)%>">
<script language="javascript" type="text/javascript" >

var usersLdapTable;
var params = $("#ldapUsers").serializeArray();
var ldap = {
	find : function() {
		
		params = $("#ldapUsers").serializeArray();
		params.push({name:"accion", value:"<%=MaintenanceServlet.JX_LDAP_SEARCH %>"});

		usersLdapTable.fnClearTable();
		mainAjax.call(params, function (data) {
			
			$(data).each(function(){
				
				var user = [
					this.name+' '+this.lastname,
					this.username,
					this.email
				];
				usersLdapTable.fnAddData(user);
			});
		});
	},
	load : function() {
		
		var selected = usersLdapTable.fnGetSelectedsData();
		var f = document.forms["frm_contact_pop"];
		
		f.contact_full_name.value 	= selected[0][0];
		f.contact_email.value 		= selected[0][2];
		f.login.value				= selected[0][1];
		
		<%if (SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_LDAP)
				|| SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL_SEARCH_LDAP)) {%>
			$("#login_name_info").val(selected[0][1]);
		<%}%>
	}
};
readyMethods.add(function () {

	$("#findLdap").click(ldap.find);
	
	usersLdapTable = $('#tb_ldapusers').dataTable({
		"sDom": 'T<"clear">lfrtip',
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"iDisplayLength": 10,
		"aaSorting": [[ 0, "asc" ]],
		"bFilter": false,
		"oTableTools": {
			"sRowSelect": "single", "aButtons": [],
			"fnRowSelected": ldap.load
		},
		"bAutoWidth": false,
		"aoColumns": [ 
            { "sClass": "left" },
            { "sClass": "left" },
			{ "sClass": "left" }
   		]	
	});
});

</script>
</c:if>
<form id="frm_contacts" name="frm_contacts" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	<fieldset>
		<legend><fmt:message key="maintenance.contact" /></legend>
		<div class="title" style="padding:10px;">
			<fmt:message key="show"/>&nbsp;<fmt:message key="disabled"/>&nbsp;<input type="checkbox" id="showDisable" style="width: auto;"/>
		</div>
		<table id="tb_contacts" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
				 <th width="0%">&nbsp;</th>
				 <th width="20%"><fmt:message key="maintenance.contact.full_name" /></th>
				 <th width="25%"><fmt:message key="maintenance.contact.job_title" /></th>
				 <th width="0%"><fmt:message key="maintenance.contact.file_as" /></th>
				 <th width="12%"><fmt:message key="maintenance.contact.business_phone" /></th>
				 <th width="10%"><fmt:message key="maintenance.contact.mobile_phone" /></th>
				 <th width="0%"><fmt:message key="maintenance.contact.business_address" /></th>
				 <th width="25%"><fmt:message key="maintenance.contact.email" /></th>
				 <th width="0%"><fmt:message key="maintenance.contact.notes" /></th>
				 <th width="8%">
				 	<img onclick="contact.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
				 <th>&nbsp;</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="contact" items="${list}">
					<tr>
						<td>${contact.idContact}</td>
						<td>${tl:escape(contact.fullName)}</td>
						<td>${tl:escape(contact.jobTitle)}</td>
						<td>${tl:escape(contact.fileAs)}</td>
						<td>${tl:escape(contact.businessPhone)}</td>
						<td>${tl:escape(contact.mobilePhone)}</td>
						<td>${tl:escape(contact.businessAddress)}</td>
						<td>${tl:escape(contact.email)}</td>
						<td>${tl:escape(contact.notes)}</td>
						<td></td>
						<td>${tl:defBoolean(contact.disable)}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
</form>

<div id="contacts-popup">
	<div id="contact-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="contact-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_contact_pop" id="frm_contact_pop">
		<input type="hidden" name="id" id="contact_id"/>
		<fieldset>
			<legend id="legendContact"><fmt:message key="maintenance.contact.new"/></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					 <th><fmt:message key="maintenance.contact.full_name" />&nbsp;*</th>
					 <th><fmt:message key="maintenance.contact.file_as" />&nbsp;*</th>
					 <th><fmt:message key="maintenance.contact.job_title" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="full_name" id="contact_full_name" class="campo" maxlength="50"/></td>
					<td><input type="text" name="file_as" id="contact_file_as" class="campo" maxlength="60"/></td>
					<td><input type="text" name="job_title" id="contact_job_title" class="campo" maxlength="50"/></td>
				</tr>
				<tr>
					 <th colspan="2"><fmt:message key="maintenance.contact.business_address" /></th>
					 <th><fmt:message key="maintenance.contact.business_phone" /></th>
				</tr>
				<tr>
					<td colspan="2">
						<input type="text" name="business_address" id="contact_business_address" class="campo" maxlength="200"/>
					</td>
					<td>
						<input type="text" name="business_phone" id="contact_business_phone" class="campo" maxlength="12"/>
					</td>
				</tr>
				<tr>
					 <th><fmt:message key="maintenance.contact.username" />&nbsp;*</th>
					 <th><fmt:message key="maintenance.contact.mobile_phone" /></th>
					 <th><fmt:message key="maintenance.contact.email" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<c:choose>
							<c:when test="<%=SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_LDAP) 
								|| SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL_SEARCH_LDAP)%>">
								<input type="text" class="campo" id="login_name_info" disabled/>
								<input type="hidden" name="login" id="login_name" maxlength="20"/>
							</c:when>
							<c:otherwise>
								<input type="text" class="campo" name="login" id="login_name" maxlength="20"/>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
						<input type="text" name="mobile_phone" id="contact_mobile_phone" class="campo" maxlength="12"/>
					</td>
					<td>
						<input type="text" name="email" id="contact_email" class="campo" maxlength="50"/>
					</td>
				</tr>
				<tr>
					 <th colspan="3"><fmt:message key="maintenance.contact.notes" /></th>
				</tr>			
				<tr>
					<td colspan="3">
						<textarea name="notes" id="contact_notes" name="contact_notes" class="campo" style="width:98%;"></textarea>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<c:if test="<%=!SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_BBDD) && !SettingUtil.getString(settings, Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE).equals(Constants.SECURITY_LOGIN_EXTERNAL)%>">
	    	<fieldset id="ldapUsers">
				<legend><fmt:message key="find.ldap.user"/></legend>
				<table style="width:100%">
					<tr>
						<th width="20%"><fmt:message key="labels.name" /></th>
						<th width="22%"><fmt:message key="contact.surname" /></th>
						<th width="25%"><fmt:message key="maintenance.contact.username" /></th>
						<th width="25%"><fmt:message key="maintenance.contact.email" /></th>
						<th width="8%">&nbsp;</th>
					</tr>
					<tr>
						<td><input type="text" name="nameFind" class="campo"/></td>
						<td><input type="text" name="lastNameFind" class="campo"/></td>
						<td><input type="text" name="userNameFind" class="campo"/></td>
						<td><input type="text" name="emailFind" class="campo"/></td>
						<td><input type="button" id="findLdap" class="boton" value="<fmt:message key="find" />" /></td>
					</tr>
				</table>
				<table id="tb_ldapusers" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
						 <th width="34%"><fmt:message key="maintenance.contact.full_name" /></th>
						 <th width="33%"><fmt:message key="maintenance.contact.username" /></th>
						 <th width="33%"><fmt:message key="maintenance.contact.email" /></th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</fieldset>
		</c:if>
    	<div class="popButtons">
    		<c:if test="<%=Constants.SECURITY_LOGIN_BBDD.equals(SettingUtil.getString(SettingUtil.getSettings(request), Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE))
				|| Constants.SECURITY_LOGIN_MIXED.equals(SettingUtil.getString(SettingUtil.getSettings(request), Settings.SETTING_LOGIN_TYPE, Settings.DEFAULT_LOGIN_TYPE)) %>">
    			<input type="button" id="reset_password" class="boton" value="<fmt:message key="maintenance.employee.reset_password" />" />
    		</c:if>
    		<input type="submit" class="boton" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

