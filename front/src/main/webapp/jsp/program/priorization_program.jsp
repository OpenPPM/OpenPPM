<%@page import="es.sm2.openppm.core.model.impl.Program"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@page import="es.sm2.openppm.front.servlets.PriorizationProgramServlet"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.front.utils.OpenppmUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
  ~ File: priorization_program.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:message key="data_not_found" var="dataNotFound" />

<!-- Validations -->
<fmt:message key="validation.valid_number" var="validNumber" />
<fmt:message key="validation.range" var="range">
	<fmt:param>0</fmt:param>
	<fmt:param>100</fmt:param>
</fmt:message>
<fmt:message key="validation.integer_positive" var="integerPositive" />


<fmt:setLocale value="${locale}" scope="request"/>


<script type="text/javascript">
<!--
var projectsTable;
var validatorPriorities;
var priorizationProgramAjax = new AjaxCall('<%= PriorizationProgramServlet.REFERENCE %>','<fmt:message key="error"/>');


var program = {
		// Return programs page
		back: function () {
			
			var f 			= document.forms["frm_projects"];
			f.action 		= "<%= ProgramServlet.REFERENCE %>";
			f.accion.value 	= "";
			loadingPopup();
			f.submit();
		},
		// Update program
		update: function() {
			
			if (validatorPriorities.numberOfInvalids() == 0) {
				
				var ids 		= "";
				var priorities 	= "";
	
				$('#tb_projects input.priority').each(function() {
					
					var row = projectsTable.fnGetData(this.parentNode.parentNode);
					
					if ($(row[3]).attr("value") != $(this).val()) {
						
						if (ids.length > 0) {
							ids += ",";
						}
						
						ids += row[0];
						
						if (priorities.length > 0) {
							priorities += ",";
						}
						
						priorities += $(this).val();
					}
				});
				
			 	var params = {
					accion: "<%= PriorizationProgramServlet.JX_UPDATE_PROJECTS %>",
					ids: ids,
					priorities: priorities
				}; 
				
				priorizationProgramAjax.call(params, function(data) {
					
				});
			}
		},
		// Create table
		createTable: function() {
			
			return $('#tb_projects').dataTable({
				"oLanguage": datatable_language,
				"bPaginate": true,
				"sPaginationType": "full_numbers",
				"iDisplayLength": 50,
		     	"aaSorting": [[1, 'asc']],
				"aoColumns": [
					{"bVisible": false }, /* idProject	 */						  
		            {"sClass": "left" }, /* nameProject */
		            {"sClass": "left" }, /* chartLabel */
		            {"sClass": "left" }, /* status */
		            {"sClass": "center", "sSortDataType": "dom-text", "sType": "numeric" } /* priority */
		     	]
			});
		},
		// Validations
		validate: function() {
			
			return $("#frm_projects").validate({
				errorContainer: 'div#projects-errors',
				errorLabelContainer: 'div#projects-errors ol',
				wrapper: 'li',
				showErrors: function(errorMap, errorList) {
					$('span#projects-numerrors').html(this.numberOfInvalids());
					this.defaultShowErrors();
				},
				rules: {
					priority: { number: true, range: [0,100], integerPositive: true}
				},
				messages: {
					priority: { number: '${validNumber}', range: '${range}', integerPositive: '${integerPositive}' }
				}
			});	
		},
		// Total recalculate
		recalculateTotal: function() {
			
			var total = 0;
			
			$('#tb_projects input.priority').each(function() {
				total += parseInt($(this).val());
			});
			
			var span = $("<span>").html(total);
			
			if (total != 100) {
				span = $(span).attr("style","color:red;font-weight: bold;")
			}
			
			$("#totalPriority").html(span);
		}
};

readyMethods.add(function () {
	
	projectsTable 		= program.createTable();
	
	validatorPriorities = program.validate();
	
	// Change input
	$('#tb_projects input.priority').blur(function() {
		
		if (validatorPriorities.numberOfInvalids() == 0) {
			program.recalculateTotal();
		}
	});
	
});

//-->
</script>
	
<!-- PROJECTS -->
<fmt:message key="projects" var="titleProjects"/>
<visual:panel title="${titleProjects}" >
	<form name="frm_projects" id="frm_projects" method="post">
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="<%= Program.IDPROGRAM %>" id="<%= Program.IDPROGRAM %>" value="${program.idProgram}" />
		
		<div id="projects-errors" class="ui-state-error ui-corner-all hide" style="margin-top: 15px;">		
			<p>
				<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
				<strong><fmt:message key="msg.error_title"/></strong>
				&nbsp;(<b><span id="projects-numerrors"></span></b>)
			</p>
			<ol></ol>
		</div>
		
		<table id="tb_projects" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="0%">&nbsp;</th><!-- idProject -->
					<th width="60%"><fmt:message key="project" /></th>
					<th width="22%"><fmt:message key="project.chart_label" /></th>
					<th width="10%"><fmt:message key="status" /></th>
					<th width="8%"><fmt:message key="strategic_value" />&nbsp;%</th>
				</tr>
			</thead>
			<tbody>
				<c:set var="totalPriority">${0}</c:set>
				<c:set var="closed" value="<%= Constants.INVESTMENT_CLOSED %>"/>
				<c:forEach var="project" items="${projects}">
					<tr>
						<td>${project.idProject}</td>
						<td>${tl:escape(project.projectName)}</td>
						<td>${tl:escape(project.chartLabel)}</td>
						<td>
							<c:set var="status">${project.status}</c:set>
							<c:set var="disable">${project.disable}</c:set>
							<c:choose>
								<c:when test="${project.disable}">
									<fmt:message key="project.delete"/>
								</c:when>
                                <c:otherwise>
                                    <fmt:message key="project_status.${project.status}"/>
                                </c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${project.investmentStatus == closed || project.disable}">
									<input name="priority" class="center" type="text" value="${tl:escape(project.priority)}"  disabled>
								</c:when>
								<c:otherwise>
									<c:set var="totalPriority" value="${totalPriority + project.priority}"/>
									<input name="priority" class="priority center campo" style="width:25%; margin-right:0px;" type="text" maxlength="3" value="${tl:escape(project.priority)}">
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr> 
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td class="center" id="totalPriority">
						<c:choose>
							<c:when test="${totalPriority != 100}">
								<span style="color:red;font-weight: bold;">${totalPriority}</span>
							</c:when>
							<c:otherwise>
								<span>${totalPriority}</span> 
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</tfoot>
		</table>
	</form>
</visual:panel>

<div align="right" style="margin-top:10px;">
	<a href="javascript: program.back();" class="boton"><fmt:message key="close" /></a>
	<a href="javascript: program.update();" class="boton"><fmt:message key="save" /></a>
</div>