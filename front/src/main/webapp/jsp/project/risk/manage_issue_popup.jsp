<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>

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
  ~ File: manage_issue_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<div id="issue-popup" class="popup">

	<div id="issue-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="issue-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_issue" id="frm_issue">
		<fieldset>
			<legend><fmt:message key="issue"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="34%"><fmt:message key="issue.raise_date"/>&nbsp;*</th>
					<th class="left" width="33%"><fmt:message key="issue.due_date"/>&nbsp;*</th>
					<th class="left" width="33%"><fmt:message key="issue.close_date"/></th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="issue_id" />
						<input type="text" name="date_logged" id="issue_date_logged" class="campo fecha" />
					</td>
					<td><input type="text" name="target_date" id="issue_target_date" class="campo fecha" /></td>
					<td><input type="text" name="date_closed" id="issue_date_closed" class="campo fecha" /></td>
				</tr>
				<tr>
					<th colspan="2" class="left"><fmt:message key="issue.assigned_to"/>&nbsp;*</th>
					<th class="left"><fmt:message key="issue.rating"/>&nbsp;*</th>
					
				</tr>
				<tr>
					<td colspan="2"><input type="text" name="assigned_to" id="issue_assigned_to" class="campo" maxlength="100" /></td>
					<td>
						<select name="priority" id="issue_priority" class="campo">
							<option value=""><fmt:message key="select_opt" /></option>
							<option value="<%=Constants.PRIORITY_HIGH %>"><fmt:message key="priority.high" /></option>
							<option value="<%=Constants.PRIORITY_MEDIUM %>"><fmt:message key="priority.medium" /></option>
							<option value="<%=Constants.PRIORITY_LOW %>"><fmt:message key="priority.low" /></option>
						</select>
					</td>
				</tr>
                <tr>
                    <th colspan="3" class="left"><fmt:message key="issue.owner"/></th>
                </tr>
                <tr>
                    <td colspan="3"><input type="text" name="owner" id="owner" class="campo" maxlength="100" /></td>
                </tr>
                <tr>
					<th colspan="3" class="left"><fmt:message key="issue.description"/></th>
				</tr>
				<tr>
					<td colspan="3"><textarea name="description" id="issueDescription" class="campo" style="width:98%;" rows="6"></textarea></td>
				</tr>				
				<tr>
					<th colspan="3" class="left"><fmt:message key="issue.resolution"/></th>
				</tr>
				<tr>
					<td colspan="3"><textarea name="resolution" id="resolution" class="campo" style="width:98%;" rows="6"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	<c:if test="${op:hasPermission(user,project.status,tab)}">
    		<div class="popButtons">
    			<input type="submit" class="boton" onclick="saveIssue(); return false;" value="<fmt:message key="save" />" />
    		</div>
    	</c:if>    	
    </form>
</div>