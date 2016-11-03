/**
 * JQPLOT CHARTS
 */


/**
 * P A R A M E T E R S
 * 
 * @param chartDiv: String
 * @param dataSeries: Array Object
 * @param optionsObj: {
 * 	title:	String
 * 	ticks: Array Object
 * 	labelX: String
 * 	labelY: String
 * 	seriesColors: Array Object
 * 	seriesParameters: Array Object -->Exemple: [{show:false,shadow:false},{},...]
 *  enableCursor: boolean
 *  formatString: String -->Exemple: '%.2f'
 *  stackSeries: boolean
 *  shadow: boolean
 * }
 * @param dataNotFound: String
 * @returns: Object
 */



/**
 * Function to draw line charts
 */
function drawLineChart(chartDiv, dataSeries, optionsObj, dataNotFound){
	
	var chart	= null;
	var content = false;
	
	try {
		
		if (dataSeries.length != 0) {
			var i;
			for(i=0;i<dataSeries.length;i++){
				if(dataSeries[i].length != 0){
					content = true;
				}
			}
		}
	
		if (content == true){
			$("#" + chartDiv).attr( 'style', 'margin: 20px auto;height:400px;width:700px;');
			
			if (typeof optionsObj.enableCursor === 'undefined') {
				optionsObj.enableCursor = false;
			}
			if (typeof optionsObj.formatString === 'undefined') {
				optionsObj.formatString = '%.0f';
			}
			if (typeof optionsObj.showPointLabels === 'undefined') {
				optionsObj.showPointLabels = true;
			}
			if (typeof optionsObj.animate === 'undefined') {
                optionsObj.animate = false;
            }
            if (typeof optionsObj.padding === 'undefined') {
                optionsObj.padding = 0;
			}
			if (typeof optionsObj.axes === 'undefined') {
				optionsObj.axes = 	{
										xaxis:{
											label: optionsObj.labelX

										},
										yaxis:{
											label: optionsObj.labelY,
											labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
											labelOptions: {
												angle: -90
											},
											tickOptions:{
												formatString: optionsObj.formatString
											},
                                            pad: optionsObj.padding
                                            }
										}

								  	};
			}
			if (typeof optionsObj.series === 'undefined') {
				optionsObj.series = [];
			}
			
			var dataOptions = {
			  	title: optionsObj.title,
			  	axesDefaults: {
					tickRenderer: $.jqplot.CanvasAxisTickRenderer,
					tickOptions: {
						fontFamily: 'Arial,Helvetica,sans-serif',
						fontSize: '10px',
						textColor: '#444444'
					},
					labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
					labelOptions: {
						fontFamily: 'Arial,Helvetica,sans-serif',
						fontSize: '12px',
						textColor: '#444444'
					},
					pad:1.0
				},
			  	axes: optionsObj.axes,
			  	seriesColors: optionsObj.seriesColors,
			  	// Turns on animation for all series in this plot.
			    animate: optionsObj.animate,
				seriesDefaults: { 
			        showMarker:true,
			        pointLabels: { show: optionsObj.showPointLabels }
		    	},
				highlighter: {
					show: true,
					sizeAdjust: 7.5,
				    formatString: optionsObj.formatString + ', ' + '%.0d'
			  	},
				cursor:{ 
					show: optionsObj.enableCursor,
					zoom:true, 
					showTooltip:false
			   },
			   series: optionsObj.series
			};
			
			chart = $.jqplot(chartDiv, dataSeries, dataOptions);
			
			return chart;
		}
	

	catch (e) {
		content = false;
	}
	
	if (!content) {
		
		$("#" + chartDiv)
		.attr( 'style', 'margin: 20px auto;text-align:center')
		.html(dataNotFound);
	}
	
	return chart;
}

/**
 * Function to draw bar charts
 */
