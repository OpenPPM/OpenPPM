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
 * File: FiltroTabla.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.javabean;

import java.io.Serializable;
import java.util.List;

import es.sm2.openppm.utils.StringPool;

public class FiltroTabla implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String GREATHER_EQUAL	= "greaterEqual";
	public static final String LESS_EQUAL		= "lessEqual";
	public static final String BETWEEN			= "between";
	
	private Integer displayStart;
	private Integer displayLength;
	private List<DatoColumna> orden;
	private List<DatoColumna> filtro;
	private String first;
	private String last;
	private String filterPriority;
	
	public FiltroTabla() {
		super();
	}
	
	public FiltroTabla(Integer displayStart, Integer displayLength) {
		super();
		this.displayStart = displayStart;
		this.displayLength = displayLength;
	}


	public FiltroTabla(Integer displayStart, Integer displayLength,
			List<DatoColumna> orden) {
		super();
		this.displayStart = displayStart;
		this.displayLength = displayLength;
		this.orden = orden;
	}


	public FiltroTabla(Integer displayStart, Integer displayLength,
			List<DatoColumna> orden, List<DatoColumna> filtroOR) {
		super();
		this.displayStart = displayStart;
		this.displayLength = displayLength;
		this.filtro = filtroOR;
		this.orden = orden;
	}
	
	public FiltroTabla(String[] array) {
		super();
		
		if(array.length > 0 && !array[0].equals(StringPool.BLANK)){
			this.filterPriority = array[0];
			
			if (array.length > 1 && !array[1].equals(StringPool.BLANK)) {
				this.first = array[1];
			}
			
			if (array.length > 2 && !array[2].equals(StringPool.BLANK)) {
				this.last = array[2];
			}
		}
	}

	public Integer getDisplayStart() {
		return displayStart;
	}

	public void setDisplayStart(Integer displayStart) {
		this.displayStart = displayStart;
	}

	public Integer getDisplayLength() {
		return displayLength;
	}

	public void setDisplayLength(Integer displayLength) {
		this.displayLength = displayLength;
	}

	public List<DatoColumna> getOrden() {
		return orden;
	}

	public void setOrden(List<DatoColumna> orden) {
		this.orden = orden;
	}

	public List<DatoColumna> getFiltro() {
		return filtro;
	}

	public void setFiltro(List<DatoColumna> filtro) {
		this.filtro = filtro;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getFirst() {
		return first;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getLast() {
		return last;
	}

	public void setFilterPriority(String filterPriority) {
		this.filterPriority = filterPriority;
	}

	public String getFilterPriority() {
		return filterPriority;
	}
}
