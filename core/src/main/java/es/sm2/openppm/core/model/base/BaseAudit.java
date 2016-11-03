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
 * File: BaseAudit.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 16:49:23
 */

package es.sm2.openppm.core.model.base;

import java.util.Date;

/**
 * Created by jordi.ripoll on 10/08/2015.
 */
public class BaseAudit {

    public static final String IDAUDIT = "idAudit";
    public static final String CREATIONDATE = "creationDate";
    public static final String LOCATION = "location";
    public static final String PROJECTSTATUS = "projectStatus";
    public static final String IDEMPLOYEE = "idEmployee";
    public static final String IDCONTACT = "idContact";
    public static final String IDCOMPANY = "idCompany";
    public static final String IDPROJECT = "idProject";
    public static final String USERNAME = "username";
    public static final String DATAOBJECT = "dataObject";

    private Integer idAudit;
    private Date creationDate;
    private String location;
    private String projectStatus;
    private Integer idEmployee;
    private Integer idContact;
    private Integer idCompany;
    private Integer idProject;
    private String username;
    private byte[] dataObject;

    public BaseAudit(){
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
     * Getter for property 'idCompany'.
     *
     * @return Value for property 'idCompany'.
     */
    public Integer getIdCompany() {

        return idCompany;
    }

    /**
     * Setter for property 'idCompany'.
     *
     * @param idCompany Value to set for property 'idCompany'.
     */
    public void setIdCompany(Integer idCompany) {

        this.idCompany = idCompany;
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
     * Getter for property 'dataObject'.
     *
     * @return Value for property 'dataObject'.
     */
    public byte[] getDataObject() {

        return dataObject;
    }

    /**
     * Setter for property 'dataObject'.
     *
     * @param dataObject Value to set for property 'dataObject'.
     */
    public void setDataObject(byte[] dataObject) {

        this.dataObject = dataObject;
    }

    public BaseAudit(Integer idAudit){
        idAudit = idAudit;
    }

    /**
     * Getter for property 'idAudit'.
     *
     * @return Value for property 'idAudit'.
     */
    public Integer getIdAudit() {

        return idAudit;
    }

    /**
     * Setter for property 'idAudit'.
     *
     * @param idAudit Value to set for property 'idAudit'.
     */
    public void setIdAudit(Integer idAudit) {

        this.idAudit = idAudit;
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
     * Getter for property 'location'.
     *
     * @return Value for property 'location'.
     */
    public String getLocation() {

        return location;
    }

    /**
     * Setter for property 'location'.
     *
     * @param location Value to set for property 'location'.
     */
    public void setLocation(String location) {

        this.location = location;
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
     * Getter for property 'idEmployee'.
     *
     * @return Value for property 'idEmployee'.
     */
    public Integer getIdEmployee() {

        return idEmployee;
    }

    /**
     * Setter for property 'idEmployee'.
     *
     * @param idEmployee Value to set for property 'idEmployee'.
     */
    public void setIdEmployee(Integer idEmployee) {

        this.idEmployee = idEmployee;
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
}
