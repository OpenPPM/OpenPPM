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
 * File: ExecutivereportLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.ExecutivereportDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.dao.ProjectFollowupDAO;
import es.sm2.openppm.core.model.impl.Executivereport;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.utils.DateUtil;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import es.sm2.openppm.utils.javabean.CsvFile;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.ResourceBundle;

//import javax.servlet.http.HttpServletRequest;



/**
 * Logic object for domain model class Executivereport
 * @see es.sm2.openppm.core.logic.impl.ExecutivereportLogic
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class ExecutivereportLogic extends AbstractGenericLogic<Executivereport, Integer> {

	public ExecutivereportLogic() {
		super();
	}
	
	public ExecutivereportLogic(ResourceBundle bundle) {
		super(bundle);
	}

	/**
	 * Get executive report by project
	 * @param proj
	 * @return
	 * @throws Exception
	 */
	public Executivereport findByProject(Project proj) throws Exception {
		
		Executivereport executivereport = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ExecutivereportDAO executivereportDAO = new ExecutivereportDAO(session);
			executivereport = executivereportDAO.findByProject(proj);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return executivereport;
	}
	
	/**
	 * Export Status Repor to CSV
	 * 
	 * @param ids
	 * @param showExecutiveReport
	 * @return
	 * @throws Exception
	 */
	public CsvFile exportSRToCSV (Integer[] ids, Boolean showExecutiveReport) throws Exception{
		
		CsvFile file = null;
		Transaction tx = null;
		
		// Initiate session
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		try {			
			tx = session.beginTransaction();
			file = new CsvFile(Constants.SEPARATOR_CSV);

			// Init DAOs
			ProjectDAO projDAO = new ProjectDAO(session);
			ProjectFollowupDAO followupDAO = new ProjectFollowupDAO(session);
			Projectfollowup followup = null;
			ExecutivereportDAO executivereportDAO = new ExecutivereportDAO(session);
			Executivereport executivereport = null;
			
			// Set header
			file.addValue(getBundle().getString("project_name"));
			file.addValue(getBundle().getString("date"));
			file.addValue(getBundle().getString("status"));
			file.addValue(getBundle().getString("type"));
			file.addValue(getBundle().getString("desc"));
			file.newLine();
			file.newLine();

			// Get projects
			List<Project> projects = projDAO.consList(ids, null, null, null);			
			
			for (Project project : projects) {
				
				// Get followup for the project
				followup = followupDAO.findLastWithDataByProject(project);
				
				// Get executive report for the project if the configuration allows it
				if (showExecutiveReport){
					
					executivereport = executivereportDAO.findByProject(project);
				}

				// Fill the csv with the data
				if (followup != null && executivereport == null) {
					
					file.addValue(StringPool.BLANK);
					file.addValue(StringPool.BLANK);
					file.addValue((followup.getGeneralFlag()== null ? StringPool.BLANK : getBundle().getString("followup.status_" + followup.getGeneralFlag().toString())));
					file.addValue(getBundle().getString("general"));
					file.addValue((followup.getGeneralComments()== null ? StringPool.BLANK : followup.getGeneralComments().replaceAll("\\s", StringPool.SPACE)));
					file.newLine();
					
					file.addValue(project.getProjectName());
					file.addValue(followup.getFollowupDate() == null?StringPool.BLANK : DateUtil.format(getBundle(), followup.getFollowupDate()));
					file.addValue((followup.getRiskFlag()== null ? StringPool.BLANK : getBundle().getString("followup.status_" + followup.getRiskFlag().toString())));
					file.addValue(getBundle().getString("risk"));
					file.addValue((followup.getRisksComments()== null ? StringPool.BLANK : followup.getRisksComments().replaceAll("\\s", StringPool.SPACE)));
					file.newLine();
					
					file.addValue(StringPool.BLANK);
					file.addValue(StringPool.BLANK);
					file.addValue((followup.getScheduleFlag()== null ? StringPool.BLANK : getBundle().getString("followup.status_" + followup.getScheduleFlag().toString())));
					file.addValue(getBundle().getString("schedule"));
					file.addValue((followup.getScheduleComments()== null ? StringPool.BLANK : followup.getScheduleComments().replaceAll("\\s", StringPool.SPACE)));
					file.newLine();
					
					file.addValue(StringPool.BLANK);
					file.addValue(StringPool.BLANK);
					file.addValue((followup.getCostFlag()== null ? StringPool.BLANK : getBundle().getString("followup.status_" + followup.getCostFlag().toString())));
					file.addValue(getBundle().getString("cost"));
					file.addValue((followup.getCostComments()== null ? StringPool.BLANK : followup.getCostComments().replaceAll("\\s", StringPool.SPACE)));
					file.newLine();
					file.newLine();
				}
				else if (followup != null && executivereport != null) {
					
					file.addValue(StringPool.BLANK);
					file.addValue(executivereport.getStatusDate() == null ? StringPool.BLANK : DateUtil.format(getBundle(), executivereport.getStatusDate()));
					file.addValue(getBundle().getString("project_status."+project.getStatus()));
					file.addValue(getBundle().getString("executive_report.internal"));
					file.addValue(executivereport.getInternal() == null ? StringPool.BLANK : executivereport.getInternal().replaceAll("\\s", StringPool.SPACE));
					file.newLine();
					
					file.addValue(StringPool.BLANK);
					file.addValue(StringPool.BLANK);
					file.addValue(StringPool.BLANK);
					file.addValue(getBundle().getString("executive_report.external"));
					file.addValue(executivereport.getExternal() == null ? StringPool.BLANK : executivereport.getExternal().replaceAll("\\s", StringPool.SPACE));
					file.newLine();
					
					file.addValue(project.getProjectName());
					file.addValue(StringPool.BLANK);
					file.addValue((followup.getGeneralFlag()== null ? StringPool.BLANK : getBundle().getString("followup.status_" + followup.getGeneralFlag().toString())));
					file.addValue(getBundle().getString("general"));
					file.addValue((followup.getGeneralComments()== null ? StringPool.BLANK : followup.getGeneralComments().replaceAll("\\s", StringPool.SPACE)));
					file.newLine();
					
					file.addValue(StringPool.BLANK);
					file.addValue(StringPool.BLANK);
					file.addValue((followup.getRiskFlag()== null ? StringPool.BLANK : getBundle().getString("followup.status_" + followup.getRiskFlag().toString())));
					file.addValue(getBundle().getString("risk"));
					file.addValue((followup.getRisksComments()== null ? StringPool.BLANK : followup.getRisksComments().replaceAll("\\s", StringPool.SPACE)));
					file.newLine();
					
					file.addValue(StringPool.BLANK);
					file.addValue(followup.getFollowupDate() == null?StringPool.BLANK : DateUtil.format(getBundle(), followup.getFollowupDate()));
					file.addValue((followup.getScheduleFlag()== null ? StringPool.BLANK : getBundle().getString("followup.status_" + followup.getScheduleFlag().toString())));
					file.addValue(getBundle().getString("schedule"));
					file.addValue((followup.getScheduleComments()== null ? StringPool.BLANK : followup.getScheduleComments().replaceAll("\\s", StringPool.SPACE)));
					file.newLine();
					
					file.addValue(StringPool.BLANK);
					file.addValue(StringPool.BLANK);
					file.addValue((followup.getCostFlag()== null ? StringPool.BLANK : getBundle().getString("followup.status_" + followup.getCostFlag().toString())));
					file.addValue(getBundle().getString("cost"));
					file.addValue((followup.getCostComments()== null ? StringPool.BLANK : followup.getCostComments().replaceAll("\\s", StringPool.SPACE)));
					file.newLine();
					file.newLine();
				}
				else if (followup == null && executivereport != null) {
					
					file.addValue(project.getProjectName());
					file.addValue(executivereport.getStatusDate() == null ? StringPool.BLANK : DateUtil.format(getBundle(), executivereport.getStatusDate()));
					file.addValue(getBundle().getString("project_status."+project.getStatus()));
					file.addValue(getBundle().getString("executive_report.internal"));
					file.addValue(executivereport.getInternal() == null ? StringPool.BLANK : executivereport.getInternal().replaceAll("\\s", StringPool.SPACE));
					file.newLine();
					
					file.addValue(StringPool.BLANK);
					file.addValue(StringPool.BLANK);
					file.addValue(StringPool.BLANK);
					file.addValue(getBundle().getString("executive_report.external"));
					file.addValue(executivereport.getExternal() == null ? StringPool.BLANK : executivereport.getExternal().replaceAll("\\s", StringPool.SPACE));
					file.newLine();
					file.newLine();
				}	
			}
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return file;
	}
}

