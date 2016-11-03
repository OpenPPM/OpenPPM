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
 * File: BaseContactnotification.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contactnotification;
import es.sm2.openppm.core.model.impl.Notification;


import java.util.Date;

 /**
 * Base Pojo object for domain model class Contactnotification
 * @see es.sm2.openppm.core.model.base.Contactnotification
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Contactnotification
 */
public class BaseContactnotification  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "contactnotification";
	
	public static final String IDCONTACTNOTIFICATION = "idContactNotification";
	public static final String NOTIFICATION = "notification";
	public static final String CONTACT = "contact";
	public static final String TYPE = "type";
	public static final String READNOTIFY = "readNotify";
	public static final String READDATE = "readDate";
	public static final String MODE = "mode";

    private Integer idContactNotification;
    private Notification notification;
    private Contact contact;
    private String type;
    private boolean readNotify;
    private Date readDate;
    private String mode;

    public BaseContactnotification() {
    }
    
    public BaseContactnotification(Integer idContactNotification) {
    	this.idContactNotification = idContactNotification;
    }
   
    public Integer getIdContactNotification() {
        return this.idContactNotification;
    }
    
    public void setIdContactNotification(Integer idContactNotification) {
        this.idContactNotification = idContactNotification;
    }
    public Notification getNotification() {
        return this.notification;
    }
    
    public void setNotification(Notification notification) {
        this.notification = notification;
    }
    public Contact getContact() {
        return this.contact;
    }
    
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public boolean isReadNotify() {
        return this.readNotify;
    }
    
    public void setReadNotify(boolean readNotify) {
        this.readNotify = readNotify;
    }
    public Date getReadDate() {
        return this.readDate;
    }
    
    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }
    
    public String getMode() {
    	return this.mode;
    }

    public void setMode(String mode) {
    	this.mode = mode;
    }

	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Contactnotification) )  { result = false; }
		 else if (other != null) {
		 	Contactnotification castOther = (Contactnotification) other;
			if (castOther.getIdContactNotification().equals(this.getIdContactNotification())) { result = true; }
         }
		 return result;
   }


}


