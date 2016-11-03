<%@page import="es.sm2.openppm.core.model.impl.Projectactivity"%>
<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.core.model.impl.Skill"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.core.model.impl.Jobcategory"%>
<%@page import="es.sm2.openppm.core.model.impl.Teammember"%>
<%@page import="es.sm2.openppm.front.servlets.ResourceServlet"%>
<%@ page import="es.sm2.openppm.front.servlets.ProjectPlanServlet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
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
  ~ File: resource_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:04
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtInDateRequired">
	<fmt:param><b><fmt:message key="team_member.date_in"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtOutDateRequired">
	<fmt:param><b><fmt:message key="team_member.date_out"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtInDateFormat">
	<fmt:param><b><fmt:message key="team_member.date_in"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtOutDateFormat">
	<fmt:param><b><fmt:message key="team_member.date_out"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.out_of_range" var="fmtFteOutOfRange">
	<fmt:param><b><fmt:message key="team_member.fte"/></b></fmt:param>
	<fmt:param>1 - 100</fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtMemberInDateAfterStart">
	<fmt:param><b><fmt:message key="team_member.date_in"/></b></fmt:param>
	<fmt:param><b><fmt:message key="activity.planned_init_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_after_date" var="fmtMemberInDateBeforeFinish">
	<fmt:param><b><fmt:message key="team_member.date_in"/></b></fmt:param>
	<fmt:param><b><fmt:message key="activity.planned_end_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtMemberOutDateAfterStart">
	<fmt:param><b><fmt:message key="team_member.date_out"/></b></fmt:param>
	<fmt:param><b><fmt:message key="activity.planned_init_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_after_date" var="fmtMemberOutDateBeforeFinish">
	<fmt:param><b><fmt:message key="team_member.date_out"/></b></fmt:param>
	<fmt:param><b><fmt:message key="activity.planned_end_date"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >
<!--
var planAjax = new AjaxCall('<%=ProjectPlanServlet.REFERENCE%>','<fmt:message key="error" />');
var validatorMember;

function optionsJobCategory(data) {
	
	var options = '<option value=""><fmt:message key="select_opt"/></option>';
	
	$(data).each(function() {
		options += '<option value="'+this.<%=Jobcategory.IDJOBCATEGORY%>+'">'+escape(this.<%=Jobcategory.NAME%>)+'</option>';
	});
	return options;
}

