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
 * File: BasePerformingorg.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Performingorg
 * @see es.sm2.openppm.core.model.base.Performingorg
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Performingorg
 */
public class BasePerformingorg  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "performingorg";
	
	public static final String IDPERFORG = "idPerfOrg";
	public static final String EMPLOYEE = "employee";
	public static final String COMPANY = "company";
	public static final String ONSAAS = "onSaaS";
	public static final String NAME = "name";
	public static final String PROJECTS = "projects";
	public static final String EMPLOYEES = "employees";
	public static final String PROGRAMS = "programs";

     private Integer idPerfOrg;
     private Employee employee;
     private Company company;
     private Boolean onSaaS;
     private String name;
     private Set<Project> projects = new HashSet<Project>(0);
     private Set<Employee> employees = new HashSet<Employee>(0);
     private Set<Program> programs = new HashSet<Program>(0);

    public BasePerformingorg() {
    }
    
    public BasePerformingorg(Integer idPerfOrg) {
    	this.idPerfOrg = idPerfOrg;
    }
   
    public Integer getIdPerfOrg() {
        return this.idPerfOrg;
    }
    
    public void setIdPerfOrg(Integer idPerfOrg) {
        this.idPerfOrg = idPerfOrg;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public Boolean getOnSaaS() {
        return this.onSaaS;
    }
    
    public void setOnSaaS(Boolean onSaaS) {
        this.onSaaS = onSaaS;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Set<Project> getProjects() {
        return this.projects;
    }
    
    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
    public Set<Employee> getEmployees() {
        return this.employees;
    }
    
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
    public Set<Program> getPrograms() {
        return this.programs;
    }
    
    public void setPrograms(Set<Program> programs) {
        this.programs = programs;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Performingorg) )  { result = false; }
		 else if (other != null) {
		 	Performingorg castOther = (Performingorg) other;
			if (castOther.getIdPerfOrg().equals(this.getIdPerfOrg())) { result = true; }
         }
		 return result;
   }


}


