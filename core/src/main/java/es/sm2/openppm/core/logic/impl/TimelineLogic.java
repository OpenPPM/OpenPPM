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
 * File: TimelineLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.DocumentprojectDAO;
import es.sm2.openppm.core.dao.TimelineDAO;
import es.sm2.openppm.core.model.impl.Documentproject;
import es.sm2.openppm.core.model.impl.Timeline;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Map;
import java.util.ResourceBundle;


/**
 * Logic object for domain model class Timeline
 * @see es.sm2.openppm.logic.TimelineLogic
 * @author Hibernate Generator by Javier Hernandez
 *
 * IMPORTANT! Instantiate the class for use generic methods
 *
 */
public final class TimelineLogic extends AbstractGenericLogic<Timeline, Integer> {


    private Map<String, String> settings;

    public TimelineLogic(Map<String, String> settings, ResourceBundle bundle) {
        super(bundle);
        this.settings = settings;
    }

    /**
     * Getter for property 'settings'.
     *
     * @return Value for property 'settings'.
     */
    public Map<String, String> getSettings() {
        return settings;
    }

    /**
     * Setter for property 'settings'.
     *
     * @param settings Value to set for property 'settings'.
     */
    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    /**
     * Save document
     *
     * @param idTimeline
     * @param doc
     */
    public void saveDocument(int idTimeline, Documentproject doc) throws Exception {

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            // DAO
            TimelineDAO timelineDAO = new TimelineDAO(session);

            // Find data
            Timeline timeline = timelineDAO.findById(idTimeline);

            // Set document
            timeline.setDocumentproject(doc);

            // Update closure check project
            timelineDAO.makePersistent(timeline);

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }

    /**
     * Delete timeline and document
     *
     * @param timeline
     */
    public void remove(Timeline timeline) throws Exception {

        Transaction tx	= null;
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            if (timeline.getIdTimeline() != null) {

                // DAO
                TimelineDAO timelineDAO = new TimelineDAO(session);

                // Find data
                timeline = timelineDAO.findById(timeline.getIdTimeline());

                if (timeline.getDocumentproject() != null && timeline.getDocumentproject().getIdDocumentProject() != null) {

                    // DAO
                    DocumentprojectLogic documentprojectLogic = new DocumentprojectLogic(getSettings(), getBundle());

                    // Delete document
                    documentprojectLogic.remove(timeline.getDocumentproject(), session);
                }

                // Delete timeline
                timelineDAO.makeTransient(timeline);
            }

            tx.commit();
        }
        catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
        finally { SessionFactoryUtil.getInstance().close(); }
    }
}

