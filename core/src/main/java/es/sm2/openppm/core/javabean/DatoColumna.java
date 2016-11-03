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
 * File: DatoColumna.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.javabean;

import java.io.Serializable;

public class DatoColumna implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String nombre;
	private String[] nombreList;
	@SuppressWarnings("rawtypes")
	private Class tipo;
	@SuppressWarnings("rawtypes")
	private Class subTipo;
	private String valor;
	private String[] valorList;
	private Object object;
	private Object[] objectList;
	private String criteria;
	

	public DatoColumna() {
		super();
	}
	
	@SuppressWarnings("rawtypes")
	public DatoColumna(String nombre, Class tipo) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
	}
	
	@SuppressWarnings("rawtypes")
	public DatoColumna(String nombre, Class tipo, Class subTipo) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.subTipo = subTipo;
	}
	
	@SuppressWarnings("rawtypes")
	public DatoColumna(String nombre, Class tipo, Class subTipo, String criteria) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.subTipo = subTipo;
		this.criteria = criteria;
	}
	
	@SuppressWarnings("rawtypes")
	public DatoColumna(String nombre, Class tipo, String valor) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.valor = valor;
	}
	
	@SuppressWarnings("rawtypes")
	public DatoColumna(String nombre, Class tipo, String[] valorList) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.valorList = valorList;
	}
	
	@SuppressWarnings("rawtypes")
	public DatoColumna(String nombre, Class tipo, Object object) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.object = object;
	}
	
	@SuppressWarnings("rawtypes")
	public DatoColumna(String nombre, Class tipo, Object object, String criteria) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.object = object;
		this.criteria = criteria;
	}
	
	@SuppressWarnings("rawtypes")
	public DatoColumna(String[] nombreList, Class tipo, Class subTipo) {
		super();
		this.nombreList = nombreList;
		this.tipo = tipo;
		this.subTipo = subTipo;
	}
	
	@SuppressWarnings("rawtypes")
	public DatoColumna(String[] nombreList, Class tipo, Class subTipo, Object[] objectList) {
		super();
		this.nombreList = nombreList;
		this.tipo = tipo;
		this.subTipo = subTipo;
		this.objectList = objectList;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@SuppressWarnings("rawtypes")
	public Class getTipo() {
		return tipo;
	}

	@SuppressWarnings("rawtypes")
	public void setTipo(Class tipo) {
		this.tipo = tipo;
	}
	
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public void setValorList(String[] valorList) {
		this.valorList = valorList;
	}

	public String[] getValorList() {
		return valorList;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}
	public String[] getNombreList() {
		return nombreList;
	}

	public void setNombreList(String[] nombreList) {
		this.nombreList = nombreList;
	}

	@SuppressWarnings("rawtypes")
	public Class getSubTipo() {
		return subTipo;
	}

	public void setSubTipo(@SuppressWarnings("rawtypes") Class subTipo) {
		this.subTipo = subTipo;
	}

	public Object[] getObjectList() {
		return objectList;
	}

	public void setObjectList(Object[] objectList) {
		this.objectList = objectList;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}
}
