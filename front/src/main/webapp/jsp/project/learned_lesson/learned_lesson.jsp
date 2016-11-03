<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@ page import="es.sm2.openppm.front.utils.SecurityUtil" %>
<%@ page import="es.sm2.openppm.front.utils.SettingUtil" %>
<%@ page import="es.sm2.openppm.core.logic.setting.GeneralSetting" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Visual" prefix="visual" %>
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
  ~ File: learned_lesson.jsp
  ~ Create User: jordi.ripoll
  ~ Create Date: 09/09/2015 09:15:11
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for confirmation --%>
<fmt:message key="yes" var="msgYes" />
<fmt:message key="no" var="msgNo" />
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_used">
    <fmt:param><fmt:message key="VIEW.LEARNED_LESSON.USED"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_used">
    <fmt:param><fmt:message key="VIEW.LEARNED_LESSON.USED"/></fmt:param>
</fmt:message>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_generated">
    <fmt:param><fmt:message key="VIEW.LEARNED_LESSON.GENERATED"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_generated">
    <fmt:param><fmt:message key="VIEW.LEARNED_LESSON.GENERATED"/></fmt:param>
</fmt:message>

<fmt:message key="VIEW.LEARNED_LESSON.WARNING.LINKED" var="msg_warning_delete">
    <fmt:param><span class="learned-ranking"></span></fmt:param>
</fmt:message>
<%-- Constants --%>
<c:set var="lessonTypeOpportunity"><%= Constants.LessonType.OPPORTUNITY%></c:set>
<c:set var="lessonTypeThreat"><%= Constants.LessonType.THREAT %></c:set>

<c:set var="lessonProcessGroupClosure"><%= Constants.LessonProcessGroup.CLOSURE %></c:set>
<c:set var="lessonProcessGroupExecution"><%= Constants.LessonProcessGroup.EXECUTION %></c:set>
<c:set var="lessonProcessGroupInitiation"><%= Constants.LessonProcessGroup.INITIATION %></c:set>
<c:set var="lessonProcessGroupMonitoring"><%= Constants.LessonProcessGroup.MONITORING_AND_CONTROL %></c:set>
<c:set var="lessonProcessGroupPlanning"><%= Constants.LessonProcessGroup.PLANNING %></c:set>

<script type="text/javascript">
<!--

var tableGenerateds;
var tableUseds;

var used = {

    remove: function(element) {

        confirmUI(
            '${msg_title_confirm_delete_used}','${msg_confirm_delete_used}',
            '<fmt:message key="yes"/>', '<fmt:message key="no"/>',
            function() {

                var row = tableUseds.fnGetData(element.parentNode.parentNode);

                $.ajax({
                    url: 'rest/project/'+ $("#id").val() +'/learnedlesson/' + row[0],
                    type: 'DELETE',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: {},
                    success: function () {
                        tableUseds.fnDeleteSelected();
                    },
                    error: function (jqXHR) {

                        if (typeof jqXHR.responseText !== 'undefined' && jqXHR.responseText.length > 0) {
                            alertUI("Error", JSON.parse(jqXHR.responseText).message);
                        }
                    }
                });
            });
    }
};

var generated = {

    remove: function(element) {

        var row = tableGenerateds.fnGetData(element.parentNode.parentNode);

        // Message warning
        //
        var confirmDelete =
                '<div class="alert alert-warning">'+
                    '<label>'+
                        '${msg_warning_delete}'+
                    '</label>'+
                '</div>';

        var message = '${msg_confirm_delete_generated}';

        if (row[8] !== "0") {

            message = message + confirmDelete;

            message = message.replace('<span class="learned-ranking"></span>', row[8]);
        }


        confirmUI(
                '${msg_title_confirm_delete_generated}', message,
                '<fmt:message key="yes"/>', '<fmt:message key="no"/>',
                function() {

                    $.ajax({
                        url: 'rest/learnedlesson/' + row[0],
                        type: 'DELETE',
                        dataType: 'json',
                        contentType: 'application/json',
                        data: {},
                        success: function () {
                            tableGenerateds.fnDeleteSelected();
                        },
                        error: function (jqXHR) {

                            if (typeof jqXHR.responseText !== 'undefined' && jqXHR.responseText.length > 0) {
                                alertUI("Error", JSON.parse(jqXHR.responseText).message);
                            }
                        }
                    });
                });
    }
};

