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
 * File: AuditDTO.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 17:50:24
 */

package es.sm2.openppm.api.model.project;

import es.sm2.openppm.api.model.common.EntityDTO;

import java.util.Date;

/**
 * Created by jordi.ripoll on 10/08/2015.
 */
public class AuditDTO extends EntityDTO {

    private Date creationDate;
    private String projectStatus;
    private String username;
    private ProjectDTO project;

    public AuditDTO(){
    }

    /**
     * Getter for property 'creationDate'.
     *
     * @return Value for property 'creationDate'.
     */
    public Date getCreationDate() {

        return creationDate;
    }

    /**
     * Setter for property 'creationDate'.
     *
     * @param creationDate Value to set for property 'creationDate'.
     */
    public void setCreationDate(Date creationDate) {

        this.creationDate = creationDate;
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
     * Getter for property 'username'.
     *
     * @return Value for property 'username'.
     */
    public String getUsername() {

        return username;
    }

    /**
     * Setter for property 'username'.
     *
     * @param username Value to set for property 'username'.
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Getter for property 'project'.
     *
     * @return Value for property 'project'.
     */
    public ProjectDTO getProject() {

        return project;
    }

    /**
     * Setter for property 'project'.
     *
     * @param project Value to set for property 'project'.
     */
    public void setProject(ProjectDTO project) {

        this.project = project;
    }
}
