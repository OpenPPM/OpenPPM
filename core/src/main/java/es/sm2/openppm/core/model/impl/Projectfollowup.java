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
 * File: Projectfollowup.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import es.sm2.openppm.core.dao.ProjectCalendarExceptionsDAO;
import es.sm2.openppm.core.dao.ProjectcalendarDAO;
import es.sm2.openppm.core.logic.impl.ProjectCalendarExceptionsLogic;
import es.sm2.openppm.core.logic.impl.ProjectcalendarLogic;
import es.sm2.openppm.utils.DateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.ProjectActivityDAO;
import es.sm2.openppm.core.logic.impl.ProjectActivityLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.core.model.base.BaseProjectfollowup;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;



/**
 * Model class Projectfollowup.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Projectfollowup extends BaseProjectfollowup {

	private static final long serialVersionUID = 1L;
	private Integer daysToDate;
	private HashMap<String, String> settings;
	private Double budget;
	
	public Projectfollowup() {
		super();
    }
    public Projectfollowup(Integer idProjectFollowup) {
		super(idProjectFollowup);
    }


	public Integer getDaysToDate() throws Exception {
    	
    	if (daysToDate == null) {
    		
    		ProjectLogic projectLogic 			= new ProjectLogic(settings, null);
    		ProjectActivityLogic activityLogic 	= new ProjectActivityLogic(settings, null);
			ProjectCalendarExceptionsLogic projectCalendarExceptionsLogic = new ProjectCalendarExceptionsLogic();
			ProjectcalendarLogic projectCalendarLogic = new ProjectcalendarLogic();
    		
	    	Project project				= projectLogic.findByFollowup(this);
	    	Projectactivity rootAct 	= project.getRootActivity();

			Projectcalendar projectCalendar = projectCalendarLogic.consCalendarByProject(project);
			List<Projectcalendarexceptions> exceptions = projectCalendarExceptionsLogic.findByRelation(Projectcalendarexceptions.PROJECTCALENDAR,
																										projectCalendar);

			List<Date> exceptionDates = ProjectCalendarExceptionsLogic.getExceptionDates(exceptions);

	    	Junction restrictions = Restrictions.conjunction().add(Restrictions.eq(Projectactivity.PROJECT, project)).add(Restrictions.isNull(Projectactivity.ACTUALINITDATE));
	    	
	    	if (rootAct != null && rootAct.getActualInitDate() != null && rootAct.getPlanInitDate() != null
	    			&& activityLogic.rowCount(restrictions) == 0 && rootAct.getActualInitDate().before(rootAct.getPlanInitDate())) {
	    		
	    		daysToDate = ValidateUtil.calculateWorkDays(rootAct.getActualInitDate(), getFollowupDate(), exceptionDates);
	    	}
	    	else if (rootAct != null && rootAct.getPlanInitDate() != null) {
	    		daysToDate = ValidateUtil.calculateWorkDays(rootAct.getPlanInitDate(), getFollowupDate(), exceptionDates);
	    	}
	    	else {
	    		daysToDate = ValidateUtil.calculateWorkDays(project.getPlannedInitDate(), getFollowupDate(), exceptionDates);
	    	}
    	}
    	
    	return daysToDate;
    }

    public Integer getDaysToDate(Project project, Session session) throws Exception {
    	
    	if (daysToDate == null) {

            // Declare DAO
    		ProjectActivityDAO activityDAO  = new ProjectActivityDAO(session);
			ProjectcalendarDAO projectCalendarDAO = new ProjectcalendarDAO(session);
			ProjectCalendarExceptionsDAO projectCalendarExceptionsDAO = new ProjectCalendarExceptionsDAO(session);

            // DAO consult
	    	Projectactivity rootAct         = activityDAO.consRootActivity(project);

			Projectcalendar projectCalendar = projectCalendarDAO.findByProject(getProject());
			List<Projectcalendarexceptions> exceptions = projectCalendarExceptionsDAO.findByProjectCalendar(projectCalendar,
																										project.getInitDate(),
																										project.getFinishDate());
			List<Date> exceptionDates = ProjectCalendarExceptionsLogic.getExceptionDates(exceptions);

	    	if (rootAct != null && rootAct.getActualInitDate() != null) {
	    		daysToDate = ValidateUtil.calculateWorkDays(rootAct.getActualInitDate(), getFollowupDate(), exceptionDates);
	    	}
	    	else if (rootAct != null && rootAct.getPlanInitDate() != null) {
	    		daysToDate = ValidateUtil.calculateWorkDays(rootAct.getPlanInitDate(), getFollowupDate(), exceptionDates);
	    	}
	    	else {
	    		daysToDate = ValidateUtil.calculateWorkDays(project.getPlannedInitDate(), getFollowupDate(), exceptionDates);
	    	}
    	}
    	return daysToDate;
    }
    
    public Double getCv() {
    	Double cv = null;

		if (getAc() != null && getEv() != null) {
			cv = getEv() - getAc();
		}
		return cv;
	}
    
    public void setSettings(HashMap<String, String> settings) {
    	this.settings = settings; 
    }
    
    public void setBudget(Double budget) {
    	
    	this.budget = budget; 
    }
    
	public Double getEac() {
		
		Double eac = null;

		if (Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_USE_BAC_PLANNING, Settings.SETTING_USE_BAC_PLANNING_DEFAULT)) ){
			
			if (budget != null) {
				
				if ( getCpi() != null) {
					eac = (budget - (getEv()== null?0:getEv()))/getCpi() +(getAc() == null?0:getAc());
				}
			}
		}
		else {
			
			if (getProject() != null && getProject().getBac() != null) {
				
				if ( getCpi() != null) {
					eac = (getProject().getBac() - (getEv()== null?0:getEv()))/getCpi() +(getAc() == null?0:getAc());
				}
			}
		}
		
		return eac;
	}

	public Double getSpi() {
		Double spi = null;

		if (getEv() != null && getPv() != null && getPv() > 0) {
			spi = getEv() / getPv();
		}
		return spi;
	}

	public Double getSv() {
		Double sv = null;
		
		if (getEv() != null && getPv() != null) {
			sv = getEv() - getPv();
		}
		return sv;
	}
	
	public Double getCpi() {
		Double cpi = null;

		if (getAc() != null && getEv() != null && getAc() > 0) {
			cpi = getEv() / getAc();
		}
		return cpi;
	}
	
	public Double getPoc() {
		
		Double poc = null;

		if (Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_USE_BAC_PLANNING, Settings.SETTING_USE_BAC_PLANNING_DEFAULT)) ){
			
			if (budget != null && budget > 0 && getEv() != null) {
				poc = getEv()/budget;
			}
		}
		else {
			
			if (getProject() != null && getProject().getBac() != null && getProject().getBac() > 0 && getEv() != null) {
				poc = getEv()/ getProject().getBac();
			}
		}
		
		return poc;
	}
	
	public String getClassGeneral() {
		String styleClass;
		
		if (this.getGeneralFlag() != null && Constants.RISK_HIGH == this.getGeneralFlag().charValue()) {
			styleClass = "high_importance";
		}
		else if(this.getGeneralFlag() != null && Constants.RISK_MEDIUM == this.getGeneralFlag().charValue()){
			styleClass = "medium_importance";
		}
		else if(this.getGeneralFlag() != null && Constants.RISK_LOW == this.getGeneralFlag().charValue()){
			styleClass = "low_importance";
		}
		else if(this.getGeneralFlag() != null && Constants.RISK_NORMAL == this.getGeneralFlag().charValue()){
			styleClass = "normal_importance";
		}
		else{
			styleClass = StringPool.BLANK;
		}
		
		return styleClass;
	}
	
	public String getClassRisk() {
		String styleClass;
		
		if (this.getRiskFlag() != null && Constants.RISK_HIGH == this.getRiskFlag().charValue()) {
			styleClass = "high_importance";
		}
		else if(this.getRiskFlag() != null && Constants.RISK_MEDIUM == this.getRiskFlag().charValue()){
			styleClass = "medium_importance";
		}
		else if(this.getRiskFlag() != null && Constants.RISK_LOW == this.getRiskFlag().charValue()){
			styleClass = "low_importance";
		}
		else if(this.getRiskFlag() != null && Constants.RISK_NORMAL == this.getRiskFlag().charValue()){
			styleClass = "normal_importance";
		}
		else{
			styleClass = StringPool.BLANK;
		}
		
		return styleClass;
	}
	
	public String getClassSchedule() {
		String styleClass;
		
		if (this.getScheduleFlag() != null && Constants.RISK_HIGH == this.getScheduleFlag().charValue()) {
			styleClass = "high_importance";
		}
		else if(this.getScheduleFlag() != null && Constants.RISK_MEDIUM == this.getScheduleFlag().charValue()){
			styleClass = "medium_importance";
		}
		else if(this.getScheduleFlag() != null && Constants.RISK_LOW == this.getScheduleFlag().charValue()){
			styleClass = "low_importance";
		}
		else if(this.getScheduleFlag() != null && Constants.RISK_NORMAL == this.getScheduleFlag().charValue()){
			styleClass = "normal_importance";
		}
		else{
			styleClass = StringPool.BLANK;
		}
		
		return styleClass;
	}
	
	public String getClassCost() {
		String styleClass;
		
		if (this.getCostFlag() != null && Constants.RISK_HIGH == this.getCostFlag().charValue()) {
			styleClass = "high_importance";
		}
		else if(this.getCostFlag() != null && Constants.RISK_MEDIUM == this.getCostFlag().charValue()){
			styleClass = "medium_importance";
		}
		else if(this.getCostFlag() != null && Constants.RISK_LOW == this.getCostFlag().charValue()){
			styleClass = "low_importance";
		}
		else if(this.getCostFlag() != null && Constants.RISK_NORMAL == this.getCostFlag().charValue()){
			styleClass = "normal_importance";
		}
		else{
			styleClass = StringPool.BLANK;
		}
		
		return styleClass;
	}
	
}

