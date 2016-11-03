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
 * File: DataTableDAO.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import es.sm2.openppm.core.javabean.DatoColumna;
import es.sm2.openppm.core.javabean.FiltroTabla;
import es.sm2.openppm.utils.functions.ValidateUtil;
import es.sm2.openppm.utils.hibernate.dao.AbstractGenericHibernateDAO;



/**
 * 
 * @author Hibernate Tools - Template by Javier Hernandez
 */
public class DataTableDAO extends AbstractGenericHibernateDAO<Object, Integer> {


	public DataTableDAO(Session session) {
		super(session);
	}
	
	/**
	 * Buscar con por ejemplo un listado de personas
	 * @param example
	 * @param tipo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findByExample(Object example, Class tipo) {
		
		Criteria crit = getSession().createCriteria(tipo)
			.add(
				Example.create(example)
					.enableLike(MatchMode.ANYWHERE)
					.excludeZeroes()
			);
		
		return crit.list();
	}
	
	@SuppressWarnings({ "rawtypes" })
	public List findByFiltro(FiltroTabla filtro,Class tipo, ArrayList<String> joins) throws ParseException {

		Criteria crit = getSession().createCriteria(tipo)
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
			.setFirstResult(filtro.getDisplayStart())
			.setMaxResults(filtro.getDisplayLength());
		
		for (DatoColumna order : filtro.getOrden()) {
			crit.addOrder((order.getValor().equals("asc")?Order.asc(order.getNombre()):Order.desc(order.getNombre())));
		}
		
		applyFilters(crit, filtro.getFiltro());
		
		if (joins != null) {
			for (String join : joins) {
				crit.setFetchMode(join, FetchMode.JOIN);
			}
		}
		
		return crit.list();
	}

	@SuppressWarnings("rawtypes")
	public int findTotal(List<DatoColumna> filtrosExtras, Class tipo) throws ParseException {

		Criteria crit = getSession().createCriteria(tipo).setProjection(Projections.rowCount());
		applyFilters(crit, filtrosExtras);
		
		return ((Integer)crit.list().get(0)).intValue();
	}

	@SuppressWarnings("rawtypes")
	public int findTotalFiltered(FiltroTabla filtro, Class tipo) throws ParseException {
		
		Criteria crit = getSession().createCriteria(tipo);

		applyFilters(crit, filtro.getFiltro());
		
		crit.setProjection(Projections.rowCount());
			
		return ((Integer)crit.list().get(0)).intValue();
	}

	private void applyFilters(Criteria crit, List<DatoColumna> filtrosExtras) throws ParseException {
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		for (DatoColumna filtroColumna : filtrosExtras) {
			
			if (filtroColumna.getTipo() == Date.class) {
				
				// Filtramos si es una fecha
				Date tempDate = df.parse(filtroColumna.getValor());
				crit.add(Restrictions.eq(filtroColumna.getNombre(),tempDate));
			}
			else if (filtroColumna.getTipo() == Integer.class) {
				
				// Filtramos si es un numero
				Integer integer = Integer.parseInt(filtroColumna.getValor());
				crit.add(Restrictions.eq(filtroColumna.getNombre(),integer));
			}
			else if (filtroColumna.getTipo() == Boolean.class) {
				
				// Filtramos si es un booleano
				Boolean bool = Boolean.parseBoolean(filtroColumna.getValor());
				crit.add(Restrictions.eq(filtroColumna.getNombre(),bool));
			}
			else if (filtroColumna.getTipo() == List.class) {
				
				// Since Until
				if (filtroColumna.getSubTipo() != null && filtroColumna.getSubTipo() == Date.class) {
					
					Date sinceDate = (Date) filtroColumna.getObjectList()[0];
					Date untilDate = (Date) filtroColumna.getObjectList()[1];

					String sinceName = (String) filtroColumna.getNombreList()[0];
					String untilName = (String) filtroColumna.getNombreList()[1];
					
					crit.add(Restrictions.disjunction()
							.add(Restrictions.between(sinceName, sinceDate, untilDate))
							.add(Restrictions.between(untilName, sinceDate, untilDate))
							.add(Restrictions.and(
										Restrictions.le(sinceName, sinceDate),
										Restrictions.ge(untilName, untilDate)
									)
								)
							);
				}
				else {
					// Tiene que estar en la lista
					crit.add(Restrictions.in(filtroColumna.getNombre(),filtroColumna.getValorList()));
				}
			}
			else if (filtroColumna.getTipo() == String.class) {
				
				if (filtroColumna.getSubTipo() == List.class) {
					
					if (!ValidateUtil.isNull(filtroColumna.getValor())) {
						String[] value = filtroColumna.getValor().split(",");
						List<Integer> ids = new ArrayList<Integer>();
						
						for (String id : value) { ids.add(Integer.parseInt(id)); }
						if (filtroColumna.getCriteria() != null) {
							Criteria crit2 = crit.createCriteria(filtroColumna.getCriteria());
							crit2.add(Restrictions.in(filtroColumna.getNombre(),ids));
						}
						else {
							crit.add(Restrictions.in(filtroColumna.getNombre(),ids));
						}
					}
				}
				else {
					// Comparando texto
					crit.add(Restrictions.like(filtroColumna.getNombre(),"%"+filtroColumna.getValor()+"%"));
				}
			}
			else {
				
				// Filtrando Objectos
				crit.add(Restrictions.eq(filtroColumna.getNombre(),filtroColumna.getObject()));
			}
		}
	}


}

