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
 * File: ProjectKpiLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import es.sm2.openppm.core.model.impl.Projectfollowup;
import es.sm2.openppm.core.utils.OpenppmUtil;
import es.sm2.openppm.utils.Info;
import es.sm2.openppm.utils.StringPool;
import es.sm2.openppm.utils.functions.ValidateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.HistorickpiDAO;
import es.sm2.openppm.core.dao.ProjectKpiDAO;
import es.sm2.openppm.core.exceptions.KpiWeightExcededException;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Historickpi;
import es.sm2.openppm.core.model.impl.Metrickpi;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectkpi;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

/**
 * This class provides all methods for Project' KPI
 * @author juanma.lopez
 *
 */

public class ProjectKpiLogic extends AbstractGenericLogic<Projectkpi, Integer> {

    private Map<String, String> settings;

    public ProjectKpiLogic(Map<String, String> settings, ResourceBundle bundle) {
        super(bundle);
        this.settings = settings;
    }

    public ProjectKpiLogic() {

    }

    /**
     * Save project KPI
     *
     * @param session
     * @param kpi
     * @param user
     * @return
     * @throws Exception
     */
    public Projectkpi saveProjectKpi(Session session, Projectkpi kpi, Employee user, Historickpi.UpdatedType updatedType) throws Exception {

        ProjectKpiDAO kpiDAO			= new ProjectKpiDAO(session);
        HistorickpiDAO historickpiDAO	= new HistorickpiDAO(session);

        Projectkpi projectkpi = kpiDAO.makePersistent(kpi);

        double total = kpiDAO.getTotalWeight(kpi.getProject());

        if (total > 100.0) {
            throw new KpiWeightExcededException();
        }

        Historickpi historickpi = new Historickpi();
        historickpi.setProjectkpi(kpi);
        historickpi.setLowerThreshold(kpi.getLowerThreshold());
        historickpi.setUpperThreshold(kpi.getUpperThreshold());
        historickpi.setValueKpi(kpi.getValue());
        historickpi.setWeight(kpi.getWeight());
        historickpi.setEmployee(user);
        historickpi.setActualDate(new Date());

        if (updatedType != null) {
            historickpi.setUpdatedType(updatedType.toString());
        }

        historickpiDAO.makePersistent(historickpi);

        return projectkpi;
    }

	/**
	 * Save project KPI
     *
	 * @param kpi
	 * @param user 
	 * @throws Exception 
	 */
	public Projectkpi saveProjectKpi(Projectkpi kpi, Employee user, Historickpi.UpdatedType updatedType) throws Exception {

        Projectkpi projectkpi = null;

		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();

            projectkpi = saveProjectKpi(session, kpi, user, updatedType);
			
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

        return projectkpi;
	}

	/**
	 * Check if metric is assigned in other project KPI
	 * @param project
	 * @param idProjectKPI
	 * @param metrickpi
	 * @return
	 * @throws Exception 
	 */
	public boolean metricExist(Project project, int idProjectKPI, Metrickpi metrickpi) throws Exception {
		
		boolean metricExist = false;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectKpiDAO kpiDAO = new ProjectKpiDAO(session);
			metricExist = kpiDAO.metricExist(project, idProjectKPI, metrickpi);
			
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
		return metricExist;
	}


	public List<Projectkpi> getAggregates(Integer[] ids) throws Exception {

		List<Projectkpi> list = null;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectKpiDAO kpiDAO = new ProjectKpiDAO(session);
			
			List<String> joins = new ArrayList<String>();
			joins.add(Projectkpi.PROJECT);
			joins.add(Projectkpi.METRICKPI);
			
			list = kpiDAO.findAggregateProjects(Projectkpi.AGGREGATEKPI, ids, joins);
			
			tx.commit();
			
			for (Projectkpi projectkpi : list) {
				Project project 	= new Project(projectkpi.getProject().getProjectName());
				projectkpi.setProject(project);
				
				if(projectkpi.getMetrickpi() != null){
					Metrickpi metrickpi = new Metrickpi(projectkpi.getMetrickpi().getName());
					projectkpi.setMetrickpi(metrickpi);
				}
				
			}
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
		return list;
	}
	
