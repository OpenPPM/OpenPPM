<%@page import="es.sm2.openppm.core.model.impl.Jobcategory"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectactivity"%>
<%@page import="es.sm2.openppm.front.servlets.ResourceServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Teammember"%>
<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored ="false"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page import="es.sm2.openppm.front.utils.SettingUtil" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="es.sm2.openppm.core.common.Settings" %>

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
  ~ File: edit_teammember_popup.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="error" var="fmt_error" />
<fmt:message key="msg.error.date_before_date" var="fmt_activity_init_befire_out">
    <fmt:param><fmt:message key="activity"/>&nbsp;<fmt:message key="activity.end_date"/></fmt:param>
    <fmt:param><fmt:message key="team_member.date_out"/></fmt:param>
</fmt:message>

<fmt:message key="msg.error.date_before_date" var="fmt_activity_in_befire_end">
    <fmt:param><fmt:message key="team_member.date_in"/></fmt:param>
    <fmt:param><fmt:message key="activity"/>&nbsp;<fmt:message key="activity.init_date"/></fmt:param>
</fmt:message>

<fmt:message key="msg.error.date_before_date" var="fmt_activity_range_date">
    <fmt:param><fmt:message key="team_member.date_out"/></fmt:param>
    <fmt:param><fmt:message key="team_member.date_in"/></fmt:param>
</fmt:message>

<c:set var="rol_pmo" value="<%=Constants.ROLE_PMO %>"/>
<c:set var="rol_pm" value="<%=Constants.ROLE_PM %>"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtActivityRequired">
    <fmt:param><b><fmt:message key="activity"/></b></fmt:param>
</fmt:message>
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
    <fmt:param>0 - 100</fmt:param>
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

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<script type="text/javascript">
<!--
var resourceAjax    = new AjaxCall('<%=ResourceServlet.REFERENCE%>','<fmt:message key="error" />');
var planAjax        = new AjaxCall('<%=ProjectPlanServlet.REFERENCE%>','<fmt:message key="error" />');

var validatorMember;

var resource = {
    checkForRelease: function() {

        var f = document.frm_member;

        var params = {
            accion: '<%=ProjectPlanServlet.JX_CHECK_RELEASED_RESOURCE%>',
            idResource: f.idmember.value
        };
        planAjax.call(params, function(data) {

            if (data.showWarning) {
                confirmUI('<fmt:message key="resource.release"/>', '<fmt:message key="msg.warning.hour_app0"/>',
                        '<fmt:message key="yes"/>','<fmt:message key="no"/>',
                        releasedResource
                );
            }
            else {
                releasedResource();
            }
        });
    },
    messageValidateInMin : function() {
        return '${fmtMemberInDateAfterStart} ('+$('#activityMinDate').val()+')';
    },
    messageValidateInMax : function() {
        return '${fmtMemberInDateBeforeFinish} ('+$('#activityMaxDate').val()+')';
    },
    messageValidateOutMin : function() {
        return '${fmtMemberOutDateAfterStart} ('+$('#activityMinDate').val()+')';
    },
    messageValidateOutMax : function() {
        return '${fmtMemberOutDateBeforeFinish} ('+$('#activityMaxDate').val()+')';
    }
};
function chargeBTPop() {
    $('td#jobcategory div').bt({
        fill: '#F9FBFF',
        cssStyles: {color: '#343C4E', width: 'auto'},
        width: 250,
        padding: 10,
        cornerRadius: 5,
        spikeLength: 15,
        spikeGirth: 5,
        shadow: true,
        positions: 'left'
    });
}

