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
 * File: ProjectUsedLessonLogic.java
 * Create User: jordi.ripoll
 * Create Date: 17/08/2015 10:10:03
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.LearnedLessonLinkDAO;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.LearnedLessonLink;
import es.sm2.openppm.core.model.impl.LearnedLessonProject;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordi.ripoll on 17/08/2015.
 */
public class LearnedLessonLinkLogic extends AbstractGenericLogic<LearnedLessonLink, Integer> {

    /**
     * Find by project
     *
     * @param project
     * @return
     */
    public List<LearnedLessonLink> findByProject(Project project) throws Exception {

        List<LearnedLessonLink> list = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            // Declare DAO
            LearnedLessonLinkDAO learnedLessonLinkDAO = new LearnedLessonLinkDAO(session);

            // Joins
            List<String> joins = new ArrayList<String>();

            joins.add(LearnedLessonLink.PROJECT);

            // Generic joins
            joins.add(LearnedLessonLink.LEARNEDLESSON);
            joins.add(LearnedLessonLink.LEARNEDLESSON + "." + LearnedLesson.LEARNEDLESSONLINKS);
            joins.add(LearnedLessonLink.LEARNEDLESSON + "." + LearnedLesson.ACTIONSLESSON);
            joins.add(LearnedLessonLink.LEARNEDLESSON + "." + LearnedLesson.RECSLESSON);
            joins.add(LearnedLessonLink.LEARNEDLESSON + "." + LearnedLesson.KNOWLEDGEAREA);
            joins.add(LearnedLessonLink.LEARNEDLESSON + "." + LearnedLesson.CONTACT);

            // Specific joins
            joins.add(LearnedLessonLink.LEARNEDLESSON + "." + LearnedLesson.LEARNEDLESSONPROJECTS);
            joins.add(LearnedLessonLink.LEARNEDLESSON + "." + LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT);
            joins.add(LearnedLessonLink.LEARNEDLESSON + "." + LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.PERFORMINGORG);
            joins.add(LearnedLessonLink.LEARNEDLESSON + "." + LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.EMPLOYEEBYPROJECTMANAGER);
            joins.add(LearnedLessonLink.LEARNEDLESSON + "." + LearnedLesson.LEARNEDLESSONPROJECTS + "." + LearnedLessonProject.PROJECT + "." + Project.EMPLOYEEBYPROJECTMANAGER + "." +
                    Employee.CONTACT);

            // DAO
            list = learnedLessonLinkDAO.findByProject(project, joins);

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

        return list;
    }

}
