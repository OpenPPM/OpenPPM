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
 * File: BaseSecurity.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.core.model.impl.*;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Security
 * @see es.sm2.openppm.core.model.base.Security
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Security
 */
public class BaseSecurity  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "security";
	
	public static final String IDSEC = "idSec";
	public static final String CONTACT = "contact";
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String AUTORIZATIONLEVEL = "autorizationLevel";
	public static final String DATECREATION = "dateCreation";
	public static final String DATELAPSED = "dateLapsed";
	public static final String ATTEMPTS = "attempts";
	public static final String DATELASTATTEMPT = "dateLastAttempt";

     private Integer idSec;
     private Contact contact;
     private String login;
     private String password;
     private Character autorizationLevel;
     private Date dateCreation;
     private Date dateLapsed;
     private Integer attempts;
     private Date dateLastAttempt;

    public BaseSecurity() {
    }
    
    public BaseSecurity(Integer idSec) {
    	this.idSec = idSec;
    }
   
    public Integer getIdSec() {
        return this.idSec;
    }
    
    public void setIdSec(Integer idSec) {
        this.idSec = idSec;
    }
    public Contact getContact() {
        return this.contact;
    }
    
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    public String getLogin() {
        return this.login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public Character getAutorizationLevel() {
        return this.autorizationLevel;
    }
    
    public void setAutorizationLevel(Character autorizationLevel) {
        this.autorizationLevel = autorizationLevel;
    }
    public Date getDateCreation() {
        return this.dateCreation;
    }
    
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    public Date getDateLapsed() {
        return this.dateLapsed;
    }
    
    public void setDateLapsed(Date dateLapsed) {
        this.dateLapsed = dateLapsed;
    }
    public Integer getAttempts() {
        return this.attempts;
    }
    
    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }
    public Date getDateLastAttempt() {
        return this.dateLastAttempt;
    }
    
    public void setDateLastAttempt(Date dateLastAttempt) {
        this.dateLastAttempt = dateLastAttempt;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Security) )  { result = false; }
		 else if (other != null) {
		 	Security castOther = (Security) other;
			if (castOther.getIdSec().equals(this.getIdSec())) { result = true; }
         }
		 return result;
   }


}


