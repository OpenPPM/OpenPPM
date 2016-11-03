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
 * File: XStreamUtil.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 13:27:56
 */

package es.sm2.openppm.plugin.synchronize.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import es.sm2.openppm.core.dao.LabelDAO;
import es.sm2.openppm.core.dao.StakeholderclassificationDAO;
import es.sm2.openppm.plugin.synchronize.beans.ActivitySync;
import es.sm2.openppm.plugin.synchronize.beans.AssumptionSync;
import es.sm2.openppm.plugin.synchronize.beans.AssumptionlogSync;
import es.sm2.openppm.plugin.synchronize.beans.ChargeCostSync;
import es.sm2.openppm.plugin.synchronize.beans.ChecklistSync;
import es.sm2.openppm.plugin.synchronize.beans.EmployeeSync;
import es.sm2.openppm.plugin.synchronize.beans.FollowupSync;
import es.sm2.openppm.plugin.synchronize.beans.IncomeSync;
import es.sm2.openppm.plugin.synchronize.beans.IssuelogSync;
import es.sm2.openppm.plugin.synchronize.beans.LabelSync;
import es.sm2.openppm.plugin.synchronize.beans.MilestonSync;
import es.sm2.openppm.plugin.synchronize.beans.ProjectCharterSync;
import es.sm2.openppm.plugin.synchronize.beans.ProjectSync;
import es.sm2.openppm.plugin.synchronize.beans.RiskSync;
import es.sm2.openppm.plugin.synchronize.beans.StageGateSync;
import es.sm2.openppm.plugin.synchronize.beans.StakeholderSync;
import es.sm2.openppm.plugin.synchronize.beans.WbsnodeSync;
import es.sm2.openppm.plugin.synchronize.beans.WorkingCostSync;
import es.sm2.openppm.core.model.impl.Assumptionreassessmentlog;
import es.sm2.openppm.core.model.impl.Assumptionregister;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Checklist;
import es.sm2.openppm.core.model.impl.Currency;
import es.sm2.openppm.core.model.impl.Incomes;
import es.sm2.openppm.core.model.impl.Issuelog;
import es.sm2.openppm.core.model.impl.Label;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Projectlabel;
import es.sm2.openppm.core.model.impl.Riskcategories;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.impl.Workingcosts;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.hibernate.Session;

import java.util.List;

public class XStreamUtil {

	/**
	 * Create instance
	 * @return
	 */
	public static XStream createXStrean() {
		
		XStream xstream = new XStream(new DomDriver("UTF-8"));
		
		xstream.alias("activity", ActivitySync.class);
		xstream.alias("assumptionlog", AssumptionlogSync.class);
		xstream.alias("assumption", AssumptionSync.class);
		xstream.alias("chargeCost", ChargeCostSync.class);
		xstream.alias("checklist", ChecklistSync.class);
		xstream.alias("employee", EmployeeSync.class);
		xstream.alias("followup", FollowupSync.class);
		xstream.alias("income", IncomeSync.class);
		xstream.alias("issuelog", IssuelogSync.class);
		xstream.alias("milestone", MilestonSync.class);
		xstream.alias("projectCharter", ProjectCharterSync.class);
		xstream.alias("project", ProjectSync.class);
		xstream.alias("riskRegister", RiskSync.class);
		xstream.alias("stakeholder", StakeholderSync.class);
		xstream.alias("wbsnode", WbsnodeSync.class);
		xstream.alias("workingCost", WorkingCostSync.class);
		xstream.alias("label", LabelSync.class);
		xstream.alias("stageGate", StageGateSync.class);
		
		return xstream;
	}
	
