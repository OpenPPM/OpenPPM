<%@page import="es.sm2.openppm.utils.params.GetterUtil"%>
<%@page import="es.sm2.openppm.utils.StringUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Skill"%>
<%@page import="es.sm2.openppm.core.model.impl.Jobcategory"%>
<%@page import="es.sm2.openppm.core.model.impl.Teammember"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.core.model.impl.Performingorg"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@ page import="es.sm2.openppm.core.common.Configurations" %>
<%@ page import="es.sm2.openppm.front.utils.RequestUtil" %>

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
  ~ File: human_resources.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="data_not_found" var="data_not_found" />
<fmt:message key="loading_chart" var="loading_chart" />

<%-- Messages --%>
<fmt:message key="error" var="fmtError" />

<%-- Messages for Confirmation --%>
<fmt:message key="msg.confirm_release" var="fmtReleaseMsgMember">
	<fmt:param><fmt:message key="team_member"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_release" var="fmtReleaseTitleMember">
	<fmt:param><fmt:message key="team_member"/></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var teamTable;
var searchTable;
var findates;

function newTeamMember(element) {
	
	var f = document.frm_member;
	f.reset();
	
	var aData = searchTable.fnGetData( element.parentNode.parentNode );
	
	$("#act_planInitDate").text("");
	$("#act_planEndDate").text("");
	$('#tm_fullname').text(aData[1]);
	$('#jobcategory').html(aData[8]);
	$('#<%=Employee.COSTRATE%>').text(aData[5]);
	$('#<%=Contact.BUSINESSPHONE%>').text(aData[12]);
	$('#<%=Contact.MOBILEPHONE%>').text(aData[13]);
	$('#<%=Contact.EMAIL%>').text(aData[14]);
	$('#<%=Jobcategory.IDJOBCATEGORY%>').html(aData[15]);
	$("#commentsPM").text('');
	$('#sellRate').val(aData[5]);
	
	f.employee_id.value = aData[0];
	f.idmember.value	= '';

	$('#btnReleased, #commentsForRM, #btnUpdate').hide();
	$('#btnPreassigned').show();
	
	$('#idActivity').attr('disabled', false);
	$('#<%=Jobcategory.IDJOBCATEGORY%>').attr('disabled', false);
	$('#<%=Teammember.SELLRATE %>').attr('disabled', false);
	$('#member_fte').attr('disabled', false);
	
	chargeBTPop();
	$('#member_datein').datepicker('enable');
	showTeamMemberPopup();
}

function optionsJobs(data) {
	var options = '<option value=""><fmt:message key="select_opt"/></option>';
	
	$(data).each(function() {
		options += '<option value="'+this.<%=Jobcategory.IDJOBCATEGORY%>+'">'+escape(this.<%=Jobcategory.NAME%>)+'</option>';
	});
	
	return options;
}

function findTeamMembers() {
	searchTable.fnClearTable();
	planAjax.call({
			accion: "<%=ProjectPlanServlet.JX_FIND_MEMBER %>",
			<%=Jobcategory.IDJOBCATEGORY%>: $('#f_jobcategory').val()+"",
			<%=Performingorg.IDPERFORG %>: $('#<%=Performingorg.IDPERFORG %>_select').val(),
			idResourcePool: $('#idResourcePool_select').val(),
			<%= Skill.IDSKILL %>: $('#idSkill').val()+"",
			idSeller: $('#idSeller').val(),
		}, function(data) {

			for(var i=0; i< data.length; i++) {
				searchTable.fnAddData( [
						data[i].id,				
	            		escape(data[i].fullname),
	            		escape(data[i].profile),
	            		escape(data[i].resource_pool),
	            		'',
	            		toCurrency(data[i].cost_rate),
	            		escape(data[i].performing_org),
	            		data[i].contact_id,
	            		(data[i].jobcategory == ' ' ? ' ' : ('<div class="btitle" title="' + escape(data[i].jobcategories) + '">' + escape(data[i].jobcategory) + '...</div>')),
	            		(data[i].skill == ' ' ? ' ' : ('<div class="btitle" title="' + escape(data[i].skills) + '">' + escape(data[i].skill) + '...</div>')),
	            		escape(data[i].seller),
	            		'<img onclick="newTeamMember(this)" title="<fmt:message key="staff.pre_assign"/>" class="link ico" src="images/add_proj.png">',
	            		escape(data[i].<%=Contact.BUSINESSPHONE%>),
	            		escape(data[i].<%=Contact.MOBILEPHONE%>),
	            		escape(data[i].<%=Contact.EMAIL%>),
	            		optionsJobs(data[i].jobArray)
		           	]);
	        }
			chargeBT();	
	});
}

