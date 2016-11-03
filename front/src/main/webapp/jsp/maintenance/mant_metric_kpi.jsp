<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.core.model.impl.Bscdimension"%>
<%@page import="es.sm2.openppm.core.model.impl.Metrickpi"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>

<%@page import="es.sm2.openppm.front.servlets.MaintenanceServlet"%>

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
  ~ File: mant_metric_kpi.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:48:07
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.confirm_delete" var="msgConfirmDelete">
	<fmt:param><fmt:message key="metric"/></fmt:param>
</fmt:message>

<fmt:message key="msg.title_confirm_delete" var="msgTitleConfirmDelete">
	<fmt:param><fmt:message key="metric"/></fmt:param>
</fmt:message>

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="metric.name"/></b></fmt:param>
</fmt:message>


<script language="javascript" type="text/javascript" >

var metricsTable;
var metricsValidator;

function addMetric() {
	var f = document.frm_metrics_pop;
	f.reset();
	f.<%=Metrickpi.IDMETRICKPI%>.value = '';
	$(f.<%=Metrickpi.DEFINITION%>).text('');
	$('#metrics-popup').dialog('open');
}

function saveMetric() {

	if (metricsValidator.form()) {
		
		mainAjax.call($("#frm_metrics_pop").serialize(), function (data) {
			
			var f	 = document.frm_metrics_pop;

			var $bsc    = $(f.<%= Bscdimension.IDBSCDIMENSION %>);
            var $type   = $(f.<%= Metrickpi.TYPE %>);
			
			var dataRow = [
				data.<%=Metrickpi.IDMETRICKPI%>,
				escape(f.<%=Metrickpi.NAME%>.value),
                escape(f.<%= Metrickpi.TYPE %>.value),
                $type.val() != ""?$type.find('option:selected').text():"",
				escape(f.<%=Metrickpi.DEFINITION%>.value),
				$bsc.val() != ""?$bsc.find('option:selected').text():"",
				f.<%=Bscdimension.IDBSCDIMENSION%>.value,
				'<nobr><img onclick="editMetric(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png">'+
				'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
				'<img onclick="deleteMetric(this);" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg"></nobr>'
			];

			if (f.<%=Metrickpi.IDMETRICKPI%>.value == "") { metricsTable.fnAddDataAndSelect(dataRow); } 
			else { metricsTable.fnUpdateAndSelect(dataRow); }
				
			$('#metrics-popup').dialog('close');
		});	
	}	
}

function editMetric(element) {
	
	var aData = metricsTable.fnGetData( element.parentNode.parentNode.parentNode );
	var f	  = document.frm_metrics_pop;
	f.reset();
	
	f.<%=Metrickpi.IDMETRICKPI%>.value		 = aData[0];
	f.<%=Metrickpi.NAME%>.value				 = unEscape(aData[1]);
    $(f.<%=Metrickpi.TYPE%>).val(aData[2]);
	$(f.<%=Metrickpi.DEFINITION%>).text(unEscape(aData[4]));
	f.<%=Bscdimension.IDBSCDIMENSION%>.value = aData[6];
	
	// Display followup info
	$('#metrics-popup').dialog('open');
}

function deleteMetric(element) {
	
	confirmUI(
		'${msgTitleConfirmDelete}','${msgConfirmDelete}',
		'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
		function() {
			var aData = metricsTable.fnGetData( element.parentNode.parentNode.parentNode );
			var f	  = document.frm_metrics_pop;

			f.accion.value	 = "<%=MaintenanceServlet.DEL_METRIC %>";
			f.<%=Metrickpi.IDMETRICKPI%>.value	= aData[0];
			
			loadingPopup();
			f.submit();
		}
	);
}

readyMethods.add(function () {

	metricsTable = $('#tb_metrics').dataTable({
		"sPaginationType": "full_numbers",
		"oLanguage": datatable_language,
		"aaSorting": [[ 0, "asc" ]],
		"aoColumns": [ 
			{ "bVisible": false },
			{ "sClass": "left" },
            { "bVisible": false },
			{ "sClass": "left" },
            { "sClass": "left" },
			{ "sClass": "left" },
			{ "bVisible": false },
            { "sClass": "center", "bSortable" : false } 
   		]
	});

	$('#tb_metrics tbody tr').live('click',  function (event) {
		metricsTable.fnSetSelectable(this,'selected_internal');
	});
	
	$('div#metrics-popup').dialog({
		autoOpen: false,
		modal: true,
		width: 400,
		resizable: false,
		open: function(event, ui) { metricsValidator.resetForm(); }
	});

	metricsValidator = $("#frm_metrics_pop").validate({
		errorContainer: $('div#metrics-errors'),
		errorLabelContainer: 'div#metrics-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#metrics-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			<%=Metrickpi.NAME%>: { required: true },
			<%=Metrickpi.DEFINITION%>: { maxlength: 100 }
		},
		messages:{
			<%=Metrickpi.NAME%>: { required: '${fmtNameRequired}'}
		}
	});

    $('#<%= Metrickpi.TYPE %>').change(function() {

        $("#nameMetric").val($('#<%= Metrickpi.TYPE %>' + " option[value='"+ $('#<%= Metrickpi.TYPE %>').val() +"']").text());
    });

});