	/**
	 * Synchronize data
	 * @param project
	 * @param projectSync
	 * @return
	 */
	public static Project synchronize(Project project, ProjectSync projectSync) {
		
		project.setProjectName(projectSync.getProjectName());
		project.setStatus(projectSync.getStatus());
		project.setRisk(projectSync.getRisk());
		project.setPriority(projectSync.getPriority());
		project.setBac(projectSync.getBac());
		project.setNetIncome(projectSync.getNetIncome());
		project.setTcv(projectSync.getTcv());
		project.setInitDate(projectSync.getInitDate());
		project.setEndDate(projectSync.getEndDate());
		project.setDuration(projectSync.getDuration());
		project.setEffort(projectSync.getEffort());
		project.setPlannedFinishDate(projectSync.getPlannedFinishDate());
		project.setPlanDate(projectSync.getPlanDate());
		project.setExecDate(projectSync.getExecDate());
		project.setPlannedInitDate(projectSync.getPlannedInitDate());
		project.setCloseComments(projectSync.getCloseComments());
		project.setCloseStakeholderComments(projectSync.getCloseStakeholderComments());
		project.setCloseUrlLessons(projectSync.getCloseUrlLessons());
		project.setCloseLessons(projectSync.getCloseLessons());
		project.setInternalProject(projectSync.getInternalProject());
		project.setProjectDoc(projectSync.getProjectDoc());
		project.setBudgetYear(projectSync.getBudgetYear());
		project.setChartLabel(projectSync.getChartLabel());
		project.setProbability(projectSync.getProbability());
		project.setIsGeoSelling(projectSync.getIsGeoSelling());
		project.setInvestmentStatus(projectSync.getInvestmentStatus());
		project.setSended(projectSync.getSended());
		project.setNumCompetitors(projectSync.getNumCompetitors());
		project.setFinalPosition(projectSync.getFinalPosition());
		project.setClientComments(projectSync.getClientComments());
		project.setCanceledComments(projectSync.getCanceledComments());
		project.setComments(projectSync.getComments());
		project.setLinkDoc(projectSync.getLinkDoc());
		project.setAccountingCode(projectSync.getAccountingCode());
		project.setStatusDate(projectSync.getStatusDate());
		project.setLowerThreshold(projectSync.getLowerThreshold());
		project.setUpperThreshold(projectSync.getUpperThreshold());
		project.setLinkComment(projectSync.getLinkComment());
		project.setScopeStatement(projectSync.getScopeStatement());
		project.setHdDescription(projectSync.getHdDescription());
		project.setRag(projectSync.getRag());
		project.setCurrencyOptional1(projectSync.getCurrencyOptional1());
		project.setCurrencyOptional2(projectSync.getCurrencyOptional2());
		project.setCurrencyOptional3(projectSync.getCurrencyOptional3());
		project.setCurrencyOptional4(projectSync.getCurrencyOptional4());
		project.setStartDate(projectSync.getStartDate());
		project.setFinishDate(projectSync.getFinishDate());
		project.setPoc(projectSync.getPoc());
		project.setKpiStatus(projectSync.getKpiStatus());
		
		return project;
	}

	/**
	 * Synchronize data 
	 * @param projectcharter
	 * @param projectSync
	 * @return
	 */
	public static Projectcharter synchronize(Projectcharter projectcharter,
			ProjectSync projectSync) {
		
		if (projectSync.getProjectCharter() != null) {
			projectcharter.setBusinessNeed(projectSync.getProjectCharter().getBusinessNeed());
	    	projectcharter.setProjectObjectives(projectSync.getProjectCharter().getProjectObjectives());
	    	projectcharter.setSucessCriteria(projectSync.getProjectCharter().getSucessCriteria());
	    	projectcharter.setMainConstraints(projectSync.getProjectCharter().getMainConstraints());
	    	projectcharter.setMilestones(projectSync.getProjectCharter().getMilestones());
	    	projectcharter.setMainAssumptions(projectSync.getProjectCharter().getMainAssumptions());
	    	projectcharter.setMainRisks(projectSync.getProjectCharter().getMainRisks());
	    	projectcharter.setExclusions(projectSync.getProjectCharter().getExclusions());
	    	projectcharter.setMainDeliverables(projectSync.getProjectCharter().getMainDeliverables());
		}
		
		return projectcharter;
	}

	/**
	 * Create wbsnode
	 * @param node
	 * @return
	 */
	public static Wbsnode createWbsnode(WbsnodeSync node) {
		
		Wbsnode wbsnode = new Wbsnode();
		
		wbsnode.setCode(node.getCode());
    	wbsnode.setName(node.getName());
    	wbsnode.setDescription(node.getDescription());
    	wbsnode.setIsControlAccount(node.getIsControlAccount());
    	wbsnode.setBudget(node.getBudget());
    	
		return wbsnode;
	}

