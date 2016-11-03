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
 * File: BaseSeller.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Activityseller;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Procurementpayments;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Seller
 * @see es.sm2.openppm.core.model.base.Seller
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Seller
 */
public class BaseSeller  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "seller";
	
	public static final String IDSELLER = "idSeller";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String SELECTED = "selected";
	public static final String QUALIFIED = "qualified";
	public static final String QUALIFICATIONDATE = "qualificationDate";
	public static final String SOLESOURCE = "soleSource";
	public static final String SINGLESOURCE = "singleSource";
	public static final String INFORMATION = "information";
	public static final String PROCUREMENTPAYMENTSES = "procurementpaymentses";
	public static final String ACTIVITYSELLERS = "activitysellers";
	public static final String EMPLOYEES = "employees";
    public static final String LEARNEDLESSONS = "learnedLessons";

     private Integer idSeller;
     private Company company;
     private String name;
     private Boolean selected;
     private Boolean qualified;
     private Date qualificationDate;
     private Boolean soleSource;
     private Boolean singleSource;
     private String information;
     private Set<Procurementpayments> procurementpaymentses = new HashSet<Procurementpayments>(0);
     private Set<Activityseller> activitysellers = new HashSet<Activityseller>(0);
     private Set<Employee> employees = new HashSet<Employee>(0);
     private Set<LearnedLesson> learnedLessons = new HashSet<LearnedLesson>(0);

    public BaseSeller() {
    }
    
    public BaseSeller(Integer idSeller) {
    	this.idSeller = idSeller;
    }

     /**
      * Getter for property 'learnedLessons'.
      *
      * @return Value for property 'learnedLessons'.
      */
     public Set<LearnedLesson> getLearnedLessons() {
         return learnedLessons;
     }

     /**
      * Setter for property 'learnedLessons'.
      *
      * @param learnedLessons Value to set for property 'learnedLessons'.
      */
     public void setLearnedLessons(Set<LearnedLesson> learnedLessons) {
         this.learnedLessons = learnedLessons;
     }

     public Integer getIdSeller() {
        return this.idSeller;
    }
    
    public void setIdSeller(Integer idSeller) {
        this.idSeller = idSeller;
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
    public Boolean getSelected() {
        return this.selected;
    }
    
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    public Boolean getQualified() {
        return this.qualified;
    }
    
    public void setQualified(Boolean qualified) {
        this.qualified = qualified;
    }
    public Date getQualificationDate() {
        return this.qualificationDate;
    }
    
    public void setQualificationDate(Date qualificationDate) {
        this.qualificationDate = qualificationDate;
    }
    public Boolean getSoleSource() {
        return this.soleSource;
    }
    
    public void setSoleSource(Boolean soleSource) {
        this.soleSource = soleSource;
    }
    public Boolean getSingleSource() {
        return this.singleSource;
    }
    
    public void setSingleSource(Boolean singleSource) {
        this.singleSource = singleSource;
    }
    public String getInformation() {
        return this.information;
    }
    
    public void setInformation(String information) {
        this.information = information;
    }
    public Set<Procurementpayments> getProcurementpaymentses() {
        return this.procurementpaymentses;
    }
    
    public void setProcurementpaymentses(Set<Procurementpayments> procurementpaymentses) {
        this.procurementpaymentses = procurementpaymentses;
    }
    public Set<Activityseller> getActivitysellers() {
        return this.activitysellers;
    }
    
    public void setActivitysellers(Set<Activityseller> activitysellers) {
        this.activitysellers = activitysellers;
    }
    public Set<Employee> getEmployees() {
        return this.employees;
    }
    
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Seller) )  { result = false; }
		 else if (other != null) {
		 	Seller castOther = (Seller) other;
			if (castOther.getIdSeller().equals(this.getIdSeller())) { result = true; }
         }
		 return result;
   }


}