function releasedResource() {
    var f = document.frm_member;

    var params = {
        accion: '<%=ProjectPlanServlet.JX_RELEASED_RESOURCE%>',
        idResource: f.idmember.value,
        commentsPm: $('#commentsPm').val()
    };
    planAjax.call(params, function(data) {

        if (data.success !== 'undefined') {
            var aData = teamTable.fnGetSelectedData();

            aData[4] = '<fmt:message key="resource.released"/>';

            teamTable.fnUpdateAndSelect(aData);

            closeTeamMember();
        }
    });
}
function editTeamMember(element) {

    var teamMember = teamTable.fnGetData( element.parentNode.parentNode );

    var params = {
        accion: '<%=ResourceServlet.JX_VIEW_RESOURCE%>',
        idResource: teamMember[0]
    };
    resourceAjax.call(params, function(data) {

        var f = document.frm_member;
        f.reset();

        f.idmember.value		= teamMember[0];
        $('#tm_fullname').text(data.<%=Contact.FULLNAME%>);
        $('#jobcategory').html(data.<%=Teammember.JOBCATEGORY%>);
        f.member_fte.value		= data.<%=Teammember.FTE%>;
        f.employee_id.value 	= data.<%=Employee.IDEMPLOYEE%>;
        f.idActivity.value 		= data.<%=Projectactivity.IDACTIVITY%>;
        f.<%=Teammember.SELLRATE%>.value = toCurrency(data.<%=Teammember.SELLRATE%>);
        $('#<%=Contact.BUSINESSPHONE%>').text(data.<%=Contact.BUSINESSPHONE%>);
        $('#<%=Contact.MOBILEPHONE%>').text(data.<%=Contact.MOBILEPHONE%>);
        $('#<%=Contact.EMAIL%>').text(data.<%=Contact.EMAIL%>);
        $('#<%=Employee.COSTRATE%>').text(data.<%=Employee.COSTRATE%>);
        $('#<%=Jobcategory.IDJOBCATEGORY%>').html(optionsJobs(data.jobArray));
        $('#<%=Jobcategory.IDJOBCATEGORY%>').val(data.<%=Jobcategory.IDJOBCATEGORY%>);
        $("#commentsPm").val(data.<%=Teammember.COMMENTSPM%>);


        var commentsRm = data.<%=Teammember.COMMENTSRM%>;

        if (typeof commentsRm === 'undefined' || commentsRm == '' || commentsRm == null) {
            $('#commentsForRM').hide();
        }
        else {
            $('#commentsForRM').show();
            $('#commentsRm').html(commentsRm);
        }

        $('#btnPreassigned, #btnReleased, #btnUpdate').hide();

        if (data.statusVal == '<%=Constants.RESOURCE_ASSIGNED%>') {
            $('#btnReleased, #btnUpdate').show();
        }
        else if (data.statusVal == '<%=Constants.RESOURCE_PROPOSED%>' ||
                data.statusVal == '<%=Constants.RESOURCE_PRE_ASSIGNED%>') {
            $('#btnPreassigned, #btnReleased').show();
        }
        else if (data.statusVal == '<%=Constants.RESOURCE_TURNED_DOWN%>') {
            $('#btnReleased').show();
        }

        changeActivityDates();
        chargeBTPop();

        $("#member_datein").val(data.<%=Teammember.DATEIN%>)

        $("#member_dateout").datepicker("option", "minDate", $('#member_datein').val());
        $("#member_dateout").val(data.<%=Teammember.DATEOUT%>)


        if (data.statusVal == '<%=Constants.RESOURCE_ASSIGNED%>') {
            $('#idActivity').attr('disabled', true);
            $('#<%=Jobcategory.IDJOBCATEGORY%>').attr('disabled', true);
            $('#<%=Teammember.SELLRATE %>').attr('disabled', true);
            $('#member_fte').attr('disabled', <%=SettingUtil.getBoolean(settings, Settings.SettingType.PRELOAD_HOURS_WITH_WORKLOAD)%>);
        }
        else {
            $('#idActivity').attr('disabled', false);
            $('#<%=Jobcategory.IDJOBCATEGORY%>').attr('disabled', false);
            $('#<%=Teammember.SELLRATE %>').attr('disabled', false);
            $('#member_fte').attr('disabled', false);
        }

        showTeamMemberPopup();
    });
}

function showTeamMemberPopup() { $('#member-popup').dialog('open'); }

