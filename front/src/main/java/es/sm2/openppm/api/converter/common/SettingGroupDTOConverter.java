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
 * File: ConfigurationDTOConverter.java
 * Create User: jordi.ripoll
 * Create Date: 20/08/2015 13:47:17
 */

package es.sm2.openppm.api.converter.common;

import com.google.common.base.CaseFormat;
import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.common.SettingGroupDTO;
import es.sm2.openppm.core.cache.CacheStatic;
import es.sm2.openppm.core.common.ApplicationSetting;
import es.sm2.openppm.utils.functions.ValidateUtil;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by jordi.ripoll on 03/08/2015.
 */
public class SettingGroupDTOConverter extends AbstractConverter<SettingGroupDTO, Class<? extends ApplicationSetting>> {


    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    @Override
    public SettingGroupDTO toDTO(Class<? extends ApplicationSetting> model) {

        String name = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, model.getSimpleName());

        SettingGroupDTO dto = new SettingGroupDTO(name);
//        dto.setSettingList(new HashMap<String, String>());
//
//        for (ApplicationSetting applicationSetting : model.getEnumConstants()) {
//            dto.getSettingList().put(applicationSetting.name(), applicationSetting.getDefaultValue());
//        }

        return dto;
    }

    /**
     * Conver DTO model into BD model
     *
     * @param modelDTO
     * @return
     */
    @Override
    public Class<? extends ApplicationSetting> toModel(SettingGroupDTO modelDTO) {

        Class<? extends ApplicationSetting> applicationSetting = null;

        if (modelDTO != null && ValidateUtil.isNotNull(modelDTO.getName())) {

            String name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, modelDTO.getName());

            Set<Class<? extends ApplicationSetting>> applicationSettingList = CacheStatic.getApplicationSettingList();

            Iterator<Class<? extends ApplicationSetting>> iterator = applicationSettingList.iterator();
            while (applicationSetting == null && iterator.hasNext()) {
                Class<? extends ApplicationSetting> setting = iterator.next();

                if (setting.getSimpleName().equals(name)) {
                    applicationSetting = setting;
                }
            }
        }

        return applicationSetting;
    }



}
