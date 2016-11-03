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
 * File: ProcurementpaymentsDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import es.sm2.openppm.core.javabean.ProcurementBudget;
import es.sm2.openppm.core.model.impl.Procurementpayments;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Seller;
import es.sm2.openppm.core.model.wrap.RangeDateWrap;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

/**
 * DAO object for domain model class Procurementpayments
 * @see es.sm2.openppm.core.dao.Procurementpayments
 * @author Hibernate Generator by Javier Hernandez
 */
public class ProcurementpaymentsDAO extends AbstractGenericHibernateDAO<Procurementpayments, Integer> {


	public ProcurementpaymentsDAO(Session session) {
		super(session);
	}

	/**
	 * Get Procurement Payments from Project
	 * @param proj
	 * @param joins
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Procurementpayments> consProcurementPaymentsByProject(Project proj, List<String> joins) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		for (String join : joins) {			
			crit.setFetchMode(join, FetchMode.JOIN);
		}
		
		crit.addOrder(Order.asc(Procurementpayments.SELLER))
			.add(Restrictions.eq(Procurementpayments.PROJECT, proj));
		
		return crit.list();
	}
	
	/**
	 * 
	 * @param seller
	 * @return
	 */
	public List<String> consPruchaseOrder(Seller seller, Project project) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
						
		crit.addOrder(Order.asc(Procurementpayments.SELLER))
			.add(Restrictions.eq(Procurementpayments.SELLER, seller))
			.add(Restrictions.eq(Procurementpayments.PROJECT, project));
		
		List<String> lista = new ArrayList<String>();
		
		for(int i = 0; i < crit.list().size(); i++) {
			Procurementpayments proc = (Procurementpayments)crit.list().get(i);
			lista.add(proc.getPurchaseOrder());			
		}
		
		return lista;
	}
	
	/**
	 * Get Procurement Budgets from Project
	 * 
	 * @param proj
	 * @param joins
	 * @return
	 */
	public List<ProcurementBudget> consProcurementBudgetsByProject(Project proj, List<String> joins) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		for (String join : joins) {			
			crit.setFetchMode(join, FetchMode.JOIN);
		}
		
		crit.addOrder(Order.asc(Procurementpayments.SELLER))
			.setProjection(Projections.projectionList()					
					.add(Projections.sum(Procurementpayments.PLANNEDPAYMENT))
					.add(Projections.sum(Procurementpayments.ACTUALPAYMENT))
					.add(Projections.rowCount())					
					.add(Projections.groupProperty(Procurementpayments.SELLER)))
			.add(Restrictions.eq(Procurementpayments.PROJECT, proj));
		
		List<ProcurementBudget> lista = new ArrayList<ProcurementBudget>();
		List<String> purchase = null;
		
		for(int i = 0; i < crit.list().size(); i++) {
			ProcurementBudget budget = new ProcurementBudget();
			Object[] row = (Object[])crit.list().get(i);
			budget.setPlannedPayment((Double)row[0]);
			budget.setActualPayment((Double)row[1]);
			budget.setnPayments((Integer)row[2]);
			
			Seller seller = (Seller)row[3];
			budget.setSeller(seller.getName());
			
			purchase = consPruchaseOrder(seller, proj);			
			String order = "";
			if(purchase != null) {				
				for (String s : purchase) {
					if(!order.equals("")) {
						order += ", ";
					}
					order += s;
				}
			}
			budget.setPurchaseOrder(order);						
			
			lista.add(budget);
		}
		
		return lista;
	}

	
	/**
	 * Find min and max actual dates of projects
	 * 
	 * @param projects
	 * @return
	 */
	public RangeDateWrap findMinAndMaxActualDatesByProjects(List<Project> projects) {
		
		RangeDateWrap range = null;
		
		if (ValidateUtil.isNotNull(projects)) {
			
			Criteria crit = getSession().createCriteria(getPersistentClass())
				.add(Restrictions.in(Procurementpayments.PROJECT, projects))
				.setProjection(Projections.projectionList()
					.add(Projections.min(Procurementpayments.ACTUALDATE), RangeDateWrap.Fields.MINDATE.toString())
					.add(Projections.max(Procurementpayments.ACTUALDATE), RangeDateWrap.Fields.MAXDATE.toString()))
				.setResultTransformer(Transformers.aliasToBean(RangeDateWrap.class));
			
			range = (RangeDateWrap) crit.uniqueResult();
		}
			
		return range;
	}

}

