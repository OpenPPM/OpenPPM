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
 * File: ChangeControlSearch.java
 * Create User: javier.hernandez
 * Create Date: 22/09/2015 15:25:19
 */

package es.sm2.openppm.core.model.search;

import es.sm2.openppm.core.model.base.BaseSearch;

/**
 * Created by javier.hernandez on 22/09/2015.
 */
public class ChangeControlSearch extends BaseSearch {

    private Boolean resolution;

    public ChangeControlSearch(Integer idProject, Boolean resolution) {

        super(idProject);
        this.resolution = resolution;
    }

    /**
     * Getter for property 'resolution'.
     *
     * @return Value for property 'resolution'.
     */
    public Boolean getResolution() {

        return resolution;
    }

    /**
     * Setter for property 'resolution'.
     *
     * @param resolution Value to set for property 'resolution'.
     */
    public void setResolution(Boolean resolution) {

        this.resolution = resolution;
    }
}
