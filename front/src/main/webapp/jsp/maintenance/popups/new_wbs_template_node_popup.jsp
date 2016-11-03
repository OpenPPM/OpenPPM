<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Wbstemplate"%>
<%@page import="es.sm2.openppm.core.model.impl.Company"%>
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
  ~ File: new_wbs_template_node_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:05
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtParentRequired">
	<fmt:param><b><fmt:message key="wbs.parent"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="wbs.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.info.updated_wbs" var="fmtUpdateWBS"></fmt:message>
<fmt:message key="msg.info.updated_wbs_budget" var="fmtUpdateWBSEV">
	<fmt:param><fmt:message key="activity.ev"/></fmt:param>
	<fmt:param><fmt:message key="percent_complete"/></fmt:param>
</fmt:message>


<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>

<script type="text/javascript">

var wbsNodeValidator;

function saveWbsNode() {
	if (wbsNodeValidator.form()) {
		wbsTemplateObject.saveChildWBSTemplate();
	}
}

function closeWbsNode() {
	$('div#wbsnode-popup').dialog('close');
}

readyMethods.add(function() {
	
	$('div#wbsnode-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 500, 
		minWidth: 500, 
		resizable: false,
		open: function(event, ui) { wbsNodeValidator.resetForm(); }
	});
	
	wbsNodeValidator = $("#frm_wbsnode").validate({
		errorContainer: 'div#wbsnode-errors',
		errorLabelContainer: 'div#wbsnode-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#wbsnode-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			wbs_name: { required: true },
		    wbs_parent: { required: true },
		    wbs_desc: { maxlength: 2500 }
		},
		messages: {
			wbs_name: { required: '${fmtNameRequired}' },
		    wbs_parent: { required: '${fmtParentRequired}' }
		}
	});

});
</script>

<div id="wbsnode-popup" class="popup">

	<div id="wbsnode-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="wbsnode-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_wbsnode" id="frm_wbsnode" method="post" action="<%=MaintenanceServlet.REFERENCE%>" >
		<input type="hidden" name="id" value="" />
		<input type="hidden" name="wbs_id" id="wbs_id" value="" />		
		<input type="hidden" name="accion" value="" />
		
		<%-- WBS NODO --%>
		<div id="wbsNodo">
			<fieldset>
				<legend><fmt:message key="wbs_node"/></legend>
			
				<table border="0" cellpadding="2" cellspacing="1" width="100%">
					<tr>
						<th class="left" width="65%"><fmt:message key="wbs.parent"/></th>
						<th class="left" width="35%"><fmt:message key="wbs.code"/></th>
					</tr>
					<tr>
						<td>
							<select name="wbs_parent" id="wbs_parent" class="campo"></select>
						</td>
						<td>
							<input type="text" name="wbs_code" class="campo" maxlength="10" />
						</td>
					</tr>
					<tr>
						<th class="left"><fmt:message key="wbs.name"/>&nbsp;*</th>
						<th class="left"><fmt:message key="wbs.ca_long"/></th>
					</tr>
					<tr>
						<td>
							<input type="text" name="wbs_name" class="campo" maxlength="80" />
						</td>
						<td align="center">
							<input style="width:20px; vertical-align: top;" type="checkbox" name="wbs_ca" id="wbs_ca"/>
						</td>
					</tr>

					<tr><th class="left" colspan="2"><fmt:message key="wbs.dictionary"/></th></tr>
					<tr>
						<td colspan="2">
							<textarea name="wbs_desc" class="campo" rows="10" style="width:98%;" maxlength="2500"></textarea>
						</td>
					</tr>
	    		</table>
	    	</fieldset>
	    	
	    	<div class="popButtons">
    			<input type="submit" class="boton" onclick="saveWbsNode(); return false;" value="<fmt:message key="save" />" />
				<a href="javascript:closeWbsNode();" class="boton"><fmt:message key="close" /></a>
    		</div>
    		
	    </div>
    </form>
</div>