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
 * Module: plugin-project-synchronize
 * File: ExportAction.java
 * Create User: javier.hernandez
 * Create Date: 22/03/2015 13:03:59
 */

package es.sm2.openppm.plugin.synchronize.action;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.plugin.action.GenericAction;
import es.sm2.openppm.core.plugin.action.beans.FileResponse;
import es.sm2.openppm.core.plugin.action.beans.PluginResponse;
import es.sm2.openppm.core.plugin.annotations.PluginAction;
import es.sm2.openppm.plugin.synchronize.logic.SynchronizeLogic;
import es.sm2.openppm.utils.DocumentUtils;

import java.io.File;

/**
 * SM2 Baleares
 * Created by javier.hernandez on 02/10/2014.
 */
@PluginAction(action = "ExportSynchronizeAction", plugin = "SynchronizeProject")
public class ExportSynchronizeAction extends GenericAction {

    @Override
    public PluginResponse process() {

        FileResponse response = new FileResponse();

        if (isUserRole(Constants.ROLE_PMO) || isUserRole(Constants.ROLE_PM)) {

            try {
                SynchronizeLogic logic = new SynchronizeLogic();
                File file = logic.exportFile(getIdProject());

                if (file != null) {

                    // Create response
                    response.setFileBytes(DocumentUtils.getBytesFromFile(file));
                    response.setName(file.getName());

                    // delete temporal file
                    DocumentUtils.deleteFile(file);
                }
            } catch (Exception e) {
                addError(response, this.getClass(), e);
            }
        }

        return response;
    }
}
