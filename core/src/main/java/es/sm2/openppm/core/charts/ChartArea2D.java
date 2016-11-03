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
 * File: ChartArea2D.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.charts;

import java.util.ArrayList;
import java.util.List;

public class ChartArea2D {

	private String yAxisMinValue;
	private String yAxisMaxValue;
	private String areaBgColor;
	private String areaBorderColor;
	private String showValues;
	private String decimalPrecision;
	private List<ChartElements> set = new ArrayList<ChartElements>();
	

	/**
	 * Create Chart with default settings
	 */
	public ChartArea2D() {
		this.areaBgColor = "C2D1FF";
		this.areaBorderColor = "000000";
		this.showValues = "1";
		this.decimalPrecision = "0";
		this.yAxisMaxValue = "100";
	}

	
	/**
	 * @param yAxisMinValue
	 * @param yAxisMaxValue
	 * @param areaBgColor
	 * @param areaBorderColor
	 * @param showValues
	 * @param decimalPrecision
	 * @param set
	 */
	public ChartArea2D(String yAxisMinValue, String yAxisMaxValue,
			String areaBgColor, String areaBorderColor, String showValues,
			String decimalPrecision, List<ChartElements> set) {
		super();
		this.yAxisMinValue = yAxisMinValue;
		this.yAxisMaxValue = yAxisMaxValue;
		this.areaBgColor = areaBgColor;
		this.areaBorderColor = areaBorderColor;
		this.showValues = showValues;
		this.decimalPrecision = decimalPrecision;
		this.set = set;
	}

	/**
	 * Return XML for generate Chart
	 * @return
	 */
	public String generateXML() {

		StringBuilder xml = new StringBuilder();
		xml.append("<graph ");
		
		if (yAxisMinValue != null) {
			xml.append(" yAxisMinValue='" + yAxisMinValue + "'");
		}
		if (yAxisMaxValue != null) {
			xml.append(" yAxisMaxValue='" + yAxisMaxValue + "'");
		}
		xml.append(" areaBgColor='" + areaBgColor + "' areaBorderColor='" + areaBorderColor + 
				"' showValues='" + showValues + "' decimalPrecision='" + decimalPrecision + "'>");
		for (ChartElements element : set) {
			xml.append("<set name='" + element.getName() + "' value='" + element.getValue() + "' />");
		}
		xml.append("</graph> ");
		return xml.toString();
	}
	
	/**
	 * Add element of set
	 * @param name
	 * @param value
	 */
	public void addElement(String name, String value) {
		this.set.add(new ChartElements(name,value));
	}
	
	/**
	 * @return the set
	 */
	public List<ChartElements> getSet() {
		return set;
	}

	/**
	 * @param set the set to set
	 */
	public void setSet(List<ChartElements> set) {
		this.set = set;
	}
	
	/**
	 * @return the yAxisMinValue
	 */
	public String getYAxisMinValue() {
		return yAxisMinValue;
	}
	/**
	 * @param yAxisMinValue the yAxisMinValue to set
	 */
	public void setYAxisMinValue(String yAxisMinValue) {
		this.yAxisMinValue = yAxisMinValue;
	}
	/**
	 * @return the yAxisMaxValue
	 */
	public String getYAxisMaxValue() {
		return yAxisMaxValue;
	}
	/**
	 * @param yAxisMaxValue the yAxisMaxValue to set
	 */
	public void setYAxisMaxValue(String yAxisMaxValue) {
		this.yAxisMaxValue = yAxisMaxValue;
	}
	/**
	 * @return the areaBgColor
	 */
	public String getAreaBgColor() {
		return areaBgColor;
	}
	/**
	 * @param areaBgColor the areaBgColor to set
	 */
	public void setAreaBgColor(String areaBgColor) {
		this.areaBgColor = areaBgColor;
	}
	/**
	 * @return the areaBorderColor
	 */
	public String getAreaBorderColor() {
		return areaBorderColor;
	}
	/**
	 * @param areaBorderColor the areaBorderColor to set
	 */
	public void setAreaBorderColor(String areaBorderColor) {
		this.areaBorderColor = areaBorderColor;
	}
	/**
	 * @return the showValues
	 */
	public String getShowValues() {
		return showValues;
	}
	/**
	 * @param showValues the showValues to set
	 */
	public void setShowValues(String showValues) {
		this.showValues = showValues;
	}
	/**
	 * @return the decimalPrecision
	 */
	public String getDecimalPrecision() {
		return decimalPrecision;
	}
	/**
	 * @param decimalPrecision the decimalPrecision to set
	 */
	public void setDecimalPrecision(String decimalPrecision) {
		this.decimalPrecision = decimalPrecision;
	}
	
}
