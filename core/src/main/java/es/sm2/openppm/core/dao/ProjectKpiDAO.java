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
 * File: ProjectKpiDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Metrickpi;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectkpi;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class ProjectKpiDAO extends AbstractGenericHibernateDAO<Projectkpi, Integer> {

	public ProjectKpiDAO(Session session) {
		super(session);
	}
	
	/**
	 * 
	 * @param project
	 * @return
	 */
	public Double getTotalWeight(Project project) {
		
		Double total = null;
		
		if (project != null) {
			Query q = getSession().createQuery(
					"select sum(kpi.weight) " +
					"from Projectkpi as kpi " + 
					"join kpi.project as project " +
					"where project.idProject = :idProject");
			q.setInteger("idProject", project.getIdProject());
			
			total = (Double) q.uniqueResult();
		}
		
		return total;
	}

	
	/**
	 * Score = sum (kpi.weight * (kpi.adjustedValue/100))
	 * adjustedValue = 
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Double calcScore(Project project) {
		Double score = null;
		
		if (project != null) {
			Query q = getSession().createQuery(
					"select kpi " +
					"from Projectkpi as kpi " + 
					"where project.idProject = :idProject"
					);
			q.setInteger("idProject", project.getIdProject());
			
			List<Projectkpi> kpis = q.list();
			if (!kpis.isEmpty()) {
				score = 0D;
				for (Projectkpi kpi : kpis) {
					Double kpiScore = new Double(kpi.getWeight() * (new Double(kpi.getAdjustedValue())/100));
					score += kpiScore;
				}
			}
		}
		
		return score;
	}

	/**
	 * Check if metric is assigned in other project KPI
	 * @param project
	 * @param idProjectKPI
	 * @param metrickpi
	 * @return
	 */
	public boolean metricExist(Project project, int idProjectKPI, Metrickpi metrickpi) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq(Projectkpi.PROJECT, project))
			.add(Restrictions.eq(Projectkpi.METRICKPI, metrickpi));
		
		if (idProjectKPI != -1) {
			crit.add(Restrictions.ne(Projectkpi.IDPROJECTKPI, idProjectKPI));
		}
		Integer count = (Integer) crit.uniqueResult();
		
		return (count != null && count > 0);
	}

	/**
	 * Return aggregate kpis by filter project
	 * @param aggregatekpi
	 * @param ids 
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Projectkpi> findAggregateProjects(String aggregatekpi, Integer[] ids, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.eq(Projectkpi.AGGREGATEKPI, true));
		
		addJoins(crit, joins);
		
		crit.createCriteria(Projectkpi.PROJECT)
			.add(Restrictions.in(Project.IDPROJECT, ids));
		
		return crit.list();
	}

	/**
	 * Find if a project KPIS empty
	 * @param project
	 * @return
	 */
	public boolean thereKPISEmpty(Project project) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.setProjection(Projections.rowCount())
				.add(Restrictions.eq(Projectkpi.PROJECT, project))
				.add(Restrictions.isNull(Projectkpi.VALUE));
			
		Integer count = (Integer) crit.uniqueResult();
		
		return (count != null && count > 0);
	}

    /**
     * Find by project and contains metrickpi type
     *
     * @param project
     * @return
     */
    public List<Projectkpi> findByProjectAndMetricType(Project project) {

        // Find by project
        Criteria crit = getSession().createCriteria(getPersistentClass())
                .add(Restrictions.eq(Projectkpi.PROJECT, project));

        // Find by metrickpi contains type
        crit.createCriteria(Projectkpi.METRICKPI)
                .add(Restrictions.isNotNull(Metrickpi.TYPE));

        return crit.list();
    }
}
