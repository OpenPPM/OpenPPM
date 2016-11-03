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
 * File: Contactnotification.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.impl;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.model.base.BaseContactnotification;



/**
 * Model class Contactnotification.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Contactnotification extends BaseContactnotification {

	public final static Logger LOGGER = Logger.getLogger(Contactnotification.class);
	
	private static final long serialVersionUID = 1L;

    public Contactnotification() {
		super();
    }
    public Contactnotification(Integer idContactNotification) {
		super(idContactNotification);
    }

    public enum ContactnotificationType {
    	TO("TO"),
    	COPY("COPY"),
    	HIDDEN_COPY("HIDDEN_COPY");
    	
    	private String type;
    	
    	private ContactnotificationType(String type) {
    		this.type = type;
    	}

		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}
    }
    
    /**
     * Get notification type
     * 
     * @return
     */
    public ContactnotificationType getContactNotificationType() {
    	
    	ContactnotificationType type = null;
    	
    	try {
    		type = ContactnotificationType.valueOf(this.getType());
    	}
    	catch (Exception e) {
    		LOGGER.warn("Contact notification type is not defined: "+this.getType());
    	}
    	
    	return type;
    }
    
}

