<%@page import="es.sm2.openppm.front.utils.RequestUtil"%>
<%@page import="es.sm2.openppm.core.common.Configurations"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Openppm" prefix="op" %>

<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>


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
  ~ File: loadConfigurations.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:00
  --%>

<script type="text/javascript">
<!--

<%-- Congifurations --%>
var configurations = {
		load: function () {
			
			<%-- Checkbox show operations in Resource Capacity Running --%>
			$('#<%= Configurations.SHOW_OPERATIONS %>').prop('checked', "<%= RequestUtil.getConfiguration(request, Configurations.SHOW_OPERATIONS) %>" === 'true');
		}
};

readyMethods.add(function() {
	
	<%-- Load configurations --%>
	configurations.load();
});
//-->
</script>

