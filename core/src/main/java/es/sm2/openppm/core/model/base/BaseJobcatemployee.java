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
 * File: BaseJobcatemployee.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Jobcatemployee;


/**
 * Base Pojo object for domain model class Jobcatemployee
 * @see es.sm2.openppm.core.model.base.Jobcatemployee
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Jobcatemployee
 */
public class BaseJobcatemployee  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "jobcatemployee";
	
	public static final String IDJOBCATEMPLOYEE = "idJobCatEmployee";
	public static final String JOBCATEGORY = "jobcategory";
	public static final String EMPLOYEE = "employee";

     private Integer idJobCatEmployee;
     private Jobcategory jobcategory;
     private Employee employee;

    public BaseJobcatemployee() {
    }
    
    public BaseJobcatemployee(Integer idJobCatEmployee) {
    	this.idJobCatEmployee = idJobCatEmployee;
    }
   
    public Integer getIdJobCatEmployee() {
        return this.idJobCatEmployee;
    }
    
    public void setIdJobCatEmployee(Integer idJobCatEmployee) {
        this.idJobCatEmployee = idJobCatEmployee;
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


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Jobcatemployee) )  { result = false; }
		 else if (other != null) {
		 	Jobcatemployee castOther = (Jobcatemployee) other;
			if (castOther.getIdJobCatEmployee().equals(this.getIdJobCatEmployee())) { result = true; }
         }
		 return result;
   }


}


