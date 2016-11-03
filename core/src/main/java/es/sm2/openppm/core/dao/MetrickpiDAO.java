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
 * File: MetrickpiDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import es.sm2.openppm.utils.functions.ValidateUtil;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Bscdimension;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Metrickpi;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectkpi;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Metrickpi
 * @see es.sm2.openppm.core.dao.Metrickpi
 * @author Hibernate Generator by Javier Hernandez
 */
public class MetrickpiDAO extends AbstractGenericHibernateDAO<Metrickpi, Integer> {


	public MetrickpiDAO(Session session) {
		super(session);
	}

	/**
	 * Find by company of user
	 * @param company
	 * @param joins 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Metrickpi> findByCompany(Company company, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		if (joins != null && !joins.isEmpty()) {
			for (String join : joins) { crit.setFetchMode(join, FetchMode.JOIN); }
		}
		
		crit.add(Restrictions.eq(Metrickpi.COMPANY, company));
		
		return crit.list();
	}
/**
 * Search metricKpis by filter
 *
 * @param name
 * @param type
 *@param company  @return
 */
	@SuppressWarnings("unchecked")
	public List<Metrickpi> searchByFilter(String name, Integer idBSCDimension, String type, Project project, Company company, List<String> joins) {
		
		Metrickpi example = new Metrickpi();
		example.setName(name);
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
			.add(Example.create(example)
						.ignoreCase()
						.enableLike(MatchMode.ANYWHERE)
			);

        // Add joins
		if (joins != null && !joins.isEmpty()) {
			for (String join : joins) { crit.setFetchMode(join, FetchMode.JOIN); }
		}

        // by company
		crit.createCriteria(Metrickpi.COMPANY)
			.add(Restrictions.idEq(company.getIdCompany()))
			.add(Restrictions.or(
					Restrictions.isNull(Company.DISABLE),
					Restrictions.ne(Company.DISABLE, true))
				);

        // By bsc dimension
		if (!idBSCDimension.equals(-1)) {
			crit.add(Restrictions.eq(Metrickpi.BSCDIMENSION, new Bscdimension(idBSCDimension)));
		}

        // By type
        if (ValidateUtil.isNotNull(type)) {
            crit.add(Restrictions.eq(Metrickpi.TYPE, type));
        }

        // By project
		if (project.getIdProject() != -1) {
			crit.createCriteria(Projectkpi.PROJECT)
					.add(Restrictions.idEq(project.getIdProject()));
		}
		
		return crit.list();
	}

}

