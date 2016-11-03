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
 * File: NotificationSpecificLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Stagegate;
import es.sm2.openppm.core.model.impl.Timesheet;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rendersnake.HtmlCanvas;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.EmployeeDAO;
import es.sm2.openppm.core.dao.MilestoneDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.core.model.impl.Resourceprofiles.Profile;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;

public class NotificationSpecificLogic {

	private static final Logger LOGGER = Logger.getLogger(NotificationSpecificLogic.class);
	
	private ResourceBundle resourceBundle;
	
	public NotificationSpecificLogic() {
		this.resourceBundle = ResourceBundle.getBundle("es.sm2.openppm.front.common.openppm", Constants.DEF_LOCALE);
	}

	public NotificationSpecificLogic(Map<String, String> settings) {

        LOGGER.info("\tLanguage notifications: "+ SettingUtil.getString(settings, Settings.SettingType.LOCALE));

        String[] locale = SettingUtil.getString(settings, Settings.SettingType.LOCALE).split(StringPool.UNDERLINE);

        this.resourceBundle = ResourceBundle.getBundle("es.sm2.openppm.front.common.openppm",  new Locale(locale[0], locale[1]));
	}
	
	/**
	 * Create notification if change project status
	 * 
	 * @param idProject
	 * @throws Exception 
	 */
	public void changeProjectStatus(Integer idProject) throws Exception{
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			changeProjectStatus(session, idProject);
						
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
	 * Create notification if change project phase
	 * 
	 * @param idProject
	 * @param stageGateOld
	 * @throws Exception
	 */
	public void changeStageGate(Integer idProject, Stagegate stageGateOld) throws Exception{
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			changeStageGate(session, idProject, stageGateOld);
						
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
     * Create notification if change project geography
     *
     * @param idProject
     * @param geographyOld
     * @throws Exception
     */
    public void changeGeography(Integer idProject, Geography geographyOld) throws Exception {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;

        try {

            tx = session.beginTransaction();

            changeGeography(session, idProject, geographyOld);

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
     * Create notification assigned project
     *
     * @param idProject
     */
    public void assignedProject(Integer idProject) throws Exception {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;

        try {

            tx = session.beginTransaction();

            assignedProject(session, idProject);

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
     *  Create notification if project approval
     *
     * @param session
     * @param idProject
     * @throws Exception
     */
    public void projectApproval(Session session, Integer idProject) throws Exception {

        if (idProject != null) {

            // Declare DAOs
            ProjectDAO projectDAO = new ProjectDAO(session);

            // Set roles
            //
            List<String> roles = new ArrayList<String>();
            roles.add(Profile.PROJECT_MANAGER.toString());

            // Get project
            Project project = projectDAO.findById(idProject, null);

            if (project != null) {

                NotificationLogic notificationLogic = new NotificationLogic();

                // Set subject
                String subject 	= new ParamResourceBundle(NotificationType.PROJECT_APPROVAL.getSubject(),
                        project.getProjectName()).toString(resourceBundle);

                // Set body
                String body		= new ParamResourceBundle(NotificationType.PROJECT_APPROVAL.getBody(),
                        createStrongTextWithHTML(project.getProjectName())).toString(resourceBundle);

                // Set contacts
                //
                Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();

                contactsHash = setContacts(session, project, roles);

                // Create notifications
                notificationLogic.createNotification(session, subject, body, NotificationType.PROJECT_APPROVAL, new ArrayList<Contact>(contactsHash.values()));
            }
        }
    }

	/**
	 * Create notification if change project status
	 * 
	 * @param idProject
	 * @throws Exception 
	 */
	public void changeProjectStatus(Session session, Integer idProject) throws Exception {
		
		if (idProject != null) {
			
			// Declare DAOs
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			// Set roles 
			//
			List<String> roles = new ArrayList<String>();
			
			roles.add(Profile.PROJECT_MANAGER.toString());
			roles.add(Profile.INVESTMENT_MANAGER.toString());
			roles.add(Profile.FUNCTIONAL_MANAGER.toString());
			roles.add(Profile.PROGRAM_MANAGER.toString());
			roles.add(Profile.PMO.toString());
			
			// Get project
			Project project = projectDAO.findById(idProject);
			
			if (project != null) {
				
				NotificationLogic notificationLogic = new NotificationLogic();
				
				// Set subject
				String subject 	= new ParamResourceBundle(NotificationType.CHANGE_PROJECT_STATUS.getSubject(), project.getProjectName()).toString(resourceBundle);
				
				// Set body
				String body		= new ParamResourceBundle(NotificationType.CHANGE_PROJECT_STATUS.getBody(),
						createStrongTextWithHTML(project.getProjectName()), 
						createStrongTextWithHTML(resourceBundle.getString("project_status."+project.getStatus()))).toString(resourceBundle);
				
				// Set contacts
				//
				Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();
				
				contactsHash = setContacts(session, project, roles);
				
				// Create notifications
				notificationLogic.createNotification(session, subject, body, NotificationType.CHANGE_PROJECT_STATUS, 
						new ArrayList<Contact>(contactsHash.values()));
			}
		}
	}
	
	/**
	 * Create notification if change project phase
	 * 
	 * @param idProject
	 * @param stageGateOld
	 * @throws Exception 
	 */
	public void changeStageGate(Session session, Integer idProject, Stagegate stageGateOld) throws Exception {
		
		if (idProject != null) {
			
			// Declare DAOs
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			// Set roles 
			//
			List<String> roles = new ArrayList<String>();
			
			roles.add(Profile.PROJECT_MANAGER.toString());
			roles.add(Profile.INVESTMENT_MANAGER.toString());
			roles.add(Profile.PMO.toString());
			
			// Get project
			Project project = projectDAO.findById(idProject, null);

			if (project != null) {
				
				NotificationLogic notificationLogic = new NotificationLogic();
				
				// Set subject
				String subject 	= new ParamResourceBundle(NotificationType.CHANGE_STAGE_GATE.getSubject(), 
						project.getProjectName()).toString(resourceBundle);
				
				// Set body
				String body		= new ParamResourceBundle(NotificationType.CHANGE_STAGE_GATE.getBody(), 
						createStrongTextWithHTML(project.getProjectName()), 
						createStrongTextWithHTML(stageGateOld.getName()), 
						createStrongTextWithHTML(project.getStagegate().getName())).toString(resourceBundle);
				
				// Set contacts
				//
				Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();
				
				contactsHash = setContacts(session, project, roles);
				
				// Create notifications
				notificationLogic.createNotification(session, subject, body, NotificationType.CHANGE_STAGE_GATE, new ArrayList<Contact>(contactsHash.values()));
			}
		}
	}

    /**
     * Create notification assigned project
     *
     * @param session
     * @param idProject
     * @throws Exception
     */
    public void assignedProject(Session session, Integer idProject) throws Exception {

        if (idProject != null) {

            // Declare DAOs
            ProjectDAO projectDAO = new ProjectDAO(session);

            // Set roles
            //
            List<String> roles = new ArrayList<String>();
            roles.add(Profile.PROJECT_MANAGER.toString());

            // Get project
            Project project = projectDAO.findById(idProject, null);

            if (project != null) {

                NotificationLogic notificationLogic = new NotificationLogic();

                // Set subject
                String subject 	= new ParamResourceBundle(NotificationType.ASSIGNMENT_PROJECT.getSubject(),
                        project.getProjectName()).toString(resourceBundle);

                // Set body
                String body		= new ParamResourceBundle(NotificationType.ASSIGNMENT_PROJECT.getBody(),
                        createStrongTextWithHTML(project.getProjectName())).toString(resourceBundle);

                // Set contacts
                //
                Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();

                contactsHash = setContacts(session, project, roles);

                // Create notifications
                notificationLogic.createNotification(session, subject, body, NotificationType.ASSIGNMENT_PROJECT, new ArrayList<Contact>(contactsHash.values()));
            }
        }
    }

    /**
     * Create notification if change project geography
     *
     * @param idProject
     * @param geographyOld
     * @throws Exception
     */
    public void changeGeography(Session session, Integer idProject, Geography geographyOld) throws Exception {

        if (idProject != null) {

            // Declare DAOs
            ProjectDAO projectDAO = new ProjectDAO(session);

            // Set roles
            //
            List<String> roles = new ArrayList<String>();
            roles.add(Profile.PMO.toString());

            // Get project
            Project project = projectDAO.findById(idProject, null);

            if (project != null) {

                NotificationLogic notificationLogic = new NotificationLogic();

                // Set subject
                String subject 	= new ParamResourceBundle(NotificationType.CHANGE_GEOGRAPHY.getSubject(),
                        project.getProjectName()).toString(resourceBundle);

                // Set body
                String body		= new ParamResourceBundle(NotificationType.CHANGE_GEOGRAPHY.getBody(),
                        createStrongTextWithHTML(project.getProjectName()),
                        createStrongTextWithHTML(geographyOld.getName()),
                        createStrongTextWithHTML(project.getGeography().getName())).toString(resourceBundle);

                // Set contacts
                //
                Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();

                contactsHash = setContacts(session, project, roles);

                // Create notifications
                notificationLogic.createNotification(session, subject, body, NotificationType.CHANGE_GEOGRAPHY, new ArrayList<Contact>(contactsHash.values()));
            }
        }
    }

	/**
	 * Create notification if project delay (if actual finish is greater baseline finish)
	 * 
	 * @param idProject
	 * @param session
	 * @throws Exception 
	 */
	public void projectDelay(Session session, Integer idProject) throws Exception {
		
		if (idProject != null) {
			
			// Declare logics
			NotificationLogic notificationLogic = new NotificationLogic();
			
			// Declare DAOs
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			// Set roles 
			//
			List<String> roles = new ArrayList<String>();
			
			roles.add(Profile.PROJECT_MANAGER.toString());
			roles.add(Profile.FUNCTIONAL_MANAGER.toString());
			roles.add(Profile.PMO.toString());
			
			// Joins
			List<String> joins = addJoins(roles);

			// Get project
			Project project = projectDAO.consProject(new Project(idProject), joins);
			
			if (project != null) {
				
				// Set subject
				String subject 	= new ParamResourceBundle(NotificationType.PROJECT_DELAY.getSubject(), project.getProjectName()).toString(resourceBundle);
				
				// Set body
				String body		= resourceBundle.getString(NotificationType.PROJECT_DELAY.getBody());
				
				// Set contacts
				//
				Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();
				
				contactsHash = setContacts(session, project, roles);
				
				// Create notifications
				notificationLogic.createNotification(session, subject, body, NotificationType.PROJECT_DELAY, new ArrayList<Contact>(contactsHash.values()));
			}
		}
	}
	
	/**
	 * Create notification if project correct advance
	 * 
	 * @param idProject
	 * @param session
	 * @throws Exception 
	 */
	public void projectNotDelay(Session session, Integer idProject) throws Exception {
		
		if (idProject != null) {
			
			// Declare DAOs
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			// Set roles 
			//
			List<String> roles = new ArrayList<String>();
			
			roles.add(Profile.PROJECT_MANAGER.toString());
			roles.add(Profile.FUNCTIONAL_MANAGER.toString());
			roles.add(Profile.PMO.toString());
			
			// Joins
			List<String> joins = addJoins(roles);

			// Get project
			Project project = projectDAO.consProject(new Project(idProject), joins);
			
			if (project != null) {
				
				NotificationLogic notificationLogic = new NotificationLogic();
				
				// Set subject
				String subject 	= new ParamResourceBundle(NotificationType.PROJECT_NOT_DELAY.getSubject(), project.getProjectName()).toString(resourceBundle);
				
				// Set body
				String body		= resourceBundle.getString(NotificationType.PROJECT_NOT_DELAY.getBody());
				
				// Set contacts
				//
				Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();
				
				contactsHash = setContacts(session, project, roles);
				
				// Create notifications
				notificationLogic.createNotification(session, subject, body, NotificationType.PROJECT_NOT_DELAY, new ArrayList<Contact>(contactsHash.values()));
			}
		}
	}
	
	/**
	 * Create notification if change planned dates of project
	 * 
	 * @param session
	 * @param idProject
	 * @param activityName
	 * @throws Exception
	 */
	public void changePlannedDates(Session session, Integer idProject, String activityName) throws Exception {
		
		if (idProject != null) {
			
			// Declare logics
			NotificationLogic notificationLogic = new NotificationLogic();
			
			// Declare DAOs
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			// Set roles 
			//
			List<String> roles = new ArrayList<String>();
			
			roles.add(Profile.PMO.toString());
			
			// Joins
			List<String> joins = addJoins(roles);

			// Get project
			Project project = projectDAO.consProject(new Project(idProject), joins);
			
			if (project != null) {
				
				// Set subject
				String subject 	= new ParamResourceBundle(NotificationType.CHANGE_PLANNED_DATES.getSubject(), project.getProjectName()).toString(resourceBundle);
				
				activityName = ValidateUtil.isNotNull(activityName) ? activityName : StringPool.BLANK;
				
				// Set body
				String body		= new ParamResourceBundle(NotificationType.CHANGE_PLANNED_DATES.getBody(), createStrongTextWithHTML(project.getProjectName()), 
						createStrongTextWithHTML(activityName)).toString(resourceBundle);
				
				// Set contacts
				//
				Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();
				
				contactsHash = setContacts(session, project, roles);
				
				// Create notifications
				notificationLogic.createNotification(session, subject, body, NotificationType.CHANGE_PLANNED_DATES, new ArrayList<Contact>(contactsHash.values()));
			}
		}
	}
	
	/**
	 * Create notification if change control dates of project
	 * 
	 * @param session
	 * @param idProject
	 * @param activityName
	 * @throws Exception
	 */
	public void changeControlDates(Session session, Integer idProject, String activityName) throws Exception {
		
		if (idProject != null) {
			
			// Declare logics
			NotificationLogic notificationLogic = new NotificationLogic();
			
			// Declare DAOs
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			// Set roles 
			//
			List<String> roles = new ArrayList<String>();
			
			roles.add(Profile.PMO.toString());
			
			// Joins
			List<String> joins = addJoins(roles);
			
			// Get project
			Project project = projectDAO.consProject(new Project(idProject), joins);
			
			if (project != null) {
				
				// Set subject
				String subject 	= new ParamResourceBundle(NotificationType.CHANGE_CONTROL_DATES.getSubject(), project.getProjectName()).toString(resourceBundle);
				
				// Set body
				String body		= new ParamResourceBundle(NotificationType.CHANGE_CONTROL_DATES.getBody(), createStrongTextWithHTML(project.getProjectName()), createStrongTextWithHTML(activityName)).toString(resourceBundle);
				
				// Set contacts
				//
				Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();
				
				contactsHash = setContacts(session, project, roles);
				
				// Create notifications
				notificationLogic.createNotification(session, subject, body, NotificationType.CHANGE_CONTROL_DATES, new ArrayList<Contact>(contactsHash.values()));
			}
		}
	}
	
	/**
	 * Create notification to milestones
	 * 
	 * @throws Exception
	 */
	public void milestones() throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
		
			// Declare logics
			NotificationLogic notificationLogic = new NotificationLogic();
			
			// Declare DAOs
			MilestoneDAO milestoneDAO = new MilestoneDAO(session);
			
			// Get milestones
			List<Milestones> milestones = milestoneDAO.findForNotify();
			
			// Set roles 
			//
			List<String> roles = new ArrayList<String>();

			if (ValidateUtil.isNotNull(milestones) && milestones.get(0).getProject() != null &&
					milestones.get(0).getProject().getPerformingorg().getCompany() != null &&
                    (milestones.get(0).getProject().getDisable() == null || !milestones.get(0).getProject().getDisable())) {
				
				// Get settings
				HashMap<String, String> settings = SettingUtil.getSettings(session, milestones.get(0).getProject().getPerformingorg().getCompany());

                // TODO 28/10/2014 jordi ripoll el idioma de las notificaciones se modificara en el nuevo desarrollo
                // Set bundle
                //
                String[] locale = SettingUtil.getString(settings, Settings.SettingType.LOCALE).split(StringPool.UNDERLINE);

                resourceBundle = ResourceBundle.getBundle("es.sm2.openppm.front.common.openppm",  new Locale(locale[0], locale[1]));

                // Set role
                roles.add(Profile.PROJECT_MANAGER.toString());

                // Set another role by check
				if (SettingUtil.getBoolean(settings, NotificationType.MILESTONE_PMO)) {
					roles.add(Profile.PMO.toString());
				}

                for (Milestones milestone : milestones) {

                    // Set subject
                    String subject 	= new ParamResourceBundle(NotificationType.MILESTONE.getSubject(),
                            ValidateUtil.isNotNull(milestone.getName()) ? milestone.getName() : StringPool.BLANK,
                            milestone.getProject().getProjectName()).toString(resourceBundle);

                    // Set body
                    String body = new ParamResourceBundle(NotificationType.MILESTONE.getBody(),
                            createStrongTextWithHTML(ValidateUtil.isNotNull(milestone.getName()) ? milestone.getName() : StringPool.BLANK),
                            createStrongTextWithHTML(ValidateUtil.isNotNull(milestone.getNotificationText()) ? milestone.getNotificationText() : StringPool.BLANK)).toString(resourceBundle);

                    // Set contacts
                    //
                    Hashtable<Integer, Contact> contactsHash = setContacts(session, milestone.getProject(), roles);

                    // Create notifications
					if (Constants.STATUS_CONTROL.equals(milestone.getProject().getStatus())) {
						notificationLogic.createNotification(session, subject, body, NotificationType.MILESTONE, new ArrayList<Contact>(contactsHash.values()));
					}
                }
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
	 * Create notification if delete milestone from plan tab
	 * 
	 * @param session
	 * @param milestone 
	 * @param idProject
	 * @param activityName
	 * @throws Exception 
	 */
	public void deleteMilestone(Session session, Milestones milestone, Integer idProject, String activityName) throws Exception {
		
		if (idProject != null) {
			
			// Declare logics
			NotificationLogic notificationLogic = new NotificationLogic();
			
			// Declare DAOs
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			// Set roles 
			//
			List<String> roles = new ArrayList<String>();
			
			roles.add(Profile.PMO.toString());
			roles.add(Profile.INVESTMENT_MANAGER.toString());
			roles.add(Profile.PROJECT_MANAGER.toString());
			
			// Joins
			List<String> joins = addJoins(roles);
			
			// Get project
			Project project = projectDAO.consProject(new Project(idProject), joins);
			
			if (project != null) {
				
				// Set subject
				String subject 	= new ParamResourceBundle(NotificationType.DELETE_MILESTONE.getSubject(), project.getProjectName()).toString(resourceBundle);
				
				// Set body
				String body		= new ParamResourceBundle(NotificationType.DELETE_MILESTONE.getBody(), createStrongTextWithHTML(milestone.getName()), 
						createStrongTextWithHTML(activityName), createStrongTextWithHTML(project.getProjectName())).toString(resourceBundle);
				
				// Set contacts
				//
				Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();
				
				contactsHash = setContacts(session, project, roles);
				
				// Create notifications
				notificationLogic.createNotification(session, subject, body, NotificationType.DELETE_MILESTONE, new ArrayList<Contact>(contactsHash.values()));
			}
		}
	}
	
	/**
	 * Create notification if request rejected hours
	 * 
	 * @param session
	 * @param timesheet
	 * @param settings 
	 * @throws Exception 
	 */
	public void requestRejectedHours(Session session, Timesheet timesheet, HashMap<String, String> settings) throws Exception {
		
		if (timesheet != null && timesheet.getEmployee() != null && timesheet.getEmployee().getContact() != null) {
			
			// Declare logics
			NotificationLogic notificationLogic = new NotificationLogic();
			
			// Init Contacts
			Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();
			
			// Activity
			//
			if (timesheet.getProjectactivity() != null && timesheet.getProjectactivity().getProject() != null) {
				
				// Declare DAOs
				ProjectDAO projectDAO = new ProjectDAO(session);
				
				// Set roles 
				//
				List<String> roles = new ArrayList<String>();
				
				if (Constants.TIMESTATUS_APP1.equals(timesheet.getStatus())) {
					roles.add(Profile.PROJECT_MANAGER.toString());
				}
				else if (Constants.TIMESTATUS_APP2.equals(timesheet.getStatus()) || Constants.TIMESTATUS_APP3.equals(timesheet.getStatus())) {
					
					// Setting administration
					if (SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET, 
							Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_FM))) {
						roles.add(Profile.FUNCTIONAL_MANAGER.toString()); 
					}
					else if (SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET, 
							Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_PMO))) {
						roles.add(Profile.PMO.toString()); 
					}
				}
				
				// Joins
				List<String> joins = addJoins(roles);
				
				// Get project
				Project project = projectDAO.consProject(timesheet.getProjectactivity().getProject(), joins);
				
				// Set contacts
				//
				contactsHash = setContacts(session, project, roles);
			}
			// Operation
			//
			else if (timesheet.getOperation() != null) {
				
				// Setting administration
				if (SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET, 
						Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_FM))) {
					
					// Set FMs contacts
					setContactsByPOandRole(session, timesheet.getEmployee().getPerformingorg(), Constants.ROLE_FM, contactsHash);
					
				}
				else if (SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET, 
						Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_PMO))) {
					
					// Set PMOs contacts
					setContactsByPOandRole(session, timesheet.getEmployee().getPerformingorg(), Constants.ROLE_PMO, contactsHash);
				}
				else {
					//TODO si es PM no puede ver las cuentas de operacion
				}
			}
			
			// Set subject
			String subject 	= new ParamResourceBundle(NotificationType.REQUEST_REJECTED_HOURS.getSubject(), 
					timesheet.getEmployee().getContact().getFullName(), 
					DateUtil.format(Constants.DATE_PATTERN, timesheet.getInitDate()) + 
					StringPool.BLANK_DASH + DateUtil.format(Constants.DATE_PATTERN, timesheet.getEndDate())).toString(resourceBundle);
			
			// Set body
			String body		= new ParamResourceBundle(NotificationType.REQUEST_REJECTED_HOURS.getBody(),
					createStrongTextWithHTML(timesheet.getEmployee().getContact().getFullName()), 
					createStrongTextWithHTML(DateUtil.format(Constants.DATE_PATTERN, timesheet.getInitDate()) + 
							StringPool.BLANK_DASH + DateUtil.format(Constants.DATE_PATTERN, timesheet.getEndDate())),
							createStrongTextWithHTML(ValidateUtil.isNotNull(timesheet.getSuggestRejectComment()) ? 
									timesheet.getSuggestRejectComment() : StringPool.BLANK)).toString(resourceBundle);
			
			// Create notifications
			notificationLogic.createNotification(session, subject, body, NotificationType.REQUEST_REJECTED_HOURS, new ArrayList<Contact>(contactsHash.values()));
		}
		else {
			LOGGER.info("\tRequest rejected hours --> NO DATA");
		}
	}
	
	/**
	 * Create notification if rejected hours
	 * 
	 * @param session
	 * @param employee
	 * @param user 
	 * @param endDate 
	 * @param initDate 
	 * @param comments 
	 * @throws Exception 
	 */
	public void rejectedHours(Session session, Employee employee, Employee user, Date initDate, Date endDate, String comments) throws Exception {
		
		if (employee != null && user != null && initDate != null && endDate != null) {
			
			// Declare logics
			NotificationLogic notificationLogic = new NotificationLogic();
				
			// Set subject
			String subject 	= new ParamResourceBundle(NotificationType.REJECTED_HOURS.getSubject(), 
					user.getContact().getFullName(), 
					DateUtil.format(Constants.DATE_PATTERN, initDate) + 
					StringPool.BLANK_DASH + DateUtil.format(Constants.DATE_PATTERN, endDate)).toString(resourceBundle);
			
			// Set body
			String body		= new ParamResourceBundle(NotificationType.REJECTED_HOURS.getBody(),
					createStrongTextWithHTML(user.getContact().getFullName()), 
					createStrongTextWithHTML(DateUtil.format(Constants.DATE_PATTERN, initDate) + 
					StringPool.BLANK_DASH + DateUtil.format(Constants.DATE_PATTERN, endDate)),
					createStrongTextWithHTML(ValidateUtil.isNotNull(comments) ? comments : StringPool.BLANK)).toString(resourceBundle);
			
			// Set contacts
			//
			Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();
			
			addContact(contactsHash, employee.getContact()); //TOOD mirar esto
			
			// Create notifications
			notificationLogic.createNotification(session, subject, body, NotificationType.REJECTED_HOURS, new ArrayList<Contact>(contactsHash.values()));
		}
	}

    /**
     * Create notification approve operation
     *
     * @param session
     * @param timesheet
     * @param settings
     * @throws Exception
     */
    public void approveOperation(Session session, Timesheet timesheet, HashMap<String, String> settings) throws Exception {

        if (timesheet != null && timesheet.getOperation() != null) {

            Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();

            // Setting administration
            if (SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET,
                    Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_FM))) {

                // Set FMs contacts
                setContactsByPOandRole(session, timesheet.getEmployee().getPerformingorg(), Constants.ROLE_FM, contactsHash);
            }
            else if (SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET,
                    Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_PMO))) {

                // Set PMOs contacts
                setContactsByPOandRole(session, timesheet.getEmployee().getPerformingorg(), Constants.ROLE_PMO, contactsHash);
            }

            // Set imputed dates
            List<Date> imputedDates = new ArrayList<Date>();

            // Save the dates into a string
            List<String> imputedDays =  new ArrayList<String>();

            Calendar calendar = DateUtil.getCalendar();

            calendar.setTime(timesheet.getInitDate());

            if (timesheet.getHoursDay1() != null && timesheet.getHoursDay1() != 0.0) {
                imputedDates.add(calendar.getTime());
                imputedDays.add(DateUtil.format(Constants.DATE_PATTERN,calendar.getTime()));
            }

            if (timesheet.getHoursDay2() != null && timesheet.getHoursDay2() != 0.0) {

                calendar.setTime(timesheet.getInitDate());
                calendar.add(Calendar.DAY_OF_WEEK, 1);

                imputedDates.add(calendar.getTime());
                imputedDays.add(DateUtil.format(Constants.DATE_PATTERN,calendar.getTime()));
            }

            if (timesheet.getHoursDay3() != null && timesheet.getHoursDay3() != 0.0) {

                calendar.setTime(timesheet.getInitDate());
                calendar.add(Calendar.DAY_OF_WEEK, 2);

                imputedDates.add(calendar.getTime());
                imputedDays.add(DateUtil.format(Constants.DATE_PATTERN,calendar.getTime()));
            }

            if (timesheet.getHoursDay4() != null && timesheet.getHoursDay4() != 0.0) {

                calendar.setTime(timesheet.getInitDate());
                calendar.add(Calendar.DAY_OF_WEEK, 3);

                imputedDates.add(calendar.getTime());
                imputedDays.add(DateUtil.format(Constants.DATE_PATTERN,calendar.getTime()));
            }

            if (timesheet.getHoursDay5() != null && timesheet.getHoursDay5() != 0.0) {

                calendar.setTime(timesheet.getInitDate());
                calendar.add(Calendar.DAY_OF_WEEK, 4);

                imputedDates.add(calendar.getTime());
                imputedDays.add(DateUtil.format(Constants.DATE_PATTERN,calendar.getTime()));
            }

            if (timesheet.getHoursDay6() != null && timesheet.getHoursDay6() != 0.0) {

                calendar.setTime(timesheet.getInitDate());
                calendar.add(Calendar.DAY_OF_WEEK, 5);

                imputedDates.add(calendar.getTime());
                imputedDays.add(DateUtil.format(Constants.DATE_PATTERN,calendar.getTime()));
            }

            if (timesheet.getHoursDay7() != null && timesheet.getHoursDay7() != 0.0) {

                calendar.setTime(timesheet.getInitDate());
                calendar.add(Calendar.DAY_OF_WEEK, 6);

                imputedDates.add(calendar.getTime());
                imputedDays.add(DateUtil.format(Constants.DATE_PATTERN,calendar.getTime()));
            }

            // Convert the List of Day Strings into a single String
            String imputedDaysString = StringPool.SPACE + StringPool.OPEN_PARENTHESIS;
            for (String s : imputedDays){
                imputedDaysString += s + StringPool.COMMA_AND_SPACE;
            }
            imputedDaysString = imputedDaysString.substring(0,imputedDaysString.length() - 2);
            imputedDaysString += StringPool.CLOSE_PARENTHESIS;

            // Set PMS
            setPMsContacts(session, timesheet.getEmployee(), imputedDates, contactsHash);

            // Declare logics
            NotificationLogic notificationLogic = new NotificationLogic();

            // Set subject
            String subject 	= new ParamResourceBundle(NotificationType.APPROVE_OPERATION.getSubject(),
                    timesheet.getEmployee().getContact().getFullName(),
                    DateUtil.format(Constants.DATE_PATTERN, timesheet.getInitDate()) +
                            StringPool.BLANK_DASH + DateUtil.format(Constants.DATE_PATTERN, timesheet.getEndDate())).toString(resourceBundle);

            // Set body
            String body		= new ParamResourceBundle(NotificationType.APPROVE_OPERATION.getBody(),
                    createStrongTextWithHTML(timesheet.getEmployee().getContact().getFullName()),
                    createStrongTextWithHTML(timesheet.getOperation().getOperationName()),
                    createStrongTextWithHTML(DateUtil.format(Constants.DATE_PATTERN, timesheet.getInitDate()) +
                            StringPool.BLANK_DASH + DateUtil.format(Constants.DATE_PATTERN, timesheet.getEndDate()) + imputedDaysString)).toString(resourceBundle);

            // Create notifications
            notificationLogic.createNotification(session, subject, body, NotificationType.APPROVE_OPERATION, new ArrayList<Contact>(contactsHash.values()));
        }
    }

    /**
     * Create notification if change timesheet status
     *
     * @param session
     * @param newTimesheet
     * @param resourceBundle
     * @throws Exception
     */
	public void updateTimesheet(Session session, Timesheet newTimesheet, ResourceBundle resourceBundle) throws Exception {

        if (newTimesheet != null && newTimesheet.getEmployee() != null && newTimesheet.getEmployee().getContact() != null) {

            // Declare logics
            NotificationLogic notificationLogic = new NotificationLogic();

            // Init Contacts
            Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();

			String projectOrOperationName;

            // If activity
            if (newTimesheet.getProjectactivity() != null) {

                // Declare DAOs
                ProjectDAO projectDAO = new ProjectDAO(session);

                // Set roles
                //
                List<String> roles = new ArrayList<String>();

                if (Constants.TIMESTATUS_APP1.equals(newTimesheet.getStatus())) {

                    roles.add(Profile.PROJECT_MANAGER.toString());
                }
                else if (Constants.TIMESTATUS_APP2.equals(newTimesheet.getStatus())) {

                    roles.add(Profile.PMO.toString());
                }

                // Joins
                List<String> joins = addJoins(roles);

                // Get project
                Project project = projectDAO.consProject(newTimesheet.getProjectactivity().getProject(), joins);

				projectOrOperationName = project.getProjectName();

                // Set contacts
                //
                contactsHash = setContacts(session, project, roles);
            }
            // Else operation
            else {

                setContactsByPOandRole(session, newTimesheet.getEmployee().getPerformingorg(), Constants.ROLE_PMO, contactsHash);

				projectOrOperationName = newTimesheet.getOperation().getOperationName();
            }

			// Set imputed dates
			List<Date> imputedDates = new ArrayList<Date>();

			// Save the dates into a string
			List<String> imputedDays =  new ArrayList<String>();

			Calendar calendar = DateUtil.getCalendar();

			calendar.setTime(newTimesheet.getInitDate());

			if (newTimesheet.getHoursDay1() != null && newTimesheet.getHoursDay1() != 0.0) {

				imputedDates.add(calendar.getTime());
				imputedDays.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) +
								StringPool.SPACE + resourceBundle.getString("week.monday_short")
								+ StringPool.SPACE + StringPool.OPEN_PARENTHESIS + newTimesheet.getHoursDay1().toString() +
								"h" + StringPool.CLOSE_PARENTHESIS);
			}

			if (newTimesheet.getHoursDay2() != null && newTimesheet.getHoursDay2() != 0.0) {

				calendar.setTime(newTimesheet.getInitDate());
				calendar.add(Calendar.DAY_OF_WEEK, 1);

				imputedDates.add(calendar.getTime());
				imputedDays.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) +
						StringPool.SPACE + resourceBundle.getString("week.tuesday_short")
						+ StringPool.SPACE + StringPool.OPEN_PARENTHESIS + newTimesheet.getHoursDay2().toString() +
						"h" + StringPool.CLOSE_PARENTHESIS);
			}

			if (newTimesheet.getHoursDay3() != null && newTimesheet.getHoursDay3() != 0.0) {

				calendar.setTime(newTimesheet.getInitDate());
				calendar.add(Calendar.DAY_OF_WEEK, 2);

				imputedDates.add(calendar.getTime());
				imputedDays.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) +
						StringPool.SPACE + resourceBundle.getString("week.wednesday_short")
						+ StringPool.SPACE + StringPool.OPEN_PARENTHESIS + newTimesheet.getHoursDay3().toString() +
						"h" + StringPool.CLOSE_PARENTHESIS);
			}

			if (newTimesheet.getHoursDay4() != null && newTimesheet.getHoursDay4() != 0.0) {

				calendar.setTime(newTimesheet.getInitDate());
				calendar.add(Calendar.DAY_OF_WEEK, 3);

				imputedDates.add(calendar.getTime());
				imputedDays.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) +
						StringPool.SPACE + resourceBundle.getString("week.thursday_short")
						+ StringPool.SPACE + StringPool.OPEN_PARENTHESIS + newTimesheet.getHoursDay4().toString() +
						"h" + StringPool.CLOSE_PARENTHESIS);
			}

			if (newTimesheet.getHoursDay5() != null && newTimesheet.getHoursDay5() != 0.0) {

				calendar.setTime(newTimesheet.getInitDate());
				calendar.add(Calendar.DAY_OF_WEEK, 4);

				imputedDates.add(calendar.getTime());
				imputedDays.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) +
						StringPool.SPACE + resourceBundle.getString("week.friday_short")
						+ StringPool.SPACE + StringPool.OPEN_PARENTHESIS + newTimesheet.getHoursDay5().toString() +
						"h" + StringPool.CLOSE_PARENTHESIS);
			}

			if (newTimesheet.getHoursDay6() != null && newTimesheet.getHoursDay6() != 0.0) {

				calendar.setTime(newTimesheet.getInitDate());
				calendar.add(Calendar.DAY_OF_WEEK, 5);

				imputedDates.add(calendar.getTime());
				imputedDays.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) +
						StringPool.SPACE + resourceBundle.getString("week.saturday_short")
						+ StringPool.SPACE + StringPool.OPEN_PARENTHESIS + newTimesheet.getHoursDay6().toString() +
						"h" + StringPool.CLOSE_PARENTHESIS);
			}

			if (newTimesheet.getHoursDay7() != null && newTimesheet.getHoursDay7() != 0.0) {

				calendar.setTime(newTimesheet.getInitDate());
				calendar.add(Calendar.DAY_OF_WEEK, 6);

				imputedDates.add(calendar.getTime());
				imputedDays.add(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) +
						StringPool.SPACE + resourceBundle.getString("week.sunday_short")
						+ StringPool.SPACE + StringPool.OPEN_PARENTHESIS + newTimesheet.getHoursDay7().toString() +
						"h" + StringPool.CLOSE_PARENTHESIS);
			}

			// Convert the List of Day Strings into a single String
            //
			String imputedDaysString = StringPool.BLANK;

			for (String imputedDay : imputedDays){
				imputedDaysString += imputedDay + StringPool.COMMA_AND_SPACE;
			}

			imputedDaysString = imputedDaysString.substring(0,imputedDaysString.length() - 2);

            // Set subject
            //
            String subject 	= new ParamResourceBundle(NotificationType.CHANGE_TIMESHEET_STATUS.getSubject(),
                    newTimesheet.getEmployee().getContact().getFullName(),
                    DateUtil.format(Constants.DATE_PATTERN, newTimesheet.getInitDate()) +
                            StringPool.BLANK_DASH + DateUtil.format(Constants.DATE_PATTERN, newTimesheet.getEndDate())).toString(resourceBundle);

            // Set body
            //
            String body		= new ParamResourceBundle(NotificationType.CHANGE_TIMESHEET_STATUS.getBody(),
                    createStrongTextWithHTML(newTimesheet.getEmployee().getContact().getFullName()),
                    createStrongTextWithHTML(DateUtil.format(Constants.DATE_PATTERN, newTimesheet.getInitDate()) +
                            StringPool.BLANK_DASH + DateUtil.format(Constants.DATE_PATTERN, newTimesheet.getEndDate())),
							imputedDaysString,
					createStrongTextWithHTML(projectOrOperationName)).toString(resourceBundle);

            // Create notifications
            notificationLogic.createNotification(session, subject, body, NotificationType.CHANGE_TIMESHEET_STATUS, new ArrayList<Contact>(contactsHash.values()));
        }
        else {
            LOGGER.info("\tRequest timesheet status change notification --> NO DATA");
        }
	}

	/**
     * Assigned operation
     *
     * @param session
     * @param settings
     * @param employee
     * @param operation
     * @param dates
     */
    public void assignedOperation(Session session, HashMap<String, String> settings,
                                  Employee employee, Operation operation, List<Date> dates) throws Exception {

        if (ValidateUtil.isNotNull(dates)) {

            Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();

            // Setting administration
            if (SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET,
                    Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_FM))) {

                // Set FMs contacts
                setContactsByPOandRole(session, employee.getPerformingorg(), Constants.ROLE_FM, contactsHash);
            }
            else if (SettingUtil.getString(settings, Settings.SETTING_LAST_LEVEL_FOR_APPROVE_SHEET,
                    Settings.DEFAULT_LAST_LEVEL_FOR_APPROVE_SHEET).equals(String.valueOf(Constants.ROLE_PMO))) {

                // Set PMOs contacts
                setContactsByPOandRole(session, employee.getPerformingorg(), Constants.ROLE_PMO, contactsHash);
            }

            // Set PMS
            setPMsContacts(session, employee, dates, contactsHash);

            // Sort dates
            Collections.sort(dates);

            // Concatenate dates
            String concatenateDates = StringPool.BLANK;

            for (int i = 0; i < dates.size(); i++) {

                if (i == dates.size() -1) {
                    concatenateDates += DateUtil.format(Constants.DATE_PATTERN, dates.get(i));
                }
                else {
                    concatenateDates += DateUtil.format(Constants.DATE_PATTERN, dates.get(i)) + StringPool.COMMA_AND_SPACE;
                }
            }

            // Declare logics
            NotificationLogic notificationLogic = new NotificationLogic();

            // Set subject
            String subject 	= new ParamResourceBundle(NotificationType.ASSIGNED_OPERATION.getSubject(),
                    operation.getOperationName(),
                    employee.getContact().getFullName()).toString(resourceBundle);

            // Set body
            String body		= new ParamResourceBundle(NotificationType.ASSIGNED_OPERATION.getBody(),
                    createStrongTextWithHTML(operation.getOperationName()),
                    createStrongTextWithHTML(employee.getContact().getFullName()),
                    createStrongTextWithHTML(concatenateDates)).toString(resourceBundle);

            // Create notifications
            notificationLogic.createNotification(session, subject, body, NotificationType.ASSIGNED_OPERATION, new ArrayList<Contact>(contactsHash.values()));
        }
    }

    /**
     * Expired investment
     *
     * @param investment
     * @throws Exception
     */
    public void expiredInvestment(Project investment) throws Exception {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;

        try {

            tx = session.beginTransaction();

            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);

            // Declare logics
            NotificationLogic notificationLogic = new NotificationLogic();

            // Declare DAO
            ProjectDAO projectDAO = new ProjectDAO(session);

            // Set roles
            //
            List<String> roles = new ArrayList<String>();
            roles.add(Profile.INVESTMENT_MANAGER.toString());
            roles.add(Profile.PMO.toString());

            // Joins
            List<String> joins = addJoins(roles);

            // Get project
            investment = projectDAO.consProject(investment, joins);

            // Get settings
            HashMap<String, String> settings = SettingUtil.getSettings(session, investment.getPerformingorg().getCompany());

            if (SettingUtil.getBoolean(settings, NotificationType.EXPIRED_INVESTMENT)) {

                // TODO 28/10/2014 jordi ripoll el idioma de las notificaciones se modificara en el nuevo desarrollo
                // Set bundle
                //
                String[] locale = SettingUtil.getString(settings, Settings.SettingType.LOCALE).split(StringPool.UNDERLINE);

                resourceBundle = ResourceBundle.getBundle("es.sm2.openppm.front.common.openppm",  new Locale(locale[0], locale[1]));

                // Set subject
                String subject 	= new ParamResourceBundle(NotificationType.EXPIRED_INVESTMENT.getSubject(), investment.getChartLabel()).toString(resourceBundle);

                // Set body
                String body		= new ParamResourceBundle(NotificationType.EXPIRED_INVESTMENT.getBody(),
                        createStrongTextWithHTML(investment.getProjectName()),
                        createStrongTextWithHTML(dateFormat.format(investment.getPlannedInitDate()))).toString(resourceBundle);

                // Set contacts
                //
                Hashtable<Integer, Contact> contactsHash = setContacts(session, investment, roles);

                // Create notifications
                notificationLogic.createNotification(session, subject, body, NotificationType.EXPIRED_INVESTMENT, new ArrayList<Contact>(contactsHash.values()));
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
	
	/***********************/
	/*** PRIVATE METHODS ***/
	/***********************/
	
	
	/**
	 * Create html strong text
	 * 
	 * @param text
	 * @return
	 * @throws Exception 
	 */
	private String createStrongTextWithHTML(String text) throws Exception {
		
		String textHtml = StringPool.BLANK;
		
		if (ValidateUtil.isNotNull(text)) {
			
			HtmlCanvas html = new HtmlCanvas();
			
			// Bold text
			html.b().content(text);
			
			textHtml = html.toHtml();
		}
		
		return textHtml;
	}

	/**
	 * Set contacts in hash map by role
	 * 
	 * @param project 
	 * @param roles
	 * @return
	 */
	private Hashtable<Integer, Contact> setContacts(Session session, Project project, List<String> roles) {
		
		// Contacts for notification
		Hashtable<Integer, Contact> contactsHash = new Hashtable<Integer, Contact>();
		
		if (project != null && ValidateUtil.isNotNull(roles)) {
			
			for (String role : roles) {

                Employee employee;

				// Set PM
				if (Profile.PROJECT_MANAGER.toString().equals(role) 
						&& project.getEmployeeByProjectManager() != null) {

                    employee = project.getEmployeeByProjectManager();

                    if (employee!= null && !ValidateUtil.defBoolean(employee.getDisable())) {

                        // Contact by PM
                        Contact contact = employee.getContact();

                        addContact(contactsHash, contact);
                    }
                    else if (employee != null) {
                        LOGGER.debug("Not add contact("+employee.getContact().getIdContact()+"): "+
                                employee.getContact().getFullName()+" because employee("+ employee.getIdEmployee()+")disabled");
                    }
				}
				// Set IM
				else if (Profile.INVESTMENT_MANAGER.toString().equals(role) 
						&& project.getEmployeeByInvestmentManager() != null) {

                    employee = project.getEmployeeByInvestmentManager();

                    if (employee!= null && !ValidateUtil.defBoolean(employee.getDisable())) {

                        // Contact by IM
                        Contact contact = project.getEmployeeByInvestmentManager().getContact();

                        addContact(contactsHash, contact);
                    }
                    else if (employee != null) {
                        LOGGER.debug("Not add contact("+employee.getContact().getIdContact()+"): "+
                                employee.getContact().getFullName()+" because employee("+ employee.getIdEmployee()+")disabled");
                    }
				}
				// Set PMM
				else if (Profile.PROGRAM_MANAGER.toString().equals(role)
						&& project.getProgram() != null && project.getProgram().getEmployee() != null) {

                    employee = project.getProgram().getEmployee();

                    if (employee!= null && !ValidateUtil.defBoolean(employee.getDisable())) {

                        // Contact by Program Manager
                        Contact contact = employee.getContact();

                        addContact(contactsHash, contact);
                    }
                    else if (employee != null) {
                        LOGGER.debug("Not add contact("+employee.getContact().getIdContact()+"): "+
                                employee.getContact().getFullName()+" because employee("+ employee.getIdEmployee()+")disabled");
                    }
				}
				// Set FM
				else if (Profile.FUNCTIONAL_MANAGER.toString().equals(role) 
						&& project.getEmployeeByFunctionalManager() != null) {

                    employee = project.getEmployeeByFunctionalManager();

                    if (employee!= null && !ValidateUtil.defBoolean(employee.getDisable())) {

                        // Contact by FM
                        Contact contact = project.getEmployeeByFunctionalManager().getContact();

                        addContact(contactsHash, contact);
                    }
                    else if (employee != null) {
                        LOGGER.debug("Not add contact("+employee.getContact().getIdContact()+"): "+
                                employee.getContact().getFullName()+" because employee("+ employee.getIdEmployee()+")disabled");
                    }
				}
				// Set PMO
				else if (Profile.PMO.toString().equals(role)
						&& project.getPerformingorg() != null) {
					
					setContactsByPOandRole(session, project.getPerformingorg(), Constants.ROLE_PMO, contactsHash);
				}
			}
		}
		
		return contactsHash;
	}
	
	/**
	 * Set contacts by po and role
	 * 
	 * @param session
	 * @param performingorg
	 * @param contactsHash
	 */
	private void setContactsByPOandRole(Session session, Performingorg performingorg, int role, Hashtable<Integer, Contact> contactsHash) {
			
		// Instance DAO for search
		EmployeeDAO employeeDAO = new EmployeeDAO(session);
		
		// Find PMOs
		List<Employee> employees = employeeDAO.findByPOAndRol(performingorg, role);
		
		if (ValidateUtil.isNotNull(employees)) {

			for (Employee employee :employees) {

                if (!ValidateUtil.defBoolean(employee.getDisable())) {

                    // Add contact PMO
                    Contact contact = employee.getContact();

                    addContact(contactsHash, contact);
                }
                else if (employee != null){
                    LOGGER.debug("Not add contact("+ employee.getContact().getIdContact() +"): "+ employee.getContact().getFullName()+" because employee("+ employee.getIdEmployee()+")disabled");
                }
			}
		}
	}

    /**
     * Set PMs contacts
     *
     * @param session
     * @param employee
     * @param dates
     * @param contactsHash
     */
    private void setPMsContacts(Session session, Employee employee, List<Date> dates, Hashtable<Integer, Contact> contactsHash) {

        // Instance DAO for search
        ProjectDAO projectDAO = new ProjectDAO(session);

        // Find PMOs
        List<Project> projects = projectDAO.findPMs(employee, dates);

        if (ValidateUtil.isNotNull(projects)) {

            for (Project project : projects) {

                Employee pm = project.getEmployeeByProjectManager();

                if (pm != null && !ValidateUtil.defBoolean(pm.getDisable())) {

                    // Add contact PM
                    addContact(contactsHash, pm.getContact());
                }
                else if (pm != null){
                    LOGGER.debug("Not add contact("+ pm.getContact().getIdContact() +"): "+
                            pm.getContact().getFullName()+" because employee("+ pm.getIdEmployee()+")disabled");
                }

            }
        }
    }

	/**
	 * Add contact for notification
     * @param contactsHash
     * @param contact
     */
	private void addContact(Hashtable<Integer, Contact> contactsHash, Contact contact) {

        if (contact != null && contactsHash != null) {

            // Add contact
            if (!ValidateUtil.defBoolean(contact.getDisable()) && !contactsHash.contains(contact.getIdContact())) {
                contactsHash.put(contact.getIdContact(), contact);
            }
            // Messages because not add contact
            else if(ValidateUtil.defBoolean(contact.getDisable())) {
                LOGGER.debug("Not add contact("+contact.getIdContact()+"): "+ contact.getFullName()+" because contact disabled");
            }
            else if(contactsHash.contains(contact.getIdContact())) {
                LOGGER.debug("Not add contact("+contact.getIdContact()+"): "+ contact.getFullName()+" because already exists in the list");
            }
        }
	}

	/**
	 * Add joins by role
	 * 
	 * @param roles
	 * @return
	 */
	private List<String> addJoins(List<String> roles) {
		
		List<String> joins = new ArrayList<String>();
		
		for (String role : roles) {
			
			// PM
			if (Profile.PROJECT_MANAGER.toString().equals(role)) {
				
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER);
				joins.add(Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
			}
			// IM
			else if (Profile.INVESTMENT_MANAGER.toString().equals(role)) {
				
				joins.add(Project.EMPLOYEEBYINVESTMENTMANAGER);
				joins.add(Project.EMPLOYEEBYINVESTMENTMANAGER+"."+Employee.CONTACT);
			}
			// FM
			else if (Profile.FUNCTIONAL_MANAGER.toString().equals(role)) {
				
				joins.add(Project.EMPLOYEEBYFUNCTIONALMANAGER);
				joins.add(Project.EMPLOYEEBYFUNCTIONALMANAGER+"."+Employee.CONTACT);
			}
			// PRM
			else if (Profile.PROGRAM_MANAGER.toString().equals(role)) {
				
				joins.add(Project.PROGRAM);
				joins.add(Project.PROGRAM+"."+Program.EMPLOYEE);
				joins.add(Project.PROGRAM+"."+ Program.EMPLOYEE+"."+Employee.CONTACT);
			}
			// PMO
			else if (Profile.PMO.toString().equals(role)) {
				
				joins.add(Project.PERFORMINGORG);
				joins.add(Project.PERFORMINGORG+"."+Performingorg.EMPLOYEE);
				joins.add(Project.PERFORMINGORG+"."+Performingorg.EMPLOYEE+"."+Employee.CONTACT);
			}
		}
		
		return joins;
	}
}