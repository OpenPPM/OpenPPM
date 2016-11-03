/*
 *   Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program has been created in the hope that it will be useful.
 *   It is distributed WITHOUT ANY WARRANTY of any Kind,
 *   without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see http://www.gnu.org/licenses/.
 *
 *   For more information, please contact SM2 Software & Services Management.
 *   Mail: info@talaia-openppm.com
 *   Web: http://www.talaia-openppm.com
 *
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;

/**
 * Created by francisco.bisquerra on 11/09/2015.
 */
public class BaseSearch {

    private String query;
    private Company company;
    private Integer idProject;
    private Employee user;

    public BaseSearch() {
    }

    public BaseSearch(Company company, Integer idProject) {

        this.company = company;
        this.idProject = idProject;
    }

    /**
     * Getter for property 'user'.
     *
     * @return Value for property 'user'.
     */
    public Employee getUser() {

        return user;
    }

    /**
     * Setter for property 'user'.
     *
     * @param user Value to set for property 'user'.
     */
    public void setUser(Employee user) {

        this.user = user;
    }

    public BaseSearch(Integer idProject) {

        this.idProject = idProject;
    }

    public BaseSearch(Company company) {

        this.company = company;
    }

    /**
     * Getter for property 'idProject'.
     *
     * @return Value for property 'idProject'.
     */
    public Integer getIdProject() {

        return idProject;
    }

    /**
     * Setter for property 'idProject'.
     *
     * @param idProject Value to set for property 'idProject'.
     */
    public void setIdProject(Integer idProject) {

        this.idProject = idProject;
    }

    /**
     * Getter for property 'query'.
     *
     * @return Value for property 'query'.
     */
    public String getQuery() {

        return query;
    }

    /**
     * Setter for property 'query'.
     *
     * @param query Value to set for property 'query'.
     */
    public void setQuery(String query) {

        this.query = query;
    }

    /**
     * Getter for property 'company'.
     *
     * @return Value for property 'company'.
     */
    public Company getCompany() {

        return company;
    }

    /**
     * Setter for property 'company'.
     *
     * @param company Value to set for property 'company'.
     */
    public void setCompany(Company company) {

        this.company = company;
    }
}
