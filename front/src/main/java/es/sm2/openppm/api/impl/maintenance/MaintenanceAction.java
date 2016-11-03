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
 * File: MaintenanceAction.java
 * Create User: javier.hernandez
 * Create Date: 04/08/2015 09:57:53
 */

package es.sm2.openppm.api.impl.maintenance;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;

import javax.ws.rs.core.Response;
import java.util.ResourceBundle;

/**
 * Created by javier.hernandez on 04/08/2015.
 */
public interface MaintenanceAction {

    /**
     * Save
     *
     * @param object
     * @param company
     * @return
     */
    public Response save(Object object, Company company);

    /**
     * Update
     *
     * @param object
     * @param company
     * @return
     */
    public Response update(Object object, Company company);

    /**
     * Find all
     *
     * @return
     * @param company
     */
    public Response findAll(Company company, Employee User);

    /**
     * Find by id
     *
     * @param id
     * @return
     */
    public Response findByID(Integer id);

    /**
     * Delete
     *
     * @param id
     * @param resourceBundle
     * @return
     */
    public Response remove(Integer id, ResourceBundle resourceBundle);
}