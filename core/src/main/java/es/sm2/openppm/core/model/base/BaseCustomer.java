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
 * File: BaseCustomer.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Customer;
import es.sm2.openppm.core.model.impl.Customertype;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Customer
 * @see es.sm2.openppm.core.model.base.BaseCustomer
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Customer
 */
public class BaseCustomer  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "customer";
	
	public static final String IDCUSTOMER = "idCustomer";
	public static final String CUSTOMERTYPE = "customertype";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String PROJECTS = "projects";
    public static final String LEARNEDLESSONS = "learnedLessons";

    private Integer idCustomer;
    private Customertype customertype;
    private Company company;
    private String name;
    private String description;
    private Set<Project> projects = new HashSet<Project>(0);
    private Set<LearnedLesson> learnedLessons = new HashSet<LearnedLesson>(0);

    public BaseCustomer() {
    }
    
    public BaseCustomer(Integer idCustomer) {
    	this.idCustomer = idCustomer;
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

     public Integer getIdCustomer() {
        return this.idCustomer;
    }
    
    public void setIdCustomer(Integer idCustomer) {
        this.idCustomer = idCustomer;
    }
    public Customertype getCustomertype() {
        return this.customertype;
    }
    
    public void setCustomertype(Customertype customertype) {
        this.customertype = customertype;
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
    public Set<Project> getProjects() {
        return this.projects;
    }
    
    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Customer) )  { result = false; }
		 else if (other != null) {
		 	Customer castOther = (Customer) other;
			if (castOther.getIdCustomer().equals(this.getIdCustomer())) { result = true; }
         }
		 return result;
   }


}


