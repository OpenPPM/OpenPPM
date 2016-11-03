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
 * File: ChartDataSet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.charts;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javier.hernandez
 *
 */
public class ChartDataSet {
	protected String seriesName;
	protected String color;
	protected String showValues;
	protected String alpha;
	protected List<String> listValues = new ArrayList<String>();
	
	public ChartDataSet() {
		super();
	}
	
	/**
	 * @param seriesName
	 * @param color
	 * @param showValues
	 */
	public ChartDataSet(String seriesName, String color, String showValues) {
		super();
		this.seriesName = seriesName;
		this.color = color;
		this.showValues = showValues;
	}

	/**
	 * @param seriesName
	 * @param color
	 * @param showValues
	 * @param alpha
	 * @param listValues
	 */
	public ChartDataSet(String seriesName, String color, String showValues,
			String alpha, List<String> listValues) {
		super();
		this.seriesName = seriesName;
		this.color = color;
		this.showValues = showValues;
		this.alpha = alpha;
		this.listValues = listValues;
	}

	/**
	 * Add value to data set
	 * @param listValue
	 */
	public void addValue(String listValue) {
		this.listValues.add(listValue);
	}

	/**
	 * @return the seriesName
	 */
	public String getSeriesName() {
		return seriesName;
	}

	/**
	 * @param seriesName the seriesName to set
	 */
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
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
	 * @return the alpha
	 */
	public String getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha the alpha to set
	 */
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	/**
	 * @return the listValues
	 */
	public List<String> getListValues() {
		return listValues;
	}

	/**
	 * @param listValues the listValues to set
	 */
	public void setListValues(List<String> listValues) {
		this.listValues = listValues;
	}
}
