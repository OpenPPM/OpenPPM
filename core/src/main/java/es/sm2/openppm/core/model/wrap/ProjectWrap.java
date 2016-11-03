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
 * File: ProjectWrap.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.wrap;

import es.sm2.openppm.core.logic.impl.ProjectFollowupLogic;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Projectlabel;
import es.sm2.openppm.utils.StringPool;

import java.util.Date;

public class ProjectWrap {

	private Integer idProject;
	private String status;
	private String investmentStatus;
	private String rag;
	private String kpiStatus;
	private String accountingCode;
	private String projectName;
	private String chartLabel;
	private String projectDescription;
	private String performingOrg;
	private Double tcv;
	private Integer priority;
	private Integer probability;
	private Double poc;
	private Date plannedInitDate;
	private Date plannedFinishDate;
	private Date calculatedPlanStartDate;
	private Date calculatedPlanFinishDate;
	private Date startDate;
	private Date finishDate;
	private Double lastAc;
	private Integer effort;
	private Double externalCost;
	private String projectManager;
	private String program;
	private Double bac;
	private String labels;
    private String category;
    private String classificationLevel;
    private Integer duration;
    private String stagegate;
	
	public ProjectWrap(
			Integer idProject,
			String status,
			String investmentStatus,
			String rag,
			String kpiStatus,
			String accountingCode,
			String projectName,
			String chartLabel,
			String performingOrg,
			Double tcv,
			Integer priority,
			Integer probability,
			Double poc,
			Date plannedInitDate,
			Date plannedFinishDate,
			Date calculatedPlanStartDate,
			Date calculatedPlanFinishDate,
			Date startDate,
			Date finishDate,
			Double lastAc,
			Integer effort,
			Double externalCost,
			Double bac,
			String program,
			String projectManager,
            String category,
            String classificationLevel,
            Integer duration,
            String stagegate
			) {
		
		super();
		
		this.idProject = idProject;
		this.status = status+"";
		this.investmentStatus = investmentStatus+"";
		this.rag = rag == null ? StringPool.BLANK : rag.toString();
		this.kpiStatus = kpiStatus;
		this.accountingCode = accountingCode;
		this.projectName = projectName;
		this.chartLabel = chartLabel;
		this.performingOrg = performingOrg;
		this.tcv = tcv;
		this.priority = priority;
		this.probability = probability;
		this.poc = poc;
		this.plannedInitDate = plannedInitDate;
		this.plannedFinishDate = plannedFinishDate;
		this.calculatedPlanStartDate = calculatedPlanStartDate;
		this.calculatedPlanFinishDate = calculatedPlanFinishDate;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.lastAc = lastAc;
		this.effort = effort;
		this.externalCost = externalCost;
		this.bac = bac;
		this.projectManager = projectManager;
		this.program = program;
        this.category = category;
        this.classificationLevel = classificationLevel;
        this.duration = duration;
        this.stagegate = stagegate;
	}
	
	public ProjectWrap (
			Project project,
			Double externalCost
			) {
		
		super();
		
		this.idProject = project.getIdProject();
		this.status = project.getStatus()+"";
		this.investmentStatus = project.getInvestmentStatus()+"";
		this.rag = project.getRag() == null ? StringPool.BLANK : project.getRag().toString();
		this.kpiStatus = project.getKpiStatus();
		this.projectName = project.getProjectName();
		
		for (Projectcharter projectcharter :  project.getProjectcharters()) {
			this.projectDescription = projectcharter.getSucessCriteria();
		}

		this.performingOrg =  project.getPerformingorg().getName();
		this.tcv = project.getTcv();
		this.probability = project.getProbability();
		this.poc = project.getPoc();
		this.startDate = project.getStartDate();
		this.finishDate = project.getFinishDate();
		this.calculatedPlanStartDate = project.getCalculatedPlanStartDate();
		this.calculatedPlanFinishDate = project.getCalculatedPlanFinishDate();
		this.effort = project.getEffort();
		this.externalCost = externalCost;
		this.program = project.getProgram().getProgramName();
		this.bac = project.getBac();
		
		StringBuilder builder = new StringBuilder();
		
		// Add labels
		for (Projectlabel projectLabel : project.getProjectlabels()) {
			
			if (builder.length() > 0) { builder.append(StringPool.COMMA_AND_SPACE); }
			
			builder.append(projectLabel.getLabel().getName());
		}
		
		this.setLabels(builder.toString());
	}
	
