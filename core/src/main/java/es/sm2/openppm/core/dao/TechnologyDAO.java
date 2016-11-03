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
 * File: TechnologyDAO.java
 * Create User: jordi.ripoll
 * Create Date: 29/06/2015 14:18:44
 */

package es.sm2.openppm.core.dao;

import es.sm2.openppm.core.logic.persistance.MappingGenericDAO;
import es.sm2.openppm.core.logic.persistance.MappingHql;
import es.sm2.openppm.core.logic.persistance.hql.generic.GenericNamedOrder;
import es.sm2.openppm.core.logic.persistance.hql.generic.NamedParams;
import es.sm2.openppm.core.logic.persistance.hql.technology.TechnologyNamedQuery;
import es.sm2.openppm.core.logic.persistance.hql.technology.TechnologyNamedRestriction;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Technology;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;


/**
 * DAO object for domain model class Label
 * @see es.sm2.openppm.core.dao.TechnologyDAO
 * @author Hibernate Generator by Javier Hernandez
 */
public class TechnologyDAO extends MappingGenericDAO<Technology, Integer> {


	public TechnologyDAO(Session session) {
		super(session);
	}

    /**
     * Search technologies except those who already have the project
     *
     * @param project
     * @param company
     * @return
     */
    public List<Technology> findNotHave(Project project, Company company) {

        // Create query
        //
        MappingHql mappingHql = this.getMappingHql().setNamedQuery(TechnologyNamedQuery.FIND_NOT_HAVE)
                .putRestrictions(TechnologyNamedRestriction.EQ_COMPANY)
                .putRestrictions(TechnologyNamedRestriction.EXCLUDE_IN_PROJECT);


        String statement = mappingHql.putOrders(GenericNamedOrder.BY_NAME).create();

        Query query = getSession().createQuery(statement);

        // Set parameters
        //
        if (project != null) {
            query.setParameter(NamedParams.PROJECT.name(), project);
        }

        if (company != null) {
            query.setParameter(NamedParams.COMPANY.name(), company);
        }

        return query.list();
    }
}