function viewResource(idResource) {
	
	validatorMember.resetForm();
	
	var params = {
		accion: '<%=ResourceServlet.JX_VIEW_RESOURCE%>',
		idResource: idResource
	};
	resourceAjax.call(params, function(data) {
		
		var f = document.frm_resource_pop;
		f.reset();
		f.idResource.value = idResource;
		f.idResourceProposed.value = data.<%=Employee.IDEMPLOYEE%>;
		
		$('#idJobCategory').html(optionsJobCategory(data.jobArray));
		
		f.member_fte.value		= data.<%=Teammember.FTE%>;
		f.member_datein.value	= data.<%=Teammember.DATEIN%>;
		f.member_dateout.value	= data.<%=Teammember.DATEOUT%>;
		f.<%=Teammember.FTE%>.value		= data.<%=Teammember.FTE%>;
		f.<%=Teammember.DATEIN%>.value	= data.<%=Teammember.DATEIN%>;
		f.<%=Teammember.DATEOUT%>.value	= data.<%=Teammember.DATEOUT%>;
		f.<%= Teammember.JOBCATEGORY %>.value	= data.<%= Teammember.JOBCATEGORY %>;
		f.idJobCategory.value		= data.<%=Jobcategory.IDJOBCATEGORY%>;
		f.statusTag.value	= data.statusTag;
		f.<%=Teammember.PROJECTACTIVITY%>.value = data.<%=Teammember.PROJECTACTIVITY%>;
		f.<%=Project.PROJECTNAME%>.value	= data.<%=Project.PROJECTNAME%>;
		f.<%=Project.STATUS%>.value			= data.<%=Project.STATUS%>;
		f.<%=Project.PLANNEDINITDATE%>.value	= data.<%=Project.PLANNEDINITDATE%>;
		f.<%=Project.PLANNEDFINISHDATE%>.value	= data.<%=Project.PLANNEDFINISHDATE%>;
		f.<%=Project.EMPLOYEEBYPROJECTMANAGER%>.value = data.<%=Project.EMPLOYEEBYPROJECTMANAGER%>;
		f.<%=Project.PERFORMINGORG%>.value	= data.<%=Project.PERFORMINGORG%>;
		f.<%=Project.PROGRAM%>.value		= data.<%=Project.PROGRAM%>;
		f.<%=Contact.FULLNAME%>.value		= data.<%=Contact.FULLNAME%>;
		f.proposed_fullname.value		= data.<%=Contact.FULLNAME%>;
        // Activity dates plan or control
		f.firstDate.value  = data.firstDate;
		f.lastDate.value   = data.lastDate;
		
		$('#commentsRm').val(data.<%=Teammember.COMMENTSRM%>);
		
		var commentsPm = data.<%=Teammember.COMMENTSPM%>;
		
		if (typeof commentsPm === 'undefined' || commentsPm == '' || commentsPm == null) {
			$('#commentsForPM').hide();
		}
		else {
			$('#commentsForPM').show();
			$('#commentsPm').html(commentsPm);
		}
		
		if (data.statusVal == '<%=Constants.RESOURCE_PRE_ASSIGNED%>') {
			$('#tableActions').show();
			$('.status').attr('checked',true);
			$('#approve').attr('checked',false);
					
			if (data.statusProject == '<%= Constants.STATUS_INITIATING%>') {
				$('#approveTh').hide();
			}
			else {
				$('#approveTh').show();
			}
		}
		else {
			$('#tableActions').hide();
		}
		
		<%-- Update capacity planning panel --%>
		capacityPlanning.call();
		
		<%-- Show detail --%>
		changeAction();
		$(".listView").hide();
		$("#reject_resource_popup").show();
	});
}

function closeResource() {
	$("#reject_resource_popup").hide();
	$(".listView").show();
}

function changeResource(accion) {
	
	var f = document.frm_resource_pop;
	
	var params = {
		accion: accion,
		idResource: f.idResource.value,
		commentsRm: $('#commentsRm').val()
	};
	resourceAjax.call(params, function() {
		
		resourceTable.fnDraw();
		closeResource();
	});
}

function proposedResource() {
	
	if (validatorMember.form()) {
		var f = document.frm_resource_pop;
		
		var params = {
			accion: '<%=ResourceServlet.JX_PROPOPSED_RESOURCE%>',
			idResource: f.idResource.value,
			idResourceProposed: f.idResourceProposed.value,
			dateIn: f.member_datein.value,
			dateOut: f.member_dateout.value,
			fte: f.member_fte.value,
			idJobCategory: f.idJobCategory.value,
			commentsRm: $('#commentsRm').val()
		};
		resourceAjax.call(params,function() {
			resourceTable.fnDraw();
			closeResource();
		});
	}
}

function actionResource() {
	
	var value = $('.status:checked').val();
	
	if (value == 'approve') { changeResource('<%=ResourceServlet.JX_APPROVE_RESOURCE%>'); }
	else if (value == 'reject') { changeResource('<%=ResourceServlet.JX_REJECT_RESOURCE%>'); }
	else if (value == 'proposed') { proposedResource(); }
	
	return false;
}

