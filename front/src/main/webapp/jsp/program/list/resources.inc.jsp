<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Visual" prefix="visual" %>

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
  ~ File: resources.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>
<%--
<script language="javascript" type="text/javascript" >
<!--

function infoSalesForecast() {

	programAjax.call({
		accion: "<%=ProgramServlet.JX_CONS_SALES_FORECAST%>",
		ids: dat.ids
	}, function(data) {
		if (typeof data.xml === 'undefined') { $("#salesForecastChart").html('<fmt:message key="data_not_found"/>'); }
		else { 
			var chartSales = new FusionCharts("FusionCharts/FCF_Area2D.swf", "SalesId", 850, 450); 
		    chartSales.setDataXML(data.xml);    
		    chartSales.render("salesForecastChart");
		}
		show('fieldPrgSalesForecast');
	});
}
//-->
</script>
 --%>

<%-- RESOURCES --%>
<fmt:message key="resources" var="titleResources"/>
<visual:panel id="fieldPrgSalesForecast" title="${titleResources }">
	Not Available
	<%--
		<div style="margin-left:30px; margin-bottom:10px;">
			<a href="javascript:infoSalesForecast();" class="boton"><fmt:message key="chart.refresh"/></a>
		</div>
		<div class="center">
			<div id="salesForecastChart"></div>
		</div>
	--%>
</visual:panel>
