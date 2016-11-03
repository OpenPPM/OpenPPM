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
 * File: ActivityReport.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.reports.beans;

/**
 * Created by jaume.sastre on 18/11/2014.
 */
public class ActivityReport {

    private String activityName;
    private String planInitDate;
    private String actualInitDate;
    private String planEndDate;
    private String actualEndDate;
    private String activityPoc;

    public ActivityReport(String activityName, String planInitDate, String actualInitDate,
                                               String planEndDate, String actualEndDate, String activityPoc) {
        this.setActivityName(activityName);
        this.setPlanInitDate(planInitDate);
        this.setActualInitDate(actualInitDate);
        this.setPlanEndDate(planEndDate);
        this.setActualEndDate(actualEndDate);
        this.setActivityPoc(activityPoc);
    }

    /**
     * @return the activityName
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * @param activityName the activityName to set
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    /**
     * @return the planInitDate
     */
    public String getPlanInitDate() {
        return planInitDate;
    }

    /**
     * @param planInitDate the planInitDate to set
     */
    public void setPlanInitDate(String planInitDate) {
        this.planInitDate = planInitDate;
    }

    /**
     * @return the actualInitDate
     */
    public String getActualInitDate() {
        return actualInitDate;
    }

    /**
     * @param actualInitDate the actualInitDate to set
     */
    public void setActualInitDate(String actualInitDate) {
        this.actualInitDate = actualInitDate;
    }

    /**
     * @return the planEndDate
     */
    public String getPlanEndDate() {
        return planEndDate;
    }

    /**
     * @param planEndDate the planEndDate to set
     */
    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }

    /**
     * @return the actualEndDate
     */
    public String getActualEndDate() {
        return actualEndDate;
    }

    /**
     * @param actualEndDate the actualEndDate to set
     */
    public void setActualEndDate(String actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    /**
     * @return the activityPoc
     */
    public String getActivityPoc() {
        return activityPoc;
    }

    /**
     * @param activityPoc the activityPoc to set
     */
    public void setActivityPoc(String activityPoc) {
        this.activityPoc = activityPoc;
    }


}
