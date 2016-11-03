<%@page import="es.sm2.openppm.core.logic.impl.RiskRegisterLogic"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectRiskServlet"%>

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
  ~ File: manage_risks.inc.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:50
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<fmt:message key="error" var="fmt_error" />
<fmt:message key="msg.error.risk_reassessment_save" var="msg_err_risk_reassessment" />
<fmt:message key="msg.confirm.delete_log_reassessment" var="msg_confirm_delete_log_reassessment" />
<fmt:message key="data_not_found" var="data_not_found" />
<fmt:message key="select_opt" var="sel_option" />

<fmt:message key="msg.confirm_delete" var="msg_confirm_delete_risk">
	<fmt:param><fmt:message key="risk"/></fmt:param>
</fmt:message>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<c:set var="editRisk"><img onclick="editRisk(this);" title="<fmt:message key="edit"/>" class="link" src="images/view.png">&nbsp;&nbsp;&nbsp;</c:set>
</c:if>		

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<c:set var="editRiskReassessment"><img onclick="editRiskReassessment(this);" title="<fmt:message key="edit"/>" class="link" src="images/view.png">&nbsp;&nbsp;&nbsp;</c:set>			
</c:if>
<c:if test="${op:hasPermission(user,project.status,tab)}">
	<c:set var="deleteRiskReassessment"><img src="images/delete.jpg" class="link" onclick="deleteLogReassessment('+ data.id +');" /></c:set>
</c:if>
<c:if test="${op:hasPermission(user,project.status,tab)}">
	<c:set var="deleteRiskReassessment2"><img src="images/delete.jpg" class="link" onclick="deleteLogReassessment('+ data[i].id +');" /></c:set>
</c:if>

<c:set var="closed"  value="<%=Constants.CHAR_CLOSED%>" />
<c:set var="open"  value="<%=Constants.CHAR_OPEN%>" />

<%-- Message for validations --%>
<fmt:message key="maintenance.error_msg_a" var="fmtCodeRequired">
	<fmt:param><b><fmt:message key="risk.code"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtNameRequired">
	<fmt:param><b><fmt:message key="risk.name"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtRaisedDateRequired">
	<fmt:param><b><fmt:message key="risk.date_raised"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtRaisedDateFormat">
	<fmt:param><b><fmt:message key="risk.date_raised"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtChangeRequired">
	<fmt:param><b><fmt:message key="reassessment_log.change"/></b></fmt:param>
</fmt:message>
<fmt:message key="maintenance.error_msg_a" var="fmtLogDateRequired">
	<fmt:param><b><fmt:message key="reassessment_log.date"/></b></fmt:param>
</fmt:message>
<fmt:message key="msg.error.invalid_format" var="fmtLogDateFormat">
	<fmt:param><b><fmt:message key="reassessment_log.date"/></b></fmt:param>
	<fmt:param>${datePattern}</fmt:param>
</fmt:message>

<fmt:message key="yes" var="msg_yes" />
<fmt:message key="no" var="msg_no" />
<fmt:message key="msg.confirm" var="msgConfirm" />
<fmt:message key="risk.confirm_changes" var="msgRiskConfirmChanges" />
<fmt:message key="insufficient_values" var="insufficientValues" />

<fmt:message key="msg.confirm" var="msgConfirm" />
<fmt:message key="risk.confirm_export" var="msgConfirmExport" />

<script type="text/javascript">
<!--
var riskTable;
var validatorRisk;
var riskRatingChart;


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

function updateRiskRating(prob, impact) {

	$('#risk_rating').removeClass('risk_high');
	$('#risk_rating').removeClass('risk_medium');
	$('#risk_rating').removeClass('risk_low');

    var p = typeof prob === 'string' ? parseInt(prob) : prob;
    var i = typeof impact === 'string' ? parseInt(impact) : impact;

    // Operation
    var riskRating = p * i;

    $('#risk_rating').addClass(getClassRating(riskRating));
}

function addRisk() {
	var f = document.forms["frm_risk"];
	f.action = "<%=ProjectRiskServlet.REFERENCE%>";
	f.accion.value = "<%=ProjectRiskServlet.ADD_RISK%>";
	loadingPopup();
	f.submit();
}

