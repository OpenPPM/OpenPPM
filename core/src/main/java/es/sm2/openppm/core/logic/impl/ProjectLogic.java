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
 * File: ProjectLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.common.enums.LocationAuditEnum;
import es.sm2.openppm.core.dao.*;
import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.exceptions.ProjectKpiWeightException;
import es.sm2.openppm.core.exceptions.ProjectNotFoundException;
import es.sm2.openppm.core.exceptions.ProposalNotWonException;
import es.sm2.openppm.core.model.impl.*;
import es.sm2.openppm.core.model.search.ProjectSearch;
import es.sm2.openppm.core.javabean.ProjectInfoJavaBean;
import es.sm2.openppm.core.logic.exceptions.SecurityException;
import es.sm2.openppm.core.logic.setting.AuditSetting;
import es.sm2.openppm.core.logic.setting.GeneralSetting;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.model.wrap.ProjectWrap;
import es.sm2.openppm.core.utils.DataUtil;
import es.sm2.openppm.core.utils.Order;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sun.util.calendar.Gregorian;

import java.util.*;

public class ProjectLogic extends AbstractGenericLogic<Project, Integer> {

	private static final Logger LOGGER = Logger.getLogger(ProjectLogic.class);

	private Map<String, String> settings;
	
	public ProjectLogic(Map<String, String> settings, ResourceBundle bundle) {
		super(bundle);
		this.settings = settings;
	}

	public ProjectLogic(HashMap<String, String> settings) {
		super();
		this.settings = settings;
	}
	
	/**
	 * Check if accounting code is used for other projects
	 * @param project
	 * @param user 
	 * @return
	 * @throws Exception
	 */
	public boolean accountingCodeInUse(Project project, Employee user) throws Exception {
		boolean used = false;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			CompanyDAO companyDAO = new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(user);
			
			used = projDAO.accountingCodeInUse(project, company);
			
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
		
		return used;
	}


    /**
     * Find projects where an Employee is team member in range of days
     *
     * @param employee
     * @param dayMonth
     * @return
     * @throws Exception
     */
	public List<Project> findByResourceInProject(Employee employee, Date dayMonth) throws Exception {
		List<Project> empProjects = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO = new ProjectDAO(session);
			empProjects = projectDAO.findByResourceInProject(employee, DateUtil.getFirstMonthDay(dayMonth), DateUtil.getLastMonthDay(dayMonth));
			
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
		
		return empProjects;
	}
	
	
	/**
	 * Return project
	 * @param idProject
	 * @return
	 * @throws Exception 
	 */
	public Project consProject (Integer idProject) throws Exception {
		if (idProject == -1) {
			throw new NoDataFoundException();
		}
		Project project = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			if (idProject != -1) {
				project = projDAO.findById(idProject, false);
			}
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
		return project;
	}


    /**
     * Java Bean of Project Info
     *
     * @param idProject
     * @param performingorg
     * @param company
     * @return
     * @throws Exception
     */
	public ProjectInfoJavaBean findById (int idProject, Performingorg performingorg, Company company) throws Exception {
		ProjectInfoJavaBean info = new ProjectInfoJavaBean();
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

            // Declare DAO
			ProjectDAO projDAO = new ProjectDAO(session);

            // DAO get project
			Project project = projDAO.consInitiatingProject(new Project(idProject));

            // Set project
			info.setProject(project);			
			
			ProjectCharterDAO projCharterDAO = new ProjectCharterDAO(session);
			Projectcharter projectcharter = projCharterDAO.findByProject(project);
			info.setProjectCharter(projectcharter);
			
			EmployeeDAO employeeDAO = new EmployeeDAO(session);
			Employee example = new Employee();
			example.setPerformingorg(project.getPerformingorg());
			info.setEmployees(employeeDAO.searchByExample(example));
			
			ProgramDAO programDAO = new ProgramDAO(session);
			info.setPrograms(programDAO.searchByPerfOrg(performingorg, null));
			
			ContractTypeDAO contractTypeDAO = new ContractTypeDAO(session);
			info.setContractTypes(contractTypeDAO.findByRelation(Contracttype.COMPANY, company, Contracttype.DESCRIPTION, Order.ASC));
			
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
		return info;
	}
	
	
	/**
	 * Create or Update project and it project Charter
	 * @param proj
	 * @param projCharter
	 * @param dependentsJSON 
	 * @param leadsJSON 
	 * @throws Exception 
	 */
	public List<String> saveInitiatingProject(Project proj, Projectcharter projCharter,
			JSONArray leadsJSON, JSONArray dependentsJSON, Date newPlannedInitDate, Employee user) throws Exception {
		
		if (proj == null) { throw new NoDataFoundException(); }
		
		List<String> newInfo = new ArrayList<String>();
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			
			tx = session.beginTransaction();

			newInfo = saveInitiatingProject(session, proj, projCharter, leadsJSON, dependentsJSON, newPlannedInitDate, user);

			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }

        // Create audit
        AuditLogic auditLogic = new AuditLogic();
        auditLogic.createAudit(LocationAuditEnum.PROJECT_DATA, proj, user, AuditSetting.PROJECT_DATA);

