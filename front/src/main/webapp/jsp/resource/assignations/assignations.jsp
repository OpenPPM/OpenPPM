<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.core.model.impl.Teammember"%>
<%@page import="es.sm2.openppm.front.servlets.ResourceServlet"%>
<%@ page import="es.sm2.openppm.core.common.Configurations" %>
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
  ~ File: assignations.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:04
  --%>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:message key="project.status_initiating" var="msgProjectStatusInitiating"/>

<script language="javascript" type="text/javascript" >
<!--
var resourceAjax = new AjaxCall('<%=ResourceServlet.REFERENCE%>','<fmt:message key="error" />');
var planAjax = new AjaxCall('<%=ProjectPlanServlet.REFERENCE%>','<fmt:message key="error" />');
var resourceTable;

function buttonsResource(data) {
	return '<img onclick="viewResource('+data[0]+')" title="<fmt:message key="view"/>" class="link" src="images/view.png">';
}

function actionApplyFilter() {
	resourceTable.fnDraw();
}

readyMethods.add(function () {
	
	loadFilterState();
	
	resourceTable = $('#tb_resource').dataTable({
		"bServerSide": true,
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[5,'desc']],
		"bFilter": false,
		"iDisplayLength": 50,
		"sAjaxSource": '<%=ResourceServlet.REFERENCE%>',
		"fnRowCallback": function( nRow, aData) {

			if ('<%=Constants.STATUS_INITIATING%>' == aData[aData.length -3]) {
				$(nRow).addClass('orangeRow');
			}

			$('td:eq(13)', nRow).html(buttonsResource(aData));
			return nRow;
		},
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			
			aoData.push( { "name": "accion", "value": "<%=ResourceServlet.JX_FILTER_ASSIGNATIONS%>" } );
			aoData.push( { "name": "<%=Contact.FULLNAME%>", "value": $('#<%=Contact.FULLNAME%>').val() } );
            aoData.push( { "name": "<%=Teammember.DATEIN %>", "value": $('#since').val() } );
            aoData.push( { "name": "<%=Teammember.DATEOUT %>", "value": $('#until').val() } );
            aoData.push( { "name": "<%=Project.EMPLOYEEBYPROJECTMANAGER%>", "value": $('#<%=Project.EMPLOYEEBYPROJECTMANAGER%>').val()+"" } );
            aoData.push( { "name": "jobcategory.idJobCategory", "value": $('#<%=Teammember.JOBCATEGORY %>').val()+"" } );

            <%-- Send configurations value--%>
            aoData.push( { "name": "<%= Configurations.PROJECT_NAME %>", "value": $('#<%= Configurations.PROJECT_NAME %>').val()+"" } );


            var checkboxs = document.getElementsByName('filter');

		    for(var i = 0, inp; inp = checkboxs[i]; i++) {
		        if (inp.type.toLowerCase() == 'checkbox' && inp.checked) {
					aoData.push( { "name": "<%=Teammember.STATUS%>", "value": inp.value } );
		        }
		    }
		    resourceAjax.call(aoData,fnCallback);
		},
		"aoColumns": [ 
	            { "bVisible": false }, 
	            { "sClass": "left" },
	            { "sClass": "left" },
	            { "sClass": "left" },
	            { "sClass": "left" },
	            { "sClass": "left" },
                { "sClass": "left" },
	            { "sClass": "left" },
	            { "sClass": "left" },
	            { "sClass": "center" },
	            { "sClass": "center" },
	            { "sClass": "center" },
	            { "sClass": "center" },
				{ "bVisible": false },
				{ "sClass": "center" },
	            { "sClass": "center", "bSortable": false }
      		]
	});

	createLegend("#legendPorjectInitiation", "${msgProjectStatusInitiating}", "orange", "black");
});
//-->
</script>

<form id="frm_resource" name="frm_resource" method="post" class="listView">
	<input type="hidden" name="accion" value="" />
	
	<jsp:include page="../common/filters.inc.jsp" flush="true"/>
	<div>
		<table id="tb_resource" class="tabledata" cellspacing="1" width="100%">
			<thead>
				<tr>
					<th>&nbsp;</th>
					<th width="6%"><fmt:message key="status" /></th>
					<th width="8%"><fmt:message key="contact.fullname" /></th>
					<th width="8%"><fmt:message key="job_category" /></th>
					<th width="9%"><fmt:message key="maintenance.employee.resource_pool" /></th>
					<th width="7%"><fmt:message key="project" /></th>
                    <th width="9%"><fmt:message key="project.chart_label" /></th>
					<th width="8%"><fmt:message key="activity" /></th>
					<th width="9%"><fmt:message key="project_manager" /></th>
					<th width="6%"><fmt:message key="team_member.date_in" /></th>
					<th width="6%"><fmt:message key="team_member.date_out" /></th>
					<th width="5%"><fmt:message key="team_member.fte" /></th>
					<th width="6%"><fmt:message key="team_member.sell_rate" /></th>
					<th>&nbsp;</th>
                    <th width="6%"><fmt:message key="project.status" /></th>
					<th width="8%">&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
	
	<div id="legendPorjectInitiation" class="legendChart"></div>

</form>
<div class="listView">&nbsp;</div>

<jsp:include page="resource_popup.jsp" flush="true"/>
<jsp:include page="search_resource_popup.jsp" flush="true"/>