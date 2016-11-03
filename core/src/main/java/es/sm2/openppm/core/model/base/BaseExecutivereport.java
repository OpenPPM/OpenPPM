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
 * File: BaseExecutivereport.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Executivereport;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Executivereport
 * @see es.sm2.openppm.core.model.base.Executivereport
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Executivereport
 */
public class BaseExecutivereport  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "executivereport";
	
	public static final String IDEXECUTIVEREPORT = "idExecutiveReport";
	public static final String PROJECT = "project";
	public static final String STATUSDATE = "statusDate";
	public static final String INTERNAL = "internal";
	public static final String EXTERNAL = "external";

     private Integer idExecutiveReport;
     private Project project;
     private Date statusDate;
     private String internal;
     private String external;

    public BaseExecutivereport() {
    }
    
    public BaseExecutivereport(Integer idExecutiveReport) {
    	this.idExecutiveReport = idExecutiveReport;
    }
   
    public Integer getIdExecutiveReport() {
        return this.idExecutiveReport;
    }
    
    public void setIdExecutiveReport(Integer idExecutiveReport) {
        this.idExecutiveReport = idExecutiveReport;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public Date getStatusDate() {
        return this.statusDate;
    }
    
    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
    public String getInternal() {
        return this.internal;
    }
    
    public void setInternal(String internal) {
        this.internal = internal;
    }
    public String getExternal() {
        return this.external;
    }
    
    public void setExternal(String external) {
        this.external = external;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Executivereport) )  { result = false; }
		 else if (other != null) {
		 	Executivereport castOther = (Executivereport) other;
			if (castOther.getIdExecutiveReport().equals(this.getIdExecutiveReport())) { result = true; }
         }
		 return result;
   }


}


