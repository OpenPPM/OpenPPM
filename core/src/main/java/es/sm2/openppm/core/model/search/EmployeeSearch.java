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
 * File: EmployeeSearch.java
 * Create User: jordi.ripoll
 * Create Date: 06/10/2015 13:29:37
 */

package es.sm2.openppm.core.model.search;

import es.sm2.openppm.core.model.base.BaseSearch;
import es.sm2.openppm.core.model.impl.Company;

/**
 * Created by jordi.ripoll on 06/10/2015.
 */
public class EmployeeSearch extends BaseSearch {

    private Integer codeProfile;
    private Integer codePerformingOrganization;


    public EmployeeSearch(Company company) {
        super(company);
    }

    /**
     * Getter for property 'codeProfile'.
     *
     * @return Value for property 'codeProfile'.
     */
    public Integer getCodeProfile() {
        return codeProfile;
    }

    /**
     * Setter for property 'codeProfile'.
     *
     * @param codeProfile Value to set for property 'codeProfile'.
     */
    public void setCodeProfile(Integer codeProfile) {
        this.codeProfile = codeProfile;
    }

    /**
     * Getter for property 'codePerformingOrganization'.
     *
     * @return Value for property 'codePerformingOrganization'.
     */
    public Integer getCodePerformingOrganization() {
        return codePerformingOrganization;
    }

    /**
     * Setter for property 'codePerformingOrganization'.
     *
     * @param codePerformingOrganization Value to set for property 'codePerformingOrganization'.
     */
    public void setCodePerformingOrganization(Integer codePerformingOrganization) {
        this.codePerformingOrganization = codePerformingOrganization;
    }
}
