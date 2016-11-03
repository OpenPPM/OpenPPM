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
 * File: BaseExpenseaccounts.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Expenseaccounts;
import es.sm2.openppm.core.model.impl.Expensesheet;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Expenseaccounts
 * @see es.sm2.openppm.core.model.base.Expenseaccounts
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Expenseaccounts
 */
public class BaseExpenseaccounts  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "expenseaccounts";
	
	public static final String IDEXPENSEACCOUNT = "idExpenseAccount";
	public static final String COMPANY = "company";
	public static final String DESCRIPTION = "description";
	public static final String EXPENSESHEETS = "expensesheets";

     private Integer idExpenseAccount;
     private Company company;
     private String description;
     private Set<Expensesheet> expensesheets = new HashSet<Expensesheet>(0);

    public BaseExpenseaccounts() {
    }
    
    public BaseExpenseaccounts(Integer idExpenseAccount) {
    	this.idExpenseAccount = idExpenseAccount;
    }
   
    public Integer getIdExpenseAccount() {
        return this.idExpenseAccount;
    }
    
    public void setIdExpenseAccount(Integer idExpenseAccount) {
        this.idExpenseAccount = idExpenseAccount;
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
    public Set<Expensesheet> getExpensesheets() {
        return this.expensesheets;
    }
    
    public void setExpensesheets(Set<Expensesheet> expensesheets) {
        this.expensesheets = expensesheets;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Expenseaccounts) )  { result = false; }
		 else if (other != null) {
		 	Expenseaccounts castOther = (Expenseaccounts) other;
			if (castOther.getIdExpenseAccount().equals(this.getIdExpenseAccount())) { result = true; }
         }
		 return result;
   }


}


