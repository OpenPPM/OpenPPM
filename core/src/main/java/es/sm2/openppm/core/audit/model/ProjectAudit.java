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
 * File: ProjectSync.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:24:00
 */

package es.sm2.openppm.core.audit.model;

import es.sm2.openppm.core.dao.LabelDAO;
import es.sm2.openppm.core.model.impl.Assumptionregister;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Incomes;
import es.sm2.openppm.core.model.impl.Issuelog;
import es.sm2.openppm.core.model.impl.Label;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.impl.Workingcosts;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectAudit {

    private String geography;
    private EmployeeAudit employeeByInvestmentManager;
    private String performingorg;
    private String program;
    private String customer;
    private String category;
    private EmployeeAudit employeeBySponsor;
    private EmployeeAudit employeeByFunctionalManager;
    private EmployeeAudit employeeByProjectManager;
    private String contracttype;
    private String projectName;
    private String status;
    private Character risk;
    private Integer priority;
    private Double bac;
    private Double netIncome;
    private Double tcv;
    private Date initDate;
    private Date endDate;
    private Integer duration;
    private Integer effort;
    private Date plannedFinishDate;
    private Date planDate;
    private Date execDate;
    private Date plannedInitDate;
    private String closeComments;
    private String closeStakeholderComments;
    private String closeUrlLessons;
    private String closeLessons;
    private Boolean internalProject;
    private String projectDoc;
    private Integer budgetYear;
    private String chartLabel;
    private Integer probability;
    private Boolean isGeoSelling;
    private String investmentStatus;
    private Boolean sended;
    private Integer numCompetitors;
    private Integer finalPosition;
    private String clientComments;
    private String canceledComments;
    private String comments;
    private String linkDoc;
    private String accountingCode;
    private Date statusDate;
    private Double lowerThreshold;
    private Double upperThreshold;
    private String linkComment;
    private String scopeStatement;
    private String hdDescription;
    private Character rag;
    private Double currencyOptional1;
    private Double currencyOptional2;
    private Double currencyOptional3;
    private Double currencyOptional4;
    private Date startDate;
    private Date finishDate;
    private Double poc;
    private String kpiStatus;
    private EntityAudit classificationlevel;
    private ProjectCharterAudit projectCharter;
    private StageGateAudit stageGate;
    private List<WbsnodeAudit> wbsnodes = new ArrayList<WbsnodeAudit>();
    private List<FollowupAudit> followups = new ArrayList<FollowupAudit>();
    private List<StakeholderAudit> stakeholders = new ArrayList<StakeholderAudit>();
    private List<IncomeAudit> incomes = new ArrayList<IncomeAudit>();
    private List<AssumptionAudit> assumptions = new ArrayList<AssumptionAudit>();
    private List<IssuelogAudit> issuelogs = new ArrayList<IssuelogAudit>();
    private List<RiskAudit> risks = new ArrayList<RiskAudit>();
    private List<ChargeCostAudit> chargeCosts = new ArrayList<ChargeCostAudit>();
    private List<WorkingCostAudit> workingCosts = new ArrayList<WorkingCostAudit>();
    private List<LabelAudit> labels = new ArrayList<LabelAudit>();

    public ProjectAudit() {
		super();
    }

	public ProjectAudit(Project project, Session session) {
		super();

		setProjectName(project.getProjectName());
		setStatus(project.getStatus());
		setRisk(project.getRisk());
		setPriority(project.getPriority());
		setBac(project.getBac());
		setNetIncome(project.getNetIncome());
		setTcv(project.getTcv());
		setInitDate(project.getInitDate());
		setEndDate(project.getEndDate());
		setDuration(project.getDuration());
		setEffort(project.getEffort());
		setPlannedFinishDate(project.getPlannedFinishDate());
		setPlanDate(project.getPlanDate());
		setExecDate(project.getExecDate());
		setPlannedInitDate(project.getPlannedInitDate());
		setCloseComments(project.getCloseComments());
		setCloseStakeholderComments(project.getCloseStakeholderComments());
		setCloseUrlLessons(project.getCloseUrlLessons());
		setCloseLessons(project.getCloseLessons());
		setInternalProject(project.getInternalProject());
		setProjectDoc(project.getProjectDoc());
		setBudgetYear(project.getBudgetYear());
		setChartLabel(project.getChartLabel());
		setProbability(project.getProbability());
		setIsGeoSelling(project.getIsGeoSelling());
		setInvestmentStatus(project.getInvestmentStatus());
		setSended(project.getSended());
		setNumCompetitors(project.getNumCompetitors());
		setFinalPosition(project.getFinalPosition());
		setClientComments(project.getClientComments());
		setCanceledComments(project.getCanceledComments());
		setComments(project.getComments());
		setLinkDoc(project.getLinkDoc());
		setAccountingCode(project.getAccountingCode());
		setStatusDate(project.getStatusDate());
		setLowerThreshold(project.getLowerThreshold());
		setUpperThreshold(project.getUpperThreshold());
		setLinkComment(project.getLinkComment());
		setScopeStatement(project.getScopeStatement());
		setHdDescription(project.getHdDescription());
		setRag(project.getRag());
		setCurrencyOptional1(project.getCurrencyOptional1());
		setCurrencyOptional2(project.getCurrencyOptional2());
		setCurrencyOptional3(project.getCurrencyOptional3());
		setCurrencyOptional4(project.getCurrencyOptional4());
		setStartDate(project.getStartDate());
		setFinishDate(project.getFinishDate());
		setPoc(project.getPoc());
		setKpiStatus(project.getKpiStatus());

		if (project.getGeography() != null) { setGeography(project.getGeography().getName()); }
		if (project.getEmployeeByInvestmentManager() != null) { setEmployeeByInvestmentManager(new EmployeeAudit(project.getEmployeeByInvestmentManager())); }
		if (project.getPerformingorg() != null) { setPerformingorg(project.getPerformingorg().getName()); }
		if (project.getProgram() != null) { setProgram(project.getProgram().getProgramName()); }
		if (project.getCustomer() != null) { setCustomer(project.getCustomer().getName()); }
		if (project.getCategory() != null) { setCategory(project.getCategory().getName()); }
		if (project.getEmployeeBySponsor() != null) { setEmployeeBySponsor(new EmployeeAudit(project.getEmployeeBySponsor())); }
		if (project.getEmployeeByFunctionalManager() != null) { setEmployeeByFunctionalManager(new EmployeeAudit(project.getEmployeeByFunctionalManager())); }
		if (project.getEmployeeByProjectManager() != null) { setEmployeeByProjectManager(new EmployeeAudit(project.getEmployeeByProjectManager())); }
		if (project.getContracttype() != null) { setContracttype(project.getContracttype().getDescription()); }
		if (project.getStagegate() != null) { setStageGate(new StageGateAudit(project.getStagegate())); }
		if (project.getClassificationlevel() != null) { setClassificationlevel(new EntityAudit(project.getClassificationlevel().getName())); }

		// GENERATE PROJECT CHARTER
		if (project.getProjectcharters() != null && !project.getProjectcharters().isEmpty()) {

			Projectcharter projectcharter = null;
			for (Projectcharter item : project.getProjectcharters()) {
				projectcharter = item;
			}

			setProjectCharter(new ProjectCharterAudit(projectcharter));
		}

		// GENERATE WBSNODES
		if (project.getWbsnodes() != null && !project.getWbsnodes().isEmpty()) {

			for (Wbsnode wbsnode: project.getWbsnodes()) {

				getWbsnodes().add(new WbsnodeAudit(wbsnode));
			}
		}

		// GENERATE STAKEHOLDERS
		if (project.getStakeholders() != null && !project.getStakeholders().isEmpty()) {

			for (Stakeholder stakeholder: project.getStakeholders()) {

				getStakeholders().add(new StakeholderAudit(stakeholder));
			}
		}

		// GENERATE FOLLOWUPS
		if (project.getProjectfollowups() != null && !project.getProjectfollowups().isEmpty()) {

			for (Projectfollowup followup: project.getProjectfollowups()) {

				getFollowups().add(new FollowupAudit(followup));
			}
		}

		// GENERATE INCOMES
		if (project.getIncomeses() != null && !project.getIncomeses().isEmpty()) {

			for (Incomes income : project.getIncomeses()) {

				getIncomes().add(new IncomeAudit(income));
			}
		}

		// GENERATE ISSUES LOG
		if (project.getIssuelogs() != null && !project.getIssuelogs().isEmpty()) {

			for (Issuelog issuelog : project.getIssuelogs()) {

				getIssuelogs().add(new IssuelogAudit(issuelog));
			}
		}

		// GENERATE ASSUMPTIONS
		if (project.getAssumptionregisters() != null && !project.getAssumptionregisters().isEmpty()) {

			for (Assumptionregister assumptionregister : project.getAssumptionregisters()) {

				getAssumptions().add(new AssumptionAudit(assumptionregister));
			}
		}

		// GENERATE RISK REGISTER
		if (project.getRiskregisters() != null && !project.getRiskregisters().isEmpty()) {

			for (Riskregister riskregister : project.getRiskregisters()) {

				getRisks().add(new RiskAudit(riskregister));
			}
		}

		// GENERATE CHARGE COSTS
		if (project.getChargescostses() != null && !project.getChargescostses().isEmpty()) {

			for (Chargescosts chargecost : project.getChargescostses()) {

				getChargeCosts().add(new ChargeCostAudit(chargecost));
			}
		}

		// GENERATE WORKING COSTS
		if (project.getWorkingcostses() != null && !project.getWorkingcostses().isEmpty()) {

			for (Workingcosts workingcost : project.getWorkingcostses()) {

				getWorkingCosts().add(new WorkingCostAudit(workingcost));
			}
		}

		// GENERATE LABELS
		LabelDAO labelDAO = new LabelDAO(session);

		List<Project> projects = new ArrayList<Project>();

		projects.add(project);

		if (!projects.isEmpty()) {

			List<Label> labels = labelDAO.findByProjects(projects);

			if (labels != null && !labels.isEmpty()) {

				for (Label label : labels) {
					getLabels().add(new LabelAudit(label));
				}
			}
		}
	}

    /**
     * Getter for property 'classificationlevel'.
     *
     * @return Value for property 'classificationlevel'.
     */
    public EntityAudit getClassificationlevel() {

        return classificationlevel;
    }

    /**
     * Setter for property 'classificationlevel'.
     *
     * @param classificationlevel Value to set for property 'classificationlevel'.
     */
    public void setClassificationlevel(EntityAudit classificationlevel) {

        this.classificationlevel = classificationlevel;
    }

    /**
	 * @return the geography
	 */
	public String getGeography() {
		return geography;
	}


	/**
	 * @param geography the geography to set
	 */
	public void setGeography(String geography) {
		this.geography = geography;
	}


	/**
	 * @return the employeeByInvestmentManager
	 */
	public EmployeeAudit getEmployeeByInvestmentManager() {
		return employeeByInvestmentManager;
	}


	/**
	 * @param employeeByInvestmentManager the employeeByInvestmentManager to set
	 */
	public void setEmployeeByInvestmentManager(EmployeeAudit employeeByInvestmentManager) {
		this.employeeByInvestmentManager = employeeByInvestmentManager;
	}


	/**
	 * @return the performingorg
	 */
	public String getPerformingorg() {
		return performingorg;
	}


	/**
	 * @param performingorg the performingorg to set
	 */
	public void setPerformingorg(String performingorg) {
		this.performingorg = performingorg;
	}


	/**
	 * @return the program
	 */
	public String getProgram() {
		return program;
	}


	/**
	 * @param program the program to set
	 */
	public void setProgram(String program) {
		this.program = program;
	}


	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}


	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}


	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}


	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}


	/**
	 * @return the employeeBySponsor
	 */
	public EmployeeAudit getEmployeeBySponsor() {
		return employeeBySponsor;
	}


	/**
	 * @param employeeBySponsor the employeeBySponsor to set
	 */
	public void setEmployeeBySponsor(EmployeeAudit employeeBySponsor) {
		this.employeeBySponsor = employeeBySponsor;
	}


	/**
	 * @return the employeeByFunctionalManager
	 */
	public EmployeeAudit getEmployeeByFunctionalManager() {
		return employeeByFunctionalManager;
	}


	/**
	 * @param employeeByFunctionalManager the employeeByFunctionalManager to set
	 */
	public void setEmployeeByFunctionalManager(
			EmployeeAudit employeeByFunctionalManager) {
		this.employeeByFunctionalManager = employeeByFunctionalManager;
	}


	/**
	 * @return the employeeByProjectManager
	 */
	public EmployeeAudit getEmployeeByProjectManager() {
		return employeeByProjectManager;
	}


	/**
	 * @param employeeByProjectManager the employeeByProjectManager to set
	 */
	public void setEmployeeByProjectManager(EmployeeAudit employeeByProjectManager) {
		this.employeeByProjectManager = employeeByProjectManager;
	}


	/**
	 * @return the contracttype
	 */
	public String getContracttype() {
		return contracttype;
	}


	/**
	 * @param contracttype the contracttype to set
	 */
	public void setContracttype(String contracttype) {
		this.contracttype = contracttype;
	}


	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}


	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * @return the risk
	 */
	public Character getRisk() {
		return risk;
	}


	/**
	 * @param risk the risk to set
	 */
	public void setRisk(Character risk) {
		this.risk = risk;
	}


	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}


	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}


	/**
	 * @return the bac
	 */
	public Double getBac() {
		return bac;
	}


	/**
	 * @param bac the bac to set
	 */
	public void setBac(Double bac) {
		this.bac = bac;
	}


	/**
	 * @return the netIncome
	 */
	public Double getNetIncome() {
		return netIncome;
	}


	/**
	 * @param netIncome the netIncome to set
	 */
	public void setNetIncome(Double netIncome) {
		this.netIncome = netIncome;
	}


	/**
	 * @return the tcv
	 */
	public Double getTcv() {
		return tcv;
	}


	/**
	 * @param tcv the tcv to set
	 */
	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}


	/**
	 * @return the initDate
	 */
	public Date getInitDate() {
		return initDate;
	}


	/**
	 * @param initDate the initDate to set
	 */
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}


	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}


	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	/**
	 * @return the duration
	 */
	public Integer getDuration() {
		return duration;
	}


	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}


	/**
	 * @return the effort
	 */
	public Integer getEffort() {
		return effort;
	}


	/**
	 * @param effort the effort to set
	 */
	public void setEffort(Integer effort) {
		this.effort = effort;
	}


	/**
	 * @return the plannedFinishDate
	 */
	public Date getPlannedFinishDate() {
		return plannedFinishDate;
	}


	/**
	 * @param plannedFinishDate the plannedFinishDate to set
	 */
	public void setPlannedFinishDate(Date plannedFinishDate) {
		this.plannedFinishDate = plannedFinishDate;
	}


	/**
	 * @return the planDate
	 */
	public Date getPlanDate() {
		return planDate;
	}


	/**
	 * @param planDate the planDate to set
	 */
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}


	/**
	 * @return the execDate
	 */
	public Date getExecDate() {
		return execDate;
	}


	/**
	 * @param execDate the execDate to set
	 */
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}


	/**
	 * @return the plannedInitDate
	 */
	public Date getPlannedInitDate() {
		return plannedInitDate;
	}


	/**
	 * @param plannedInitDate the plannedInitDate to set
	 */
	public void setPlannedInitDate(Date plannedInitDate) {
		this.plannedInitDate = plannedInitDate;
	}


	/**
	 * @return the closeComments
	 */
	public String getCloseComments() {
		return closeComments;
	}


	/**
	 * @param closeComments the closeComments to set
	 */
	public void setCloseComments(String closeComments) {
		this.closeComments = closeComments;
	}


	/**
	 * @return the closeStakeholderComments
	 */
	public String getCloseStakeholderComments() {
		return closeStakeholderComments;
	}


	/**
	 * @param closeStakeholderComments the closeStakeholderComments to set
	 */
	public void setCloseStakeholderComments(String closeStakeholderComments) {
		this.closeStakeholderComments = closeStakeholderComments;
	}


	/**
	 * @return the closeUrlLessons
	 */
	public String getCloseUrlLessons() {
		return closeUrlLessons;
	}


	/**
	 * @param closeUrlLessons the closeUrlLessons to set
	 */
	public void setCloseUrlLessons(String closeUrlLessons) {
		this.closeUrlLessons = closeUrlLessons;
	}


	/**
	 * @return the closeLessons
	 */
	public String getCloseLessons() {
		return closeLessons;
	}


	/**
	 * @param closeLessons the closeLessons to set
	 */
	public void setCloseLessons(String closeLessons) {
		this.closeLessons = closeLessons;
	}


	/**
	 * @return the internalProject
	 */
	public Boolean getInternalProject() {
		return internalProject;
	}


	/**
	 * @param internalProject the internalProject to set
	 */
	public void setInternalProject(Boolean internalProject) {
		this.internalProject = internalProject;
	}


	/**
	 * @return the projectDoc
	 */
	public String getProjectDoc() {
		return projectDoc;
	}


	/**
	 * @param projectDoc the projectDoc to set
	 */
	public void setProjectDoc(String projectDoc) {
		this.projectDoc = projectDoc;
	}


	/**
	 * @return the budgetYear
	 */
	public Integer getBudgetYear() {
		return budgetYear;
	}


	/**
	 * @param budgetYear the budgetYear to set
	 */
	public void setBudgetYear(Integer budgetYear) {
		this.budgetYear = budgetYear;
	}


	/**
	 * @return the chartLabel
	 */
	public String getChartLabel() {
		return chartLabel;
	}


	/**
	 * @param chartLabel the chartLabel to set
	 */
	public void setChartLabel(String chartLabel) {
		this.chartLabel = chartLabel;
	}


	/**
	 * @return the probability
	 */
	public Integer getProbability() {
		return probability;
	}


	/**
	 * @param probability the probability to set
	 */
	public void setProbability(Integer probability) {
		this.probability = probability;
	}


	/**
	 * @return the isGeoSelling
	 */
	public Boolean getIsGeoSelling() {
		return isGeoSelling;
	}


	/**
	 * @param isGeoSelling the isGeoSelling to set
	 */
	public void setIsGeoSelling(Boolean isGeoSelling) {
		this.isGeoSelling = isGeoSelling;
	}


	/**
	 * @return the investmentStatus
	 */
	public String getInvestmentStatus() {
		return investmentStatus;
	}


	/**
	 * @param investmentStatus the investmentStatus to set
	 */
	public void setInvestmentStatus(String investmentStatus) {
		this.investmentStatus = investmentStatus;
	}


	/**
	 * @return the sended
	 */
	public Boolean getSended() {
		return sended;
	}


	/**
	 * @param sended the sended to set
	 */
	public void setSended(Boolean sended) {
		this.sended = sended;
	}


	/**
	 * @return the numCompetitors
	 */
	public Integer getNumCompetitors() {
		return numCompetitors;
	}


	/**
	 * @param numCompetitors the numCompetitors to set
	 */
	public void setNumCompetitors(Integer numCompetitors) {
		this.numCompetitors = numCompetitors;
	}


	/**
	 * @return the finalPosition
	 */
	public Integer getFinalPosition() {
		return finalPosition;
	}


	/**
	 * @param finalPosition the finalPosition to set
	 */
	public void setFinalPosition(Integer finalPosition) {
		this.finalPosition = finalPosition;
	}


	/**
	 * @return the clientComments
	 */
	public String getClientComments() {
		return clientComments;
	}


	/**
	 * @param clientComments the clientComments to set
	 */
	public void setClientComments(String clientComments) {
		this.clientComments = clientComments;
	}


	/**
	 * @return the canceledComments
	 */
	public String getCanceledComments() {
		return canceledComments;
	}


	/**
	 * @param canceledComments the canceledComments to set
	 */
	public void setCanceledComments(String canceledComments) {
		this.canceledComments = canceledComments;
	}


	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}


	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}


	/**
	 * @return the linkDoc
	 */
	public String getLinkDoc() {
		return linkDoc;
	}


	/**
	 * @param linkDoc the linkDoc to set
	 */
	public void setLinkDoc(String linkDoc) {
		this.linkDoc = linkDoc;
	}


	/**
	 * @return the accountingCode
	 */
	public String getAccountingCode() {
		return accountingCode;
	}


	/**
	 * @param accountingCode the accountingCode to set
	 */
	public void setAccountingCode(String accountingCode) {
		this.accountingCode = accountingCode;
	}


	/**
	 * @return the statusDate
	 */
	public Date getStatusDate() {
		return statusDate;
	}


	/**
	 * @param statusDate the statusDate to set
	 */
	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}


	/**
	 * @return the lowerThreshold
	 */
	public Double getLowerThreshold() {
		return lowerThreshold;
	}


	/**
	 * @param lowerThreshold the lowerThreshold to set
	 */
	public void setLowerThreshold(Double lowerThreshold) {
		this.lowerThreshold = lowerThreshold;
	}


	/**
	 * @return the upperThreshold
	 */
	public Double getUpperThreshold() {
		return upperThreshold;
	}


	/**
	 * @param upperThreshold the upperThreshold to set
	 */
	public void setUpperThreshold(Double upperThreshold) {
		this.upperThreshold = upperThreshold;
	}


	/**
	 * @return the linkComment
	 */
	public String getLinkComment() {
		return linkComment;
	}


	/**
	 * @param linkComment the linkComment to set
	 */
	public void setLinkComment(String linkComment) {
		this.linkComment = linkComment;
	}


	/**
	 * @return the scopeStatement
	 */
	public String getScopeStatement() {
		return scopeStatement;
	}


	/**
	 * @param scopeStatement the scopeStatement to set
	 */
	public void setScopeStatement(String scopeStatement) {
		this.scopeStatement = scopeStatement;
	}


	/**
	 * @return the hdDescription
	 */
	public String getHdDescription() {
		return hdDescription;
	}


	/**
	 * @param hdDescription the hdDescription to set
	 */
	public void setHdDescription(String hdDescription) {
		this.hdDescription = hdDescription;
	}


	/**
	 * @return the rag
	 */
	public Character getRag() {
		return rag;
	}


	/**
	 * @param rag the rag to set
	 */
	public void setRag(Character rag) {
		this.rag = rag;
	}


	/**
	 * @return the currencyOptional1
	 */
	public Double getCurrencyOptional1() {
		return currencyOptional1;
	}


	/**
	 * @param currencyOptional1 the currencyOptional1 to set
	 */
	public void setCurrencyOptional1(Double currencyOptional1) {
		this.currencyOptional1 = currencyOptional1;
	}


	/**
	 * @return the currencyOptional2
	 */
	public Double getCurrencyOptional2() {
		return currencyOptional2;
	}


	/**
	 * @param currencyOptional2 the currencyOptional2 to set
	 */
	public void setCurrencyOptional2(Double currencyOptional2) {
		this.currencyOptional2 = currencyOptional2;
	}


	/**
	 * @return the currencyOptional3
	 */
	public Double getCurrencyOptional3() {
		return currencyOptional3;
	}


	/**
	 * @param currencyOptional3 the currencyOptional3 to set
	 */
	public void setCurrencyOptional3(Double currencyOptional3) {
		this.currencyOptional3 = currencyOptional3;
	}


	/**
	 * @return the currencyOptional4
	 */
	public Double getCurrencyOptional4() {
		return currencyOptional4;
	}


	/**
	 * @param currencyOptional4 the currencyOptional4 to set
	 */
	public void setCurrencyOptional4(Double currencyOptional4) {
		this.currencyOptional4 = currencyOptional4;
	}


	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	/**
	 * @return the finishDate
	 */
	public Date getFinishDate() {
		return finishDate;
	}


	/**
	 * @param finishDate the finishDate to set
	 */
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}


	/**
	 * @return the poc
	 */
	public Double getPoc() {
		return poc;
	}


	/**
	 * @param poc the poc to set
	 */
	public void setPoc(Double poc) {
		this.poc = poc;
	}


	/**
	 * @return the kpiStatus
	 */
	public String getKpiStatus() {
		return kpiStatus;
	}


	/**
	 * @param kpiStatus the kpiStatus to set
	 */
	public void setKpiStatus(String kpiStatus) {
		this.kpiStatus = kpiStatus;
	}

	/**
	 * @return the projectCharter
	 */
	public ProjectCharterAudit getProjectCharter() {
		return projectCharter;
	}

	/**
	 * @param projectCharter the projectCharter to set
	 */
	public void setProjectCharter(ProjectCharterAudit projectCharter) {
		this.projectCharter = projectCharter;
	}

	/**
	 * @return the wbsnodes
	 */
	public List<WbsnodeAudit> getWbsnodes() {
		return wbsnodes;
	}

	/**
	 * @param wbsnodes the wbsnodes to set
	 */
	public void setWbsnodes(List<WbsnodeAudit> wbsnodes) {
		this.wbsnodes = wbsnodes;
	}

	/**
	 * @return the followups
	 */
	public List<FollowupAudit> getFollowups() {
		return followups;
	}

	/**
	 * @param followups the followups to set
	 */
	public void setFollowups(List<FollowupAudit> followups) {
		this.followups = followups;
	}

	/**
	 * @return the stakeholders
	 */
	public List<StakeholderAudit> getStakeholders() {
		return stakeholders;
	}

	/**
	 * @param stakeholders the stakeholders to set
	 */
	public void setStakeholders(List<StakeholderAudit> stakeholders) {
		this.stakeholders = stakeholders;
	}

	/**
	 * @return the incomes
	 */
	public List<IncomeAudit> getIncomes() {
		return incomes;
	}

	/**
	 * @param incomes the incomes to set
	 */
	public void setIncomes(List<IncomeAudit> incomes) {
		this.incomes = incomes;
	}

	/**
	 * @return the assumptions
	 */
	public List<AssumptionAudit> getAssumptions() {
		return assumptions;
	}

	/**
	 * @param assumptions the assumptions to set
	 */
	public void setAssumptions(List<AssumptionAudit> assumptions) {
		this.assumptions = assumptions;
	}

	/**
	 * @return the issuelogs
	 */
	public List<IssuelogAudit> getIssuelogs() {
		return issuelogs;
	}

	/**
	 * @param issuelogs the issuelogs to set
	 */
	public void setIssuelogs(List<IssuelogAudit> issuelogs) {
		this.issuelogs = issuelogs;
	}

	/**
	 * @return the risks
	 */
	public List<RiskAudit> getRisks() {
		return risks;
	}

	/**
	 * @param risks the risks to set
	 */
	public void setRisks(List<RiskAudit> risks) {
		this.risks = risks;
	}

	/**
	 * @return the chargeCosts
	 */
	public List<ChargeCostAudit> getChargeCosts() {
		return chargeCosts;
	}

	/**
	 * @param chargeCosts the chargeCosts to set
	 */
	public void setChargeCosts(List<ChargeCostAudit> chargeCosts) {
		this.chargeCosts = chargeCosts;
	}

	/**
	 * @return the workingCosts
	 */
	public List<WorkingCostAudit> getWorkingCosts() {
		return workingCosts;
	}

	/**
	 * @param workingCosts the workingCosts to set
	 */
	public void setWorkingCosts(List<WorkingCostAudit> workingCosts) {
		this.workingCosts = workingCosts;
	}

	/**
	 * @return the labels
	 */
	public List<LabelAudit> getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(List<LabelAudit> labels) {
		this.labels = labels;
	}

	/**
	 * @return the stageGate
	 */
	public StageGateAudit getStageGate() {
		return stageGate;
	}

	/**
	 * @param stageGate the stageGate to set
	 */
	public void setStageGate(StageGateAudit stageGate) {
		this.stageGate = stageGate;
	}
}



