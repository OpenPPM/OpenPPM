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
 * Module: plugin-project-synchronize
 * File: FollowupSync.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 13:25:36
 */

package es.sm2.openppm.plugin.synchronize.beans;

import es.sm2.openppm.core.model.impl.Projectfollowup;

import java.util.Date;

public class FollowupSync {

     private Date followupDate;
     private Double ev;
     private Double pv;
     private Double ac;
     private String generalComments;
     private String risksComments;
     private String costComments;
     private String scheduleComments;
     private Character generalFlag;
     private Character riskFlag;
     private Character costFlag;
     private Character scheduleFlag;
     private String performanceDoc;
     private Integer riskRating;

    public FollowupSync(Projectfollowup followup) {
    	
    	setFollowupDate(followup.getFollowupDate());
    	setEv(followup.getEv());
    	setPv(followup.getPv());
    	setAc(followup.getAc());
    	setGeneralComments(followup.getGeneralComments());
    	setRisksComments(followup.getRisksComments());
    	setCostComments(followup.getCostComments());
    	setScheduleComments(followup.getScheduleComments());
    	setGeneralFlag(followup.getGeneralFlag());
    	setRiskFlag(followup.getRiskFlag());
    	setCostFlag(followup.getCostFlag());
    	setScheduleFlag(followup.getScheduleFlag());
    	setPerformanceDoc(followup.getPerformanceDoc());
    	setRiskRating(followup.getRiskRating());
    }
    
    public Date getFollowupDate() {
        return this.followupDate;
    }
    
    public void setFollowupDate(Date followupDate) {
        this.followupDate = followupDate;
    }
    public Double getEv() {
        return this.ev;
    }
    
    public void setEv(Double ev) {
        this.ev = ev;
    }
    public Double getPv() {
        return this.pv;
    }
    
    public void setPv(Double pv) {
        this.pv = pv;
    }
    public Double getAc() {
        return this.ac;
    }
    
    public void setAc(Double ac) {
        this.ac = ac;
    }
    public String getGeneralComments() {
        return this.generalComments;
    }
    
    public void setGeneralComments(String generalComments) {
        this.generalComments = generalComments;
    }
    public String getRisksComments() {
        return this.risksComments;
    }
    
    public void setRisksComments(String risksComments) {
        this.risksComments = risksComments;
    }
    public String getCostComments() {
        return this.costComments;
    }
    
    public void setCostComments(String costComments) {
        this.costComments = costComments;
    }
    public String getScheduleComments() {
        return this.scheduleComments;
    }
    
    public void setScheduleComments(String scheduleComments) {
        this.scheduleComments = scheduleComments;
    }
    public Character getGeneralFlag() {
        return this.generalFlag;
    }
    
    public void setGeneralFlag(Character generalFlag) {
        this.generalFlag = generalFlag;
    }
    public Character getRiskFlag() {
        return this.riskFlag;
    }
    
    public void setRiskFlag(Character riskFlag) {
        this.riskFlag = riskFlag;
    }
    public Character getCostFlag() {
        return this.costFlag;
    }
    
    public void setCostFlag(Character costFlag) {
        this.costFlag = costFlag;
    }
    public Character getScheduleFlag() {
        return this.scheduleFlag;
    }
    
    public void setScheduleFlag(Character scheduleFlag) {
        this.scheduleFlag = scheduleFlag;
    }
    public String getPerformanceDoc() {
        return this.performanceDoc;
    }
    
    public void setPerformanceDoc(String performanceDoc) {
        this.performanceDoc = performanceDoc;
    }
    public Integer getRiskRating() {
        return this.riskRating;
    }
    
    public void setRiskRating(Integer riskRating) {
        this.riskRating = riskRating;
    }

}


