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
 * File: ContactLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import es.sm2.openppm.core.model.impl.Jobcategory;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.Skill;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.ContactDAO;
import es.sm2.openppm.core.dao.SecurityDAO;
import es.sm2.openppm.core.exceptions.ContactNotFoundException;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Performingorg;
import es.sm2.openppm.core.model.impl.Resourceprofiles;
import es.sm2.openppm.core.model.impl.Security;
import es.sm2.openppm.utils.exceptions.LogicException;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

public class ContactLogic extends AbstractGenericLogic<Contact, Integer> {
	
	/**
	 * Save new contact 
	 * @param contact
	 * @param secutiry
	 * @return
	 * @throws Exception
	 */
	public Contact saveContact(Contact contact, Security secutiry) throws Exception {
		if (contact == null) {
			throw new ContactNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		Contact item = null;
		
		try {
			tx = session.beginTransaction();
			
			ContactDAO contactDAO = new ContactDAO(session);
			item = contactDAO.makePersistent(contact);
			
			secutiry.setContact(item);
			
			SecurityDAO securityDAO = new SecurityDAO(session);
			securityDAO.makePersistent(secutiry);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return item;
	}
	
	/**
	 * Delete Contact
	 * @param idCntact
	 * @throws Exception 
	 */
	public void deleteContact(Integer idContact) throws Exception {
		if (idContact == null || idContact == -1) {
			throw new ContactNotFoundException();
		}
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			ContactDAO contactDAO = new ContactDAO(session);
			Contact contact = contactDAO.findById(idContact, false);
			
			if (!contact.getEmployees().isEmpty()) {
				throw new LogicException("msg.error.delete.this_has","contact","maintenance.resources");
			}			
			else {
				contactDAO.makeTransient(contact);
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

	
	/**
	 * Consulting Contact with a specified id
	 * @param idContact
	 * @return
	 * @throws Exception 
	 */
	public Contact consultContact(int idContact) throws Exception {
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		Contact contact = new Contact();
		try {
			
			tx = session.beginTransaction();
			
			ContactDAO contactDAO = new ContactDAO(session);
			contact = contactDAO.findByIdContact(new Contact(idContact));
			
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
		return contact;
	}


    /**
     * Search Contacts by filter
     *
     * @param fullName
     * @param fileAs
     * @param performingorg
     * @param company
     * @param resourcepools
     * @param skills
     *@param jobcategories @return
     * @throws Exception
     */
	public List<Contact> searchContacts(String fullName, String fileAs,
                                        Performingorg performingorg, Company company, List<Resourcepool> resourcepools,
                                        List<Skill> skills, List<Jobcategory> jobcategories) throws Exception {
		
		List<Contact> contacts = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ContactDAO contactDAO = new ContactDAO(session);
			contacts = contactDAO.searchByFilter(fullName, fileAs, performingorg, company, resourcepools, skills, jobcategories);
			
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
		return contacts;
	}

	/**
	 * Find by login user name
	 * @param remoteUser
	 * @return
	 * @throws Exception
	 */
	public Contact findByUser(String remoteUser) throws Exception {
		
		Contact contact = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ContactDAO contactDAO = new ContactDAO(session);
			contact = contactDAO.findByUser(remoteUser);
			
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
		return contact;
	}
	
	/**
	 * These contact has profile in company
	 * @param perfOrg
	 * @param contact
	 * @param profile
	 * @return
	 * @throws Exception
	 */
	public boolean hasProfile(Performingorg perfOrg, Contact contact, Resourceprofiles profile) throws Exception {
		
		boolean has = false;
		
		Transaction tx = null;		
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
		
			ContactDAO contactDAO = new ContactDAO(session);
			has = contactDAO.hasProfile(perfOrg, contact, profile);
			
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
		
		return has;
	}

    /**
     * These contact has resource pool in company
     * @param contact
     * @return
     * @throws Exception
     */
    public boolean hasResourcePool(Contact contact, Resourceprofiles teamMember) throws Exception {

        boolean has = false;

        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ContactDAO contactDAO = new ContactDAO(session);
            has = contactDAO.hasResourcePool(contact, teamMember);

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

        return has;
    }


    /**
     * Find owners LLAA
     *
     * @param company
     * @return
     */
    public List<Contact> findUsedInLLAA(Company company) throws Exception {

        List<Contact> contacts = null;
        Transaction tx = null;

        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        try {
            tx = session.beginTransaction();

            ContactDAO contactDAO = new ContactDAO(session);

            contacts = contactDAO.findUsedInLLAA(company);

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

        return contacts;
    }
}
