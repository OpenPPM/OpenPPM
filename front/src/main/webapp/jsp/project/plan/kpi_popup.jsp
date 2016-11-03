<%@page import="es.sm2.openppm.core.model.impl.Metrickpi"%>
<%@page import="es.sm2.openppm.core.model.impl.Projectkpi"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectPlanServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
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
  ~ File: kpi_popup.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtMetricRequired">
	<fmt:param><b><fmt:message key="kpi.metric"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtUpperRequired">
	<fmt:param><b><fmt:message key="kpi.upper_threshold"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtLowerRequired">
	<fmt:param><b><fmt:message key="kpi.lower_threshold"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtWeightRequired">
	<fmt:param><b><fmt:message key="kpi.weight"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.out_of_range" var="fmtWeightOutOfRange">
	<fmt:param><b><fmt:message key="kpi.weight"/></b></fmt:param>
	<fmt:param>0 - 100</fmt:param>
</fmt:message>
<fmt:message key="msg.error.not_equal_to" var="fmtLowerNotEqualTo">
	<fmt:param><b><fmt:message key="kpi.lower_threshold"/></b></fmt:param>
	<fmt:param><b><fmt:message key="kpi.upper_threshold"/></b></fmt:param>
</fmt:message>

<%-- Message for confirmation --%>
<fmt:message key="msg.confirm_delete" var="fmtKpiDeleteMsg">
	<fmt:param><fmt:message key="kpi"/></fmt:param>
</fmt:message>
<fmt:message key="msg.title_confirm_delete" var="fmtKpiDeleteTitle">
	<fmt:param><fmt:message key="kpi"/></fmt:param>
</fmt:message>

<script type="text/javascript">
<!--
var kpiValidator;

function addKpi() {
	var f = document.frm_kpi;
	f.reset();
	
	//no se hace el reset de los input hidden, hay que hacerlo a mano
	f.<%=Projectkpi.IDPROJECTKPI%>.value 	= '';
	f.<%=Metrickpi.IDMETRICKPI%>.value		= '';
	f.<%= Metrickpi.NAME %>.value			= '';
	f.<%=Metrickpi.DEFINITION%>.value 		= '';
	f.<%= Projectkpi.SPECIFICKPI %>.value 	= '';
	
	$('#tempWeight').val('0');
	$('#searchMetric').show();
	$("#<%= Projectkpi.AGGREGATEKPI %>").removeAttr("checked");
	$('#metricName').prop("readonly",true);
	
	$('#kpi-popup').dialog('open');
}

function editKpi(element) {
	var aData = kpiTable.fnGetData( element.parentNode.parentNode.parentNode );
	
	var f = document.frm_kpi;
	f.reset();
	
	$('#tempWeight').val(aData[6]);
	
	f.<%=Projectkpi.IDPROJECTKPI%>.value		= aData[0];
	f.<%=Metrickpi.IDMETRICKPI%>.value			= aData[1];
	f.<%= Metrickpi.NAME %>.value 				= aData[2];
	f.<%= Metrickpi.DEFINITION %>.value 		= aData[3];
	f.<%=Projectkpi.UPPERTHRESHOLD%>.value		= aData[4];
	f.<%=Projectkpi.LOWERTHRESHOLD%>.value		= aData[5];
	f.<%=Projectkpi.WEIGHT%>.value				= aData[6];
	f.<%= Projectkpi.SPECIFICKPI %>.value 		= aData[7];
	if(aData[7].length == 0){
		f.metricOrSpecific.value 				= 'metric';
		f.metricName.value						= f.<%= Metrickpi.NAME %>.value;
		$('#metricName').prop("readonly",true);
		$('#searchMetric').show();
	}else{
		f.metricOrSpecific.value 				= 'specific';
		f.metricName.value						= f.<%= Projectkpi.SPECIFICKPI %>.value;
		$('#metricName').prop("readonly",false);
		$('#searchMetric').hide();
	}
	
	f.<%=Projectkpi.AGGREGATEKPI%>.value		= aData[9]; 
	
	if(f.<%=Projectkpi.AGGREGATEKPI%>.value == "true"){
		$("#<%= Projectkpi.AGGREGATEKPI %>").attr("checked","checked");
	}else{
		$("#<%= Projectkpi.AGGREGATEKPI %>").removeAttr("checked");
	}
	
	$('#kpi-popup').dialog('open');
}

