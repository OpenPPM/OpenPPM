<%@page import="es.sm2.openppm.core.model.impl.Technology"%>
<%@page import="es.sm2.openppm.core.model.impl.ProjectTechnology"%>
<%@ page import="es.sm2.openppm.front.servlets.ProjectInitServlet" %>
<%@ page import="es.sm2.openppm.core.logic.security.actions.InitTabAction" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

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
  ~ File: technologies.inc.jsp
  ~ Create User: jordi.ripoll
  ~ Create Date: 30/06/2015 12:12:34
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_select" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="TECHNOLOGY"/></b></fmt:param>
</fmt:message>
<fmt:message key="yes" var="msgYes" />
<fmt:message key="no" var="msgNo" />
<fmt:message key="msg.confirm_delete" var="msgDelete">
	<fmt:param><fmt:message key="TECHNOLOGY"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleDelete">
	<fmt:param><fmt:message key="TECHNOLOGY"/></fmt:param>
</fmt:message>

<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
	<c:set var="buttonsProjectTechnologies"><img title="<fmt:message key="delete"/>" class="link removeTechnology" src="images/delete.jpg"></c:set>
</c:if>

<script type="text/javascript">

var projectTechnologiesTable, technologyPopup, searchTechnologyTable;

var technology = {
	add : function() {

        var params = {
            accion: "<%= InitTabAction.JX_SEARCH_TECHNOLOGY %>",
            idProject: $("#id").val()
        };

        initAjax.call(params, function (data) {

            searchTechnologyTable.fnClearTable();

            $.each(data, function(key, value) {

                var dataRow = [
                    value.<%=Technology.IDTECHNOLOGY%>,
                    value.<%=Technology.NAME%>,
                    value.<%=Technology.DESCRIPTION%>
                ];

                searchTechnologyTable.fnAddData(dataRow);
            });

            technologyPopup.dialog('open');
        });
	},
	save : function() {

        var selecteds = searchTechnologyTable.fnGetSelectedsData();

        var idsTechnology = '';

        $(selecteds).each(function() {
            idsTechnology += this[0] + ',';
        });

        var params = {
            accion: "<%= InitTabAction.JX_SAVE_PROJECT_TECHNOLOGIES %>",
            idProject: $("#id").val(),
            idsTechnology: idsTechnology
        };

        initAjax.call(params, function (data) {

            $(data.projTechnologies).each(function() {

                var dataRow = [
                    this.<%=ProjectTechnology.IDPROJECTTECHNOLOGY %>,
                    this.<%=Technology.IDTECHNOLOGY%>,
                    this.<%=Technology.NAME%>,
                    this.<%=Technology.DESCRIPTION%>,
                    '<nobr>${buttonsProjectTechnologies}</nobr>'
                ];

                projectTechnologiesTable.fnAddData(dataRow);
            });

            technologyPopup.dialog('close');
        });
	},
	remove : function() {

		var row = projectTechnologiesTable.fnGetData( this.parentNode.parentNode.parentNode );

		var params = {
			accion: "<%= InitTabAction.JX_DELETE_PROJECT_TECHNOLOGY %>",
			<%= ProjectTechnology.IDPROJECTTECHNOLOGY %>: row[0]
		};

		confirmUI('${msgTitleDelete}', '${msgDelete}', '${msgYes}', '${msgNo}', function() {

			initAjax.call(params, function (data) { projectTechnologiesTable.fnDeleteSelected(); });
		});
	},
	close: function() { technologyPopup.dialog('close'); }
};

readyMethods.add(function() {

	projectTechnologiesTable = $('#tb_project_technologies').dataTable({
		"oLanguage": datatable_language,
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": false,
		"aoColumns": [
			{ "bVisible": false },
			{ "bVisible": false },
			{ "bVisible": true },
			{ "bVisible": true },
			{ "sClass": "center", "bSortable": false, "bVisible": ${op:hasPermissionSetting(settings, user,project.status,tab)} }
		]
	});

    searchTechnologyTable = $('#tb_search_technology').dataTable({
        "sDom": 'T<"clear">lfrtip',
        "oLanguage": datatable_language,
        "sPaginationType": "full_numbers",
        "bFilter": false,
        "oTableTools": { "sRowSelect": "multi", "aButtons": [] },
        "iDisplayLength": 10,
        "aoColumns": [
            { "bVisible": false },
            { "sClass": "left" },
            { "sClass": "left"}
        ]
    });

	$('#tb_project_technologies tbody tr').live('click', function (event) {
		projectTechnologiesTable.fnSetSelectable(this,'selected_internal');
	});

	technologyPopup = $('#technologyPopup').dialog({
		autoOpen: false,
		modal: true,
		width: 700,
		resizable: false,
		open: function(event, ui) { }
	});

	$(".removeTechnology").live('click', technology.remove);
	$("#addTechnology").on('click', technology.add);
	$("#saveTechnology").on('click', technology.save);
	$("#closeTechnology").on('click', technology.close);
});
</script>

<table id="tb_project_technologies" class="tabledata" cellspacing="1" width="100%">
	<thead>
		<tr>
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th width="38%"><fmt:message key="name" /></th>
			<th width="46%"><fmt:message key="description" /></th>
			<th width="8%">
				<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
					<img id="addTechnology" title="<fmt:message key="add"/>" class="link" src="images/add.png">
				</c:if>
			</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="projectTechnology" items="${projectTechnologies}">
		<tr>
			<td>${projectTechnology.idProjectTechnology}</td>
			<td>${projectTechnology.technology.idTechnology}</td>
			<td>${tl:escape(projectTechnology.technology.name)}</td>
			<td>${tl:escape(projectTechnology.technology.description)}</td>
			<td>
				<c:if test="${op:hasPermissionSetting(settings, user,project.status,tab)}">
					<nobr>${buttonsProjectTechnologies }</nobr>
				</c:if>
			</td>
		</tr>
		</c:forEach>
	</tbody>
</table>
<div style="width: 100px;">&nbsp;</div>

<div id="technologyPopup" class="popup">
	<div id="technologyErrors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="technologyNumerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	<form name="frmTechnology" id="frmTechnology" action="<%=ProjectInitServlet.REFERENCE %>">
		<fieldset>
			<legend><fmt:message key="TECHNOLOGIES_MAP"/></legend>
            <table id="tb_search_technology" class="tabledata" cellspacing="1" width="100%">
                <thead>
                    <tr>
                        <th width="0%">&nbsp;</th>
                        <th width="40%"><fmt:message key="name" /></th>
                        <th width="60%"><fmt:message key="description" /></th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
		</fieldset>
		<div class="popButtons">
			<input type="button" class="boton" id="saveTechnology" value="<fmt:message key="add" />" />
			<input type="button" class="boton" id="closeTechnology" value="<fmt:message key="close" />" />
		</div>
	</form>
</div>