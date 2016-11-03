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
 * File: ProgramLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.common.Settings;
import es.sm2.openppm.core.dao.CompanyDAO;
import es.sm2.openppm.core.dao.ProgramDAO;
import es.sm2.openppm.core.dao.ProjectDAO;
import es.sm2.openppm.core.dao.ProjectFollowupDAO;
import es.sm2.openppm.core.dao.ProjectKpiDAO;
import es.sm2.openppm.core.dao.SettingDAO;
import es.sm2.openppm.core.exceptions.ProgramNotFoundException;
import es.sm2.openppm.core.javabean.ProjectScore;
import es.sm2.openppm.core.javabean.PropertyRelation;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.model.impl.Setting;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class ProgramLogic extends AbstractGenericLogic<Program, Integer> {

	/**
	 * Find Program by Id
	 * @param idProgram
	 * @return
	 * @throws Exception 
	 */
	public Program consProgram(Integer idProgram) throws Exception {
		Program program = null;
		
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ProgramDAO programDAO = new ProgramDAO(session);
			if (idProgram != -1) {
				program = programDAO.findById(idProgram, false);
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
		
		return program;
	}
	
	
	/**
	 * Returns list Programs
	 * @return
	 * @throws Exception 
	 */
	public List<Program> findAllWithManager() throws Exception {
		List<Program> programs = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {	
			tx = session.beginTransaction();
			
			ProgramDAO programDAO = new ProgramDAO(session);
			programs = programDAO.searchByExample(new Program(), Contact.class);
			
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
		
		return programs;
	}
	
	/**
	 * Delete Program
	 * @param idCompany
	 * @throws Exception 
	 */
	public void deleteProgram(Integer idProgram) throws Exception {
		if (idProgram == null || idProgram == -1) {
			throw new ProgramNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			ProgramDAO programDAO	= new ProgramDAO(session);
			Program program			= programDAO.findById(idProgram, false);
			
			if (program != null) {
				
				if (!program.getProjects().isEmpty()) {
					throw new LogicException("msg.error.delete.this_has","program","project");
				}
				
				programDAO.makeTransient(program);
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
	}
	
	
	public List<Program> consByProgramManager(Employee employee) throws Exception {
		
		List<Program> programs = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {	
			tx = session.beginTransaction();
			
			ProgramDAO programDAO = new ProgramDAO(session);
			Program example = new Program();
			example.setEmployee(employee);
			
			programs = programDAO.searchByExample(example, Contact.class);
			
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
		
		return programs;
	}
	
	public List<Program> consByPO(Employee user) throws Exception {
		
		List<Program> programs = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {	
			tx = session.beginTransaction();
			
			ProgramDAO programDAO = new ProgramDAO(session);
			programs = programDAO.findByPO(user);
			
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
		
		return programs;
	}

	public List<ProjectScore> consProjectScores(List<Project> projects) throws Exception {
		
		List<ProjectScore> projectScores = new ArrayList<ProjectScore>();
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {	
			tx = session.beginTransaction();
			
			ProjectKpiDAO kpiDAO = new ProjectKpiDAO(session);
			
			for (Project proj : projects) {
				Double score = kpiDAO.calcScore(proj);
				
				ProjectScore pScore = new ProjectScore();
				pScore.setProject(proj);
				pScore.setScore(score);
				
				projectScores.add(pScore);
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
		
		return projectScores;
	}

	/**
	 * Search by PerfOrg
	 * @param performingorg
	 * @return
	 * @throws Exception
	 */
	public List<Program> searchByPerfOrg(Performingorg performingorg) throws Exception {
		
		List<Program> programs = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {	
			tx = session.beginTransaction();
			
			ProgramDAO programDAO = new ProgramDAO(session);
			programs = programDAO.searchByPerfOrg(performingorg, null);
			
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
		return programs;
	}

	/**
	 * Search by company of user
	 * @param user
	 * @param joins
	 * @return
	 * @throws Exception 
	 */
	public List<Program> consByCompany(Employee user, List<String> joins) throws Exception {
		
		List<Program> programs = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		try {	
			tx = session.beginTransaction();
			
			ProgramDAO programDAO = new ProgramDAO(session);
			CompanyDAO companyDAO = new CompanyDAO(session);
			
			Company company = companyDAO.searchByEmployee(user);
			
			programs = programDAO.consByCompany(company, joins);
			
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
		return programs;
	}
	
	/**
	 *  Get setting projects disabled
	 *  
	 * @return
	 */
	private boolean projectsDisabled(Session session) {
		
		boolean projectsDisabled = false;
		
		SettingDAO settingDAO 			= new SettingDAO(session);
		
		List<Setting> projectDisableList = settingDAO.findSetting(Settings.SETTING_DISABLE_PROJECT);
		
		
		if (ValidateUtil.isNotNull(projectDisableList)) {
			projectsDisabled = "true".equals(projectDisableList.get(0).getValue());
		}
		
		return projectsDisabled;
	}
	
	/**
	 * 
	 * @param program
	 * @return
	 * @throws Exception
	 */
	public double calcActualCost(Program program) throws Exception {
		
		double cost = 0;		
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			ProjectDAO projectDAO 			= new ProjectDAO(session);
			ProjectFollowupDAO followDAO 	= new ProjectFollowupDAO(session);
			
			// Projects disabled 
			//
			boolean projectsDisabled = projectsDisabled(session);
			
			List<Project> projects = projectDAO.findByProgramAndProjectsDisabled(program, projectsDisabled);
			
			for (Project p : projects) {
				
				List<Projectfollowup> followups = followDAO.findByProject(p, Constants.DESCENDENT);
				
				for (Projectfollowup f : followups) {
					
					if(f.getAc() != null) {
						
						cost += f.getAc();
						break;
					}
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
		return cost;
	}


	/**
	 * Search program by name and PO
	 * @param programName
	 * @param performingorg
	 * @return
	 * @throws Exception 
	 */
	public Program findByNameProgramAndPO(String programName, Performingorg performingorg) throws Exception {
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		Program program = null;
		
		try {
			tx = session.beginTransaction();
		
			ProgramDAO programDAO = new ProgramDAO(session);
			program = programDAO.findByNameProgramAndPO(programName, performingorg);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return program;
	}


	/**
	 * Search programs by name and PO
	 * @param name
	 * @param performingorg
	 * @param company 
	 * @return
	 * @throws Exception 
	 */
	public List<Program> searchPrograms(String name, Performingorg performingorg, Company company) throws Exception {
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		List<Program> programs = null;
		
		try {
			tx = session.beginTransaction();
		
			// Declare DAO
			ProgramDAO programDAO = new ProgramDAO(session);
			
			programs = programDAO.searchPrograms(name, performingorg, company);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return programs;
	}


	/**
	 * Search programs by filters
	 * @param list
	 * @param properyOrder
	 * @param typeOrder
	 * @param joins 
	 * @return
	 * @throws Exception 
	 */
	public List<Program> findByFilters(ArrayList<PropertyRelation> list, String propertyOrder, String typeOrder, List<String> joins) throws Exception {
		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
		List<Program> programs = null;
		
		try {
			tx = session.beginTransaction();
		
			ProgramDAO programDAO = new ProgramDAO(session);
			programs = programDAO.findByFilters(list, propertyOrder, typeOrder, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return programs;
		
	}
}