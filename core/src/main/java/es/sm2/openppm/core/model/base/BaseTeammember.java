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
 * File: BaseTeammember.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Teammember;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Teammember
 * @see es.sm2.openppm.core.model.base.Teammember
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Teammember
 */
public class BaseTeammember  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "teammember";
	
	public static final String IDTEAMMEMBER = "idTeamMember";
	public static final String PROJECTACTIVITY = "projectactivity";
	public static final String JOBCATEGORY = "jobcategory";
	public static final String EMPLOYEE = "employee";
	public static final String DATEAPPROVED = "dateApproved";
	public static final String SELLRATE = "sellRate";
	public static final String FTE = "fte";
	public static final String DATEIN = "dateIn";
	public static final String DATEOUT = "dateOut";
	public static final String HOURS = "hours";
	public static final String EXPENSES = "expenses";
	public static final String STATUS = "status";
	public static final String COMMENTSPM = "commentsPm";
	public static final String COMMENTSRM = "commentsRm";

     private Integer idTeamMember;
     private Projectactivity projectactivity;
     private Jobcategory jobcategory;
     private Employee employee;
     private Date dateApproved;
     private Double sellRate;
     private Integer fte;
     private Date dateIn;
     private Date dateOut;
     private Integer hours;
     private Integer expenses;
     private String status;
     private String commentsPm;
     private String commentsRm;

    public BaseTeammember() {
    }
    
    public BaseTeammember(Integer idTeamMember) {
    	this.idTeamMember = idTeamMember;
    }
   
    public Integer getIdTeamMember() {
        return this.idTeamMember;
    }
    
    public void setIdTeamMember(Integer idTeamMember) {
        this.idTeamMember = idTeamMember;
    }
    public Projectactivity getProjectactivity() {
        return this.projectactivity;
    }
    
    public void setProjectactivity(Projectactivity projectactivity) {
        this.projectactivity = projectactivity;
    }
    public Jobcategory getJobcategory() {
        return this.jobcategory;
    }
    
    public void setJobcategory(Jobcategory jobcategory) {
        this.jobcategory = jobcategory;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Date getDateApproved() {
        return this.dateApproved;
    }
    
    public void setDateApproved(Date dateApproved) {
        this.dateApproved = dateApproved;
    }
    public Double getSellRate() {
        return this.sellRate;
    }
    
    public void setSellRate(Double sellRate) {
        this.sellRate = sellRate;
    }
    public Integer getFte() {
        return this.fte;
    }
    
    public void setFte(Integer fte) {
        this.fte = fte;
    }
    public Date getDateIn() {
        return this.dateIn;
    }
    
    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }
    public Date getDateOut() {
        return this.dateOut;
    }
    
    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }
    public Integer getHours() {
        return this.hours;
    }
    
    public void setHours(Integer hours) {
        this.hours = hours;
    }
    public Integer getExpenses() {
        return this.expenses;
    }
    
    public void setExpenses(Integer expenses) {
        this.expenses = expenses;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCommentsPm() {
        return this.commentsPm;
    }
    
    public void setCommentsPm(String commentsPm) {
        this.commentsPm = commentsPm;
    }
    public String getCommentsRm() {
        return this.commentsRm;
    }
    
    public void setCommentsRm(String commentsRm) {
        this.commentsRm = commentsRm;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Teammember) )  { result = false; }
		 else if (other != null) {
		 	Teammember castOther = (Teammember) other;
			if (castOther.getIdTeamMember().equals(this.getIdTeamMember())) { result = true; }
         }
		 return result;
   }


}


