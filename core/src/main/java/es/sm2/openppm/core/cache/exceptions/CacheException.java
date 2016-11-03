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
 * File: CacheException.java
 * Create User: javier.hernandez
 * Create Date: 21/03/2015 11:50:24
 */

package es.sm2.openppm.core.cache.exceptions;

import es.sm2.openppm.core.exceptions.CoreException;

/**
 * Created by javier.hernandez on 21/03/2015.
 */
public class CacheException extends CoreException {

    public CacheException(String msg) {

        super(msg);
    }

    public CacheException(String msg, String... param) {

        super(msg, param);
    }

    public CacheException(Throwable cause) {

        super(cause);
    }
}
