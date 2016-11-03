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
 * File: GenericActionListener.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.listener.common;

import es.sm2.openppm.core.listener.ProcessListener;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.utils.Info;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by javier.hernandez on 23/10/2014.
 */
public class GenericActionListener {

    protected GenericActionListener() {
        // These class is not instantiate
    }

    private Map<String, Object> extraData;

    public Map<String, Object> getExtraData() {
        return extraData;
    }

    public void setExtraData(Map<String, Object> extraData) {
        this.extraData = extraData;
    }

    /**
     * Get settings from extra data
     *
     * @return - settings
     */
    protected Map<String, String> getSettings() {

        return (Map<String, String>) getExtraData().get(ProcessListener.EXTRA_DATA_SETTINGS);
    }

    /**
     * Get user from extra data
     *
     * @return - settings
     */
    protected Employee getUser() {

        return (Employee) getExtraData().get(ProcessListener.EXTRA_DATA_USER);
    }

    /**
     * Get session from extra data
     *
     * @return
     */
    protected Session getSession() {

        return (Session) getExtraData().get(ProcessListener.EXTRA_DATA_SESSION);
    }

    /**
     * Get session from extra data
     *
     * @return
     */
    protected ResourceBundle getResourceBundle() {

        return (ResourceBundle) getExtraData().get(ProcessListener.EXTRA_DATA_RESOURCE_BUNDLE);
    }

    /**
     * Get id project from extra data
     *
     * @return - settings
     */
    protected Integer getIdProject() {

        return (Integer) getExtraData().get(ProcessListener.EXTRA_DATA_IDPROJECT);
    }

    /**
     * Get infos from extra data
     *
     * @return - settings
     */
    protected List<Info> getInfo() {

        return (List<Info>) getExtraData().get(ProcessListener.EXTRA_DATA_INFO);
    }
}
