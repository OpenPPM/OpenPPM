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
 * File: SecuritySetting.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.setting;

import es.sm2.openppm.core.common.GenericSetting;
import es.sm2.openppm.core.common.enums.SecurityProtocolEnum;
import es.sm2.openppm.core.logic.setting.common.TypeSetting;

/**
 * SM2 Baleares
 * Created by javier.hernandez on 09/07/2014.
 */
public enum SecuritySetting implements GenericSetting {

    PROTOCOL(SecurityProtocolEnum.LDAP, SecurityProtocolEnum.class);

    private String defaultValue;
    private TypeSetting typeSetting;
    private Class<? extends Enum> enumElements;

    private SecuritySetting(String defaultValue) {

        this.defaultValue = defaultValue;
    }

    private <E extends Enum> SecuritySetting(E defaultValue, Class<E> enumElements) {

        this(defaultValue.name());
        this.enumElements = enumElements;
        this.typeSetting = TypeSetting.SELECT;

    }

    public Enum[] getEnumElements() {
        return enumElements.getEnumConstants();
    }

    public TypeSetting getTypeSetting() {
        return this.typeSetting == null?TypeSetting.CHECKBOX: this.typeSetting;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name();
    }

    public boolean isVisible() {
        return true;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return the messageInfo
     */
    public String getMessageInfo() {
        return "SETTING."+this.name()+"_INFO";
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return "SETTING."+this.name();
    }
}
