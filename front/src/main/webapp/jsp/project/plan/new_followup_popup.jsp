 <%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
 <%@ page import="es.sm2.openppm.core.logic.security.actions.PlanTabAction" %>
 <%@ page import="es.sm2.openppm.core.common.enums.PeriodicityEnum" %>

 <%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
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
  ~ File: new_followup_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:48
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="error" var="fmt_error" />

<c:if test="${not op:isStringEquals(project.status, status_closed)}">
	<c:set var="editFollowup"><img onclick="editFollowup(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png"></c:set>
	<c:set var="deleteFollowup"><img onclick="deleteFollowup(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></c:set>
</c:if>

<%-- Message for validations --%>
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

<fmt:message key="msg.error.date_after_date" var="fmtFollowupAfterFinishSince">
     <fmt:param><b><fmt:message key="dates.since"/></b></fmt:param>
     <c:choose>
         <c:when test="${rootActivity.lastDate != rootActivity.planEndDate}">
             <fmt:param><b><fmt:message key="activity.actual_end_date"/></b></fmt:param>
         </c:when>
         <c:otherwise>
             <fmt:param><b><fmt:message key="activity.planned_end_date"/></b></fmt:param>
         </c:otherwise>
     </c:choose>
 </fmt:message>
 <fmt:message key="msg.error.date_before_date" var="fmtFollowupBeforeStartSince">
     <fmt:param><b><fmt:message key="dates.since"/></b></fmt:param>
     <fmt:param><b><fmt:message key="activity.planned_init_date"/></b></fmt:param>
 </fmt:message>

 <fmt:message key="msg.error.date_after_date" var="fmtFollowupAfterFinishUntil">
     <fmt:param><b><fmt:message key="dates.until"/></b></fmt:param>
     <c:choose>
         <c:when test="${rootActivity.lastDate != rootActivity.planEndDate}">
             <fmt:param><b><fmt:message key="activity.actual_end_date"/></b></fmt:param>
         </c:when>
         <c:otherwise>
             <fmt:param><b><fmt:message key="activity.planned_end_date"/></b></fmt:param>
         </c:otherwise>
     </c:choose>
 </fmt:message>
 <fmt:message key="msg.error.date_before_date" var="fmtFollowupBeforeStartUntil">
     <fmt:param><b><fmt:message key="dates.until"/></b></fmt:param>
     <fmt:param><b><fmt:message key="activity.planned_init_date"/></b></fmt:param>
 </fmt:message>
 <fmt:message key="maintenance.error_msg_a" var="fmtFollowupSinceDateRequired">
     <fmt:param><b><fmt:message key="dates.since"/></b></fmt:param>
 </fmt:message>
 <fmt:message key="msg.error.invalid_format" var="fmtFollowupSinceDateFormat">
     <fmt:param><b><fmt:message key="dates.since"/></b></fmt:param>
     <fmt:param>${datePattern}</fmt:param>
 </fmt:message>
 <fmt:message key="maintenance.error_msg_a" var="fmtFollowupUntilDateRequired">
     <fmt:param><b><fmt:message key="dates.until"/></b></fmt:param>
 </fmt:message>
 <fmt:message key="msg.error.invalid_format" var="fmtFollowupUntilDateFormat">
     <fmt:param><b><fmt:message key="dates.until"/></b></fmt:param>
     <fmt:param>${datePattern}</fmt:param>
 </fmt:message>
 <fmt:message key="maintenance.error_msg_a" var="fmtFollowupPeriodicityRequired">
     <fmt:param><b><fmt:message key="periodicity"/></b></fmt:param>
 </fmt:message>


<script type="text/javascript">
<!--
var followupValidator;
var followupCreateDatesValidator;

