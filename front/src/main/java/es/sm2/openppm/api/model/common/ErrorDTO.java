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
 * File: ErrorDTO.java
 * Create User: javier.hernandez
 * Create Date: 05/08/2015 08:29:31
 */

package es.sm2.openppm.api.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.core.Response;

/**
 * Created by javier.hernandez on 05/08/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {

    private int status;
    private String code;
    private String message;
    private String developerMessage;
    private String moreInfo;

    public ErrorDTO(Response.Status status, String message) {
        this.status = status.getStatusCode();
        this.message = message;
    }

    /**
     * Getter for property 'status'.
     *
     * @return Value for property 'status'.
     */
    public int getStatus() {

        return status;
    }

    /**
     * Setter for property 'status'.
     *
     * @param status Value to set for property 'status'.
     */
    public void setStatus(int status) {

        this.status = status;
    }

    /**
     * Getter for property 'code'.
     *
     * @return Value for property 'code'.
     */
    public String getCode() {

        return code;
    }

    /**
     * Setter for property 'code'.
     *
     * @param code Value to set for property 'code'.
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Getter for property 'message'.
     *
     * @return Value for property 'message'.
     */
    public String getMessage() {

        return message;
    }

    /**
     * Setter for property 'message'.
     *
     * @param message Value to set for property 'message'.
     */
    public void setMessage(String message) {

        this.message = message;
    }

    /**
     * Getter for property 'developerMessage'.
     *
     * @return Value for property 'developerMessage'.
     */
    public String getDeveloperMessage() {

        return developerMessage;
    }

    /**
     * Setter for property 'developerMessage'.
     *
     * @param developerMessage Value to set for property 'developerMessage'.
     */
    public void setDeveloperMessage(String developerMessage) {

        this.developerMessage = developerMessage;
    }

    /**
     * Getter for property 'moreInfo'.
     *
     * @return Value for property 'moreInfo'.
     */
    public String getMoreInfo() {

        return moreInfo;
    }

    /**
     * Setter for property 'moreInfo'.
     *
     * @param moreInfo Value to set for property 'moreInfo'.
     */
    public void setMoreInfo(String moreInfo) {

        this.moreInfo = moreInfo;
    }
}
