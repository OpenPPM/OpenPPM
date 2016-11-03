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
 * File: BaseMetrickpi.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Bscdimension;
import es.sm2.openppm.core.model.impl.Company;
import es.sm2.openppm.core.model.impl.Metrickpi;
import es.sm2.openppm.core.model.impl.Projectkpi;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Metrickpi
 * @see es.sm2.openppm.core.model.base.BaseMetrickpi
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Metrickpi
 */
public class BaseMetrickpi  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "metrickpi";
	
	public static final String IDMETRICKPI = "idMetricKpi";
	public static final String BSCDIMENSION = "bscdimension";
	public static final String COMPANY = "company";
	public static final String NAME = "name";
	public static final String DEFINITION = "definition";
     public static final String TYPE = "type";
	public static final String PROJECTKPIS = "projectkpis";

     private Integer idMetricKpi;
     private Bscdimension bscdimension;
     private Company company;
     private String name;
     private String definition;
     private String type;
     private Set<Projectkpi> projectkpis = new HashSet<Projectkpi>(0);

    public BaseMetrickpi() {
    }
    
    public BaseMetrickpi(Integer idMetricKpi) {
    	this.idMetricKpi = idMetricKpi;
    }
   
    public Integer getIdMetricKpi() {
        return this.idMetricKpi;
    }
    
    public void setIdMetricKpi(Integer idMetricKpi) {
        this.idMetricKpi = idMetricKpi;
    }
    public Bscdimension getBscdimension() {
        return this.bscdimension;
    }
    
    public void setBscdimension(Bscdimension bscdimension) {
        this.bscdimension = bscdimension;
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
    public String getDefinition() {
        return this.definition;
    }
    
    public void setDefinition(String definition) {
        this.definition = definition;
    }
    public Set<Projectkpi> getProjectkpis() {
        return this.projectkpis;
    }
    
    public void setProjectkpis(Set<Projectkpi> projectkpis) {
        this.projectkpis = projectkpis;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Metrickpi) )  { result = false; }
		 else if (other != null) {
		 	Metrickpi castOther = (Metrickpi) other;
			if (castOther.getIdMetricKpi().equals(this.getIdMetricKpi())) { result = true; }
         }
		 return result;
   }


     public String getType() {
         return type;
     }

     public void setType(String type) {
         this.type = type;
     }
 }