function preasignedResource() {

    if (validatorMember.form()) {

        var f = document.frm_member;

        id_member = f.idmember.value;

        planAjax.call({
            accion: "<%=ProjectPlanServlet.JX_SAVE_MEMBER%>",
            idActivity: f.idActivity.value,
            id: document.forms['frm_project'].id.value,
            plannedFinishDate: $('#plannedFinishDate').html(),
            member_id: f.idmember.value,
            member_fte: f.member_fte.value,
            member_datein: f.member_datein.value,
            member_dateout: f.member_dateout.value,
            employee_id: f.employee_id.value,
            <%=Teammember.SELLRATE%>: f.<%=Teammember.SELLRATE%>.value,
            <%=Jobcategory.IDJOBCATEGORY%>: f.<%=Jobcategory.IDJOBCATEGORY%>.value,
            commentsPm: $("#commentsPm").val()
        }, function (data) {

            var dataRow = [
                data.id,
                escape(data.contact_fullname),
                escape(data.activity),
                data.fte+'%',
                escape(data.status),
                escape(data.performing_org),
                data.date_in,
                data.date_out,
                f.<%=Teammember.SELLRATE%>.value,
                '<img onclick="editTeamMember(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">',
                false,
                data.isExpiredEndDate
            ];

            if (id_member == '') { teamTable.fnAddDataAndSelect(dataRow); }
            else { teamTable.fnUpdateAndSelect(dataRow); }

            f.reset();

            $('#member-popup').dialog('close');
        });
    }
}

function updateResource() {

    if (validatorMember.form()) {

        var f = document.frm_member;

        id_member = f.idmember.value;

        planAjax.call({
            accion: "<%=ProjectPlanServlet.JX_UPDATE_MEMBER%>",
            id: document.forms['frm_project'].id.value,
            member_id: f.idmember.value,
            member_datein: f.member_datein.value,
            member_dateout: f.member_dateout.value,
            member_fte: f.member_fte.value
        }, function (data) {

            var dataRow = [
                data.id,
                escape(data.contact_fullname),
                escape(data.activity),
                data.fte+'%',
                escape(data.status),
                escape(data.performing_org),
                data.date_in,
                data.date_out,
                f.<%=Teammember.SELLRATE%>.value,
                '<img onclick="editTeamMember(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">',
                false,
                data.isExpiredEndDate
            ];

            if (id_member == '') { teamTable.fnAddDataAndSelect(dataRow); }
            else { teamTable.fnUpdateAndSelect(dataRow); }

            f.reset();

            $('#member-popup').dialog('close');
        });
    }
}

function changeActivityDates() {

    var idActivity = $("#idActivity").val();

    if (idActivity == -1) {
        $("#act_planInitDate").html("");
        $("#act_planEndDate").html("");
        $("#member_datein").val("");
        $("#member_dateout").val("");
        $("#activityMinDate").val("");
        $("#activityMaxDate").val("");
    }
    else {
        $("#act_planInitDate").html($("#planInitDate" + idActivity).val());
        $("#act_planEndDate").html($("#planEndDate" + idActivity).val());
        $("#member_datein").val($("#planInitDate" + idActivity).val());
        $("#member_dateout").val($("#planEndDate" + idActivity).val());
        $("#activityMinDate").val($("#planInitDate" + idActivity).val());
        $("#activityMaxDate").val($("#planEndDate" + idActivity).val());
    }
}

function closeTeamMember() {
    $('div#member-popup').dialog('close');
}

// Load operation table
function loadOperations() {

    var f = document.frm_member;

    if ($("#act_planInitDate").html() != "" && $("#act_planEndDate").html() != "") {

        var params = {
            accion:         '<%= ProjectPlanServlet.JX_CONSULT_OPERATIONS %>',
            idEmployee:     f.employee_id.value,
            datein:  $("#act_planInitDate").html(),
            dateout: $("#act_planEndDate").html()
        };

        planAjax.call(params, function(data) {

            $('#operationsTable').html(data);

            $('#operationField').show();
        },'html');
    }
    else {
        $('#operationField').hide();
    }
}

