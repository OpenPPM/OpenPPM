<%@page import="es.sm2.openppm.front.servlets.ScenarioAnalysisServlet"%>
<%@page import="es.sm2.openppm.core.model.impl.Program"%>
<%@page import="es.sm2.openppm.front.servlets.ProgramServlet"%>
<%@page import="es.sm2.openppm.front.servlets.PriorizationProgramServlet"%>
<%@page import="es.sm2.openppm.front.utils.SettingUtil"%>
<%@page import="java.util.HashMap"%>
<%@page import="es.sm2.openppm.front.utils.OpenppmUtil"%>
<%@page import="es.sm2.openppm.core.common.Settings"%>
<%@page import="es.sm2.openppm.core.common.Constants"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored ="false"%>
	
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@taglib uri="Utilidades" prefix="tl" %>
<%@taglib uri="Openppm" prefix="op" %>
<%@taglib uri="Visual" prefix="visual" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
  ~ File: scenario_analysis.jsp
  ~ Create User: javier.hernandez
  ~ Create Date: 15/03/2015 12:47:47
  --%>

<fmt:message key="data_not_found" var="dataNotFound" />

<!-- Validations -->
<fmt:message key="validation.valid_number" var="validNumber" />
<fmt:message key="validation.range" var="range">
	<fmt:param>0</fmt:param>
	<fmt:param>100</fmt:param>
</fmt:message>
<fmt:message key="validation.integer_positive" var="integerPositive" />


<fmt:setLocale value="${locale}" scope="request"/>


<script type="text/javascript">
<!--
var scenarioAnalysisData 	;
var efficientFrontierData 	;
var scenarioAnalysisChart	;
var efficientFrontierChart	;
var scenarioAnalysisAjax 	= new AjaxCall('<%= ScenarioAnalysisServlet.REFERENCE %>','<fmt:message key="error"/>');

var color = {
	// Yellow
	selected: 	"#FFCC00",
	// Grey
	unSelected: "#E3E3E3",
	// Red
	forcedOut: 	"#CC0000",
	// Green
	forcedIn: 	"#99CC00"
};

