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
 * File: BaseEmployeeoperationdate.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Employeeoperationdate;
import es.sm2.openppm.core.model.impl.Operation;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Employeeoperationdate
 * @see es.sm2.openppm.core.model.base.Employeeoperationdate
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Employeeoperationdate
 */
public class BaseEmployeeoperationdate  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "employeeoperationdate";
	
	public static final String IDEMPLOPEDATE = "idEmplOpeDate";
	public static final String OPERATION = "operation";
	public static final String EMPLOYEE = "employee";
	public static final String DATEFOROPERATION = "dateForOperation";

     private Integer idEmplOpeDate;
     private Operation operation;
     private Employee employee;
     private Date dateForOperation;

    public BaseEmployeeoperationdate() {
    }
    
    public BaseEmployeeoperationdate(Integer idEmplOpeDate) {
    	this.idEmplOpeDate = idEmplOpeDate;
    }
   
    public Integer getIdEmplOpeDate() {
        return this.idEmplOpeDate;
    }
    
    public void setIdEmplOpeDate(Integer idEmplOpeDate) {
        this.idEmplOpeDate = idEmplOpeDate;
    }
    public Operation getOperation() {
        return this.operation;
    }
    
    public void setOperation(Operation operation) {
        this.operation = operation;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Date getDateForOperation() {
        return this.dateForOperation;
    }
    
    public void setDateForOperation(Date dateForOperation) {
        this.dateForOperation = dateForOperation;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Employeeoperationdate) )  { result = false; }
		 else if (other != null) {
		 	Employeeoperationdate castOther = (Employeeoperationdate) other;
			if (castOther.getIdEmplOpeDate().equals(this.getIdEmplOpeDate())) { result = true; }
         }
		 return result;
   }


}


