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
 * File: BaseNotificationemployee.java
 * Create User: daniel.casas
 * Create Date: 31/07/2015 13:05:40
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Notificationemployee;
import es.sm2.openppm.core.model.impl.Notificationtype;
import es.sm2.openppm.core.model.impl.Employee;


/**
* Base Pojo object for domain model class Notificationemployee
* @see es.sm2.openppm.core.model.impl.Notificationemployee
* @author 
* For implement your own methods use class Notificationemployee
*/
public class BaseNotificationemployee implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ENTITY = "NOTIFICATIONEMPLOYEE";

	public static final String IDNOTIFICATIONEMPLOYEE = "idNotificationEmployee";
	public static final String STATUS = "status";
	public static final String MODE = "mode";
	public static final String NOTIFICATIONTYPE = "notificationType";
	public static final String EMPLOYEE = "employee";

	private Integer idNotificationEmployee;
	private String status;
	private String mode;
	private Notificationtype notificationType;
	private Employee employee;

	public BaseNotificationemployee() {
	}

	public BaseNotificationemployee(Integer idNotificationEmployee) {
		this.idNotificationEmployee = idNotificationEmployee;
	}

	public Integer getIdNotificationEmployee() {
		return this.idNotificationEmployee;
	}
	
	public void setIdNotificationEmployee(Integer idNotificationEmployee) {
		this.idNotificationEmployee = idNotificationEmployee;
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
	
	public Employee getEmployee() {
		return this.employee;
	}
	
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (this == other) { result = true; }
        else if (other == null) { result = false; }
        else if (!(other instanceof Notificationemployee) )  { result = false; }
        else if (other != null) {
        	Notificationemployee castOther = (Notificationemployee) other;
            if (castOther.getIdNotificationEmployee().equals(this.getIdNotificationEmployee())) { result = true; }
        }
        return result;
    }

}


