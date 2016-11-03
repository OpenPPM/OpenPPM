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
 * File: XStreamUtil.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:23:30
 */

package es.sm2.openppm.core.audit;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import es.sm2.openppm.core.dao.LabelDAO;
import es.sm2.openppm.core.dao.StakeholderclassificationDAO;
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
import es.sm2.openppm.core.audit.model.ActivityAudit;
import es.sm2.openppm.core.audit.model.AssumptionAudit;
import es.sm2.openppm.core.audit.model.AssumptionlogAudit;
import es.sm2.openppm.core.audit.model.ChargeCostAudit;
import es.sm2.openppm.core.audit.model.ChecklistAudit;
import es.sm2.openppm.core.audit.model.EmployeeAudit;
import es.sm2.openppm.core.audit.model.FollowupAudit;
import es.sm2.openppm.core.audit.model.IncomeAudit;
import es.sm2.openppm.core.audit.model.IssuelogAudit;
import es.sm2.openppm.core.audit.model.LabelAudit;
import es.sm2.openppm.core.audit.model.MilestonAudit;
import es.sm2.openppm.core.audit.model.ProjectCharterAudit;
import es.sm2.openppm.core.audit.model.ProjectAudit;
import es.sm2.openppm.core.audit.model.RiskAudit;
import es.sm2.openppm.core.audit.model.StageGateAudit;
import es.sm2.openppm.core.audit.model.StakeholderAudit;
import es.sm2.openppm.core.audit.model.WbsnodeAudit;
import es.sm2.openppm.core.audit.model.WorkingCostAudit;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.hibernate.Session;

import java.util.List;

public class XStreamAuditUtil {

	/**
	 * Create instance
	 * @return
	 */
	public static XStream createXStrean() {
		
		XStream xstream = new XStream(new DomDriver("UTF-8"));
		
		xstream.alias("activity", ActivityAudit.class);
		xstream.alias("assumptionlog", AssumptionlogAudit.class);
		xstream.alias("assumption", AssumptionAudit.class);
		xstream.alias("chargeCost", ChargeCostAudit.class);
		xstream.alias("checklist", ChecklistAudit.class);
		xstream.alias("employee", EmployeeAudit.class);
		xstream.alias("followup", FollowupAudit.class);
		xstream.alias("income", IncomeAudit.class);
		xstream.alias("issuelog", IssuelogAudit.class);
		xstream.alias("milestone", MilestonAudit.class);
		xstream.alias("projectCharter", ProjectCharterAudit.class);
		xstream.alias("project", ProjectAudit.class);
		xstream.alias("riskRegister", RiskAudit.class);
		xstream.alias("stakeholder", StakeholderAudit.class);
		xstream.alias("wbsnode", WbsnodeAudit.class);
		xstream.alias("workingCost", WorkingCostAudit.class);
		xstream.alias("label", LabelAudit.class);
		xstream.alias("stageGate", StageGateAudit.class);
		
		return xstream;
	}
	
	/**
	 * Synchronize data
	 * @param project
	 * @param projectAudit
	 * @return
	 */
	public static Project synchronize(Project project, ProjectAudit projectAudit) {
		
		project.setProjectName(projectAudit.getProjectName());
		project.setStatus(projectAudit.getStatus());
		project.setRisk(projectAudit.getRisk());
		project.setPriority(projectAudit.getPriority());
		project.setBac(projectAudit.getBac());
		project.setNetIncome(projectAudit.getNetIncome());
		project.setTcv(projectAudit.getTcv());
		project.setInitDate(projectAudit.getInitDate());
		project.setEndDate(projectAudit.getEndDate());
		project.setDuration(projectAudit.getDuration());
		project.setEffort(projectAudit.getEffort());
		project.setPlannedFinishDate(projectAudit.getPlannedFinishDate());
		project.setPlanDate(projectAudit.getPlanDate());
		project.setExecDate(projectAudit.getExecDate());
		project.setPlannedInitDate(projectAudit.getPlannedInitDate());
		project.setCloseComments(projectAudit.getCloseComments());
		project.setCloseStakeholderComments(projectAudit.getCloseStakeholderComments());
		project.setCloseUrlLessons(projectAudit.getCloseUrlLessons());
		project.setCloseLessons(projectAudit.getCloseLessons());
		project.setInternalProject(projectAudit.getInternalProject());
		project.setProjectDoc(projectAudit.getProjectDoc());
		project.setBudgetYear(projectAudit.getBudgetYear());
		project.setChartLabel(projectAudit.getChartLabel());
		project.setProbability(projectAudit.getProbability());
		project.setIsGeoSelling(projectAudit.getIsGeoSelling());
		project.setInvestmentStatus(projectAudit.getInvestmentStatus());
		project.setSended(projectAudit.getSended());
		project.setNumCompetitors(projectAudit.getNumCompetitors());
		project.setFinalPosition(projectAudit.getFinalPosition());
		project.setClientComments(projectAudit.getClientComments());
		project.setCanceledComments(projectAudit.getCanceledComments());
		project.setComments(projectAudit.getComments());
		project.setLinkDoc(projectAudit.getLinkDoc());
		project.setAccountingCode(projectAudit.getAccountingCode());
		project.setStatusDate(projectAudit.getStatusDate());
		project.setLowerThreshold(projectAudit.getLowerThreshold());
		project.setUpperThreshold(projectAudit.getUpperThreshold());
		project.setLinkComment(projectAudit.getLinkComment());
		project.setScopeStatement(projectAudit.getScopeStatement());
		project.setHdDescription(projectAudit.getHdDescription());
		project.setRag(projectAudit.getRag());
		project.setCurrencyOptional1(projectAudit.getCurrencyOptional1());
		project.setCurrencyOptional2(projectAudit.getCurrencyOptional2());
		project.setCurrencyOptional3(projectAudit.getCurrencyOptional3());
		project.setCurrencyOptional4(projectAudit.getCurrencyOptional4());
		project.setStartDate(projectAudit.getStartDate());
		project.setFinishDate(projectAudit.getFinishDate());
		project.setPoc(projectAudit.getPoc());
		project.setKpiStatus(projectAudit.getKpiStatus());
		
		return project;
	}

