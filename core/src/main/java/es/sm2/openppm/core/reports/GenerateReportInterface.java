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
 * File: GenerateReportInterface.java
 * Create User: javier.hernandez
 * Create Date: 21/03/2015 10:40:36
 */

package es.sm2.openppm.core.reports;

import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.reports.beans.ReportFile;
import es.sm2.openppm.core.reports.beans.ReportParams;
import es.sm2.openppm.core.reports.exceptions.ReportException;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Created by javier.hernandez on 21/03/2015.
 */
public interface GenerateReportInterface {

    /**
     * Generate report
     *
     * @param reportParams
     * @return
     * @throws ReportException
     */
    public ReportFile generate(ReportParams reportParams) throws ReportException;
}
