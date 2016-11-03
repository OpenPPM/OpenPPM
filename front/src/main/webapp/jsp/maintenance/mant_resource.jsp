<%@page import="es.sm2.openppm.front.servlets.CalendarServlet"%>
<%@page import="java.util.Calendar"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.core.model.impl.Resourcepool"%>

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
  ~ File: mant_resource.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:07
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.employee.new" var="new_employee_title" />
<fmt:message key="maintenance.employee.edit" var="edit_employee_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_employee">
	<fmt:param><fmt:message key="maintenance.employee"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_employee">
	<fmt:param><fmt:message key="maintenance.employee"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_select_a" var="fmtContactRequired">
	<fmt:param><b><fmt:message key="maintenance.employee.contact"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_select_a" var="fmtManagerRequired">
	<fmt:param><b><fmt:message key="maintenance.employee.resource_manager"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_select_a" var="fmtProfileRequired">
	<fmt:param><b><fmt:message key="maintenance.employee.profile"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtCostRateRequired">
	<fmt:param><b><fmt:message key="maintenance.employee.cost_rate"/></b></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: Program list
	listResProfiles: List of Resources Profiles
	listPerfOrgs: List of performing Organization
	listResManagers: List of Resources Manager
	listContacts: List of contacts
	listSkills: List of skills
	listJobCategories: List of job categories
	listSellers: List of sellers
--%>

<script language="javascript" type="text/javascript" >

var employeesTable;
var employeeValidator;

