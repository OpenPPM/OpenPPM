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
 * File: Week.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.javabean;

import java.util.Calendar;

public class Week {

	Calendar sinceDay;
	
	Calendar untilDay;
	
	public Week(Calendar sinceDay, Calendar untilDay) {
		this.sinceDay = sinceDay;
		this.untilDay = untilDay;
	}
	
	/**
	 * @return the sinceDay
	 */
	public Calendar getSinceDay() {
		return sinceDay;
	}

	/**
	 * @param sinceDay the sinceDay to set
	 */
	public void setSinceDay(Calendar sinceDay) {
		this.sinceDay = sinceDay;
	}

	/**
	 * @return the untilDay
	 */
	public Calendar getUntilDay() {
		return untilDay;
	}

	/**
	 * @param untilDay the untilDay to set
	 */
	public void setUntilDay(Calendar untilDay) {
		this.untilDay = untilDay;
	}
}
