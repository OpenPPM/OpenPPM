<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectClosureServlet"%>
<%@ page import="es.sm2.openppm.front.servlets.ProjectControlServlet" %>
<%@ page import="es.sm2.openppm.front.servlets.ProjectInitServlet" %>
<%@ page import="es.sm2.openppm.front.servlets.ProjectLearnedLessonServlet" %>
<%@ page import="es.sm2.openppm.front.servlets.ProjectPlanServlet" %>
<%@ page import="es.sm2.openppm.front.servlets.ProjectProcurementServlet" %>
<%@ page import="es.sm2.openppm.front.servlets.ProjectRiskServlet" %>
<%@ page import="es.sm2.openppm.front.servlets.ProjectServlet" %>
<%@ page import="es.sm2.openppm.front.servlets.UtilServlet" %>
<%@ page import="es.sm2.openppm.front.utils.SecurityUtil" %>
<%@ page import="es.sm2.openppm.core.common.Settings" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

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
  ~ File: header_project.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:51
  --%>

<%-- Messages confirmation --%>
<fmt:message key="MSG.CONFIRM_RETURN_STATUS" var="msg_confirm_return_status"></fmt:message>
<fmt:message key="MSG.TITLE_CONFIRM_RETURN_STATUS" var="msg_title_return_status"></fmt:message>

<fmt:setLocale value="${locale}" scope="request"/>

<%
String actualPage = request.getParameter("actual_page"); 
Project project = (Project) request.getAttribute("project");

String disabledTabs = (Constants.STATUS_INITIATING.equals(project.getStatus()) ?"3,4,5":(Constants.STATUS_PLANNING.equals(project.getStatus()) ?"3,5":""));

if ("I".equals(actualPage)) { actualPage = "0"; }
else if ("P1".equals(actualPage)) { actualPage = "1"; }
else if ("R".equals(actualPage)) { actualPage = "2"; }
else if ("C".equals(actualPage)) { actualPage = "3"; }
else if ("PR".equals(actualPage)) { actualPage = "4"; }
else if ("CL".equals(actualPage)) { actualPage = "5"; }
else if ("LL".equals(actualPage)) { actualPage = "6"; }
%>

<c:set var="statusInvestmentRejected"><%= Constants.INVESTMENT_REJECTED %></c:set>
<c:set var="statusInvestmentInactivated"><%= Constants.INVESTMENT_INACTIVATED %></c:set>

<script type="text/javascript">
var projectAjax = new AjaxCall('<%=ProjectServlet.REFERENCE%>','<fmt:message key="error"/>');

function toTab(tab, element) {

	var $container = $(element).parent('li');

	if (!$container.hasClass('ui-state-disabled')) {

		var f = document.forms["frm_project"];

		switch(tab) {
			case 0: f.action = "<%=ProjectInitServlet.REFERENCE%>"; break;
			case 1: f.action = "<%=ProjectPlanServlet.REFERENCE%>"; break;
			case 2: f.action = "<%=ProjectRiskServlet.REFERENCE%>"; break;
			case 3: f.action = "<%=ProjectControlServlet.REFERENCE%>"; break;
			case 4: f.action = "<%=ProjectProcurementServlet.REFERENCE%>"; break;
			case 5: f.action = "<%=ProjectClosureServlet.REFERENCE%>"; break;
            case 6: f.action = "<%=ProjectLearnedLessonServlet.REFERENCE%>"; break;
		}

		f.accion.value = "";
		loadingPopup();
		f.scrollTop.value = $(document).scrollTop();
		f.submit();
	}
}
readyMethods.add(function() {
	$('#projectTabs').tabs({
		select: function(event, ui) { event.preventDefault(); },
		disabled: [ <%=disabledTabs%>],
		selected: <%=actualPage%>,
		show: function(event, ui) {
			$('#projectTabs ul.hide').removeClass('hide');
		}
	});
});
</script>

