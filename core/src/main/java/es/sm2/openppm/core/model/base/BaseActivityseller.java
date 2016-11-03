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
 * File: BaseActivityseller.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectactivity;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Activityseller
 * @see es.sm2.openppm.core.model.base.Activityseller
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Activityseller
 */
public class BaseActivityseller  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "activityseller";
	
	public static final String IDACTIVITYSELLER = "idActivitySeller";
	public static final String SELLER = "seller";
	public static final String PROJECTACTIVITY = "projectactivity";
	public static final String PROJECT = "project";
	public static final String STATEMENTOFWORK = "statementOfWork";
	public static final String PROCUREMENTDOCUMENTS = "procurementDocuments";
	public static final String BASELINESTART = "baselineStart";
	public static final String STARTDATE = "startDate";
	public static final String BASELINEFINISH = "baselineFinish";
	public static final String FINISHDATE = "finishDate";
	public static final String WORKSCHEDULEINFO = "workScheduleInfo";
	public static final String SELLERPERFORMANCEINFO = "sellerPerformanceInfo";
	public static final String INDIRECT = "indirect";

     private Integer idActivitySeller;
     private Seller seller;
     private Projectactivity projectactivity;
     private Project project;
     private String statementOfWork;
     private String procurementDocuments;
     private Date baselineStart;
     private Date startDate;
     private Date baselineFinish;
     private Date finishDate;
     private String workScheduleInfo;
     private String sellerPerformanceInfo;
     private Boolean indirect;

    public BaseActivityseller() {
    }
    
    public BaseActivityseller(Integer idActivitySeller) {
    	this.idActivitySeller = idActivitySeller;
    }
   
    public Integer getIdActivitySeller() {
        return this.idActivitySeller;
    }
    
    public void setIdActivitySeller(Integer idActivitySeller) {
        this.idActivitySeller = idActivitySeller;
    }
    public Seller getSeller() {
        return this.seller;
    }
    
    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    public Projectactivity getProjectactivity() {
        return this.projectactivity;
    }
    
    public void setProjectactivity(Projectactivity projectactivity) {
        this.projectactivity = projectactivity;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public String getStatementOfWork() {
        return this.statementOfWork;
    }
    
    public void setStatementOfWork(String statementOfWork) {
        this.statementOfWork = statementOfWork;
    }
    public String getProcurementDocuments() {
        return this.procurementDocuments;
    }
    
    public void setProcurementDocuments(String procurementDocuments) {
        this.procurementDocuments = procurementDocuments;
    }
    public Date getBaselineStart() {
        return this.baselineStart;
    }
    
    public void setBaselineStart(Date baselineStart) {
        this.baselineStart = baselineStart;
    }
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getBaselineFinish() {
        return this.baselineFinish;
    }
    
    public void setBaselineFinish(Date baselineFinish) {
        this.baselineFinish = baselineFinish;
    }
    public Date getFinishDate() {
        return this.finishDate;
    }
    
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
    public String getWorkScheduleInfo() {
        return this.workScheduleInfo;
    }
    
    public void setWorkScheduleInfo(String workScheduleInfo) {
        this.workScheduleInfo = workScheduleInfo;
    }
    public String getSellerPerformanceInfo() {
        return this.sellerPerformanceInfo;
    }
    
    public void setSellerPerformanceInfo(String sellerPerformanceInfo) {
        this.sellerPerformanceInfo = sellerPerformanceInfo;
    }
    public Boolean getIndirect() {
        return this.indirect;
    }
    
    public void setIndirect(Boolean indirect) {
        this.indirect = indirect;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Activityseller) )  { result = false; }
		 else if (other != null) {
		 	Activityseller castOther = (Activityseller) other;
			if (castOther.getIdActivitySeller().equals(this.getIdActivitySeller())) { result = true; }
         }
		 return result;
   }


}


