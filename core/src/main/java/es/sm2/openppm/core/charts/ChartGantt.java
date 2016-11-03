/*
 * Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program has been created in the hope that it will be useful.
 * It is distributed WITHOUT ANY WARRANTY of any Kind,
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * For more information, please contact SM2 Software & Services Management.
 * Mail: info@talaia-openppm.com
 * Web: http://www.talaia-openppm.com
 *
 * Module: core
 * File: ChartGantt.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.charts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;

/**
 * @author javier.hernandez
 *
 */
public class ChartGantt extends AbstractChart {
	
	Logger logger = Logger.getLogger(ChartGantt.class);
	
	private String dateFormat;
	private String hoverCapBorderColor;
	private String hoverCapBgColor;
	private String ganttWidthPercent;
	private String ganttLineAlpha;
	private String canvasBorderColor;
	private String canvasBorderThickness;
	private String gridBorderColor;
	private String gridBorderAlpha;
	private String titleBgColor;
	private String titleStart;
	private String titleEnd;
	private String titleAlign;
	private String titleName;
	private String titleFontColor;
	private String titleIsBold;
	private String titleFontSize;
	private String categoriesBgColor;
	private String categoriesFontColor;
	private String categoriesFontSize;
	private List<ChartGanttCategory> listCategory  = new ArrayList<ChartGanttCategory>();
	private String taskInfoHeaderText;
	private String taskInfoFontColor;
	private String taskInfoFontSize;
	private String taskInfoIsBold;
	private String taskInfoIsAnimated;
	private String taskInfoBgColor;
	private String taskInfoHeaderVAling;
	private String taskInfoHeaderbgColor;
	private String taskInfoHeaderFontColor;
	private String taskInfoHeaderFontSize;
	private String taskInfoWidth;
	private String taskInfoAlign;
	private List<ChartGanttTask> listTasks  = new ArrayList<ChartGanttTask>();
	private String numTasks;
	private List<ChartGanttInfoTask> listInfoTask  = new ArrayList<ChartGanttInfoTask>();
	private List<ChartGanttMilestone> listMilestones  = new ArrayList<ChartGanttMilestone>();
	
	/**
	 * Create the x-axis with each month
	 * @param titleStart
	 * @param titleEnd
	 * @param titleName
	 * @param taskInfoHeaderText
	 * @param numTasks
	 * @throws ParserConfigurationException 
	 */
	