	public Projectfollowup getLastFollowup() throws Exception {
    	ProjectFollowupLogic projectFollowupLogic = new ProjectFollowupLogic();
    	
    	return projectFollowupLogic.findLastByProject(new Project(getIdProject()));
    }
	
	/**
	 * @return the idProject
	 */
	public Integer getIdProject() {
		return idProject;
	}
	/**
	 * @param idProject the idProject to set
	 */
	public void setIdProject(Integer idProject) {
		this.idProject = idProject;
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
	 * @return the rag
	 */
	public String getRag() {
		return rag;
	}
	/**
	 * @param rag the rag to set
	 */
	public void setRag(String rag) {
		this.rag = rag;
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
	 * @return the performingOrg
	 */
	public String getPerformingOrg() {
		return performingOrg;
	}
	/**
	 * @param performingOrg the performingOrg to set
	 */
	public void setPerformingOrg(String performingOrg) {
		this.performingOrg = performingOrg;
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
	 * @return the lastAc
	 */
	public Double getLastAc() {
		return lastAc;
	}
	/**
	 * @param lastAc the lastAc to set
	 */
	public void setLastAc(Double lastAc) {
		this.lastAc = lastAc;
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
	 * @return the externalCost
	 */
	public Double getExternalCost() {
		return externalCost;
	}
	/**
	 * @param externalCost the externalCost to set
	 */
	public void setExternalCost(Double externalCost) {
		this.externalCost = externalCost;
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
	 * @return the projectDescription
	 */
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 * @param projectDescription the projectDescription to set
	 */
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
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
	 * @return the labels
	 */
	public String getLabels() {
		return labels;
	}

	/**
	 * @param labels the labels to set
	 */
	public void setLabels(String labels) {
		this.labels = labels;
	}

	/**
	 * @return the projectManager
	 */
	public String getProjectManager() {
		return projectManager;
	}

	/**
	 * @param projectManager the projectManager to set
	 */
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	/**
	 * @return the calculatedPlanStartDate
	 */
	public Date getCalculatedPlanStartDate() {
		return calculatedPlanStartDate;
	}

	/**
	 * @param calculatedPlanStartDate the calculatedPlanStartDate to set
	 */
	public void setCalculatedPlanStartDate(Date calculatedPlanStartDate) {
		this.calculatedPlanStartDate = calculatedPlanStartDate;
	}

	/**
	 * @return the calculatedPlanFinishDate
	 */
	public Date getCalculatedPlanFinishDate() {
		return calculatedPlanFinishDate;
	}

	/**
	 * @param calculatedPlanFinishDate the calculatedPlanFinishDate to set
	 */
	public void setCalculatedPlanFinishDate(Date calculatedPlanFinishDate) {
		this.calculatedPlanFinishDate = calculatedPlanFinishDate;
	}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClassificationLevel() {
        return classificationLevel;
    }

    public void setClassificationLevel(String classificationLevel) {
        this.classificationLevel = classificationLevel;
    }

    /**
     * Getter for property 'duration'.
     *
     * @return Value for property 'duration'.
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Setter for property 'duration'.
     *
     * @param duration Value to set for property 'duration'.
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Getter for property 'stagegate'.
     *
     * @return Value for property 'stagegate'.
     */
    public String getStagegate() {
        return stagegate;
    }

    /**
     * Setter for property 'stagegate'.
     *
     * @param stagegate Value to set for property 'stagegate'.
     */
    public void setStagegate(String stagegate) {
        this.stagegate = stagegate;
    }
}