	/**
	 * Create activity
	 * @param activity
	 * @return
	 */
	public static Projectactivity createActivity(ActivitySync activity) {
		
		Projectactivity projectactivity = new Projectactivity();
		projectactivity.setActivityName(activity.getActivityName());
    	projectactivity.setWbsdictionary(activity.getWbsdictionary());
    	projectactivity.setPlanInitDate(activity.getPlanInitDate());
    	projectactivity.setActualInitDate(activity.getActualInitDate());
    	projectactivity.setPlanEndDate(activity.getPlanEndDate());
    	projectactivity.setActualEndDate(activity.getActualEndDate());
    	projectactivity.setEv(activity.getEv());
    	projectactivity.setPv(activity.getPv());
    	projectactivity.setAc(activity.getAc());
    	projectactivity.setPoc(activity.getPoc());
    	projectactivity.setCommentsPoc(activity.getCommentsPoc());
    	projectactivity.setCommentsDates(activity.getCommentsDates());
    	
		return projectactivity;
	}

	/**
	 * Create Milestone
	 * @param milestoneSync
	 * @return
	 */
	public static Milestones createMilestone(MilestonSync milestoneSync) {
		
		Milestones milestone = new Milestones();
		milestone.setDescription(milestoneSync.getDescription());
    	milestone.setLabel(milestoneSync.getLabel());
    	milestone.setReportType(milestoneSync.getReportType());
    	milestone.setPlanned(milestoneSync.getPlanned());
    	milestone.setEstimatedDate(milestoneSync.getEstimatedDate() == null ? milestoneSync.getPlanned() : milestoneSync.getEstimatedDate());
    	milestone.setAchieved(milestoneSync.getAchieved());
    	milestone.setAchievedComments(milestoneSync.getAchievedComments());
    	milestone.setNotify(milestoneSync.getNotify());
    	milestone.setNotifyDays(milestoneSync.getNotifyDays());
    	milestone.setNotificationText(milestoneSync.getNotificationText());
    	milestone.setNotifyDate(milestoneSync.getNotifyDate());
    	milestone.setName(milestoneSync.getName());
		
		return milestone;
	}

	/**
	 * Create CheckList
	 * @param checklistSync
	 * @return
	 */
	public static Checklist createCheckList(ChecklistSync checklistSync) {
		
		Checklist checklist = new Checklist();
		checklist.setCode(checklistSync.getCode());
    	checklist.setDescription(checklistSync.getDescription());
    	checklist.setPercentageComplete(checklistSync.getPercentageComplete());
    	checklist.setActualizationDate(checklistSync.getActualizationDate());
    	checklist.setName(checklistSync.getName());
    	checklist.setComments(checklistSync.getComments());
    	
		return checklist;
	}

	/**
	 * Create income
	 * @param incomeSync
	 * @return
	 */
	public static Incomes createIncome(IncomeSync incomeSync) {
		
		Incomes income = new Incomes();
		income.setPlannedBillDate(incomeSync.getPlannedBillDate());
    	income.setPlannedBillAmmount(incomeSync.getPlannedBillAmmount());
    	income.setActualBillDate(incomeSync.getActualBillDate());
    	income.setActualBillAmmount(incomeSync.getActualBillAmmount());
    	income.setPlannedDescription(incomeSync.getPlannedDescription());
    	income.setActualDescription(incomeSync.getActualDescription());
    	income.setActualPaymentDate(incomeSync.getActualPaymentDate());
    	
		return income;
	}

	/**
	 * Create stakeholder
	 * @param stakeholderSync
	 * @return
	 */
	public static Stakeholder createStakeholder(StakeholderSync stakeholderSync, Session session) {
		
		Stakeholder stakeholder = new Stakeholder();
		stakeholder.setProjectRole(stakeholderSync.getProjectRole());
    	stakeholder.setRequirements(stakeholderSync.getRequirements());
    	stakeholder.setExpectations(stakeholderSync.getExpectations());
    	stakeholder.setInfluence(stakeholderSync.getInfluence());
    	stakeholder.setMgtStrategy(stakeholderSync.getMgtStrategy());

    	stakeholder.setType(stakeholderSync.getType());
    	stakeholder.setDepartment(stakeholderSync.getDepartment());
    	stakeholder.setOrderToShow(stakeholderSync.getOrderToShow());
    	stakeholder.setComments(stakeholderSync.getComments());
		stakeholder.setContactName(stakeholderSync.getContactName());

		// Instance DAO for find stakeholder classification
		StakeholderclassificationDAO stakeholderclassificationDAO = new StakeholderclassificationDAO(session);

		// Set stakeholder classification if exist
		stakeholder.setStakeholderclassification(stakeholderclassificationDAO.findByName(stakeholderSync.getClassification()));

		return stakeholder;
	}

