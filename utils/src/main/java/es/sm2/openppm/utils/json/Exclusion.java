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
 * Module: utils
 * File: Exclusion.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class Exclusion implements ExclusionStrategy {
	
	private final Class<?> typeToSkip;
	private final List<String> nameExlusion;
	
	public Exclusion(Class<?> typeToSkip) {
		this.typeToSkip = typeToSkip;
		this.nameExlusion = new ArrayList<String>();
	}
	
	public Exclusion(Class<?> typeToSkip, List<String> nameExlusion) {
		
		this.nameExlusion = nameExlusion;
		this.typeToSkip = typeToSkip;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
    
    	boolean exclusion = false;
		
		if (nameExlusion.isEmpty()) {
			exclusion = typeToSkip.equals(clazz);
		}
		return exclusion;
    }

	public boolean shouldSkipField(FieldAttributes f) {
		
		boolean exclusion = false;
		
		if (nameExlusion.isEmpty()) {
			exclusion = typeToSkip.equals(f.getDeclaredClass());
		}
		else {
			exclusion = (typeToSkip.equals(f.getDeclaredClass()) && nameExlusion.contains(f.getName()));
		}
		return exclusion;
	}  
}