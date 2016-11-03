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
 * File: ChangeControlDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import es.sm2.openppm.core.model.search.ChangeControlSearch;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Changecontrol;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class ChangeControlDAO extends AbstractGenericHibernateDAO<Changecontrol, Integer> {

	public ChangeControlDAO(Session session) {
		super(session);
	}
	
    @Deprecated
	public List<Changecontrol> findByProject(Project proj) {
		
		List<Changecontrol> list = null;
		
		if (proj != null) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.add(Restrictions.eq(Changecontrol.PROJECT, proj));
			
			// Joins
			crit.setFetchMode(Changecontrol.CHANGETYPE, FetchMode.JOIN);
			crit.setFetchMode(Changecontrol.CHANGEREQUESTWBSNODES, FetchMode.JOIN);
			
			// Order
			crit.addOrder(Order.asc(Changecontrol.CHANGEDATE));
			
			list = crit.list();
		}
		
		return list;
	}
	
	
	/**
	 * Find by Id and return data
	 * @param change
	 * @return
	 */
	public Changecontrol findByIdWithData(Changecontrol change) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		crit.setFetchMode("changetype", FetchMode.JOIN);
		crit.setFetchMode("wbsnode", FetchMode.JOIN);
		crit.add(Restrictions.eq("idChange", change.getIdChange()));
		
		return (Changecontrol) crit.uniqueResult();
	}

    /**
     * Find change controls
     *
     * @param search
     * @return
     */
    public List<Changecontrol> find(ChangeControlSearch search) {

        List<Changecontrol> list = null;

        if (search.getIdProject() != null) {

            Criteria crit = getSession().createCriteria(getPersistentClass())
                    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                    .add(Restrictions.eq(Changecontrol.PROJECT, new Project(search.getIdProject())));

            // Joins
            crit.setFetchMode(Changecontrol.CHANGETYPE, FetchMode.JOIN);
            crit.setFetchMode(Changecontrol.CHANGEREQUESTWBSNODES, FetchMode.JOIN);

            // Filter by resolution
            if (search.getResolution() != null) {
                crit.add(Restrictions.eq(Changecontrol.RESOLUTION, search.getResolution()));
            }
            // Order
            crit.addOrder(Order.asc(Changecontrol.CHANGEDATE));

            list = crit.list();
        }

        return list;
    }
}
