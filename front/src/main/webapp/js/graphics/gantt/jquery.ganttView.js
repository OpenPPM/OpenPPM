

/*
Options
-----------------
showWeekends: boolean
data: object
cellWidth: number
cellHeight: number
slideWidth: number
dataUrl: string
start: string
end: string
monthNames: object

*/

(function (jQuery) {
	
var startEnd;

    jQuery.fn.ganttView = function () {
    	
    	var args = Array.prototype.slice.call(arguments);
    	
    	if (args.length == 1 && typeof(args[0]) == "object") {
        	build.call(this, args[0]);
    	}
    	
    	if (args.length == 2 && typeof(args[0]) == "string") {
    		handleMethod.call(this, args[0], args[1]);
    	}
    	
    };
    
    function build(options) {
    	
    	var els = this;
        var defaults = {
            showWeekends: false,
            cellWidth: 2,	//2,4,6,8
            cellHeight: 18,
            slideWidth: 730,
            controlLine: null,
            showPoc: false
        };
        
        var opts = jQuery.extend(true, defaults, options);

		if (opts.data) {
			build();
		} else if (opts.dataUrl) {
			jQuery.getJSON(opts.dataUrl, function (data) { opts.data = data; build(); });
		}

		function build() {
			var minDays = Math.floor((opts.slideWidth / opts.cellWidth));
			startEnd = DateUtils.getBoundaryDatesFromData(opts.data, minDays, opts.start, opts.end);
			
	        els.each(function () {

	            var container = jQuery(this);

                opts.slideWidth = container.parent().width()- (opts.showPoc?191:133);

	            var div = jQuery("<div>", { "class": "ganttview" });
	            new Chart(div, opts).render();
				container.append(div);
				
				var w = jQuery("div.ganttview-vtheader", container).outerWidth() +
					jQuery("div.ganttview-slide-container", container).outerWidth();
	            container.css("width", (w + 2) + "px");
	        });
		}
    }

	function handleMethod(method, value) {
		
		if (method == "setSlideWidth") {
			var div = $("div.ganttview", this);
			div.each(function () {
				var vtWidth = $("div.ganttview-vtheader", div).outerWidth();
				$(div).width(vtWidth + value + 1);
				$("div.ganttview-slide-container", this).width(value);
			});
		}
	}

	var Chart = function(div, opts) {
		
		function render() {
			addVtHeader(div, opts.data, opts.cellHeight, opts.showPoc);

            var slideDiv = jQuery("<div>", {
                "class": "ganttview-slide-container",
                "css": { "width": opts.slideWidth + "px" }
            });
			
            dates = getDates(startEnd[0], startEnd[1]);
            var totalW = addHzHeader(slideDiv, dates, opts.cellWidth);
            addBlockContainers(slideDiv, opts.data, totalW);
            addBlocks(slideDiv, opts.data, opts.cellWidth, startEnd[0], startEnd[1]);
            div.append(slideDiv);
            applyLastClass(div.parent());
		}
		
		var monthNames = opts.monthNames;

		// Creates a 3 dimensional array [year][month][day] of every day 
		// between the given start and end dates
        function getDates(start, end) {
            var dates = [];
            start = new Date(start);
			dates[start.getFullYear()] = [];
			dates[start.getFullYear()][start.getMonth()] = [start];
			var last = start;
			while (DateUtils.compareTo(last, end) == -1) {
				var next = DateUtils.addDays(DateUtils.clone(last) ,1);
				if (!dates[next.getFullYear()]) { dates[next.getFullYear()] = []; }
				if (!dates[next.getFullYear()][next.getMonth()]) { 
					dates[next.getFullYear()][next.getMonth()] = []; 
				}
				dates[next.getFullYear()][next.getMonth()].push(next);
				last = next;
			}
			return dates;
        }

        function addVtHeader(div, data, cellHeight, showPoc) {
            var headerDiv = jQuery("<div>", { "class": "ganttview-vtheader"});

            if (showPoc) {
                headerDiv.css({ width: "188px"});
            }
            for (var i = 0; i < data.length; i++) {
                var itemDiv = jQuery("<div>", { "class": "ganttview-vtheader-item" });
                itemDiv.append(jQuery("<div>", {
                    "class": "ganttview-vtheader-item-name",
                    "css": { "height": ((data[i].series.length + 1) * (cellHeight))-3 + "px" } 
                }).append(data[i].name));


                if (showPoc) {
                    itemDiv.append(jQuery("<div>", {
                        "class": "ganttview-vtheader-item-poc",
                        "css": { "height": ((data[i].series.length + 1) * (cellHeight))-3 + "px" }
                    }).append(data[i].poc));
                }

                headerDiv.append(itemDiv);

            }
            div.append(headerDiv);
        }

        function addHzHeader(div, dates, cellWidth) {
            var headerDiv = jQuery("<div>", { "class": "ganttview-hzheader" });
            var monthsDiv = jQuery("<div>", { "class": "ganttview-hzheader-months" });
            var totalW = 0;
			for (var y in dates) {
				for (var m in dates[y]) {
					var w = dates[y][m].length * cellWidth;
					totalW = totalW + w;
					monthsDiv.append(jQuery("<div>", {
						"class": "ganttview-hzheader-month",
						"css": { "width": (w - 1) + "px"}
						})
						.append(monthNames[m] + " " + y)
						.attr("title", monthNames[m] + " " + y)
					);
				}
			}
            monthsDiv.css("width", totalW + "px");
            headerDiv.append(monthsDiv);
            
            div.append(headerDiv);
            
            return totalW;
        }
        
        function addBlockContainers(div, data, totalW) {
			
            var blocksDiv = jQuery("<div>", { "class": "ganttview-blocks" }).css("width", totalW + "px");
            
            for (var i = 0; i < data.length; i++) {
                for (var j = 0; j < data[i].series.length; j++) {
                    blocksDiv.append(jQuery("<div>", { "class": "ganttview-block-container" }));
                }
                blocksDiv.append(jQuery("<div>", { "class": "separator" }));
            }
            div.append(blocksDiv);
        }
        
        //change mm/dd/yyyy for dd/mm/yyyy and reverse
        function formatDate(date){
			var dateObj = date.split('/');
			var month 	= dateObj[0];
			dateObj[0]	= dateObj[1];
			dateObj[1] 	= month; 
			return dateObj.join('/');
        }
        
        function addBlocks(div, data, cellWidth, start, end) {
            var rows = jQuery("div.ganttview-blocks div.ganttview-block-container", div);
            var rowIdx = 0;
            var lines = 0;
            for (var i = 0; i < data.length; i++) {
                lines++;
                for (var j = 0; j < data[i].series.length; j++) {
                    lines++;
                    var series = data[i].series[j];
                    var size = DateUtils.daysBetween(series.start, series.end) + 1;
					var offset = DateUtils.daysBetween(start, series.start);
					var block = jQuery("<div>", {
                        "class": "ganttview-block",
                        "title": series.name + " " + formatDate(series.start) + " - " + formatDate(series.end),
                        "css": {
                            "width": (size*cellWidth > 9 ?((size*cellWidth)-9):cellWidth) + "px",
                            "margin-left": ((offset * cellWidth) + 3) + "px"
                        }
                    });
					
					//milestones
					if (typeof data[i].series[j].milestones !== 'undefined') {
						
						var milestones = data[i].series[j].milestones;
						
						for (var m = 0; m < milestones.length; m++) {
							
							var dateStr = formatDate(milestones[m].date);
							var position = DateUtils.daysBetween(series.start, milestones[m].date);
							var widthImage = 15 + 2; //width image
							var margin = ((position * cellWidth) - widthImage) < 0 ? 0 : ((position * cellWidth) - widthImage);
							var milestone = $('<div/>', { 
								'class':'milestone',
								"title": dateStr + ' - ' + milestones[m].name,
								"css":{
									'margin-left': margin + "px"
								}
							});
							
							if (milestones[m].type === 'real') {
								milestone.append($('<img>', { 'src': 'images/gantt/milestoneReal.png'}));
							}
							else if (milestones[m].type === 'estimated') {
								milestone.append($('<img>', { 'src': 'images/gantt/milestoneEstimated.png'}));
							}
							else if (milestones[m].type === 'planned') {
								milestone.append($('<img>', { 'src': 'images/gantt/milestonePlanned.png'}));
							}
							
							block.prepend(milestone);
						}
					}
					
                    addBlockData(block, data[i], series);
                    if (data[i].series[j].color) {
                        block.css("background-color", data[i].series[j].color);
                    }
                    block.append(jQuery("<div>", { "class": "ganttview-block-text" }).text(""));
					jQuery(rows[rowIdx]).append(block);
					
					//percentage
					if(typeof data[i].series[j].complete !== 'undefined'){
						var blockComplete = data[i].series[j].complete;
						var sizeComplete = DateUtils.daysBetween(blockComplete.start, blockComplete.end) + 1;
						var offsetComplete = DateUtils.daysBetween(start, blockComplete.start);
						var complete = $("<div>",{
							"class": "gantview-block-complete",
							"title": blockComplete.poc + " " + blockComplete.name,
							"css": {
								"width": (sizeComplete * cellWidth > 9 ? ((sizeComplete * cellWidth) - 9): cellWidth) + "px",
								"margin-left": ((offsetComplete * cellWidth) + 5) + "px",
								"background-color": blockComplete.color
							}
						});
						
						// Status Date
						if(typeof blockComplete.statusDate !== 'undefined'){
							var position = DateUtils.daysBetween(series.start, blockComplete.statusDate);
							var statusDate = $('<div/>', { 
								'class':'statusDate',
								"title": formatDate(blockComplete.statusDate) ,
								"css":{
									'margin-left':((position * cellWidth) -3) + "px"
								}
							});
							statusDate.append($('<img>', { 'src': 'images/gantt/statusDate.png'}));
							complete.append(statusDate);
						}
						
						$(rows[rowIdx]).append(complete);
					}
					
					//Control line
					if(opts.controlLine != null && i==0 && j==0){
		            	var offsetControl = DateUtils.daysBetween(series.start, opts.controlLine);
		            	var controlLine = $('<div/>', { 
							'class':'controlLine',
							"title": formatDate(opts.controlLine) ,
							"css":{
								'margin-left':((offsetControl * cellWidth) -9) + "px"
							}
						});
		            	controlLine.append($('<img>', { 'src': 'images/gantt/statusDate.png'}));
		            	block.append(controlLine);
					}
					
                    rowIdx = rowIdx + 1;
                }
            }
            
	        // Time line 
	        var blocksDiv		= jQuery("div.ganttview-blocks", div);
	        var offsetTime		= DateUtils.daysBetween(start, new Date());
	        var drawTimeLine	= (offsetTime > 0 && offsetTime <= DateUtils.daysBetween(start, end));
	        
	        if(drawTimeLine){
	            var timeLineWidth = ((offsetTime * cellWidth) -4);
	        	addLine(timeLineWidth, lines, 'time-line', blocksDiv);
	        }
        }
        
        function addLine(lineWidth, lines, typeLine, blocksDiv) {
            var lineHeight = 18 * lines;
            var styleLine = "height:"+lineHeight+"px; width:"+lineWidth+"px;";
            blocksDiv.prepend(jQuery("<div>", { "class": typeLine, "style": styleLine }));
        }
        
        function addBlockData(block, data, series) {
        	// This allows custom attributes to be added to the series data objects
        	// and makes them available to the 'data' argument of click, resize, and drag handlers
        	var blockData = { id: data.id, name: data.name };
			
        	jQuery.extend(blockData, series);
        	block.data("block-data", blockData);
        }

        function applyLastClass(div) {
            jQuery("div.ganttview-grid-row div.ganttview-grid-row-cell:last-child", div).addClass("last");
            jQuery("div.ganttview-hzheader-days div.ganttview-hzheader-day:last-child", div).addClass("last");
            jQuery("div.ganttview-hzheader-months div.ganttview-hzheader-month:last-child", div).addClass("last");
        }
		
		return {render: render};
	};


	var DateUtils = {
    	
        daysBetween: function (start, end) {
            if (!start || !end) { return 0; }
            //start = Date.parse(start); end = Date.parse(end);
            start	= new Date(start);
        	end 	= new Date(end);
            if (start.getFullYear() == 1901 || end.getFullYear() == 8099) { return 0; }
            var count = 0, date = DateUtils.clone(start);
            while (DateUtils.compareTo(date, end) == -1) { count = count + 1; DateUtils.addDays(date, 1); }
            return count;
        },
        
        isWeekend: function (date) {
            return date.getDay() % 6 == 0;
        },

		getBoundaryDatesFromData: function (data, minDays, startDate, endDate) {
			var minStart = new Date(); 
			var maxEnd 	 = new Date();
			
			if( (typeof startDate == 'undefined') || (typeof endDate == 'undefined') ){
				for (var i = 0; i < data.length; i++) {
					for (var j = 0; j < data[i].series.length; j++) {
						var start = Date.parse(data[i].series[j].start);
						var end = Date.parse(data[i].series[j].end);
						if (i == 0 && j == 0) { 
							minStart 	= start; 
							maxEnd 		= end;
						}
						if (DateUtils.compareTo(minStart, start) == 1) { minStart = start; }
						if (DateUtils.compareTo(maxEnd, end) == -1) { maxEnd = end; }
					}
				}
			}
			
			if (!jQuery.isEmptyObject(startDate)) {
				minStart = Date.parse(startDate);
			}
			if (!jQuery.isEmptyObject(endDate)) {
				maxEnd 	 = Date.parse(endDate);
			}
			
			// Insure that the width of the chart is at least the slide width to avoid empty
			// whitespace to the right of the grid
			if (DateUtils.daysBetween(minStart, maxEnd) < minDays) {
				maxEnd = DateUtils.addDays(DateUtils.clone(minStart), minDays);
			}
			
			return [minStart, maxEnd];
		},
		
		//TODO todas las funciones de date.js conflictivas
		compareTo: function (date1, date2) {
			if (typeof date1 !== 'number') {
				date1 = Date.parse(date1);
			}
			if (typeof date2 !== 'number') {
				date2 = Date.parse(date2);
			}

			if (isNaN(date1) || isNaN(date2)) {
				throw new Error(date1+" - "+date2);
			}
			else if (typeof date1 === 'number' && typeof date2 === 'number') {
				return (date1 < date2) ? -1 : (date1 > date2) ? 1 : 0;
			}
			else {
				throw new TypeError(date1+" - "+date2);
			}
		},
		clone: function(date) {
			if (typeof date === 'number') {
				date = new Date(date);
			}
			return new Date(date.getTime());
		},
		addDays: function(date, value) {
			date.setDate(date.getDate() + value);
			return date;
		}
        
    };

})(jQuery);