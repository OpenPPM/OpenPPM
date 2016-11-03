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
 * File: CapacityPlanningLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.javabean.CapacityPlanningDetail;
import es.sm2.openppm.core.javabean.TeamMembersFTEs;
import es.sm2.openppm.core.javabean.WeekFte;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;

public class CapacityPlanningLogic {

	/**
	 * Capacity planning detail
	 * 
	 * @param idsEmployees
	 * @param since
	 * @param until
	 * @param statusList
	 * @param settings
	 * @param user
     * @return
	 * @throws Exception 
	 */
	public List<TeamMembersFTEs> capacityPlanningDetail(Integer[] idsEmployees, Date since, Date until,
                                                        String[] statusList, HashMap<String, String> settings,
                                                        Employee user) throws Exception {
		
		List<TeamMembersFTEs> ftEs = null;
		
		if (ValidateUtil.isNotNull(idsEmployees)) {
			
			// Declare logics
			TeamMemberLogic memberLogic = new TeamMemberLogic();
			
			// Adjust dates
			//
			Calendar sinceCal		= DateUtil.getCalendar();
			Calendar untilCal		= DateUtil.getCalendar();
			
			if (since == null) {
				since = DateUtil.getFirstMonthDay(new Date());
			}
			
			if (until == null ) {
				
				untilCal.add(Calendar.MONTH, +1);
				
				until = DateUtil.getLastMonthDay(untilCal.getTime());
			}
			
			sinceCal.setTime(DateUtil.getFirstWeekDay(since));
			untilCal.setTime(DateUtil.getLastWeekDay(until));
			
			// Teammembers by employees 
			//
			List<Teammember> teammembers = new ArrayList<Teammember>();
			
			List<String> joins = new ArrayList<String>();
			joins.add(Teammember.EMPLOYEE+"."+Employee.CONTACT);
			
			for (Integer idEmployee : idsEmployees) {
				// Logic
				teammembers.addAll(memberLogic.capacityPlanningResource(idEmployee, since, until, statusList, joins, settings, user));
			}
			
			// Parse
			ftEs = memberLogic.generateFTEsMembersDetail(teammembers, sinceCal.getTime(), untilCal.getTime());
		}
		
		return ftEs;
	}

	/**
	 * Parse TeamMembersFTEs to CapacityPlanningDetail
	 * 
	 * @param ftEs
	 * @return
	 */
	public List<CapacityPlanningDetail> parse(List<TeamMembersFTEs> ftEs) {
		
		List<CapacityPlanningDetail> capacityPlanningDetails = null;
		
		if (ValidateUtil.isNotNull(ftEs)) {
				
			Hashtable<String, CapacityPlanningDetail> projectsByActivity = new Hashtable<String, CapacityPlanningDetail>();
			
			for (TeamMembersFTEs memberFte : ftEs) {
				
				CapacityPlanningDetail capacityPlanningDetail = new CapacityPlanningDetail();
				
				String key = StringPool.BLANK;
				
				// Set project
				if (memberFte.getProject() != null && ValidateUtil.isNotNull(memberFte.getProject().getProjectName())) {
					
					capacityPlanningDetail.setProjectName(memberFte.getProject().getProjectName());
					
					key += memberFte.getProject().getProjectName() + StringPool.SEMICOLON;
				}
				
				// Set activity
				if (memberFte.getMember() != null && memberFte.getMember().getProjectactivity() != null && ValidateUtil.isNotNull(memberFte.getMember().getProjectactivity().getActivityName())) {
					
					capacityPlanningDetail.setActivityName(memberFte.getMember().getProjectactivity().getActivityName());
					
					key += memberFte.getMember().getProjectactivity().getActivityName();
				}
				
				// Set ftes
				//
				if (memberFte.getFtes() != null) {
					
					// Update ftes
					if (projectsByActivity.containsKey(key)) {
						
						CapacityPlanningDetail tempCapacityPlanningDetail = projectsByActivity.get(key);
						
						if (tempCapacityPlanningDetail != null) {
							
							List<WeekFte> weeks 	= new ArrayList<WeekFte>();
							List<WeekFte> tempWeeks = tempCapacityPlanningDetail.getWeeks();
							
							int i = 0;
							for (Integer fte : memberFte.getFtes()) {
								
								WeekFte weekFte = tempWeeks.get(i);
								
								if (memberFte.getMember() != null && Constants.RESOURCE_ASSIGNED.equals(memberFte.getMember().getStatus())) {
									weekFte.setFteAssigned(fte);
								}
								else if (memberFte.getMember() != null && Constants.RESOURCE_PRE_ASSIGNED.equals(memberFte.getMember().getStatus())) {
									weekFte.setFtePreAssigned(fte);
								}
								
								weeks.add(weekFte);
								
								i++;
							}
							
							tempCapacityPlanningDetail.setWeeks(weeks);
							
							// Save in hash
							projectsByActivity.put(key, tempCapacityPlanningDetail);
						}
					}
					// New ftes
					else {
						
						List<WeekFte> weeks = new ArrayList<WeekFte>();
						
						for (Integer fte : memberFte.getFtes()) {
							
							WeekFte weekFte = new WeekFte(null, null);
							
							if (memberFte.getMember() != null && Constants.RESOURCE_ASSIGNED.equals(memberFte.getMember().getStatus())) {
								weekFte.setFteAssigned(fte);
							}
							else if (memberFte.getMember() != null && Constants.RESOURCE_PRE_ASSIGNED.equals(memberFte.getMember().getStatus())) {
								weekFte.setFtePreAssigned(fte);
							}
							
							weeks.add(weekFte);
						}
						
						capacityPlanningDetail.setWeeks(weeks);
						
						// Save in hash
						projectsByActivity.put(key, capacityPlanningDetail);
					}
				}
			}
			
			// Sort by project and activities
			//
			if (ValidateUtil.isNotNull(projectsByActivity.keySet())) {
				
				List<String> tempProjectsByActivity = new ArrayList<String>(projectsByActivity.keySet());
				
				Collections.sort(tempProjectsByActivity);
				
				capacityPlanningDetails = new ArrayList<CapacityPlanningDetail>();
				
				for (String projectByActivity : tempProjectsByActivity) {
					capacityPlanningDetails.add(projectsByActivity.get(projectByActivity));
				}
			}
		}
		
		return capacityPlanningDetails;
	}

}
