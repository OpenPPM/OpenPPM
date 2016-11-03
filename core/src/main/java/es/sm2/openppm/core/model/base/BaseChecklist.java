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
 * File: BaseChecklist.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Checklist;
import es.sm2.openppm.core.model.impl.Wbsnode;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Checklist
 * @see es.sm2.openppm.core.model.base.Checklist
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Checklist
 */
public class BaseChecklist  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "checklist";
	
	public static final String IDCHECKLIST = "idChecklist";
	public static final String WBSNODE = "wbsnode";
	public static final String CODE = "code";
	public static final String DESCRIPTION = "description";
	public static final String PERCENTAGECOMPLETE = "percentageComplete";
	public static final String ACTUALIZATIONDATE = "actualizationDate";
	public static final String NAME = "name";
	public static final String COMMENTS = "comments";

     private Integer idChecklist;
     private Wbsnode wbsnode;
     private String code;
     private String description;
     private Integer percentageComplete;
     private Date actualizationDate;
     private String name;
     private String comments;

    public BaseChecklist() {
    }
    
    public BaseChecklist(Integer idChecklist) {
    	this.idChecklist = idChecklist;
    }
   
    public Integer getIdChecklist() {
        return this.idChecklist;
    }
    
    public void setIdChecklist(Integer idChecklist) {
        this.idChecklist = idChecklist;
    }
    public Wbsnode getWbsnode() {
        return this.wbsnode;
    }
    
    public void setWbsnode(Wbsnode wbsnode) {
        this.wbsnode = wbsnode;
    }
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getPercentageComplete() {
        return this.percentageComplete;
    }
    
    public void setPercentageComplete(Integer percentageComplete) {
        this.percentageComplete = percentageComplete;
    }
    public Date getActualizationDate() {
        return this.actualizationDate;
    }
    
    public void setActualizationDate(Date actualizationDate) {
        this.actualizationDate = actualizationDate;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getComments() {
        return this.comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Checklist) )  { result = false; }
		 else if (other != null) {
		 	Checklist castOther = (Checklist) other;
			if (castOther.getIdChecklist().equals(this.getIdChecklist())) { result = true; }
         }
		 return result;
   }


}


