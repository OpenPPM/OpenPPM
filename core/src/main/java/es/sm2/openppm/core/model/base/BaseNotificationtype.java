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
 * File: BaseNotificationtype.java
 * Create User: daniel.casas
 * Create Date: 31/07/2015 12:14:40
 */

package es.sm2.openppm.core.model.base;

import java.util.HashSet;
import java.util.Set;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Notificationprofile;
import es.sm2.openppm.core.model.impl.Notificationtype;


/**
* Base Pojo object for domain model class Notificationtype
* @see es.sm2.openppm.core.model.impl.Notificationtype
* @author 
* For implement your own methods use class Notificationtype
*/
public class BaseNotificationtype implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String ENTITY = "NOTIFICATIONTYPE";

	public static final String IDNOTIFICATIONTYPE = "idNotificationType";
	public static final String KEY = "key";
	public static final String STATUS = "status";
	public static final String DISTRIBUTIONLIST = "distributionList";
	public static final String COMPANY = "company";
	public static final String PROFILES = "profiles";

	private Integer idNotificationType;
	private String key;
	private String status;
	private String distributionList;
	private Company company;
	private Set<Notificationprofile> profiles = new HashSet<Notificationprofile>(0);

	public BaseNotificationtype() {
	}

	public BaseNotificationtype(Integer idNotificationType) {
		this.idNotificationType = idNotificationType;
	}

	public Integer getIdNotificationType() {
		return this.idNotificationType;
	}
   
	public void setIdNotificationType(Integer idNotificationType) {
		this.idNotificationType = idNotificationType;
	}
   
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDistributionList() {
		return this.distributionList;
	}

	public void setDistributionList(String distributionList) {
		this.distributionList = distributionList;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

    /**
	 * @return the profiles
	 */
	public Set<Notificationprofile> getProfiles() {
		return profiles;
	}

	/**
	 * @param profiles the profiles to set
	 */
	public void setProfiles(Set<Notificationprofile> profiles) {
		this.profiles = profiles;
	}

	@Override
    public boolean equals(Object other) {
        boolean result = false;
        if (this == other) { result = true; }
        else if (other == null) { result = false; }
        else if (!(other instanceof Notificationtype) )  { result = false; }
        else if (other != null) {
        	Notificationtype castOther = (Notificationtype) other;
            if (castOther.getIdNotificationType().equals(this.getIdNotificationType())) { result = true; }
        }
        return result;
    }
}
