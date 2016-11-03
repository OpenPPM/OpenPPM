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
 * Module: front
 * File: UserDTO.java
 * Create User: jordi.ripoll
 * Create Date: 20/08/2015 12:08:15
 */

package es.sm2.openppm.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by javier.hernandez on 05/08/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends EntityDTO {

    private String locale;
    private EntityDTO profile;

    /**
     * Getter for property 'locale'.
     *
     * @return Value for property 'locale'.
     */
    public String getLocale() {

        return locale;
    }

    /**
     * Setter for property 'locale'.
     *
     * @param locale Value to set for property 'locale'.
     */
    public void setLocale(String locale) {

        this.locale = locale;
    }

    public EntityDTO getProfile() {
        return profile;
    }

    public void setProfile(EntityDTO profile) {
        this.profile = profile;
    }
}
