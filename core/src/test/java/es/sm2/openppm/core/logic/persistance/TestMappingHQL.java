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
 * File: TestMappingHQL.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:53:04
 */
package es.sm2.openppm.core.logic.persistance;

import es.sm2.openppm.core.logic.persistance.hql.employee.EmployeeNamedQuery;
import es.sm2.openppm.core.logic.persistance.hql.employee.EmployeeNamedRestriction;
import es.sm2.openppm.core.logic.persistance.hql.generic.GenericNamedOrder;
import es.sm2.openppm.core.model.impl.Employee;
import org.junit.Test;

import es.sm2.openppm.core.dao.ProjectDAO.ProjectNamedOrder;
import es.sm2.openppm.core.dao.ProjectDAO.ProjectNamedQuery;
import es.sm2.openppm.core.dao.ProjectDAO.ProjectNamedRestriction;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.utils.LogManager;

/**
 * @author javier.hernandez
 *
 */
public class TestMappingHQL {

	@Test
	public void testProjectHQL() {
		
		// Read HQL Project
		MappingHql mappingHql = new MappingHql(Project.ENTITY);

		// Create test query
		String statement = mappingHql.setNamedQuery(ProjectNamedQuery.FIND_BY_ID)
				.putRestrictions(ProjectNamedRestriction.BY_PROJECT, ProjectNamedRestriction.BY_PM)
				.putOrders(ProjectNamedOrder.BY_NAME_DESC)
				.create();
		
		// Print to log
		LogManager.getConsoleLog(getClass()).debug(statement);
	}

    @Test
    public void testEmployeeHQL() {

        // Read HQL Project
        MappingHql mappingHql = new MappingHql(Employee.ENTITY);

        // Create test query
        String statement = mappingHql.setNamedQuery(EmployeeNamedQuery.FIND_UNSIGNED)
                .putRestrictions(EmployeeNamedRestriction.EXCLUDE_IN_DATES, EmployeeNamedRestriction.LIKE_NAME,
                        EmployeeNamedRestriction.IN_POOLS, EmployeeNamedRestriction.IN_CATEGORIES)
                .putSubSelect(":CATEGORIES", "Select id from Subselect")
                .putOrders(GenericNamedOrder.BY_CONTACT_NAME).create();


        // Print to log
        LogManager.getConsoleLog(getClass()).debug(statement);
    }
}
