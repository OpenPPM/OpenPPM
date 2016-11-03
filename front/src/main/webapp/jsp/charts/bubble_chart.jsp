<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.front.servlets.ProjectServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Riskcategory"%>
<%@page import="es.sm2.openppm.core.model.impl.Project"%>
<%@page import="es.sm2.openppm.front.servlets.UtilServlet"%>
<%@page import="es.sm2.openppm.front.utils.SecurityUtil"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>

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
  ~ File: bubble_chart.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:35
  --%>

<fmt:setLocale value="${locale}" scope="request"/>

<%-- Message for Confirmations --%>
<fmt:message key="msg.title_confirm_uncheked" var="msgTitleConfirmUnchecked"/>
<fmt:message key="msg.confirm_unchecked_adjust" var="msgConfirmUncheckedRisk">
	<fmt:param><fmt:message key="riskRating.total"/></fmt:param>
</fmt:message>
<fmt:message key="msg.confirm_unchecked_adjust" var="msgConfirmUncheckedStrategic">
	<fmt:param><fmt:message key="strategicAlignment.total"/></fmt:param>
</fmt:message>

<c:set var="SHOW_PANEL_RISK_ADJUST"><%=Settings.SHOW_PANEL_RISK_ADJUST? "block":"none" %></c:set>

<%--
params: page
--%>

<c:choose>
	<c:when test='${param.page == "investments"}'>
		<c:set var="investiment">true</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="investiment">false</c:set>
	</c:otherwise>
</c:choose>

<script type="text/javascript">
<!--

var priorityAdjustmentTB;
var priorityAdjustmentValidator;

function showValueChart() {
	
	if (typeof dataInfoTable === 'undefined') { 
		loadDashboard = true; 
	}
	else {
		var params = {
			accion: "<%=ProgramServlet.CHART_BUBBLE%>",
			ids: dataInfoTable.ids,
			axesXType: $("#selectBubbleChart").val(),
			priorityAdjustmentApply: $('#priorityAdjustmentApply').prop('checked'),
			<%= Riskcategory.ENTITY%>: $('#<%= Riskcategory.ENTITY%>').val()+""
		};
		
		programAjax.call(params, function(data) {
			
			$('#priorityAdjustmentProjects').val(dataInfoTable.ids);
			
			priorityAdjustmentTB.fnClearTable();
			
			$(data.projects).each(function(){
				
				var dataRow = [
					this.<%=Project.IDPROJECT%>,
					this.<%=Project.PROJECTNAME%>,
					this.actualRiskRating,
					this.<%=Project.PRIORITY%>,
					this.<%=Project.RISKRATINGADJUSTAMENT%>,
					this.<%=Project.STRATEGICADJUSTAMENT%>,
					this.totalRiskRatingHTML,
					this.totalAdjustmentHTML
				];
				
				priorityAdjustmentTB.fnAddDataAndSelect(dataRow);
			});
			
			priorityAdjustmentTable.throwEvents();
			
			priorityAdjustmentValidator = $("#priorityAdjustmentFRM").validate({
				errorContainer: $('div#priorityAdjustment-errors'),
				errorLabelContainer: 'div#priorityAdjustment-errors ol',
				wrapper: 'li',
				showErrors: function(errorMap, errorList) {
					$('span#priorityAdjustment-numerrors').html(this.numberOfInvalids());
					this.defaultShowErrors();
				}
			});
			
			var legendContent = '<span style="background-color:#E3E3E3;">&nbsp;&nbsp;</span><fmt:message key="risk.undefined" />'+
				'<span style="background-color:#99CC00;">&nbsp;&nbsp;</span><fmt:message key="risk.low" />' +
				'<span style="background-color:#FFCC00;">&nbsp;&nbsp;</span><fmt:message key="risk.medium" />' +
				'<span style="background-color:#CC0000;">&nbsp;&nbsp;</span><fmt:message key="risk.high" />';
			
			//JQPlot parameters 
			var optionsObj;
			
			if ($("#selectBubbleChart").val() == '<%= Project.INITDATE %>' || $("#selectBubbleChart").val() == '<%= Project.ENDDATE %>') {
				optionsObj = {
					axes:{
						xaxis: {
							label: $("#selectBubbleChart :selected").text(),
							renderer: $.jqplot.DateAxisRenderer,
							tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
							tickOptions:{
								formatString:"%d/%m/%Y",
								angle: -45
							}
						},
						yaxis: {
							label: '<fmt:message key="table.priority" />',
							labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
							labelOptions: {
								angle: -90
							}
						}
					}
				};
			}
			else {
				optionsObj = {
					labelX: $("#selectBubbleChart :selected").text(),
					labelY:'<fmt:message key="table.priority" />'
				};
			}
			
			if (typeof bubbleChart === 'undefined') {
				bubbleChart = createBubbleChart('bubble_chart', [data.chart], optionsObj, '${dataNotFound}', legendContent);
			}
			else {
				$("#legend-bubble_chart").remove();
				bubbleChart.destroy();
				bubbleChart = createBubbleChart('bubble_chart', [data.chart], optionsObj, '${dataNotFound}', legendContent);
			}
			
		});
	}
}

