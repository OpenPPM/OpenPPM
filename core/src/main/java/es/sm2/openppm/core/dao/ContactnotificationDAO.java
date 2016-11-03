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
 * File: ContactnotificationDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import es.sm2.openppm.core.model.search.NotificationSearch;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contactnotification;
import es.sm2.openppm.core.model.impl.Notification;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Contactnotification
 * @see es.sm2.openppm.core.dao.Contactnotification
 * @author Hibernate Generator by Javier Hernandez
 */
public class ContactnotificationDAO extends AbstractGenericHibernateDAO<Contactnotification, Integer> {


	public ContactnotificationDAO(Session session) {
		super(session);
	}

	/**
	 * Find notifications by contact
	 * 
	 * @param company
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Contactnotification> findByContact(Contact contact) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		if (contact != null && contact.getCompany() != null) {
			
			// Filter by contact and company
			crit.createCriteria(Contactnotification.CONTACT)
					.add(Restrictions.eq(Contact.COMPANY, contact.getCompany()))
					.add(Restrictions.eq(Contact.IDCONTACT, contact.getIdContact()));
			
			// Join notification and orders
			//
			crit.createCriteria(Contactnotification.NOTIFICATION)
					.addOrder(Order.desc(Notification.CREATIONDATE));
			
			crit.addOrder(Order.desc(Contactnotification.READNOTIFY));
			
			// Max results
			crit.setMaxResults(50);
		}
		
		return crit.list();
	}

    /**
     * Find notifications
     *
     * @param notificationSearch
     * @return
     */
    public List<Contactnotification> find(NotificationSearch notificationSearch) {

		Criteria crit = getSession().createCriteria(getPersistentClass());

        // TODO javier.hernandez - 14/09/2015 - implement filters

        // Filter by contact and company
        crit.createCriteria(Contactnotification.CONTACT)
                .add(Restrictions.eq(Contact.IDCONTACT, notificationSearch.getContact().getIdContact()));

        // Join notification and orders
        //
        crit.createCriteria(Contactnotification.NOTIFICATION)
                .addOrder(Order.desc(Notification.CREATIONDATE));

        crit.addOrder(Order.desc(Contactnotification.READNOTIFY));

        // Max results
        crit.setMaxResults(50);

		return crit.list();
	}
}