function fieldChange (param, fieldId) {
	if (param == $("#"+ fieldId).val() || "-1" == $("#"+ fieldId).val() || null == $("#"+ fieldId).val()) {
		return false;
	}
	else {
		return true;
	}
}

function saveRisk() {
	if (validatorRisk.form()) {
		var f = document.forms["frm_risk"];
		f.action = "<%=ProjectRiskServlet.REFERENCE%>";
		f.accion.value = "<%=ProjectRiskServlet.SAVE_RISK%>";
		
		// Popup confirmation reassement log
		if (f.risk_id.value != "" && f.risk_id.value != null) {
			if (fieldChange("${risk.status}", "risk_status") || fieldChange("${risk.riskType}", "risk_type") || fieldChange("${risk.riskcategories.idRiskCategory}", "responseRisk")) {
				confirmUI("${msgConfirm}", "${msgRiskConfirmChanges}", "${msg_yes}", "${msg_no}",
					function() {
						$("#saveReassessmentLog").val(true);
						loadingPopup();
						f.submit();
					},
					function() {
						$("#saveReassessmentLog").val(false);
						loadingPopup();
						f.submit();
					}
				);
			}
			else {
				loadingPopup();
				f.scrollTop.value = $(document).scrollTop();
				f.submit();
			}
		}
		else {
			loadingPopup();
			f.scrollTop.value = $(document).scrollTop();
			f.submit();
		}
	} 
}
	
function editRisk(element) {	
	var risk = riskTable.fnGetData( element.parentNode.parentNode );
	var f = document.forms["frm_risk"];	
	f.action = "<%=ProjectRiskServlet.REFERENCE%>";	
	f.accion.value = "<%=ProjectRiskServlet.EDIT_RISK%>";	
	f.risk_id.value = risk[0];
	loadingPopup();
	f.submit();	
}

function deleteRisk(id) {
	
	document.forms["frm_project"].risk_id.value = id;
	
	confirmUI(
		'', '${msg_confirm_delete_risk}', 
		"${msg_yes}", "${msg_no}", 
		function () {
			$('#dialog-confirm').dialog("close"); 
			var f = document.forms["frm_project"];
			f.accion.value = "<%=ProjectRiskServlet.DELETE_RISK%>";
			loadingPopup();
			f.submit();	
	});
	
}

function updateRiskMaterialization() {
	if ($('#materialized').prop('checked')) {
		$('#risk_date_materialized').attr('disabled', false);
		$('#risk_date_materialized').datepicker("enable");
	}
	else {
		$('#risk_date_materialized').val("");
		$('#risk_date_materialized').attr('disabled', true);
		$('#risk_date_materialized').datepicker("disable");
	}
}

// Risk Reassessment log functions
//
var riskReassessmentTable;
var validatorLogReassessment;

function addRiskReassessment() {
	if (document.forms["frm_risk"].risk_id.value!= "") {
		$('#reassessment-log-popup').dialog('open');
		
		var f = document.forms["frm_reassessment_log"];
		f.reset();
		f.reassessment_log_id.value = "";
		f.reassessment_log_date.focus();
		f.reassessment_log_change.focus();
		$("#reassessment_log_change").text("");
	}
	else {
		alertUI("${fmt_error}", "${msg_err_risk_reassessment }");
	}
}

function editRiskReassessment(element) {

	var log = riskReassessmentTable.fnGetData( element.parentNode.parentNode.parentNode );

	var f = document.forms["frm_reassessment_log"];
	f.reset();
	f.reassessment_log_id.value = log[0];
	f.reassessment_log_date.value = log[1];

	$("#reassessment_log_change").text(unEscape(log[3]));

	$('#reassessment-log-popup').dialog('open');
	f.reassessment_log_date.focus();
}

