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
 * File: NotificationSearch.java
 * Create User: javier.hernandez
 * Create Date: 14/09/2015 19:24:19
 */

package es.sm2.openppm.core.model.search;

import es.sm2.openppm.core.model.impl.Contact;

import java.util.Date;

/**
 * Created by javier.hernandez on 14/09/2015.
 */
public class NotificationSearch {

    private Contact contact;
    private Date since;
    private Date until;
    private String subject;
    private String body;
    private String status;
    private boolean includeRead;

    public NotificationSearch(Contact contact) {
        this.contact = contact;
    }

    /**
     * Getter for property 'includeRead'.
     *
     * @return Value for property 'includeRead'.
     */
    public boolean isIncludeRead() {

        return includeRead;
    }

    /**
     * Setter for property 'includeRead'.
     *
     * @param includeRead Value to set for property 'includeRead'.
     */
    public void setIncludeRead(boolean includeRead) {

        this.includeRead = includeRead;
    }

    /**
     * Getter for property 'contact'.
     *
     * @return Value for property 'contact'.
     */
    public Contact getContact() {

        return contact;
    }

    /**
     * Setter for property 'contact'.
     *
     * @param contact Value to set for property 'contact'.
     */
    public void setContact(Contact contact) {

        this.contact = contact;
    }

    /**
     * Getter for property 'since'.
     *
     * @return Value for property 'since'.
     */
    public Date getSince() {

        return since;
    }

    /**
     * Setter for property 'since'.
     *
     * @param since Value to set for property 'since'.
     */
    public void setSince(Date since) {

        this.since = since;
    }

    /**
     * Getter for property 'until'.
     *
     * @return Value for property 'until'.
     */
    public Date getUntil() {

        return until;
    }

    /**
     * Setter for property 'until'.
     *
     * @param until Value to set for property 'until'.
     */
    public void setUntil(Date until) {

        this.until = until;
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
}