readyMethods.add(function() {

    tableGenerateds = $('#tableGenerateds').dataTable({
        "oLanguage": datatable_language,
        "bInfo": false,
        "bAutoWidth": false,
        "sPaginationType": "full_numbers",
        "aoColumns": [
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "center"},
            { "sClass": "center"},
            { "sClass": "center"},
            { "sClass": "center", "bSortable": false}
        ]
    });

    $('#tableGenerateds tbody tr').live('click', function (event) {
        tableGenerateds.fnSetSelectable(this,'selected_internal');
    });

    $('#tableGenerateds .remove').live('click', function () {
        generated.remove(this);
    });

    tableUseds = $('#tableUseds').dataTable({
        "oLanguage": datatable_language,
        "bInfo": false,
        "bAutoWidth": false,
        "sPaginationType": "full_numbers",
        "aoColumns": [
            { "bVisible": false },
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "left"},
            { "sClass": "center"},
            { "sClass": "center"},
            { "sClass": "center"},
            { "sClass": "center", "bSortable": false}
        ]
    });

    $('#tableUseds tbody tr').live('click', function (event) {
        tableUseds.fnSetSelectable(this,'selected_internal');
    });

    $('#tableUseds .remove').live('click', function () {
        used.remove(this);
    });

});
//-->
</script>

