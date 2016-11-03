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
 * File: Program.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.impl;

import org.apache.log4j.Logger;

import es.sm2.openppm.core.logic.impl.ProgramLogic;
import es.sm2.openppm.core.logic.impl.ProjectLogic;
import es.sm2.openppm.core.model.base.BaseProgram;



/**
 * Model class Program.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Program extends BaseProgram {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Project.class);
	
	public Program() {
		super();
    }
    
    public Program(Integer idProgram) {
		super(idProgram);
    }
    
    /**
     * 
     * @return
     */
    public Double getBudgetBottomUp () {
    	
    	Double budget = null;
    	
		try {
			ProjectLogic projectLogic = new ProjectLogic(null, null);
			
			budget = projectLogic.calcBudgetBottomUp(this);
		}
    	catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
		}
		return budget;
    }
    
    /**
     * 
     * @return
     */
    public Double getSumActualCost() {
    	
    	Double cost = null;
    	
		try {
			ProgramLogic programLogic = new ProgramLogic();
			
			cost = programLogic.calcActualCost(this);
		}
    	catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
		}
		return cost;
    }
    
    /**
     * Calculated the prioritization of the program according to the associated projects
     */
    public Integer getPriorization() {
    	
    	Integer priority = 0;
    	
    	try {
			
			ProjectLogic projectLogic = new ProjectLogic(null, null);
			
			priority = projectLogic.sumPriorityByProgram(this);
			
		}
    	catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
		}
    	
		return priority;
    }
    
}

