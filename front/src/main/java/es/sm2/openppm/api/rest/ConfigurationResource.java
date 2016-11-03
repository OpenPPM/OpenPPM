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
 * File: ConfigurationResource.java
 * Create User: jordi.ripoll
 * Create Date: 19/08/2015 12:07:39
 */

package es.sm2.openppm.api.rest;

import es.sm2.openppm.api.model.common.ConfigurationDTO;
import es.sm2.openppm.api.rest.error.RestServiceException;
import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.GenericSetting;
import es.sm2.openppm.core.logic.impl.SettingLogic;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.utils.LogManager;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Resource for settings table
 *
 * Created by jordi.ripoll on 19/08/2015.
 */
@Path("/configuration")
@Produces({MediaType.APPLICATION_JSON})
public class ConfigurationResource extends BaseResource {

    /**
     * Get settings
     *
     * @return
     */
    @GET
    public Response getSettingsData() {

        // Check permissions
        Response  response = checkPermission();

        if (response == null) {

            try {

                // Find Data
                SettingLogic settingLogic = new SettingLogic();
                List<Setting> settings = settingLogic.findByCompany(getCompany());

                // Parse settings for find values
                HashMap<String, String> values = new HashMap<String, String>();
                for (Setting setting : settings) {
                    values.put(setting.getName(), setting.getValue());
                }

                // Response Values
                HashMap<String, String> responseValues = new HashMap<String, String>();

                // Add default values
                Set<Class<? extends GenericSetting>> configurationSettings = CacheStatic.getConfigurationSettings();
                for (Class<? extends GenericSetting> configSetting : configurationSettings) {
                    for (GenericSetting genericSetting : configSetting.getEnumConstants()) {

                        // Add configuration
                        if (values.containsKey(genericSetting.getName())) {
                            responseValues.put(genericSetting.getName(), values.get(genericSetting.getName()));
                        }
                        else {
                            responseValues.put(genericSetting.getName(), genericSetting.getDefaultValue());
                        }
                    }
                }

                // Converter and send data
                List<ConfigurationDTO> configurations = new ArrayList<ConfigurationDTO>();
                for (String settingCode : responseValues.keySet()) {
                    configurations.add(new ConfigurationDTO(settingCode, responseValues.get(settingCode)));
                }

                response = Response.ok().entity(configurations).build();

            }
            catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "getSettings()", e);
            }
        }

        return response;
    }

    @PUT
    @Path("{KEY}")
    public Response update(@PathParam("KEY") String key, ConfigurationDTO configurationDTO) {

        // Check permissions
        Response response = checkPermission(Constants.ROLE_ADMIN);

        if (response == null) {

            // Default Configurations
            HashMap<String, GenericSetting> configurations = new HashMap<String, GenericSetting>();

            // Find enum configuration
            Set<Class<? extends GenericSetting>> configurationSettings = CacheStatic.getConfigurationSettings();
            for (Class<? extends GenericSetting> configSetting : configurationSettings) {
                for (GenericSetting genericSetting : configSetting.getEnumConstants()) {

                    // Add configuration
                    configurations.put(genericSetting.getName(), genericSetting);
                }
            }

            GenericSetting genericSetting = configurations.get(configurationDTO.getKey());

            if (genericSetting == null) {
                throw new RestServiceException(Response.Status.BAD_REQUEST, "Configuration [KEY] are invalid: "+configurationDTO.getKey());
            }

            try {
                SettingLogic settingLogic = new SettingLogic();
                settingLogic.updateSetting(getCompany(), genericSetting, configurationDTO.getValue());

                response = Response.ok().build();
            }
            catch (Exception e) {
                response = getErrorResponse(LogManager.getLog(getClass()), "update()", e);
            }
        }
        return response;
    }
}
