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
 * File: TimelineTypeLogic.java
 * Create User: jordi.ripoll
 * Create Date: 18/11/2015 13:21:36
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.TimelineTypeDAO;
import es.sm2.openppm.core.model.impl.TimelineType;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ResourceBundle;

/**
 * Created by mariano.fontana on 31/07/2015.
 */
public class TimelineTypeLogic extends AbstractGenericLogic<TimelineType, Integer> {

    public TimelineTypeLogic() {
        super();
    }

    public TimelineTypeLogic(ResourceBundle bundle) {
        super(bundle);
    }

    /**
     * Remove
     *
     * @param idTimelineType
     * @throws Exception
     */
    public void remove(Integer idTimelineType) throws Exception {

        if (idTimelineType == null || idTimelineType == -1) {
            throw new Exception("No data");
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();

            // Declare DAO
            TimelineTypeDAO timelineTypeDAO = new TimelineTypeDAO(session);

            // DAO
            TimelineType timelineType = timelineTypeDAO.findById(idTimelineType, false);

            // Foreign key error
            if (ValidateUtil.isNotNull(timelineType.getTimelines())) {

                ParamResourceBundle paramResourceBundle = new ParamResourceBundle("msg.error.delete.this_has", "TIMELINE_TYPE", "timeline");

                throw new Exception(paramResourceBundle.getMessage(getBundle()));
            }
            else{
                // DAO
                timelineTypeDAO.makeTransient(timelineType);
            }

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
    }
}