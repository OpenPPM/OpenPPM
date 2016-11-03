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
 * File: ReportParams.java
 * Create User: javier.hernandez
 * Create Date: 21/03/2015 12:01:11
 */

package es.sm2.openppm.core.reports.beans;

import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by javier.hernandez on 21/03/2015.
 */
public class ReportParams {

    public enum Type {
        CHANGE_ID
    }

    private Project project;
    private ResourceBundle bundle;
    private HashMap<String, String> settings;
    private Employee user;
    private Map<Type, Object> parameters = new HashMap<Type, Object>();

    public ReportParams(Project project, ResourceBundle bundle, HashMap<String, String> settings, Employee user) {

        this.project = project;
        this.bundle = bundle;
        this.settings = settings;
        this.user = user;
    }

    public ReportParams() {

    }

    /**
     * Add parameter
     *
     * @param type
     * @param data
     */
    public void addParam(Type type, Object data) {
        parameters.put(type, data);
    }

    /**
     * Get parameter
     *
     * @param type
     * @return
     */
    public Object getParam(Type type) {
        return parameters.get(type);
    }

    /**
     * Getter for property 'project'.
     *
     * @return Value for property 'project'.
     */
    public Project getProject() {

        return project;
    }

    /**
     * Setter for property 'project'.
     *
     * @param project Value to set for property 'project'.
     */
    public void setProject(Project project) {

        this.project = project;
    }

    /**
     * Getter for property 'bundle'.
     *
     * @return Value for property 'bundle'.
     */
    public ResourceBundle getBundle() {

        return bundle;
    }

    /**
     * Setter for property 'bundle'.
     *
     * @param bundle Value to set for property 'bundle'.
     */
    public void setBundle(ResourceBundle bundle) {

        this.bundle = bundle;
    }

    /**
     * Getter for property 'settings'.
     *
     * @return Value for property 'settings'.
     */
    public HashMap<String, String> getSettings() {

        return settings;
    }

    /**
     * Setter for property 'settings'.
     *
     * @param settings Value to set for property 'settings'.
     */
    public void setSettings(HashMap<String, String> settings) {

        this.settings = settings;
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
}
