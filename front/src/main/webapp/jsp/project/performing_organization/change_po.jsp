<%@page import="es.sm2.openppm.front.servlets.ProjectInitServlet"%>
<%@page import="es.sm2.openppm.front.servlets.ChangePOServlet"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.front.utils.OpenppmUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
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
  ~ File: change_po.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:03
  --%>

<fmt:message key="data_not_found" var="dataNotFound" />
<fmt:message key="maintenance.error_msg_a" var="fmtProjectManagerRequired">
	<fmt:param><b><fmt:message key="project_manager"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtProgramRequired">
	<fmt:param><b><fmt:message key="program"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_change_po" var="fmtChangePOTitle"></fmt:message>
<fmt:message key="msg.confirm_change_po" var="fmtChangePOMsg"></fmt:message>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--

var validator;
var changePOAjax = new AjaxCall('<%=ChangePOServlet.REFERENCE%>','<fmt:message key="error"/>');

function updatePO(){
	
	if (validator.form()) {
		confirmUI(
				'${fmtChangePOTitle}','${fmtChangePOMsg}',
				'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
				function() {
					
					var params = {
						accion: "<%= ChangePOServlet.JX_UPDATE_PO %>",
						id: $('#id').val(),
						idPerfOrg: $('#new_perf_org option:selected').val(),
						idInvestmentManager: $('#investmentmanager').val(),
						idProjectManager: $('#projectmanager').val(),
						idFunctionalManager: $('#functionalmanager').val(),
						idSponsor: $('#sponsor').val(),
						idProgram: $('#program').val()
					};
					
					changePOAjax.call(params, function (data) {
						var f = document.forms["frm_project"];
						f.action = "<%=ProjectServlet.REFERENCE%>";
						f.accion.value = "";
						f.po_name.value = $('#new_perf_org option:selected').text();
						loadingPopup();
						f.submit();
					});
					
				}
		);
	}
}

function back() {
	
	var f = document.forms["frm_project"];
	f.action = "<%= ProjectInitServlet.REFERENCE %>";
	f.accion.value = "";
	loadingPopup();
	f.submit();
}

readyMethods.add(function () {
	
	$('#new_perf_org').change(function(){
		
		var params = {
			accion: "<%= ChangePOServlet.JX_VIEW_PO %>", 
			idPerfOrg: $('#new_perf_org option:selected').val(),
			idInvestmentManager: $('#actual_investmentmanager').val(),
			idProjectManager: $('#actual_projectmanager').val(),
			idFunctionalManager: $('#actual_functionalmanager').val(),
			idSponsor: $('#actual_sponsor').val(),
			idProgram: $('#actual_program').val()
		};
	   		
		changePOAjax.call(params, function (data) {
			
			if(!jQuery.isEmptyObject(data.investmentManager)){
				$('#investmentmanager').attr('value', data.investmentManager.id);
				$('#investmentmanager_name').attr('value', data.investmentManager.name);
			}
			else {
				$('#investmentmanager').attr('value', "");
				$('#investmentmanager_name').attr('value', "");
			}
			
			if(!jQuery.isEmptyObject(data.projectManager)){
				$('#projectmanager').attr('value', data.projectManager.id);
				$('#projectmanager_name').attr('value', data.projectManager.name);
			}
			else {
				$('#projectmanager').attr('value', "");
				$('#projectmanager_name').attr('value', "");
			}
			
			if(!jQuery.isEmptyObject(data.functionalManager)){
				$('#functionalmanager').attr('value', data.functionalManager.id);
				$('#functionalmanager_name').attr('value', data.functionalManager.name);
			}
			else {
				$('#functionalmanager').attr('value', "");
				$('#functionalmanager_name').attr('value', "");
			}
			
			if(!jQuery.isEmptyObject(data.sponsor)){
				$('#sponsor').attr('value', data.sponsor.id);
				$('#sponsor_name').attr('value', data.sponsor.name);
			}
			else {
				$('#sponsor').attr('value', "");
				$('#sponsor_name').attr('value', "");
			}
			
			if(!jQuery.isEmptyObject(data.program)){
				$('#program').attr('value', data.program.id);
				$('#program_name').attr('value', data.program.name);
			}
			else {
				$('#program').attr('value', "");
				$('#program_name').attr('value', "");
			}
			
		});
		
	});
	
	validator = $("#frm_project").validate({
		errorContainer: 'div#project-errors',
		errorLabelContainer: 'div#project-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			projectmanager_name : {required: true },
			program_name: {required: true }
		},
		messages: {
			projectmanager_name : {required: '${fmtProjectManagerRequired}' },
			program_name : {required: '${fmtProgramRequired}' }
		}
	});

});


//-->
</script>

