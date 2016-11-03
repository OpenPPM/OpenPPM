<%@page import="es.sm2.openppm.core.model.impl.Problemcheck"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>

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
  ~ File: mant_problem_check.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDelete">
	<fmt:param><fmt:message key="problem_check"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDelete">
	<fmt:param><fmt:message key="problem_check"/></fmt:param>
</fmt:message>

<fmt:message key="msg.confirm_uncheck_problem_check" var="msgConfirmUncheck">
	<fmt:param><fmt:message key="problem_check"/></fmt:param>
	<fmt:param><fmt:message key="problem_check_list"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_uncheck_problem_check" var="msgTitleConfirmUncheck">
	<fmt:param><fmt:message key="problem_check"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="name"/></b></fmt:param>
</fmt:message>

<script language="javascript" type="text/javascript" >

var problemCheckTable;
var problemCheckValidator;

var problemCheck = {
		
	add: function() {
		
		var f = document.frm_problem_check_pop;
		
		f.reset();
		f.<%= Problemcheck.IDPROBLEMCHECK %>.value = '';
		$(f.<%= Problemcheck.DESCRIPTION %>).text('');
		$("#<%= Problemcheck.SHOWCHECK %>Old").html(false);
		
		$('#problem-check-popup').dialog('open');
	},
	edit: function(element) {
		
		var aData = problemCheckTable.fnGetData( element.parentNode.parentNode.parentNode );
		
		var f = document.frm_problem_check_pop;
		f.reset();
		
		f.<%= Problemcheck.IDPROBLEMCHECK %>.value 	= aData[0];
		f.<%= Problemcheck.NAME %>.value			= unEscape(aData[1]);
		$(f.<%= Problemcheck.DESCRIPTION %>).text(unEscape(aData[2]));
		
		if (aData[5] === 'true') {
			$("#<%= Problemcheck.SHOWCHECK %>Old").html("true");
			$("#<%= Problemcheck.SHOWCHECK %>").attr("checked", true);
		}
		else {
			$("#<%= Problemcheck.SHOWCHECK %>Old").html("false");
			$("#<%= Problemcheck.SHOWCHECK %>").attr("checked", false);
		}
		
		$('#problem-check-popup').dialog('open');
	},
	del: function(element) {
		
		confirmUI(
			'${msgTitleConfirmDelete}','${msgConfirmDelete}',
			'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
			function() {
				
				var row = problemCheckTable.fnGetData( element.parentNode.parentNode.parentNode );
				
				var params={
					accion: 								"<%=MaintenanceServlet.JX_DELETE_PROBLEM_CHECK %>",
					<%= Problemcheck.IDPROBLEMCHECK %>: 	row[0]
				};
				
				mainAjax.call(params, function(data){
					problemCheckTable.fnDeleteSelected();
				});	
		});
	},
	save: function() {
		
		if (problemCheckValidator.form()) {
			
			if ($("#<%= Problemcheck.SHOWCHECK %>Old").html() == "true"  && $("#<%= Problemcheck.SHOWCHECK %>").is(':checked') == false) {
				
				confirmUI(
						'${msgTitleConfirmUncheck}','${msgConfirmUncheck}',
						'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
						function() {
						
							mainAjax.call($("#frm_problem_check_pop").serializeArray(), function (data) {
								
								var f = document.frm_problem_check_pop;
								
								var dataRow = [
									data.<%= Problemcheck.IDPROBLEMCHECK %>,
									escape(f.<%= Problemcheck.NAME %>.value),
									escape(f.<%= Problemcheck.DESCRIPTION %>.value),
									'<input type="checkbox" disabled="disabled" '+($('#<%= Problemcheck.SHOWCHECK %>').is(':checked')?'checked':'')+'/>',
									'',
									$('#<%= Problemcheck.SHOWCHECK %>').is(':checked') ? "true" : "false"
					            ];
								
								if (f.<%= Problemcheck.IDPROBLEMCHECK %>.value == "") { 
									problemCheckTable.fnAddDataAndSelect(dataRow); 
								} 
								else {
									problemCheckTable.fnUpdateAndSelect(dataRow); 
								}
									
								$('#problem-check-popup').dialog('close');
							});	
						}	
				);
			}
			else {
				
				mainAjax.call($("#frm_problem_check_pop").serializeArray(), function (data) {
					
					var f = document.frm_problem_check_pop;
					
					var dataRow = [
						data.<%= Problemcheck.IDPROBLEMCHECK %>,
						escape(f.<%= Problemcheck.NAME %>.value),
						escape(f.<%= Problemcheck.DESCRIPTION %>.value),
						'<input type="checkbox" disabled="disabled" '+($('#<%= Problemcheck.SHOWCHECK %>').is(':checked')?'checked':'')+'/>',
						'',
						$('#<%= Problemcheck.SHOWCHECK %>').is(':checked') ? "true" : "false"
		            ];
					
					if (f.<%= Problemcheck.IDPROBLEMCHECK %>.value == "") { 
						problemCheckTable.fnAddDataAndSelect(dataRow); 
					} 
					else {
						problemCheckTable.fnUpdateAndSelect(dataRow); 
					}
						
					$('#problem-check-popup').dialog('close');
				});	
			}
			
			
		}
	},
	buttons: function(disable) {
		
		var $buttons = $('<nobr/>');
		
		$buttons.append($('<img/>', {onclick: 'problemCheck.edit(this)', title: '<fmt:message key="view"/>', 'class': 'link', src: 'images/view.png'}))
				.append('&nbsp;&nbsp;&nbsp;')
				.append($('<img/>', {onclick: 'problemCheck.del(this)', title: '<fmt:message key="delete"/>', 'class': 'link', src: 'images/delete.jpg'}))
				.append('&nbsp;&nbsp;&nbsp;');
		
		return $buttons;	
	}
};

