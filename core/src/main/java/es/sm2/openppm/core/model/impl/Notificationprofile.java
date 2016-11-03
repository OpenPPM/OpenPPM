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
 * File: Notificationprofile.java
 * Create User: daniel.casas
 * Create Date: 31/07/2015 12:45:50
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.model.base.BaseNotificationprofile;
import es.sm2.openppm.core.model.enums.ModeNotificationEnum;
import es.sm2.openppm.core.model.enums.StatusNotificationEnum;


/**
 * Model class Notificationprofile.
 * @author
 */
public class Notificationprofile extends BaseNotificationprofile {

	private static final long serialVersionUID = 1L;

    public Notificationprofile() {
		super();
    }
    
    public Notificationprofile(Integer idNotificationProfile) {
		super(idNotificationProfile);
    }
    
    public ModeNotificationEnum getModeNotification() {
    	return ModeNotificationEnum.valueOf(getMode());
    }
    
    public StatusNotificationEnum getStatusNotification() {
    	return StatusNotificationEnum.valueOf(getStatus());
    }
    
    public boolean isReadOnly() {
    	// TODO Comprobar el valor del atributo readOnly para convertirlo a boolean
    	return false;
    }
}

