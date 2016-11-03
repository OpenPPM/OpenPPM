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
 * File: BaseProjectfollowup.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectfollowup;

import java.util.Date;

 /**
 * Base Pojo object for domain model class Projectfollowup
 * @see es.sm2.openppm.core.model.base.BaseProjectfollowup
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Projectfollowup
 */
public class BaseProjectfollowup  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "projectfollowup";
	
	public static final String IDPROJECTFOLLOWUP = "idProjectFollowup";
	public static final String PROJECT = "project";
	public static final String FOLLOWUPDATE = "followupDate";
	public static final String EV = "ev";
	public static final String PV = "pv";
	public static final String AC = "ac";
	public static final String GENERALCOMMENTS = "generalComments";
	public static final String RISKSCOMMENTS = "risksComments";
	public static final String COSTCOMMENTS = "costComments";
	public static final String SCHEDULECOMMENTS = "scheduleComments";
	public static final String GENERALFLAG = "generalFlag";
	public static final String RISKFLAG = "riskFlag";
	public static final String COSTFLAG = "costFlag";
	public static final String SCHEDULEFLAG = "scheduleFlag";
	public static final String PERFORMANCEDOC = "performanceDoc";
	public static final String RISKRATING = "riskRating";

     private Integer idProjectFollowup;
     private Project project;
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

    public BaseProjectfollowup() {
    }
    
    public BaseProjectfollowup(Integer idProjectFollowup) {
    	this.idProjectFollowup = idProjectFollowup;
    }
   
    public Integer getIdProjectFollowup() {
        return this.idProjectFollowup;
    }
    
    public void setIdProjectFollowup(Integer idProjectFollowup) {
        this.idProjectFollowup = idProjectFollowup;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
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


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Projectfollowup) )  { result = false; }
		 else if (other != null) {
		 	Projectfollowup castOther = (Projectfollowup) other;
			if (castOther.getIdProjectFollowup().equals(this.getIdProjectFollowup())) { result = true; }
         }
		 return result;
   }


}


