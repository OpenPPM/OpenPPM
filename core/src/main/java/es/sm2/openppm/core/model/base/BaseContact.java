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
 * File: BaseContact.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Configuration;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contactnotification;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.Security;

import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Contact
 * @see es.sm2.openppm.core.model.base.BaseContact
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Contact
 */
public class BaseContact  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "contact";
	
	public static final String IDCONTACT = "idContact";
	public static final String COMPANY = "company";
	public static final String FULLNAME = "fullName";
	public static final String JOBTITLE = "jobTitle";
	public static final String FILEAS = "fileAs";
	public static final String BUSINESSPHONE = "businessPhone";
	public static final String MOBILEPHONE = "mobilePhone";
	public static final String BUSINESSADDRESS = "businessAddress";
	public static final String EMAIL = "email";
	public static final String NOTES = "notes";
	public static final String LOCALE = "locale";
	public static final String DISABLE = "disable";
	public static final String EMPLOYEES = "employees";
	public static final String CONTACTNOTIFICATIONS = "contactnotifications";
	public static final String SECURITIES = "securities";
	public static final String CONFIGURATIONS = "configurations";
    public static final String LEARNEDLESSONS = "learnedLessons";

     private Integer idContact;
     private Company company;
     private String fullName;
     private String jobTitle;
     private String fileAs;
     private String businessPhone;
     private String mobilePhone;
     private String businessAddress;
     private String email;
     private String notes;
     private String locale;
     private Boolean disable;
     private Set<Employee> employees = new HashSet<Employee>(0);
     private Set<Contactnotification> contactnotifications = new HashSet<Contactnotification>(0);
     private Set<Security> securities = new HashSet<Security>(0);
     private Set<Configuration> configurations = new HashSet<Configuration>(0);
     private Set<LearnedLesson> learnedLessons = new HashSet<LearnedLesson>(0);

    public BaseContact() {
    }
    
    public BaseContact(Integer idContact) {
    	this.idContact = idContact;
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

     public Integer getIdContact() {
        return this.idContact;
    }
    
    public void setIdContact(Integer idContact) {
        this.idContact = idContact;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getFullName() {
        return this.fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getJobTitle() {
        return this.jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public String getFileAs() {
        return this.fileAs;
    }
    
    public void setFileAs(String fileAs) {
        this.fileAs = fileAs;
    }
    public String getBusinessPhone() {
        return this.businessPhone;
    }
    
    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }
    public String getMobilePhone() {
        return this.mobilePhone;
    }
    
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    public String getBusinessAddress() {
        return this.businessAddress;
    }
    
    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getLocale() {
        return this.locale;
    }
    
    public void setLocale(String locale) {
        this.locale = locale;
    }
    public Boolean getDisable() {
        return this.disable;
    }
    
    public void setDisable(Boolean disable) {
        this.disable = disable;
    }
    public Set<Employee> getEmployees() {
        return this.employees;
    }
    
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
    public Set<Contactnotification> getContactnotifications() {
        return this.contactnotifications;
    }
    
    public void setContactnotifications(Set<Contactnotification> contactnotifications) {
        this.contactnotifications = contactnotifications;
    }
    public Set<Security> getSecurities() {
        return this.securities;
    }
    
    public void setSecurities(Set<Security> securities) {
        this.securities = securities;
    }
    public Set<Configuration> getConfigurations() {
        return this.configurations;
    }
    
    public void setConfigurations(Set<Configuration> configurations) {
        this.configurations = configurations;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Contact) )  { result = false; }
		 else if (other != null) {
		 	Contact castOther = (Contact) other;
			if (castOther.getIdContact().equals(this.getIdContact())) { result = true; }
         }
		 return result;
   }


}


