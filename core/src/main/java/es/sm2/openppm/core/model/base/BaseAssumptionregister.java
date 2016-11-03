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
 * File: BaseAssumptionregister.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Assumptionreassessmentlog;
import es.sm2.openppm.core.model.impl.Assumptionregister;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Assumptionregister
 * @see es.sm2.openppm.core.model.base.Assumptionregister
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Assumptionregister
 */
public class BaseAssumptionregister  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "assumptionregister";
	
	public static final String IDASSUMPTION = "idAssumption";
	public static final String PROJECT = "project";
	public static final String DESCRIPTION = "description";
	public static final String ASSUMPTIONCODE = "assumptionCode";
	public static final String ASSUMPTIONNAME = "assumptionName";
	public static final String ORIGINATOR = "originator";
	public static final String ASSUMPTIONDOC = "assumptionDoc";
	public static final String ASSUMPTIONREASSESSMENTLOGS = "assumptionreassessmentlogs";

     private Integer idAssumption;
     private Project project;
     private String description;
     private String assumptionCode;
     private String assumptionName;
     private String originator;
     private String assumptionDoc;
     private Set<Assumptionreassessmentlog> assumptionreassessmentlogs = new HashSet<Assumptionreassessmentlog>(0);

    public BaseAssumptionregister() {
    }
    
    public BaseAssumptionregister(Integer idAssumption) {
    	this.idAssumption = idAssumption;
    }
   
    public Integer getIdAssumption() {
        return this.idAssumption;
    }
    
    public void setIdAssumption(Integer idAssumption) {
        this.idAssumption = idAssumption;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public String getAssumptionCode() {
        return this.assumptionCode;
    }
    
    public void setAssumptionCode(String assumptionCode) {
        this.assumptionCode = assumptionCode;
    }
    public String getAssumptionName() {
        return this.assumptionName;
    }
    
    public void setAssumptionName(String assumptionName) {
        this.assumptionName = assumptionName;
    }
    public String getOriginator() {
        return this.originator;
    }
    
    public void setOriginator(String originator) {
        this.originator = originator;
    }
    public String getAssumptionDoc() {
        return this.assumptionDoc;
    }
    
    public void setAssumptionDoc(String assumptionDoc) {
        this.assumptionDoc = assumptionDoc;
    }
    public Set<Assumptionreassessmentlog> getAssumptionreassessmentlogs() {
        return this.assumptionreassessmentlogs;
    }
    
    public void setAssumptionreassessmentlogs(Set<Assumptionreassessmentlog> assumptionreassessmentlogs) {
        this.assumptionreassessmentlogs = assumptionreassessmentlogs;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Assumptionregister) )  { result = false; }
		 else if (other != null) {
		 	Assumptionregister castOther = (Assumptionregister) other;
			if (castOther.getIdAssumption().equals(this.getIdAssumption())) { result = true; }
         }
		 return result;
   }


}


