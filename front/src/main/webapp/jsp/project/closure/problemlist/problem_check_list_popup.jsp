<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Problemcheckproject"%>
<%@page import="es.sm2.openppm.core.model.impl.Problemcheck"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectClosureServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.core.common.Constants"%>

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
  ~ File: problem_check_list_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtProblemCheckRequired">
	<fmt:param><b><fmt:message key="problem_check"/></b></fmt:param>
</fmt:message>

<c:set var="deleteProblem">
	<c:if test="<%= SecurityUtil.isUserInRole(request, Constants.ROLE_PMO) || SecurityUtil.isUserInRole(request, Constants.ROLE_PM) %>">
		<img class="delete" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
	</c:if>	
</c:set>

<script type="text/javascript">
<!--
var validatorProblemList;
var date;

var problemListPopup = {

	save: function() {

		if (validatorProblemList.form()) {

		 	var params = {
	 			accion: 								"<%= ProjectClosureServlet.JX_SAVE_PROBLEM_CHECK_LIST %>",
	 			<%= Problemcheck.IDPROBLEMCHECK %>: 	$("#problemcheck option:selected").val(),
	 			<%= Project.IDPROJECT %>:				$("#id").val()
			};

		 	closureAjax.call(params, function(data){

				var dataRow = [
					data.<%= Problemcheckproject.IDPROBLEMCHECKPROJECT %>,
					$("#problemcheck option:selected").val(),
					$("#problemcheck option:selected").html(),
					$("#problemcheck option:selected").attr("description") != 'undefined' ? $("#problemcheck option:selected").attr("description") : "",
					"<nobr>" + '${deleteProblem}' + "</nobr>"
		        ];

				problemTable.fnAddDataAndSelect(dataRow);

				$("#problemlist-popup").dialog('close');
		 	});
		}
	}
};

readyMethods.add(function() {
	
	// Dialog
	$('div#problemlist-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 500,
		minWidth: 500,
		resizable: false,
		open: function(event, ui) {

			$("#problemlist-popup").css("overflow", "hidden");

			validatorProblemList.resetForm();
		}
	});

	// Validator
	validatorProblemList = $("#frm_problemlist_popup").validate({
		errorContainer: 'div#problemlist-errors',
		errorLabelContainer: 'div#problemlist-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#problemlist-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%= Problemcheck.ENTITY %>: { required: true },
		},
		messages: {
			<%= Problemcheck.ENTITY %>: { required: '${fmtProblemCheckRequired}'}
		}
	});

	$('#saveProblemCheckProject').live('click', function (event) {
		problemListPopup.save();
	});

	$("#<%= Problemcheck.ENTITY %>").selectDescription({width : '93%', positions: 'most'});
	
});
//-->
</script>
<div id="problemlist-popup" class="popup">

	<%-- Errors --%>
	<div id="problemlist-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="problemlist-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_problemlist_popup" id="frm_problemlist_popup" method="post">

		<fieldset>
			<legend><fmt:message key="problem_check_list"/></legend>

			<div id="tableProblemListPopup" style="display:block;">
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr></tr>
					<tr>
						<td>
							<select name="<%= Problemcheck.ENTITY %>" id="<%= Problemcheck.ENTITY %>" class="campo">
								<option value="" selected><fmt:message key="select_opt"/></option>
								<c:forEach var="problemCheck" items="${problemChecks}">
									<option value="${problemCheck.idProblemCheck}" description="${tl:escape(problemCheck.description)}">${tl:escape(problemCheck.name)}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</table>
			</div>
			<%-- Information no data --%>
			<div id="noDataProblemListPopup" style="margin-bottom: 10px; padding: 0pt 0.7em; display:none;" class="ui-state-highlight ui-corner-all">
				<p>
					<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
					<strong><fmt:message key="msg.info"/>: </strong>
				</p>
				<p>
					<fmt:message key="msg.info.problem_check">
						<fmt:param><b><fmt:message key="problem_check"/></b></fmt:param>
					</fmt:message>
				</p>
			</div>
    	</fieldset>
    	<div class="popButtons">
    		<button type="button" class="boton" id="saveProblemCheckProject"><fmt:message key="create"/></button>
    	</div>
    </form>
</div>
