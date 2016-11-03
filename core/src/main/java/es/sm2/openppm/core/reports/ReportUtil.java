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
 * File: ReportUtil.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 17:07:35
 */

package es.sm2.openppm.core.reports;

import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.cache.exceptions.CacheException;
import es.sm2.openppm.core.reports.annotations.ReportType;
import es.sm2.openppm.core.reports.beans.ReportFile;
import es.sm2.openppm.utils.LogManager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by javier.hernandez on 22/03/2015.
 */
public class ReportUtil {

    private static ReportUtil ourInstance = new ReportUtil();

    public static ReportUtil getInstance() {

        return ourInstance;
    }

    private ReportUtil() {

    }


    /**
     * Check if report is activated
     *
     * @param reportType
     * @return
     */
    public boolean isReportActive(ReportType reportType) {

        boolean active = false;

        try {
            GenerateReportInterface generateReportInterface = CacheStatic.getGenerateReport(reportType);

            active = (generateReportInterface != null);

        } catch (CacheException e) {
            LogManager.getLog(getClass()).warn("Consult report to cache: " + reportType.name(), e);
        }

        return active;
    }

    /**
     * Get absolute file for development
     * @param file
     * @return
     */
    public InputStream getDeveloperInputStream(String file) {

        InputStream is = null;

        try {
            is = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            LogManager.getLog(this.getClass()).debug("File not found: " + file);
        } catch (IOException e) {
            LogManager.getLog(this.getClass()).debug("Error load file: " + file, e);
        }

        return is;
    }

    /**
     * Parse report files in one zip file
     *
     * @param files
     * @return
     * @throws IOException
     */
    public byte[] zipFiles(List<ReportFile> files) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        for (ReportFile file : files) {

            zos.putNextEntry(new ZipEntry(file.getName()));

            zos.write(file.getFile());

            zos.closeEntry();
        }

        zos.flush();
        baos.flush();
        zos.close();
        baos.close();

        return baos.toByteArray();
    }
}
