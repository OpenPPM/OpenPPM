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
 * File: BaseBudgetaccounts.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:55
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Budgetaccounts;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Directcosts;
import es.sm2.openppm.core.model.impl.Expenses;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Budgetaccounts
 * @see es.sm2.openppm.core.model.base.Budgetaccounts
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Budgetaccounts
 */
public class BaseBudgetaccounts  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "budgetaccounts";
	
	public static final String IDBUDGETACCOUNT = "idBudgetAccount";
	public static final String COMPANY = "company";
	public static final String DESCRIPTION = "description";
	public static final String TYPECOST = "typeCost";
	public static final String EXPENSESES = "expenseses";
	public static final String DIRECTCOSTSES = "directcostses";

     private Integer idBudgetAccount;
     private Company company;
     private String description;
     private Integer typeCost;
     private Set<Expenses> expenseses = new HashSet<Expenses>(0);
     private Set<Directcosts> directcostses = new HashSet<Directcosts>(0);

    public BaseBudgetaccounts() {
    }
    
    public BaseBudgetaccounts(Integer idBudgetAccount) {
    	this.idBudgetAccount = idBudgetAccount;
    }
   
    public Integer getIdBudgetAccount() {
        return this.idBudgetAccount;
    }
    
    public void setIdBudgetAccount(Integer idBudgetAccount) {
        this.idBudgetAccount = idBudgetAccount;
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
    public Integer getTypeCost() {
        return this.typeCost;
    }
    
    public void setTypeCost(Integer typeCost) {
        this.typeCost = typeCost;
    }
    public Set<Expenses> getExpenseses() {
        return this.expenseses;
    }
    
    public void setExpenseses(Set<Expenses> expenseses) {
        this.expenseses = expenseses;
    }
    public Set<Directcosts> getDirectcostses() {
        return this.directcostses;
    }
    
    public void setDirectcostses(Set<Directcosts> directcostses) {
        this.directcostses = directcostses;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Budgetaccounts) )  { result = false; }
		 else if (other != null) {
		 	Budgetaccounts castOther = (Budgetaccounts) other;
			if (castOther.getIdBudgetAccount().equals(this.getIdBudgetAccount())) { result = true; }
         }
		 return result;
   }


}


