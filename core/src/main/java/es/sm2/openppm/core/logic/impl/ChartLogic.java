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
 * File: ChartLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.charts.ChartArea2D;
import es.sm2.openppm.core.charts.PlChartInfo;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings.SettingType;
import es.sm2.openppm.core.dao.DirectCostsDAO;
import es.sm2.openppm.core.dao.ExpensesDAO;
import es.sm2.openppm.core.dao.IwosDAO;
import es.sm2.openppm.core.dao.MilestoneDAO;
import es.sm2.openppm.core.dao.ProjectActivityDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.dao.ProjectFollowupDAO;
import es.sm2.openppm.core.dao.RiskRegisterDAO;
import es.sm2.openppm.core.exceptions.DateNotFoundException;
import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.exceptions.ProjectNetIncomeEmptyException;
import es.sm2.openppm.core.exceptions.ProjectNotFoundException;
import es.sm2.openppm.core.exceptions.ProjectTcvEmptyException;
import es.sm2.openppm.core.utils.JSONModelUtil;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.GenericLogic;

public final class ChartLogic extends GenericLogic {
	
	private ChartLogic() {
		super();
	}

	/**
	 * Collects information to generate Chart Gantt
	 *
	 * @param project
	 * @param showStatusDate
	 * @param until
	 * @param since
	 * @param hashMap
	 * @param directionSorting
	 * @param propertySort
	 * @return
	 * @throws Exception
	 */
	public static JSONObject consChartGantt(ResourceBundle idioma, Project project,boolean showStatusDate,
			boolean showPlanned, Date since, Date until, String idsBySorting, HashMap<String, String> settings) throws Exception {
		
		if (project.getIdProject() == -1) {
			throw new ProjectNotFoundException();
		}
		else if (since == null || until == null) {
			throw new DateNotFoundException();
		}
		
		JSONObject returnJSON = new JSONObject();
		JSONArray tasksJSON = new JSONArray();

		String numTasks				= null;
		Set<Milestones> milestones	= null;
		
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", idioma.getLocale());
		
		int numTask		= 0;
		double days;
		double resdays;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectActivityDAO activityDAO 				= new ProjectActivityDAO(session);
			List<Projectactivity> projectactivities 	= new ArrayList<Projectactivity>();
			List<Projectactivity> tempProjectactivities = new ArrayList<Projectactivity>();
			
			projectactivities.add(activityDAO.consRootActivity(project));
			
			Integer[] idsBySortingList = StringUtil.splitStrToIntegers(idsBySorting, null);
			
			if (idsBySortingList != null) {
				for (Integer id : idsBySortingList) {
					projectactivities.add(activityDAO.findById(id));
				}
			}
			
			// Filter by since and until
			Date planInitDate 	= null;
			Date planEndDate 	= null;
			
			for (Projectactivity projectactivity : projectactivities) {
				
				planInitDate 	= projectactivity.getPlanInitDate();
				planEndDate 	= projectactivity.getPlanEndDate();
				
				if ((planInitDate != null && planEndDate != null) && 
					((planInitDate.compareTo(since) >= 0 && planInitDate.compareTo(until) <= 0)  ||
					(planEndDate.compareTo(since) >= 0 && planEndDate.compareTo(until) <= 0) ||
					(planInitDate.compareTo(since) <= 0 && planEndDate.compareTo(until) >= 0))) {
					
					tempProjectactivities.add(projectactivity);
				}
			}
			
			projectactivities = tempProjectactivities;
			
			Calendar sinceCal = DateUtil.getCalendar();
			sinceCal.setTime(since);
			sinceCal.set(Calendar.DAY_OF_MONTH,1);
			since = sinceCal.getTime();
			
			Calendar untilCal = DateUtil.getCalendar();
			untilCal.setTime(until);
			untilCal.set(Calendar.DAY_OF_MONTH,untilCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			until = untilCal.getTime();
			
			for (int i = 0; i < projectactivities.size(); i++) {
				
				Projectactivity task = projectactivities.get(i);
				
				Date planInit	= null;
				Date planEnd	= null;
				Date actInit	= null;
				Date actEnd		= null;
				
				// Root
				if (i == 0) {
					
					// Dates planned calculated to root activity
					//
					if (SettingUtil.getBoolean(settings, SettingType.BASELINE_DATES)) {
						
						planInit	= project.getPlannedInitDate();
						planEnd		= project.getPlannedFinishDate();
					}
					else {
						
						planInit	= project.getCalculatedPlanStartDate();
						planEnd		= project.getCalculatedPlanFinishDate();
					}
					
					actInit 	= project.getStartDate();
					actEnd		= project.getFinishDate();
				}
				else {
					planInit	= task.getPlanInitDate();
					planEnd		= task.getPlanEndDate();
					actInit 	= (task.getActualInitDate() == null?task.getPlanInitDate():task.getActualInitDate());
					actEnd 		= (task.getActualEndDate() == null?task.getPlanEndDate():task.getActualEndDate());
				}
				
				JSONArray seriesJSON = new JSONArray();
				JSONObject taskJSON = new JSONObject();
				milestones = task.getMilestoneses();
				
				if (actInit != null && actEnd != null && showPlanned) {
					
					Date sinceInit 	= (actInit.before(since)?since:actInit);
					Date untilEnd 	= (until.before(actEnd)?until:actEnd);
					
					// Add actual series
					JSONObject actualJSON = new JSONObject();
					actualJSON.put("name", idioma.getString("cost.actual"));
					actualJSON.put("start", df.format(sinceInit));
					actualJSON.put("end", df.format(untilEnd));
					actualJSON.put("color", "#4567AA");
					
					// Add milestones for actual
					//
					JSONArray milestonesActualJSON = new JSONArray();

					for (Milestones itemMilestone : milestones) {

						if (itemMilestone.getAchieved() != null && DateUtil.between(since, until, itemMilestone.getAchieved())) {

							// Add milestone real
							JSONObject milestoneJSON= new JSONObject();
							milestoneJSON.put("name", itemMilestone.getName());
							milestoneJSON.put("date", df.format(itemMilestone.getAchieved()));
							milestoneJSON.put("type", "real");
							milestonesActualJSON.add(milestoneJSON);
						}

						if (itemMilestone.getEstimatedDate() != null && DateUtil.between(since, until, itemMilestone.getEstimatedDate())
								&& ((itemMilestone.getAchieved() != null && !DateUtil.equals(itemMilestone.getEstimatedDate(), itemMilestone.getAchieved()))
										|| itemMilestone.getAchieved() == null
										)) {

							// Add milestone estimated
							JSONObject milestoneJSON= new JSONObject();
							milestoneJSON.put("name", itemMilestone.getName());
							milestoneJSON.put("date", df.format(itemMilestone.getEstimatedDate()));
							milestoneJSON.put("type", "estimated");
							milestonesActualJSON.add(milestoneJSON);
						}
					}

					actualJSON.put("milestones",milestonesActualJSON);
					
					Double poc = null;
					if (task.getWbsnode().getWbsnode() == null) {
						poc = activityDAO.calcPoc(task, settings);
						if (poc != null) { poc = poc*100; }
					}
					else {
						poc = task.getPoc();
					}
					if (poc != null && poc > 0 && showPlanned) {
						
						Calendar initDate = DateUtil.getCalendar();
						initDate.setTime(actInit);
						
						Calendar endDate = DateUtil.getCalendar();
						endDate.setTime(actEnd);
						
						days = endDate.getTimeInMillis() - initDate.getTimeInMillis();
						resdays = days / (1000 * 60 * 60 * 24);
						
						resdays = resdays * (poc / 100);
						
						endDate.setTime(actInit);
						endDate.add(Calendar.DATE, (int)resdays);
						
						Date completeStart 	= (actInit.before(since)?since:actInit);
						Date completeEnd 	= (until.before(endDate.getTime())?until:endDate.getTime());
						
						if (DateUtil.between(actInit, actEnd, endDate.getTime())) {
							// Add percentage complete for actual
							JSONObject completeJSON = new JSONObject();
							completeJSON.put("name", idioma.getString("complete"));
							completeJSON.put("start", df.format(completeStart));
							completeJSON.put("end", df.format(completeEnd));
							completeJSON.put("poc", ValidateUtil.toPercent(poc/100));

							completeJSON.put("color","black");
							
							actualJSON.put("complete",completeJSON);
						}
					}
					seriesJSON.add(actualJSON);
				}
				
				if (planInit != null && planEnd != null) {
					Date sinceInit = (planInit.before(since)?since:planInit);
					Date untilEnd = (until.before(planEnd)?until:planEnd);
					
					numTask++;
					numTasks = Integer.toString(numTask);
					
					// Add information
					taskJSON.put("id", numTasks);
					taskJSON.put("name", task.getActivityName());
				
					// Add planned series
					JSONObject plannedJSON = new JSONObject();
					plannedJSON.put("name", idioma.getString("cost.planned"));
					plannedJSON.put("start", df.format(sinceInit));
					plannedJSON.put("end", df.format(untilEnd));
					plannedJSON.put("color", "#A9A9A9");
					
					// Milestones for planned
					//
					JSONArray milestonesPlannedJSON = new JSONArray();
					
					for (Milestones itemMilestones : milestones) {
						
						if (DateUtil.between(since, until, itemMilestones.getPlanned())) {
						
							// Add milestone planned
							JSONObject milestoneJSON= new JSONObject();
							milestoneJSON.put("name", itemMilestones.getName());
							milestoneJSON.put("date", df.format(itemMilestones.getPlanned()));
							milestoneJSON.put("type", "planned");
							milestonesPlannedJSON.add(milestoneJSON);
						}
					}
					
					plannedJSON.put("milestones", milestonesPlannedJSON);
					
					seriesJSON.add(plannedJSON);
				}
				
				taskJSON.put("series", seriesJSON);
				tasksJSON.add(taskJSON);
			}
			
			if (showStatusDate
					&& project.getStatusDate() != null
					&& DateUtil.between(since, until, project.getStatusDate())) {
				
				returnJSON.put("controlLine", df.format(project.getStatusDate()));
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		returnJSON.put("tasks", tasksJSON);
		
		return returnJSON;
	}

    /**
     * Chart Gantt
     *
     * @param idioma
     * @param listProjects
     * @param since
     * @param until
     * @param showMilestones
     * @param settings
     * @return
     * @throws Exception
     */
	public static JSONObject consChartGantt(ResourceBundle idioma,
                                            List<Project> listProjects, Date since, Date until,
                                            boolean showMilestones, HashMap<String, String> settings) throws Exception {
		
		if (listProjects == null || listProjects.isEmpty()) {
			throw new ProjectNotFoundException();
		}
		
		JSONObject returnJSON 	= new JSONObject();
		JSONArray tasksJSON 	= new JSONArray();
		
		int numTask				= 0;
		
		Transaction tx 	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			MilestoneDAO milestoneDAO		= new MilestoneDAO(session);
			ProjectDAO projectDAO			= new ProjectDAO(session);
			
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", idioma.getLocale());
			
			//interval of dates for chart
			Calendar initDate	= null;
			Calendar endDate	= null;
			Calendar tempCal	= DateUtil.getCalendar();

			if (since == null || until == null) {
				
				for (Project proj :listProjects) {

                    Date start 	= proj.getStartDate();
                    Date finish  = proj.getFinishDate();

                    Date dateStartBySetting;
                    Date dateFinishBySetting;

                    if (SettingUtil.getBoolean(settings, SettingType.BASELINE_DATES)) {

                        dateStartBySetting	= proj.getPlannedInitDate();
                        dateFinishBySetting	= proj.getPlannedFinishDate();
                    }
                    else {

                        dateStartBySetting	= proj.getCalculatedPlanStartDate() == null ? proj.getPlannedInitDate() : proj.getCalculatedPlanStartDate();
                        dateFinishBySetting	= proj.getCalculatedPlanFinishDate() == null ? proj.getPlannedFinishDate() : proj.getCalculatedPlanFinishDate();
                    }

					if (start != null && start.before(dateStartBySetting)) {
						tempCal.setTime(start);
					}
					else {
						tempCal.setTime(dateStartBySetting);
					}
					
					if (initDate == null || initDate.after(tempCal)) {
						
						if (initDate == null) { initDate = DateUtil.getCalendar(); }
						initDate.setTime(tempCal.getTime());
					}
					
					if (finish != null && finish.after(dateFinishBySetting)) {
						tempCal.setTime(finish);
					}
					else {
						tempCal.setTime(dateFinishBySetting);
					}
					
					if (endDate == null || endDate.before(tempCal)) {
						
						if (endDate == null) { endDate = DateUtil.getCalendar(); }
						endDate.setTime(tempCal.getTime());
					}
				}
			}
			
			if (since != null) {
				initDate = DateUtil.getCalendar();
				initDate.setTime(since);
				initDate.set(Calendar.DAY_OF_MONTH,1);
				since = initDate.getTime();
			}
			else {
				since = initDate.getTime();
			}
			if (until != null) {	
				endDate = DateUtil.getCalendar();
				endDate.setTime(until);
				endDate.set(Calendar.DAY_OF_MONTH,endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
				until = endDate.getTime();
			}
			else {
				until = endDate.getTime();
			}
			
			if (endDate != null && initDate != null) {

				for (Project proj : listProjects) {
					
					Date start 	= proj.getStartDate();
					Date finish = proj.getFinishDate();

                    Date dateStartBySetting;
                    Date dateFinishBySetting;

                    if (SettingUtil.getBoolean(settings, SettingType.BASELINE_DATES)) {

                        dateStartBySetting	= proj.getPlannedInitDate();
                        dateFinishBySetting	= proj.getPlannedFinishDate();
                    }
                    else {

                        dateStartBySetting	= proj.getCalculatedPlanStartDate() == null ? proj.getPlannedInitDate() : proj.getCalculatedPlanStartDate();
                        dateFinishBySetting	= proj.getCalculatedPlanFinishDate() == null ? proj.getPlannedFinishDate() : proj.getCalculatedPlanFinishDate();
                    }

					if ((since.before(dateStartBySetting) && until.after(dateStartBySetting))
							|| (since.before(dateFinishBySetting) && until.after(dateFinishBySetting))
							|| (since.after(dateStartBySetting) && until.before(dateFinishBySetting))
							|| DateUtil.equals(since, dateStartBySetting)
							|| DateUtil.equals(until, dateFinishBySetting)) {
						
						numTask++;
						
						Date startPlan = since.before(dateStartBySetting)?dateStartBySetting:since;
						Date finishPlan = until.after(dateFinishBySetting)?dateFinishBySetting:until;
						
						JSONArray seriesJSON = new JSONArray();
						List<Milestones> milestones = milestoneDAO.findByProject(proj);

                        double poc = proj.getPoc() == null?0:proj.getPoc();

						// Add information
						JSONObject taskJSON = new JSONObject();
						taskJSON.put("id", numTask);
						taskJSON.put("name",  proj.getProjectName());
						taskJSON.put("poc",  ValidateUtil.toPercent(poc));

						if (!Constants.INVESTMENT_IN_PROCESS.equals(proj.getInvestmentStatus())
								&& start != null && finish != null) {
							
							Date startAct = since.before(start)?start:since;
							Date finishAct = until.after(finish)?finish:until;
							
							// Add actual series
							JSONObject actualJSON = new JSONObject();
							actualJSON.put("name", idioma.getString("cost.actual"));
							actualJSON.put("start", df.format(startAct));
							actualJSON.put("end", df.format(finishAct));
							
							// Project is dependent of other project
							if (projectDAO.hasParent(proj)) {
								
								actualJSON.put("color", "#FF8F35");
							}
							else {
								
								actualJSON.put("color", "#4567AA");
							}
							
							// Add milestones for actual
							JSONArray milestonesActualJSON = new JSONArray();

                            // Show milestones only if is active in view
                            if (showMilestones) {
                                for (Milestones milestone : milestones) {

                                    if ("Y".equals(milestone.getReportType().toString().toUpperCase())) {

                                        if (milestone.getAchieved() != null
                                                && DateUtil.between(startAct, finishAct, milestone.getAchieved())) {

                                            // Add milestone real
                                            JSONObject milestoneJSON = new JSONObject();
                                            milestoneJSON.put("name", milestone.getName());
                                            milestoneJSON.put("date", df.format(milestone.getAchieved()));
                                            milestoneJSON.put("type", "real");
                                            milestonesActualJSON.add(milestoneJSON);
                                        }

                                        if (milestone.getEstimatedDate() != null && DateUtil.between(startAct, finishAct, milestone.getEstimatedDate())
                                                && ((milestone.getAchieved() != null && !DateUtil.equals(milestone.getEstimatedDate(), milestone.getAchieved()))
                                                || milestone.getAchieved() == null
                                        )) {

                                            // Add milestone estimated
                                            JSONObject milestoneJSON = new JSONObject();
                                            milestoneJSON.put("name", milestone.getName());
                                            milestoneJSON.put("date", df.format(milestone.getEstimatedDate()));
                                            milestoneJSON.put("type", "estimated");
                                            milestonesActualJSON.add(milestoneJSON);
                                        }
                                    }
                                }
                            }
							
							actualJSON.put("milestones",milestonesActualJSON);
							
							if (poc > 0 && start != null && finish != null) {
								
								Calendar initDateTemp = DateUtil.getCalendar();
								initDateTemp.setTime(start);
								
								Calendar endDateTemp = DateUtil.getCalendar();
								endDateTemp.setTime(finish);
								
								long days = endDateTemp.getTimeInMillis() - initDateTemp.getTimeInMillis();
								double resdays = days / (1000 * 60 * 60 * 24);
								poc = Math.round(poc*Math.pow(10,2))/Math.pow(10,2);
								
								resdays = resdays * poc;
								
								initDateTemp.add(Calendar.DATE, (int)resdays);
								
								Date finishActComplete = until.after(initDateTemp.getTime())?initDateTemp.getTime():until;
								
								if (initDateTemp.getTime().after(start)) {
									// Add percentage complete for actual
									JSONObject completeJSON = new JSONObject();
									completeJSON.put("name", idioma.getString("complete"));
									completeJSON.put("start", df.format(startAct));
									completeJSON.put("end", df.format(finishActComplete));
									completeJSON.put("poc", ValidateUtil.toPercent(proj.getPoc()));
									completeJSON.put("color","black");
									
									// Add status dates
									if (showMilestones && proj.getStatusDate() != null) {
										completeJSON.put("statusDate", df.format(proj.getStatusDate()));
									}
									
									actualJSON.put("complete",completeJSON);
								}
							}
							seriesJSON.add(actualJSON);
						}
						
						// Add planned series
						JSONObject plannedJSON = new JSONObject();
						plannedJSON.put("name", idioma.getString("cost.planned"));
						plannedJSON.put("start", df.format(startPlan));
						plannedJSON.put("end", df.format(finishPlan));
						
						// Project is dependent of other project
						if (Constants.INVESTMENT_IN_PROCESS.equals(proj.getInvestmentStatus())
								&& projectDAO.hasParent(proj)) {
							
							plannedJSON.put("color", "#FEC18F");
						}
						else {
							
							plannedJSON.put("color", "#A9A9A9");
						}
						
					
						// Add milestones for planned
						JSONArray milestonesPlannedJSON = new JSONArray();

                        // Show milestones only if is active in view
                        if (showMilestones) {
                            for (Milestones milestone : milestones) {

                                if ("Y".equals(milestone.getReportType().toString().toUpperCase())
                                        && DateUtil.between(startPlan, finishPlan, milestone.getPlanned())) {

                                    // Add milestone planned
                                    JSONObject milestoneJSON = new JSONObject();
                                    milestoneJSON.put("name", milestone.getName());
                                    milestoneJSON.put("date", df.format(milestone.getPlanned()));
                                    milestoneJSON.put("type", "planned");
                                    milestonesPlannedJSON.add(milestoneJSON);
                                }
                            }
                        }
						
						plannedJSON.put("milestones", milestonesPlannedJSON);
						
						seriesJSON.add(plannedJSON);
						
						taskJSON.put("series",seriesJSON);
						tasksJSON.add(taskJSON);
					}
				}
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		returnJSON.put("tasks", tasksJSON);
		
		return (numTask > 0 ? returnJSON : null);
	}

	
	/**
	 * Return info for generate chart PL
	 * @param idProject
	 * @return
	 * @throws Exception 
	 */
	public static PlChartInfo consInfoPlChart(Project project) throws Exception {
		
		PlChartInfo plChartInfo = new PlChartInfo(); 
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			if (project.getTcv() == null) {
				throw new ProjectTcvEmptyException();
			}
			
			IwosDAO iwosDAO = new IwosDAO(session);
			plChartInfo.setSumIwos(iwosDAO.getTotal(project));
			
			ExpensesDAO expensesDAO = new ExpensesDAO(session);
			plChartInfo.setSumExpenses(expensesDAO.getTotal(project));
			
			DirectCostsDAO directCostsDAO = new DirectCostsDAO(session);
			plChartInfo.setSumDirectCost(directCostsDAO.getTotal(project));

			plChartInfo.setTcv(project.getTcv());
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return plChartInfo;
	}
	
	
	/**
	 * Return actual info for generate chart PL to Control Costs
	 * @param idProject
	 * @return
	 * @throws Exception 
	 */
	public static PlChartInfo consActualInfoPlChart(Project project) throws Exception {
		
		PlChartInfo plChartInfo = new PlChartInfo(); 
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			if (project.getTcv() == null) {
				throw new ProjectTcvEmptyException();
			}
			if (project.getNetIncome() == null || project.getNetIncome() <= 0) {
				throw new ProjectNetIncomeEmptyException();
			}
			
			IwosDAO iwosDAO = new IwosDAO(session);
			plChartInfo.setSumIwos(iwosDAO.getActualTotal(project));
			
			ExpensesDAO expensesDAO = new ExpensesDAO(session);
			plChartInfo.setSumExpenses(expensesDAO.getActualTotal(project));
			
			DirectCostsDAO directCostsDAO = new DirectCostsDAO(session);
			plChartInfo.setSumDirectCost(directCostsDAO.getActualTotal(project));

			plChartInfo.setTcv(project.getTcv());
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return plChartInfo;
	}
	
	/**
	 * Create Chart Sales Forecast
	 * @param idioma 
	 * @return
	 * @throws Exception 
	 */
	public static ChartArea2D consChartSales(ResourceBundle idioma, Integer[] ids) throws Exception {
		
		ChartArea2D chart = null;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			Calendar initDate = DateUtil.getCalendar();
			initDate.setTime(DateUtil.getFirstMonthDay(initDate.getTime()));
			initDate.add(Calendar.MONTH, +1);
			
			Calendar endDate = DateUtil.getCalendar();
			endDate.add(Calendar.MONTH, +13);
			endDate.setTime(DateUtil.getLastMonthDay(endDate.getTime()));
			
			Calendar tempDate = DateUtil.getCalendar();
			tempDate.setTime(initDate.getTime());
			
			ProjectDAO projectDAO = new ProjectDAO(session);
			List<Project> investments = projectDAO.consInvestmentInProcess(ids);
			
			if (!investments.isEmpty()) {
				
				int[] listData = salesData(investments, initDate, endDate);
				
				chart = new ChartArea2D();
				int i = 0;
				
				while (tempDate.before(endDate)) {
					chart.addElement(idioma.getString("month.month" + (tempDate.get(Calendar.MONTH)+1)+"_short")
							, String.valueOf(listData[i]));
					i++;
					tempDate.add(Calendar.MONTH, + 1);
				}
			}
			tx.commit();
		}
		catch (NoDataFoundException e) {
			chart = null;
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return chart;
	}

	
	private static int[] salesData(List<Project> investments, Calendar initDate, Calendar endDate) throws NoDataFoundException {

		boolean noData = true;
		
		Calendar tempDate = DateUtil.getCalendar();
		tempDate.setTime(initDate.getTime());
		
		int[] listData = new int[13];
		int i = 0;
		while (tempDate.before(endDate)) {
			listData[i] = 0;
			
			for (Project investment : investments) {
				
				Date start = investment.getPlannedInitDate();
				Date finish = investment.getPlannedFinishDate();
				
				if (start != null) {
					
					if (!start.after(tempDate.getTime()) &&
							!tempDate.after(finish)) {
						
						double months = DateUtil.monthsBetween(start, finish);
						double tv = (investment.getTcv() == null ? 0 : investment.getTcv().doubleValue());
						double winProbability = (investment.getPriority() == null?0:investment.getPriority());
						double total;
						
						tv = (tv / 1000);
						winProbability = (winProbability / 100);
						total = ((tv * winProbability) / months);
						
						if (total > 0) { 
							noData = false;				
							listData[i] += total;
						}
					}
				}
			}
			
			i++;
			tempDate.add(Calendar.MONTH, + 1);
		}
		
		if (noData) { throw new NoDataFoundException(); }
		
		return listData;
	}
	
	
	/**
	 * Create JSON Array for show bubble chart
	 * @param idioma
	 * @param projList
	 * @param investiment
	 * @param axesXType 
	 * @param idRiskCategories 
	 * @param adjustmentApply 
	 * @return
	 * @throws Exception
	 */
	public static JSONArray createBubbleChart(ResourceBundle idioma, List<Project> projList, String areaType,
			String axesXType, boolean adjustmentApply, Integer[] idRiskCategories) throws Exception {
		
		JSONArray projectsJSON = new JSONArray();
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectFollowupDAO followupDAO	= new ProjectFollowupDAO(session);
			
			Double xAxis 				= new Double(0);
			Double yAxis 				= new Double(0);
			SimpleDateFormat date 		= new SimpleDateFormat("MM/dd/yyyy");
			String xValueDate 			= StringPool.BLANK;
			Double area 				= new Double(0);
			String color				= null;
			
			// Max value for area
			double maxValue = 0; 
			Double value = null;
			
			for (Project proj : projList) {
				
				if (Project.TCV.equals(areaType)) {
					value = (proj.getTcv() == null ? 0 : proj.getTcv());
				}
				else if (Project.BAC.equals(areaType)) {
					value = (proj.getBac() == null ? 0 : proj.getBac());
				}

				if (value != null && proj.getProbability() != null && maxValue < value) {
					maxValue = value;
				}
			}
			
			maxValue = (maxValue/1000);
			
			boolean activeALL = isActiveAll(idRiskCategories);
			
			for (Project proj : projList) {
				
					// X axe
					if ("poc".equals(axesXType)) {
						xAxis = (proj.getPoc() == null?0:(proj.getPoc()*100));
					}
					else if (Project.PROBABILITY.equals(axesXType)) {
						xAxis = proj.getProbability() != null ? proj.getProbability().doubleValue() : 0.0;
					}
					else if (Project.INITDATE.equals(axesXType) ) {
						
						Date start = proj.getStartDate();
						
						xValueDate = start != null ? date.format(start) : date.format(new Date()); 
					}
					else if (Project.ENDDATE.equals(axesXType)) {
						
						Date finish = proj.getFinishDate();
						
						xValueDate = finish != null ? date.format(finish) : date.format(new Date()); 
					}
					else if ("riskRating".equals(axesXType)) {
						
						Integer riskRating = null;
						
						if (adjustmentApply && proj.getUseRiskAdjust()) {
							riskRating = proj.getRiskRatingAdjustament() == null?0:proj.getRiskRatingAdjustament();
						}
						else {
							riskRating = calculateRiskRatingActual(session,
									proj, idRiskCategories,
									activeALL);
						}
						
						xAxis = riskRating.doubleValue();
					}
					else if (Project.TCV.equals(axesXType)) {
						xAxis = proj.getTcv() == null ? 0.0 : proj.getTcv()/1000.0;
					}
					else {
						throw new LogicException("Select X axe type");
					}
					
					
					// Y axe
					if ("riskRating".equals(axesXType) && adjustmentApply && proj.getUseStrategicAdjust()) {
						
						int strategicAdjustment = proj.getStrategicAdjustament() == null?0: proj.getStrategicAdjustament();
						
						yAxis = new Double(strategicAdjustment);
					}
					else {
						
						yAxis = (proj.getPriority() == null ? 0.0 : proj.getPriority().doubleValue());
					}
					
					
					// Area
					if (Project.TCV.equals(areaType)) {
						area = (proj.getTcv() == null ? 0.0 : proj.getTcv()/1000.0);
					}
					else if (Project.BAC.equals(areaType)) {
						area = (proj.getBac() == null ? 0.0 : proj.getBac()/1000.0);
					}
					else {
						area = 0.0;
					}

					if (maxValue > 0) { 
						
						area = (area * Constants.BUBBLE_MAX)/maxValue; 
						
						if (area < Constants.BUBBLE_MIN) { 
							area = Constants.BUBBLE_MIN; 
						}
					}
					else { 
						area = Constants.BUBBLE_DEFAULT; 
					}
					
					
					// Color
					if (Project.BAC.equals(areaType) || Project.TCV.equals(areaType)) {
						
						Projectfollowup followupRisk = followupDAO.findLastByProjectRisk(proj);
						
						Character risk = (followupRisk != null ? followupRisk.getRiskFlag() : null);
						
						if (risk == null) {
							color = "#E3E3E3"; // Gris
						}
						else if (risk.equals(Constants.RISK_LOW) || risk.equals(Constants.RISK_NORMAL)) { 
							color = "#99CC00"; // Verde
						}
						else if (risk.equals(Constants.RISK_MEDIUM)) { 
							color = "#FFCC00"; // Amarillo
						}
						else if (risk.equals(Constants.RISK_HIGH)) { 
							color = "#CC0000"; // Rojo
						}
					}
					else if (Project.INVESTMENTSTATUS.equals(areaType)) {
						
						if (Constants.INVESTMENT_APPROVED.equals(proj.getInvestmentStatus())) {
							color = "#99CC00"; // Verde
						}
						else if (Constants.INVESTMENT_INACTIVATED.equals(proj.getInvestmentStatus())) {
							color = "#CC0000"; // Rojo
						}
						else if (Constants.INVESTMENT_IN_PROCESS.equals(proj.getInvestmentStatus())) {
							color = "#E3E3E3"; // Gris
						}
					}
					else {
						color = "#E3E3E3"; // Gris
					}
					
					
					String shortNameProject	= ValidateUtil.isNullCh(proj.getChartLabel(), proj.getAccountingCode());
					
					if (xValueDate != StringPool.BLANK) {
						projectsJSON.add(JSONModelUtil.createBubbleProject(xValueDate, yAxis, area, shortNameProject, color, proj.getIdProject(), proj.getStatus(), proj.getProjectName()));
					}
					else {
						projectsJSON.add(JSONModelUtil.createBubbleProject(xAxis, yAxis, area, shortNameProject, color, proj.getIdProject(), proj.getStatus(), proj.getProjectName()));
					}
				}
			
			tx.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return projectsJSON;
	}

	/**
	 * Check is active all option
	 * 
	 * @param idRiskCategories
	 * @return
	 */
	private static boolean isActiveAll(Integer[] idRiskCategories) {
		
		boolean activeALL = false;
		
		if (ValidateUtil.isNotNull(idRiskCategories)) {
			
			int i = 0;
			while (!activeALL && i < idRiskCategories.length) {
				
				activeALL =(idRiskCategories[i++] == -1);
			}
		}
		return activeALL;
	}

	/**
	 * Calculate risk Rating
	 * 
	 * @param session
	 * @param proj
	 * @param idRiskCategories
	 * @param activeALL
	 * @return
	 */
	private static Integer calculateRiskRatingActual(Session session,
			Project proj, Integer[] idRiskCategories,
			boolean activeALL) {
		
		Integer riskRating 					= 0;
		
		// Declare DAO
		RiskRegisterDAO riskRegisterDAO 	= new RiskRegisterDAO(session);
		
		// DAO
		List<Riskregister> riskregisters = riskRegisterDAO.findByProjectAndOpen(proj);
		
		// Calculated total risk rating
		for (Riskregister riskregister : riskregisters) {

			if (ValidateUtil.isNotNull(idRiskCategories) && !activeALL) {
			
				for (Integer id : idRiskCategories) {
					
					if (riskregister.getRiskcategory() != null && riskregister.getRiskcategory().getIdRiskcategory().equals(id)) {
						riskRating += riskregister.getRiskRating();
					}
				}
			}
			else {
			
				riskRating += riskregister.getRiskRating();
			}
		}
		
		return riskRating;
	}	

	/**
	 * Calculate risk Rating
	 * 
	 * @param proj
	 * @param idRiskCategories
	 * @return
	 * @throws Exception 
	 */
	public static int calculateRiskRatingActual(Project proj,
			Integer[] idRiskCategories) throws Exception {
		
		int riskRating = 0;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			// Check active all
			boolean activeALL = isActiveAll(idRiskCategories);
			
			// Calculate risk
			riskRating = calculateRiskRatingActual(session, proj, idRiskCategories, activeALL);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		
		return riskRating;
	}
}