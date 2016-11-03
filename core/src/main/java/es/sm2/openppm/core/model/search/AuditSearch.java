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
 * File: AuditSearch.java
 * Create User: javier.hernandez
 * Create Date: 15/09/2015 15:34:43
 */

package es.sm2.openppm.core.model.search;

import es.sm2.openppm.core.common.enums.LocationAuditEnum;
import es.sm2.openppm.core.model.impl.Company;

import javax.ws.rs.QueryParam;
import java.util.Date;

/**
 * Created by javier.hernandez on 15/09/2015.
 */
public class AuditSearch {

    private Integer idProject;
    private Integer idContact;
    private String projectStatus;
    private Date since;
    private Date until;
    private Company company;
    private LocationAuditEnum location;

    public AuditSearch(LocationAuditEnum location, Integer idProject, Company company) {

        this.idProject = idProject;
        this.company = company;
        this.location = location;
    }

    /**
     * Getter for property 'location'.
     *
     * @return Value for property 'location'.
     */
    public LocationAuditEnum getLocation() {

        return location;
    }

    /**
     * Setter for property 'location'.
     *
     * @param location Value to set for property 'location'.
     */
    public void setLocation(LocationAuditEnum location) {

        this.location = location;
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
     * Getter for property 'idContact'.
     *
     * @return Value for property 'idContact'.
     */
    public Integer getIdContact() {

        return idContact;
    }

    /**
     * Setter for property 'idContact'.
     *
     * @param idContact Value to set for property 'idContact'.
     */
    public void setIdContact(Integer idContact) {

        this.idContact = idContact;
    }

    /**
     * Getter for property 'projectStatus'.
     *
     * @return Value for property 'projectStatus'.
     */
    public String getProjectStatus() {

        return projectStatus;
    }

    /**
     * Setter for property 'projectStatus'.
     *
     * @param projectStatus Value to set for property 'projectStatus'.
     */
    public void setProjectStatus(String projectStatus) {

        this.projectStatus = projectStatus;
    }

    /**
     * Getter for property 'since'.
     *
     * @return Value for property 'since'.
     */
    public Date getSince() {

        return since;
    }

    /**
     * Setter for property 'since'.
     *
     * @param since Value to set for property 'since'.
     */
    public void setSince(Date since) {

        this.since = since;
    }

    /**
     * Getter for property 'until'.
     *
     * @return Value for property 'until'.
     */
    public Date getUntil() {

        return until;
    }

    /**
     * Setter for property 'until'.
     *
     * @param until Value to set for property 'until'.
     */
    public void setUntil(Date until) {

        this.until = until;
    }

    @Override
    public String toString() {
        return "AuditSearch{" +
                "idProject=" + idProject +
                ", idContact=" + idContact +
                ", projectStatus='" + projectStatus + '\'' +
                ", since=" + since +
                ", until=" + until +
                ", company=" + company +
                ", location=" + location +
                '}';
    }
}
