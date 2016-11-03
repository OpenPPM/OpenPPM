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
 * File: EmployeeDTO.java
 * Create User: javier.hernandez
 * Create Date: 24/09/2015 13:11:44
 */

package es.sm2.openppm.api.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.sm2.openppm.api.model.common.EntityDTO;
import es.sm2.openppm.api.model.management.WeekDTO;
import es.sm2.openppm.utils.functions.ValidateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javier.hernandez on 24/09/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDTO extends EntityDTO {

    private EntityDTO pool;
    private EntityDTO seller;
    private List<WeekDTO> weekList;

    public EmployeeDTO(Integer code, String name) {

        super(code, name);
    }

    public EmployeeDTO() {

    }

    /**
     * Getter for property 'pool'.
     *
     * @return Value for property 'pool'.
     */
    public EntityDTO getPool() {

        return pool;
    }

    /**
     * Setter for property 'pool'.
     *
     * @param pool Value to set for property 'pool'.
     */
    public void setPool(EntityDTO pool) {

        this.pool = pool;
    }

    /**
     * Getter for property 'seller'.
     *
     * @return Value for property 'seller'.
     */
    public EntityDTO getSeller() {

        return seller;
    }

    /**
     * Setter for property 'seller'.
     *
     * @param seller Value to set for property 'seller'.
     */
    public void setSeller(EntityDTO seller) {

        this.seller = seller;
    }

    /**
     * Add Week
     *
     * @param week
     */
    public void addWeek(WeekDTO week) {

        if (ValidateUtil.isNull(weekList)) { weekList = new ArrayList<WeekDTO>(); }
        weekList.add(week);
    }

    /**
     * Getter for property 'weekList'.
     *
     * @return Value for property 'weekList'.
     */
    public List<WeekDTO> getWeekList() {

        return weekList;
    }

    /**
     * Setter for property 'weekList'.
     *
     * @param weekList Value to set for property 'weekList'.
     */
    public void setWeekList(List<WeekDTO> weekList) {

        this.weekList = weekList;
    }


}
