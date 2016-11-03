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
 * File: CustomizationUtil.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:47:22
 */

package es.sm2.openppm.front.utils;

import es.sm2.openppm.core.common.Settings.SettingType;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * SM2 Baleares
 * Created by javier.hernandez on 07/07/2014.
 */
public class CustomizationUtil {

    private static final Logger LOGGER = Logger.getLogger(CustomizationUtil.class);

    private static CustomizationUtil ourInstance = new CustomizationUtil();

    public static CustomizationUtil getInstance() {
        return ourInstance;
    }

    private CustomizationUtil() {
    }


    /**
     * Generate color by levels in Resource Capacity Planning
     *
     * @param settings
     * @param fteCalculated
     * @return
     */
    public String generateColorCP(HashMap<String, String> settings, Object fteCalculated) {

        String style = "";

        if (fteCalculated != null) {

            try {

                Integer level1 = SettingUtil.getInteger(settings, SettingType.CAPACITY_PLANNING_LEVEL1);
                Integer level2 = SettingUtil.getInteger(settings, SettingType.CAPACITY_PLANNING_LEVEL2);
                Integer level3 = SettingUtil.getInteger(settings, SettingType.CAPACITY_PLANNING_LEVEL3);

                // FTE for calculate color
                Integer fte = Integer.valueOf(fteCalculated.toString());

                if (fte > 0 && fte <= level1) {

                    style = "background-color:"+SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL1_COLOR);
                }
                else if (fte >= level1 && fte <= level2) {

                    style = "background-color:"+SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL2_COLOR);
                }
                else if (fte >= level2 && fte <= level3) {

                    style = "background-color:"+SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL3_COLOR);
                }
                else if (fte >= level3) {
                	
                	style = "background-color:"+SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_OUTOFRANGE_COLOR);
                }
                
            }
            catch(Exception e) {
                LOGGER.warn("Calculating color for show in Resource Capacity Planning",e);
            }

        }
        return style;
    }
    
    /**
     * Generate color by levels in Resource Capacity Planning
     *
     * @param settings
     * @param fteCalculated
     * @return
     */
    public String generateFontColorCP(HashMap<String, String> settings, Object fteCalculated) {

        String style = "";

        if (fteCalculated != null) {

            try {

                Integer level1 = SettingUtil.getInteger(settings, SettingType.CAPACITY_PLANNING_LEVEL1);
                Integer level2 = SettingUtil.getInteger(settings, SettingType.CAPACITY_PLANNING_LEVEL2);
                Integer level3 = SettingUtil.getInteger(settings, SettingType.CAPACITY_PLANNING_LEVEL3);

                // FTE for calculate color
                Integer fte = Integer.valueOf(fteCalculated.toString());

                if (fte > 0 && fte <= level1) {

                    style = "color:"+SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL1_COLOR_TEXT);
                }
                else if (fte >= level1 && fte <= level2) {

                    style = "color:"+SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL2_COLOR_TEXT);
                }
                else if (fte >= level2 && fte <= level3) {
                	
                    style = "color:"+SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_LEVEL3_COLOR_TEXT);
                }
            	else if (fte >= level3) {
            		
            		style = "color:"+SettingUtil.getString(settings, SettingType.CAPACITY_PLANNING_OUTOFRANGE_TEXT);
                }
                
            }
            catch(Exception e) {
                LOGGER.warn("Calculating font color for show in Resource Capacity Planning",e);
            }

        }
        return style;
    }
}
