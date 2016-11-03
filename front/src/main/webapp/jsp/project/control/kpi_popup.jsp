<%@page import="es.sm2.openppm.core.model.impl.Contact"%>
<%@page import="es.sm2.openppm.core.model.impl.Employee"%>
<%@page import="es.sm2.openppm.core.model.impl.Historickpi"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectControlServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectkpi"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
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
  ~ File: kpi_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:59
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtValueRequired">
	<fmt:param><b><fmt:message key="kpi.value"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.maximum_allowed" var="fmtValueMax">
	<fmt:param><b><fmt:message key="kpi.value"/></b></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var kpiValidator, historicKpiTable;
var kpiTable;

function calculatedColor(normalizedValue, lowerThreshold, upperThreshold, value) {
	
	var riskColor;
	
	if(normalizedValue == 100){
		riskColor = 'risk_low';
	}
	else if (normalizedValue == 0 && lowerThreshold != value) { 
		riskColor = 'risk_high'; 
	}
	else{
		riskColor = 'risk_medium';
	}
	
	return riskColor;
}

function editKpi(element) {

	var aData = kpiTable.fnGetData( element.parentNode.parentNode );
	
	var f = document.frm_kpi;
	f.reset();
	
	f.<%=Projectkpi.IDPROJECTKPI%>.value	= aData[0];
	f.<%=Projectkpi.UPPERTHRESHOLD%>.value	= aData[5];
	f.<%=Projectkpi.LOWERTHRESHOLD%>.value	= aData[6];
	f.<%=Projectkpi.VALUE%>.value			= aData[7];
	f.adjustedValue.value					= aData[8];
	f.<%=Projectkpi.WEIGHT%>.value			= aData[9];
	$('#oldTotalScore').val(aData[10]);

	var params = {
		accion: "<%=ProjectControlServlet.JX_HISTORIC_KPI %>",
		<%=Projectkpi.IDPROJECTKPI%>: aData[0]
	};
	
	historicKpiTable.fnClearTable();
	
	controlAjax.call(params, function(data) {
		
		$(data).each(function() {

			var kpi = [
				toCurrency(this.<%=Historickpi.UPPERTHRESHOLD%>),
				toCurrency(this.<%=Historickpi.LOWERTHRESHOLD%>),
				toCurrency(this.<%=Historickpi.WEIGHT%>),
				toCurrency(this.<%=Historickpi.VALUEKPI%>),
				this.<%=Historickpi.EMPLOYEE%>.<%=Employee.CONTACT%>.<%=Contact.FULLNAME%>,
                this.<%=Historickpi.UPDATEDTYPE%>,
				this.<%=Historickpi.ACTUALDATE%>
			];
			
			historicKpiTable.fnAddData(kpi);
		});
		$('#kpi-popup').dialog('open');
	});
}

function updateKpi() {

	var f = document.frm_kpi;
	
	if (kpiValidator.form()) {
		
		var params = {
			accion: "<%=ProjectControlServlet.JX_UPDATE_KPI%>",
			adjustedValue: f.adjustedValue.value,
			idProject: f.<%=Project.IDPROJECT%>.value ,
			idProjectKpi: f.<%=Projectkpi.IDPROJECTKPI%>.value,
			lowerThreshold: parseFloat(toNumber(f.<%=Projectkpi.LOWERTHRESHOLD%>.value)),
			oldTotalScore: parseFloat(toNumber(f.oldTotalScore.value)),
			upperThreshold: parseFloat(toNumber(f.<%=Projectkpi.LOWERTHRESHOLD%>.value)),
			value: parseFloat(toNumber(f.<%=Projectkpi.VALUE%>.value)),
			weight: parseFloat(toNumber(f.<%=Projectkpi.WEIGHT%>.value))
		};
		
		controlAjax.call(params, function (data) {

			var f = document.frm_kpi;
			
			var dataRow = kpiTable.fnGetSelectedData();
			
			dataRow[2]	= '<span class="'+calculatedColor(data.adjustedValue, f.<%=Projectkpi.LOWERTHRESHOLD%>.value, f.<%=Projectkpi.UPPERTHRESHOLD%>.value, f.<%=Projectkpi.VALUE%>.value)+'">&nbsp;&nbsp;&nbsp;</span>';			
			dataRow[7] 	= f.<%=Projectkpi.VALUE%>.value;
			dataRow[8] 	= toCurrency(parseFloat(data.adjustedValue));
			dataRow[10] = toCurrency(data.score);
			
			kpiTable.fnUpdateRow(dataRow);
			
			//update totals values
			$('#totalScore').text(toCurrency(parseFloat($('#totalScore').text()) - parseFloat($('#oldTotalScore').val()) + parseFloat(data.score)));
			
			if (!data.thereKPISEmpty) {
				
				$('#totalValue').text($('#totalScore').text());
				
				$('#projectColorRAG span').attr("class", data.kpiStatus);
			}
			
			$('#kpi-popup').dialog('close');
		});
	}
}

