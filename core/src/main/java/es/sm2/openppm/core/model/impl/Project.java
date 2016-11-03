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
 * File: Project.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import es.sm2.openppm.utils.functions.ValidateUtil;
import org.apache.log4j.Logger;

import es.sm2.openppm.core.logic.impl.MilestoneLogic;
import es.sm2.openppm.core.logic.impl.ProjectActivityLogic;
import es.sm2.openppm.core.logic.impl.ProjectFollowupLogic;
import es.sm2.openppm.core.model.base.BaseProject;



/**
 * Model class Project.
 * @author Hibernate Generator by Javier Hernandez
 */
@XmlRootElement
public class Project extends BaseProject {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Project.class);

    public Project() {
		super();
    }
    public Project(Integer idProject) {
		super(idProject);
    }

    public Project(String projectName) {
    	setProjectName(projectName);
    }
    
    /**
     * Cons Root Activity (Do Not use in any logic class)
     * @return
     * @throws Exception
     */
    public Projectactivity getRootActivity() throws Exception {
    	
    	Projectactivity root = null;
    	try {
    		ProjectActivityLogic projectActivityLogic = new ProjectActivityLogic(null, null);
    		
    		root = projectActivityLogic.consRootActivity(this);
        	
    		if (root == null) {
    			root = new Projectactivity();
    			StringBuffer msg = new StringBuffer();
    			
    				msg.append("Error\n");
    				
    				Integer idProject = this.getIdProject();
    				msg.append("\nidProject: "+idProject);
    				msg.append("\nProject: "+this.getProjectName());
    			
    			msg.append("\n");
    			LOGGER.error(msg.toString(), new Exception("No hay actividad principal"));
    		}
    	}
    	catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
		}
    	
    	return root;
    }
    
    public Projectfollowup getLastFollowup() throws Exception {
    	ProjectFollowupLogic projectFollowupLogic = new ProjectFollowupLogic();
    	
    	return projectFollowupLogic.findLastByProject(this);
    }
    
    public Projectfollowup getLastWithDataFollowup() throws Exception {
    	ProjectFollowupLogic projectFollowupLogic = new ProjectFollowupLogic();
    	
    	return projectFollowupLogic.findLastWithDataByProject(this);
    }
	
	public List<Milestones>  getMilestones() {
		List<Milestones> milestones	= null;
		
		try {
			MilestoneLogic milestoneLogic = new MilestoneLogic();
			
			milestones = milestoneLogic.consMilestones(this.getIdProject());
		}
    	catch (Exception e) {
    		LOGGER.error(e.getMessage(), e);
		}
		
		return milestones;
	}

    /**
     * Get one project charter
     *
     * @return
     */
    public Projectcharter getProjectCharter() {

        Projectcharter projectcharter = new Projectcharter();

        if (ValidateUtil.isNotNull(this.getProjectcharters())) {

            // Only one element in set
            for (Projectcharter item : this.getProjectcharters()) {

                projectcharter = item;
            }
        }

        return projectcharter;
    }

    /**
     * Get one project closure
     *
     * @return
     */
    public Projectclosure getProjectclosure() {

        Projectclosure projectclosure = new Projectclosure();

        if (ValidateUtil.isNotNull(this.getProjectclosures())) {

            // Only one element in set
            for (Projectclosure item : this.getProjectclosures()) {

                projectclosure = item;
            }
        }

        return projectclosure;
    }
	
}

