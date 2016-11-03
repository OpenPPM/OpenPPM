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
 * File: BaseProjectData.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;

import es.sm2.openppm.core.model.impl.Project;

import java.util.Date;

/**
 * Created by jaume.sastre on 13/02/2015.
 */
public class BaseProjectData  implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    public static final String IDPROJECTDATA = "idProjectData";
    public static final String PROJECT = "project";
    public static final String CANCELED = "canceled";
    public static final String DATECANCELED = "dateCanceled";
    public static final String COMMENTCANCELED = "commentCanceled";

    private Integer idProjectData;
    private Project project;
    private Boolean canceled;
    private Date dateCanceled;
    private String commentCanceled;

    public BaseProjectData() {
    }

    public BaseProjectData(Integer idProjectData) {
        this.idProjectData = idProjectData;
    }

    public Integer getIdProjectData() {
        return idProjectData;
    }

    public void setIdProjectData(Integer idProjectData) {
        this.idProjectData = idProjectData;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public Date getDateCanceled() {
        return dateCanceled;
    }

    public void setDateCanceled(Date dateCanceled) {
        this.dateCanceled = dateCanceled;
    }

    public String getCommentCanceled() {
        return commentCanceled;
    }

    public void setCommentCanceled(String commentCanceled) {
        this.commentCanceled = commentCanceled;
    }
}
