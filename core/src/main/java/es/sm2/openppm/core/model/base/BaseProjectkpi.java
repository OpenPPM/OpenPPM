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
 * File: BaseProjectkpi.java
 * Create User: javier.hernandez
 * Create Date: 15/03/2015 12:52:47
 */

package es.sm2.openppm.core.model.base;
import es.sm2.openppm.core.model.impl.Historickpi;
import es.sm2.openppm.core.model.impl.Metrickpi;
import es.sm2.openppm.core.model.impl.Project;
import es.sm2.openppm.core.model.impl.Projectkpi;
import es.sm2.openppm.core.model.impl.*;


import java.util.HashSet;
import java.util.Set;

 /**
 * Base Pojo object for domain model class Projectkpi
 * @see es.sm2.openppm.core.model.base.Projectkpi
 * @author Hibernate Generator by Javier Hernandez
 * For implement your own methods use class Projectkpi
 */
public class BaseProjectkpi  implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	
	public static final String ENTITY = "projectkpi";
	
	public static final String IDPROJECTKPI = "idProjectKpi";
	public static final String METRICKPI = "metrickpi";
	public static final String PROJECT = "project";
	public static final String UPPERTHRESHOLD = "upperThreshold";
	public static final String LOWERTHRESHOLD = "lowerThreshold";
	public static final String WEIGHT = "weight";
	public static final String VALUE = "value";
	public static final String AGGREGATEKPI = "aggregateKpi";
	public static final String SPECIFICKPI = "specificKpi";
	public static final String HISTORICKPIS = "historickpis";

     private Integer idProjectKpi;
     private Metrickpi metrickpi;
     private Project project;
     private Double upperThreshold;
     private Double lowerThreshold;
     private Double weight;
     private Double value;
     private Boolean aggregateKpi;
     private String specificKpi;
     private Set<Historickpi> historickpis = new HashSet<Historickpi>(0);

    public BaseProjectkpi() {
    }
    
    public BaseProjectkpi(Integer idProjectKpi) {
    	this.idProjectKpi = idProjectKpi;
    }
   
    public Integer getIdProjectKpi() {
        return this.idProjectKpi;
    }
    
    public void setIdProjectKpi(Integer idProjectKpi) {
        this.idProjectKpi = idProjectKpi;
    }
    public Metrickpi getMetrickpi() {
        return this.metrickpi;
    }
    
    public void setMetrickpi(Metrickpi metrickpi) {
        this.metrickpi = metrickpi;
    }
    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    public Double getUpperThreshold() {
        return this.upperThreshold;
    }
    
    public void setUpperThreshold(Double upperThreshold) {
        this.upperThreshold = upperThreshold;
    }
    public Double getLowerThreshold() {
        return this.lowerThreshold;
    }
    
    public void setLowerThreshold(Double lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
    }
    public Double getWeight() {
        return this.weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public Double getValue() {
        return this.value;
    }
    
    public void setValue(Double value) {
        this.value = value;
    }
    public Boolean getAggregateKpi() {
        return this.aggregateKpi;
    }
    
    public void setAggregateKpi(Boolean aggregateKpi) {
        this.aggregateKpi = aggregateKpi;
    }
    public String getSpecificKpi() {
        return this.specificKpi;
    }
    
    public void setSpecificKpi(String specificKpi) {
        this.specificKpi = specificKpi;
    }
    public Set<Historickpi> getHistorickpis() {
        return this.historickpis;
    }
    
    public void setHistorickpis(Set<Historickpi> historickpis) {
        this.historickpis = historickpis;
    }


	@Override
	public boolean equals(Object other) {
		boolean result = false;
         if (this == other) { result = true; }
		 else if (other == null) { result = false; }
		 else if (!(other instanceof Projectkpi) )  { result = false; }
		 else if (other != null) {
		 	Projectkpi castOther = (Projectkpi) other;
			if (castOther.getIdProjectKpi().equals(this.getIdProjectKpi())) { result = true; }
         }
		 return result;
   }


}


