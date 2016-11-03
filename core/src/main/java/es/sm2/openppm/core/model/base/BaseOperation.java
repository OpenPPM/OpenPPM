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
 * File: BaseOperation.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employeeoperationdate;
import es.sm2.openppm.core.model.impl.Expensesheet;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Operationaccount;
import es.sm2.openppm.core.model.impl.Timesheet;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Operation
 * @see es.sm2.openppm.core.model.base.BaseOperation
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Operation
 */
public class BaseOperation  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "operation";
	
	public static final String IDOPERATION = "idOperation";
	public static final String OPERATIONACCOUNT = "operationaccount";
	public static final String OPERATIONNAME = "operationName";
	public static final String OPERATIONCODE = "operationCode";
	public static final String AVAILABLEFORMANAGER = "availableForManager";
	public static final String EXPENSESHEETS = "expensesheets";
	public static final String EMPLOYEEOPERATIONDATES = "employeeoperationdates";
	public static final String TIMESHEETS = "timesheets";
     public static final String AVAILABLE_FOR_APPROVE = "availableForApprove";
     public static final String EXCLUDEEXTERNALS = "excludeExternals";

     private Integer idOperation;
     private Operationaccount operationaccount;
     private String operationName;
     private String operationCode;
     private Boolean availableForManager;
     private Boolean availableForApprove;
     private Boolean excludeExternals;
     private Set<Expensesheet> expensesheets = new HashSet<Expensesheet>(0);
     private Set<Employeeoperationdate> employeeoperationdates = new HashSet<Employeeoperationdate>(0);
     private Set<Timesheet> timesheets = new HashSet<Timesheet>(0);

    public BaseOperation() {
    }
    
    public BaseOperation(Integer idOperation) {
    	this.idOperation = idOperation;
    }
   
    public Integer getIdOperation() {
        return this.idOperation;
    }
    
    public void setIdOperation(Integer idOperation) {
        this.idOperation = idOperation;
    }
    public Operationaccount getOperationaccount() {
        return this.operationaccount;
    }
    
    public void setOperationaccount(Operationaccount operationaccount) {
        this.operationaccount = operationaccount;
    }
    public String getOperationName() {
        return this.operationName;
    }
    
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    public String getOperationCode() {
        return this.operationCode;
    }
    
    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }
    public Boolean getAvailableForManager() {
        return this.availableForManager;
    }
    
    public void setAvailableForManager(Boolean availableForManager) {
        this.availableForManager = availableForManager;
    }
    public Set<Expensesheet> getExpensesheets() {
        return this.expensesheets;
    }
    
    public void setExpensesheets(Set<Expensesheet> expensesheets) {
        this.expensesheets = expensesheets;
    }
    public Set<Employeeoperationdate> getEmployeeoperationdates() {
        return this.employeeoperationdates;
    }
    
    public void setEmployeeoperationdates(Set<Employeeoperationdate> employeeoperationdates) {
        this.employeeoperationdates = employeeoperationdates;
    }
    public Set<Timesheet> getTimesheets() {
        return this.timesheets;
    }
    
    public void setTimesheets(Set<Timesheet> timesheets) {
        this.timesheets = timesheets;
    }

     public Boolean getExcludeExternals() {
         return excludeExternals;
     }

     public void setExcludeExternals(Boolean excludeExternals) {
         this.excludeExternals = excludeExternals;
     }

	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Operation) )  { result = false; }
		 else if (other != null) {
		 	Operation castOther = (Operation) other;
			if (castOther.getIdOperation().equals(this.getIdOperation())) { result = true; }
         }
		 return result;
   }

     public Boolean getAvailableForApprove() {
         return availableForApprove;
     }

     public void setAvailableForApprove(Boolean availableForApprove) {
         this.availableForApprove = availableForApprove;
     }
 }


