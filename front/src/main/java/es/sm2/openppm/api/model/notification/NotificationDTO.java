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
 * File: NotificationDTO.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 19:33:51
 */

package es.sm2.openppm.api.model.notification;

import es.sm2.openppm.api.model.common.EntityDTO;

import java.util.Date;

/**
 * Created by javier.hernandez on 14/09/2015.
 */
public class NotificationDTO extends EntityDTO {

    private String status;
    private String type;
    private String subject;
    private String body;
    private Date creationDate;
    private Date readDate;
    private String messageError;

    /**
     * Getter for property 'status'.
     *
     * @return Value for property 'status'.
     */
    public String getStatus() {

        return status;
    }

    /**
     * Setter for property 'status'.
     *
     * @param status Value to set for property 'status'.
     */
    public void setStatus(String status) {

        this.status = status;
    }

    /**
     * Getter for property 'type'.
     *
     * @return Value for property 'type'.
     */
    public String getType() {

        return type;
    }

    /**
     * Setter for property 'type'.
     *
     * @param type Value to set for property 'type'.
     */
    public void setType(String type) {

        this.type = type;
    }

    /**
     * Getter for property 'subject'.
     *
     * @return Value for property 'subject'.
     */
    public String getSubject() {

        return subject;
    }

    /**
     * Setter for property 'subject'.
     *
     * @param subject Value to set for property 'subject'.
     */
    public void setSubject(String subject) {

        this.subject = subject;
    }

    /**
     * Getter for property 'body'.
     *
     * @return Value for property 'body'.
     */
    public String getBody() {

        return body;
    }

    /**
     * Setter for property 'body'.
     *
     * @param body Value to set for property 'body'.
     */
    public void setBody(String body) {

        this.body = body;
    }

    /**
     * Getter for property 'creationDate'.
     *
     * @return Value for property 'creationDate'.
     */
    public Date getCreationDate() {

        return creationDate;
    }

    /**
     * Setter for property 'creationDate'.
     *
     * @param creationDate Value to set for property 'creationDate'.
     */
    public void setCreationDate(Date creationDate) {

        this.creationDate = creationDate;
    }

    /**
     * Getter for property 'readDate'.
     *
     * @return Value for property 'readDate'.
     */
    public Date getReadDate() {

        return readDate;
    }

    /**
     * Setter for property 'readDate'.
     *
     * @param readDate Value to set for property 'readDate'.
     */
    public void setReadDate(Date readDate) {

        this.readDate = readDate;
    }

    /**
     * Getter for property 'messageError'.
     *
     * @return Value for property 'messageError'.
     */
    public String getMessageError() {

        return messageError;
    }

    /**
     * Setter for property 'messageError'.
     *
     * @param messageError Value to set for property 'messageError'.
     */
    public void setMessageError(String messageError) {

        this.messageError = messageError;
    }
}