function chargeBT() {
	$('.btitle').bt({
		fill: '#F9FBFF',
		cssStyles: {color: '#343C4E', width: 'auto'},
		width: 250,
		padding: 10,
		cornerRadius: 5,
		spikeLength: 15,
		spikeGirth: 5,
		shadow: true,
		positions: 'top'
	});
}

function updateAssignTable() {
	
	planAjax.call({
		accion:"<%=ProjectPlanServlet.JX_UPDATE_STAFFING_TABLE%>",
		idProject:'${project.idProject}',
		since: $("#resourcesStartCapacityPlanning").val(),
		until: $("#resourcesFinishCapacityPlanning").val(),
        FILTER_DISABLED: $('#showDisabledInRCP').is(':checked')
	},function(data) {
		$('#staffingTableCapacityPlanning').html(data);
	},'html');
}

function chartOBS() {
	
	var params = {
		accion: "<%=ProjectPlanServlet.JX_OBS_CHART%>",
		id: $("#id").val()
	};
	
	$('#obsChart').html('${loading_chart}');

	planAjax.call(params, function (data) {

		if (typeof data.name === 'undefined') {
			$("#obsChart").html('${data_not_found}');
		}
		else {
			
			$('#obsChart').html(initWbs(data, 'obsOrg'));	
			
		    $("#obsOrg").jOrgChart({
				chartElement : '#obsChart'
			});
		    
		    $('#legendObs').empty();
		    createLegend('#legendObs', '<fmt:message key="work_group"/>', "white",  "gray");
			createLegend('#legendObs', '<fmt:message key="wbs.ca_long"/>', "#FFD876", "gray");
			createLegend('#legendObs', '<fmt:message key="wbs.associated_seller"/>',  "#FFD876", "#64D4FA");
            createLegend('#legendObs', '<fmt:message key="wbs.no_budget"/>',  "#FFD876", "#fa9300");
            createLegend('#legendObs', '<fmt:message key="team_members"/>',  "#80DDFC", "gray");
			
			createBT('.btitleChart');
		}
	});
}


