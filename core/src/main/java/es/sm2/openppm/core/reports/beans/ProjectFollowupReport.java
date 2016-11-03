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
 * File: ProjectFollowupReport.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.reports.beans;

/**
 * Created by jaume.sastre on 20/11/2014.
 */
public class ProjectFollowupReport {

    private String riskRating;
    private String generalFlag;
    private String riskFlag;
    private String costFlag;
    private String scheduleFlag;
    private String generalComments;
    private String riskComments;
    private String costComments;
    private String scheduleComments;

    public ProjectFollowupReport (String riskRating, String generalFlag, String riskFlag, String costFlag,
                                  String scheduleFlag, String generalComments, String riskComments,
                                  String costComments, String scheduleComments){

        this.riskRating = riskRating;
        this.generalFlag = generalFlag;
        this.riskFlag = riskFlag;
        this.costFlag = costFlag;
        this.scheduleFlag = scheduleFlag;
        this.generalComments = generalComments;
        this.riskComments = riskComments;
        this.costComments = costComments;
        this.scheduleComments = scheduleComments;

    }

    /**
     * @return the riskRating
     */
    public String getRiskRating() {
        return riskRating;
    }

    /**
     * @param riskRating the riskRating to set
     */
    public void setRiskRating(String riskRating) {
        this.riskRating = riskRating;
    }

    /**
     * @return the generalFlag
     */
    public String getGeneralFlag() {
        return generalFlag;
    }

    /**
     * @param generalFlag the generalFlag to set
     */
    public void setGeneralFlag(String generalFlag) {
        this.riskRating = generalFlag;
    }

    /**
     * @return the riskFlag
     */
    public String getRiskFlag() {
        return riskFlag;
    }

    /**
     * @param riskFlag the riskFlag to set
     */
    public void setRiskFlag(String riskFlag) {
        this.riskFlag = riskFlag;
    }

    /**
     * @return the costFlag
     */
    public String getCostFlag() {
        return costFlag;
    }

    /**
     * @param costFlag the costFlag to set
     */
    public void setCostFlag(String costFlag) {
        this.costFlag = costFlag;
    }

    /**
     * @return the scheduleFlag
     */
    public String getScheduleFlag() {
        return scheduleFlag;
    }

    /**
     * @param scheduleFlag the scheduleFlag to set
     */
    public void setScheduleFlag(String scheduleFlag) {
        this.scheduleFlag = scheduleFlag;
    }

    /**
     * @return the generalComments
     */
    public String getGeneralComments() {
        return generalComments;
    }

    /**
     * @param generalComments the generalComments to set
     */
    public void setGeneralComments(String generalComments) {
        this.generalComments = generalComments;
    }

    /**
     * @return the riskComments
     */
    public String getRiskComments() {
        return riskComments;
    }

    /**
     * @param riskComments the riskComments to set
     */
    public void setRiskComments(String riskComments) {
        this.riskComments = riskComments;
    }

    /**
     * @return the costComments
     */
    public String getCostComments() {
        return costComments;
    }

    /**
     * @param costComments the costComments to set
     */
    public void setCostComments(String costComments) {
        this.costComments = costComments;
    }

    /**
     * @return the scheduleComments
     */
    public String getScheduleComments() {
        return scheduleComments;
    }

    /**
     * @param scheduleComments the scheduleComments to set
     */
    public void setScheduleComments(String scheduleComments) {
        this.scheduleComments = scheduleComments;
    }

}