var followup = {
	
	load : function() {
		
		$("#followup_date").on('change', followup.calculatePV);
	},
	calculatePV : function() {
		
		var params = {
			accion: '<%=ProjectPlanServlet.CALCULATED_PV%>',
			date: $("#followup_date").val(),
			idProject: document.forms["frm_project"].id.value
		};

		planAjax.call(params,  function (data) {
			$("#followup_pv").val(""); //reset value
			
			if(data.pv != -1){ // There are earlier date and later
				$("#followup_pv").val(toCurrency(data.pv));
			}
		});

		if (followupValidator.numberOfInvalids() > 0) followupValidator.form();
	},
    save: function() {

        if (followupValidator.form()) {

            var f = document.frm_followup;

            var params = {
                accion: "<%=ProjectPlanServlet.JX_SAVE_FOLLOWUP%>",
                id: document.forms["frm_project"].id.value,
                followup_id: f.followup_id.value,
                date: f.date.value,
                pv: f.pv.value
            };

            planAjax.call(params,  function (data) {

                var dataRow = [
                    data.id,
                    data.date,
                    data.daysToDate,
                    data.pv,
                            '${editFollowup}'+
                            '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
                            '${deleteFollowup}'
                ];

                if (f.followup_id.value != data.id) { followupsTable.fnAddDataAndSelect(dataRow); }
                else { followupsTable.fnUpdateAndSelect(dataRow); }

                f.reset();
                $('#followup-popup').dialog('close');
            });
        }
    },
    createDates: function() {

        if (followupCreateDatesValidator.form()) {

            var f = document.frmFollowupCreateDates;

            var params = {
                accion: "<%= PlanTabAction.JX_CREATE_FOLLOWUPS %>",
                dateSince: f.sinceFollowup.value,
                dateUntil: f.untilFollowup.value,
                periodicity: f.periodicity.value,
                periodicityCount: f.periodicityCount.value,
                idProject: $("#id").val()
            };

            planAjax.call(params,  function (data) {

                for (var i = 0; i < data.followups.length; i++) {

                    var followup = data.followups[i];

                    var dataRow = [
                        followup.id,
                        followup.date,
                        followup.daysToDate,
                        typeof followup.pv === 'undefined' ? "" : toCurrency(followup.pv),
                        '${editFollowup}'+
                        '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
                        '${deleteFollowup}'
                    ];

                    followupsTable.fnAddDataAndSelect(dataRow);
                }

                f.reset();
                $('#followupCreateDates-popup').dialog('close');
            });
        }
    },
    generic: function() {
        $(".optionFollowup").hide();
        $("#genericFollowup").show();
    },
    specific: function() {
        $(".optionFollowup").hide();
        $("#specificFollowup").show();
    }
};

readyMethods.add(function() {

    // Create popup
	$('div#followup-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 450, 
		resizable: false,
		open: function(event, ui) { followupValidator.resetForm(); }
	});

    // Create popup
    $('div#followupCreateDates-popup').dialog({
        autoOpen: false,
        modal: true,
        width: 450,
        resizable: false,
        open: function(event, ui) { followupCreateDatesValidator.resetForm(); }
    });

	followupValidator = $("#frm_followup").validate({
		errorContainer: 'div#followup-errors',
		errorLabelContainer: 'div#followup-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#followup-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			date: { required: true, date: true, minDateTo: '#valLastDate', maxDateTo: '#valPlanInitDate' }
		},
		messages: {
			date: {
				required: '${fmtFollowupDateRequired}',
				date: '${fmtFollowupDateFormat}',
				minDateTo: '${fmtFollowupDateAfterFinish} ('+$('#valLastDate').val()+')',
				maxDateTo: '${fmtFollowupDateBeforeStart} ('+$('#valPlanInitDate').val()+')'
			}
		}
	});

    // Validator create dates periodically for followups
    followupCreateDatesValidator = $("#frmFollowupCreateDates").validate({
        errorContainer: 'div#followupCreateDates-errors',
        errorLabelContainer: 'div#followupCreateDates-errors ol',
        wrapper: 'li',
        showErrors: function(errorMap, errorList) {
            $('span#followupCreateDates-numerrors').html(this.numberOfInvalids());
            this.defaultShowErrors();
        },
        rules: {
            sinceFollowup: { required: true, date: true, minDateTo: '#valLastDate', maxDateTo: '#valPlanInitDate' },
            untilFollowup:  { required: true, date: true, minDateTo: '#valLastDate', maxDateTo: '#valPlanInitDate' },
            periodicity: {required: true},
            radioFollowup: {required: true},
            periodicityCount: {required: true}
        },
        messages: {
            sinceFollowup: {
                required: '${fmtFollowupSinceDateRequired}',
                date: '${fmtFollowupSinceDateFormat}',
                minDateTo: '${fmtFollowupAfterFinishSince} ('+$('#valLastDate').val()+')',
                maxDateTo: '${fmtFollowupBeforeStartSince} ('+$('#valPlanInitDate').val()+')'
            },
            untilFollowup: {
                required: '${fmtFollowupUntilDateRequired}',
                date: '${fmtFollowupUntilDateFormat}',
                minDateTo: '${fmtFollowupAfterFinishUntil} ('+$('#valLastDate').val()+')',
                maxDateTo: '${fmtFollowupBeforeStartUntil} ('+$('#valPlanInitDate').val()+')'
            },
            periodicity: {required: '${fmtFollowupPeriodicityRequired}'},
            radioFollowup: {required: '${fmtFollowupPeriodicityRequired}'},
            periodicityCount: {required: '${fmtFollowupPeriodicityRequired}'}
        }
    });
	
	$('#followup_date').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		changeMonth: true,
		numberOfMonths: ${numberOfMonths},
		onSelect: followup.calculatePV
	});

    $('#sinceFollowup').datepicker({
        dateFormat: '${datePickerPattern}',
        firstDay: 1,
        showOn: 'button',
        buttonImage: 'images/calendario.png',
        buttonImageOnly: true,
        changeMonth: true,
        numberOfMonths: ${numberOfMonths},
        minDate:  $("#valPlanInitDate").val(),
        maxDate: $("#valLastDate").val(),
        onSelect: function() {
            if (followupCreateDatesValidator.numberOfInvalids() > 0) followupCreateDatesValidator.form();
        }
    });

    $('#untilFollowup').datepicker({
        dateFormat: '${datePickerPattern}',
        firstDay: 1,
        showOn: 'button',
        buttonImage: 'images/calendario.png',
        buttonImageOnly: true,
        changeMonth: true,
        numberOfMonths: ${numberOfMonths},
        minDate:  $("#valPlanInitDate").val(),
        maxDate: $("#valLastDate").val(),
        onSelect: function() {
            if (followupCreateDatesValidator.numberOfInvalids() > 0) followupCreateDatesValidator.form();
        }
    });
	
	followup.load();

    $("#saveFollowup").click(followup.save);

    $("#createDatesFollowup").click(followup.createDates);

    <%--  Events checkbox --%>
    $("#radioSpecific").click(followup.specific);
    $("#radioGeneric").click(followup.generic);
});
//-->
</script>

