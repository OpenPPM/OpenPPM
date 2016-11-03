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
 * File: AbstractConverter.java
 * Create User: jordi.ripoll
 * Create Date: 20/08/2015 12:09:46
 */

package es.sm2.openppm.api.converter;

import es.sm2.openppm.core.common.ApplicationSetting;
import es.sm2.openppm.core.utils.EntityUtil;
import es.sm2.openppm.utils.LogManager;
import es.sm2.openppm.utils.functions.ValidateUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by javier.hernandez on 31/07/2015.
 */
public abstract class AbstractConverter<DTO,M> {

    /**
     * Convert BD model object into DTO model
     *
     * @param model
     * @return
     */
    public abstract DTO toDTO(M model);

    /**
     * Conver DTO model into BD model
     *
     * @param dto
     * @return
     */
    public abstract M toModel(DTO dto);


    /**
     * To model list
     *
     * @param dtos
     * @return
     */
    public List<M> toModelList(Collection<DTO> dtos) {

        List<M> models = new ArrayList<M>();

        if (ValidateUtil.isNotNull(dtos)) {

            for (DTO dto : dtos) {
                models.add(toModel(dto));
            }
        }

        return models;
    }

    /**
     * To model list
     *
     * @param dtos
     * @return
     */
    public Set<M> toModelSet(Collection<DTO> dtos) {

        Set<M> models = new HashSet<M>();

        if (ValidateUtil.isNotNull(dtos)) {

            for (DTO dto : dtos) {
                models.add(toModel(dto));
            }
        }

        return models;
    }

    /**
     * To DTO list
     *
     * @param models
     * @return
     */
    public List<DTO> toDTOList(Collection<M> models) {

        List<DTO> dtos = new ArrayList<DTO>();

        if (ValidateUtil.isNotNull(models)) {

            for (M m : models) {
                dtos.add(toDTO(m));
            }
        }

        return dtos;
    }

    /**
     * Create copy of model BD instance to DTO
     *
     * @param model
     * @param dtoClass
     * @return
     */
    public DTO getInstanceDTO(M model, Class<DTO> dtoClass) {

        DTO modelDTO = null;

        EntityUtil entityUtil = new EntityUtil();

        try {

            modelDTO = entityUtil.copyEntity(model, dtoClass.getConstructor().newInstance());

        } catch (InstantiationException e) {
            LogManager.getLog(this.getClass()).warn(e);
        } catch (IllegalAccessException e) {
            LogManager.getLog(this.getClass()).warn(e);
        } catch (InvocationTargetException e) {
            LogManager.getLog(this.getClass()).warn(e);
        } catch (NoSuchMethodException e) {
            LogManager.getLog(this.getClass()).warn(e);
        }

        return modelDTO;
    }

    /**
     * Create copy of DTO instance to model BD
     *
     * @param modelDTO
     * @param modelBDClass
     * @return
     */
    public M getInstanceModel(DTO modelDTO, Class<M> modelBDClass) {

        M model = null;

        EntityUtil entityUtil = new EntityUtil();

        try {

            model = entityUtil.copyEntity(modelDTO, modelBDClass.getConstructor().newInstance());

        } catch (InstantiationException e) {
            LogManager.getLog(this.getClass()).warn(e);
        } catch (IllegalAccessException e) {
            LogManager.getLog(this.getClass()).warn(e);
        } catch (InvocationTargetException e) {
            LogManager.getLog(this.getClass()).warn(e);
        } catch (NoSuchMethodException e) {
            LogManager.getLog(this.getClass()).warn(e);
        }

        return model;
    }


}
