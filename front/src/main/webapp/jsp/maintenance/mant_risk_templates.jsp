<%@page import="es.sm2.openppm.core.model.impl.Riskregistertemplate"%>
<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>
<%@page import="es.sm2.openppm.front.servlets.RiskTemplatesServlet"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
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
  ~ File: mant_risk_templates.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:08
  --%>

<%-- Variables --%>
<c:set var="closed"  value="<%=Constants.CHAR_CLOSED%>" />
<c:set var="open"  value="<%=Constants.CHAR_OPEN%>" />

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msg_confirm_delete">
	<fmt:param><fmt:message key="risk_template"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="msg_title_confirm_delete">
	<fmt:param><fmt:message key="risk_template"/></fmt:param>
</fmt:message>

<%-- 
Request Attributes:
	list_maintenance: list
--%>

<script language="javascript" type="text/javascript" >
// GLOBAL VARS
var riskTemplatesAjax = new AjaxCall('<%= RiskTemplatesServlet.REFERENCE %>','<fmt:message key="error"/>');
var riskTemplatesTable;

// OBJECTS
var riskTemplates = {
	form: function() {
		
		return document.riskTemplatesForm;
	},
	createTable: function() {

        calculateRiskRating();

		return $('#riskTemplatesTable').dataTable({
			"pepito": -1,
			"sPaginationType": "full_numbers",
			"oLanguage": datatable_language,
			"bInfo": false,
			"aaSorting": [[1,'asc'], [2,'asc']],
			"aoColumns": [ 
				{ "sClass": "center", "bVisible": false },
				{ "sClass": "left" },
				{ "sClass": "left" },
				{ "sClass": "center"},
				{ "sSortDataType": "dom-div", "sType": "numeric" },
				{ "sClass": "center"},
				{ "sClass": "center"},
				{ "sClass": "center", "bSortable": false}
	      	]
		});
	},
	add: function() {
		
		this.formReset();
		
		var f = this.form();
		
		f.accion.value = "<%= RiskTemplatesServlet.VIEW_RISK_TEMPLATE %>";
		
		this.call();
	},
	view: function(element) {
		
		var row = riskTemplatesTable.fnGetData(element.parentNode.parentNode);
		
		var f = this.form();
		
		f.<%= Riskregistertemplate.IDRISK %>.value 	= row[0];
		f.accion.value 								= "<%= RiskTemplatesServlet.VIEW_RISK_TEMPLATE %>";
		
		this.call();
	},
	deleteRT: function(element) {
		
		confirmUI(
				'${msg_title_confirm_delete}','${msg_confirm_delete}',
				'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
				function() {
					var row = riskTemplatesTable.fnGetData(element.parentNode.parentNode);
					
					var params={
						accion: 								"<%= RiskTemplatesServlet.JX_DELETE_RISK_TEMPLATE %>",
						<%= Riskregistertemplate.IDRISK %>: 	row[0]
					};
					
					riskTemplatesAjax.call(params, function(data){
						riskTemplatesTable.fnDeleteSelected();
					});	
				}
		);
	},
	formReset: function() {
		
		var f = this.form();
		f.reset();
		// los hidden se tienen que resetear a mano
		f.<%= Riskregistertemplate.IDRISK %>.value 	= '';
		f.accion.value 								= '';
	},
	call: function() {
		
		var f = this.form();
		f.action = "<%= RiskTemplatesServlet.REFERENCE %>";
		loadingPopup();
		f.submit();
	}
};

function calculateRiskRating() {

    $.each($(".rating"), function(index, value ) {

        // Get data
        var riskImpact      = $(value.getElementsByClassName("riskImpact"));
        var riskProbability = $(value.getElementsByClassName("riskProbability"));

        // Operation
        var riskRating = parseInt($(riskImpact).html()) * parseInt($(riskProbability).html());

        // Print result
        $.each($(value.getElementsByClassName("riskValue")), function(index, value ) {

            if (index === 0) {
                $(value).html(riskRating);
            }
            else {
                $(value).html("(" + riskRating + ")");
            }
        });

        // Add class
        $(value.getElementsByClassName("riskColor")).addClass(getClassRating(riskRating));
    });
}

