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
 * File: EntityComparator.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:58
 */

package es.sm2.openppm.core.utils;

import java.util.Comparator;

public class EntityComparator<T> implements Comparator<T> {

    private Order order;
    
    public EntityComparator(Order order) {
        this.order = order;
    }

	public int compare(T object1, T object2) {
        try {
        	
            Object value1 = DataUtil.getFieldValue(object1, this.order.getProperty(), null);
            Object value2 = DataUtil.getFieldValue(object2, this.order.getProperty(), null);

            return DataUtil.sortFunction(value1, value2, order);
        }
        catch(Exception e) {
        	e.printStackTrace();
            throw new RuntimeException();
        }
    }
}