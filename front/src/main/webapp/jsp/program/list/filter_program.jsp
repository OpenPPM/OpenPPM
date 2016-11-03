<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.javabean.FiltroTabla"%>
<%@page import="es.sm2.openppm.core.model.impl.Program"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored ="false"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

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
  ~ File: filter_program.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--

inputImportObject = {
		// SHOW AND HIDE INPUTS
		showFirstLast: function(id){
			$('#' + id).change(function() {
				
				var $option = $(this);
				
				if ($option.val() == "<%=FiltroTabla.GREATHER_EQUAL%>" || $option.val() == "<%=FiltroTabla.LESS_EQUAL%>") {
					$('#last_' + id).show();
					$('#first_' + id).hide();
				}
				else if ($option.val() == '<%=FiltroTabla.BETWEEN%>') {
					$('#first_'+id + ',' + '#last_' + id).show();
				}
				else {
					$('#first_'+id + ',' + '#last_' + id).hide();
				}
				
			}).trigger('change');
		},
		// GET VALUES SELECT AND INPUTS
		// RETURN STRING
		getValues: function(idSelect){
			var arrayValues;
			
			if ($('#' + idSelect).val() == "") {
				arrayValues =  [$('#' + idSelect).val(),"", ""];
			}
			else {
				arrayValues =  [$('#' + idSelect).val(), toNumber($('#first_' + idSelect).val()),  toNumber($('#last_' + idSelect).val())];
			}
			
			return arrayValues.join();
		},
		//RESET SELECT AND INPUTS
		resetValues: function(idSelect){
			$("#" + idSelect).val("");
			$('#first_'+idSelect + ',' + '#last_' + idSelect).hide();
			$("#first_" + idSelect).val("");
			$("#last_" + idSelect).val("");
		}
};


function applyFilterProgram(){
	
	var params = {
		accion: "<%=ProgramServlet.JX_SEARCH_PROGRAMS%>",
		<%= Program.PROGRAMNAME %>: $('#' + '<%= Program.PROGRAMNAME %>').val(),
		programBudget: inputImportObject.getValues('programBudget'),
		programActualCost: inputImportObject.getValues('programActualCost'),
		programBudgetBottomUp: inputImportObject.getValues('programBudgetBottomUp'),
		<%= Program.INITBUDGETYEAR %>: $('#' + '<%= Program.INITBUDGETYEAR %>').val(),
		<%= Program.FINISHBUDGETYEAR %>: $('#' + '<%= Program.FINISHBUDGETYEAR %>').val()
		<%if (!SecurityUtil.isUserInRole(request, Constants.ROLE_PROGM)) { %>
			,idsProgramManager: $('#idProgramManager').val() == null ? "" :  $('#idProgramManager').val().join()
		<%}%>		
		<% if (SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)) {%>
			,idsPerfOrg: $('#idPerfOrg').val() == null ? "" :  $('#idPerfOrg').val().join()
		<%}%>
	};
	
	programAjax.call(params, function(data) {
		
		var priorization = '';
		var budgetBottomUp = '';
		
		programsTable.fnClearTable();
		
		$(data.list_programs).each(function(){
			
			if (this.priorization != 100) {
				priorization = '<span style="color:red;font-weight: bold;">' + this.priorization + '</span>';
			}
			else {
				priorization = this.priorization;
			}
			
			if (<%= SettingUtil.getBoolean(request, Settings.SETTING_PROJECT_EXCEEDED_BUDGET, Settings.DEFAULT_PROJECT_EXCEEDED_BUDGET) %> &&
					this.budgetBottomUp > this.budget) {
				budgetBottomUp = '<span style="color:red;font-weight: bold;">'+toCurrency(this.budgetBottomUp)+'</span>';
			}
			else {
				budgetBottomUp = toCurrency(this.budgetBottomUp);
			}
			var program = [
				              this.idProgram,
				              unEscape(this.programManagerFullName),
				              unEscape(this.programCode),
				              unEscape(this.programName),
				              unEscape(this.description),
				              <% if (SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)) {%>
				              	unEscape(this.perfOrg),
				              <%}%>	
				              unEscape(this.initBudgetYear),
				              unEscape(this.finishBudgetYear),
				              toCurrency(this.budget),
				              toCurrency(this.sumActualCost),
				              budgetBottomUp,
				              unEscape(this.programTitle),
				              unEscape(this.programDoc),
				              unEscape(priorization),
				              '',
				              this.idEmployee
			              ];
			
			programsTable.fnAddData(program);
		});
	});
}

function deselected(select){
	for (var i=0;i<select.length;i++){
		select.options[i].selected = false;
	}
}

function resetFilterPrograms(){
	$("#" + "<%=Program.PROGRAMNAME%>").val("");

	inputImportObject.resetValues('programBudget');
	inputImportObject.resetValues('programActualCost');
	inputImportObject.resetValues('programBudgetBottomUp');
	
	$("#" + "<%=Program.INITBUDGETYEAR%>").val("");
	$("#" + "<%=Program.FINISHBUDGETYEAR%>").val("");

	<%if (!SecurityUtil.isUserInRole(request, Constants.ROLE_PROGM)) { %>
		deselected(document.getElementById("idProgramManager"));
	<%}%>
	
	<% if (SecurityUtil.isUserInRole(request, Constants.ROLE_PORFM)) {%>
		deselected(document.getElementById("idPerfOrg"));
	<%}%>
}

function advancedFilterPrograms() {
	$('#advancedFilterPrograms').toggle('blind');
}


