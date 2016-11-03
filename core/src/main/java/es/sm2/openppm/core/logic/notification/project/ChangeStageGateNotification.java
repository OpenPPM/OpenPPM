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
 * File: ChangeStageGateNotification.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.notification.project;

import es.sm2.openppm.core.listener.exceptions.ActionListenerException;
import es.sm2.openppm.core.listener.impl.project.SaveProjectAbstractListener;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.logic.notification.annotation.NotificationConfiguration;

/**
 * Created by javier.hernandez on 22/10/2014.
 */
@NotificationConfiguration(key = "ChangeStageGateNotification")
public class ChangeStageGateNotification extends SaveProjectAbstractListener {

    @Override
    public void run(Project oldInstance, Project newInstance) throws ActionListenerException {

        // implementation code

        if (oldInstance != null && oldInstance.getStagegate() != null && newInstance.getStagegate() != null &&
                !oldInstance.getStagegate().getIdStageGate().equals(newInstance.getStagegate().getIdStageGate())) {

            // TODO javier.hernandez - 29/10/2014 - ejemplo de notificacion con listener
        }
    }
}
