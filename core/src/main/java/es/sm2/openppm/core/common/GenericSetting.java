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
 * File: GenericSetting.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.common;

import es.sm2.openppm.core.logic.setting.common.TypeSetting;

@Deprecated
public interface GenericSetting {

    // TODO javier.hernandez - 16/09/2015 - crear tag que indique si es editable y si se envia por api

	public String getName();
	public String getDefaultValue();
	public String getMessage();
	public String getMessageInfo();
    public TypeSetting getTypeSetting();
    public boolean isVisible();
	public Enum[] getEnumElements();
}