	/**
	 * Create project followup
	 * @param followupSync
	 * @return
	 */
	public static Projectfollowup createFollowup(FollowupSync followupSync) {
		
		Projectfollowup projectfollowup = new Projectfollowup();
		projectfollowup.setFollowupDate(followupSync.getFollowupDate());
    	projectfollowup.setEv(followupSync.getEv());
    	projectfollowup.setPv(followupSync.getPv());
    	projectfollowup.setAc(followupSync.getAc());
    	projectfollowup.setGeneralComments(followupSync.getGeneralComments());
    	projectfollowup.setRisksComments(followupSync.getRisksComments());
    	projectfollowup.setCostComments(followupSync.getCostComments());
    	projectfollowup.setScheduleComments(followupSync.getScheduleComments());
    	projectfollowup.setGeneralFlag(followupSync.getGeneralFlag());
    	projectfollowup.setRiskFlag(followupSync.getRiskFlag());
    	projectfollowup.setCostFlag(followupSync.getCostFlag());
    	projectfollowup.setScheduleFlag(followupSync.getScheduleFlag());
    	projectfollowup.setPerformanceDoc(followupSync.getPerformanceDoc());
    	projectfollowup.setRiskRating(followupSync.getRiskRating());
    	
		return projectfollowup;
	}

	/**
	 * Create issue log
	 * @param issuelogSync
	 * @return
	 */
	public static Issuelog createIssueLog(IssuelogSync issuelogSync) {
		
		Issuelog issuelog = new Issuelog();
		issuelog.setDescription(issuelogSync.getDescription());
		issuelog.setPriority(issuelogSync.getPriority());
		issuelog.setDateLogged(issuelogSync.getDateLogged());
		issuelog.setOriginator(issuelogSync.getOriginator());
		issuelog.setAssignedTo(issuelogSync.getAssignedTo());
		issuelog.setTargetDate(issuelogSync.getTargetDate());
		issuelog.setResolution(issuelogSync.getResolution());
		issuelog.setDateClosed(issuelogSync.getDateClosed());
		issuelog.setIssueDoc(issuelogSync.getIssueDoc());
		
		return issuelog;
	}

	/**
	 * Create assumtion register
	 * @param assumptionSync
	 * @return
	 */
	public static Assumptionregister createAssumption(
			AssumptionSync assumptionSync) {
		
		Assumptionregister assumptionregister = new Assumptionregister();
		assumptionregister.setDescription(assumptionSync.getDescription());
		assumptionregister.setAssumptionCode(assumptionSync.getAssumptionCode());
		assumptionregister.setAssumptionName(assumptionSync.getAssumptionName());
		assumptionregister.setOriginator(assumptionSync.getOriginator());
		assumptionregister.setAssumptionDoc(assumptionSync.getAssumptionDoc());
		
		return assumptionregister;
	}

	/**
	 * Create assumption log
	 * @param assumptionlogSync
	 * @return
	 */
	public static Assumptionreassessmentlog createAssumtionLog(
			AssumptionlogSync assumptionlogSync) {
		
		Assumptionreassessmentlog assumptionreassessmentlog = new Assumptionreassessmentlog();
		assumptionreassessmentlog.setAssumptionDate(assumptionlogSync.getAssumptionDate());
		assumptionreassessmentlog.setAssumptionChange(assumptionlogSync.getAssumptionChange());
		
		return assumptionreassessmentlog;
	}