function saveKpi() {

	var f = document.frm_kpi;
	
	if (kpiValidator.form()) {
	
		f.<%=Projectkpi.AGGREGATEKPI%>.value = $('#<%=Projectkpi.AGGREGATEKPI%>').prop('checked');
		
		if(f.metricOrSpecific.value == 'specific'){
			f.<%=Projectkpi.SPECIFICKPI%>.value = f.metricName.value;
			f.<%=Metrickpi.IDMETRICKPI%>.value 	= null; 
		}
		
		var params = {
			accion: "<%=ProjectPlanServlet.JX_SAVE_KPI%>",
			idProject: f.<%=Project.IDPROJECT%>.value,
			idProjectKpi: f.<%=Projectkpi.IDPROJECTKPI%>.value,
			idMetricKpi: f.<%=Metrickpi.IDMETRICKPI%>.value,
			upperThreshold: parseFloat(toNumber(f.<%=Projectkpi.UPPERTHRESHOLD%>.value)),
			lowerThreshold: parseFloat(toNumber(f.<%=Projectkpi.LOWERTHRESHOLD%>.value)),
			weight: parseFloat(toNumber(f.<%=Projectkpi.WEIGHT%>.value)),
			aggregateKpi: f.<%=Projectkpi.AGGREGATEKPI%>.value,
			specificKpi: f.<%=Projectkpi.SPECIFICKPI%>.value
		};
		
		planAjax.call(params, function (data) {

			var dataRow = [
				data.<%=Projectkpi.IDPROJECTKPI%>,
				f.<%=Metrickpi.IDMETRICKPI%>.value,
				f.metricName.value,
				f.<%=Metrickpi.DEFINITION%>.value,
				f.<%=Projectkpi.UPPERTHRESHOLD%>.value,
				f.<%=Projectkpi.LOWERTHRESHOLD%>.value,
				f.<%=Projectkpi.WEIGHT%>.value,
				f.<%=Projectkpi.SPECIFICKPI%>.value,
				(f.<%=Projectkpi.AGGREGATEKPI%>.value == "true"? '<input disabled="disabled" checked type="checkbox">' : '<input disabled="disabled" type="checkbox">'),
				f.<%=Projectkpi.AGGREGATEKPI%>.value,
				'<nobr><img onclick="editKpi(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteKpi(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
			];
			
			if (f.<%=Projectkpi.IDPROJECTKPI%>.value == '') { kpiTable.fnAddDataAndSelect(dataRow); }
			else { kpiTable.fnUpdateAndSelect(dataRow); }
			
			$('#totalWeight').text(toCurrency(parseFloat(toNumber($('#totalWeight').text())) - parseFloat(toNumber($('#tempWeight').val())) + parseFloat(toNumber(f.<%=Projectkpi.WEIGHT%>.value))));
			
			$('#kpi-popup').dialog('close');
		});
	}
}

function deleteKpi(element) {
	
	confirmUI(
		'${fmtKpiDeleteTitle}','${fmtKpiDeleteMsg}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			
			var aData = kpiTable.fnGetData( element.parentNode.parentNode.parentNode );
			
			planAjax.call({
				accion : '<%=ProjectPlanServlet.JX_DELETE_KPI %>',
				<%=Projectkpi.IDPROJECTKPI%>: aData[0],
				<%=Project.IDPROJECT%>: '${project.idProject}'
				},function(data) {
					
					$('#totalWeight').text(parseFloat($('#totalWeight').text()) - parseFloat(aData[6]));
					
					kpiTable.fnDeleteSelected();
			});
	});
}

