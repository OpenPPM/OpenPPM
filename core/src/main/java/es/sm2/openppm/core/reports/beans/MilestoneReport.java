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
 * File: MilestoneReport.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.reports.beans;

public class MilestoneReport {

	private String nombre;
	private String fecha;
	private String realDate;

	public MilestoneReport(String nombre, String fecha) {
		this.setNombre(nombre);
		this.setFecha(fecha);
	}

    public MilestoneReport(String nombre, String fecha, String realDate) {
		this.setNombre(nombre);
		this.setFecha(fecha);
		this.setRealDate(realDate);
	}

    /**
     * Getter for property 'realDate'.
     *
     * @return Value for property 'realDate'.
     */
    public String getRealDate() {

        return realDate;
    }

    /**
     * Setter for property 'realDate'.
     *
     * @param realDate Value to set for property 'realDate'.
     */
    public void setRealDate(String realDate) {

        this.realDate = realDate;
    }

    /**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
}
