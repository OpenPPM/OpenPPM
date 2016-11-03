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
 * File: Metrickpi.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.model.base.BaseMetrickpi;
import es.sm2.openppm.utils.functions.ValidateUtil;


/**
 * Model class Metrickpi.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Metrickpi extends BaseMetrickpi {

	private static final long serialVersionUID = 1L;

    public Metrickpi() {
		super();
    }
    public Metrickpi(int idMetricKpi) {
		super(idMetricKpi);
    }
    public Metrickpi(String name) {
		setName(name);
    }

    /**
     * Type KPI
     */
    public enum TypeKPI {
        CPI("CPI"),
        SPI("SPI"),
        SPI_T("SPI(t)"),
        CV("CV"),
        SV("SV"),
        SV_T("SV(t)"),
        EAC("EAC");

        private String label;

        private TypeKPI(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public String getTypeKPI() {

        String type = this.getType();

        if (ValidateUtil.isNotNull(type)) {
            type = TypeKPI.valueOf(this.getType()).getLabel();
        }

        return type;
    }
}