readyMethods.add(function() {
	$('div#kpi-popup').dialog({ autoOpen: false, modal: true, width: 700, resizable: false });

	kpiValidator = $("#frm_kpi").validate({
		errorContainer: 'div#kpi-errors',
		errorLabelContainer: 'div#kpi-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#kpi-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Projectkpi.VALUE%>: { required: true , maxlength: <%=Constants.MAX_CURRENCY%> }
		},
		messages: {
			<%=Projectkpi.VALUE%>: { required: '${fmtValueRequired}', maxlength:'${fmtValueMax}'}
		}
	});
	
	historicKpiTable = $('#tb_historicKpi').dataTable({
		"sPaginationType": "full_numbers",
		"iDisplayLength": 25,
		"oLanguage": datatable_language,
		"aaSorting": [[6,'desc']],
		"aoColumns": [ 
             { "sClass": "right" }, 
             { "sClass": "right" }, 
             { "sClass": "right" }, 
             { "sClass": "right" },
             { "sClass": "center" },
             { "sClass": "center" },
             { "sClass": "center" , sType: "date-euro"}
    	]
	});
});
//-->
</script>

<div id="kpi-popup" class="popup">
	<div id="kpi-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="kpi-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>
	<form name="frm_kpi" id="frm_kpi" >
		<input type="hidden" name="accion" value="<%=ProjectControlServlet.JX_UPDATE_KPI%>"/>
		<input type="hidden" name="<%=Project.IDPROJECT%>" value="${project.idProject}"/>
		<input type="hidden" name="<%=Projectkpi.IDPROJECTKPI%>"/>
		<input type="hidden" name="adjustedValue"/>
		<input type="hidden" id="oldTotalScore" name="oldTotalScore"/>
		
		<fieldset>
			<legend><fmt:message key="kpi" /></legend>
		
			<table cellspacing="1" width="100%">
				<tr>
					<th width="25%"><fmt:message key="kpi.upper_threshold" /></th>
					<th width="25%"><fmt:message key="kpi.lower_threshold" /></th>
					<th width="25%"><fmt:message key="kpi.weight" />&nbsp;%</th>
					<th width="25%"><fmt:message key="kpi.value" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" class="center" name="<%=Projectkpi.UPPERTHRESHOLD%>" readonly/></td>
					<td><input type="text" class="center" name="<%=Projectkpi.LOWERTHRESHOLD%>" readonly/></td>
					<td><input type="text" class="center" name="<%=Projectkpi.WEIGHT%>" readonly/></td>
					<td><input type="text" class="campo importe" name="<%=Projectkpi.VALUE%>"/></td>
				</tr>
    		</table>
    	</fieldset>
    	<fmt:message key="kpi.historic" var="hitoricKpiName"/>
    	<visual:panel id="fieldHistoricKpi" title="${hitoricKpiName }">
			<table id="tb_historicKpi" class="tabledata" cellspacing="1" width="100%">
				<thead>
					<tr>
						<th width="10%"><fmt:message key="kpi.upper_threshold" /></th>
						<th width="10%"><fmt:message key="kpi.lower_threshold" /></th>
						<th width="10%"><fmt:message key="kpi.weight" />&nbsp;%</th>
						<th width="10%"><fmt:message key="kpi.value" /></th>
						<th width="30%"><fmt:message key="user" /></th>
                        <th width="10%"><fmt:message key="updatedtype" /></th>
						<th width="20%"><fmt:message key="date" /></th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
    	</visual:panel>
   		<div class="popButtons">
			<input type="submit" onclick="javascript:updateKpi(); return false;" class="boton" value="<fmt:message key="update"/>" />
   		</div>
    </form>
</div>