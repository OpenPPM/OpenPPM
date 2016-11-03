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
 * File: ContentFileDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.Documentation;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * DAO object for domain model class File
 * @see es.sm2.openppm.core.dao.File
 * @author Hibernate Generator by Javier Hernandez
 */
public class ContentFileDAO extends AbstractGenericHibernateDAO<Contentfile, Integer> {


	public ContentFileDAO(Session session) {
		super(session);
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 */
	public Contentfile findByDocumentation(Documentation doc) {

		Contentfile content = null;

		Criteria crit = getSession().createCriteria(getPersistentClass())
			.createCriteria(Contentfile.DOCUMENTATIONS)
			    .add(Restrictions.eq(Documentation.CONTENTFILE, doc.getContentfile()));

		content = (Contentfile) crit.uniqueResult();

		return content;
	}

	/**
	 * Find by entity and ID
	 * 
	 * @param entity
	 * @param id
	 * @return
	 */
	public Contentfile findByEntity(String entity, Integer id) {
		
		Contentfile content = null;
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.createCriteria(entity)
			.add(Restrictions.idEq(id));

		content = (Contentfile) crit.uniqueResult();
		
		return content;
	}
}

