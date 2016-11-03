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
 * File: FundingSourceDTOConverter.java
 * Create User: mariano.fontana
 * Create Date: 02/09/2015 09:25:08
 */

package es.sm2.openppm.api.converter.maintenance;

import es.sm2.openppm.api.converter.AbstractConverter;
import es.sm2.openppm.api.model.maintenance.FundingSourceDTO;
import es.sm2.openppm.core.model.impl.Fundingsource;

/**
 * Created by mariano.fontana on 02/09/2015.
 */
public class FundingSourceDTOConverter extends AbstractConverter<FundingSourceDTO, Fundingsource> {
    @Override
    public FundingSourceDTO toDTO(Fundingsource model) {

        FundingSourceDTO fundingSourceDTO = getInstanceDTO(model, FundingSourceDTO.class);

        //Set ID
        fundingSourceDTO.setCode(model.getIdFundingSource());

        return fundingSourceDTO;
    }

    @Override
    public Fundingsource toModel(FundingSourceDTO modelDTO) {

        Fundingsource fundingsource = getInstanceModel(modelDTO, Fundingsource.class);

        //Set ID
        fundingsource.setIdFundingSource(modelDTO.getCode());

        return fundingsource;
    }
}
