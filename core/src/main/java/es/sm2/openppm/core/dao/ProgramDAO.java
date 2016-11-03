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
 * File: ProgramDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:44
 */

package es.sm2.openppm.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.common.Constants;
import es.sm2.openppm.core.javabean.PropertyRelation;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;

public class ProgramDAO extends AbstractGenericHibernateDAO<Program, Integer> {

	public ProgramDAO(Session session) {
		super(session);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Program> searchByExample(Program exampleInstance,
			Class... joins) {
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.addOrder(Order.asc(Program.PROGRAMNAME));
		
		for (Class c : joins) {
			if (c.equals(Contact.class)) {
				crit.setFetchMode("employee", FetchMode.JOIN);
				crit.setFetchMode("employee.contact", FetchMode.JOIN);
			}
		}
		
		if (exampleInstance.getEmployee() != null) {// Find by Program Mgr.
			crit.add(Restrictions.eq("employee.idEmployee", exampleInstance.getEmployee().getIdEmployee()));
		}		
		
		return crit.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Program> findByPO(Employee user) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.addOrder(Order.asc(Program.PROGRAMNAME))
			.setFetchMode("employee", FetchMode.JOIN)
			.setFetchMode("employee.contact", FetchMode.JOIN)
			.add(Restrictions.eq(Program.PERFORMINGORG, user.getPerformingorg()));
		
		return crit.list();
	}


	/**
	 * Search by PerfOrg
	 * @param budgetYear 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Program> searchByPerfOrg(Performingorg perfOrg, Integer budgetYear) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.addOrder(Order.asc(Program.PROGRAMNAME));
		
		if (budgetYear != null) {
			crit.add(Restrictions.disjunction()
					.add(Restrictions.eq(Program.INITBUDGETYEAR, budgetYear+""))
					.add(Restrictions.isNull(Program.INITBUDGETYEAR))
					.add(Restrictions.eq(Program.INITBUDGETYEAR, ""))
			);
		}
		
		crit.createCriteria("employee")
			.add(Restrictions.eq("performingorg",perfOrg));
		
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Program> consByCompany(Company company, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.addOrder(Order.asc(Program.PROGRAMNAME));
		
		if (joins != null) {
			for (String join : joins) {
				crit.setFetchMode(join, FetchMode.JOIN);
			}
		}
		crit.createCriteria(Program.PERFORMINGORG)
			.add(Restrictions.eq(Performingorg.COMPANY, company));
		
		return crit.list();
	}

	/**
	 * Search program by name and PO
	 * @param programName
	 * @param performingorg
	 * @return
	 */
	public Program findByNameProgramAndPO(String programName, Performingorg performingorg) {

		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		crit.add(Restrictions.eq(Program.PROGRAMNAME, programName))
			.add(Restrictions.eq(Employee.PERFORMINGORG, performingorg));
		
		return (Program) crit.uniqueResult();
	}

	/**
	 * Search programs by filter
	 * @param name
	 * @param performingorg
	 * @param company 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Program> searchPrograms(String name, Performingorg performingorg, Company company) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		// Filter program name
		crit.add(Restrictions.ilike(Program.PROGRAMNAME, "%"+name+"%"));
		
		// Join performing org
		Criteria critPerfOrg = crit.createCriteria(Program.PERFORMINGORG);
		
		if (performingorg.getIdPerfOrg() != -1) {
			// Filter performing org
			critPerfOrg.add(Restrictions.eq(Performingorg.IDPERFORG, performingorg.getIdPerfOrg()));
		}
		else {
			// Filter by company
			critPerfOrg.add(Restrictions.eq(Performingorg.COMPANY, company));
		}
		
		return crit.list();
	}

	/**
	 * Search programs by filters
	 * @param list
	 * @param properyOrder
	 * @param typeOrder
	 * @param joins 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Program> findByFilters(ArrayList<PropertyRelation> list, String propertyOrder, String typeOrder, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		if (joins != null) {
			for (String join : joins) {
				crit.setFetchMode(join, FetchMode.JOIN);
			}
		}
		
		for(PropertyRelation propertyRelation : list){
			crit.add(findRestriction(propertyRelation));
		}
		
		addOrder(crit, propertyOrder, typeOrder);
		
		return crit.list();
	}

	/**
	 * Find restriction type
	 * @param propertyRelation
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Criterion findRestriction(PropertyRelation propertyRelation) {
		
		Criterion criterion = null;
		
		if(propertyRelation.getRestriction() == Constants.EQUAL_RESTRICTION){
			criterion = Restrictions.eq(propertyRelation.getProperty(), propertyRelation.getRelation());
		}
		else if(propertyRelation.getRestriction() == Constants.NOT_EQUAL_RESTRICTION){
			criterion = Restrictions.ne(propertyRelation.getProperty(), propertyRelation.getRelation());
		}
		else if(propertyRelation.getRestriction() == Constants.GREATER_OR_EQUAL_RESTRICTION){
			criterion = Restrictions.ge(propertyRelation.getProperty(), propertyRelation.getRelation());
		}
		else if(propertyRelation.getRestriction() == Constants.LESS_OR_EQUAL_RESTRICTION){
			criterion = Restrictions.le(propertyRelation.getProperty(), propertyRelation.getRelation());
		}
		else if(propertyRelation.getRestriction() == Constants.ILIKE_RESTRICTION){
			criterion = Restrictions.ilike(propertyRelation.getProperty(), propertyRelation.getRelation());
		}
		else if(propertyRelation.getRestriction() == Constants.DISJUNCTION){
			ArrayList<PropertyRelation> listDisjunctions = (ArrayList<PropertyRelation>) propertyRelation.getRelation();
			
			Disjunction d = Restrictions.disjunction();
			
			for(PropertyRelation propertyRelationDis : listDisjunctions){
				d.add(findRestriction(propertyRelationDis));
			}
			
			criterion = d;
		}
		
		return criterion;
	}
	
}