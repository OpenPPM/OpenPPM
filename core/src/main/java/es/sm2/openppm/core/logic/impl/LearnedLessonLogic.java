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
 * File: LearnedLessonLogic.java
 * Create User: mariano.fontana
 * Create Date: 31/07/2015 13:04:33
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.LearnedLessonDAO;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.search.LearnedLessonSearch;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by mariano.fontana on 31/07/2015.
 */
public class LearnedLessonLogic extends AbstractGenericLogic<LearnedLesson, Integer> {

    /***
     * Find generated learned lessons
     *
     * @param searchParams
     * @return
     * @throws Exception
     */
    public List<LearnedLesson> findGenerated(LearnedLessonSearch searchParams) throws Exception {

        List<LearnedLesson> list = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            // Declare DAO
            LearnedLessonDAO learnedLessonDAO = new LearnedLessonDAO(session);

            // DAO
            list = learnedLessonDAO.findGenerated(searchParams);

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

    /***
     * Find generated learned lesson by id
     *
     * @param id
     * @param joins
     * @return
     * @throws Exception
     */
    public LearnedLesson findGenerated(Integer id, List<String> joins) throws Exception {

        LearnedLesson learnedLesson = null;

        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            // Declare DAO
            LearnedLessonDAO learnedLessonDAO = new LearnedLessonDAO(session);

            // DAO
            learnedLesson = learnedLessonDAO.findGenerated(id, joins);

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

        return learnedLesson;
    }

    /**
     * Check if role match
     *
     * @param searchParams
     * @param rolePmo
     * @return
     */
    private boolean isUserInRole(LearnedLessonSearch searchParams, int rolePmo) {

        return searchParams.getUser().getResourceprofiles().getIdProfile() == rolePmo;
    }
}
