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
 * File: ContentFileLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.ContentFileDAO;
import es.sm2.openppm.core.logic.exceptions.FileNotFoundException;
import es.sm2.openppm.core.model.impl.Contentfile;
import es.sm2.openppm.core.model.impl.Documentation;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;

/**
 * Logic object for domain model class File
 * @see es.sm2.openppm.logic.File
 * @author Hibernate Generator by Javier Hernandez
 */
public class ContentFileLogic extends AbstractGenericLogic<Contentfile, Integer>{
	
	/**
	 * Find the content file from documentation
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public Contentfile findByDocumentation (Documentation doc) throws Exception {
		
		Contentfile content = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			ContentFileDAO fileDAO = new ContentFileDAO(session);
			content = fileDAO.findByDocumentation(doc);
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
		return content;
	}

	/**
	 * Find by entity and ID
	 * 
	 * @param entity
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public Contentfile findByEntity(String entity, Integer id) throws Exception {
		
		Contentfile content = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			ContentFileDAO fileDAO = new ContentFileDAO(session);
			
			content = fileDAO.findByEntity(entity, id);
			
			if (content == null) {
				throw new FileNotFoundException();
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
		return content;
	}

}

