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
 * File: StakeholderclassificationLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.logic.impl;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.dao.StakeholderDAO;
import es.sm2.openppm.core.dao.StakeholderclassificationDAO;
import es.sm2.openppm.core.model.impl.Stakeholder;
import es.sm2.openppm.core.model.impl.Stakeholderclassification;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;
import es.sm2.openppm.utils.javabean.ParamResourceBundle;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.ResourceBundle;


public final class StakeholderclassificationLogic extends AbstractGenericLogic<Stakeholderclassification, Integer> {

	public StakeholderclassificationLogic () {
		super();
	}

	public StakeholderclassificationLogic (ResourceBundle bundle) {
		super(bundle);
	}

	/**
	 * Delete stakeholderclassification
	 *
	 * @param stakeholderClassification
	 * @param IdStakeholderClassification
	 * @throws Exception
	 */
	public void deleteStakeholderClassification(Stakeholderclassification stakeholderClassification,
	        Integer IdStakeholderClassification) throws Exception {

		Transaction tx	= null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {

			tx = session.beginTransaction();

			// Declare DAOs
			StakeholderclassificationDAO stakeholderclassificationDAO 	= new StakeholderclassificationDAO(session);
			StakeholderDAO stakeholderDAO 								= new StakeholderDAO(session);

			// Foreign keys control
			List<Stakeholder> stakeholders = stakeholderDAO.findByRelation(Stakeholder.STAKEHOLDERCLASSIFICATION,
					stakeholderClassification, Stakeholder.IDSTAKEHOLDER, Constants.ASCENDENT);

			if (!stakeholders.isEmpty()) {

				ParamResourceBundle paramResourceBundle = new ParamResourceBundle("msg.error.delete.this_has","STAKEHOLDER_CLASSIFICATION","stakeholder");

				throw new Exception(paramResourceBundle.getMessage(getBundle()));
			}

			else{

				stakeholderClassification = stakeholderclassificationDAO.findById(IdStakeholderClassification, false);
				//Save
				stakeholderclassificationDAO.makeTransient(stakeholderClassification);
			}

			tx.commit();
		}

		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}
}

