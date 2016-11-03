<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Milestonetype"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.model.impl.Milestones"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>

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
  ~ File: new_milestone_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:01
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message var="msg_milestone_sign" key="milestone.report_type.milestone_sign" />
<fmt:message var="msg_no_report" key="milestone.report_type.no_report" />
<fmt:message key="error" var="fmt_error" />
<fmt:message key="msg.error.date_outside_project" var="fmt_error_range_date" >
	<fmt:param><fmt:message key="milestone"/></fmt:param>
</fmt:message>
<fmt:message key="msg.milestone.planned_edit" var="editOnlyToPlan" />

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtActivityRequired">
	<fmt:param><b><fmt:message key="activity.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtMilestoneNameRequired">
	<fmt:param><b><fmt:message key="milestone.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtArchivedDateFormat">
	<fmt:param><b><fmt:message key="milestone.actual_date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_after_date" var="fmtArchivedDateAfterFinish">
	<fmt:param><b><fmt:message key="milestone.actual_date"/></b></fmt:param>
	<fmt:param><b><fmt:message key="activity.actual_end_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtArchivedDateBeforeStart">
	<fmt:param><b><fmt:message key="milestone.actual_date"/></b></fmt:param>
	<fmt:param><b><fmt:message key="activity.actual_init_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtEstimatedDateFormat">
	<fmt:param><b><fmt:message key="milestone.due_date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtEstimatedDateRequired">
	<fmt:param><b><fmt:message key="milestone.due_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_after_date" var="fmtEstimatedDateAfterFinish">
	<fmt:param><b><fmt:message key="milestone.due_date"/></b></fmt:param>
	<fmt:param><b><fmt:message key="activity.actual_end_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtEstimatedDateBeforeStart">
	<fmt:param><b><fmt:message key="milestone.due_date"/></b></fmt:param>
	<fmt:param><b><fmt:message key="activity.actual_init_date"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtTypeRequired">
	<fmt:param><b><fmt:message key="milestone.type"/></b></fmt:param>
</fmt:message>

<%-- Buttons --%>
<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="deleteMilestone"><img onclick="milestone.delete(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></c:set>
</c:if>

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="controlMilestone"><img onclick="milestone.view(this);" title="<fmt:message key="edit"/>" class="link" src="images/view.png"></c:set>										
</c:if>

<script type="text/javascript">
<!--
var validatorMilestone;

var milestonePopup = {
		
		// Save
		save: function() {
			
			var f = document.forms["frm_milestone"];

			if (validatorMilestone.form()) {
				
				var params = {
					accion: 							"<%=ProjectControlServlet.JX_SAVE_MILESTONE %>",
					milestone_id: 						f.milestone_id.value,
					activity:							f.activity.value,
					milestoneName:						f.<%= Milestones.NAME %>.value,
					description:						$("#milestoneDesc").val(),
					report_type:						($('#sign').is(':checked')?"Y":"N"),
					<%= Project.IDPROJECT %>:			f.<%=Project.IDPROJECT%>.value,
					estimatedDate: 						f.estimatedDate.value,
					achieved: 							f.achieved.value,
					<%= Milestones.MILESTONETYPE %>:	f.<%= Milestones.MILESTONETYPE %>.value,
					<%= Milestones.MILESTONECATEGORY %>:f.<%= Milestones.MILESTONECATEGORY %>.value,
					<%=Milestones.ACHIEVEDCOMMENTS %>:	$("#<%=Milestones.ACHIEVEDCOMMENTS %>").val()
				};
				
				controlAjax.call(params, function (data) {
					
					var dataRow = [
						data.id,
						$("#milestone_activity option:selected").text(),
						escape(data.name),
						$("#milestoneDesc").val(),
						data.planned,
						'<input type="checkbox" disabled="disabled" '+(data.report_type == 'Y'?'checked':'')+'/>',
						data.estimatedDate,
						data.achieved,
						escape($('#<%=Milestones.ACHIEVEDCOMMENTS %>').val()),
						data.milestoneTypeId,
						escape(data.milestoneTypeName),
						data.milestoneCategoryId,
						escape(data.milestoneCategoryName),
						'${controlMilestone}'+
						'&nbsp;&nbsp;&nbsp;'+
						(data.planned == "" ? '${deleteMilestone}' : ''),
						data.idActivity,
						data.description,
						data.report_type
					];
			
					if (f.milestone_id.value == '') { milestonesTable.fnAddDataAndSelect(dataRow); }
					else { milestonesTable.fnUpdateAndSelect(dataRow); }
					
					f.reset();
					$('#milestone-popup').dialog('close');
				});
			} 
		},
		
		// Close
		close: function() {
			$('div#milestone-popup').dialog('close');
		}
};

