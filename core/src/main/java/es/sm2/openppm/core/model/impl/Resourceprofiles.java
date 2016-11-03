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
 * File: Resourceprofiles.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.model.base.BaseResourceprofiles;



/**
 * Model class Resourceprofiles.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Resourceprofiles extends BaseResourceprofiles {

	private static final long serialVersionUID = 1L;

    public Resourceprofiles() {
		super();
    }
    public Resourceprofiles(int idProfile) {
		super(idProfile);
    }

	private Profile profile = null;

	/**
	 * Get Enum profile
	 *
	 * @return
	 */
	public Profile getProfile() {

		int i = 0;
		while (profile == null && i < Profile.values().length) {
			Profile currentProfile = Profile.values()[i++];
			if (currentProfile.getID() == getIdProfile()) {
				profile = currentProfile;
			}
		}

		return profile;
	}

	public enum Profile {
    	RESOURCE("RESOURCE",1),
    	PROJECT_MANAGER("PROJECT_MANAGER",2),
    	INVESTMENT_MANAGER("INVESTMENT_MANAGER",3),
    	FUNCTIONAL_MANAGER("FUNCTIONAL_MANAGER",4),
    	PROGRAM_MANAGER("PROGRAM_MANAGER",5),
    	RESOURCE_MANAGER("RESOURCE_MANAGER",6),
    	PMO("PMO",7),
    	SPONSOR("SPONSOR",8),
    	PORFOLIO_MANAGER("PORFOLIO_MANAGER",9),
    	ADMIN("ADMIN",10),
    	STAKEHOLDER("STAKEHOLDER",11),
        LOGISTIC("LOGISTIC",12);
    	
    	private String profileName;
    	private int id;
    	
    	private Profile(String profileName, int id) {
    		this.profileName = profileName;
    		this.id = id;
    	}

		/**
		 * @return the mode
		 */
		public String getProfileName() {
			return profileName;
		}

        /**
         * @return the number of profile equals bbdd
         */
        public int getID() {
        	return id;
        }
        
        
    }
}

