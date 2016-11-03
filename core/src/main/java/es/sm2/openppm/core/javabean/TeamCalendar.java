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
 * File: TeamCalendar.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.javabean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.sm2.openppm.core.model.impl.Employee;

public class TeamCalendar {

	private String name;
	private Integer idEmployee;
	private List<Date> work = new ArrayList<Date>();
	private List<String> values = new ArrayList<String>();
	
	public TeamCalendar() {}
	
	public TeamCalendar(Employee employee) {
		this.idEmployee	= employee.getIdEmployee();
		this.name		= employee.getContact().getFullName();
	}
	
	public void addWork(Date date) {
		work.add(date);
	}
	
	public void addValue(String value) {
		values.add(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Date> getWork() {
		return work;
	}

	public void setWork(List<Date> work) {
		this.work = work;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public List<String> getValues() {
		return values;
	}

	public void setIdEmployee(Integer idEmployee) {
		this.idEmployee = idEmployee;
	}

	public Integer getIdEmployee() {
		return idEmployee;
	}
	
	
}
