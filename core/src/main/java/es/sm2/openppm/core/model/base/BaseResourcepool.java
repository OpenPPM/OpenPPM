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
 * File: BaseResourcepool.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Managepool;
import es.sm2.openppm.core.model.impl.Resourcepool;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Resourcepool
 * @see es.sm2.openppm.core.model.base.Resourcepool
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Resourcepool
 */
public class BaseResourcepool  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "resourcepool";
	
	public static final String IDRESOURCEPOOL = "idResourcepool";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String EMPLOYEES = "employees";
	public static final String MANAGEPOOLS = "managepools";

     private Integer idResourcepool;
     private Company company;
     private String name;
     private String description;
     private Set<Employee> employees = new HashSet<Employee>(0);
     private Set<Managepool> managepools = new HashSet<Managepool>(0);

    public BaseResourcepool() {
    }
    
    public BaseResourcepool(Integer idResourcepool) {
    	this.idResourcepool = idResourcepool;
    }
   
    public Integer getIdResourcepool() {
        return this.idResourcepool;
    }
    
    public void setIdResourcepool(Integer idResourcepool) {
        this.idResourcepool = idResourcepool;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<Employee> getEmployees() {
        return this.employees;
    }
    
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
    public Set<Managepool> getManagepools() {
        return this.managepools;
    }
    
    public void setManagepools(Set<Managepool> managepools) {
        this.managepools = managepools;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Resourcepool) )  { result = false; }
		 else if (other != null) {
		 	Resourcepool castOther = (Resourcepool) other;
			if (castOther.getIdResourcepool().equals(this.getIdResourcepool())) { result = true; }
         }
		 return result;
   }


}