var employee = {
	enable : function(element) {
		var $tr = $(element).parents('tr');
		var item = employeesTable.fnGetData( $tr[0] );
		
		var params = {
			accion: "<%=MaintenanceServlet.JX_ENABLE_EMPLOYEE %>",
			idEmployee: item[0]
		};
			
		mainAjax.call(params, function (data) {
			item[13] = 'false';
			employeesTable.fnUpdate(item, $tr[0]);
		});
	},
	disable : function(element) {
		var $tr = $(element).parents('tr');
		var item = employeesTable.fnGetData( $tr[0] );

		var params = {
			accion: "<%=MaintenanceServlet.JX_DISABLE_EMPLOYEE %>",
			idEmployee: item[0]
		};
		
		mainAjax.call(params, function (data) {
			item[13] = 'true';
			employeesTable.fnUpdate(item, $tr[0]);
		});
	},
	edit : function (element) {
		
		var employee = employeesTable.fnGetData(element.parentNode.parentNode.parentNode);
		
		var params = {
			accion: "<%=MaintenanceServlet.JX_CONS_SKILLS_AND_JOB_CATEGORIES %>",
			<%=Employee.IDEMPLOYEE %>: employee[0]	
		};		
		
		mainAjax.call(params, function(data) {
			
			var f = document.forms["frm_employee_pop"];
			f.reset();
			
			f.employee_id.value 	= employee[0];
			f.idContact_name.value 	= employee[1];
			f.idContact.value 		= employee[9];
			f.idManager.value 		= employee[10];
			f.idProfile.value 		= employee[11];	
			f.cost_rate.value 		= employee[6];
			f.idCalendarBase.value	= employee[12];
			f.idSeller.value		= employee[14];

			$("#skills").val('');
				
			var skills = data.skills;
			
			if(skills.length > 0) {
				for (var i = 0; i < skills.length; i++) {					
					$("#skills option[value='" + skills[i] + "']").attr("selected", true);
				}
			}
			
			$("#jobCategories").val('');
			
			var jobCategories = data.jobCategories;
			
			if(jobCategories.length > 0) {
				for (var i = 0; i < jobCategories.length; i++) {					
					$("#jobCategories option[value='" + jobCategories[i] + "']").attr("selected", true);
				}
			}
			
			f.idProfile.disabled = true;
			f.idContact_name.disabled = true;
			f.isUpdate.value = "true";
			$('.button_img').hide();		
			
			resourceOpt();
			
			// Display followup info
			$('#employees-popup legend').html('${edit_employee_title}');
			$('#employees-popup').dialog('open');	
		});	
		
	},
	del : function (element) {
		
		confirmUI(
			'${msg_title_confirm_delete_employee}','${msg_confirm_delete_employee}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var employee = employeesTable.fnGetData( element.parentNode.parentNode.parentNode );
				
				var f = document.frm_employees;

				f.accion.value = "<%=MaintenanceServlet.DEL_EMPLOYEE %>";
				f.id.value = employee[0];
				f.idManten.value = $('select#idManten').val();
				
				loadingPopup();
				
				f.submit();
			}
		);
	},
	buttons : function(disable, profile) {
		var $buttons = $('<nobr/>');

		if (profile == <%=Constants.ROLE_RESOURCE %>) {
			$buttons.append($('<img/>', {onclick: 'employee.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
					.append('&nbsp;&nbsp;&nbsp;');
		}

		$buttons.append($('<img/>', {onclick: 'employee.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
				.append('&nbsp;&nbsp;&nbsp;');

		if (disable == 'true') {
			$buttons.append($('<img/>', {onclick: 'employee.enable(this)', title: '<fmt:message key="enable"/>', 'class': 'link', src: 'images/lock.png'}));
		}
		else {
			$buttons.append($('<img/>', {onclick: 'employee.disable(this)', title: '<fmt:message key="disable"/>', 'class': 'link', src: 'images/lock_open.png'}));
		}
		return $buttons;
	}
};

function addEmployee() {
	var f = document.frm_employee_pop;
	f.reset();

	f.employee_id.value			= "";
	f.idContact.value			= "";
	f.idProfile.disabled		= false;
	f.idContact_name.disabled	= false;
	f.isUpdate.value			= "false";
	f.idCalendarBase.value		= "";
	
	$("#skills").val('');
	$("#jobCategories").val('');
	
	$('.button_img').show();
	
	resourceOpt();
	
	$('#employees-popup legend').html('${new_employee_title}');
	$('#employees-popup').dialog('open');
}

function saveEmployee() {
	if (employeeValidator.form()) {
		var f = document.frm_employee_pop;
		
		f.accion.value = "<%=MaintenanceServlet.SAVE_EMPLOYEE%>";
		f.idManten.value = "<%=Constants.MANT_RESOURCE%>";
		
		$('#employees-popup').dialog('close');
		
		f.idProfile.disabled = false;

		loadingPopup();
		f.submit();	
	}	
}

function resourceOpt() {
	
	if ($("#employee_profile").val() == <%=Constants.ROLE_RESOURCE%>) { $('.resource_opt').show(); }
	else { $('.resource_opt').hide(); }
}

function viewCalendar() {

	var f = document.forms.frm_calendar;
	f.calendarId.value = $('#idCalendarBase').val();
	loadingPopup();
	f.submit();
}

readyMethods.add(function () {

	employeesTable = $('#tb_employees').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"iDisplayLength": 50,
		"aaSorting": [[ 0, "asc" ]],
		"bAutoWidth": false,
		"fnRowCallback": function( nRow, aData) {
			$('td:eq(7)', nRow).html( employee.buttons(aData[13], aData[11]) );
			return nRow;
		},
		"aoColumns": [ 
			{ "bVisible": false },
            { "sClass": "left" },
            { "sClass": "left" },
            { "sClass": "left" },
            { "sClass": "left" },
            { "sClass": "left" },
            { "sClass": "right" },
			{ "sClass": "center", "sType": "es_date" },
            { "sClass": "center", "bSortable" : false },
			{ "bVisible": false },
			{ "bVisible": false },
			{ "bVisible": false },
			{ "bVisible": false },
			{ "bVisible": false },
			{ "bVisible": false }
   		]	
	});

	$('#tb_employees tbody tr').live('click',  function (event) {
		employeesTable.fnSetSelectable(this,'selected_internal');
	});

	$('div#employees-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 550,
		resizable: false,
		open: function(event, ui) { employeeValidator.resetForm(); }
	});

	employeeValidator = $("#frm_employee_pop").validate({
		errorContainer: $('div#employee-errors'),
		errorLabelContainer: 'div#employee-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#employee-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			idContact_name: {required: true },
			idManager: {required: function(){return $('#employee_profile').val() == <%=Constants.ROLE_RESOURCE%>;} },
			cost_rate: {required: function(){return $('#employee_profile').val() == <%=Constants.ROLE_RESOURCE%>;} },
			idProfile: {required: true }
		},
		messages: {
			idContact_name: {required: '${fmtContactRequired}'},
			idManager: {required: '${fmtManagerRequired}'},
			cost_rate: {required: '${fmtCostRateRequired}'},
			idProfile: {required: '${fmtProfileRequired}'}
		}
	});
	
	$('#employee_profile').change(function() { resourceOpt(); });
	$('#showDisable').on('change', function() {
		if ($(this).prop('checked')) { employeesTable.fnFilter( '', 13); }
		else { employeesTable.fnFilter( 'false', 13); }
	});

	$('#showDisable').trigger('change');
});
</script>

<%-- Load plugin --%>
<div id="ratingPMs"></div>

<form name="frm_calendar" method="post" action="<%=CalendarServlet.REFERENCE%>">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="calendarId" value="" />	
</form>

<form id="frm_employees" name="frm_employees" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />	
	
	<fieldset>
		<legend><fmt:message key="MANTEINANCE.EMPLOYEE_PROFILES" /></legend>
		<div class="title" style="padding:10px;">
			<fmt:message key="show"/>&nbsp;<fmt:message key="disable"/>&nbsp;<input type="checkbox" id="showDisable" style="width: auto;"/>
		</div>
		<table id="tb_employees" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="0%"></th>
					<th width="17%"><fmt:message key="maintenance.employee.contact" /></th>
					<th width="13%"><fmt:message key="maintenance.employee.resource_pool" /></th>
					<th width="17%"><fmt:message key="maintenance.employee.perf_org" /></th>
					<th width="13%"><fmt:message key="maintenance.employee.profile" /></th>
					<th width="13%"><fmt:message key="maintenance.employee.seller" /></th>
					<th width="8%"><fmt:message key="maintenance.employee.cost_rate" /></th>
					<th width="11%"><fmt:message key="maintenance.employee.profile_date" /></th>
					<th width="8%">
				 		<img onclick="addEmployee()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 	</th>
					<th width="0%"></th>
					<th width="0%"></th>
					<th width="0%"></th>
					<th width="0%"></th>
					<th width="0%"></th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="employee" items="${list}">
					<tr>
						<td>${employee.idEmployee}</td>
						<td>${tl:escape(employee.contact.fullName)}</td>
						<td>${tl:escape(employee.resourcepool.name)}</td>
						<td>${tl:escape(employee.performingorg.name)}</td>
						<td>${tl:escape(employee.resourceprofiles.profileName)}</td>
						<td>${tl:escape(employee.seller.name)}</td>
						<td>${tl:toCurrency(employee.costRate)}</td>
						<td><fmt:formatDate value="${employee.profileDate}" pattern="${datePattern}" /></td>
						<td></td>
						<td>${employee.contact.idContact}</td>
						<td>${employee.resourcepool.idResourcepool}</td><%-- id resourcepool --%>
						<td>${employee.resourceprofiles.idProfile}</td>
						<td>${employee.calendarbase.idCalendarBase}</td>
						<td>${tl:defBoolean(employee.disable)}</td>
						<td>${employee.seller.idSeller}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
</form>


<div id="employees-popup">

	<div id="employee-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="employee-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_employee_pop" id="frm_employee_pop" method="post">
		<input type="hidden" name="employee_id" value="" />
		<input type="hidden" id="isUpdate" name="isUpdate" />
		<input type="hidden" name="idManten" value="" />
		<input type="hidden" name="accion" />
		
		<fieldset>
			<legend><fmt:message key="maintenance.employee.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th width="50%">
						<fmt:message key="maintenance.employee.contact" />&nbsp;*
						<a class="button_img" href="javascript:searchContactPop('idContact');" >
							<span class="normal"><fmt:message key="search"/></span>
							<img style="width:16px;" src="images/search.png"/>
						</a>
					</th>
					<th width="50%"><fmt:message key="maintenance.employee.profile" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" id="idContact" name="idContact" />
						<input type="text" id="idContact_name" name="idContact_name" class="campo" readonly="readonly" />
					</td>
					<td>
						<select class="campo" name="idProfile" id="employee_profile">
							<option value=""><fmt:message key="select_opt" /></option>
							<c:forEach var="resProfile" items="${profiles}">
								<c:if test="${resProfile.idProfile ne 10 }">
									<option value="${resProfile.idProfile}">${resProfile.profileName}</option>
								</c:if>
							</c:forEach>
						</select>
					</td>					
				</tr>				
				<tr class="resource_opt"><td>&nbsp;</td></tr>
				<tr class="resource_opt">
					<th><fmt:message key="maintenance.employee.resource_pool" />&nbsp;*</th>								
					<th><fmt:message key="maintenance.employee.seller" />&nbsp;</th>
				</tr>
				<tr class="resource_opt">
					<td>
						<select class="campo" name="idManager" id="employee_manager">
							<option value=""><fmt:message key="select_opt" /></option>
							<c:forEach var="resourcePool" items="${listResourcePool}">
								<option value="${resourcePool.idResourcepool}">${resourcePool.name}</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<select class="campo" name="idSeller" id="idSeller">
							<option value=""><fmt:message key="select_opt" /></option>
							<c:forEach var="seller" items="${listSellers}">
								<option value="${seller.idSeller}">${seller.name}</option>
							</c:forEach>
						</select>
					</td>						
				</tr>
				<tr class="resource_opt">
					<th><fmt:message key="calendar.base" /></th>
					<th><fmt:message key="maintenance.employee.cost_rate" />&nbsp;*</th>
				</tr>
				<tr class="resource_opt">
					<td>
						<select id="idCalendarBase" name="idCalendarBase" class="campo" style="width: 73%; margin-right: 0px;">
							<option value=""><fmt:message key="select_opt"/></option>
							<c:forEach var="calendar" items="${calendars}">
								<option value="${calendar.idCalendarBase}">${tl:escape(calendar.name)}</option>
							</c:forEach>
						</select>
						<div style="float: right">
							<a href="javascript:viewCalendar();" class="boton"><fmt:message key="view"/></a>
						</div>
					</td>
					<td class="center">
						<input type="text" name="cost_rate" class="campo importe" id="employee_costRate" style="width: 60px;"/>
					</td>
				</tr>
				<tr class="resource_opt"><td>&nbsp;</td></tr>
				<tr class="resource_opt">
					<th colspan="1"><fmt:message key="maintenance.employee.skills" />&nbsp;</th>
					<th colspan="1"><fmt:message key="maintenance.job_category" />&nbsp;</th>
				</tr>
				<tr class="resource_opt">
					<td colspan="1">
						<select id="skills" name="skills" style="height: 72px; width: 98%" multiple="multiple" class="campo">
							<c:forEach var="skill" items="${listSkills}">
								<option value="${skill.idSkill}">${skill.name}</option>
							</c:forEach>
						</select>
					</td>
					<td colspan="1">
						<select id="jobCategories" name="jobCategories" style="height: 72px; width: 98%" multiple="multiple" class="campo">
							<c:forEach var="jobCategory" items="${listJobCategories}">
								<option value="${jobCategory.idJobCategory}">${jobCategory.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<a href="javascript:saveEmployee();" class="boton"><fmt:message key="save" /></a>
	   	</div>    	
    </form>
</div>
<jsp:include page="../common/search_contact_popup.jsp" flush="true" />