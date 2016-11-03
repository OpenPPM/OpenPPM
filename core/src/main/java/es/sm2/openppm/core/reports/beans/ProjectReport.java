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
 * File: ProjectReport.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.reports.beans;

import java.io.InputStream;
import java.util.HashMap;

import es.sm2.openppm.core.reports.ReportDataSource;

public class ProjectReport {

	private HashMap <String, String> translationMap;

	private ReportDataSource<AprobacioReport> aprobaciones;
	private ReportDataSource<MilestoneReport> milestones;
	private ReportDataSource<StakeholderReport> stakeholders;
	private ReportDataSource<NombreReport> depende;
	private ReportDataSource<NombreReport> afecta;
	
	private ReportDataSource<NombreReport> leadsAndDependents;
	private ReportDataSource<NombreReport> sellers;
	private ReportDataSource<NombreReport> infrastructures;
	private ReportDataSource<NombreReport> licenses;
	private ReportDataSource<WorkReport> works;
    private ReportDataSource<NombreReport> documents;

    private ReportDataSource<WbsnodeReport> wbsNodeReport;

    private ReportDataSource<ActivityReport> activities;
    private ReportDataSource<RiskRegisterReport> risksRegister;
    private ReportDataSource<ProjectFollowupReport> projectFollowup;

    private ReportDataSource<NombreReport> deliverables;

    private InputStream subReportActivities;

	private String entregables;
	private String supuestos;
	private String projectName;
	private String code;
	private String dateDocument;
	private String autor;
	private String category;
	private String customer;
	private String contractType;
	private String projectManager;
	private String stakeholder;
	private String fechas;
	private String sponsor;
	private String businessNeed;
	private String descripcion;
	private String objetivos;
	private String totalValue;
	private String netValue;
	private String principalesRiesgos;
	private String situacionActual;
	private String exclusiones;
	private String restricciones;
	private String program;
	private String po;
	private String investmentManager;
	private String budgetYear;
	private String plannedStartDate;
	private String plannedFinishDate;
    private String actualStartDate;
	private String actualFinishDate;
	private String duration;
	private String expenses;
	private String workCosts;
	private String budget;
	private String sellersCost;
	private String infrastructuresCost;
	private String licensesCost;
	private String effort;
	private String functionalManager;
	private String programManager;
	private String projectStatus;
	private String directCost;
	private String bac;
	private String currency;
	private String tipo;
	private String priority;
	private String mfo;
	private String mfoPercent;
    private String isGeoSelling;
    private String classificationlevel;
    private String isInternalProject;
    private String poc;
    private String followupDate;
    private String accountingCode;
    private String externalCost;
	private String projectResults;
    private String projectArchievement;
    private String actualCost;
    private String lessonsLearned;


	private InputStream logo;
	private InputStream subReport;

    /**
     * Getter for property 'deliverables'.
     *
     * @return Value for property 'deliverables'.
     */
    public ReportDataSource<NombreReport> getDeliverables() {

        return deliverables;
    }

    /**
     * Setter for property 'deliverables'.
     *
     * @param deliverables Value to set for property 'deliverables'.
     */
    public void setDeliverables(ReportDataSource<NombreReport> deliverables) {

        this.deliverables = deliverables;
    }

    /**
     * Getter for property 'lessonsLearned'.
     *
     * @return Value for property 'lessonsLearned'.
     */
    public String getLessonsLearned() {

        return lessonsLearned;
    }

    /**
     * Setter for property 'lessonsLearned'.
     *
     * @param lessonsLearned Value to set for property 'lessonsLearned'.
     */
    public void setLessonsLearned(String lessonsLearned) {

        this.lessonsLearned = lessonsLearned;
    }

    /**
     * Getter for property 'actualStartDate'.
     *
     * @return Value for property 'actualStartDate'.
     */
    public String getActualStartDate() {

        return actualStartDate;
    }

    /**
     * Setter for property 'actualStartDate'.
     *
     * @param actualStartDate Value to set for property 'actualStartDate'.
     */
    public void setActualStartDate(String actualStartDate) {

        this.actualStartDate = actualStartDate;
    }

    /**
     * Getter for property 'actualFinishDate'.
     *
     * @return Value for property 'actualFinishDate'.
     */
    public String getActualFinishDate() {

        return actualFinishDate;
    }

