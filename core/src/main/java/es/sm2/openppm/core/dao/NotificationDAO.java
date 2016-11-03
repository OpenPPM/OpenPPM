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
 * File: NotificationDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contactnotification;
import es.sm2.openppm.core.model.impl.Notification;
import es.sm2.openppm.core.model.impl.Notification.NotificationStatus;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class Notification
 * @see es.sm2.openppm.core.model.impl.Notification
 * @author Hibernate Generator by Javier Hernandez
 */
public class NotificationDAO extends AbstractGenericHibernateDAO<Notification, Integer> {


	public NotificationDAO(Session session) {
		super(session);
	}
	
	/**
	 * Find notifications pendings
	 * 
	 * @param company
	 * @param notificationsForNotify
     * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Notification> findPendings(Company company, List<String> notificationsForNotify) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		crit.add(Restrictions.eq(Notification.STATUS, NotificationStatus.PENDING.toString()));
		crit.add(Restrictions.in(Notification.TYPE, notificationsForNotify));

		Criteria contactNotCrit = crit.createCriteria(Notification.CONTACTNOTIFICATIONS);
		
		contactNotCrit.createCriteria(Contactnotification.CONTACT)
						.add(Restrictions.eq(Contact.COMPANY, company));
		
		return crit.list();
	}

}

