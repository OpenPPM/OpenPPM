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
 * File: SynchronizeLogic.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 13:23:54
 */

package es.sm2.openppm.plugin.synchronize.logic;

import com.thoughtworks.xstream.XStream;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.common.enums.LocationAuditEnum;
import es.sm2.openppm.core.dao.AssumptionLogDAO;
import es.sm2.openppm.core.dao.AssumptionRegisterDAO;
import es.sm2.openppm.core.dao.ChargescostsDAO;
import es.sm2.openppm.core.dao.ChecklistDAO;
import es.sm2.openppm.core.dao.IncomesDAO;
import es.sm2.openppm.core.dao.IssuelogDAO;
import es.sm2.openppm.core.dao.LogprojectstatusDAO;
import es.sm2.openppm.core.dao.MilestoneDAO;
import es.sm2.openppm.core.dao.ProjectActivityDAO;
import es.sm2.openppm.core.dao.ProjectCharterDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.dao.ProjectFollowupDAO;
import es.sm2.openppm.core.dao.ProjectlabelDAO;
import es.sm2.openppm.core.dao.RiskRegisterDAO;
import es.sm2.openppm.core.dao.StakeholderDAO;
import es.sm2.openppm.core.dao.WBSNodeDAO;
import es.sm2.openppm.core.dao.WorkingcostsDAO;
import es.sm2.openppm.core.logic.impl.AuditLogic;
import es.sm2.openppm.core.logic.setting.AuditSetting;
import es.sm2.openppm.plugin.synchronize.beans.AssumptionSync;
import es.sm2.openppm.plugin.synchronize.beans.AssumptionlogSync;
import es.sm2.openppm.plugin.synchronize.beans.ChargeCostSync;
import es.sm2.openppm.plugin.synchronize.beans.ChecklistSync;
import es.sm2.openppm.plugin.synchronize.beans.FollowupSync;
import es.sm2.openppm.plugin.synchronize.beans.IncomeSync;
import es.sm2.openppm.plugin.synchronize.beans.IssuelogSync;
import es.sm2.openppm.plugin.synchronize.beans.LabelSync;
import es.sm2.openppm.plugin.synchronize.beans.MilestonSync;
import es.sm2.openppm.plugin.synchronize.beans.ProjectSync;
import es.sm2.openppm.plugin.synchronize.beans.RiskSync;
import es.sm2.openppm.plugin.synchronize.beans.StakeholderSync;
import es.sm2.openppm.plugin.synchronize.beans.WbsnodeSync;
import es.sm2.openppm.plugin.synchronize.beans.WorkingCostSync;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Assumptionreassessmentlog;
import es.sm2.openppm.core.model.impl.Assumptionregister;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Checklist;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Incomes;
import es.sm2.openppm.core.model.impl.Issuelog;
import es.sm2.openppm.core.model.impl.Logprojectstatus;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Projectlabel;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.impl.Workingcosts;
import es.sm2.openppm.plugin.synchronize.util.XStreamUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.StringUtil;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SynchronizeLogic {

	public final static Logger LOGGER = Logger.getLogger(SynchronizeLogic.class);
	
	/**
	 * Create export openppm file
	 * @param idProject
	 * @return
	 * @throws Exception 
	 */
	public File exportFile(Integer idProject) throws Exception {
		
		File file 		= null;
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			Project project = projectDAO.findById(idProject);
			
			XStream xstream = XStreamUtil.createXStrean();

			// Information of project
			ProjectSync projectSync = new ProjectSync(project, session);
			
			file = new File(Settings.TEMP_DIR_GENERATE_DOCS + Constants.PATH_SEPARATOR +
					StringUtil.clearSlashes(project.getProjectName()+ StringPool.BLANK_DASH + project.getChartLabel())+".xml");
			
			FileOutputStream fileOut = new FileOutputStream(file);

			xstream.toXML(projectSync, fileOut);

			fileOut.close();
			
			LOGGER.info("Absolute Path for file: "+file.getAbsolutePath());

			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }

		return file;
	}

	/**
	 * Synchronize project from project sync
	 * @param idProject
	 * @param projectSync
	 * @return
	 * @throws Exception 
	 */
	public Project synchronize(int idProject, ProjectSync projectSync) throws Exception {
		
		Project project = null;
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO = new ProjectDAO(session);
			
			project = projectDAO.consInitiatingProject(new Project(idProject));
			
			tx.commit();
			
			project = XStreamUtil.synchronize(project, projectSync);
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return project;
	}

	/**
	 * Synchronize project charter from project sync
	 * @param idProject
	 * @param projectSync
	 * @return
	 * @throws Exception 
	 */
	public Projectcharter synchronizeCharter(int idProject, ProjectSync projectSync) throws Exception {
		
		Projectcharter projectCharter = null;
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			ProjectCharterDAO charterDAO = new ProjectCharterDAO(session);
			
			List<Projectcharter> charters = charterDAO.findByRelation(Projectcharter.PROJECT, new Project(idProject));
			
			tx.commit();
			
			if (ValidateUtil.isNotNull(charters)) {
				projectCharter = XStreamUtil.synchronize(charters.get(0), projectSync);
			}
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return projectCharter;
	}

	/**
	 * Import data from project sync
	 * @param project
	 * @param charter 
	 * @param projectSync
	 * @param user 
	 * @param plannedInitDate 
	 * @return 
	 * @throws Exception 
	 */
	public List<String> importData(Project project, Projectcharter charter, ProjectSync projectSync,
			Employee user, Date plannedInitDate, HashMap<String, String> settings) throws Exception {
		
		List<String> newInfo = new ArrayList<String>();
		
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {
			tx = session.beginTransaction();
			
			if (ValidateUtil.isNotNull(projectSync.getWbsnodes())) {
				
				ProjectDAO projectDAO				= new ProjectDAO(session);
				WBSNodeDAO wbsNodeDAO				= new WBSNodeDAO(session);
				ProjectActivityDAO activityDAO		= new ProjectActivityDAO(session);
				MilestoneDAO milestoneDAO			= new MilestoneDAO(session);
				ChecklistDAO checklistDAO			= new ChecklistDAO(session);
				IncomesDAO incomesDAO				= new IncomesDAO(session);
				StakeholderDAO stakeholderDAO		= new StakeholderDAO(session);
				ProjectFollowupDAO followupDAO		= new ProjectFollowupDAO(session);
				IssuelogDAO issuelogDAO				= new IssuelogDAO(session);
				AssumptionRegisterDAO assumptionDAO = new AssumptionRegisterDAO(session);
				AssumptionLogDAO assumptionLogDAO	= new AssumptionLogDAO(session);
				RiskRegisterDAO riskRegisterDAO		= new RiskRegisterDAO(session);
				ChargescostsDAO chargescostsDAO		= new ChargescostsDAO(session);
				WorkingcostsDAO workingcostsDAO		= new WorkingcostsDAO(session);
				LogprojectstatusDAO logStatusDAO	= new LogprojectstatusDAO(session);
				ProjectlabelDAO projectlabelDAO		= new ProjectlabelDAO(session);
				
				// REMOVE OLD DATA
				projectDAO.prepareForImport(project);

				HashMap<Integer, Wbsnode> keyNode		= new HashMap<Integer, Wbsnode>();
				HashMap<WbsnodeSync, Wbsnode> relation	= new HashMap<WbsnodeSync, Wbsnode>();
				
				// CREATE WBSNODES
				for (WbsnodeSync node : projectSync.getWbsnodes()) {

					Wbsnode wbsnode = XStreamUtil.createWbsnode(node);
					wbsnode.setProject(project);
					
					wbsnode = wbsNodeDAO.makePersistent(wbsnode);
					
					keyNode.put(node.getId(), wbsnode);
					relation.put(node, wbsnode);
					
					// CREATE ACTIVITY
					if (node.getActivity() != null) {
						
						Projectactivity projectactivity = XStreamUtil.createActivity(node.getActivity());
						projectactivity.setWbsnode(wbsnode);
						projectactivity.setProject(project);
						
						projectactivity = activityDAO.makePersistent(projectactivity);
						
						// CREATE MILESTONES
						if (ValidateUtil.isNotNull(node.getActivity().getMilestones())) {
							
							for (MilestonSync milestoneSync :node.getActivity().getMilestones()) {
								
								Milestones milestone = XStreamUtil.createMilestone(milestoneSync);
								milestone.setProjectactivity(projectactivity);
								milestone.setProject(project);
								
								milestoneDAO.makePersistent(milestone);
							}
						}
					}
					
					// CREATE CHECKLIST
					if (ValidateUtil.isNotNull(node.getChecklists())) {
						
						for (ChecklistSync checklistSync : node.getChecklists()) {
							
							Checklist checklist = XStreamUtil.createCheckList(checklistSync);
							checklist.setWbsnode(wbsnode);
							
							checklistDAO.makePersistent(checklist);
						}
					}
				}
				
				// CREATE RELATIONS
				for (WbsnodeSync node : projectSync.getWbsnodes()) {
					
					if (node.getParent() != null) {
						
						Wbsnode wbsnode = relation.get(node);
						wbsnode.setWbsnode(keyNode.get(node.getParent()));
						
						wbsNodeDAO.makePersistent(wbsnode);
					}
				}
				
				// CREATE INCOMES
				if (ValidateUtil.isNotNull(projectSync.getIncomes())) {
					
					for (IncomeSync incomeSync : projectSync.getIncomes()) {
						
						Incomes income = XStreamUtil.createIncome(incomeSync);
						income.setProject(project);
						
						incomesDAO.makePersistent(income);
					}
				}
				
				//CREATE STAKEHOLDERS
				if (ValidateUtil.isNotNull(projectSync.getStakeholders())) {
					
					for (StakeholderSync stakeholderSync : projectSync.getStakeholders()) {
						
						Stakeholder stakeholder = XStreamUtil.createStakeholder(stakeholderSync, session);
						stakeholder.setProject(project);
						
						stakeholderDAO.makePersistent(stakeholder);
					}
				}
				
				// CREATE FOLLOWUPS
				if (ValidateUtil.isNotNull(projectSync.getFollowups())) {
					
					for (FollowupSync followupSync : projectSync.getFollowups()) {
						
						Projectfollowup projectfollowup = XStreamUtil.createFollowup(followupSync);
						projectfollowup.setProject(project);
						
						followupDAO.makePersistent(projectfollowup);
					}
				}
				
				// CREATE ISSUES LOGS
				if (ValidateUtil.isNotNull(projectSync.getIssuelogs())) {
					
					for (IssuelogSync issuelogSync : projectSync.getIssuelogs()) {
						
						Issuelog issuelog = XStreamUtil.createIssueLog(issuelogSync);
						issuelog.setProject(project);
						
						issuelogDAO.makePersistent(issuelog);
					}
				}
				
				// CREATE ASSUMPTIONS
				if (ValidateUtil.isNotNull(projectSync.getAssumptions())) {
					
					for (AssumptionSync assumptionSync : projectSync.getAssumptions()) {
						
						Assumptionregister assumptionregister = XStreamUtil.createAssumption(assumptionSync);
						assumptionregister.setProject(project);
						
						assumptionDAO.makePersistent(assumptionregister);
						
						// CREATE ASSUMPTIONS LOGS
						if (ValidateUtil.isNotNull(assumptionSync.getAssumptionlogs())) {
							
							for (AssumptionlogSync assumptionlogSync : assumptionSync.getAssumptionlogs()) {
								
								Assumptionreassessmentlog assumptionlog = XStreamUtil.createAssumtionLog(assumptionlogSync);
								assumptionlog.setAssumptionregister(assumptionregister);
								
								assumptionLogDAO.makePersistent(assumptionlog);
							}
						}
					}
				}
				
				// CREATE ISSUES LOGS
				if (ValidateUtil.isNotNull(projectSync.getRisks())) {
					
					for (RiskSync riskSync : projectSync.getRisks()) {
						
						Riskregister riskregister = XStreamUtil.createRisk(riskSync);
						riskregister.setProject(project);
						
						riskRegisterDAO.makePersistent(riskregister);
					}
				}
				
				// CREATE CHARGE COSTS
				if (ValidateUtil.isNotNull(projectSync.getChargeCosts())) {
					
					for (ChargeCostSync chargeCostSync : projectSync.getChargeCosts()) {
						
						Chargescosts chargescosts = XStreamUtil.createChargeCost(chargeCostSync);
						chargescosts.setProject(project);
						
						chargescostsDAO.makePersistent(chargescosts);
					}
				}
				
				// CREATE WORKING COSTS
				if (ValidateUtil.isNotNull(projectSync.getWorkingCosts())) {
					
					for (WorkingCostSync workingCostSync : projectSync.getWorkingCosts()) {
						
						Workingcosts workingcost = XStreamUtil.createWorkingcost(workingCostSync);
						workingcost.setProject(project);
						
						workingcostsDAO.makePersistent(workingcost);
					}
				}
				
				// CREATE LABELS
				if (ValidateUtil.isNotNull(projectSync.getLabels())) {
					
					for (LabelSync labelSync : projectSync.getLabels()) {
						
						Projectlabel projectLabel = XStreamUtil.createlabel(session, labelSync);

                        if (projectLabel != null) {

                            projectLabel.setProject(project);

                            // If label found in application
                            projectlabelDAO.makePersistent(projectLabel);
                        }
					}
				}
				
				
				// SAVE INITIATING PROJECT
				ProjectLogic logic = new ProjectLogic(settings, null);
				newInfo = logic.saveInitiatingProject(session, project, charter, null, null, plannedInitDate, user);
				
				// SAVE LOG STATUS
				logStatusDAO.makePersistent(new Logprojectstatus(
					project, user, project.getStatus(),project.getInvestmentStatus(), new Date()));

                // Create audit
                AuditLogic auditLogic = new AuditLogic();
                auditLogic.createAudit(LocationAuditEnum.PROJECT_DATA, project, user, AuditSetting.PROJECT_DATA, session);
			}
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return newInfo;
	}
}