function getClassRating(riskRating) {

    var classRating = 'risk_low';

    // Parse
    if (typeof riskRating === 'string') {
        riskRating = parseInt(riskRating);
    }

    // Class
    if (riskRating > 1500) {
        classRating = 'risk_high';
    }
    else if (riskRating > 500) {
        classRating = 'risk_medium';
    }
    else if (riskRating == 0) {
        classRating = '';
    }

    return classRating;
}

readyMethods.add(function () {

	/* Create an array with the values of all the div boxes in a column */
	$.fn.dataTableExt.afnSortData['dom-div'] = function  ( oSettings, iColumn )
	{
		var aData 	= [];
		var cont 	= 0;
		
		for (var i = 0; i < iColumn; i++) {
			
			if (oSettings.aoColumns[i].bVisible == false) {
				cont += 1;
			}
		}
		
		$('td:eq('+(iColumn-cont)+') .riskValue.order', oSettings.oApi._fnGetTrNodes(oSettings) ).each( function () {
			aData.push( $(this).html() );
		});
		
		return aData;
	};
	
	riskTemplatesTable = riskTemplates.createTable();
	
	$('#riskTemplatesTable tbody tr').live('click',  function (event) {
		riskTemplatesTable.fnSetSelectable(this,'selected_internal');
	});
});

</script>

<form id="riskTemplatesForm" name="riskTemplatesForm" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="<%= Riskregistertemplate.IDRISK %>" id="<%= Riskregistertemplate.IDRISK %>" value="" />
	<fieldset>
		<legend><fmt:message key="maintenance.risk_templates" /></legend>
		<%-- Risk templates table --%>
		<table id="riskTemplatesTable" class="tabledata" width="100%">
			<thead>
				<tr>
					<th width="0%">&nbsp;</th>
					<th width="9%"><fmt:message key="risk.code"/></th>
					<th width="10%"><fmt:message key="risk.name"/></th>
					<th width="10%"><fmt:message key="risk.status"/></th>
					<th width="10%"><fmt:message key="risk.rating"/></th>
					<th width="11%"><fmt:message key="risk.response"/></th>
					<th width="30%"><fmt:message key="risk.description"/></th>
					<th width="8%">
						<img onclick="riskTemplates.add()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
					</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="template" items="${list}">
					<tr>
						<td>${template.idRisk}</td>
						<td>${template.riskCode}</td>
						<td>${template.riskName}</td>
						<td>
							<c:choose>
								<c:when test="${template.status == closed}">
									<%=Constants.CLOSED%>
								</c:when>
								<c:when test="${template.status == open}">
									<%=Constants.OPEN%>
								</c:when>
							</c:choose>
						</td>
						<td>
							<div style="margin: 0px auto; width: 80px;" class="rating">
                                <div style="display:none;" class="riskValue order"></div> <!-- order -->
                                <div style="display:none;" class="riskProbability">${template.probability}</div>
                                <div style="display:none;" class="riskImpact">${template.impact}</div>
                                <div style="width: 25%; float: left; margin-left: 10px;" class="riskColor">&nbsp;</div>
                                <div style="float: left; margin-left: 10px;" class="riskValue"></div>
							</div>
						</td>
						<td>${tl:escape(template.riskcategories.description)}</td>						
						<td>${tl:escape(template.description)}</td>
						<td>
							<img onclick="riskTemplates.view(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">
							&nbsp;&nbsp;&nbsp;
							<img onclick="riskTemplates.deleteRT(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</fieldset>	
	
	<div class="spacer"></div>
</form>

<%-- Charge plugins --%>
<div id="plugin"></div>