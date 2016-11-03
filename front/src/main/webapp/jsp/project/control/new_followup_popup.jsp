<%@page import="es.sm2.openppm.front.servlets.FollowProjectsServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectfollowup"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

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
  ~ File: new_followup_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:00
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="maintenance.error_msg_a" var="fmtFollowupDateRequired">
	<fmt:param><b><fmt:message key="followup.date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtFollowupDateFormat">
	<fmt:param><b><fmt:message key="followup.date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="msg.error.date_after_date" var="fmtFollowupDateAfterFinish">
	<fmt:param><b><fmt:message key="followup.date"/></b></fmt:param>
	<c:choose>
		<c:when test="${rootActivity.lastDate != rootActivity.planEndDate}">
			<fmt:param><b><fmt:message key="activity.actual_end_date"/></b></fmt:param>
		</c:when>
		<c:otherwise>
			<fmt:param><b><fmt:message key="activity.planned_end_date"/></b></fmt:param>
		</c:otherwise>
	</c:choose>
</fmt:message>
<fmt:message key="msg.error.date_before_date" var="fmtFollowupDateBeforeStart">
	<fmt:param><b><fmt:message key="followup.date"/></b></fmt:param>
	<fmt:param><b><fmt:message key="activity.planned_init_date"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--

var newFollowupValidator;
var planAjax = new AjaxCall('<%=ProjectPlanServlet.REFERENCE%>','<fmt:message key="error"/>');

function openCreateFollowup() {

	document.frm_new_followup.reset();

    $("#followupDate").val($("#actualDate").val());

	$('div#new-followup-popup').dialog('open');
}
function createFollowup() {

	var params = {
		accion: "<%= ProjectControlServlet.JX_EXIST_FOLLOWUP %>",
		id: $("#id").val(),
		date: $("#followupDate").val()
	};
	
	controlAjax.call(params, function(data) {
		if (newFollowupValidator.form()) {
			loadingPopup();

			var f =  document.forms["frm_new_followup"];
			f.scrollTop.value = $(document).scrollTop();
			document.frm_new_followup.submit();
		} 
	});
}

readyMethods.add(function() {
	$('div#new-followup-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 450, 
		resizable: false,
		open: function(event, ui) { newFollowupValidator.resetForm(); }
	});

	var dates = $('#<%=Projectfollowup.FOLLOWUPDATE %>').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function() {
			var params = {
				accion: '<%=ProjectPlanServlet.CALCULATED_PV%>',
				date: $('#<%=Projectfollowup.FOLLOWUPDATE %>').val(),
				idProject: document.forms["frm_project"].id.value
			};

			planAjax.call(params,  function (data) {
				$('#<%=Projectfollowup.PV%>').val(""); //reset value
				
				if(data.pv != -1){ // There are earlier date and later
					$('#<%=Projectfollowup.PV%>').val(toCurrency(data.pv));
				}
			});
			
			
			if (newFollowupValidator.numberOfInvalids() > 0) {
				newFollowupValidator.form();
			}
		}//,maxDate: '+1d'
	});
	
	newFollowupValidator = $("#frm_new_followup").validate({
		errorContainer: 'div#new-followup-errors',
		errorLabelContainer: 'div#new-followup-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#new-followup-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Projectfollowup.FOLLOWUPDATE %>: { required: true, date: true, minDateTo: '#valLastDate', maxDateTo: '#valPlanInitDate'}
		},
		messages: {
			<%=Projectfollowup.FOLLOWUPDATE %>: {
				required: '${fmtFollowupDateRequired}',
				date: '${fmtFollowupDateFormat}',
				minDateTo: '${fmtFollowupDateAfterFinish} ('+$('#valLastDate').val()+')',
				maxDateTo: '${fmtFollowupDateBeforeStart} ('+$('#valPlanInitDate').val()+')'
			}
		}
	});
});
//-->
</script>

<div id="new-followup-popup" class="popup">

	<div id="new-followup-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="new-followup-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_new_followup" id="frm_new_followup" action="<%=ProjectControlServlet.REFERENCE%>" method="post">
		<input type="hidden" name="accion" value="<%=ProjectControlServlet.NEW_FOLLOWUP%>"/>
		<input type="hidden" name="<%=Project.IDPROJECT %>" value="${project.idProject }"/>
		<input type="hidden" name="scrollTop" value="" />
		<fieldset>
			<legend><fmt:message key="planned_value"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="50%"><fmt:message key="followup.date"/>&nbsp;*</th>
					<th class="left" width="50%"><fmt:message key="followup.value"/></th>
				</tr>
				<tr>
					<td>
						<input type="text" id="<%=Projectfollowup.FOLLOWUPDATE %>" name="<%=Projectfollowup.FOLLOWUPDATE %>" class="campo fecha" />
					</td>
					<td>
						<input type="text" name="<%=Projectfollowup.PV%>" id="<%=Projectfollowup.PV%>" class="campo importe" />
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="createFollowup(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>