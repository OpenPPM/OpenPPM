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
 * File: Timesheet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.model.base.BaseTimesheet;


/**
 * Model class Timesheet.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Timesheet extends BaseTimesheet {

	private static final long serialVersionUID = 1L;

    public Timesheet() {
		super();
    }
    public Timesheet(Integer idTimeSheet) {
		super(idTimeSheet);
    }

	/**
	 * Indicate if is operation time for manage by RM
	 *
	 * @return
	 */
	public boolean isOperationForManager() {
		return getOperation() != null && getOperation().getAvailableForManager() != null && getOperation().getAvailableForManager();
	}

	/**
	 * Indicate if is operation time for approve by RM
	 *
	 * @return
	 */
	public boolean isOperationForApprove() {
		return getOperation() != null && getOperation().getAvailableForApprove() != null && getOperation().getAvailableForApprove();
	}

	/**
	 * Indicate if time is imputed in activity
	 *
	 * @return
	 */
	public boolean isActivityTime() {
		return getOperation() == null;
	}

    /**
     * Sum hours
     * @return
     */
	public Double sumHours() {
		
		Double hours = 0.0;
		
		if (this.getHoursDay1() != null) {
			hours += this.getHoursDay1();
		}
		
		if (this.getHoursDay2() != null) {
			hours += this.getHoursDay2();
		}
		
		if (this.getHoursDay3() != null) {
			hours += this.getHoursDay3();
		}
		
		if (this.getHoursDay4() != null) {
			hours += this.getHoursDay4();
		}
		
		if (this.getHoursDay5() != null) {
			hours += this.getHoursDay5();
		}
		
		if (this.getHoursDay6() != null) {
			hours += this.getHoursDay6();
		}
		
		if (this.getHoursDay7() != null) {
			hours += this.getHoursDay7();
		}
		
		return hours;
	}
}
