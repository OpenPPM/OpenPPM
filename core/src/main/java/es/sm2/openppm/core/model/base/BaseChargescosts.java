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
 * File: BaseChargescosts.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:54
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Chargescosts;
import es.sm2.openppm.core.model.impl.Currency;
import es.sm2.openppm.core.model.impl.Project;


/**
 * Base Pojo object for domain model class Chargescosts
 * @see es.sm2.openppm.core.model.base.Chargescosts
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Chargescosts
 */
public class BaseChargescosts  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "chargescosts";
	
	public static final String IDCHARGESCOSTS = "idChargesCosts";
	public static final String CURRENCY = "currency";
	public static final String PROJECT = "project";
	public static final String NAME = "name";
	public static final String COST = "cost";
	public static final String IDCHARGETYPE = "idChargeType";

     private Integer idChargesCosts;
     private Currency currency;
     private Project project;
     private String name;
     private Double cost;
     private int idChargeType;

    public BaseChargescosts() {
    }
    
    public BaseChargescosts(Integer idChargesCosts) {
    	this.idChargesCosts = idChargesCosts;
    }
   
    public Integer getIdChargesCosts() {
        return this.idChargesCosts;
    }
    
    public void setIdChargesCosts(Integer idChargesCosts) {
        this.idChargesCosts = idChargesCosts;
    }
    public Currency getCurrency() {
        return this.currency;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public Double getCost() {
        return this.cost;
    }
    
    public void setCost(Double cost) {
        this.cost = cost;
    }
    public int getIdChargeType() {
        return this.idChargeType;
    }
    
    public void setIdChargeType(int idChargeType) {
        this.idChargeType = idChargeType;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Chargescosts) )  { result = false; }
		 else if (other != null) {
		 	Chargescosts castOther = (Chargescosts) other;
			if (castOther.getIdChargesCosts().equals(this.getIdChargesCosts())) { result = true; }
         }
		 return result;
   }


}


