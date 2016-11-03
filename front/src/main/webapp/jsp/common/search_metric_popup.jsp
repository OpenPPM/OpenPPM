<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.model.impl.Bscdimension"%>
<%@page import="es.sm2.openppm.core.logic.impl.BscdimensionLogic"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectkpi"%>
<%@page import="es.sm2.openppm.core.model.impl.Metrickpi"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.front.servlets.UtilServlet"%>

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
  ~ File: search_metric_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:44
  --%>

<%
Employee user = SecurityUtil.validateLoggedUser(request);
%>

<fmt:setLocale value="${locale}" scope="request"/>
<fmt:message key="error" var="fmt_error" />

<%-- 
Request Attributes:
	profiles:		List of Resourceprofiles
	perforgs:		List of Performing Organization
--%>

<script type="text/javascript">
<!--
var searchTableMetric;

var utilAjax = new AjaxCall('<%=UtilServlet.REFERENCE%>','<fmt:message key="error"/>');

function searchMetric() {
	
	var f = document.forms["frm_search_metric"];
	
	utilAjax.call($('#frm_search_metric').serialize(), function(data){
		
		searchTableMetric.fnClearTable();
		
		for (var i = 0; i < data.length; i++) {
			
			searchTableMetric.fnAddData([
				data[i].<%= Metrickpi.IDMETRICKPI %>,
				data[i].<%= Metrickpi.NAME %> != null ? data[i].<%= Metrickpi.NAME %> : "",
				data[i].<%= Metrickpi.BSCDIMENSION %> != null ? data[i].<%= Metrickpi.BSCDIMENSION %> : "",
                data[i].<%= Metrickpi.TYPE %> != null ? data[i].<%= Metrickpi.TYPE %> : "",
				data[i].<%= Metrickpi.DEFINITION %> != null ? data[i].<%= Metrickpi.DEFINITION %> : "",
				'<img class="icono"  title="<fmt:message key="add"/>" src="images/AddContact.png" onclick="sendMetric(this);" />'
			]);
		}
		
	});
}

function sendMetric(element) {
		
	var metric = searchTableMetric.fnGetData( element.parentNode.parentNode );
	
	$('#<%= Metrickpi.IDMETRICKPI %>').val(metric[0]);
	$('#metricName').val(metric[1]);
	$('#<%=Metrickpi.DEFINITION%>').val(metric[4]);
	
	closeSearchMetric();
}

function searchMetricPop(id, name) {
	
	searchTableMetric.fnClearTable();
	document.forms["frm_search_metric"].reset();
	
	if (typeof name !== 'undefined' && name != '') {
		$('#search_name').val(name);
		$('#searchByProject').val(id);
		searchMetric();
	}
	
	if ($("#" + "<%= Metrickpi.BSCDIMENSION %>" + " option").length == 1) {
		
		var params = {
			accion: "<%= UtilServlet.JX_GET_BSCDIMENSIONS %>"
		}; 
		
		utilAjax.call(params, function(data) {
			
			var option;
			
			for (var i = 0; i < data.length; i++) {
				
				option = $("<option>");
				option.attr("value", data[i].<%= Bscdimension.IDBSCDIMENSION %>);
				option.html(data[i].<%= Bscdimension.NAME %>);
				
				$("#"+ "<%= Metrickpi.BSCDIMENSION %>").append(option);
			}
		});
	}

	$('div#search-metric-popup').dialog('open');
}

function closeSearchMetric() {
	$('div#search-metric-popup').dialog('close');
}

readyMethods.add(function() {

	$('div#search-metric-popup').dialog({ autoOpen: false, modal: true, width: 750, resizable: false,
		open: function(event, ui) { $('#search_name').focus(); }
	});

	$.fn.dataTableExt.oStdClasses.sWrapper = "modified_wrapper";
	
	searchTableMetric = $('#tb_search_metric').dataTable({
		"oLanguage": datatable_language,
		"bFilter": false,
		"bInfo": false,
		"iDisplayLength": 10,
		"bAutoWidth": false,
		"sPaginationType": "full_numbers",
		"aoColumns": [
	        { "bVisible": false }, 
	        { "sClass": "left", "sWidth": "60%"},
	        { "sClass": "left", "sWidth": "20%"},
            { "sClass": "left", "sWidth": "10%"},
	        { "bVisible": false },
	        { "sClass": "center","sWidth": "10%", "bSortable" : false}
		]
	});

	$("#frm_search_metric").bind("submit", function() { searchMetric(); return false; });
	
});
//-->
</script>

<div id="search-metric-popup" class="popup">

	<form name="frm_search_metric" id="frm_search_metric">
		<input type="hidden" name="accion" value="<%=UtilServlet.JX_SEARCH_METRIC_KPI%>"/>
		<input type="hidden" name="<%=Projectkpi.IDPROJECTKPI%>"/>
		<fieldset>
			<legend><fmt:message key="search_metric"/></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tbody>
					<tr>
						<th width="50%" align="left"><fmt:message key="kpi.metric"/></th>
						<th width="20%" align="left"><fmt:message key="metric.bsc_dimension"/></th>
                        <th width="20%" align="left"><fmt:message key="metric.type"/></th>
                        <th width="10%"></th>
					</tr>
					<tr>
						<td><input id="search_name" class="campo" type="text" name="search_name" /></td>
						<td>
							<select name="<%= Metrickpi.BSCDIMENSION %>" id="<%= Metrickpi.BSCDIMENSION %>" class="campo">
								<option value=""><fmt:message key="select_opt"/></option>
								<c:forEach var="bsc" items="${bscdimensions}">
									<option value="${bsc.idBscDimension}">${bsc.name}</option>
								</c:forEach>
							</select>
						</td>
                        <td>
                            <select name="<%= Metrickpi.TYPE %>" id="<%= Metrickpi.TYPE %>" class="campo">
                                <option value=""><fmt:message key="select_opt"/></option>
                                <c:forEach var="type" items="<%= Metrickpi.TypeKPI.values() %>">
                                    <option value="${type}">${type.label}</option>
                                </c:forEach>
                            </select>
                        </td>
						<td><input type="submit" class="boton" value="<fmt:message key="search" />" /></td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>
				</tbody>
			</table>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tbody>
					<tr>
						<td colspan="2">
							<table id="tb_search_metric" class="tabledata" cellspacing="1" >
								<thead>
									<tr>
										<th>&nbsp;</th>
										<th><fmt:message key="kpi.metric"/></th>
										<th><fmt:message key="metric.bsc_dimension"/></th>
                                        <th><fmt:message key="metric.type"/></th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
									</tr>
								</thead>
								<tbody></tbody>
							</table>
						</td>
					</tr>
				</tbody>
    		</table>
    	</fieldset>
   	   	<div class="popButtons">
			<a href="javascript:closeSearchMetric();" class="boton" ><fmt:message key="cancel" /></a>
    	</div>
    </form>
</div>