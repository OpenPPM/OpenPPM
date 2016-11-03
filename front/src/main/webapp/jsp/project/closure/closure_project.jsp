<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.logic.security.actions.ClosureTabAction"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectClosureServlet"%>
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
  ~ File: closure_project.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for confirmation --%>
<fmt:message key="msg.confirm" var="msgConfirm" />
<fmt:message key="msg.archive_project_confirm" var="msgArchiveProjectConfirm" />
<fmt:message key="yes" var="msgYes" />
<fmt:message key="no" var="msgNo" />

<script type="text/javascript">
<!--
var closureAjax = new AjaxCall('<%=ProjectClosureServlet.REFERENCE%>','<fmt:message key="error" />');
var closureValidator;

var closure = {

    archive : function() {
        confirmUI("${msgConfirm }", "${msgArchiveProjectConfirm }", "${msgYes }", "${msgNo }",
            function () {
                var f = document.forms["frm_close"];
                f.id.value = document.forms["frm_project"].id.value;
                f.accion.value = "<%=ClosureTabAction.ARCHIVE_PROJECT.getAction() %>";
                closeConfirmUI();
                loadingPopup();
                f.submit();
            });
    },
    load : function() {
        $('#archiveProject').click(closure.archive);
    }
};

function generateProjectClose() {
	var f = document.forms["frm_project"];
	f.action = "<%=ProjectClosureServlet.REFERENCE%>";
	f.accion.value = "<%=ProjectClosureServlet.VIEW_CLOSE%>";
	f.submit();
}

function saveClosure() {

	if (closureValidator.form()) {

		var f = document.forms["frm_project"];

		var params = {
			accion: '<%=ProjectClosureServlet.JX_SAVE_CLOSURE%>',
			id: $("#id").val(), // Project ID
			results: f.projectResults.value,
			goal: f.goalAchievement.value,
			lessons: f.lessonsLearned.value,
			canceled: $('#canceled').is(":checked"),
			dateCanceled: f.dateCanceled.value,
			commentCanceled: f.commentCanceled.value
		};

		closureAjax.call(params);
	}
}

readyMethods.add(function() {

    closure.load();

	$('div#close-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 500,
		minWidth: 500,
		resizable: false,
		open: function(event, ui) { closeValidator.resetForm(); }
	});

	closureValidator = $("#frm_project").validate({
		errorContainer: 'div#closure-errors',
		errorLabelContainer: 'div#closure-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#closure-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			lessonsLearned: {maxlength: 1500 },
			projectResults: {maxlength: 1500 },
			goalAchievement: {maxlength: 1500 },
            commentCanceled: {maxlength: 1000 },
            dateCanceled: {date : true}
		}
	});
});
//-->
</script>

<div id="projectTabs">

	<jsp:include page="../common/header_project.jsp">
		<jsp:param value="CL" name="actual_page"/>
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
			<input type="hidden" id="idDocument" name="idDocument" />
			<input type="hidden" name="scrollTop" />
            <input type="hidden" name="actualDate" id="actualDate" value="${actualDate}" />
			
			<%-- PROPERTY --%>
			<div style="display:<%=Settings.SHOW_PANEL_LESSONS_LEARNED?"block;":"none;" %>;">
				<%-- LESSONS LEARNED--%>
				<fmt:message key="closure.lessons_learned" var="titleLessonsLearned"/>
				<visual:panel id="fieldLessonsLearned" title="${titleLessonsLearned }">
					<table width="100%">
						<tr>
							<th class="left"><fmt:message key="closure.lessons_learned" /></th>						
						</tr>
						<tr>
							<td class="center">
								<textarea name="lessonsLearned" class="campo show_scroll" rows="8" style="width:97%;">${closure.lessonsLearned}</textarea>
							</td>						
						</tr>
					</table>
                    <c:if test="${op:hasPermission(user,project.status,tab)}">
                        <div align="right" style="margin-top:10px; margin-right: 15px;">
                            <a href="javascript:saveClosure();" class="boton"><fmt:message key="save" /></a>
                        </div>
                    </c:if>
				</visual:panel>
			 </div>

			<%-- CLOUSURE REPORT --%>
			<jsp:include page="closure_report.inc.jsp" flush="true"/>

			<%-- CLOSURE CHECK LIST --%>
			<jsp:include page="checklist/closure_check_list.inc.jsp" flush="true"/>

			<%-- PROBLEM CHECK LIST --%>
			<jsp:include page="problemlist/problem_check_list.inc.jsp" flush="true"/>

			<%-- DOCUMENT REPOSITORY --%>
			<fmt:message key="closure.document_repository" var="titleDocRepository"/>
			<visual:panel id="fieldClosureDoc" title="${titleDocRepository }">
				<jsp:include page="closure_document.inc.jsp" flush="true"/>
			</visual:panel>
		</form>
		
		<fmt:message var="documentationTitle" key="documentation.closure"/>
		<visual:panel title="${documentationTitle }">
			<jsp:include page="../common/project_documentation.jsp">
				<jsp:param name="documentationType" value="<%=Constants.DOCUMENT_CLOSURE %>"/>
			</jsp:include>
		</visual:panel>

    <c:if test="<%=(SettingUtil.getBoolean(request, GeneralSetting.ONLY_PMO_CHANGE_STATUS) && SecurityUtil.isUserInRole(request, Constants.ROLE_PMO)) ||
                            !SettingUtil.getBoolean(request, GeneralSetting.ONLY_PMO_CHANGE_STATUS)%>">
        <div align="right">
            <c:if test="<%=SecurityUtil.hasPermission(request, ClosureTabAction.ARCHIVE_PROJECT)%>">
                <button type="button" id="archiveProject" class="boton ui-special"><fmt:message key="project.button.archive"/></button>
            </c:if>
            <c:if test="<%=SecurityUtil.hasPermission(request, ClosureTabAction.CLOSE_PROJECT)%>">
                <a href="javascript:openCloseProject();" class="boton ui-special"><fmt:message key="close_project" /></a>
            </c:if>
        </div>
    </c:if>
        <div>&nbsp;</div>

	</div>
</div>

<%--<jsp:include page="edit_documentation_popup.jsp" flush="true"/>--%>
<jsp:include page="close_project_popup.jsp" flush="true" />
<jsp:include page="checklist/checklist_popup.jsp" flush="true" />
<jsp:include page="problemlist/problem_check_list_popup.jsp" flush="true" />