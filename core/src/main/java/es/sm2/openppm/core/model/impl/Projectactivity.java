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
 * File: Projectactivity.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.logic.impl.ActivitysellerLogic;
import es.sm2.openppm.core.logic.impl.ProjectActivityLogic;
import es.sm2.openppm.core.model.base.BaseProjectactivity;



/**
 * Model class Projectactivity.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Projectactivity extends BaseProjectactivity {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Projectactivity.class);
	
    public Projectactivity() {
		super();
    }
    public Projectactivity(Integer idActivity) {
		super(idActivity);
    }

    public Double getPocCalc() throws Exception {
    	
    	Double poc = null;
    	try {
    		ProjectActivityLogic projectActivityLogic = new ProjectActivityLogic(null, null);
    		
    		poc = projectActivityLogic.calcPoc(this);
    	}
    	catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
		}
    	return poc;
    }
    
    /**
     * Activity has associated sellers
     * @return
     */
    public boolean getHasSellers() {
    	boolean result = false;
    	
    	try {
    		ActivitysellerLogic activitysellerLogic = new ActivitysellerLogic();
    		
    		result = activitysellerLogic.hasActivity(this);
    	}
    	catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
		}
		
    	
		return result;
    }
    
    /**
     * Last end date
     * 
     * @return
     */
    public Date getLastDate() {
    	
    	Date lastDate = null;
    	
    	if (this.getPlanEndDate() != null && this.getActualEndDate() != null
    			&& this.getActualEndDate().after(this.getPlanEndDate())) {
    		
    		lastDate = this.getActualEndDate();
    	}
    	else {
    		lastDate = this.getPlanEndDate();
    	}
    	
    	return lastDate;
    }
    
    /**
     * First init date
     * 
     * @return
     */
    public Date getFirstDate() {
    	
    	Date first = null;
    	
    	if (this.getPlanInitDate() != null && this.getActualInitDate() != null
    			&& this.getActualInitDate().before(this.getPlanInitDate())) {
    		
    		first = this.getActualInitDate();
    	}
    	else {
    		first = this.getPlanInitDate();
    	}
    	
    	return first;
    }
}

