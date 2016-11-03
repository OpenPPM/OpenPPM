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
 * File: ImportanceEnum.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.model.enums;

/**
 * Created by jordi.ripoll on 29/10/2014.
 */
public enum ImportanceEnum {
    NORMAL("normal_importance", 2, "imp.normal_importance"),
    HIGH("medium_importance", 1, "imp.high_importance"),
    VERY_HIGH("high_importance", 0, "imp.very_high_importance");

    private String classHtml;
    private Integer order;
    private String label;

    private ImportanceEnum(String classHtml, Integer order, String label) {
        this.classHtml  = classHtml;
        this.order      = order;
        this.label      = label;
    }

    public Integer getOrder() {
        return order;
    }

    public String getLabel() {
        return label;
    }

    public String getClassHtml() {
        return classHtml;
    }
}