function changeAction() {
	
	var value = $('.status:checked').val();
	
	$('#buttonAction').show();
	
	$('#commentsRm').attr('disabled',false);
	
	if (value == 'approve') {
		$('#tableProposed').hide();
		$('#buttonAction').val('<fmt:message key="approve" />');
	}
	else if (value == 'reject') {
		$('#tableProposed').hide();
		$('#buttonAction').val('<fmt:message key="resource.turndown" />');
	}
	else if (value == 'proposed') {
		$('#tableProposed').show();
		$('#buttonAction').val('<fmt:message key="resource.propose" />');
	}
	else {
		$('#commentsRm').attr('disabled',true);
		$('#buttonAction, #tableProposed').hide();
	}
}

function returnDate(id, msg) { return msg+' ('+$(id).val()+')'; }

<%-- Capacity planning --%>
var capacityPlanning = {
		
	call : function() {
		
		var f = document.frm_resource_pop;
		
		var params = {
			accion: '<%= ResourceServlet.JX_VIEW_CAPACITY_PLANNING_RESOURCE_DETAIL %>',
			idResource: f.idResource.value,
			dateIn: f.dateIn.value,
			dateOut: f.dateOut.value
		};
		
		<%-- Reset --%>
		$('#capacityPlanningTable').empty();
		$('#capacityPlanningLegend').empty();
		
		resourceAjax.call(params, function(data) {
			
			<%-- Paint data --%>
			$('#capacityPlanningTable').html(data);
			
			if ($('#capacityPlanningTable').find("table").size() > 0) {
				<%-- Legend --%>
				createLegend("#capacityPlanningLegend", '<fmt:message key="resource.assigned"/>', "green", "black");
				createLegend("#capacityPlanningLegend", '<fmt:message key="resource.preassigned"/>', "orange", "black");
				createLegend("#capacityPlanningLegend", '<fmt:message key="resource.assigned"/> + <fmt:message key="resource.preassigned"/>', "black", "black");
			}
			
		},'html');
	}
};

// Load operation table
function loadOperations() {

    var f = document.frm_resource_pop;

    if (f.firstDate.value != "" && f.lastDate.value != "") {

        var params = {
            accion:         '<%= ProjectPlanServlet.JX_CONSULT_OPERATIONS %>',
            idEmployee:     f.idResourceProposed.value,
            datein:         f.firstDate.value,
            dateout:        f.lastDate.value
        };

        planAjax.call(params, function(data) {

            $("#operationsTableTitle").show();
            $('#operationsTable').html(data);

            $("#datesOperation").css("width","auto");
        },'html');
    }
}

readyMethods.add(function () {
	
	validatorMember = $("#frm_resource_pop").validate({
		errorContainer: 'div#member-errors',
		errorLabelContainer: 'div#member-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#member-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			member_fte: { range: [1,100] },
			member_datein: {
				required: true,
				date: true,
				minDateTo: '#lastDate',
				maxDateTo: '#firstDate'
			},
			member_dateout: {
				required: true,
				date: true,
				minDateTo: '#lastDate',
				maxDateTo: '#firstDate'
			}
		},
		messages: {
			member_fte: { range: '${fmtFteOutOfRange}' },
			member_datein: {
				required: '${fmtInDateRequired}',
				date: '${fmtInDateFormat}',
				minDateTo: function() { return returnDate('#lastDate','${fmtMemberInDateBeforeFinish}'); },
				maxDateTo: function() { return returnDate('#firstDate','${fmtMemberInDateAfterStart}'); }
			},
			member_dateout: {
				required: '${fmtOutDateRequired}',
				date: '${fmtOutDateFormat}',
				minDateTo: function() { return returnDate('#lastDate','${fmtMemberOutDateBeforeFinish}'); },
				maxDateTo: function() { return returnDate('#firstDate','${fmtMemberOutDateAfterStart}'); }
			}
		}
	});
	
	var dates = $('#member_datein, #member_dateout').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function(selectedDate) {
			if (validatorMember.numberOfInvalids() > 0) validatorMember.form();
		}
	});

    $("#idResourceProposed").on("change", function(){
        loadOperations();
    });
	
});
//-->
</script>


