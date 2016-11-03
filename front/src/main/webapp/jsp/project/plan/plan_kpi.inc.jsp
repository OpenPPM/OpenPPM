<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
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
  ~ File: plan_kpi.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script language="javascript" type="text/javascript" >
<!--
var kpiTable;

readyMethods.add(function () {

	kpiTable = $('#tb_kpi').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"iDisplayLength": 50,
		"bAutoWidth": false,
		"aoColumns": [ 
             { "bVisible": false }, 
             { "bVisible": false },
             { "sClass": "left" },
             { "sClass": "left" },
             { "sClass": "right" }, 
             { "sClass": "right" }, 
             { "sClass": "right" },
             { "bVisible": false },
             { "sClass": "right" },
             { "bVisible": false },
             { "sClass": "center", "bSortable": false }  
     	]
	});

	$('#tb_kpi tbody tr').live('click', function (event) {		
		kpiTable.fnSetSelectable(this,'selected_internal');
	});
		
});
//-->
</script>

<c:if test="${op:hasPermission(user,project.status,tab)}">

<script language="javascript" type="text/javascript" >

    readyMethods.add(function () {

        $("#projectUpperThreshold").change(function() {
            var params = {
                accion: '<%=ProjectPlanServlet.JX_UPDATE_THRESHOLD%>',
                <%=Project.IDPROJECT%>: '${project.idProject}',
                <%=Project.LOWERTHRESHOLD%>: parseFloat(toNumber($("#projectLowerThreshold").val())),
                <%=Project.UPPERTHRESHOLD%>:$("#projectUpperThreshold").val()
            };
            planAjax.call(params);
        });

        $("#projectLowerThreshold").change(function() {
            var params = {
                accion: '<%=ProjectPlanServlet.JX_UPDATE_THRESHOLD%>',
                <%=Project.IDPROJECT%>: '${project.idProject}',
                <%=Project.LOWERTHRESHOLD%>: $("#projectLowerThreshold").val(),
                <%=Project.UPPERTHRESHOLD%>: parseFloat(toNumber($("#projectUpperThreshold").val()))
            };
            planAjax.call(params);
        });

    searchProjects.validateAndProcess = function(ids, id, processAction) {

        var params = {
            accion: '<%=ProjectPlanServlet.JX_CHECK_KPI%>',
            idImportProjects: ids,
            id: id
        };

        planAjax.call(params,function(data) {

            if (data.isOverWeight) {
                confirmUI('<fmt:message key="kpi.import"/>', '<fmt:message key="kpi.msg_import_weight"/>', '<fmt:message key="yes"/>', '<fmt:message key="no"/>', processAction);
            }
            else {
                processAction();
            }
        });
    };

    $('#openImportKpi').on('click', searchProjects.open);
});
</script>
</c:if>
<fmt:message key="kpi.name" var="titleKpiName"/>
<visual:panel id="planKPI" title="${titleKpiName }">
	<div style="margin: 5px 0px; text-align: right;">
        <c:if test="${op:hasPermission(user,project.status,tab)}">
            <input type="button" id="openImportKpi" class="boton" value="<fmt:message key="kpi.import"/>"/>
        </c:if>
	</div>
	<table id="tb_kpi" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%"><fmt:message key="id" /></th>
				<th width="0%">&nbsp;</th>
				<th width="32%"><fmt:message key="kpi.metric" /></th>
				<th width="40%"><fmt:message key="kpi.definition" /></th>
				<th width="5%"><fmt:message key="kpi.upper_threshold" /></th>
				<th width="5%"><fmt:message key="kpi.lower_threshold" /></th>
				<th width="5%"><fmt:message key="kpi.weight" />&nbsp;%</th>
				<th width="0%">&nbsp;</th>
				<th width="5%"><fmt:message key="aggregate" /></th>
				<th width="0%">&nbsp;</th>
				<th width="8%">
					<c:if test="${op:hasPermission(user,project.status,tab)}">
						<img onclick="addKpi()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
					</c:if>
				</th>
			</tr>
		</thead>
		<tbody>
			<c:set var="totalWeight" value="${0}"/>
			<c:forEach var="kpi" items="${project.projectkpis }">
				<c:set var="totalWeight" value="${kpi.weight + totalWeight}"/>
				<tr>
					<td>${kpi.idProjectKpi}</td>
					<td>${kpi.metrickpi.idMetricKpi}</td>
					<td>
						<c:choose>
							<c:when test="${kpi.metrickpi.idMetricKpi == null}">
								${tl:escape(kpi.specificKpi)}
							</c:when>
							<c:otherwise>
								${tl:escape(kpi.metrickpi.name)}
							</c:otherwise>
						</c:choose>
					</td>
					<td>${tl:escape(kpi.metrickpi.definition)}</td>
					<td>${tl:toCurrency(kpi.upperThreshold)}</td>
					<td>${tl:toCurrency(kpi.lowerThreshold)}</td>
					<td>${tl:toCurrency(kpi.weight)}</td>
					<td>${kpi.specificKpi}</td>
					<c:choose>
						<c:when test="${kpi.aggregateKpi == true}">
							<td><input type="checkbox" disabled checked/></td>
						</c:when>
						<c:otherwise>
							<td><input type="checkbox" disabled /></td>
						</c:otherwise>
					</c:choose>
					<td>${kpi.aggregateKpi}</td>
					<td>
						<c:if test="${op:hasPermission(user,project.status,tab)}">
							<nobr>
								<img onclick="editKpi(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
								&nbsp;&nbsp;&nbsp;
								<img onclick="deleteKpi(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
							</nobr>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><input type="text" class="campo importe" id="projectUpperThreshold" value="${project.upperThreshold}"/></td>
				<td><input type="text" class="campo importe" id="projectLowerThreshold" value="${project.lowerThreshold}"/></td>
				<td><div id="totalWeight" class="right">${tl:toCurrency(totalWeight)}</div></td>
				<td>&nbsp;</td>
			</tr>
		</tfoot>
	</table>
</visual:panel>