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
 * File: BaseProject.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Assumptionregister;
import es.sm2.openppm.core.model.impl.Category;
import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Classificationlevel;
import es.sm2.openppm.core.model.impl.Closurecheckproject;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Executivereport;
import es.sm2.openppm.core.model.impl.Expensesheet;
import es.sm2.openppm.core.model.impl.Geography;
import es.sm2.openppm.core.model.impl.Incomes;
import es.sm2.openppm.core.model.impl.Issuelog;
import es.sm2.openppm.core.model.impl.Logprojectstatus;
import es.sm2.openppm.core.model.impl.Milestones;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Problemcheckproject;
import es.sm2.openppm.core.model.impl.Procurementpayments;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.ProjectData;
import es.sm2.openppm.core.model.impl.LearnedLessonProject;
import es.sm2.openppm.core.model.impl.ProjectTechnology;
import es.sm2.openppm.core.model.impl.LearnedLessonLink;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Projectassociation;
import es.sm2.openppm.core.model.impl.Projectcalendar;
import es.sm2.openppm.core.model.impl.Projectcharter;
import es.sm2.openppm.core.model.impl.Projectclosure;
import es.sm2.openppm.core.model.impl.Projectcosts;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Projectfundingsource;
import es.sm2.openppm.core.model.impl.Projectkpi;
import es.sm2.openppm.core.model.impl.Projectlabel;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.impl.Stagegate;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Timeline;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.impl.Workingcosts;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Project
 * @see es.sm2.openppm.core.model.base.BaseProject
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Project
 */