	/**
	 * Create risk register
	 * @param riskSync
	 * @return
	 */
	public static Riskregister createRisk(RiskSync riskSync) {
		
		Riskregister riskregister = new Riskregister();
		riskregister.setRiskCode(riskSync.getRiskCode());
		riskregister.setRiskName(riskSync.getRiskName());
		riskregister.setOwner(riskSync.getOwner());
		riskregister.setDateRaised(riskSync.getDateRaised());
		riskregister.setPotentialCost(riskSync.getPotentialCost());
		riskregister.setPotentialDelay(riskSync.getPotentialDelay());
		riskregister.setRiskTrigger(riskSync.getRiskTrigger());
		riskregister.setDescription(riskSync.getDescription());
		riskregister.setProbability(riskSync.getProbability());
		riskregister.setImpact(riskSync.getImpact());
		riskregister.setMaterialized(riskSync.getMaterialized());
		riskregister.setMitigationActionsRequired(riskSync.getMitigationActionsRequired());
		riskregister.setContingencyActionsRequired(riskSync.getContingencyActionsRequired());
		riskregister.setActualMaterializationCost(riskSync.getActualMaterializationCost());
		riskregister.setActualMaterializationDelay(riskSync.getActualMaterializationDelay());
		riskregister.setFinalComments(riskSync.getFinalComments());
		riskregister.setRiskDoc(riskSync.getRiskDoc());
		riskregister.setRiskType(riskSync.getRiskType());
		riskregister.setPlannedMitigationCost(riskSync.getPlannedMitigationCost());
		riskregister.setPlannedContingencyCost(riskSync.getPlannedContingencyCost());
		riskregister.setClosed(riskSync.getClosed());
		riskregister.setDateMaterialization(riskSync.getDateMaterialization());
		riskregister.setDueDate(riskSync.getDueDate());
		riskregister.setStatus(riskSync.getStatus());
		riskregister.setResponseDescription(riskSync.getResponseDescription());
		riskregister.setResidualRisk(riskSync.getResidualRisk());
		riskregister.setResidualCost(riskSync.getResidualCost());
		
		if (riskSync.getIdRiskCategory() != null) {
			riskregister.setRiskcategories(new Riskcategories(riskSync.getIdRiskCategory()));
		}
		
		return riskregister;
	}

	/**
	 * Create charge cost
	 * @param chargeCostSync
	 * @return
	 */
	public static Chargescosts createChargeCost(ChargeCostSync chargeCostSync) {
		
		Chargescosts chargescosts = new Chargescosts();
		if (chargeCostSync.getIdCurrency() != null) {
			chargescosts.setCurrency(new Currency(chargeCostSync.getIdCurrency()));
		}
		chargescosts.setName(chargeCostSync.getName());
		chargescosts.setCost(chargeCostSync.getCost());
		chargescosts.setIdChargeType(chargeCostSync.getIdChargeType());
		
		return chargescosts;
	}

	/**
	 * Create working cost
	 * @param workingCostSync
	 * @return
	 */
	public static Workingcosts createWorkingcost(WorkingCostSync workingCostSync) {
		
		Workingcosts workingcosts = new Workingcosts();
		if (workingCostSync.getIdCurrency() != null) {
			workingcosts.setCurrency(new Currency(workingCostSync.getIdCurrency()));
		}
		workingcosts.setResourceName(workingCostSync.getResourceName());
		workingcosts.setResourceDepartment(workingCostSync.getResourceDepartment());
		workingcosts.setEffort(workingCostSync.getEffort());
		workingcosts.setRate(workingCostSync.getRate());
		workingcosts.setWorkCost(workingCostSync.getWorkCost());
		workingcosts.setQ1(workingCostSync.getQ1());
		workingcosts.setQ2(workingCostSync.getQ2());
		workingcosts.setQ3(workingCostSync.getQ3());
		workingcosts.setQ4(workingCostSync.getQ4());
		workingcosts.setRealEffort(workingCostSync.getRealEffort());
		
		return workingcosts;
	}

	/**
	 * Create label
	 * 
	 * @param session
	 * @param labelSync
	 * @return
	 */
	public static Projectlabel createlabel(Session session, LabelSync labelSync) {
		
		LabelDAO dao = new LabelDAO(session);
		
		List<Label> labels = dao.findByRelation(Label.NAME, labelSync.getName());
		
		Projectlabel projectlabel = null;
		
		if (ValidateUtil.isNotNull(labels)) {
		
			projectlabel = new Projectlabel();
			projectlabel.setLabel(labels.get(0));
		}
		
		return projectlabel;
	}
}

