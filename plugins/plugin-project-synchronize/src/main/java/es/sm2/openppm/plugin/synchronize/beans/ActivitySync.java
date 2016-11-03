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
 * File: ActivitySync.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 13:25:36
 */

package es.sm2.openppm.plugin.synchronize.beans;

import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Projectactivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivitySync {

     private String activityName;
     private String wbsdictionary;
     private Date planInitDate;
     private Date actualInitDate;
     private Date planEndDate;
     private Date actualEndDate;
     private Double ev;
     private Double pv;
     private Double ac;
     private Double poc;
     private String commentsPoc;
     private String commentsDates;
     private List<MilestonSync> milestones = new ArrayList<MilestonSync>();

    public ActivitySync(Projectactivity activity) {

    	setActivityName(activity.getActivityName());
    	setWbsdictionary(activity.getWbsdictionary());
    	setPlanInitDate(activity.getPlanInitDate());
    	setActualInitDate(activity.getActualInitDate());
    	setPlanEndDate(activity.getPlanEndDate());
    	setActualEndDate(activity.getActualEndDate());
    	setEv(activity.getEv());
    	setPv(activity.getPv());
    	setAc(activity.getAc());
    	setPoc(activity.getPoc());
    	setCommentsPoc(activity.getCommentsPoc());
    	setCommentsDates(activity.getCommentsDates());

    	// SET MILESTONES
    	if (activity.getMilestoneses() != null && !activity.getMilestoneses().isEmpty()) {

    		for (Milestones mileston : activity.getMilestoneses()) {
    			getMilestones().add(new MilestonSync(mileston));
    		}
    	}
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
	 * @return the wbsdictionary
	 */
	public String getWbsdictionary() {
		return wbsdictionary;
	}

	/**
	 * @param wbsdictionary the wbsdictionary to set
	 */
	public void setWbsdictionary(String wbsdictionary) {
		this.wbsdictionary = wbsdictionary;
	}

	/**
	 * @return the planInitDate
	 */
	public Date getPlanInitDate() {
		return planInitDate;
	}

	/**
	 * @param planInitDate the planInitDate to set
	 */
	public void setPlanInitDate(Date planInitDate) {
		this.planInitDate = planInitDate;
	}

	/**
	 * @return the actualInitDate
	 */
	public Date getActualInitDate() {
		return actualInitDate;
	}

	/**
	 * @param actualInitDate the actualInitDate to set
	 */
	public void setActualInitDate(Date actualInitDate) {
		this.actualInitDate = actualInitDate;
	}

	/**
	 * @return the planEndDate
	 */
	public Date getPlanEndDate() {
		return planEndDate;
	}

	/**
	 * @param planEndDate the planEndDate to set
	 */
	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	/**
	 * @return the actualEndDate
	 */
	public Date getActualEndDate() {
		return actualEndDate;
	}

	/**
	 * @param actualEndDate the actualEndDate to set
	 */
	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	/**
	 * @return the ev
	 */
	public Double getEv() {
		return ev;
	}

	/**
	 * @param ev the ev to set
	 */
	public void setEv(Double ev) {
		this.ev = ev;
	}

	/**
	 * @return the pv
	 */
	public Double getPv() {
		return pv;
	}

	/**
	 * @param pv the pv to set
	 */
	public void setPv(Double pv) {
		this.pv = pv;
	}

	/**
	 * @return the ac
	 */
	public Double getAc() {
		return ac;
	}

	/**
	 * @param ac the ac to set
	 */
	public void setAc(Double ac) {
		this.ac = ac;
	}

	/**
	 * @return the poc
	 */
	public Double getPoc() {
		return poc;
	}

	/**
	 * @param poc the poc to set
	 */
	public void setPoc(Double poc) {
		this.poc = poc;
	}

	/**
	 * @return the commentsPoc
	 */
	public String getCommentsPoc() {
		return commentsPoc;
	}

	/**
	 * @param commentsPoc the commentsPoc to set
	 */
	public void setCommentsPoc(String commentsPoc) {
		this.commentsPoc = commentsPoc;
	}

	/**
	 * @return the commentsDates
	 */
	public String getCommentsDates() {
		return commentsDates;
	}

	/**
	 * @param commentsDates the commentsDates to set
	 */
	public void setCommentsDates(String commentsDates) {
		this.commentsDates = commentsDates;
	}

	/**
	 * @return the milestones
	 */
	public List<MilestonSync> getMilestones() {
		return milestones;
	}

	/**
	 * @param milestones the milestones to set
	 */
	public void setMilestones(List<MilestonSync> milestones) {
		this.milestones = milestones;
	}
}


