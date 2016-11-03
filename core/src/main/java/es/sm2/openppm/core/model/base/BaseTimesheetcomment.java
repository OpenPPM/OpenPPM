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
 * File: BaseTimesheetcomment.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.model.impl.Timesheetcomment;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Timesheetcomment
 * @see es.sm2.openppm.core.model.base.Timesheetcomment
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Timesheetcomment
 */
public class BaseTimesheetcomment  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "timesheetcomment";
	
	public static final String IDTEMESHEETCOMMENT = "idTemesheetComment";
	public static final String TIMESHEET = "timesheet";
	public static final String PREVIOUSSTATUS = "previousStatus";
	public static final String ACTUALSTATUS = "actualStatus";
	public static final String CONTENTCOMMENT = "contentComment";
	public static final String COMMENTDATE = "commentDate";

     private Integer idTemesheetComment;
     private Timesheet timesheet;
     private String previousStatus;
     private String actualStatus;
     private String contentComment;
     private Date commentDate;

    public BaseTimesheetcomment() {
    }
    
    public BaseTimesheetcomment(Integer idTemesheetComment) {
    	this.idTemesheetComment = idTemesheetComment;
    }
   
    public Integer getIdTemesheetComment() {
        return this.idTemesheetComment;
    }
    
    public void setIdTemesheetComment(Integer idTemesheetComment) {
        this.idTemesheetComment = idTemesheetComment;
    }
    public Timesheet getTimesheet() {
        return this.timesheet;
    }
    
    public void setTimesheet(Timesheet timesheet) {
        this.timesheet = timesheet;
    }
    public String getPreviousStatus() {
        return this.previousStatus;
    }
    
    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }
    public String getActualStatus() {
        return this.actualStatus;
    }
    
    public void setActualStatus(String actualStatus) {
        this.actualStatus = actualStatus;
    }
    public String getContentComment() {
        return this.contentComment;
    }
    
    public void setContentComment(String contentComment) {
        this.contentComment = contentComment;
    }
    public Date getCommentDate() {
        return this.commentDate;
    }
    
    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Timesheetcomment) )  { result = false; }
		 else if (other != null) {
		 	Timesheetcomment castOther = (Timesheetcomment) other;
			if (castOther.getIdTemesheetComment().equals(this.getIdTemesheetComment())) { result = true; }
         }
		 return result;
   }


}


