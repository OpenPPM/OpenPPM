<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Problemcheck"%>
<%@page import="es.sm2.openppm.core.model.impl.Problemcheckproject"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectClosureServlet"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
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
  ~ File: problem_check_list.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<%-- Messages --%>
<fmt:message key="problem_check_list" var="titleProblemCheckList"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDeleteChecklist">
	<fmt:param><fmt:message key="problem_check_list"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDeleteChecklist">
	<fmt:param><fmt:message key="problem_check_list"/></fmt:param>
</fmt:message>

<fmt:setLocale value="${locale}" scope="request"/>

<script type="text/javascript">
<!--

var problemAjax = new AjaxCall('<%=ProjectClosureServlet.REFERENCE%>','<fmt:message key="error" />');
var problemTable;

var problemList = {
		
		add: function() { 
			
			var params={
				accion: 					"<%=ProjectClosureServlet.JX_SELECT_PROBLEM_CHECK_LIST %>",
				<%= Project.IDPROJECT %>: 	$("#id").val()
			}; 
			
			closureAjax.call(params, function(data){
				
				if (data.length > 0) {
					
					$("#tableProblemListPopup").show();
					$("#noDataProblemListPopup").hide();
					$("#problemlist-popup .popButtons").show();
					
					$("#problemcheck").empty();
					
					$(data).each(function(){
						$("#problemcheck").append('<option value="'+ this.<%= Problemcheck.IDPROBLEMCHECK %> +'" description="'+ 
								 this.<%= Problemcheck.DESCRIPTION %> +'" >'+ this.<%= Problemcheck.NAME %> +'</option>');
					});				
				}
				else {
					$("#tableProblemListPopup").hide();
					$("#noDataProblemListPopup").show();
					$("#problemlist-popup .popButtons").hide();
				}
				
				$('#<%= Problemcheck.ENTITY %>').trigger('change');
				
				$("#problemlist-popup").dialog('open');
			});	
		},
		del: function(element) {
			
		 	confirmUI(
					'${msgTitleConfirmDeleteChecklist}','${msgConfirmDeleteChecklist}',
					'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
					function() {
						
						var row = problemTable.fnGetData( element.parentNode.parentNode.parentNode );
						
						var params={
							accion: 											"<%= ProjectClosureServlet.JX_DELETE_PROBLEM_CHECK_LIST %>",
							<%= Problemcheckproject.IDPROBLEMCHECKPROJECT %>: 	row[0]
						};
						
						closureAjax.call(params, function(data){
							problemTable.fnDeleteSelected();
						});
				});
		}
};

readyMethods.add(function() {
	
	problemTable = $('#tb_problemlist').dataTable({
		"oLanguage": datatable_language,		
		"bInfo": false,
		"bAutoWidth": false,
		"sPaginationType": "full_numbers",
		"aoColumns": [ 
  				{ "bVisible": false }, <%-- Id problem check project --%>
  				{ "bVisible": false }, <%-- Id problem check --%>
  				{ "sClass": "left"}, <%-- name problem check --%>
  				{ "sClass": "left"}, <%-- description problem check --%>
              	{ "sClass": "center", "bSortable": false ,
                    "bVisible": (${op:hasPermission(user,project.status,tab)} &&
                        <%= SecurityUtil.isUserInRole(request, Constants.ROLE_PMO) || SecurityUtil.isUserInRole(request, Constants.ROLE_PM) %>)}
		]
	});
	
	$('#tb_problemlist tbody tr').live('click', function (event) {		
		problemTable.fnSetSelectable(this,'selected_internal');
	});	
	
	$('#tb_problemlist .add').live('click', function (event) {		
		problemList.add(this);
	});
	
	$('#tb_problemlist .delete').live('click', function (event) {	
		problemList.del(this);
	});
});
//-->
</script>

<visual:panel id="fieldProblemCheckList" title="${titleProblemCheckList }">
	
	<table id="tb_problemlist" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%"></th> <%-- Id problem check project --%>
				<th width="0%"></th> <%-- Id problem check --%>
				<th width="40%"><fmt:message key="name" /></th>
				<th width="55%"><fmt:message key="description" /></th>
				<th width="5%">
                    <img title="<fmt:message key="new"/>" class="link add" src="images/add.png">
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="check" items="${project.problemcheckprojects}">
				<tr>
					<td>${check.idProblemCheckProject }</td>
					<td>${check.problemcheck.idProblemCheck }</td>
					<td>${tl:escape(check.problemcheck.name)}</td>
					<td>${tl:escape(check.problemcheck.description)}</td>
					<td>
						<nobr>
							<c:if test="<%= SecurityUtil.isUserInRole(request, Constants.ROLE_PMO) || SecurityUtil.isUserInRole(request, Constants.ROLE_PM) %>">
								<img class="delete" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
							</c:if>	
						</nobr>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</visual:panel>