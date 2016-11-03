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
 * File: ChargescostsDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Currency;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Chargescosts
 * @see es.sm2.openppm.core.dao.Chargescosts
 * @author Hibernate Generator by Javier Hernandez
 */
public class ChargescostsDAO extends AbstractGenericHibernateDAO<Chargescosts, Integer> {


	public ChargescostsDAO(Session session) {
		super(session);
	}

	/**
	 * Cons Charge Costs by project and type
	 * @param proj
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Chargescosts> consChargescostsByProject(Project proj, int type) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.eq(Chargescosts.PROJECT, proj))
			.add(Restrictions.eq(Chargescosts.IDCHARGETYPE, type))
			.setFetchMode(Chargescosts.CURRENCY, FetchMode.JOIN);
		
		return crit.list();
	}

	public double getSumCosts(Project project, int type) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.sum(Chargescosts.COST))
			.add(Restrictions.eq(Chargescosts.PROJECT, project))
			.add(Restrictions.eq(Chargescosts.IDCHARGETYPE, type));	
		
		Double cost = (Double)crit.uniqueResult();
		return (cost == null?0:cost);
	}

	/**
	 * Sum costs by currency
	 * @param project
	 * @param nameCurrency
	 * @return
	 */
	public double getSumCosts(Project project, String nameCurrency) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.sum(Chargescosts.COST))
			.add(Restrictions.eq(Chargescosts.PROJECT, project))
			.createCriteria(Chargescosts.CURRENCY)
			.add(Restrictions.eq(Currency.NAME, nameCurrency));	
			
		Double cost = (Double)crit.uniqueResult();
		return (cost == null?0:cost);
	}
}