</script>

<fieldset>
	<legend><fmt:message key="maintenance.metric_kpi" /></legend>
	<table id="tb_metrics" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
			 <th width="0%">&nbsp;</th>
			 <th width="30%"><fmt:message key="metric.name" /></th>
             <th width="0%">&nbsp;</th>
             <th width="10%"><fmt:message key="metric.type" /></th>
			 <th width="40%"><fmt:message key="metric.definition" /></th>
			 <th width="20%"><fmt:message key="metric.bsc_dimension" /></th>
			 <th width="0%">&nbsp;</th>
			 <th width="10%">
				<img onclick="addMetric()" title="<fmt:message key="new"/>" class="link" src="images/add.png">
			 </th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="metric" items="${metrics}">
				<tr>
					<td>${metric.idMetricKpi}</td>
					<td>${tl:escape(metric.name)}</td>
                    <td>${tl:escape(metric.type)}</td>
                    <td>${tl:escape(metric.typeKPI)}</td>
					<td>${tl:escape(metric.definition)}</td>
					<td>${tl:escape(metric.bscdimension.name)}</td>
					<td>${metric.bscdimension.idBscDimension}</td>
					<td>
						<nobr>
							<img onclick="editMetric(this)" title="<fmt:message key="view"/>" class="link" src="images/view.png" >
							&nbsp;&nbsp;&nbsp;
							<img onclick="deleteMetric(this)" title="<fmt:message key="delete"/>" class="link" src="images/delete.jpg">
						</nobr>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</fieldset>

<div id="metrics-popup" class="popup">

	<div id="metrics-errors" class="ui-state-error ui-corner-all hide">
		<p>
			<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
			<strong><fmt:message key="msg.error_title"/></strong>
			&nbsp;(<b><span id="metrics-numerrors"></span></b>)
		</p>
		<ol></ol>
	</div>

	<form name="frm_metrics_pop" id="frm_metrics_pop">
		<input type="hidden" name="accion" value="<%=MaintenanceServlet.JX_SAVE_METRIC%>"/>
		<input type="hidden" name="<%=Metrickpi.IDMETRICKPI%>"/>
		<input type="hidden" name="idManten" value="<%=Constants.MANT_METRIC_KPI%>"/>
		
		<fieldset>
			<legend><fmt:message key="metric"/></legend>
			<table cellpadding="2" cellspacing="1" width="100%">
				<tr>
					<th><fmt:message key="metric.name" />&nbsp;*</th>
				</tr>
				<tr>
					<td><input type="text" name="<%=Metrickpi.NAME %>" id="nameMetric" class="campo" maxlength="100"/></td>
				</tr>
				<tr>
					<th><fmt:message key="metric.bsc_dimension" /></th>
				</tr>
				<tr>
					<td>
						<select name="<%=Bscdimension.IDBSCDIMENSION %>" id="<%=Bscdimension.IDBSCDIMENSION %>" class="campo">
							<option value=""><fmt:message key="select_opt"/></option>
                            <c:forEach var="bsc" items="${bscdimensions }">
                                <option value="${bsc.idBscDimension }">${bsc.name }</option>
                            </c:forEach>
						</select>
					</td>
				</tr>
                <tr>
                    <th><fmt:message key="metric.type" /></th>
                </tr>
                <tr>
                    <td>
                        <select name="<%= Metrickpi.TYPE %>" id="<%= Metrickpi.TYPE %>" class="campo">
                            <option value=""><fmt:message key="select_opt"/></option>
                            <c:forEach var="type" items="<%= Metrickpi.TypeKPI.values() %>">
                                <option value="${type}" >${type.label}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
				<tr>
					<th><fmt:message key="metric.definition" /></th>
				</tr>
				<tr>
					<td><textarea rows="4" name="<%=Metrickpi.DEFINITION %>" class="campo" style="width: 98%"></textarea></td>
				</tr>
    		</table>
    	</fieldset>
    	<div class="popButtons">
    		<input type="submit" class="boton" onclick="saveMetric(); return false;" value="<fmt:message key="save" />" />
    	</div>
    </form>
</div>