readyMethods.add(function () {

	problemCheckTable = $('#tb_problem_check').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"fnRowCallback": function( nRow, aData) {
			$('td:eq(3)', nRow).html( problemCheck.buttons(aData[4]) );
			return nRow;
		},
		"aoColumns": [ 
			{ "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
			{ "sClass": "center" },
            { "sClass": "center", "bSortable" : false },
            { "bVisible": false }
     	]
	});

	$('#tb_problem_check tbody tr').live('click',  function (event) {
		problemCheckTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#problem-check-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 400,
		resizable: false,
		open: function(event, ui) { problemCheckValidator.resetForm(); }
	});

	problemCheckValidator = $("#frm_problem_check_pop").validate({
		errorContainer: $('div#problem_check-errors'),
		errorLabelContainer: 'div#problem_check-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#problem_check-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%= Problemcheck.NAME %>: { required: true },
			<%= Problemcheck.DESCRIPTION %>: { maxlength: 500 }
		},
		messages:{
			<%= Problemcheck.NAME %>: { required: '${fmtNameRequired}'}
		}
	});

	$("#saveProblemCheck").on('click', problemCheck.save);
});

</script>

<fieldset>
	<legend><fmt:message key="problem_check" /></legend>
	<table id="tb_problem_check" class="tabledata" cellspacing="1" style="width:100%">
		<thead>
			<tr>
				 <th width="0%">&nbsp;</th>
				 <th width="30%"><fmt:message key="name" /></th>
				 <th width="55%"><fmt:message key="description" /></th>
				 <th width="5%"><fmt:message key="show" /></th>
				 <th width="10%">
					<img onclick="problemCheck.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
				 <th width="0%"></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="problemCheck" items="${list}">
				<tr>
					<td>${problemCheck.idProblemCheck}</td>
					<td>${tl:escape(problemCheck.name)}</td>
					<td>${tl:escape(problemCheck.description)}</td>
					<td>
						<input type="checkbox" disabled="disabled"
							<c:if test="${problemCheck.showCheck eq true}">checked</c:if> 
						/>
					</td>
					<td></td>
					<td>${problemCheck.showCheck}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>

<div id="problem-check-popup" class="popup">

	<%-- Errors message --%>
	<div id="problem_check-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="problem_check-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_problem_check_pop" id="frm_problem_check_pop">
		<input type="hidden" name="accion" value="<%= MaintenanceServlet.JX_SAVE_PROBLEM_CHECK %>"/>
		<input type="hidden" name="<%= Problemcheck.IDPROBLEMCHECK %>"/>
		<input type="hidden" name="idManten" value="<%= Constants.MANT_PROBLEM_CHECK %>"/>
		<fieldset>
			<legend><fmt:message key="problem_check"/></legend>
			<table cellpadding="2" cellspacing="1" style="width:100%">
				<tr>
					<th width="70%"><fmt:message key="name" />&nbsp;*</th>
					<th width="30%"><fmt:message key="show" /></th>
				</tr>
				<tr>
					<td><input type="text" name="<%= Problemcheck.NAME %>" class="campo" maxlength="50"/></td>
					<td>
						<div style="display:none;" id="<%= Problemcheck.SHOWCHECK %>Old"></div>
						<input type="checkbox" name="<%= Problemcheck.SHOWCHECK %>" id="<%= Problemcheck.SHOWCHECK %>" class="campo" maxlength="50"/>
					</td>
				</tr>
				<tr>
					<th colspan="2"><fmt:message key="description" /></th>
				</tr>
				<tr>
					<td colspan="2"><textarea rows="4" name="<%= Problemcheck.DESCRIPTION %>" class="campo" style="width: 98%"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="button" class="boton" id="saveProblemCheck" value="<fmt:message key="save"/>" />
    	</div>
    </form>
</div>