readyMethods.add(function() {

    $('div#member-popup').dialog({
        autoOpen: false,
        modal: true,
        width: 800,
        minWidth: 700,
        resizable: false,
        open: function(event, ui) {

            validatorMember.resetForm();

            loadOperations();
        }
    });

    validatorMember = $("#frm_member").validate({
        errorContainer: 'div#member-errors',
        errorLabelContainer: 'div#member-errors ol',
        wrapper: 'li',
        showErrors: function(errorMap, errorList) {
            $('span#member-numerrors').html(this.numberOfInvalids());
            this.defaultShowErrors();
        },
        rules: {
            member_fte: { range: [0,100] },
            member_datein: {
                required: true,
                date: true,
                minDateTo: '#activityMaxDate',
                maxDateTo: '#activityMinDate'
            },
            member_dateout: {
                required: true,
                date: true,
                minDateTo: '#activityMaxDate',
                maxDateTo: '#activityMinDate'
            },
            idActivity: { act_cheked: true }
        },
        messages: {
            member_fte: { range: '${fmtFteOutOfRange}' },
            member_datein: {
                required: '${fmtInDateRequired}',
                date: '${fmtInDateFormat}',
                minDateTo: resource.messageValidateInMax,
                maxDateTo: resource.messageValidateInMin
            },
            member_dateout: {
                required: '${fmtOutDateRequired}',
                date: '${fmtOutDateFormat}',
                minDateTo: resource.messageValidateOutMax,
                maxDateTo: resource.messageValidateOutMin
            },
            idActivity: { act_cheked: '${fmtActivityRequired}' }
        }
    });

    $.validator.addMethod("act_cheked", function(value, element) {
        if(value == -1) {
            return false;
        }
        return true;
    });

    $("#idActivity").change( function() {
        changeActivityDates();
    });

    $("#idActivity").on("change", function(){
        loadOperations();
    });

});
//-->
</script>

<c:if test="${op:hasPermission(user,project.status,tab)}">
    <script>
        readyMethods.add(function() {
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
        });
    </script>
</c:if>

