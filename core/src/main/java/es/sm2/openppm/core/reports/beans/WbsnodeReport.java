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
 * File: WbsnodeReport.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.reports.beans;

import es.sm2.openppm.core.reports.ReportDataSource;

/**
 * Created by jaume.sastre on 25/11/2014.
 */
public class WbsnodeReport {

    private String wbsnodeName;
    private String wbsPoc;
    private ReportDataSource<WbsnodeReport> wbsnodeChilds;
    private String childsLength;

    public WbsnodeReport(String wbsnodeName, String wbsPoc, ReportDataSource<WbsnodeReport> wbsnodeChilds, String childsLength){

        this.wbsnodeName = wbsnodeName;
        this.wbsPoc = wbsPoc;
        this.wbsnodeChilds = wbsnodeChilds;
        this.childsLength = childsLength;

    }

    /**
     * @return the wbsnodeName
     */
    public String getWbsnodeName() {
        return wbsnodeName;
    }

    /**
     * @param wbsnodeName the wbsnodeName to set
     */
    public void setWbsnodeName(String wbsnodeName) {
        this.wbsnodeName = wbsnodeName;
    }

    /**
     * @return the wbsPoc
     */
    public String getWbsPoc() {
        return wbsPoc;
    }

    /**
     * @param wbsPoc the wbsPoc to set
     */
    public void setWbsPoc(String wbsPoc) {
        this.wbsPoc = wbsPoc;
    }

    public ReportDataSource<WbsnodeReport> getWbsnodeChilds() {
        return wbsnodeChilds;
    }

    public void setWbsnodeChilds(ReportDataSource<WbsnodeReport> wbsnodeChilds) {
        this.wbsnodeChilds = wbsnodeChilds;
    }

    public String getChildsLength() {
        return childsLength;
    }

    public void setChildsLength(String childsLength) {
        this.childsLength = childsLength;
    }
}
