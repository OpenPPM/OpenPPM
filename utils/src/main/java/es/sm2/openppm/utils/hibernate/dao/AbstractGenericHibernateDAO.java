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
 * File: AbstractGenericHibernateDAO.java
 * Create User: javier.hernandez
 * Create Date: 06/03/2015 14:35:37
 */

package es.sm2.openppm.utils.hibernate.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public abstract class AbstractGenericHibernateDAO<T, ID extends Serializable>
		implements GenericDAO<T, ID> {

	public static final String ASCENDENT = "asc";
	public static final String DESCENDENT = "desc";
	
	private Class<T> persistentClass;

	private Session session;

	@SuppressWarnings("unchecked")
	public AbstractGenericHibernateDAO(Session session) {
		
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.session = session;
	}
	
	public AbstractGenericHibernateDAO(Session session, Class<T> persistentClass) {
		
		this.persistentClass = persistentClass;
		this.session = session;
	}

	/********************************************/
	/*************** FIND BY ID *****************/
	/********************************************/
	
	@SuppressWarnings("unchecked")
	public T findById(ID id, boolean lock) {
		T entity;
		if (lock) {
			entity = (T) getSession().get(getPersistentClass(), id, LockMode.UPGRADE);
		}
		else {
			entity = (T) getSession().get(getPersistentClass(), id);
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	public T findById(ID id,List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.idEq(id));
		
		addJoins(crit, joins);
		
		return (T) crit.uniqueResult();
	}

	public T findById(ID id) {
		
		return (T) findById(id, null);
	}

	/********************************************/
	/**************** FIND ALL ******************/
	/********************************************/
	
	public List<T> findAll() {
		return findByCriteria();
	}
	
	public List<T> findAll(List<String> joins) {
		return findByCriteria(joins);
	}

	/********************************************/
	/*************** ROW COUNT ******************/
	/********************************************/
	
	public int rowCount(Criterion... restrictions) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.rowCount());
		
		if (restrictions != null) {
			for (Criterion restriction : restrictions) {
				crit.add(restriction);
			}
		}
		Integer count = (Integer) crit.uniqueResult();
		return (count==null?0:count);
	}
	
	public int rowCountEq(String property, Object object) {
		
		return rowCount(Restrictions.eq(property, object));
	}
	
	public int rowCountNe(String property, Object object) {
		
		return rowCount(Restrictions.ne(property, object));
	}
	
	/********************************************/
	/*************** IS IN LIST *****************/
	/********************************************/
	
	/**
	 * @Deprecated User methods with order
	 * @see {@link #isInList(String, Object[], String, String)}
	 * @see {@link #isInList(String, Object[], String, String, java.util.List)}
	 * 
	 * @param property
	 * @param list
	 * @return
	 */
	@Deprecated
	public List<T> isInList(String property, Object[] list) {
		
		return isInList(property, list, null, null, null);
	}
	
	public List<T> isInList(String property, Object[] list, String propertyOrder, String typeOrder) {
		
		return isInList(property, list, propertyOrder, typeOrder, null);
	}
	
	/**
	 * @Deprecated User methods with order
	 * @see {@link #isInList(String, Object[], String, String)}
	 * @see {@link #isInList(String, Object[], String, String, java.util.List)}
	 * 
	 * @param property
	 * @param list
	 * @param joins
	 * @return
	 */
	@Deprecated
	public List<T> isInList(String property, Object[] list, List<String> joins) {
		
		return isInList(property, list, null, null, joins);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> isInList(String property, Object[] list, String propertyOrder, String typeOrder, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.add(Restrictions.in(property, list));
			
		addJoins(crit, joins);
		addOrder(crit, propertyOrder, typeOrder);
		
		return crit.list();
	}
	
	/********************************************/
	/************* SUM PROPERTY *****************/
	/********************************************/
	
	public Object sumProperty(String property, Criterion... restrictions) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass())
			.setProjection(Projections.sum(property));
		
		if (restrictions != null) {
			for (Criterion restriction : restrictions) {
				crit.add(restriction);
			}
		}
		return crit.uniqueResult();
	}
	
	public Object sumPropertyEq(String propertySum, String propertyEq, Object object) {
		
		return sumProperty(propertySum, Restrictions.eq(propertyEq, object));
	}
	
	public Object sumPropertyNe(String propertySum, String propertyNe, Object object) {
		
		return sumProperty(propertySum, Restrictions.eq(propertyNe, object));
	}
	
	/********************************************/
	/************** FIND BY EXAMPLE *************/
	/********************************************/
	
	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, List<String> joins, String[] excludeProperty) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example = Example.create(exampleInstance);
		if (excludeProperty != null) {
			for (String exclude : excludeProperty) {
				example.excludeProperty(exclude);
			}
		}
		crit.add(example);
		
		addJoins(crit, joins);
		
		return crit.list();
	}
	
	public List<T> findByExample(T exampleInstance) {
		return findByExample(exampleInstance, null, null);
	}
	
	public List<T> findByExample(T exampleInstance, List<String> joins) {
		return findByExample(exampleInstance, joins, null);
	}
	
	public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
		return findByExample(exampleInstance, null, excludeProperty);
	}
	
	/********************************************/
	/************** FIND BY RELATION ************/
	/********************************************/
	
	/**
	 * @Deprecated  User methods with order
	 * @see {@link #findByRelation(String, Object, String, String)} 
	 * @see {@link #findByRelation(String, Object, String, String, java.util.List)}
	 * 
	 * @param property
	 * @param relation
	 * @return
	 */
	@Deprecated
	public List<T> findByRelation(String property, Object relation) {
		
		return findByRelation(property, relation, null, null, null);
	}
	
	/**
	 * @Deprecated  User methods with order
	 * @see {@link #findByRelation(String, Object, String, String)} 
	 * @see {@link #findByRelation(String, Object, String, String, java.util.List)}
	 * 
	 * @param property
	 * @param relation
	 * @param joins
	 * @return
	 */
	@Deprecated
	public List<T> findByRelation(String property, Object relation, List<String> joins) {
		
		return findByRelation(property, relation, null, null, joins);
	}
	
	public List<T> findByRelation(String property, Object relation, String propertyOrder, String typeOrder) {
		
		return findByRelation(property, relation, propertyOrder, typeOrder, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByRelation(String property, Object relation, String propertyOrder, String typeOrder, List<String> joins) {
		
		Criteria crit = getSession().createCriteria(getPersistentClass());
		
		addJoins(crit, joins);
		addOrder(crit, propertyOrder, typeOrder);
		
		crit.add(Restrictions.eq(property, relation));
		
		return crit.list();
	}
	
	/********************************************/
	/******************* OTHERS *****************/
	/********************************************/
	
	/**
	 * Save or Update
	 */
	public T makePersistent(T entity) {
		getSession().saveOrUpdate(entity);
		return entity;
	}

	/**
	 * Delete
	 */
	public void makeTransient(T entity) {
		getSession().delete(entity);
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}
	
	public void setSession(Session s) {
		this.session = s;
	}
	
	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	protected Session getSession() {
		if (session == null) {
			throw new IllegalStateException(
					"Session has not been set on DAO before usage");
		}
		return session;
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}
	
	/********************************************/
	/************** PROTECTED METHODS ***********/
	/********************************************/
	
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(List<String>joins, Criterion... criterion) {
		List<T> listado = null;
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		addJoins(crit, joins);
		listado = crit.list();
		return listado;
	}
	
	protected List<T> findByCriteria(Criterion... criterion) {
		return findByCriteria(null, criterion);
	}
	
	protected void addJoins(Criteria crit, List<String> joins) {
		if (joins != null) { for (String join : joins) { crit.setFetchMode(join, FetchMode.JOIN); } }
	}
	
	protected void addOrder(Criteria crit, String propertyOrder, String typeOrder) {
		if (propertyOrder != null && typeOrder != null) {
			if (ASCENDENT.equals(typeOrder)) { crit.addOrder(Order.asc(propertyOrder)); }
			else { crit.addOrder(Order.desc(propertyOrder));	 }
		}
	}

}