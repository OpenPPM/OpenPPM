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
 * File: BaseOperationaccount.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Operation;
import es.sm2.openppm.core.model.impl.Operationaccount;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Operationaccount
 * @see es.sm2.openppm.core.model.base.Operationaccount
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Operationaccount
 */
public class BaseOperationaccount  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "operationaccount";
	
	public static final String IDOPACCOUNT = "idOpAccount";
	public static final String COMPANY = "company";
	public static final String DESCRIPTION = "description";
	public static final String OPERATIONS = "operations";

     private Integer idOpAccount;
     private Company company;
     private String description;
     private Set<Operation> operations = new HashSet<Operation>(0);

    public BaseOperationaccount() {
    }
    
    public BaseOperationaccount(Integer idOpAccount) {
    	this.idOpAccount = idOpAccount;
    }
   
    public Integer getIdOpAccount() {
        return this.idOpAccount;
    }
    
    public void setIdOpAccount(Integer idOpAccount) {
        this.idOpAccount = idOpAccount;
    }
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<Operation> getOperations() {
        return this.operations;
    }
    
    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Operationaccount) )  { result = false; }
		 else if (other != null) {
		 	Operationaccount castOther = (Operationaccount) other;
			if (castOther.getIdOpAccount().equals(this.getIdOpAccount())) { result = true; }
         }
		 return result;
   }


}


