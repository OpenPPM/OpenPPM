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
 * File: BaseTimesheet.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.impl.Timesheetcomment;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Timesheet
 * @see es.sm2.openppm.core.model.base.Timesheet
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Timesheet
 */
public class BaseTimesheet  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "timesheet";
	
	public static final String IDTIMESHEET = "idTimeSheet";
	public static final String OPERATION = "operation";
	public static final String PROJECTACTIVITY = "projectactivity";
	public static final String EMPLOYEE = "employee";
	public static final String INITDATE = "initDate";
	public static final String ENDDATE = "endDate";
	public static final String STATUS = "status";
	public static final String HOURSDAY1 = "hoursDay1";
	public static final String HOURSDAY2 = "hoursDay2";
	public static final String HOURSDAY3 = "hoursDay3";
	public static final String HOURSDAY4 = "hoursDay4";
	public static final String HOURSDAY5 = "hoursDay5";
	public static final String HOURSDAY6 = "hoursDay6";
	public static final String HOURSDAY7 = "hoursDay7";
	public static final String SUGGESTREJECT = "suggestReject";
	public static final String SUGGESTREJECTCOMMENT = "suggestRejectComment";
	public static final String TIMESHEETCOMMENTS = "timesheetcomments";

     private Integer idTimeSheet;
     private Operation operation;
     private Projectactivity projectactivity;
     private Employee employee;
     private Date initDate;
     private Date endDate;
     private String status;
     private Double hoursDay1;
     private Double hoursDay2;
     private Double hoursDay3;
     private Double hoursDay4;
     private Double hoursDay5;
     private Double hoursDay6;
     private Double hoursDay7;
     private Boolean suggestReject;
     private String suggestRejectComment;
     private Set<Timesheetcomment> timesheetcomments = new HashSet<Timesheetcomment>(0);

    public BaseTimesheet() {
    }
    
    public BaseTimesheet(Integer idTimeSheet) {
    	this.idTimeSheet = idTimeSheet;
    }
   
    public Integer getIdTimeSheet() {
        return this.idTimeSheet;
    }
    
    public void setIdTimeSheet(Integer idTimeSheet) {
        this.idTimeSheet = idTimeSheet;
    }
    public Operation getOperation() {
        return this.operation;
    }
    
    public void setOperation(Operation operation) {
        this.operation = operation;
    }
    public Projectactivity getProjectactivity() {
        return this.projectactivity;
    }
    
    public void setProjectactivity(Projectactivity projectactivity) {
        this.projectactivity = projectactivity;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
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
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public Double getHoursDay1() {
        return this.hoursDay1;
    }
    
    public void setHoursDay1(Double hoursDay1) {
        this.hoursDay1 = hoursDay1;
    }
    public Double getHoursDay2() {
        return this.hoursDay2;
    }
    
    public void setHoursDay2(Double hoursDay2) {
        this.hoursDay2 = hoursDay2;
    }
    public Double getHoursDay3() {
        return this.hoursDay3;
    }
    
    public void setHoursDay3(Double hoursDay3) {
        this.hoursDay3 = hoursDay3;
    }
    public Double getHoursDay4() {
        return this.hoursDay4;
    }
    
    public void setHoursDay4(Double hoursDay4) {
        this.hoursDay4 = hoursDay4;
    }
    public Double getHoursDay5() {
        return this.hoursDay5;
    }
    
    public void setHoursDay5(Double hoursDay5) {
        this.hoursDay5 = hoursDay5;
    }
    public Double getHoursDay6() {
        return this.hoursDay6;
    }
    
    public void setHoursDay6(Double hoursDay6) {
        this.hoursDay6 = hoursDay6;
    }
    public Double getHoursDay7() {
        return this.hoursDay7;
    }
    
    public void setHoursDay7(Double hoursDay7) {
        this.hoursDay7 = hoursDay7;
    }
    public Boolean getSuggestReject() {
        return this.suggestReject;
    }
    
    public void setSuggestReject(Boolean suggestReject) {
        this.suggestReject = suggestReject;
    }
    public String getSuggestRejectComment() {
        return this.suggestRejectComment;
    }
    
    public void setSuggestRejectComment(String suggestRejectComment) {
        this.suggestRejectComment = suggestRejectComment;
    }
    public Set<Timesheetcomment> getTimesheetcomments() {
        return this.timesheetcomments;
    }
    
    public void setTimesheetcomments(Set<Timesheetcomment> timesheetcomments) {
        this.timesheetcomments = timesheetcomments;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Timesheet) )  { result = false; }
		 else if (other != null) {
		 	Timesheet castOther = (Timesheet) other;
			if (castOther.getIdTimeSheet().equals(this.getIdTimeSheet())) { result = true; }
         }
		 return result;
   }


}


