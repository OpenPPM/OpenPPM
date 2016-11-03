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
 * File: UpdateTimesheetNotification.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.notification.timesheet;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.listener.exceptions.ActionListenerException;
import es.sm2.openppm.core.listener.impl.timesheet.TimesheetAbstractListener;
import es.sm2.openppm.core.logic.impl.NotificationSpecificLogic;
import es.sm2.openppm.core.logic.notification.annotation.NotificationConfiguration;
import es.sm2.openppm.core.logic.setting.NotificationType;
import es.sm2.openppm.core.model.impl.Timesheet;
import es.sm2.openppm.core.utils.SettingUtil;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;

import java.util.Map;

/**
 * Created by jaume.sastre on 03/02/2015.
 */
@NotificationConfiguration(key = "UpdateTimesheetNotification")
public class UpdateTimesheetNotification extends TimesheetAbstractListener {

    @Override
    public void run(Timesheet oldTimesheet, Timesheet newTimesheet) throws ActionListenerException {

        if (oldTimesheet != null && ValidateUtil.isNotNull(oldTimesheet.getStatus()) &&
                newTimesheet != null && ValidateUtil.isNotNull(newTimesheet.getStatus())) {

            // Old status
            String oldStatus = oldTimesheet.getStatus();

            // New status
            String newStatus = newTimesheet.getStatus();

            // Settings
            Map<String, String> settings = getSettings();

            try {

                if (settings == null && newTimesheet!= null &&
                        newTimesheet.getEmployee() != null &&
                        newTimesheet.getEmployee().getPerformingorg() != null) {

                    settings = SettingUtil.getSettings(getSession(), newTimesheet.getEmployee().getPerformingorg().getCompany());
                }
                else {
                    LogManager.getLog(getClass()).debug("No data settings");
                }
            }
            catch (Exception e) {
                throw new ActionListenerException(e);
            }

            // Check oldstatus=APP0 and newStatus=APP1 or oldStatus=APP1 and newStatus=APP2
            if (((oldStatus.equals(Constants.TIMESTATUS_APP0) && newStatus.equals(Constants.TIMESTATUS_APP1)) ||
                    (oldStatus.equals(Constants.TIMESTATUS_APP0) && newStatus.equals(Constants.TIMESTATUS_APP2)) ||
                    (oldStatus.equals(Constants.TIMESTATUS_APP1) && newStatus.equals(Constants.TIMESTATUS_APP2))) &&
                    settings != null &&
                    SettingUtil.getBoolean(settings, NotificationType.CHANGE_TIMESHEET_STATUS)) {

                try {

                    NotificationSpecificLogic notificationSpecificLogic = new NotificationSpecificLogic();

                    notificationSpecificLogic.updateTimesheet(getSession(), newTimesheet, getResourceBundle());
                }
                catch (Exception e) {
                    throw new ActionListenerException(e);
                }
            }
        }
    }

}