<div id="followup-popup" class="popup">

    <%-- Errors --%>
	<div id="followup-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="followup-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

    <%-- SPECIFIC --%>
    <fieldset>
        <legend><fmt:message key="planned_value" /></legend>
        <form name="frm_followup" id="frm_followup" action="<%=ProjectPlanServlet.REFERENCE%>" method="post">
            <input type="hidden" name="followup_id" />
            <table border="0" cellpadding="2" cellspacing="1" width="100%">
                <tr>
                    <th class="left" width="33%"><fmt:message key="followup.date"/>&nbsp;*</th>
                    <th class="left" width="33%"><fmt:message key="planned_value.days_date"/></th>
                    <th class="left" width="34%"><fmt:message key="planned_value.value"/></th>
                </tr>
                <tr>
                    <td>
                        <input type="text" name="date" id="followup_date" class="campo fecha" />
                    </td>
                    <td>
                        <input type="text" name="daysToDate" readonly="readonly" />
                    </td>
                    <td>
                        <input type="text" name="pv" id="followup_pv" class="campo importe" />
                    </td>
                </tr>
            </table>
            <div class="popButtons">
                <input type="button" class="boton" id="saveFollowup" value="<fmt:message key="save" />" />
            </div>
        </form>
    </fieldset>
</div>

 <%-- PERIODICITY --%>
 <div id="followupCreateDates-popup" class="popup">

     <%-- Errors --%>
     <div id="followupCreateDates-errors" class="ui-state-error ui-corner-all hide">
         <p>
             <span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
             <strong><fmt:message key="msg.error_title"/></strong>
             &nbsp;(<b><span id="followupCreateDates-numerrors"></span></b>)
         </p>
         <ol></ol>
     </div>

     <fieldset>
         <legend><fmt:message key="followup.periodicity" /></legend>
         <form name="frmFollowupCreateDates" id="frmFollowupCreateDates" action="<%=ProjectPlanServlet.REFERENCE%>" method="post">

             <table border="0" cellpadding="2" cellspacing="1" width="100%">
                 <tr>
                     <th class="center"><fmt:message key="dates.since"/>&nbsp;*</th>
                     <th class="center"><fmt:message key="dates.until"/>&nbsp;*</th>
                 </tr>
                 <tr>
                    <td class="center"><input type="text" name="sinceFollowup" id="sinceFollowup" class="campo fecha" /></td>
                    <td class="center"><input type="text" name="untilFollowup" id="untilFollowup" class="campo fecha" /></td>
                 </tr>
                 <tr><td>&nbsp;</td></tr>
                 <tr>
                     <th colspan="2"><fmt:message key="periodicity"/>&nbsp;*</th>
                 </tr>
                 <tr>
                    <td colspan="2">
                        <%-- SELECT OPTION --%>
                        <div id="selectionFollowup" style="text-align: center; margin-bottom: 12px;display:block;">
                            <fmt:message key="generic"/>:&nbsp;<input type="radio" name="radioFollowup" class="radioFollowup" id="radioGeneric" style="margin-right: 10px; width: 15px;"/>
                            <fmt:message key="specific"/>:&nbsp;<input type="radio" name="radioFollowup" class="radioFollowup" id="radioSpecific" style="width:15px;"/>
                        </div>
                    </td>
                 </tr>
             </table>

             <div id="genericFollowup" class="optionFollowup" style="display:none;text-align: center;">
                 <select name="periodicity" id="periodicity" class="campo" style="width:200px;">
                     <option value="" selected><fmt:message key="select_opt"/></option>
                     <c:forEach var="periodicity" items="<%= PeriodicityEnum.values() %>">
                         <option value="${periodicity}"><fmt:message key="${periodicity.label}"/></option>
                     </c:forEach>
                 </select>
             </div>
             <div id="specificFollowup" class="optionFollowup" style="display:none;text-align: center;">
                 <fmt:message key="each"/>&nbsp;<input type="text" style="width:20px;" name="periodicityCount" id="periodicityCount" class="campo number valid" autocomplete="off"><fmt:message key="days"/>
             </div>

             <div class="popButtons" style="margin-top:10px;">
                 <input type="button" class="boton" id="createDatesFollowup" value="<fmt:message key="create"/>" />
             </div>
         </form>
     </fieldset>
 </div>