<div id="member-popup" class="popup">

    <div id="member-errors" class="ui-state-error ui-corner-all hide">
        <p>
            <span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
            <strong><fmt:message key="msg.error_title"/></strong>
            &nbsp;(<b><span id="member-numerrors"></span></b>)
        </p>
        <ol></ol>
    </div>

    <form name="frm_member" id="frm_member" action="<%=ProjectPlanServlet.REFERENCE%>" >
        <input type="hidden" name="idmember" value="" />
        <input type="hidden" name="employee_id" />
        <input type="hidden" id="activityMinDate" />
        <input type="hidden" id="activityMaxDate" />

        <fieldset>
            <legend><fmt:message key="contact"/></legend>
            <table width="100%" cellpadding="4">
                <tr>
                    <th width="33%"><fmt:message key="contact.fullname"/></th>
                    <th width="33%"><fmt:message key="maintenance.resource.cost_rate"/></th>
                    <th width="33%"><fmt:message key="job_category"/></th>
                </tr>
                <tr>
                    <td class="center" id="tm_fullname"></td>
                    <td class="center" id="<%=Employee.COSTRATE%>"></td>
                    <td class="center" id="jobcategory" colspan="2"></td>
                </tr>
                <tr>
                    <th width="33%"><fmt:message key="maintenance.contact.business_phone"/></th>
                    <th width="33%"><fmt:message key="maintenance.contact.mobile_phone"/></th>
                    <th width="33%"><fmt:message key="maintenance.contact.email"/></th>
                </tr>
                <tr>
                    <td class="center" id="<%=Contact.BUSINESSPHONE%>"></td>
                    <td class="center" id="<%=Contact.MOBILEPHONE%>"></td>
                    <td class="center" id="<%=Contact.EMAIL%>"></td>
                </tr>
            </table>
        </fieldset>
        <div>&nbsp;</div>
        <div id="commentsForRM">
            <fieldset>
                <legend><fmt:message key="comments.rm"/></legend>
                <div id="commentsRm"></div>
            </fieldset>
            <div>&nbsp;</div>
        </div>
        <fieldset>
            <legend><fmt:message key="edit_resource"/></legend>
            <table id="tb_team_pop" width="100%" border="0" cellpadding="4">
                <tr>
                    <th class="center" width="60%" colspan="2"><fmt:message key="activity"/>&nbsp;*</th>
                    <th class="center" width="40%" colspan="2"><fmt:message key="job_category"/></th>
                </tr>
                <tr>
                    <td colspan="2">
                        <select id="idActivity" name="idActivity" class="campo">
                            <option value="-1"><fmt:message key="select_opt"/></option>
                            <c:forEach var="activity" items="${activities}">
                                <c:if test="${((not empty activity.firstDate) and (not empty activity.lastDate) and activity.wbsnode.isControlAccount) }">
                                    <option value="${activity.idActivity }">${activity.activityName }</option>
                                </c:if>
                            </c:forEach>
                        </select>
                        <c:forEach var="activity" items="${activities }">
                            <c:if test="${((not empty activity.firstDate) and (not empty activity.lastDate)) }">
                                <input type="hidden" id="planInitDate${activity.idActivity }"
                                       value="<fmt:formatDate value="${activity.firstDate}" pattern="${datePattern}"/>" />
                                <input type="hidden" id="planEndDate${activity.idActivity }"
                                       value="<fmt:formatDate value="${activity.lastDate}" pattern="${datePattern}"/>" />
                            </c:if>
                        </c:forEach>
                    </td>
                    <td colspan="2">
                        <select id="<%=Jobcategory.IDJOBCATEGORY%>" name="<%=Jobcategory.IDJOBCATEGORY%>" class="campo">
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="center" width="33%"><fmt:message key="activity.init_date"/></th>
                    <th class="center" colspan="2"><fmt:message key="activity.end_date"/></th>
                    <th class="center" width="34%"><fmt:message key="team_member.sell_rate"/></th>
                </tr>
                <tr>
                    <td class="center" id="act_planInitDate"></td>
                    <td class="center" id="act_planEndDate" colspan="2"></td>
                    <td class="center">
                        <input type="text" name="<%=Teammember.SELLRATE %>" id="<%=Teammember.SELLRATE %>" class="campo importe" style="width: 40px;"/>
                    </td>
                </tr>
                <tr>
                    <th class="center"><fmt:message key="team_member.fte"/></th>
                    <th class="center" colspan="2"><fmt:message key="team_member.date_in"/>&nbsp;*</th>
                    <th class="center"><fmt:message key="team_member.date_out"/>&nbsp;*</th>
                </tr>
                <tr>
                    <td class="center">
                        <input type="text" id="member_fte" name="member_fte" class="campo right" style="width:34px !important;"/>
                    </td>
                    <td align="center" colspan="2">
                        <input type="text" id="member_datein" name="member_datein" class="campo fecha" />
                    </td>
                    <td align="center">
                        <input type="text" id="member_dateout" name="member_dateout" class="campo fecha" />
                    </td>
                </tr>
                <tr>
                    <th colspan="4"><fmt:message key="comments"/></th>
                </tr>
                <tr>
                    <td colspan="4"><textarea style="width: 100%" class="campo" name="commentsPm" id="commentsPm"></textarea></td>
                </tr>
            </table>
        </fieldset>

        <div>&nbsp;</div>

        <%-- Operations --%>
        <div id="operationField" style="display:none;">
            <fieldset>
                <legend><fmt:message key="timesheet.operation"/></legend>
                <div id="operationsTable"></div>
            </fieldset>
        </div>

        <div class="popButtons">
            <c:if test="${op:hasPermission(user,project.status,tab)}">
                <a href="javascript:preasignedResource();" class="boton" id="btnPreassigned"><fmt:message key="resource.preassign"/></a>
                <a href="javascript:updateResource();" class="boton" id="btnUpdate"><fmt:message key="resource.update"/></a>
                <a href="javascript:resource.checkForRelease();" class="boton" id="btnReleased"><fmt:message key="resource.release"/></a>
            </c:if>
            <a href="javascript:closeTeamMember();" class="boton"><fmt:message key="close"/></a>
        </div>
    </form>
</div>