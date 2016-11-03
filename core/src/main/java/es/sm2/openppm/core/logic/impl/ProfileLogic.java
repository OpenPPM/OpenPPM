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
 * File: ProfileLogic.java
 * Create User: mariano.fontana
 * Create Date: 31/07/2015 13:00:12
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.ProfileDAO;
import es.sm2.openppm.core.model.impl.Profile;
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
public class ProfileLogic extends AbstractGenericLogic<Profile, Integer> {

    public ProfileLogic () {
        super();
    }

    public ProfileLogic (ResourceBundle bundle) {
        super(bundle);
    }

    /**
     * Remove
     *
     * @param idProfile
     * @throws Exception
     */
    public void remove(Integer idProfile) throws Exception {

        if (idProfile == null || idProfile == -1) {
            throw new Exception("No data");
        }

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();

            // Declare DAO
            ProfileDAO profileDAO = new ProfileDAO(session);

            // DAO
            Profile profile = profileDAO.findById(idProfile, false);

            // Foreign key error
            if (ValidateUtil.isNotNull(profile.getLearnedLessons())) {

                ParamResourceBundle paramResourceBundle = new ParamResourceBundle("msg.error.delete.this_has", "profile", "LEARNED_LESSONS");

                throw new Exception(paramResourceBundle.getMessage(getBundle()));
            }
            else{
                // DAO
                profileDAO.makeTransient(profile);
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