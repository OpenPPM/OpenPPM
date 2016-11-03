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
 * Module: plugin-project-change
 * File: ProjectActivityReport.java
 * Create User: javier.hernandez
 * Create Date: 20/04/2015 18:35:38
 */

package es.sm2.openppm.plugin.change.model;

/**
 * Created by javier.hernandez on 20/04/2015.
 */
public class ProjectActivityReport {

    private String name;
    private String estimatedEffort;
    private String estimatedCost;

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {

        return name;
    }

    /**
     * Setter for property 'name'.
     *
     * @param name Value to set for property 'name'.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Getter for property 'estimatedEffort'.
     *
     * @return Value for property 'estimatedEffort'.
     */
    public String getEstimatedEffort() {

        return estimatedEffort;
    }

    /**
     * Setter for property 'estimatedEffort'.
     *
     * @param estimatedEffort Value to set for property 'estimatedEffort'.
     */
    public void setEstimatedEffort(String estimatedEffort) {

        this.estimatedEffort = estimatedEffort;
    }

    /**
     * Getter for property 'estimatedCost'.
     *
     * @return Value for property 'estimatedCost'.
     */
    public String getEstimatedCost() {

        return estimatedCost;
    }

    /**
     * Setter for property 'estimatedCost'.
     *
     * @param estimatedCost Value to set for property 'estimatedCost'.
     */
    public void setEstimatedCost(String estimatedCost) {

        this.estimatedCost = estimatedCost;
    }
}