	public ChartGantt(ResourceBundle idioma, String titleStart, String titleEnd, String titleName, String taskInfoHeaderText, String numTasks) throws ParserConfigurationException {
		
		super(idioma);
		
		SimpleDateFormat simpleFormat = new SimpleDateFormat(DateUtil.getDatePattern(idioma), idioma.getLocale());
		
		int day;
		int month;
		
		this.dateFormat = DateUtil.getDatePattern(idioma);
		this.hoverCapBorderColor = "2222ff";
		this.hoverCapBgColor = "e1f5ff";
		this.ganttWidthPercent = "";
		this.ganttLineAlpha = "80";
		this.canvasBorderColor = "024455";
		this.canvasBorderThickness = "0";
		this.gridBorderColor = "4567aa";
		this.gridBorderAlpha = "20";
		this.titleBgColor = "688BB4";
		this.titleStart = titleStart;
		this.titleEnd = titleEnd;
		this.titleAlign = "center";
		this.titleName = titleName;
		this.titleFontColor = StringPool.COLOR_WHITE;
		this.titleIsBold = "1";
		this.titleFontSize = "12";
		this.categoriesBgColor = StringPool.COLOR_WHITE;
		this.categoriesFontColor = "000000";
		this.categoriesFontSize = "11";
		this.taskInfoHeaderText = taskInfoHeaderText;
		this.taskInfoFontColor = "000000";
		this.taskInfoFontSize = "11";
		this.taskInfoIsBold = "0";
		this.taskInfoIsAnimated = "1";
		this.taskInfoBgColor = StringPool.COLOR_WHITE;
		this.taskInfoHeaderVAling = "right";
		this.taskInfoHeaderbgColor = StringPool.COLOR_WHITE;
		this.taskInfoHeaderFontColor = "000000";
		this.taskInfoHeaderFontSize = "12";
		this.taskInfoWidth = "";
		this.taskInfoAlign = "left";
		this.numTasks = numTasks;
		
		try {
			Date startDate = simpleFormat.parse(this.titleStart);
			Date endDate = simpleFormat.parse(this.titleEnd);
			
			Calendar dateStart = DateUtil.getCalendar();
			dateStart.setTime(startDate);
			dateStart.set(Calendar.DAY_OF_MONTH,1);
			
			Calendar dateEnd = DateUtil.getCalendar();
			dateEnd.setTime(endDate);
			dateEnd.set(Calendar.DAY_OF_MONTH,dateEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
			
			Calendar endOfMonth = DateUtil.getCalendar();
			
			this.titleStart = simpleFormat.format(dateStart.getTime());
			this.titleEnd = simpleFormat.format(dateEnd.getTime());
			
			while (dateStart.before(dateEnd)) {
				
				day = dateStart.getActualMaximum(Calendar.DAY_OF_MONTH);
				month = (dateStart.get(Calendar.MONTH) +1);
				
				endOfMonth.setTime(dateStart.getTime());
				endOfMonth.set(Calendar.DAY_OF_MONTH, day);
				
				ChartGanttCategory elementCategory = new ChartGanttCategory();
				
				elementCategory.setStart(simpleFormat.format(dateStart.getTime()));
				elementCategory.setEnd(simpleFormat.format(endOfMonth.getTime()));
				
		        elementCategory.setName(idioma.getString("month.min_"+month));
				
				this.listCategory.add(elementCategory);
				
				dateStart.set(Calendar.MONTH, dateStart.get(Calendar.MONTH) +1);
			}
		} 
		catch (ParseException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public void setHoverCapBorderColor(String hoverCapBorderColor) {
		this.hoverCapBorderColor = hoverCapBorderColor;
	}
	
	public void setHoverCapBgColor(String hoverCapBgColor) {
		this.hoverCapBgColor = hoverCapBgColor;
	}
	
	public void setGanttWidthPercent(String ganttWidthPercent) {
		this.ganttWidthPercent = ganttWidthPercent;
	}
	
	public void setGanttLineAlpha(String ganttLineAlpha) {
		this.ganttLineAlpha = ganttLineAlpha;
	}
	
	public void setCanvasBorderColor(String canvasBorderColor) {
		this.canvasBorderColor = canvasBorderColor;
	}
	
	public void setCanvasBorderThickness(String canvasBorderThickness) {
		this.canvasBorderThickness = canvasBorderThickness;
	}
	
	public void setGridBorderColor(String gridBorderColor) {
		this.gridBorderColor = gridBorderColor;
	}
	
	public void setGridBorderAlpha(String gridBorderAlpha) {
		this.gridBorderAlpha = gridBorderAlpha;
	}
	
	public void setTitleBgColor(String titleBgColor) {
		this.titleBgColor = titleBgColor;
	}
	
	public void setTitleStart(String titleStart) {
		this.titleStart = titleStart;
	}
	
	public void setTitleEnd(String titleEnd) {
		this.titleEnd = titleEnd;
	}
	
	public void setTitleAlign(String titleAlign) {
		this.titleAlign = titleAlign;
	}
	
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	
	public void setTitleFontColor(String titleFontColor) {
		this.titleFontColor = titleFontColor;
	}
	
	public void setTitleIsBold(String titleIsBold) {
		this.titleIsBold = titleIsBold;
	}
	
	public void setTitleFontSize(String titleFontSize) {
		this.titleFontSize = titleFontSize;
	}
	
	public void setCategoriesBgColor(String categoriesBgColor) {
		this.categoriesBgColor = categoriesBgColor;
	}
	
	public void setCategoriesFontColor(String categoriesFontColor) {
		this.categoriesFontColor = categoriesFontColor;
	}
	
	public void setCategoriesFontSize(String categoriesFontSize) {
		this.categoriesFontSize = categoriesFontSize;
	}
	
	public void addCategory(String start, String end, String name) {
		ChartGanttCategory elementCategory = new ChartGanttCategory(start, end, name);
		this.listCategory.add(elementCategory);
	}
	
	public void setTaskInfoHeaderText(String taskInfoHeaderText) {
		this.taskInfoHeaderText = taskInfoHeaderText;
	}
	
	public void setTaskInfoFontColor(String taskInfoFontColor) {
		this.taskInfoFontColor = taskInfoFontColor;
	}
	
	public void setTaskInfoFontSize(String taskInfoFontSize) {
		this.taskInfoFontSize = taskInfoFontSize;
	}
	
	public void setTaskInfoIsBold(String taskInfoIsBold) {
		this.taskInfoIsBold = taskInfoIsBold;
	}
	
	public void setTaskInfoIsAnimated(String taskInfoIsAnimated) {
		this.taskInfoIsAnimated = taskInfoIsAnimated;
	}
	
	public void setTaskInfoBgColor(String taskInfoBgColor) {
		this.taskInfoBgColor = taskInfoBgColor;
	}
	
	public void setTaskInfoHeaderVAling(String taskInfoHeaderVAling) {
		this.taskInfoHeaderVAling = taskInfoHeaderVAling;
	}
	
	public void setTaskInfoHeaderbgColor(String taskInfoHeaderbgColor) {
		this.taskInfoHeaderbgColor = taskInfoHeaderbgColor;
	}
	
	public void setTaskInfoHeaderFontColor(String taskInfoHeaderFontColor) {
		this.taskInfoHeaderFontColor = taskInfoHeaderFontColor;
	}
	
	public void setTaskInfoHeaderFontSize(String taskInfoHeaderFontSize) {
		this.taskInfoHeaderFontSize = taskInfoHeaderFontSize;
	}
	
	public void setTaskInfoWidth(String taskInfoWidth) {
		this.taskInfoWidth = taskInfoWidth;
	}
	
	public void setTaskInfoAlign(String taskInfoAlign) {
		this.taskInfoAlign = taskInfoAlign;
	}
	
	public void addTask(String name, String itemId) {
		ChartGanttTask elementTask = new ChartGanttTask(name, itemId);
		this.listTasks.add(elementTask);
	}
	
	public void setNumTasks(String numTasks) {
		this.numTasks = numTasks;
	}
	
	public String getNumTasks() {
		return this.numTasks;
	}
	
	public void addCategories(String name, String  processId, String  start, String  end, String  itemId, int type) {
		ChartGanttInfoTask elementTask = new ChartGanttInfoTask(name, processId, start, end, itemId, type);
		this.listInfoTask.add(elementTask);
	}
	
	public void addMilestone(String date, String taskId, String name) {
		ChartGanttMilestone elementMilestone = new ChartGanttMilestone(date, taskId, name);
		this.listMilestones.add(elementMilestone);
	}
	
	public void addMilestone(String date, String taskId, int radius, String name) {
		ChartGanttMilestone elementMilestone = new ChartGanttMilestone(date, taskId, radius, name);
		this.listMilestones.add(elementMilestone);
	}
	
	@Override
	public void createXML() {
		
		Element root = getDocument().createElement("chart");
		
		root.setAttribute("dateFormat",				dateFormat);
		root.setAttribute("hoverCapBorderColor",	hoverCapBorderColor);
		root.setAttribute("hoverCapBgColor",		hoverCapBgColor);
		root.setAttribute("ganttWidthPercent",		ganttWidthPercent);
		root.setAttribute("ganttLineAlpha",			ganttLineAlpha);
		root.setAttribute("canvasBorderColor",		canvasBorderColor);
		root.setAttribute("canvasBorderThickness",	canvasBorderThickness);
		root.setAttribute("gridBorderColor",		gridBorderColor);
		root.setAttribute("gridBorderAlpha",		gridBorderAlpha);
		
		getDocument().appendChild(root);
		
		Element categories = getDocument().createElement("categories");
		categories.setAttribute("bgColor", titleBgColor);
		
		Element category = getDocument().createElement("category");
		category.setAttribute("start",		titleStart);
		category.setAttribute("end",		titleEnd);
		category.setAttribute("align",		titleAlign);
		category.setAttribute("name",		titleName);
		category.setAttribute("fontColor",	titleFontColor);
		category.setAttribute("isBold",		titleIsBold);
		category.setAttribute("fontSize",	titleFontSize);
		
		categories.appendChild(category);
		root.appendChild(categories);
		
		categories = getDocument().createElement("categories");
		categories.setAttribute("bgColor", categoriesBgColor);
		categories.setAttribute("fontColor", categoriesFontColor);
		categories.setAttribute("fontSize", categoriesFontSize);
		
		for (ChartGanttCategory elementCategory : listCategory) {
			
			category = getDocument().createElement("category");
			category.setAttribute("start",	elementCategory.getStart());
			category.setAttribute("end",	elementCategory.getEnd());
			category.setAttribute("align",	elementCategory.getAlign());
			category.setAttribute("name",	elementCategory.getName());
			category.setAttribute("isBold",	elementCategory.getIsBold());
			
			categories.appendChild(category);
		}
		
		root.appendChild(categories);

		Element processes = getDocument().createElement("processes");
		processes.setAttribute("headerText",		taskInfoHeaderText);
		processes.setAttribute("fontColor",			taskInfoFontColor);
		processes.setAttribute("fontSize",			taskInfoFontSize);
		processes.setAttribute("isBold",			taskInfoIsBold);
		processes.setAttribute("isAnimated",		taskInfoIsAnimated);
		processes.setAttribute("bgColor",			taskInfoBgColor);
		processes.setAttribute("headerVAlign",		taskInfoHeaderVAling);
		processes.setAttribute("headerbgColor",		taskInfoHeaderbgColor);
		processes.setAttribute("headerFontColor",	taskInfoHeaderFontColor);
		processes.setAttribute("headerFontSize",	taskInfoHeaderFontSize);
		processes.setAttribute("width",				taskInfoWidth);
		processes.setAttribute("align",				taskInfoAlign);
		
		for (ChartGanttTask elementTask: listTasks) {
			
			Element process = getDocument().createElement("process");
			process.setAttribute("name",	elementTask.getName());
			process.setAttribute("id",		elementTask.getItemId());
			
			processes.appendChild(process);
		}
		
		root.appendChild(processes);

		Element tasks = getDocument().createElement("tasks");
		tasks.setAttribute("width", numTasks);
		
		for (ChartGanttInfoTask elementListInfoTask : listInfoTask) {
			
			Element task = getDocument().createElement("task");
			task.setAttribute("name", elementListInfoTask.getName());
			task.setAttribute("processId", elementListInfoTask.getProcessId());
			task.setAttribute("start", elementListInfoTask.getStart());
			task.setAttribute("end", elementListInfoTask.getEnd());
			task.setAttribute("id", elementListInfoTask.getItemId());
			task.setAttribute("color", elementListInfoTask.getColor());
			task.setAttribute("height", elementListInfoTask.getHeight());
			task.setAttribute("topPadding", elementListInfoTask.getTopPadding());
			task.setAttribute("animation", elementListInfoTask.getAnimation());
			
			tasks.appendChild(task);
		}
		root.appendChild(tasks);
		
		if (listMilestones != null) {
			
			Element milestones = getDocument().createElement("milestones");
			
			for (ChartGanttMilestone item : listMilestones) {
				
				Element milestone = getDocument().createElement("milestone");
				milestone.setAttribute("date", item.getDate());
				milestone.setAttribute("taskId", item.getTaskId());
				milestone.setAttribute("radius", item.getRadius());
				milestone.setAttribute("color", item.getColor());
				milestone.setAttribute("shape", item.getShape());
				milestone.setAttribute("numSides", item.getNumSides());
				milestone.setAttribute("borderThickness", item.getBorderThickness());
				
				
				milestones.appendChild(milestone);
			}
			root.appendChild(milestones);
		}
	}
}


