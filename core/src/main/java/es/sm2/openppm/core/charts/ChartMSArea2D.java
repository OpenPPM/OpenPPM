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
 * File: ChartMSArea2D.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.charts;

import java.util.ArrayList;
import java.util.List;

public class ChartMSArea2D {

	private String divlinecolor;
	private String numdivlines;
	private String showAreaBorder;
	private String areaBorderColor;
	private String decimalPrecision;
	private String yAxisMaxValue;
	private String trendValue;
	private String trendColor;
	private String trendDisplayvalue;
	private String yAxisName;
	private List<String> categories = new ArrayList<String>();
	private List<ChartMSArea2DDataset> datasets = new ArrayList<ChartMSArea2DDataset>();
	
	
	public ChartMSArea2D(String yAxisName) {
		this.yAxisName = yAxisName;
		this.divlinecolor = "FFFFFF";
		this.numdivlines = "4";
		this.showAreaBorder = "1";
		this.areaBorderColor = "000000";
		this.decimalPrecision = "0";
		this.trendColor = "000000";
		this.trendDisplayvalue = "";
		
	}
			
	
	/**
	 * @param divlinecolor
	 * @param numdivlines
	 * @param showAreaBorder
	 * @param areaBorderColor
	 * @param decimalPrecision
	 * @param yAxisMaxValue
	 * @param categories
	 * @param dataset
	 */
	public ChartMSArea2D(String divlinecolor, String numdivlines,
			String showAreaBorder, String areaBorderColor,
			String decimalPrecision, String yAxisMaxValue,
			List<String> categories, List<ChartMSArea2DDataset> dataset) {
		super();
		this.divlinecolor = divlinecolor;
		this.numdivlines = numdivlines;
		this.showAreaBorder = showAreaBorder;
		this.areaBorderColor = areaBorderColor;
		this.decimalPrecision = decimalPrecision;
		this.yAxisMaxValue = yAxisMaxValue;
		this.categories = categories;
		this.datasets = dataset;
	}
	
	
	public String generateXML() {
		StringBuilder xml = new StringBuilder();
		
		xml.append("<graph formatNumber='1' formatNumberScale='1' divlinecolor='" + divlinecolor + "' numdivlines='" + numdivlines + 
				"' showAreaBorder='" + showAreaBorder + "' areaBorderColor='" + areaBorderColor + 
				"' decimalPrecision='" + decimalPrecision + "' yAxisName='" + yAxisName + "' " +
				"chartLeftMargin='10' chartRightMargin='15' ");
		
		if (yAxisMaxValue != null) { xml.append("yAxisMaxValue='" + yAxisMaxValue + "'"); }
		
		xml.append("><categories>");
		for (String name : categories) {
			xml.append("<category name='" + name + "' /> ");
		}
		xml.append("</categories>");
		
		for (ChartMSArea2DDataset dataset : datasets) {
			
			xml.append("<dataset seriesname='" + dataset.getSeriesName()+ "' color='" + dataset.getColor() + 
					"' showValues='" + dataset.getShowValues()+ "' ");
			if (dataset.getShowAreaBorder() != null) { xml.append("showAreaBorder='" + dataset.getShowAreaBorder()+ "' "); }
			if (dataset.getAreaBorderColor() != null) { xml.append("areaBorderColor='" + dataset.getAreaBorderColor() + "' "); }
			xml.append(">");
			
			for (String value : dataset.getListValues()) {
				xml.append("<set value='" + value + "' /> ");
			}
			
			xml.append("</dataset>");
		}

		if (trendValue != null) {
			xml.append("<trendLines>" +
				"<line startValue='"+ trendValue +"' endValue='"+ trendValue +"' color='" + trendColor +
				"' displayvalue='" + trendDisplayvalue + "' thickness='2' alpha='50' isTrendZone='0' showOnTop='1'/>" +
				"</trendLines>");
		}
		xml.append("</graph> ");
		return xml.toString();
	}
	
	/**
	 * @return the xAxisName
	 */
	public String getyAxisName() {
		return yAxisName;
	}


	/**
	 * @param yAxisName the xAxisName to set
	 */
	public void setyAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}
	
	/**
	 * Add Data Set
	 * @param dataset
	 */
	public void addDatasets(ChartMSArea2DDataset dataset) {
		this.datasets.add(dataset);
	}
	
	/**
	 * @return the datasets
	 */
	public List<ChartMSArea2DDataset> getDatasets() {
		return datasets;
	}


	/**
	 * @param datasets the datasets to set
	 */
	public void setDatasets(List<ChartMSArea2DDataset> datasets) {
		this.datasets = datasets;
	}


	/**
	 * Add category
	 * @param name
	 */
	public void addCategory(String name) {
		this.categories.add(name);
	}
	/**
	 * @return the divlinecolor
	 */
	public String getDivlinecolor() {
		return divlinecolor;
	}
	/**
	 * @param divlinecolor the divlinecolor to set
	 */
	public void setDivlinecolor(String divlinecolor) {
		this.divlinecolor = divlinecolor;
	}
	/**
	 * @return the numdivlines
	 */
	public String getNumdivlines() {
		return numdivlines;
	}
	/**
	 * @param numdivlines the numdivlines to set
	 */
	public void setNumdivlines(String numdivlines) {
		this.numdivlines = numdivlines;
	}
	/**
	 * @return the showAreaBorder
	 */
	public String getShowAreaBorder() {
		return showAreaBorder;
	}
	/**
	 * @param showAreaBorder the showAreaBorder to set
	 */
	public void setShowAreaBorder(String showAreaBorder) {
		this.showAreaBorder = showAreaBorder;
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
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}
	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	/**
	 * @return the dataset
	 */
	public List<ChartMSArea2DDataset> getDataset() {
		return datasets;
	}
	/**
	 * @param dataset the dataset to set
	 */
	public void setDataset(List<ChartMSArea2DDataset> datasets) {
		this.datasets = datasets;
	}


	public void setTrendValue(String trendValue) {
		this.trendValue = trendValue;
	}


	public String getTrendValue() {
		return trendValue;
	}


	public void setTrendColor(String trendColor) {
		this.trendColor = trendColor;
	}


	public String getTrendColor() {
		return trendColor;
	}


	public void setTrendDisplayvalue(String trendDisplayvalue) {
		this.trendDisplayvalue = trendDisplayvalue;
	}


	public String getTrendDisplayvalue() {
		return trendDisplayvalue;
	}
	
}
