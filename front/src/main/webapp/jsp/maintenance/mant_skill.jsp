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
  ~ File: mant_skill.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:06
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message --%>
<fmt:message key="maintenance.skill.new" var="new_skill_title" />
<fmt:message key="maintenance.skill.edit" var="edit_skill_title" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_skill">
	<fmt:param><fmt:message key="maintenance.skill"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete_skill">
	<fmt:param><fmt:message key="maintenance.skill"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtSkillNameRequired">
	<fmt:param><b><fmt:message key="maintenance.skill.name"/></b></fmt:param>
</fmt:message>


<script language="javascript" type="text/javascript" >

var skillsTable;
var skillValidator;

function addSkill() {
	var f = document.frm_skill_pop;
	f.reset();
	f.id.value = "";
	$(f.description).text('');
	
	$('#skills-popup legend').html('${new_skill_title}');
	$('#skills-popup').dialog('open');
}

function saveSkill() {

	var f = document.frm_skill_pop;
	
	if (skillValidator.form()) {
		
		var params = {
				accion: "<%=MaintenanceServlet.JX_SAVE_SKILL %>",
				id: f.id.value,
				name: f.skill_name.value,
				description: f.skill_description.value
		};
		
		mainAjax.call(params, function (data) {
			
			var dataRow = [
				data.id,
				escape(f.skill_name.value),
				escape(f.skill_description.value),
				'<nobr><img onclick="editSkill(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteSkill(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
            ];

			if (f.id.value == data.id){ skillsTable.fnUpdateAndSelect(dataRow);} 
			else {skillsTable.fnAddDataAndSelect(dataRow);}
				
			f.reset();
			$('#skills-popup').dialog('close');
		});	
	}
}

function editSkill(element) {
	
	var skill = skillsTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	var f = document.forms["frm_skill_pop"];
	f.reset();
	f.id.value 				= skill[0];
	f.name.value 			= unEscape(skill[1]);
	$(f.description).text(unEscape(skill[2]));

	$('#skills-popup legend').html('${edit_skill_title}');
	$('#skills-popup').dialog('open');
}

function deleteSkill(element) {
	
	confirmUI(
		'${msg_title_confirm_delete_skill}','${msg_confirm_delete_skill}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var skill = skillsTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			var f = document.frm_skills;

			f.accion.value = "<%=MaintenanceServlet.DEL_SKILL %>";
			f.id.value = skill[0];
			f.idManten.value = $('select#idManten').val();
			
			loadingPopup();
			f.submit();
	});
}


readyMethods.add(function () {

	skillsTable = $('#tb_skills').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"aoColumns": [ 
			{ "sClass": "center", "bSortable" : false, "bVisible": false },
			{ "sClass": "left" },
			{ "sClass": "left" },
            { "sClass": "center", "bSortable" : false } 
    	]
	});

	$('#tb_skills tbody tr').live('click',  function (event) {
		skillsTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#skills-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 400,
		resizable: false,
		open: function(event, ui) { skillValidator.resetForm(); }
	});

	skillValidator = $("#frm_skill_pop").validate({
		errorContainer: $('div#skill-errors'),
		errorLabelContainer: 'div#skill-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#skill-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			name: {required: true },
			description : { maxlength : 100 }
		},
		messages:{
			name:{required: '${fmtSkillNameRequired}'}
		}
	});

});

</script>

<form id="frm_skills" name="frm_skills" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="idManten" value="" />
	<input type="hidden" id="id" name="id" />

	
	<fieldset>
		<legend><fmt:message key="maintenance.skill" /></legend>
		<table id="tb_skills" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
				 <th><fmt:message key="maintenance.skill" /></th>
				 <th width="30%"><fmt:message key="maintenance.skill.name" /></th>
				 <th width="62%"><fmt:message key="maintenance.skill.description" /></th>
				 <th width="8%">
				 	<img onclick="addSkill()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
				 </th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="skill" items="${skills}">
					<tr>
						<td>${skill.idSkill}</td>
						<td>${tl:escape(skill.name)}</td>
						<td>${tl:escape(skill.description)}</td>
						<td>
							<nobr>
								<img onclick="editSkill(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" >
								&nbsp;&nbsp;&nbsp;
								<img onclick="deleteSkill(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
							</nobr>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>
</form>

<div id="skills-popup">

	<div id="skill-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="skill-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_skill_pop" id="frm_skill_pop">
		<fieldset>
			<legend><fmt:message key="maintenance.skill.new"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="maintenance.skill.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="id" id="skill_id" />
						<input type="text" name="name" id="skill_name" class="campo" maxlength="50"/>
					</td>
					<td>
						
					</td>
				</tr>
				<tr>
					<th><fmt:message key="maintenance.skill.description" /></th>
				</tr>
				<tr>
					<td>
						<textarea rows="3" name="description" id="skill_description" class="campo" style="width:98%;"></textarea>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveSkill(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