var program = {
	// Return programs page
	back: function () {
		
		var f 			= document.forms["frm_projects"];
		f.action 		= "<%= ProgramServlet.REFERENCE %>";
		f.accion.value 	= "";
		loadingPopup();
		f.submit();
	},
	// Scenario analysis call
	scenarioAnalysisCall: function() {
		
		var f = document.forms["frm_projects"];
		
		var params = {
			accion: 	"<%= ScenarioAnalysisServlet.JX_SCENARIO_ANALYSIS_CHART %>",
			idProgram: f.<%= Program.IDPROGRAM %>.value
		};
		
		scenarioAnalysisAjax.call(params, function(data) {
			
			if (typeof data.projects !== 'undefined') {
				
				scenarioAnalysisData = data.projects;
				
				program.paintScenario();
			}
			else {
				$("#scenarioAnalysisChart").html('${dataNotFound}');
				$("#scenarioAnalysisChart").attr("style", "text-align: center; margin-top: 15px;");
				$("#scenarioAnalysisChart").prev().html('');
				$("#scenarioAnalysisChart").next().html('');
			}
			
		});
	},
	// Scenario analysis chart
	scenarioAnalysisChart: function() {
		
		var legendContent = '<span style="background-color:#FFCC00;">&nbsp;&nbsp;</span><fmt:message key="scenario.selected" />' +
							'<span style="background-color:#E3E3E3;">&nbsp;&nbsp;</span><fmt:message key="scenario.unselected" />' +
							'<span style="background-color:#CC0000;">&nbsp;&nbsp;</span><fmt:message key="scenario.forcedout" />' +
							'<span style="background-color:#99CC00;">&nbsp;&nbsp;</span><fmt:message key="scenario.forcedin" />'
							;
		
		//JQPlot parameters 
		var optionsObj;
		
		optionsObj = {
			axes: {
				xaxis:{
					label: '<fmt:message key="activity.budget" /> k',
					tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
					tickOptions:{
						formatString:"%d",
						angle: -45
					}
				},
				yaxis:{
					label:'<fmt:message key="table.priority" />'
				}
			},
			enableCursor: true
		};
		
		if (typeof scenarioAnalysisChart === 'undefined') {
			scenarioAnalysisChart = createBubbleChart('scenarioAnalysisChart', [scenarioAnalysisData], optionsObj, '${dataNotFound}', legendContent);
		}
		else {
			
			$("#legend-scenarioAnalysisChart").remove();
			
			scenarioAnalysisChart.destroy();
			
			scenarioAnalysisChart = createBubbleChart('scenarioAnalysisChart', [scenarioAnalysisData], optionsObj, '${dataNotFound}', legendContent);
		}
	},
	// Total x axe
	totalXAxeCalculated: function() {
		
		var totalXAxe = 0;
		
		var i = 0;
		for (i=0; i < scenarioAnalysisData.length; i++) {
			
			if (scenarioAnalysisData[i][3].color == color.selected || scenarioAnalysisData[i][3].color == color.forcedIn) {
				totalXAxe += scenarioAnalysisData[i][0];
			}
		}
		
		$("#totalXAxe span:last").html(totalXAxe);
	},
	// EF x axe
	EFXAxeCalculated: function() {
		
		var EFXAxe = 0;
		
			if (typeof efficientFrontierData !== 'undefined' && typeof efficientFrontierData.EFXAxe !== 'undefined') {
			EFXAxe = parseInt(efficientFrontierData.EFXAxe[0]);
		} 
		
		$("#EFXAxe span").html(EFXAxe);
	},
	// Total y axe
	totalYAxeCalculated: function() {
		
		var totalYAxe = 0;
		
		var i = 0;
		for (i=0; i < scenarioAnalysisData.length; i++) {
			
			if (scenarioAnalysisData[i][3].color == color.selected || scenarioAnalysisData[i][3].color == color.forcedIn) {
				totalYAxe += scenarioAnalysisData[i][1];
			}
		}
		
		$("#totalYAxe span:last").html(totalYAxe);
	},
	// EF y axe
	EFYAxeCalculated: function() {
		
		var EFYAxe = 0;
		
		if (typeof efficientFrontierData !== 'undefined' && typeof efficientFrontierData.EFYAxe !== 'undefined') {
			EFYAxe = parseInt(efficientFrontierData.EFYAxe[1]);
		}
		
		$("#EFYAxe span").html(EFYAxe);
	},
	// Paint scenario chart and values
	paintScenario: function () {
		
		// Chart
		program.scenarioAnalysisChart();
		
		// Total x axe
		program.totalXAxeCalculated();
		
		// EF x axe
		program.EFXAxeCalculated();
		
		// Total y axe
		program.totalYAxeCalculated();
		
		// EF y axe
		program.EFYAxeCalculated();
	},
	// Save scenario
	save: function () {
		
		$(".panel.hide").removeClass("hidePrint");
		$(".panel.hide").removeClass("hide");
		
		program.efficientFrontierCall();
	},
	// Efficient frontier call
	efficientFrontierCall: function() {
		
		if (typeof scenarioAnalysisData !== 'undefined') {
			
			var idsSelectedAndForcedIn = "";
			
			var i = 0;
			for (i = 0; i < scenarioAnalysisData.length; i++) {
				
				if (scenarioAnalysisData[i][3].color == color.selected || scenarioAnalysisData[i][3].color == color.forcedIn) {
					
					if (idsSelectedAndForcedIn != "") {
						idsSelectedAndForcedIn += ",";
					}
					
					idsSelectedAndForcedIn += scenarioAnalysisData[i][4];					
				}
			}
			
			var f = document.forms["frm_projects"];
			
			 var params = {
				accion: 				"<%= ScenarioAnalysisServlet.JX_EFFICIENT_FRONTIER_CHART %>",
				idsSelectedAndForcedIn: idsSelectedAndForcedIn,
				idProgram: 				f.<%= Program.IDPROGRAM %>.value
			}; 
			
			scenarioAnalysisAjax.call(params, function(data) {
				
				if (typeof data.selection !== 'undefined') {
					
					program.efficientFrontierChart([data.efficientFrontier, data.selection]);
					$("#efficientFrontierLegend").show();
					
					efficientFrontierData = data;
					
					program.paintScenario();
				}
				else {
					
					$("#efficientFrontierChart").html('${dataNotFound}');
					$("#efficientFrontierChart").attr("style", "text-align: center; margin-top: 15px;");
					$("#efficientFrontierLegend").hide();
				}
			});
		}
	},
	// Efficient frontier chart
	efficientFrontierChart: function(data) {
		
		//JQPlot parameters 
		var optionsObj;
		
		optionsObj = {
			axes: {
				xaxis:{
					label: '<fmt:message key="activity.budget" /> k',
					tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
					tickOptions:{
						formatString:"%d",
						angle: -45
					}
				},
				yaxis:{
					label:'<fmt:message key="table.priority" />'
				}
			},
			series:[
		    	{
		    		lineWidth:2, 
		            markerOptions: { size: 8, style:'x' }
	          	}, 
		        {
	          		color: "#FFCC00",
					showLine:false, 
					markerOptions: { size: 10, style:"filledDiamond" }
				}
			],
			showPointLabels: false,
			animate: !$.jqplot.use_excanvas
		};
		
		
		if (typeof efficientFrontierChart === 'undefined') {
			efficientFrontierChart = drawLineChart('efficientFrontierChart', data, optionsObj, '${dataNotFound}');
		}
		else {
			
			efficientFrontierChart.destroy();
			
			efficientFrontierChart = drawLineChart('efficientFrontierChart', data, optionsObj, '${dataNotFound}');
		}
	}
		
};

