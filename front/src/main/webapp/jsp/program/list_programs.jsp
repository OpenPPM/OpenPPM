<%@page import="es.sm2.openppm.front.servlets.ScenarioAnalysisServlet"%>
<%@page import="es.sm2.openppm.front.servlets.PriorizationProgramServlet"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored ="false"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Program"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>

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
  ~ File: list_programs.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Vars --%>
<c:set var="consistent"><%= Constants.CONSISTENT %></c:set>
<c:set var="inconsistent"><%= Constants.INCONSISTENT %></c:set>

<fmt:message key="tabledata.deselect_all" var="deselectAll" />

<%-- Message for confirmations --%>
<fmt:message key="msg.confirm_delete_el" var="msg_confirm_delete_program">
	<fmt:param><fmt:message key="maintenance.program"/></fmt:param>
</fmt:message>

<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_program">
	<fmt:param><fmt:message key="maintenance.program"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtManagerRequired">
	<fmt:param><b><fmt:message key="maintenance.program.manager"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtCodeRequired">
	<fmt:param><b><fmt:message key="maintenance.program.code"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="maintenance.program.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtTitleRequired">
	<fmt:param><b><fmt:message key="maintenance.program.title"/></b></fmt:param>
</fmt:message>

<%-- Message for popup --%>
<fmt:message key="maintenance.program.new" var="new_program_title" />
<fmt:message key="maintenance.program.edit" var="edit_program_title" />

<c:set var="settingProjectExceededBudget"><%= SettingUtil.getBoolean(request, Settings.SETTING_PROJECT_EXCEEDED_BUDGET, Settings.DEFAULT_PROJECT_EXCEEDED_BUDGET) %></c:set>

<c:if test="<%= SecurityUtil.hasPermission(request, PriorizationProgramServlet.REFERENCE) && SettingUtil.getBoolean(request, Settings.SETTING_SHOW_PRIORIZATION_PROGRAMS, Settings.DEFAULT_SETTING_SHOW_PRIORIZATION_PROGRAMS) %>">
	<c:set var="priorizationBtn"><img onclick="editPriorities(this)" title="<fmt:message key="direct_priorization"/>" class="link" src="images/priorities.png">&nbsp;&nbsp;</c:set>
</c:if>
<c:if test="<%= SecurityUtil.hasPermission(request, ScenarioAnalysisServlet.REFERENCE) && SettingUtil.getBoolean(request, Settings.SETTING_SHOW_SCENARIO_ANALYSIS, Settings.DEFAULT_SETTING_SHOW_SCENARIO_ANALYSIS) %>">
	<c:set var="scenarioBtn"><img onclick="scenarioAnalysis(this)" title="<fmt:message key="program.scenario_analysis"/>" class="link" src="images/scenarioAnalysis.png">&nbsp;&nbsp;</c:set>
</c:if>
	
<c:if test="<%=SecurityUtil.hasPermission(request, ProgramServlet.SAVE_PROGRAM) %>">
	<c:set var="editProgBtn"><img onclick="editProgram(this)" title="<fmt:message key="edit"/>" class="link" src="images/view.png">&nbsp;&nbsp;</c:set>
</c:if>
<c:if test="<%=SecurityUtil.hasPermission(request, ProgramServlet.DEL_PROGRAM) %>">
	<c:set var="deleteProgBtn"><img onclick="deleteProgram(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></c:set>
</c:if>

<script language="javascript" type="text/javascript" >
var programsTable;
var programValidator;

function addProgram() {
	var f = document.frm_program_pop;
	f.reset();
	f.program_id.value = "";
	confirmLink();
	$('#programs-popup legend').html('${new_program_title}');
	$('#programs-popup').dialog('open');
}

function saveProgram() {

	if (programValidator.form()) {
		
		loadingPopup();
		document.frm_program_pop.submit();
	}
}

function editProgram(element) {	
	
	var program = programsTable.fnGetData(element.parentNode.parentNode.parentNode);
	
	var f = document.frm_program_pop;
	
	f.program_id.value 			= program[0];	
	f.program_code.value 		= unEscape(program[2]);
	f.program_name.value 		= unEscape(program[3]);
	f.program_title.value 		= unEscape(program[10]);

	$("#program_description").text(unEscape(program[4]));
	f.program_doc.value 		= program[11];
	f.budget.value 				= program[7];
	f.initBudgetYear.value	 	= unEscape(program[5]);
	f.finishBudgetYear.value	= unEscape(program[6]);
	f.actualCost.value	 		= program[8];
	f.budgetBottom.value 		= $(program[9]).html();
	f.program_manager.value 	= program[14];
	
	// Display followup info
	$('#programs-popup legend').html('${edit_program_title}');
	confirmLink();
	$('#programs-popup').dialog('open');
}

