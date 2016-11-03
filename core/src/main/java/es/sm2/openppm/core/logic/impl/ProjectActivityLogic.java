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
 * File: ProjectActivityLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.ProjectActivityDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.exceptions.ImputedHoursException;
import es.sm2.openppm.core.exceptions.NoDataFoundException;
import es.sm2.openppm.core.exceptions.ProjectNotFoundException;
import es.sm2.openppm.core.exceptions.ProjectDateException;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import java.util.*;

public class ProjectActivityLogic extends AbstractGenericLogic<Projectactivity, Integer> {

	private Map<String, String> settings;
	
	public ProjectActivityLogic(Map<String, String> settings, ResourceBundle bundle) {
		
		super(bundle);
		this.settings = settings;
	}
	
	/**
	 * Save activity. If project status is P, actual dates are updated with planned dates.
	 *
     * @param session
     * @param activity
	 * @param role
     * @throws Exception
	 */
	public void savePlanActivity (Session session, Projectactivity activity, Resourceprofiles role) throws Exception {
		
		ProjectLogic projectLogic		= new ProjectLogic(settings, getBundle());
		ProjectActivityDAO activityDAO 	= new ProjectActivityDAO(session);
		ProjectDAO projectDAO			= new ProjectDAO(session);
		
		Project project = activity.getProject();

		if (activity.getIdActivity() == -1) {
			throw new NoDataFoundException();
		}
		if (activity.getPlanEndDate() != null && activity.getPlanInitDate() != null &&
                activity.getPlanEndDate().before(activity.getPlanInitDate())) {

			throw new ProjectDateException("msg.error.start_before_finish");
		}
		if (project == null) {
			throw new ProjectNotFoundException();
		}

		if (activityDAO.checkAfterActivityInputed(activity)) {
			// Hours have been allocated in a previous activity to different modified, 
			// can not allocate hours to days to hours and assigned
			throw new ImputedHoursException();
		}

		activity = activityDAO.makePersistent(activity);
		
		if (activity.getPoc() != null && activity.getPoc() >= 100 && activity.getActualEndDate() != null) {
			activity.setPlanEndDate(activity.getActualEndDate());
			activity = activityDAO.makePersistent(activity);
		}
		
		Projectactivity rootActivity = activityDAO.consRootActivity(project);

        // Update root dates
        //
		activityDAO.updatePlannedDates(project, rootActivity);
        activityDAO.updateActualDates(project, rootActivity);
		
		// Update dates of project
		project.setStartDate(projectLogic.getStartDate(project, session));
		project.setFinishDate(projectLogic.getFinishDate(project, session));
		
		projectDAO.makePersistent(project);
		
		// Update projects leads
		projectLogic.updateDatesLeads(session, project);
	}
	
