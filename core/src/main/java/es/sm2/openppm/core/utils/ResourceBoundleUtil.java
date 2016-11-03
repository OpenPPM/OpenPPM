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
 * File: ResourceBoundleUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;

import java.util.ResourceBundle;

/**
 * SM2 Baleares
 * Created by javier.hernandez on 03/07/2014.
 */
public class ResourceBoundleUtil {

    private static ResourceBoundleUtil ourInstance = new ResourceBoundleUtil();

    public static ResourceBoundleUtil getInstance() {
        return ourInstance;
    }

    private ResourceBoundleUtil() {
    }


    /**
     * Get Value
     *
     * @param bundle
     * @param key
     * @param params
     * @return
     */
    public String value(ResourceBundle bundle, String key, Object...params) {

        StringBuilder value = new StringBuilder();

        ParamResourceBundle paramResourceBundle = new ParamResourceBundle(key, params);
		paramResourceBundle.setBlack(false);

        try {
            value.append(paramResourceBundle.toString(bundle));
        }
        catch(Exception e) {

            value.append(key);

            if (ValidateUtil.isNotNull(params)) {

                for (Object param : params) {
                    value.append(StringPool.SPACE).append(param.toString());
                }
            }
        }

        return value.toString();
    }
}
