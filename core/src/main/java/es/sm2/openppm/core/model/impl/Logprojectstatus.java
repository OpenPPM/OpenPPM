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
 * File: Logprojectstatus.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:57
 */

package es.sm2.openppm.core.model.impl;

import java.util.Date;

import es.sm2.openppm.core.model.base.BaseLogprojectstatus;



/**
 * Model class Logprojectstatus.
 * @author Hibernate Generator by Javier Hernandez
 */
public class Logprojectstatus extends BaseLogprojectstatus {

	private static final long serialVersionUID = 1L;

    public Logprojectstatus() {
		super();
    }
    public Logprojectstatus(Integer idLogProjectStatus) {
		super(idLogProjectStatus);
    }
    
    public Logprojectstatus(Project project, Employee employee, String projectStatus, String investmentStatus, Date logDate) {
		super();
		this.setProject(project);
		this.setEmployee(employee);
		this.setProjectStatus(projectStatus);
		this.setInvestmentStatus(investmentStatus);
		this.setLogDate(logDate);
    }
}

