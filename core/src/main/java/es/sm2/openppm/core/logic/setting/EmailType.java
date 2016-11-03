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
 * File: EmailType.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.setting;

import es.sm2.openppm.core.common.GenericSetting;
import es.sm2.openppm.core.logic.setting.common.TypeSetting;

/**
 * Created by jordi.ripoll on 05/09/2014.
 */
public enum EmailType implements GenericSetting {

    EMAIL_SIGNATURE("", TypeSetting.INPUT),
    EMAIL_SIGNATURE_TEMPLATE("/es/sm2/openppm/front/templates/mail.ftl", false);

    private String defaultValue;
    private TypeSetting typeSetting;
    private boolean visible;

    private EmailType(String defaultValue, TypeSetting typeSetting) {
        this.defaultValue 	= defaultValue;
        this.typeSetting    = typeSetting;
        this.visible        = true;
    }

    private EmailType(String defaultValue, boolean visible) {
        this.defaultValue 	= defaultValue;
        this.visible        = visible;
    }

    public Enum[] getEnumElements() {
        return null;
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

    @Override
    public boolean isVisible() {
        return visible;
    }

}
