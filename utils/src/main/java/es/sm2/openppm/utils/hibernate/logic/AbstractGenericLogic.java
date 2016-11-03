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
 * Module: utils
 * File: AbstractGenericLogic.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.hibernate.logic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;

import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.dao.GenDAO;
import es.sm2.openppm.utils.json.JsonUtil;

public abstract class AbstractGenericLogic<T, ID extends Serializable> extends JsonUtil  {

	private Class<T> persistentClass;
	private ResourceBundle bundle;
	
	public AbstractGenericLogic(ResourceBundle bundle) {
		this();
		this.setBundle(bundle);
	}
	
	@SuppressWarnings("unchecked")
	public AbstractGenericLogic() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/********************************************/
	/*************** FIND BY ID *****************/
	/********************************************/
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T findById(ID id, boolean lock) throws Exception {
		
		T item = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			item = (T) genDAO.findById(id, lock);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return item;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T findById(ID id,List<String> joins) throws Exception {
		
		T item = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			item = (T) genDAO.findById(id, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return item;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T findById(ID id) throws Exception {
		
		T item = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			item = (T) genDAO.findById(id);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return item;
	}
	
	/********************************************/
	/**************** FIND ALL ******************/
	/********************************************/
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAll() throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.findAll();
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAll(List<String> joins) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.findAll(joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	/********************************************/
	/*************** ROW COUNT ******************/
	/********************************************/
	
	@SuppressWarnings("rawtypes")
	public int rowCount(Criterion... restrictions) throws Exception {
		
		int count = 0;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			count = genDAO.rowCount(restrictions);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return count;
	}
	
	@SuppressWarnings("rawtypes")
	public int rowCountEq(String property, Object object) throws Exception {
		
		int count = 0;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			count = genDAO.rowCountEq(property, object);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return count;
	}
	
	@SuppressWarnings("rawtypes")
	public int rowCountNe(String property, Object object) throws Exception {
		
		int count = 0;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			count = genDAO.rowCountNe(property, object);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return count;
	}
	
	/********************************************/
	/*************** IS IN LIST *****************/
	/**
	 * @throws Exception ******************************************/
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> isInList(String property, Object[] list) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.isInList(property, list);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> isInList(String property, Object[] list, String propertyOrder, String typeOrder) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.isInList(property, list, propertyOrder, typeOrder);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> isInList(String property, Object[] list, List<String> joins) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.isInList(property, list, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> isInList(String property, Object[] list, String propertyOrder, String typeOrder, List<String> joins) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.isInList(property, list, propertyOrder, typeOrder, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	/********************************************/
	/************* SUM PROPERTY *****************/
	/********************************************/
	
	@SuppressWarnings("rawtypes")
	public Object sumProperty(String property, Criterion... restrictions) throws Exception {
		
		Object result	= null;
		Transaction tx	= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			result = genDAO.sumProperty(property, restrictions);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public Object sumPropertyEq(String propertySum, String propertyEq, Object object) throws Exception {
		
		Object result	= null;
		Transaction tx	= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			result = genDAO.sumPropertyEq(propertySum, propertyEq, object);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public Object sumPropertyNe(String propertySum, String propertyNe, Object object) throws Exception {
		
		Object result	= null;
		Transaction tx	= null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			result = genDAO.sumPropertyNe(propertySum, propertyNe, object);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return result;
	}

	/********************************************/
	/************** FIND BY EXAMPLE *************/
	/********************************************/
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByExample(T exampleInstance, List<String> joins, String[] excludeProperty) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.findByExample(exampleInstance, joins, excludeProperty);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByExample(T exampleInstance) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.findByExample(exampleInstance);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByExample(T exampleInstance, List<String> joins) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.findByExample(exampleInstance, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByExample(T exampleInstance, String[] excludeProperty) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.findByExample(exampleInstance, excludeProperty);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	/********************************************/
	/************** FIND BY RELATION ************/
	/********************************************/
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByRelation(String property, Object relation) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.findByRelation(property, relation);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByRelation(String property, Object relation, String properyOrder, String typeOrder) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.findByRelation(property, relation, properyOrder, typeOrder);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByRelation(String property, Object relation, List<String> joins) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.findByRelation(property, relation, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findByRelation(String property, Object relation, String properyOrder, String typeOrder, List<String> joins) throws Exception {
		
		List<T> items = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			items = (List<T>) genDAO.findByRelation(property, relation, properyOrder, typeOrder, joins);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T save(T entity) throws Exception {
		
		T item = null;
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			item = (T) genDAO.makePersistent(entity);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
		
		return item;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void delete(T entity) throws Exception {
		
		Transaction tx = null;
		
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		try {
			tx = session.beginTransaction();
			
			GenDAO genDAO = new GenDAO<T, ID>(session, persistentClass);
			genDAO.makeTransient(entity);
			
			tx.commit();
		}
		catch (Exception e) { if (tx != null) { tx.rollback(); } throw e; }
		finally { SessionFactoryUtil.getInstance().close(); }
	}
	
	/**
	 * @return the bundle
	 */
	protected ResourceBundle getBundle() {
		return bundle;
	}

	/**
	 * @param bundle the bundle to set
	 */
	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}
}