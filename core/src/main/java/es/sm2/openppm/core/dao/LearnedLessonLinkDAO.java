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
 * File: ProjectUsedLessonDAO.java
 * Create User: jordi.ripoll
 * Create Date: 17/08/2015 10:29:03
 */

package es.sm2.openppm.core.dao;

import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.LearnedLessonLink;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by jordi.ripoll on 17/08/2015.
 */
    public class LearnedLessonLinkDAO extends AbstractGenericHibernateDAO<LearnedLessonLink, Integer> {

    public LearnedLessonLinkDAO(Session session) {
            super(session);
        }

    /**
     * Find by project
     *
     * @param project
     * @param joins
     * @return
     */
    public List<LearnedLessonLink> findByProject(Project project, List<String> joins) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .add(Restrictions.eq(LearnedLessonLink.PROJECT, project));

        // Left joins
        addJoins(crit, joins);

        return crit.list();
    }
}