readyMethods.add(function() {
	
	inputImportObject.showFirstLast('programBudget');
	inputImportObject.showFirstLast('programActualCost');
	inputImportObject.showFirstLast('programBudgetBottomUp');
	
});

//-->
</script>
<fieldset style="margin-bottom: 15px; padding-top:10px; display:block;">

	<table width="100%">
		<tr>
			<td width="15%">
				<b><fmt:message key="filter" />:&nbsp;<a class="advancedFilter" href="javascript:advancedFilterPrograms();"><fmt:message key="filter.advanced"/></a></b>
			</td>
			<td width="40%">
				<fmt:message key="search" />:&nbsp;
				<input type="text" id="<%= Program.PROGRAMNAME %>" class="campo" style="width: 300px;">
			</td>
			<td class="right">
				<a href="javascript:applyFilterProgram();" class="boton"><fmt:message key="filter.apply" /></a>
				<a href="javascript:resetFilterPrograms();" class="boton"><fmt:message key="filter.reset" /></a>
			</td>
		</tr>
	</table>
	
	<div>&nbsp;</div>
	
	<table width="100%" class="center hide" id="advancedFilterPrograms">
		<tr>
			<th width="30%">
				<fmt:message key="program.budget" />:&nbsp;
			</th>
			<th width="30%">
				<fmt:message key="program.actual_cost" />:&nbsp;
			</th>
			<th width="30%">
				<fmt:message key="program.budget_bottom_up" />:&nbsp;
			</th>
		</tr>
		<tr>
			<td>
				<input type="text" class="campo importe" id="first_programBudget" style="margin:0; max-width: 20%;" />
				<select class="campo" id="programBudget"  style="margin-right:3px; width: 137px;">
					<option value="" selected><fmt:message key="show.all"/></option>
					<option value="<%=FiltroTabla.GREATHER_EQUAL%>"><fmt:message key="filter.greater_equal"/></option>
					<option value="<%=FiltroTabla.LESS_EQUAL%>"><fmt:message key="filter.less_equal"/></option>
					<option value="<%=FiltroTabla.BETWEEN%>"><fmt:message key="filter.between"/></option>
				</select>
				<input type="text" class="campo importe" id="last_programBudget" style="margin:0; max-width: 20%;" />
			</td>
			<td>
				<input type="text" class="campo importe" id="first_programActualCost" style="margin:0; max-width: 20%;" />
				<select class="campo" id="programActualCost"  style="margin-right:3px; width: 137px;">
					<option value="" selected><fmt:message key="show.all"/></option>
					<option value="<%=FiltroTabla.GREATHER_EQUAL%>"><fmt:message key="filter.greater_equal"/></option>
					<option value="<%=FiltroTabla.LESS_EQUAL%>"><fmt:message key="filter.less_equal"/></option>
					<option value="<%=FiltroTabla.BETWEEN%>"><fmt:message key="filter.between"/></option>
				</select>
				<input type="text" class="campo importe" id="last_programActualCost" style="margin:0; max-width: 20%;"/>
			</td>
			<td>
				<input type="text" class="campo importe" id="first_programBudgetBottomUp" style="margin:0; max-width: 20%;" />
				<select class="campo" id="programBudgetBottomUp"  style="margin-right:3px; width: 137px;">
					<option value="" selected><fmt:message key="show.all"/></option>
					<option value="<%=FiltroTabla.GREATHER_EQUAL%>"><fmt:message key="filter.greater_equal"/></option>
					<option value="<%=FiltroTabla.LESS_EQUAL%>"><fmt:message key="filter.less_equal"/></option>
					<option value="<%=FiltroTabla.BETWEEN%>"><fmt:message key="filter.between"/></option>
				</select>
				<input type="text" class="campo importe" id="last_programBudgetBottomUp" style="margin:0; max-width: 20%;"/>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<th width="10%">
				<fmt:message key="program.budget_year" />:&nbsp;
			</th>
			<c:if test="<%=!SecurityUtil.isUserInRole(request, Constants.ROLE_PROGM) %>">
				<th colspan="1">
					<fmt:message key="program.manager" />:&nbsp;
				</th>
			</c:if>
			<c:if test="${profile == role_porf_manager}">
				<th colspan="1">
					<fmt:message key="perf_organization" />:&nbsp;
				</th>
			</c:if>
		</tr>
		<tr>
			<td>
				<input type="text" class="campo" id="<%=Program.INITBUDGETYEAR%>" style="width:32px;margin-right:0px;" maxlength="4"/>
				-
				<input type="text" class="campo" id="<%=Program.FINISHBUDGETYEAR%>" style="width: 30px; margin: 0px;" maxlength="4"/>
			</td>
			<c:if test="<%=!SecurityUtil.isUserInRole(request, Constants.ROLE_PROGM) %>">
				<td colspan="1">
					<select id="idProgramManager" style="height: 72px;" multiple="multiple" class="campo">
						<c:forEach var="programManager" items="${list_employees}">
							<option value="${programManager.idEmployee}">
								${programManager.contact.fullName}
								(${programManager.performingorg.name})
							</option>
						</c:forEach>
					</select>
				</td>
			</c:if>
			<td colspan="1">
				<c:if test="${profile == role_porf_manager}">
					<select id="idPerfOrg" style="height: 72px;" multiple="multiple" class="campo">
						<c:forEach var="perfOrg" items="${performingorgs}">
							<option value="${perfOrg.idPerfOrg}">${perfOrg.name}</option>
						</c:forEach>
					</select>
				</c:if>
			</td>
		</tr>
	</table>
	
</fieldset>