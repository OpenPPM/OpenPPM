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
 * File: ProjectGeneratedLessonLogic.java
 * Create User: mariano.fontana
 * Create Date: 31/07/2015 13:01:27
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.LearnedLessonProjectDAO;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.LearnedLessonProject;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mariano.fontana on 31/07/2015.
 */
public class LearnedLessonProjectLogic extends AbstractGenericLogic<LearnedLessonProject, Integer> {

    /**
     * Find by project
     *
     * @param project
     * @return
     * @throws Exception
     */
    public List<LearnedLessonProject> findByProject(Project project) throws Exception {

        List<LearnedLessonProject> list = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            // Declare DAO
            LearnedLessonProjectDAO learnedLessonProjectDAO = new LearnedLessonProjectDAO(session);

            // Joins
            List<String> joins = new ArrayList<String>();

            joins.add(LearnedLessonProject.LEARNEDLESSON);
            joins.add(LearnedLessonProject.LEARNEDLESSON + "." + LearnedLesson.KNOWLEDGEAREA);
            joins.add(LearnedLessonProject.LEARNEDLESSON + "." + LearnedLesson.ACTIONSLESSON);
            joins.add(LearnedLessonProject.LEARNEDLESSON + "." + LearnedLesson.RECSLESSON);
            joins.add(LearnedLessonProject.LEARNEDLESSON + "." + LearnedLesson.CONTACT);
            joins.add(LearnedLessonProject.LEARNEDLESSON + "." + LearnedLesson.LEARNEDLESSONLINKS);

            // DAO
            list = learnedLessonProjectDAO.findByProject(project, joins);

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

    /**
     * Find by project and Learned Lesson
     *
     * @param project
     * @param learnedLesson
     * @return
     * @throws Exception
     */
    public LearnedLessonProject findByProjectAndLA(Project project, LearnedLesson learnedLesson) throws Exception {

        LearnedLessonProject learnedLessonProject = null;

        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            // Joins
            List<String> joins = new ArrayList<String>();

            joins.add(LearnedLessonProject.PROJECT);
            joins.add(LearnedLessonProject.LEARNEDLESSON);

            // Declare DAO
            LearnedLessonProjectDAO learnedLessonProjectDAO = new LearnedLessonProjectDAO(session);

            // DAO
            learnedLessonProject = learnedLessonProjectDAO.findByProjectAndLA(project, learnedLesson, joins);

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

        return learnedLessonProject;
    }
}
