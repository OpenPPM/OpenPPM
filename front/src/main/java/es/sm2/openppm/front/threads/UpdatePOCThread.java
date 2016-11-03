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
 * File: UpdatePOCThread.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.threads;

import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jordi.ripoll on 21/10/2014.
 */
public class UpdatePOCThread extends Thread {

    private static final Logger LOGGER = Logger.getLogger(UpdatePOCThread.class);

    private List<Project> projects;
    private HashMap<String, String> settings;

    public UpdatePOCThread(List<Project> projects, HashMap<String, String> settings) {
        this.projects = projects;
        this.settings = settings;
    }

    public void run() {

        // Logic
        ProjectLogic projectLogic = new ProjectLogic(getSettings(), null);

        if (ValidateUtil.isNotNull(projects)) {

            for (Project project : getProjects()) {

                try {

                    projectLogic.updatePoc(project, getSettings());

                    LOGGER.info("\t\t\tUpdate POC Project: " + project.getChartLabel());
                }
                catch (Exception e) {

                    LOGGER.error("\t\t\t Not Update POC Project: " + project.getChartLabel());

                    e.printStackTrace();
                }
            }
        }
    }

    public HashMap<String, String> getSettings() {
        return settings;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