readyMethods.add(function() {
	$('div#kpi-popup').dialog({ autoOpen: false, modal: true, width: 500, minWidth: 500, resizable: false });

	kpiValidator = $("#frm_kpi").validate({
		errorContainer: 'div#kpi-errors',
		errorLabelContainer: 'div#kpi-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#kpi-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			metricName: { required: true },
			<%=Projectkpi.WEIGHT%>: { required: true, rangeDouble: [0,100] },
			<%=Projectkpi.LOWERTHRESHOLD%>: { required: true, notEqualTo: '#<%=Projectkpi.UPPERTHRESHOLD%>'  },
			<%=Projectkpi.UPPERTHRESHOLD%>: { required: true }
		},
		messages: {
			metricName: { required: '${fmtMetricRequired}' },
			<%=Projectkpi.WEIGHT%>: { required: '${fmtWeightRequired}', rangeDouble: '${fmtWeightOutOfRange}' },
			<%=Projectkpi.LOWERTHRESHOLD%>: { required: '${fmtLowerRequired}', notEqualTo :'${fmtLowerNotEqualTo}' },
			<%=Projectkpi.UPPERTHRESHOLD%>: { required: '${fmtUpperRequired}' }
		}
	});	
	
	
	$('#metricOrSpecific').on('change', function() {
		if ($(this).val() == "metric") {
			$('#searchMetric').show();
			$('#metricName').prop("readonly",true);
			$('#metricName').val('');
		}
		else {
			$('#searchMetric').hide();
			$('#metricName').prop("readonly",false);
			$('#metricName').val('');
		}
	});
	
	$('#searchMetric').on('click', function() {
		searchMetricPop($('#<%=Project.IDPROJECT%>').val(),$('#metricName').val());
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
	<form name="frm_kpi" id="frm_kpi" method="post">
		<input type="hidden" id="<%=Project.IDPROJECT%>" name="<%=Project.IDPROJECT%>" value="${project.idProject}"/>
		<input type="hidden" id="<%=Projectkpi.IDPROJECTKPI%>" name="<%=Projectkpi.IDPROJECTKPI%>"/>
		<input type="hidden" id="<%=Metrickpi.IDMETRICKPI%>" name="<%=Metrickpi.IDMETRICKPI%>" />
		<input type="hidden" id="<%=Metrickpi.NAME%>" name="<%=Metrickpi.NAME%>" />
		<input type="hidden" id="<%=Metrickpi.DEFINITION%>" name="<%=Metrickpi.DEFINITION%>" />
		<input type="hidden" id="<%=Projectkpi.SPECIFICKPI%>" name="<%=Projectkpi.SPECIFICKPI%>" />
		<input type="hidden" id="tempWeight">
		
		<fieldset>
			<legend><fmt:message key="kpi" /></legend>
			<table cellspacing="1" width="100%">
				<tr>
					<th class="left"><fmt:message key="kpi.metric" />&nbsp;*</th>
					<th>&nbsp;</th>
					<th class="center"><fmt:message key="aggregate" />&nbsp;</th>
				</tr>
				<tr>
					<td class="left"> 
						<select id="metricOrSpecific" name="metricOrSpecific" class="campo" style="width: 100px;">
							<option value="metric"><fmt:message key="metric"/></option>
							<option value="specific"><fmt:message key="specific"/></option>
						</select>
						<img id="searchMetric" class="position_right icono" title='<fmt:message key="search"/>' src="images/search.png"/>
					</td>
					<td>
						<input id="metricName" class="campo" type="text" name="metricName" maxlength="100"/>
					</td>
					<td class="center">
						<input id="<%= Projectkpi.AGGREGATEKPI %>" type="checkbox" style="width:20px;" name="<%= Projectkpi.AGGREGATEKPI %>" />
					</td>
				</tr>
				<tr>
					<th>&nbsp;</th>
				</tr>
				<tr>
					<th width="34%" class="left"><fmt:message key="kpi.upper_threshold" />&nbsp;*</th>
					<th width="33%" class="left"><fmt:message key="kpi.lower_threshold" />&nbsp;*</th>
					<th width="33%" class="left"><fmt:message key="kpi.weight" />&nbsp;%&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" class="campo importe" name="<%=Projectkpi.UPPERTHRESHOLD%>" id="<%=Projectkpi.UPPERTHRESHOLD%>"/></td>
					<td><input type="text" class="campo importe" name="<%=Projectkpi.LOWERTHRESHOLD%>"/></td>
					<td><input type="text" class="campo importe" name="<%=Projectkpi.WEIGHT%>" /></td>
				</tr>
    		</table>
    	</fieldset>
   		<div class="popButtons">
   			<input type="submit" class="boton" onclick="saveKpi(); return false;" value="<fmt:message key="save"/>" />
   		</div>
    </form>
</div>