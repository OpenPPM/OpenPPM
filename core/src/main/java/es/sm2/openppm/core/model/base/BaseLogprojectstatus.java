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
 * File: BaseLogprojectstatus.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Logprojectstatus;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Logprojectstatus
 * @see es.sm2.openppm.core.model.base.Logprojectstatus
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Logprojectstatus
 */
public class BaseLogprojectstatus  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "logprojectstatus";
	
	public static final String IDLOGPROJECTSTATUS = "idLogProjectStatus";
	public static final String PROJECT = "project";
	public static final String EMPLOYEE = "employee";
	public static final String PROJECTSTATUS = "projectStatus";
	public static final String INVESTMENTSTATUS = "investmentStatus";
	public static final String LOGDATE = "logDate";

     private Integer idLogProjectStatus;
     private Project project;
     private Employee employee;
     private String projectStatus;
     private String investmentStatus;
     private Date logDate;

    public BaseLogprojectstatus() {
    }
    
    public BaseLogprojectstatus(Integer idLogProjectStatus) {
    	this.idLogProjectStatus = idLogProjectStatus;
    }
   
    public Integer getIdLogProjectStatus() {
        return this.idLogProjectStatus;
    }
    
    public void setIdLogProjectStatus(Integer idLogProjectStatus) {
        this.idLogProjectStatus = idLogProjectStatus;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public String getProjectStatus() {
        return this.projectStatus;
    }
    
    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
    public String getInvestmentStatus() {
        return this.investmentStatus;
    }
    
    public void setInvestmentStatus(String investmentStatus) {
        this.investmentStatus = investmentStatus;
    }
    public Date getLogDate() {
        return this.logDate;
    }
    
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Logprojectstatus) )  { result = false; }
		 else if (other != null) {
		 	Logprojectstatus castOther = (Logprojectstatus) other;
			if (castOther.getIdLogProjectStatus().equals(this.getIdLogProjectStatus())) { result = true; }
         }
		 return result;
   }


}