function saveLogReassessment() {
	var f = document.forms["frm_reassessment_log"];

	if (validatorLogReassessment.form()) {
		var idReassessmentLog = f.reassessment_log_id.value;
		
		var params = {
			accion: "<%=ProjectRiskServlet.JX_SAVE_REASSESSMENT_LOG%>",
			risk_id: document.forms["frm_risk"].risk_id.value,
			reassessment_log_id: idReassessmentLog,
			date: f.reassessment_log_date.value,
			change: f.reassessment_log_change.value
		};
		
		riskAjax.call(params, function (data) {
			var dataRow = [
	            data.id,
				data.date,
				escape(data.user),
				escape(data.change),
				'<nobr>${editRiskReassessment}${deleteRiskReassessment}</nobr>'
			];
			if (idReassessmentLog == '') { riskReassessmentTable.fnAddDataAndSelect(dataRow); }
			else { riskReassessmentTable.fnUpdateAndSelect(dataRow); }
			f.reset();
			$('#reassessment-log-popup').dialog('close');
		});
	} 
}

function deleteLogReassessmentConfirmed() {
	var f = document.forms["frm_project"];
	
	var params = {
		accion: "<%=ProjectRiskServlet.JX_DELETE_LOG_REASSESSMENT%>",
		reassessment_log_id: document.forms["frm_project"].reassessment_log_id.value
	};
	
	riskAjax.call(params, function (data) {		
		riskReassessmentTable.fnDeleteSelected();
	});
}

function deleteLogReassessment(id) {
	document.forms["frm_project"].reassessment_log_id.value = id;
	
	confirmUI('', '${msg_confirm_delete_log_reassessment}', 
			"${msg_yes}", "${msg_no}", 
			deleteLogReassessmentConfirmed);
}

function refreshReassessmentLog(idRisk) {
	if (idRisk > 0) {
		var params = {
			accion: "<%=ProjectRiskServlet.JX_CONS_LOG_REASSESSMENT%>",
			risk_id: idRisk
		};
		riskAjax.call(params, function (data) {
			riskReassessmentTable.fnClearTable();
			if (data.length > 0) {
				for (var i=0; i<data.length; i++) {
					riskReassessmentTable.fnAddData([
						data[i].id,
						data[i].date,
						escape(data[i].user),
						escape(data[i].change),
						'<nobr>${editRiskReassessment}${deleteRiskReassessment2}</nobr>'
      				]);
				}
			}
		});
	}
}

function updateRiskRatingSelect() {

	var prob    = $('#risk_probability option:selected').val();
	var impact  = $('#risk_impact option:selected').val();

	if (prob != "" && impact != "") {
		updateRiskRating(prob, impact);
	}
	else {
		$('#risk_rating').attr("class", "riskRating");
	}
}

// Draw risk rating chart
function drawRiskRatingChart () {
	
	var riskTableSettings = riskTable.fnSettings();
	
	var idsBySortingArray = [];
	for (var i = 0; i < riskTableSettings.asDataSearch.length; i++) {
		idsBySortingArray[i] = riskTableSettings.asDataSearch[i].match(/^([0-9]*)\s/)[1];
	}
	
	var params = {
		accion: "<%=ProjectRiskServlet.JX_RISK_RATING_CHART %>",
		idsBySorting: idsBySortingArray.join(",")
	};
		
	riskAjax.call(params, function(data) {
		
		if (!data.insufficientDates) {
			
			//JQPlot parameters risk rating chart
			var dataSeries = data.riskRatingValues;
			var optionsObj = {
				enableCursor: true,
				axes: {
					xaxis:{
						label: '<fmt:message key="date"/>',
						renderer:$.jqplot.DateAxisRenderer,
						tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
						tickOptions:{
							formatString:"%d/%m/%Y",
							angle: -45
						},
						min: data.minDate,
						max: data.maxDate,
						tickInterval: data.tickInterval
					},
					yaxis:{
						label: '<fmt:message key="risk.rating"/>',
						labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
						labelOptions: {
							angle: -90
						},
						tickOptions:{
							formatString: '%.0f'
						},
						min: 0,
						pad: 1.2
					}
				},
				seriesColors: data.riskRatingColors,
				formatString: '%s',
				animate: !$.jqplot.use_excanvas
			};
			
			if(typeof riskRatingChart === 'undefined'){
				$("#riskRatingChart").empty();
				riskRatingChart = drawLineChart('riskRatingChart', dataSeries, optionsObj, '${dataNotFound}');
			}
			else {
				$("#riskRatingChart").empty();
				//riskRatingChart.destroy();
				riskRatingChart = drawLineChart('riskRatingChart', dataSeries, optionsObj, '${dataNotFound}');
			}
			
			
			if(typeof riskRatingChart === 'undefined'){
				$("#legendChartRiskRating").html('');
			}
			else {
				$('#legendChartRiskRating').empty();
				
				var i;
				for(i=0;i<data.riskRatingNames.length;i++){
					createLegend('#legendChartRiskRating',data.riskRatingNames[i],data.riskRatingColors[i]);
				}
			}
			
		}
		else {
			$('#legendChartRiskRating').empty();
			$("#riskRatingChart")
			.attr( 'style', 'margin: 20px auto;text-align:center')
			.html('${insufficientValues}');
		}
	});

}