public class BaseProject implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "project";
	
	public static final String IDPROJECT = "idProject";
	public static final String GEOGRAPHY = "geography";
	public static final String EMPLOYEEBYINVESTMENTMANAGER = "employeeByInvestmentManager";
	public static final String CLASSIFICATIONLEVEL = "classificationlevel";
	public static final String PERFORMINGORG = "performingorg";
	public static final String PROGRAM = "program";
	public static final String PROJECTCALENDAR = "projectcalendar";
	public static final String CUSTOMER = "customer";
	public static final String CATEGORY = "category";
	public static final String EMPLOYEEBYSPONSOR = "employeeBySponsor";
	public static final String EMPLOYEEBYFUNCTIONALMANAGER = "employeeByFunctionalManager";
	public static final String EMPLOYEEBYPROJECTMANAGER = "employeeByProjectManager";
	public static final String STAGEGATE = "stagegate";
	public static final String CONTRACTTYPE = "contracttype";
	public static final String PROJECTNAME = "projectName";
	public static final String STATUS = "status";
	public static final String RISK = "risk";
	public static final String PRIORITY = "priority";
	public static final String BAC = "bac";
	public static final String NETINCOME = "netIncome";
	public static final String TCV = "tcv";
	public static final String INITDATE = "initDate";
	public static final String ENDDATE = "endDate";
	public static final String DURATION = "duration";
	public static final String EFFORT = "effort";
	public static final String PLANNEDFINISHDATE = "plannedFinishDate";
	public static final String PLANDATE = "planDate";
	public static final String EXECDATE = "execDate";
	public static final String PLANNEDINITDATE = "plannedInitDate";
	public static final String CLOSECOMMENTS = "closeComments";
	public static final String CLOSESTAKEHOLDERCOMMENTS = "closeStakeholderComments";
	public static final String CLOSEURLLESSONS = "closeUrlLessons";
	public static final String CLOSELESSONS = "closeLessons";
	public static final String INTERNALPROJECT = "internalProject";
	public static final String PROJECTDOC = "projectDoc";
	public static final String BUDGETYEAR = "budgetYear";
	public static final String CHARTLABEL = "chartLabel";
	public static final String PROBABILITY = "probability";
	public static final String ISGEOSELLING = "isGeoSelling";
	public static final String INVESTMENTSTATUS = "investmentStatus";
	public static final String SENDED = "sended";
	public static final String NUMCOMPETITORS = "numCompetitors";
	public static final String FINALPOSITION = "finalPosition";
	public static final String CLIENTCOMMENTS = "clientComments";
	public static final String CANCELEDCOMMENTS = "canceledComments";
	public static final String COMMENTS = "comments";
	public static final String LINKDOC = "linkDoc";
	public static final String ACCOUNTINGCODE = "accountingCode";
	public static final String STATUSDATE = "statusDate";
	public static final String LOWERTHRESHOLD = "lowerThreshold";
	public static final String UPPERTHRESHOLD = "upperThreshold";
	public static final String LINKCOMMENT = "linkComment";
	public static final String SCOPESTATEMENT = "scopeStatement";
	public static final String HDDESCRIPTION = "hdDescription";
	public static final String RAG = "rag";
	public static final String CURRENCYOPTIONAL1 = "currencyOptional1";
	public static final String CURRENCYOPTIONAL2 = "currencyOptional2";
	public static final String CURRENCYOPTIONAL3 = "currencyOptional3";
	public static final String CURRENCYOPTIONAL4 = "currencyOptional4";
	public static final String STARTDATE = "startDate";
	public static final String FINISHDATE = "finishDate";
	public static final String POC = "poc";
	public static final String KPISTATUS = "kpiStatus";
	public static final String DISABLE = "disable";
	public static final String RISKRATINGADJUSTAMENT = "riskRatingAdjustament";
	public static final String STRATEGICADJUSTAMENT = "strategicAdjustament";
	public static final String CALCULATEDPLANSTARTDATE = "calculatedPlanStartDate";
	public static final String CALCULATEDPLANFINISHDATE = "calculatedPlanFinishDate";
	public static final String USERISKADJUST = "useRiskAdjust";
	public static final String USESTRATEGICADJUST = "useStrategicAdjust";
	public static final String LOGPROJECTSTATUSES = "logprojectstatuses";
	public static final String CHANGECONTROLS = "changecontrols";
	public static final String ACTIVITYSELLERS = "activitysellers";
	public static final String PROJECTACTIVITIES = "projectactivities";
	public static final String EXECUTIVEREPORTS = "executivereports";
	public static final String WBSNODES = "wbsnodes";
	public static final String INCOMESES = "incomeses";
	public static final String RISKREGISTERS = "riskregisters";
	public static final String PROJECTASSOCIATIONSFORDEPENDENT = "projectassociationsForDependent";
	public static final String PROJECTCLOSURES = "projectclosures";
	public static final String ASSUMPTIONREGISTERS = "assumptionregisters";
	public static final String MILESTONESES = "milestoneses";
	public static final String EXPENSESHEETS = "expensesheets";
	public static final String STAKEHOLDERS = "stakeholders";
	public static final String PROJECTLABELS = "projectlabels";
	public static final String PROJECTASSOCIATIONSFORLEAD = "projectassociationsForLead";
	public static final String PROCUREMENTPAYMENTSES = "procurementpaymentses";
	public static final String DOCUMENTPROJECTS = "documentprojects";
	public static final String WORKINGCOSTSES = "workingcostses";
	public static final String PROBLEMCHECKPROJECTS = "problemcheckprojects";
	public static final String PROJECTFUNDINGSOURCES = "projectfundingsources";
	public static final String TIMELINES = "timelines";
	public static final String PROJECTFOLLOWUPS = "projectfollowups";
	public static final String CLOSURECHECKPROJECTS = "closurecheckprojects";
	public static final String PROJECTCOSTSES = "projectcostses";
	public static final String PROJECTCHARTERS = "projectcharters";
	public static final String ISSUELOGS = "issuelogs";
	public static final String CHARGESCOSTSES = "chargescostses";
	public static final String PROJECTKPIS = "projectkpis";
    public static final String ARCHIVEDATE = "archiveDate";
    public static final String PROJECSTDATA = "projectsData";
    public static final String PROJECTTECHNOLOGIES = "projecttechnologies";
    public static final String LEARNEDLESSONLINKS = "learnedLessonLinks";
     public static final String LEARNEDLESSONPROJECTS = "learnedLessonProjects";

     private Integer idProject;
     private Geography geography;
     private Employee employeeByInvestmentManager;
     private Classificationlevel classificationlevel;
     private Performingorg performingorg;
     private Program program;
     private Projectcalendar projectcalendar;
     private Customer customer;
     private Category category;
     private Employee employeeBySponsor;
     private Employee employeeByFunctionalManager;
     private Employee employeeByProjectManager;
     private Stagegate stagegate;
     private Contracttype contracttype;
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
     private Boolean disable;
     private Integer riskRatingAdjustament;
     private Integer strategicAdjustament;
     private Date calculatedPlanStartDate;
     private Date calculatedPlanFinishDate;
     private Boolean useRiskAdjust;
     private Boolean useStrategicAdjust;
     private Date archiveDate;
     private Set<Logprojectstatus> logprojectstatuses = new HashSet<Logprojectstatus>(0);
     private Set<Changecontrol> changecontrols = new HashSet<Changecontrol>(0);
     private Set<Activityseller> activitysellers = new HashSet<Activityseller>(0);
     private Set<Projectactivity> projectactivities = new HashSet<Projectactivity>(0);
     private Set<Executivereport> executivereports = new HashSet<Executivereport>(0);
     private Set<Wbsnode> wbsnodes = new HashSet<Wbsnode>(0);
     private Set<Incomes> incomeses = new HashSet<Incomes>(0);
     private Set<Riskregister> riskregisters = new HashSet<Riskregister>(0);
     private Set<Projectassociation> projectassociationsForDependent = new HashSet<Projectassociation>(0);
     private Set<Projectclosure> projectclosures = new HashSet<Projectclosure>(0);
     private Set<Assumptionregister> assumptionregisters = new HashSet<Assumptionregister>(0);
     private Set<Milestones> milestoneses = new HashSet<Milestones>(0);
     private Set<Expensesheet> expensesheets = new HashSet<Expensesheet>(0);
     private Set<Stakeholder> stakeholders = new HashSet<Stakeholder>(0);
     private Set<Projectlabel> projectlabels = new HashSet<Projectlabel>(0);
     private Set<Projectassociation> projectassociationsForLead = new HashSet<Projectassociation>(0);
     private Set<Procurementpayments> procurementpaymentses = new HashSet<Procurementpayments>(0);
     private Set<Documentproject> documentprojects = new HashSet<Documentproject>(0);
     private Set<Workingcosts> workingcostses = new HashSet<Workingcosts>(0);
     private Set<Problemcheckproject> problemcheckprojects = new HashSet<Problemcheckproject>(0);
     private Set<Projectfundingsource> projectfundingsources = new HashSet<Projectfundingsource>(0);
     private Set<Timeline> timelines = new HashSet<Timeline>(0);
     private Set<Projectfollowup> projectfollowups = new HashSet<Projectfollowup>(0);
     private Set<Closurecheckproject> closurecheckprojects = new HashSet<Closurecheckproject>(0);
     private Set<Projectcosts> projectcostses = new HashSet<Projectcosts>(0);
     private Set<Projectcharter> projectcharters = new HashSet<Projectcharter>(0);
     private Set<Issuelog> issuelogs = new HashSet<Issuelog>(0);
     private Set<Chargescosts> chargescostses = new HashSet<Chargescosts>(0);
     private Set<Projectkpi> projectkpis = new HashSet<Projectkpi>(0);
     private Set<ProjectData> projectsData = new HashSet<ProjectData>(0);
     private Set<ProjectTechnology> projecttechnologies = new HashSet<ProjectTechnology>(0);
     private Set<LearnedLessonProject> learnedLessonProjects = new HashSet<LearnedLessonProject>(0);
     private Set<LearnedLessonLink> learnedLessonLinks = new HashSet<LearnedLessonLink>(0);

    public BaseProject() {
    }
    
    public BaseProject(Integer idProject) {
    	this.idProject = idProject;
    }
   
    public Integer getIdProject() {
        return this.idProject;
    }
    
    public void setIdProject(Integer idProject) {
        this.idProject = idProject;
    }
    public Geography getGeography() {
        return this.geography;
    }
    
    public void setGeography(Geography geography) {
        this.geography = geography;
    }
    public Employee getEmployeeByInvestmentManager() {
        return this.employeeByInvestmentManager;
    }
    
    public void setEmployeeByInvestmentManager(Employee employeeByInvestmentManager) {
        this.employeeByInvestmentManager = employeeByInvestmentManager;
    }
    public Classificationlevel getClassificationlevel() {
        return this.classificationlevel;
    }
    
    public void setClassificationlevel(Classificationlevel classificationlevel) {
        this.classificationlevel = classificationlevel;
    }
    public Performingorg getPerformingorg() {
        return this.performingorg;
    }
    
    public void setPerformingorg(Performingorg performingorg) {
        this.performingorg = performingorg;
    }
    public Program getProgram() {
        return this.program;
    }
    
    public void setProgram(Program program) {
        this.program = program;
    }
    public Projectcalendar getProjectcalendar() {
        return this.projectcalendar;
    }
    
    public void setProjectcalendar(Projectcalendar projectcalendar) {
        this.projectcalendar = projectcalendar;
    }
    public Customer getCustomer() {
        return this.customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    public Employee getEmployeeBySponsor() {
        return this.employeeBySponsor;
    }
    
    public void setEmployeeBySponsor(Employee employeeBySponsor) {
        this.employeeBySponsor = employeeBySponsor;
    }
    public Employee getEmployeeByFunctionalManager() {
        return this.employeeByFunctionalManager;
    }
    
    public void setEmployeeByFunctionalManager(Employee employeeByFunctionalManager) {
        this.employeeByFunctionalManager = employeeByFunctionalManager;
    }
    public Employee getEmployeeByProjectManager() {
        return this.employeeByProjectManager;
    }
    
    public void setEmployeeByProjectManager(Employee employeeByProjectManager) {
        this.employeeByProjectManager = employeeByProjectManager;
    }
    public Stagegate getStagegate() {
        return this.stagegate;
    }
    
    public void setStagegate(Stagegate stagegate) {
        this.stagegate = stagegate;
    }
    public Contracttype getContracttype() {
        return this.contracttype;
    }
    
    public void setContracttype(Contracttype contracttype) {
        this.contracttype = contracttype;
    }
    public String getProjectName() {
        return this.projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public Character getRisk() {
        return this.risk;
    }
    
    public void setRisk(Character risk) {
        this.risk = risk;
    }
    public Integer getPriority() {
        return this.priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public Double getBac() {
        return this.bac;
    }
    
    public void setBac(Double bac) {
        this.bac = bac;
    }
    public Double getNetIncome() {
        return this.netIncome;
    }
    
    public void setNetIncome(Double netIncome) {
        this.netIncome = netIncome;
    }
    public Double getTcv() {
        return this.tcv;
    }
    
    public void setTcv(Double tcv) {
        this.tcv = tcv;
    }
    public Date getInitDate() {
        return this.initDate;
    }
    
    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Integer getDuration() {
        return this.duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    public Integer getEffort() {
        return this.effort;
    }
    
    public void setEffort(Integer effort) {
        this.effort = effort;
    }
    public Date getPlannedFinishDate() {
        return this.plannedFinishDate;
    }
    
    public void setPlannedFinishDate(Date plannedFinishDate) {
        this.plannedFinishDate = plannedFinishDate;
    }
    public Date getPlanDate() {
        return this.planDate;
    }
    
    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }
    public Date getExecDate() {
        return this.execDate;
    }
    
    public void setExecDate(Date execDate) {
        this.execDate = execDate;
    }
    public Date getPlannedInitDate() {
        return this.plannedInitDate;
    }
    
    public void setPlannedInitDate(Date plannedInitDate) {
        this.plannedInitDate = plannedInitDate;
    }
    public String getCloseComments() {
        return this.closeComments;
    }
    
    public void setCloseComments(String closeComments) {
        this.closeComments = closeComments;
    }
    public String getCloseStakeholderComments() {
        return this.closeStakeholderComments;
    }
    
    public void setCloseStakeholderComments(String closeStakeholderComments) {
        this.closeStakeholderComments = closeStakeholderComments;
    }
    public String getCloseUrlLessons() {
        return this.closeUrlLessons;
    }
    
    public void setCloseUrlLessons(String closeUrlLessons) {
        this.closeUrlLessons = closeUrlLessons;
    }
    public String getCloseLessons() {
        return this.closeLessons;
    }
    
    public void setCloseLessons(String closeLessons) {
        this.closeLessons = closeLessons;
    }
    public Boolean getInternalProject() {
        return this.internalProject;
    }
    
    public void setInternalProject(Boolean internalProject) {
        this.internalProject = internalProject;
    }
    public String getProjectDoc() {
        return this.projectDoc;
    }
    
    public void setProjectDoc(String projectDoc) {
        this.projectDoc = projectDoc;
    }
    public Integer getBudgetYear() {
        return this.budgetYear;
    }
    
    public void setBudgetYear(Integer budgetYear) {
        this.budgetYear = budgetYear;
    }
    public String getChartLabel() {
        return this.chartLabel;
    }
    
    public void setChartLabel(String chartLabel) {
        this.chartLabel = chartLabel;
    }
    public Integer getProbability() {
        return this.probability;
    }
    
    public void setProbability(Integer probability) {
        this.probability = probability;
    }
    public Boolean getIsGeoSelling() {
        return this.isGeoSelling;
    }
    
    public void setIsGeoSelling(Boolean isGeoSelling) {
        this.isGeoSelling = isGeoSelling;
    }
    public String getInvestmentStatus() {
        return this.investmentStatus;
    }
    
    public void setInvestmentStatus(String investmentStatus) {
        this.investmentStatus = investmentStatus;
    }
    public Boolean getSended() {
        return this.sended;
    }
    
    public void setSended(Boolean sended) {
        this.sended = sended;
    }
    public Integer getNumCompetitors() {
        return this.numCompetitors;
    }
    
    public void setNumCompetitors(Integer numCompetitors) {
        this.numCompetitors = numCompetitors;
    }
    public Integer getFinalPosition() {
        return this.finalPosition;
    }
    
    public void setFinalPosition(Integer finalPosition) {
        this.finalPosition = finalPosition;
    }
    public String getClientComments() {
        return this.clientComments;
    }
    
    public void setClientComments(String clientComments) {
        this.clientComments = clientComments;
    }
    public String getCanceledComments() {
        return this.canceledComments;
    }
    
    public void setCanceledComments(String canceledComments) {
        this.canceledComments = canceledComments;
    }
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getLinkDoc() {
        return this.linkDoc;
    }
    
    public void setLinkDoc(String linkDoc) {
        this.linkDoc = linkDoc;
    }
    public String getAccountingCode() {
        return this.accountingCode;
    }
    
    public void setAccountingCode(String accountingCode) {
        this.accountingCode = accountingCode;
    }
    public Date getStatusDate() {
        return this.statusDate;
    }
    
    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
    public Double getLowerThreshold() {
        return this.lowerThreshold;
    }
    
    public void setLowerThreshold(Double lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
    }
    public Double getUpperThreshold() {
        return this.upperThreshold;
    }
    
    public void setUpperThreshold(Double upperThreshold) {
        this.upperThreshold = upperThreshold;
    }
    public String getLinkComment() {
        return this.linkComment;
    }
    
    public void setLinkComment(String linkComment) {
        this.linkComment = linkComment;
    }
    public String getScopeStatement() {
        return this.scopeStatement;
    }
    
    public void setScopeStatement(String scopeStatement) {
        this.scopeStatement = scopeStatement;
    }
    public String getHdDescription() {
        return this.hdDescription;
    }
    
    public void setHdDescription(String hdDescription) {
        this.hdDescription = hdDescription;
    }
    public Character getRag() {
        return this.rag;
    }
    
    public void setRag(Character rag) {
        this.rag = rag;
    }
    public Double getCurrencyOptional1() {
        return this.currencyOptional1;
    }
    
    public void setCurrencyOptional1(Double currencyOptional1) {
        this.currencyOptional1 = currencyOptional1;
    }
    public Double getCurrencyOptional2() {
        return this.currencyOptional2;
    }
    
    public void setCurrencyOptional2(Double currencyOptional2) {
        this.currencyOptional2 = currencyOptional2;
    }
    public Double getCurrencyOptional3() {
        return this.currencyOptional3;
    }
    
    public void setCurrencyOptional3(Double currencyOptional3) {
        this.currencyOptional3 = currencyOptional3;
    }
    public Double getCurrencyOptional4() {
        return this.currencyOptional4;
    }
    
    public void setCurrencyOptional4(Double currencyOptional4) {
        this.currencyOptional4 = currencyOptional4;
    }
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getFinishDate() {
        return this.finishDate;
    }
    
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
    public Double getPoc() {
        return this.poc;
    }
    
    public void setPoc(Double poc) {
        this.poc = poc;
    }
    public String getKpiStatus() {
        return this.kpiStatus;
    }
    
    public void setKpiStatus(String kpiStatus) {
        this.kpiStatus = kpiStatus;
    }
    public Boolean getDisable() {
        return this.disable;
    }
    
    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
    public Integer getRiskRatingAdjustament() {
        return this.riskRatingAdjustament;
    }
    
    public void setRiskRatingAdjustament(Integer riskRatingAdjustament) {
        this.riskRatingAdjustament = riskRatingAdjustament;
    }
    public Integer getStrategicAdjustament() {
        return this.strategicAdjustament;
    }
    
    public void setStrategicAdjustament(Integer strategicAdjustament) {
        this.strategicAdjustament = strategicAdjustament;
    }
    public Date getCalculatedPlanStartDate() {
        return this.calculatedPlanStartDate;
    }
    
    public void setCalculatedPlanStartDate(Date calculatedPlanStartDate) {
        this.calculatedPlanStartDate = calculatedPlanStartDate;
    }
    public Date getCalculatedPlanFinishDate() {
        return this.calculatedPlanFinishDate;
    }
    
    public void setCalculatedPlanFinishDate(Date calculatedPlanFinishDate) {
        this.calculatedPlanFinishDate = calculatedPlanFinishDate;
    }
    public Boolean getUseRiskAdjust() {
        return this.useRiskAdjust;
    }
    
    public void setUseRiskAdjust(Boolean useRiskAdjust) {
        this.useRiskAdjust = useRiskAdjust;
    }
    public Boolean getUseStrategicAdjust() {
        return this.useStrategicAdjust;
    }
    
    public void setUseStrategicAdjust(Boolean useStrategicAdjust) {
        this.useStrategicAdjust = useStrategicAdjust;
    }
    public Set<Logprojectstatus> getLogprojectstatuses() {
        return this.logprojectstatuses;
    }
    
    public void setLogprojectstatuses(Set<Logprojectstatus> logprojectstatuses) {
        this.logprojectstatuses = logprojectstatuses;
    }
    public Set<Changecontrol> getChangecontrols() {
        return this.changecontrols;
    }
    
    public void setChangecontrols(Set<Changecontrol> changecontrols) {
        this.changecontrols = changecontrols;
    }
    public Set<Activityseller> getActivitysellers() {
        return this.activitysellers;
    }
    
    public void setActivitysellers(Set<Activityseller> activitysellers) {
        this.activitysellers = activitysellers;
    }
    public Set<Projectactivity> getProjectactivities() {
        return this.projectactivities;
    }
    
    public void setProjectactivities(Set<Projectactivity> projectactivities) {
        this.projectactivities = projectactivities;
    }
    public Set<Executivereport> getExecutivereports() {
        return this.executivereports;
    }
    
    public void setExecutivereports(Set<Executivereport> executivereports) {
        this.executivereports = executivereports;
    }
    public Set<Wbsnode> getWbsnodes() {
        return this.wbsnodes;
    }
    
    public void setWbsnodes(Set<Wbsnode> wbsnodes) {
        this.wbsnodes = wbsnodes;
    }
    public Set<Incomes> getIncomeses() {
        return this.incomeses;
    }
    
    public void setIncomeses(Set<Incomes> incomeses) {
        this.incomeses = incomeses;
    }
    public Set<Riskregister> getRiskregisters() {
        return this.riskregisters;
    }
    
    public void setRiskregisters(Set<Riskregister> riskregisters) {
        this.riskregisters = riskregisters;
    }
    public Set<Projectassociation> getProjectassociationsForDependent() {
        return this.projectassociationsForDependent;
    }
    
    public void setProjectassociationsForDependent(Set<Projectassociation> projectassociationsForDependent) {
        this.projectassociationsForDependent = projectassociationsForDependent;
    }
    public Set<Projectclosure> getProjectclosures() {
        return this.projectclosures;
    }
    
    public void setProjectclosures(Set<Projectclosure> projectclosures) {
        this.projectclosures = projectclosures;
    }
    public Set<Assumptionregister> getAssumptionregisters() {
        return this.assumptionregisters;
    }
    
    public void setAssumptionregisters(Set<Assumptionregister> assumptionregisters) {
        this.assumptionregisters = assumptionregisters;
    }
    public Set<Milestones> getMilestoneses() {
        return this.milestoneses;
    }
    
    public void setMilestoneses(Set<Milestones> milestoneses) {
        this.milestoneses = milestoneses;
    }
    public Set<Expensesheet> getExpensesheets() {
        return this.expensesheets;
    }
    
    public void setExpensesheets(Set<Expensesheet> expensesheets) {
        this.expensesheets = expensesheets;
    }
    public Set<Stakeholder> getStakeholders() {
        return this.stakeholders;
    }
    
    public void setStakeholders(Set<Stakeholder> stakeholders) {
        this.stakeholders = stakeholders;
    }
    public Set<Projectlabel> getProjectlabels() {
        return this.projectlabels;
    }
    
    public void setProjectlabels(Set<Projectlabel> projectlabels) {
        this.projectlabels = projectlabels;
    }
    public Set<Projectassociation> getProjectassociationsForLead() {
        return this.projectassociationsForLead;
    }
    
    public void setProjectassociationsForLead(Set<Projectassociation> projectassociationsForLead) {
        this.projectassociationsForLead = projectassociationsForLead;
    }
    public Set<Procurementpayments> getProcurementpaymentses() {
        return this.procurementpaymentses;
    }
    
    public void setProcurementpaymentses(Set<Procurementpayments> procurementpaymentses) {
        this.procurementpaymentses = procurementpaymentses;
    }
    public Set<Documentproject> getDocumentprojects() {
        return this.documentprojects;
    }
    
    public void setDocumentprojects(Set<Documentproject> documentprojects) {
        this.documentprojects = documentprojects;
    }
    public Set<Workingcosts> getWorkingcostses() {
        return this.workingcostses;
    }
    
    public void setWorkingcostses(Set<Workingcosts> workingcostses) {
        this.workingcostses = workingcostses;
    }
    public Set<Problemcheckproject> getProblemcheckprojects() {
        return this.problemcheckprojects;
    }
    
    public void setProblemcheckprojects(Set<Problemcheckproject> problemcheckprojects) {
        this.problemcheckprojects = problemcheckprojects;
    }
    public Set<Projectfundingsource> getProjectfundingsources() {
        return this.projectfundingsources;
    }
    
    public void setProjectfundingsources(Set<Projectfundingsource> projectfundingsources) {
        this.projectfundingsources = projectfundingsources;
    }
    public Set<Timeline> getTimelines() {
        return this.timelines;
    }
    
    public void setTimelines(Set<Timeline> timelines) {
        this.timelines = timelines;
    }
    public Set<Projectfollowup> getProjectfollowups() {
        return this.projectfollowups;
    }
    
    public void setProjectfollowups(Set<Projectfollowup> projectfollowups) {
        this.projectfollowups = projectfollowups;
    }
    public Set<Closurecheckproject> getClosurecheckprojects() {
        return this.closurecheckprojects;
    }
    
    public void setClosurecheckprojects(Set<Closurecheckproject> closurecheckprojects) {
        this.closurecheckprojects = closurecheckprojects;
    }
    public Set<Projectcosts> getProjectcostses() {
        return this.projectcostses;
    }
    
    public void setProjectcostses(Set<Projectcosts> projectcostses) {
        this.projectcostses = projectcostses;
    }
    public Set<Projectcharter> getProjectcharters() {
        return this.projectcharters;
    }
    
    public void setProjectcharters(Set<Projectcharter> projectcharters) {
        this.projectcharters = projectcharters;
    }
    public Set<Issuelog> getIssuelogs() {
        return this.issuelogs;
    }
    
    public void setIssuelogs(Set<Issuelog> issuelogs) {
        this.issuelogs = issuelogs;
    }
    public Set<Chargescosts> getChargescostses() {
        return this.chargescostses;
    }
    
    public void setChargescostses(Set<Chargescosts> chargescostses) {
        this.chargescostses = chargescostses;
    }
    public Set<Projectkpi> getProjectkpis() {
        return this.projectkpis;
    }
    
    public void setProjectkpis(Set<Projectkpi> projectkpis) {
        this.projectkpis = projectkpis;
    }

     public Date getArchiveDate() {
         return archiveDate;
     }

     public void setArchiveDate(Date archiveDate) {
         this.archiveDate = archiveDate;
     }

     public Set<ProjectData> getProjectsData() {
         return projectsData;
     }

     public void setProjectsData(Set<ProjectData> projectsData) {
         this.projectsData = projectsData;
     }

     /**
      * Getter for property 'learnedLessonProjects'.
      *
      * @return Value for property 'learnedLessonProjects'.
      */
     public Set<LearnedLessonProject> getLearnedLessonProjects() {
         return learnedLessonProjects;
     }

     /**
      * Setter for property 'learnedLessonProjects'.
      *
      * @param learnedLessonProjects Value to set for property 'learnedLessonProjects'.
      */
     public void setLearnedLessonProjects(Set<LearnedLessonProject> learnedLessonProjects) {
         this.learnedLessonProjects = learnedLessonProjects;
     }

     /**
      * Getter for property 'learnedLessonLinks'.
      *
      * @return Value for property 'learnedLessonLinks'.
      */
     public Set<LearnedLessonLink> getLearnedLessonLinks() {
         return learnedLessonLinks;
     }

     /**
      * Setter for property 'learnedLessonLinks'.
      *
      * @param learnedLessonLinks Value to set for property 'learnedLessonLinks'.
      */
     public void setLearnedLessonLinks(Set<LearnedLessonLink> learnedLessonLinks) {
         this.learnedLessonLinks = learnedLessonLinks;
     }

     @Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Project) )  { result = false; }
		 else if (other != null) {
		 	Project castOther = (Project) other;
			if (castOther.getIdProject().equals(this.getIdProject())) { result = true; }
         }
		 return result;
   }


     public Set<ProjectTechnology> getProjecttechnologies() {
         return projecttechnologies;
     }

     public void setProjecttechnologies(Set<ProjectTechnology> projecttechnologies) {
         this.projecttechnologies = projecttechnologies;
     }
 }


