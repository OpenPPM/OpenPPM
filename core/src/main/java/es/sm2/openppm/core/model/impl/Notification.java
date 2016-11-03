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
 * File: Notification.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.logic.setting.NotificationType;
import org.apache.log4j.Logger;

import es.sm2.openppm.core.model.base.BaseNotification;



/**
 * Model class Notification.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Notification extends BaseNotification {

	public final static Logger LOGGER = Logger.getLogger(Notification.class);
	
	private static final long serialVersionUID = 1L;

    public Notification() {
		super();
    }
    public Notification(Integer idNotification) {
		super(idNotification);
    }

    public enum NotificationMode {
    	MANUAL("MANUAL"),
    	AUTOMATIC("AUTOMATIC");
    	
    	private String mode;
    	
    	private NotificationMode(String mode) {
    		this.mode = mode;
    	}

		/**
		 * @return the mode
		 */
		public String getMode() {
			return mode;
		}
    }
    
    public enum NotificationStatus {
    	ERROR("ERROR"),
    	PENDING("PENDING"),
    	ONLYVIEW("ONLYVIEW"),
    	SEND("SEND");
    	
    	private String status;
    	
    	private NotificationStatus(String status) {
    		this.status = status;
    	}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}
    }
    
    /**
     * Get notification type
     * 
     * @return
     */
    public NotificationType getNotificationType() {
    	
    	NotificationType type = null;
    	
    	try {
    		type = NotificationType.valueOf(this.getType());
    	}
    	catch (Exception e) {
    		LOGGER.warn("Notification type is not defined: "+this.getType());
    	}
    	
    	return type;
    }
}

