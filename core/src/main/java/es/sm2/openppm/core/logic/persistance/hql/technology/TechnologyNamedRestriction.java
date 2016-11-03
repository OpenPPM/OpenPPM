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
 * File: TechnologyNamedRestriction.java
 * Create User: jordi.ripoll
 * Create Date: 03/07/2015 12:41:11
 */

package es.sm2.openppm.core.logic.persistance.hql.technology;

import es.sm2.openppm.core.logic.persistance.bean.NamedRestriction;

/**
 * Created by jordi.ripoll on 05/11/2014.
 */
public enum TechnologyNamedRestriction implements NamedRestriction {
    EXCLUDE_IN_PROJECT,
    EQ_COMPANY
}