var priorityAdjustmentTable = {
		
		createTableUnadjusted: function(){
			
			priorityAdjustmentTB = $('#priorityAdjustmentTB').dataTable({
				"oLanguage": datatable_language,
				"bFilter": false,
			 	"bInfo": true,
				"bPaginate": false,
				"bLengthChange": false,
				"iDisplayLength": 100,
				"sPaginationType": "full_numbers",
				"aaSorting": [[ 1, "asc" ]],
				"bAutoWidth": false,
				"aoColumns": [ 
					{ "bVisible": false},
					{ "sClass": "left" },
					{ "sClass": "right" },
					{ "sClass": "right" },
					{ "bVisible": false, "sClass": "right" },
					{ "bVisible": false, "sClass": "right" },
					{ "bVisible": false, "sClass": "center" , "sSortDataType": "orderInputNumber", "sType": "numeric"},
					{ "bVisible": false,"sClass": "center", "sSortDataType": "orderInputNumber", "sType": "numeric" }
				]		
			});
			
			$("#priorityAdjustmentButtons").css("display","none");
		},
		createTableAdjusted: function(){
			
			priorityAdjustmentTB = $('#priorityAdjustmentTB').dataTable({
				"oLanguage": datatable_language,
				"bFilter": false,
			 	"bInfo": true,
				"bPaginate": false,
				"bLengthChange": false,
				"iDisplayLength": 100,
				"sPaginationType": "full_numbers",
				"aaSorting": [[ 1, "asc" ]],
				"bAutoWidth": false,
				"aoColumns": [ 
					{ "bVisible": false },
					{ "sClass": "left"  },
					{ "sClass": "right" , "sSortDataType": "dom-span", "sType": "numeric"},
					{ "sClass": "right" , "sSortDataType": "dom-span", "sType": "numeric"},
					{ "sClass": "right" , "sSortDataType": "dom-span", "sType": "numeric"},
					{ "sClass": "right" , "sSortDataType": "dom-span", "sType": "numeric"},
					{ "sClass": "center", "sSortDataType": "orderInputNumber", "sType": "numeric" },
					{ "sClass": "center", "sSortDataType": "orderInputNumber", "sType": "numeric" }
				]		
			});
			
			$("#priorityAdjustmentButtons").css("display","block");
		},
		throwEvents: function() {
			// Event checkbox risk adjustament
			$('.useRiskAdjust').on('change', function(){
				
				var td = $(this).parent();
				
				var inputNumber = td.find(".totalRiskRating");
				
				// Unchecked
				if (!$(this).is(":checked")) {
					
					if ($(inputNumber).val() != '') {
						
						confirmUI(
								'${msgTitleConfirmUnchecked}','${msgConfirmUncheckedRisk}',
								'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
								function() { // Disable input
									
									$(td).parent().find(".riskAdjust").html("");
									$(inputNumber).val("");
									$(inputNumber).addClass("disabled");
									$(inputNumber).attr("readonly", "");
								},
								function() { // Checked checkbox
									$(td).find(".useRiskAdjust").attr("checked", "");
								}
						);
					}
					else {
						$(inputNumber).val("");
						$(inputNumber).addClass("disabled");
						$(inputNumber).attr("readonly", "");
					}
				}
				else {
					$(inputNumber).removeClass("disabled");
					$(inputNumber).removeAttr("readonly");
				}
			});
			
			// Event checkbox strategic adjustament
			$('.useStrategicAdjust').on('change', function(){
				
				var td = $(this).parent();
				
				var inputNumber = td.find(".totalAdjustment");
				
				// Unchecked
				if (!$(this).is(":checked")) {
					
					if ($(inputNumber).val() != "0" && $(inputNumber).val() != '') {
						
						confirmUI(
								'${msgTitleConfirmUnchecked}','${msgConfirmUncheckedStrategic}',
								'<fmt:message key="yes"/>', '<fmt:message key="no"/>',
								function() { // Disable input
									
									$(td).parent().find(".strategicAdjust").html("");
									$(inputNumber).val("");
									$(inputNumber).addClass("disabled");
									$(inputNumber).attr("readonly", "");
								},
								function() { // Checked checkbox
									
									$(td).find(".useStrategicAdjust").attr("checked", "");
								}
						);
					}
					else {
						$(inputNumber).val("");
						$(inputNumber).addClass("disabled");
						$(inputNumber).attr("readonly", "");
					}
				}
				else {
					$(inputNumber).removeClass("disabled");
					$(inputNumber).removeAttr("readonly");
				}
			});
		}
}

