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
 * File: SkillsemployeeLogic.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:46
 */

package es.sm2.openppm.core.logic.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.sm2.openppm.core.dao.SkillsemployeeDAO;
import es.sm2.openppm.core.exceptions.EmployeeNotFoundException;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Skillsemployee;
import es.sm2.openppm.utils.hibernate.SessionFactoryUtil;
import es.sm2.openppm.utils.hibernate.logic.AbstractGenericLogic;



/**
 * Logic object for domain model class Skillsemployee
 * @see es.sm2.openppm.logic.Skillsemployee
 * @author Hibernate Generator by Javier Hernandez
 */
public class SkillsemployeeLogic extends AbstractGenericLogic<Skillsemployee, Integer>{

	
	/**
	 * Return employee's skills
	 * @param employee
	 * @return
	 * @throws Exception
	 */
	public List<Skillsemployee> findByEmployee(Employee employee) throws Exception {
		if (employee == null) {
			throw new EmployeeNotFoundException();
		}
		
		List<Skillsemployee> skills = null;
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			SkillsemployeeDAO SkillsemployeeDAO = new SkillsemployeeDAO(session);
			skills = SkillsemployeeDAO.findByEmployee(employee);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			throw e;
		}
		finally {
			SessionFactoryUtil.getInstance().close();
		}
		return skills;
	}
}