// When document is ready
readyMethods.add(function() {
	
	teamTable = $('#tb_team').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"iDisplayLength": 50,
		"bAutoWidth": false,
		"aaSorting": [[0,'desc']],
		"aoColumns": [
              { "bVisible": false },
              { "sWidth": "19%" }, 
              { "sWidth": "21%" },  
              { "sClass": "center", "sWidth": "10%" }, 
              { "sClass": "center", "sWidth": "10%" }, 
              { "sWidth": "10%" },
              { "sClass": "center", "sWidth": "8%" ,"sType": "es_date" }, 
              { "sClass": "center", "sWidth": "8%" ,"sType": "es_date" },
              { "sClass": "center", "sWidth": "8%" },
              { "sClass": "center", "sWidth": "4%", "bSortable" : false  },
              { "bVisible": false },
              { "bVisible": false }
      		]
	});

	$('#pStatus_select').on('change', function() {
	
		teamTable.fnFilter($('#pStatus_select').val(), 4);
	});

	$('#tb_team tbody tr').live('click', function (event) {		
		teamTable.fnSetSelectable(this,'selected_internal');
	} );
	
	searchTable = $('#find_result').dataTable({
		"oLanguage": datatable_language,
		"sPaginationType": "full_numbers",
		"iDisplayLength": 50,
		"aaSorting": [[1,'asc']],
		"aoColumns": [
			  { "bVisible": false },
              { "sClass": "left" },
			  { "bVisible": false },
              { "sClass": "left" }, 
              { "bVisible": false }, 
              { "sClass": "right"}, 
              { "sClass": "left" }, 
              { "bVisible": false },
              { "sClass": "left" }, 
              { "sClass": "left" },
              { "sClass": "left" },
              { "sClass": "center", "bSortable": false },
              { "bVisible": false },
              { "bVisible": false },
              { "bVisible": false },
              { "bVisible": false }
     		]
	});
	$('#find_result tbody tr').live('click', function (event) {		
		searchTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div.dataTables_paginate span').live('mouseup', function() {
		setTimeout("chargeBT()",1000);
	});
	
	var dates = $('#resourcesStartCapacityPlanning, #resourcesFinishCapacityPlanning').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			var option = this.id == "resourcesStartCapacityPlanning" ? "minDate" : "maxDate";
			var instance = $(this).data("datepicker");
			var date = $.datepicker.parseDate(instance.settings.dateFormat || $.datepicker._defaults.dateFormat, selectedDate, instance.settings);
			dates.not(this).datepicker("option", option, date);
		}
	});
	
	chargeBT();
	
	$('#obsChartReload').on('click', function(e){
		e.stopPropagation();
		chartOBS();
	});
	
	$('#showEmployeeDisabled').on('change', function() {
		
		if ($('#showEmployeeDisabled').is(':checked')) {
			teamTable.fnFilter('', 10);
		}
		else {
			teamTable.fnFilter('false', 10);
		}
	}).trigger('change');

    // Only view current assignments
    $('#onlyCurrentAssignments').on('change', function() {

        if ($('#onlyCurrentAssignments').is(':checked')) {
            teamTable.fnFilter('false', 11);
        }
        else {
            teamTable.fnFilter('', 11);
        }
    }).trigger('change');

});
//-->
</script>
<div class="hColor"><fmt:message key="team_members"/></div>
<table id="team_filter" cellspacing="0" style="text-align: left; padding-bottom: 10px; padding-top: 10px;">
	<tr>
		<th><fmt:message key="status"/></th>
		<th><fmt:message key="show.disabled"/></th>
        <th>&nbsp;</th>
        <th><fmt:message key="show.only_current_assignments"/></th>
	</tr>
	<tr>
		<td>
			<select id="pStatus_select" name="pStatus" class="campo" style="width:150px;">
				<option value="" selected><fmt:message key="select_opt"/></option>
				<option value="<fmt:message key="resource.assigned"/>"><fmt:message key="resource.assigned"/></option>
				<option value="<fmt:message key="resource.preassigned"/>"><fmt:message key="resource.preassigned"/></option>
				<option value="<fmt:message key="resource.turneddown"/>"><fmt:message key="resource.turneddown"/></option>
				<option value="<fmt:message key="resource.released"/>"><fmt:message key="resource.released"/></option>
			</select>
		</td>
		<td><input type="checkbox" id="showEmployeeDisabled"/></td>
        <td>&nbsp;</td>
        <td><input type="checkbox" id="onlyCurrentAssignments" checked/></td>
	</tr>
