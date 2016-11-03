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
 * Module: front
 * File: OpenppmUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.ProjectcalendarLogic;
import es.sm2.openppm.core.logic.impl.TeamMemberLogic;
import es.sm2.openppm.core.logic.setting.VisibilityInvestmentSetting;
import es.sm2.openppm.core.logic.setting.VisibilityProjectSetting;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class OpenppmUtil {

	public final static Logger LOGGER = Logger.getLogger(OpenppmUtil.class);
	
	private OpenppmUtil() {}
	
	public static int numOfCulumnsForProject(HttpServletRequest req, HashMap<String, String> settings) {
		
		int columns = 0;
		
		if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)                                                                                ) { columns++; }

        if (SettingUtil.getBoolean(settings, VisibilityProjectSetting.PROJECT_COLUMN_IDPROJECT)							                            ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_RAG, Settings.DEFAULT_PROJECT_COLUMN_RAG)							) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_STATUS, Settings.DEFAULT_PROJECT_COLUMN_STATUS)					) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_ACCOUNTING_CODE, Settings.DEFAULT_PROJECT_COLUMN_ACCOUNTING_CODE)	) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_NAME, Settings.DEFAULT_PROJECT_COLUMN_NAME)						) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_SHORT_NAME, Settings.DEFAULT_PROJECT_COLUMN_SHORT_NAME)			) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_BUDGET, Settings.DEFAULT_PROJECT_COLUMN_BUDGET)					) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_PRIORITY, Settings.DEFAULT_PROJECT_COLUMN_PRIORITY)				) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_POC, Settings.DEFAULT_PROJECT_COLUMN_POC)							) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_BASELINE_START, Settings.DEFAULT_PROJECT_COLUMN_BASELINE_START)	) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_BASELINE_FINISH, Settings.DEFAULT_PROJECT_COLUMN_BASELINE_FINISH)	) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_PLANNED_START, Settings.DEFAULT_PROJECT_COLUMN_PLANNED_START)		) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_PLANNED_FINISH, Settings.DEFAULT_PROJECT_COLUMN_PLANNED_FINISH)	) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_START, Settings.DEFAULT_PROJECT_COLUMN_START)						) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_FINISH, Settings.DEFAULT_PROJECT_COLUMN_FINISH)					) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_ACTUAL_COST, Settings.DEFAULT_PROJECT_COLUMN_ACTUAL_COST)			) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_INTERNAL_EFFORT, Settings.DEFAULT_PROJECT_COLUMN_INTERNAL_EFFORT)	) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_EXTERNAL_COST, Settings.DEFAULT_PROJECT_COLUMN_EXTERNAL_COST)		) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_KPI, Settings.DEFAULT_PROJECT_COLUMN_KPI)							) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_BAC, Settings.DEFAULT_PROJECT_COLUMN_BAC)							) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_PROJECT_COLUMN_PM, Settings.DEFAULT_PROJECT_COLUMN_PM)							) { columns++; }
        if (SettingUtil.getBoolean(settings, VisibilityProjectSetting.PROJECT_COLUMN_CATEGORY)							                        ) { columns++; }
        if (SettingUtil.getBoolean(settings, VisibilityProjectSetting.PROJECT_COLUMN_CLASSIFICATION_LEVEL)							            ) { columns++; }
        if (SettingUtil.getBoolean(settings, VisibilityProjectSetting.PROJECT_COLUMN_DURATION)							                        ) { columns++; }
        if (SettingUtil.getBoolean(settings, VisibilityProjectSetting.PROJECT_COLUMN_STAGEGATE)							                        ) { columns++; }
        if (SettingUtil.getBoolean(settings, VisibilityProjectSetting.PROJECT_COLUMN_PROGRAM)							                        ) { columns++; }

		return columns;
	}
	
	public static int numOfCulumnsForInvestment(HttpServletRequest req, HashMap<String, String> settings) {
		
		int columns = 0;
		
		if (SecurityUtil.isUserInRole(req, Constants.ROLE_PORFM)                                                                                        ) { columns++; }

        if (SettingUtil.getBoolean(settings, VisibilityProjectSetting.PROJECT_COLUMN_IDPROJECT)							                                    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_STATUS, Settings.DEFAULT_INVESTMENT_COLUMN_STATUS)					    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_ACCOUNTING_CODE, Settings.DEFAULT_INVESTMENT_COLUMN_ACCOUNTING_CODE)	) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_NAME, Settings.DEFAULT_INVESTMENT_COLUMN_NAME)						    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_SHORT_NAME, Settings.DEFAULT_INVESTMENT_COLUMN_SHORT_NAME)			    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_BUDGET, Settings.DEFAULT_INVESTMENT_COLUMN_BUDGET)					    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_PRIORITY, Settings.DEFAULT_INVESTMENT_COLUMN_PRIORITY)				    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_POC, Settings.DEFAULT_INVESTMENT_COLUMN_POC)							) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_BASELINE_START, Settings.DEFAULT_INVESTMENT_COLUMN_BASELINE_START)	    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_BASELINE_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_BASELINE_FINISH)	) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_PLANNED_START, Settings.DEFAULT_INVESTMENT_COLUMN_PLANNED_START)	    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_PLANNED_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_PLANNED_FINISH)	    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_START, Settings.DEFAULT_INVESTMENT_COLUMN_START)						) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_FINISH, Settings.DEFAULT_INVESTMENT_COLUMN_FINISH)					    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_INTERNAL_EFFORT, Settings.DEFAULT_INVESTMENT_COLUMN_INTERNAL_EFFORT)	) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_EXTERNAL_COST, Settings.DEFAULT_INVESTMENT_COLUMN_EXTERNAL_COST)		) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_BAC, Settings.DEFAULT_INVESTMENT_COLUMN_BAC)		                    ) { columns++; }
		if (SettingUtil.getBoolean(settings, Settings.SETTING_INVESTMENT_COLUMN_PROGRAM, Settings.DEFAULT_INVESTMENT_COLUMN_PROGRAM)		            ) { columns++; }
        if (SettingUtil.getBoolean(settings, VisibilityInvestmentSetting.INVESTMENT_COLUMN_DURATION)							                        ) { columns++; }
        if (SettingUtil.getBoolean(settings, VisibilityInvestmentSetting.INVESTMENT_COLUMN_STAGEGATE)							                        ) { columns++; }
		
		return columns;
	}
	
	/**
	 * Check if is Exception day in project
	 * @param initDate
	 * @param dayOfWeek
	 * @param idActivity
	 * @return
	 */
	public static boolean isExceptionDay(Date initDate, Integer dayOfWeek, Integer idActivity, Integer idEmployee) {
		
		ProjectcalendarLogic projectcalendarLogic = new ProjectcalendarLogic();
		
		boolean isException = false;
		try {
			
			isException = projectcalendarLogic.isException(initDate, dayOfWeek, idActivity, idEmployee);
		}
		catch (Exception e) { ExceptionUtil.sendToLogger(LOGGER, e, null); }
		
		return isException;
	}
	
	/**
	 * Check if employee is assigned in these day on the project activity
	 * @param initDate
	 * @param dayOfWeek
	 * @param idActivity
	 * @param employee
	 * @return
	 */
	public static boolean isWorkDay(Date initDate, Integer dayOfWeek, Integer idActivity, Employee employee) {

		TeamMemberLogic memberLogic = new TeamMemberLogic();
		
		boolean isWorkDay = false;
		try {
			
			isWorkDay = memberLogic.isWorkDay(initDate, dayOfWeek, idActivity, employee);
		}
		catch (Exception e) { ExceptionUtil.sendToLogger(LOGGER, e, null); }
		
		return isWorkDay;
	}
	
	
	/**
	 * Calculate ES
	 * @param list
	 * @param followup
	 * @return
	 * @throws Exception 
	 */
	public static Double getES(List<Projectfollowup> list, Projectfollowup followup) throws Exception {
		
		Integer minWorkdays = null;
		Double minPv 		= null;
		
		Projectfollowup f0 	= null;
		Projectfollowup f1 	= null;
		
		Double ev 			= followup.getEv();
		Double es 			= null;
		
		if (ev != null) {
				
			// Get Followup for T0, PV0 and Min Days to date
			for (Projectfollowup item : list) {
				
				if (item.getPv() != null) {
					
					if (minPv == null || (item.getPv() >= minPv && item.getPv() <= ev)) {
						
						minPv 	= item.getPv();
						f0 		= item;
					}
					
					// Min Days to Date
					Integer daysToDate 	= item.getDaysToDate();
					daysToDate 			= (daysToDate == null ? 0 : daysToDate);
					
					if (minWorkdays == null || minWorkdays > daysToDate) {
						minWorkdays = daysToDate;
					}
				}
			}
			
			// Get followup for T1 PV1
			for (Projectfollowup item : list) {
				
				if (item.getPv()!= null && 
						((f1 == null && f0.getPv() < item.getPv()) ||
						(f1 != null && f0.getPv() < item.getPv() && f1.getPv() > item.getPv()))) {
					
					f1 = item;
				}
			}
			
			if (minPv != null && minPv > ev) { 
				es = (minWorkdays == null ? null : minWorkdays.doubleValue()); 
			}
			else {
				
				double t0 	= (f0 != null && f0.getDaysToDate() != null ? f0.getDaysToDate() : 0);
				double t1 	= (f1 != null && f1.getDaysToDate() != null ? f1.getDaysToDate() : 0);
				
				double pv0 	= (f0 != null ? f0.getPv() : 0);
				double pv1	= (f1 != null ? f1.getPv() : 0);
				
				if ((pv1-pv0) != 0) {
					es = t0+(t1-t0)*(ev-pv0)/(pv1-pv0);
				}
			}
		}
		return es;
	}
	
	
	/**
	 * Calculate ES
	 * @param list
	 * @param followup
	 * @return
	 * @throws Exception 
	 */
	public static Double getES(List<Projectfollowup> list, Projectfollowup followup, Project project, Session session) throws Exception {
		
		Integer minWorkdays = null;
		Double minPv = null;
		
		Projectfollowup f0 = null;
		Projectfollowup f1 = null;
		
		Double ev = followup.getEv();
		Double es = null;
		
		if (ev != null) {
				
			// Get Followup fot T0, PV0 and Min Days to date
			for (Projectfollowup item : list) {
				if (item.getPv()!= null) {
					if (minPv == null || (item.getPv() >= minPv && item.getPv() <= ev)) {
						minPv = item.getPv();
						f0 = item;
					}
					
					// Min Days to Date
					Integer daysToDate = item.getDaysToDate(project, session);
					daysToDate = (daysToDate == null?0:daysToDate);
					
					if (minWorkdays == null || minWorkdays > daysToDate) {
						minWorkdays = daysToDate;
					}
				}
			}
			
			// Get followup for T1 PV1
			for (Projectfollowup item : list) {
				if (item.getPv()!= null && 
						((f1 == null && f0.getPv() < item.getPv()) ||
						(f1 != null && f0.getPv() < item.getPv() && f1.getPv() > item.getPv()))) {
					f1 = item;
				}
			}
			
			if (minPv != null && minPv > ev) { es = (minWorkdays == null?null:minWorkdays.doubleValue()); }
			else {
				double t0 = (f0 != null && f0.getDaysToDate(project, session) != null?f0.getDaysToDate(project, session):0);
				double t1 = (f1 != null && f1.getDaysToDate(project, session) != null?f1.getDaysToDate(project, session):0);
				
				double pv0 = (f0 != null?f0.getPv():0);
				double pv1 = (f1 != null?f1.getPv():0);
				
				if ((pv1-pv0) != 0) {
					es = t0+(t1-t0)*(ev-pv0)/(pv1-pv0);
				}
			}
		}
		return es;
	}
}