<form name="frm_project" id="frm_project" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="id" id="id" value="${project.idProject}" />
	<input type="hidden" name="actual_perf_org" id="actual_perf_org" value="${project.performingorg.idPerfOrg}" />
	<input type="hidden" name="actual_investmentmanager" id="actual_investmentmanager" value="${project.employeeByInvestmentManager.idEmployee}" />
	<input type="hidden" name="actual_projectmanager" id="actual_projectmanager" value="${project.employeeByProjectManager.idEmployee}" />
	<input type="hidden" name="actual_functionalmanager" id="actual_functionalmanager" value="${project.employeeByFunctionalManager.idEmployee}" />
	<input type="hidden" name="actual_sponsor" id="actual_sponsor" value="${project.employeeBySponsor.idEmployee}" />
	<input type="hidden" name="actual_program" id="actual_program" value="${project.program.idProgram}" />
	<input type="hidden" name="project_name" id="project_name" value="${project.projectName}" />
	<input type="hidden" name="po_name" id="po_name" value="" />
	<input type="hidden" name="scrollTop" />
	
	<!-- Change PO -->
	<fmt:message key="perf_organization.change" var="titleChangePO"/>
	<visual:panel title="${titleChangePO}" showTiltePanel="false">
		
		<div id="project-errors" class="ui-state-error ui-corner-all hide"  style="margin-bottom: 15px;">
			<p>
				<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
				<strong><fmt:message key="msg.error_title"/></strong>
				&nbsp;(<b><span id="numerrors"></span></b>)
			</p>
			<ol></ol>
		</div>
		
		<table id="tb_pos"  width="100%">
			<tbody>
				<tr>
					<th class="left"><fmt:message key="perf_organization"/></th>
					<th class="left">
						<fmt:message key="program" />&nbsp;*
						<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="searchProgramPop('program',$('#new_perf_org option:selected').val());" />
					</th>
				</tr>
				<tr>
					<td>
						<select class="campo" id="new_perf_org" name="new_perf_org">
							<c:forEach var="po" items="${perforgs}">
							
								<c:choose>
									<c:when test="${po.idPerfOrg == project.performingorg.idPerfOrg}">
										<option value="${po.idPerfOrg}" selected="selected">${tl:escape(po.name)}</option>
									</c:when>
									<c:otherwise>
										<option value="${po.idPerfOrg}">${tl:escape(po.name)}</option>
									</c:otherwise>
								</c:choose>
								
							</c:forEach>
						</select>
					</td>
					<td>
						<input type="hidden" id="program" name="program" value="${project.program.idProgram }"/>
						<input type="text" id="program_name" class="campo" name="program_name" value="${project.program.programName }" readonly/>
					</td>
				</tr>
				<tr>
					<th class="left">
						<fmt:message key="proposal.bid_manager"/>
						<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="searchEmployeePop('investmentmanager', $('#new_perf_org option:selected').val(), <%=Constants.ROLE_IM %>);" />
					</th>
					<th class="left">
						<fmt:message key="project_manager" />&nbsp;*
						<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="searchEmployeePop('projectmanager', $('#new_perf_org option:selected').val(), <%=Constants.ROLE_PM %>);"/>
					</th>
					<th class="left">
						<fmt:message key="business_manager" />
						<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="searchEmployeePop('functionalmanager', $('#new_perf_org option:selected').val(), <%=Constants.ROLE_FM%>);" />
					</th>
					<th class="left">
						<fmt:message key="sponsor" />
						<img class="position_right icono"  title="<fmt:message key="search"/>" src="images/search.png" onclick="searchEmployeePop('sponsor', $('#new_perf_org option:selected').val(), <%=Constants.ROLE_SPONSOR%>);" />
					</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" id="investmentmanager" name="investmentmanager" value="${project.employeeByInvestmentManager.idEmployee }"/>
						<input type="text" id="investmentmanager_name" class="campo" name="investmentmanager_name" value="${project.employeeByInvestmentManager.contact.fullName }" readonly/>
					</td>
					<td>
						<input type="hidden" id="projectmanager" name="projectmanager" value="${project.employeeByProjectManager.idEmployee}"/>
						<input type="text" id="projectmanager_name" class="campo" name="projectmanager_name" value="${project.employeeByProjectManager.contact.fullName }" readonly/>
					</td>
					<td>
						<input type="hidden" id="functionalmanager" name="functionalmanager" value="${project.employeeByFunctionalManager.idEmployee }"/>
						<input type="text" class="campo" id="functionalmanager_name" name="functionalmanager_name" value="${project.employeeByFunctionalManager.contact.fullName }" readonly/>
					</td>
					<td>
						<input type="hidden" id="sponsor" name="sponsor" value="${project.employeeBySponsor.idEmployee}"/>
						<input type="text" class="campo" id="sponsor_name" name="sponsor_name" value="${project.employeeBySponsor.contact.fullName}" readonly/>
					</td>
				</tr>
			</tbody>
		</table>
	</visual:panel>

	<div align="right" style="margin-top:10px;" class="hidePrint">
		<a href="javascript: back();" class="boton"><fmt:message key="close" /></a>
		<a href="javascript: updatePO();" class="boton"><fmt:message key="save" /></a>
	</div>
</form>
		
<jsp:include page="../../common/search_employee_popup.jsp" flush="true" />
<jsp:include page="../../common/search_program_popup.jsp" flush="true" />

<div class="spacer"></div>

