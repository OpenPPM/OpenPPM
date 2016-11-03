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
 * File: BaseStakeholderclassification.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Stakeholderclassification;

import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Stakeholderclassification
 * @see es.sm2.openppm.core.model.base.BaseStakeholderclassification
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Stakeholderclassification
 */
public class BaseStakeholderclassification  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "stakeholderclassification";
	
	public static final String IDSTAKEHOLDERCLASSIFICATION = "idStakeholderClassification";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String STAKEHOLDERS = "stakeholders";
	public static final String STAKEHOLDERS_1 = "stakeholders_1";

     private Integer idStakeholderClassification;
     private Company company;
     private String name;
     private String description;
     private Set<Stakeholder> stakeholders = new HashSet<Stakeholder>(0);
     private Set<Stakeholder> stakeholders_1 = new HashSet<Stakeholder>(0);

    public BaseStakeholderclassification() {
    }
    
    public BaseStakeholderclassification(Integer idStakeholderClassification) {
    	this.idStakeholderClassification = idStakeholderClassification;
    }

 public Integer getIdStakeholderClassification() {
        return this.idStakeholderClassification;
    }
    
    public void setIdStakeholderClassification(Integer idStakeholderClassification) {
        this.idStakeholderClassification = idStakeholderClassification;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<Stakeholder> getStakeholders() {
        return this.stakeholders;
    }
    
    public void setStakeholders(Set<Stakeholder> stakeholders) {
        this.stakeholders = stakeholders;
    }
    public Set<Stakeholder> getStakeholders_1() {
        return this.stakeholders_1;
    }
    
    public void setStakeholders_1(Set<Stakeholder> stakeholders_1) {
        this.stakeholders_1 = stakeholders_1;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Stakeholderclassification) )  { result = false; }
		 else if (other != null) {
		 	Stakeholderclassification castOther = (Stakeholderclassification) other;
			if (castOther.getIdStakeholderClassification().equals(this.getIdStakeholderClassification())) { result = true; }
         }
		 return result;
   }


}