function deleteProgram(element) {
	
	confirmUI(
		'${msg_title_confirm_delete_program}','${msg_confirm_delete_program}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var program = programsTable.fnGetData(element.parentNode.parentNode.parentNode);
			
			var f = document.frm_programs;
			f.accion.value	= "<%=ProgramServlet.DEL_PROGRAM %>";
			f.id.value		= program[0];
			
			loadingPopup();
			f.submit();
	});
}

function confirmLink() {
	$('#toggleEditLink').hide();
	$('#toggleALink').show();
	
	$('#program_doc_link')
		.text($('#program_doc').val())
		.attr('href',$('#program_doc').val());
}
function modifyLink() {

	$('#toggleALink').hide();
	$('#toggleEditLink').show();
}

function closeProgram() {
	$('div#programs-popup').dialog('close');
}

//Call priorizationProgramServlet
function editPriorities(element) {
	
	var program = programsTable.fnGetData(element.parentNode.parentNode.parentNode);
	
	var f 				= document.frm_program_pop;
	f.action			= "<%= PriorizationProgramServlet.REFERENCE %>";
	f.accion.value		= "";
	f.program_id.value 	= program[0];	
	loadingPopup();
	f.submit();
}

//Call ScenarioAnalysisServlet
function scenarioAnalysis(element) {
	
	var program = programsTable.fnGetData(element.parentNode.parentNode.parentNode);
	
	var f 				= document.frm_program_pop;
	f.action			= "<%= ScenarioAnalysisServlet.REFERENCE %>";
	f.accion.value		= "";
	f.program_id.value 	= program[0];	
	loadingPopup();
	f.submit();
}

readyMethods.add(function () {
	
	programsTable = $('#tb_programs').dataTable({
		"sDom": 'T<"clear">lfrtip',
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"iDisplayLength": 50,
		"aaSorting": [[ 2, "asc" ], [ 3, "asc" ]],
		"bAutoWidth": false,
		"bFilter": false,
		"oTableTools": {
			"sRowSelect": "multi",
			"aButtons": [  {
                "sExtends": "select_none",
                "sButtonText": "${deselectAll}"
            } ]
		},
		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
			$('td:eq(<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)?11:(SecurityUtil.isUserInRole(request, Constants.ROLE_PROGM)?9:10)%>)', nRow).html('<nobr>${priorizationBtn}${scenarioBtn}${editProgBtn}${deleteProgBtn}</nobr>'); 
			
			return nRow;
		},
		"aoColumns": [ 
				{ "bVisible": false },
				{ "sClass": "left", "bVisible": <%=!SecurityUtil.isUserInRole(request, Constants.ROLE_PROGM) %> },
	            { "sClass": "left"},
	            { "sClass": "left"},
	            { "sClass": "left"},
	            <% if (SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)) {%>
            	{ "sClass": "left"},
            	<%}%>
	            { "sClass": "center"},
	            { "sClass": "center"},
	            { "sClass": "right"},
	            { "sClass": "right"},
	            { "sClass": "right"},
	            { "bVisible": false },						
				{ "bVisible": false  },
				{ "sClass": "center"},
	            { "sClass": "center", "bSortable" : false },
	            { "bVisible": false}
      		]
	});

	$('div#programs-popup').dialog({ autoOpen: false, modal: true, width: 550, minWidth: 300, resizable: false });

	programValidator = $("#frm_program_pop").validate({
		errorContainer: $('div#program-errors'),
		errorLabelContainer: $("ol", $('div#program-errors')),
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#program-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			program_manager: { required: true },
			program_code: { required: true },
			program_name: { required: true },
			program_title: { required: true },
			program_description: { maxlength : 200 },
			initBudgetYear: {maxlength : 4, number: true},
			finishBudgetYear: {maxlength : 4, number:true, min: function() {return $("#initBudgetYearPop").val(); }}
		},
		messages: {
			program_manager: { required: '${fmtManagerRequired}' },
			program_code: { required: '${fmtCodeRequired}' },
			program_name: { required: '${fmtNameRequired}' },
			program_title: { required: '${fmtTitleRequired}' }
		}
	});
});