		return newInfo;
	}
	
	/**
	 * Save initiating project from another Logic
	 * 
	 * @param session
	 * @param proj
	 * @param projCharter
	 * @param leadsJSON
	 * @param dependentsJSON
	 * @param newPlannedInitDate
	 * @return
	 * @throws Exception
	 */
	public List<String> saveInitiatingProject(Session session, Project proj, Projectcharter projCharter,
			JSONArray leadsJSON, JSONArray dependentsJSON, Date newPlannedInitDate, Employee user) throws Exception {
	
		List<String> newInfo = new ArrayList<String>();
		
		Date oldPlannedInitDate = proj.getPlannedInitDate();
		proj.setPlannedInitDate(newPlannedInitDate);
		
		ProjectDAO projDAO				 = new ProjectDAO(session);
		WBSNodeDAO wbsNodeDAO 			 = new WBSNodeDAO(session);
		ProjectCharterDAO projCharterDAO = new ProjectCharterDAO(session);
		ProjectActivityDAO activityDAO	 = new ProjectActivityDAO(session);
		ProjectassociationDAO projectassociationDAO		= new ProjectassociationDAO(session);
		
		if (proj.getInitDate() == null) {
			proj.setInitDate(new Date());
		}
		
		// Update Root Node name
		Wbsnode rootNode = wbsNodeDAO.findFirstNode(proj);
		rootNode.setName(proj.getProjectName());
		wbsNodeDAO.makePersistent(rootNode);
		
		proj = projDAO.makePersistent(proj);
		
		// Change name of root activity
		Projectactivity rootActivity = activityDAO.consRootActivity(proj);
		rootActivity.setActivityName(proj.getProjectName());
		activityDAO.makePersistent(rootActivity);
		
		// Delete all associations
		for (Projectassociation association : proj.getProjectassociationsForDependent()) {
			projectassociationDAO.makeTransient(association);
		}
		for (Projectassociation association : proj.getProjectassociationsForLead()) {
			projectassociationDAO.makeTransient(association);
		}
		
		// Create Associations
		if (leadsJSON != null) {
			
			for (int i = 0;i < leadsJSON.size(); i++) {
				
				JSONObject item = leadsJSON.getJSONObject(i);
				
				Projectassociation association = new Projectassociation();
				association.setProjectByDependent(proj);
				association.setProjectByLead(new Project(item.getInt(Project.IDPROJECT)));
				association.setUpdateDates(item.getBoolean(Projectassociation.UPDATEDATES));
				
				projectassociationDAO.makePersistent(association);
			}
		}
		
		List<Project> parentProjects = new ArrayList<Project>();
		
		if (dependentsJSON != null) {
			
			for (int i = 0;i < dependentsJSON.size(); i++) {
				
				JSONObject item = dependentsJSON.getJSONObject(i);
				
				boolean updateDates = item.getBoolean(Projectassociation.UPDATEDATES);
				
				Project parentProject = null;
				
				// Add parent project for update dates
				if (updateDates) {
					
					parentProject = projDAO.findById(item.getInt(Project.IDPROJECT));
					parentProjects.add(parentProject);
				}
				else {
					parentProject = new Project(item.getInt(Project.IDPROJECT));
				}
				
				Projectassociation association = new Projectassociation();
				association.setProjectByDependent(parentProject);
				association.setProjectByLead(proj);
				association.setUpdateDates(updateDates);
				projectassociationDAO.makePersistent(association);
			}
		}
		
		// update dates
		if (Constants.STATUS_INITIATING.equals(proj.getStatus())) {
			
			// Update dates from parent projects
			if (ValidateUtil.isNotNull(parentProjects)) {
	
				
				Date endDate = null;
				
				// Finish date from parents projects
				for (Project parent : parentProjects) {
				
					Date parentEndDate = this.getFinishDate(parent, session);
					if (endDate == null || parentEndDate.after(endDate)) {
						endDate = parentEndDate;
					}
				}
				
				// Update date from parent projects
				if (endDate != null) {
					
					// Create new dates
					Calendar init = Calendar.getInstance();
					init.setTime(endDate);
					init.add(Calendar.DAY_OF_MONTH, 1);
					
					// Update dates of lead project
					int days = DateUtil.daysBetween(proj.getPlannedInitDate(), proj.getPlannedFinishDate());
					
					Calendar end = Calendar.getInstance();
					end.setTime(init.getTime());
					end.add(Calendar.DAY_OF_MONTH, days);
					
					proj.setPlannedInitDate(init.getTime());
					proj.setPlannedFinishDate(end.getTime());
					
					newPlannedInitDate = endDate;
					proj = projDAO.makePersistent(proj);
				}
			}
				
			updateDatesLeads(session, proj);
		}
		
		// Update dates
		newInfo.addAll(updateDates(session, proj, newPlannedInitDate, oldPlannedInitDate));
		
		if (projCharter != null) {
			projCharter.setProject(proj);
			
			projCharterDAO.makePersistent(projCharter);
		}
		
		// Save project
		projDAO.makePersistent(proj);

        return newInfo;
	}

	/**
	 * Update dates of project
	 * 
	 * @param session
	 * @param proj
	 * @param newPlannedInitDate
	 * @param oldPlannedInitDate
	 * @return
	 * @throws Exception 
	 */
	private List<String> updateDates(
			Session session,
			Project proj,
			Date newPlannedInitDate,
			Date oldPlannedInitDate) throws Exception {

        // Declare DAO
		ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);

		ProjectcalendarDAO projectCalendarDAO = new ProjectcalendarDAO(session);
		Projectcalendar projectCalendar = projectCalendarDAO.findByProject(proj);

		ProjectCalendarExceptionsDAO projectCalendarExceptionsDAO = new ProjectCalendarExceptionsDAO(session);
		List<Projectcalendarexceptions> exceptions = projectCalendarExceptionsDAO.findByRelation(Projectcalendarexceptions.PROJECTCALENDAR,
				projectCalendar);

		List<Date> exceptionDates = ProjectCalendarExceptionsLogic.getExceptionDates(exceptions);

        // Info message
		List<String> newInfo = new ArrayList<String>();

        if (SettingUtil.getBoolean(settings, GeneralSetting.SHIFT_DATES_WHEN_MODIFY_BASELINE) &&
                Constants.INVESTMENT_IN_PROCESS.equals(proj.getInvestmentStatus()) &&
                oldPlannedInitDate != null && newPlannedInitDate != null &&
                !DateUtil.equals(oldPlannedInitDate, newPlannedInitDate)) {

            ProjectFollowupDAO followupDAO	= new ProjectFollowupDAO(session);
            TeamMemberDAO memberDAO			= new TeamMemberDAO(session);
            MilestoneDAO milestoneDAO		= new MilestoneDAO(session);

            Calendar tempCal = DateUtil.getCalendar();

            Calendar oldCal = DateUtil.getCalendar();
            oldCal.setTime(oldPlannedInitDate);

            Calendar newCal = DateUtil.getCalendar();
            newCal.setTime(newPlannedInitDate);

            // New days
            Long days = (newCal.getTimeInMillis() - oldCal.getTimeInMillis())/Constants.MILLSECS_PER_DAY;

            boolean addInfo = false;

            // Update Activity dates
            List<Projectactivity> activities = activityDAO.findByProject(proj, null);

            for (Projectactivity activity : activities) {

                if (activity.getPlanInitDate() != null && activity.getPlanEndDate() != null) {

                    tempCal.setTime(activity.getPlanInitDate());

					tempCal.add(Calendar.DAY_OF_MONTH, days.intValue());

					if (tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

						if (days.intValue() > 0) {

							tempCal.add(Calendar.DAY_OF_WEEK, 2);
						}
						else {

							tempCal.add(Calendar.DAY_OF_WEEK, -1);
						}
					}
					else if (tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

						if (days.intValue() > 0) {

							tempCal.add(Calendar.DAY_OF_WEEK, 1);
						}
						else {

							tempCal.add(Calendar.DAY_OF_WEEK, -2);
						}

					}



                    activity.setPlanInitDate(tempCal.getTime());

                    tempCal.setTime(activity.getPlanEndDate());

					tempCal.add(Calendar.DAY_OF_MONTH, days.intValue());

					if (tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

						if (days.intValue() > 0) {

							tempCal.add(Calendar.DAY_OF_WEEK, 2);
						}
						else {

							tempCal.add(Calendar.DAY_OF_WEEK, -1);
						}
					}
					else if (tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

						if (days.intValue() > 0) {

							tempCal.add(Calendar.DAY_OF_WEEK, 1);
						}
						else {

							tempCal.add(Calendar.DAY_OF_WEEK, -2);
						}
					}

                    activity.setPlanEndDate(tempCal.getTime());

                    activityDAO.makePersistent(activity);

                    addInfo = true;
                }
            }

            if (addInfo) { newInfo.add("msg.info.check_activity_dates"); }

            // Update Milestone date
            List<Milestones> milestones = milestoneDAO.findByProject(proj);

            for (Milestones milestone : milestones) {

                if (milestone.getPlanned() != null) {

                    tempCal.setTime(milestone.getPlanned());

                    tempCal.add(Calendar.DAY_OF_MONTH, days.intValue());
                    milestone.setPlanned(tempCal.getTime());

                    if (milestone.getNotifyDate() != null) {
                        tempCal.setTime(milestone.getNotifyDate());

                        tempCal.add(Calendar.DAY_OF_MONTH, days.intValue());
                        milestone.setNotifyDate(tempCal.getTime());
                    }

                    milestoneDAO.makePersistent(milestone);

                    addInfo = true;
                }
            }

            if (addInfo) { newInfo.add("msg.info.check_milestones_dates"); addInfo = false; }

            // Update followup date
            List<Projectfollowup> followups = followupDAO.findByRelation(Projectfollowup.PROJECT, proj);


            for (Projectfollowup followup : followups) {

                if (followup.getFollowupDate() != null) {

                    tempCal.setTime(followup.getFollowupDate());

                    tempCal.add(Calendar.DAY_OF_MONTH, days.intValue());

					if (tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

						if (days.intValue() > 0) {

							tempCal.add(Calendar.DAY_OF_WEEK, 2);
						}
						else {

							tempCal.add(Calendar.DAY_OF_WEEK, -1);
						}
					}
					else if (tempCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

						if (days.intValue() > 0) {

							tempCal.add(Calendar.DAY_OF_WEEK, 1);
						}
						else {

							tempCal.add(Calendar.DAY_OF_WEEK, -2);
						}
					}

					// if the followup lands on an exception day, then delete it so it does not affect the
					if (exceptionDates.contains(tempCal.getTime())) {

						followupDAO.makeTransient(followup);
					}
					// if no followup in the same time of the new followup, then add it
					else {

						if (followupDAO.findBydDate(followup.getIdProjectFollowup(), proj, null, tempCal.getTime()) == null) {

							followup.setFollowupDate(tempCal.getTime());
						}

						followupDAO.makePersistent(followup);
					}

                    addInfo = true;
                }
            }

            if (addInfo) { newInfo.add("msg.info.check_followups_dates"); addInfo = false; }

            if (memberDAO.isAssigned(proj)) { newInfo.add("msg.info.check_members_assigned"); }
        }

        // Update dates of project
        proj.setStartDate(getStartDate(proj, session));
        proj.setFinishDate(getFinishDate(proj, session));

        // Update Calculate Plan Dates to root activity
        activityDAO.updatePlannedDates(proj, activityDAO.consRootActivity(proj));

		return newInfo;
	}
	
	/**
	 * Update projects leads
	 * 
	 * @param session
	 * @param proj
	 * @return
	 * @throws Exception
	 */
	public List<String> updateDatesLeads(Session session, Project proj) throws Exception {
		
		ProjectassociationDAO dao	= new ProjectassociationDAO(session);
		ProjectDAO projectDAO		= new ProjectDAO(session);
		
		List<Project> leads = dao.findChilds(proj);

		List<String> info = new ArrayList<String>();
		
		if (ValidateUtil.isNotNull(leads)) {
			
			LOGGER.debug("Update leads of: "+proj.getProjectName());
			
			for (Project lead : leads) {
				
				Date endDate = null;
				
				List<Project> parents = dao.findParents(lead);
				
				for (Project parent : parents) {
					
					Date parentEndDate = this.getFinishDate(parent, session);
					if (endDate == null || parentEndDate.after(endDate)) {
						endDate = parentEndDate;
					}
				}
				
				// Create new dates
				Calendar init = Calendar.getInstance();
				init.setTime(endDate);
				init.add(Calendar.DAY_OF_MONTH, 1);
				
				// Update dates of lead project
				int days = DateUtil.daysBetween(lead.getPlannedInitDate(), lead.getPlannedFinishDate());
				
				Calendar end = Calendar.getInstance();
				end.setTime(init.getTime());
				end.add(Calendar.DAY_OF_MONTH, days);
				
				lead.setPlannedInitDate(init.getTime());
				lead.setPlannedFinishDate(end.getTime());
				
				projectDAO.makePersistent(lead);
				info.addAll(updateDates(session, lead, init.getTime(), lead.getPlannedInitDate()));
				
				// Find for update leads of lead
				info.addAll(updateDatesLeads(session, lead));
				
			}
		}
		else {
			LOGGER.debug("No leads found for: "+proj.getProjectName());
		}
		
		return info;
	}

	
	/**
	 * Create or Update project and it project Charter
     *
	 * @param proj
	 * @param company
	 * @throws Exception 
	 */
	public Project createProject(Project proj, Company company) throws Exception {
		
		if (proj == null) {
			throw new NoDataFoundException();
		}
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			// Declare DAOs
			ProjectDAO projDAO					= new ProjectDAO(session);
			ProjectCharterDAO projCharterDAO	= new ProjectCharterDAO(session);		
			ProjectActivityDAO activityDAO		= new ProjectActivityDAO(session);
			ProjectFollowupDAO followupDAO		= new ProjectFollowupDAO(session);
			ProjectCostsDAO projCostsDAO		= new ProjectCostsDAO(session);
			EmployeeDAO employeeDAO				= new EmployeeDAO(session);
			
			if (proj.getInitDate() == null) {
				proj.setInitDate(new Date());
			}

			proj = projDAO.makePersistent(proj);
			
			// Create Project Charter
			Projectcharter projCharter = new Projectcharter();
			projCharter.setProject(proj);
			
			projCharterDAO.makePersistent(projCharter);
								
			// Create root WbsNode
			Wbsnode node = new Wbsnode();
			node.setName(proj.getProjectName());
			node.setProject(proj);
			node.setIsControlAccount(true);
			WBSNodeDAO wbsnodeDAO = new WBSNodeDAO(session);
			wbsnodeDAO.makePersistent(node);
			
			// Create activity for root WbsNode 
			Projectactivity activity = new Projectactivity();
			activity.setActivityName(proj.getProjectName());
			activity.setPlanInitDate(proj.getPlannedInitDate());
			activity.setPlanEndDate(proj.getPlannedFinishDate());
			activity.setWbsnode(node);
			activity.setProject(proj);
			activityDAO.makePersistent(activity);
			
			// Create default followups for initiating and ending project, if setting 
			// create_followup_with_project_creation is true

			if (Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_CREATE_FOLLOWUP_WITH_PROJECT_CREATION, 
					Settings.SETTING_CREATE_FOLLOWUP_WITH_PROJECT_CREATION_DEFAULT))) {

				Calendar auxDate = new GregorianCalendar();
				auxDate.setTime(new Date(proj.getPlannedInitDate().getTime()));

				if(auxDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

					auxDate.add(Calendar.DAY_OF_WEEK, 2);
				}
				else if(auxDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){

					auxDate.add(Calendar.DAY_OF_WEEK, 1);
				}

				Projectfollowup init = new Projectfollowup();
				init.setProject(proj);
				init.setFollowupDate(auxDate.getTime());
				init.setPv(new Double(0));

				auxDate.setTime(new Date(proj.getPlannedFinishDate().getTime()));

				if(auxDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

					auxDate.add(Calendar.DAY_OF_WEEK, -1);
				}
				else if(auxDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){

					auxDate.add(Calendar.DAY_OF_WEEK, -2);
				}
				Projectfollowup end = new Projectfollowup();
				end.setProject(proj);
				end.setFollowupDate(auxDate.getTime());
				end.setPv(proj.getBac());
				
				followupDAO.makePersistent(init);
				followupDAO.makePersistent(end);
			}
			
			// Create Team members for Project Manager
			Employee resource = null; 
				
			if (proj.getEmployeeByProjectManager() != null) {
				resource = employeeDAO.findResourceByPM(proj.getEmployeeByProjectManager());
			}
			
			if (resource != null) {
				
				Teammember memberPM = new Teammember();
				memberPM.setStatus(Constants.RESOURCE_ASSIGNED);
				memberPM.setDateApproved(DateUtil.getCalendar().getTime());
				memberPM.setDateIn(proj.getPlannedInitDate());
				memberPM.setDateOut(proj.getPlannedFinishDate());
				memberPM.setEmployee(resource);
				memberPM.setProjectactivity(activity);
				
				TeamMemberDAO memberDAO = new TeamMemberDAO(session);
				memberDAO.makePersistent(memberPM);
			}
			
			// Create default expenses
			Projectcosts projectCosts = new Projectcosts();
			projectCosts.setCostDate(proj.getPlannedInitDate());
			projectCosts.setProject(proj);
			
			projCostsDAO.makePersistent(projectCosts);
			
			// Update dates of project
			proj.setStartDate(getStartDate(proj, session));
			proj.setFinishDate(getFinishDate(proj, session));
			
			// If company has a base calendar only, the project calendar is created with this base calendar
			CalendarbaseDAO calendarbaseDAO = new CalendarbaseDAO(session);
			
			List<Calendarbase> calendars = calendarbaseDAO.findByRelation(Calendarbase.COMPANY, company);
			
			if (calendars.size() == 1) {
				ProjectcalendarDAO projectcalendarDao = new ProjectcalendarDAO(session);
				Projectcalendar projCalendar = new Projectcalendar();						
				Calendarbase base = calendars.get(0);
				
				projCalendar.setDaysMonth(base.getDaysMonth());
				projCalendar.setEndTime1(base.getEndTime1());
				projCalendar.setEndTime2(base.getEndTime2());
				projCalendar.setFiscalYearStart(base.getFiscalYearStart());
				projCalendar.setHoursDay(base.getHoursDay());
				projCalendar.setHoursWeek(base.getHoursWeek());
				projCalendar.setStartTime1(base.getStartTime1());
				projCalendar.setStartTime2(base.getStartTime2());
				projCalendar.setWeekStart(base.getWeekStart());		    			
				projCalendar.setCalendarbase(base);
				
				projCalendar = projectcalendarDao.makePersistent(projCalendar);
				
				proj.setProjectcalendar(projCalendar);
			}
			
			// TODO Create project base association
			//
			
			projDAO.makePersistent(proj);
			
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
		
		return proj;
	}
	
	
	/**
	 * Approve planning project
	 * @param project
	 * @param user
	 * @throws Exception 
	 */
	public void approveProject(Project project, Employee user) throws Exception {
		if (project == null) {
			throw new ProjectNotFoundException();
		}

        // Only PMO change status of project if setting is activated
        if (Constants.ROLE_PMO != user.getResourceprofiles().getIdProfile()
                && SettingUtil.getBoolean(settings, GeneralSetting.ONLY_PMO_CHANGE_STATUS)) {

            throw new SecurityException("Only PMO change status of project");
        }

		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();

			// Declare logics
			NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(settings);
			
			// Declare DAOs
			ProjectDAO projectDAO			= new ProjectDAO(session);
			ProjectKpiDAO kpiDAO			= new ProjectKpiDAO(session);
			ProjectActivityDAO activityDAO	= new ProjectActivityDAO(session);
			LogprojectstatusDAO statusDAO	= new LogprojectstatusDAO(session);
			
			project.setStatus(Constants.STATUS_CONTROL);
			
			project.setExecDate(new Date());
			
			Double totalWeight = kpiDAO.getTotalWeight(project);
			
			if (totalWeight != null &&  totalWeight != 100) {
				throw new ProjectKpiWeightException();
			}
			
			if (!Constants.INVESTMENT_APPROVED.equals(project.getInvestmentStatus())) {
				throw new ProposalNotWonException();
			}
			projectDAO.makePersistent(project);
			
			statusDAO.makePersistent(new Logprojectstatus(
					project,user, Constants.STATUS_CONTROL, Constants.INVESTMENT_APPROVED, new Date()));
			
			Projectactivity rootActivity = activityDAO.consRootActivity(project);
			
			rootActivity.setActualInitDate(rootActivity.getPlanInitDate());
			rootActivity.setActualEndDate(rootActivity.getPlanEndDate());
			
			activityDAO.makePersistent(rootActivity);
			
			// Update dates of project
			project.setStartDate(getStartDate(project, session));
			project.setFinishDate(getFinishDate(project, session));
			
			projectDAO.makePersistent(project);
			
			// Update projects leads
			this.updateDatesLeads(session, project);
			
			// Notification - The project has changed state
			if (SettingUtil.getBoolean(settings, NotificationType.CHANGE_PROJECT_STATUS)) {
				notificationsLogic.changeProjectStatus(session, project.getIdProject());
			}
						
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
	}

	/**
	 * Update Data of Project
	 * @param project
	 * @throws Exception
	 */
	public void saveAndUpdateDataProject(Project project) throws Exception {
		if (project == null) {
			throw new ProjectNotFoundException();
		}
		
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ProjectDAO projectDAO		= new ProjectDAO(session);
			
			// Store first data
			project = projectDAO.makePersistent(project);
			
			// Update dates of project
			project.setStartDate(getStartDate(project, session));
			project.setFinishDate(getFinishDate(project, session));
			project.setStatusDate(new Date());
			
			// Update activities
			ProjectActivityDAO projectActivityDAO = new ProjectActivityDAO(session);
			
			List<Projectactivity> projectActivities = projectActivityDAO.findByProject(project, null);
			
			if (ValidateUtil.isNotNull(projectActivities)) {
				
				for (Projectactivity activity : projectActivities) {
					
					// Update poc and ev
					activity.setEv(0.0);
					activity.setPoc(0.0);
					
					// Update actual dates
					if (activity.getPlanInitDate() != null) {
						activity.setActualInitDate(activity.getPlanInitDate());
					}
					
					if (activity.getPlanEndDate() != null) {
						activity.setActualEndDate(activity.getPlanEndDate());
					}
					
					projectActivityDAO.makePersistent(activity);
				}
			}
			
			projectDAO.makePersistent(project);
			
			// Update projects leads
			this.updateDatesLeads(session, project);
			
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
	}
	
	/**
	 * Update Data of Project
	 * 
	 * @param project
	 * @throws Exception
	 */
	public void saveAndUpdateDataProject(Project project, Session session) throws Exception {
		
		if (project == null) {
			throw new ProjectNotFoundException();
		}
		
		// Declare DAOs
		ProjectDAO projectDAO					= new ProjectDAO(session);
		ProjectActivityDAO projectActivityDAO 	= new ProjectActivityDAO(session);
		
		// Store first data
		project = projectDAO.makePersistent(project);
		
		// Update dates of project
		project.setStartDate(getStartDate(project, session));
		project.setFinishDate(getFinishDate(project, session));
		project.setStatusDate(new Date());
		
		// Update activities
		
		List<Projectactivity> projectActivities = projectActivityDAO.findByProject(project, null);
		
		if (ValidateUtil.isNotNull(projectActivities)) {
			
			for (Projectactivity activity : projectActivities) {
				
				// Update poc and ev
				activity.setEv(0.0);
				activity.setPoc(0.0);
				
				// Update actual dates
				if (activity.getPlanInitDate() != null) {
					activity.setActualInitDate(activity.getPlanInitDate());
				}
				
				if (activity.getPlanEndDate() != null) {
					activity.setActualEndDate(activity.getPlanEndDate());
				}
				
				projectActivityDAO.makePersistent(activity);
			}
		}
		
		projectDAO.makePersistent(project);
		
		// Update projects leads
		this.updateDatesLeads(session, project);
	}
	
	/**
	 * Update Data of project
	 * 
	 * @param idProject
	 * @throws Exception
	 */
	public Project updateDataProject(Integer idProject) throws Exception {
		
		Project project = null;
		
		if (idProject == null) {
			throw new ProjectNotFoundException();
		}
		
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ProjectDAO projectDAO		= new ProjectDAO(session);
			
			// Load Project
			project = projectDAO.findById(idProject);
			
			// Update dates of project
			project.setStartDate(getStartDate(project, session));
			project.setFinishDate(getFinishDate(project, session));
			
			project = projectDAO.makePersistent(project);
			
			// Update projects leads
			this.updateDatesLeads(session, project);
			
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
		
		return project;
	}
	
	/**
	 * Update Data of project
	 * 
	 * @param idProject
	 * @param session
	 * @throws Exception
	 */
	public Project updateDataProject(Integer idProject, Session session) throws Exception {
		
		Project project = null;

		if (idProject == null) {
			throw new ProjectNotFoundException();
		}
		
		ProjectDAO projectDAO		= new ProjectDAO(session);
		
		// Load Project
		project = projectDAO.findById(idProject);
		
		// Update dates of project
		project.setStartDate(getStartDate(project, session));
		project.setFinishDate(getFinishDate(project, session));
		
		project = projectDAO.makePersistent(project);
		
		// Update projects leads
		this.updateDatesLeads(session, project);

		return project;
	}


    /**
     * Update KPI status of project
     *
     * @param session
     * @param idProject
     * @return
     * @throws Exception
     */
    public Project updateKPIStatus(Session session, Integer idProject) throws Exception {

        Project project;

        if (idProject == null) {
            throw new ProjectNotFoundException();
        }

        ProjectDAO projectDAO		= new ProjectDAO(session);
        ProjectKpiDAO kpiDAO 		= new ProjectKpiDAO(session);

        // Load Project
        project = projectDAO.findById(idProject);

        if (!kpiDAO.thereKPISEmpty(project)) {

            Set<Projectkpi> projectkpis = project.getProjectkpis();

            if (!projectkpis.isEmpty()) {

                Double totalScore 	= 0.0;
                Double threshold	= (project.getLowerThreshold() == null?0.0:project.getLowerThreshold().doubleValue());
                Double target       = (project.getUpperThreshold() == null?0.0:project.getUpperThreshold().doubleValue());

                //totalValue = totalScore of project
                for (Projectkpi kpi: projectkpis) {
                    totalScore += kpi.getScore();
                }

                //calculated normalized value
                Double value 			= totalScore;

                if ((target - threshold) != 0.0) {

                    Double calculated 	= ((value-threshold)/(target - threshold)) == -0.0 ? 0.0 : ((value-threshold)/(target - threshold));

                    if (threshold != value && calculated < 0.0) {
                        project.setKpiStatus(Constants.KPI_STATUS_HIGH);
                    }
                    else if (calculated > 1.0) {
                        project.setKpiStatus(Constants.KPI_STATUS_LOW);
                    }
                    else {
                        project.setKpiStatus(Constants.KPI_STATUS_MEDIUM);
                    }
                }

                project = projectDAO.makePersistent(project);
            }
        }

        return project;
    }

	/**
	 * Update KPI status of project 
	 * 
	 * @param idProject
	 * @throws Exception
	 */
	public Project updateKPIStatus(Integer idProject) throws Exception {
		
		Project project = null;
		
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			
			tx = session.beginTransaction();

            project = updateKPIStatus(session, idProject);
						
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
		
		return project;
	}

	/**
	 * Calculate Work In Progress of a project
	 * WIP = Suma(Activity.EV) - Suma(Income.ActualBillAmmount)
	 * @param project
	 * @return WIP
	 * @throws Exception 
	 */
	public Double calculateWIP(Project project) throws Exception {
		Double wip = 0D;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
		
			ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
			IncomesDAO incomesDAO = new IncomesDAO(session);
			
			Double ev = activityDAO.consCurrentProjectEV(project.getIdProject());
			Double actualBillAmmount = incomesDAO.consActualBillAmmount(project.getIdProject());
			
			wip = ev - actualBillAmmount;
			
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
		
		return wip;
	}
	
			
	/**
	 * Calculate DSO billed
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public Integer calculateDSOBilled(Project project) throws Exception {
		Integer dsoBilled = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO = new ProjectDAO(session);
			dsoBilled = projectDAO.calculateDSOBilled(project);
			
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
		
		return dsoBilled;
	}


	/**
	 * Calculate DSO unbilled
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Integer calculateDSOUnbilled(Project project) throws Exception {
		Integer dsoUnbilled = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO = new ProjectDAO(session);
			dsoUnbilled = projectDAO.calculateDSOUnbilled(project);
			
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
		
		return dsoUnbilled;
	}

	
	/**
	 * Check hours in APP2 or APP1 for close project
	 * @param project
	 * @throws Exception
	 */
	public List<String> checkForCloseProject(Project project) throws Exception {
		
		List<String> warnings = new ArrayList<String>();
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			TeamMemberDAO memberDAO			= new TeamMemberDAO(session);

			// Close date
			Date closeDate = new Date();
			
			// Update teammembers dates
			List<Teammember> teammembers = memberDAO.findByProjectInDate(project, closeDate);
			
			TeamMemberLogic memberLogic = new TeamMemberLogic(getBundle());
			
			for (Teammember item : teammembers) {
				
				// Find warning
				String warning = memberLogic.checkTeamMember(session, item.getIdTeamMember(), closeDate, Constants.TIMESTATUS_APP1);
				
				// Add warning to list
				if (ValidateUtil.isNotNull(warning)) { warnings.add(warning); }
				
				// Find warning
				warning = memberLogic.checkTeamMember(session, item.getIdTeamMember(), closeDate, Constants.TIMESTATUS_APP2);
				
				// Add warning to list
				if (ValidateUtil.isNotNull(warning)) { warnings.add(warning); }
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return warnings;
	}

	
	/**
	 * Delete project in initiating
	 * @param project
     * @throws Exception
	 */
	public void deleteProject(Project project) throws Exception {
		
		if (project == null) {
			throw new ProjectNotFoundException();
		}

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();

            // Declare DAOs
			ProjectDAO projDAO                          = new ProjectDAO(session);
			StakeholderDAO stakeholderDAO               = new StakeholderDAO(session);
            MilestoneDAO milestoneDAO                   = new MilestoneDAO(session);
			ProjectCharterDAO charterDAO                = new ProjectCharterDAO(session);
			ProjectCostsDAO costsDAO                    = new ProjectCostsDAO(session);
            ClosurecheckprojectDAO closurecheckprojDAO  = new ClosurecheckprojectDAO(session);
			
			project = projDAO.findById(project.getIdProject(), false);
			
			//Delete all stackeholder
			stakeholderDAO.deleteByProject(project);
			
			//Delete all milestones
			milestoneDAO.deleteByProject(project);
			
			//Delete project Chartet
			charterDAO.deleteByProject(project);
			
			//Delete all costs
			costsDAO.deleteByProject(project);

            //Delete closure check project
            closurecheckprojDAO.deleteByProject(project);
			
			//Delete project
			projDAO.makeTransient(project);

			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

	
	/**
	 * Consult list of projects by ids
	 * @param ids
	 * @param property
	 * @param order
	 * @param joins
	 * @return
	 * @throws Exception
	 */
	public List<Project> consList(Integer[] ids, String property, String order, List<String> joins) throws Exception {
		
		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			if (ids != null) {
				ProjectDAO projDAO = new ProjectDAO(session);
				list = projDAO.consList(ids, property, order, joins);
			}
			else { list = new ArrayList<Project>(); }
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return list;
	}
	
	/**
	 * @param program
	 * @param investiment
	 * @return
	 * @throws Exception
	 */
	public List<Project> findByProgram(Program program,
			boolean investiment) throws Exception {
		
		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			list = projDAO.findByProgram(program, investiment);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return list;
	}
	
	/**
	 * Cons Project and extra info
	 * @param project
	 * @param joins
	 * @return
	 * @throws Exception
	 */
	public Project consProject(Project project, List<String> joins) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			project = projDAO.consProject(project, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return project;
	}	
	
	/**
	 * @param proj
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean chartLabelInUse(Project proj, Employee user) throws Exception {
		boolean used = false;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			CompanyDAO companyDAO = new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(user);
			
			used = projDAO.chartLabelInUse(proj, company);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return used;
	}

	/**
	 * @param ids
	 * @param property
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public List<Project> consListInControl(Integer[] ids, String property, String order) throws Exception {
		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			list = projDAO.consListInControl(ids, property, order);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return list;
	}

	/**
	 * @param project
	 * @param user
	 * @param tab
	 * @return
	 * @throws Exception
	 */
	public boolean hasPermission(Project project, Employee user, int tab) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		boolean permission = false;
		try {	
			tx = session.beginTransaction();
			
			ProjectDAO projDAO		= new ProjectDAO(session);
			CompanyDAO companyDAO	= new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(user);
			permission		= projDAO.hasPermission(project, user, company, tab);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return permission;
	}

	/**
	 * Find project by income
	 * @param income
	 * @return
	 * @throws Exception
	 */
	public Project findByIncome(Incomes income) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		Project project = null;
		
		try {	
			tx = session.beginTransaction();
			
			ProjectDAO projDAO		= new ProjectDAO(session);
			
			project	= projDAO.findByIncome(income);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return project;
	}

	/**
	 * Find by Project Followup
	 * @param projectfollowup
	 * @return
	 * @throws Exception 
	 */
	public Project findByFollowup(Projectfollowup projectfollowup) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		Project project = null;
		
		try {	
			tx = session.beginTransaction();
			
			ProjectDAO projDAO		= new ProjectDAO(session);
			
			project	= projDAO.findByFollowup(projectfollowup);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return project;
	}

	/**
	 * @param search
	 * @param performingorg
	 * @param company
	 * @return
	 * @throws Exception
	 */
	public List<Project> find(String search, Performingorg performingorg, Company company) throws Exception {
		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			list = projDAO.find(search, performingorg, company);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return list;
	}

	/**
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public double calcExternalCosts(Project project) throws Exception {
		
		double costs = 0;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ChargescostsDAO chargescostsDAO = new ChargescostsDAO(session);
			
			costs =	chargescostsDAO.getSumCosts(project, Constants.SELLER_CHARGE_COST);
			costs += chargescostsDAO.getSumCosts(project, Constants.INFRASTRUCTURE_CHARGE_COST);
			costs += chargescostsDAO.getSumCosts(project, Constants.LICENSE_CHARGE_COST);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return costs;
	}
	
	/**
	 * Calc POC of project 
	 * 
	 * @param project
	 * @param settings
     * @return
	 * @throws Exception
	 */
	public double updatePoc(Project project, HashMap<String, String> settings) throws Exception {

		Double poc = null;

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// Declare DAO
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			// DAO get project
			project = projectDAO.findById(project.getIdProject());
			
			// Calculated Poc
			poc = calcPoc(project, session, settings);
			
			// Set data for update
			project.setPoc(poc);
			
			// DAO update
			projectDAO.makePersistent(project);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }

		return (poc == null ? 0 : poc);
	}
	  
	/**
	 * Calc POC of project 
	 * @param project
	 * @param session
	 * @param settings
     * @return
	 * @throws Exception
	 */
    public double calcPoc(Project project, Session session, HashMap<String, String> settings) throws Exception {
    	
    	Double poc = null;
    	try {
    		ProjectActivityDAO activityDAO	= new ProjectActivityDAO(session);
        	Projectactivity rootActivity	= activityDAO.consRootActivity(project);

    		if (rootActivity != null) {
    			poc = activityDAO.calcPoc(rootActivity, settings);
    		}
    		else {
    			throw new LogicException("No hay actividad principal: "+project.getProjectName());
    		}
    	}
    	catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
		}
    	return (poc == null?0:poc);
    }

	/**
	 * Change status closed of project to control
	 * @param project
	 * @param user
	 * @throws Exception 
	 */
	public void changeToControl(Project project, Employee user) throws Exception {

        // Only PMO change status of project if setting is activated
        if (Constants.ROLE_PMO != user.getResourceprofiles().getIdProfile()
                && SettingUtil.getBoolean(settings, GeneralSetting.ONLY_PMO_CHANGE_STATUS)) {

            throw new SecurityException("Only PMO change status of project");
        }

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO			= new ProjectDAO(session);
			LogprojectstatusDAO statusDAO	= new LogprojectstatusDAO(session);
			
			project = projectDAO.findById(project.getIdProject(), false);
			
			project.setStatus(Constants.STATUS_CONTROL);
			project.setInvestmentStatus(Constants.INVESTMENT_APPROVED);
			
			projectDAO.makePersistent(project);
			
			statusDAO.makePersistent(new Logprojectstatus(
					project,user, Constants.STATUS_CONTROL, Constants.INVESTMENT_APPROVED, new Date()));
			
			// Update dates of project
			project.setStartDate(getStartDate(project, session));
			project.setFinishDate(getFinishDate(project, session));
			
			projectDAO.makePersistent(project);
			
			// Update projects leads
			this.updateDatesLeads(session, project);

            // Notification - The project has changed state
            if (SettingUtil.getBoolean(settings, NotificationType.CHANGE_PROJECT_STATUS)) {

                NotificationSpecificLogic notificationsLogic 	= new NotificationSpecificLogic(settings);
                notificationsLogic.changeProjectStatus(session, project.getIdProject());
            }
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}
	
	/**
	 * 
	 * @param program
	 * @return
	 * @throws Exception
	 */
	public double calcBudgetBottomUp(Program program) throws Exception {
		
		double budgets = 0;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			boolean projectsDisabled = projectsDisabled(session);
			
			budgets = projectDAO.getBudgetBottomUp(program, projectsDisabled);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return budgets;
	}
	
	/**
	 * 
	 * @param employee
	 * @return
	 * @throws Exception
	 */
	public List<Project> findByEmployee(Employee employee, List<String> joins) throws Exception {
		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			list = projDAO.findByEmployee(employee, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return list;
	}


	/**
	 * Find by PO and equals Planning or Execution
	 * 
	 * @param performingorg
	 * @return
	 * @throws Exception 
	 */
	public List<Project> findByPO(Performingorg performingorg) throws Exception {
		
		List<Project> projects = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			projects = projectDAO.findByPO(performingorg, projectsDisabled(session));
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return projects;
	}
	
	/**
	 * Find projects by employee between initDate and endDate
	 * @param employee
	 * @param initDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public List<Project> findByEmployee(Employee employee, Date initDate, Date endDate) throws Exception {
		
		List<Project> projects = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO = new ProjectDAO(session);
			projects = projectDAO.findByEmployee(employee, initDate, endDate);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return projects;
	}

	/**
	 * Check if project is in these status
	 * @param project
	 * @param status
	 * @return
	 * @throws Exception 
	 */
	public boolean hasPermissionProject(Project project, String...status) throws Exception {
		
		boolean permission = false;
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO = new ProjectDAO(session);
			permission = projectDAO.hasPermissionProject(project, status);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return permission;
	}

    /**
     * Generate resource capacity running
     *
     * @param ids
     * @param since
     * @param until
     * @param idResourcePool
     * @param company
     * @param user
     * @param showAll
     * @return
     * @throws Exception
     */
	public Object[][] capacityRunningResource(Integer[] ids, Date since, Date until,
			Integer idResourcePool, Company company, Employee user, Boolean showAll) throws Exception {
		
		Object[][] table = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			// Create DAOS
			ProjectDAO projDAO			= new ProjectDAO(session);
			EmployeeDAO employeeDAO		= new EmployeeDAO(session);
			TimesheetDAO timesheetDAO	= new TimesheetDAO(session);
			OperationDAO operationDAO	= new OperationDAO(session);
			
			List<Project> projects		= projDAO.consList(ids, Project.ACCOUNTINGCODE, Constants.ASCENDENT, null);
			List<Employee> employees	= employeeDAO.findInputedInProjects(projects, since, until, idResourcePool, null);
			
			if (Constants.ROLE_PMO == user.getResourceprofiles().getIdProfile() && showAll && ValidateUtil.isNotNull(employees)) {
				employees.addAll(employeeDAO.findInputedInOperations(employees, since, until, idResourcePool));
			}
			
			if (ValidateUtil.isNotNull(employees)) {
				
				// Find operations 
				//
				List<Operation> operations = operationDAO.findByEmployeesAndDatesAndApproved(employees, since, until, company);
				
				int rows = projects.size() + operations.size() + 2;
			
				table = new Object[rows][employees.size()+2];
				
				// Set legends for projects
				int i = 1;
				for (Project project : projects) {
					table[i++][0] = ValidateUtil.isNullCh(project.getAccountingCode(), StringPool.BLANK) + StringPool.SPACE + project.getProjectName();
				}
				for (Operation operation : operations) {
					table[i++][0] = operation.getOperationName();
				}
				
				// Set legends for employees
				i = 1;
				for (Employee employee : employees) {
					table[0][i++] = employee.getContact().getFullName();
				}
				
				// Set values
				i = 1;
				for (Project project : projects) {
					
					int j = 1;
					for (Employee employee : employees) {
						
						double hours = timesheetDAO.getHoursResourceInDates(project, employee, since, until, null, null, null);
						
						// Hours for employee in project
						table[i][j] = (table[i][j] == null ? hours : ((Double)table[i][j])+hours);
						
						// Total hours for project
						table[rows-1][j] = (table[rows-1][j] == null ? hours : ((Double)table[rows-1][j])+hours);
						
						// Total hours for employee
						table[i][employees.size()+1] = (table[i][employees.size()+1] == null ? hours : ((Double)table[i][employees.size()+1])+hours);
						
						// Total
						table[rows-1][employees.size()+1] = (table[rows-1][employees.size()+1] == null ? hours : ((Double)table[rows-1][employees.size()+1])+hours);
						j++;
					}
					i++;
				}
				for (Operation operation : operations) {
					
					int j = 1;
					for (Employee employee : employees) {
						
						double hours = timesheetDAO.getHoursResourceInDates(null, employee, since, until, null, operation, null);
						
						// Hours for employee in project
						table[i][j] = (table[i][j] == null ? hours : ((Double)table[i][j])+hours);
						
						// Total hours for project
						table[rows-1][j] = (table[rows-1][j] == null ? hours : ((Double)table[rows-1][j])+hours);
						
						// Total hours for employee
						table[i][employees.size()+1] = (table[i][employees.size()+1] == null ? hours : ((Double)table[i][employees.size()+1])+hours);
						
						// Total
						table[rows-1][employees.size()+1] = (table[rows-1][employees.size()+1] == null ? hours : ((Double)table[rows-1][employees.size()+1])+hours);
						j++;
					}
					i++;
				}
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return table;
	}

    /**
     * Capacity running by month
     *
     * @param ids
     * @param since
     * @param until
     * @param idioma
     * @param idResourcePool
     * @param company
     * @param user
     * @param showAll
     * @return
     * @throws Exception
     */
	public Object[][] capacityRunningMonth(Integer[] ids, Date since, Date until, ResourceBundle idioma,
			Integer idResourcePool, Company company, Employee user, Boolean showAll) throws Exception {
		
		Object[][] table = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			// Create DAOS
			ProjectDAO projDAO			= new ProjectDAO(session);
			TimesheetDAO timesheetDAO	= new TimesheetDAO(session);
			OperationDAO operationDAO	= new OperationDAO(session);
			EmployeeDAO employeeDAO		= new EmployeeDAO(session);
			
			List<Project> projects		= projDAO.consList(ids, Project.ACCOUNTINGCODE, Constants.ASCENDENT, null);
			List<Employee> employees	= employeeDAO.findInputedInProjects(projects, since, until, idResourcePool, null);

            if (Constants.ROLE_PMO == user.getResourceprofiles().getIdProfile() && showAll && ValidateUtil.isNotNull(employees)) {
                employees.addAll(employeeDAO.findInputedInOperations(employees, since, until, idResourcePool));
            }

            if (ValidateUtil.isNotNull(projects) && ValidateUtil.isNotNull(employees)) {

                // Find operation only if projects and employees not null
                List<Operation> operations	= operationDAO.findByEmployeesAndDatesAndApproved(employees, since, until, company);

                int rows = projects.size() + operations.size() + 2;
				
				int months = 0;
				Calendar sinceCal = null;
				Calendar untilCal = null;
				
				if (since != null && until != null) {
					sinceCal = DateUtil.getCalendar();
					sinceCal.setTime(since);
					
					untilCal = DateUtil.getCalendar();
					untilCal.setTime(until);
					
					while ( sinceCal.before(untilCal) || sinceCal.equals(untilCal)) {
						sinceCal.add(Calendar.MONTH, 1);
						months++; 
					}
					
					sinceCal.setTime(since);
				}
				
				if (months > 0) {
					
					table = new Object[rows][months+2];
					
					// Set legends for projects
					int i = 1;
					for (Project project : projects) {
						table[i++][0] = ValidateUtil.isNullCh(project.getAccountingCode(), StringPool.BLANK) + StringPool.SPACE + project.getProjectName();
					}
					
					if (ValidateUtil.isNotNull(operations)) {
						
						for (Operation operation : operations) {
							table[i++][0] = operation.getOperationName();
						}
					}
					
					// Set legends for months
					i = 1;
					while ( sinceCal.before(untilCal) || sinceCal.equals(untilCal)) {
						table[0][i++] = idioma.getString("month.month_"+(sinceCal.get(Calendar.MONTH)+1));
						sinceCal.add(Calendar.MONTH, 1);
					}
					
					// Set values
					i = 1;
					for (Project project : projects) {
						
						sinceCal.setTime(since);
						for (int j = 1; j <= months ; j++) {
							
							double hours = 0.0;
							if (Constants.ROLE_PMO != user.getResourceprofiles().getIdProfile() || !showAll) {
								
								for (Employee employee : employees) {
									
									hours += timesheetDAO.getHoursResourceInDates(project, employee,
											DateUtil.getFirstMonthDay(sinceCal.getTime()),
											DateUtil.getLastMonthDay(sinceCal.getTime()),
											idResourcePool, null, null);
								}
							}
							else {
								
								hours = timesheetDAO.getHoursResourceInDates(project, null,
										DateUtil.getFirstMonthDay(sinceCal.getTime()),
										DateUtil.getLastMonthDay(sinceCal.getTime()),
										idResourcePool, null, null);
							}
							
							// Hours for month in project
							table[i][j] = (table[i][j] == null ? hours : ((Double)table[i][j])+hours);
							
							// Total hours for project
							table[rows-1][j] = (table[rows-1][j] == null ? hours : ((Double)table[rows-1][j])+hours);
							
							// Total hours for month
							table[i][months+1] = (table[i][months+1] == null ? hours : ((Double)table[i][months+1])+hours);
							
							// Total of year
							table[rows-1][months+1] = (table[rows-1][months+1] == null ? hours : ((Double)table[rows-1][months+1])+hours);
							
							// Next Month
							sinceCal.add(Calendar.MONTH, 1);
						}
						i++;
					}
					
					if (ValidateUtil.isNotNull(operations)) {
						
						for (Operation operation : operations) {
							
							sinceCal.setTime(since);
							for (int j = 1; j <= months ; j++) {
								
								double hours = 0.0;
								if (Constants.ROLE_PMO != user.getResourceprofiles().getIdProfile() || !showAll) {
									
									for (Employee employee : employees) {
										
										hours += timesheetDAO.getHoursResourceInDates(null, employee,
												DateUtil.getFirstMonthDay(sinceCal.getTime()),
												DateUtil.getLastMonthDay(sinceCal.getTime()),
												idResourcePool, operation, null);
									}
								}
								else {
									
									hours = timesheetDAO.getHoursResourceInDates(null, null,
											DateUtil.getFirstMonthDay(sinceCal.getTime()),
											DateUtil.getLastMonthDay(sinceCal.getTime()),
											idResourcePool, operation, null);
								}
								
								// Hours for month in project
								table[i][j] = (table[i][j] == null ? hours : ((Double)table[i][j])+hours);
								
								// Total hours for project
								table[rows-1][j] = (table[rows-1][j] == null ? hours : ((Double)table[rows-1][j])+hours);
								
								// Total hours for month
								table[i][months+1] = (table[i][months+1] == null ? hours : ((Double)table[i][months+1])+hours);
								
								// Total of year
								table[rows-1][months+1] = (table[rows-1][months+1] == null ? hours : ((Double)table[rows-1][months+1])+hours);
								
								// Next Month
								sinceCal.add(Calendar.MONTH, 1);
							}
							i++;
						}
					}
				}
			}
			
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return table;
	}

    /**
     *
     * @param ids
     * @param since
     * @param until
     * @param idioma
     * @param idResourcePool
     * @param company
     * @param user
     * @param showAll
     * @return
     * @throws Exception
     */
	public Object[][] capacityRunningActivityResource(Integer[] ids, Date since,
			Date until, ResourceBundle idioma, Integer idResourcePool,
			Company company, Employee user, Boolean showAll) throws Exception {
	
		Object[][] table = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			TimesheetDAO timesheetDAO		= new TimesheetDAO(session);
			ProjectActivityDAO activityDAO	= new ProjectActivityDAO(session);
			EmployeeDAO employeeDAO			= new EmployeeDAO(session);
			OperationDAO operationDAO		= new OperationDAO(session);
			
			List<Projectactivity> activities	= activityDAO.consByProjectList(ids);
			List<Employee> employees			= employeeDAO.findInputedInProjects(null, since, until, idResourcePool, activities);
			
			// Show all employees and all activities
			if (Constants.ROLE_PMO == user.getResourceprofiles().getIdProfile() && showAll && ValidateUtil.isNotNull(employees)) {
				employees.addAll(employeeDAO.findInputedInOperations(employees, since, until, idResourcePool));
			}
			// Show only employees and activities inputed
			else {
				activities = activityDAO.findActivitiesInputedByProjectsAndEmployees(ids, since, until, idResourcePool);
			}
			
			if (ValidateUtil.isNotNull(employees) && ValidateUtil.isNotNull(activities)) {
				
				List<Operation> operations = operationDAO.findByEmployeesAndDatesAndApproved(employees, since, until, company);
				
				// Create table
				int rows = activities.size() + operations.size() + 2;
				table = new Object[rows][employees.size()+4];
				
				// Set legends for projects and operations
				int i = 1;
				for (Projectactivity activity : activities) {
					Project project = activity.getProject();
					
					table[i][0] =  activity.getIdActivity();
					
					table[i][1] = ValidateUtil.isNullCh(project.getAccountingCode(), StringPool.BLANK)+
						StringPool.SPACE + project.getProjectName();
					
					table[i][2] =  activity.getActivityName();
					i++;
				}
				
				if (ValidateUtil.isNotNull(operations)) {
					
					for (Operation operation : operations) {
						table[i][0] = operation.getIdOperation();
						table[i][1] = operation.getOperationName();
						i++;
					}
				}
				
				// Set legends for employees
				i = 3;
				for (Employee employee : employees) {
					table[0][i++] = employee.getContact().getFullName();
				}
				
				// Set hours for activities
				int j = 3;
				for (Employee employee : employees) {
					
					List<Object[]> list = timesheetDAO.getHoursResourceInDates(since, until, activities, employee);

					i = 1;
					for (Object[] item : list) {
						
						while (!((Integer)table[i][0]).equals((Integer)item[1])) {
							i++; 
						}
						
						double hours = item[2] == null?0d:(Double)item[2];
						
						// Hours for resource in activity
						table[i][j] = (table[i][j] == null ? hours : ((Double)table[i][j])+ hours);
						
						// Total hours for activity
						table[rows-1][j] = (table[rows-1][j] == null ? hours : ((Double)table[rows-1][j])+hours);
						
						// Total hours for resource
						table[i][employees.size()+3] = (table[i][employees.size()+3] == null ? hours : ((Double)table[i][employees.size()+3])+hours);
						
						// Total
						table[rows-1][employees.size()+3] = (table[rows-1][employees.size()+3] == null ? hours : ((Double)table[rows-1][employees.size()+3])+hours);
						i++;
					}
					j++;
				}
				
				// Set hours for operations
				j = 3;
				for (Employee employee : employees) {
					
					List<Object[]> list = timesheetDAO.getHoursResourceInDatesOperation(since, until, operations, employee);
					
					i = 1;
					for (Object[] item : list) {
						
						while (!((Integer)table[i][0]).equals((Integer)item[0])) { i++; }
						
						double hours = item[1] == null? 0d:(Double)item[1];
						
						// Hours for resource in activity
						table[i][j] = (table[i][j] == null ? hours : ((Double)table[i][j])+ hours);
						
						// Total hours for activity
						table[rows-1][j] = (table[rows-1][j] == null ? hours : ((Double)table[rows-1][j])+hours);
						
						// Total hours for resource
						table[i][employees.size()+3] = (table[i][employees.size()+3] == null ? hours : ((Double)table[i][employees.size()+3])+hours);
						
						// Total
						table[rows-1][employees.size()+3] = (table[rows-1][employees.size()+3] == null ? hours : ((Double)table[rows-1][employees.size()+3])+hours);
						i++;
					}
					j++;
				}
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return table;
	}


    /**
     * Project x Activity x Month
     *
     * @param ids
     * @param since
     * @param until
     * @param idioma
     * @param idResourcePool
     * @param company
     * @param user
     * @param showAll
     * @return
     * @throws Exception
     */
	public Object[][] capacityRunningActivityMonth(Integer[] ids, Date since,
			Date until, ResourceBundle idioma, Integer idResourcePool,
			Company company, Employee user, Boolean showAll) throws Exception {
		
		Object[][] table = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			// Create DAOS
			ProjectActivityDAO activityDAO	= new ProjectActivityDAO(session);
			TimesheetDAO timesheetDAO		= new TimesheetDAO(session);
			OperationDAO operationDAO		= new OperationDAO(session);
			EmployeeDAO employeeDAO			= new EmployeeDAO(session);
			
			List<Projectactivity> activities	= activityDAO.consByProjectList(ids);
			List<Employee> employees			= employeeDAO.findInputedInProjects(null, since, until, idResourcePool, activities);

            // Show all employees and all activities
            if (Constants.ROLE_PMO == user.getResourceprofiles().getIdProfile() && showAll && ValidateUtil.isNotNull(employees)) {
                employees.addAll(employeeDAO.findInputedInOperations(employees, since, until, idResourcePool));
            }
			// Show only activities imputed
			else {
                activities = activityDAO.findActivitiesInputedByProjectsAndEmployees(ids, since, until, idResourcePool);
            }

            if (ValidateUtil.isNotNull(activities) && ValidateUtil.isNotNull(employees)) {

                List<Operation> operations = operationDAO.findByEmployeesAndDatesAndApproved(employees, since, until, company);

                // Create table
                int rows = activities.size() + operations.size() + 2;
				
				int months = 0;
				Calendar sinceCal = null;
				Calendar untilCal = null;
				
				if (since != null && until != null) {
					sinceCal = DateUtil.getCalendar();
					sinceCal.setTime(since);
					
					untilCal = DateUtil.getCalendar();
					untilCal.setTime(until);
					
					while ( sinceCal.before(untilCal) || sinceCal.equals(untilCal)) {
						sinceCal.add(Calendar.MONTH, 1);
						months++; 
					}
					
					sinceCal.setTime(since);
				}
				
				if (months > 0) {
					
					table = new Object[rows][months+3];
					
					// Set legends for projects
					int i = 1;
					for (Projectactivity activity : activities) {
						Project project = activity.getProject();
						table[i][0] = ValidateUtil.isNullCh(project.getAccountingCode(), StringPool.BLANK)+
								StringPool.SPACE + project.getProjectName();
						
						table[i++][1] =  activity.getActivityName();
					}
					for (Operation operation : operations) {
						table[i++][0] = operation.getOperationName();
					}
					
					// Set legends for months
					i = 2;
					while ( sinceCal.before(untilCal) || sinceCal.equals(untilCal)) {
						table[0][i++] = idioma.getString("month.month_"+(sinceCal.get(Calendar.MONTH)+1));
						sinceCal.add(Calendar.MONTH, 1);
					}
					
					// Set values
					i = 1;
					for (Projectactivity activity : activities) {
						
						sinceCal.setTime(since);
						for (int j = 1; j <= months ; j++) {
							
							double hours = 0.0;
							if (Constants.ROLE_PMO != user.getResourceprofiles().getIdProfile() || !showAll) {
								
								for (Employee employee : employees) {
									
									hours = timesheetDAO.getHoursResourceInDates(null, employee,
											DateUtil.getFirstMonthDay(sinceCal.getTime()),
											DateUtil.getLastMonthDay(sinceCal.getTime()),
											idResourcePool, null, activity);
								}
							}
							else {
								
								hours = timesheetDAO.getHoursResourceInDates(null, null,
										DateUtil.getFirstMonthDay(sinceCal.getTime()),
										DateUtil.getLastMonthDay(sinceCal.getTime()),
										idResourcePool, null, activity);
							}
							
							
							// Hours for month in activity
							table[i][j+1] = (table[i][j+1] == null ? hours : ((Double)table[i][j+1])+hours);
							
							// Total hours for activity
							table[rows-1][j+1] = (table[rows-1][j+1] == null ? hours : ((Double)table[rows-1][j+1])+hours);
							
							// Total hours for month
							table[i][months+2] = (table[i][months+2] == null ? hours : ((Double)table[i][months+2])+hours);
							
							// Total of year
							table[rows-1][months+2] = (table[rows-1][months+2] == null ? hours : ((Double)table[rows-1][months+2])+hours);
							
							// Next Month
							sinceCal.add(Calendar.MONTH, 1);
						}
						i++;
					}
					
					if (ValidateUtil.isNotNull(operations)) {
						
						for (Operation operation : operations) {
							
							sinceCal.setTime(since);
							for (int j = 1; j <= months ; j++) {
								
								double hours = 0.0;
								if (Constants.ROLE_PMO != user.getResourceprofiles().getIdProfile() || !showAll) {
									
									for (Employee employee : employees) {
										
										hours += timesheetDAO.getHoursResourceInDates(null, employee,
												DateUtil.getFirstMonthDay(sinceCal.getTime()),
												DateUtil.getLastMonthDay(sinceCal.getTime()),
												idResourcePool, operation, null);
									}
								}
								else {
									
									hours = timesheetDAO.getHoursResourceInDates(null, null,
											DateUtil.getFirstMonthDay(sinceCal.getTime()),
											DateUtil.getLastMonthDay(sinceCal.getTime()),
											idResourcePool, operation, null);
								}
								
								// Hours for month in project
								table[i][j+1] = (table[i][j+1] == null ? hours : ((Double)table[i][j+1])+hours);
								
								// Total hours for project
								table[rows-1][j+1] = (table[rows-1][j+1] == null ? hours : ((Double)table[rows-1][j+1])+hours);
								
								// Total hours for month
								table[i][months+2] = (table[i][months+2] == null ? hours : ((Double)table[i][months+2])+hours);
								
								// Total of year
								table[rows-1][months+2] = (table[rows-1][months+2] == null ? hours : ((Double)table[rows-1][months+2])+hours);
								
								// Next Month
								sinceCal.add(Calendar.MONTH, 1);
							}
							i++;
						}
					}
				}
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return table;
	}


	public List<Project> find(String search, Performingorg performingorg, List<String> joins) throws Exception {
		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			list = projDAO.find(search, performingorg, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return list;
	}

	/**
	 * Update tvs
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Project updateTCVs(Project project) throws Exception {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projDAO							= new ProjectDAO(session);
			ChargescostsDAO chargescostsDAO				= new ChargescostsDAO(session);
			WorkingcostsDAO workingcostsDAO				= new WorkingcostsDAO(session);
			ProjectfundingsourceDAO fundingsourceDAO	= new ProjectfundingsourceDAO(session);
			
			double costDolar	= chargescostsDAO.getSumCosts(project, "Dolar");
			double costEuro		= chargescostsDAO.getSumCosts(project, "Euro");
			
			costDolar	+= workingcostsDAO.getSumCosts(project, "Dolar");
			costEuro	+= workingcostsDAO.getSumCosts(project, "Euro");
			
			project = projDAO.findById(project.getIdProject());
			
			project.setCurrencyOptional1(costEuro);
			project.setCurrencyOptional2(costDolar);
			
			project = projDAO.makePersistent(project);
			
			List<Projectfundingsource> list = fundingsourceDAO.findByRelation(Projectfundingsource.PROJECT, project);
			
			for (Projectfundingsource item : list) {
				
				boolean save = false;
				
				if (item.getPercentage() != null && project.getCurrencyOptional1() != null) {
					
					double euros = project.getCurrencyOptional1() * item.getPercentage();
					item.setEuros(euros > 0?euros /100: null);
					
					save = true;
				}
				
				if (item.getPercentage() != null && project.getCurrencyOptional1() != null) {
					
					double dolares = project.getCurrencyOptional2() * item.getPercentage();
					item.setDolares(dolares > 0?dolares /100: null);
					
					save = true;
				}
				
				if (save) { fundingsourceDAO.makePersistent(item); }
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return project;
	}

	/**
	 *  Get setting projects disabled
	 *  
	 * @return
	 */
	@Deprecated
	private boolean projectsDisabled(Session session) {
		
		// FIXME falta company y default --> usar Setting findSetting(Company company, String name) 
		
		boolean projectsDisabled = false;
		
		SettingDAO settingDAO = new SettingDAO(session);
		List<Setting> projectDisableList = settingDAO.findSetting(Settings.SETTING_DISABLE_PROJECT);
		
		if (ValidateUtil.isNotNull(projectDisableList)) {
			projectsDisabled = "true".equals(projectDisableList.get(0).getValue());
		}
		
		return projectsDisabled;
	}
	
	/**
	 * Sum priority by program
	 * @param program
	 * @return
	 * @throws Exception 
	 */
	public Integer sumPriorityByProgram(Program program) throws Exception {
		
		Integer priority = 0;
		
		Transaction tx 	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			ProjectDAO projDAO = new ProjectDAO(session);
			
			// Projects disabled 
			//
			boolean projectsDisabled = projectsDisabled(session);
						
			priority = projDAO.sumPriorityByProgram(program, projectsDisabled);

			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return priority;
	}

	/**
	 * Search projects with list investment status
	 * @param program
	 * @param investmentStatus 
	 * @return
	 * @throws Exception 
	 */
	public List<Project> findByProgramAndInvestmentStatus(Program program, List<String> investmentStatus) throws Exception {
		
		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx 	= null;
		
		try {
			
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			
			boolean projectsDisabled = projectsDisabled(session);
			
			list = projDAO.findByProgramAndInvestmentStatus(program, investmentStatus, projectsDisabled);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * Search projects with list project status
	 *
	 * @param po
	 * @param projectStatus
	 * @return
	 * @throws Exception
	 */
	public List<Project> findByPOAndProjectStatus(Performingorg po, List<String> projectStatus) throws Exception {

		List<Project> list = null;

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx 	= null;

		try {

			tx = session.beginTransaction();

			// Declare DAO
			ProjectDAO projDAO = new ProjectDAO(session);

			boolean projectsDisabled = projectsDisabled(session);

			list = projDAO.findByPOAndProjectStatus(po, projectStatus, projectsDisabled);

			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }

		return list;
	}

	/**
	 * Find projects by filters
	 * 
	 * @param filter
     * @return
	 * @throws Exception
	 */
	public List<ProjectWrap> findProjects(ProjectSearch filter) throws Exception {
		
		List<ProjectWrap> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx 	= null;
		
		try {
			
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);

			list = projDAO.findProjects(filter);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}
	
	/**
	 * Start date of project
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Date getStartDate(Project project) throws Exception {
		
		Date date = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx 	= null;
		
		try {
			
			tx = session.beginTransaction();
			
			getStartDate(project, session);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return date;
	}

	/**
	 * Start date of project
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public Date getStartDate(Project project, Session session) throws Exception {
		Date start = null;
		
		try {
			ProjectActivityDAO activityDAO	= new ProjectActivityDAO(session);
        	Projectactivity rootActivity	= activityDAO.consRootActivity(project);
			
        	if (Constants.STATUS_INITIATING.equals(project.getStatus())) {
				start = project.getPlannedInitDate();
			}
			else if (Constants.STATUS_PLANNING.equals(project.getStatus()) && rootActivity != null) {
				
				if (rootActivity.getPlanInitDate() != null) {
					start = rootActivity.getPlanInitDate();
				}
				else {
					start = project.getPlannedInitDate();
				}
			}
			else {
				
				if (rootActivity.getActualInitDate() != null) {
					start = rootActivity.getActualInitDate();
				}
				else if (rootActivity.getPlanInitDate() != null) {
					start = rootActivity.getPlanInitDate();
				}
				else {
					start = project.getPlannedInitDate();
				}
			}
		}
    	catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
		}
		
		return start != null ? start : new Date();
	}
	
	/**
	 * Finish date of project
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public Date getFinishDate(Project project) throws Exception {
		
		Date date = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx 	= null;
		
		try {
			
			tx = session.beginTransaction();
			
			getFinishDate(project, session);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return date;
	}
	
	/**
	 * Finish date of project
	 * 
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public Date getFinishDate(Project project, Session session) throws Exception {
		Date finish = null;
		
		try {
			ProjectActivityDAO activityDAO	= new ProjectActivityDAO(session);
        	Projectactivity rootActivity	= activityDAO.consRootActivity(project);
			
        	if (Constants.STATUS_INITIATING.equals(project.getStatus())) {
				finish	= project.getPlannedFinishDate();
			}
			else if (Constants.STATUS_PLANNING.equals(project.getStatus()) && rootActivity != null) {
				
				if (rootActivity.getPlanEndDate() != null) {
					finish	= rootActivity.getPlanEndDate();
				}
				else {
					finish	= project.getPlannedFinishDate();
				}
			}
			else {
				
				// Update dates for project in control
				if (rootActivity.getActualEndDate() != null) {
					finish = rootActivity.getActualEndDate();
				}
				else if (rootActivity.getPlanEndDate() != null) {
					finish	= rootActivity.getPlanEndDate();
				}
				else {
					finish	= project.getPlannedFinishDate();
				}
			}
		}
    	catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
		}
		
		return finish != null ? finish : new Date();
	}

	/**
	 * All leads for the projects
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public List<Project> leads(Integer[] ids) throws Exception {

		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			if (ids != null) {
				
				ProjectDAO projDAO = new ProjectDAO(session);
				
				list = projDAO.leads(ids);
			}
			else { list = new ArrayList<Project>(); }
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * All dependents for the projects
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public List<Project> dependents(Integer[] ids) throws Exception {
		
		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			if (ids != null) {
				
				ProjectDAO projDAO = new ProjectDAO(session);
				
				list = projDAO.dependents(ids);
			}
			else { list = new ArrayList<Project>(); }
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;	
	}

	/**
	 * Consult project leads
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public List<Project> consLeads(Project project) throws Exception {
		
		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			if (project != null && project.getIdProject() != null) {
				
				ProjectDAO projDAO = new ProjectDAO(session);
				
				list = projDAO.consLeads(project);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;	
	}
	
	/**
	 * Consult project depedents
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public List<Project> consDependents(Project project) throws Exception {
		
		List<Project> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			if (project != null && project.getIdProject() != null) {
				
				ProjectDAO projDAO = new ProjectDAO(session);
				
				list = projDAO.consDependents(project);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;	
	}

	/**
	 * Find projects by filters for executive report
	 * 
	 * @param filter
	 * @return
	 * @throws Exception 
	 */
	public List<ProjectWrap> findProjectsForExecutiveReport(ProjectSearch filter) throws Exception {
		
		List<ProjectWrap> list = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx 	= null;
		
		try {
			
			tx = session.beginTransaction();
			
			ProjectDAO projDAO = new ProjectDAO(session);
			
			list = projDAO.findProjectsForExecutiveReport(filter);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return list;
	}

	/**
	 * Find by user role and not Initiating project
	 * 
	 * @param user
	 * @param settings
	 * @return
	 * @throws Exception
	 */
	public List<Project> findByUser(Employee user, HashMap<String, String> settings) throws Exception {
		
		// Declare return list
		List<Project> projects = null;
		
		// Open connection
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			// Begin transaction
			tx = session.beginTransaction();
			
			// Declare DAOs
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			// Find by user role and disable or not disable
			projects = projectDAO.findByUser(
					user,
					SettingUtil.getBoolean(settings, Settings.SETTING_DISABLE_PROJECT, Settings.DEFAULT_DISABLE_PROJECT));
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return projects;
	}

	/**
	 * 
	 * @param idProject
	 * @param numCompetitors
	 * @param comments
	 * @param user 
	 * @return 
	 * @throws Exception 
	 */
	public JSONObject approveInvestment(int idProject, int numCompetitors, String comments, Employee user) throws Exception {

        // Only PMO change status of project if setting is activated
        if (Constants.ROLE_PMO != user.getResourceprofiles().getIdProfile()
                && SettingUtil.getBoolean(settings, GeneralSetting.ONLY_PMO_CHANGE_STATUS)) {

            throw new SecurityException("Only PMO change status of project");
        }

		JSONObject infoJSON = new JSONObject();
		Project proj = null;
		
		// Open connection
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			// Begin transaction
			tx = session.beginTransaction();
			
			// Declare DAOs
			ProjectDAO projectDAO 					= new ProjectDAO(session);
			ProgramDAO programDAO 					= new ProgramDAO(session);
			LogprojectstatusDAO logprojectstatusDAO	= new LogprojectstatusDAO(session);
			
			Double totalBudget = 0.0;
			
			if (idProject == -1) {
				throw new ProjectNotFoundException();
			}
			
			proj = projectDAO.findById(idProject, false);
			
			if (SettingUtil.getBoolean(settings,
					Settings.SETTING_PROJECT_EXCEEDED_BUDGET, Settings.DEFAULT_PROJECT_EXCEEDED_BUDGET)) {
				
				Program program 		= programDAO.findById(proj.getProgram().getIdProgram());
				List<Project> projects 	= projectDAO.findByProgram(program, false);
				
				totalBudget += (proj.getTcv() == null ? 0.0 : proj.getTcv());
				
				for (Project project: projects){
					totalBudget += (project.getTcv() == null ? 0.0 : project.getTcv());
				}
				
				Double programBudget = program.getBudget() == null ? 0.0 : program.getBudget();
				
				if (totalBudget > programBudget) {
					Double tempExceed 	= totalBudget - programBudget;
					Integer exceed 		= tempExceed.intValue();
					
					info(getBundle(), StringPool.INFORMATION, "investment.exceed_program_budget", infoJSON, exceed.toString());
				}
			}
			
			proj.setNumCompetitors(numCompetitors);
			proj.setProbability(100);
			proj.setFinalPosition(1);
			proj.setStatus(Constants.STATUS_PLANNING);
			proj.setPlanDate(new Date());
			proj.setClientComments(comments);
			proj.setInvestmentStatus(Constants.INVESTMENT_APPROVED);
			
			this.saveAndUpdateDataProject(proj, session);
			
			Logprojectstatus logprojectstatus = new Logprojectstatus(proj, user, Constants.STATUS_PLANNING, Constants.INVESTMENT_APPROVED, new Date());
			
			logprojectstatusDAO.makePersistent(logprojectstatus);

            // Notifications
            //
            NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(settings);

            // Notification - The project has changed state
            if(SettingUtil.getBoolean(settings, NotificationType.CHANGE_PROJECT_STATUS)) {
                notificationsLogic.changeProjectStatus(session, proj.getIdProject());
            }

            // Notification - Project approval
            if(SettingUtil.getBoolean(settings, NotificationType.PROJECT_APPROVAL)) {
                notificationsLogic.projectApproval(session, proj.getIdProject());
            }

			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return infoJSON;
	}

    /**
     * Close project, only if conditions to close accomplished
     *
     * @param settings
     * @param project
     * @param user
     * @throws Exception
     */
    public void closeProject(HashMap<String, String> settings, Project project, Employee user) throws Exception {

        // Only PMO change status of project if setting is activated
        if (Constants.ROLE_PMO != user.getResourceprofiles().getIdProfile()
                && SettingUtil.getBoolean(settings, GeneralSetting.ONLY_PMO_CHANGE_STATUS)) {

            throw new SecurityException("Only PMO change status of project");
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Declare logics
            NotificationSpecificLogic notificationsLogic 	= new NotificationSpecificLogic(settings);
            TeamMemberLogic memberLogic 					= new TeamMemberLogic(getBundle());

            // Declare DAOs
            ProjectDAO projectDAO			= new ProjectDAO(session);
            ProjectActivityDAO  activityDAO = new ProjectActivityDAO(session);
            LogprojectstatusDAO statusDAO	= new LogprojectstatusDAO(session);
            TeamMemberDAO memberDAO			= new TeamMemberDAO(session);


            if (Boolean.parseBoolean(SettingUtil.getString(settings, Settings.SETTING_PROJECT_CLOSE_CHANGE_PRIORITY, Settings.SETTING_PROJECT_CLOSE_CHANGE_PRIORITY_DEFAULT))) {
                project.setPriority(0);
            }

            project.setStatus(Constants.STATUS_CLOSED);
            project.setInvestmentStatus(Constants.INVESTMENT_CLOSED);
            project.setEndDate(new Date());

            List<Projectactivity> activities = activityDAO.findByProject(project, null);

            // Update activities dates
            for (Projectactivity act : activities) {

                if (act.getActualEndDate() != null) {
                    act.setPlanEndDate(act.getActualEndDate());
                    activityDAO.makePersistent(act);
                }
            }

            // Update dates of project
            project.setStartDate(getStartDate(project, session));
            project.setFinishDate(getFinishDate(project, session));

            projectDAO.makePersistent(project);

            // Update projects leads
            this.updateDatesLeads(session, project);

            // Close date
            Date closeDate = new Date();

            // Log status
            statusDAO.makePersistent(new Logprojectstatus(
                    project, user, Constants.STATUS_CLOSED, Constants.INVESTMENT_CLOSED, closeDate));

            // Update teammembers dates
            List<Teammember> teammembers = memberDAO.findByProjectInDate(project, closeDate);

            StringBuilder errors = new StringBuilder();

            for (Teammember item : teammembers) {

                try {
                    // Update date
                    memberLogic.updateTeamMember(session, settings, item.getIdTeamMember(), null, closeDate, null);
                }
                catch(LogicException e) {

                    // Create errors
                    errors.append((ValidateUtil.isNotNull(errors.toString())?"<br/>":StringPool.BLANK)+e.getMessage());
                }
            }

            // Send all error to front
            if (ValidateUtil.isNotNull(errors.toString())) {
                throw new LogicException(errors.toString());
            }

            // Notification - The project has changed state
            if (SettingUtil.getBoolean(settings, NotificationType.CHANGE_PROJECT_STATUS)) {
                notificationsLogic.changeProjectStatus(session, project.getIdProject());
            }

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }

    /**
     * Archive project
     *
     * @param project
     * @param user
     */
    public void archiveProject(Project project, Employee user) throws Exception {

        // Only PMO change status of project if setting is activated
        if (Constants.ROLE_PMO != user.getResourceprofiles().getIdProfile()
                && SettingUtil.getBoolean(settings, GeneralSetting.ONLY_PMO_CHANGE_STATUS)) {

            throw new SecurityException("Only PMO change status of project");
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Declare DAOs
            ProjectDAO projectDAO			= new ProjectDAO(session);
            LogprojectstatusDAO statusDAO	= new LogprojectstatusDAO(session);

            // Load Project
            project = projectDAO.findById(project.getIdProject());

            // Date for archive
            Date archiveDate = new Date();

            project.setStatus(Constants.STATUS_ARCHIVED);
            project.setInvestmentStatus(Constants.INVESTMENT_ARCHIVED);
            project.setArchiveDate(archiveDate);

            projectDAO.makePersistent(project);

            // Log status
            statusDAO.makePersistent(new Logprojectstatus(
                    project, user, Constants.STATUS_ARCHIVED, Constants.INVESTMENT_ARCHIVED, archiveDate));

            // Notification - The project has changed state
            if (SettingUtil.getBoolean(settings, NotificationType.CHANGE_PROJECT_STATUS)) {

                NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(settings);

                notificationsLogic.changeProjectStatus(session, project.getIdProject());
            }

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }

    /**
     * Change project to closed
     *
     * @param project
     * @param user
     * @throws Exception
     */
    public void changeArchivedToClosed(Project project, Employee user) throws Exception {

        // Only PMO change status of project if setting is activated
        if (Constants.ROLE_PMO != user.getResourceprofiles().getIdProfile()
                && SettingUtil.getBoolean(settings, GeneralSetting.ONLY_PMO_CHANGE_STATUS)) {

            throw new SecurityException("Only PMO change status of project");
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            ProjectDAO projectDAO			= new ProjectDAO(session);
            LogprojectstatusDAO statusDAO	= new LogprojectstatusDAO(session);

            project = projectDAO.findById(project.getIdProject(), false);

            project.setStatus(Constants.STATUS_CLOSED);
            project.setInvestmentStatus(Constants.INVESTMENT_CLOSED);

            projectDAO.makePersistent(project);

            statusDAO.makePersistent(new Logprojectstatus(
                    project,user, Constants.STATUS_CLOSED, Constants.INVESTMENT_CLOSED, new Date()));

            // Update dates of project
            project.setStartDate(getStartDate(project, session));
            project.setFinishDate(getFinishDate(project, session));

            projectDAO.makePersistent(project);

            // Update projects leads
            this.updateDatesLeads(session, project);

            // Notification - The project has changed state
            if (SettingUtil.getBoolean(settings, NotificationType.CHANGE_PROJECT_STATUS)) {

                NotificationSpecificLogic notificationsLogic 	= new NotificationSpecificLogic(settings);
                notificationsLogic.changeProjectStatus(session, project.getIdProject());
            }

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }

    /**
     * Find investments in process and not disabled
     *
     * @return
     * @throws Exception
     */
    public List<Project> findInvestmentsInProcess() throws Exception {
        // Declare return list
        List<Project> projects = null;

        // Open connection
        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {
            // Begin transaction
            tx = session.beginTransaction();

            // Declare DAOs
            ProjectDAO projectDAO = new ProjectDAO(session);

            // DAO
            projects = projectDAO.findInvestmentsInProcess();

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return projects;
    }

    /**
     * Update effort
     *
     * @param project
     * @throws Exception
     */
    public void updateEffort(Project project) throws Exception {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Declare DAO
            ProjectDAO projectDAO = new ProjectDAO(session);

            // DAO update
            projectDAO.updateEffort(project);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }

    /**
     * Update BAC
     *
     * @param project
     */
    public void updateBAC(Project project) throws Exception {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Declare DAO
            ProjectDAO projectDAO = new ProjectDAO(session);

            // DAO update
            projectDAO.updateBAC(project);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }

	public void checkRecalculatePvViability(int projectID) throws Exception{

		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try{

			tx = session.beginTransaction();
			ProjectActivityDAO projectActivityDAO = new ProjectActivityDAO(session);
			ProjectDAO projectDAO = new ProjectDAO(session);

			// JOINS
			List<String> joins = new ArrayList();
			joins.add(Projectactivity.WBSNODE);

			// get project related data (project, activities, BAC, costs)
			Project project = projectDAO.findById(projectID);
			List <Projectactivity> projectActivities = projectActivityDAO.findByProject(project, joins);


			for (Projectactivity projectActivity : projectActivities) {

				if (projectActivity.getWbsnode().getIsControlAccount()) {

					if (projectActivity.getFirstDate() == null || projectActivity.getLastDate() == null) {

						throw new LogicException(getBundle().getString("msg.error.ca_no_date"));
					}
				}
			}



			tx.commit();
		}
	catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
	finally { SessionFactoryUtil.getInstance().close(); }
	}



    /**
     * Find by search
     *
     * @param searchParams
     * @param searchByRole
     * @return
     * @throws Exception
     */
    public List<Project> find(ProjectSearch searchParams, Boolean searchByRole) throws Exception {

        // Declare return list
        List<Project> projects = null;

        // Open connection
        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {
            // Begin transaction
            tx = session.beginTransaction();

            // Filter by rol
            if (searchByRole) {

                if (isUserInRole(searchParams, Constants.ROLE_PMO) ||
                        isUserInRole(searchParams, Constants.ROLE_FM) ||
                        isUserInRole(searchParams, Constants.ROLE_LOGISTIC)) {	// PMO or Functional Manager or Logistic

                    searchParams.setPerformingorgs(DataUtil.toList(searchParams.getUser().getPerformingorg().getIdPerfOrg()));
                }
                else if (isUserInRole(searchParams, Constants.ROLE_PORFM) ||
                        isUserInRole(searchParams, Constants.ROLE_ADMIN)) {	// Porfolio Manager

                    searchParams.setCompany(searchParams.getUser().getContact().getCompany());
                }
                else if (isUserInRole(searchParams, Constants.ROLE_IM)) {	// Investment Manager

                    searchParams.setEmployeeByInvestmentManager(searchParams.getUser());
                }
                else if (isUserInRole(searchParams, Constants.ROLE_PM)) {	// Project Manager

                    searchParams.setEmployeeByProjectManagers(DataUtil.toList(searchParams.getUser().getIdEmployee()));
                }
                else if (isUserInRole(searchParams, Constants.ROLE_SPONSOR)) {	// Sponsor

                    searchParams.setEmployeeBySponsors(DataUtil.toList(searchParams.getUser().getIdEmployee()));
                }
                else if (isUserInRole(searchParams, Constants.ROLE_STAKEHOLDER)) {	// Stakeholder

                    searchParams.setStakeholder(searchParams.getUser());
                }
                else if (isUserInRole(searchParams, Constants.ROLE_PROGM)) {	// Program Manager

                    searchParams.setProgramManager(searchParams.getUser());
                }
            }


            // Declare DAOs
            ProjectDAO projectDAO = new ProjectDAO(session);


            // Find by user role and disable or not disable
            projects = projectDAO.find(searchParams);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            SessionFactoryUtil.getInstance().close(); }

        return projects;
    }

    /**
     * Check if role match
     *
     * @param searchParams
     * @param rolePmo
     * @return
     */
    private boolean isUserInRole(ProjectSearch searchParams, int rolePmo) {

        return searchParams.getUser().getResourceprofiles().getIdProfile() == rolePmo;
    }
}