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
 * File: BaseAssumptionreassessmentlog.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Assumptionreassessmentlog;
import es.sm2.openppm.core.model.impl.Assumptionregister;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Assumptionreassessmentlog
 * @see es.sm2.openppm.core.model.base.Assumptionreassessmentlog
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Assumptionreassessmentlog
 */
public class BaseAssumptionreassessmentlog  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "assumptionreassessmentlog";
	
	public static final String IDLOG = "idLog";
	public static final String ASSUMPTIONREGISTER = "assumptionregister";
	public static final String ASSUMPTIONDATE = "assumptionDate";
	public static final String ASSUMPTIONCHANGE = "assumptionChange";

     private Integer idLog;
     private Assumptionregister assumptionregister;
     private Date assumptionDate;
     private String assumptionChange;

    public BaseAssumptionreassessmentlog() {
    }
    
    public BaseAssumptionreassessmentlog(Integer idLog) {
    	this.idLog = idLog;
    }
   
    public Integer getIdLog() {
        return this.idLog;
    }
    
    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }
    public Assumptionregister getAssumptionregister() {
        return this.assumptionregister;
    }
    
    public void setAssumptionregister(Assumptionregister assumptionregister) {
        this.assumptionregister = assumptionregister;
    }
    public Date getAssumptionDate() {
        return this.assumptionDate;
    }
    
    public void setAssumptionDate(Date assumptionDate) {
        this.assumptionDate = assumptionDate;
    }
    public String getAssumptionChange() {
        return this.assumptionChange;
    }
    
    public void setAssumptionChange(String assumptionChange) {
        this.assumptionChange = assumptionChange;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Assumptionreassessmentlog) )  { result = false; }
		 else if (other != null) {
		 	Assumptionreassessmentlog castOther = (Assumptionreassessmentlog) other;
			if (castOther.getIdLog().equals(this.getIdLog())) { result = true; }
         }
		 return result;
   }


}


