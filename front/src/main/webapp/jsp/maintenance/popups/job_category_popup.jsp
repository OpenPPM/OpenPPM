<%@page import="es.sm2.openppm.core.model.impl.Jobcategory"%>
<%@page import="es.sm2.openppm.core.model.impl.Businessdriverset"%>
<%@page import="es.sm2.openppm.core.model.impl.Businessdriver"%>
<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

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
  ~ File: job_category_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="job_category.name"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
// GLOBAL VARS
var jobCategoryValidator;

// OBJECTS
var jobCategoryPopup = {
		initialize: function() {
			$('div#jobCategoryPopup').dialog({
				autoOpen: false, 
				modal: true, 
				width: 500, 
				minWidth: 500, 
				resizable: false,
				open: function(event, ui) { jobCategoryValidator.resetForm(); }
			});
		},
		open: function () {
			$('div#jobCategoryPopup').dialog('open');
		},
		close: function() {
			$('div#jobCategoryPopup').dialog('close');
		},
		formValidator: function() {
			return  $("#jobCategoryPopupForm").validate({
				errorContainer: 'div#jobCategory-errors',
				errorLabelContainer: 'div#jobCategory-errors ol',
				wrapper: 'li',
				showErrors: function(errorMap, errorList) {
					$('span#jobCategory-numerrors').html(this.numberOfInvalids());
					this.defaultShowErrors();
				},
				rules: {
					<%= Jobcategory.NAME %>: "required",
				},
				messages: {
					<%= Jobcategory.NAME %>: { required: '${fmtNameRequired}' },
				}
			});
		},
		save: function(){
			if (jobCategoryValidator.form()) {
				jobCategory.save();
			}
		},
		formReset: function(form) {
			form.reset();
			// los hidden se tienen que resetear a mano
			form.<%= Jobcategory.IDJOBCATEGORY %>.value = '';
		}
};

readyMethods.add(function() {
	jobCategoryPopup.initialize();
	
	jobCategoryValidator = jobCategoryPopup.formValidator();
});

</script>

<div id="jobCategoryPopup" class="popup">

	<div id="jobCategory-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="jobCategory-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="jobCategoryPopupForm" id="jobCategoryPopupForm" method="post" action="<%=MaintenanceServlet.REFERENCE%>" >
		<input type="hidden" name="accion" value="" />
		<input type="hidden" name="idManten" value="<%= Constants.MANT_JOB_CATEGORY %>" />
		<input type="hidden" name="<%= Jobcategory.IDJOBCATEGORY %>" id="<%= Jobcategory.IDJOBCATEGORY %>" value="" />
		
		<fieldset>
			<legend><fmt:message key="job_category"/></legend>
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="20%"><fmt:message key="job_category.name"/>*</th>
					<th class="left" width="80%"><fmt:message key="job_category.description"/></th>
				</tr>
				<tr>
					<td>
						<input type="text" name="<%= Jobcategory.NAME %>" class="campo" maxlength="20" style="width: 103px;" />
					</td>
					<td>
						<input type="text" name="<%= Jobcategory.DESCRIPTION %>" class="campo" maxlength="50" />
					</td>
				</tr>
    		</table>
    	</fieldset>
    	
    	<div class="popButtons">
   			<input type="submit" class="boton" onclick="jobCategoryPopup.save(); return false;" value="<fmt:message key="save" />" />
			<a href="javascript:jobCategoryPopup.close();" class="boton"><fmt:message key="close" /></a>
   		</div>
    		
    </form>
</div>