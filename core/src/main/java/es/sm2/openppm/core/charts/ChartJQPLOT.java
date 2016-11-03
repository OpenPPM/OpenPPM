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
 * File: ChartJQPLOT.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.charts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.utils.DateUtil;

/**
 * JQPLOT methods
 * @author jordi.ripoll
 *
 */
public class ChartJQPLOT {

	private Date minDate;
	private Date maxDate;
	private String tickInterval;
	
	public ChartJQPLOT () {
		
	}
	
	public ChartJQPLOT (Date minDate, Date maxDate) {
		this.setMinDate(minDate);
		this.setMaxDate(maxDate);
		this.setTickInterval(tickInterval(minDate, maxDate));
	}
	
	/**
	 * Tick interval for dates
	 * @param minDate
	 * @param maxDate
	 * @return
	 */
	public String tickInterval (Date minDate, Date maxDate) {
		int days 			= 0;
		String tickInterval = null;
		
		if (minDate != null && maxDate != null) {
			days = DateUtil.daysBetween(minDate, maxDate);
			
			if (days < 7) {
				tickInterval = Constants.ONE_DAY;
			}
			else if (days < 15) {
				tickInterval = Constants.TWO_DAY;
			}
			else if (days < 30) {
				tickInterval = Constants.ONE_WEEK;
			}
			else {
				tickInterval = Constants.ONE_MONTH;
			}
		}
		
		return tickInterval;
	}

	/**
	 * Add or subtract date
	 * @param tickInterval
	 * @param date
	 * @param add is -1 if subtract and add is 1 if add
	 * @return
	 */
	public String addDate (Date date, int add) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar result 			= DateUtil.getCalendar();
		
		if (date != null && this.getTickInterval() != null && (add == -1 || add == 1)) {
			result.setTime(date);
			
			if (Constants.ONE_DAY.equals(this.getTickInterval())) {
				result.add(Calendar.DAY_OF_MONTH, add);
			}
			else if (Constants.TWO_DAY.equals(this.getTickInterval())) {
				result.add(Calendar.DAY_OF_MONTH, add*2);
			}
			else if (Constants.ONE_WEEK.equals(this.getTickInterval())) {
				result.add(Calendar.DAY_OF_MONTH, add*7);
			}
			else if (Constants.ONE_MONTH.equals(this.getTickInterval())) {
				result.add(Calendar.DAY_OF_MONTH, add*30);
			}
			
			return dateFormat.format(result.getTime());
		}
		else {
			return null;
		}
	}

	public Date getMinDate() {
		return minDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public String getTickInterval() {
		return tickInterval;
	}

	public void setTickInterval(String tickInterval) {
		this.tickInterval = tickInterval;
	}
	
}
