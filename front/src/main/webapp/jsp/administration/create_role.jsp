<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.servlets.AdministrationServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="Utilidades" prefix="tl" %>

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
  ~ File: create_role.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:25
  --%>

<%HashMap<String, String> settings = SettingUtil.getSettings(request);%>

<script>

<%-- 
Request Attributes:
	role: admin, pmo, etc
--%>

</script>
	

<%-- Selection --%>
<div id="selection_${param.role}" style="text-align: center; display:block;">
	<fieldset>
		<legend><fmt:message key="wbs.selection"/></legend>
		<fmt:message key="new_contact"/>:&nbsp;<input type="radio" name="options_create_${param.role}" value="create" style="margin-right: 10px; width: 15px;" /> 
		<fmt:message key="search_contact"/>:&nbsp;<input type="radio" name="options_create_${param.role}" value="select" style="width:15px;" />
	</fieldset>
</div>

<%-- Space --%>
<div style="width: 100px; display:block">&nbsp;</div>

  	<%-- Create role --%>
  	<div id="new_${param.role}" class="band ${param.role}" style="display:none">
  		<fieldset>
			<legend><fmt:message key="new_contact"/></legend>
		   	<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					 <th><fmt:message key="maintenance.contact.full_name" />&nbsp;*</th>
					 <th><fmt:message key="maintenance.contact.file_as" />&nbsp;*</th>
					 <th><fmt:message key="maintenance.contact.job_title" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="full_name_${param.role}" id="contact_full_name_${param.role}" class="campo" maxlength="50"/></td>
					<td><input type="text" name="file_as_${param.role}" id="contact_file_as_${param.role}" class="campo" maxlength="60"/></td>
					<td><input type="text" name="job_title_${param.role}" id="contact_job_title_${param.role}" class="campo" maxlength="50"/></td>
				</tr>
				<tr>
					 <th colspan="2"><fmt:message key="maintenance.contact.business_address" /></th>
					 <th><fmt:message key="maintenance.contact.business_phone" /></th>
				</tr>
				<tr>
					<td colspan="2">
						<input type="text" name="business_address_${param.role}" id="contact_business_address_${param.role}" class="campo" maxlength="200"/>
					</td>
					<td>
						<input type="text" name="business_phone_${param.role}" id="contact_business_phone_${param.role}" class="campo" maxlength="12"/>
					</td>
				</tr>
				<tr>
					 <th><fmt:message key="maintenance.contact.username" />&nbsp;*</th>
					 <th><fmt:message key="maintenance.contact.mobile_phone" /></th>
					 <th><fmt:message key="maintenance.contact.email" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" class="campo" name="login_${param.role}" id="login_name_${param.role}" maxlength="20"/></td>
					<td>
						<input type="text" name="mobile_phone_${param.role}" id="contact_mobile_phone_${param.role}" class="campo" maxlength="12"/>
					</td>
					<td>
						<input type="text" name="email_${param.role}" id="contact_email_${param.role}" class="campo" maxlength="50"/>
					</td>
				</tr>
				<tr><th>&nbsp;</th></tr>
				<tr>
					 <th colspan="3"><fmt:message key="maintenance.contact.notes" /></th>
				</tr>			
				<tr>
					<td colspan="3">
						<textarea id="contact_notes_${param.role}" name="contact_notes_${param.role}" class="campo" rows="5" style="width:98%;"></textarea>
					</td>
				</tr>
	   		</table>
   		</fieldset>	
  	</div>
  	
	<%-- Select role --%>
	<div id="select_${param.role}" class="band ${param.role}" style="display:none">
  		<div id="employees-popup">
			<div id="frm_employee_pop">
				<fieldset>
					<legend><fmt:message key="search_contact"/></legend>
					<table border="0" cellpadding="2" cellspacing="1" width="100%">
						<tr>
							<th width="50%" align="left">
								<fmt:message key="maintenance.employee.contact" />&nbsp;*
								<a class="button_img" href="javascript:searchContactPop('idContact_${param.role}');" >
									<span class="normal"><fmt:message key="search"/></span>
									<img style="width:16px;padding-left: 5px;" src="images/search.png"/>
								</a>
							</th>
						</tr>
						<tr>
							<td>
								<input type="hidden" id="idContact_${param.role}" name="idContact_${param.role}" />
								<input type="text" id="idContact_${param.role}_name" name="idContact_name_${param.role}" class="campo" readonly="readonly" style="width:15%;"/>
							</td>
						</tr>				
		    		</table>
		    	</fieldset>
		    </div>
  		</div>
  	</div>