	/**
	 * Save control activity, only save EV
	 * @param activity
	 * @throws Exception 
	 */
	public void saveControlActivity (Project project, Projectactivity activity) throws Exception {
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ProjectActivityDAO activityDAO 	= new ProjectActivityDAO(session);
			
			activity = activityDAO.makePersistent(activity);
			
			Projectactivity rootActivity = activityDAO.consRootActivity(project);
			
			if (!activity.equals(rootActivity)) {
				activityDAO.updateActualDates(project, rootActivity);
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
	 * Save control activity, only save EV
	 * 
	 * @param activity
	 * @param session
	 * @throws Exception 
	 */
	public void saveControlActivity (Project project, Projectactivity activity, Session session) throws Exception {
		
		ProjectActivityDAO activityDAO 	= new ProjectActivityDAO(session);
		
		activity = activityDAO.makePersistent(activity);
		
		Projectactivity rootActivity = activityDAO.consRootActivity(project);
		
		if (!activity.equals(rootActivity)) {
			activityDAO.updateActualDates(project, rootActivity);
		}
	}
	
	/**
	 * Return project activity by id
	 * @param idActivity
	 * @return
	 * @throws Exception 
	 */
	public Projectactivity consActivity (Integer idActivity) throws Exception {
		
		Projectactivity activity = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ProjectActivityDAO activitiesDAO = new ProjectActivityDAO(session);
			
			if (idActivity != -1) {
				activity =  activitiesDAO.findById(idActivity, false);
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
		return activity;
	}
	
	
	/**
	 * Search activitys by project
	 * @param proj
	 * @return
	 * @throws Exception 
	 */
	public List<Projectactivity> consActivities(Project proj, List<String> joins) throws Exception {
		
		List<Projectactivity> listActivitys = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
			listActivitys = activityDAO.findByProject(proj, joins);
			
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
		return listActivitys;
	}
	
	
	/**
	 * Cons Root Activity By Project
	 * @param proj
	 * @return
	 * @throws Exception
	 */
	public Projectactivity consRootActivity(Project proj) throws Exception {
		
		Projectactivity activity = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
			activity = activityDAO.consRootActivity(proj);
			
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
		return activity;
	}
	
	/**
	 * List not assigned activities to seller
	 * 	
	 * @param seller
	 * @param project
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> consNoAssignedActivities(Seller seller, Project project, Order... order) throws Exception {
		
		List<Object[]> listActivitys = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
			listActivitys = activityDAO.consNoAssignedActivities(seller, project, order);
			
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
		return listActivitys;
	}

	public Double calcPoc(Projectactivity projectactivity) throws Exception {
		
		Double poc = null;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
			
			poc = activityDAO.calcPoc(projectactivity, settings);
			
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
		return poc;
	}

	/**
	 * Find By Redmine
	 * @param accountingCode
	 * @param wbsnodeCode
	 * @return
	 * @throws Exception 
	 */
	public Projectactivity findByRedMine(String accountingCode,
			String wbsnodeCode) throws Exception {
		
		Projectactivity activity = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
			activity = activityDAO.findByRedMine(accountingCode, wbsnodeCode);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return activity;
	}


	/**
	 * Update Actual Dates of root activity
	 * @param project
	 * @param rootActivity
	 * @throws Exception 
	 */
	public void updateActualDates(Project project, Projectactivity rootActivity) throws Exception {
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
			activityDAO.updateActualDates(project, rootActivity);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

	/**
	 * Find activity by wbsnode
	 * 
	 * @param project
	 * @param idWbsnode
	 * @return
	 * @throws Exception 
	 */
	public Projectactivity findByWBSnode(Project project, Integer idWbsnode) throws Exception {
		
		Projectactivity activity = null;
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
			activity = activityDAO.findByWBSnode(project, idWbsnode);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		return activity;
	}

	/**
	 * Save activity in control
	 * 
	 * @param idProject
	 * @param idActivity
	 * @param actualInitDate
	 * @param actualEndDate
	 * @param commentsDates
	 * @throws Exception 
	 */
	public void saveActivityInControl(Integer idProject, Integer idActivity, Date actualInitDate, Date actualEndDate, String commentsDates) throws Exception {
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			Project project 				= null;
			Projectactivity activity 		= null;
			Calendar finishActualDateOld 	= DateUtil.getCalendar();
			Calendar finishPlannedDateOld 	= DateUtil.getCalendar();
			Calendar finishActualDateNew 	= DateUtil.getCalendar();
			Calendar finishPlannedDateNew 	= DateUtil.getCalendar();
			
			// Declare logic
			ProjectLogic projectLogic 					 	= new ProjectLogic(settings, getBundle());
			NotificationSpecificLogic notificationsLogic 	= new NotificationSpecificLogic(settings);

			// Declare DAOs
			ProjectActivityDAO activityDAO 	= new ProjectActivityDAO(session);
			ProjectDAO projectDAO			= new ProjectDAO(session);
			
			// Consult activity
			if (idActivity != null) {
				activity = activityDAO.findById(idActivity, false);
			}
			
			// Set old dates for notifications delay project
			//
			boolean updateDatesProject = false;
			if ((SettingUtil.getBoolean(settings, NotificationType.PROJECT_DELAY)
								|| SettingUtil.getBoolean(settings, NotificationType.PROJECT_NOT_DELAY))
						&& activity != null 
						&& ((activity.getActualEndDate() == null && actualEndDate != null)	
								|| (activity.getActualEndDate() != null && actualEndDate != null 
									&& !DateUtil.equals(activity.getActualEndDate(), actualEndDate)))) {
				
				// Consult project
				if (idProject != null) {
					project = projectDAO.findById(idProject, false);
				}
				
				// Set finishdate old and plannedfinishdate old
				if (project != null && project.getFinishDate() != null && project.getCalculatedPlanFinishDate() != null) {
					
					finishActualDateOld.setTime(project.getFinishDate());
					finishPlannedDateOld.setTime(project.getCalculatedPlanFinishDate());
					
					updateDatesProject = true;
				}
			}
			
			//Notification - Notification change control dates
			boolean updateDatesActivity = false;
			if (SettingUtil.getBoolean(settings, NotificationType.CHANGE_CONTROL_DATES)
					&& activity != null 
					&& (activity.getActualInitDate() != null || activity.getActualEndDate() != null)
					&& (actualInitDate != null || actualEndDate != null)) {
				
				Calendar initDateOld 	= DateUtil.getCalendar();
				Calendar endDateOld 	= DateUtil.getCalendar();
				Calendar initDateNew 	= DateUtil.getCalendar();
				Calendar endDateNew 	= DateUtil.getCalendar();
				
				if (activity.getActualInitDate() != null && actualInitDate != null) {
					
					initDateOld.setTime(activity.getActualInitDate());
					initDateNew.setTime(actualInitDate);
					
					if (!initDateOld.equals(initDateNew)) {
						updateDatesActivity = true;
					}
				}
				
				if (activity.getActualEndDate() != null && actualEndDate != null) {
					
					endDateOld.setTime(activity.getActualEndDate());
					endDateNew.setTime(actualEndDate);
					
					if (!endDateOld.equals(endDateNew)) {
						updateDatesActivity = true;
					}
				}
				
				if (updateDatesActivity) {
					// Consult project
					if (idProject != null) {
						project = projectDAO.findById(idProject, false);
					}
					
					if (project != null && project.getIdProject() != null && Constants.STATUS_CONTROL.equals(project.getStatus())) {
						notificationsLogic.changeControlDates(session, project.getIdProject(), activity.getActivityName());
					}
				}
			}
			
			// Set activity
			activity.setActualInitDate(actualInitDate);
			activity.setActualEndDate(actualEndDate);
			activity.setCommentsDates(commentsDates);
			
			this.saveControlActivity(new Project(idProject), activity, session);
			
			// Update project
			project = projectLogic.updateDataProject(idProject, session);
			
			// Notifications
			if (updateDatesProject && project != null && project.getFinishDate() != null && project.getCalculatedPlanFinishDate() != null) {
				
				// Set new dates for notifications
				finishActualDateNew.setTime(project.getFinishDate());
				finishPlannedDateNew.setTime(project.getCalculatedPlanFinishDate());
				
				// Notification - The actual finish date is greater baseline finish date
				//
				if (SettingUtil.getBoolean(settings, NotificationType.PROJECT_DELAY) 
						&& (finishActualDateOld.before(finishPlannedDateOld) || finishActualDateOld.equals(finishPlannedDateOld))
						&& finishActualDateNew.after(finishPlannedDateNew)) {
					
					notificationsLogic.projectDelay(session, project.getIdProject());
				}
				
				// Notification - Notification correct advance
				//
				if (SettingUtil.getBoolean(settings, NotificationType.PROJECT_NOT_DELAY) 
						&& finishActualDateOld.after(finishPlannedDateOld)
						&& (finishActualDateNew.before(finishPlannedDateNew) || finishActualDateNew.equals(finishPlannedDateNew))) {
					
					notificationsLogic.projectNotDelay(session, project.getIdProject());
				}
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}

    /**
     * Save activity in plan
     *
     * @param activity
     * @param role
     * @throws Exception
     */
	public void saveActivityInPlan(Projectactivity activity, Resourceprofiles role) throws Exception {

		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
		
			Calendar initDateOld 	= DateUtil.getCalendar();
			Calendar endDateOld 	= DateUtil.getCalendar();
			Calendar initDateNew 	= DateUtil.getCalendar();
			Calendar endDateNew 	= DateUtil.getCalendar();
			boolean updateDates		= false;
			Project project 		= null; 
			
			NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(settings);
			
			// Declare DAOs
			ProjectActivityDAO projectActivityDAO 	= new ProjectActivityDAO(session);
			ProjectDAO projectDAO 					= new ProjectDAO(session);
			
			if (activity.getIdActivity() == -1) {
				throw new NoDataFoundException();
			}
			
			Projectactivity activityOld = projectActivityDAO.findById(activity.getIdActivity(), false);
			
			//Notification - Change planned dates
			if (SettingUtil.getBoolean(settings, NotificationType.CHANGE_PLANNED_DATES)
					&& activityOld != null
					&& activityOld.getPlanInitDate() != null && activityOld.getPlanEndDate() != null
					&& activity.getPlanInitDate() != null && activity.getPlanEndDate() != null) {
				
				initDateOld.setTime(activityOld.getPlanInitDate());
				endDateOld.setTime(activityOld.getPlanEndDate());
				
				initDateNew.setTime(activity.getPlanInitDate());
				endDateNew.setTime(activity.getPlanEndDate());
				
				if (!initDateOld.equals(initDateNew) || !endDateOld.equals(endDateNew)) {
					
					if (activityOld.getProject() != null && activityOld.getProject().getIdProject() != -1) {
						project = projectDAO.findById(activityOld.getProject().getIdProject(), false);
					}
					
					if (project != null && project.getIdProject() != null && Constants.STATUS_CONTROL.equals(project.getStatus())) {
						updateDates = true;
					}
				}
			}

            // Set activity
            //
			activityOld.setPlanInitDate(activity.getPlanInitDate());
			activityOld.setPlanEndDate(activity.getPlanEndDate());

            // Update actual dates with plan dates
            activityOld.setActualInitDate(activity.getPlanInitDate());
            activityOld.setActualEndDate(activity.getPlanEndDate());

            activityOld.setWbsdictionary(activity.getWbsdictionary());
			activityOld.setPv(activity.getPv());

            // Update activity
            //
			this.savePlanActivity(session, activityOld, role);

            // Update project
            //
            ProjectLogic projectLogic = new ProjectLogic(settings, getBundle());
            project = projectLogic.updateDataProject(activityOld.getProject().getIdProject(), session);

			// Notification - Create change planned dates
			//
			if (updateDates && project != null && activityOld != null) {
				notificationsLogic.changePlannedDates(session, project.getIdProject(), activityOld.getActivityName());
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
	}

    /**
     *
     *
     * @param project
     * @param projectactivity
     * @param rootActivity
     * @return
     */
    public Projectactivity findActivityMaxEndDate(Project project, Projectactivity projectactivity, Projectactivity rootActivity) throws Exception {

        Projectactivity activity = null;

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {
            tx = session.beginTransaction();

            ProjectActivityDAO activityDAO = new ProjectActivityDAO(session);
            activity = activityDAO.findActivityMaxEndDate(project, projectactivity, rootActivity);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }

        return activity;
    }

    /**
     * Update root dates
     *
     * @param idProject
     */
    public void updateRootDates(Integer idProject) throws Exception {

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();

        try {
            tx = session.beginTransaction();

            // Declare DAO
            ProjectActivityDAO activityDAO  = new ProjectActivityDAO(session);
            ProjectDAO projectDAO           = new ProjectDAO(session);

            // Get project
            Project project = projectDAO.findById(idProject);

            // Get root activity
            Projectactivity rootActivity = activityDAO.consRootActivity(project);

            // Update dates
            activityDAO.updatePlannedDates(project, rootActivity);
            activityDAO.updateActualDates(project, rootActivity);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }
}