readyMethods.add(function () {
	
	// Open panel
	openPanel("scenarioAnalysis");
	program.scenarioAnalysisCall();
	
	// Reload chart
	$('#scenarioAnalysisChartReload').on('click', function(e){
		e.stopPropagation();
		
		program.scenarioAnalysisCall();
		
		$("#efficientFrontierChart").parent().parent().addClass("hide");
	});
	
	// Click bubble in scenario analysis
	$('#scenarioAnalysisChart').bind('jqplotDataClick', 
		function (ev, seriesIndex, pointIndex, data, radius) {
			
			var replot = false;
			
			var i = 0;
			for (i=0; i < scenarioAnalysisData.length; i++) {
				
				if (scenarioAnalysisData[i][4] == data[4]) {
					
					if (data[3].color != color.forcedIn) {
						
						replot = true;
						
						if (data[3].color == color.forcedIn || data[3].color == color.forcedOut) {
							data[3].color = color.selected;
						}
						else if (data[3].color == color.selected) {
							data[3].color = color.unSelected;
						}
						else if (data[3].color == color.unSelected) {
							data[3].color = color.forcedOut;
						}
					}
					
					break;
				}
			}
			
			if (replot) {
				
				scenarioAnalysisData[pointIndex] = data;
				
				if (!$("#efficientFrontierChart").parent().parent().hasClass("hide")) {
					program.efficientFrontierCall();
				}
				else {
					program.paintScenario();
				}
			}
		}
	);
	
	// Now bind function to the highlight event to show the tooltip
    $('#scenarioAnalysisChart').bind('jqplotDataHighlight', 
        function (ev, seriesIndex, pointIndex, data, radius) { 
    		
            var chart_left = $('#scenarioAnalysisChart').offset().left,
                chart_top = $('#scenarioAnalysisChart').offset().top,
                x = scenarioAnalysisChart.axes.xaxis.u2p(data[0]),  // convert x axis units to pixels on grid
                y = scenarioAnalysisChart.axes.yaxis.u2p(data[1]);  // convert y axis units to pixels on grid
                
            $('#tooltipScenarioAnalysisChart')
	           .css({left:chart_left+x+radius+5, top:chart_top+y})
	           .html('<span>' + data[6] + '</span>')
	           .show();
        }
    );
    
    // Bind a function to the unhighlight event to clean up after highlighting.
    $('#scenarioAnalysisChart').bind('jqplotDataUnhighlight', 
        function (ev, seriesIndex, pointIndex, data) {
    	
            $('#tooltipScenarioAnalysisChart').empty();
            $('#tooltipScenarioAnalysisChart').hide();
        }
    );
	
});

