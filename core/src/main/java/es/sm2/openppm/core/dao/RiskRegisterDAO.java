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
 * File: RiskRegisterDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Riskregister;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class RiskRegisterDAO extends AbstractGenericHibernateDAO<Riskregister, Integer> {

	public RiskRegisterDAO(Session session) {
		super(session);
	}

	/**
	 * Find by project and open risk
	 * @param project
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Riskregister> findByProjectAndOpen(Project project) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
				
		crit.add(Restrictions.eq(Riskregister.PROJECT, project))
			.add(Restrictions.eq(Riskregister.STATUS, Constants.CHAR_OPEN));
		
		return crit.list();
	}

    /**
     * Find by project
     * @param project
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Riskregister> findByProject(Project project) {
        Criteria crit = getSession().createCriteria(getPersistentClass());

        crit.add(Restrictions.eq(Riskregister.PROJECT, project));

        return crit.list();
    }
	
	/**
	 * Check for closed risks
	 * 
	 * @param project
	 * @return
	 */
	public boolean risksNotClosed(Project project) {
		
		Criteria critRisks = getSession().createCriteria(getPersistentClass())
				.setProjection(Projections.rowCount())
				.add(Restrictions.not(
						Restrictions.eq(Riskregister.STATUS, Constants.CHAR_CLOSED)))
				.add(Restrictions.eq(Riskregister.PROJECT, project));
		
		Integer count = (Integer) critRisks.uniqueResult();
		
		return (count != null && count > 0);
	}
}