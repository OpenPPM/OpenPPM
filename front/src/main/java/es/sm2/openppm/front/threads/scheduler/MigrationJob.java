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
 * File: MigrationJob.java
 * Create User: jordi.ripoll
 * Create Date: 23/06/2015 15:34:41
 */

package es.sm2.openppm.front.threads.scheduler;

import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.impl.CompanyLogic;
import es.sm2.openppm.core.logic.impl.DocumentprojectLogic;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.utils.StringUtils;
import es.sm2.openppm.front.utils.ExceptionUtil;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.apache.commons.io.FileUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jordi.ripoll on 23/06/2015.
 */
public class MigrationJob implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {

            LogManager.getLog(getClass()).debug("START MIGRATION DOCUMENTS..");

            // Declare logic
            DocumentprojectLogic documentprojectLogic = new DocumentprojectLogic(null, null);

            // Get all documents
            //
            List<String> joins = new ArrayList<String>();
            joins.add(Documentproject.PROJECT);

            List<Documentproject> documents = documentprojectLogic.findAll(joins);

            if (ValidateUtil.isNotNull(documents)) {

                // Declare logic
                CompanyLogic companyLogic = new CompanyLogic();

                // Get companies
                List<Company> companies = companyLogic.findAll();

                if (ValidateUtil.isNotNull(companies)) {

                    for (Company company : companies) {

                        LogManager.getLog(getClass()).debug("Migration for company: " + company.getIdCompany());

                        // Get settings
                        HashMap<String, String> settings = SettingUtil.getSettings(company);

                        // Root directory
                        String docFolder = SettingUtil.getString(settings, Settings.SETTING_PROJECT_DOCUMENT_FOLDER, Settings.DEFAULT_PROJECT_DOCUMENT_FOLDER);

                        for (Documentproject document : documents) {

                            if (ValidateUtil.isNotNull(document.getName())) {

                                // Search document in root directory
                                //
                                File file = new File(docFolder + File.separator + document.getIdDocumentProject() + StringPool.UNDERLINE + document.getName());

                                if (file.exists()) {

                                    // Get project
                                    Project project = document.getProject();

                                    // Search folder by project
                                    String newDir = docFolder.concat(File.separator + StringUtils.formatting(project.getChartLabel()) + StringPool.UNDERLINE + project.getIdProject());

                                    File folderNewDir = new File(newDir);

                                    // If not exists create
                                    if (!folderNewDir.exists()) {
                                        folderNewDir.mkdir();
                                    }

                                    // Add subfolder
                                    folderNewDir = new File(newDir.concat(File.separator + document.getType()));

                                    // Rename file
                                    //
                                    String newName = document.getIdDocumentProject() + StringPool.UNDERLINE + (document.getName().lastIndexOf("\\") != -1 ?
                                            StringUtils.formatting(document.getName().substring(document.getName().lastIndexOf("\\") + 1)) :
                                            StringUtils.formatting(document.getName()));

                                    File newFileRename = new File(docFolder + File.separator, newName);

                                    if (file.renameTo(newFileRename)) {

                                        // Move file to directory
                                        FileUtils.moveFileToDirectory(newFileRename, folderNewDir, true);

                                        LogManager.getLog(getClass()).debug("Migration success: " + newFileRename.getAbsolutePath() + " to " + folderNewDir.getAbsolutePath());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            LogManager.getLog(getClass()).debug("END MIGRATION DOCUMENTS");
        }
        catch (Exception e) {
            ExceptionUtil.sendToLogger(LogManager.getLog(getClass()), e, null);
        }
    }
}