</script>

<%-- Filter programs --%>
<jsp:include page="list/filter_program.jsp" flush="true"/>

<form id="frm_programs" name="frm_programs" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" id="id" name="id" />
	
	<table width="100%">
		<tr>
			<td>
				<table id="tb_programs" class="tabledata" cellspacing="1" width="100%">
					<thead>
						<tr>
							 <th width="0%">&nbsp;</th>
							 <th width="10%"><fmt:message key="program.manager" /></th>
							 <th width="7%"><fmt:message key="program.code" /></th>
							 <th width="15%"><fmt:message key="program.name" /></th>
							 <th width="13%"><fmt:message key="program.description" /></th>
							 <c:if test="${profile == role_porf_manager}">
							 	<th width="15%"><fmt:message key="perf_organization" /></th>
							 </c:if>
							 <th width="5%"><fmt:message key="program.initial_budget_year" /></th>
						  	 <th width="5%"><fmt:message key="program.finish_budget_year" /></th>
							 <th width="7%"><fmt:message key="program.budget" /></th>
							 <th width="10%"><fmt:message key="program.actual_cost" /></th>
							 <th width="10%"><fmt:message key="program.budget_bottom_up" /></th>
							 <th width="0%"><fmt:message key="program.title" /></th>							 
							 <th width="0%"><fmt:message key="program.doc" /></th>
							 <th width="8%"><fmt:message key="program.priorization" /></th>
							 <th width="10%">
							 	<c:if test="<%=SecurityUtil.hasPermission(request, ProgramServlet.SAVE_PROGRAM) %>">
								 	<c:if test="${profile != role_pgm_manager}">
										<img onclick="addProgram()" title="<fmt:message key="add"/>" class="link" src="images/add.png">
									</c:if>
								</c:if>
							 </th>
							 <th width="0%">&nbsp;</th>
						</tr>
					</thead>
					
					<tbody>
						<c:forEach var="program" items="${list_programs}">
							<tr>
								<td>${program.idProgram}</td>
								<td>${tl:escape(program.employee.contact.fullName)}</td>
								<td>${tl:escape(program.programCode)}</td>
								<td>${tl:escape(program.programName)}</td>
								<td>${tl:escape(program.description)}</td>
								<c:if test="${profile == role_porf_manager}">
									<td>${tl:escape(program.performingorg.name)}</td>
								</c:if>
								<td>${tl:escape(program.initBudgetYear)}</td>
								<td>${tl:escape(program.finishBudgetYear)}</td>
								<td>${tl:toCurrency(program.budget)}</td>
								<td>${tl:toCurrency(program.sumActualCost)}</td>
								<td>
								<c:choose>
									<c:when test="${settingProjectExceededBudget && (program.budgetBottomUp > program.budget)}">
										<span style="color:red;font-weight: bold;">${tl:toCurrency(program.budgetBottomUp)}</span>
									</c:when>
									<c:otherwise>
										<span>${tl:toCurrency(program.budgetBottomUp)}</span>
									</c:otherwise>
								</c:choose>
								</td>
								<td>${tl:escape(program.programTitle)}</td>								
								<td>${program.programDoc}</td>
								<td>
									<c:set var="priority">${program.priorization}</c:set>
									<c:choose>
										<c:when test="${priority != 100}">
											<span style="color:red;font-weight: bold;">${priority}</span>
										</c:when>
										<c:otherwise>
											${priority}
										</c:otherwise>
									</c:choose>
								</td>	
								<td></td>
								<td>${program.employee.idEmployee}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</td>
		</tr>
	</table>	
</form>

<%-- PROJECTS --%>
<jsp:include page="list/list_projects.inc.jsp" flush="true"/>