<div id="reject_resource_popup" class="popup" style="overflow: hidden;">

	<div id="member-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="member-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	
	<form name="frm_resource_pop" id="frm_resource_pop" method="post">
		<input type="hidden" name="idResource" value="" />
		<input type="hidden" name="idResourceProposed" id="idResourceProposed" value="" />
		<input type="hidden" name="firstDate" id="firstDate" value="" />
		<input type="hidden" name="lastDate" id="lastDate" value="" />
		
		<%-- Basic data --%>
		<fmt:message key="basic_data" var="tilteBasicData"/>
		<visual:panel id="tbProjectData" title="${tilteBasicData }">
			<table border="0" cellpadding="2" cellspacing="1"  style="width:100%;">
				<tr>
					<th width="40%" align="left"><fmt:message key="project" /></th>
					<th width="20%" align="left"><fmt:message key="status" /></th>
					<th width="20%" align="left"><fmt:message key="planned_init_date" /></th>
					<th width="20%" align="left"><fmt:message key="planned_close_date" /></th>
				</tr>
				<tr>
					<td><input type="text" name="<%=Project.PROJECTNAME%>" readonly/></td>
					<td><input type="text" name="<%=Project.STATUS%>" class="left" readonly/></td>
					<td><input type="text" name="<%=Project.PLANNEDINITDATE%>" id="valPlanInitDate" class="left" readonly/></td>
					<td><input type="text" name="<%=Project.PLANNEDFINISHDATE%>" id="valPlanEndDate" class="left" readonly/></td>
				</tr>
				<tr>
					<th align="left"><fmt:message key="project_manager" /></th>
					<th align="left"><fmt:message key="perforg" /></th>
					<th colspan="2" align="left"><fmt:message key="program" /></th>
				</tr>
				<tr>
					<td><input type="text" name="<%=Project.EMPLOYEEBYPROJECTMANAGER%>" readonly/></td>
					<td><input type="text" name="<%=Project.PERFORMINGORG%>" readonly/></td>
					<td colspan="2"><input type="text" name="<%=Project.PROGRAM%>" readonly/></td>
				</tr>
    		</table>
		</visual:panel>
		
		<%-- Assignations --%>
		<fmt:message key="assignments" var="tilteAssignations"/>
		<visual:panel id="assignations" title="${tilteAssignations}">
		
			<%-- Resource data --%>
			<fieldset>
				<legend><fmt:message key="resource.data"/></legend>
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr>
						<th width="40%" align="left"><fmt:message key="contact.fullname" /></th>
						<th width="20%" align="left"><fmt:message key="status" /></th>
						<th width="40%" colspan="2" align="left"><fmt:message key="job_category" /></th>
					</tr>
					<tr>
						<td><input type="text" name="<%=Contact.FULLNAME%>" readonly/></td>
						<td><input type="text" name="statusTag" readonly/></td>
						<td colspan="2"><input type="text" name="<%=Teammember.JOBCATEGORY%>" readonly/></td>
					</tr>
					<tr>
						<th align="left"><fmt:message key="activity" /></th>
						<th align="left"><fmt:message key="team_member.fte" /></th>
						<th align="left"><fmt:message key="team_member.date_in" /></th>
						<th align="left"><fmt:message key="team_member.date_out" /></th>
					</tr>
					<tr>
						<td><input type="text" name="<%=Teammember.PROJECTACTIVITY%>" readonly/></td>
						<td><input type="text" name="<%=Teammember.FTE%>" readonly/></td>
						<td><input type="text" name="<%=Teammember.DATEIN%>" class="left" readonly/></td>
						<td><input type="text" name="<%=Teammember.DATEOUT%>" class="left" readonly/></td>
					</tr>
	    		</table>
	    	</fieldset>
	    	
	    	<div>&nbsp;</div>
	    	
	    	<%-- Actions --%>
	    	<div id="commentsForPM">
				<fieldset>
					<legend><fmt:message key="comments.pm"/></legend>
					<div id="commentsPm"></div>
				</fieldset>
				<div>&nbsp;</div>
			</div>
			<div id="tableActions">
		  		<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr>
						<th id="approveTh">
							<input type="radio" name="statusRe" class="status" id="approve" value="approve" style="width:20px;" onchange="changeAction()"/>
							&nbsp;<fmt:message key="approve"/>
						</th>
						<th>
							<input type="radio" name="statusRe" class="status" id="reject" value="reject" style="width:20px;" onchange="changeAction()"/>
							&nbsp;<fmt:message key="resource.turndown"/>
						</th>
						<th>
							<input type="radio" name="statusRe" class="status" id="proposed" value="proposed" style="width:20px;" onchange="changeAction()"/>
							&nbsp;<fmt:message key="resource.propose"/>
						</th>
					</tr>
		   		</table>
		   		<div>&nbsp;</div>
	   		</div>
	   		
	    	<%-- Proposed --%>
			<fieldset id="tableProposed" style="margin-bottom: 15px;">
				<legend><fmt:message key="resource.proposed.info"/></legend>
                <table border="0" cellpadding="2" cellspacing="1"  style="width:100%;">
					<tr>
						<th style="width:40%;">
							<fmt:message key="resource" />
							<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="openFind()" />
						</th>
						<th style="width:21%;"><fmt:message key="job_category" /></th>
						<th style="width:13%;"><fmt:message key="team_member.fte"/></th>
						<th style="width:13%;"><fmt:message key="team_member.date_in"/></th>
						<th style="width:13%;"><fmt:message key="team_member.date_out"/></th>
					</tr>
					<tr>
						<td><input name="proposed_fullname" id="proposed_fullname" class="campo" readonly/></td>
						<td>
							<select id="idJobCategory" name="idJobCategory" class="campo">
							</select>
						</td>
						<td><input type="text" class="campo" style="width: 60px;" name="member_fte"/></td>
						<td><input type="text" class="campo fecha" name="member_datein" id="member_datein"/></td>
						<td><input type="text" class="campo fecha" name="member_dateout" id="member_dateout"/></td>
					</tr>
	    		</table>
            </fieldset>

            <%-- No se puede meter dentro el fieldset porque hace overflow, solucion esperar migracion --%>
            <%-- Operations --%>
            <div id="operationsTableTitle" style="display:none;">
                <table border="0" cellpadding="2" cellspacing="1"  style="width:100%;">
                    <tr>
                        <th><fmt:message key="timesheet.operation"/></th>
                    </tr>
                </table>
            </div>
            <div id="operationsTable"></div>
            <div>&nbsp;</div>

	   		<%-- Comments --%>
			<fieldset id="tableComments">
				<legend><fmt:message key="comments"/></legend>
				<table border="0" cellpadding="2" cellspacing="1"  style="width:100%;">
					<tr>
						<td>
							<textarea name="commentsRm" id="commentsRm" style="width: 100%;" rows="5" class="campo"></textarea>
						</td>
					</tr>
	    		</table>
	    	</fieldset>
	    	
		<%-- Buttons --%>
    	<div class="popButtons" style="padding-bottom: 0;">
    		<input type="submit" class="boton" id="buttonAction" style="width: 50px;" onclick="return actionResource();" value="<fmt:message key="reject" />">
			<a href="javascript:closeResource()" class="boton"><fmt:message key="back" /></a>
    	</div>
		</visual:panel>
		
		<%-- Capacity Planning --%>
		<fmt:message key="capacity_planning" var="tilteCapacityPlanning"/>
		<visual:panel id="capacityPlanningPanel" title="${tilteCapacityPlanning}" >
			<div id="capacityPlanningTable"></div>
			<div id="capacityPlanningLegend" class="legendChart"></div>
		</visual:panel>
		
		<%-- Buttons --%>
    	<div class="popButtons" style="padding-bottom: 0;">
			<a href="javascript:closeResource()" class="boton"><fmt:message key="back" /></a>
    	</div>
    </form>
</div>