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
 * File: BaseEmployee.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Calendarbase;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Employeeoperationdate;
import es.sm2.openppm.core.model.impl.Expensesheet;
import es.sm2.openppm.core.model.impl.Historickpi;
import es.sm2.openppm.core.model.impl.Historicrisk;
import es.sm2.openppm.core.model.impl.Jobcatemployee;
import es.sm2.openppm.core.model.impl.Logprojectstatus;
import es.sm2.openppm.core.model.impl.Managepool;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Riskreassessmentlog;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.Skillsemployee;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Teammember;
import es.sm2.openppm.core.model.impl.Timesheet;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Employee
 * @see es.sm2.openppm.core.model.base.Employee
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Employee
 */
public class BaseEmployee  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "employee";
	
	public static final String IDEMPLOYEE = "idEmployee";
	public static final String CALENDARBASE = "calendarbase";
	public static final String SELLER = "seller";
	public static final String PERFORMINGORG = "performingorg";
	public static final String CONTACT = "contact";
	public static final String RESOURCEPOOL = "resourcepool";
	public static final String RESOURCEPROFILES = "resourceprofiles";
	public static final String COSTRATE = "costRate";
	public static final String PROFILEDATE = "profileDate";
	public static final String TOKEN = "token";
	public static final String DISABLE = "disable";
	public static final String TEAMMEMBERS = "teammembers";
	public static final String PROJECTSFORINVESTMENTMANAGER = "projectsForInvestmentManager";
	public static final String PERFORMINGORGS = "performingorgs";
	public static final String JOBCATEMPLOYEES = "jobcatemployees";
	public static final String PROGRAMS = "programs";
	public static final String STAKEHOLDERS = "stakeholders";
	public static final String RISKREASSESSMENTLOGS = "riskreassessmentlogs";
	public static final String PROJECTSFORPROJECTMANAGER = "projectsForProjectManager";
	public static final String TIMESHEETS = "timesheets";
	public static final String EXPENSESHEETS = "expensesheets";
	public static final String PROJECTSFORFUNCTIONALMANAGER = "projectsForFunctionalManager";
	public static final String SKILLSEMPLOYEES = "skillsemployees";
	public static final String HISTORICRISKS = "historicrisks";
	public static final String MANAGEPOOLS = "managepools";
	public static final String EMPLOYEEOPERATIONDATES = "employeeoperationdates";
	public static final String LOGPROJECTSTATUSES = "logprojectstatuses";
	public static final String HISTORICKPIS = "historickpis";
	public static final String PROJECTSFORSPONSOR = "projectsForSponsor";

     private Integer idEmployee;
     private Calendarbase calendarbase;
     private Seller seller;
     private Performingorg performingorg;
     private Contact contact;
     private Resourcepool resourcepool;
     private Resourceprofiles resourceprofiles;
     private Double costRate;
     private Date profileDate;
     private String token;
     private Boolean disable;
     private Set<Teammember> teammembers = new HashSet<Teammember>(0);
     private Set<Project> projectsForInvestmentManager = new HashSet<Project>(0);
     private Set<Performingorg> performingorgs = new HashSet<Performingorg>(0);
     private Set<Jobcatemployee> jobcatemployees = new HashSet<Jobcatemployee>(0);
     private Set<Program> programs = new HashSet<Program>(0);
     private Set<Stakeholder> stakeholders = new HashSet<Stakeholder>(0);
     private Set<Riskreassessmentlog> riskreassessmentlogs = new HashSet<Riskreassessmentlog>(0);
     private Set<Project> projectsForProjectManager = new HashSet<Project>(0);
     private Set<Timesheet> timesheets = new HashSet<Timesheet>(0);
     private Set<Expensesheet> expensesheets = new HashSet<Expensesheet>(0);
     private Set<Project> projectsForFunctionalManager = new HashSet<Project>(0);
     private Set<Skillsemployee> skillsemployees = new HashSet<Skillsemployee>(0);
     private Set<Historicrisk> historicrisks = new HashSet<Historicrisk>(0);
     private Set<Managepool> managepools = new HashSet<Managepool>(0);
     private Set<Employeeoperationdate> employeeoperationdates = new HashSet<Employeeoperationdate>(0);
     private Set<Logprojectstatus> logprojectstatuses = new HashSet<Logprojectstatus>(0);
     private Set<Historickpi> historickpis = new HashSet<Historickpi>(0);
     private Set<Project> projectsForSponsor = new HashSet<Project>(0);

    public BaseEmployee() {
    }
    
    public BaseEmployee(Integer idEmployee) {
    	this.idEmployee = idEmployee;
    }
   
    public Integer getIdEmployee() {
        return this.idEmployee;
    }
    
    public void setIdEmployee(Integer idEmployee) {
        this.idEmployee = idEmployee;
    }
    public Calendarbase getCalendarbase() {
        return this.calendarbase;
    }
    
    public void setCalendarbase(Calendarbase calendarbase) {
        this.calendarbase = calendarbase;
    }
    public Seller getSeller() {
        return this.seller;
    }
    
    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    public Performingorg getPerformingorg() {
        return this.performingorg;
    }
    
    public void setPerformingorg(Performingorg performingorg) {
        this.performingorg = performingorg;
    }
    public Contact getContact() {
        return this.contact;
    }
    
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    public Resourcepool getResourcepool() {
        return this.resourcepool;
    }
    
    public void setResourcepool(Resourcepool resourcepool) {
        this.resourcepool = resourcepool;
    }
    public Resourceprofiles getResourceprofiles() {
        return this.resourceprofiles;
    }
    
    public void setResourceprofiles(Resourceprofiles resourceprofiles) {
        this.resourceprofiles = resourceprofiles;
    }
    public Double getCostRate() {
        return this.costRate;
    }
    
    public void setCostRate(Double costRate) {
        this.costRate = costRate;
    }
    public Date getProfileDate() {
        return this.profileDate;
    }
    
    public void setProfileDate(Date profileDate) {
        this.profileDate = profileDate;
    }
    public String getToken() {
        return this.token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    public Boolean getDisable() {
        return this.disable;
    }
    
    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
    public Set<Teammember> getTeammembers() {
        return this.teammembers;
    }
    
    public void setTeammembers(Set<Teammember> teammembers) {
        this.teammembers = teammembers;
    }
    public Set<Project> getProjectsForInvestmentManager() {
        return this.projectsForInvestmentManager;
    }
    
    public void setProjectsForInvestmentManager(Set<Project> projectsForInvestmentManager) {
        this.projectsForInvestmentManager = projectsForInvestmentManager;
    }
    public Set<Performingorg> getPerformingorgs() {
        return this.performingorgs;
    }
    
    public void setPerformingorgs(Set<Performingorg> performingorgs) {
        this.performingorgs = performingorgs;
    }
    public Set<Jobcatemployee> getJobcatemployees() {
        return this.jobcatemployees;
    }
    
    public void setJobcatemployees(Set<Jobcatemployee> jobcatemployees) {
        this.jobcatemployees = jobcatemployees;
    }
    public Set<Program> getPrograms() {
        return this.programs;
    }
    
    public void setPrograms(Set<Program> programs) {
        this.programs = programs;
    }
    public Set<Stakeholder> getStakeholders() {
        return this.stakeholders;
    }
    
    public void setStakeholders(Set<Stakeholder> stakeholders) {
        this.stakeholders = stakeholders;
    }
    public Set<Riskreassessmentlog> getRiskreassessmentlogs() {
        return this.riskreassessmentlogs;
    }
    
    public void setRiskreassessmentlogs(Set<Riskreassessmentlog> riskreassessmentlogs) {
        this.riskreassessmentlogs = riskreassessmentlogs;
    }
    public Set<Project> getProjectsForProjectManager() {
        return this.projectsForProjectManager;
    }
    
    public void setProjectsForProjectManager(Set<Project> projectsForProjectManager) {
        this.projectsForProjectManager = projectsForProjectManager;
    }
    public Set<Timesheet> getTimesheets() {
        return this.timesheets;
    }
    
    public void setTimesheets(Set<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }
    public Set<Expensesheet> getExpensesheets() {
        return this.expensesheets;
    }
    
    public void setExpensesheets(Set<Expensesheet> expensesheets) {
        this.expensesheets = expensesheets;
    }
    public Set<Project> getProjectsForFunctionalManager() {
        return this.projectsForFunctionalManager;
    }
    
    public void setProjectsForFunctionalManager(Set<Project> projectsForFunctionalManager) {
        this.projectsForFunctionalManager = projectsForFunctionalManager;
    }
    public Set<Skillsemployee> getSkillsemployees() {
        return this.skillsemployees;
    }
    
    public void setSkillsemployees(Set<Skillsemployee> skillsemployees) {
        this.skillsemployees = skillsemployees;
    }
    public Set<Historicrisk> getHistoricrisks() {
        return this.historicrisks;
    }
    
    public void setHistoricrisks(Set<Historicrisk> historicrisks) {
        this.historicrisks = historicrisks;
    }
    public Set<Managepool> getManagepools() {
        return this.managepools;
    }
    
    public void setManagepools(Set<Managepool> managepools) {
        this.managepools = managepools;
    }
    public Set<Employeeoperationdate> getEmployeeoperationdates() {
        return this.employeeoperationdates;
    }
    
    public void setEmployeeoperationdates(Set<Employeeoperationdate> employeeoperationdates) {
        this.employeeoperationdates = employeeoperationdates;
    }
    public Set<Logprojectstatus> getLogprojectstatuses() {
        return this.logprojectstatuses;
    }
    
    public void setLogprojectstatuses(Set<Logprojectstatus> logprojectstatuses) {
        this.logprojectstatuses = logprojectstatuses;
    }
    public Set<Historickpi> getHistorickpis() {
        return this.historickpis;
    }
    
    public void setHistorickpis(Set<Historickpi> historickpis) {
        this.historickpis = historickpis;
    }
    public Set<Project> getProjectsForSponsor() {
        return this.projectsForSponsor;
    }
    
    public void setProjectsForSponsor(Set<Project> projectsForSponsor) {
        this.projectsForSponsor = projectsForSponsor;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Employee) )  { result = false; }
		 else if (other != null) {
		 	Employee castOther = (Employee) other;
			if (castOther.getIdEmployee().equals(this.getIdEmployee())) { result = true; }
         }
		 return result;
   }


}