    /**
     * Setter for property 'actualFinishDate'.
     *
     * @param actualFinishDate Value to set for property 'actualFinishDate'.
     */
    public void setActualFinishDate(String actualFinishDate) {

        this.actualFinishDate = actualFinishDate;
    }

    /**
     * Getter for property 'projectResults'.
     *
     * @return Value for property 'projectResults'.
     */
    public String getProjectResults() {

        return projectResults;
    }

    /**
     * Setter for property 'projectResults'.
     *
     * @param projectResults Value to set for property 'projectResults'.
     */
    public void setProjectResults(String projectResults) {

        this.projectResults = projectResults;
    }

    /**
     * Getter for property 'projectArchievement'.
     *
     * @return Value for property 'projectArchievement'.
     */
    public String getProjectArchievement() {

        return projectArchievement;
    }

    /**
     * Setter for property 'projectArchievement'.
     *
     * @param projectArchievement Value to set for property 'projectArchievement'.
     */
    public void setProjectArchievement(String projectArchievement) {

        this.projectArchievement = projectArchievement;
    }

    /**
     * Getter for property 'actualCost'.
     *
     * @return Value for property 'actualCost'.
     */
    public String getActualCost() {

        return actualCost;
    }

    /**
     * Setter for property 'actualCost'.
     *
     * @param actualCost Value to set for property 'actualCost'.
     */
    public void setActualCost(String actualCost) {

        this.actualCost = actualCost;
    }

    /**
     * Getter for property 'externalCost'.
     *
     * @return Value for property 'externalCost'.
     */
    public String getExternalCost() {

        return externalCost;
    }

    /**
     * Setter for property 'externalCost'.
     *
     * @param externalCost Value to set for property 'externalCost'.
     */
    public void setExternalCost(String externalCost) {

        this.externalCost = externalCost;
    }

    /**
     * Getter for property 'accountingCode'.
     *
     * @return Value for property 'accountingCode'.
     */
    public String getAccountingCode() {

        return accountingCode;
    }

