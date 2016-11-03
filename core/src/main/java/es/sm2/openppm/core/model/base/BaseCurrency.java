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
 * File: BaseCurrency.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Currency;
import es.sm2.openppm.core.model.impl.Workingcosts;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Currency
 * @see es.sm2.openppm.core.model.base.Currency
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Currency
 */
public class BaseCurrency  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "currency";
	
	public static final String IDCURRENCY = "idCurrency";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String CURRENCY = "currency";
	public static final String WORKINGCOSTSES = "workingcostses";
	public static final String CHARGESCOSTSES = "chargescostses";

     private Integer idCurrency;
     private Company company;
     private String name;
     private String currency;
     private Set<Workingcosts> workingcostses = new HashSet<Workingcosts>(0);
     private Set<Chargescosts> chargescostses = new HashSet<Chargescosts>(0);

    public BaseCurrency() {
    }
    
    public BaseCurrency(Integer idCurrency) {
    	this.idCurrency = idCurrency;
    }
   
    public Integer getIdCurrency() {
        return this.idCurrency;
    }
    
    public void setIdCurrency(Integer idCurrency) {
        this.idCurrency = idCurrency;
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
    public String getCurrency() {
        return this.currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public Set<Workingcosts> getWorkingcostses() {
        return this.workingcostses;
    }
    
    public void setWorkingcostses(Set<Workingcosts> workingcostses) {
        this.workingcostses = workingcostses;
    }
    public Set<Chargescosts> getChargescostses() {
        return this.chargescostses;
    }
    
    public void setChargescostses(Set<Chargescosts> chargescostses) {
        this.chargescostses = chargescostses;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Currency) )  { result = false; }
		 else if (other != null) {
		 	Currency castOther = (Currency) other;
			if (castOther.getIdCurrency().equals(this.getIdCurrency())) { result = true; }
         }
		 return result;
   }


}


