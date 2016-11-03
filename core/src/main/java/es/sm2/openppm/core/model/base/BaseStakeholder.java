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
 * File: BaseStakeholder.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Stakeholderclassification;
import es.sm2.openppm.core.model.impl.*;



 /**
 * Base Pojo object for domain model class Stakeholder
 * @see es.sm2.openppm.core.model.base.Stakeholder
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Stakeholder
 */
public class BaseStakeholder  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "stakeholder";
	
	public static final String IDSTAKEHOLDER = "idStakeholder";
	public static final String STAKEHOLDERCLASSIFICATION = "stakeholderclassification";
	public static final String PROJECT = "project";
	public static final String EMPLOYEE = "employee";
	public static final String PROJECTROLE = "projectRole";
	public static final String REQUIREMENTS = "requirements";
	public static final String EXPECTATIONS = "expectations";
	public static final String INFLUENCE = "influence";
	public static final String MGTSTRATEGY = "mgtStrategy";
	public static final String TYPE = "type";
	public static final String CONTACTNAME = "contactName";
	public static final String DEPARTMENT = "department";
	public static final String ORDERTOSHOW = "orderToShow";
	public static final String COMMENTS = "comments";

     private Integer idStakeholder;
     private Stakeholderclassification stakeholderclassification;
     private Project project;
     private Employee employee;
     private String projectRole;
     private String requirements;
     private String expectations;
     private String influence;
     private String mgtStrategy;
     private Character type;
     private String contactName;
     private String department;
     private Integer orderToShow;
     private String comments;

    public BaseStakeholder() {
    }
    
    public BaseStakeholder(Integer idStakeholder) {
    	this.idStakeholder = idStakeholder;
    }
   
    public Integer getIdStakeholder() {
        return this.idStakeholder;
    }
    
    public void setIdStakeholder(Integer idStakeholder) {
        this.idStakeholder = idStakeholder;
    }
    public Stakeholderclassification getStakeholderclassification() {
        return this.stakeholderclassification;
    }
    
    public void setStakeholderclassification(Stakeholderclassification stakeholderclassification) {
        this.stakeholderclassification = stakeholderclassification;
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
    public String getProjectRole() {
        return this.projectRole;
    }
    
    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }
    public String getRequirements() {
        return this.requirements;
    }
    
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    public String getExpectations() {
        return this.expectations;
    }
    
    public void setExpectations(String expectations) {
        this.expectations = expectations;
    }
    public String getInfluence() {
        return this.influence;
    }
    
    public void setInfluence(String influence) {
        this.influence = influence;
    }
    public String getMgtStrategy() {
        return this.mgtStrategy;
    }
    
    public void setMgtStrategy(String mgtStrategy) {
        this.mgtStrategy = mgtStrategy;
    }
    public Character getType() {
        return this.type;
    }
    
    public void setType(Character type) {
        this.type = type;
    }
    public String getContactName() {
        return this.contactName;
    }
    
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getDepartment() {
        return this.department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    public Integer getOrderToShow() {
        return this.orderToShow;
    }
    
    public void setOrderToShow(Integer orderToShow) {
        this.orderToShow = orderToShow;
    }
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Stakeholder) )  { result = false; }
		 else if (other != null) {
		 	Stakeholder castOther = (Stakeholder) other;
			if (castOther.getIdStakeholder().equals(this.getIdStakeholder())) { result = true; }
         }
		 return result;
   }


}