function drawBarChart(chartDiv, dataSeries, optionsObj, dataNotFound){
	
	var chart;
	var content = false;
	
	if (dataSeries.length != 0) {
		var i;
		for(i=0;i<dataSeries.length;i++){
			if(dataSeries[i].length != 0){
				content = true;
			}
		}
	}
	
	if (content == true){
		
		$("#" + chartDiv)
			.html('') 
			.attr( 'style', 'margin: 20px auto;height:400px;width:700px;');
		
		if(typeof optionsObj.enableCursor === 'undefined'){
			optionsObj.enableCursor = false;
		}
		if(typeof optionsObj.formatString === 'undefined'){
			optionsObj.formatString = '%.0f';
		}
		if(typeof optionsObj.stackSeries === 'undefined'){
			optionsObj.stackSeries = true;
		}
		if(typeof optionsObj.shadow === 'undefined'){
			optionsObj.shadow = true;
		}
		if(typeof optionsObj.barMargin === 'undefined'){
			optionsObj.barMargin = 10;
		}
		if(typeof optionsObj.highlightMouseOver === 'undefined'){
			optionsObj.highlightMouseOver = true;
		}
		if(typeof optionsObj.showPointLabels === 'undefined'){
			optionsObj.showPointLabels = false;
		}
		if(typeof optionsObj.showHighlighter === 'undefined'){
			optionsObj.showHighlighter = true;
		}
		if(typeof optionsObj.tickAngleY === 'undefined'){
			optionsObj.tickAngleY = -90;
		}
		if(typeof optionsObj.animate === 'undefined'){
			optionsObj.animate = false;
		}
		if(typeof optionsObj.minX === 'undefined'){
			optionsObj.minX = null;
		}
		if(typeof optionsObj.minY === 'undefined'){
			optionsObj.minY = null;
		}
		
	 	var dataOptions = {
			title: optionsObj.title,
			axesDefaults: {
				tickRenderer: $.jqplot.CanvasAxisTickRenderer ,
				tickOptions: {
					fontFamily: 'Arial,Helvetica,sans-serif',
					fontSize: '10px',
					textColor: '#444444'
				},
				labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
				labelOptions: {
					fontFamily: 'Arial,Helvetica,sans-serif',
					fontSize: '12px',
					textColor: '#444444'
				}
			},
			axes:{
				xaxis: {
					label: optionsObj.labelX,
					renderer: $.jqplot.CategoryAxisRenderer,
					ticks: optionsObj.ticks,
					tickOptions: {
						angle: optionsObj.tickAngleY
					},
					min:optionsObj.minX
				},
				yaxis: {
					tickOptions: {
						angle: 0,
						formatString: optionsObj.formatString
					},
					label: optionsObj.labelY,
					labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
					labelOptions: {
						angle:-90
					},
					min:optionsObj.minY
				}
			},
			seriesColors: optionsObj.seriesColors,
			stackSeries: optionsObj.stackSeries,
            // Turns on animation for all series in this plot.
            animate: optionsObj.animate,
			seriesDefaults:{
				shadow: optionsObj.shadow,
				renderer:$.jqplot.BarRenderer,
				rendererOptions: {
				  fillToZero: true,
				  varyBarColor: true,
				  shadowOffset:2,
				  shadowDepth:5,
				  barMargin: optionsObj.barMargin,
				  useNegativeColors:false,
				  highlightMouseOver: optionsObj.highlightMouseOver
				},
				pointLabels: {
					show: optionsObj.showPointLabels,
					hideZeros:true
				}
			},
			series: optionsObj.seriesParameters,
			highlighter: {
			  show: optionsObj.showHighlighter,
			  showMarker:false,
			  tooltipOffset:0,
			  tooltipLocation:'n',
			  tooltipAxes: 'y',
			  yvalues: 1,
			  formatString: optionsObj.formatString
			},
			cursor:{ 
				show: optionsObj.enableCursor,
				zoom:true, 
				showTooltip:false
		   }
		};
		
	 	chart = $.jqplot(chartDiv, dataSeries, dataOptions);

	 	return chart;
	}
	else {
		
		$("#" + chartDiv)
			.attr( 'style', 'margin: 20px auto;text-align:center')
			.html(dataNotFound);
		
		return chart;
	}
}


/**
 * Function to draw bubble charts
 */
function createBubbleChart(chartDiv, dataSeries, optionsObj, dataNotFound, legendContent){
	var chart;
	var content = false;

	if (dataSeries.length != 0) {
		var i;
		for(i=0;i<dataSeries.length;i++){
			if(dataSeries[i].length != 0){
				content = true;
			}
		}
	}

	if (content == true) {
		$("#" + chartDiv).attr( 'style', 'margin: 20px auto;height:400px;width:700px;');
		
		if (typeof optionsObj.axes === 'undefined') {
			optionsObj.axes = 	{
									xaxis: {
										label: optionsObj.labelX
									},
									yaxis: {
										label: optionsObj.labelY,
										labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
										labelOptions: {
											angle:-90
										}
									}
							  	};
		}
		
		
		var dataOptions = {
			title: optionsObj.title,
			axesDefaults: {
				renderer:$.jqplot.LinearAxisRenderer,
				rendererOptions:{
	     		   forceTickAt0:true,
	     		   forceTickAt100:true
				},
				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				tickOptions:{ 
					formatString:'%d',
					fontFamily: 'Arial,Helvetica,sans-serif',
					fontSize: '10px',
					textColor: '#444444'
				},
				pad: 1.0,
				labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
				labelOptions: {
					fontFamily: 'Arial,Helvetica,sans-serif',
					fontSize: '12px',
					textColor: '#444444'
				}
			},
			seriesDefaults:{
				renderer: $.jqplot.BubbleRenderer,
				rendererOptions: {
					bubbleAlpha: 0.6,
					highlightAlpha: 0.8,
					autoscaleBubbles: false
				}
			},
			axes: optionsObj.axes
		};
		
		chart = $.jqplot(chartDiv, dataSeries, dataOptions);
		
		//legend
		 var legend = $("<div>", {
             "class": "legendChart",
             "id": "legend-" + chartDiv
         });
		 $("#" + chartDiv).after(legend);
		legend.html('');
		legend.append(legendContent);
		
	 	return chart;
	}
	else {
		$("#" + chartDiv)
			.attr( 'style', 'margin: 20px auto;text-align:center')
			.html(dataNotFound);
		
		return chart;
	}
	
}