<div id="projectTabs">

	<jsp:include page="../common/header_project.jsp">
		<jsp:param value="LL" name="actual_page"/>
	</jsp:include>

    <%-- INFORMATION OF PROJECT --%>
    <jsp:include page="../common/info_project.jsp" flush="true" />

	<div id="panels" style="padding: 15px;">
		
		<div id="closure-errors" class="ui-state-error ui-corner-all hide">
			<p>
				<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
				<strong><fmt:message key="msg.error_title"/></strong>
				&nbsp;(<b><span id="closure-numerrors"></span></b>)
			</p>
			<ol></ol>
		</div>
	
		<form name="frm_project" id="frm_project" method="post" accept-charset="UTF-8">
			<input type="hidden" name="accion" value="" />
			<input type="hidden" name="id" id="id" value="${project.idProject}" />
			<input type="hidden" name="status" value="${project.status}" />

            <%-- GENERATEDS --%>
            <fmt:message key="VIEW.LEARNED_LESSON.GENERATED" var="titleGenerateds"/>
            <visual:panel id="panelGenerateds" title="${titleGenerateds}">
                <table id="tableGenerateds" class="tabledata" cellspacing="1" width="100%">
                    <thead>
                        <tr>
                            <th width="5%"><fmt:message key="VIEW.LEARNED_LESSON.ID_LLAA"></fmt:message></th>
                            <th width="34%"><fmt:message key="VIEW.LEARNED_LESSON.SUBJECT"></fmt:message></th>
                            <th width="15%"><fmt:message key="owner"></fmt:message></th>
                            <th width="15%"><fmt:message key="PROCESS_GROUP.NAME"></fmt:message></th>
                            <th width="15%"><fmt:message key="KNOWLEDGE_AREA"></fmt:message></th>
                            <th width="5%"><fmt:message key="type"></fmt:message></th>
                            <th width="2%"><fmt:message key="VIEW.LEARNED_LESSON.ACTIONS"></fmt:message></th>
                            <th width="2%"><fmt:message key="VIEW.LEARNED_LESSON.RECOMMENDATIONS"></fmt:message></th>
                            <th width="2%"><fmt:message key="VIEW.LEARNED_LESSON.RANKING"></fmt:message></th>
                            <th width="5%">
                                <a href="static/index.html#/learned-lesson/detail/?toProject&codeProject=${project.idProject}">
                                    <img title="<fmt:message key="add"/>" class="link" src="images/add.png">
                                </a>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="generate" items="${generateds}">
                        <tr>
                            <td>${generate.learnedLesson.idLearnedLesson}</td>
                            <td>${generate.learnedLesson.name}</td>
                            <td>${generate.learnedLesson.contact.fullName}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${op:isStringEquals(generate.learnedLesson.processGroup, lessonProcessGroupClosure)}">
                                        <fmt:message key="PROCESS_GROUP.CLOSURE"></fmt:message>
                                    </c:when>
                                    <c:when test="${op:isStringEquals(generate.learnedLesson.processGroup, lessonProcessGroupExecution)}">
                                        <fmt:message key="PROCESS_GROUP.EXECUTION"></fmt:message>
                                    </c:when>
                                    <c:when test="${op:isStringEquals(generate.learnedLesson.processGroup, lessonProcessGroupInitiation)}">
                                        <fmt:message key="PROCESS_GROUP.INITIATION"></fmt:message>
                                    </c:when>
                                    <c:when test="${op:isStringEquals(generate.learnedLesson.processGroup, lessonProcessGroupMonitoring)}">
                                        <fmt:message key="PROCESS_GROUP.MONITORING_AND_CONTROL"></fmt:message>
                                    </c:when>
                                    <c:when test="${op:isStringEquals(generate.learnedLesson.processGroup, lessonProcessGroupPlanning)}">
                                        <fmt:message key="PROCESS_GROUP.PLANNING"></fmt:message>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>${generate.learnedLesson.knowledgeArea.name}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${op:isStringEquals(generate.learnedLesson.type, lessonTypeOpportunity)}">
                                        <fmt:message key="TYPE.OPPORTUNITY"></fmt:message>
                                    </c:when>
                                    <c:when test="${op:isStringEquals(generate.learnedLesson.type, lessonTypeThreat)}">
                                        <fmt:message key="TYPE.THREAT"></fmt:message>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>${generate.learnedLesson.actionsLesson.size()}</td>
                            <td>${generate.learnedLesson.recsLesson.size()}</td>
                            <td>${generate.learnedLesson.learnedLessonLinks.size()}</td>
                            <td>
                                <a href="static/index.html#/learned-lesson/detail/${generate.learnedLesson.idLearnedLesson}?toProject&codeProject=${project.idProject}" style="text-decoration: inherit;">
                                    <img title="<fmt:message key="view"/>" class="link view" src="images/view.png">
                                </a>
                                &nbsp;
                                <img title="<fmt:message key="remove"/>" class="link remove" src="images/delete.png">
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </visual:panel>

            <%-- USEDS --%>
            <fmt:message key="VIEW.LEARNED_LESSON.USED" var="titleUseds"/>
            <visual:panel id="panelUseds" title="${titleUseds}">
                <table id="tableUseds" class="tabledata" cellspacing="1" width="100%">
                    <thead>
                        <tr>
                            <th width="0%"></th>
                            <th width="3%"><fmt:message key="VIEW.PROJECT.ID_PROJECT"></fmt:message></th>
                            <th width="15%"><fmt:message key="VIEW.PROJECT.PROJECT_OR_DOMAIN"></fmt:message></th>
                            <th width="5%"><fmt:message key="VIEW.LEARNED_LESSON.ID_LLAA"></fmt:message></th>
                            <th width="25%"><fmt:message key="VIEW.LEARNED_LESSON.SUBJECT"></fmt:message></th>
                            <th width="10%"><fmt:message key="owner"></fmt:message></th>
                            <th width="10%"><fmt:message key="PROCESS_GROUP.NAME"></fmt:message></th>
                            <th width="19%"><fmt:message key="VIEW.LEARNED_LESSON.KNOWLEDGE_AREA"></fmt:message></th>
                            <th width="5%"><fmt:message key="VIEW.LEARNED_LESSON.TABLE.TYPE"></fmt:message></th>
                            <th width="1%"><fmt:message key="VIEW.LEARNED_LESSON.ACTIONS"></fmt:message></th>
                            <th width="1%"><fmt:message key="VIEW.LEARNED_LESSON.RECOMMENDATIONS"></fmt:message></th>
                            <th width="1%"><fmt:message key="VIEW.LEARNED_LESSON.RANKING"></fmt:message></th>
                            <th width="5%"></th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="used" items="${useds}">
                        <tr>
                            <c:choose>
                            <c:when test="${empty used.learnedLesson.learnedLessonProjects}">
                                <td>${used.idLearnedLessonLink}</td>
                                <td></td>
                                <td>Global</td>
                                <td>${used.learnedLesson.idLearnedLesson}</td>
                                <td>${used.learnedLesson.name}</td>
                                <td>${used.learnedLesson.contact.fullName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${op:isStringEquals(used.learnedLesson.processGroup, lessonProcessGroupClosure)}">
                                            <fmt:message key="PROCESS_GROUP.CLOSURE"></fmt:message>
                                        </c:when>
                                        <c:when test="${op:isStringEquals(used.learnedLesson.processGroup, lessonProcessGroupExecution)}">
                                            <fmt:message key="PROCESS_GROUP.EXECUTION"></fmt:message>
                                        </c:when>
                                        <c:when test="${op:isStringEquals(used.learnedLesson.processGroup, lessonProcessGroupInitiation)}">
                                            <fmt:message key="PROCESS_GROUP.INITIATION"></fmt:message>
                                        </c:when>
                                        <c:when test="${op:isStringEquals(used.learnedLesson.processGroup, lessonProcessGroupMonitoring)}">
                                            <fmt:message key="PROCESS_GROUP.MONITORING_AND_CONTROL"></fmt:message>
                                        </c:when>
                                        <c:when test="${op:isStringEquals(used.learnedLesson.processGroup, lessonProcessGroupPlanning)}">
                                            <fmt:message key="PROCESS_GROUP.PLANNING"></fmt:message>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>${used.learnedLesson.knowledgeArea.name}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${op:isStringEquals(used.learnedLesson.type, lessonTypeOpportunity)}">
                                            <fmt:message key="TYPE.OPPORTUNITY"></fmt:message>
                                        </c:when>
                                        <c:when test="${op:isStringEquals(used.learnedLesson.type, lessonTypeThreat)}">
                                            <fmt:message key="TYPE.THREAT"></fmt:message>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>${used.learnedLesson.actionsLesson.size()}</td>
                                <td>${used.learnedLesson.recsLesson.size()}</td>
                                <td>${used.learnedLesson.learnedLessonLinks.size()}</td>
                                <td>
                                    <a href="static/index.html#/learned-lesson/detail/${used.learnedLesson.idLearnedLesson}?toProject&codeProject=${project.idProject}&notEditable" style="text-decoration: inherit;">
                                        <img title="<fmt:message key="view"/>" class="link view" src="images/view.png">
                                    </a>
                                    &nbsp;
                                    <img title="<fmt:message key="remove"/>" class="link remove" src="images/delete.png">
                                </td>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="learnedLessonProject" items="${used.learnedLesson.learnedLessonProjects}">
                                    <td>${used.idLearnedLessonLink}</td>
                                    <td>${learnedLessonProject.project.idProject}</td>
                                    <td>${learnedLessonProject.project.projectName}</td>
                                    <td>${used.learnedLesson.idLearnedLesson}</td>
                                    <td>${used.learnedLesson.name}</td>
                                    <td>${used.learnedLesson.contact.fullName}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${op:isStringEquals(used.learnedLesson.processGroup, lessonProcessGroupClosure)}">
                                                <fmt:message key="PROCESS_GROUP.CLOSURE"></fmt:message>
                                            </c:when>
                                            <c:when test="${op:isStringEquals(used.learnedLesson.processGroup, lessonProcessGroupExecution)}">
                                                <fmt:message key="PROCESS_GROUP.EXECUTION"></fmt:message>
                                            </c:when>
                                            <c:when test="${op:isStringEquals(used.learnedLesson.processGroup, lessonProcessGroupInitiation)}">
                                                <fmt:message key="PROCESS_GROUP.INITIATION"></fmt:message>
                                            </c:when>
                                            <c:when test="${op:isStringEquals(used.learnedLesson.processGroup, lessonProcessGroupMonitoring)}">
                                                <fmt:message key="PROCESS_GROUP.MONITORING_AND_CONTROL"></fmt:message>
                                            </c:when>
                                            <c:when test="${op:isStringEquals(used.learnedLesson.processGroup, lessonProcessGroupPlanning)}">
                                                <fmt:message key="PROCESS_GROUP.PLANNING"></fmt:message>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>${used.learnedLesson.knowledgeArea.name}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${op:isStringEquals(used.learnedLesson.type, lessonTypeOpportunity)}">
                                                <fmt:message key="TYPE.OPPORTUNITY"></fmt:message>
                                            </c:when>
                                            <c:when test="${op:isStringEquals(used.learnedLesson.type, lessonTypeThreat)}">
                                                <fmt:message key="TYPE.THREAT"></fmt:message>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                    <td>${used.learnedLesson.actionsLesson.size()}</td>
                                    <td>${used.learnedLesson.recsLesson.size()}</td>
                                    <td>${used.learnedLesson.learnedLessonLinks.size()}</td>
                                    <td>
                                        <a href="static/index.html#/learned-lesson/detail/${used.learnedLesson.idLearnedLesson}?toProject&codeProject=${project.idProject}&notEditable" style="text-decoration: inherit;">
                                            <img title="<fmt:message key="view"/>" class="link view" src="images/view.png">
                                        </a>
                                        &nbsp;
                                        <img title="<fmt:message key="remove"/>" class="link remove" src="images/delete.png">
                                    </td>
                                </c:forEach>
                            </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </visual:panel>

		</form>

        <div>&nbsp;</div>

	</div>
</div>