readyMethods.add(function() {
	
	$('#bubble_chart').bind('jqplotDataClick', 
		function (ev, seriesIndex, pointIndex, data, radius) {
			viewProject(data[4],data[5]);
		}
	);
	
	// Now bind function to the highlight event to show the tooltip
    $('#bubble_chart').bind('jqplotDataHighlight', 
        function (ev, seriesIndex, pointIndex, data, radius) { 
    	
            var chart_left = $('#bubble_chart').offset().left,
                chart_top = $('#bubble_chart').offset().top,
                x = bubbleChart.axes.xaxis.u2p(data[0]),  // convert x axis units to pixels on grid
                y = bubbleChart.axes.yaxis.u2p(data[1]);  // convert y axis units to pixels on grid
                
            $('#tooltipBubbleChart')
	           .css({left:chart_left+x+radius+5, top:chart_top+y})
	           .html('<span>' + data[6] + '</span>')
	           .show();
        }
    );
    
    // Bind a function to the unhighlight event to clean up after highlighting.
    $('#bubble_chart').bind('jqplotDataUnhighlight', 
        function (ev, seriesIndex, pointIndex, data) {
    	
            $('#tooltipBubbleChart').empty();
            $('#tooltipBubbleChart').hide();
        }
    );
    
	$('.bubbleChartReload, ').on('click', function(e){
		e.stopPropagation();
		showValueChart();
	});
	
	$("#selectBubbleChart").change(function(){
		
		if ($("#selectBubbleChart").val() == 'riskRating') {
			
			$('.riskRatingAdjustment').show();
		}
		else {
			$('.riskRatingAdjustment').hide();
		}
		showValueChart();
	});
	
	// Initialize priority Adjustment Table
	if (!$("#priorityAdjustmentApply").is(":checked")) {
		priorityAdjustmentTable.createTableUnadjusted();
		
		$("#riskcategory").val("-1");
	}
	else {
		priorityAdjustmentTable.createTableAdjusted();
		priorityAdjustmentTable.throwEvents();
		
		$("#riskcategory").val("-1");
		$("#riskcategory option[value != '-1']").attr("disabled", "disabled");
		$("#riskcategory option[value != '-1']").addClass('disabled');
	}
	
	// Event update
	$('#priorityAdjustmentSave').bind('click', function() {
		
		if (priorityAdjustmentValidator.form()) {
			projectAjax.call($('#priorityAdjustmentFRM').serializeArray(), function(){
				showValueChart();
			});
		}
	});
	
	// Initialize legend risk rating adjust
	if ($("#selectBubbleChart").val() == 'riskRating' && $(".riskRatingAdjustment").parent().css("display") == "block") {
		$('.riskRatingAdjustment').show();
	}
	
	// Event priority adjustment apply
	$('#priorityAdjustmentApply').on('change', function(){
		
		if (!$(this).is(":checked")) {
			
			priorityAdjustmentTB.fnDestroy();
			 
			priorityAdjustmentTable.createTableUnadjusted();
			
			$("#riskcategory option[value != '-1']").removeAttr("disabled");
			$("#riskcategory option[value != '-1']").removeClass('disabled');
		}
		else {
			
			priorityAdjustmentTB.fnDestroy();
			
			priorityAdjustmentTable.createTableAdjusted();
			priorityAdjustmentTable.throwEvents();
			
			$("#riskcategory").val("-1");
			$("#riskcategory option[value != '-1']").attr("disabled", "disabled");
			$("#riskcategory option[value!='-1']").addClass('disabled');
		}
		
		showValueChart();
	});
	
	// Event select sum risk rating
	$('#riskcategory').on('change', function(){
		showValueChart();
	});
	
});
//-->
</script>

<fmt:message key="dashboard" var="titleDashboard"/>
<fmt:message key="priorityAdjustment" var="titlePriorityAdjustment"/>
	<c:set var="bubbleChartBTN">
		<img class="bubbleChartReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
	</c:set>
