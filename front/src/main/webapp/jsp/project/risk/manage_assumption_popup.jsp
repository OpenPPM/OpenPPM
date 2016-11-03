<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>

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
  ~ File: manage_assumption_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<div id="assumption-popup" class="popup">

	<div id="assumption-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="assumption-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_assumption" id="frm_assumption">
		<fieldset>
			<legend><fmt:message key="assumption"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="25%"><fmt:message key="assumption.code"/>&nbsp;*</th>
					<th class="left" width="75%"><fmt:message key="assumption.name"/></th>
				</tr>
				<tr>
					<td>
						<input type="hidden" name="assumption_id" />
						<input type="text" name="assumption_code" id="assumption_code" class="campo" maxlength="5" />
					</td>
					<td><input type="text" name="name" class="campo" maxlength="50" /></td>
				</tr>
				<tr>
					<th colspan="2" class="left"><fmt:message key="assumption.originator"/></th>
				</tr>
				<tr>
					<td colspan="2"><input type="text" name="originator" class="campo" maxlength="100" /></td>
				</tr>
				<tr>
					<th colspan="2" class="left"><fmt:message key="assumption.description"/>&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="2"><textarea name="assumption_description" id="assumption_description" class="campo" style="width:98%;" rows="6"></textarea></td>
				</tr>
				<tr class="hide">
					<th colspan="2" class="left"><fmt:message key="assumption.doc"/></th>
				</tr>
				<tr class="hide">
					<td colspan="2"><input type="text" name="doc" class="campo" maxlength="100" /></td>
				</tr>
				<tr>
					<td colspan="2">
						<fmt:message key="change_log" var="titleChangeLog"/>
						<visual:panel id="field_assumption_log_control" title="${titleChangeLog }">
							<table id="tb_assumption_logs" class="tabledata" cellspacing="1" width="100%">
								<thead>
									<tr>
										<th width="0%" class="cabecera_tabla"></th>
										<th width="11%" class="cabecera_tabla"><fmt:message key="date"/></th>
										<th width="42%" class="cabecera_tabla"><fmt:message key="assumption_log.description_change"/></th>
										<th width="8%" class="cabecera_tabla">
											<c:if test="${not op:isStringEquals(project.status, status_closed) and op:hasPermission(user,project.status,tab)}">
												<img onclick="addLogAssumption();" title="<fmt:message key="add"/>" class="link" src="images/add.png">
											</c:if>
										</th>
									</tr>
								</thead>
								<tbody></tbody>
							</table>
						</visual:panel>
					</td>
				</tr>
    		</table>
    	</fieldset>
    	<c:if test="${op:hasPermission(user,project.status,tab)}">
    		<div class="popButtons">
	    		<input type="submit" class="boton" onclick="saveAssumption(); return false;" value="<fmt:message key="save" />" />
    		</div>
    	</c:if>    	
    </form>
</div>

<div id="assumption-log-popup" class="popup">

	<div id="assumption-log-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="assumption-log-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_assumption_log" id="frm_assumption_log">
		<fieldset>
			<legend><fmt:message key="assumption_log"/></legend>
		
			<table border="0" cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th class="left" width="30%"><fmt:message key="date"/>&nbsp;*</th>
					<td width="70%">
						<input type="hidden" name="assumption_log_id" />
						<input type="text" name="assumption_log_date" id="assumption_log_date" class="campo fecha" />
					</td>
				</tr>
				<tr>
					<th class="left" colspan="2"><fmt:message key="assumption_log.description_change"/>&nbsp;*</th>
				</tr>
				<tr>
					<td colspan="2"><textarea name="assumption_log_change" id="assumption_log_change" class="campo" style="width:98%;" rows="6"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	<c:if test="${op:hasPermission(user,project.status,tab)}">
    		<div class="popButtons">
    			<input type="submit" class="boton" onclick="saveLogAssumption(); return false;" value="<fmt:message key="save" />" />
    		</div>
    	</c:if>    	
    </form>
</div>