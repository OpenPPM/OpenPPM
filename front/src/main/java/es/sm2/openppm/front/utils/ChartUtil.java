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
 * Module: front
 * File: ChartUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;


public class ChartUtil {

	private ChartUtil() {}

	
	public static List<String> createSeriesPL(String value1, String value2, String Value3, int type) {
		
		List<String> listValues = new ArrayList<String>();
		
		switch (type) {
			case 1: {
				listValues.add(value1);
				listValues.add("0");
				listValues.add("0");
				listValues.add(value2);
				listValues.add("0");
				listValues.add("0");
				break;
			}
			case 2: {
				listValues.add("0");
				listValues.add(value1);
				listValues.add(value2);
				listValues.add("0");
				listValues.add(Value3);
				listValues.add("0");
				break;
			}
			case 3: {
				for (int i = 0; i <= 5; i++) { 
					if (i == 4) { listValues.add(value1); }
					else { listValues.add("0"); } 
				}
				break;
			}
			case 4: {
				for (int i = 0; i <= 5; i++) { 
					if (i == 1) { listValues.add(value1); }
					else { listValues.add("0"); } 
				}
				break;
			}
			case 5: {
				for (int i = 0; i <= 5; i++) { 
					if (i == 2) { listValues.add(value1); }
					else { listValues.add("0"); } 
				}
				break;
			}
			case 6: {
				for (int i = 0; i < 5; i++) { listValues.add("0"); }
				listValues.add(value1);
				break;
			}
			default: break;
		}
		return listValues;
	}
	
	public static JSONObject getLegend(String name, int type) {
		 
		JSONObject legendJSON = new JSONObject();
		legendJSON.put("name", name);
		
		if (type == 0) { legendJSON.put("color","#EFDF00"); }
		else {
			
			switch(type) {
				case 1 : legendJSON.put("color","#36CDDD"); break;
				case 2 : legendJSON.put("color","#6793ED"); break;
				case 3 : legendJSON.put("color","#634BF9"); break;
				case 4 : legendJSON.put("color","#A192FE"); break;
				case 5 : legendJSON.put("color","#5EB1FA"); break;
				case 6 : legendJSON.put("color","#A3D1F9"); break;
				case 7 : legendJSON.put("color","#1A92FB"); break;
				default: legendJSON.put("color","#999999"); break;
			}
		}
		
		return legendJSON;
	}
}