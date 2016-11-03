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
 * File: PeriodicityEnum.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.common.enums;

import java.util.Calendar;

/**
 * Created by jordi.ripoll on 31/10/2014.
 */
public enum PeriodicityEnum {
    EVERY_DAY("periodicity.every_day", Calendar.DAY_OF_MONTH),
    ONCE_A_WEEK("periodicity.once_a_week", Calendar.WEEK_OF_MONTH),
    START_MONTH("periodicity.start_month", Calendar.MONTH),
    HALF_MONTH("periodicity.half_month", Calendar.MONTH),
    END_MONTH("periodicity.finish_month", Calendar.MONTH);

    private String label;
    private int calendarField;

    private PeriodicityEnum(String label, int calendarField) {

        this.label          = label;
        this.calendarField  = calendarField;
    }

    public String getLabel() {
        return label;
    }

    public int getCalendarField() {
        return calendarField;
    }
}