function exportRisk(element) {
	var risk = riskTable.fnGetData(element.parentNode.parentNode);
	
	confirmUI("${msgConfirm }", "${msgConfirmExport}", "${msg_yes}", "${msg_no}",
		function() {
			var params = {
				accion: "<%= ProjectRiskServlet.JX_EXPORT_RISK %>", 
				idRisk: risk[0]
			};
			
			riskAjax.call(params);
		}
	);
}

function importRisk() {
	$('#selection-popup').dialog('open');
}

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
};

readyMethods.add(function() {
	
	if ('${showRisk}') {

		updateRiskMaterialization();
		updateRiskRating('${risk.probability}', '${risk.impact}');
		refreshReassessmentLog('${risk.idRisk}');
		show('risk');
	}
	
	riskTable = $('#tb_risks').dataTable({
		"oLanguage": datatable_language,
		"bFilter": true,
		"bInfo": true,
		"bPaginate": true,
		"iDisplayLength": 50,
		"bAutoWidth": false,
		"sPaginationType": "full_numbers",
		"aaSorting": [[ 3, "desc" ],[ 1, "asc" ]],
		"aoColumns": [
             { "bVisible": false },
             { "sClass": "center" },
         	 { "sClass": "left" },
             { "sClass": "center", "sType": "es_date" },		              
             { "sClass": "center", "sType": "es_date" },
             { "sClass": "center"  },
             { "sSortDataType": "dom-div", "sType": "numeric" },
             { "sClass": "center"  },
             { "sClass": "center", "bSortable": false }
    	]
	});

	$('#tb_risks tbody tr').live('click', function (event) {		
		riskTable.fnSetSelectable(this,'selected_internal');
	} );

	riskReassessmentTable = $('#tb_reassessment_logs').dataTable( {
		"oLanguage": datatable_language,
		"bFilter": true,
		"bInfo": true,
		"bPaginate": true,
		"iDisplayLength": 50,
		"bAutoWidth": false,
		"sPaginationType": "full_numbers",
		"aaSorting": [[ 1, "desc" ]],
		"aoColumns": [
             { "bVisible": false },
             { "sClass": "center", "sType": "es_date" },
             { "sClass": "center" },
             { "sClass": "left" },
             { "sClass": "center", "bSortable": false }
    	]
	});

	$('#tb_reassessment_logs tbody tr').live('click', function (event) {		
		riskReassessmentTable.fnSetSelectable(this,'selected_internal');
	});
	
	validatorRisk = $("#frm_risk").validate({
		errorContainer: 'div#risk-errors',
		errorLabelContainer: 'div#risk-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#risk-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			risk_code: {required: true },
			risk_name: {required: true },
			date_raised: {required: true, date: true },
			description : { maxlength : 3000 },
			risk_trigger: { maxlength : 3000 },
			response_description: { maxlength : 3000 },
			mitigation_actions: { maxlength : 3000 },
			contingency_actions: { maxlength : 3000 },
			residual_risk: { maxlength : 3000 },
			final_comments: { maxlength : 3000 }
		},
		messages: {
			risk_code: {required: '${fmtCodeRequired}'},
			risk_name: {required: '${fmtNameRequired}'},
			date_raised: {required: '${fmtRaisedDateRequired}', date: '${fmtRaisedDateFormat}'}
		}
	});	

	$('#risk_type').filterSelect({
		selectFilter:'responseRisk',
		prefix: 'response_',
		disabledEmpty: true,
		showEmpty: true
	});
	
	$("#responseRisk").val('${risk.riskcategories.idRiskCategory}');


	$('#risk_probability,#risk_impact').change(function() {
		updateRiskRatingSelect();
	});

	validatorLogReassessment = $("#frm_reassessment_log").validate({
		errorContainer: 'div#reassessment-log-errors',
		errorLabelContainer: 'div#reassessment-log-errors ol',
		wrapper: 'li',
		showErrors: function(errorMap, errorList) {
			$('span#reassessment-log-numerrors').html(this.numberOfInvalids());
			this.defaultShowErrors();
		},
		rules: {
			reassessment_log_date: {required: true, date:true },
			reassessment_log_change: {required: true, maxlength: 3000 }
		},
		messages: {
			reassessment_log_date: {required: '${fmtLogDateRequired}', date: '${fmtLogDateFormat}' },
			reassessment_log_change: {required: '${fmtChangeRequired}' }
		}
	});
	
	$('#reassessment-log-popup').dialog({
		autoOpen: false, 
		modal: true, 
		width: 550, 
		resizable: false,
		open: function(event, ui) { validatorLogReassessment.resetForm(); }
	});

	$('#reassessment_log_date').datepicker({
		dateFormat: '${datePickerPattern}',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		numberOfMonths: ${numberOfMonths},
		changeMonth: true,
		onSelect: function() {
			if (validatorLogReassessment.numberOfInvalids() > 0) validatorLogReassessment.form();
		}
	});
	
	$('#materialized').click(function() {
		updateRiskMaterialization();
	});	
	
	$('#riskRatingChartReload').on('click', function(e){
		e.stopPropagation();
		drawRiskRatingChart();
	});
	
	$('#riskRatingChartZoomInfo').on('click', function(e){
		e.stopPropagation();
	});
	
	createBT('.btitle');

    calculateRiskRating();

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

        $( 'td:eq('+(iColumn-cont)+') .riskValue.order', oSettings.oApi._fnGetTrNodes(oSettings) ).each( function () {
            aData.push( $(this).html() );
        } );

        return aData;
    };
});
//-->
</script>

