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
 * File: SettingsResource.java
 * Create User: javier.hernandez
 * Create Date: 01/10/2015 16:32:57
 */

package es.sm2.openppm.api.rest.util;

import es.sm2.openppm.api.rest.BaseResource;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.logic.setting.GeneralSettingAuthorization;
import es.sm2.openppm.front.utils.SettingUtil;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * Resource for static settings
 */
@Path("/util/setting")
@Produces(MediaType.APPLICATION_JSON)
public class SettingsResource extends BaseResource {

    /**
     * List of static settings
     *
     * @return
     * @throws es.sm2.openppm.api.rest.error.RestServiceException
     */
	@GET
    public Response getSettingList() throws RestServiceException {

        HashMap<String, Object> settings = new HashMap<String, Object>();

        // Modules
        settings.put("SHOW_MANAGE_OPERATIONS", Settings.SHOW_MANAGE_OPERATIONS);
        settings.put("SHOW_LEARNED_LESSONS", Settings.SHOW_LEARNED_LESSONS);
        settings.put("SHOW_AUDIT", Settings.SHOW_AUDIT);
        settings.put("SHOW_NOTIFICATIONS", Settings.SHOW_NOTIFICATIONS);

        // Version
        settings.put("VERSION_APP", Settings.VERSION_APP);
        settings.put("VERSION_NAME_APP", Settings.VERSION_NAME_APP);
        settings.put("EASTER_EGG", Settings.EASTER_EGG);

        try {
            HashMap<String, String> settingStored = SettingUtil.getSettings(getCompany());

            settings.put(GeneralSettingAuthorization.ACTIVE_MANAGEMENT_OPERATION.name(),
                    SettingUtil.getBoolean(settingStored, GeneralSettingAuthorization.ACTIVE_MANAGEMENT_OPERATION));


        } catch (Exception e) {
            sendError(LogManager.getLog(getClass()), "getSettingList()", e);
        }

        return Response.ok(settings).build();
	}

}
