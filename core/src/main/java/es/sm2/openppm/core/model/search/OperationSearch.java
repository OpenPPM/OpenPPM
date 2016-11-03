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
 * File: OperationSearch.java
 * Create User: javier.hernandez
 * Create Date: 25/09/2015 12:58:45
 */

package es.sm2.openppm.core.model.search;

import es.sm2.openppm.core.model.base.BaseSearch;
import es.sm2.openppm.core.model.impl.Company;

/**
 * Created by javier.hernandez on 25/09/2015.
 */
public class OperationSearch extends BaseSearch {

    private boolean availableForManager;
    private boolean availableForApprove;

    public OperationSearch(Company company) {

        super(company);
    }


    /**
     * Getter for property 'availableForManager'.
     *
     * @return Value for property 'availableForManager'.
     */
    public boolean isAvailableForManager() {

        return availableForManager;
    }

    /**
     * Setter for property 'availableForManager'.
     *
     * @param availableForManager Value to set for property 'availableForManager'.
     */
    public void setAvailableForManager(boolean availableForManager) {

        this.availableForManager = availableForManager;
    }

    public void setAvailableForApprove(boolean availableForApprove) {
        this.availableForApprove = availableForApprove;
    }

    public boolean isAvailableForApprove() {
        return availableForApprove;
    }
}
