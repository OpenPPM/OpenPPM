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
 * File: BaseNotificationprofile.java
 * Create User: daniel.casas
 * Create Date: 31/07/2015 12:46:40
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Notificationprofile;
import es.sm2.openppm.core.model.impl.Notificationtype;
import es.sm2.openppm.core.model.impl.Resourceprofiles;


/**
* Base Pojo object for domain model class Notificationprofile
* @see es.sm2.openppm.core.model.impl.Notificationprofile
* @author 
* For implement your own methods use class Notificationprofile
*/
public class BaseNotificationprofile implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ENTITY = "NOTIFICATIONPROFILE";

	public static final String IDNOTIFICATIONPROFILE = "idNotificationProfile";
	public static final String STATUS = "status";
	public static final String READONLY = "readOnly";
	public static final String MODE = "mode";
	public static final String NOTIFICATIONTYPE = "notificationType";
	public static final String RESOURCEPROFILE = "resourceProfile";

	private Integer idNotificationProfile;
	private String status;
	private String readOnly;
	private String mode;
	private Notificationtype notificationType;
	private Resourceprofiles resourceProfile;

	public BaseNotificationprofile() {
	}

	public BaseNotificationprofile(Integer idNotificationProfile) {
		this.idNotificationProfile = idNotificationProfile;
	}

	public Integer getIdNotificationProfile() {
		return this.idNotificationProfile;
	}
	
	public void setIdNotificationProfile(Integer idNotificationProfile) {
		this.idNotificationProfile = idNotificationProfile;
	}
   
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
   
	public String getReadOnly() {
		return this.readOnly;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Notificationtype getNotificationType() {
		return this.notificationType;
	}

	public void setNotificationType(Notificationtype notificationType) {
		this.notificationType = notificationType;
	}
	
	public Resourceprofiles getResourceProfile() {
		return this.resourceProfile;
	}
	
	public void setResourceProfile(Resourceprofiles resourceProfile) {
		this.resourceProfile = resourceProfile;
	}
	
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (this == other) { result = true; }
        else if (other == null) { result = false; }
        else if (!(other instanceof Notificationprofile) )  { result = false; }
        else if (other != null) {
        	Notificationprofile castOther = (Notificationprofile) other;
            if (castOther.getIdNotificationProfile().equals(this.getIdNotificationProfile())) { result = true; }
        }
        return result;
    }

}


