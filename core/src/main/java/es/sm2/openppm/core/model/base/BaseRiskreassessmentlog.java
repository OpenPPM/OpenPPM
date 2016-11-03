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
 * File: BaseRiskreassessmentlog.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Riskreassessmentlog;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Riskreassessmentlog
 * @see es.sm2.openppm.core.model.base.Riskreassessmentlog
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Riskreassessmentlog
 */
public class BaseRiskreassessmentlog  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "riskreassessmentlog";
	
	public static final String IDLOG = "idLog";
	public static final String RISKREGISTER = "riskregister";
	public static final String EMPLOYEE = "employee";
	public static final String RISKDATE = "riskDate";
	public static final String RISKCHANGE = "riskChange";

     private Integer idLog;
     private Riskregister riskregister;
     private Employee employee;
     private Date riskDate;
     private String riskChange;

    public BaseRiskreassessmentlog() {
    }
    
    public BaseRiskreassessmentlog(Integer idLog) {
    	this.idLog = idLog;
    }
   
    public Integer getIdLog() {
        return this.idLog;
    }
    
    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }
    public Riskregister getRiskregister() {
        return this.riskregister;
    }
    
    public void setRiskregister(Riskregister riskregister) {
        this.riskregister = riskregister;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Date getRiskDate() {
        return this.riskDate;
    }
    
    public void setRiskDate(Date riskDate) {
        this.riskDate = riskDate;
    }
    public String getRiskChange() {
        return this.riskChange;
    }
    
    public void setRiskChange(String riskChange) {
        this.riskChange = riskChange;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Riskreassessmentlog) )  { result = false; }
		 else if (other != null) {
		 	Riskreassessmentlog castOther = (Riskreassessmentlog) other;
			if (castOther.getIdLog().equals(this.getIdLog())) { result = true; }
         }
		 return result;
   }


}


