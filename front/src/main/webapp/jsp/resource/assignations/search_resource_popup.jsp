<%@page import="es.sm2.openppm.core.model.impl.Skill"%>
<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Performingorg"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

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
  ~ File: search_resource_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:04
  --%>

<fmt:setLocale value="${locale}" scope="request"/>


<script type="text/javascript">
<!--
var searchTable;

function findTeamMembers() {

	searchTable.fnClearTable();

	planAjax.call({
			accion: "<%=ProjectPlanServlet.JX_FIND_MEMBER %>",
			idSkill: $('#f_skill').val()+"",
			<%=Performingorg.IDPERFORG %>: $('#<%=Performingorg.IDPERFORG %>_select').val(),
			idResourceManager: '${user.idEmployee}'
		}, function(data) {

			for(var i=0; i< data.length; i++) {
				searchTable.fnAddData( [
						data[i].id,				
	            		escape(data[i].fullname),
	            		escape(data[i].profile),
	            		escape(data[i].resource_manager),
	            		'',
	            		data[i].cost_rate,
	            		escape(data[i].performing_org),
	            		data[i].contact_id,
	            		(data[i].skill == ' ' ? ' ' : ('<div class="btitle" title="' + escape(data[i].skills) + '">' + escape(data[i].skill) + '...</div>')),
	            		'<img onclick="addProposedResource(this)" title="<fmt:message key="resource.proposed"/>" class="link ico" src="images/add_proj.png">',
	            		escape(data[i].<%=Contact.BUSINESSPHONE%>),
	            		escape(data[i].<%=Contact.MOBILEPHONE%>),
	            		escape(data[i].<%=Contact.EMAIL%>),
	            		optionsSkills(data[i].skillArray)
		           	]);
	        }

			chargeBT();	
	});
}

function addProposedResource(element) {
	
	var f = document.frm_resource_pop;
	
	var aData = searchTable.fnGetData( element.parentNode.parentNode );

	$('#proposed_fullname').val(aData[1]);
	$('#idSkill').html(aData[13]);
	
	f.idResourceProposed.value = aData[0];

    $("#idResourceProposed").trigger('change');

	closeFind();
}

function chargeBT() {
	$('.btitle').bt({
		fill: '#F9FBFF',
		cssStyles: {color: '#343C4E', width: 'auto'},
		width: 250,
		padding: 10,
		cornerRadius: 5,
		spikeLength: 15,
		spikeGirth: 5,
		shadow: true,
		positions: 'top'
	});
}

function optionsSkills(data) {
	var options = '<option value=""><fmt:message key="select_opt"/></option>';
	
	$(data).each(function() {
		
		options += '<option value="'+this.<%=Skill.IDSKILL%>+'">'+escape(this.<%=Skill.NAME%>)+'</option>';
	});
	return options;
}

function openFind() { $('#find_resource_popup').dialog('open'); }
function closeFind() { $('#find_resource_popup').dialog('close'); }

readyMethods.add(function() {

	$('div#find_resource_popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 700, 
		resizable: false
	});
	
	searchTable = $('#find_result').dataTable({
		"oLanguage": datatable_language,
		"sPaginationType": "full_numbers",
		"iDisplayLength": 50,
		"aoColumns": [
			  { "bVisible": false },
              { "sClass": "left" },
			  { "bVisible": false },
              { "sClass": "left" }, 
              { "bVisible": false }, 
              { "sClass": "right"}, 
              { "sClass": "left" }, 
              { "bVisible": false },
              { "sClass": "left" }, 
              { "sClass": "center", "bSortable": false },
              { "bVisible": false },
              { "bVisible": false },
              { "bVisible": false },
              { "bVisible": false }
     		]
	});


	$('#find_result tbody tr').live('click', function (event) {
		fnSetSelectable(searchTable, this);
	});
});

//-->
</script>

<div id="find_resource_popup" class="popup">
	<fieldset>
		<legend><fmt:message key="resource"/></legend>
		<table id="find_filter" cellspacing="0" width="100%" style="text-align: center; padding-bottom: 10px;">
			<tr>
				<th width="45%"><fmt:message key="skill"/></th>
				<th width="45%"><fmt:message key="maintenance.businessline.perf_org"/></th>
				<th width="10%" rowspan="2">
					<a class="boton" href="javascript:findTeamMembers();"><fmt:message key="find"/></a>
				</th>
			</tr>
			<tr>
				<td>
					<select id="f_skill" name="f_skill" class="campo" multiple="multiple">
						<c:forEach var="skill" items="${skills}">
							<option value="${skill.idSkill}">${skill.name}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<select id="<%=Performingorg.IDPERFORG %>_select" name="<%=Performingorg.IDPERFORG %>" class="campo">
						<option value="-1" selected><fmt:message key="select_opt"/></option>
						<c:forEach var="perforg" items="${perforgs}">
							<option value="${perforg.idPerfOrg}">${perforg.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
		<table id="find_result" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th width="0%">&nbsp;</th>
					<th width="25%"><fmt:message key="contact.fullname"/></th>
					<th width="0%"><fmt:message key="profile"/></th>
					<th width="20%"><fmt:message key="resource_manager"/></th>
					<th width="0%">&nbsp;</th>
					<th width="10"><fmt:message key="cost.rate"/></th>
					<th width="20%"><fmt:message key="perforg"/></th>
					<th width="0%">&nbsp;</th>
					<th width="25"><fmt:message key="skill"/></th>
					<th width="5%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
					<th width="0%">&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</fieldset>
	<div class="popButtons">
		<a href="javascript:closeFind();" class="boton"><fmt:message key="close"/></a>
	</div>
</div>