	/**
	 * Synchronize data 
	 * @param projectcharter
	 * @param projectAudit
	 * @return
	 */
	public static Projectcharter synchronize(Projectcharter projectcharter,
			ProjectAudit projectAudit) {
		
		if (projectAudit.getProjectCharter() != null) {
			projectcharter.setBusinessNeed(projectAudit.getProjectCharter().getBusinessNeed());
	    	projectcharter.setProjectObjectives(projectAudit.getProjectCharter().getProjectObjectives());
	    	projectcharter.setSucessCriteria(projectAudit.getProjectCharter().getSucessCriteria());
	    	projectcharter.setMainConstraints(projectAudit.getProjectCharter().getMainConstraints());
	    	projectcharter.setMilestones(projectAudit.getProjectCharter().getMilestones());
	    	projectcharter.setMainAssumptions(projectAudit.getProjectCharter().getMainAssumptions());
	    	projectcharter.setMainRisks(projectAudit.getProjectCharter().getMainRisks());
	    	projectcharter.setExclusions(projectAudit.getProjectCharter().getExclusions());
	    	projectcharter.setMainDeliverables(projectAudit.getProjectCharter().getMainDeliverables());
		}
		
		return projectcharter;
	}

	/**
	 * Create wbsnode
	 * @param node
	 * @return
	 */
	public static Wbsnode createWbsnode(WbsnodeAudit node) {
		
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
	public static Projectactivity createActivity(ActivityAudit activity) {
		
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
	public static Milestones createMilestone(MilestonAudit milestoneSync) {
		
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
	 * @param checklistAudit
	 * @return
	 */
	public static Checklist createCheckList(ChecklistAudit checklistAudit) {
		
		Checklist checklist = new Checklist();
		checklist.setCode(checklistAudit.getCode());
    	checklist.setDescription(checklistAudit.getDescription());
    	checklist.setPercentageComplete(checklistAudit.getPercentageComplete());
    	checklist.setActualizationDate(checklistAudit.getActualizationDate());
    	checklist.setName(checklistAudit.getName());
    	checklist.setComments(checklistAudit.getComments());
    	
		return checklist;
	}

	/**
	 * Create income
	 * @param incomeAudit
	 * @return
	 */
	public static Incomes createIncome(IncomeAudit incomeAudit) {
		
		Incomes income = new Incomes();
		income.setPlannedBillDate(incomeAudit.getPlannedBillDate());
    	income.setPlannedBillAmmount(incomeAudit.getPlannedBillAmmount());
    	income.setActualBillDate(incomeAudit.getActualBillDate());
    	income.setActualBillAmmount(incomeAudit.getActualBillAmmount());
    	income.setPlannedDescription(incomeAudit.getPlannedDescription());
    	income.setActualDescription(incomeAudit.getActualDescription());
    	income.setActualPaymentDate(incomeAudit.getActualPaymentDate());
    	
		return income;
	}

	/**
	 * Create stakeholder
	 * @param stakeholderAudit
	 * @return
	 */
	public static Stakeholder createStakeholder(StakeholderAudit stakeholderAudit, Session session) {
		
		Stakeholder stakeholder = new Stakeholder();
		stakeholder.setProjectRole(stakeholderAudit.getProjectRole());
    	stakeholder.setRequirements(stakeholderAudit.getRequirements());
    	stakeholder.setExpectations(stakeholderAudit.getExpectations());
    	stakeholder.setInfluence(stakeholderAudit.getInfluence());
    	stakeholder.setMgtStrategy(stakeholderAudit.getMgtStrategy());

    	stakeholder.setType(stakeholderAudit.getType());
    	stakeholder.setDepartment(stakeholderAudit.getDepartment());
    	stakeholder.setOrderToShow(stakeholderAudit.getOrderToShow());
    	stakeholder.setComments(stakeholderAudit.getComments());
		stakeholder.setContactName(stakeholderAudit.getContactName());

		// Instance DAO for find stakeholder classification
		StakeholderclassificationDAO stakeholderclassificationDAO = new StakeholderclassificationDAO(session);

		// Set stakeholder classification if exist
		stakeholder.setStakeholderclassification(stakeholderclassificationDAO.findByName(stakeholderAudit.getClassification()));

		return stakeholder;
	}

	/**
	 * Create project followup
	 * @param followupAudit
	 * @return
	 */
	public static Projectfollowup createFollowup(FollowupAudit followupAudit) {
		
		Projectfollowup projectfollowup = new Projectfollowup();
		projectfollowup.setFollowupDate(followupAudit.getFollowupDate());
    	projectfollowup.setEv(followupAudit.getEv());
    	projectfollowup.setPv(followupAudit.getPv());
    	projectfollowup.setAc(followupAudit.getAc());
    	projectfollowup.setGeneralComments(followupAudit.getGeneralComments());
    	projectfollowup.setRisksComments(followupAudit.getRisksComments());
    	projectfollowup.setCostComments(followupAudit.getCostComments());
    	projectfollowup.setScheduleComments(followupAudit.getScheduleComments());
    	projectfollowup.setGeneralFlag(followupAudit.getGeneralFlag());
    	projectfollowup.setRiskFlag(followupAudit.getRiskFlag());
    	projectfollowup.setCostFlag(followupAudit.getCostFlag());
    	projectfollowup.setScheduleFlag(followupAudit.getScheduleFlag());
    	projectfollowup.setPerformanceDoc(followupAudit.getPerformanceDoc());
    	projectfollowup.setRiskRating(followupAudit.getRiskRating());
    	
		return projectfollowup;
	}

	/**
	 * Create issue log
	 * @param issuelogAudit
	 * @return
	 */
	public static Issuelog createIssueLog(IssuelogAudit issuelogAudit) {
		
		Issuelog issuelog = new Issuelog();
		issuelog.setDescription(issuelogAudit.getDescription());
		issuelog.setPriority(issuelogAudit.getPriority());
		issuelog.setDateLogged(issuelogAudit.getDateLogged());
		issuelog.setOriginator(issuelogAudit.getOriginator());
		issuelog.setAssignedTo(issuelogAudit.getAssignedTo());
		issuelog.setTargetDate(issuelogAudit.getTargetDate());
		issuelog.setResolution(issuelogAudit.getResolution());
		issuelog.setDateClosed(issuelogAudit.getDateClosed());
		issuelog.setIssueDoc(issuelogAudit.getIssueDoc());
		
		return issuelog;
	}

	/**
	 * Create assumtion register
	 * @param assumptionAudit
	 * @return
	 */
	public static Assumptionregister createAssumption(
			AssumptionAudit assumptionAudit) {
		
		Assumptionregister assumptionregister = new Assumptionregister();
		assumptionregister.setDescription(assumptionAudit.getDescription());
		assumptionregister.setAssumptionCode(assumptionAudit.getAssumptionCode());
		assumptionregister.setAssumptionName(assumptionAudit.getAssumptionName());
		assumptionregister.setOriginator(assumptionAudit.getOriginator());
		assumptionregister.setAssumptionDoc(assumptionAudit.getAssumptionDoc());
		
		return assumptionregister;
	}

	/**
	 * Create assumption log
	 * @param assumptionlogSync
	 * @return
	 */
	public static Assumptionreassessmentlog createAssumtionLog(
			AssumptionlogAudit assumptionlogSync) {
		
		Assumptionreassessmentlog assumptionreassessmentlog = new Assumptionreassessmentlog();
		assumptionreassessmentlog.setAssumptionDate(assumptionlogSync.getAssumptionDate());
		assumptionreassessmentlog.setAssumptionChange(assumptionlogSync.getAssumptionChange());
		
		return assumptionreassessmentlog;
	}

	/**
	 * Create risk register
	 * @param riskAudit
	 * @return
	 */
	public static Riskregister createRisk(RiskAudit riskAudit) {
		
		Riskregister riskregister = new Riskregister();
		riskregister.setRiskCode(riskAudit.getRiskCode());
		riskregister.setRiskName(riskAudit.getRiskName());
		riskregister.setOwner(riskAudit.getOwner());
		riskregister.setDateRaised(riskAudit.getDateRaised());
		riskregister.setPotentialCost(riskAudit.getPotentialCost());
		riskregister.setPotentialDelay(riskAudit.getPotentialDelay());
		riskregister.setRiskTrigger(riskAudit.getRiskTrigger());
		riskregister.setDescription(riskAudit.getDescription());
		riskregister.setProbability(riskAudit.getProbability());
		riskregister.setImpact(riskAudit.getImpact());
		riskregister.setMaterialized(riskAudit.getMaterialized());
		riskregister.setMitigationActionsRequired(riskAudit.getMitigationActionsRequired());
		riskregister.setContingencyActionsRequired(riskAudit.getContingencyActionsRequired());
		riskregister.setActualMaterializationCost(riskAudit.getActualMaterializationCost());
		riskregister.setActualMaterializationDelay(riskAudit.getActualMaterializationDelay());
		riskregister.setFinalComments(riskAudit.getFinalComments());
		riskregister.setRiskDoc(riskAudit.getRiskDoc());
		riskregister.setRiskType(riskAudit.getRiskType());
		riskregister.setPlannedMitigationCost(riskAudit.getPlannedMitigationCost());
		riskregister.setPlannedContingencyCost(riskAudit.getPlannedContingencyCost());
		riskregister.setClosed(riskAudit.getClosed());
		riskregister.setDateMaterialization(riskAudit.getDateMaterialization());
		riskregister.setDueDate(riskAudit.getDueDate());
		riskregister.setStatus(riskAudit.getStatus());
		riskregister.setResponseDescription(riskAudit.getResponseDescription());
		riskregister.setResidualRisk(riskAudit.getResidualRisk());
		riskregister.setResidualCost(riskAudit.getResidualCost());
		
		if (riskAudit.getIdRiskCategory() != null) {
			riskregister.setRiskcategories(new Riskcategories(riskAudit.getIdRiskCategory()));
		}
		
		return riskregister;
	}

	/**
	 * Create charge cost
	 * @param chargeCostAudit
	 * @return
	 */
	public static Chargescosts createChargeCost(ChargeCostAudit chargeCostAudit) {
		
		Chargescosts chargescosts = new Chargescosts();
		if (chargeCostAudit.getIdCurrency() != null) {
			chargescosts.setCurrency(new Currency(chargeCostAudit.getIdCurrency()));
		}
		chargescosts.setName(chargeCostAudit.getName());
		chargescosts.setCost(chargeCostAudit.getCost());
		chargescosts.setIdChargeType(chargeCostAudit.getIdChargeType());
		
		return chargescosts;
	}

	/**
	 * Create working cost
	 * @param workingCostAudit
	 * @return
	 */
	public static Workingcosts createWorkingcost(WorkingCostAudit workingCostAudit) {
		
		Workingcosts workingcosts = new Workingcosts();
		if (workingCostAudit.getIdCurrency() != null) {
			workingcosts.setCurrency(new Currency(workingCostAudit.getIdCurrency()));
		}
		workingcosts.setResourceName(workingCostAudit.getResourceName());
		workingcosts.setResourceDepartment(workingCostAudit.getResourceDepartment());
		workingcosts.setEffort(workingCostAudit.getEffort());
		workingcosts.setRate(workingCostAudit.getRate());
		workingcosts.setWorkCost(workingCostAudit.getWorkCost());
		workingcosts.setQ1(workingCostAudit.getQ1());
		workingcosts.setQ2(workingCostAudit.getQ2());
		workingcosts.setQ3(workingCostAudit.getQ3());
		workingcosts.setQ4(workingCostAudit.getQ4());
		workingcosts.setRealEffort(workingCostAudit.getRealEffort());
		
		return workingcosts;
	}

	/**
	 * Create label
	 * 
	 * @param session
	 * @param labelAudit
	 * @return
	 */
	public static Projectlabel createlabel(Session session, LabelAudit labelAudit) {
		
		LabelDAO dao = new LabelDAO(session);
		
		List<Label> labels = dao.findByRelation(Label.NAME, labelAudit.getName());
		
		Projectlabel projectlabel = null;
		
		if (ValidateUtil.isNotNull(labels)) {
		
			projectlabel = new Projectlabel();
			projectlabel.setLabel(labels.get(0));
		}
		
		return projectlabel;
	}
}