<visual:panel id="dashboard" showTiltePanel="false" title="${titleDashboard }" callback="showValueChart" buttons="${bubbleChartBTN}">

	<visual:panel id="priorityAdjustment" showTiltePanel="false" title="${titlePriorityAdjustment }" callback="showValueChart" buttons="${bubbleChartBTN}">
		
		<div id="priorityAdjustment-errors" class="ui-state-error ui-corner-all hide">
			<p>
				<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
				<strong><fmt:message key="msg.error_title"/></strong>
				&nbsp;(<b><span id="priorityAdjustment-numerrors"></span></b>)
			</p>
			<ol></ol>
		</div>
	
		<form name="priorityAdjustmentFRM" id="priorityAdjustmentFRM" action="<%=ProjectServlet.REFERENCE%>" method="post">
			<input type="hidden" name="accion" value="<%=ProjectServlet.JX_UPDATE_PRIORITY_ADJUSTMENT%>" />
			<input type="hidden" id="priorityAdjustmentProjects" name="ids" value="" />
			
			<div style="display:${SHOW_PANEL_RISK_ADJUST};">
				<table id="priorityAdjustmentTB" class="tabledata" cellspacing="1" style="width:100%">
					<thead>
						<tr>
						 <th width="0%">&nbsp;</th>
						 <th width="32%"><fmt:message key="project" /></th>
						 <th width="10%"><fmt:message key="riskRating.actual" /></th>
						 <th width="10%"><fmt:message key="strategicAlignment.actual" /></th>
						 <th width="10%"><fmt:message key="riskRating.adjustment" /></th>
						 <th width="10%"><fmt:message key="strategicAlignment.adjustment" /></th>
						 <th width="14%"><fmt:message key="riskRating.total" /></th>
						 <th width="14%"><fmt:message key="strategicAlignment.total" /></th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
				
				<c:if test="<%=SecurityUtil.isUserInRole(request, Constants.ROLE_PMO) %>">
					<div class="popButtons" id ="priorityAdjustmentButtons" style="display:none;">
						<input type="button" class="boton" id="priorityAdjustmentSave" value="<fmt:message key="save" />"/>
					</div>
				</c:if>
			</div>
			
			<div style="margin-bottom: 10px; padding: 0pt 0.7em;display:<%= !Settings.SHOW_PANEL_RISK_ADJUST?"block;":"none;" %>;" class="ui-state-highlight ui-corner-all">
				<p>
					<span style="float: left; margin-right: 0.3em;" class="ui-icon ui-icon-info"></span>
					<strong><fmt:message key="msg.info"/>: </strong>
				</p>
				<p>
					<fmt:message key="inConstruction" />
				</p>
			</div>
			
		</form>
	</visual:panel>

	<visual:panel id="chart_valuechart" title="${titleDashboard }" showTiltePanel="false" callback="showValueChart" buttons="${bubbleChartBTN}">
		
		<table style="width:100%" style="padding-top:10px;">
			<tr>
				<td style="vertical-align: top; padding-top: 25px;">
					<div class="hidePrint" style="margin-bottom:10px;">
						<div>
							<fmt:message key="chart.axis" />&nbsp;X:&nbsp;
							<select id="selectBubbleChart" class="campo alwaysEditable" style="width:120px">
								<option value="poc"><fmt:message key="table.poc" /></option>
					 			<option value="<%= Project.PROBABILITY %>" <c:if test="${investiment}">selected="selected"</c:if>><fmt:message key="table.probability" /></option>
								<option value="<%= Project.INITDATE %>"><fmt:message key="date"/>&nbsp;<fmt:message key="start" /></option>
							  	<option value="<%= Project.ENDDATE %>"><fmt:message key="date"/>&nbsp;<fmt:message key="finish" /></option>
							  	<option value="riskRating" 
								  	<c:if test="${!investiment && SHOW_PANEL_RISK_ADJUST == 'block'}">
								  		selected="selected"
								  	</c:if>
							  	><fmt:message key="riskRating" /></option>
							</select>
						</div>
						
						<div style="display:${SHOW_PANEL_RISK_ADJUST};">
							<div style="padding-top:10px" class="riskRatingAdjustment hide">
								<nobr>
									<input type="checkbox" style="width: 16px" id="priorityAdjustmentApply"/>
								 	&nbsp;<fmt:message key="priorityAdjustment.apply" />
								</nobr>
							</div>
							<div style="padding-top:10px" class="riskRatingAdjustment hide">
								<fmt:message key="riskRating.select_legend" /><br/>
								<select id="<%= Riskcategory.ENTITY%>" class="campo" multiple="multiple" style="height: 200px">
									<option value="-1" selected><fmt:message key="all"/></option>
									<c:forEach var="riskCatory" items="${risksCatory}">
										<option value="${riskCatory.idRiskcategory}">${tl:escape(riskCatory.name)} - ${tl:escape(riskCatory.description)}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
				</td>
				<td>
					<div id="bubble_chart" style="margin: 20px auto;"></div>
					<div style="position:absolute;z-index:99;display:none;" id="tooltipBubbleChart" class="tooltip"></div>
				</td>
			</tr>
		</table>
							
	</visual:panel>
</visual:panel>


