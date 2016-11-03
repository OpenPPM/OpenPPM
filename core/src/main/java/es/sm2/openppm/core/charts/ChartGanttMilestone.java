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
 * File: ChartGanttMilestone.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.charts;

/**
 * @author javier.hernandez
 *
 */
public class ChartGanttMilestone {
	private String date;
	private String taskId;
	private String radius;
	private String color;
	private String shape;
	private String numSides;
	private String borderThickness;
	private String name;
	private String topPadding;
	
	public ChartGanttMilestone(String date, String taskId, String name) {
		this.date = date;
		this.taskId = taskId;
		this.radius = "9";
		this.color = "333333";
		this.shape = "polygon";
		this.numSides = "3";
		this.borderThickness = "1";
		this.name = name;
		this.topPadding = "20";
	}
	
	public ChartGanttMilestone(String date, String taskId, int radius, String name) {
		this.date = date;
		this.taskId = taskId;
		this.radius = String.valueOf(radius);
		this.color = "333333";
		this.shape = "polygon";
		this.numSides = "3";
		this.borderThickness = "1";
		this.name = name;
		this.topPadding = "20";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getNumSides() {
		return numSides;
	}

	public void setNumSides(String numSides) {
		this.numSides = numSides;
	}

	public String getBorderThickness() {
		return borderThickness;
	}

	public void setBorderThickness(String borderThickness) {
		this.borderThickness = borderThickness;
	}

	public void setTopPadding(String topPadding) {
		this.topPadding = topPadding;
	}

	public String getTopPadding() {
		return topPadding;
	}
}