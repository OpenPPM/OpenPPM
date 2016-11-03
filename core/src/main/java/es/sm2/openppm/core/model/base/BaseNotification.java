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
 * File: BaseNotification.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Contactnotification;
import es.sm2.openppm.core.model.impl.Notification;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Notification
 * @see es.sm2.openppm.core.model.base.Notification
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Notification
 */
public class BaseNotification  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "notification";
	
	public static final String IDNOTIFICATION = "idNotification";
	public static final String SUBJECT = "subject";
	public static final String BODY = "body";
	public static final String STATUS = "status";
	public static final String DIRECTION = "direction";
	public static final String MESSAGEERROR = "messageError";
	public static final String TYPE = "type";
	public static final String MODENOTIFICATION = "modeNotification";
	public static final String CREATIONDATE = "creationDate";
	public static final String CHANGESTATUSDATE = "changeStatusDate";
	public static final String DISTRIBUTIONLIST = "distributionList";
	public static final String CONTACTNOTIFICATIONS = "contactnotifications";

    private Integer idNotification;
    private String subject;
    private String body;
    private String status;
    private String direction;
    private String messageError;
    private String type;
    private String modeNotification;
    private Date creationDate;
    private Date changeStatusDate;
    private String distributionList;
    private Set<Contactnotification> contactnotifications = new HashSet<Contactnotification>(0);

    public BaseNotification() {
    }
    
    public BaseNotification(Integer idNotification) {
    	this.idNotification = idNotification;
    }
   
    public Integer getIdNotification() {
        return this.idNotification;
    }
    
    public void setIdNotification(Integer idNotification) {
        this.idNotification = idNotification;
    }
    public String getSubject() {
        return this.subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getBody() {
        return this.body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDirection() {
        return this.direction;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getMessageError() {
        return this.messageError;
    }
    
    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public String getModeNotification() {
        return this.modeNotification;
    }
    
    public void setModeNotification(String modeNotification) {
        this.modeNotification = modeNotification;
    }
    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Date getChangeStatusDate() {
        return this.changeStatusDate;
    }
    
    public void setChangeStatusDate(Date changeStatusDate) {
        this.changeStatusDate = changeStatusDate;
    }
    
    public String getDistributionList() {
    	return this.distributionList;
    }
    
    public void setDistributionList(String distributionList) {
    	this.distributionList = distributionList;
    }

    public Set<Contactnotification> getContactnotifications() {
        return this.contactnotifications;
    }
    
    public void setContactnotifications(Set<Contactnotification> contactnotifications) {
        this.contactnotifications = contactnotifications;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Notification) )  { result = false; }
		 else if (other != null) {
		 	Notification castOther = (Notification) other;
			if (castOther.getIdNotification().equals(this.getIdNotification())) { result = true; }
         }
		 return result;
   }


}


