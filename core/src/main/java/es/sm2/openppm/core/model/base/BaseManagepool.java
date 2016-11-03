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
 * File: BaseManagepool.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Employee;
import es.sm2.openppm.core.model.impl.Managepool;
import es.sm2.openppm.core.model.impl.Resourcepool;


/**
 * Base Pojo object for domain model class Managepool
 * @see BaseManagepool
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Managepool
 */
public class BaseManagepool  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "managepool";
	
	public static final String IDMANAGEPOOL = "idManagePool";
	public static final String EMPLOYEE = "employee";
	public static final String RESOURCEPOOL = "resourcepool";

     private Integer idManagePool;
     private Employee employee;
     private Resourcepool resourcepool;

    public BaseManagepool() {
    }
    
    public BaseManagepool(Integer idManagePool) {
    	this.idManagePool = idManagePool;
    }
   
    public Integer getIdManagePool() {
        return this.idManagePool;
    }
    
    public void setIdManagePool(Integer idManagePool) {
        this.idManagePool = idManagePool;
    }
    public Employee getEmployee() {
        return this.employee;
    }
    
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public Resourcepool getResourcepool() {
        return this.resourcepool;
    }
    
    public void setResourcepool(Resourcepool resourcepool) {
        this.resourcepool = resourcepool;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Managepool) )  { result = false; }
		 else if (other != null) {
		 	Managepool castOther = (Managepool) other;
			if (castOther.getIdManagePool().equals(this.getIdManagePool())) { result = true; }
         }
		 return result;
   }


}


