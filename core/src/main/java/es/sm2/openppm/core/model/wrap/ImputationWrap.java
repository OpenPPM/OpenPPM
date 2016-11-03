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
 * File: ImputationWrap.java
 * Create User: jordi.ripoll
 * Create Date: 27/04/2015 13:03:52
 */

package es.sm2.openppm.core.model.wrap;

/**
 * Created by jordi.ripoll on 27/04/2015.
 */
public class ImputationWrap {

    public static final String PROJECTNAME = "projectName";
    public static final String OPERATIONNAME = "operationName";
    public static final String SHORTNAME = "operationName";
    public static final String HOURSAPP3 = "hoursAPP3";

    private String projectName;
    private String operationName;
    private String shortName;
    private Double hoursAPP3;

    /**
     * Constructor for project
     *
     * @param projectName
     * @param shortName
     * @param hoursAPP3
     */
    public ImputationWrap(String projectName, String shortName, Double hoursAPP3) {

        this.projectName = projectName;
        this.shortName = shortName;
        this.hoursAPP3 = hoursAPP3;
    }

    /**
     * Constructor for operation
     *
     * @param operationName
     * @param hoursAPP3
     */
    public ImputationWrap(String operationName, Double hoursAPP3) {

        this.operationName = operationName;
        this.shortName = operationName;
        this.hoursAPP3 = hoursAPP3;
    }


    public Double getHoursAPP3() {
        return hoursAPP3;
    }

    public void setHoursAPP3(Double hoursAPP3) {
        this.hoursAPP3 = hoursAPP3;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
