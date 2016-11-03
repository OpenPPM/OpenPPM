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
 * File: ProjectInfoJavaBean.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:00
 */

package es.sm2.openppm.core.javabean;

import java.util.List;

import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Contact;
import es.sm2.openppm.core.model.impl.Contracttype;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Program;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectcharter;

public class ProjectInfoJavaBean {
	
	private Project project;
	private Projectcharter projectCharter;
	private List<Employee> employees;
	private List<Contact> contacts;
	private List<Program> programs;
	private List<Contracttype> contractTypes;
	private List<Company> companies;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Projectcharter getProjectCharter() {
		return projectCharter;
	}

	public void setProjectCharter(Projectcharter projectCharter) {
		this.projectCharter = projectCharter;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public List<Program> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Program> programs) {
		this.programs = programs;
	}

	public List<Contracttype> getContractTypes() {
		return contractTypes;
	}

	public void setContractTypes(List<Contracttype> contractTypes) {
		this.contractTypes = contractTypes;
	}

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}
}