function updateActivityDates(id) {
	
	var data = activitiesTable.fnFindData(id,0);
	
	if (data != null) {
		
		var start	= (data[5] == ''?data[3]:data[5]);
		var finish	= (data[6] == ''?data[4]:data[6]);
		
		$("#achieved").datepicker("option", "minDate", start);
		$("#achieved").datepicker("option", "maxDate", finish);
		$("#estimatedDate").datepicker("option", "minDate", start);
		$("#estimatedDate").datepicker("option", "maxDate", finish);
		
		$("#plannedDateMin").val(start);
		$("#plannedDateMax").val(finish);
	}
	else {
		$("#planned_date").datepicker("option", "minDate", '');
		$("#planned_date").datepicker("option", "maxDate", '');
		$("#plannedDateMin").val('');
		$("#plannedDateMax").val('');
	}
} 

readyMethods.add(function() {
	
	$('div#milestone-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 550, 
		minWidth: 550, 
		resizable: false,
		open: function(event, ui) { validatorMilestone.resetForm(); }
	});
	
	//Validate required fields
	validatorMilestone = $("#frm_milestone").validate({
		errorContainer: 'div#milestone-errors',
		errorLabelContainer: 'div#milestone-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#milestone-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			activity: 							{ required: true },	
			<%= Milestones.NAME %>:				{ required: true },	
			<%=Milestones.DESCRIPTION %>: 		{ maxlength: 1500 },
			estimatedDate: 						{required: true, date: true, minDateTo : '#plannedDateMax', maxDateTo : '#plannedDateMin' },
			achieved: 							{date: true, minDateTo : '#plannedDateMax', maxDateTo : '#plannedDateMin' },
			<%=Milestones.ACHIEVEDCOMMENTS%>: 	{ maxlength: 1500 },
			<%= Milestones.MILESTONETYPE %>:	{required: true}
		},
		messages: {
			activity: 							{ required: '${fmtActivityRequired}' },	
			<%= Milestones.NAME %>: 			{ required: '${fmtMilestoneNameRequired}' },	
			<%= Milestones.MILESTONETYPE %>: 	{ required: '${fmtTypeRequired}'},
			estimatedDate: {
				required: '${fmtEstimatedDateRequired}',
				date: '${fmtEstimatedDateFormat}',
				minDateTo : function() { return '${fmtEstimatedDateAfterFinish} ('+$('#plannedDateMax').val()+')';},
				maxDateTo : function() { return '${fmtEstimatedDateBeforeStart} ('+$('#plannedDateMin').val()+')';}
			},
			achieved: {
				date: '${fmtArchivedDateFormat}',
				minDateTo : function() { return '${fmtArchivedDateAfterFinish} ('+$('#plannedDateMax').val()+')';},
				maxDateTo : function() { return '${fmtArchivedDateBeforeStart} ('+$('#plannedDateMin').val()+')';}
			}
		}
	});
	
	var dates = $('#achieved').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function() {
			if (validatorMilestone.numberOfInvalids() > 0) validatorMilestone.form();
		}
	});
	
	var dates = $('#estimatedDate').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function() {
			if (validatorMilestone.numberOfInvalids() > 0) validatorMilestone.form();
		}
	});
	
	$("#milestone_activity").bind("change",function() { updateActivityDates($(this).val()); });
	
	createBT('.btitlePlanned', "most");
	
	$("#<%= Milestones.MILESTONETYPE %>").selectDescription();
	
	$("#<%= Milestones.MILESTONECATEGORY %>").selectDescription({
		callback:function($data){
		
			if ($data.find('option:selected').attr("value") != "") {
				
				$("#milestoneName").val($data.find('option:selected').attr("description"));
				
				$("#milestoneName").addClass("disabled");
				
				$("#milestoneName").attr("readonly","readonly");
			}
			else {
				
				$("#milestoneName").removeClass("disabled");
				
				$("#milestoneName").removeAttr("readonly");
			}
		}
	});
	
});
//-->
</script>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<script type="text/javascript">
	<!--
		readyMethods.add(function() {
			var dates = $('#planned_date').datepicker({
				dateFormat: '${datePickerPattern}',
				firstDay: 1,
				showOn: 'button',
				buttonImage: 'images/calendario.png',
				buttonImageOnly: true,
				numberOfMonths: ${numberOfMonths},
				changeMonth: true,
				onSelect: function() {
					if (validatorMilestone.numberOfInvalids() > 0) validatorMilestone.form();
				}
			});
		});		
	//-->
	</script>

</c:if>