<c:choose>

	<c:when test="${op:isStringEquals(project.investmentStatus, statusInvestmentRejected)}">
		<div id="msg_project_closed" style="margin-bottom: 10px; padding: 0pt 0.7em;" class="ui-state-highlight ui-corner-all"> 
			<p>
				<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
				<strong><fmt:message key="msg.info"/>: </strong>
			</p>
			<p>
				<fmt:message key="msg.investment_rejected_info" />
			</p>
		</div>
	</c:when>
	
	<c:when test="${op:isStringEquals(project.investmentStatus, statusInvestmentInactivated)}">
		<div id="msg_project_closed" style="margin-bottom: 10px; padding: 0pt 0.7em;" class="ui-state-highlight ui-corner-all"> 
			<p>
				<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
				<strong><fmt:message key="msg.info"/>: </strong>
			</p>
			<p>
				<fmt:message key="msg.investment_inactivated_info" />
			</p>
		</div>
	</c:when>

	<c:when test="${op:isStringEquals(project.status, status_closed)}">
		<div id="msg_project_closed" style="margin-bottom: 10px; padding: 0pt 0.7em;" class="ui-state-highlight ui-corner-all"> 
		<p>
			<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
			<strong><fmt:message key="msg.info"/>: </strong>
		</p>
		<p>
			<fmt:message key="msg.project_closed_info" />&nbsp;
			<c:if test="<%=SecurityUtil.hasPermission(request, UtilServlet.JX_CHANGE_CLOSED_TO_CONTROL) %>">
				<fmt:message key="msg.info.change_previous_state"/>&nbsp;
				<a class="boton" href="javascript:changeState();"><fmt:message key="msg.info.click_here"/></a>
			</c:if>
		</p>
		</div>
		<c:if test="<%=SecurityUtil.hasPermission(request, UtilServlet.JX_CHANGE_CLOSED_TO_CONTROL) %>">
			<script>
			function changeState() {

                confirmUI(
                    '${msg_title_return_status}','${msg_confirm_return_status}',
                    '<fmt:message key="yes"/>', '<fmt:message key="no"/>',
                    function() {

                        var params = {
                            accion:'<%=UtilServlet.JX_CHANGE_CLOSED_TO_CONTROL%>',
                            idProject:'${project.idProject}'
                        };

                        utilAjax.call(params,function(data){
                            loadingPopup();
                            var f = document.frm_project;
                            f.action = "";
                            f.accion.value = "";
                            f.submit();
                        });
                });
			}
			</script>
		</c:if>
	</c:when>

    <c:when test="${op:isStringEquals(project.status, statusArchived)}">
		<div id="msg_project_closed" style="margin-bottom: 10px; padding: 0pt 0.7em;" class="ui-state-highlight ui-corner-all">
		<p>
			<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
			<strong><fmt:message key="msg.info"/>: </strong>
		</p>
		<p>
			<fmt:message key="msg.project_archived_info" />&nbsp;
			<c:if test="<%=SecurityUtil.hasPermission(request, UtilServlet.JX_CHANGE_ARCHIVED_TO_CLOSED) %>">
				<fmt:message key="msg.info.change_something"/>&nbsp;
				<button type="button" class="boton" id="changeClosed" ><fmt:message key="msg.info.click_here"/></button>
			</c:if>
		</p>
		</div>
		<c:if test="<%=SecurityUtil.hasPermission(request, UtilServlet.JX_CHANGE_ARCHIVED_TO_CLOSED) %>">
			<script>
            $('#changeClosed').click(function() {

                confirmUI(
                    '${msg_title_return_status}','${msg_confirm_return_status}',
                    '<fmt:message key="yes"/>', '<fmt:message key="no"/>',
                    function() {

                        var params = {
                            accion:'<%=UtilServlet.JX_CHANGE_ARCHIVED_TO_CLOSED%>',
                            idProject:'${project.idProject}'
                        };

                        utilAjax.call(params,function(data){
                            loadingPopup();
                            var f = document.frm_project;
                            f.action = "";
                            f.accion.value = "";
                            f.submit();
                        });
                });
			});
			</script>
		</c:if>
	</c:when>

</c:choose>


<ul class="hide">
	<li><a href="#initiationTab" onclick="toTab(0,this)"><fmt:message key="initiation"/></a></li>
	<li><a href="#planTab" onclick="toTab(1, this)"><fmt:message key="plan"/></a></li>
	<li><a href="#riskTab" onclick="toTab(2, this)"><fmt:message key="risk"/></a></li>
	<c:if test="${not op:isStringEquals(project.investmentStatus,statusInvestmentInactivated) && not op:isStringEquals(project.investmentStatus,statusInvestmentRejected)}">
		<li><a href="#controlTab" onclick="toTab(3, this)"><fmt:message key="control"/></a></li>
		<li><a href="#procuramentTab" onclick="toTab(4, this)"><fmt:message key="project_status.procurement"/></a></li>
		<li><a href="#closureTab" onclick="toTab(5, this)"><fmt:message key="closure"/></a></li>
	</c:if>
    <c:if test="<%=Settings.SHOW_LEARNED_LESSONS%>">
        <li><a href="#learnedLessonTab" onclick="toTab(6, this)"><fmt:message key="learned_lessons"/></a></li>
    </c:if>
</ul>