</table>
<table id="tb_team" class="tabledata" cellspacing="1" width="100%">
	<thead>
		<tr>
			<th>&nbsp;</th>
			<th><fmt:message key="contact.fullname"/></th>
			<th><fmt:message key="activity"/></th>
			<th><fmt:message key="team_member.fte"/></th>
			<th><fmt:message key="status"/></th>
			<th><fmt:message key="perforg"/></th>
			<th><fmt:message key="team_member.date_in"/></th>
			<th><fmt:message key="team_member.date_out"/></th>
			<th><fmt:message key="team_member.sell_rate"/></th>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
            <th>&nbsp;</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="member" items="${team}">
			<tr>
				<td>${member.idTeamMember}</td>
				<td>${tl:escape(member.employee.contact.fullName)}</td>
				<td>${tl:escape(member.projectactivity.activityName) }</td>
				<td>${member.fte +0}%</td>
				<td><fmt:message key="resource.${member.status}"/></td>
				<td>${tl:escape(member.employee.performingorg.name)}</td>
				<td><fmt:formatDate value="${member.dateIn}" pattern="${datePattern}" /></td>
				<td><fmt:formatDate value="${member.dateOut}" pattern="${datePattern}" /></td>
				<td>${member.sellRate}</td>
				<td><img onclick="editTeamMember(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png"></td>
				<td>${member.employee.disable || member.employee.contact.disable }</td>
                <td>${member.expiredEndDate}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<%-- FIND RESOURCE --%>
	<fmt:message key="find" var="titleFind"/>
	<visual:panel id="fieldFindResource" title="${titleFind}" cssClass="panel2">
		<table id="find_filter" cellspacing="0" width="100%" style="text-align: center; padding-bottom: 10px;">
			<tr>
				<th width="20%"><fmt:message key="job_category"/></th>
				<th width="20%"><fmt:message key="skill"/></th>
				<th width="10%"><fmt:message key="maintenance.businessline.perf_org"/></th>
				<th width="10%"><fmt:message key="maintenance.employee.resource_pool"/></th>
				<th width="10%"><fmt:message key="seller"/></th>
				<th width="10%" rowspan="2">
					<a class="boton" href="javascript:findTeamMembers();"><fmt:message key="find"/></a>
				</th>
			</tr>
			<tr>
				<td>
					<select id="f_jobcategory" name="f_jobcategory" class="campo" multiple="multiple">
						<c:forEach var="jobcategory" items="${jobcategories}">
							<option value="${jobcategory.idJobCategory}">${jobcategory.name}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<select id="idSkill" name="idSkill" class="campo" multiple="multiple">
						<c:forEach var="skill" items="${skills}">
							<option value="${skill.idSkill}">${skill.name}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<select id="<%=Performingorg.IDPERFORG %>_select" name="<%=Performingorg.IDPERFORG %>" class="campo">
						<option value="-1" selected><fmt:message key="select_opt"/></option>
						<c:forEach var="perforg" items="${perforgs}">
							<option value="${perforg.idPerfOrg}">${perforg.name}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<select id="idResourcePool_select" name="idResourcePool" class="campo">
						<option value="-1" selected><fmt:message key="select_opt"/></option>
						<c:forEach var="resourcePool" items="${resourcePools}">
							<option value="${resourcePool.idResourcepool}">${resourcePool.name}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<select id="idSeller" name="idSeller" class="campo">
						<option value="-1" selected><fmt:message key="select_opt"/></option>
						<c:forEach var="seller" items="${sellers}">
							<option value="${seller.idSeller}">${seller.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
		<table id="find_result" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="0%">&nbsp;</th>
					<th width="15%"><fmt:message key="contact.fullname"/></th>
					<th width="0%"><fmt:message key="profile"/></th>
					<th width="15%"><fmt:message key="maintenance.employee.resource_pool"/></th>
					<th width="0%">&nbsp;</th>
					<th width="10"><fmt:message key="cost.rate"/></th>
					<th width="15%"><fmt:message key="perforg"/></th>
					<th width="0%">&nbsp;</th>
					<th width="15"><fmt:message key="job_category"/></th>
					<th width="15"><fmt:message key="skill"/></th>
					<th width="10"><fmt:message key="seller"/></th>
					<th width="5%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</visual:panel>
</c:if>

<%-- PROJECT STAFFING TABLE --%>
<fmt:message key="menu.resource_capacity_planning" var="titleProjectStaffing"/>
<visual:panel id="fieldPlanStaffing" title="${titleProjectStaffing }" cssClass="panel2">
    <div>
        <span style="margin-right:5px;">
			<fmt:message key="dates.since"/>:&nbsp;
			<input type="text" id="resourcesStartCapacityPlanning" class="campo fecha alwaysEditable" value="${valPlanInitDate}"/>
        </span>
       	<span style="margin-right:5px;">
			<fmt:message key="dates.until"/>:&nbsp;
			<input type="text" id="resourcesFinishCapacityPlanning" class="campo fecha alwaysEditable" value="${valLastDate}"/>
		</span>
        <span style="margin-right:5px;">
            <fmt:message key="show.disabled"/>:&nbsp;
            <input type="checkbox" id="showDisabledInRCP" style="width: 15px" <%= RequestUtil.getBooleanConfiguration(request, Configurations.FILTER_DISABLED)?"checked":"" %>/>
        </span>
		<a href="javascript:updateAssignTable();" class="boton"><fmt:message key="staff.refresh" /></a>
	</div>
	<div id="staffingTableCapacityPlanning"></div>
</visual:panel>

<%-- OBS CHART --%>
<fmt:message key="obs" var="titleObs"/>
<c:set var="obsChartBTN">
	<img id="obsChartReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
</c:set>
<visual:panel id="content-obs" title="${titleObs}" callback="chartOBS" buttons="${obsChartBTN}" cssClass="panel2">
	<div style="overflow-y: hidden; overflow-x: auto; margin:0 auto;">
		<div id="obsChart"></div>		
	</div>
	<div id="legendObs" class="legendChart"></div>
</visual:panel>