	/**
	 * Find if a project KPIS empty
	 * @param project
	 * @return
	 * @throws Exception 
	 */
	public boolean thereKPISEmpty(Project project) throws Exception {
		boolean thereKPISEmpty = false;
		
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			ProjectKpiDAO kpiDAO = new ProjectKpiDAO(session);
			
			thereKPISEmpty = kpiDAO.thereKPISEmpty(project);
			
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
		
		return thereKPISEmpty;
	}
	
	/**
	 * Calculate imported kpi weight
	 * @param importProject
	 * @param actualProject
	 * @return
	 * @throws Exception 
	 */
	public Double calcWeightKpi(Project importProject, Project actualProject) throws Exception {
		
		Transaction tx = null;
		
		Double total = 0.0;
		Double totalProject = 0.0;

		try {
			
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			
			// New KPIDAO
			ProjectKpiDAO kpiDAO = new ProjectKpiDAO(session);
			
			// Get kpi weight from actual project
			
			totalProject = kpiDAO.getTotalWeight(actualProject);
			
			// Calculating total + new kpi weight
			 for (Projectkpi projectkpi : importProject.getProjectkpis()) {
				 
					total = total + projectkpi.getWeight();	
			 }
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
		
		if (totalProject != null) {
			total = total + totalProject;
		}
		
		// Return the weight from new kpi imported and already existing kpi's
		return total;
	}
	
	/**
	 * Importing KPI
	 * @param importProject
	 * @param actualProject
	 * @param isOverWeight
	 * @param user
	 * @throws Exception
	 */
	public void importProjectKpi(Project importProject, Project actualProject, Boolean isOverWeight, Employee user) throws Exception {
	
		Transaction tx = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
				
		try {
			tx = session.beginTransaction();
			
			ProjectKpiDAO kpiDAO			= new ProjectKpiDAO(session);
			HistorickpiDAO historickpiDAO	= new HistorickpiDAO(session);
			
			 for (Projectkpi projectkpi : importProject.getProjectkpis()) {
					
					projectkpi.setIdProjectKpi(null);
					projectkpi.setProject(actualProject);
					projectkpi.setAggregateKpi(null);
					projectkpi.setValue(null);
					
					if (isOverWeight) {
						projectkpi.setWeight(0.0);
					}
					
					projectkpi = kpiDAO.makePersistent(projectkpi);
					
					Historickpi historickpi = new Historickpi();
					historickpi.setProjectkpi(projectkpi);
					historickpi.setLowerThreshold(projectkpi.getLowerThreshold());
					historickpi.setUpperThreshold(projectkpi.getUpperThreshold());
					historickpi.setValueKpi(projectkpi.getValue());
					historickpi.setWeight(projectkpi.getWeight());
					historickpi.setEmployee(user);
					historickpi.setActualDate(new Date());
					
					historickpiDAO.makePersistent(historickpi);
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

    /**
     * Update kpis by enum typeKPI
     *
     * @param session
     * @param followupsES
     * @param projectfollowup
     * @param user   @throws Exception
     */
    public List<Info> updateAutomaticKPIs(Session session, List<Projectfollowup> followupsES, Projectfollowup projectfollowup, Employee user) throws Exception {

        List<Info> infos = new ArrayList<Info>();

        // Declare DAO
        ProjectKpiDAO projectKpiDAO = new ProjectKpiDAO(session);

        // Find project kpis by project and contains a type
        List<Projectkpi> projectkpis = projectKpiDAO.findByProjectAndMetricType(projectfollowup.getProject());

        if (ValidateUtil.isNotNull(projectkpis)) {

            // Update kpi by type
            //
            for (Projectkpi projectkpi : projectkpis) {

                boolean updateKPI = false;

                // CPI
                if (projectkpi.getMetrickpi() != null && ValidateUtil.isNotNull(projectkpi.getMetrickpi().getType()) &&
                        Metrickpi.TypeKPI.CPI.toString().equals(projectkpi.getMetrickpi().getType()) &&
                        projectfollowup.getCpi() != null) {

                    projectkpi.setValue(projectfollowup.getCpi());

                    // Add message
                    //
                    Info info = new Info(StringPool.InfoType.SUCCESS, "update_automatic_kpi", projectkpi.getMetrick());

                    infos.add(info);

                    updateKPI = true;
                }
                // SPI
                else if (projectkpi.getMetrickpi() != null && ValidateUtil.isNotNull(projectkpi.getMetrickpi().getType()) &&
                        Metrickpi.TypeKPI.SPI.toString().equals(projectkpi.getMetrickpi().getType()) &&
                        projectfollowup.getSpi() != null) {

                    projectkpi.setValue(projectfollowup.getSpi());

                    // Add message
                    //
                    Info info = new Info(StringPool.InfoType.SUCCESS, "update_automatic_kpi", projectkpi.getMetrick());

                    infos.add(info);

                    updateKPI = true;
                }
                // SPI(t)
                else if (projectkpi.getMetrickpi() != null && ValidateUtil.isNotNull(projectkpi.getMetrickpi().getType()) &&
                        Metrickpi.TypeKPI.SPI_T.toString().equals(projectkpi.getMetrickpi().getType())) {

                    Double follES       = OpenppmUtil.getES(followupsES, projectfollowup, projectfollowup.getProject(), session);
                    Integer daysToDate  = projectfollowup.getDaysToDate(projectfollowup.getProject(), session);

                    if (follES != null && follES > 0 && daysToDate != null && daysToDate > 0) {

                        projectkpi.setValue(follES/daysToDate);

                        // Add message
                        //
                        Info info = new Info(StringPool.InfoType.SUCCESS, "update_automatic_kpi", projectkpi.getMetrick());

                        infos.add(info);

                        updateKPI = true;
                    }
                }
                // CV
                else if (projectkpi.getMetrickpi() != null && ValidateUtil.isNotNull(projectkpi.getMetrickpi().getType()) &&
                        Metrickpi.TypeKPI.CV.toString().equals(projectkpi.getMetrickpi().getType()) &&
                        projectfollowup.getCv() != null) {

                    projectkpi.setValue(projectfollowup.getCv());

                    // Add message
                    //
                    Info info = new Info(StringPool.InfoType.SUCCESS, "update_automatic_kpi", projectkpi.getMetrick());

                    infos.add(info);

                    updateKPI = true;
                }
                // SV
                else if (projectkpi.getMetrickpi() != null && ValidateUtil.isNotNull(projectkpi.getMetrickpi().getType()) &&
                        Metrickpi.TypeKPI.SV.toString().equals(projectkpi.getMetrickpi().getType()) &&
                        projectfollowup.getSv() != null) {

                    projectkpi.setValue(projectfollowup.getSv());

                    // Add message
                    //
                    Info info = new Info(StringPool.InfoType.SUCCESS, "update_automatic_kpi", projectkpi.getMetrick());

                    infos.add(info);

                    updateKPI = true;
                }
                // SV(t)
                else if (projectkpi.getMetrickpi() != null && ValidateUtil.isNotNull(projectkpi.getMetrickpi().getType()) &&
                        Metrickpi.TypeKPI.SV_T.toString().equals(projectkpi.getMetrickpi().getType())) {

                    Double follES       = OpenppmUtil.getES(followupsES, projectfollowup, projectfollowup.getProject(), session);
                    Integer daysToDate  = projectfollowup.getDaysToDate(projectfollowup.getProject(), session);

                    if (follES != null && follES > 0 && daysToDate != null) {

                        projectkpi.setValue(follES - daysToDate);

                        // Add message
                        //
                        Info info = new Info(StringPool.InfoType.SUCCESS, "update_automatic_kpi", projectkpi.getMetrick());

                        infos.add(info);

                        updateKPI = true;
                    }
                }
                // EAC
                else if (projectkpi.getMetrickpi() != null && ValidateUtil.isNotNull(projectkpi.getMetrickpi().getType()) &&
                        Metrickpi.TypeKPI.EAC.toString().equals(projectkpi.getMetrickpi().getType()) &&
                        projectfollowup.getEac() != null) {

                    projectkpi.setValue(projectfollowup.getEac());

                    // Add message
                    //
                    Info info = new Info(StringPool.InfoType.SUCCESS, "update_automatic_kpi", projectkpi.getMetrick());

                    infos.add(info);

                    updateKPI = true;
                }

                if (updateKPI) {

                    // Update project kpi and historic kpi
                    this.saveProjectKpi(session, projectkpi, user, Historickpi.UpdatedType.AUTOMATIC_EVM);
                }
            }

            // Update kpi status for project
            //
            ProjectLogic projectLogic = new ProjectLogic(settings, getBundle());

            projectLogic.updateKPIStatus(session, projectfollowup.getProject().getIdProject());
        }

        return infos;
    }
}