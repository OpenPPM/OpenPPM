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
 * File: BaseNotificationproject.java
 * Create User: daniel.casas
 * Create Date: 31/07/2015 12:58:40
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Notificationproject;
import es.sm2.openppm.core.model.impl.Notificationtype;
import es.sm2.openppm.core.model.impl.Project;


/**
* Base Pojo object for domain model class Notificationproject
* @see es.sm2.openppm.core.model.impl.Notificationproject
* @author 
* For implement your own methods use class Notificationproject
*/
public class BaseNotificationproject implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ENTITY = "NOTIFICATIONPROJECT";

	public static final String IDNOTIFICATIONPROJECT = "idNotificationProject";
	public static final String STATUS = "status";
	public static final String MODE = "mode";
	public static final String NOTIFICATIONTYPE = "notificationType";
	public static final String PROJECT = "project";

	private Integer idNotificationProject;
	private String status;
	private String mode;
	private Notificationtype notificationType;
	private Project project;

	public BaseNotificationproject() {
	}

	public BaseNotificationproject(Integer idNotificationProject) {
		this.idNotificationProject = idNotificationProject;
	}

	public Integer getIdNotificationProject() {
		return this.idNotificationProject;
	}
	
	public void setIdNotificationProject(Integer idNotificationProject) {
		this.idNotificationProject = idNotificationProject;
	}
   
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	public Project getProject() {
		return this.project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
	
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (this == other) { result = true; }
        else if (other == null) { result = false; }
        else if (!(other instanceof Notificationproject) )  { result = false; }
        else if (other != null) {
        	Notificationproject castOther = (Notificationproject) other;
            if (castOther.getIdNotificationProject().equals(this.getIdNotificationProject())) { result = true; }
        }
        return result;
    }

}


