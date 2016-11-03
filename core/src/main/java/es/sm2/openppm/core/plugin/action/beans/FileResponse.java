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
 * File: FileResponse.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.plugin.action.beans;

/**
 * SM2 Baleares
 * Created by javier.hernandez on 01/10/2014.
 */
public class FileResponse extends PluginResponse {

    @Override
    public ResponseType getResponseType() {
        return ResponseType.FILE;
    }

    byte[] fileBytes;
    private String name;
    private String contentType;

    /**
     * @return the fileBytes
     */
    public byte[] getFileBytes() {
        return fileBytes;
    }
    /**
     * @param fileBytes the fileBytes to set
     */
    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }
    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
