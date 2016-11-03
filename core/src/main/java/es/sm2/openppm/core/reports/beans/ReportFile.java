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
 * File: ReportFile.java
 * Create User: javier.hernandez
 * Create Date: 21/03/2015 10:42:31
 */

package es.sm2.openppm.core.reports.beans;

/**
 * Created by javier.hernandez on 21/03/2015.
 */
public class ReportFile {

    private byte[] file;
    private String name;
    private String extension;

    /**
     * Getter for property 'file'.
     *
     * @return Value for property 'file'.
     */
    public byte[] getFile() {

        return file;
    }

    /**
     * Setter for property 'file'.
     *
     * @param file Value to set for property 'file'.
     */
    public void setFile(byte[] file) {

        this.file = file;
    }

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
     * Getter for property 'extension'.
     *
     * @return Value for property 'extension'.
     */
    public String getExtension() {

        return extension;
    }

    /**
     * Setter for property 'extension'.
     *
     * @param extension Value to set for property 'extension'.
     */
    public void setExtension(String extension) {

        this.extension = extension;
    }
}
