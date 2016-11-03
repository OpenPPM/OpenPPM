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
 * File: ContactDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:44
 */

package es.sm2.openppm.core.dao;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Jobcatemployee;
import es.sm2.openppm.core.model.impl.LearnedLesson;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.core.model.impl.Skill;
import es.sm2.openppm.core.model.impl.Skillsemployee;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ContactDAO extends AbstractGenericHibernateDAO<Contact, Integer> {

	public ContactDAO(Session session) {
		super(session);
	}
	
	/**
	 * Return contact with company
	 * @param contact
	 * @return
	 */
	public Contact findByIdContact(Contact contact) {
		Contact cont = null;
		if (contact.getIdContact() != null) {
			Criteria crit = getSession().createCriteria(getPersistentClass());
			crit.setFetchMode("company", FetchMode.JOIN);
			crit.add(Restrictions.eq("idContact", contact.getIdContact()));
			cont = (Contact) crit.uniqueResult();
		}
		return cont;
	}

	
	/**
	 * Search Contacts by filter
	 * @param resourcepools
     * @param fullName
     * @param fileAs
     * @param performingorg
     * @param company
     * @param skills
     *@param jobcategories @return
	 */
	@SuppressWarnings("unchecked")
	public List<Contact> searchByFilter(String fullName, String fileAs,
                                        Performingorg performingorg, Company company, List<Resourcepool> resourcepools,
                                        List<Skill> skills, List<Jobcategory> jobcategories) {
		
		Contact example = new Contact();
		example.setFileAs(fileAs);
		example.setFullName(fullName);
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
			.add(Example.create(example)
					.ignoreCase()
					.enableLike(MatchMode.ANYWHERE)
				)
			.add(Restrictions.or(
				Restrictions.isNull(Contact.DISABLE),
				Restrictions.ne(Contact.DISABLE, true))
			);

        // Company
        //
		crit.createCriteria(Contact.COMPANY)
			.add(Restrictions.idEq(company.getIdCompany()))
			.add(Restrictions.or(
					Restrictions.isNull(Company.DISABLE),
					Restrictions.ne(Company.DISABLE, true))
				);

        // Employee
        Criteria critEmployee = crit.createCriteria(Contact.EMPLOYEES, CriteriaSpecification.LEFT_JOIN);

		if (performingorg != null && performingorg.getIdPerfOrg() != null &&
                performingorg.getIdPerfOrg() != -1) {

            critEmployee.createCriteria(Employee.PERFORMINGORG)
					.add(Restrictions.idEq(performingorg.getIdPerfOrg()));
		}

        if (ValidateUtil.isNotNull(resourcepools)) {
            critEmployee.add(Restrictions.in(Employee.RESOURCEPOOL, resourcepools));
        }

        if (ValidateUtil.isNotNull(skills)) {
            critEmployee.createCriteria(Employee.SKILLSEMPLOYEES)
                    .add(Restrictions.in(Skillsemployee.SKILL, skills));
        }

        if (ValidateUtil.isNotNull(jobcategories)) {
            critEmployee.createCriteria(Employee.JOBCATEMPLOYEES)
                    .add(Restrictions.in(Jobcatemployee.JOBCATEGORY, jobcategories));
        }

        return crit.list();
	}

	/**
	 * Find by login user name
	 * @param remoteUser
	 * @return
	 */
	public Contact findByUser(String remoteUser) {
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.createCriteria(Contact.SECURITIES)
				.add(Restrictions.eq(Security.LOGIN, remoteUser));
		
		return (Contact)crit.uniqueResult();
	}
	
	/**
	 * These contact has profile in company
	 * @param perfOrg
	 * @param contact
	 * @param profile
	 * @return
	 */
	public boolean hasProfile (Performingorg perfOrg, Contact contact, Resourceprofiles profile) {
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount())
			.add(Restrictions.idEq(contact.getIdContact()));
			
		crit.createCriteria(Contact.EMPLOYEES)
			.add(Restrictions.eq(Employee.RESOURCEPROFILES, profile))
			.add(Restrictions.eq(Employee.PERFORMINGORG, perfOrg));	
		
		return ((Integer)crit.uniqueResult() > 0);
	}


    /**
     * These contact has a resource pool in company
     * @param contact
     * @return
     */
    public boolean hasResourcePool (Contact contact, Resourceprofiles teamMember) {
        Criteria crit = getSession().createCriteria(getPersistentClass())
                .setProjection(Projections.rowCount())
                .add(Restrictions.idEq(contact.getIdContact()));

        crit.createCriteria(Contact.EMPLOYEES)
                .add(Restrictions.eq(Employee.RESOURCEPROFILES, teamMember))
                .add(Restrictions.isNotNull(Employee.RESOURCEPOOL));

        return ((Integer)crit.uniqueResult() > 0);
    }

    /**
     * Find owners LLAA
     *
     * @param company
     * @return
     */
    public List<Contact> findUsedInLLAA(Company company) {

        Criteria crit = getSession().createCriteria(getPersistentClass())
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .add(Restrictions.eq(Contact.COMPANY, company))
                .add(Restrictions.or(
                    Restrictions.isNull(Contact.DISABLE),
                    Restrictions.ne(Contact.DISABLE, true))
                )
                .addOrder(Order.asc(Contact.FULLNAME));

        crit.createCriteria(Contact.LEARNEDLESSONS)
                .add(Restrictions.eq(LearnedLesson.COMPANY, company));

        return crit.list();
    }
}