<c:if test="${op:hasPermission(user,project.status,tab)}">
	<script type="text/javascript">
	<!--
	readyMethods.add(function() {
		$('#date_raised').datepicker({
			dateFormat: '${datePickerPattern}',
			firstDay: 1,
			showOn: 'button',
			buttonImage: 'images/calendario.png',
			buttonImageOnly: true,
			numberOfMonths: ${numberOfMonths},
			changeMonth: true,
			onSelect: function() {
				if (validatorRisk.numberOfInvalids() > 0) validatorRisk.form();
			}
		});
		
		$('#due_date').datepicker({
			dateFormat: '${datePickerPattern}',
			firstDay: 1,
			showOn: 'button',
			buttonImage: 'images/calendario.png',
			buttonImageOnly: true,
			numberOfMonths: ${numberOfMonths},
			changeMonth: true,
			onSelect: function() {
				if (validatorRisk.numberOfInvalids() > 0) validatorRisk.form();
			}
		});
		
		$('#risk_date_materialized').datepicker({
			dateFormat: '${datePickerPattern}',
			firstDay: 1,
			showOn: 'button',
			buttonImage: 'images/calendario.png',
			buttonImageOnly: true,
			numberOfMonths: ${numberOfMonths},
			changeMonth: true,
			onSelect: function() {
				if (validatorRisk.numberOfInvalids() > 0) validatorRisk.form();
			}
		});
	});
	//-->
	</script>
</c:if>