//-->
</script>
	
<form name="frm_projects" id="frm_projects" method="post">
	<input type="hidden" name="accion" value="" />
	<input type="hidden" name="<%= Program.IDPROGRAM %>" id="<%= Program.IDPROGRAM %>" value="${program.idProgram}" />

	<!-- Scenario Analysis -->
	<fmt:message key="program.scenario_analysis" var="titleScenarioAnalysis"/>
	<c:set var="scenarioAnalysisChartBTN">
		<img id="scenarioAnalysisChartReload" src="images/panel/reload.png" title="<fmt:message key="refresh"/>">
	</c:set>
	<visual:panel id="scenarioAnalysis" title="${titleScenarioAnalysis}" callback="program.scenarioAnalysisCall" buttons="${scenarioAnalysisChartBTN}" showTiltePanel="false">
		
		<div style="margin: 20px auto; height: 400px; width: 700px; position: relative;">
		
			<div class="yLegend">
				<!-- EF x axe -->
				<div id="EFYAxe" class="rotateAndtranslateDown" style="width: 80px; text-align: right; position: relative; top: 13px;">EF = <span></span>%</div>
				
				<!-- Total y axe -->
				<div id="totalYAxe" class="rotateAndtranslateUp" style="width: 80px; position: relative; text-align: right; top: 270px;"><span><fmt:message key="total"/> = </span><span></span>%</div>
			</div>
			
			<!-- Chart -->
			<div id="scenarioAnalysisChart" ></div>
		
			<div class="xLegend">
				<!-- Total x axe -->
				<div id="totalXAxe" style="width: 80px; text-align: left; float: left; margin-left: 15px;"><span><fmt:message key="total"/> = </span><span></span>k</div>
				
				<!-- EF x axe -->
				<div id="EFXAxe" style="width: 80px; text-align: right; float: right; margin-right: 15px;">EF = <span></span>k</div>
			</div>
			
		</div>
		
		<!-- Tooltip -->
		<div style="position:absolute;z-index:99;display:none;" id="tooltipScenarioAnalysisChart" class="tooltip"></div>
	</visual:panel>

	<!-- Efficient Frontier -->
	<fmt:message key="program.efficient_frontier" var="titleEfficientFrontier"/>
	<visual:panel cssClass="hide" title="${titleEfficientFrontier}" >
		
		<!-- Chart -->
		<div id="efficientFrontierChart"></div>
		
		<!-- Legend -->
		<div id="efficientFrontierLegend" style="margin:0 auto; width: 200px; display:none;">
			<div style="color:#4bb2c5; font-weight: 900; float:left;  margin-left: 30px; margin-right: 5px;">X</div>
			<div style="float:left; margin-right: 10px;"><fmt:message key="program.efficient_frontier" /></div>
			<div style="float:left; position:relative; top: -4px;  margin-right: 5px;">
				<div class="diamond"></div>
			</div>
			<div style="float:left;">Selection</div>
		</div>
		
	</visual:panel>

	<div align="right" style="margin-top:10px;" class="hidePrint">
		<a href="javascript: program.back();" class="boton"><fmt:message key="close" /></a>
		<a href="javascript: program.save();" class="boton"><fmt:message key="save" /></a>
	</div>
	
</form>