<div id="milestone-popup" class="popup">
	
	<div id="milestone-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="milestone-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	
	<form name="frm_milestone" id="frm_milestone" action="<%=ProjectControlServlet.REFERENCE%>" >
		<input type="hidden" name="accion" value=""/>
		<input type="hidden" name="milestone_id" id="milestone_id"/>
		<input type="hidden" name="<%=Project.IDPROJECT%>" value="${project.idProject }"/>
		<input type="hidden" name="label" class="campo" maxlength="30" />
		<input type="hidden" id="plannedDateMin"/>
		<input type="hidden" id="plannedDateMax"/>
		
		<fieldset>
			<legend><fmt:message key="milestone"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>					
					<th colspan="4" class="left" ><fmt:message key="activity.name"/>&nbsp;*</th>
				</tr>
				<tr>					
					<td colspan="4" >
						<select name="activity" id="milestone_activity" class="campo" style="width: 100%;">
							<option value="" selected><fmt:message key="select_opt"/></option>
							<c:forEach var="activity" items="${activities}">
								<option value="${activity.idActivity}">${tl:escape(activity.activityName)}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				
				<tr>
					<th colspan="2" class="left"><fmt:message key="milestone.category"/>&nbsp;</th>
					<th colspan="2" class="left" ><fmt:message key="milestone.name"/>&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="2">
						<%-- Milestone category --%>
						<select name="<%= Milestones.MILESTONECATEGORY %>" id="<%= Milestones.MILESTONECATEGORY %>" class="campo">
							<option value="" selected><fmt:message key="select_opt"/></option>
							<c:forEach var="milestoneCategory" items="${milestoneCategories}">
								<option value="${milestoneCategory.idMilestoneCategory}" description="${tl:escape(milestoneCategory.description)}">${tl:escape(milestoneCategory.name)}</option>
							</c:forEach>
						</select>
					</td>
					<td colspan="2"><input type="text" name="<%= Milestones.NAME %>" class="campo" id="milestoneName"/></td>
				</tr>
				
				<tr>
					<th colspan="2" class="left"><fmt:message key="milestone.type"/>&nbsp;*</th>
					<th class="left"><fmt:message key="milestone.planned_date"/></th>
					<th class="left"><fmt:message key="milestone.show_milestone_sign"/></th>
				</tr>
				<tr>
					<td colspan="2">
						<%-- Milestone types --%>
						<select name="<%= Milestones.MILESTONETYPE %>" id="<%= Milestones.MILESTONETYPE %>" class="campo">
							<option value="" selected><fmt:message key="select_opt"/></option>
							<c:forEach var="milestoneType" items="${milestoneTypes}">
								<option value="${milestoneType.idMilestoneType}" description="${tl:escape(milestoneType.description)}">${tl:escape(milestoneType.name)}</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<input type="text" name="plannedDate" readonly="readonly" class="campo fecha disabled" disabled/>
						<img src="images/info.png" class="btitlePlanned" style="display: inline;" title="" bt-xtitle="${editOnlyToPlan}">
					</td>
					<td><input type="checkbox" name="report_type" id="sign" style="width: 16px;"/></td>
				</tr>
				
				<%-- Description --%>
				<tr>
					<th colspan="4" class="left"><fmt:message key="milestone.desc"/></th>
				</tr>
				<tr>
					<td colspan="4">
						<textarea name="<%= Milestones.DESCRIPTION %>" id="milestoneDesc" class="campo" style="width:98%;" rows="8" maxlength="1500"></textarea>
					</td>
				</tr>
				
				<%-- Dates --%>
				<tr>
					<th class="left"><fmt:message key="milestone.due_date"/>&nbsp;*</th>
					<th class="left"><fmt:message key="milestone.actual_date"/></th>
					<th></th>
					<th></th>
				</tr>
				<tr>
					<td><input type="text" name="estimatedDate" id="estimatedDate" class="campo fecha" /></td>
					<td><input type="text" name="achieved" id="achieved" class="campo fecha" /></td>
					<td></td>
					<td></td>
				</tr>
				
				<%-- Comments --%>
				<tr>
					<th class="left" colspan="4"><fmt:message key="milestone.achievement_comments"/></th>
				</tr>
				<tr>
					<td colspan="4">
						<textarea name="<%=Milestones.ACHIEVEDCOMMENTS %>" id="<%=Milestones.ACHIEVEDCOMMENTS %>" style="width: 98%" class="campo" maxlength="1500" rows="8"></textarea>
					</td>
				</tr>
				
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<c:if test="${op:hasPermission(user,project.status,tab)}">
    			<input type="submit" class="boton" onclick="milestonePopup.save(); return false;" value="<fmt:message key="save" />" />
			</c:if>
			<a href="javascript:milestonePopup.close();" class="boton"><fmt:message key="close" /></a>
    	</div>
    </form>
</div>