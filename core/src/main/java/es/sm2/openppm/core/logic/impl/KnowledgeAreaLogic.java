/*
 *   Copyright (C) 2009-2015 SM2 SOFTWARE & SERVICES MANAGEMENT
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program has been created in the hope that it will be useful.
 *   It is distributed WITHOUT ANY WARRANTY of any Kind,
 *   without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program. If not, see http://www.gnu.org/licenses/.
 *
 *   For more information, please contact SM2 Software & Services Management.
 *   Mail: info@talaia-openppm.com
 *   Web: http://www.talaia-openppm.com
 *
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.dao.KnowledgeAreaDAO;
import es.sm2.openppm.core.model.impl.KnowledgeArea;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ResourceBundle;

/**
 * Created by francisco.bisquerra on 31/07/2015.
 */
public class KnowledgeAreaLogic extends AbstractGenericLogic<KnowledgeArea, Integer> {

    public KnowledgeAreaLogic () {
        super();
    }

    public KnowledgeAreaLogic (ResourceBundle bundle) {
        super(bundle);
    }

    /**
     * Remove
     *
     * @param idKnowledgeArea
     * @throws Exception
     */
    public void remove(Integer idKnowledgeArea) throws Exception {

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();

            // Declare DAO
            KnowledgeAreaDAO knowledgeAreaDAO = new KnowledgeAreaDAO(session);

            // DAO
            KnowledgeArea knowledgeArea = knowledgeAreaDAO.findById(idKnowledgeArea, false);

            // Foreign key error
            if (ValidateUtil.isNotNull(knowledgeArea.getLearnedLessons())) {

                ParamResourceBundle paramResourceBundle = new ParamResourceBundle("msg.error.delete.this_has", "KNOWLEDGE_AREA", "LEARNED_LESSONS");

                throw new Exception(paramResourceBundle.getMessage(getBundle()));
            }
            else{
                // DAO
                knowledgeAreaDAO.makeTransient(knowledgeArea);
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
