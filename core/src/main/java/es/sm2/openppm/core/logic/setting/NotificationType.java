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
 * File: NotificationType.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.setting;

import es.sm2.openppm.core.common.GenericMail;
import es.sm2.openppm.core.common.GenericSetting;
import es.sm2.openppm.core.logic.setting.common.TypeSetting;
import es.sm2.openppm.core.model.impl.Notification.NotificationStatus;

public enum NotificationType implements GenericSetting, GenericMail {

    MILESTONE("true", false),
    MILESTONE_PMO("false"),
    PROJECT_DELAY("false"),
    PROJECT_NOT_DELAY("false"),
    CHANGE_PLANNED_DATES("false"),
    CHANGE_CONTROL_DATES("false"),
    TEAMMEMBER_ASSIGNATION("true"), // TODO crear notificacion en NotificationSpecificLogic
    TEAMMEMBER_ASSIGNATION_UPDATE("true"), // TODO crear notificacion en NotificationSpecificLogic
    CHANGE_PROJECT_STATUS("false"),
    DELETE_MILESTONE("true"),
    CHANGE_STAGE_GATE("true"),
    REQUEST_REJECTED_HOURS("true"),
    REJECTED_HOURS("true"),
    APPROVE_OPERATION("true"),
    ASSIGNED_OPERATION("true"),
    CHANGE_GEOGRAPHY("true"),
    ASSIGNMENT_PROJECT("true"),
    PROJECT_APPROVAL("true"),
    EXPIRED_INVESTMENT("true"),
    CHANGE_TIMESHEET_STATUS("false");

    private String defaultValue;
    private boolean visible;
    private NotificationStatus notificationStatus = NotificationStatus.PENDING;

    private NotificationType(String defaultValue) {

        this.defaultValue 	= defaultValue;
        this.visible		= true;
    }

    private NotificationType(String defaultValue, boolean visible) {

        this.defaultValue 	= defaultValue;
        this.visible		= visible;
    }

    public Enum[] getEnumElements() {
        return null;
    }

    public TypeSetting getTypeSetting() {
        return TypeSetting.CHECKBOX;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name();
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return the messageInfo
     */
    public String getMessageInfo() {
        return "SETTING."+this.name()+"_INFO";
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return "SETTING."+this.name();
    }

    /**
     * @return the body
     */
    public String getBody() {
        return "SETTING."+this.name()+"_BODY";
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return "SETTING."+this.name()+"_SUBJECT";
    }

    /**
     * @return the notification status
     */
    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    /**
     * @return the showCheck
     */
    public boolean isVisible() {
        return visible;
    }
}