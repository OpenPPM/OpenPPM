<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
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
  ~ File: select_two_options_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:56
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%--
	params: functionOptionOne, textOptionOne, functionOptionTwo, textOptionTwo
--%>

<script type="text/javascript">

function viewOptionOne(){
	$('div#selection-popup').dialog('close');
	
	${param.functionOptionOne};
	
	$('.options_add').attr('checked',false);
}

function viewOptionTwo(){
	$('div#selection-popup').dialog('close');
	
	${param.functionOptionTwo};
	
	$('.options_add').attr('checked',false);
}

readyMethods.add(function() {
	$('div#selection-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false
	});
	
});
</script>

<div id="selection-popup" class="popup">
	<%-- SELECT OPTION --%>
	<div id="selection" style="text-align: center; margin-bottom: 12px; margin-top: 14px; display:block;">
		<fieldset>
			<legend><fmt:message key="wbs.selection"/></legend>
			${param.textOptionOne}:&nbsp;<input type="radio" class="options_add" style="margin-right: 10px; width: 15px;" onclick="viewOptionOne();"/> 
			${param.textOptionTwo}:&nbsp;<input type="radio" class="options_add" style="width:15px;" onclick="viewOptionTwo();"/>
		</fieldset>
	</div>
</div>