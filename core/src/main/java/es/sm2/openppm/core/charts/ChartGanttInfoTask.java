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
 * File: ChartGanttInfoTask.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.charts;
	
/**
 * @author javier.hernandez
 *
 */
public class ChartGanttInfoTask {
	private String name;
	private String processId;
	private String start;
	private String end;
	private String itemId;
	private String color;
	private String alpha;
	private String height;
	private String topPadding;
	private String animation;
	private String isAnimated;
	
	public ChartGanttInfoTask() {
		super();
	}
	
	public ChartGanttInfoTask(String name, String processId, String start, String end, String itemId, int type) { 
		this.name = name;
		this.processId = processId;
		this.start = start;
		this.end = end;
		this.itemId = itemId;
		this.alpha = "100";
		this.height = "12";
		this.animation = "1";
		this.isAnimated = "1";
		
		if (type == 1) { 
			this.height = "5";
			this.topPadding = "9"; 
			this.color = "000000";
		}
		else if (type == 2) { 
			this.topPadding = "20"; 
			this.color = "A9A9A9";
		}
		else { 
			this.topPadding = "5";
			this.color = "4567aa";
		}
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getProcessId() {
		return processId;
	}



	public void setProcessId(String processId) {
		this.processId = processId;
	}



	public String getStart() {
		return start;
	}



	public void setStart(String start) {
		this.start = start;
	}



	public String getEnd() {
		return end;
	}



	public void setEnd(String end) {
		this.end = end;
	}



	public String getItemId() {
		return itemId;
	}



	public void setItemId(String itemId) {
		this.itemId = itemId;
	}



	public String getColor() {
		return color;
	}



	public void setColor(String color) {
		this.color = color;
	}



	public String getAlpha() {
		return alpha;
	}



	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}



	public String getHeight() {
		return height;
	}



	public void setHeight(String height) {
		this.height = height;
	}



	public String getTopPadding() {
		return topPadding;
	}



	public void setTopPadding(String topPadding) {
		this.topPadding = topPadding;
	}



	public String getAnimation() {
		return animation;
	}



	public void setAnimation(String animation) {
		this.animation = animation;
	}



	public String getIsAnimated() {
		return isAnimated;
	}



	public void setIsAnimated(String isAnimated) {
		this.isAnimated = isAnimated;
	}

}