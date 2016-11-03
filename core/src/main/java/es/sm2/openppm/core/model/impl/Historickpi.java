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
 * File: Historickpi.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.model.base.BaseHistorickpi;
import es.sm2.openppm.utils.LogManager;


/**
 * Model class Historickpi.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Historickpi extends BaseHistorickpi {

	private static final long serialVersionUID = 1L;

    public Historickpi() {
		super();
    }
    public Historickpi(Integer idHistoricKpi) {
		super(idHistoricKpi);
    }

    public enum UpdatedType {
        MANUAL("updatedtype.manual"),
        AUTOMATIC_EVM("updatedtype.automatic_evm");

        private String label;

        private UpdatedType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    /**
     * Get update type
     *
     * @return
     */
    public UpdatedType getEnumUpdatedType() {

        UpdatedType updatedType = null;

        try {
            updatedType = UpdatedType.valueOf(this.getUpdatedType());
        }
        catch (Exception e) {
            LogManager.getLog(getClass()).warn("Updated type is not defined: "+this.getUpdatedType());
        }

        return updatedType;
    }

    /**
     * Adjusted value
     * 
     * @return
     */
    public Double getAdjustedValue() {
    	
    	Double adjustedValue = null;
    	
    	if (getUpperThreshold() != null && getLowerThreshold() != null && getValueKpi() != null && (getUpperThreshold() - getLowerThreshold()) != 0) {
    		
    		double target		= getUpperThreshold();
    		double threshold 	= getLowerThreshold();
    		double value 		= getValueKpi();
    		
    		double calculated 	= ((value - threshold)/(target - threshold)) == -0.0 ? 0.0 : ((value - threshold)/(target - threshold));
    		
    		if (calculated < 0.0) {
    			adjustedValue = 0.0;
    		}
    		else if (calculated > 1.0) {
    			adjustedValue = 100.0;
    		}
    		else {
    			adjustedValue = calculated*100;
    		}
    	}
    	
    	return  adjustedValue;
    }
    
}

