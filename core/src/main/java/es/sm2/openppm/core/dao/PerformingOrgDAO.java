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
 * File: PerformingOrgDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class PerformingOrgDAO extends AbstractGenericHibernateDAO<Performingorg, Integer> {

	public PerformingOrgDAO(Session session) {
		super(session);
	}

	/**
	 * Return Performingorg by Contact
	 * @param contact
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Performingorg> searchByContact(Contact contact) {
		
		Query query = getSession().createQuery(
				"select distinct p from Performingorg as p "+
				"left join p.employees as e " +
				"left join e.resourceprofiles as rp " +
				"where e.contact.idContact = :idContact " +
				"order by p.name"
			);
		query.setInteger("idContact",contact.getIdContact());
		
		return query.list();
		
	}

	/**
	 * Find other POs by company
	 * @param company
	 * @param performingorg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Performingorg> findOtherPOs(Company company, Integer idPerfOrg) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		crit.add(Restrictions.ne(Performingorg.IDPERFORG, idPerfOrg))
			.add(Restrictions.eq(Performingorg.COMPANY, company));
		
		addOrder(crit, Performingorg.NAME, Constants.ASCENDENT);
		
		return crit.list();
	}

}