<fmt:message key="risk_register" var="titleRiskRegister"/>
<visual:panel id="field_risk_register" title="${titleRiskRegister}">
	<div style="margin: 5px 0px; text-align: right;">
        <c:if test="${op:hasPermission(user,project.status,tab)}">
		    <a href="javascript:importRisk();" class="boton"><fmt:message key="risk.import"/></a>
        </c:if>
	</div>
	<table id="tb_risks" class="tabledata" cellspacing="1" width="100%">
		<thead>
			<tr>
				<th width="0%">&nbsp;</th>
				<th width="9%"><fmt:message key="risk.code"/></th>
				<th width="30%"><fmt:message key="risk.name"/></th>
				<th width="10%"><fmt:message key="risk.date_raised"/></th>					
				<th width="10%"><fmt:message key="risk.due_date"/></th>
				<th width="10%"><fmt:message key="risk.status"/></th>
				<th width="10%"><fmt:message key="risk.rating"/></th>
				<th width="11%"><fmt:message key="risk.response"/></th>
				<th width="10%">
					<c:if test="${op:hasPermission(user,project.status,tab)}">
						&nbsp;<img onclick="addRisk();" title="<fmt:message key="add"/>" class="link" src="images/add.png">
					</c:if>						
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="risk" items="${risks}">
				<tr>
					<td>${risk.idRisk }</td>
					<td>${tl:escape(risk.riskCode)}</td>
					<td>${tl:escape(risk.riskName)}</td>
					<td><fmt:formatDate value="${risk.dateRaised}" pattern="${datePattern}"/></td>						
					<td><fmt:formatDate value="${risk.dueDate}" pattern="${datePattern}"/></td>						
					<td>
						<c:choose>
							<c:when test="${risk.status == closed}">
								<%=Constants.CLOSED%>
							</c:when>
							<c:when test="${risk.status == open}">
								<%=Constants.OPEN%>
							</c:when>
						</c:choose>
					</td>				
					<td>
						<div style="margin: 0px auto; width: 80px;" class="rating">
                            <div style="display:none;" class="riskValue order"></div> <!-- order -->
                            <div style="display:none;" class="riskProbability">${risk.probability}</div>
                            <div style="display:none;" class="riskImpact">${risk.impact}</div>
							<div style="width: 25%; float: left; margin-left: 10px;" class="riskColor">&nbsp;</div>
                            <div style="float: left; margin-left: 10px;" class="riskValue"></div>
                        </div>
					</td>
                    <td>${tl:escape(risk.riskcategories.description)}</td>
					<td>
						<c:if test="${op:hasPermission(user,project.status,tab)}">
							<img onclick="editRisk(this);" title="<fmt:message key="edit"/>" class="link" src="images/view.png">
							&nbsp;&nbsp;
						</c:if>
						<c:if test="${op:hasPermission(user,project.status,tab)}">
							<img src="images/delete.jpg" class="link" onclick="deleteRisk(${risk.idRisk });" />
						</c:if>
						<c:if test="${op:hasPermission(user,project.status,tab)}">
							&nbsp;&nbsp;
							<img onclick="exportRisk(this)" class="icono" src="images/add_proj.png" title="<fmt:message key="risk.export"/>"/>
						</c:if>	
															
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

    <%-- Charge plugins --%>
    <div id="plugin"></div>

	<div id="risk" class="hide">
		<jsp:include page="manage_risk.inc.jsp" flush="true" />
	</div>	
	
	<!-- Chart risk rating -->
	<fmt:message key="chart.risk_rating" var="riskRatingChart"/>
	<c:set var="riskRatingChartBTN">
		<img id="riskRatingChartReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
		<img class="btitle" id="riskRatingChartZoomInfo" src="images/info.png" title="<fmt:message key="chart.reset_zoom_double_click.info"/>">
	</c:set>
	<visual:panel id="riskRatingChartPanel" title="${riskRatingChart}" cssClass="panel3" callback="drawRiskRatingChart" buttons="${riskRatingChartBTN}" >
		<div id="riskRatingChart" style="margin: 20px auto;"></div>
		<div id="legendChartRiskRating" class="legendChart"></div>
	</visual:panel>
	
</visual:panel>