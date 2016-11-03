<%@page import="es.sm2.openppm.front.servlets.ProjectClosureServlet"%>
<%@ page import="es.sm2.openppm.core.logic.security.actions.ClosureTabAction" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

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
  ~ File: close_project_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for confirmation --%>
<fmt:message key="msg.confirm" var="msgConfirm" />
<fmt:message key="msg.close_project_confirm" var="msgCloseProjectConfirm" />
<fmt:message key="yes" var="msgYes" />
<fmt:message key="no" var="msgNo" />

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtLessonsRequired">
	<fmt:param><b><fmt:message key="learned_lessons"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var closeValidator;

function openCloseProject() {
	
	var f = document.forms["frm_project"];
	var params = {
		accion: '<%=ProjectClosureServlet.JX_CHECK_CLOSURE_PROJECT%>', 
		id: f.id.value
	};
		
	closureAjax.call(params, function(data) {
		
		var $warings = $("#warnings");
		$warings.html('');
		
		if (typeof data.showWarning !== 'undefined') {
			$(data.showWarning).each(function(i, value) {
				
				$warings.append((i>0?"<br/>":"")+value);
			});
			
			$("#closureWarnings").show();
		}
		else {
			$("#closureWarnings").hide();
		}
		
		$('#close-popup').dialog('open');
	});
}

function closeProject() {
	
	if (closeValidator.form()) {
		confirmUI("${msgConfirm }", "${msgCloseProjectConfirm }", "${msgYes }", "${msgNo }",
				function() {
					var f = document.forms["frm_close"];
					f.id.value = document.forms["frm_project"].id.value;
					f.accion.value = "<%=ClosureTabAction.CLOSE_PROJECT.getAction() %>";
					closeConfirmUI();
					loadingPopup();
					f.submit();
				}
			);
	}
}

readyMethods.add(function() {
	$('div#close-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 750, 
		minWidth: 500, 
		resizable: false,
		open: function(event, ui) { closeValidator.resetForm(); }
	});

	closeValidator = $("#frm_close").validate({
		errorContainer: 'div#close-errors',
		errorLabelContainer: 'div#close-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#close-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			comments: {maxlength: 200 },
			stk_comments: {maxlength: 200 }

		}
	});
});
//-->
</script>

<div id="close-popup" class="popup">

	<div id="close-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="close-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_close" id="frm_close" action="<%=ProjectClosureServlet.REFERENCE%>" method="post">
		<input type="hidden" name="id" value="" />
		<input type="hidden" name="accion" value="" />
		
		<fieldset id="closureWarnings">
			<legend><fmt:message key="msg.warning.pending_approval" /></legend>
			<div id="warnings" style="margin-bottom: 10px; padding: 5px 0.7em;" class="ui-state-highlight ui-corner-all"></div>
		</fieldset>
		
		<fieldset>
			<legend><fmt:message key="close_project" /></legend>
			
			<table width="100%">
				<tr>
					<th colspan="2" class="left"><fmt:message key="comments.pm" /></th>
				</tr>
				<tr>
					<td colspan="2"><textarea name="comments" id="comments" class="campo" style="width:95%"></textarea></td>
				</tr>
				<tr>
					<th colspan="2" class="left"><fmt:message key="stakeholder_comments" /></th>
				</tr>
				<tr>
					<td colspan="2"><textarea name="stk_comments" id="stk_comments" class="campo" style="width:95%"></textarea></td>
				</tr>
			</table>
    	</fieldset>
		<div class="popButtons">
			<a href="javascript:closeProject();" class="boton"><fmt:message key="proceed.close_project"/></a>
		</div>
    </form>    
</div>