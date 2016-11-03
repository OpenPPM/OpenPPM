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
 * File: Projectkpi.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.impl;

import es.sm2.openppm.core.model.base.BaseProjectkpi;
import es.sm2.openppm.utils.StringPool;


/**
 * Model class Projectkpi.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Projectkpi extends BaseProjectkpi {

	private static final long serialVersionUID = 1L;

    public static final String METRICK_NAME = "metrick";

    private String metrick;

    public Projectkpi() {
		super();
    }
    public Projectkpi(Integer idProjectKpi) {
		super(idProjectKpi);
    }

	/**
	 * Get adjusted value
	 *
	 * @return
	 */
    public Double getAdjustedValue() {
    	
    	Double adjustedValue = null;
    	
    	if (getUpperThreshold() != null && getLowerThreshold() != null && getValue() != null && (getUpperThreshold() - getLowerThreshold()) != 0) {
    		
    		double target		= getUpperThreshold();
    		double threshold 	= getLowerThreshold();
    		double value 		= getValue();
    		
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

	/**
	 * Get score
	 *
	 * @return
	 */
    public Double getScore() {
    	
    	Double score 			= 0.0;
    	Double adjustedValue 	= getAdjustedValue();
    	Double weight 			= this.getWeight();
		
		if (weight != null && adjustedValue != null) {
			score = weight * (adjustedValue/100);
		}

		return score;
	}

	/**
	 * Get RAG
	 *
	 * @return
	 */
	public String getRAG() {

		String rag 				= StringPool.BLANK;
		Double adjustedValue 	= getAdjustedValue();

		if (adjustedValue != null) {

			if (new Double(100).equals(adjustedValue)) {
				rag = "risk_low";
			}
			else if (new Double(0).equals(adjustedValue) && getLowerThreshold() != getValue()) {
				rag = "risk_high";
			}
			else {
				rag = "risk_medium";
			}
		}

		return rag;
	}

	/**
	 * Get project name
	 *
	 * @return
	 */
	public String getProjectName(){
		return getProject().getProjectName();
	}

	/**
	 *	Get metrick name
	 *
	 * @return
	 */
	public String getMetrick() {
		return getMetrickpi() == null ? getSpecificKpi() : getMetrickpi().getName();
	}

}