<div id="programs-popup">
	<div id="program-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="program-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_program_pop" id="frm_program_pop" method="post">
		<input type="hidden" name="program_id"/>
		<input type="hidden" name="accion" value="<%=ProgramServlet.SAVE_PROGRAM %>"/>
		
		<fieldset>
			<legend><fmt:message key="maintenance.program.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th width="50%"><fmt:message key="program.code" />&nbsp;*</th>
					<th width="50%"><fmt:message key="program.name" />&nbsp;*</th>					
				</tr>
				<tr>
					<td>
						<input type="text" name="program_code" class="campo" maxlength="20"
							<c:if test="${profile == role_pgm_manager}">
								disabled
							</c:if>
						/>
					</td>
					<td>
						<input type="text" name="program_name" class="campo" maxlength="50"
							<c:if test="${profile == role_pgm_manager}">
								disabled
							</c:if>
						/>
					</td>
				</tr>
				<tr>
					<th><fmt:message key="program.manager" />&nbsp;*</th>
					<th><fmt:message key="program.title" />&nbsp;*</th>					
				</tr>
				<tr>
					<td>
						<select class="campo" name="program_manager"
							<c:if test="${profile == role_pgm_manager}">
								disabled
							</c:if>
						>
							<option value=""><fmt:message key="select_opt" /></option>
							<c:forEach var="programManager" items="${list_employees}">
								<option value="${programManager.idEmployee}">${programManager.contact.fullName}</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<input type="text" name="program_title" class="campo" maxlength="50"
							<c:if test="${profile == role_pgm_manager}">
								disabled
							</c:if>
						/>
					</td>				
				</tr>
				<tr>
					<th colspan = 2><fmt:message key="program.doc" /></th>					
				</tr>
				<tr>
					<td colspan = 2>
						<div id="toggleEditLink">
							<c:if test="<%=SecurityUtil.hasPermission(request, ProgramServlet.SAVE_PROGRAM) %>">
								<c:if test="${profile != role_pgm_manager}">
									<img style="width:16px;" src="images/approve.png" onclick="confirmLink()"/>
								</c:if>
							</c:if>
							<input type="text" name="program_doc" id="program_doc" style="width: 452px;" maxlength="200" class="campo"/>
						</div>
						<div id="toggleALink">
							<c:if test="<%=SecurityUtil.hasPermission(request, ProgramServlet.SAVE_PROGRAM) %>">
								<c:if test="${profile != role_pgm_manager}">
									<img style="width:16px;" src="images/modify.png" onclick="modifyLink()"/>
								</c:if>
							</c:if>
							<a href="" id="program_doc_link"></a>
						</div>
					</td>					
				</tr>
				<tr>
					<td colspan = 2>
						<table border="0" cellpadding="2" cellspacing="1" width="100%">
							<tr>
								<th width="25%"><fmt:message key="program.budget_year" /></th>
								<th width="25%"><fmt:message key="program.budget" /></th>
								<th width="25%"><fmt:message key="program.actual_cost" /></th>
								<th width="25%"><fmt:message key="program.budget_bottom_up" /></th>
							</tr>
							<tr>
								<td class="center">
									<input type="text" name="initBudgetYear" id="initBudgetYearPop" class="campo" maxlength="4" style="width: 32px;"
										<c:if test="${profile == role_pgm_manager}">
											disabled
										</c:if>
									/>
									<input type="text" name="finishBudgetYear" id="finishBudgetYearPop" class="campo" maxlength="4" style="width: 32px;"
										<c:if test="${profile == role_pgm_manager}">
											disabled
										</c:if>
									/>
								</td>
								<td class="center">
									<input type="text" name="budget" id="budget" class="campo importe" style="width: 120px;"
										<c:if test="${profile == role_pgm_manager}">
											disabled
										</c:if>
									/>
								</td>								
								<td><input type="text" name="actualCost" id="actualCost" style="text-align: center;" class="importe" readonly/></td>
								<td><input type="text" name="budgetBottom" id="budgetBottom" style="text-align: center;" class="importe" readonly/></td>								
							</tr>
						</table>						
					</td>	
				</tr>
				<tr>
					<th colspan="2"><fmt:message key="program.description" /></th>
				</tr>
				<tr>
					<td colspan="2">
						<textarea name="program_description" id="program_description" class="campo" style="width:98%;" ></textarea>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<c:if test="<%=SecurityUtil.hasPermission(request, ProgramServlet.SAVE_PROGRAM) %>">
				<a href="javascript:saveProgram();" class="boton"><fmt:message key="save" /></a>
			</c:if>
			<a href="javascript:closeProgram();" class="boton"><fmt:message key="close" /></a>
    	</div>
    </form>
</div>