    /**
     * Setter for property 'accountingCode'.
     *
     * @param accountingCode Value to set for property 'accountingCode'.
     */
    public void setAccountingCode(String accountingCode) {

        this.accountingCode = accountingCode;
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
	 * @return the contractType
	 */
	public String getContractType() {
		return contractType;
	}
	/**
	 * @param contractType the contractType to set
	 */
	public void setContractType(String contractType) {
		this.contractType = contractType;
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
	 * @return the stakeholder
	 */
	public String getStakeholder() {
		return stakeholder;
	}
	/**
	 * @param stakeholder the stakeholder to set
	 */
	public void setStakeholder(String stakeholder) {
		this.stakeholder = stakeholder;
	}
	/**
	 * @return the aprobaciones
	 */
	public ReportDataSource<AprobacioReport> getAprobaciones() {
		return aprobaciones;
	}
	/**
	 * @param aprobaciones the aprobaciones to set
	 */
	public void setAprobaciones(ReportDataSource<AprobacioReport> aprobaciones) {
		this.aprobaciones = aprobaciones;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the dateDocument
	 */
	public String getDateDocument() {
		return dateDocument;
	}
	/**
	 * @param dateDocument the dateDocument to set
	 */
	public void setDateDocument(String dateDocument) {
		this.dateDocument = dateDocument;
	}
	/**
	 * @return the autor
	 */
	public String getAutor() {
		return autor;
	}
	/**
	 * @param autor the autor to set
	 */
	public void setAutor(String autor) {
		this.autor = autor;
	}
	/**
	 * @return the entregables
	 */
	public String getEntregables() {
		return entregables;
	}
	/**
	 * @param entregables the entregables to set
	 */
	public void setEntregables(String entregables) {
		this.entregables = entregables;
	}
	/**
	 * @return the fechas
	 */
	public String getFechas() {
		return fechas;
	}
	/**
	 * @param fechas the fechas to set
	 */
	public void setFechas(String fechas) {
		this.fechas = fechas;
	}
	/**
	 * @return the sponsor
	 */
	public String getSponsor() {
		return sponsor;
	}
	/**
	 * @param sponsor the sponsor to set
	 */
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	/**
	 * @return the businessNeed
	 */
	public String getBusinessNeed() {
		return businessNeed;
	}
	/**
	 * @param businessNeed the businessNeed to set
	 */
	public void setBusinessNeed(String businessNeed) {
		this.businessNeed = businessNeed;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the objetivos
	 */
	public String getObjetivos() {
		return objetivos;
	}
	/**
	 * @param objetivos the objetivos to set
	 */
	public void setObjetivos(String objetivos) {
		this.objetivos = objetivos;
	}
	/**
	 * @return the milestones
	 */
	public ReportDataSource<MilestoneReport> getMilestones() {
		return milestones;
	}
	/**
	 * @param milestones the milestones to set
	 */
	public void setMilestones(ReportDataSource<MilestoneReport> milestones) {
		this.milestones = milestones;
	}
	/**
	 * @return the totalValue
	 */
	public String getTotalValue() {
		return totalValue;
	}
	/**
	 * @param totalValue the totalValue to set
	 */
	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}
	/**
	 * @return the netValue
	 */
	public String getNetValue() {
		return netValue;
	}
	/**
	 * @param netValue the netValue to set
	 */
	public void setNetValue(String netValue) {
		this.netValue = netValue;
	}
	/**
	 * @return the stakeholders
	 */
	public ReportDataSource<StakeholderReport> getStakeholders() {
		return stakeholders;
	}
	/**
	 * @param stakeholders the stakeholders to set
	 */
	public void setStakeholders(ReportDataSource<StakeholderReport> stakeholders) {
		this.stakeholders = stakeholders;
	}
	/**
	 * @return the principalesRiesgos
	 */
	public String getPrincipalesRiesgos() {
		return principalesRiesgos;
	}
	/**
	 * @param principalesRiesgos the principalesRiesgos to set
	 */
	public void setPrincipalesRiesgos(String principalesRiesgos) {
		this.principalesRiesgos = principalesRiesgos;
	}
	/**
	 * @return the depende
	 */
	public ReportDataSource<NombreReport> getDepende() {
		return depende;
	}
	/**
	 * @param depende the depende to set
	 */
	public void setDepende(ReportDataSource<NombreReport> depende) {
		this.depende = depende;
	}
	/**
	 * @return the afecta
	 */
	public ReportDataSource<NombreReport> getAfecta() {
		return afecta;
	}
	/**
	 * @param afecta the afecta to set
	 */
	public void setAfecta(ReportDataSource<NombreReport> afecta) {
		this.afecta = afecta;
	}
	/**
	 * @return the situacionActual
	 */
	public String getSituacionActual() {
		return situacionActual;
	}
	/**
	 * @param situacionActual the situacionActual to set
	 */
	public void setSituacionActual(String situacionActual) {
		this.situacionActual = situacionActual;
	}
	/**
	 * @return the exclusiones
	 */
	public String getExclusiones() {
		return exclusiones;
	}
	/**
	 * @param exclusiones the exclusiones to set
	 */
	public void setExclusiones(String exclusiones) {
		this.exclusiones = exclusiones;
	}
	public String getRestricciones() {
		return restricciones;
	}
	public void setRestricciones(String restricciones) {
		this.restricciones = restricciones;
	}
	/**
	 * @return the supuestos
	 */
	public String getSupuestos() {
		return supuestos;
	}
	/**
	 * @param supuestos the supuestos to set
	 */
	public void setSupuestos(String supuestos) {
		this.supuestos = supuestos;
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
	 * @return the po
	 */
	public String getPo() {
		return po;
	}
	/**
	 * @param po the po to set
	 */
	public void setPo(String po) {
		this.po = po;
	}
	/**
	 * @return the investmentManager
	 */
	public String getInvestmentManager() {
		return investmentManager;
	}
	/**
	 * @param investmentManager the investmentManager to set
	 */
	public void setInvestmentManager(String investmentManager) {
		this.investmentManager = investmentManager;
	}
	/**
	 * @return the budgetYear
	 */
	public String getBudgetYear() {
		return budgetYear;
	}
	/**
	 * @param budgetYear the budgetYear to set
	 */
	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}
	/**
	 * @return the plannedStartDate
	 */
	public String getPlannedStartDate() {
		return plannedStartDate;
	}
	/**
	 * @param plannedStartDate the plannedStartDate to set
	 */
	public void setPlannedStartDate(String plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	/**
	 * @return the plannedFinishDate
	 */
	public String getPlannedFinishDate() {
		return plannedFinishDate;
	}
	/**
	 * @param plannedFinishDate the plannedFinishDate to set
	 */
	public void setPlannedFinishDate(String plannedFinishDate) {
		this.plannedFinishDate = plannedFinishDate;
	}
	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}
	/**
	 * @return the expenses
	 */
	public String getExpenses() {
		return expenses;
	}
	/**
	 * @param expenses the expenses to set
	 */
	public void setExpenses(String expenses) {
		this.expenses = expenses;
	}
	/**
	 * @return the workCosts
	 */
	public String getWorkCosts() {
		return workCosts;
	}
	/**
	 * @param workCosts the workCosts to set
	 */
	public void setWorkCosts(String workCosts) {
		this.workCosts = workCosts;
	}
	/**
	 * @return the sellers
	 */
	public ReportDataSource<NombreReport> getSellers() {
		return sellers;
	}
	/**
	 * @param sellers the sellers to set
	 */
	public void setSellers(ReportDataSource<NombreReport> sellers) {
		this.sellers = sellers;
	}
	/**
	 * @return the budget
	 */
	public String getBudget() {
		return budget;
	}
	/**
	 * @param budget the budget to set
	 */
	public void setBudget(String budget) {
		this.budget = budget;
	}
	/**
	 * @return the licenses
	 */
	public ReportDataSource<NombreReport> getLicenses() {
		return licenses;
	}
	/**
	 * @param licenses the licenses to set
	 */
	public void setLicenses(ReportDataSource<NombreReport> licenses) {
		this.licenses = licenses;
	}
	/**
	 * @return the licensesCost
	 */
	public String getLicensesCost() {
		return licensesCost;
	}
	/**
	 * @param licensesCost the licensesCost to set
	 */
	public void setLicensesCost(String licensesCost) {
		this.licensesCost = licensesCost;
	}
	/**
	 * @return the leadsAndDependents
	 */
	public ReportDataSource<NombreReport> getLeadsAndDependents() {
		return leadsAndDependents;
	}
	/**
	 * @param leadsAndDependents the leadsAndDependents to set
	 */
	public void setLeadsAndDependents(ReportDataSource<NombreReport> leadsAndDependents) {
		this.leadsAndDependents = leadsAndDependents;
	}
	/**
	 * @return the infrastructures
	 */
	public ReportDataSource<NombreReport> getInfrastructures() {
		return infrastructures;
	}
	/**
	 * @param infrastructures the infrastructures to set
	 */
	public void setInfrastructures(ReportDataSource<NombreReport> infrastructures) {
		this.infrastructures = infrastructures;
	}
	/**
	 * @return the infrastructuresCost
	 */
	public String getInfrastructuresCost() {
		return infrastructuresCost;
	}
	/**
	 * @param infrastructuresCost the infrastructuresCost to set
	 */
	public void setInfrastructuresCost(String infrastructuresCost) {
		this.infrastructuresCost = infrastructuresCost;
	}
	/**
	 * @return the works
	 */
	public ReportDataSource<WorkReport> getWorks() {
		return works;
	}
	/**
	 * @param works the works to set
	 */
	public void setWorks(ReportDataSource<WorkReport> works) {
		this.works = works;
	}
	/**
	 * @return the effort
	 */
	public String getEffort() {
		return effort;
	}
	/**
	 * @param effort the effort to set
	 */
	public void setEffort(String effort) {
		this.effort = effort;
	}
	/**
	 * @return the sellersCost
	 */
	public String getSellersCost() {
		return sellersCost;
	}
	/**
	 * @param sellersCost the sellersCost to set
	 */
	public void setSellersCost(String sellersCost) {
		this.sellersCost = sellersCost;
	}
	/**
	 * @return the logo
	 */
	public InputStream getLogo() {
		return logo;
	}
	/**
	 * @param logo the logo to set
	 */
	public void setLogo(InputStream logo) {
		this.logo = logo;
	}
	/**
	 * @return the subReport
	 */
	public InputStream getSubReport() {
		return subReport;
	}
	/**
	 * @param subReport the subReport to set
	 */
	public void setSubReport(InputStream subReport) {
		this.subReport = subReport;
	}
	/**
	 * @return the functionalManager
	 */
	public String getFunctionalManager() {
		return functionalManager;
	}
	/**
	 * @param functionalManager the functionalManager to set
	 */
	public void setFunctionalManager(String functionalManager) {
		this.functionalManager = functionalManager;
	}
	/**
	 * @return the programManager
	 */
	public String getProgramManager() {
		return programManager;
	}
	/**
	 * @param programManager the programManager to set
	 */
	public void setProgramManager(String programManager) {
		this.programManager = programManager;
	}
	/**
	 * @return the projectStatus
	 */
	public String getProjectStatus() {
		return projectStatus;
	}
	/**
	 * @param projectStatus the projectStatus to set
	 */
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	/**
	 * @return the directCost
	 */
	public String getDirectCost() {
		return directCost;
	}
	/**
	 * @param directCost the directCost to set
	 */
	public void setDirectCost(String directCost) {
		this.directCost = directCost;
	}
	/**
	 * @return the bac
	 */
	public String getBac() {
		return bac;
	}
	/**
	 * @param bac the bac to set
	 */
	public void setBac(String bac) {
		this.bac = bac;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}
	/**
	 * @return the mfo
	 */
	public String getMfo() {
		return mfo;
	}
	/**
	 * @param mfo the mfo to set
	 */
	public void setMfo(String mfo) {
		this.mfo = mfo;
	}
	/**
	 * @return the mfoPercent
	 */
	public String getMfoPercent() {
		return mfoPercent;
	}
	/**
	 * @param mfoPercent the mfoPercent to set
	 */
	public void setMfoPercent(String mfoPercent) {
		this.mfoPercent = mfoPercent;
	}

    public String getIsGeoSelling() {
        return isGeoSelling;
    }

    public void setIsGeoSelling(String isGeoSelling) {
        this.isGeoSelling = isGeoSelling;
    }

    public String getIsInternalProject() {
        return isInternalProject;
    }

    public void setIsInternalProject(String isInternalProject) {
        this.isInternalProject = isInternalProject;
    }

    public String getClassificationlevel() {
        return classificationlevel;
    }

    public void setClassificationlevel(String classificationlevel) {
        this.classificationlevel = classificationlevel;
    }

    public ReportDataSource<NombreReport> getDocuments() {
        return documents;
    }

    public void setDocuments(ReportDataSource<NombreReport> documents) {
        this.documents = documents;
    }

    public String getPoc() {
        return poc;
    }

    public void setPoc(String poc) {
        this.poc = poc;
    }

    /**
     * @return the activities
     */
    public ReportDataSource<ActivityReport> getActivities() {
        return activities;
    }
    /**
     * @param activities the activities to set
     */
    public void setActivities(ReportDataSource<ActivityReport> activities) {
        this.activities = activities;
    }

    /**
     * @return the risksRegister
     */
    public ReportDataSource<RiskRegisterReport> getRisksRegister() {
        return risksRegister;
    }
    /**
     * @param risksRegister the risksRegister to set
     */
    public void setRisksRegister(ReportDataSource<RiskRegisterReport> risksRegister) {
        this.risksRegister = risksRegister;
    }

    /**
     * @return the projectFollowup
     */
    public ReportDataSource<ProjectFollowupReport> getProjectFollowup() {
        return projectFollowup;
    }
    /**
     * @param projectFollowup the projectFollowup to set
     */
    public void setProjectFollowup(ReportDataSource<ProjectFollowupReport> projectFollowup) {
        this.projectFollowup = projectFollowup;
    }

    /** @return the subReportActivities
    */
    public InputStream getSubReportActivities() {
        return subReportActivities;
    }
    /**
     * @param subReportActivities the subReportActivities to set
     */
    public void setSubReportActivities(InputStream subReportActivities) {
        this.subReportActivities = subReportActivities;
    }

	/** @return the translationMap
	 */
	public HashMap<String, String> getTranslationMap() {
		return translationMap;
	}
	/**
	 * @param translationMap the translationMap to set
	 */
	public void setTranslationMap(HashMap<String, String> translationMap) {
		this.translationMap = translationMap;
	}

	public ReportDataSource<WbsnodeReport> getWbsNodeReport() {
        return wbsNodeReport;
    }

    public void setWbsNodeReport(ReportDataSource<WbsnodeReport> wbsNodeReport) {
        this.wbsNodeReport = wbsNodeReport;
    }

    public String getFollowupDate() {
        return followupDate;
    }

    public void setFollowupDate(String followupDate) {
        this.followupDate = followupDate;
    }
}

