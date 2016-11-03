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
 * File: MilestoneLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.MilestoneDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Milestonecategory;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Milestonetype;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import es.sm2.openppm.utils.javabean.CsvFile;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class MilestoneLogic extends AbstractGenericLogic<Milestones, Integer> {
	
	private HashMap<String, String> settings; 
	
	public final static Logger LOGGER = Logger.getLogger(MilestoneLogic.class);
	
	public MilestoneLogic() {
		super();
	}
	
	public MilestoneLogic(HashMap<String, String> settings, ResourceBundle bundle) {
		super(bundle);
		this.settings = settings;
	}
	
	/**
	 * Return Milestone
	 * @param milestone
	 * @return
	 * @throws Exception 
	 */
	public Milestones consMilestone(Milestones milestone) throws Exception {
		
		Milestones mileston = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			MilestoneDAO milestoneDAO = new MilestoneDAO(session);
			mileston = milestoneDAO.findMilestone(milestone);

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
		return mileston;
	}
	
	/**
	 *  Return Milestones
	 * @param idProject
	 * @return
	 * @throws Exception 
	 */
	public List<Milestones> consMilestones(Integer idProject) throws Exception {
		
		List<Milestones> milestones = null;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			MilestoneDAO milestoneDAO = new MilestoneDAO(session);
			if (idProject != -1) {
				milestones = milestoneDAO.findByProject(new Project(idProject));
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
		return milestones;
	}

	/**
	 * Check Milestone Errors
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public boolean checkMilestones(Project project) throws Exception {
		
		Transaction tx		= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		boolean errors = false;
		try {
			tx = session.beginTransaction();
			
			MilestoneDAO milestoneDAO = new MilestoneDAO(session);
			List <Milestones> milestones = milestoneDAO.findMilestoneWithoutActivity(project);
			
			if (!milestones.isEmpty()) {
				errors = true;
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
		return errors;
	}

	/**
	 * Change first activity by second activity 
	 * 
	 * @param activity
	 * @throws Exception
	 */
	public boolean changeActivity(Projectactivity activity, Projectactivity activityChange) throws Exception {
		
		boolean hasMilestonesUpdated = false;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			MilestoneDAO milestoneDAO = new MilestoneDAO(session);
			
			List<Milestones> milestones = milestoneDAO.findByRelation(Milestones.PROJECTACTIVITY, activity, Milestones.IDMILESTONE, Constants.ASCENDENT);
			
			// Update milestones
			if (milestones != null && !milestones.isEmpty()) {
				
				hasMilestonesUpdated = true;
				
				for (Milestones milestone : milestones) {
					
					milestone.setProjectactivity(activityChange);
					
					milestoneDAO.makePersistent(milestone);
				}
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return hasMilestonesUpdated;
	}

	/**
	 * Find milestones filter by projects and milestones type and dates
	 * 
	 * @param projects
	 * @param milestonetype 
	 * @param milestonecategory 
	 * @param until 
	 * @param since 
	 * @param milestonePending
	 * @param property
	 * @param order
	 * @param joins
	 * @return
	 * @throws Exception
	 */
	public List<Milestones> filter(List<Project> projects,
			Milestonetype milestonetype, Milestonecategory milestonecategory, Date since, Date until, 
			String milestonePending, String property, String order, List<String> joins) throws Exception {
		
		List<Milestones> milestones = null;
		Date sinceCalc				= null;
		Date untilCalc 				= null;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			// Declare DAO
			MilestoneDAO milestoneDAO = new MilestoneDAO(session);
			
			// Set dates
			//
			if (since != null || until != null) {
				
				if (since != null) {
					sinceCalc = since;
				}
				
				if (until != null) {
					untilCalc = until;
				}
			}
			else {
				
				Calendar sinceCal = DateUtil.getCalendar();
				Calendar untilCal = DateUtil.getCalendar();
				
				untilCal.add(Calendar.MONTH, +1);
				
				sinceCalc = sinceCal.getTime();
				untilCalc = untilCal.getTime();
			}
			
			// DAO
			milestones = milestoneDAO.filter(projects, milestonetype, milestonecategory, sinceCalc, untilCalc, milestonePending, property, order, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return milestones;
	}
	
	/**
	 * Find milestones by filters
	 * 
	 * @param session
	 * @param projects
	 * @param milestonetype
	 * @param milestonecategory 
	 * @param since
	 * @param until
 	 * @param milestonePending
	 * @param property
	 * @param order
	 * @param joins
	 * @return
	 * @throws Exception
	 */
	public List<Milestones> filter(Session session, List<Project> projects,
			Milestonetype milestonetype, Milestonecategory milestonecategory, 
			Date since, Date until, String milestonePending, String property, String order, List<String> joins) throws Exception {
		
		List<Milestones> milestones = null;
		Date sinceCalc				= null;
		Date untilCalc 				= null;
		
		// Declare DAO
		MilestoneDAO milestoneDAO = new MilestoneDAO(session);
		
		// Set dates
		//
		if (since != null || until != null) {
			
			if (since != null) {
				sinceCalc = since;
			}
			
			if (until != null) {
				untilCalc = until;
			}
		}
		else {
			
			Calendar sinceCal = DateUtil.getCalendar();
			Calendar untilCal = DateUtil.getCalendar();
			
			untilCal.add(Calendar.MONTH, +1);
			
			sinceCalc = sinceCal.getTime();
			untilCalc = untilCal.getTime();
		}
		
		// DAO
		milestones = milestoneDAO.filter(projects, milestonetype, milestonecategory, 
				sinceCalc, untilCalc, milestonePending, property, order, joins);
		
		return milestones;
	}

	/**
	 * Export to CSV
	 * 
	 * @param ids
	 * @param until 
	 * @param since 
	 * @param milestonetype 
	 * @param milestonecategory 
	 * @return
	 * @throws Exception 
	 */
	public CsvFile exportToCSV(Integer[] ids, Milestonetype milestonetype, Milestonecategory milestonecategory, 
			Date since, Date until, String milestonePending) throws Exception {
		
		CsvFile file = null;
		Transaction tx = null;
		
		// Initiate session
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {			
			tx = session.beginTransaction();
			file = new CsvFile(Constants.SEPARATOR_CSV);

			// Init DAOs
			ProjectDAO projDAO = new ProjectDAO(session);
			
			// Set header
			file.addValue(getBundle().getString("project"));
			file.addValue(getBundle().getString("project.chart_label"));
			file.addValue(getBundle().getString("project_manager"));
			file.addValue(getBundle().getString("milestone.type"));
			file.addValue(getBundle().getString("milestone.category"));
			file.addValue(getBundle().getString("milestone.name"));
			file.addValue(getBundle().getString("milestone.desc"));
			file.addValue(getBundle().getString("milestone.planned_date"));
			file.addValue(getBundle().getString("milestone.due_date"));
			file.addValue(getBundle().getString("milestone.actual_date"));
			file.newLine();

			// Get projects
			List<Project> projects = projDAO.consList(ids, null, null, null);			
			
			// Get milestones
			//
			List<String> joins = new ArrayList<String>();
			joins.add(Milestones.MILESTONETYPE);
			joins.add(Milestones.PROJECT);
			joins.add(Milestones.PROJECT+"."+Project.EMPLOYEEBYPROJECTMANAGER);
			joins.add(Milestones.PROJECT+"."+Project.EMPLOYEEBYPROJECTMANAGER+"."+Employee.CONTACT);
			
			// Logic
			List<Milestones> milestones	= filter(session, projects, milestonetype, milestonecategory, 
					since, until, milestonePending, Milestones.PLANNED, Constants.ASCENDENT, joins);
			
			if (ValidateUtil.isNotNull(milestones)) {
				
				for (Milestones milestone : milestones) {
					
					file.addValue(ValidateUtil.isNotNull(milestone.getProject().getProjectName()) ? 
							milestone.getProject().getProjectName() : StringPool.BLANK);
					
					file.addValue(ValidateUtil.isNotNull(milestone.getProject().getChartLabel()) ? 
							milestone.getProject().getChartLabel() : StringPool.BLANK);
					
					if (milestone.getProject().getEmployeeByProjectManager() != null && milestone.getProject().getEmployeeByProjectManager().getContact() != null) {
						file.addValue(milestone.getProject().getEmployeeByProjectManager().getContact().getFullName());
					}
					else {
						file.addValue(StringPool.BLANK);
					}
					
					// Milestone type 
					//
					if (milestone.getMilestonetype() != null && ValidateUtil.isNotNull(milestone.getMilestonetype().getName())) {
						file.addValue(milestone.getMilestonetype().getName());
					}
					else {
						file.addValue(StringPool.BLANK);
					}
					
					// Milestone category 
					//
					if (milestone.getMilestonecategory() != null && ValidateUtil.isNotNull(milestone.getMilestonecategory().getName())) {
						file.addValue(milestone.getMilestonecategory().getName());
					}
					else {
						file.addValue(StringPool.BLANK);
					}
					
					file.addValue(ValidateUtil.isNotNull(milestone.getName()) ? 
							milestone.getName() : StringPool.BLANK);
					
					file.addValue(ValidateUtil.isNotNull(milestone.getDescription()) ? 
							milestone.getDescription().replaceAll("\\s", StringPool.SPACE) : StringPool.BLANK);
					
					if (milestone.getPlanned() != null) {
						file.addValue(DateUtil.format(getBundle(), milestone.getPlanned()));
					}
					else {
						file.addValue(StringPool.BLANK);
					}
					
					if (milestone.getEstimatedDate() != null) {
						file.addValue(DateUtil.format(getBundle(), milestone.getEstimatedDate()));
					}
					else {
						file.addValue(StringPool.BLANK);
					}
					
					if (milestone.getAchieved() != null) {
						file.addValue(DateUtil.format(getBundle(), milestone.getAchieved()));
					}
					else {
						file.addValue(StringPool.BLANK);
					}
					
					file.newLine();
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
		return file;
	}

	/**
	 * Delete milestone and create notification
	 * 
	 * @param milestone
	 * @param activityName 
	 * @param idProject 
	 * @throws Exception 
	 */
	public void deleteMilestone(Milestones milestone, Integer idProject, String activityName) throws Exception {
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			// Declare DAO
			MilestoneDAO milestoneDAO = new MilestoneDAO(session);
            ProjectDAO projectDAO     = new ProjectDAO(session);

            // DAO project
            Project project = projectDAO.findById(idProject);
			
			// Notification - Create change planned dates
			//
			if (SettingUtil.getBoolean(settings, NotificationType.DELETE_MILESTONE) && (Constants.STATUS_CONTROL.equals(project.getStatus()))) {
				
				NotificationSpecificLogic notificationsLogic = new NotificationSpecificLogic(settings);
				
				notificationsLogic.deleteMilestone(session, milestone, idProject, activityName);
			}
			
			// DAO delete
			milestoneDAO.makeTransient(milestone);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
	}

}
